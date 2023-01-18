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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * @author Ghada TRIKI
 */
public abstract class AbstractMariusQLDMLDriver extends AbstractMariusQLDriver implements MariusQLDMLDriver {

	public AbstractMariusQLDMLDriver(MariusQLSession pSession) {
		super(pSession);
	}

	@Override
	public int insertRow(String name, List<String> columns, List<String> values) {
		if (columns == null || values == null || columns.size() != values.size()) {
			throw new MariusQLException("parameters error");
		}

		StringBuilder columnsResult = new StringBuilder();
		for (String currentColumn : columns) {
			columnsResult.append(currentColumn + ",");
		}

		StringBuilder valuesResult = new StringBuilder();
		for (String currentValue : values) {
			valuesResult.append(currentValue + ",");
		}

		if (!(columnsResult.length() == 0)) {
			columnsResult = columnsResult.delete(columnsResult.length() - 1, columnsResult.length());
		}

		if (!(valuesResult.length() == 0)) {
			valuesResult = valuesResult.delete(valuesResult.length() - 1, valuesResult.length());
		}

		String cmdDML = "insert into " + name + " (" + columnsResult + ") values (" + valuesResult + ")";

		try {
			Statement statement = this.getSession().createSQLStatement();
			int res = statement.executeUpdate(cmdDML);

			statement.close();

			return res;
		} catch (SQLException e) {
			throw new MariusQLException(e + cmdDML);
		}
	}

	@Override
	public int updateRow(String name, List<String> columns, List<String> values, List<String> constraintNames,
			List<String> constraintValues) {
		if (columns == null || values == null || columns.size() != values.size() || constraintNames == null
				|| constraintValues == null || constraintNames.size() != constraintValues.size()) {
			throw new MariusQLException("parameters error");
		}

		if (columns.isEmpty()) {
			return 0;
		}

		StringBuilder resultValue = new StringBuilder();
		for (int i = 0; i < columns.size(); i++) {
			resultValue.append(columns.get(i) + " = " + values.get(i) + ",");
		}

		if (!(resultValue.length() == 0)) {
			resultValue = resultValue.delete(resultValue.length() - 1, resultValue.length());
		}

		StringBuilder constraintsString = new StringBuilder();
		for (int i = 0; i < constraintNames.size(); i++) {
			final String currentConstraintValue = constraintValues.get(i);
			constraintsString.append(constraintNames.get(i)
					+ (currentConstraintValue == null ? " is null" : " = " + currentConstraintValue) + " AND ");
		}

		if (!(constraintsString.length() == 0)) {
			constraintsString = constraintsString.delete(constraintsString.length() - 5, constraintsString.length());
		}

		String cmdDML = "update " + name + " SET " + resultValue + " where " + constraintsString;
		try {
			Statement statement = this.getSession().createSQLStatement();
			int res = statement.executeUpdate(cmdDML);

			statement.close();

			return res;
		} catch (SQLException e) {
			throw new MariusQLException(e + " (query:" + cmdDML + ")");
		}
	}

	@Override
	public int updateRow(String name, String column, String value, String constraintName, String constraintValue) {
		List<String> values = new ArrayList<String>();
		List<String> columns = new ArrayList<String>();

		values.add(value);
		columns.add(column);

		return this.updateRow(name, columns, values, Arrays.asList(constraintName), Arrays.asList(constraintValue));
	}

	@Override
	public int deleteRow(String name, Long rid) {
		String cmdDML = "delete from " + name + " where " + MariusQLConstants.RID_COLUMN_NAME + " = " + rid;

		try {
			Statement statement = this.getSession().createSQLStatement();
			int res = statement.executeUpdate(cmdDML);

			statement.close();

			return res;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}
}
