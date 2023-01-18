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
package fr.ensma.lias.mariusql.driver.postgresql;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import fr.ensma.lias.mariusql.driver.AbstractMariusQLDDLDriver;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * @author Mickael BARON
 * @author Ghada TRIKI
 */
public class MariusQLDDLDriverPostgreSQLImpl extends AbstractMariusQLDDLDriver {

	public MariusQLDDLDriverPostgreSQLImpl(MariusQLSession pSession) {
		super(pSession);
	}

	@Override
	public void createTable(String name, String primaryKeyName, String primaryKeyType, String sequenceName,
			List<String> attributes) {

		StringBuilder cmdDDL = new StringBuilder("CREATE TABLE " + name + "(" + primaryKeyName + " " + primaryKeyType
				+ " NOT NULL DEFAULT nextval('" + sequenceName + "'::regclass)");

		for (String currentAttribute : attributes) {
			cmdDDL.append(", " + currentAttribute);
		}

		cmdDDL.append(", CONSTRAINT " + name + "_pkey PRIMARY KEY (" + primaryKeyName + "))");
		try {
			Statement statement = this.getSession().createSQLStatement();
			statement.executeUpdate(cmdDDL.toString());
			statement.close();
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public void createSequence(String name) {
		String cmdDDL = "CREATE SEQUENCE " + name + " START 1";

		try {
			Statement statement = this.getSession().createSQLStatement();
			statement.executeUpdate(cmdDDL);

			statement.close();
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public void dropSequence(String name) {
		String cmdDDL = "DROP SEQUENCE IF EXISTS " + name + " CASCADE";

		try {
			Statement statement = this.getSession().createSQLStatement();
			statement.executeUpdate(cmdDDL);

			statement.close();
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}
}
