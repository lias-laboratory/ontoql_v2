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
package fr.ensma.lias.mariusql.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * Implementation of methods implemented in the same way for the class and for
 * the entities
 * 
 * @author Mickael BARON
 * @author Florian MHUN
 */
public abstract class AbstractCategory implements Category {

	/**
	 * Current session.
	 */
	protected MariusQLSession session;

	/**
	 * the alias used for this class in the ontoql query
	 */
	protected String categoryAlias = null;

	/**
	 * the alias used for this class in the generated sql query
	 */
	protected String tableAlias = null;

	/**
	 * The namespace of the this ontology class.
	 */
	protected String namespace;

	/**
	 * Is this property used in a polymorphic context
	 */
	protected boolean isPolymorph = false;

	public AbstractCategory(MariusQLSession p) {
		this.session = p;
	}

	@Override
	public boolean isPolymorph() {
		return isPolymorph;
	}

	@Override
	public void setPolymorph(boolean polymorph) {
		this.isPolymorph = polymorph;
	}

	@Override
	public String getCategoryAlias() {
		return categoryAlias;
	}

	@Override
	public void setCategoryAlias(String entityAlias) {
		this.categoryAlias = entityAlias;
	}

	@Override
	public String getTableAlias() {
		return tableAlias;
	}

	@Override
	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public MariusQLSession getSession() {
		return this.session;
	}

	@Override
	public boolean hasSubCategories() {
		return this.getDirectSubCategories().size() > 0;
	}

	@Override
	public List<Category> getHierarchyFromThisCategory() {
		List<Category> classHierarchy = new ArrayList<Category>();

		classHierarchy.add(this);
		classHierarchy.addAll(this.getAllSubcategories());

		return classHierarchy;
	}

	@Override
	public List<Category> getAllSubcategories() {
		List<Category> hierarchy = new ArrayList<Category>();

		Stack<Category> categoriesToTraverse = new Stack<Category>();
		Category current = null;
		categoriesToTraverse.addAll(this.getDirectSubCategories());

		while (!categoriesToTraverse.isEmpty()) {
			current = categoriesToTraverse.pop();
			hierarchy.add(current);
			categoriesToTraverse.addAll(current.getDirectSubCategories());
		}

		return hierarchy;
	}

	@Override
	public String toString() {
		return "Category[" + this.getInternalId() + "]";
	}
}
