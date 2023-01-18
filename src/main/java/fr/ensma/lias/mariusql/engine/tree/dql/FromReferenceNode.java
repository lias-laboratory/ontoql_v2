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

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.engine.tree.DisplayableNode;
import fr.ensma.lias.mariusql.engine.tree.InitializeableNode;
import fr.ensma.lias.mariusql.engine.tree.ResolvableNode;

/**
 * Represents a reference to a FROM element, for example a class alias in a
 * WHERE clause.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 */
public abstract class FromReferenceNode extends AbstractSelectExpression
		implements ResolvableNode, DisplayableNode, InitializeableNode {

	private static final long serialVersionUID = 6617763889299414305L;

	/**
	 * The from element referenced by this node.
	 */
	protected FromElement fromElement;

	@Override
	public FromElement getFromElement() {
		return fromElement;
	}

	/**
	 * set the from element referenced by this node.
	 * 
	 * @param fromElement the from element referenced by this node
	 */
	public final void setFromElement(final FromElement fromElement) {
		this.fromElement = fromElement;
	}

	@Override
	public String getDisplayText() {
		StringBuilder buf = new StringBuilder();
		buf.append(MariusQLConstants.OPEN_BRACKET)
				.append((fromElement == null) ? "no fromElement" : fromElement.getDisplayText());
		buf.append(MariusQLConstants.CLOSE_BRACKET);
		return buf.toString();
	}
}
