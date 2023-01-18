/*********************************************************************************
 * This file is part of MariusQL Project.
 * Copyright (C) 2014  LIAS - ENSMA
 *   Teleport 2 - 1 avenue Clement Ader
 *   BP 40109 - 86961 Futuroscope Chasseneuil Cedex - FRANCE
 * 
 * MariusQL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MariusQL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with MariusQL.  If not, see <http://www.gnu.org/licenses/>.
 **********************************************************************************/
package fr.ensma.lias.mariusql.jdbc.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.aeonbits.owner.ConfigFactory;

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.bulkload.MariusQLBulkload;
import fr.ensma.lias.mariusql.bulkload.impl.MariusQLBulkloadImpl;
import fr.ensma.lias.mariusql.cache.FactoryCache;
import fr.ensma.lias.mariusql.cache.data.DataCache;
import fr.ensma.lias.mariusql.cache.data.LanguageParametersCacheAdapter;
import fr.ensma.lias.mariusql.cache.querycache.QueryCache;
import fr.ensma.lias.mariusql.cfg.MariusQLConfig;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.metamodel.Language;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.core.model.MGenericClass;
import fr.ensma.lias.mariusql.driver.FactoryDriver;
import fr.ensma.lias.mariusql.driver.MariusQLDDLDriver;
import fr.ensma.lias.mariusql.driver.MariusQLDMLDriver;
import fr.ensma.lias.mariusql.driver.MariusQLDQLDriver;
import fr.ensma.lias.mariusql.driver.MariusQLDriverAPI;
import fr.ensma.lias.mariusql.driver.MariusQLFeaturesDriver;
import fr.ensma.lias.mariusql.driver.MariusQLTypesDriver;
import fr.ensma.lias.mariusql.engine.MariusQLGenerator;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotYetImplementedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;
import fr.ensma.lias.mariusql.metric.MariusQLMetric;
import fr.ensma.lias.mariusql.metric.impl.MariusQLMetricImpl;
import fr.ensma.lias.mariusql.util.DefaultNameSpaceEvent;
import fr.ensma.lias.mariusql.util.LanguageParametersListener;
import fr.ensma.lias.mariusql.util.ReferenceLanguageEvent;

/**
 * @author Mickael BARON
 * @author Valentin CASSAIR
 */
public class MariusQLSessionImpl implements MariusQLSession {

	/**
	 * Give the current reference language.
	 */
	protected String referenceLanguage = MariusQLConstants.NO_LANGUAGE;

	/**
	 * Map on namespace and alias.
	 */
	protected Map<String, String> nameSpaceAlias;

	/**
	 * Give the default namespace.
	 */
	protected String defaultNameSpace;

	/**
	 * Specify the default implementations for static functions.
	 */
	private Map<String, String> defaultImplementations;

	/**
	 * Native JDBC connection.
	 */
	protected Connection connection;

	/**
	 * LanguageParametersListener list.
	 */
	protected List<LanguageParametersListener> refLanguageParametersListener;

	/**
	 * Cache dedicated for meta-model part.
	 */
	protected DataCache<MMEntity> currentMMCache;

	/**
	 * Cache dedicated for model part.
	 */
	protected DataCache<MGenericClass> currentMCache;

	/**
	 * Cache for queries.
	 */
	protected QueryCache currentQueryCache;

	/**
	 * 
	 */
	protected MariusQLDriverAPI currentDriverAPI;

	/**
	 * 
	 */
	protected MariusQLConfig cfg;

	/**
	 * 
	 */
	protected MariusQLMetric currentMetric;

	/**
	 * 
	 */
	protected MariusQLBulkload currentBulkload;

	public MariusQLSessionImpl(Properties props) {
		if (props == null) {
			cfg = ConfigFactory.create(MariusQLConfig.class);
		} else {
			cfg = ConfigFactory.create(MariusQLConfig.class, props);
		}

		// Initialize attributes.
		this.refLanguageParametersListener = new ArrayList<LanguageParametersListener>();
		this.nameSpaceAlias = new HashMap<String, String>();
		this.defaultImplementations = new HashMap<String, String>();

		// Initialize caches
		this.currentMMCache = FactoryCache.createMetaModelCache(this);
		this.currentMCache = FactoryCache.createModelCache(this);
		this.currentQueryCache = FactoryCache.createQueryCache(this);

		// Register cache listeners
		this.addLanguageParametersListener(new LanguageParametersCacheAdapter(this.currentMCache));

		// Initialize driver API
		this.currentDriverAPI = FactoryDriver.create(this, cfg.driverClass());
		this.initializeConnection();

		// Initialize language and namespace.
		this.setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);
		this.setReferenceLanguage(MariusQLConstants.ENGLISH);

		// Check the current MariusQL version
		String currentVersion = this.getMariusQLDQL().getMariusQLVersion();
		if (!MariusQLConstants.MARIUSQL_VERSION.equalsIgnoreCase(currentVersion)) {
			throw new MariusQLException("The current schema version (" + currentVersion
					+ ") is not compatible with this version of MariusQL (" + MariusQLConstants.MARIUSQL_VERSION
					+ ").");
		}

		this.currentMetric = new MariusQLMetricImpl(this);
		this.currentBulkload = new MariusQLBulkloadImpl(this);
	}

	public MariusQLSessionImpl() {
		this(null);
	}

	private void initializeConnection() {
		try {
			Class.forName(this.getMariusQLDriverAPI().getJDBCClass());
			this.connection = DriverManager.getConnection(this.getMariusQLDriverAPI().getURL(), cfg.user(),
					cfg.password());
			this.connection.setAutoCommit(false);
		} catch (ClassNotFoundException e) {
			throw new NotYetImplementedException();
		} catch (SQLException e) {
			throw new MariusQLException("Problem with driver connection " + e.getMessage());
		}
	}

	@Override
	public void setNameSpaceAlias(String nameSpace, String alias) {
		nameSpaceAlias.put(nameSpace, alias);
	}

	@Override
	public String getDefaultNameSpace() {
		return defaultNameSpace;
	}

	@Override
	public void setDefaultNameSpace(String defaultNameSpace) {
		DefaultNameSpaceEvent currentDefaultNameSpaceEvent = new DefaultNameSpaceEvent(this.defaultNameSpace,
				defaultNameSpace);

		this.defaultNameSpace = defaultNameSpace;

		for (LanguageParametersListener current : refLanguageParametersListener) {
			current.defaultNameSpacePerformed(currentDefaultNameSpaceEvent);
		}
	}

	@Override
	public MariusQLFeaturesDriver getMariusQLFeatures() {
		return this.currentDriverAPI.getFeaturesDriver();
	}

	@Override
	public void setReferenceLanguage(String referenceLanguage) {
		if (!referenceLanguage.equalsIgnoreCase(MariusQLConstants.FRENCH)
				&& !referenceLanguage.equalsIgnoreCase(MariusQLConstants.ENGLISH)
				&& !referenceLanguage.equalsIgnoreCase(MariusQLConstants.NO_LANGUAGE)) {
			boolean languageExist = false;
			List<Language> listLanguage = this.getMariusQLDQL().getLanguages();
			for (Language l : listLanguage) {
				if (l.getName().equalsIgnoreCase(referenceLanguage)) {
					languageExist = true;
					referenceLanguage = l.getCode();
					break;
				}
			}
			if (!languageExist) {
				throw new MariusQLException("The language does not exist: " + referenceLanguage);
			}

		}

		ReferenceLanguageEvent currentReferenceLanguageEvent = new ReferenceLanguageEvent(this.referenceLanguage,
				referenceLanguage);
		this.referenceLanguage = referenceLanguage;

		for (LanguageParametersListener current : refLanguageParametersListener) {
			current.referenceLanguagePerformed(currentReferenceLanguageEvent);
		}
	}

	@Override
	public boolean isNoReferenceLanguage() {
		return (this.getReferenceLanguage().equals(MariusQLConstants.NO_LANGUAGE));
	}

	@Override
	public String getReferenceLanguage() {
		return referenceLanguage;
	}

	@Override
	public void commit() {
		try {
			this.connection.commit();
		} catch (SQLException e) {
			throw new MariusQLException("Can not commit transaction.");
		}
	}

	@Override
	public void rollback() {
		try {
			this.connection.rollback();
		} catch (SQLException e) {
			throw new MariusQLException("Can not rollback transaction.");
		}
	}

	@Override
	public void addLanguageParametersListener(LanguageParametersListener p) {
		if (p != null) {
			refLanguageParametersListener.add(p);
		}
	}

	@Override
	public void removeLanguageParametersListener(LanguageParametersListener p) {
		if (p != null) {
			refLanguageParametersListener.remove(p);
		}
	}

	@Override
	public String getDefaultImplementation(String operationName) {
		return (this.defaultImplementations == null || this.defaultImplementations.isEmpty()) ? null
				: defaultImplementations.get(operationName);
	}

	@Override
	public void setDefaultImplementation(String operationName, String implementationName) {
		if (this.defaultImplementations != null) {
			this.defaultImplementations.put(operationName, implementationName);
		}
	}

	@Override
	public MariusQLStatement createMariusQLStatement() {
		return new MariusQLStatementImpl(this);
	}

	@Override
	public DataCache<MMEntity> getMetaModelCache() {
		return this.currentMMCache;
	}

	@Override
	public MariusQLDDLDriver getMariusQLDDL() {
		return this.currentDriverAPI.getDDLDriver();
	}

	@Override
	public MariusQLTypesDriver getMariusQLTypes() {
		return this.currentDriverAPI.getTypesDriver();
	}

	@Override
	public MariusQLDQLDriver getMariusQLDQL() {
		return this.currentDriverAPI.getDQLDriver();
	}

	@Override
	public MariusQLDMLDriver getMariusQLDML() {
		return this.currentDriverAPI.getDMLDriver();
	}

	@Override
	public MariusQLDriverAPI getMariusQLDriverAPI() {
		return this.currentDriverAPI;
	}

	@Override
	public MariusQLConfig getMariusQLConfig() {
		return this.cfg;
	}

	@Override
	public DataCache<MGenericClass> getModelCache() {
		return currentMCache;
	}

	@Override
	public MariusQLGenerator getGenerator() {
		return this.currentDriverAPI.getGenerator();
	}

	@Override
	public boolean getAutoCommit() {
		try {
			return this.connection.getAutoCommit();
		} catch (SQLException e) {
			throw new MariusQLException("Problem with JDBC connection.");
		}
	}

	@Override
	public void setAutoCommit(boolean autoCommit) {
		try {
			this.connection.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			throw new MariusQLException("Problem with JDBC connection.");
		}
	}

	@Override
	public Statement createSQLStatement() throws SQLException {
		return connection.createStatement();
	}

	@Override
	public void close() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public Connection getConnection() {
		return this.connection;
	}

	@Override
	public void addLanguage(String name, String code, String description) {
		FactoryCore.createNewLanguage(this, name, code, description);
	}

	@Override
	public MariusQLMetric getMariusQLMetric() {
		return this.currentMetric;
	}

	@Override
	public MariusQLBulkload getMariusQLBulkload() {
		return this.currentBulkload;
	}

	@Override
	public List<String> getAllNameSpaces() {
		return this.getMariusQLDQL().getAllNameSpaces();
	}

	@Override
	public QueryCache getQueryCache() {
		return this.currentQueryCache;
	}
}
