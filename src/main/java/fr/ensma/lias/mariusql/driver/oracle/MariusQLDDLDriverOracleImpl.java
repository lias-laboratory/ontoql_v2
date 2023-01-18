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

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import fr.ensma.lias.mariusql.driver.AbstractMariusQLDDLDriver;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotYetImplementedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * @author Mickael BARON
 * @author Ghada TRIKI
 * @author Larbi LATRECHE
 */
public class MariusQLDDLDriverOracleImpl extends AbstractMariusQLDDLDriver {

	public MariusQLDDLDriverOracleImpl(MariusQLSession pSession) {
		super(pSession);
	}

	@Override
	public void createTable(String name, String primaryKeyName, String primaryKeyType, String sequenceName,
			List<String> attributes) {

		StringBuilder cmdDDL = new StringBuilder("CREATE TABLE " + name + "(" + primaryKeyName + " " + primaryKeyType
				+ " DEFAULT " + sequenceName + ".NEXTVAL");

		for (String currentAttribute : attributes) {
			cmdDDL.append(", " + currentAttribute);
		}

		cmdDDL.append(", CONSTRAINT " + name + "_pkey PRIMARY KEY (" + primaryKeyName + "))");

		try {
			Statement statement = this.getSession().createSQLStatement();
			statement.executeUpdate(cmdDDL.toString());
			this.getSession().rollback();
			statement.close();
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public int addTableColumns(String name, List<String> newColumns) {
		final String startCommand = "ALTER TABLE " + name + " ADD ( ";

		try {
			Statement statement = this.getSession().createSQLStatement();
			int res = 0;

			for (String current : newColumns) {

				String cmdDDL = startCommand + current + " )";
				res += statement.executeUpdate(cmdDDL);
			}
			this.getSession().rollback();
			statement.close();

			return res;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public int removeTableColumns(String name, List<String> columns) {
		final String startCommand = "ALTER TABLE " + name + " DROP (";

		try {
			Statement statement = this.getSession().createSQLStatement();
			int res = 0;

			for (String current : columns) {
				String cmdDDL = startCommand + current + " )";
				res += statement.executeUpdate(cmdDDL);
			}
			this.getSession().rollback();
			statement.close();

			return res;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public void createSequence(String name) {
		throw new NotYetImplementedException();
	}

	@Override
	public void dropSequence(String name) {
		throw new NotYetImplementedException();
	}
}
