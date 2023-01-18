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
package fr.ensma.lias.mariusql.engine.tree.ddl;

import java.util.ArrayList;
import java.util.List;

import antlr.collections.AST;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.Datatype;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.core.Description;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.metamodel.MMAttribute;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.core.model.MClass;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.engine.MariusQLSQLWalkerNode;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLSQLTokenTypes;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotYetImplementedException;
import fr.ensma.lias.mariusql.util.MariusQLHelper;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * Defines an AST node representing an OntoQL descriptor clause.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 * @author Ghada TRIKI
 * @author Florian MHUN
 */
public class DescriptionDefinitionNode extends MariusQLSQLWalkerNode {

	private static final long serialVersionUID = 3361573839927591475L;

	/**
	 * Attribute defined by this node.
	 */
	private Description descriptionDefined = null;

	/**
	 * Get the attribute defined by this node.
	 * 
	 * @return the attribute defined by this node.
	 */
	public MMAttribute getAttributeDefined(MMEntity scopeAttribute) {
		if (descriptionDefined == null) {
			this.loadAttributeDefined(scopeAttribute);
		}
		return (MMAttribute) this.descriptionDefined;
	}

	/**
	 * Get the property defined by this node.
	 * 
	 * @param descriptionScope
	 * @return
	 */
	public MProperty getPropertyDefined(MClass mClass) {
		if (descriptionDefined == null) {
			this.loadPropertyDefined(mClass);
		}
		return (MProperty) this.descriptionDefined;
	}

	/**
	 * Load the property defined by this node.
	 * 
	 * @param mClass
	 */
	private void loadPropertyDefined(MClass scopeProperty) {
		AST idPropertyNode = getFirstChild();
		AST datatypePropertyNode = idPropertyNode.getNextSibling();
		AST descriptorNode = datatypePropertyNode.getNextSibling();

		String idProperty = idPropertyNode.getText();
		Datatype datatypeAttribute = getPropertyDatatype(datatypePropertyNode, scopeProperty);

		MMEntity propertyEntity = FactoryCore.createExistingMMEntity(getSession(),
				MariusQLConstants.PROPERTY_CORE_ENTITY_NAME);

		descriptionDefined = FactoryCore.createNewMProperty(this.getSession(), propertyEntity, idProperty,
				scopeProperty);

		descriptionDefined.setRange(datatypeAttribute);

		if (descriptorNode != null) {
			DescriptorClause descriptorClause = (DescriptorClause) descriptorNode;
			descriptorClause.setDescriptor((MProperty) descriptionDefined, propertyEntity);
		}
	}

	private Datatype getPropertyDatatype(AST nodeDatatype, MClass categoryDefined) {
		Datatype res = null;

		AST firstChild = nodeDatatype.getFirstChild();
		if (nodeDatatype.getType() == MariusQLSQLTokenTypes.REF) {
			res = FactoryCore.createNewMDatatypeReference(this.getSession(),
					MariusQLHelper.getEntityIdentifier(firstChild.getText()));
		} else if (nodeDatatype.getType() == MariusQLSQLTokenTypes.ARRAY_DEF) {
			Datatype collectionDatatype = this.getPropertyDatatype(firstChild, categoryDefined);
			res = FactoryCore.createNewMDatatypeCollection(this.getSession(), collectionDatatype);
		} else {
			String nameDatatype = nodeDatatype.getText();
			final DatatypeEnum datatypeEnum = MariusQLHelper.getDatatypeEnum(nameDatatype);

			switch (datatypeEnum) {
			case DATATYPEENUMERATE: {
				if (firstChild != null) {
					List<String> enumValues = new ArrayList<String>();
					AST firstValueNode = firstChild.getFirstChild();

					enumValues.add(StringHelper.removeFirstAndLastletter(firstValueNode.getText()));
					while (firstValueNode.getNextSibling() != null) {
						firstValueNode = firstValueNode.getNextSibling();
						enumValues.add(StringHelper.removeFirstAndLastletter(firstValueNode.getText()));
					}

					res = FactoryCore.createNewMDatatypeEnumerate(this.getSession(), enumValues);
				} else {
					throw new MariusQLException();
				}

				break;
			}
			case DATATYPECOUNT: {
				return FactoryCore.createNewMDatatypeCount(this.getSession());
			}
			default: {
				res = FactoryCore.createMSimpleDatatype(this.getSession(), datatypeEnum);

				if (firstChild != null) {
					throw new NotYetImplementedException();
				}
			}
			}
		}
		return res;
	}

	/**
	 * Load the attribute defined by this node.
	 * 
	 * @param scopeAttribute
	 */
	private void loadAttributeDefined(MMEntity scopeAttribute) {
		AST nameAttributeNode = getFirstChild();
		AST datatypeAttributeNode = nameAttributeNode.getNextSibling();
		String nameAttribute = MariusQLHelper.getEntityIdentifier(nameAttributeNode.getText());

		MMAttribute aMapAttribute = new MMAttribute(this.getSession(), nameAttribute, scopeAttribute);
		Datatype datatypeAttribute = getAttributeDatatype(datatypeAttributeNode, scopeAttribute);

		// For the moment, we handle only optional attribute
		aMapAttribute.setOptional(true);

		descriptionDefined = aMapAttribute;
		descriptionDefined.setRange(datatypeAttribute);
	}

	private Datatype getAttributeDatatype(AST nodeDatatype, MMEntity categoryDefined) {
		Datatype res = null;

		AST firstChild = nodeDatatype.getFirstChild();

		if (nodeDatatype.getType() == MariusQLSQLTokenTypes.REF) {
			res = FactoryCore.createNewMMDatatypeReference(this.getSession(),
					MariusQLHelper.getEntityIdentifier(firstChild.getText()));
		} else if (nodeDatatype.getType() == MariusQLSQLTokenTypes.ARRAY_DEF) {
			Datatype collectionDatatype = this.getAttributeDatatype(firstChild, categoryDefined);
			res = FactoryCore.createNewMMDatatypeCollection(this.getSession(), collectionDatatype);
		} else {
			String nameDatatype = nodeDatatype.getText();
			res = FactoryCore.createMMSimpleDatatype(this.getSession(), MariusQLHelper.getDatatypeEnum(nameDatatype));
		}

		return res;
	}
}
