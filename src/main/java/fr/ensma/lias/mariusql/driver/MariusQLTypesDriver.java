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

import java.util.List;

import antlr.collections.AST;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.core.model.MGenericClass;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.engine.tree.IdentNode;
import fr.ensma.lias.mariusql.engine.tree.dml.SetClause;
import fr.ensma.lias.mariusql.engine.tree.dml.ValuesClause;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSet;

/**
 * @author Mickael BARON
 * @author Florian MHUN
 * @author Valentin CASSAIR
 * @author Ghada TRIKI
 */
public interface MariusQLTypesDriver extends MariusQLDriver {

	/**
	 * @return
	 */
	String getIntegerType();

	/**
	 * @return
	 */
	String getStringType();

	/**
	 * @return
	 */
	String getBooleanType();

	/**
	 * @return
	 */
	String getDoubleType();

	/**
	 * @param datatype
	 * @return
	 */
	String getCollectionType(DatatypeEnum datatype);

	/**
	 * @param value
	 * @return
	 */
	String getcollectionValue(String value);

	/**
	 * 
	 * @return
	 */
	String getReferenceType();

	/**
	 * @param name
	 * @return
	 */
	SQLFunction getSQLFunction(String name);

	/**
	 * @return
	 */
	FactorySQLFunction getFactorySQLFunction();

	/**
	 * 
	 * @param pValues
	 * @return
	 */
	String getCollectionAssociationQuotedSQLValue(List<Long> pValues);

	/**
	 * @param pValues
	 * @return
	 */
	String getCollectionStringQuotedSQLValue(List<String> pValues);

	/**
	 * 
	 * @param arrayData
	 * @return
	 */
	String getJDBCArray(Object arrayData);

	/**
	 * @param fromElement
	 */
	String addImplicitJoinCondition(boolean isArrayTable, IdentNode identNode, MProperty property,
			MGenericClass rangeClass);

	/**
	 * 
	 * @return
	 */
	String getLeftOuterJoin();

	/**
	 * 
	 * @param columnType
	 * @param exprRefNode
	 * @param rs
	 */
	void setExprRefNode(String columnType, AST exprRefNode, MariusQLResultSet rs);

	/**
	 * 
	 * @param setClause
	 */
	void setArrayDescription(SetClause setClause);

	/**
	 * 
	 * @param valueClause
	 */
	void setInsertArrayDescription(ValuesClause valueClause);

	/**
	 * 
	 * @return
	 */
	String getTrue();

	/**
	 * 
	 * @return
	 */
	String getFalse();

	/**
	 * 
	 * @return
	 */
	String getUnnestFunction();

	/**
	 * @param modelSequenceName
	 * @return
	 */
	Long getSequenceNextVal(String modelSequenceName);

	/**
	 * @param modelSequenceName
	 * @return
	 */
	String getDefaultSequenceNextValue(String modelSequenceName);

	/**
	 * @param sqlRange
	 * @return
	 */
	String getCastProjection(String sqlRange);
}
