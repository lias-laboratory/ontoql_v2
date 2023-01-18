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
package fr.ensma.lias.mariusql.sparql;

import java.util.List;

import antlr.ASTFactory;
import antlr.collections.AST;

/**
 * Methods define by a SPARQL class or entity
 * 
 * @author Stephane JEAN, Mickael BARON and Raoul TIAM
 */
public interface CategorySPARQL {

	/**
	 * Get the list of properties or attributes of this class or entity
	 * 
	 * @return the list of properties or attributes of this class or entity
	 */
	List<DescriptionSPARQL> getDescriptions();

	/**
	 * Add a property or an attribute to this class or entity
	 * 
	 * @param description the property or attribute to add
	 */
	void addDescription(DescriptionSPARQL description);

	/**
	 * Get the alias used in the OntoQL query to reference this SPARQL class or
	 * entity
	 * 
	 * @return the alias used in the OntoQL query to reference this SPARQL class or
	 *         entity
	 */
	String getAlias();

	/**
	 * Get the variable used in the SPARQL query to reference this SPARQL class or
	 * entity
	 * 
	 * @return the variable used in the SPARQL query to reference this SPARQL class
	 *         or entity
	 */
	String getVariable();

	/**
	 * Set the alias used in the OntoQL query to reference this SPARQL class or
	 * entity
	 * 
	 * @param alias the alias used in the OntoQL query to reference this SPARQL
	 *              class or entity
	 */
	void setAlias(String alias);

	/**
	 * Set the variable used in the SPARQL query to reference this SPARQL class or
	 * entity
	 * 
	 * @param the variable used in the SPARQL query to reference this SPARQL class
	 *            or entity
	 */
	void setVariable(String variable);

	/**
	 * Get the OntoQL from element corresponding to this category
	 * 
	 * @param astFactory a factory of node
	 * @return the OntoQL from element corresponding to this category
	 */
	AST getFromElement(ASTFactory astFactory);

	/**
	 * @return true if this category is a class (false if it is an entity)
	 */
	boolean isClass();
}
