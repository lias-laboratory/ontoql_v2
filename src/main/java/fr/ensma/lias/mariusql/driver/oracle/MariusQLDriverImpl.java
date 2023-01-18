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
package fr.ensma.lias.mariusql.driver.oracle;

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

	protected MariusQLGenerator currentGenerator;

	protected MariusQLFeaturesDriver featuresDriver;

	private static final String ORACLE_JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";

	public MariusQLDriverImpl() {
		currentGenerator = new OracleSQLGenerator();
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
		return "jdbc:oracle:thin:@" + this.getSession().getMariusQLConfig().host() + ":"
				+ this.getSession().getMariusQLConfig().port() + ":" + this.getSession().getMariusQLConfig().sid();
	}

	@Override
	public String getJDBCClass() {
		return ORACLE_JDBC_DRIVER;
	}

	@Override
	public void initialize(MariusQLSession pSession) {
		this.setSession(pSession);

		this.ddlDriver = new MariusQLDDLDriverOracleImpl(this.getSession());
		this.dqlDriver = new MariusQLDQLDriverOracleImpl(this.getSession());
		this.dmlDriver = new MariusQLDMLDriverOracleImpl(this.getSession());
		this.typeDriver = new MariusQLTypesDriverOracleImpl(this.getSession());
		this.featuresDriver = new MariusQLFeaturesDriverOracleImpl(getSession());
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
