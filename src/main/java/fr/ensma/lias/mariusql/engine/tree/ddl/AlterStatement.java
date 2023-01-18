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

import antlr.SemanticException;
import antlr.collections.AST;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.metamodel.MMAttribute;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.core.model.MClass;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.engine.MariusQLSQLWalkerNode;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLSQLTokenTypes;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLTokenTypes;
import fr.ensma.lias.mariusql.engine.tree.IdentNode;
import fr.ensma.lias.mariusql.engine.tree.Statement;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.util.MariusQLHelper;

/**
 * @author Ghada TRIKI
 * @author Florian MHUN
 */
public class AlterStatement extends MariusQLSQLWalkerNode implements Statement {

	private static final long serialVersionUID = 5807459462215293920L;

	@Override
	public int getStatementType() {
		return MariusQLSQLTokenTypes.ALTER;
	}

	public void process() throws SemanticException {

		AST entityTypeNode = getFirstChild();

		if (entityTypeNode.getType() == MariusQLTokenTypes.ENTITY) {
			processAlterEntity(entityTypeNode.getFirstChild());
		} else if (entityTypeNode.getType() == MariusQLTokenTypes.ONTOLOGY_MODEL_ID) {
			processAlterModelElement(entityTypeNode);
		} else if (entityTypeNode.getType() == MariusQLTokenTypes.EXTENT) {
			processAlterExtent(entityTypeNode.getFirstChild());
		} else {
			throw new MariusQLException("The type is not supported");
		}

	}

	private void processAlterExtent(AST classNode) throws SemanticException {
		AST actionNode = classNode.getNextSibling();
		MClass aClass = FactoryCore.createExistingMClass(this.getSession(), classNode.getText());

		AST currentPropertyNode = actionNode.getNextSibling();

		if (MariusQLSQLTokenTypes.ADD == actionNode.getType()) {
			List<MProperty> addedProperties = new ArrayList<MProperty>();
			while (currentPropertyNode != null) {
				MProperty currentProperty = (MProperty) ((IdentNode) currentPropertyNode).loadDescription(aClass);
				addedProperties.add(currentProperty);
				currentPropertyNode = currentPropertyNode.getNextSibling();
			}

			aClass.addNewUsedProperties(addedProperties);

			this.getWalker().setRowCount(addedProperties.size());

		} else if (MariusQLSQLTokenTypes.DROP == actionNode.getType()) {
			MProperty currentProperty = null;
			if (currentPropertyNode != null) {
				currentProperty = (MProperty) ((IdentNode) currentPropertyNode).loadDescription(aClass);
				currentPropertyNode = currentPropertyNode.getNextSibling();
			}

			aClass.removeUsedProperty(currentProperty.getName());

			this.getWalker().setRowCount(1);
		} else {
			throw new MariusQLException("The type is not supported");
		}
	}

	private void processAlterEntity(AST nodeNameEntity) {
		String entityName = MariusQLHelper.getEntityIdentifier(nodeNameEntity.getText());
		MMEntity entity = FactoryCore.createExistingMMEntity(getWalker().getSession(), entityName);

		AST actionNode = nodeNameEntity.getNextSibling();
		if (actionNode.getType() == MariusQLSQLTokenTypes.DROP) {
			String propertyName = actionNode.getNextSibling().getText();
			MMAttribute attributeDropped = entity
					.getDefinedMMAttribute(MariusQLHelper.getEntityIdentifier(propertyName));
			if (attributeDropped == null) {
				throw new MariusQLException(
						"The attribute " + propertyName + " is not defined on the ENTITY " + entity.getName());
			} else {
				entity.removeDefinedAttribute(attributeDropped);
				this.getWalker().setRowCount(entity.update());
			}
		} else if (actionNode.getType() == MariusQLSQLTokenTypes.ADD) {
			DescriptionDefinitionNode attributeNode = (DescriptionDefinitionNode) actionNode.getNextSibling();
			attributeNode.getAttributeDefined(entity);
			this.getWalker().setRowCount(entity.update());
		} else {
			throw new MariusQLException("The type is not supported");
		}

	}

	private void processAlterModelElement(AST nodeNameClass) {
		AST instanceEntityIdentifierNode = nodeNameClass.getFirstChild();
		final String identifier = MariusQLHelper.getEntityIdentifier(instanceEntityIdentifierNode.getText());

		MClass klass = FactoryCore.createExistingMClass(getSession(), identifier);

		AST actionNode = instanceEntityIdentifierNode.getNextSibling();
		if (actionNode.getType() == MariusQLSQLTokenTypes.DROP) {
			String propertyName = actionNode.getNextSibling().getText();
			klass.removeDefinedProperty(propertyName);
		} else if (actionNode.getType() == MariusQLSQLTokenTypes.ADD) {
			DescriptionDefinitionNode attributeNode = (DescriptionDefinitionNode) actionNode.getNextSibling();

			MProperty newProperty = attributeNode.getPropertyDefined(klass);
			klass.addDefinedProperty(newProperty);

			this.getWalker().setRowCount(klass.update());
		} else {
			throw new MariusQLException("The type is not supported");
		}
	}
}
