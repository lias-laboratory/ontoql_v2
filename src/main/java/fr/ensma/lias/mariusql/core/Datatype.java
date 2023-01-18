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
package fr.ensma.lias.mariusql.core;

import java.util.List;

/**
 * (OLD: EntityDatatype)
 * 
 * Methods required on an model datatype for the execution of an MariusQL query.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 * @author Valentin CASSAIR
 * @author Florian MHUN
 */
public interface Datatype extends PersistentObject {

	String OP_LIKE = "LIKE";

	String OP_NOT_LIKE = "NOT LIKE";

	String OP_INF = "<";

	String OP_SUP = ">";

	String OP_INFEG = "<=";

	String OP_SUPEG = ">=";

	String OP_EG = "=";

	String OP_IN = "IN";

	String OP_PLUS = "+";

	String OP_MINUS = "-";

	String OP_DIV = "/";

	String OP_STAR = "*";

	String OP_CONCAT = "||";

	String REAL_NAME = "REAL";

	String INT_NAME = "INT";

	String BOOLEAN_NAME = "BOOLEAN";

	String ENNUMERATE_NAME = "ENUM";

	String COLLECTION_NAME = "ARRAY";

	String ASSOCIATION_NAME = "REF";

	String MULTISTRING_TYPE_NAME = "MULTISTRING";

	String URI_TYPE_NAME = "URITYPE";

	String COUNT_TYPE_NAME = "COUNTTYPE";

	/**
	 * @return True if this datatype is an association type.
	 */
	boolean isAssociationType();

	/**
	 * @return True if this datatype is a collection type.
	 */
	boolean isCollectionType();

	/**
	 * @return True if this datatype is a collection of association type.
	 */
	boolean isCollectionAssociationType();

	/**
	 * @return True if this datatype is a simple type
	 */
	boolean isSimpleType();

	/**
	 * @return True if this datatype is a multilingual type.
	 */
	boolean isMultilingualType();

	/**
	 * Set the internal identifier of this datatype.
	 * 
	 * @param id the internal identifier of this datatype.
	 */
	void setInternalId(Long id);

	/**
	 * The name for this datatype.
	 * 
	 * @return a printable label for this datatype
	 */
	String getName();

	/**
	 * The name of the table where this datatype persist.
	 * 
	 * @return a printable label for this datatype
	 */
	String getTableName();

	/**
	 * The boolean operators usable on this datatype.
	 * 
	 * @return the usable boolean operators
	 */
	List<String> getBooleanOperators();

	/**
	 * The arithmetic operators usable on this datatype.
	 * 
	 * @return the usable arithmetics operators
	 */
	List<String> getArithmeticOperators();

	/**
	 * @return
	 */
	String toSQL();

	/**
	 * @return
	 */
	DatatypeEnum getDatatypeEnum();

	/**
	 * @return
	 */
	DatatypeCollection toCollection();

	/**
	 * 
	 * @return
	 */
	DatatypeReference toReference();
}
