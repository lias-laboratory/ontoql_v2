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
package fr.ensma.lias.mariusql.jdbc.impl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;
import fr.ensma.lias.mariusql.cache.querycache.QueryModel;
import fr.ensma.lias.mariusql.engine.MariusQLGenerator;
import fr.ensma.lias.mariusql.engine.MariusQLParser;
import fr.ensma.lias.mariusql.engine.MariusQLSQLWalker;
import fr.ensma.lias.mariusql.engine.SPARQLMariusQLWalker;
import fr.ensma.lias.mariusql.engine.SPARQLParser;
import fr.ensma.lias.mariusql.engine.tree.dql.ApproxNode;
import fr.ensma.lias.mariusql.engine.tree.dql.SelectExpression;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSet;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;

/**
 * The object used for executing an OntoQL statement or an QueryOntoQL object
 * and returning the results it produces.
 * 
 * @author Mickael BARON
 * @author Adel GHAMNIA
 */
public class MariusQLStatementImpl implements MariusQLStatement {

	/**
	 * A logger for this class.
	 */
	private static Logger log = LoggerFactory.getLogger(MariusQLStatementImpl.class);

	/**
	 * The SQL query executed.
	 */
	private String sqlString = "";

	/**
	 * Session to access the database.
	 */
	private MariusQLSession session;

	/**
	 * A pure JDBC statement wrapped by this class
	 */
	private Statement sqlStatement;

	/**
	 * Statements used for relaxation queries.
	 */
	private List<MariusQLStatement> subStatements;

	public Statement getNativeStatement() {
		return sqlStatement;
	}

	public MariusQLStatementImpl(MariusQLSessionImpl pSession) {
		this.session = pSession;

		try {
			this.sqlStatement = pSession.createSQLStatement();
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public String getSQLString() {
		return sqlString;
	}

	@Override
	public int executeUpdate(String mariusQL) throws MariusQLException {
		int res = 0;

		MariusQLParser parser = MariusQLParser.getInstance(mariusQL);
		log.debug("MariusQL: " + mariusQL);
		log.debug("ReferenceLanguage: " + session.getReferenceLanguage());

		try {
			// walk on MariusQL AST
			parser.statement();
			parser.getParseErrorHandler().throwQueryException();
			AST mariusQLAst = parser.getAST();

			// print MariusQL AST
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			parser.showAst(mariusQLAst, new PrintStream(baos));
			logAst("MariusQL AST", baos.toString());

			// walk on SQL AST
			MariusQLSQLWalker w = new MariusQLSQLWalker(this.session);
			w.statement(mariusQLAst);
			w.getParseErrorHandler().throwQueryException();

			if (w.isDMLStatement()) {
				// print SQL AST
				baos = new ByteArrayOutputStream();
				w.showAst(w.getAST(), new PrintStream(baos));
				logAst("SQL AST", baos.toString());

				// generate SQL
				AST sqlAst = w.getAST();
				MariusQLGenerator gen = session.getGenerator();
				gen.statement(sqlAst);

				// print generated SQL
				String sql = gen.getSQL();
				log.debug("SQL GEN:  " + sql);
				sqlString = sql;
				res = sqlStatement.executeUpdate(sql);
			} else {
				return w.getRowCount();
			}

		} catch (TokenStreamException exc) {
			throw new MariusQLException(exc);
		} catch (RecognitionException exc) {
			throw new MariusQLException(exc);
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}

		return res;
	}

	protected MariusQLSession getSession() {
		return session;
	}

	public MariusQLResultSet executeQuery(AST ast, MariusQLSQLWalker currentWalker) {
		try {
			String queryCacheKey = ast.toStringList();

			if (this.getSession().getQueryCache().isEnabled()) {
				if (this.getSession().getQueryCache().isExists(queryCacheKey)) {
					final QueryModel sql = this.getSession().getQueryCache().getSQL(queryCacheKey);

					this.sqlString = sql.getSQL();
					log.debug("SQL GEN From QueryCache: " + this.sqlString);
					ResultSet sqlResult = sqlStatement.executeQuery(sqlString);
					return new MariusQLResultSetImpl(sqlResult, sql.getSelectExpression(), true);
				}
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			currentWalker.statement(ast);

			if (currentWalker.isRelaxationQuery()) {
				final ApproxNode relaxationNode = currentWalker.getRelaxationNode();
				final MariusQLResultSet process = relaxationNode.process(ast);

				this.subStatements = relaxationNode.getSubStatements();

				return process;
			}

			AST sqlAst = currentWalker.getAST();
			currentWalker.showAst(sqlAst, new PrintStream(baos));
			logAst("SQL AST: ", baos.toString());

			final MariusQLGenerator generator = this.getSession().getGenerator();
			generator.statement(sqlAst);
			String sql = generator.getSQL();
			generator.getParseErrorHandler().throwQueryException();

			this.sqlString = sql;
			log.debug("SQL GEN: " + this.sqlString);

			ResultSet sqlResult = sqlStatement.executeQuery(sql);
			final List<SelectExpression> selectExpression = currentWalker.getExpressionInSelect();

			if (this.getSession().getQueryCache().isEnabled()) {
				this.getSession().getQueryCache().addQuery(queryCacheKey, new QueryModel(sqlString, selectExpression));
			}

			return new MariusQLResultSetImpl(sqlResult, selectExpression);
		} catch (RecognitionException exc) {
			throw new MariusQLException(exc);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MariusQLException(e);
		}
	}

	@Override
	public MariusQLResultSet executeQuery(String mariusQL) throws MariusQLException {
		MariusQLParser parser = MariusQLParser.getInstance(mariusQL);
		log.debug("parsing of the MariusQL query: " + mariusQL + " - language: " + session.getReferenceLanguage());

		try {
			parser.statement();
			parser.getParseErrorHandler().throwQueryException();
			AST ast = parser.getAST();

			final MariusQLSQLWalker currentWalker = new MariusQLSQLWalker(this.getSession());
			final MariusQLResultSet executeQuery = this.executeQuery(ast, currentWalker);

			return executeQuery;
		} catch (TokenStreamException exc) {
			throw new MariusQLException(exc);
		} catch (RecognitionException exc) {
			throw new MariusQLException(exc);
		}
	}

	private void logAst(String title, String astDescription) {
		String wrapper = "======================================";
		log.debug(title + ": \n" + wrapper + wrapper + "\n" + astDescription + wrapper + wrapper);
	}

	@Override
	public void close() throws MariusQLException {
		try {
			this.sqlStatement.close();

			if (this.subStatements != null) {
				for (MariusQLStatement current : this.subStatements) {
					current.close();
				}

				this.subStatements = null;
			}
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public MariusQLResultSet executeSPARQLQuery(String sparqlQuery) throws MariusQLException {
		SPARQLParser parser = SPARQLParser.getInstance(sparqlQuery);
		log.debug("parse() - SPARQL: " + sparqlQuery);
		String sql = null;
		try {
			parser.query();
			parser.getParseErrorHandler().throwQueryException();
			AST sparqlAst = parser.getAST();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			parser.showAst(sparqlAst, new PrintStream(baos));
			log.debug("SPARQL AST : \n" + baos.toString());
			log.debug(sparqlAst.toStringTree());

			SPARQLMariusQLWalker wSPARQL = new SPARQLMariusQLWalker(this.session);
			wSPARQL.unionSparqlQuery(sparqlAst);
			baos = new ByteArrayOutputStream();
			AST ontoqlAst = wSPARQL.getAST();
			wSPARQL.showAst(ontoqlAst, new PrintStream(baos));
			log.debug("MariusQL AST : " + baos.toString());

			MariusQLSQLWalker w = new MariusQLSQLWalker(this.session);
			w.statement(ontoqlAst);
			baos = new ByteArrayOutputStream();
			AST sqlAst = w.getAST();
			w.showAst(sqlAst, new PrintStream(baos));
			logAst("SQL AST : ", baos.toString());

			final MariusQLGenerator generator = this.getSession().getGenerator();
			generator.statement(sqlAst);
			sql = generator.getSQL();
			generator.getParseErrorHandler().throwQueryException();

			log.debug("SQL GEN:  " + sql);
			sqlString = sql;
			ResultSet sqlResult = sqlStatement.executeQuery(sqlString);
			return new MariusQLResultSetImpl(sqlResult, w.getExpressionInSelect());
		} catch (TokenStreamException exc) {
			throw new MariusQLException(exc);
		} catch (RecognitionException exc) {
			throw new MariusQLException(exc);
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public int executeUpdates(String mariusQL) throws MariusQLException {
		return this.executeUpdates(mariusQL, "\n");
	}

	@Override
	public int executeUpdates(String mariusQL, String endOfLines) throws MariusQLException {
		StringTokenizer strTkn = new StringTokenizer(mariusQL, "\n");
		int total = 0;
		while (strTkn.hasMoreTokens()) {
			String statement = strTkn.nextToken();

			log.debug(statement);
			total += this.executeUpdate(statement);
		}

		return total;
	}

	@Override
	public int executeUpdates(InputStream mariusQL) throws MariusQLException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(mariusQL));
		String line;
		int total = 0;
		try {
			while ((line = reader.readLine()) != null) {
				if (line != null && !line.trim().isEmpty()) {
					total += this.executeUpdate(line);
				}
			}
		} catch (IOException e) {
			throw new MariusQLException(e);
		}
		return total;
	}
}
