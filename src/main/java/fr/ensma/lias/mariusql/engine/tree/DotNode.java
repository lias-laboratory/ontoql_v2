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

import antlr.SemanticException;
import antlr.collections.AST;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.core.metamodel.MMAttribute;
import fr.ensma.lias.mariusql.core.model.MClass;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.driver.postgresql.antlr.PostgresSQLTokenTypes;
import fr.ensma.lias.mariusql.engine.tree.dql.FromClause;
import fr.ensma.lias.mariusql.engine.tree.dql.FromElement;
import fr.ensma.lias.mariusql.engine.tree.dql.FromReferenceNode;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotSupportedException;

/**
 * Represents a path expression.
 *
 * @author Mickael BARON
 * @author Adel GHAMNIA
 */
public class DotNode extends FromReferenceNode implements ResolvableNode {

	private static final long serialVersionUID = -331903866525033629L;

	@Override
	public final AST resolve(AST prefix) throws SemanticException {
		AST res = resolve(prefix, null);
		return res;
	}

	@Override
	public String getLabel() {
		return "";
	}

	/**
	 * Helper method to resolve a path expression.
	 * 
	 * @param prefix prefix of this node
	 * @param f      from element corresponding to this node
	 * @return the resolved node
	 * @throws SemanticException if a semantic error is detected
	 */
	public final AST resolve(AST prefix, final FromElement f) throws SemanticException {
		// Get the current from clause
		FromClause currentFromClause = getWalker().getCurrentFromClause();
		// Get the left child of this dot node
		IdentNode propNode = (IdentNode) getFirstChild();
		// It may be a prefix
		String prefixText = propNode.getText();
		FromElement currentFromElement = currentFromClause.getFromElement(prefixText);

		if (currentFromElement != null) {
			return ((ResolvableNode) propNode.getNextSibling()).resolve(propNode);
		}

		if (f == null) {
			propNode.resolve(prefix, true, true);
		} else {
			propNode.setFromElement(f);
			propNode.resolve(prefix, false, true);
		}

		if (propNode.getDescription() instanceof MMAttribute) {
			AST pathPropNode = propNode.getNextSibling();
			if (pathPropNode.getType() == PostgresSQLTokenTypes.DOT) {
				return ((DotNode) pathPropNode).resolve(null, propNode.getFromElement());
			} else {
				((IdentNode) pathPropNode).setFromElement(propNode.getFromElement());
				return ((IdentNode) pathPropNode).resolve(null, false, true);
			}
		}

		return resolveDotExpresession(propNode, propNode.getFromElement().getCategory().isPolymorph());
	}

	/**
	 * Helper method to resolve a path expression.
	 * 
	 * @param propNode  node corresponding to an association property
	 * @param polymorph true if the query is polymorphic
	 * @return the resolved node
	 * @throws SemanticException if a semantic error is detected
	 */
	protected final AST resolveDotExpresession(final IdentNode propNode, final boolean polymorph)
			throws SemanticException {
		final MClass currentContext = (MClass) propNode.getFromElement().getCategory();
		MProperty pathProp = null;
		if (propNode.getDescription().isProperty())
			pathProp = (MProperty) propNode.getDescription();
		else
			throw new MariusQLException("Is not a property");

		pathProp.setCurrentContext(currentContext);

		// Add an alias to the range of the path property
		FromElement fromElementAlreadyAdded = getWalker().getGeneratedFromElement(pathProp);
		if (fromElementAlreadyAdded == null) {
			// The path hasn't already been proceed
			fromElementAlreadyAdded = getWalker().addImplicitJoin(propNode.getFromElement(), propNode, polymorph);
		}

		// The path of this property is the next sibling
		AST pathPropNode = propNode.getNextSibling();

		if (pathPropNode.getType() == PostgresSQLTokenTypes.DOT) {
			return ((DotNode) pathPropNode).resolve(null, fromElementAlreadyAdded);
		} else {
			MProperty finalPathProperty = null;
			AST firstChild = pathPropNode.getFirstChild();
			if (firstChild != null && firstChild.getType() == PostgresSQLTokenTypes.NAMESPACE_ALIAS) {
				throw new MariusQLException();
			} else {
				MClass referencedClass = (MClass) fromElementAlreadyAdded.getCategory();
				finalPathProperty = (MProperty) referencedClass.getDefinedDescription(pathPropNode.getText());

			}
			finalPathProperty.setCurrentContext(fromElementAlreadyAdded.getCategory());
			if (!finalPathProperty.isDefined()) {
				throw new MariusQLException(
						"The property '" + finalPathProperty.getName() + "' is not defined on the range of the class "
								+ fromElementAlreadyAdded.getCategory().getName());
			}
			pathPropNode.setType(PostgresSQLTokenTypes.COLUMN);
			pathPropNode.setText(finalPathProperty.toSQL());
			((IdentNode) pathPropNode).setDescription(finalPathProperty);

			return pathPropNode;

		}

	}

	@Override
	public DatatypeEnum getDataType() {
		throw new NotSupportedException();
	}

}
