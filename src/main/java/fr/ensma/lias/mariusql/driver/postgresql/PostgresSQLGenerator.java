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
package fr.ensma.lias.mariusql.driver.postgresql;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import antlr.RecognitionException;
import antlr.collections.AST;
import fr.ensma.lias.mariusql.driver.SQLFunction;
import fr.ensma.lias.mariusql.driver.postgresql.antlr.PostgesSQLGeneratorBase;
import fr.ensma.lias.mariusql.engine.MariusQLGenerator;
import fr.ensma.lias.mariusql.engine.tree.MethodNode;
import fr.ensma.lias.mariusql.engine.util.ErrorCounter;
import fr.ensma.lias.mariusql.engine.util.ErrorReporter;
import fr.ensma.lias.mariusql.engine.util.ParseErrorHandler;

/**
 * Generates SQL by overriding callback methods in the base class, which does
 * the actual SQL AST walking.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 * @author Valentin CASSAIR
 */
public class PostgresSQLGenerator extends PostgesSQLGeneratorBase implements ErrorReporter, MariusQLGenerator {

	/**
	 * Writer of the result SQL query.
	 */
	protected SqlWriter writer;

	/**
	 * Handles parser errors.
	 */
	protected ParseErrorHandler parseErrorHandler;

	/**
	 * Handle function call.
	 */
	private LinkedList<SqlWriter> outputStack = new LinkedList<SqlWriter>();

	/**
	 * Constructor without parameter.
	 */
	public PostgresSQLGenerator() {
		this.writer = new DefaultWriter();
		this.parseErrorHandler = new ErrorCounter();
	}

	@Override
	protected final void out(final String s) {
		writer.clause(s);
	}

	@Override
	public final void reportError(final RecognitionException e) {
		parseErrorHandler.reportError(e);
	}

	@Override
	public final void reportError(final String s) {
		parseErrorHandler.reportError(s);
	}

	@Override
	public final void reportWarning(final String s) {
		parseErrorHandler.reportWarning(s);
	}

	/**
	 * @return the error handler
	 */
	public final ParseErrorHandler getParseErrorHandler() {
		return parseErrorHandler;
	}

	@Override
	protected final void commaBetweenParameters(final String comma) {
		writer.commaBetweenParameters(comma);
	}

	@Override
	protected final void optionalSpace() {
		int c = getLastChar();
		switch (c) {
		case -1:
			return;
		case ' ':
			return;
		case ')':
			return;
		case '(':
			return;
		default:
			out(" ");
		}
	}

	@Override
	protected final void beginFunctionTemplate(final AST m, final AST i) {
		MethodNode methodNode = (MethodNode) m;
		SQLFunction template = methodNode.getSQLFunction();
		if (template == null) {
			// if template is null we just write the function out as it appears
			// in the hql statement
			super.beginFunctionTemplate(m, i);
		} else {
			// this function has a template -> redirect output and catch the
			// arguments
			outputStack.addFirst(writer);
			writer = new FunctionArguments();
		}
	}

	@Override
	protected final void endFunctionTemplate(final AST m) {
		MethodNode methodNode = (MethodNode) m;
		SQLFunction template = methodNode.getSQLFunction();
		if (template == null) {
			super.endFunctionTemplate(m);
		} else {
			// this function has a template -> restore output, apply the
			// template and write the result out
			FunctionArguments functionArguments = (FunctionArguments) writer;
			writer = (SqlWriter) outputStack.removeFirst();
			out(template.render(functionArguments.getArgs()));
		}
	}

	/**
	 * Writes SQL fragments.
	 */
	interface SqlWriter {

		/**
		 * Write a clause.
		 * 
		 * @param clause clause to write
		 */
		void clause(String clause);

		/**
		 * @param comma string representing a comma
		 */
		void commaBetweenParameters(String comma);
	}

	/**
	 * SQL function processing code redirects generated SQL output to an instance of
	 * this class which catches function arguments.
	 */
	class FunctionArguments implements SqlWriter {

		/**
		 * Index of argument.
		 */
		private int argInd;

		/**
		 * List of argument.
		 */
		private final List<String> args = new ArrayList<String>(3);

		@Override
		public void clause(final String clause) {
			if (argInd == args.size()) {
				args.add(clause);
			} else {
				args.set(argInd, args.get(argInd) + clause);
			}
		}

		@Override
		public void commaBetweenParameters(final String comma) {
			++argInd;
		}

		/**
		 * @return List of argument
		 */
		public List<String> getArgs() {
			return args;
		}
	}

	/**
	 * The default SQL writer.
	 */
	class DefaultWriter implements SqlWriter {

		@Override
		public void clause(final String clause) {
			getStringBuffer().append(clause);
		}

		@Override
		public void commaBetweenParameters(final String comma) {
			getStringBuffer().append(comma);
		}
	}

	/**
	 * @return the SQL query executed
	 */
	public final String getSQL() {
		return getStringBuffer().toString();
	}
}
