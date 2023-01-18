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
package fr.ensma.lias.mariusql.driver;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * @author Ghada TRIKI
 */
public abstract class AbstractMariusQLDDLDriver extends AbstractMariusQLDriver implements MariusQLDDLDriver {

	public AbstractMariusQLDDLDriver(MariusQLSession pSession) {
		super(pSession);
	}

	public MariusQLSession getSession() {
		return this.refSession;
	}

	@Override
	public int addTableColumns(String name, List<String> newColumns) {
		final String startCommand = "ALTER TABLE " + name + " ADD COLUMN ";

		try {
			Statement statement = this.getSession().createSQLStatement();
			int res = 0;

			for (String current : newColumns) {
				String cmdDDL = startCommand + current;
				res += statement.executeUpdate(cmdDDL);
			}

			statement.close();

			return res;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public int removeTableColumns(String name, List<String> columns) {
		final String startCommand = "ALTER TABLE " + name + " DROP COLUMN ";

		try {
			Statement statement = this.getSession().createSQLStatement();
			int res = 0;

			for (String current : columns) {
				String cmdDDL = startCommand + current;
				res += statement.executeUpdate(cmdDDL);
			}

			statement.close();

			return res;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public void dropTable(String name) {
		final String cmdDDL = "DROP TABLE " + name;

		try {
			Statement statement = this.getSession().createSQLStatement();

			statement.executeUpdate(cmdDDL);
			statement.close();
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}
}
