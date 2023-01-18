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
package fr.ensma.lias.mariusql.querycache;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.mariusql.AbstractMariusQLTest;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSet;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;

/**
 * @author Mickael BARON
 */
public class MariusQLQueryCacheTest extends AbstractMariusQLTest {

	private Logger log = LoggerFactory.getLogger(MariusQLQueryCacheTest.class);

	@Test
	public void testQueryCacheExecutionPerformance() throws SQLException {
		log.debug("MariusQLQueryCacheTest.testQueryCachePerformance()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		this.getSession().getModelCache().setEnabled(false);
		this.getSession().getMetaModelCache().setEnabled(false);
		this.getSession().getQueryCache().setEnabled(true);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS SuperClass (PROPERTIES (prop1 real, prop2 real, prop3 real, prop4 int, prop5 int, prop6 real, prop7 String, prop8 int, prop9 int, prop10 real))"));
		for (int i = 0; i < 100; i++) {
			Assert.assertEquals(1, statement.executeUpdate("CREATE #CLASS Class" + i + " under SuperClass"));
			Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Class" + i
					+ "(prop1, prop2, prop3, prop4, prop5, prop6, prop7, prop8, prop9, prop10)"));
		}

		long start = System.currentTimeMillis();
		MariusQLResultSet resultSet = statement.executeQuery(
				"SELECT rid, prop1, prop2, prop3, prop4, prop5, prop6, prop7, prop8, prop9, prop10 FROM SuperClass");
		Assert.assertFalse(resultSet.getMariusQLMetaData().isQueryFromCache());
		long finish = System.currentTimeMillis() - start;

		start = System.currentTimeMillis();
		resultSet = statement.executeQuery(
				"SELECT rid, prop1, prop2, prop3, prop4, prop5, prop6, prop7, prop8, prop9, prop10 FROM SuperClass");
		Assert.assertTrue(resultSet.getMariusQLMetaData().isQueryFromCache());
		Assert.assertEquals(11, resultSet.getMariusQLMetaData().getColumnCount());
		Assert.assertTrue((System.currentTimeMillis() - start) < finish);

		this.getSession().rollback();
	}

	@Test
	public void testQueryCacheSearchPerformance() throws SQLException {
		log.debug("MariusQLQueryCacheTest.testQueryCacheSearchPerformance()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		this.getSession().getModelCache().setEnabled(false);
		this.getSession().getMetaModelCache().setEnabled(false);
		this.getSession().getQueryCache().setEnabled(true);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS SuperClass (PROPERTIES (prop1 real, prop2 real, prop3 real, prop4 int, prop5 int, prop6 real, prop7 String, prop8 int, prop9 int, prop10 real))"));
		for (int i = 0; i < 10; i++) {
			Assert.assertEquals(1, statement.executeUpdate("CREATE #CLASS Class" + i + " under SuperClass"));
			Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Class" + i
					+ "(prop1, prop2, prop3, prop4, prop5, prop6, prop7, prop8, prop9, prop10)"));
		}

		MariusQLResultSet resultSet = statement.executeQuery(
				"SELECT rid, prop1, prop2, prop3, prop4, prop5, prop6, prop7, prop8, prop9, prop10 FROM SuperClass");
		Assert.assertFalse(resultSet.getMariusQLMetaData().isQueryFromCache());

		resultSet = statement.executeQuery("SELECT rid, prop1, prop2 FROM SuperClass");
		Assert.assertFalse(resultSet.getMariusQLMetaData().isQueryFromCache());
		Assert.assertEquals(3, resultSet.getMariusQLMetaData().getColumnCount());

		resultSet = statement.executeQuery("SELECT rid, prop2 FROM SuperClass");
		Assert.assertFalse(resultSet.getMariusQLMetaData().isQueryFromCache());
		Assert.assertEquals(2, resultSet.getMariusQLMetaData().getColumnCount());

		resultSet = statement.executeQuery("SELECT rid, prop3 FROM SuperClass");
		Assert.assertFalse(resultSet.getMariusQLMetaData().isQueryFromCache());
		Assert.assertEquals(2, resultSet.getMariusQLMetaData().getColumnCount());

		resultSet = statement.executeQuery("SELECT rid, prop4 FROM SuperClass");
		Assert.assertFalse(resultSet.getMariusQLMetaData().isQueryFromCache());
		Assert.assertEquals(2, resultSet.getMariusQLMetaData().getColumnCount());

		resultSet = statement.executeQuery("SELECT rid, prop5 FROM SuperClass");
		Assert.assertFalse(resultSet.getMariusQLMetaData().isQueryFromCache());
		Assert.assertEquals(2, resultSet.getMariusQLMetaData().getColumnCount());

		resultSet = statement.executeQuery("SELECT rid, prop1, prop2 FROM SuperClass");
		Assert.assertTrue(resultSet.getMariusQLMetaData().isQueryFromCache());
		Assert.assertEquals(3, resultSet.getMariusQLMetaData().getColumnCount());

		this.getSession().rollback();
	}
}
