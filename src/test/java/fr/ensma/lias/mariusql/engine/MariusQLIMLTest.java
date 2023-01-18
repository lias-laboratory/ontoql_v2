/*********************************************************************************
Ql * This file is part of MariusQL Project.
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
import fr.ensma.lias.mariusql.DriverDelegateTest.DriverEnum;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotSupportedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSet;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;
import fr.ensma.lias.mariusql.util.MariusQLHelper;

/**
 * This test class is for Data Definition Language (DDL).
 * 
 * @author Mickael BARON
 * @author Florian MHUN
 * @author Ghada TRIKI
 */
public class MariusQLIMLTest extends AbstractMariusQLTest {

	private Logger log = LoggerFactory.getLogger(MariusQLIMLTest.class);

	@Test
	public void testIMLInsertWithErrors() {
		log.debug("MariusQLIMLTest.testIMLInsertWithErrors()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst)"));

		try {
			Assert.assertEquals(1, statement
					.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));

			Assert.fail("Must throw an exception");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertEquals("propertyIntSecond is not a used property.", e.getMessage());
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testIMLInsertWithSimpleType() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLInsertWithSimpleType()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (p1 int, p2 real, p3 boolean, p4 string))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (p1, p2, p3, p4)"));

		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (p1, p2, p3, p4) values (1, 1.5, true, 'string')"));

		ResultSet resultset = statement.executeQuery("SELECT p1, p2, p3, p4 FROM Vehicule");
		Assert.assertTrue(resultset.next());
		Assert.assertEquals(1, resultset.getInt(1));
		Assert.assertEquals(1.5, resultset.getFloat(2), .0);
		Assert.assertEquals(true, resultset.getBoolean(3));
		Assert.assertEquals("string", resultset.getString(4));
		Assert.assertFalse(resultset.next());

		final MariusQLResultSet executeQuery = statement
				.executeQuery("select #rid from #class where #code = 'Vehicule'");
		Assert.assertTrue(executeQuery.next());

		Assert.assertEquals(1, this.getSession().getMariusQLDQL().getCountRow("i_" + executeQuery.getInt(1), true));

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLInsertWithCountType() {
		log.debug("MariusQLMDLTest.testIMLInsertWithCountType()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = 0;
		try {
			res = statement.executeUpdate(
					"CREATE #CLASS Vehicule (DESCRIPTOR (#code ='Vehicule') PROPERTIES (referenceBE CountType, designation String))");
			Assert.assertEquals(1, res);
		} catch (NotSupportedException e) {
			if (this.getCurrentDriver() != DriverEnum.HSQLDB) {
				Assert.fail("Don't throw a NotSupportedException");
			}

			// HSQLDB driver doesn't support this feature.
			return;
		}

		res = statement.executeUpdate("CREATE EXTENT OF Vehicule (designation, referenceBE)");
		Assert.assertEquals(1, res);

		Assert.assertEquals(1,
				statement.executeUpdate("insert into Vehicule (designation) values ('premier message')"));
		ResultSet resultset;
		try {
			resultset = statement.executeQuery("select designation, referenceBE from Vehicule");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals(2, resultset.getMetaData().getColumnCount());
			Assert.assertEquals("premier message", resultset.getString(1));
			Assert.assertEquals(1, resultset.getInt(2));
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		Assert.assertEquals(1, statement
				.executeUpdate("insert into Vehicule (designation, referenceBE) values ('second message', 100)"));
		try {
			resultset = statement
					.executeQuery("select designation, referenceBE from Vehicule where designation = 'second message'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals(2, resultset.getMetaData().getColumnCount());
			Assert.assertEquals("second message", resultset.getString(1));
			Assert.assertEquals(100, resultset.getInt(2));
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLInsertWithReferenceErrors() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLInsertWithReferenceErrors()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Person (name)"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Pink')"));

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (owner ref(Person)))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (owner)"));

		ResultSet resultset = statement.executeQuery("SELECT rid FROM Person");
		Assert.assertTrue(resultset.next());
		Long owner = resultset.getLong(1);
		Long refNotExists = owner + 20;

		// reference not exists
		try {
			Assert.assertEquals(1,
					statement.executeUpdate("INSERT INTO Vehicule (owner) values (" + refNotExists + ")"));
			Assert.fail("Don't throw an MariusQLException");
		} catch (MariusQLException e) {
			Assert.assertEquals("Reference on class Person not exists: " + refNotExists, e.getMessage());
		}

		// nothing inserted
		resultset = statement.executeQuery("SELECT owner FROM Vehicule");
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLInsertWithReference() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLInsertWithReference()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Person (name)"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Pink')"));

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (owner ref(Person)))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (owner)"));

		ResultSet resultset = statement.executeQuery("SELECT rid FROM Person");
		Assert.assertTrue(resultset.next());
		Long owner = resultset.getLong(1);

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (owner) values (" + owner + ")"));
		resultset = statement.executeQuery("SELECT owner FROM Vehicule");
		Assert.assertTrue(resultset.next());
		Assert.assertEquals(owner.longValue(), resultset.getLong(1));
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLInsertWithReferenceAndInheritanceErrors() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLInsertWithReferenceAndInheritanceErrors()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Male UNDER Person (DESCRIPTOR (#code = 'Male'))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Female UNDER Person (DESCRIPTOR (#code = 'Female'))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS SexyFemale UNDER Female (DESCRIPTOR (#code = 'SexyFemale'))"));
		// only male can own a car ;)
		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (owner ref(Male)))"));

		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Male (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Female (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF SexyFemale (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (owner)"));

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Female (name) values ('Ms. Pink')"));

		ResultSet resultset = statement.executeQuery("SELECT rid FROM Female");
		Assert.assertTrue(resultset.next());
		Long owner = resultset.getLong(1);
		Assert.assertFalse(resultset.next());

		// error: female can not own a car
		try {
			Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (owner) values (" + owner + ")"));
			Assert.fail("Don't throw an MariusQLException");
		} catch (MariusQLException e) {
			Assert.assertEquals("Reference on class Male not exists: " + owner, e.getMessage());
		}

		// nothing inserted
		resultset = statement.executeQuery("SELECT owner FROM Vehicule");
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLInsertWithReferenceAndInheritance() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLInsertWithReferenceAndInheritance()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Male UNDER Person (DESCRIPTOR (#code = 'Male'))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Female UNDER Person (DESCRIPTOR (#code = 'Female'))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS SexyFemale UNDER Female (DESCRIPTOR (#code = 'SexyFemale'))"));
		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (owner ref(Person)))"));

		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Male (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Female (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF SexyFemale (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (owner)"));

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO SexyFemale (name) values ('Ms. Pink')"));

		ResultSet resultset = statement.executeQuery("SELECT rid FROM SexyFemale");
		Assert.assertTrue(resultset.next());
		Long owner = resultset.getLong(1);
		Assert.assertFalse(resultset.next());

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (owner) values (" + owner + ")"));
		resultset = statement.executeQuery("SELECT owner FROM Vehicule");
		Assert.assertTrue(resultset.next());
		Assert.assertEquals(owner.longValue(), resultset.getLong(1));
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLInsertWithCollectionOfSimpleTypes() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLInsertWithCollectionOfSimpleTypes()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (p1 INT ARRAY, p2 REAL ARRAY, p3 BOOLEAN ARRAY, p4 STRING ARRAY))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (p1, p2, p3, p4)"));

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (p1, p2, p3, p4) "
				+ "VALUES (ARRAY[1,2,3], ARRAY[1.1, 2.2, 3.3], ARRAY[true, false, true], ARRAY['it', 'should', 'work'])"));

		MariusQLResultSet resultset = statement.executeQuery("SELECT p1, p2, p3, p4 FROM Vehicule");
		Assert.assertTrue(resultset.next());

		Assert.assertEquals("{1,2,3}", this.listToString(resultset.getList(1, Integer.class)));
		Assert.assertEquals("{1.1,2.2,3.3}", this.listToString(resultset.getList(2, Double.class)));
		Assert.assertEquals("{true,false,true}", this.listToString(resultset.getList(3, Boolean.class)));
		Assert.assertEquals("{it,should,work}", this.listToString(resultset.getList(4, String.class)));
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLInsertWithCollectionOfReferences() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLInsertCollectionOfReferences()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Person (name)"));

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Pink')"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Blue')"));

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (owners REF(Person) ARRAY))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (owners)"));

		MariusQLResultSet resultset = statement.executeQuery("SELECT rid FROM Person");
		List<Long> owners = new ArrayList<Long>();
		while (resultset.next()) {
			owners.add(resultset.getLong(1));
		}
		Assert.assertEquals(2, owners.size());

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (owners) " + "values ("
				+ MariusQLHelper.getCollectionAssociationOntoQLValue(owners) + ")"));
		resultset = statement.executeQuery("SELECT owners FROM Vehicule");
		Assert.assertTrue(resultset.next());
		Assert.assertEquals(MariusQLHelper.getCollectionAssociationSQLValue(owners),
				this.listToString(resultset.getList(1, Long.class)));
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLInsertWithCollectionOfReferenceErrors() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLInsertWithCollectionOfReferenceErrors()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Person (name)"));

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Pink')"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Blue')"));

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (owners REF(Person) ARRAY))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (owners)"));

		ResultSet resultset = statement.executeQuery("SELECT rid FROM Person");
		List<Long> owners = new ArrayList<Long>();
		while (resultset.next()) {
			owners.add(resultset.getLong(1));
		}
		Assert.assertEquals(2, owners.size());

		Long refNotExists = owners.get(owners.size() - 1) + 200;
		owners.add(refNotExists);

		// reference not exists
		try {
			Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (owners) " + "VALUES ("
					+ MariusQLHelper.getCollectionAssociationOntoQLValue(owners) + ")"));
			Assert.fail("Don't throw an MariusQLException");
		} catch (MariusQLException e) {
			Assert.assertEquals("Reference on class Person not exists: " + refNotExists, e.getMessage());
		}

		// nothing inserted
		resultset = statement.executeQuery("SELECT owners FROM Vehicule");
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLInsertWithCollectionOfReferenceAndInheritance() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLInsertWithCollectionOfReferenceAndInheritance()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Male UNDER Person (DESCRIPTOR (#code = 'Male'))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Female UNDER Person (DESCRIPTOR (#code = 'Female'))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS SexyFemale UNDER Female (DESCRIPTOR (#code = 'SexyFemale'))"));
		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (owners REF(Person) ARRAY))"));

		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Person (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Male (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Female (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF SexyFemale (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (owners)"));

		// insert two different kind of person
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO SexyFemale (name) values ('Ms. Pink')"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Male (name) values ('Mr. Blue')"));

		MariusQLResultSet resultset = statement.executeQuery("SELECT rid FROM Person");
		List<Long> owners = new ArrayList<Long>();
		while (resultset.next()) {
			owners.add(resultset.getLong(1));
		}
		Assert.assertEquals(2, owners.size());

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (owners) values ("
				+ MariusQLHelper.getCollectionAssociationOntoQLValue(owners) + ")"));
		resultset = statement.executeQuery("SELECT owners FROM Vehicule");
		Assert.assertTrue(resultset.next());
		Assert.assertEquals(MariusQLHelper.getCollectionAssociationSQLValue(owners),
				this.listToString(resultset.getList(1, Long.class)));
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLInsertWithCollectionOfReferenceAndInheritanceErrors() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLInsertWithCollectionOfReferenceAndInheritanceErrors()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Male UNDER Person (DESCRIPTOR (#code = 'Male'))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Female UNDER Person (DESCRIPTOR (#code = 'Female'))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS SexyFemale UNDER Female (DESCRIPTOR (#code = 'SexyFemale'))"));
		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (owners REF(Person) ARRAY))"));

		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Person (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Male (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Female (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF SexyFemale (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (owners)"));

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO SexyFemale (name) values ('Ms. Pink')"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Male (name) values ('Mr. Blue')"));

		ResultSet resultset = statement.executeQuery("SELECT rid FROM Person");
		List<Long> owners = new ArrayList<Long>();
		while (resultset.next()) {
			owners.add(resultset.getLong(1));
		}
		Assert.assertEquals(2, owners.size());

		Long refNotExists = owners.get(owners.size() - 1) + 200;
		owners.add(refNotExists);

		// reference does not exist
		try {
			statement.executeUpdate("INSERT INTO Vehicule (owners) values ("
					+ MariusQLHelper.getCollectionAssociationOntoQLValue(owners) + ")");
			Assert.fail("Must throw an exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Reference on class Person not exists: " + refNotExists, e.getMessage());
		}

		// nothing inserted
		resultset = statement.executeQuery("SELECT owners FROM Vehicule");
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLDeleteWithErrors() {
		log.debug("MariusQLIMLTest.testIMLWithErrors()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst)"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst) values (0)"));

		// Delete a non defined property.
		try {
			Assert.assertEquals(1, statement.executeUpdate("delete from Vehicule where propertyInt = 1"));
			Assert.fail("Must throw an exception");
		} catch (Exception e) {
			Assert.assertEquals("Attribute/Property is not defined: propertyInt", e.getMessage());
		}

		// Delete a non used property
		try {
			Assert.assertEquals(1, statement.executeUpdate("delete from Vehicule where propertyIntSecond = 1"));
			Assert.fail("Must throw an exception");
		} catch (Exception e) {
			Assert.assertEquals("propertyIntSecond is not a used property.", e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLDeleteWithSimpleType() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLInsertWithSimpleType()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (p1 int, p2 real, p3 boolean, p4 string))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (p1, p2, p3, p4)"));

		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (p1, p2, p3, p4) values (1, 1.5, true, 'string')"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (p1, p2, p3, p4) values (2, 2.5, false, 'string')"));

		ResultSet resultset = statement.executeQuery("SELECT p1, p2, p3, p4 FROM Vehicule");
		Assert.assertTrue(resultset.next() && resultset.next());
		Assert.assertFalse(resultset.next());

		// delete the row inserted
		Assert.assertEquals(1, statement
				.executeUpdate("delete from Vehicule where p1 = 1 and p2 = 1.5 and p3 = true and p4 = 'string'"));
		resultset = statement.executeQuery("SELECT p1, p2, p3, p4 FROM Vehicule");
		Assert.assertTrue(resultset.next());
		Assert.assertEquals(2, resultset.getLong(1));
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLDeleteWithReference() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLInsertWithReference()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Person (name)"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Pink')"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mrs. Brown')"));

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (owner ref(Person)))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (owner)"));

		ResultSet resultset = statement.executeQuery("SELECT rid FROM Person where name = 'Mr. Pink'");
		Assert.assertTrue(resultset.next());
		Long owner = resultset.getLong(1);

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (owner) values (" + owner + ")"));
		resultset = statement.executeQuery("SELECT owner FROM Vehicule");
		Assert.assertTrue(resultset.next());
		Assert.assertEquals(owner.longValue(), resultset.getLong(1));
		Assert.assertFalse(resultset.next());

		// delete a data non referenced
		Assert.assertEquals(1, statement.executeUpdate("delete from Person where name = 'Mrs. Brown'"));
		resultset = statement.executeQuery("SELECT name FROM Person where name = 'Mrs. Brown'");
		Assert.assertFalse(resultset.next());

		// delete the row inserted using a reference first
		Assert.assertEquals(1, statement.executeUpdate("delete from Vehicule where owner =" + owner));
		resultset = statement.executeQuery("SELECT owner FROM Vehicule");
		Assert.assertFalse(resultset.next());

		// The we can delete a referenced data
		Assert.assertEquals(1, statement.executeUpdate("delete from Person where name = 'Mr. Pink'"));
		resultset = statement.executeQuery("SELECT name FROM Person where name = 'Mr. Pink'");
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLDeleteWithReferenceWithErrors() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLInsertWithReference()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Person (name)"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Pink')"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mrs. Brown')"));

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (owner ref(Person)))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (owner)"));

		ResultSet resultset = statement.executeQuery("SELECT rid FROM Person");
		Assert.assertTrue(resultset.next());
		Long owner = resultset.getLong(1);

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (owner) values (" + owner + ")"));
		resultset = statement.executeQuery("SELECT owner FROM Vehicule");
		Assert.assertTrue(resultset.next());
		Assert.assertEquals(owner.longValue(), resultset.getLong(1));
		Assert.assertFalse(resultset.next());

		try {
			Assert.assertEquals(1, statement.executeUpdate("delete from Person where name = 'Mr. Pink'"));
			Assert.fail("Must throw an exception");

		} catch (MariusQLException e) {
			Assert.assertEquals("You can not remove a referenced data.", e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLDeleteWithCollectionOfReferences() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLInsertCollectionOfReferences()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Person (name)"));

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Pink')"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Blue')"));

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (owners REF(Person) ARRAY))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (owners)"));

		ResultSet resultset = statement.executeQuery("SELECT rid FROM Person");
		List<Long> owners = new ArrayList<Long>();
		while (resultset.next()) {
			owners.add(resultset.getLong(1));
		}
		Assert.assertEquals(2, owners.size());

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (owners) " + "values ("
				+ MariusQLHelper.getCollectionAssociationOntoQLValue(owners) + ")"));

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Beans')"));
		resultset = statement.executeQuery("SELECT rid FROM Person where name = 'Mr. Beans'");
		Assert.assertTrue(resultset.next());

		// delete a data non used in a collection
		Assert.assertEquals(1, statement.executeUpdate("delete from Person where name = 'Mr. Beans'"));
		resultset = statement.executeQuery("SELECT name FROM Person where name = 'Mr. Beans'");
		Assert.assertFalse(resultset.next());

		// delete the data using the collection of references first
		Assert.assertEquals(1, statement.executeUpdate("delete from Vehicule"));
		resultset = statement.executeQuery("SELECT rid FROM Vehicule");
		Assert.assertFalse(resultset.next());

		// then delete a data previously used in a collection
		Assert.assertEquals(1, statement.executeUpdate("delete from Person where name = 'Mr. Pink'"));
		resultset = statement.executeQuery("SELECT name FROM Person where name = 'Mr. Pink'");
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLDeleteWithCollectionOfReferencesWithErrors() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLInsertCollectionOfReferences()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Person (name)"));

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Pink')"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Blue')"));

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (owners REF(Person) ARRAY))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (owners)"));

		ResultSet resultset = statement.executeQuery("SELECT rid FROM Person");
		List<Long> owners = new ArrayList<Long>();
		while (resultset.next()) {
			owners.add(resultset.getLong(1));
		}
		Assert.assertEquals(2, owners.size());

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (owners) " + "values ("
				+ MariusQLHelper.getCollectionAssociationOntoQLValue(owners) + ")"));

		// delete a data used in a collection of reference
		try {
			Assert.assertEquals(1, statement.executeUpdate("delete from Person where name = 'Mr. Pink'"));
			Assert.fail("Must throw an exception");

		} catch (MariusQLException e) {
			Assert.assertEquals("You can not remove a referenced data.", e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLUpdateWithErrors() {
		log.debug("MariusQLIMLTest.testIMLUpdateWithErrors()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (p1 int, p2 int))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (p1)"));

		try {
			statement.executeUpdate("UPDATE Vehicule SET p2 = 1");
			Assert.fail("Must throw an exception");
		} catch (MariusQLException e) {
			e.printStackTrace();
			Assert.assertEquals("p2 is not an used property.", e.getMessage());
		} finally {
			this.getSession().rollback();
		}
		statement.close();
	}

	@Test
	public void testIMLUpdateWithSimpleType() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLUpdateWithSimpleType()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (p1 int, p2 real, p3 boolean, p4 string))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (p1, p2, p3, p4)"));

		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (p1, p2, p3, p4) values (1, 1.5, true, 'insert')"));
		Assert.assertEquals(1,
				statement.executeUpdate("UPDATE Vehicule SET p1 = 2, p2 = 2.5, p3 = false, p4 = 'update'"));

		ResultSet resultset = statement.executeQuery("SELECT p1, p2, p3, p4 FROM Vehicule");
		Assert.assertTrue(resultset.next());
		Assert.assertEquals(2, resultset.getInt(1));
		Assert.assertEquals(2.5, resultset.getFloat(2), .0);
		Assert.assertEquals(false, resultset.getBoolean(3));
		Assert.assertEquals("update", resultset.getString(4));
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLUpdateWhereWithSimpleType() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLUpdateWhereWithSimpleType()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (PROPERTIES (key string, p1 int, p2 real, p3 boolean, p4 string))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (key, p1, p2, p3, p4)"));

		Assert.assertEquals(1, statement
				.executeUpdate("INSERT INTO Vehicule (key, p1, p2, p3, p4) values ('i1', 1, 1.5, true, 'insert1')"));
		Assert.assertEquals(1, statement
				.executeUpdate("INSERT INTO Vehicule (key, p1, p2, p3, p4) values ('i2', 2, 2.5, true, 'insert2')"));

		Assert.assertEquals(1, statement
				.executeUpdate("UPDATE Vehicule SET p1 = 3, p2 = 3.5, p3 = false, p4 = 'update' WHERE key = 'i1'"));

		ResultSet resultset = statement.executeQuery("SELECT key, p1, p2, p3, p4 FROM Vehicule WHERE key = 'i1'");
		Assert.assertTrue(resultset.next());
		Assert.assertEquals("i1", resultset.getString(1));
		Assert.assertEquals(3, resultset.getInt(2));
		Assert.assertEquals(3.5, resultset.getFloat(3), .0);
		Assert.assertEquals(false, resultset.getBoolean(4));
		Assert.assertEquals("update", resultset.getString(5));
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLUpdateWithReference() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLUpdateWithReference()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Person (name)"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Blue')"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Ms. Pink')"));

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (owner ref(Person)))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (owner)"));

		ResultSet resultset = statement.executeQuery("SELECT rid FROM Person WHERE name = 'Mr. Blue'");
		Assert.assertTrue(resultset.next());
		Long mrBlueRid = resultset.getLong(1);
		Assert.assertFalse(resultset.next());

		resultset = statement.executeQuery("SELECT rid FROM Person WHERE name = 'Ms. Pink'");
		Assert.assertTrue(resultset.next());
		Long msPinkRid = resultset.getLong(1);
		Assert.assertFalse(resultset.next());

		// two cars for Mr. Blue
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (owner) values (" + mrBlueRid + ")"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (owner) values (" + mrBlueRid + ")"));

		// Mr. Blue offer both cars to Ms. Pink
		Assert.assertEquals(2, statement.executeUpdate("UPDATE Vehicule SET owner = " + msPinkRid));

		resultset = statement.executeQuery("SELECT owner FROM Vehicule");
		Assert.assertTrue(resultset.next());
		Assert.assertEquals(msPinkRid.longValue(), resultset.getLong(1));
		Assert.assertTrue(resultset.next());
		Assert.assertEquals(msPinkRid.longValue(), resultset.getLong(1));
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLUpdateWithReferenceErrors() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLUpdateWithReferenceErrors()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Person (name)"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Blue')"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Ms. Pink')"));

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (owner ref(Person)))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (owner)"));

		ResultSet resultset = statement.executeQuery("SELECT rid FROM Person WHERE name = 'Mr. Blue'");
		Assert.assertTrue(resultset.next());
		Long mrBlueRid = resultset.getLong(1);
		Assert.assertFalse(resultset.next());

		// two cars for Mr. Blue
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (owner) values (" + mrBlueRid + ")"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (owner) values (" + mrBlueRid + ")"));

		Long unknownPerson = (mrBlueRid + 200);

		// Mr. Blue offer both cars to an unknown person
		try {
			statement.executeUpdate("UPDATE Vehicule SET owner = " + unknownPerson);
			Assert.fail("Don't throw an MariusQLException");
		} catch (MariusQLException e) {
			Assert.assertEquals("Reference on class Person not exists: " + unknownPerson, e.getMessage());
		}

		// nothing changed
		resultset = statement.executeQuery("SELECT owner FROM Vehicule");
		Assert.assertTrue(resultset.next());
		Assert.assertEquals(mrBlueRid.longValue(), resultset.getLong(1));
		Assert.assertTrue(resultset.next());
		Assert.assertEquals(mrBlueRid.longValue(), resultset.getLong(1));
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLUpdateWithReferenceAndInheritance() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLUpdateWithReferenceAndInheritance()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Male UNDER Person (DESCRIPTOR (#code = 'Male'))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Female UNDER Person (DESCRIPTOR (#code = 'Female'))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS SexyFemale UNDER Female (DESCRIPTOR (#code = 'SexyFemale'))"));
		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (owner ref(Person)))"));

		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Male (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Female (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF SexyFemale (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (owner)"));

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO SexyFemale (name) values ('Ms. Pink')"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Male (name) values ('Mr. Blue')"));

		ResultSet resultset = statement.executeQuery("SELECT rid FROM Male where name = 'Mr. Blue'");
		Assert.assertTrue(resultset.next());
		Long mrBlueRid = resultset.getLong(1);
		Assert.assertFalse(resultset.next());

		resultset = statement.executeQuery("SELECT rid FROM SexyFemale where name = 'Ms. Pink'");
		Assert.assertTrue(resultset.next());
		Long msPinkRid = resultset.getLong(1);
		Assert.assertFalse(resultset.next());

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (owner) values (" + mrBlueRid + ")"));
		statement.executeUpdate("UPDATE Vehicule SET owner = " + msPinkRid);

		resultset = statement.executeQuery("SELECT owner FROM Vehicule");
		Assert.assertTrue(resultset.next());
		Assert.assertEquals(msPinkRid.longValue(), resultset.getLong(1));
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLUpdateWithCollectionOfReference() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLUpdateWithCollectionOfReference()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Person (name)"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Pink')"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Black')"));

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (owners ref(Person) array))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (owners)"));

		MariusQLResultSet resultset = statement.executeQuery("SELECT rid FROM Person");
		List<Long> owners = new ArrayList<Long>();
		while (resultset.next()) {
			owners.add(resultset.getLong(1));
		}
		Assert.assertEquals(2, owners.size());
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (owners) values ("
				+ MariusQLHelper.getCollectionAssociationOntoQLValue(owners) + ")"));

		List<Long> newOwners = new ArrayList<Long>(owners);
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Blue')"));
		resultset = statement.executeQuery("SELECT rid FROM Person WHERE name = 'Mr. Blue'");
		Assert.assertTrue(resultset.next());
		newOwners.add(resultset.getLong(1));
		Assert.assertFalse(resultset.next());

		Assert.assertEquals(1, statement.executeUpdate(
				"UPDATE Vehicule SET owners = " + MariusQLHelper.getCollectionAssociationOntoQLValue(newOwners)));

		resultset = statement.executeQuery("SELECT owners FROM Vehicule");
		Assert.assertTrue(resultset.next());
		Assert.assertEquals(MariusQLHelper.getCollectionAssociationSQLValue(newOwners),
				this.listToString(resultset.getList(1, Long.class)));
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLUpdateWithCollectionOfReferenceErrors() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLUpdateWithCollectionOfReferenceErrors()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Person (name)"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Pink')"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Person (name) values ('Mr. Black')"));

		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (owners ref(Person) array))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (owners)"));

		MariusQLResultSet resultset = statement.executeQuery("SELECT rid FROM Person");
		List<Long> owners = new ArrayList<Long>();
		while (resultset.next()) {
			owners.add(resultset.getLong(1));
		}
		Assert.assertEquals(2, owners.size());
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (owners) values ("
				+ MariusQLHelper.getCollectionAssociationOntoQLValue(owners) + ")"));

		Long refNotExists = owners.get(owners.size() - 1) + 200;
		List<Long> newOwners = new ArrayList<Long>(owners);
		newOwners.add(refNotExists);

		// reference does not exist
		try {
			statement.executeUpdate(
					"UPDATE Vehicule SET owners = " + MariusQLHelper.getCollectionAssociationOntoQLValue(newOwners));
			Assert.fail("Must throw an exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Reference on class Person not exists: " + refNotExists, e.getMessage());
		}

		// nothing changed
		resultset = statement.executeQuery("SELECT owners FROM Vehicule");
		Assert.assertTrue(resultset.next());
		Assert.assertEquals(MariusQLHelper.getCollectionAssociationSQLValue(owners),
				this.listToString(resultset.getList(1, Long.class)));
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLUpdateWithCollectionOfReferenceAndInheritance() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLUpdateWithCollectionOfReferenceAndInheritance()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Male UNDER Person (DESCRIPTOR (#code = 'Male'))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Female UNDER Person (DESCRIPTOR (#code = 'Female'))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS SexyFemale UNDER Female (DESCRIPTOR (#code = 'SexyFemale'))"));
		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (owners REF(Person) ARRAY))"));

		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Person (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Male (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Female (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF SexyFemale (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (owners)"));

		// insert two different kind of person
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO SexyFemale (name) values ('Ms. Pink')"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Male (name) values ('Mr. Blue')"));

		MariusQLResultSet resultset = statement.executeQuery("SELECT rid FROM Person");
		List<Long> owners = new ArrayList<Long>();
		while (resultset.next()) {
			owners.add(resultset.getLong(1));
		}
		Assert.assertEquals(2, owners.size());
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (owners) values ("
				+ MariusQLHelper.getCollectionAssociationOntoQLValue(owners) + ")"));

		List<Long> newOwners = new ArrayList<Long>(owners);
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Female (name) values ('Ms. Black')"));
		resultset = statement.executeQuery("SELECT rid FROM Female f WHERE f.name = 'Ms. Black'");
		Assert.assertTrue(resultset.next());
		newOwners.add(resultset.getLong(1));

		Assert.assertEquals(1, statement.executeUpdate(
				"UPDATE Vehicule SET owners = " + MariusQLHelper.getCollectionAssociationOntoQLValue(newOwners)));

		resultset = statement.executeQuery("SELECT owners FROM Vehicule");
		Assert.assertTrue(resultset.next());
		Assert.assertEquals(MariusQLHelper.getCollectionAssociationSQLValue(newOwners),
				this.listToString(resultset.getList(1, Long.class)));
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIMLUpdateWithCollectionOfReferenceAndInheritanceErrors() throws SQLException {
		log.debug("MariusQLIMLTest.testIMLUpdateWithCollectionOfReferenceAndInheritanceErrors()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Male UNDER Person (DESCRIPTOR (#code = 'Male'))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Female UNDER Person (DESCRIPTOR (#code = 'Female'))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS SexyFemale UNDER Female (DESCRIPTOR (#code = 'SexyFemale'))"));
		Assert.assertEquals(1, statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (owners REF(Person) ARRAY))"));

		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Person (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Male (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Female (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF SexyFemale (name)"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (owners)"));

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO SexyFemale (name) values ('Ms. Pink')"));
		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Male (name) values ('Mr. Blue')"));

		MariusQLResultSet resultset = statement.executeQuery("SELECT rid FROM Person");
		List<Long> owners = new ArrayList<Long>();
		while (resultset.next()) {
			owners.add(resultset.getLong(1));
		}
		Assert.assertEquals(2, owners.size());

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (owners) values ("
				+ MariusQLHelper.getCollectionAssociationOntoQLValue(owners) + ")"));

		Long refNotExists = owners.get(owners.size() - 1) + 200;
		List<Long> newOwners = new ArrayList<Long>(owners);
		newOwners.add(refNotExists);

		// reference does not exist
		try {
			statement.executeUpdate(
					"UPDATE Vehicule SET owners = " + MariusQLHelper.getCollectionAssociationOntoQLValue(newOwners));
			Assert.fail("Must throw an exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Reference on class Person not exists: " + refNotExists, e.getMessage());
		}

		// nothing changed
		resultset = statement.executeQuery("SELECT owners FROM Vehicule");
		Assert.assertTrue(resultset.next());
		Assert.assertEquals(MariusQLHelper.getCollectionAssociationSQLValue(owners),
				this.listToString(resultset.getList(1, Long.class)));
		Assert.assertFalse(resultset.next());

		this.getSession().rollback();
		statement.close();
	}
}
