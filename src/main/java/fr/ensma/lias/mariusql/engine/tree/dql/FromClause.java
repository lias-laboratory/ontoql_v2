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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import antlr.ASTFactory;
import antlr.SemanticException;
import antlr.collections.AST;
import fr.ensma.lias.mariusql.core.Category;
import fr.ensma.lias.mariusql.core.DatatypeCollection;
import fr.ensma.lias.mariusql.core.DatatypeReference;
import fr.ensma.lias.mariusql.core.model.MClass;
import fr.ensma.lias.mariusql.core.model.MGenericClass;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.driver.postgresql.antlr.PostgresSQLTokenTypes;
import fr.ensma.lias.mariusql.engine.MariusQLSQLWalkerNode;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLTokenTypes;
import fr.ensma.lias.mariusql.engine.tree.DisplayableNode;
import fr.ensma.lias.mariusql.engine.util.ASTUtil;

/**
 * Represents the 'FROM' part of a query or subquery.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 * @author Adel GHAMNIA
 */
public class FromClause extends MariusQLSQLWalkerNode implements DisplayableNode {

	private static final long serialVersionUID = 4609323434113663252L;

	/**
	 * List of from elements of this from clause.
	 */
	private Set<FromElement> fromElements;

	/**
	 * Map beetween alias and from element.
	 */
	private Map<String, FromElement> fromElementByClassAlias;

	/**
	 * Pointer to the parent FROM clause, if there is one.
	 */
	private FromClause parentFromClause;

	/**
	 * Counts the from elements as they are added.
	 */
	private int fromElementCounter = 0;

	/**
	 * Root level of nested query.
	 */
	public static final int ROOT_LEVEL = 1;

	/**
	 * Level of nested query.
	 */
	private int level = ROOT_LEVEL;

	public FromClause() {
		fromElements = new HashSet<FromElement>();
		fromElementByClassAlias = new HashMap<String, FromElement>();
	}

	/**
	 * Add a from element to this from clause.
	 * 
	 * @param path     from element to add
	 * @param star     node corresponding to the polymorphic operator
	 * @param alias    alias of this from element
	 * @param genAlias true if we must generate an alias
	 * @return the from element added
	 * @throws SemanticException if a semantic error is detected
	 */
	public FromElement addFromElement(final AST node, final AST star, final AST alias, final boolean genAlias,
			boolean isDml) throws SemanticException {
		String path = node.getText();
		String namespaceAlias = null;
		AST firstChild = node.getFirstChild();
		if (firstChild != null) {
			namespaceAlias = firstChild.getText();
		}
		// The path may be a reference to an alias defined in the parent query.
		String classAlias = (alias == null) ? path : alias.getText();
		checkForDuplicateClassAlias(classAlias);

		FromElement fromElement = new FromElement(path, namespaceAlias, classAlias, star, genAlias, this.getWalker(),
				this, isDml);
		registerFromElement(fromElement);
		fromElement.setFromClause(this);
		return fromElement;
	}

	/**
	 * Check if the alias is already used.
	 * 
	 * @param classAlias the alias to test
	 * @throws SemanticException if the alias exists
	 */
	private void checkForDuplicateClassAlias(final String classAlias) throws SemanticException {
		if (classAlias != null && fromElementByClassAlias.containsKey(classAlias)) {
			throw new SemanticException("Duplicate definition of alias '" + classAlias + "'");
		}
	}

	/**
	 * Register a from element.
	 * 
	 * @param element the from element to register
	 */
	public void registerFromElement(final FromElement element) {
		fromElements.add(element);
		element.setFromClause(this);
		if (element.getCategory() != null) {
			String classAlias = element.getCategory().getCategoryAlias();
			if (classAlias != null) {
				fromElementByClassAlias.put(classAlias, element);
			}
		}
	}

	/**
	 * Set a from clause as the parent of this one.
	 * 
	 * @param parentFromClause the parent from clause of this from clause
	 */
	public void setParentFromClause(final FromClause parentFromClause) {
		this.parentFromClause = parentFromClause;
		if (parentFromClause != null && !(parentFromClause instanceof FromClause)) {
			level = parentFromClause.getLevel() + 1;
			parentFromClause.addChild(this);
		}
	}

	/**
	 * Gives a string representation of this node.
	 * 
	 * @return a string representing this node
	 * @see DisplayableNode#getDisplayText()
	 */
	public String getDisplayText() {
		return "FromClause{" + "level=" + level + ", fromElementCounter=" + fromElementCounter + ", fromElements="
				+ getFromElements().size() + "}";
	}

	/**
	 * Returns the list of from elements in order.
	 * 
	 * @return the list of from elements (instances of FromElement).
	 */
	public List<FromElement> getFromElements() {
		return ASTUtil.collectDirectChildren(this, FromElement.class);
	}

	/**
	 * @return the level of nesting of this from clause
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return a string representation of this object
	 * @see Object#toString()
	 */
	public String toString() {
		return "FromClause{" + "level=" + level + "}";
	}

	/**
	 * @return the parent from clause
	 */
	public final FromClause getParentFromClause() {
		return parentFromClause;
	}

	/**
	 * Retreives the from element represented by the given alias.
	 * 
	 * @param aliasOrClassName The alias by which to locate the from-element.
	 * @return The from element assigned the given alias, or null if none.
	 */
	public final FromElement getFromElement(final String aliasOrClassName) {
		FromElement fromElement = (FromElement) fromElementByClassAlias.get(aliasOrClassName);
		if (fromElement == null && parentFromClause != null) {
			fromElement = parentFromClause.getFromElement(aliasOrClassName);
		}

		// if aliasOrName is the name of a property, which is a class reference
		// the fromElement becomes the class referenced by this property
		if (fromElement != null) {
			Category cat = fromElement.getCategory();
			if (cat.isClass()) {
				MProperty property = ((MClass) cat).getUsedProperty(aliasOrClassName);
				if (property == null)
					return fromElement;
				else {
					final MGenericClass rangeClass;
					if (property.getRange().isCollectionAssociationType()) {
						rangeClass = (MGenericClass) ((DatatypeReference) ((DatatypeCollection) property.getRange())
								.getDatatype()).getCategory();
						FromElement rangeFromElement = new FromElement(rangeClass, rangeClass.isPolymorph(),
								getWalker());
						fromElementByClassAlias.put(aliasOrClassName, rangeFromElement);
						return rangeFromElement;
					} else if (property.getRange().isAssociationType()) {
						rangeClass = (MGenericClass) ((DatatypeReference) property.getRange()).getCategory();
						FromElement rangeFromElement = new FromElement(rangeClass, rangeClass.isPolymorph(),
								getWalker());
						fromElementByClassAlias.put(aliasOrClassName, rangeFromElement);
						return rangeFromElement;
					}
				}
			}
		}

		return fromElement;
	}

	/**
	 * Convenience method to check whether a given token represents a from element
	 * alias.
	 * 
	 * @param possibleAlias The potential from-element alias to check.
	 * @return True if the possibleAlias is an alias to a from-element visible from
	 *         this point in the query graph.
	 */
	public final boolean isFromElementAlias(final String possibleAlias) {
		boolean isAlias = fromElementByClassAlias.containsKey(possibleAlias);
		if (!isAlias && parentFromClause != null) {
			// try the parent FromClause...
			isAlias = parentFromClause.isFromElementAlias(possibleAlias);
		}
		return isAlias;
	}

	/**
	 * Add a AND condition in the Where condition of this from clause. If the Where
	 * clause doesn't exist, it's created.
	 * 
	 * @param condition a condition to add in the where clause
	 */
	public void addWhereCondition(String condition) {
		// A node factory is required
		ASTFactory astFactory = getWalker().getASTFactory();
		AST joinCondition = ASTUtil.create(astFactory, PostgresSQLTokenTypes.JOIN_CONDITION, condition);
		AST whereClause = this.getNextSibling();
		if (whereClause == null || whereClause.getType() != MariusQLTokenTypes.WHERE) {
			// The where clause doesn't exist
			whereClause = ASTUtil.create(getWalker().getASTFactory(), PostgresSQLTokenTypes.WHERE, "where");
			whereClause.addChild(joinCondition);
			ASTUtil.appendSibling(this, whereClause);
		} else {
			// the where clause exist, a AND condition is added
			AST andClause = ASTUtil.create(getWalker().getASTFactory(), PostgresSQLTokenTypes.AND, "and");
			andClause.addChild(joinCondition);
			andClause.addChild(whereClause.getFirstChild());
			whereClause.setFirstChild(andClause);
		}
	}
}
