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
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.core.model.MClass;
import fr.ensma.lias.mariusql.core.model.MGenericClass;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.engine.MariusQLSQLWalkerNode;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLSQLTokenTypes;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLTokenTypes;
import fr.ensma.lias.mariusql.engine.tree.IdentNode;
import fr.ensma.lias.mariusql.engine.tree.Statement;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotSupportedException;
import fr.ensma.lias.mariusql.util.MariusQLHelper;

/**
 * Defines a top-level AST node representing an MariusQL create statement.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 * @author Florian MHUN
 */
public class CreateStatement extends MariusQLSQLWalkerNode implements Statement {

	private static final long serialVersionUID = -9056425005485577660L;

	/**
	 * Process the creation of the extent of the given class.
	 * 
	 * @param classNode node containing the identifier the class
	 * @throws SemanticException
	 */
	public void processCreateExtent(AST classNode) throws SemanticException {
		if (FactoryCore.isStaticClass(this.getSession(), classNode.getText())) {
			throw new MariusQLException("Can not extent a static class: " + classNode.getText());
		}

		MClass aClass = FactoryCore.createExistingMClass(this.getSession(), classNode.getText());

		AST currentPropertyNode = classNode.getNextSibling();
		List<MProperty> usedProperties = new ArrayList<MProperty>();

		while (currentPropertyNode != null) {
			MProperty currentProperty = (MProperty) ((IdentNode) currentPropertyNode).loadDescription(aClass);
			usedProperties.add(currentProperty);
			currentPropertyNode = currentPropertyNode.getNextSibling();
		}

		aClass.setUsedProperties(usedProperties);
		aClass.create();

		this.getWalker().setRowCount(1);
	}

	/**
	 * Process the creation of a given entity.
	 * 
	 * @param entityNode node containing the name of the new entity
	 */
	public void processCreateMetaModelElement(AST entityNode) {
		final String newEntityName = MariusQLHelper.getEntityIdentifier(entityNode.getText());

		MMEntity entity = null;

		AST nextNode = entityNode.getNextSibling();
		if (nextNode.getType() == MariusQLTokenTypes.UNDER) {
			final String underEntity = MariusQLHelper.getEntityIdentifier(nextNode.getFirstChild().getText());

			MMEntity superEntity = FactoryCore.createExistingMMEntity(this.getSession(), underEntity);
			entity = FactoryCore.createNewMMEntity(this.getSession(), newEntityName);
			entity.setSuperEntity(superEntity);
			nextNode = nextNode.getNextSibling();
		} else {
			MMEntity superEntity = FactoryCore.createExistingMMEntity(this.getSession(),
					MariusQLConstants.STATIC_CLASS_CORE_ENTITY_NAME);
			entity = FactoryCore.createNewMMEntity(this.getSession(), newEntityName);
			entity.setSuperEntity(superEntity);
		}

		if (nextNode != null) {
			// The entity has some attributes definition.
			DescriptionsDefintionClause attributesNode = (DescriptionsDefintionClause) nextNode;
			if (attributesNode != null) {
				// Get the list of attributes.
				attributesNode.setDescriptionScope(entity);
				attributesNode.collectAttributes();
			}
		}

		// Insert element.
		this.getWalker().setRowCount(entity.insert());

		// Create table.
		entity.create();
	}

	/**
	 * Process the creation of a given ontology element for the moment a class or an
	 * a posteriori case of.
	 * 
	 * @param entityTypeNode
	 */
	private void processCreateModelElement(AST entityTypeNode) {
		AST classIdentifierNode = entityTypeNode.getFirstChild();

		MGenericClass klass = null;

		final String entityName = MariusQLHelper.getEntityIdentifier(entityTypeNode.getText());
		final String classIdentifier = classIdentifierNode.getText();

		MMEntity modelEntity = FactoryCore.createExistingMMEntity(this.getSession(), entityName);

		// Create class.
		if (modelEntity.isSubMMEntityOf(MariusQLConstants.CLASS_CORE_ENTITY_NAME)) {
			klass = FactoryCore.createNewMClass(this.getSession(), modelEntity, classIdentifier);
		} else if (modelEntity.isSubMMEntityOf(MariusQLConstants.STATIC_CLASS_CORE_ENTITY_NAME)) {
			klass = FactoryCore.createNewMStaticClass(this.getSession(), modelEntity, classIdentifier);
		} else {
			throw new MariusQLException();
		}

		// Manage inheritance with UNDER.
		AST optionalClauseNode = classIdentifierNode.getNextSibling();
		if (optionalClauseNode != null) {
			if (optionalClauseNode.getType() == MariusQLSQLTokenTypes.UNDER) {
				String superClassIdentifier = optionalClauseNode.getFirstChild().getText();
				MGenericClass superklass = FactoryCore.createExistingMGenericClass(this.getSession(),
						superClassIdentifier);
				klass.setSuperClass(superklass);
			} else if (optionalClauseNode.getType() == MariusQLSQLTokenTypes.CONTEXT) {
				throw new MariusQLException();
			}
		}

		// Set class description.
		AST descriptor = getFirstChild().getNextSibling();
		DescriptionsDefintionClause propertiesNode = null;
		DescriptorClause descriptorClause = null;

		if (descriptor != null) {
			if (descriptor.getType() == MariusQLSQLTokenTypes.DESCRIPTOR) {
				descriptorClause = (DescriptorClause) descriptor;
				descriptorClause.setDescriptor(klass, FactoryCore.createExistingMMEntity(getSession(),
						MariusQLHelper.getEntityIdentifier(entityName)));

				propertiesNode = (DescriptionsDefintionClause) descriptor.getNextSibling();
			} else {
				propertiesNode = (DescriptionsDefintionClause) descriptor;
			}
		}

		if (propertiesNode != null) {
			if (!klass.isAbstract()) {
				propertiesNode.setDescriptionScope(klass);
				final List<MProperty> collectProperties = propertiesNode.collectProperties();

				((MClass) klass).setDefinedProperties(collectProperties);
			} else {
				throw new MariusQLException("The class is abstract");
			}
		}

		this.getWalker().setRowCount(klass.insert());
	}

	/**
	 * Process the creation of the given entity or extent.
	 * 
	 * @throws SemanticException
	 */
	public void process() throws SemanticException {
		AST entityTypeNode = getFirstChild();

		if (entityTypeNode.getType() == MariusQLTokenTypes.ENTITY) {
			processCreateMetaModelElement(entityTypeNode.getFirstChild());
		} else if (entityTypeNode.getType() == MariusQLTokenTypes.EXTENT) {
			processCreateExtent(entityTypeNode.getFirstChild());
		} else if (entityTypeNode.getType() == MariusQLTokenTypes.OPERATION) {
			processCreateMOperation(entityTypeNode.getFirstChild());
		} else if (entityTypeNode.getType() == MariusQLTokenTypes.IMPLEMENTATION) {
			handleMImplementationCreation(entityTypeNode.getFirstChild());
		} else if (entityTypeNode.getType() == MariusQLTokenTypes.ONTOLOGY_MODEL_ID) {
			processCreateModelElement(entityTypeNode);
		} else {
			throw new MariusQLException("The type is not supported");
		}
	}

	public void processCreateMOperation(AST entityTypeNode) {
		throw new NotSupportedException();
	}

	private void handleMImplementationCreation(AST entityTypeNode) {
		throw new NotSupportedException();
	}

	@Override
	public int getStatementType() {
		throw new NotSupportedException();
	}
}
