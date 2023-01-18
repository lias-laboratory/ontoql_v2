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

import java.util.List;

import antlr.SemanticException;
import antlr.collections.AST;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.engine.tree.dql.FromElement;
import fr.ensma.lias.mariusql.engine.tree.dql.FromReferenceNode;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotSupportedException;

/**
 * Represents an internal identifier.
 * 
 * @author Mickael BARON
 * @author Adel GHAMNIA
 */
public class StarNode extends FromReferenceNode implements ResolvableNode {

	private static final long serialVersionUID = 8764160474109345898L;

	@Override
	public DatatypeEnum getDataType() {
		throw new NotSupportedException();
	}

	@Override
	public String getLabel() {
		throw new NotSupportedException();
	}

	/**
	 * Translate * for a from element.
	 * 
	 * @param fromElement The from element to translate
	 * @return the first properties translated
	 * @throws SemanticException if a semantic error is detected
	 */
	public final List<IdentNode> resolveStar(final FromElement fromElement) throws SemanticException {
		// Create the list of properties node, add it in select, and translate it in SQL
		// resolving the association property.
		List<IdentNode> listPropertiesNode = getWalker().createDescriptionsNodes(fromElement, true);
		// The last node is the oid node if the previous list is empty.
		if (listPropertiesNode.get(1) == null) {
			listPropertiesNode.set(1, listPropertiesNode.get(0));
		}

		// The result must not be added in the list of the projection.
		// This is handle manually.
		listPropertiesNode.get(0).setToAddInProjectionList(false);
		return listPropertiesNode;
	}

	@Override
	public AST resolve(AST prefix) throws SemanticException {
		IdentNode res = null;
		// The result is a node representing the oid of the first from element
		// It is link to nodes representing properties of this from element and
		// to the nodes representing properties of other from elements

		// First get the list of all elements in the from clause
		List<FromElement> fromElements = getWalker().getCurrentFromClause().getFromElements();
		// store the last properties of the set of properties of the previous
		// from element
		IdentNode lastResPreviousCurrentFromElement = null;

		// Iters over each element of the list
		for (int i = 0; i < fromElements.size(); i++) {
			FromElement currentFromElement = fromElements.get(i);
			// Do not iter on added from element to compute path expressions
			if (!currentFromElement.isImplicitJoin()) {

				// Store the result of resolve star (first and last element).
				List<IdentNode> resCurrentFromElement = this.resolveStar(currentFromElement);

				if (resCurrentFromElement == null || resCurrentFromElement.size() != 2) {
					throw new MariusQLException();
				}

				IdentNode firtResCurrentFromElement = resCurrentFromElement.get(0);
				IdentNode lastResCurrentFromElement = resCurrentFromElement.get(1);

				// The final result is the node of the first from element
				if (i == 0) {
					res = firtResCurrentFromElement;
				} else {
					lastResPreviousCurrentFromElement.setNextSibling(firtResCurrentFromElement);
				}
				lastResPreviousCurrentFromElement = lastResCurrentFromElement;
			}
		}

		return res;
	}
}
