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
import fr.ensma.lias.mariusql.core.Category;
import fr.ensma.lias.mariusql.core.Description;
import fr.ensma.lias.mariusql.core.metamodel.MMAttribute;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.core.model.MClass;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.engine.MariusQLSQLWalkerNode;
import fr.ensma.lias.mariusql.exception.MariusQLException;

/**
 * Defines an AST node representing an OntoQL descriptor clause.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 * @author Florian MHUN
 */
public class DescriptionsDefintionClause extends MariusQLSQLWalkerNode {

	private static final long serialVersionUID = 9157982167457649665L;

	/**
	 * The scope of the description of this clause.
	 */
	private Category descriptionScope;

	/**
	 * Returns an array of Properties gathered from the children of the given parent
	 * AST node.
	 * 
	 * @return an array of Properties gathered from the children of the given parent
	 *         AST node.
	 */
	public List<Description> collectDescription(boolean isProperty) {
		AST firstChild = getFirstChild();
		AST parent = this;
		List<Description> list = new ArrayList<Description>(parent.getNumberOfChildren());

		for (AST n = firstChild; n != null; n = n.getNextSibling()) {
			if (isProperty) {
				final MProperty propertyDefined = ((DescriptionDefinitionNode) n)
						.getPropertyDefined((MClass) descriptionScope);

				for (Description currentList : list) {
					if (propertyDefined.getName().equalsIgnoreCase(((MProperty) currentList).getName())) {
						throw new MariusQLException("Property already exists: " + propertyDefined.getName());
					}
				}

				list.add(propertyDefined);
			} else {
				// TODO : use polymorphic method on Category to get all Attributes or Properties
				// called getAllDescriptions()
				MMEntity category = (MMEntity) this.descriptionScope;
				Description desc = ((DescriptionDefinitionNode) n).getAttributeDefined(category);

				List<MMAttribute> allAttributes = category.getAllAttributes();
				allAttributes.remove(desc); // allAttributes but not desc

				for (MMAttribute attribute : allAttributes) {
					if (attribute.getName().equalsIgnoreCase(desc.getName())) {
						throw new MariusQLException("Attribute '" + desc.getName() + "' already defined for entity '"
								+ category.getName() + "'");
					}
				}

				list.add(desc);
			}
		}

		return list;
	}

	public void setDescriptionScope(Category descriptionScope) {
		this.descriptionScope = descriptionScope;
	}

	@SuppressWarnings("unchecked")
	public List<MMAttribute> collectAttributes() {
		return (List<MMAttribute>) (List<?>) collectDescription(false);
	}

	@SuppressWarnings("unchecked")
	public List<MProperty> collectProperties() {
		return (List<MProperty>) (List<?>) collectDescription(true);
	}
}
