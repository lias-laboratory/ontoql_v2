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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.mariusql.AbstractMariusQLTest;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.model.MClass;
import fr.ensma.lias.mariusql.core.model.MGenericClass;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSet;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;

/**
 * This test class is a particular Model Query Language (MQL) test dedicated for
 * Model Quey Language (MQL).
 * 
 * @author Stéphane JEAN
 * @author Mickael BARON
 * @author Adel GHAMNIA
 * @author Valentin CASSAIR
 */
public class MariusQLMQLTest extends AbstractMariusQLTest {

	private Logger log = LoggerFactory.getLogger(MariusQLMQLTest.class);

	@Test
	public void testMQLWithSeveralErrors() {
		log.debug("MariusQLMQLTest.testMQLWithSeveralErrors()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		statement.executeUpdate("CREATE #CLASS BB3DFD4 (DESCRIPTOR (#code = 'BB3DFD4'))");

		try {
			statement.executeQuery("select #name from #class");
		} catch (MariusQLException e) {
			Assert.assertEquals(e.getMessage(), "Attribute is not defined:name");
		}

		try {
			statement.executeQuery("select #definition from #class");
		} catch (MariusQLException e) {
			Assert.assertEquals(e.getMessage(), "Attribute is not defined:definition");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testMQLSelectAlias() throws SQLException {
		log.debug("MariusQLMQLTest.testMQLSelectAlias()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		statement.executeUpdate("CREATE #CLASS  BB3DFD4 (DESCRIPTOR (#code = 'BB3DFD4'))");
		try {
			ResultSet resultSetObtenu = statement.executeQuery("SELECT #CODE AS LEFIRSTNAME from #CLASS");
			ResultSetMetaData metaData = resultSetObtenu.getMetaData();
			Assert.assertEquals("LEFIRSTNAME", metaData.getColumnName(1));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testMQLSelectWithSeveralAttributes() throws SQLException {
		log.debug("MariusQLMQLTest.testMQLSelectWithSeveralAttributes()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		statement.executeUpdate(
				"CREATE #CLASS C_Vehicle ( " + "DESCRIPTOR (#code = 'C_Vehicle', #package='package') " + ")");
		try {
			ResultSet rs = statement.executeQuery("select #package AS P, #code AS C from #class");
			ResultSetMetaData metaData = rs.getMetaData();
			Assert.assertTrue(rs.next());
			Assert.assertEquals("package", rs.getString(1));
			Assert.assertEquals("C_Vehicle", rs.getString(2));
			Assert.assertEquals("P", metaData.getColumnName(1));
			Assert.assertEquals("C", metaData.getColumnName(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testMQLSelectWithWhere() throws SQLException {
		log.debug("MariusQLMQLTest.testMQLSelectWithWhere()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		statement.executeUpdate(
				"CREATE #CLASS C_Vehicle ( " + "DESCRIPTOR (#code = 'C_Vehicle', #package='package') " + ")");
		try {
			ResultSet rs = statement.executeQuery("select #package, #code from #class where #package = 'package' ");
			Assert.assertTrue(rs.next());
			Assert.assertEquals("package", rs.getString(1));
			Assert.assertEquals("C_Vehicle", rs.getString(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet rs = statement.executeQuery(
					"select #package, #code from #class where #package = 'package' AND #code = 'C_Vehicle'");
			Assert.assertTrue(rs.next());
			Assert.assertEquals("package", rs.getString(1));
			Assert.assertEquals("C_Vehicle", rs.getString(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet rs = statement.executeQuery(
					"select #package, #code from #class where #package = 'package' OR #code = 'C_Vehicle'");
			Assert.assertTrue(rs.next());
			Assert.assertEquals("package", rs.getString(1));
			Assert.assertEquals("C_Vehicle", rs.getString(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testMQLSelectWithLike() {
		log.debug("MariusQLMQLTest.testMQLSelectWithLike()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		statement.executeUpdate(
				"CREATE #CLASS C_Vehicle ( " + "DESCRIPTOR (#code = 'C_Vehicle', #package='package') " + ")");

		try {
			ResultSet rs = statement.executeQuery("select #package, #code from #class where #package like 'package' ");
			Assert.assertTrue(rs.next());
			Assert.assertEquals("package", rs.getString(1));
			Assert.assertEquals("C_Vehicle", rs.getString(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet rs = statement.executeQuery("select #package, #code from #class where #package like '%cka%' ");
			Assert.assertTrue(rs.next());
			Assert.assertEquals("package", rs.getString(1));
			Assert.assertEquals("C_Vehicle", rs.getString(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet rs = statement.executeQuery("select #package, #code from #class where #code like 'C_V%' ");
			Assert.assertTrue(rs.next());
			Assert.assertEquals("package", rs.getString(1));
			Assert.assertEquals("C_Vehicle", rs.getString(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet rs = statement.executeQuery("select #package, #code from #class where #code like '%icle' ");
			Assert.assertTrue(rs.next());
			Assert.assertEquals("package", rs.getString(1));
			Assert.assertEquals("C_Vehicle", rs.getString(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet rs = statement.executeQuery("select #package, #code from #class where #package like '%pka%'");
			Assert.assertFalse(rs.next());
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet rs = statement.executeQuery("select #package, #code from #class where #package like '%ka'");
			Assert.assertFalse(rs.next());
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet rs = statement.executeQuery("select #package, #code from #class where #code like 'Vehic%'");
			Assert.assertFalse(rs.next());
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		statement.close();
	}

	@Test
	public void testMQLSelectWithUpper() throws SQLException {
		log.debug("MariusQLMQLTest.testMQLSelectWithUpper()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		statement.executeUpdate(
				"CREATE #CLASS C_Vehicle ( " + "DESCRIPTOR (#code = 'C_Vehicle', #package='package') " + ")");
		try {
			ResultSet rs = statement.executeQuery("select UPPER(#package) , UPPER(#code) from #class");
			Assert.assertTrue(rs.next());
			Assert.assertEquals("PACKAGE", rs.getString(1));
			Assert.assertEquals("C_VEHICLE", rs.getString(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet rs = statement.executeQuery("select #package, UPPER(#code) from #class");
			Assert.assertTrue(rs.next());
			Assert.assertEquals("package", rs.getString(1));
			Assert.assertEquals("C_VEHICLE", rs.getString(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet rs = statement.executeQuery("select UPPER(#package), #code from #class");
			Assert.assertTrue(rs.next());
			Assert.assertEquals("PACKAGE", rs.getString(1));
			Assert.assertEquals("C_Vehicle", rs.getString(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testMQLSelectWithLower() throws SQLException {
		log.debug("MariusQLMQLTest.testMQLSelectWithLower()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		statement.executeUpdate(
				"CREATE #CLASS C_Vehicle ( " + "DESCRIPTOR (#code = 'C_Vehicle', #package='PACKAGE') " + ")");

		try {
			ResultSet rs = statement.executeQuery("select #package, LOWER(#code) from #class");
			Assert.assertTrue(rs.next());
			Assert.assertEquals("PACKAGE", rs.getString(1));
			Assert.assertEquals("c_vehicle", rs.getString(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet rs = statement.executeQuery("select LOWER(#package), #code from #class");
			Assert.assertTrue(rs.next());
			Assert.assertEquals("package", rs.getString(1));
			Assert.assertEquals("C_Vehicle", rs.getString(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet rs = statement.executeQuery("select LOWER(#package), LOWER(#code) from #class");
			Assert.assertTrue(rs.next());
			Assert.assertEquals("package", rs.getString(1));
			Assert.assertEquals("c_vehicle", rs.getString(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testMQLSelectWithLength() throws SQLException {
		log.debug("MariusQLMQLTest.testMQLSelectWithLength()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		statement.executeUpdate(
				"CREATE #CLASS C_Vehicle ( " + "DESCRIPTOR (#code = 'C_Vehicle', #package='PACKAGE') " + ")");

		try {
			ResultSet rs = statement.executeQuery("select LENGTH(#package), #code from #class");
			Assert.assertTrue(rs.next());
			Assert.assertEquals(7, rs.getInt(1));
			Assert.assertEquals("C_Vehicle", rs.getString(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testMQLSelectWithBitLength() throws SQLException {
		log.debug("MariusQLMQLTest.testMQLSelectWithBitLength()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		statement.executeUpdate(
				"CREATE #CLASS C_Vehicle ( " + "DESCRIPTOR (#code = 'C_Vehicle', #package='PACKAGE') " + ")");

		try {
			ResultSet resl = statement.executeQuery("select #package from #class");
			Assert.assertTrue(resl.next());
			Assert.assertEquals("PACKAGE", resl.getString(1));

			ResultSet rs = statement.executeQuery("select BIT_LENGTH(#package), BIT_LENGTH(#code) from #class");
			Assert.assertTrue(rs.next());
			Assert.assertEquals(56, rs.getInt(1));
			Assert.assertEquals(72, rs.getInt(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testMQLSelectWithCount() throws SQLException {
		log.debug("MariusQLMQLTest.testMQLSelectWithCount()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		statement.executeUpdate(
				"CREATE #CLASS C_Vehicle ( " + "DESCRIPTOR (#code = 'C_Vehicle', #package='PACKAGE') " + ")");
		statement.executeUpdate(
				"CREATE #CLASS C_Vehicle2 ( " + "DESCRIPTOR (#code = 'C_Vehicle2', #package='PACKAGE') " + ")");

		try {
			ResultSet rs = statement.executeQuery("select COUNT(code), COUNT(package) from #class");
			Assert.assertTrue(rs.next());
			Assert.assertEquals(2, rs.getInt(1));
			Assert.assertEquals(2, rs.getInt(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}
		statement.executeUpdate(
				"CREATE #CLASS C_Vehicle3 ( " + "DESCRIPTOR (#code = 'C_Vehicle3', #package='PACKAGE') " + ")");

		try {
			ResultSet rs = statement.executeQuery("select COUNT(*) from #class");
			Assert.assertTrue(rs.next());
			Assert.assertEquals(3, rs.getInt(1));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testMQLSelectWithSum() throws SQLException {
		log.debug("MariusQLMQLTest.testMQLSelectWithSum()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		statement.executeUpdate(
				"CREATE #CLASS C_Vehicle ( " + "DESCRIPTOR (#code = 'C_Vehicle', #package='PACKAGE') " + ")");

		statement.executeUpdate(
				"CREATE #CLASS C_Vehicle2 ( " + "DESCRIPTOR (#code = 'C_Vehicle2', #package='PACKAGE') " + ")");

		try {
			ResultSet rs = statement.executeQuery("select #rid from #class");
			Assert.assertTrue(rs.next());
			int rid1 = rs.getInt(1);
			Assert.assertTrue(rs.next());
			int rid2 = rs.getInt(1);
			rs = statement.executeQuery("select SUM(#rid) from #class");
			Assert.assertTrue(rs.next());
			Assert.assertEquals((rid1 + rid2), rs.getInt(1));
			;
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testMQLSelectWithAvg() throws SQLException {
		log.debug("MariusQLMQLTest.testMQLSelectWithAvg()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		statement.executeUpdate(
				"CREATE #CLASS C_Vehicle ( " + "DESCRIPTOR (#code = 'C_Vehicle', #package='PACKAGE') " + ")");

		statement.executeUpdate(
				"CREATE #CLASS C_Vehicle2 ( " + "DESCRIPTOR (#code = 'C_Vehicle2', #package='PACKAGE') " + ")");

		try {
			ResultSet rs = statement.executeQuery("select #rid from #class");
			Assert.assertTrue(rs.next());
			int rid1 = rs.getInt(1);
			Assert.assertTrue(rs.next());
			int rid2 = rs.getInt(1);
			rs = statement.executeQuery("select AVG(#rid) from #class");
			Assert.assertTrue(rs.next());
			Assert.assertEquals(((rid1 + rid2) / 2), rs.getInt(1));
			;
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testMQLSelectStar() throws SQLException {
		log.debug("MariusQLMQLTest.testMQLStar()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		statement.executeUpdate("CREATE #CLASS BB3DFD4 (DESCRIPTOR (#code = 'BB3DFD4'))");
		statement.executeUpdate("CREATE #CLASS BB3DFD5 (DESCRIPTOR (#code = 'BB3DFD5', #dtype='string'))");

		try {
			ResultSet res = statement.executeQuery("SELECT * from #CLASS");
			ResultSetMetaData metaData = res.getMetaData();

			Assert.assertTrue(res.next());
			Assert.assertEquals(10, metaData.getColumnCount());

			Set<String> values = new HashSet<String>();
			values.add(metaData.getColumnLabel(1).toLowerCase());
			values.add(metaData.getColumnLabel(2).toLowerCase());
			values.add(metaData.getColumnLabel(3).toLowerCase());
			values.add(metaData.getColumnLabel(4).toLowerCase());
			values.add(metaData.getColumnLabel(5).toLowerCase());
			values.add(metaData.getColumnLabel(6).toLowerCase());
			values.add(metaData.getColumnLabel(7).toLowerCase());
			values.add(metaData.getColumnLabel(8).toLowerCase());
			values.add(metaData.getColumnLabel(9).toLowerCase());
			values.add(metaData.getColumnLabel(10).toLowerCase());

			Set<String> expectedValues = new HashSet<String>();
			expectedValues.add("rid");
			expectedValues.add("code");
			expectedValues.add("dtype");
			expectedValues.add("package");
			expectedValues.add("superclass");
			expectedValues.add("directproperties");
			expectedValues.add("usedproperties");
			expectedValues.add("isextension");
			expectedValues.add("name_en");
			expectedValues.add("definition_en");

			Assert.assertEquals(expectedValues, values);
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testMQLLimitClause() throws SQLException {
		log.debug("MariusQLMQLTest.testMQLLimitClause()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		statement.executeUpdate("CREATE #CLASS BB3DFD4 (DESCRIPTOR (#code ='BB3DFD4')))");
		statement.executeUpdate("CREATE #CLASS BB3DFD5 (DESCRIPTOR (#code ='BB3DFD5')))");
		statement.executeUpdate("CREATE #CLASS BB3DFD6 (DESCRIPTOR (#code ='BB3DFD6')))");

		try {
			ResultSet res = statement.executeQuery("SELECT code from #CLASS limit 2");

			ArrayList<String> listCode = new ArrayList<String>();
			listCode.add("BB3DFD4");
			listCode.add("BB3DFD5");
			listCode.add("BB3DFD6");
			Assert.assertTrue(res.next());
			Assert.assertTrue(listCode.contains(res.getString("code")));
			Assert.assertTrue(res.next());
			Assert.assertTrue(listCode.contains(res.getString("code")));
			Assert.assertFalse(res.next());

			res = statement.executeQuery("SELECT code from #CLASS limit 2 offset 1");

			Assert.assertTrue(res.next());
			Assert.assertTrue(listCode.contains(res.getString("code")));
			Assert.assertTrue(res.next());
			Assert.assertTrue(listCode.contains(res.getString("code")));
			Assert.assertFalse(res.next());
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testMQLWithSimpleTypeAttributesUsingNameSpace() {
		log.debug("MariusQLMQLTest.testMQLWithSimpleTypeAttributesUsingNameSpace()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		// Create a class without a superclass
		this.getSession().setDefaultNameSpace("http://www.lias-lab.fr");
		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		String query = "CREATE #CLASS BB3DFD4 (DESCRIPTOR (#code ='BB3DFD4', package='http://www.lias-lab.fr')))";
		statement.executeUpdate(query);

		String queryMariusQL = "SELECT code from #class using namespace 'http://www.lias-lab.fr'";
		ResultSet executeQuery = statement.executeQuery(queryMariusQL);

		try {
			ResultSetMetaData metaData = executeQuery.getMetaData();

			int numCols = metaData.getColumnCount();
			Assert.assertEquals(1, numCols);
			Assert.assertEquals("code", metaData.getColumnLabel(1).toLowerCase());
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals("BB3DFD4", executeQuery.getString("code"));
		} catch (SQLException e) {
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		queryMariusQL = "SELECT code from #class using namespace 'http://www.lisi.ensma.fr'";

		try {
			executeQuery = statement.executeQuery(queryMariusQL);
			ResultSetMetaData metaData = executeQuery.getMetaData();

			int numCols = metaData.getColumnCount();
			Assert.assertEquals(1, numCols);
			Assert.assertEquals("code", metaData.getColumnLabel(1).toLowerCase());
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			Assert.fail("Don't throw an exception: " + e.getMessage());
		} finally {
			this.getSession().rollback();
		}

		// Ensure that the condition WHER package = NAMESPACE is added on the query

		queryMariusQL = "SELECT c.code, p.code from #class c, #property p where p.scope = c.rid using namespace 'http://www.lisi.ensma.fr'";
		try {
			executeQuery = statement.executeQuery(queryMariusQL);
			ResultSetMetaData metaData = executeQuery.getMetaData();

			int numCols = metaData.getColumnCount();
			Assert.assertEquals(2, numCols);
			Assert.assertEquals("code", metaData.getColumnLabel(1).toLowerCase());
			Assert.assertEquals("code", metaData.getColumnLabel(2).toLowerCase());
			Assert.assertFalse(executeQuery.next());

			String sqlQuery = statement.getSQLString();
			Assert.assertTrue(sqlQuery.contains("package='http://www.lisi.ensma.fr'"));
		} catch (SQLException e) {
			Assert.fail("Don't throw an exception: " + e.getMessage());
		} finally {
			this.getSession().rollback();
		}

		queryMariusQL = "SELECT c.code, p.code from #class c, #property p where p.scope = c.rid order by rid asc";
		try {
			executeQuery = statement.executeQuery(queryMariusQL);
			ResultSetMetaData metaData = executeQuery.getMetaData();

			int numCols = metaData.getColumnCount();
			Assert.assertEquals(2, numCols);
			Assert.assertEquals("code", metaData.getColumnLabel(1).toLowerCase());
			Assert.assertEquals("code", metaData.getColumnLabel(2).toLowerCase());
			Assert.assertFalse(executeQuery.next());

			String sqlQuery = statement.getSQLString();
			Assert.assertTrue(sqlQuery.contains("package='http://www.lias-lab.fr'"));
		} catch (SQLException e) {
			Assert.fail("Don't throw an exception: " + e.getMessage());
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testMQLWithMAXandMIN() {
		log.debug("MariusQLMQLTest.testMQLWithMAXandMIN");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		statement.executeUpdate("CREATE #CLASS BB3DFD4 (DESCRIPTOR (#code ='BB3DFD4')))");
		statement.executeUpdate("CREATE #CLASS BB3DFD5 (DESCRIPTOR (#code ='BB3DFD5')))");

		try {
			ResultSet resultset = statement.executeQuery("SELECT rid from #class where #code = 'BB3DFD4'");
			Assert.assertTrue(resultset.next());
			Long rid1 = resultset.getLong(1);

			resultset = statement.executeQuery("SELECT rid from #class where #code = 'BB3DFD5'");
			Assert.assertTrue(resultset.next());
			Long rid2 = resultset.getLong(1);

			resultset = statement.executeQuery("SELECT max(#rid) AS maxRid, min(#rid) AS minRid from #class");

			Assert.assertTrue(resultset.next());
			ResultSetMetaData metaData = resultset.getMetaData();
			Assert.assertEquals(2, metaData.getColumnCount());
			Assert.assertEquals("maxRid", metaData.getColumnName(1));
			Assert.assertEquals("minRid", metaData.getColumnName(2));

			Assert.assertEquals(Math.max(rid1, rid2), resultset.getInt(1));
			Assert.assertEquals(Math.min(rid1, rid2), resultset.getInt(2));
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testMQLSelectWithCaseIsOfOnlyFunctionForDTypeIdentification() throws SQLException {
		log.debug("MariusQLMQLTest.testMQLSelectWithCaseIsOfFunctionForDTypeIdentification()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int result = statement.executeUpdate(
				"CREATE #CLASS Voiture (DESCRIPTOR (#name = 'Voiture', #code = 'code1') properties (prop1 int, prop2 String))");
		Assert.assertEquals(1, result);

		MariusQLResultSet rset = statement.executeQuery("select case " + "when d IS OF (only #int) then '1' "
				+ "when d IS OF (only #string) then '2' ELSE '0' END "
				+ "from #property as p, #datatype as d where p.#range = d.#rid");

		List<String> values = new ArrayList<String>();
		while (rset.next()) {
			values.add(rset.getString(1));
		}

		Assert.assertTrue(values.contains("1"));
		Assert.assertTrue(values.contains("2"));
	}

	@Test
	public void testMQLSelectWithCaseISOFFunction() throws SQLException {
		log.debug("MariusQLMQLTest.testMQLSelectWithCaseISOFFunction()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		statement.executeUpdate("CREATE #CLASS RefClass (DESCRIPTOR (#name = 'RefClass'))");
		statement.executeUpdate("" + "CREATE #CLASS Voiture (DESCRIPTOR (#name = 'Voiture') "
				+ "properties (prop1 String, " + "prop2 int, " + "prop3 real, " + "prop4 boolean, " + "prop5 uritype, "
				+ "prop6 ENUM ('NOIR','ROUGE','BLANC'), " + "prop7 int array, " + "prop8 ref(RefClass) array, "
				+ "prop9 ref(RefClass)))");

		MariusQLResultSet rset = statement.executeQuery("select #rid, #code from #property");
		Map<String, Integer> properties = new HashMap<String, Integer>();

		while (rset.next()) {
			properties.put(rset.getString(2), rset.getInt(1));
		}

		for (String currentProperty : properties.keySet()) {
			String query = "select case " + "when d IS OF (only #string) then 1 " + "when d IS OF (only #int) then 2 "
					+ "when d IS OF (only #real) then 3 " + "when d IS OF (only #boolean) then 4 "
					+ "when d IS OF (only #uritype) then 5 " + "when d IS OF (only #enum) then 6 "
					+ "when d IS OF (only #array) then 7 " + "when d IS OF (only #ref) then 8 else 0 end, "
					+ "p.#range " + "from #Property AS p, #DataType AS d " + "where p.#rid = "
					+ properties.get(currentProperty) + " AND p.#range=d.#rid";
			ResultSet rs = statement.executeQuery(query);
			Assert.assertTrue(rs.next());

			if (currentProperty.equals("prop1")) {
				Assert.assertEquals(1, rs.getInt(1));
			} else if (currentProperty.equals("prop2")) {
				Assert.assertEquals(2, rs.getInt(1));
			} else if (currentProperty.equals("prop3")) {
				Assert.assertEquals(3, rs.getInt(1));
			} else if (currentProperty.equals("prop4")) {
				Assert.assertEquals(4, rs.getInt(1));
			} else if (currentProperty.equals("prop5")) {
				Assert.assertEquals(5, rs.getInt(1));
			} else if (currentProperty.equals("prop6")) {
				Assert.assertEquals(6, rs.getInt(1));
			} else if (currentProperty.equals("prop7")) {
				Assert.assertEquals(7, rs.getInt(1));
			} else if (currentProperty.equals("prop8")) {
				Assert.assertEquals(7, rs.getInt(1));
			} else if (currentProperty.equals("prop9")) {
				Assert.assertEquals(8, rs.getInt(1));
			} else {
				Assert.fail("Must be defined.");
			}
		}
	}

	@Test
	public void testMQLSelectWithOrderBy() throws SQLException {
		log.debug("MariusQLMQLTest.testMQLSelectWithOrderBy()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		statement.executeUpdate(
				"CREATE #CLASS C_Vehicle ( " + "DESCRIPTOR (#code = 'C_Vehicle', #package='PACKAGE') " + ")");

		statement.executeUpdate(
				"CREATE #CLASS C_Vehicle2 ( " + "DESCRIPTOR (#code = 'C_Vehicle2', #package='PACKAGE') " + ")");

		statement.executeUpdate(
				"CREATE #CLASS C_Vehicle3 ( " + "DESCRIPTOR (#code = 'C_Vehicle3', #package='PACKAGE') " + ")");

		try {
			ResultSet rs = statement.executeQuery("select #rid from #class ORDER BY #rid asc");
			Assert.assertTrue(rs.next());
			int rid1 = rs.getInt(1);

			Assert.assertTrue(rs.next());
			int rid2 = rs.getInt(1);

			Assert.assertTrue(rs.next());
			int rid3 = rs.getInt(1);

			Assert.assertTrue(rid1 < rid2);
			Assert.assertTrue(rid2 < rid3);
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet rs = statement.executeQuery("select #rid from #class ORDER BY #rid desc");
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
	public void testMQLSelectWithInheritance() throws SQLException {
		log.debug("MariusQLMQLTest.testMQLSelectWithInheritance()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule', #package='package'))"));
		MGenericClass mClass = FactoryCore.createExistingMGenericClass(getSession(), "Vehicule");
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Car UNDER Vehicule (DESCRIPTOR (#code = 'Car' ))"));

		ResultSet executeQuery = statement
				.executeQuery("select superclass from #class where #code = 'Car' ORDER BY #rid asc");

		try {
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals(mClass.getInternalId().toString(), executeQuery.getString("superclass"));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}
		statement.close();
	}

	@Test
	public void testMQLWithMultiLanguageAttribute() {
		log.debug("MariusQLMQLTest.testMQLWithMultiLanguageAttribute()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		statement.executeUpdate(
				"CREATE #CLASS VehiculeClass (DESCRIPTOR (#code = 'VehiculeClass', #name[en] = 'Car', #name[fr] = 'Vehicule'))");

		statement.executeUpdate(
				"CREATE #CLASS VehiculeClass2 (DESCRIPTOR (#code = 'VehiculeClass2', #name[en] = 'Car2', #name[fr] = 'Vehicule2'))");

		try {
			ResultSet res = statement.executeQuery("SELECT #code, #name[fr] from #CLASS WHERE #name[en]='Car' ");
			Assert.assertTrue(res.next());
			Assert.assertEquals("VehiculeClass", res.getString(1));
			Assert.assertEquals("Vehicule", res.getString(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		List<String> codes = Arrays.asList("VehiculeClass", "VehiculeClass2");
		List<String> namesEn = Arrays.asList("Car", "Car2");

		try {
			ResultSet res = statement.executeQuery("SELECT #code, #name[en] from #CLASS WHERE #name[fr] LIKE 'V%' ");
			Assert.assertTrue(res.next());
			Assert.assertTrue(codes.contains(res.getString(1)));
			Assert.assertTrue(namesEn.contains(res.getString(2)));
			Assert.assertTrue(res.next());
			Assert.assertTrue(codes.contains(res.getString(1)));
			Assert.assertTrue(namesEn.contains(res.getString(2)));
			Assert.assertFalse(res.next());
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet res = statement
					.executeQuery("SELECT #code, lower(#name[en]) from #CLASS WHERE #name[fr] LIKE '%2' ");
			Assert.assertTrue(res.next());
			Assert.assertEquals("VehiculeClass2", res.getString(1));
			Assert.assertEquals("car2", res.getString(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet res = statement
					.executeQuery("SELECT #code, #name[fr], #name[en] from #CLASS WHERE #code = 'VehiculeClass'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("VehiculeClass", res.getString(1));
			Assert.assertEquals("Vehicule", res.getString(2));
			Assert.assertEquals("Car", res.getString(3));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testMQLWithReferenceLanguage() {
		log.debug("MariusQLMQLTest.testMQLWithReferenceLanguage()");

		this.getSession().setReferenceLanguage(MariusQLConstants.ENGLISH);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		statement.executeUpdate("CREATE #CLASS VehiculeClass (DESCRIPTOR (#code = 'VehiculeClass', #name = 'Car'))");

		try {
			ResultSet res = statement.executeQuery("SELECT #code, #name from #CLASS");
			Assert.assertTrue(res.next());
			Assert.assertEquals("VehiculeClass", res.getString(1));
			Assert.assertEquals("Car", res.getString(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet res = statement.executeQuery("SELECT #code from #CLASS WHERE #name='Car' ");
			Assert.assertTrue(res.next());
			Assert.assertEquals("VehiculeClass", res.getString(1));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet res = statement.executeQuery("SELECT #code, #name from #CLASS WHERE #name LIKE 'C%' ");
			Assert.assertTrue(res.next());
			Assert.assertEquals("VehiculeClass", res.getString(1));
			Assert.assertEquals("Car", res.getString(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			ResultSet res = statement.executeQuery("SELECT #code, #name[en], #name[fr] from #CLASS");
			Assert.assertTrue(res.next());
			Assert.assertEquals("VehiculeClass", res.getString(1));
			Assert.assertEquals("Car", res.getString(2));
			Assert.assertEquals(null, res.getString(3));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testMQLSelectWithUnnestCollectionProperties() throws SQLException {
		log.debug("MariusQLMQLTest.testMQLSelectWithUnnestCollectionProperties()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (p1 INT ARRAY, p2 STRING ARRAY))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (p1, p2)"));

		Assert.assertEquals(1, statement.executeUpdate(
				"INSERT INTO Vehicule (p1, p2) " + "VALUES (ARRAY[1,2,3], ARRAY['it', 'should', 'work'])"));

		try {
			ResultSet executeQuery = statement
					.executeQuery("select c.#code from #class c, unnest(c.#directproperties) as p");
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals("Vehicule", executeQuery.getString(1));
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals("Vehicule", executeQuery.getString(1));
			Assert.assertFalse(executeQuery.next());
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testMQLSelectDatatype() throws SQLException {
		log.debug("MariusQLMQLTest.testMQLSelectDatatype()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		this.getSession().setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule'))"));
		MClass vehicule = FactoryCore.createExistingMClass(getSession(), "Vehicule");
		final Long vehiculeRid = vehicule.getInternalId();
		Assert.assertTrue(vehiculeRid != null && vehiculeRid != -1);
		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS VehiculeClass (DESCRIPTOR (#code = 'VehiculeClass') PROPERTIES (prop1 REF (Vehicule), prop2 REF (Vehicule)))"));

		MariusQLResultSet resultSet = statement
				.executeQuery("select #rid from #datatype where #dtype = 'ref' and #onclass = " + vehiculeRid);
		Assert.assertTrue(resultSet.next());
		Assert.assertTrue(resultSet.getLong(1) != 0);
		Assert.assertTrue(resultSet.next());
		Assert.assertTrue(resultSet.getLong(1) != 0);

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMQLSelectProperties() throws SQLException {
		log.debug("MariusQLMQLTest.testMQLSelectProperties()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (p1 INT ARRAY, p2 STRING ARRAY))"));

		// Recover rid from property.
		MariusQLResultSet rset = statement.executeQuery("select #rid, #scope, #range from #property");
		Assert.assertTrue(rset.next());
		Long rid1 = rset.getLong(1);
		Assert.assertTrue(rid1 != 0);
		Assert.assertTrue(rset.getLong(2) != 0);
		Assert.assertTrue(rset.getLong(3) != 0);
		Assert.assertTrue(rset.next());
		Long rid2 = rset.getLong(1);
		Assert.assertTrue(rid2 != 0);
		Assert.assertTrue(rset.getLong(2) != 0);
		Assert.assertTrue(rset.getLong(3) != 0);

		// Ensure the result.
		rset = statement.executeQuery("select #directproperties from #class");
		Assert.assertTrue(rset.next());
		Assert.assertEquals("{" + rid1 + "," + rid2 + "}", this.listToString(rset.getList(1, Long.class)));
		Assert.assertFalse(rset.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMQLSelectClass() {
		log.debug("MariusQLMQLTest.testMQLSelectClass()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		statement.executeUpdate("CREATE #CLASS BB3DFD4 (DESCRIPTOR (#code = 'BB3DFD4'))");

		try {
			ResultSet res = statement.executeQuery("SELECT #CODE from #CLASS");
			Assert.assertTrue(res.next());
			Assert.assertEquals("BB3DFD4", res.getString(1));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testMQLSelectDatatypeEnum() throws SQLException {
		log.debug("MariusQLMQLTest.testMQLSelectDatatypeEnum()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int result = statement.executeUpdate("CREATE #CLASS CFCA (PROPERTIES (color ENUM ('NOIR','ROUGE','BLANC')))");
		Assert.assertEquals(1, result);

		MariusQLResultSet resultSet = statement
				.executeQuery("select p.#rid, p.#range from #Property p where p.#code = 'color'");
		Assert.assertTrue(resultSet.next());
		Assert.assertTrue(resultSet.getLong(1) != 0);
		Assert.assertTrue(resultSet.getLong(2) != 0);

		resultSet = statement
				.executeQuery("select d.#enumvalues from #Datatype d where d.#rid = " + resultSet.getLong(2));
		Assert.assertTrue(resultSet.next());
		Assert.assertEquals("{NOIR,ROUGE,BLANC}", this.listToString(resultSet.getList(1, String.class)));

		resultSet = statement.executeQuery(
				"select d.#rid, d.#enumvalues from #Datatype d, #Property p where p.#range = d.#rid and p.#code = 'color'");
		Assert.assertTrue(resultSet.next());
		Assert.assertTrue(resultSet.getLong(1) != 0);
		Assert.assertEquals("{NOIR,ROUGE,BLANC}", this.listToString(resultSet.getList(2, String.class)));

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMQLSelectIsMandatoryIsVisibleAttributes() throws SQLException {
		log.debug("MariusQLMQLTest.testMQLSelectIsMandatoryIsVisibleAttributes()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int result = statement
				.executeUpdate("CREATE #CLASS FirstClass (DESCRIPTOR (#code = '0002-41982799300025#01-2#1'))");
		Assert.assertEquals(1, result);
		result = statement.executeUpdate(
				"ALTER #CLASS FirstClass ADD Prop1 String DESCRIPTOR (#code = '0002-41982799300025#02-10#1', #isvisible = 'true', #ismandatory = 'true')");
		Assert.assertEquals(1, result);

		this.getSession().rollback();
		statement.close();
	}
}
