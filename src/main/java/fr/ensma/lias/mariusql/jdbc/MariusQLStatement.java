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
package fr.ensma.lias.mariusql.jdbc;

import java.io.InputStream;

import fr.ensma.lias.mariusql.exception.MariusQLException;

/**
 * Defines the interface for executing a static OntoQL statement and returning
 * the results it produces.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 */
public interface MariusQLStatement {

	/**
	 * @return the SQL string generated.
	 */
	String getSQLString();

	/**
	 * Executes the given MariusQL statement, which may be an INSERT, UPDATE, or
	 * DELETE statement or a MarisQL statement that returns nothing, such as an
	 * OntoQL DDL statement.
	 * 
	 * @param ontoql an MariusQL DML or DDL statement.
	 * 
	 * @return either the row count for DML statements, or 0 for DDL statements
	 */
	int executeUpdate(String mariusQL) throws MariusQLException;

	/**
	 * Executes the given MariusQL statement, which may be a set of INSERT, UPDATE
	 * and DELETE statement or a MarisQL statement that returns nothing, such as an
	 * OntoQL DDL statement.
	 * 
	 * By default, each statement is separated by a '\n'.
	 * 
	 * @param mariusQL
	 * @return
	 * @throws MariusQLException
	 */
	int executeUpdates(String mariusQL) throws MariusQLException;

	/**
	 * Executes the given MariusQL statement, which may be a set of INSERT, UPDATE
	 * and DELETE statement or a MarisQL statement that returns nothing, such as an
	 * OntoQL DDL statement.
	 * 
	 * @param mariusQL
	 * @param endOfLines
	 * @return
	 * @throws MariusQLException
	 */
	int executeUpdates(String mariusQL, String endOfLines) throws MariusQLException;

	/**
	 * Executes the given MariusQL statement, which may be a set of INSERT, UPDATE
	 * and DELETE statement or a MarisQL statement that returns nothing, such as an
	 * OntoQL DDL statement.
	 * 
	 * @param mariusQL
	 * @return
	 * @throws MariusQLException
	 */
	int executeUpdates(InputStream mariusQL) throws MariusQLException;

	/**
	 * Executes the given MariusQL query statement (SELECT)
	 * 
	 * @param ontoql an MariusQL DQL statement.
	 * 
	 * @return the query result set.
	 */
	MariusQLResultSet executeQuery(String mariusQL) throws MariusQLException;

	/**
	 * Executes the given SPARQL statement, which returns a single
	 * <code>ResultSet</code> object.
	 * 
	 * @param sparqlQuery a SPARQL query
	 * @return a ResultSet object that contains the data produced by the given
	 *         query; never null
	 * @throws OntoQLException if a database access error occurs or the given SPARQL
	 *                         statement produces anything other than a single
	 *                         ResultSet object
	 */
	MariusQLResultSet executeSPARQLQuery(String sparqlQuery) throws MariusQLException;

	/**
	 * @throws MariusQLException
	 */
	void close() throws MariusQLException;
}