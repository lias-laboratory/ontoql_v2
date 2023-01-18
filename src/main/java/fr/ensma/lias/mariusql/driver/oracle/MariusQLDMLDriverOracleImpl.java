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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.ensma.lias.mariusql.driver.AbstractMariusQLDMLDriver;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.util.MariusQLHelper;

/**
 * @author Mickael BARON
 * @author Ghada TRIKI
 * @author Larbi LATRECHE
 */
public class MariusQLDMLDriverOracleImpl extends AbstractMariusQLDMLDriver {

	public MariusQLDMLDriverOracleImpl(MariusQLSession pSession) {
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
			if (currentValue != null) {
				if (MariusQLHelper.removeSyntaxNameIdentifier(currentValue).equalsIgnoreCase("true")) {
					currentValue = "1";
				} else if (MariusQLHelper.removeSyntaxNameIdentifier(currentValue).equalsIgnoreCase("false")) {
					currentValue = "0";
				}
			}
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
			throw new MariusQLException(e);
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
		String value = null;
		for (int i = 0; i < columns.size(); i++) {
			value = values.get(i);
			if (value != null) {
				if (MariusQLHelper.removeSyntaxNameIdentifier(value).equalsIgnoreCase("true")) {
					value = "1";
				} else if (MariusQLHelper.removeSyntaxNameIdentifier(values.get(i)).equalsIgnoreCase("false")) {
					value = "0";
				}
			}
			resultValue.append(columns.get(i) + " = " + value + ",");
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
			throw new MariusQLException(e);
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
}
