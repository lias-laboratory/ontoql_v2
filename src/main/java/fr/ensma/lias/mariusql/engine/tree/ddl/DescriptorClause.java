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

import antlr.collections.AST;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.DatatypeMultiString;
import fr.ensma.lias.mariusql.core.DatatypeString;
import fr.ensma.lias.mariusql.core.metamodel.MMAttribute;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.core.metamodel.MMMultiAttribute;
import fr.ensma.lias.mariusql.core.model.ModelElement;
import fr.ensma.lias.mariusql.engine.MariusQLSQLWalkerNode;
import fr.ensma.lias.mariusql.engine.tree.IdentNode;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.util.MariusQLHelper;

/**
 * Defines an AST node representing an OntoQL descriptor clause.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 * @author Valentin CASSAIR
 */
public class DescriptorClause extends MariusQLSQLWalkerNode {

	private static final long serialVersionUID = -7086817050167884031L;

	public void setDescriptor(ModelElement currentInstance, MMEntity pMMEntity) {
		AST currentAttributeAssignment = getFirstChild();
		AST currentASTAttribute = null;
		AST currentASTValue = null;
		MMAttribute currentAttribute = null;

		while (currentAttributeAssignment != null) {
			currentASTAttribute = currentAttributeAssignment.getFirstChild();

			currentAttribute = (MMAttribute) pMMEntity
					.getDefinedDescription(MariusQLHelper.getEntityIdentifier(currentASTAttribute.getText()));

			if (currentAttribute == null) {
				throw new MariusQLException("The attribute is null");
			}

			StringBuilder valueText = new StringBuilder();
			currentASTValue = currentASTAttribute.getNextSibling();

			if (MariusQLConstants.COLLECTION_NAME.equalsIgnoreCase(currentASTValue.getText())) {
				valueText.append(MariusQLConstants.COLLECTION_NAME);
				AST node = currentASTAttribute.getNextSibling().getFirstChild();

				while (node != null) {
					valueText.append("," + MariusQLHelper.getQuotedString(node.getText()));
					node = node.getNextSibling();
				}
			} else {
				valueText = new StringBuilder(currentASTValue.getText());
			}

			if (currentAttribute.getRange() instanceof DatatypeString
					|| currentAttribute.getRange() instanceof DatatypeMultiString) {
				valueText = new StringBuilder(MariusQLHelper.removeSyntaxNameIdentifier(valueText.toString()));
			}

			if (currentAttribute instanceof MMMultiAttribute) {
				if (((IdentNode) currentASTAttribute).getLgCode() != null) {
					currentInstance.setValue(currentAttribute, ((IdentNode) currentASTAttribute).getLgCode(),
							valueText.toString());
				} else {
					if (getSession().getReferenceLanguage().equalsIgnoreCase(MariusQLConstants.NO_LANGUAGE)) {
						currentInstance.setValue(currentAttribute, MariusQLConstants.DEFAULT_LANGUAGE,
								valueText.toString());
					} else {
						currentInstance.setValue(currentAttribute, getSession().getReferenceLanguage(),
								valueText.toString());
					}
				}
			} else {
				currentInstance.setValue(currentAttribute, valueText.toString());
			}

			currentAttributeAssignment = currentAttributeAssignment.getNextSibling();
		}
	}
}
