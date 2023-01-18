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

import java.util.ArrayList;
import java.util.List;

import antlr.collections.AST;
import fr.ensma.lias.mariusql.driver.postgresql.antlr.PostgresSQLTokenTypes;
import fr.ensma.lias.mariusql.engine.MariusQLSQLWalkerNode;
import fr.ensma.lias.mariusql.engine.util.ASTPrinter;

/**
 * Represents the list of expressions in a SELECT clause.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 */
public class SelectClause extends MariusQLSQLWalkerNode {

	private static final long serialVersionUID = 3637104396316608587L;

	/**
	 * Returns an array of SelectExpressions gathered from the children of the given
	 * parent AST node.
	 * 
	 * @return an array of SelectExpressions gathered from the children of the given
	 *         parent AST node.
	 */
	public List<SelectExpression> collectSelectExpressions() {
		// Get the first child to be considered.
		AST firstChild = getFirstSelectExpression();
		AST parent = this;
		List<SelectExpression> list = new ArrayList<SelectExpression>(parent.getNumberOfChildren());
		for (AST n = firstChild; n != null; n = n.getNextSibling()) {
			if (n instanceof SelectExpression) {
				list.add((SelectExpression) n);
			} else {
				throw new IllegalStateException("Unexpected AST: " + n.getClass().getName() + " "
						+ new ASTPrinter(PostgresSQLTokenTypes.class).showAsString(n, ""));
			}
		}
		return list;
	}

	public AST getFirstSelectExpression() {
		AST n = getFirstChild();
		// Skip 'DISTINCT' and 'ALL', so we return the first expression node.
		while (n != null
				&& (n.getType() == PostgresSQLTokenTypes.DISTINCT || n.getType() == PostgresSQLTokenTypes.ALL)) {
			n = n.getNextSibling();
		}
		return n;
	}

}
