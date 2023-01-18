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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.mariusql.AbstractMariusQLTest;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;
import fr.ensma.lias.mariusql.util.MariusQLHelper;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * This test class is a particular Data Query Language (MQL) test dedicated for
 * Instance Quey Language (IQL).
 * 
 * @author Stéphane JEAN
 * @author Mickael BARON
 * @author Adel GHAMNIA
 */
public class MariusQLIQLTest extends AbstractMariusQLTest {

	private Logger log = LoggerFactory.getLogger(MariusQLMQLTest.class);

	@Test
	public void testIQLResultSetMetaData() throws SQLException {
		log.debug("MariusQLIQLTest.testIQLResultSetMetaData()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));

		final ResultSet executeQuery = statement
				.executeQuery("SELECT propertyIntFirst, propertyIntSecond FROM Vehicule");

		final ResultSetMetaData metaData = executeQuery.getMetaData();
		Assert.assertEquals(2, metaData.getColumnCount());
		Assert.assertEquals("propertyIntFirst", metaData.getColumnName(1));
		Assert.assertEquals("propertyIntSecond", metaData.getColumnName(2));

		Assert.assertTrue(executeQuery.next());
		Assert.assertEquals(0, executeQuery.getInt(1));
		Assert.assertEquals(1, executeQuery.getInt(2));
		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIQLSimpleProperties() {
		log.debug("MariusQLIQLTest.testIQLSimpleProperties()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));

		final ResultSet executeQuery = statement
				.executeQuery("SELECT propertyIntFirst, propertyIntSecond FROM Vehicule");
		try {
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(0, executeQuery.getInt(1));
			Assert.assertEquals(1, executeQuery.getInt(2));
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testIQLLimitClause() {
		log.debug("MariusQLIQLTest.testIQLLimitClause()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (1, 2)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (2, 3)"));

		ResultSet executeQuery = statement
				.executeQuery("SELECT propertyIntFirst, propertyIntSecond FROM Vehicule limit 2");
		try {
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(0, executeQuery.getInt(1));
			Assert.assertEquals(1, executeQuery.getInt(2));
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(1, executeQuery.getInt(1));
			Assert.assertEquals(2, executeQuery.getInt(2));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		}

		executeQuery = statement
				.executeQuery("SELECT propertyIntFirst, propertyIntSecond FROM Vehicule limit 2 offset 1");

		try {
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(1, executeQuery.getInt(1));
			Assert.assertEquals(2, executeQuery.getInt(2));
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(2, executeQuery.getInt(1));
			Assert.assertEquals(3, executeQuery.getInt(2));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testIQLSelectAlias() {
		log.debug("MariusQLIQLTest.testIQLSelectAlias()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));

		ResultSet executeQuery = statement
				.executeQuery("SELECT (propertyIntFirst) AS FIRST, (propertyIntSecond) as SECOND FROM Vehicule");

		try {
			ResultSetMetaData metaData = executeQuery.getMetaData();

			Assert.assertEquals("FIRST", metaData.getColumnName(1));
			Assert.assertEquals("SECOND", metaData.getColumnName(2));

		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testIQLSelectWithWhere() {
		log.debug("MariusQLIQLTest.testIQLSelectWithWhere()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 2)"));
		ResultSet executeQuery = statement
				.executeQuery("SELECT propertyIntFirst, propertyIntSecond FROM Vehicule where propertyIntFirst=0");

		try {
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(0, executeQuery.getInt(1));
			Assert.assertEquals(1, executeQuery.getInt(2));
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(0, executeQuery.getInt(1));
			Assert.assertEquals(2, executeQuery.getInt(2));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		}

		executeQuery = statement.executeQuery(
				"SELECT propertyIntFirst, propertyIntSecond FROM Vehicule where propertyIntFirst=0 and propertyIntSecond=1");
		try {
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(0, executeQuery.getInt(1));
			Assert.assertEquals(1, executeQuery.getInt(2));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testIQLSelectWithCount() {
		log.debug("MariusQLIQLTest.testIQLSelectWithCount()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 2)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (1, 2)"));
		ResultSet executeQuery = statement
				.executeQuery("SELECT COUNT(propertyIntFirst) FROM Vehicule where propertyIntFirst=0");

		try {
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(2, executeQuery.getInt(1));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		}

		executeQuery = statement.executeQuery("SELECT COUNT(*) FROM Vehicule");
		try {
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(3, executeQuery.getInt(1));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testIQLNumericOperators() {
		log.debug("MariusQLIQLTest.testIQLNumericOperators()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));

		ResultSet executeQuery = statement
				.executeQuery("SELECT abs(-2.2), sqrt(4) FROM Vehicule where propertyIntFirst=0");

		try {
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(2.2, executeQuery.getDouble(1), 0.0);
			Assert.assertEquals(2, executeQuery.getDouble(2), 0.0);
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testIQLSelectDistinct() {
		log.debug("MariusQLIQLTest.testIQLSelectDistinct()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (1, 2)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (1, 2)"));

		ResultSet executeQuery = statement.executeQuery("SELECT DISTINCT propertyIntFirst FROM Vehicule");

		try {
			ArrayList<Integer> res = new ArrayList<Integer>();
			Assert.assertTrue(executeQuery.next());
			res.add(executeQuery.getInt(1));
			Assert.assertTrue(executeQuery.next());
			res.add(executeQuery.getInt(1));
			Assert.assertFalse(executeQuery.next());

			Assert.assertTrue(res.contains(0));
			Assert.assertTrue(res.contains(1));

		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}
		statement.close();
	}

	@Test
	public void testIQLUnionExceptIntersectAllQueries() {
		log.debug("MariusQLIQLTest.testIQLUnionExceptIntersectAllQueries()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (1, 2)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (3, 4)"));

		ResultSet executeQuery = statement
				.executeQuery("SELECT propertyIntFirst FROM Vehicule UNION select propertyIntSecond from Vehicule");

		try {
			ArrayList<Integer> res = new ArrayList<Integer>();
			Assert.assertTrue(executeQuery.next());
			res.add(executeQuery.getInt(1));
			Assert.assertTrue(executeQuery.next());
			res.add(executeQuery.getInt(1));
			Assert.assertTrue(executeQuery.next());
			res.add(executeQuery.getInt(1));
			Assert.assertTrue(executeQuery.next());
			res.add(executeQuery.getInt(1));
			Assert.assertTrue(executeQuery.next());
			res.add(executeQuery.getInt(1));
			Assert.assertFalse(executeQuery.next());

			Assert.assertTrue(res.contains(0));
			Assert.assertTrue(res.contains(1));
			Assert.assertTrue(res.contains(2));
			Assert.assertTrue(res.contains(3));
			Assert.assertTrue(res.contains(4));
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		}

		executeQuery = statement
				.executeQuery("SELECT propertyIntFirst FROM Vehicule EXCEPT select propertyIntSecond from Vehicule");

		try {
			ArrayList<Integer> res = new ArrayList<Integer>();
			Assert.assertTrue(executeQuery.next());
			res.add(executeQuery.getInt(1));
			Assert.assertTrue(executeQuery.next());
			res.add(executeQuery.getInt(1));
			Assert.assertFalse(executeQuery.next());

			Assert.assertTrue(res.contains(0));
			Assert.assertTrue(res.contains(3));
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		}

		executeQuery = statement
				.executeQuery("SELECT propertyIntFirst FROM Vehicule INTERSECT select propertyIntSecond from Vehicule");

		try {
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(1, executeQuery.getInt(1));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}
		statement.close();
	}

	@Test
	public void testIQLInternalIdentifier() {
		log.debug("MariusQLIQLTest.testIQLInternalIdentifier()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (1, 3)"));

		ResultSet executeQuery = statement.executeQuery("SELECT rid FROM #class where #code = 'Vehicule'");
		String ridClass = null, ridProperty = null, query = null;
		try {
			Assert.assertTrue(executeQuery.next());
			ridClass = executeQuery.getString(1);
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		}

		executeQuery = statement.executeQuery("SELECT rid FROM #property where #code = 'propertyIntFirst'");

		try {
			Assert.assertTrue(executeQuery.next());
			ridProperty = executeQuery.getString(1);
			Assert.assertFalse(executeQuery.next());

			query = "select !" + ridProperty + " from !" + ridClass;
			executeQuery = statement.executeQuery(query);

			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(0, executeQuery.getInt(1));
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(1, executeQuery.getInt(1));
			Assert.assertFalse(executeQuery.next());

		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testIQLSelectRidField() throws SQLException {
		log.debug("MariusQLIQLTest.testIQLSelectRidField()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));

		ResultSet executeQuery = statement.executeQuery("SELECT rid FROM Vehicule");
		Assert.assertTrue(executeQuery.next());
		Assert.assertEquals("rid", executeQuery.getMetaData().getColumnLabel(1).toLowerCase());
		Assert.assertNotNull(executeQuery.getInt(1));
		Assert.assertFalse(executeQuery.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIQLStringOperators() {
		log.debug("MariusQLIQLTest.testIQLStringOperators()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));

		ResultSet executeQuery = statement.executeQuery(
				"SELECT upper('first') as a, lower('SECOND'), LENGTH('PACKAGE'), BIT_LENGTH('C_Vehicle') FROM Vehicule where propertyIntFirst = 0");

		try {
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals("FIRST", executeQuery.getString(1));
			Assert.assertEquals("second", executeQuery.getString(2));
			Assert.assertEquals(7, executeQuery.getInt(3));
			Assert.assertEquals(72, executeQuery.getInt(4));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testIQLStar() {
		log.debug("MariusQLIQLTest.testIQLStar()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));

		ResultSet executeQuery = statement.executeQuery("SELECT * from Vehicule");

		try {
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals("rid", executeQuery.getMetaData().getColumnName(1));
			Assert.assertEquals("propertyIntFirst", executeQuery.getMetaData().getColumnName(2));
			Assert.assertEquals(0, executeQuery.getInt(2));
			Assert.assertEquals("propertyIntSecond", executeQuery.getMetaData().getColumnName(3));
			Assert.assertEquals(1, executeQuery.getInt(3));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testIQLWithSimpleTypePropertiesWithAlias() {
		log.debug("MariusQLIQLTest.testIQLWithSimpleTypePropertiesWithAlias()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));

		ResultSet executeQuery = statement.executeQuery("select p.propertyIntFirst from Vehicule p");

		try {
			Assert.assertEquals(1, executeQuery.getMetaData().getColumnCount());
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals("propertyIntFirst", executeQuery.getMetaData().getColumnName(1));
			Assert.assertEquals(0, executeQuery.getInt(1));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		}

		executeQuery = statement.executeQuery("select p.propertyIntFirst from Vehicule as p");
		try {
			Assert.assertEquals(1, executeQuery.getMetaData().getColumnCount());
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals("propertyIntFirst", executeQuery.getMetaData().getColumnName(1));
			Assert.assertEquals(0, executeQuery.getInt(1));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		}

		executeQuery = statement.executeQuery("select p.propertyIntFirst, p.propertyIntSecond from Vehicule as p");
		try {
			Assert.assertEquals(2, executeQuery.getMetaData().getColumnCount());
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals("propertyIntFirst", executeQuery.getMetaData().getColumnName(1));
			Assert.assertEquals(0, executeQuery.getInt(1));
			Assert.assertEquals("propertyIntSecond", executeQuery.getMetaData().getColumnName(2));
			Assert.assertEquals(1, executeQuery.getInt(2));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}
		statement.close();
	}

	@Test
	public void testIQLSelectWithTypeOf() throws SQLException {
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));

		ResultSet executeQuery = statement.executeQuery("select v.TYPEOF_ID from Vehicule v");
		Assert.assertTrue(executeQuery.next());
		Long typeof = executeQuery.getLong(1);

		executeQuery = statement.executeQuery("select rid from #class where code = 'Vehicule'");
		Assert.assertTrue(executeQuery.next());
		Assert.assertEquals(executeQuery.getLong(1), typeof.longValue());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIQLSelectWithCaseISOFFunction() {
		log.debug("MariusQLIQLTest.testIQLSelectWithCaseISOFFunction()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));

		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Voiture UNDER Vehicule (PROPERTIES (propertyVoiture Int))"));
		Assert.assertEquals(1, statement
				.executeUpdate("CREATE EXTENT OF Voiture (propertyIntFirst, propertyIntSecond, propertyVoiture)"));
		Assert.assertEquals(1, statement.executeUpdate(
				"INSERT INTO Voiture (propertyIntFirst, propertyIntSecond, propertyVoiture) values (3, 4, 5)"));

		try {
			ResultSet executeQuery = statement.executeQuery(
					"select case when v IS OF (Vehicule) then 'YES' ELSE 'unknown type' END from Vehicule v");
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals("YES", executeQuery.getString(1));
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals("YES", executeQuery.getString(1));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet executeQuery = statement.executeQuery(
					"select case when v IS OF (Vehicule) then 'YES' ELSE 'unknown type' END from Voiture v");
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals("YES", executeQuery.getString(1));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet executeQuery = statement.executeQuery(
					"select case when v IS OF (Voiture) then 'YES' ELSE 'unknown type' END from Vehicule v");
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals("unknown type", executeQuery.getString(1));
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals("YES", executeQuery.getString(1));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet executeQuery = statement.executeQuery(
					"select case when v IS OF (Voiture) then 'YES' ELSE 'unknown type' END from Voiture v");
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals("YES", executeQuery.getString(1));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testIQLCaseExpression() {
		log.debug("MariusQLIQLTest.testIQLCaseExpression()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst) values (0)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (1, 2)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (2, 3)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (3, 4)"));

		try {
			ResultSet executeQuery = statement
					.executeQuery("select propertyIntFirst, case propertyIntFirst when '0' then 'Zero' "
							+ "when '1' then 'Un' when '2' then 'Deux' else 'Autre nombre'" + "END  from Vehicule");
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(0, executeQuery.getInt(1));
			Assert.assertEquals("Zero", executeQuery.getString(2));
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(1, executeQuery.getInt(1));
			Assert.assertEquals("Un", executeQuery.getString(2));
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(2, executeQuery.getInt(1));
			Assert.assertEquals("Deux", executeQuery.getString(2));
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(3, executeQuery.getInt(1));
			Assert.assertEquals("Autre nombre", executeQuery.getString(2));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet executeQuery = statement.executeQuery(
					"select propertyIntFirst, case when propertyIntFirst in (0, 1) then 'Valeur <= 1' when propertyIntFirst='2' then 'Deux' ELSE 'Autre nombre' END from Vehicule");
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(0, executeQuery.getInt(1));
			Assert.assertEquals("Valeur <= 1", executeQuery.getString(2));
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(1, executeQuery.getInt(1));
			Assert.assertEquals("Valeur <= 1", executeQuery.getString(2));
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(2, executeQuery.getInt(1));
			Assert.assertEquals("Deux", executeQuery.getString(2));
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(3, executeQuery.getInt(1));
			Assert.assertEquals("Autre nombre", executeQuery.getString(2));
			Assert.assertFalse(executeQuery.next());

		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet executeQuery = statement.executeQuery(
					"SELECT nullif(propertyIntFirst, '0'),nullif(propertyIntFirst, '1') FROM Vehicule where propertyIntFirst ='1'");
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(1, executeQuery.getInt(1));
			Assert.assertNull(executeQuery.getString(2));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet executeQuery = statement.executeQuery(
					"SELECT propertyIntFirst, COALESCE(propertyIntSecond, 5) FROM Vehicule where propertyIntFirst ='0'");
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(0, executeQuery.getInt(1));
			Assert.assertEquals(5, executeQuery.getInt(2));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet executeQuery = statement.executeQuery(
					"select case when propertyIntFirst in (0, 1) then 11 when propertyIntFirst='2' then 12 ELSE 13 END from Vehicule");
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(11, executeQuery.getInt(1));
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(11, executeQuery.getInt(1));
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(12, executeQuery.getInt(1));
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(13, executeQuery.getInt(1));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testIQLSelectWithInheritance() {
		log.debug("MariusQLIQLTest.testIQLSelectWithInheritance()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));

		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Voiture UNDER Vehicule (PROPERTIES (propertyVoiture Int))"));
		Assert.assertEquals(1, statement
				.executeUpdate("CREATE EXTENT OF Voiture (propertyIntFirst, propertyIntSecond, propertyVoiture)"));
		Assert.assertEquals(1, statement.executeUpdate(
				"INSERT INTO Voiture (propertyIntFirst, propertyIntSecond, propertyVoiture) values (3, 4, 5)"));

		try {
			ResultSet executeQuery = statement
					.executeQuery("select propertyIntFirst, propertyIntSecond from Voiture v");
			Assert.assertEquals(2, executeQuery.getMetaData().getColumnCount());
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(3, executeQuery.getInt(1));
			Assert.assertEquals(4, executeQuery.getInt(2));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet executeQuery = statement
					.executeQuery("select propertyIntFirst, propertyIntSecond from Vehicule v");
			Assert.assertEquals(2, executeQuery.getMetaData().getColumnCount());
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(0, executeQuery.getInt(1));
			Assert.assertEquals(1, executeQuery.getInt(2));
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(3, executeQuery.getInt(1));
			Assert.assertEquals(4, executeQuery.getInt(2));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet executeQuery = statement
					.executeQuery("select propertyIntFirst, propertyIntSecond from only(Vehicule) v");
			Assert.assertEquals(2, executeQuery.getMetaData().getColumnCount());
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(0, executeQuery.getInt(1));
			Assert.assertEquals(1, executeQuery.getInt(2));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet executeQuery = statement.executeQuery("select * from Vehicule v");
			Assert.assertEquals(3, executeQuery.getMetaData().getColumnCount());
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(0, executeQuery.getInt(2));
			Assert.assertEquals(1, executeQuery.getInt(3));
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(3, executeQuery.getInt(2));
			Assert.assertEquals(4, executeQuery.getInt(3));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testIQLInheritanceWithIncompleteSimpleTypeProperties() throws SQLException {
		log.debug("MariusQLIQLTest.testIQLInheritanceWithIncompleteSimpleTypeProperties()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		statement.executeUpdate(
				"CREATE #CLASS SuperClass (DESCRIPTOR (#name[fr] ='SuperClasse') PROPERTIES (prop1 STRING, prop2 STRING))");
		statement.executeUpdate("CREATE EXTENT OF SuperClass (prop1, prop2)");
		statement.executeUpdate("INSERT INTO SuperClass (prop1, prop2) values ('value1','value2')");

		statement.executeUpdate(
				"CREATE #CLASS SubClass UNDER SuperClass (DESCRIPTOR (#name[fr] ='SubClasse') PROPERTIES (prop3 STRING))");
		statement.executeUpdate("CREATE EXTENT OF SubClass (prop1, prop3)"); // prop2 is not defined.
		statement.executeUpdate("INSERT INTO SubClass (prop1, prop3) values ('value11', 'value31')");

		ResultSet executeQuery = statement.executeQuery("SELECT p.prop1, p.prop2 FROM SuperClass p");
		Assert.assertTrue(executeQuery.next());
		Assert.assertEquals("value1", executeQuery.getString(1));
		Assert.assertEquals("value2", executeQuery.getString(2));
		Assert.assertTrue(executeQuery.next());
		Assert.assertEquals("value11", executeQuery.getString(1));
		Assert.assertNull(executeQuery.getString(2));

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIQLResultsetMetadataOfReferencedProperties() throws SQLException {
		log.debug("MariusQLIQLTest.testIQLInheritanceWithIncompleteSimpleTypeProperties()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement.executeUpdate("create #class Motorcycle (" + "DESCRIPTOR (#code='Motorcycle')"
				+ "properties (\"number of wheels\" int, number String))"));

		Assert.assertEquals(1, statement.executeUpdate("create #class Person (" + "DESCRIPTOR (#code='Person')"
				+ "properties (age int, its_car REF(Motorcycle)))"));

		Assert.assertEquals(1, statement.executeUpdate("create extent of Motorcycle (\"number of wheels\", number)"));
		Assert.assertEquals(1, statement.executeUpdate("create extent of Person (its_car)"));

		ResultSet resultQuery = statement.executeQuery("select its_car.number from Person");
		Assert.assertEquals(1, resultQuery.getMetaData().getColumnCount());
		Assert.assertEquals("number", resultQuery.getMetaData().getColumnName(1));

		// Inherited Reference :
		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Student UNDER Person (DESCRIPTOR (#code ='Student') PROPERTIES (grade STRING))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Student (its_car, grade)"));
		resultQuery = statement.executeQuery("select its_car.number from Student");

		Assert.assertEquals(1, resultQuery.getMetaData().getColumnCount());
		Assert.assertEquals("number", resultQuery.getMetaData().getColumnName(1));

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIQLWithCollectionOfReference() throws SQLException {
		log.debug("MariusQLIQLTest.testIQLWithCollectionOfReference()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Person (name)"));

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Pink')"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Blue')"));

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (name string, owners REF(Person) ARRAY))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (owners)"));

		ResultSet resultset = statement.executeQuery("SELECT rid FROM Person");
		List<Long> owners = new ArrayList<Long>();
		while (resultset.next()) {
			owners.add(resultset.getLong(1));
		}
		Assert.assertEquals(2, owners.size());

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (owners) " + "values ("
				+ MariusQLHelper.getCollectionAssociationOntoQLValue(owners) + ")"));
		resultset = statement.executeQuery("SELECT owners.name FROM Vehicule");

		Assert.assertEquals(1, resultset.getMetaData().getColumnCount());
		Assert.assertEquals("name", resultset.getMetaData().getColumnName(1));
		Assert.assertTrue(resultset.next());
		Assert.assertEquals("Mr. Pink", resultset.getString(1));
		Assert.assertTrue(resultset.next());
		Assert.assertEquals("Mr. Blue", resultset.getString(1));
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();

		statement.close();
	}

	@Test
	public void testIQLWithReferenceProperties() throws SQLException {
		log.debug("MariusQLIQLTest.testIQLWithReferenceProperties()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		String queryOntoQL = "create #class Motorcycle (" + "DESCRIPTOR (#name[fr]='Moto', #code='Motorcycle')"
				+ "properties (\"number of wheels\" int, number String))";

		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL));

		queryOntoQL = "create #class Person (" + "DESCRIPTOR (#name[fr]='Personne', #code='Person')"
				+ "properties (age int, its_car REF(Motorcycle)))";
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL));

		queryOntoQL = "create extent of Motorcycle (\"number of wheels\", number)";
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL));

		queryOntoQL = "insert into Motorcycle (\"number of wheels\", number) values (4, '5255TH16')";
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL));

		queryOntoQL = "create extent of Person (its_car)";
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL));

		ResultSet resultQuery = statement.executeQuery("SELECT v.rid From Motorcycle v");
		Assert.assertTrue(resultQuery.next());
		String ridMotorcycle = resultQuery.getString(1);
		Assert.assertFalse(resultQuery.next());

		queryOntoQL = "insert into Person (its_car) values (" + ridMotorcycle + ")";

		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL));
		resultQuery = statement.executeQuery("select its_car.number from Person");

		try {
			ResultSetMetaData metaData = resultQuery.getMetaData();

			Assert.assertEquals(1, metaData.getColumnCount());
			Assert.assertEquals("number", metaData.getColumnName(1));

			resultQuery.next();
			Assert.assertEquals("5255TH16", resultQuery.getString(1));
		} catch (SQLException e) {
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		queryOntoQL = "select its_car from Person";
		resultQuery = statement.executeQuery(queryOntoQL);

		try {
			ResultSetMetaData metaData = resultQuery.getMetaData();

			Assert.assertEquals(1, metaData.getColumnCount());
			Assert.assertEquals("its_car", metaData.getColumnName(1));

			resultQuery.next();
			Assert.assertNotNull(resultQuery.getString(1));
		} catch (SQLException e) {
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIQLInheritanceWithReferenceProperties() throws SQLException {
		log.debug("MariusQLIQLTest.testIQLInheritanceWithReferenceProperties()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Address (DESCRIPTOR (#name[fr] ='Adresse') PROPERTIES (street STRING))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Address (street)"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Address (street) values ('Balco Street')"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Address (street) values ('Road Street')"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Address (street) values ('Scholar Street')"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Address (street) values ('Sunset')"));

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Person (DESCRIPTOR (#name[fr] ='Personne') PROPERTIES (address REF(Address)))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Person (address)"));
		Assert.assertEquals(1, statement.executeUpdate(
				"INSERT INTO Person (address) values ((SELECT rid from only(Address) where street='Sunset'))"));

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Student UNDER Person (DESCRIPTOR (#name[fr] ='Etudiant') PROPERTIES (grade STRING))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Student (address, grade)"));

		Assert.assertEquals(1, statement.executeUpdate(
				"INSERT INTO Student (address, grade) values ((SELECT rid from only(Address) where street='Road Street'), \'aaa\')"));

		ResultSet executeQuery = statement
				.executeQuery("SELECT p.address.street FROM Person p order by p.address.street asc");

		ResultSetMetaData metaData = executeQuery.getMetaData();

		Assert.assertEquals(1, metaData.getColumnCount());
		Assert.assertTrue(executeQuery.next());
		Assert.assertEquals("Road Street", executeQuery.getString(1));
		Assert.assertTrue(executeQuery.next());
		Assert.assertEquals("Sunset", executeQuery.getString(1));

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIQLWithSimpleTypePropertiesWithNestedSelect() throws SQLException {
		log.debug("MariusQLIQLTest.testIQLWithSimpleTypePropertiesWithNestedSelect()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (1, 2)"));

		ResultSet executeQuery = statement.executeQuery(
				"SELECT v.propertyIntFirst, v.propertyIntSecond FROM Vehicule v where v.propertyIntFirst = (Select count(e.propertyIntFirst) from Vehicule e where e.propertyIntSecond = 2) ");

		Assert.assertTrue(executeQuery.next());
		Assert.assertEquals(1, executeQuery.getInt(1));
		Assert.assertEquals(2, executeQuery.getInt(2));
		Assert.assertFalse(executeQuery.next());
		this.getSession().rollback();
		statement.close();
	}

//    @Test
//    public void testIQLSelectArrayToString() throws SQLException {
//	log.debug("MariusQLIQLTest.testIQLSelectArrayToString()");
//	    
//	this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
//	MariusQLStatement statement = this.getSession().createMariusQLStatement();
//
//	Assert.assertEquals(1, statement.executeUpdate("CREATE #CLASS Voiture (PROPERTIES (refenrencesPneumatique String ARRAY))"));
//	Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Voiture (refenrencesPneumatique)"));
//	Assert.assertEquals(1, statement.executeUpdate("insert into Voiture (refenrencesPneumatique) values (ARRAY['Michelin', 'Pirreli'])"));		
//	ResultSet executeQuery = statement.executeQuery("select refenrencesPneumatique from Voiture where upper(array_to_string(refenrencesPneumatique, ',')) like Upper('%Michel%')");
//
//	Assert.assertTrue(executeQuery.next());
//	Assert.assertEquals("{Michelin,Pirreli}",executeQuery.getString(1));
//	Assert.assertFalse(executeQuery.next());
//	
//	executeQuery = statement.executeQuery("select refenrencesPneumatique from Voiture where upper(array_to_string(refenrencesPneumatique, ',')) like Upper('%Michel')");
//	Assert.assertFalse(executeQuery.next());
//	
//	this.getSession().rollback();
//    }

	@Test
	public void testIQLWithReferencePropertiesWithAlias() throws SQLException {
		log.debug("MariusQLIQLTest.testIQLWithReferencePropertiesWithAlias()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		String queryOntoQL = "create #class Motorcycle (" + "DESCRIPTOR (#name[fr]='Moto', #code='Motorcycle')"
				+ "properties (\"number of wheels\" int, number String))";

		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL));

		queryOntoQL = "create #class Person (" + "DESCRIPTOR (#name[fr]='Personne', #code='Person')"
				+ "properties (age int, its_car REF(Motorcycle)))";
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL));

		queryOntoQL = "create extent of Motorcycle (\"number of wheels\", number)";
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL));

		queryOntoQL = "insert into Motorcycle (\"number of wheels\", number) values (4, '5255TH16')";
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL));

		queryOntoQL = "create extent of Person (its_car)";
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL));

		ResultSet resultQuery = statement.executeQuery("SELECT v.rid From Motorcycle v");
		Assert.assertTrue(resultQuery.next());
		String ridMotorcycle = resultQuery.getString(1);
		Assert.assertFalse(resultQuery.next());

		queryOntoQL = "insert into Person (its_car) values (" + ridMotorcycle + ")";

		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL));

		queryOntoQL = "select p.its_car.number from Person p";
		resultQuery = statement.executeQuery(queryOntoQL);

		try {
			ResultSetMetaData metaData = resultQuery.getMetaData();

			Assert.assertEquals(1, metaData.getColumnCount());
			Assert.assertEquals("number", metaData.getColumnName(1));

			resultQuery.next();
			Assert.assertEquals("5255TH16", resultQuery.getString(1));
		} catch (SQLException e) {
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		queryOntoQL = "select p.its_car.number from Person AS p";
		resultQuery = statement.executeQuery(queryOntoQL);

		try {
			ResultSetMetaData metaData = resultQuery.getMetaData();

			Assert.assertEquals(1, metaData.getColumnCount());
			Assert.assertEquals("number", metaData.getColumnName(1));

			resultQuery.next();
			Assert.assertEquals("5255TH16", resultQuery.getString(1));
		} catch (SQLException e) {
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		queryOntoQL = "select p.its_car from Person p";
		resultQuery = statement.executeQuery(queryOntoQL);

		try {
			ResultSetMetaData metaData = resultQuery.getMetaData();

			Assert.assertEquals(1, metaData.getColumnCount());
			Assert.assertEquals("its_car", metaData.getColumnName(1));

			resultQuery.next();
			Assert.assertNotNull(resultQuery.getString(1));
		} catch (SQLException e) {
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIQLSelectWithOrderBy() throws SQLException {
		log.debug("MariusQLIQLTest.testIQLSelectWithOrderBy()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (1, 2)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (2, 3)"));

		try {
			ResultSet executeQuery = statement.executeQuery("SELECT v.rid FROM Vehicule v order by rid asc ");

			Assert.assertTrue(executeQuery.next());
			int rid1 = executeQuery.getInt(1);

			Assert.assertTrue(executeQuery.next());
			int rid2 = executeQuery.getInt(1);

			Assert.assertTrue(executeQuery.next());
			int rid3 = executeQuery.getInt(1);

			Assert.assertTrue(rid1 < rid2);
			Assert.assertTrue(rid2 < rid3);
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet rs = statement.executeQuery("SELECT v.rid FROM Vehicule v order by rid desc");
			Assert.assertTrue(rs.next());
			int rid1 = rs.getInt(1);

			Assert.assertTrue(rs.next());
			int rid2 = rs.getInt(1);

			Assert.assertTrue(rs.next());
			int rid3 = rs.getInt(1);

			Assert.assertTrue(rid1 > rid2);
			Assert.assertTrue(rid2 > rid3);
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testIQLInheritanceClassWithExtent() throws SQLException {
		log.debug("MariusQLIQLTest.testIQLInheritanceClassWithExtent()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Male UNDER Person (DESCRIPTOR (#code = 'Male'))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Female UNDER Person (DESCRIPTOR (#code = 'Female'))"));

		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Person (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Male (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Female (name)"));

		ResultSet resultSet = statement.executeQuery("SELECT rid, name FROM Person");
		Assert.assertEquals(2, resultSet.getMetaData().getColumnCount());
		Assert.assertEquals("rid", resultSet.getMetaData().getColumnName(1));
		Assert.assertFalse(resultSet.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIQLInheritanceClassWithExtentAndDifferentUsedProperties() throws SQLException {
		log.debug("MariusQLIQLTest.testIQLInheritanceClassWithExtentAndDifferentUsedProperties()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS World (PROPERTIES (prop1 real, prop2 real, prop3 real))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Bi under World (PROPERTIES (prop4 real, prop5 real))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE #CLASS Male under Bi"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE #CLASS Female under Bi"));

		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Male (prop1)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Female (prop1, prop2, prop3, prop4, prop5)"));

		ResultSet resultSet = statement.executeQuery("SELECT rid, prop1, prop2, prop3, prop4, prop5 FROM Bi");
		Assert.assertEquals(6, resultSet.getMetaData().getColumnCount());
		Assert.assertEquals("rid", resultSet.getMetaData().getColumnName(1));
		Assert.assertEquals("prop1", resultSet.getMetaData().getColumnName(2));
		Assert.assertEquals("prop2", resultSet.getMetaData().getColumnName(3));
		Assert.assertEquals("prop3", resultSet.getMetaData().getColumnName(4));
		Assert.assertEquals("prop4", resultSet.getMetaData().getColumnName(5));
		Assert.assertEquals("prop5", resultSet.getMetaData().getColumnName(6));
		Assert.assertFalse(resultSet.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIQLInheritanceClassWithoutExtent() throws SQLException {
		log.debug("MariusQLIQLTest.testIQLInheritanceClassWithoutExtent()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS World (DESCRIPTOR (#code = 'World') PROPERTIES (name string))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Person UNDER World (DESCRIPTOR (#code = 'Person'))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Male UNDER Person (DESCRIPTOR (#code = 'Male'))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Female UNDER Person (DESCRIPTOR (#code = 'Female'))"));

		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Male (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Female (name)"));

		ResultSet resultSet = statement.executeQuery("SELECT rid, name FROM World");
		Assert.assertEquals(2, resultSet.getMetaData().getColumnCount());
		Assert.assertEquals("rid", resultSet.getMetaData().getColumnName(1));
		Assert.assertFalse(resultSet.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIQLClassWithoutExtent() throws SQLException {
		log.debug("MariusQLIQLTest.testIQLClassWithoutExtent()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		ResultSet resultSet = statement.executeQuery("SELECT rid, name FROM Person");
		Assert.assertEquals(2, resultSet.getMetaData().getColumnCount());
		Assert.assertEquals("rid", resultSet.getMetaData().getColumnName(1));
		Assert.assertFalse(resultSet.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIQLTableAliasOnProperties() throws SQLException {
		log.debug("MariusQLIQLTest.testIQLTableAliasOnProperties()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (1, 2)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (2, 3)"));

		ResultSet executeQuery = statement.executeQuery("SELECT rid FROM #class where #code = 'Vehicule' ");
		Assert.assertTrue(executeQuery.next());

		String ridClass = executeQuery.getString(1);

		String tableAlias = StringHelper.generateAlias(ridClass, 0);

		executeQuery = statement.executeQuery("SELECT v.rid FROM Vehicule v order by rid asc ");
		String sqlQueryGenerated = statement.getSQLString();

		// We ensure that the 2 rid are prefixed by tableAlias eg: g4532_.rid
		// the first one
		Assert.assertTrue(sqlQueryGenerated.contains(tableAlias + ".rid"));
		sqlQueryGenerated = sqlQueryGenerated.replace("select " + tableAlias + ".rid from", "");
		// the second one
		Assert.assertTrue(sqlQueryGenerated.contains(tableAlias + ".rid"));
		sqlQueryGenerated = sqlQueryGenerated.replace(tableAlias + ".rid", "");

		// Now, there is not another rid!
		Assert.assertFalse(sqlQueryGenerated.contains(tableAlias + ".rid"));

		// but, there is the alias of the table (from () g4532_);
		Assert.assertTrue(sqlQueryGenerated.contains(tableAlias));

		this.getSession().rollback();
		statement.close();
	}
}
