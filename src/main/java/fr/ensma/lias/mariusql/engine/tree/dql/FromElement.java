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

import antlr.SemanticException;
import antlr.collections.AST;
import fr.ensma.lias.mariusql.core.Category;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.model.MClass;
import fr.ensma.lias.mariusql.driver.postgresql.antlr.PostgresSQLTokenTypes;
import fr.ensma.lias.mariusql.engine.MariusQLSQLWalker;
import fr.ensma.lias.mariusql.engine.MariusQLSQLWalkerNode;
import fr.ensma.lias.mariusql.engine.tree.DisplayableNode;
import fr.ensma.lias.mariusql.util.MariusQLHelper;

/**
 * Represents a single class mentioned in an Ontoql FROM clause.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 * @author Adel GHAMNIA
 */
public class FromElement extends MariusQLSQLWalkerNode implements DisplayableNode {

	private static final long serialVersionUID = -7997540432293587828L;

	/**
	 * MGenericClass / entity corresponding to this node.
	 */
	private Category category;

	/**
	 * Reference to its from clause.
	 */
	private FromClause fromClause;

	/**
	 * True if this from element has been added to resolve a path expression.
	 */
	private boolean implicitJoin = false;

	/**
	 * Alias used into the MariusQL query.
	 */
	private String alias;

	@Override
	public String getDisplayText() {
		StringBuilder buf = new StringBuilder();
		if (category != null) {
			buf.append("FromElement{" + category.getName() + "}");
		}

		return buf.toString();
	}

	/**
	 * Constructor of a from element.
	 * 
	 * @param path     text of this node
	 * @param alias    alias of this from element
	 * @param only     true if this is a polymorphic query
	 * @param genAlias true if must generate an alias to this node
	 * @param walker   the walker of the tree containing this node
	 * @throws SemanticException if a semantic error occurs
	 */
	public FromElement(String path, String nsAlias, String alias, final AST only, boolean genAlias,
			MariusQLSQLWalker walker, FromClause fromClause, boolean isDml) throws SemanticException {
		this.initialize(walker);
		this.setFromClause(fromClause);

		// Entity or class.
		if (MariusQLHelper.isMetaModelElement(path)) {
			this.category = FactoryCore.createExistingMMEntity(getSession(), MariusQLHelper.getEntityIdentifier(path));
		} else {
			this.category = FactoryCore.createExistingMGenericClass(getSession(), path);
		}

		if (genAlias) { // DML statement don't accept alias
			this.category
					.setTableAlias(this.getAliasGenerator().createName(Long.toString(this.category.getInternalId())));
		}

		if (isDml) {
			this.setType(PostgresSQLTokenTypes.TABLE);
			this.setText(this.category.toSQLWithAlias());
		} else {
			this.translateToSQL(only == null);
		}

		this.category.setCategoryAlias(alias);
	}

	/**
	 * Used to create an implicit join due to a path expression or a join due to a
	 * differences of ontology model.
	 * 
	 * @param aCategory category corresponding to this from element
	 * @param polymorph true if this is a polymorphic query
	 * @param walker    the walker of the tree containing this node
	 */
	public FromElement(final Category aCategory, final boolean polymorph, final MariusQLSQLWalker walker) {
		initialize(walker);
		this.category = aCategory;
		setType(PostgresSQLTokenTypes.TABLE);
		this.category.setPolymorph(polymorph);

		if (this.category.getTableAlias() == null) {
			this.category
					.setTableAlias(this.getAliasGenerator().createName(Long.toString(this.category.getInternalId())));
		}

		setText(this.category.toSQLWithAlias());
		implicitJoin = true;

	}

	private void translateToSQL(boolean isPolymorph) {
		this.category.setPolymorph(isPolymorph);
		this.setType(PostgresSQLTokenTypes.TABLE);
		StringBuilder sql = new StringBuilder();

		if (category.isClass()) {
			MClass currentClass = (MClass) this.category;
			if (isPolymorph) {
				sql.append(currentClass.getSQLPolymorphProjection());
			} else {
				sql.append(currentClass.getSQLClassProjectionOnly());
			}
		} else {
			sql.append(category.toSQL());
		}

		if (category.getTableAlias() != null) {
			sql.append(" " + category.getTableAlias());
		}
		this.setText(sql.toString());
	}

	public void setFromClause(FromClause fromClause) {
		this.fromClause = fromClause;
	}

	/**
	 * Get the class/entity corresponding to this node.
	 * 
	 * @return the class/entity corresponding to this node
	 */
	public final Category getCategory() {
		return this.category;
	}

	/**
	 * @return Returns the fromClause.
	 */
	public final FromClause getFromClause() {
		return fromClause;
	}

	/**
	 * Check if this current FromElement is an implicit join.
	 * 
	 * @return true if this from element has been added to resolve a path expression
	 */
	public final boolean isImplicitJoin() {
		return implicitJoin;
	}

	public boolean isEntityFromElement() {
		return category.isEntity();
	}

	public boolean isClassFromElement() {
		return category.isClass();
	}

	public String getGeneratedAlias() {
		return this.category.getTableAlias();
	}

	/**
	 * Set the current alias. Alias is used into the OntoQL query.
	 * 
	 * @param alias
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return this.alias;
	}
}
