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
package fr.ensma.lias.mariusql.engine.tree;

import antlr.collections.AST;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.engine.tree.dql.AbstractSelectExpression;
import fr.ensma.lias.mariusql.engine.tree.dql.SelectExpression;

/**
 * Arithmetic node.
 *
 * @author Stéphane JEAN
 * @author Adel GHAMNIA
 */
public class ArithmeticNode extends AbstractSelectExpression {

	private static final long serialVersionUID = -3229175142826766773L;

	@Override
	public final String getLabel() {
		AST first = getFirstChild();
		AST second = getFirstChild().getNextSibling();

		return ((SelectExpression) first).getLabel() + " " + getText() + " " + ((SelectExpression) second).getLabel();

	}

	@Override
	public final DatatypeEnum getDataType() {
		// For the moment return type of first element
		// Note that type checking of the arithmetic expression has already be
		// done
		AST first = getFirstChild();
		DatatypeEnum x = ((SelectExpression) first).getDataType();
		return x;
	}
}
