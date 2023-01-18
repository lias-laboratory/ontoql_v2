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
package fr.ensma.lias.mariusql.engine.tree;

import antlr.SemanticException;
import antlr.collections.AST;

/**
 * The contract for expression sub-trees that can resolve themselves.
 * 
 * @author Stéphane JEAN
 * @author Stéphane JEAN
 */
public interface ResolvableNode {

	/**
	 * Does the work of resolving an identifier.
	 * 
	 * @param prefix prefix of this node
	 * @return the resolved node
	 * @throws SemanticException if a semantic exception is detected
	 */
	AST resolve(AST prefix) throws SemanticException;
}
