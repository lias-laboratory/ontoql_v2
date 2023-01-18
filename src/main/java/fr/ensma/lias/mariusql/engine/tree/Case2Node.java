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

import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.engine.tree.dql.AbstractSelectExpression;
import fr.ensma.lias.mariusql.engine.tree.dql.SelectExpression;

/**
 * Represents a case ... when .. then ... else ... end expression in a select.
 * Adapted from Hibernate
 * 
 * @author Stéphane JEAN
 */
public class Case2Node extends AbstractSelectExpression implements SelectExpression {

	private static final long serialVersionUID = 5830222677034152726L;

	@Override
	public DatatypeEnum getDataType() {
		return getFirstThenNode().getDataType();
	}

	private SelectExpression getFirstThenNode() {
		return (SelectExpression) getFirstChild().getNextSibling().getFirstChild().getNextSibling();
	}

	@Override
	public String getLabel() {
		return getFirstThenNode().getLabel();
	}
}
