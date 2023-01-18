/*********************************************************************************
* This file is part of OntoQL2 Project.
* Copyright (C) 2014  LIAS - ENSMA
*   Teleport 2 - 1 avenue Clement Ader
*   BP 40109 - 86961 Futuroscope Chasseneuil Cedex - FRANCE
* 
* OntoQL2 is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* OntoQL2 is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public License
* along with OntoQL2.  If not, see <http://www.gnu.org/licenses/>.
**********************************************************************************/
package fr.ensma.lias.mariusql.engine.tree;

import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.engine.tree.dql.AbstractSelectExpression;
import fr.ensma.lias.mariusql.engine.tree.dql.SelectExpression;

/**
 * @author Mickael BARON
 * @author Adel GHAMNIA
 */
public class UnaryNode extends AbstractSelectExpression {

	private static final long serialVersionUID = 5057057909552500082L;

	@Override
	public final DatatypeEnum getDataType() {
		return ((SelectExpression) getFirstChild()).getDataType();
	}

	@Override
	public final String getLabel() {
		return getText() + " " + ((SelectExpression) getFirstChild()).getLabel();
	}
}
