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
package fr.ensma.lias.mariusql.engine.tree.dql;

import antlr.collections.AST;
import fr.ensma.lias.mariusql.driver.postgresql.antlr.PostgresSQLTokenTypes;
import fr.ensma.lias.mariusql.engine.MariusQLSQLWalkerNode;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLTokenTypes;
import fr.ensma.lias.mariusql.engine.tree.Statement;
import fr.ensma.lias.mariusql.engine.util.ASTUtil;

/**
 * Defines a top-level AST node representing an OntoQL query
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 */
public class SelectStatement extends MariusQLSQLWalkerNode implements Statement {

	private static final long serialVersionUID = -8510522646833918059L;

	/**
	 * From clause of this query.
	 */
	protected FromClause fromClause;

	/**
	 * Where clause of this query.
	 */
	protected AST whereClause;

	/**
	 * @see Statement#getStatementType()
	 */
	public int getStatementType() {
		return MariusQLTokenTypes.QUERY;
	}

	/**
	 * Get the from clause of this query
	 * 
	 * @return the from clause of this query
	 */
	public final FromClause getFromClause() {
		if (fromClause == null) {
			fromClause = (FromClause) ASTUtil.findTypeInChildren(this, MariusQLTokenTypes.FROM);
		}
		return fromClause;
	}

	/**
	 * Search the Where clause of this query
	 * 
	 * @return the Where clause of this query or null if there is no such clause
	 */
	protected AST locateWhereClause() {
		return ASTUtil.findTypeInChildren(this, MariusQLTokenTypes.WHERE);
	}

	/**
	 * Is this query has a Where clause?
	 * 
	 * @return True if this query has a Where clause
	 */
	public final boolean hasWhereClause() {
		AST whereClause = locateWhereClause();
		return whereClause != null && whereClause.getNumberOfChildren() > 0;
	}

	/**
	 * Get the Where clause of this query or create a new one if it doesn't has one.
	 * 
	 * @return the Where clause of this query
	 */
	public final AST getWhereClause() {
		if (whereClause == null) {
			whereClause = locateWhereClause();
		}
		return whereClause;
	}

	public final void createWhereClause() {
		whereClause = ASTUtil.create(getWalker().getASTFactory(), MariusQLTokenTypes.WHERE, "WHERE");
		// inject the WHERE after the parent
		AST parent = getFromClause();
		whereClause.setNextSibling(parent.getNextSibling());
		parent.setNextSibling(whereClause);
	}

	/**
	 * Locate the select clause that is part of this select statement.
	 * 
	 * @return the select clause.
	 */
	public final SelectClause getSelectClause() {
		return (SelectClause) ASTUtil.findTypeInChildren(this, PostgresSQLTokenTypes.SELECT_CLAUSE);
	}
}
