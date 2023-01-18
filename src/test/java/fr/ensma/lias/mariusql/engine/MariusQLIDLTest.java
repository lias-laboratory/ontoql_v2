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

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.mariusql.AbstractMariusQLTest;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.model.MClass;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSet;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;
import fr.ensma.lias.mariusql.util.MariusQLHelper;

/**
 * This test class is for Data Definition Language (DDL).
 * 
 * @author Mickael BARON
 * @author Ghada TRIKI
 * @author Florian MHUN
 */
public class MariusQLIDLTest extends AbstractMariusQLTest {

	private Logger log = LoggerFactory.getLogger(MariusQLIDLTest.class);

	@Test
	public void testIDLCreateClassExtentWithSimpleTypesWithoutCache() {
		log.debug("MariusQLIDLTest.testIDLCreateClassExtentWithSimpleTypesWithoutCache()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement
				.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (prop1 int))");
		Assert.assertEquals(1, res);

		// this.getSession().getModelCache().clean();

		res = statement.executeUpdate("CREATE EXTENT OF Vehicule (prop1)");
		Assert.assertEquals(1, res);

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testIDLCreateClassExtentCheckErrors() {
		log.debug("MariusQLIDLTest.testIDLCreateClassExtentCheckErrors()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement
				.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (prop1 int))");
		Assert.assertEquals(1, res);

		// Check a bad class identifier.
		try {
			statement.executeUpdate("CREATE EXTENT OF Vehicle (prop2)");

			Assert.fail("Must throw an exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Class not found: Vehicle", e.getMessage());
		}

		// Check a candidate used property is not defined.
		try {
			statement.executeUpdate("CREATE EXTENT OF Vehicule (prop2)");

			Assert.fail("Must throw an exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Property is not defined: prop2", e.getMessage());
		}

		// Cannot create an extent from a static class.
		res = statement.executeUpdate("CREATE #STATICCLASS VehiculeStatic (DESCRIPTOR (#code = 'VehiculeStatic'))");
		try {
			// need to get a property identifier even if this property does not exist.
			statement.executeUpdate("CREATE EXTENT OF VehiculeStatic (prop2)");

			Assert.fail("Must throw an exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Can not extent a static class: VehiculeStatic", e.getMessage());
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testIDLCreateClassExtentWithInternalIdentifiers() throws SQLException {
		log.debug("MariusQLIDLTest.testIDLCreateClassExtentWithInternalIdentifiers()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (p1 int, p2 int))");
		Assert.assertEquals(1, res);

		int vehiculeRid = 0;
		MariusQLResultSet executeQuery = statement.executeQuery("SELECT #rid from #class where #code = 'Vehicule'");
		Assert.assertTrue(executeQuery.next());
		vehiculeRid = executeQuery.getInt(1);
		Assert.assertTrue(vehiculeRid != 0);

		int propertyRid = 0;
		executeQuery = statement.executeQuery("SELECT #rid from #property where #code = 'p1'");
		Assert.assertTrue(executeQuery.next());
		propertyRid = executeQuery.getInt(1);
		Assert.assertTrue(propertyRid != 0);

		final String query = "CREATE EXTENT OF !" + vehiculeRid + " (!" + propertyRid + ")";
		res = statement.executeUpdate(query);
		Assert.assertEquals(1, res);

		Assert.assertEquals(1, statement.executeUpdate("INSERT INTO Vehicule (p1) values (0)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO !" + vehiculeRid + " (!" + propertyRid + ") values (0)"));

		executeQuery = statement.executeQuery("SELECT p1 from Vehicule");
		Assert.assertTrue(executeQuery.next());
		Assert.assertTrue(executeQuery.next());
		Assert.assertFalse(executeQuery.next());

		this.getSession().rollback();
	}

	@Test
	public void testIDLCreateClassExtentWithInheritanceAndInternalIdentifiers() throws SQLException {
		log.debug("MariusQLIDLTest.testIDLCreateClassExtentWithInternalIdentifiers()");

		this.getSession().setReferenceLanguage(MariusQLConstants.FRENCH);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		this.getSession().getModelCache().setEnabled(false);

		statement.executeUpdate("CREATE #CLASS \"Composant CFCA\" (DESCRIPTOR (#code ='0002-41982799300025#01-1#1')))");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Fiche Technique\" URIType DESCRIPTOR (#code = '0002-41982799300025#02-1#1', #definition[fr] = 'Référence externe à une description technique du composant')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Référence ERP\" String DESCRIPTOR (#code = '0002-41982799300025#02-2#1', #definition[fr] = 'Référence créée dans l''ERP')");
		statement.executeUpdate(
				"ALTER #CLASS \"Composant CFCA\" ADD \"Désignation\" String DESCRIPTOR (#code = '0002-41982799300025#02-3#1', #definition[fr] = 'Désignation')");
		statement.executeUpdate(
				"CREATE #CLASS \"Accessoires\" UNDER \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-2#1'))");
		statement.executeUpdate(
				"CREATE #CLASS Connexions UNDER \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-17#1'))");
		statement.executeUpdate(
				"ALTER #CLASS Connexions ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-25#1', #definition[fr] = 'Poids d''une unité de composant')");
		statement.executeUpdate(
				"CREATE #CLASS Manchon UNDER Connexions (DESCRIPTOR (#code = '0002-41982799300025#01-26#1'))");

		int manchonRid = 0;
		MariusQLResultSet executeQuery = statement.executeQuery("SELECT #rid from #class where #name[fr] = 'Manchon'");
		Assert.assertTrue(executeQuery.next());
		manchonRid = executeQuery.getInt(1);
		Assert.assertTrue(manchonRid != 0);

		int ficheTechniqueRid = 0;
		executeQuery = statement.executeQuery("SELECT #rid from #property where #name[fr] = 'Fiche Technique'");
		Assert.assertTrue(executeQuery.next());
		ficheTechniqueRid = executeQuery.getInt(1);
		Assert.assertTrue(ficheTechniqueRid != 0);

		int referenceERPRid = 0;
		executeQuery = statement.executeQuery("SELECT #rid from #property where #name[fr] = 'Référence ERP'");
		Assert.assertTrue(executeQuery.next());
		referenceERPRid = executeQuery.getInt(1);
		Assert.assertTrue(referenceERPRid != 0);

		int designationRid = 0;
		executeQuery = statement.executeQuery("SELECT #rid from #property where #name[fr] = 'Désignation'");
		Assert.assertTrue(executeQuery.next());
		designationRid = executeQuery.getInt(1);
		Assert.assertTrue(designationRid != 0);

		int rset = statement.executeUpdate("CREATE EXTENT OF !" + manchonRid + " (!" + referenceERPRid + ",!"
				+ ficheTechniqueRid + ",!" + designationRid + ")");
		Assert.assertEquals(1, rset);
		rset = statement.executeUpdate("insert INTO !" + manchonRid + " (!" + referenceERPRid + ",!" + ficheTechniqueRid
				+ ",!" + designationRid + ") VALUES ('test', 'test', 'test')");
		Assert.assertEquals(1, rset);

		executeQuery = statement
				.executeQuery("SELECT \"Fiche Technique\", \"Référence ERP\", \"Désignation\" from Manchon");
		Assert.assertTrue(executeQuery.next());
		Assert.assertFalse(executeQuery.next());

		this.getSession().rollback();
	}

	@Test
	public void testIDLCreateClassExtentWithSimpleTypes() {
		log.debug("MariusQLIDLTest.testIDLCreateClassExtentWithSimpleTypes()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (p1 int, p2 real, p3 boolean, p4 string))");
		Assert.assertEquals(1, res);

		MClass klass = FactoryCore.createExistingMClass(getSession(), "Vehicule");
		Assert.assertNotNull(klass);

		String expectedUsedProperties = MariusQLHelper.getCollectionAssociationSQLValue(Arrays.asList(
				klass.getDefinedDescription("p1").getInternalId(), klass.getDefinedDescription("p2").getInternalId(),
				klass.getDefinedDescription("p3").getInternalId(), klass.getDefinedDescription("p4").getInternalId()));

		res = statement.executeUpdate("CREATE EXTENT OF Vehicule (p1, p2, p3, p4)");
		Assert.assertEquals(1, res);
		Assert.assertEquals(4, klass.getUsedProperties().size());

		try {
			MariusQLResultSet resultset = statement
					.executeQuery("select #usedproperties from #class where #code = 'Vehicule'");

			Assert.assertTrue(resultset.next());
			Assert.assertEquals(expectedUsedProperties,
					this.listToString(resultset.getList("usedproperties", Long.class)));
			resultset.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testIDLCreateClassExtentWithReference() {
		log.debug("MariusQLIDLTest.testIDLCreateClassExtentWithReference()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement
				.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (p2 int, p3 int) )");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE EXTENT OF Vehicule (p2)");
		Assert.assertEquals(1, res);

		res = statement
				.executeUpdate("CREATE #CLASS Car (DESCRIPTOR (#code = 'Car') PROPERTIES (p1 ref(Vehicule), p4 int))");
		Assert.assertEquals(1, res);

		MClass klass = FactoryCore.createExistingMClass(getSession(), "Car");
		Assert.assertNotNull(klass);

		MProperty ref = (MProperty) klass.getDefinedDescription("p1");
		Assert.assertNotNull(ref);

		String expectedUsedProperties = MariusQLHelper
				.getCollectionAssociationSQLValue(Arrays.asList(ref.getInternalId()));

		res = statement.executeUpdate("CREATE EXTENT OF Car (p1)");
		Assert.assertEquals(1, res);
		Assert.assertEquals(1, klass.getUsedProperties().size());

		try {
			MariusQLResultSet resultset = statement
					.executeQuery("select #usedproperties from #class where #code = 'Car'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals(expectedUsedProperties,
					this.listToString(resultset.getList("usedproperties", Long.class)));
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testIDLCreateClassExtentWithCollectionOfSimpleTypes() {
		log.debug("MariusQLIDLTest.testIDLCreateClassExtentWithCollectionOfSimpleTypes()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate(
				"CREATE #CLASS Car (DESCRIPTOR (#code = 'Car') PROPERTIES (p1 int array, p2 real array, p3 boolean array, p4 string array))");
		Assert.assertEquals(1, res);

		MClass klass = FactoryCore.createExistingMClass(getSession(), "Car");
		Assert.assertNotNull(klass);

		MProperty p1 = (MProperty) klass.getDefinedDescription("p1");
		Assert.assertNotNull(p1);
		MProperty p2 = (MProperty) klass.getDefinedDescription("p2");
		Assert.assertNotNull(p2);
		MProperty p3 = (MProperty) klass.getDefinedDescription("p3");
		Assert.assertNotNull(p3);
		MProperty p4 = (MProperty) klass.getDefinedDescription("p4");
		Assert.assertNotNull(p4);

		String expectedUsedProperties = MariusQLHelper.getCollectionAssociationSQLValue(
				Arrays.asList(p1.getInternalId(), p2.getInternalId(), p3.getInternalId(), p4.getInternalId()));

		res = statement.executeUpdate("CREATE EXTENT OF Car (p1, p2, p3, p4)");
		Assert.assertEquals(1, res);
		Assert.assertEquals(4, klass.getUsedProperties().size());

		try {
			MariusQLResultSet resultset = statement
					.executeQuery("select #usedproperties from #class where #code = 'Car'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals(expectedUsedProperties,
					this.listToString(resultset.getList("usedproperties", Long.class)));
			resultset.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testIDLCreateClassExtentWithCollectionOfReferences() {
		log.debug("MariusQLIDLTest.testIDLCreateClassExtentWithCollectionOfReferences()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule'))");
		Assert.assertEquals(1, res);

		res = statement
				.executeUpdate("CREATE #CLASS Car (DESCRIPTOR (#code = 'Car') PROPERTIES (p1 ref(Vehicule) array))");
		Assert.assertEquals(1, res);

		MClass klass = FactoryCore.createExistingMClass(getSession(), "Car");
		Assert.assertNotNull(klass);

		MProperty collection = (MProperty) klass.getDefinedDescription("p1");
		Assert.assertNotNull(collection);

		String expectedUsedProperties = MariusQLHelper
				.getCollectionAssociationSQLValue(Arrays.asList(klass.getDefinedDescription("p1").getInternalId()));

		res = statement.executeUpdate("CREATE EXTENT OF Car (p1)");
		Assert.assertEquals(1, res);
		Assert.assertEquals(1, klass.getUsedProperties().size());

		try {
			MariusQLResultSet resultset = statement
					.executeQuery("select #usedproperties from #class where #code = 'Car'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals(expectedUsedProperties,
					this.listToString(resultset.getList("usedproperties", Long.class)));
			resultset.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testIDLDropClassExtentCheckNotExistingErrors() {
		log.debug("MariusQLIDLTest.testIDLDropClassExtentCheckErrors()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement
				.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (prop1 int))");
		Assert.assertEquals(1, res);

		// Check a bad class identifier.
		try {
			statement.executeUpdate("DROP EXTENT OF Vehicle");
			Assert.fail("Must throw an exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Class not found: Vehicle", e.getMessage());
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testIDLDropClassExtentWithSimpleTypes() {
		log.debug("MariusQLIDLTest.testIDLDropClassExtentWithSimpleTypes()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (p1 int, p2 real, p3 boolean, p4 string))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE EXTENT OF Vehicule (p1, p2, p3, p4)");
		Assert.assertEquals(1, res);

		MClass klass = FactoryCore.createExistingMClass(getSession(), "Vehicule");
		Assert.assertNotNull(klass);

		res = statement.executeUpdate("DROP EXTENT OF Vehicule");
		Assert.assertEquals(1, res);
		Assert.assertEquals(0, klass.getUsedProperties().size());

		try {
			MariusQLResultSet resultset = statement
					.executeQuery("select #usedproperties from #class where #code = 'Vehicule'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals("{}", this.listToString(resultset.getList("usedproperties", Integer.class)));
			resultset.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testIDLDropClassExtentWithInstanceReference() throws SQLException {
		log.debug("MariusQLIDLTest.testIDLDropClassExtentCheckReference()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (owner ref(Person)))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE EXTENT OF Person (name)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE EXTENT OF Vehicule (owner)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("INSERT INTO Person (name) VALUES ('Mr. Pink')");
		Assert.assertEquals(1, res);

		MariusQLResultSet resultset = statement.executeQuery("SELECT rid FROM Person");
		Assert.assertTrue(resultset.next());
		Integer owner = resultset.getInt(1);

		res = statement.executeUpdate("INSERT INTO Vehicule (owner) VALUES (" + owner + ")");
		Assert.assertEquals(1, res);

		// Drop person extension is not possible because an instance of vehicule
		// extension
		// use a person reference.
		try {
			statement.executeUpdate("DROP EXTENT OF Person");
			Assert.fail("Must Throw a MariusQl Exception");
		} catch (MariusQLException e) {
			e.printStackTrace();
			Assert.assertEquals("Class extension is referenced", e.getMessage());
		}

		// Drop Vehicule class extension first
		res = statement.executeUpdate("DROP EXTENT OF Vehicule");
		Assert.assertEquals(1, res);

		// Then drop Person extension class
		res = statement.executeUpdate("DROP EXTENT OF Person");
		Assert.assertEquals(1, res);

		// Used properties must be empty in both classes
		MClass vehicule = FactoryCore.createExistingMClass(getSession(), "Vehicule");
		Assert.assertNotNull(vehicule);
		Assert.assertEquals(0, vehicule.getUsedProperties().size());

		MClass person = FactoryCore.createExistingMClass(getSession(), "Person");
		Assert.assertNotNull(person);
		Assert.assertEquals(0, person.getUsedProperties().size());

		// Ensure schema is modified
		try {
			resultset = statement.executeQuery("select #usedproperties from #class where #code = 'Vehicule'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals("{}", this.listToString(resultset.getList("usedproperties", Integer.class)));

			resultset = statement.executeQuery("select #usedproperties from #class where #code = 'Person'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals("{}", this.listToString(resultset.getList("usedproperties", Integer.class)));
			resultset.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testIDLDropClassExtentWithInstanceReferenceInCaseOfInheritence() throws SQLException {
		log.debug("MariusQLIDLTest.testIDLDropClassExtentWithInstanceReferenceInCaseOfInheritence()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule'))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate(
				"CREATE #CLASS Car UNDER Vehicule (DESCRIPTOR (#code = 'Car') PROPERTIES (owner ref(Person)))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate(
				"CREATE #CLASS Truck UNDER Vehicule (DESCRIPTOR (#code = 'Truck') PROPERTIES (owner ref(Person), description string))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE EXTENT OF Person (name)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE EXTENT OF Car (owner)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE EXTENT OF Truck (owner)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("INSERT INTO Person (name) VALUES ('Mr. Pink')");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("INSERT INTO Person (name) VALUES ('Mr. Red')");
		Assert.assertEquals(1, res);

		MariusQLResultSet resultset;
		resultset = statement.executeQuery("SELECT rid FROM Person where name = 'Mr. Pink'");
		Assert.assertTrue(resultset.next());
		Long ownerPink = resultset.getLong(1);

		res = statement.executeUpdate("INSERT INTO Car (owner) VALUES (" + ownerPink + ")");
		Assert.assertEquals(1, res);

		resultset = statement.executeQuery("SELECT rid FROM Person where name = 'Mr. Red'");
		Assert.assertTrue(resultset.next());
		Long ownerRed = resultset.getLong(1);

		res = statement.executeUpdate("INSERT INTO Truck (owner) VALUES (" + ownerRed + ")");
		Assert.assertEquals(1, res);

		// Drop person extension is not possible because an instance of vehicule
		// extension
		// use a person reference.
		try {
			statement.executeUpdate("DROP EXTENT OF Person");
			Assert.fail("Must throw a Marius Exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Class extension is referenced", e.getMessage());
		}

		// Drop Car and Truck class extensions first
		res = statement.executeUpdate("DROP EXTENT OF Car");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("DROP EXTENT OF Truck");
		Assert.assertEquals(1, res);

		// Then drop Person extension class
		res = statement.executeUpdate("DROP EXTENT OF Person");
		Assert.assertEquals(1, res);

		// Used properties must be empty in both classes
		MClass Car = FactoryCore.createExistingMClass(getSession(), "Car");
		Assert.assertNotNull(Car);
		Assert.assertEquals(0, Car.getUsedProperties().size());

		MClass Truck = FactoryCore.createExistingMClass(getSession(), "Truck");
		Assert.assertNotNull(Truck);
		Assert.assertEquals(0, Truck.getUsedProperties().size());

		MClass person = FactoryCore.createExistingMClass(getSession(), "Person");
		Assert.assertNotNull(person);
		Assert.assertEquals(0, person.getUsedProperties().size());

		try {
			resultset = statement.executeQuery("select #usedproperties from #class where #code = 'Car'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals("{}", this.listToString(resultset.getList("usedproperties", Long.class)));

			resultset = statement.executeQuery("select #usedproperties from #class where #code = 'Person'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals("{}", this.listToString(resultset.getList("usedproperties", Long.class)));

			resultset.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testIDLDropClassExtentWithInstanceReferenceInCaseOfInheritenceCase2() throws SQLException {
		log.debug("MariusQLIDLTest.testIDLDropClassExtentWithInstanceReferenceInCaseOfInheritenceCase2()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement
				.executeUpdate("CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate(
				"CREATE #CLASS Female UNDER Person (DESCRIPTOR (#code = 'Female') PROPERTIES (age int))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule'))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate(
				"CREATE #CLASS Car UNDER Vehicule (DESCRIPTOR (#code = 'Car') PROPERTIES (owner ref(Female)))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE EXTENT OF Person (name)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE EXTENT OF Female (name, age)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE EXTENT OF Car (owner)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("INSERT INTO Person (name) VALUES ('Mr. Pink')");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("INSERT INTO Female (name) VALUES ('Ms. Red')");
		Assert.assertEquals(1, res);

		MariusQLResultSet resultset = statement.executeQuery("SELECT rid FROM Female where name = 'Ms. Red'");
		Assert.assertTrue(resultset.next());
		Long owner_red = resultset.getLong(1);

		res = statement.executeUpdate("INSERT INTO Car (owner) VALUES (" + owner_red + ")");
		Assert.assertEquals(1, res);

		// Drop person extension is not possible because an instance of vehicule
		// extension
		// use a person reference.
		try {
			statement.executeUpdate("DROP EXTENT OF Female");
			Assert.fail("Must throw a Marius Exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Class extension is referenced", e.getMessage());
		}

		// Drop person extension is possible
		res = statement.executeUpdate("DROP EXTENT OF Person");
		Assert.assertEquals(1, res);

		// Drop Car and Truck class extensions first
		res = statement.executeUpdate("DROP EXTENT OF Car");
		Assert.assertEquals(1, res);

		// Then drop Person extension class
		res = statement.executeUpdate("DROP EXTENT OF Female");
		Assert.assertEquals(1, res);

		// Used properties must be empty in both classes
		MClass Car = FactoryCore.createExistingMClass(getSession(), "Car");
		Assert.assertNotNull(Car);
		Assert.assertEquals(0, Car.getUsedProperties().size());

		MClass person = FactoryCore.createExistingMClass(getSession(), "Female");
		Assert.assertNotNull(person);
		Assert.assertEquals(0, person.getUsedProperties().size());

		// Ensure schema is modified
		try {
			resultset = statement.executeQuery("select #usedproperties from #class where #code = 'Car'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals("{}", this.listToString(resultset.getList("usedproperties", Long.class)));

			resultset = statement.executeQuery("select #usedproperties from #class where #code = 'Female'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals("{}", this.listToString(resultset.getList("usedproperties", Long.class)));

			resultset.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testIDLDropClassExtentCheckInstanceCollectionOfReferences() throws SQLException {
		log.debug("MariusQLIDLTest.testIDLDropClassExtentCheckInstanceCollectionOfReferences()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE #CLASS Car (DESCRIPTOR (#code = 'Car') PROPERTIES (name string))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate(
				"CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string , cars ref(Car) array))");
		Assert.assertEquals(1, res);

		MClass klass = FactoryCore.createExistingMClass(getSession(), "Person");
		Assert.assertNotNull(klass);

		MProperty collection = (MProperty) klass.getDefinedDescription("cars");
		Assert.assertNotNull(collection);

		res = statement.executeUpdate("CREATE EXTENT OF Car (name)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE EXTENT OF Person (name , cars)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("INSERT INTO Car (name) VALUES ('Peugeot')");
		Assert.assertEquals(1, res);

		MariusQLResultSet resultset = statement.executeQuery("SELECT rid FROM Car where name = 'Peugeot'");
		Assert.assertTrue(resultset.next());
		Long peugeotRid = resultset.getLong(1);

		res = statement.executeUpdate("INSERT INTO Car (name) VALUES ('Renault')");
		Assert.assertEquals(1, res);

		resultset = statement.executeQuery("SELECT rid FROM Car where name = 'Renault'");
		Assert.assertTrue(resultset.next());
		Long renaultRid = resultset.getLong(1);

		res = statement.executeUpdate(
				"INSERT INTO Person (name, cars) VALUES ('Mr. Bean' , ARRAY[" + peugeotRid + "," + renaultRid + "])");
		Assert.assertEquals(1, res);

		// Drop Car extension is not possible because an instance of Person extension
		// uses a car reference.
		try {
			statement.executeUpdate("DROP EXTENT OF Car");
			Assert.fail("Must throw a Marius Exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Class extension is referenced", e.getMessage());
		}

		// Drop Person class extensions first
		res = statement.executeUpdate("DROP EXTENT OF Person");
		Assert.assertEquals(1, res);

		// Then drop Car extension class
		res = statement.executeUpdate("DROP EXTENT OF Car");
		Assert.assertEquals(1, res);

		// Used properties must be empty in both classes
		MClass person = FactoryCore.createExistingMClass(getSession(), "Person");
		Assert.assertNotNull(person);
		Assert.assertEquals(0, person.getUsedProperties().size());

		MClass car = FactoryCore.createExistingMClass(getSession(), "Car");
		Assert.assertNotNull(car);
		Assert.assertEquals(0, car.getUsedProperties().size());

		// Ensure schema is modified
		try {
			resultset = statement.executeQuery("select #usedproperties from #class where #code = 'Car'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals("{}", this.listToString(resultset.getList("usedproperties", Long.class)));

			resultset = statement.executeQuery("select #usedproperties from #class where #code = 'Person'");
			Assert.assertTrue(resultset.next());

			resultset.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testIDLAlterClassExtentAddSimpleProperties() {
		log.debug("MariusQLIDLTest.testIDLAlterClassExtentAddSimpleProperties()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (prop1 int, prop2 int))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE EXTENT OF Vehicule (prop1)");
		Assert.assertEquals(1, res);
		MClass mClass = FactoryCore.createExistingMClass(getSession(), "Vehicule");
		Assert.assertNotNull(mClass);
		Assert.assertEquals(1, mClass.getUsedProperties().size());

		try {
			Statement stmt = this.getSession().createSQLStatement();
			// the instance table should be created
			stmt.executeQuery("select rid from " + mClass.toSQL());

			stmt.close();
		} catch (SQLException e) {
			Assert.fail("The table is not created " + e.getMessage());
		}

		res = statement.executeUpdate("ALTER EXTENT OF Vehicule ADD prop2");
		Assert.assertEquals(1, res);

		// Adding a property already exists in Instance
		try {
			res = statement.executeUpdate("ALTER EXTENT OF Vehicule ADD prop2");
			Assert.fail("Must throw a MariusQL Exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("property prop2 is already used in class Vehicule", e.getMessage());
		}

		mClass = FactoryCore.createExistingMClass(getSession(), "Vehicule");
		MProperty prop2 = (MProperty) mClass.getDefinedDescription("prop2");
		Assert.assertNotNull(prop2);

		Assert.assertNotNull(mClass);
		Assert.assertEquals(2, mClass.getUsedProperties().size());

		try {
			Statement stmt = this.getSession().createSQLStatement();
			// the property should be added to instance
			stmt.executeQuery("select p" + prop2.getInternalId() + " from " + mClass.toSQL());

			stmt.close();
		} catch (SQLException e) {
			Assert.fail("The property is not added " + e.getMessage());
		}

		this.getSession().rollback();

		statement.close();
	}

	@Test
	public void testIDLAlterClassExtentAddReferencedProperties() {
		log.debug("MariusQLIDLTest.testIDLAlterClassExtentAddReferencedProperties()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE #CLASS Car (DESCRIPTOR (#code = 'Car'))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (prop1 REF (Car), prop2 REF (Car)))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE EXTENT OF Vehicule (prop1)");
		Assert.assertEquals(1, res);
		MClass mClass = FactoryCore.createExistingMClass(getSession(), "Vehicule");
		Assert.assertNotNull(mClass);
		Assert.assertEquals(1, mClass.getUsedProperties().size());

		try {
			Statement stmt = this.getSession().createSQLStatement();
			// the instance table should be created
			stmt.executeQuery("select rid from " + mClass.toSQL());

			stmt.close();
		} catch (SQLException e) {
			Assert.fail("The table is not created " + e.getMessage());
		}

		res = statement.executeUpdate("ALTER EXTENT OF Vehicule ADD prop2");
		// Assert.assertEquals(1, res);

		mClass = FactoryCore.createExistingMClass(getSession(), "Vehicule");
		MProperty prop2 = (MProperty) mClass.getDefinedDescription("prop2");
		Assert.assertNotNull(prop2);

		Assert.assertNotNull(mClass);
		Assert.assertEquals(2, mClass.getUsedProperties().size());

		this.getSession().rollback();

		statement.close();
	}

	@Test
	public void testIDLAlterClassExtentDropSimpleProperties() {
		log.debug("MariusQLIDLTest.testIDLAlterClassExtentDropProperties()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (prop1 int, prop2 int))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE EXTENT OF Vehicule (prop1)");
		Assert.assertEquals(1, res);
		MClass mClass = FactoryCore.createExistingMClass(getSession(), "Vehicule");
		Assert.assertNotNull(mClass);
		Assert.assertEquals(1, mClass.getUsedProperties().size());

		try {
			Statement stmt = this.getSession().createSQLStatement();
			// the instance table should be created
			stmt.executeQuery("select rid from " + mClass.toSQL());

			stmt.close();
		} catch (SQLException e) {
			Assert.fail("The table is not created " + e.getMessage());
		}

		res = statement.executeUpdate("ALTER EXTENT OF Vehicule ADD prop2");
		Assert.assertEquals(1, res);

		mClass = FactoryCore.createExistingMClass(getSession(), "Vehicule");
		MProperty prop2 = (MProperty) mClass.getDefinedDescription("prop2");
		Assert.assertNotNull(prop2);

		res = statement.executeUpdate("ALTER EXTENT OF Vehicule DROP prop2");
		Assert.assertEquals(1, res);

		// dropping a property that not exists
		try {
			res = statement.executeUpdate("ALTER EXTENT OF Vehicule DROP prop2");
			Assert.fail("Must throw a MariusQL Exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("property prop2 is not used in class Vehicule", e.getMessage());
		}

		this.getSession().rollback();

		statement.close();
	}

	@Test
	public void testIDLAlterClassExtentDropReferencedProperties() throws SQLException {
		log.debug("MariusQLIDLTest.testIDLAlterClassExtentDropReferencedProperties()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate(
				"CREATE #CLASS Person (DESCRIPTOR (#code = 'Person') PROPERTIES (name string, prenom string))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate(
				"CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (model string, owner ref(Person)))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE EXTENT OF Person (name,prenom)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE EXTENT OF Vehicule (model, owner)");
		Assert.assertEquals(1, res);

		MClass mClass = FactoryCore.createExistingMClass(getSession(), "Vehicule");
		MProperty owner = (MProperty) mClass.getDefinedDescription("owner");
		Assert.assertNotNull(owner);

		res = statement.executeUpdate("ALTER EXTENT OF Vehicule DROP owner");
		Assert.assertEquals(1, res);

		this.getSession().rollback();

		statement.close();
	}

	@Test
	public void testIDLAlterClassExtentDropCollection() throws SQLException {
		log.debug("MariusQLIDLTest.testIDLAlterClassExtentDropCollectionOfSimpleTypes()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate(
				"CREATE #CLASS Car (DESCRIPTOR (#code = 'Car') PROPERTIES (p1 int array, p2 real array, p3 boolean array, p4 string array))");
		Assert.assertEquals(1, res);

		MClass klass = FactoryCore.createExistingMClass(getSession(), "Car");
		Assert.assertNotNull(klass);

		MProperty p1 = (MProperty) klass.getDefinedDescription("p1");
		Assert.assertNotNull(p1);
		MProperty p2 = (MProperty) klass.getDefinedDescription("p2");
		Assert.assertNotNull(p2);
		MProperty p3 = (MProperty) klass.getDefinedDescription("p3");
		Assert.assertNotNull(p3);
		MProperty p4 = (MProperty) klass.getDefinedDescription("p4");
		Assert.assertNotNull(p4);

		res = statement.executeUpdate("CREATE EXTENT OF Car (p1, p2, p3, p4)");
		Assert.assertEquals(1, res);
		Assert.assertEquals(4, klass.getUsedProperties().size());

		res = statement.executeUpdate("ALTER EXTENT OF Car DROP p1");
		Assert.assertEquals(1, res);

		this.getSession().rollback();

		statement.close();
	}
}
