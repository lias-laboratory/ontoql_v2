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

import antlr.ASTFactory;
import antlr.collections.AST;
import fr.ensma.lias.mariusql.engine.SPARQLMariusQLWalker;
import fr.ensma.lias.mariusql.util.SPARQLUtil;

/**
 * @author Stephane JEAN
 * @author Mickael BARON
 * @author Raoul TIAM
 * @author Adel GHAMNIA
 */
public class AttributeURISPARQL extends AbstractDescriptionSPARQL {

	public AttributeURISPARQL(String text, String var, SPARQLMariusQLWalker walker) {
		this.walker = walker;
		name = text;
		variable = var;
	}

	public boolean isAttribute() {
		return true;
	}

	public boolean isMultivalued() {
		return false;
	}

	public boolean isOptional() {
		return false;
	}

	public boolean isProperty() {
		return false;
	}

	/**
	 * Get the node representing this SPARQL property or attribute
	 * 
	 * @param aliasNeeded True if an alias must be added
	 * @param isCoalesce  True if this SPARQL property or attribute is involved in a
	 *                    join condition requiring the coalesce operator
	 * @return the node representing this SPARQL property or attribute
	 */
	public AST getDotElement(boolean aliasNeeded, boolean isCoalesce, ASTFactory astFactory, boolean join) {
		AST res = null;
		String aliasName = scope.getAlias();
		res = SPARQLUtil.getNodeAttributeURI(aliasName, astFactory, join);
		return res;
	}
}
