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
package fr.ensma.lias.mariusql.engine;

import antlr.ASTFactory;
import fr.ensma.lias.mariusql.engine.tree.InitializeableNode;
import fr.ensma.lias.mariusql.engine.tree.SQLNode;
import fr.ensma.lias.mariusql.engine.tree.dml.DMLEvaluator;
import fr.ensma.lias.mariusql.engine.util.AliasGenerator;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * A semantic analysis node, that points back to the main analyzer.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 * @author Adel GHAMNIA
 */
public class MariusQLSQLWalkerNode extends SQLNode implements InitializeableNode {

	private static final long serialVersionUID = -719651879390684642L;

	/**
	 * A pointer back to the phase 2 processor.
	 */
	private MariusQLSQLWalker walker;

	@Override
	public void initialize(Object param) {
		walker = (MariusQLSQLWalker) param;
	}

	/**
	 * @return the phase 2 processor
	 */
	public final MariusQLSQLWalker getWalker() {
		return walker;
	}

	public MariusQLSession getSession() {
		return this.getWalker().getSession();
	}

	/**
	 * @return the generator of alias
	 */
	public AliasGenerator getAliasGenerator() {
		return walker.getAliasGenerator();
	}

	/**
	 * @return the factory of node
	 */
	public final ASTFactory getASTFactory() {
		return walker.getASTFactory();
	}

	/**
	 * @return the evaluator of DMLEvaluator
	 */
	public final DMLEvaluator getDMLEvaluator() {
		return walker.getDMLEvaluator();
	}

	/**
	 * @return the namespace corresponding to a given alias
	 */
	public final String getNamespace(String alias) {
		return walker.getNamespace(alias);
	}
}
