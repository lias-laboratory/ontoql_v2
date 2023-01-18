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

/**
 * Interface representing a description, i.e a property of a model or an
 * attribute of the meta model.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 */
public interface Description extends PersistentObject {

	/**
	 * Get the name in the current natural language of this category.
	 * 
	 * @return the name in the current natural language of this category.
	 */
	String getName();

	/**
	 * Check if this description is a property of an ontology.
	 * 
	 * @return true if this description is a property of an ontology.
	 */
	boolean isProperty();

	/**
	 * Check if this description is an attribute of the ontology model.
	 * 
	 * @return true if this description is an attribute of the ontology model.
	 */
	boolean isAttribute();

	/**
	 * Check if this description is a multilingual attribute or property.
	 * 
	 * @return true if this description is a multilingual attribute or property.
	 */
	boolean isMultilingualDescription();

	/**
	 * Set the language of a multilingual attribute.
	 */
	void setLgCode(String lgCode);

	/**
	 * Get a label for the type of this description ("property" or "attribute").
	 * 
	 * @return a label for the type of this description.
	 */
	String getTypeLabel();

	/**
	 * @return
	 */
	String toSQL();

	/**
	 * @param currentContext
	 * @return
	 */
	String toSQL(Category currentContext);

	/**
	 * @return
	 */
	Category getCurrentContext();

	/**
	 * @param currentContext
	 */
	void setCurrentContext(Category currentContext);

	/**
	 * @return
	 */
	Datatype getRange();

	/**
	 * Set the range of this description.
	 * 
	 * @param range
	 */
	void setRange(Datatype range);

	/**
	 * Determine if this description is defined on its current context.
	 * 
	 * @return True is this description is defined on its current context
	 */
	boolean isDefined();

	/**
	 * Determine if this description is defined on the parameter context.
	 * 
	 * @param context category on which this description is tested
	 * @return True is this description is defined on the parameter context
	 */
	boolean isDefined(Category context);

	/**
	 * Return the definition of this attribute in SQL (name datatype) or null if it
	 * can not be represented in SQL.
	 * 
	 * @return the definition of this attribute in SQL or null if it can not be
	 *         reprensented (ref and collection type)
	 */
	String getSQLDefinition();

	/**
	 * 
	 * @param context
	 * @param polymorph
	 * @return
	 */
	String toSQL(Category context, boolean polymorph);

	/**
	 * 
	 * @return
	 */
	boolean isCore();

	/**
	 * 
	 * @param isCore
	 */
	void setIsCore(boolean isCore);

	/**
	 * Return true if this description is a mariusql identifier (rid, code, name),
	 * false otherwise
	 * 
	 * @return
	 */
	boolean isIdentifier();

	/**
	 * @param withAlias
	 * @return
	 */
	String toSQL(boolean withAlias);
}
