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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.mariusql.AbstractMariusQLTest;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSet;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;
import fr.ensma.lias.mariusql.util.MariusQLHelper;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * This test class is a particular Data Manipulation Language (DML) test
 * dedicated for Model Manipulation Language (MML).
 * 
 * @author Valentin CASSAIR
 */
public class MariusQLMMLTest extends AbstractMariusQLTest {

	private Logger log = LoggerFactory.getLogger(MariusQLMMDLTest.class);

	@Test
	public void testMMLUpdateClassSimpleAttribute() {
		log.debug("MariusQLMMLTest.testMMLUpdateClassSimpleAttribute()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS BB3DFD4 (DESCRIPTOR (#code ='BB3DFD4', #name = 'name')))"));

		Assert.assertEquals(1,
				statement.executeUpdate("UPDATE #class SET #code = 'testMMLUpdateSimpleAttribute', #name = 'nom'"));

		try {
			MariusQLResultSet res = (MariusQLResultSet) statement.executeQuery("SELECT #code, #name from #class c");
			Assert.assertTrue(res.next());
			Assert.assertEquals("testMMLUpdateSimpleAttribute", res.getString(1));
			Assert.assertEquals("nom", res.getString(2));
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testMDLAlterClassAddPropertiesWithBooleanAttributesDescriptors() throws SQLException {
		log.debug("MariusQLMQLTest.testMDLAlterClassAddPropertiesWithBooleanAttributesDescriptors()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int result = statement
				.executeUpdate("CREATE #CLASS FirstClass (DESCRIPTOR (#code = '0002-41982799300025#01-2#1'))");
		Assert.assertEquals(1, result);
		result = statement.executeUpdate(
				"ALTER #CLASS FirstClass ADD Prop1 String DESCRIPTOR (#code = '0002-41982799300025#02-10#1', #isvisible = true, #ismandatory = true)");
		Assert.assertEquals(1, result);

		try {
			MariusQLResultSet executeQuery = statement.executeQuery(
					"select #isvisible, #ismandatory from #property where #code = '0002-41982799300025#02-10#1'");
			Assert.assertTrue(executeQuery.next());
			Assert.assertTrue(executeQuery.getBoolean(1));
			Assert.assertTrue(executeQuery.getBoolean(2));

			Assert.assertEquals(1, statement.executeUpdate(
					"UPDATE #property SET #isvisible = false, #ismandatory = false where #code = '0002-41982799300025#02-10#1'"));
			executeQuery = statement.executeQuery(
					"select #isvisible, #ismandatory from #property where #code = '0002-41982799300025#02-10#1'");
			Assert.assertTrue(executeQuery.next());
			Assert.assertTrue(!executeQuery.getBoolean(1));
			Assert.assertTrue(!executeQuery.getBoolean(2));
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testMMLUpdatePropertySimpleAttribute() throws SQLException {
		log.debug("MariusQLMMLTest.testMMLUpdatePropertySimpleAttribute()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS BB3DFD4 (DESCRIPTOR (#code ='BB3DFD4', #name = 'name') PROPERTIES (prop1 String))"));
		Assert.assertEquals(1,
				statement.executeUpdate("UPDATE #class SET #code = 'testMMLUpdateSimpleAttribute', #name = 'nom'"));
		Assert.assertEquals(1,
				statement.executeUpdate("UPDATE #property SET #code = 'codeprop1', #name = 'nameprop1'"));

		try {
			MariusQLResultSet res = (MariusQLResultSet) statement.executeQuery("SELECT #code, #name from #class c");
			Assert.assertTrue(res.next());
			Assert.assertEquals("testMMLUpdateSimpleAttribute", res.getString(1));
			Assert.assertEquals("nom", res.getString(2));

			res = (MariusQLResultSet) statement.executeQuery("SELECT #code, #name from #property p");
			Assert.assertTrue(res.next());
			Assert.assertEquals("codeprop1", res.getString(1));
			Assert.assertEquals("nameprop1", res.getString(2));
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testMMLUpdateSimpleAttributeWithSimpleCriteria() {
		log.debug("MariusQLMMLTest.testMMLUpdateSimpleAttributeWithSimpleCriteria()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS BB3DFD4 (DESCRIPTOR (#code ='BB3DFD4', #name = 'name')))"));

		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS BB3DFD5 (DESCRIPTOR (#code ='BB3DFD5', #name = 'name')))"));

		Assert.assertEquals(1, statement.executeUpdate(
				"UPDATE #class SET #code = 'testMMLUpdateSimpleAttributeWithSimpleCriteria', #name = 'nom' WHERE #code ='BB3DFD5'"));

		try {
			MariusQLResultSet res = (MariusQLResultSet) statement
					.executeQuery("SELECT #code, #name from #class WHERE #code ='BB3DFD4'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("BB3DFD4", res.getString(1));
			Assert.assertEquals("name", res.getString(2));

			res = (MariusQLResultSet) statement.executeQuery(
					"SELECT #code, #name from #class WHERE #code ='testMMLUpdateSimpleAttributeWithSimpleCriteria'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("testMMLUpdateSimpleAttributeWithSimpleCriteria", res.getString(1));
			Assert.assertEquals("nom", res.getString(2));

			res = (MariusQLResultSet) statement.executeQuery("SELECT #code, #name from #class WHERE #code ='BB3DFD5'");
			Assert.assertFalse(res.next());
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testMMLUpdateSimpleStringAttributeWithSelectClause() {
		log.debug("MariusQLMMLTest.testMMLUpdateSimpleStringAttributeWithSelectClause()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS BB3DFD4 (DESCRIPTOR (#code ='BB3DFD4', #name = 'nom', #package = 'test')))"));
		Assert.assertEquals(1,
				statement.executeUpdate("UPDATE #class SET #name = (SELECT #package FROM #class WHERE #name = 'nom')"));

		try {
			MariusQLResultSet res = (MariusQLResultSet) statement
					.executeQuery("SELECT #code, #name from #class WHERE #code ='BB3DFD4'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("BB3DFD4", res.getString(1));
			Assert.assertEquals("test", res.getString(2));
			Assert.assertEquals(1, statement.executeUpdate(
					"UPDATE #class SET #name = (SELECT #code FROM #class WHERE #name = 'test') WHERE #code ='BB3DFD4' "));

			res = (MariusQLResultSet) statement.executeQuery("SELECT #code, #name from #class WHERE #code ='BB3DFD4'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("BB3DFD4", res.getString(1));
			Assert.assertEquals("BB3DFD4", res.getString(2));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Problem with where on update with a select.");
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testMMLUpdateSimpleStringAttributeWithSeveralSelectClause() {
		log.debug("MariusQLMMLTest.testMMLUpdateSimpleStringAttributeWithSeveralSelectClause()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS BB3DFD4 (DESCRIPTOR (#code ='BB3DFD4', #name = 'nom', #package = 'test')))"));
		Assert.assertEquals(1,
				statement.executeUpdate("UPDATE #class SET #name = (SELECT #package FROM #class WHERE #name = 'nom')"));

		try {
			MariusQLResultSet res = (MariusQLResultSet) statement
					.executeQuery("SELECT #code, #name from #class WHERE #code ='BB3DFD4'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("BB3DFD4", res.getString(1));
			Assert.assertEquals("test", res.getString(2));

			Assert.assertEquals(1, statement.executeUpdate(
					"UPDATE #class SET #name = (SELECT #code FROM #class WHERE #name = 'test'), #package = (SELECT #name FROM #class WHERE #name = 'test') WHERE #code ='BB3DFD4' "));
			res = (MariusQLResultSet) statement.executeQuery("SELECT #code, #name from #class WHERE #code ='BB3DFD4'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("BB3DFD4", res.getString(1));
			Assert.assertEquals("BB3DFD4", res.getString(2));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Problem with where on update with a select.");
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testMMLUpdateWithArray() {
		log.debug("MariusQLMMLTest.testMMLUpdateWithArray()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		try {
			// First check.
			Assert.assertEquals(1, statement.executeUpdate(
					"CREATE #CLASS BB3DFD4 (DESCRIPTOR (#code ='BB3DFD4', #name = 'nom', #package = 'test')))"));
			Assert.assertEquals(1, statement.executeUpdate("ALTER #CLASS BB3DFD4 ADD Prop1 String"));
			Assert.assertEquals(1, statement.executeUpdate("ALTER #CLASS BB3DFD4 ADD Prop2 String"));

			List<Long> list = new ArrayList<Long>();
			ResultSet resultset = statement.executeQuery("SELECT #rid FROM #property where #code = 'Prop2'");
			while (resultset.next()) {
				list.add(resultset.getLong(1));
			}

			Assert.assertEquals(1, statement.executeUpdate("UPDATE #class SET #directproperties = "
					+ MariusQLHelper.getCollectionAssociationOntoQLValue(list) + ""));

			MariusQLResultSet res = (MariusQLResultSet) statement
					.executeQuery("SELECT #code, #name, #directproperties from #class WHERE #code ='BB3DFD4'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("BB3DFD4", res.getString(1));
			Assert.assertEquals("nom", res.getString(2));
			Assert.assertEquals(MariusQLHelper.getCollectionAssociationSQLValue(list),
					this.listToString(res.getList(3, Long.class)));

			// Second check.
			Assert.assertEquals(1, statement.executeUpdate(
					"CREATE #CLASS BB3DFD5 (DESCRIPTOR (#code ='BB3DFD5', #name = 'name', #package = 'test2')))"));
			Assert.assertEquals(2,
					statement.executeUpdate("UPDATE #class SET #directproperties = ARRAY(SELECT #rid from #property)"));

			res = (MariusQLResultSet) statement
					.executeQuery("SELECT #code, #name, #directproperties from #class WHERE #code ='BB3DFD4'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("BB3DFD4", res.getString(1));
			Assert.assertEquals("nom", res.getString(2));
			String propertiesString = this.listToString(res.getList(3, Long.class));

			res = (MariusQLResultSet) statement.executeQuery("SELECT #rid from #property");

			List<String> realProperties = new ArrayList<String>();
			while (res.next()) {
				realProperties.add(res.getString(1));
			}

			String realPropertiesArray = "{" + StringHelper.join(",", realProperties.iterator()) + "}";
			Assert.assertEquals(realPropertiesArray, propertiesString);

			String returnValue = MariusQLConstants.COLLECTION_NAME + MariusQLConstants.OPEN_SQUARE;
			String returnValue2 = MariusQLConstants.OPEN_BRACKET;
			resultset = statement.executeQuery("SELECT #rid FROM #property");
			while (resultset.next()) {
				returnValue += resultset.getString(1) + ",";
				returnValue2 += resultset.getString(1) + ",";
			}
			if (!returnValue.isEmpty()) {
				returnValue = returnValue.substring(0, returnValue.length() - 1);
				returnValue2 = returnValue2.substring(0, returnValue2.length() - 1);
			}

			returnValue += MariusQLConstants.CLOSE_SQUARE;
			returnValue2 += MariusQLConstants.CLOSE_BRACKET;

			Assert.assertEquals(1, statement
					.executeUpdate("UPDATE #class SET #directproperties = " + returnValue + " WHERE #code ='BB3DFD4'"));
			res = (MariusQLResultSet) statement
					.executeQuery("SELECT #code, #name, #directproperties from #class WHERE #code ='BB3DFD4'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("BB3DFD4", res.getString(1));
			Assert.assertEquals("nom", res.getString(2));
			Assert.assertEquals(returnValue2, this.listToString(res.getList(3, Long.class)));
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testMMLUpdateWithReferences() throws SQLException {
		log.debug("MariusQLMMLTest.testMMLUpdateWithReferences()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		statement.executeUpdate("CREATE ENTITY #personne (#attribute ref(#class))");
		statement.executeUpdate("CREATE #CLASS Voiture ( DESCRIPTOR (#name = 'Vehicule', #code = 'BB3DFD4'))");
		statement.executeUpdate("CREATE #CLASS Car ( DESCRIPTOR (#name = 'Car', #code = 'BB3DFD5'))");

		ResultSet resultset = statement.executeQuery("SELECT #rid FROM #class where #name = 'Vehicule'");
		Assert.assertTrue(resultset.next());
		Long classVehiculeRid = resultset.getLong(1);
		Assert.assertFalse(resultset.next());

		resultset = statement.executeQuery("SELECT #rid FROM #class where #name = 'Car'");
		Assert.assertTrue(resultset.next());
		Long classCarRid = resultset.getLong(1);
		Assert.assertFalse(resultset.next());

		statement.executeUpdate("INSERT INTO #personne (#code, #attribute) values ('Jean'," + classVehiculeRid + ")");
		statement.executeUpdate("UPDATE #personne SET  #attribute = " + classCarRid);

		resultset = statement.executeQuery("SELECT #attribute FROM #personne WHERE #code = 'Jean'");
		Assert.assertTrue(resultset.next());
		Assert.assertEquals(classCarRid.longValue(), resultset.getLong(1));

		statement.close();
		this.getSession().rollback();
	}

	@Test
	public void testMMLUpdateWithCollectionOfReferences() throws SQLException {
		log.debug("MariusQLMMLTest.testMMLUpdateWithCollectionOfReferences()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		statement.executeUpdate("CREATE ENTITY #personne (#attribute ref(#class) ARRAY)");
		statement.executeUpdate("CREATE #CLASS Voiture ( DESCRIPTOR (#name = 'Vehicule', #code = 'BB3DFD4'))");
		statement.executeUpdate("CREATE #CLASS Car ( DESCRIPTOR (#name = 'Car', #code = 'BB3DFD5'))");
		statement.executeUpdate("CREATE #CLASS Coche ( DESCRIPTOR (#name = 'Coche', #code = 'BB3DFD6'))");
		List<Long> list = new ArrayList<Long>();
		MariusQLResultSet resultset = statement.executeQuery("SELECT #rid FROM #class");
		while (resultset.next()) {
			list.add(resultset.getLong(1));
		}

		statement.executeUpdate("CREATE #personne Pierre ( DESCRIPTOR (#attribute =  "
				+ MariusQLHelper.getCollectionAssociationOntoQLValue(list) + "))");
		statement.executeUpdate("UPDATE #personne SET #attribute = "
				+ MariusQLHelper.getCollectionAssociationOntoQLValue(list) + ", #code = 'Paul'");

		resultset = statement.executeQuery("SELECT #attribute FROM #personne WHERE #code = 'Paul'");
		Assert.assertTrue(resultset.next());
		Assert.assertEquals(MariusQLHelper.getCollectionAssociationSQLValue(list),
				this.listToString(resultset.getList(1, Long.class)));

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMMLInsertWithSimpleAttributes() {
		log.debug("MariusQLMMLTest.testMMLInsertWithSimpleAttributes()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		try {
			Assert.assertEquals(1, statement.executeUpdate(
					"INSERT INTO #class (#dtype, #code) values ('class','testOMLInsertConceptSimpleAttributesWithValuesClause')"));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			Assert.assertEquals(1, statement.executeUpdate(
					"INSERT INTO #class (#dtype, #name) values ('class','testOMLInsertConceptSimpleAttributesWithValuesClause')"));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertEquals("Does not contain the attribute code", e.getMessage());
		}

		try {
			Assert.assertEquals(1, statement.executeUpdate("INSERT INTO #class (#name, #code) values ('nom','code')"));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		} finally {
			statement.close();
			this.getSession().rollback();
		}
	}

	@Test
	public void testMMLInsertWithSelectClause() {
		log.debug("MariusQLMMLTest.testMMLInsertWithSelectClause()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		try {
			Assert.assertEquals(1, statement.executeUpdate(
					"INSERT INTO #class (#code, #name, #package) values ('BB3DFD4','Vehicule', 'package')"));
			Assert.assertEquals(1, statement.executeUpdate(
					"INSERT INTO #class (#code, #name, #package) values ('BB3DFD5',(SELECT #name FROM #class WHERE #name = 'Vehicule'), 'package')"));
			MariusQLResultSet res = (MariusQLResultSet) statement
					.executeQuery("SELECT #name, #package from #class WHERE #code ='BB3DFD5'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("Vehicule", res.getString(1));
			Assert.assertEquals("package", res.getString(2));

			Assert.assertEquals(1, statement.executeUpdate(
					"INSERT INTO #class (#code, #name, #package) values ('BB3DFD6',(SELECT #name FROM #class WHERE #code = 'BB3DFD5'), 'package')"));
			res = (MariusQLResultSet) statement
					.executeQuery("SELECT #name, #package from #class WHERE #code ='BB3DFD6'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("Vehicule", res.getString(1));
			Assert.assertEquals("package", res.getString(2));

			Assert.assertEquals(1, statement.executeUpdate(
					"INSERT INTO #class (#code, #name, #package) values ('BB3DFD7',(SELECT #name FROM #class WHERE #code = 'BB3DFD4'), (SELECT #package FROM #class WHERE #code = 'BB3DFD5')))"));
			res = (MariusQLResultSet) statement
					.executeQuery("SELECT #name, #package from #class WHERE #code ='BB3DFD7'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("Vehicule", res.getString(1));
			Assert.assertEquals("package", res.getString(2));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testMMLInsertWithMultiLanguage() {
		log.debug("MariusQLMMLTest.testMMLInsertWithMultiLanguage()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Voiture ( " + "DESCRIPTOR (#name = 'Vehicule', #code = 'BB3DFD4'))"));

		try {
			Assert.assertEquals(1, statement.executeUpdate(
					"INSERT INTO #class (#dtype, #name, #code) values ('class','testOMLInsertConceptSimpleAttributesWithValuesClause', 'code')"));
			MariusQLResultSet res = (MariusQLResultSet) statement
					.executeQuery("SELECT #dtype, #name from #class WHERE #code ='code'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("class", res.getString(1));
			Assert.assertEquals("testOMLInsertConceptSimpleAttributesWithValuesClause", res.getString(2));

			this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

			Assert.assertEquals(1, statement.executeUpdate(
					"INSERT INTO #class (#dtype, #name[en], #code) values ('class','testOMLInsertConceptSimpleAttributesWithValuesClause', 'code2')"));

			res = (MariusQLResultSet) statement.executeQuery("SELECT #dtype, #name from #class WHERE #code ='code2'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("class", res.getString(1));
			Assert.assertEquals("testOMLInsertConceptSimpleAttributesWithValuesClause", res.getString(2));

			this.getSession().setReferenceLanguage(MariusQLConstants.FRENCH);

			Assert.assertEquals(1, statement.executeUpdate(
					"INSERT INTO #class (#dtype, #name, #code) values ('class','testOMLInsertConceptSimpleAttributesWithValuesClause', 'code3')"));
			res = (MariusQLResultSet) statement.executeQuery("SELECT #dtype, #name from #class WHERE #code ='code3'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("class", res.getString(1));
			Assert.assertEquals("testOMLInsertConceptSimpleAttributesWithValuesClause", res.getString(2));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testMMLInsertWithReferences() throws SQLException {
		log.debug("MariusQLMMLTest.testMMLInsertWithReferences()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		statement.executeUpdate("CREATE ENTITY #personne (#attribute ref(#class))");
		statement.executeUpdate("CREATE #CLASS Voiture ( DESCRIPTOR (#name = 'Vehicule', #code = 'BB3DFD4'))");

		ResultSet resultset = statement.executeQuery("SELECT #rid FROM #class");
		Assert.assertTrue(resultset.next());
		statement.executeUpdate(
				"INSERT INTO #personne (#code, #attribute) values ('Jean'," + resultset.getString(1) + " )");

		ResultSet resultset2 = statement.executeQuery("SELECT #attribute FROM #personne WHERE #code = 'Jean'");
		Assert.assertTrue(resultset2.next());
		Assert.assertEquals(resultset2.getString(1), resultset2.getString(1));

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMMLInsertWithCollectionOfReferences() throws SQLException {
		log.debug("MariusQLMMLTest.testMMLInsertWithCollectionOfReferences()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		statement.executeUpdate("CREATE ENTITY #personne (#attribute ref(#class) ARRAY)");
		statement.executeUpdate("CREATE #CLASS Voiture ( DESCRIPTOR (#name = 'Vehicule', #code = 'BB3DFD4'))");
		statement.executeUpdate("CREATE #CLASS Car ( DESCRIPTOR (#name = 'Car', #code = 'BB3DFD5'))");
		statement.executeUpdate("CREATE #CLASS Coche ( DESCRIPTOR (#name = 'Coche', #code = 'BB3DFD6'))");

		ArrayList<Long> list = new ArrayList<Long>();
		MariusQLResultSet resultset = statement.executeQuery("SELECT #rid FROM #class");
		while (resultset.next()) {
			list.add(resultset.getLong(1));
		}

		statement.executeUpdate("CREATE #personne Pierre ( DESCRIPTOR (#attribute =  "
				+ MariusQLHelper.getCollectionAssociationOntoQLValue(list) + "))");
		statement.executeUpdate("INSERT INTO #personne (#code, #attribute) values ('Jean',"
				+ MariusQLHelper.getCollectionAssociationOntoQLValue(list) + " )");

		resultset = statement.executeQuery("SELECT #attribute FROM #personne WHERE #code = 'Jean'");
		Assert.assertTrue(resultset.next());
		Assert.assertEquals(MariusQLHelper.getCollectionAssociationSQLValue(list),
				this.listToString(resultset.getList(1, Long.class)));

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMMLInsertWithArray() {
		log.debug("MariusQLMMLTest.testMMLInsertWithArray()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		try {
			Assert.assertEquals(1, statement.executeUpdate(
					"CREATE #CLASS BB3DFD4 (DESCRIPTOR (#code ='BB3DFD4', #name = 'nom', #package = 'test')))"));
			Assert.assertEquals(1, statement.executeUpdate("ALTER #CLASS BB3DFD4 ADD Prop1 String"));

			List<Long> list = new ArrayList<Long>();
			MariusQLResultSet resultset = statement.executeQuery("SELECT #rid FROM #property");
			while (resultset.next()) {
				list.add(resultset.getLong(1));
			}

			Assert.assertEquals(1,
					statement.executeUpdate("INSERT INTO #class (#code, #directproperties) VALUES ('Pierre',"
							+ MariusQLHelper.getCollectionAssociationOntoQLValue(list) + ")"));
			resultset = (MariusQLResultSet) statement
					.executeQuery("SELECT #code, #directproperties from #class WHERE #code ='Pierre'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals("Pierre", resultset.getString(1));
			Assert.assertEquals(MariusQLHelper.getCollectionAssociationSQLValue(list),
					this.listToString(resultset.getList(2, Long.class)));

		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testMMLInsertSimpleStringAttributeWithSelectClause() {
		log.debug("MariusQLMMLTest.testMMLInsertSimpleStringAttributeWithSelectClause()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS BB3DFD4 (DESCRIPTOR (#code ='BB3DFD4', #name = 'nom', #package = 'test')))"));

		Assert.assertEquals(1, statement.executeUpdate(
				"INSERT INTO #class (#code, #package) VALUES ('BB3DFD5',(SELECT #package FROM #class))"));

		try {
			MariusQLResultSet res = (MariusQLResultSet) statement
					.executeQuery("SELECT #code, #package from #class WHERE #code ='BB3DFD5'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("BB3DFD5", res.getString(1));
			Assert.assertEquals("test", res.getString(2));

		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			Assert.assertEquals(1, statement.executeUpdate(
					"INSERT INTO #class (#code, #package) VALUES ('BB3DFD6',(SELECT #code FROM #class WHERE #code = 'BB3DFD5'))"));
			MariusQLResultSet res = (MariusQLResultSet) statement
					.executeQuery("SELECT #code, #package from #class WHERE #code ='BB3DFD6'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("BB3DFD6", res.getString(1));
			Assert.assertEquals("BB3DFD5", res.getString(2));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Problem with where on update with a select");
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testMMLInsertSimpleStringAttributeWithSeveralSelectClause() {
		log.debug("MariusQLMMLTest.testMMLInsertSimpleStringAttributeWithSeveralSelectClause()");
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS BB3DFD4 (DESCRIPTOR (#code ='BB3DFD4', #name = 'nom', #package = 'test')))"));

		Assert.assertEquals(1, statement.executeUpdate(
				"INSERT INTO #class (#code, #package, #name) VALUES ('BB3DFD5',(SELECT #package FROM #class), (SELECT #name FROM #class))"));

		try {
			MariusQLResultSet res = (MariusQLResultSet) statement
					.executeQuery("SELECT #code, #package, #name from #class WHERE #code ='BB3DFD5'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("BB3DFD5", res.getString(1));
			Assert.assertEquals("test", res.getString(2));
			Assert.assertEquals("nom", res.getString(3));

		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			Assert.assertEquals(1, statement.executeUpdate(
					"INSERT INTO #class (#code, #package, #name) VALUES ('BB3DFD6',(SELECT #code FROM #class WHERE #code = 'BB3DFD5'), (SELECT #name FROM #class WHERE #code = 'BB3DFD5'))"));
			MariusQLResultSet res = (MariusQLResultSet) statement
					.executeQuery("SELECT #code, #package, #name from #class WHERE #code ='BB3DFD6'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("BB3DFD6", res.getString(1));
			Assert.assertEquals("BB3DFD5", res.getString(2));
			Assert.assertEquals("nom", res.getString(3));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Problem with where on update with a select");
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testMMLUpdateEnumValues() throws SQLException {
		log.debug("MariusQLMQLTest.testMDLCreateClassWithEnumProperties()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int result = statement.executeUpdate("CREATE #CLASS CFCA (PROPERTIES (color ENUM ('NOIR','ROUGE','BLANC')))");
		Assert.assertEquals(1, result);

		MariusQLResultSet resultSet = statement
				.executeQuery("select p.#rid, p.#range from #Property p where p.#code = 'color'");
		Assert.assertTrue(resultSet.next());
		Assert.assertTrue(resultSet.getLong(1) != 0);
		final long enumDatatype = resultSet.getLong(2);
		Assert.assertTrue(enumDatatype != 0);

		resultSet = statement.executeQuery("select d.#enumvalues from #Datatype d where d.#rid = " + enumDatatype);
		Assert.assertTrue(resultSet.next());
		Assert.assertEquals("{NOIR,ROUGE,BLANC}", this.listToString(resultSet.getList(1, String.class)));

		final String query = "UPDATE #DATATYPE SET #ENUMVALUES = " + MariusQLConstants.COLLECTION_NAME
				+ MariusQLConstants.OPEN_SQUARE + "'NOIR','BLEU'" + MariusQLConstants.CLOSE_SQUARE + " WHERE #rid = "
				+ enumDatatype;
		result = statement.executeUpdate(query);

		resultSet = statement.executeQuery("select d.#enumvalues from #Datatype d where d.#rid = " + enumDatatype);
		Assert.assertTrue(resultSet.next());
		Assert.assertEquals("{NOIR,BLEU}", this.listToString(resultSet.getList(1, String.class)));
	}
}
