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
import fr.ensma.lias.mariusql.engine.antlr.MariusQLSQLTokenTypes;
import fr.ensma.lias.mariusql.engine.tree.dql.AbstractSelectExpression;
import fr.ensma.lias.mariusql.exception.MariusQLException;

/**
 * Represents a literal.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 * @author Adel GHAMNIA
 */
public class LiteralNode extends AbstractSelectExpression implements MariusQLSQLTokenTypes {

	private static final long serialVersionUID = -6432991269579861037L;

	@Override
	public DatatypeEnum getDataType() {
		switch (getType()) {
		case NUM_LONG:
		case NUM_INT:
			return DatatypeEnum.DATATYPEINT;
		case QUOTED_STRING:
		case NULL:
			return DatatypeEnum.DATATYPESTRING;
		case NUM_DOUBLE:
			return DatatypeEnum.DATATYPEREAL;
		case TRUE:
		case FALSE:
			return DatatypeEnum.DATATYPEBOOLEAN;
		}

		throw new MariusQLException("Token " + getType() + " not supported.");
	}

	@Override
	public String getLabel() {
		return this.getText();
	}
}
