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

import java.util.ArrayList;
import java.util.List;

import antlr.SemanticException;
import antlr.collections.AST;
import fr.ensma.lias.mariusql.core.Category;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.core.Description;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.metamodel.MMAttribute;
import fr.ensma.lias.mariusql.core.model.MClass;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.driver.postgresql.antlr.PostgresSQLTokenTypes;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLTokenTypes;
import fr.ensma.lias.mariusql.engine.tree.dql.FromClause;
import fr.ensma.lias.mariusql.engine.tree.dql.FromElement;
import fr.ensma.lias.mariusql.engine.tree.dql.FromReferenceNode;
import fr.ensma.lias.mariusql.engine.util.ASTUtil;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.util.MariusQLHelper;

/**
 * Represent an identifier node. It may be a reference to a from element, a
 * property or an (multilingual)attribute.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 * @author Adel GHAMNIA
 * @author Valentin CASSAIR
 * @author Ghada TRIKI
 */
public class IdentNode extends FromReferenceNode implements ResolvableNode {

	private static final long serialVersionUID = 6396339995179724201L;

	/**
	 * Constant for property.
	 */
	private static final int PROPERTY = 1;

	/**
	 * Constant for attribute.
	 */
	private static final int ATTRIBUTE = 2;

	/**
	 * Natural language of this node (if meaningfull).
	 **/
	protected String lgCode;

	/**
	 * Constant for alias ref.
	 */
	private static final int ALIAS_REF = 0;

	/**
	 * Namespace of this description
	 */
	protected String namespaceAlias = null;

	/**
	 * The property/attribute corresponding to this node. null if this node is a
	 * reference to a from element
	 */
	protected Description description;

	/**
	 * The kind of ident node : a reference to a from element, a property or an
	 * (multilingual)attribute.
	 */
	protected int typeOfIdentNode;

	/**
	 * True if this node must be added in the projection list.
	 */
	private boolean toAddInProjectionList = true;

	/**
	 * @return Returns the toAddInProjectionList.
	 */
	public final boolean isToAddInProjectionList() {
		return toAddInProjectionList;
	}

	/**
	 * @param toAddInProjectionList The toAddInProjectionList to set.
	 */
	public final void setToAddInProjectionList(final boolean toAddInProjectionList) {
		this.toAddInProjectionList = toAddInProjectionList;
	}

	@Override
	public AST resolve(AST prefix) throws SemanticException {
		FromClause currentFromClause = getWalker().getCurrentFromClause();
		if (prefix != null) {
			locateFromElementWithPrefix(prefix, currentFromClause);
		} else {
			locateFromElementWithoutPrefix(currentFromClause);
		}

		this.setType(PostgresSQLTokenTypes.COLUMN);
		this.setText(getSQL());

		return this;
	}

	/**
	 * Get the SQL or JPQL conversion of this node.
	 * 
	 * @return the JPQL conversion of this node as a string
	 */
	public String getSQL() {
		FromElement currentFromElement = getFromElement();
		Category currentCategory = currentFromElement.getCategory();
		return description.toSQL(currentCategory);
	}

	/**
	 * Translate this node in SQL.
	 * 
	 * @return The first and last node result of the translation
	 * @throws SemanticException if this node can not be translated
	 */
	public List<IdentNode> translateToSQL() throws SemanticException {
		// The result is the first and last identNode result of the translation
		List<IdentNode> res = new ArrayList<IdentNode>(2);

		// default, result = this node
		res.add(this);
		res.add(this);
		this.setType(PostgresSQLTokenTypes.COLUMN);
		this.setText(getSQL());

		return res;
	}

	/**
	 * Locate the from element corresponding to this IdentNode.
	 * 
	 * @param currentFromClause the from clause
	 */
	private void locateFromElementWithoutPrefix(FromClause currentFromClause) throws SemanticException {
		// The result may be from the current from clause or from an upper from clause.
		List<FromElement> fromElements = currentFromClause.getFromElements();
		currentFromClause = currentFromClause.getParentFromClause();

		while (currentFromClause != null) {
			fromElements.addAll(currentFromClause.getFromElements());
			currentFromClause = currentFromClause.getParentFromClause();
		}

		FromElement currentFromElement = null;
		String identifier = MariusQLHelper.getEntityIdentifier(getText());
		boolean isFound = false;
		for (int i = 0; i < fromElements.size(); i++) {
			currentFromElement = fromElements.get(i);
			Category category = currentFromElement.getCategory();
			if (category != null) {
				description = category.getDefinedDescription(identifier);
				if (description != null) {
					isFound = true;

					if (category.isClass()) {
						// TODO : MBaron
//			boolean exists = ((MClass) category)
//				.isUsedPropertyExists(description.getName());
						boolean exists = ((MClass) category).isDefinedPropertyExists(description.getName());
						if (!exists) {
							throw new MariusQLException(identifier + " is not a defined property.");
						}
					}

					// Check that the from element has not already been found in this case the query
					// is ambiguous.
					if (getFromElement() != null) {
						break;
					} else {
						this.setFromElement(currentFromElement);
						this.setDescription(description);
					}

				}
			}
		}

		if (!isFound) {
			throw new MariusQLException("Attribute/Property is not defined: " + identifier);
		}

		if (this.getFromElement() == null) {
			throw new MariusQLException("'" + this.getText() + "' is not defined in the context of the from clause");
		}
	}

	/**
	 * Locate the from element corresponding to this ident node given a prefix
	 * 
	 * @param prefix            a given prefix
	 * @param currentFromClause the from clause
	 */
	private void locateFromElementWithPrefix(final AST prefix, FromClause currentFromClause) throws SemanticException {
		FromElement currentFromElement;
		String prefixText = prefix.getText();
		currentFromElement = currentFromClause.getFromElement(prefixText);
		if (currentFromElement == null) {
			throw new MariusQLException("The prefix '" + prefixText + "' is not defined in the from clause");
		} else {
			setFromElement(currentFromElement);
			isDefined();
		}
	}

	private void isDefined() throws SemanticException {
		isDefined(getFromElement());
	}

	private void isDefined(FromElement contextFromElement) throws SemanticException {

		Category contextCategory = contextFromElement.getCategory();

		try {
			loadDescription(contextCategory);
		} catch (SemanticException oExc) {
			MClass auxClass = (MClass) contextCategory;
			if (auxClass == null || auxClass.getDefinedDescription(getText()) == null) {
				thowDescriptionNotDefinedException(description.getName(), contextCategory);
			}
		}
	}

	private void thowDescriptionNotDefinedException(String nameDescription, Category contextCategory)
			throws SemanticException {
		loadDataForException();
		String prefixText = contextCategory.getCategoryAlias();
		if (prefixText == null) {
			prefixText = contextCategory.getName();
		}
		throw new SemanticException("The " + descriptionType + " '" + nameDescription + "' is not defined on the "
				+ descriptionContextType + " '" + prefixText + "'");

	}

	/**
	 * String property of attribute used for message of error.
	 */
	private String descriptionType;

	/**
	 * String class of entity used for message of error.
	 */
	private String descriptionContextType;

	private void loadDataForException() {
		if (this.description.isProperty()) {
			descriptionType = "property";
			descriptionContextType = "class";
		} else {
			descriptionType = "attribute";
			descriptionContextType = "entity";
		}

	}

	@Override
	public DatatypeEnum getDataType() {
		return description.getRange().getDatatypeEnum();
	}

	@Override
	public String getLabel() {
		return (description == null) ? getText() : description.toString();
	}

	public void setNamespaceAlias(String namespaceAlias) {
		this.namespaceAlias = namespaceAlias;
	}

	/**
	 * @param lgCode The lgCode to set.
	 */
	public void setLgCode(String lgCode) {
		this.lgCode = lgCode;
	}

	public String getLgCode() {
		return lgCode;
	}

	/**
	 * @param description the property / attribut to set.
	 */
	public void setDescription(final Description description) {
		this.description = description;

		if (this.description.isMultilingualDescription()) {
			if (this.lgCode == null) {
				this.lgCode = getSession().getReferenceLanguage();
			}
			description.setLgCode(this.lgCode);
		}

		if (this.description.isAttribute()) {
			this.setAttribute();
		} else {
			this.setProperty();
		}
	}

	public Description loadDescription(Category category) throws SemanticException {
		Description res = null;
		String identifier = null;
		if (MariusQLHelper.isMetaModelElement(getText())) {
			identifier = MariusQLHelper.getEntityIdentifier(getText());
		} else {
			identifier = this.getText();
		}

		res = category.getDefinedDescription(identifier);

		if (res == null) {
			throw new MariusQLException(
					(category.isEntity() ? "Attribute" : "Property") + " is not defined: " + identifier);
		}

		this.setDescription(res);

		return res;
	}

	/**
	 * Set the type of this node as a property.
	 */
	public void setProperty() {
		typeOfIdentNode = PROPERTY;
	}

	/**
	 * Set the type of this node as an attribute.
	 */
	public void setAttribute() {
		typeOfIdentNode = ATTRIBUTE;
	}

	public Description getDescription() {
		return description;
	}

	/**
	 * Translate this node into SQL.
	 * 
	 * @param prefix                 reference to its from clause element
	 * @param locateFromClauseNeeded True if we need to search its from clause
	 *                               element
	 * @param inPath                 True if this node is resolved as part of a
	 *                               DotNode (path expression)
	 * @return the resolved noded
	 * @throws SemanticException if this node is not defined in the From clause
	 */
	public final AST resolve(final AST prefix, final boolean locateFromClauseNeeded, final boolean inPath)
			throws SemanticException {
		AST firstChild = getFirstChild();

		if (firstChild != null && firstChild.getType() == MariusQLTokenTypes.NAMESPACE_ALIAS) {
			setNamespaceAlias(firstChild.getText());
		}

		if (getWalker().getCurrentFromClause().isFromElementAlias(getText())) {
			resolveAliasFromElement(prefix);
			return this;
		}

		if (locateFromClauseNeeded) {
			locateFromElement(prefix);
		} else {
			isDefined();
		}

		if (this.description.isAttribute()) {
			if (this.description != null) {
				if (this.description.isMultilingualDescription()) {

					if (this.getSession().isNoReferenceLanguage() && lgCode == null) {
						throw new MariusQLException(
								"Language must be defined for a multilanguage String type attribute: "
										+ this.description.getName());
					} else if (!this.getSession().isNoReferenceLanguage() && lgCode == null) {
						lgCode = getSession().getReferenceLanguage();
					}
				} else {
					if (lgCode != null) {
						throw new MariusQLException(
								"Attribute " + this.description.getName() + " doesn't support multilanguage String.");
					}
				}
			} else {
				throw new MariusQLException("The description is nul");
			}
		} else {
			if (this.description.getRange().isCollectionAssociationType() && getWalker().isSelectStatement()
					&& getWalker().isInSelect()) {
				this.resolveCollectionProperty();
				return this;
			} else if (this.description.getRange().isAssociationType() && getWalker().isSelectStatement() && !inPath
					&& !getWalker().isInSelect()) {
				this.resolveAssociationProperty();
				return this;
			}
		}
		// Now if the attribut is not defined on this entity in the
		// model encoded in the odbd, processing must be done.
		if (inPath && description instanceof MMAttribute && !description.getRange().isCollectionAssociationType()
				&& !getWalker().isInFrom()) {
			throw new MariusQLException();
		}

		setType(PostgresSQLTokenTypes.COLUMN);
		setText(getSQL());

		return this;

	}

	/**
	 * Manage this node to take into in account, specific columns for collection
	 * type.
	 * 
	 * @throws SemanticException
	 */
	private void resolveCollectionProperty() throws SemanticException {
		String text = getSQL();
		setText(text);
		setType(PostgresSQLTokenTypes.COLUMN);
	}

	/**
	 * @param prefix
	 */
	public void resolveAliasFromElement(AST prefix) {
		typeOfIdentNode = ALIAS_REF;

		if (this.description.isProperty()) {
			this.setText(getSQL());
			setType(PostgresSQLTokenTypes.COLUMN);
		} else if (this.description.isAttribute()) {
			throw new MariusQLException("The description is an attribute");
		}
	}

	/**
	 * Check and locate the element of definition of this node in the from clause.
	 * 
	 * @param prefix prefix of this node
	 * @throws SemanticException if the from element can not be located
	 */
	public final void locateFromElement(final AST prefix) throws SemanticException {
		FromClause currentFromClause = getWalker().getCurrentFromClause();

		if (prefix != null) {
			locateFromElementWithPrefix(prefix, currentFromClause);
		} else {
			locateFromElementWithoutPrefix(currentFromClause);
		}
	}

	/**
	 * Manage this node to take into in account, specific columns for reference
	 * type.
	 * 
	 * @throws SemanticException
	 */
	private void resolveAssociationProperty() throws SemanticException {
		// search if the range of this property has already been added
		// in the from clause
		FromElement fromElementAlreadyAdded = getWalker().getGeneratedFromElement((MProperty) description);
		if (fromElementAlreadyAdded == null) {
			// the range hasn't already been added
			fromElementAlreadyAdded = getWalker().addImplicitJoin(getFromElement(), this, true);
		}
		MClass aClass = (MClass) fromElementAlreadyAdded.getCategory();
		MProperty propRid = FactoryCore.createNewMRidProperty(getSession(), aClass);
		propRid.setCurrentContext(aClass);
		setText(propRid.toSQL());
		setType(PostgresSQLTokenTypes.COLUMN);

		if (getWalker().isInSelect()) {
			IdentNode typeOfNode = getWalker().createPropertyTypeOfNode(fromElementAlreadyAdded);
			typeOfNode.translateToSQL();
			ASTUtil.appendSibling(this, typeOfNode);

			// Create all the properties nodes
			List<IdentNode> propertiesNodes = getWalker().createDescriptionsNodes(fromElementAlreadyAdded, false);
			// the first property is the next sibling of the node typeof
			typeOfNode.setNextSibling(propertiesNodes.get(0));
		}
	}
}
