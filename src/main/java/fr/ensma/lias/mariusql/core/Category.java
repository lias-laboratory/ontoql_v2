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
 * Interface representing a category, i.e a class of a model (MClass) or a class
 * of a meta model (MMClass).
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 * @author Florian MHUN
 */
public interface Category extends PersistentObject {

	/**
	 * Get the name in the current natural language of this category.
	 * 
	 * @return the name in the current natural language of this category.
	 */
	String getName();

	/**
	 * Get the namespace in which this class is defined.
	 * 
	 * @return
	 */
	String getNamespace();

	/**
	 * Check if this category is a class of an ontology.
	 * 
	 * @return true if this category is a class of an ontology.
	 */
	boolean isClass();

	/**
	 * Check if this category is an entity of the ontology model.
	 * 
	 * @return true if this category is an entity of the ontology model.
	 */
	boolean isEntity();

	/**
	 * Check if this category is an abstract class or entity.
	 * 
	 * @return true if this category is astract.
	 */
	boolean isAbstract();

	/**
	 * Get a label for the type of this category ("class" or "entity").
	 * 
	 * @return a label for the type of this category.
	 */
	String getTypeLabel();

	/**
	 * Return the SQL text allowing to retrieve the extent of this category for all
	 * its applicable properties.
	 * 
	 * @return the SQL text
	 */
	String toSQL();

	/**
	 * Return all the subcategories of this category.
	 * 
	 * @return the subcategories (with transitive closure) of this category
	 */
	List<Category> getAllSubcategories();

	/**
	 * Return only direct subcategories of this category.
	 * 
	 * @return the direct subcategories
	 */
	List<Category> getDirectSubCategories();

	/**
	 * Returns boolean indicating the category has sub categories or not
	 * 
	 * @return True if the category has subcategories, false otherwise
	 */
	boolean hasSubCategories();

	/**
	 * Get class hierarchy (including sub categories of children) from this category
	 * 
	 * @return The class hierarchy from this category
	 */
	List<Category> getHierarchyFromThisCategory();

	/**
	 * Get a description defined on this category according to an identifier.
	 * 
	 * @param identifier
	 * @return
	 */
	Description getDefinedDescription(String identifier);

	/**
	 * Set wether this category is considered in a polymorphic context.
	 * 
	 * @param polymorph True if this category is considered in a polymorphic context
	 */
	void setPolymorph(boolean polymorph);

	/**
	 * Wether this category is considered in a polymorphic context.
	 * 
	 * @return True if this category is considered in a polymorphic context
	 */
	boolean isPolymorph();

	/**
	 * Get the alias associated to this category.
	 * 
	 * @return the alias associated to this category
	 */
	String getCategoryAlias();

	/**
	 * Set the alias associated to this category.
	 * 
	 * @param alias the alias associated to this category
	 */
	void setCategoryAlias(String alias);

	/**
	 * Get the alias generated for the corresponding table of this category.
	 * 
	 * @return the alias generated for the corresponding table of this category
	 */
	String getTableAlias();

	/**
	 * Set the alias generated for the corresponding table of this category.
	 */
	void setTableAlias(String tableAlias);

	/**
	 * True if the category is referenced by existing datatypes
	 * 
	 * @return
	 */
	boolean isReferenced();

	/**
	 * True if instance of this category exists, false otherwise
	 * 
	 * @param rid
	 * @return
	 */
	boolean isModelInstanceExists(Long rid);

	/**
	 * Return the SQL text with table alias allowing to retrieve the extent of this
	 * category for all its applicable properties.
	 * 
	 * @return
	 */
	String toSQLWithAlias();
}
