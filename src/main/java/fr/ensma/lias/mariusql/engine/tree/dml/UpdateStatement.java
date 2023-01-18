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
package fr.ensma.lias.mariusql.engine.tree.dml;

import fr.ensma.lias.mariusql.engine.MariusQLSQLWalkerNode;
import fr.ensma.lias.mariusql.engine.tree.Statement;
import fr.ensma.lias.mariusql.engine.tree.dql.FromClause;
import fr.ensma.lias.mariusql.exception.NotSupportedException;

/**
 * Defines a top-level AST node representing an OntoQL "Update" statement.
 *
 * @author Valentin CASSAIR
 */
public class UpdateStatement extends MariusQLSQLWalkerNode implements Statement {

	private static final long serialVersionUID = 4410509907538922405L;

	@Override
	public int getStatementType() {
		throw new NotSupportedException();
	}

	/**
	 * Retrieve this update statement's from-clause.
	 * 
	 * @return The into-clause
	 */
	public FromClause getFromClause() {
		return (FromClause) this.getFirstChild();
	}

	/**
	 * Retrieve this update statement's set-clause.
	 * 
	 * @return
	 */
	public SetClause getSetClause() {
		return (SetClause) getFromClause().getNextSibling();
	}
}
