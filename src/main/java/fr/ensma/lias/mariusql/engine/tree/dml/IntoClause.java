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

import antlr.collections.AST;
import fr.ensma.lias.mariusql.core.Category;
import fr.ensma.lias.mariusql.core.Description;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.engine.MariusQLSQLWalkerNode;
import fr.ensma.lias.mariusql.engine.tree.DisplayableNode;
import fr.ensma.lias.mariusql.engine.tree.IdentNode;
import fr.ensma.lias.mariusql.util.MariusQLHelper;

/**
 * Represents a category referenced in the INTO clause of an OntoQL INSERT
 * statement.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 */
public class IntoClause extends MariusQLSQLWalkerNode implements DisplayableNode {

	private static final long serialVersionUID = -3057932710179261100L;

	/**
	 * The category instantiated.
	 */
	private Category categoryInstantiated;

	/**
	 * The descriptions valued by this instruction.
	 */
	private List<Description> descriptions;

	public IntoClause() {
		this.descriptions = new ArrayList<Description>();
	}

	/**
	 * Initialize this intoClause with the instanciated category
	 * 
	 * @param aClass the modified class
	 */
	public void initialize(String categoryId) {
		if (MariusQLHelper.isMetaModelElement(categoryId)) {
			this.categoryInstantiated = FactoryCore.createExistingMMEntity(this.getSession(),
					MariusQLHelper.getEntityIdentifier(categoryId));
		} else {
			this.categoryInstantiated = FactoryCore.createExistingMGenericClass(this.getSession(), categoryId);
		}

		this.getWalker().getDMLEvaluator().evaluateExt(this.categoryInstantiated, (IdentNode) this.getFirstChild());
	}

	/**
	 * Get the name of the instantiated category.
	 * 
	 * @return the name of the instantiated category.
	 */
	public String getCategoryInstanciatedName() {
		return categoryInstantiated.getName();
	}

	@Override
	public String getDisplayText() {
		StringBuilder buf = new StringBuilder();
		buf.append("IntoClause{");
		buf.append("categoryName=").append(this.getCategoryInstanciatedName());
		buf.append(",columns={").append(descriptions).append("}");
		buf.append("}");
		return buf.toString();
	}

	/**
	 * Get the category instanciated
	 * 
	 * @return the category instanciated
	 */
	public Category getCategoryInstantiated() {
		return this.categoryInstantiated;
	}

	/**
	 * Get the parent node of the valuated attributes or properties
	 * 
	 * @return the parent node of the valuated attributes or properties
	 */
	public AST getRangeClause() {
		return getFirstChild().getNextSibling();
	}

	/**
	 * add a description valuated
	 * 
	 * @param description a description valuated
	 */
	public void addDescription(Description description) {
		this.descriptions.add(description);
	}

	public List<Description> getDescriptions() {
		return this.descriptions;
	}
}
