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
package fr.ensma.lias.mariusql.sparql;

import antlr.ASTFactory;
import antlr.collections.AST;
import fr.ensma.lias.mariusql.engine.SPARQLMariusQLWalker;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLTokenTypes;
import fr.ensma.lias.mariusql.engine.util.ASTUtil;
import fr.ensma.lias.mariusql.util.SPARQLUtil;

/**
 * Default behaviour of a SPARQL property or attribute
 * 
 * @author Stephane JEAN, Mickael BARON and Raoul TIAM
 * @author Adel GHAMNIA
 */
public abstract class AbstractDescriptionSPARQL implements DescriptionSPARQL {

	/**
	 * Name of this SPARQL property or attribute
	 */
	protected String name = null;

	/**
	 * Namespace of this SPARQL property or attribute
	 */
	protected String namespace = null;

	/**
	 * variable used to reference this SPARQL property or attribute
	 */
	protected String variable = null;

	/**
	 * True if this SPARQL property or attribute is involved in a join condition
	 * requiring the coalesce operator
	 */
	protected boolean isCoalesce = false;

	/**
	 * The SPARQL property or attribute which is joined with this one in a join
	 * condition requiring the coalesce operator
	 */
	protected DescriptionSPARQL coalesceDescription = null;

	/**
	 * Walker using this SPARQL class or entity
	 */
	protected SPARQLMariusQLWalker walker = null;

	/**
	 * Scope of this SPARQL property or attribute
	 */
	protected CategorySPARQL scope = null;

	@Override
	public void setScope(CategorySPARQL category) {
		scope = category;
	}

	/**
	 * Get the node representing this SPARQL property or attribute
	 * 
	 * @param aliasNeeded True if an alias must be added
	 * @param isCoalesce  True if this SPARQL property or attribute is involved in a
	 *                    join condition requiring the coalesce operator
	 * @return the node representing this SPARQL property or attribute
	 */
	public AST getDotElement(boolean aliasNeeded, boolean isCoalesce, ASTFactory astFactory, boolean join) {
		AST res = null;
		AST dotNode = null;
		if (isCoalesce) {
			res = ASTUtil.create(astFactory, MariusQLTokenTypes.METHOD_CALL, "(");
			AST coalesceNode = ASTUtil.create(astFactory, MariusQLTokenTypes.IDENT, "COALESCE");
			AST exprListNode = ASTUtil.create(astFactory, MariusQLTokenTypes.EXPR_LIST, "exprList");
			exprListNode.addChild(this.getDotElement(false, false, astFactory, join));
			exprListNode.addChild(coalesceDescription.getDotElement(false, false, astFactory, join));
			res.addChild(coalesceNode);
			res.addChild(exprListNode);
		} else {
			String aliasName = null;
			String descriName = null;
			if (isMultivalued()) {
				if (isAttribute()) {
					dotNode = SPARQLUtil.getNodeAttributeURI(name, astFactory, join);
				} else {
					descriName = "URI";
				}
				aliasName = name;
			} else {
				aliasName = scope.getAlias();
				descriName = name;
			}
			if (dotNode == null) {
				dotNode = ASTUtil.create(astFactory, MariusQLTokenTypes.DOT, "DOT");
				AST prefixNode = ASTUtil.create(astFactory, MariusQLTokenTypes.IDENT, aliasName);
				dotNode.addChild(prefixNode);

				AST propNode = ASTUtil.create(astFactory, MariusQLTokenTypes.IDENT, descriName);
				dotNode.addChild(propNode);
				if (this.namespace != null) {
					AST aliasNode = ASTUtil.create(astFactory, MariusQLTokenTypes.NAMESPACE_ALIAS, this.namespace);
					propNode.addChild(aliasNode);
				}
			}

			if (aliasNeeded) {
				String aliasNodeName = variable.substring(1);
				int indexOfURI = variable.indexOf("URI");
				if (indexOfURI != -1) {
					aliasNodeName = aliasNodeName.substring(0, indexOfURI - 1);
				}
				AST aliasNode = ASTUtil.create(astFactory, MariusQLTokenTypes.IDENT, aliasNodeName);
				res = ASTUtil.create(astFactory, MariusQLTokenTypes.AS, "as");
				res.addChild(dotNode);
				res.addChild(aliasNode);
			} else {
				res = dotNode;
			}
		}
		return res;
	}

	/**
	 * Get the node representing this SPARQL property or attribute
	 * 
	 * @return the node representing this SPARQL property or attribute
	 */
	public AST getDotElement(boolean aliasNeeded, boolean join) {
		return getDotElement(aliasNeeded, this.isCoalesce, walker.getASTFactory(), join);
	}

	/**
	 * @param isCoalesce the isCoalesce to set
	 */
	public void setCoalesce(boolean isCoalesce) {
		this.isCoalesce = isCoalesce;
	}

	/**
	 * @param coalesceProperty the coalesceProperty to set
	 */
	public void setCoalesceDescription(DescriptionSPARQL coalesceProperty) {
		this.coalesceDescription = coalesceProperty;
	}

	/**
	 * @return the variable
	 */
	public String getVariable() {
		return variable;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public CategorySPARQL getScope() {
		return scope;
	}
}
