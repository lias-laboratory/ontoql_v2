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
package fr.ensma.lias.mariusql.driver.hsqldb;

import fr.ensma.lias.mariusql.driver.AbstractMariusQLDriverAPI;
import fr.ensma.lias.mariusql.driver.MariusQLDDLDriver;
import fr.ensma.lias.mariusql.driver.MariusQLDMLDriver;
import fr.ensma.lias.mariusql.driver.MariusQLDQLDriver;
import fr.ensma.lias.mariusql.driver.MariusQLDriverAPI;
import fr.ensma.lias.mariusql.driver.MariusQLFeaturesDriver;
import fr.ensma.lias.mariusql.driver.MariusQLTypesDriver;
import fr.ensma.lias.mariusql.engine.MariusQLGenerator;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * @author Mickael BARON
 * @author Larbi LATRECHE
 */
public class MariusQLDriverImpl extends AbstractMariusQLDriverAPI implements MariusQLDriverAPI {

	protected MariusQLDDLDriver ddlDriver;

	protected MariusQLDQLDriver dqlDriver;

	protected MariusQLDMLDriver dmlDriver;

	protected MariusQLTypesDriver typeDriver;

	protected MariusQLFeaturesDriver featuresDriver;

	protected MariusQLGenerator currentGenerator;

	private static final String HSQLDB_JDBC_DRIVER = "org.hsqldb.jdbc.JDBCDriver";

	public MariusQLDriverImpl() {
		currentGenerator = new HsqldbSQLGenerator();
	}

	@Override
	public MariusQLDDLDriver getDDLDriver() {
		return ddlDriver;
	}

	@Override
	public MariusQLDQLDriver getDQLDriver() {
		return dqlDriver;
	}

	@Override
	public MariusQLDMLDriver getDMLDriver() {
		return dmlDriver;
	}

	@Override
	public MariusQLTypesDriver getTypesDriver() {
		return typeDriver;
	}

	@Override
	public String getURL() {
		return "jdbc:hsqldb:" + this.getSession().getMariusQLConfig().host() + ":"
				+ this.getSession().getMariusQLConfig().sid();
	}

	@Override
	public String getJDBCClass() {
		return HSQLDB_JDBC_DRIVER;
	}

	@Override
	public void initialize(MariusQLSession pSession) {
		this.setSession(pSession);

		this.ddlDriver = new MariusQLDDLDriverHsqlDBImpl(this.getSession());
		this.dqlDriver = new MariusQLDQLDriverHsqlDBImpl(this.getSession());
		this.dmlDriver = new MariusQLDMLDriverHsqlDBImpl(this.getSession());
		this.typeDriver = new MariusQLTypesDriverHsqlDBImpl(this.getSession());
		this.featuresDriver = new MariusQLFeaturesDriverHsqlDBImpl(this.getSession());
	}

	@Override
	public MariusQLGenerator getGenerator() {
		return this.currentGenerator;
	}

	@Override
	public MariusQLFeaturesDriver getFeaturesDriver() {
		return this.featuresDriver;
	}
}
