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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import antlr.collections.AST;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.core.model.MGenericClass;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.driver.AbstractMariusQLTypesDriver;
import fr.ensma.lias.mariusql.driver.FactorySQLFunction;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLSQLTokenTypes;
import fr.ensma.lias.mariusql.engine.tree.IdentNode;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotYetImplementedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSet;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * @author Mickael BARON
 * @author Florian MHUN
 * @author Valentin CASSAIR
 * @author Ghada TRIKI
 * @author Adel GHAMNIA
 * @author Florian MHUN
 * @author Larbi LATRECHE
 */
public class MariusQLTypesDriverHsqlDBImpl extends AbstractMariusQLTypesDriver {

	private static final String INTEGER = "int";

	private static final String BIGINT = "bigint";

	private static final String STRING = "varchar (" + MariusQLConstants.MAX_VARCHAR_LENGTH + ")";

	private static final String DOUBLE = "double";

	private static final String COLLECTION_REF = "bigint ARRAY DEFAULT ARRAY[]";

	private static final String COLLECTION_INTEGER = "int ARRAY DEFAULT ARRAY[]";

	private static final String COLLECTION_STRING = "varchar(" + MariusQLConstants.MAX_VARCHAR_LENGTH
			+ ") ARRAY DEFAULT ARRAY[]";

	private static final String COLLECTION_BOOLEAN = "boolean ARRAY DEFAULT ARRAY[]";

	private static final String COLLECTION_DOUBLE = "double ARRAY DEFAULT ARRAY[]";

	@Override
	public String getIntegerType() {
		return MariusQLTypesDriverHsqlDBImpl.INTEGER;
	}

	@Override
	public String getStringType() {
		return MariusQLTypesDriverHsqlDBImpl.STRING;
	}

	@Override
	public String getDoubleType() {
		return MariusQLTypesDriverHsqlDBImpl.DOUBLE;
	}

	@Override
	public String getReferenceType() {
		return MariusQLTypesDriverHsqlDBImpl.BIGINT;
	}

	public MariusQLTypesDriverHsqlDBImpl(MariusQLSession pSession) {
		super(pSession);
	}

	@Override
	public String getCollectionType(DatatypeEnum datatype) {
		switch (datatype) {
		case DATATYPEINT:
			return MariusQLTypesDriverHsqlDBImpl.COLLECTION_INTEGER;
		case DATATYPESTRING:
			return MariusQLTypesDriverHsqlDBImpl.COLLECTION_STRING;
		case DATATYPEBOOLEAN:
			return MariusQLTypesDriverHsqlDBImpl.COLLECTION_BOOLEAN;
		case DATATYPEREAL:
			return MariusQLTypesDriverHsqlDBImpl.COLLECTION_DOUBLE;
		case DATATYPEMULTISTRING:
			return MariusQLTypesDriverHsqlDBImpl.COLLECTION_STRING;
		case DATATYPEREFERENCE:
			return MariusQLTypesDriverHsqlDBImpl.COLLECTION_REF;
		default:
			throw new MariusQLException("The data type does not exist");
		}
	}

	@Override
	public FactorySQLFunction getFactorySQLFunction() {
		return new FactorySQLFunctionHsqlDBImpl();
	}

	@Override
	public String getCollectionAssociationQuotedSQLValue(List<Long> pValues) {
		String returnValue = "";

		for (Long current : pValues) {
			returnValue = returnValue + current + ",";
		}

		if (!returnValue.isEmpty()) {
			returnValue = returnValue.substring(0, returnValue.length() - 1);
		}

		return MariusQLConstants.COLLECTION_NAME + MariusQLConstants.OPEN_SQUARE + returnValue
				+ MariusQLConstants.CLOSE_SQUARE;
	}

	@Override
	public String getCollectionStringQuotedSQLValue(List<String> pValues) {
		StringBuilder returnValue = new StringBuilder();

		for (String current : pValues) {
			returnValue.append(
					MariusQLConstants.SIMPLE_QUOTED_STRING + current + MariusQLConstants.SIMPLE_QUOTED_STRING + ",");
		}

		if (!(returnValue.length() == 0)) {
			returnValue = returnValue.delete(returnValue.length() - 1, returnValue.length());
		}

		return MariusQLConstants.COLLECTION_NAME + MariusQLConstants.OPEN_SQUARE + returnValue
				+ MariusQLConstants.CLOSE_SQUARE;
	}

	@Override
	public String getJDBCArray(Object data) {
		String arrayData = data.toString();
		return arrayData.replace("ARRAY[", "{").replace("]", "}");
	}

	@Override
	public String getLeftOuterJoin() {
		return "LEFT OUTER JOIN";
	}

	@Override
	public String addImplicitJoinCondition(boolean isArrayTable, IdentNode pathPropNode, MProperty usedPropertyByName,
			MGenericClass rangeClass) {
		if (isArrayTable) {
			return "ON  POSITION_ARRAY ( " + usedPropertyByName.toPropertyText(rangeClass) + " IN "
					+ pathPropNode.getSQL() + ") <> 0 ";
		}
		return "ON " + pathPropNode.getSQL() + " = " + usedPropertyByName.toPropertyText(rangeClass);
	}

	@Override
	public void setExprRefNode(String columnType, AST exprRefNode, MariusQLResultSet rs) {
		try {
			if (MariusQLConstants.VARCHAR_NAME.equalsIgnoreCase(columnType)) {
				exprRefNode.setType(MariusQLSQLTokenTypes.QUOTED_STRING);
				exprRefNode.setText("'" + rs.getString(1) + "'");
			} else if (MariusQLConstants.INT_NAME.equalsIgnoreCase(columnType)) {
				exprRefNode.setType(MariusQLSQLTokenTypes.NUM_INT);
				exprRefNode.setText(rs.getString(1));
			} else if (MariusQLConstants.BOOLEAN_NAME.equalsIgnoreCase(columnType)) {
				throw new NotYetImplementedException();
			} else if (MariusQLConstants.REF_NAME.equalsIgnoreCase(columnType)) {
				throw new NotYetImplementedException();
				// exprRefNode.setType(MariusQLSQLTokenTypes.NUM_INT);
				// exprRefNode.setText(rs.getString(1));
			} else {
				throw new NotYetImplementedException();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Long getSequenceNextVal(String modelSequenceName) {
		Statement statement = null;

		try {
			statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery("call next value for " + modelSequenceName);

			if (resultSet.next()) {
				final long value = resultSet.getLong(1);
				statement.close();
				return value;
			} else {
				throw new MariusQLException("Must return a long value.");
			}
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public String getDefaultSequenceNextValue(String modelSequenceName) {
		return " GENERATED BY DEFAULT AS SEQUENCE " + modelSequenceName;
	}

	@Override
	public String getcollectionValue(String value) {
		return "ARRAY[" + value + "]";
	}

	@Override
	public String getCastProjection(String sqlRange) {
		return "";
	}
}
