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

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.sql.ARRAY;
import antlr.collections.AST;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.core.model.MGenericClass;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.driver.AbstractMariusQLTypesDriver;
import fr.ensma.lias.mariusql.driver.FactorySQLFunction;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLSQLTokenTypes;
import fr.ensma.lias.mariusql.engine.tree.ArrayNode;
import fr.ensma.lias.mariusql.engine.tree.IdentNode;
import fr.ensma.lias.mariusql.engine.tree.dml.SetClause;
import fr.ensma.lias.mariusql.engine.tree.dml.ValuesClause;
import fr.ensma.lias.mariusql.exception.NotYetImplementedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSet;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.util.MariusQLHelper;

/**
 * @author Mickael BARON
 * @author Florian MHUN
 * @author Valentin CASSAIR
 * @author Ghada TRIKI
 * @author Adel GHAMNIA
 * @author Florian MHUN
 * @author Larbi LATRECHE
 */
@SuppressWarnings("deprecation")
public class MariusQLTypesDriverOracleImpl extends AbstractMariusQLTypesDriver {

	private static final String INTEGER = "int";

	private static final String STRING = "varchar2(" + MariusQLConstants.MAX_VARCHAR_LENGTH + ")";

	private static final String BOOLEAN = "number(1)";

	private static final String DOUBLE = "double precision";

	private static final String COLLECTION_REF = "TARRAYINT";

	private static final String COLLECTION_INTEGER = "TARRAYINT";

	private static final String COLLECTION_STRING = "TARRAYSTRING";

	private static final String COLLECTION_BOOLEAN = "TARRAYBOOLEAN";

	private static final String COLLECTION_DOUBLE = "TARRAYDOUBLE";

	private static final String TRUE = "1";

	private static final String FALSE = "0";

	@Override
	public String getTrue() {
		return MariusQLTypesDriverOracleImpl.TRUE;
	}

	@Override
	public String getFalse() {
		return MariusQLTypesDriverOracleImpl.FALSE;
	}

	@Override
	public String getIntegerType() {
		return MariusQLTypesDriverOracleImpl.INTEGER;
	}

	@Override
	public String getStringType() {
		return MariusQLTypesDriverOracleImpl.STRING;
	}

	@Override
	public String getBooleanType() {
		return MariusQLTypesDriverOracleImpl.BOOLEAN;
	}

	@Override
	public String getDoubleType() {
		return MariusQLTypesDriverOracleImpl.DOUBLE;
	}

	@Override
	public String getReferenceType() {
		return MariusQLTypesDriverOracleImpl.INTEGER;
	}

	public MariusQLTypesDriverOracleImpl(MariusQLSession pSession) {
		super(pSession);
	}

	@Override
	public String getCollectionType(DatatypeEnum datatype) {
		switch (datatype) {
		case DATATYPEINT:
			return MariusQLTypesDriverOracleImpl.COLLECTION_INTEGER;
		case DATATYPESTRING:
			return MariusQLTypesDriverOracleImpl.COLLECTION_STRING;
		case DATATYPEBOOLEAN:
			return MariusQLTypesDriverOracleImpl.COLLECTION_BOOLEAN;
		case DATATYPEREAL:
			return MariusQLTypesDriverOracleImpl.COLLECTION_DOUBLE;
		case DATATYPEMULTISTRING:
			return MariusQLTypesDriverOracleImpl.COLLECTION_STRING;
		case DATATYPEREFERENCE:
			return MariusQLTypesDriverOracleImpl.COLLECTION_REF;
		default:
			throw new NotYetImplementedException();
		}
	}

	@Override
	public FactorySQLFunction getFactorySQLFunction() {
		return new FactorySQLFunctionOracleImpl();
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
		return MariusQLConstants.ORACLE_COLLECTION_NAME + MariusQLConstants.OPEN_PARENTHESIS + returnValue
				+ MariusQLConstants.CLOSE_PARENTHESIS;
	}

	@Override
	public String getCollectionStringQuotedSQLValue(List<String> pValues) {
		String returnValue = "";

		for (String current : pValues) {
			returnValue = returnValue + current + ",";
		}

		if (!returnValue.isEmpty()) {
			returnValue = returnValue.substring(0, returnValue.length() - 1);
		}
		return MariusQLConstants.ORACLE_COLLECTION_NAME_STRING + MariusQLConstants.OPEN_PARENTHESIS + returnValue
				+ MariusQLConstants.CLOSE_PARENTHESIS;
	}

	@Override
	public String getJDBCArray(Object data) {
		BigDecimal[] values;
		List<Long> arrayValues = new ArrayList<Long>();
		try {
			values = (BigDecimal[]) ((ARRAY) data).getArray();
			for (int i = 0; i < values.length; i++) {
				BigDecimal out_value = (BigDecimal) values[i];
				arrayValues.add(out_value.longValue());
			}
			return MariusQLHelper.getCollectionAssociationSQLValue(arrayValues);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getLeftOuterJoin() {
		return "LEFT OUTER JOIN";
	}

	@Override
	public String addImplicitJoinCondition(boolean isArrayTable, IdentNode pathPropNode, MProperty usedPropertyByName,
			MGenericClass rangeClass) {
		if (isArrayTable) {
			MProperty pathProp = (MProperty) pathPropNode.getDescription();
			String refsValue = this.getSession().getMariusQLDQL().getRefsValues(pathProp.getScope().toSQL(),
					pathPropNode);
			return "ON " + usedPropertyByName.toPropertyText(rangeClass) + " = ANY " + refsValue;
		}
		return "ON " + pathPropNode.getSQL() + " = " + usedPropertyByName.toPropertyText(rangeClass);
	}

	@Override
	public void setExprRefNode(String columnType, AST exprRefNode, MariusQLResultSet rs) {
		try {
			if (MariusQLConstants.ORACLE_VARCHAR_NAME.equalsIgnoreCase(columnType)) {
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
	public void setArrayDescription(SetClause setClause) {
		AST node = setClause;
		List<DatatypeEnum> dataTypes = setClause.getValuesTypes();
		List<DatatypeEnum> newdataTypes = new ArrayList<DatatypeEnum>();

		for (DatatypeEnum type : dataTypes) {
			if (type.getName().equals(MariusQLConstants.COLLECTION_NAME)) {
				newdataTypes.add(DatatypeEnum.DATATYPEORACLECOLLECTION);
			} else {
				newdataTypes.add(type);
			}
		}
		setClause.setValuesTypes(newdataTypes);

		while (node != null) {
			AST array = node.getFirstChild().getFirstChild().getNextSibling();
			if (array instanceof ArrayNode) {
				array.setText(MariusQLConstants.ORACLE_COLLECTION_NAME);
				array.setType(MariusQLSQLTokenTypes.TARRAYINT);
			}
			node = node.getNextSibling();
		}

	}

	@Override
	public void setInsertArrayDescription(ValuesClause valueClause) {
		AST node = valueClause.getFirstChild();
		List<DatatypeEnum> dataTypes = valueClause.getValuesTypes();
		List<DatatypeEnum> newdataTypes = new ArrayList<DatatypeEnum>();

		for (DatatypeEnum type : dataTypes) {
			if (type.getName().equals(MariusQLConstants.COLLECTION_NAME)) {
				newdataTypes.add(DatatypeEnum.DATATYPEORACLECOLLECTION);
			} else {
				newdataTypes.add(type);
			}
		}
		// valueClause.setValuesTypes(newdataTypes);

		while (node != null) {
			AST array = node;
			AST arrayValue = array.getFirstChild();

			if (array instanceof ArrayNode) {
				if (arrayValue.getType() == MariusQLSQLTokenTypes.NUM_INT) {
					array.setText(MariusQLConstants.ORACLE_COLLECTION_NAME);
					array.setType(MariusQLSQLTokenTypes.TARRAYINT);
				} else if (arrayValue.getType() == MariusQLSQLTokenTypes.NUM_DOUBLE) {
					array.setText(MariusQLConstants.ORACLE_COLLECTION_NAME_DOUBLE);
					array.setType(MariusQLSQLTokenTypes.TARRAYDOUBLE);
				} else if (arrayValue.getType() == MariusQLSQLTokenTypes.QUOTED_STRING) {
					array.setText(MariusQLConstants.ORACLE_COLLECTION_NAME_STRING);
					array.setType(MariusQLSQLTokenTypes.TARRAYSTRING);
				} else if (arrayValue.getType() == MariusQLSQLTokenTypes.TRUE
						|| arrayValue.getType() == MariusQLSQLTokenTypes.FALSE) {
					array.setText(MariusQLConstants.ORACLE_COLLECTION_NAME_BOOLEAN);
					array.setType(MariusQLSQLTokenTypes.TARRAYINT);
					while (arrayValue != null) {
						if (arrayValue.getType() == MariusQLSQLTokenTypes.TRUE
								|| arrayValue.getType() == MariusQLSQLTokenTypes.FALSE) {

							if (arrayValue.getText().equalsIgnoreCase("true")) {
								arrayValue.setText(this.getSession().getMariusQLTypes().getTrue());
							} else if (arrayValue.getText().equalsIgnoreCase("false")) {
								arrayValue.setText(this.getSession().getMariusQLTypes().getFalse());
							}
						}

						arrayValue = arrayValue.getNextSibling();
					}
				} else {
					array.setText(MariusQLConstants.ORACLE_COLLECTION_NAME);
					array.setType(MariusQLSQLTokenTypes.TARRAYINT);
				}
			}
			node = node.getNextSibling();
		}
	}

	@Override
	public String getUnnestFunction() {
		return "TABLE";
	}

	@Override
	public Long getSequenceNextVal(String modelSequenceName) {
		throw new NotYetImplementedException();
	}

	@Override
	public String getDefaultSequenceNextValue(String modelSequenceName) {
		throw new NotYetImplementedException();
	}

	@Override
	public String getcollectionValue(String value) {
		throw new NotYetImplementedException();
	}

	@Override
	public String getCastProjection(String sqlRange) {
		return "";
	}
}
