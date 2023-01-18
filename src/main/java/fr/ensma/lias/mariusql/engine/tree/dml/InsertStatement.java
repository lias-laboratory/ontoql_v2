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
import fr.ensma.lias.mariusql.exception.NotSupportedException;

/**
 * Defines a top-level AST node representing an OntoQL "insert values"
 * statement.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 */
public class InsertStatement extends MariusQLSQLWalkerNode implements Statement {

	private static final long serialVersionUID = -4334616342214546988L;

	@Override
	public int getStatementType() {
		throw new NotSupportedException();
	}

	/**
	 * Retrieve this insert statement's into-clause.
	 * 
	 * @return The into-clause
	 */
	public IntoClause getIntoClause() {
		return (IntoClause) getFirstChild();
	}

	/**
	 * Retrieve this insert statement's values-clause.
	 * 
	 * @return The values-clause.
	 */
	public ValuesClause getValuesClause() {
		return ((ValuesClause) getIntoClause().getNextSibling());
	}
}
