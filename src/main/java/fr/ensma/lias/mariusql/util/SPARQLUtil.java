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
package fr.ensma.lias.mariusql.util;

import antlr.ASTFactory;
import antlr.collections.AST;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLTokenTypes;
import fr.ensma.lias.mariusql.engine.util.ASTUtil;

/**
 * @author Stephane JEAN, Mickael BARON and Raoul TIAM
 * @author Adel GHAMNIA
 */
public class SPARQLUtil {

	/**
	 * Return a node corresponding to the #uri attribute
	 * 
	 * @param aliasName  alias of the scope of this attribute
	 * @param astFactory constructor of node
	 * @return a node corresponding to the #uri attribute
	 */
	public static AST getNodeAttributeURI(String aliasName, ASTFactory astFactory, boolean join) {

		AST res = ASTUtil.create(astFactory, MariusQLTokenTypes.METHOD_CALL, "||");
		AST concatNode = ASTUtil.create(astFactory, MariusQLTokenTypes.IDENT, "concat");
		AST exprListNode = ASTUtil.create(astFactory, MariusQLTokenTypes.EXPR_LIST, "concatList");

		AST dotOWLNamespaceNode = ASTUtil.create(astFactory, MariusQLTokenTypes.DOT, "DOT");
		AST prefixNode = ASTUtil.create(astFactory, MariusQLTokenTypes.IDENT, aliasName);
		dotOWLNamespaceNode.addChild(prefixNode);
		AST OWLNamespaceNode = ASTUtil.create(astFactory, MariusQLTokenTypes.IDENT, "#OWLNamespace");
		dotOWLNamespaceNode.addChild(OWLNamespaceNode);

		AST dotCodeNode = ASTUtil.create(astFactory, MariusQLTokenTypes.DOT, "DOT");
		AST prefixCodeNode = ASTUtil.create(astFactory, MariusQLTokenTypes.IDENT, aliasName);
		dotCodeNode.addChild(prefixCodeNode);

		String cond = "";
		if (join)
			cond = "#rid";
		else
			cond = "code";
		AST codeNode = ASTUtil.create(astFactory, MariusQLTokenTypes.IDENT, cond);
		dotCodeNode.addChild(codeNode);

		// exprListNode.addChild(dotOWLNamespaceNode);
		exprListNode.addChild(dotCodeNode);
		res.addChild(concatNode);
		res.addChild(exprListNode);

		return res;
	}
}
