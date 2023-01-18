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
package fr.ensma.lias.mariusql.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import fr.ensma.lias.mariusql.bulkload.MariusQLBulkload;
import fr.ensma.lias.mariusql.cache.data.DataCache;
import fr.ensma.lias.mariusql.cache.querycache.QueryCache;
import fr.ensma.lias.mariusql.cfg.MariusQLConfig;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.core.model.MGenericClass;
import fr.ensma.lias.mariusql.driver.MariusQLDDLDriver;
import fr.ensma.lias.mariusql.driver.MariusQLDMLDriver;
import fr.ensma.lias.mariusql.driver.MariusQLDQLDriver;
import fr.ensma.lias.mariusql.driver.MariusQLDriverAPI;
import fr.ensma.lias.mariusql.driver.MariusQLFeaturesDriver;
import fr.ensma.lias.mariusql.driver.MariusQLTypesDriver;
import fr.ensma.lias.mariusql.engine.MariusQLGenerator;
import fr.ensma.lias.mariusql.metric.MariusQLMetric;
import fr.ensma.lias.mariusql.util.LanguageParametersListener;

/**
 * Interface for a MariusQL session.
 * 
 * @author Mickael BARON
 */
public interface MariusQLSession {

	/**
	 * Metamodel level cache.
	 * 
	 * @return
	 */
	DataCache<MMEntity> getMetaModelCache();

	/**
	 * Model level cache.
	 * 
	 * @return
	 */
	DataCache<MGenericClass> getModelCache();

	/**
	 * Create a statement for executing DDL or DML operations.
	 * 
	 * @return a statement for executing DDL or DML operations.
	 */
	MariusQLStatement createMariusQLStatement();

	/**
	 * @return
	 */
	MariusQLDDLDriver getMariusQLDDL();

	/**
	 * @return
	 */
	MariusQLTypesDriver getMariusQLTypes();

	/**
	 * @return
	 */
	MariusQLDQLDriver getMariusQLDQL();

	/**
	 * @return
	 */
	MariusQLDMLDriver getMariusQLDML();

	/**
	 * @return
	 */
	MariusQLFeaturesDriver getMariusQLFeatures();

	/**
	 * @return
	 */
	MariusQLConfig getMariusQLConfig();

	/**
	 * @return
	 */
	MariusQLDriverAPI getMariusQLDriverAPI();

	/**
	 * @return
	 */
	MariusQLMetric getMariusQLMetric();

	/**
	 * @return
	 */
	MariusQLBulkload getMariusQLBulkload();

	/**
	 * Define an alias for a name space.
	 * 
	 * @param nameSpace the given name space
	 * @param alias     alias for the namespace
	 */
	void setNameSpaceAlias(String nameSpace, String alias);

	/**
	 * Get the default namespace used by this session
	 * 
	 * @return the default namespace used by this session
	 */
	String getDefaultNameSpace();

	/**
	 * Retrieve all namespaces according to the existing classes.
	 * 
	 * @return
	 */
	List<String> getAllNameSpaces();

	/**
	 * @param defaultNameSpace The defaultNameSpace to set.
	 */
	void setDefaultNameSpace(String defaultNameSpace);

	/**
	 * @param referenceLanguage The referenceLanguage to set.
	 */
	void setReferenceLanguage(String referenceLanguage);

	/**
	 * Check if there exists no reference language.
	 * 
	 * @return true no defined reference language, false reference language defined.
	 */
	boolean isNoReferenceLanguage();

	/**
	 * @return Returns the referenceLanguage.
	 */
	String getReferenceLanguage();

	/**
	 * Commit a current transaction.
	 */
	void commit();

	/**
	 * Rollback a current transaction.
	 */
	void rollback();

	/**
	 * Adds an <code>LanguageParametersListener</code> to the
	 * <code>MariusSession</code>.
	 * 
	 * @param p the <code>LanguageParametersListener</code> to be added
	 */
	void addLanguageParametersListener(LanguageParametersListener p);

	/**
	 * Removes an <code>LanguageParametersListener</code> from the
	 * <code>MariusSession</code>.
	 *
	 * @param p the listener to be removed
	 */
	void removeLanguageParametersListener(LanguageParametersListener p);

	/**
	 * Retrieve the default implementation of an operation.
	 * 
	 * @param operationName
	 * @return
	 */
	String getDefaultImplementation(String operationName);

	/**
	 * Set the default implementation of an operation.
	 * 
	 * @param operationName
	 * @param implementationName
	 */
	void setDefaultImplementation(String operationName, String implementationName);

	/**
	 * According to the session, get a generator.
	 * 
	 * @return
	 */
	MariusQLGenerator getGenerator();

	/**
	 * @return
	 */
	Connection getConnection();

	/**
	 * @return
	 */
	boolean getAutoCommit();

	/**
	 * @param autoCommit
	 */
	void setAutoCommit(boolean autoCommit);

	/**
	 * @return
	 */
	Statement createSQLStatement() throws SQLException;

	/**
	 * 
	 */
	void addLanguage(String name, String code, String comment);

	/**
	 * 
	 */
	void close();

	/**
	 * @return
	 */
	QueryCache getQueryCache();
}
