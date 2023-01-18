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
package fr.ensma.lias.mariusql.engine.tree.dml;

import java.util.ArrayList;
import java.util.List;

import antlr.SemanticException;
import antlr.collections.AST;
import fr.ensma.lias.mariusql.core.Category;
import fr.ensma.lias.mariusql.core.model.MClass;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.engine.MariusQLSQLWalkerNode;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLSQLTokenTypes;
import fr.ensma.lias.mariusql.engine.tree.IdentNode;
import fr.ensma.lias.mariusql.engine.tree.Statement;
import fr.ensma.lias.mariusql.engine.tree.dql.FromClause;
import fr.ensma.lias.mariusql.engine.tree.dql.FromElement;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.util.MariusQLHelper;

/**
 * @author Ghada TRIKI
 */
public class DeleteStatement extends MariusQLSQLWalkerNode implements Statement {

	private static final long serialVersionUID = -5087465877918877607L;

	@Override
	public int getStatementType() {
		return MariusQLSQLTokenTypes.DELETE;
	}

	public void process() throws SemanticException {
		AST entityTypeNode = getFirstChild();

		if (entityTypeNode.getNextSibling() != null) {
			if (entityTypeNode.getNextSibling().getType() == MariusQLSQLTokenTypes.WHERE) {
				processDeleteWhere(entityTypeNode);
			} else {
				throw new MariusQLException("Error in the query");
			}
		} else {
			processDelete(entityTypeNode);
		}
	}

	private void processDeleteWhere(AST classNode) {
		Category category = getFromElement().getCategory();
		if (!category.isClass()) {
			throw new MariusQLException("Is not a class.");
		}

		AST nextNode = classNode.getNextSibling().getFirstChild();
		List<String> properties = new ArrayList<String>();
		List<String> valuesProperties = new ArrayList<String>();
		int type;
		AST value;
		String newValue = null;

		if (nextNode.getType() != MariusQLSQLTokenTypes.AND) {
			IdentNode currentProperty = (IdentNode) nextNode.getFirstChild();

			boolean exists = ((MClass) category).isUsedPropertyExists(currentProperty.getLabel());
			if (!exists) {
				throw new MariusQLException(currentProperty.getLabel() + " is not a used property.");
			}
			properties.add(currentProperty.getText());
			type = currentProperty.getNextSibling().getType();
			value = currentProperty.getNextSibling();

			if (type == MariusQLSQLTokenTypes.TRUE || type == MariusQLSQLTokenTypes.FALSE) {
				if (value.getText().equalsIgnoreCase("true")) {
					newValue = this.getSession().getMariusQLTypes().getTrue();
				} else if (value.getText().equalsIgnoreCase("false")) {
					newValue = this.getSession().getMariusQLTypes().getFalse();
				}
				value.setText(newValue);
			}

			valuesProperties.add(value.getText());
		} else {
			nextNode = nextNode.getFirstChild();
			while (nextNode != null) {
				if (nextNode.getType() == MariusQLSQLTokenTypes.AND) {
					properties.add(nextNode.getNextSibling().getFirstChild().getText());

					type = nextNode.getNextSibling().getFirstChild().getNextSibling().getType();
					value = nextNode.getNextSibling().getFirstChild().getNextSibling();

					if (type == MariusQLSQLTokenTypes.TRUE || type == MariusQLSQLTokenTypes.FALSE) {
						if (value.getText().equalsIgnoreCase("true")) {
							newValue = this.getSession().getMariusQLTypes().getTrue();
						} else if (value.getText().equalsIgnoreCase("false")) {
							newValue = this.getSession().getMariusQLTypes().getFalse();
						}
						value.setText(newValue);
					}

					valuesProperties.add(value.getText());
					nextNode = nextNode.getFirstChild();
				} else {
					properties.add(nextNode.getFirstChild().getText());

					type = nextNode.getFirstChild().getNextSibling().getType();
					value = nextNode.getFirstChild().getNextSibling();

					if (type == MariusQLSQLTokenTypes.TRUE || type == MariusQLSQLTokenTypes.FALSE) {
						if (value.getText().equalsIgnoreCase("true")) {
							newValue = this.getSession().getMariusQLTypes().getTrue();
						} else if (value.getText().equalsIgnoreCase("false")) {
							newValue = this.getSession().getMariusQLTypes().getFalse();
						}
						value.setText(newValue);
					}
					valuesProperties.add(value.getText());
					nextNode = nextNode.getNextSibling();
				}
			}

		}

		MClass mClass = (MClass) category;
		this.getFromElement().setText(mClass.toSQLWithAlias());
		List<MProperty> referencedProperties = this.getSession().getMariusQLDQL()
				.getPropertyWithReferenceByOnClass(mClass.getInternalId());
		List<MProperty> referencedCollectionProperties = this.getSession().getMariusQLDQL()
				.getPropertyWithCollectionOfReferenceByOnClass(mClass.getInternalId());
		List<Long> rids = this.getSession().getMariusQLDQL().getDataRid(mClass.toSQLWithAlias(), properties,
				valuesProperties);

		for (MProperty refProperty : referencedProperties) {
			for (Long rid : rids) {
				if (this.getSession().getMariusQLDQL().isReferencedDataByPropertyWithCondition(
						refProperty.getScope().toSQL(), refProperty.toSQL(), rid)) {
					throw new MariusQLException("You can not remove a referenced data.");
				}
			}
		}

		for (MProperty collectionProperty : referencedCollectionProperties) {
			List<String> collectionRows = this.getSession().getMariusQLDQL()
					.getReferencedDataCollectionByPropertyWithCondition(collectionProperty.getScope().toSQL(),
							collectionProperty.toSQL());
			for (String collectionRow : collectionRows) {
				List<Long> collectionValues = MariusQLHelper.getCollectionAssociationValues(collectionRow);
				for (Long rid : rids) {
					if (collectionValues.contains(rid)) {
						throw new MariusQLException("You can not remove a referenced data.");
					}
				}
			}
		}
	}

	private void processDelete(AST classNode) {
		Category category = getFromElement().getCategory();
		if (!category.isClass()) {
			throw new MariusQLException("Is not a class.");
		}

		if (category.isClass() && !category.isAbstract()) {
			if (((MClass) category).isReferencedByOtherUsedProperties()) {
				throw new MariusQLException("You can not remove a referenced data.");
			}
		}
	}

	/**
	 * Retrieve this delete statement's single from-element.
	 * 
	 * @return The from element
	 */
	public FromElement getFromElement() {
		return ((FromClause) this.getFirstChild()).getFromElements().get(0);
	}

}
