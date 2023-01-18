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
import antlr.Token;
import antlr.collections.AST;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLSQLTokenTypes;
import fr.ensma.lias.mariusql.engine.tree.AggregateNode;
import fr.ensma.lias.mariusql.engine.tree.ArithmeticNode;
import fr.ensma.lias.mariusql.engine.tree.ArrayNode;
import fr.ensma.lias.mariusql.engine.tree.Case2Node;
import fr.ensma.lias.mariusql.engine.tree.CaseNode;
import fr.ensma.lias.mariusql.engine.tree.DotNode;
import fr.ensma.lias.mariusql.engine.tree.IdentNode;
import fr.ensma.lias.mariusql.engine.tree.InitializeableNode;
import fr.ensma.lias.mariusql.engine.tree.LiteralNode;
import fr.ensma.lias.mariusql.engine.tree.MethodNode;
import fr.ensma.lias.mariusql.engine.tree.SQLNode;
import fr.ensma.lias.mariusql.engine.tree.StarNode;
import fr.ensma.lias.mariusql.engine.tree.UnaryNode;
import fr.ensma.lias.mariusql.engine.tree.ddl.AlterStatement;
import fr.ensma.lias.mariusql.engine.tree.ddl.CreateStatement;
import fr.ensma.lias.mariusql.engine.tree.ddl.DescriptionDefinitionNode;
import fr.ensma.lias.mariusql.engine.tree.ddl.DescriptionsDefintionClause;
import fr.ensma.lias.mariusql.engine.tree.ddl.DescriptorClause;
import fr.ensma.lias.mariusql.engine.tree.ddl.DropStatement;
import fr.ensma.lias.mariusql.engine.tree.dml.DeleteStatement;
import fr.ensma.lias.mariusql.engine.tree.dml.InsertStatement;
import fr.ensma.lias.mariusql.engine.tree.dml.IntoClause;
import fr.ensma.lias.mariusql.engine.tree.dml.SetClause;
import fr.ensma.lias.mariusql.engine.tree.dml.UpdateStatement;
import fr.ensma.lias.mariusql.engine.tree.dml.ValuesClause;
import fr.ensma.lias.mariusql.engine.tree.dql.FromClause;
import fr.ensma.lias.mariusql.engine.tree.dql.ApproxNode;
import fr.ensma.lias.mariusql.engine.tree.dql.SelectClause;
import fr.ensma.lias.mariusql.engine.tree.dql.SelectStatement;
import fr.ensma.lias.mariusql.exception.NotSupportedException;

/**
 * (OLD: SQLASTFactory)
 * 
 * Custom AST factory the intermediate tree that causes ANTLR to create
 * specialized AST nodes, given the AST node type (from MariusQLSQLTokenTypes).
 * MariusQLSQLWalker registers this factory with itself when it is initialized.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 * @author Ghada TRIKI
 * @author Valentin CASSAIR
 * @author Adel GHAMNIA
 */
public class MariusQLSQLASTFactory extends ASTFactory implements MariusQLSQLTokenTypes {

	/**
	 * Reference to the walker of the logical tree.
	 */
	private MariusQLSQLWalker walker;

	/**
	 * Create factory with a specific mapping from token type to Java AST node type.
	 * Your subclasses of ASTFactory can override and reuse the map stuff.
	 * 
	 * @param walker the walker of the logical tree
	 */
	public MariusQLSQLASTFactory(final MariusQLSQLWalker walker) {
		super();
		this.walker = walker;
	}

	@Override
	public final Class<?> getASTNodeType(final int tokenType) {
		switch (tokenType) {
		case APPROX:
			return ApproxNode.class;
		case SELECT:
			return SelectStatement.class;
		case SELECT_CLAUSE:
			return SelectClause.class;
		case INSERT:
			return InsertStatement.class;
		case DELETE:
			return DeleteStatement.class;
		case INTO:
			return IntoClause.class;
		case VALUES:
			return ValuesClause.class;
		case UPDATE:
			return UpdateStatement.class;
		case SET:
			return SetClause.class;
		case CREATE:
			return CreateStatement.class;
		case ALTER:
			return AlterStatement.class;
		case DROP:
			return DropStatement.class;
		case DESCRIPTOR:
			return DescriptorClause.class;
		case PROPERTIES:
		case ATTRIBUTES:
			return DescriptionsDefintionClause.class;
		case PROPERTY_DEF:
		case ATTRIBUTE_DEF:
			return DescriptionDefinitionNode.class;
		case FROM:
			return FromClause.class;
		case DOT:
			return DotNode.class;
		case IDENT:
			return IdentNode.class;
		case METHOD_CALL:
			return MethodNode.class;
		case COUNT:
		case AGGREGATE:
			return AggregateNode.class;
		case NUM_INT:
		case NUM_FLOAT:
		case NUM_LONG:
		case NUM_DOUBLE:
		case QUOTED_STRING:
		case TRUE:
		case FALSE:
		case NULL:
			return LiteralNode.class;
		case ARRAY:
			return ArrayNode.class;
		case ROW_STAR:
			return StarNode.class;
		case OF:
			throw new NotSupportedException();
		case PLUS:
		case MINUS:
		case STAR:
		case DIV:
			return ArithmeticNode.class;
		case UNARY_MINUS:
		case UNARY_PLUS:
			return UnaryNode.class;
		case CASE2:
			return Case2Node.class;
		case CASE:
			return CaseNode.class;
		default:
			return SQLNode.class;
		}
	}

	@Override
	protected final AST createUsingCtor(final Token token, final String className) {
		throw new NotSupportedException();
	}

	/**
	 * Initialize a node.
	 * 
	 * @param t the node to initialize
	 */
	private void initializeSqlNode(final AST t) {
		// Initialize SQL nodes here.
		if (t instanceof InitializeableNode) {
			InitializeableNode initializeableNode = (InitializeableNode) t;
			initializeableNode.initialize(walker);
		}
	}

	/**
	 * Actually instantiate the AST node.
	 * 
	 * @param c The class to instantiate.
	 * @return The instantiated and initialized node.
	 */
	@SuppressWarnings("rawtypes")
	protected final AST create(final Class c) {
		AST t;
		try {
			t = (AST) c.newInstance(); // make a new one
			initializeSqlNode(t);
		} catch (Exception e) {
			error("Can't create AST Node " + c.getName());
			return null;
		}
		return t;
	}
}
