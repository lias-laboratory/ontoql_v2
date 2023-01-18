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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.mariusql.AbstractMariusQLTest;
import fr.ensma.lias.mariusql.DriverDelegateTest.DriverEnum;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.Datatype;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.core.Description;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.model.MClass;
import fr.ensma.lias.mariusql.core.model.MDatatypeCollection;
import fr.ensma.lias.mariusql.core.model.MGenericClass;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotSupportedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSet;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;

/**
 * This test class is a particular Data Definition Language (DDL) test dedicated
 * for Model Definition Language (MDL).
 * 
 * @author Mickael BARON
 * @author Ghada TRIKI
 * @author Valentin CASSAIR
 * @author Florian MHUN
 */
public class MariusQLMDLTest extends AbstractMariusQLTest {

	private Logger log = LoggerFactory.getLogger(MariusQLMDLTest.class);

	@Test
	public void testMDLCreateClassAddPropertiesWithSameName() throws SQLException {
		log.debug("MariusQLMDLTest.testMDLCreateClassAddPropertiesWithSameName()");

		this.getSession().setReferenceLanguage(MariusQLConstants.FRENCH);
		this.getSession().setDefaultNameSpace("http://www.lias.fr/");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement
				.executeUpdate("CREATE #CLASS \"Composant CFCA\" (DESCRIPTOR (#code ='0002-41982799300025#01-1#1')))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate(
				"CREATE #CLASS Accessoires UNDER \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-2#1') PROPERTIES (Poids Real))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate(
				"CREATE #CLASS Connexions UNDER \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-17#1') PROPERTIES (Poids Real))");
		Assert.assertEquals(1, res);

		MariusQLResultSet executeQuery = statement
				.executeQuery("select #rid from #class where #name[fr] = 'Accessoires'");
		Assert.assertTrue(executeQuery.next());
		int accessoiresRid = executeQuery.getInt(1);

		executeQuery = statement.executeQuery("select #rid from #class where #name[fr] = 'Connexions'");
		Assert.assertTrue(executeQuery.next());
		int connexionsRid = executeQuery.getInt(1);

		executeQuery = statement
				.executeQuery("select #rid from #property where #name[fr] = 'Poids' and #scope = " + accessoiresRid);
		Assert.assertTrue(executeQuery.next());
		executeQuery = statement
				.executeQuery("select #rid from #property where #name[fr] = 'Poids' and #scope = " + connexionsRid);
		Assert.assertTrue(executeQuery.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLAlterClassAddPropertiesWithSameName() throws SQLException {
		log.debug("MariusQLMDLTest.testMDLAlterClassAddPropertiesWithSameName()");

		this.getSession().setReferenceLanguage(MariusQLConstants.FRENCH);
		this.getSession().setDefaultNameSpace("http://www.lias.fr/");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement
				.executeUpdate("CREATE #CLASS \"Composant CFCA\" (DESCRIPTOR (#code ='0002-41982799300025#01-1#1')))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate(
				"CREATE #CLASS Accessoires UNDER \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-2#1'))");
		Assert.assertEquals(1, res);
		res = statement.executeUpdate(
				"ALTER #CLASS Accessoires ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-10#1', #definition[fr] = 'Poids d''une unité de composant')");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate(
				"CREATE #CLASS Connexions UNDER \"Composant CFCA\" (DESCRIPTOR (#code = '0002-41982799300025#01-17#1'))");
		Assert.assertEquals(1, res);
		res = statement.executeUpdate(
				"ALTER #CLASS Connexions ADD Poids Real DESCRIPTOR (#code = '0002-41982799300025#02-25#1', #definition[fr] = 'Poids d''une unité de composant')");
		Assert.assertEquals(1, res);

		MariusQLResultSet executeQuery = statement
				.executeQuery("select #rid from #class where #name[fr] = 'Accessoires'");
		Assert.assertTrue(executeQuery.next());
		int accessoiresRid = executeQuery.getInt(1);

		executeQuery = statement.executeQuery("select #rid from #class where #name[fr] = 'Connexions'");
		Assert.assertTrue(executeQuery.next());
		int connexionsRid = executeQuery.getInt(1);

		executeQuery = statement
				.executeQuery("select #rid from #property where #name[fr] = 'Poids' and #scope = " + accessoiresRid);
		Assert.assertTrue(executeQuery.next());
		executeQuery = statement
				.executeQuery("select #rid from #property where #name[fr] = 'Poids' and #scope = " + connexionsRid);
		Assert.assertTrue(executeQuery.next());

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLCreateClassWithVarayingReferenceLanguage() {
		log.debug("MariusQLMDLTest.testMDLCreateClassWithVarayingReferenceLanguage()");

		if (!getSession().getModelCache().isEnabled()) {
			return; // skip
		}

		this.getSession().setReferenceLanguage(MariusQLConstants.FRENCH);
		this.getSession().setDefaultNameSpace("http://www.lias.fr/");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement
				.executeUpdate("CREATE #CLASS Car (DESCRIPTOR (#code='car_code', #name='Car') PROPERTIES (p1 int))");
		Assert.assertEquals(1, res);

		statement.executeUpdate("CREATE EXTENT OF Car(p1)");
		Assert.assertEquals(1, res);
		Assert.assertNotNull(this.getSession().getModelCache().getElement("Car"));
		Assert.assertNull(this.getSession().getModelCache().getElement("car_code"));

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		Assert.assertNull(this.getSession().getModelCache().getElement("Car"));

		statement.executeUpdate("insert into car_code(p1) values(1)");
		Assert.assertNotNull(this.getSession().getModelCache().getElement("car_code"));

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLCreateClassCheckErrors() {
		log.debug("MariusQLMDLTest.testMDLCreateClassCheckErrors()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		this.getSession().setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule'))");
		Assert.assertEquals(1, res);

		// We clean cache model to force for querying DB.
		this.getSession().getModelCache().clean();

		// Cannot create two classes with a same name.
		try {
			statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule'))");
			Assert.fail("Must throw an exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Class already exists.", e.getMessage());
		}

		this.getSession().setDefaultNameSpace("http://www.lias-lab.fr");
		res = statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule'))");
		Assert.assertEquals(1, res);

		// Cannot create two classes with a same name.
		try {
			statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule'))");
			Assert.fail("Must throw an exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Class already exists.", e.getMessage());
		}

		// Cannot create two properties with the same name.
		try {
			statement.executeUpdate(
					"CREATE #CLASS VehiculeBis (DESCRIPTOR (#code = 'VehiculeBis') PROPERTIES (prop1 int, prop1 int))");
			Assert.fail("Must throw an exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Property already exists: prop1", e.getMessage());
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testMDLCreateClassWithInheritanceError() {
		log.debug("MariusQLMDLTest.testMDLCreateStaticClassWithInheritance()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE #STATICCLASS Vehicule (DESCRIPTOR (#code = 'Vehicule'))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE #CLASS VehiculeClass (DESCRIPTOR (#code = 'VehiculeClass'))");
		Assert.assertEquals(1, res);

		try {
			statement.executeUpdate("CREATE #STATICCLASS Car UNDER VehiculeClass (DESCRIPTOR (#code = 'Car'))");
			Assert.fail("Must throw an exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Can not create static class under class", e.getMessage());
		}

		statement.close();
	}

	@Test
	public void testMDLCreateClassWithInheritance() {
		log.debug("MariusQLMDLTest.testMDLCreateClassWithInheritance()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule'))");
		Assert.assertEquals(1, res);
		MGenericClass mClass = FactoryCore.createExistingMGenericClass(getSession(), "Vehicule");
		res = statement.executeUpdate("CREATE #CLASS Car UNDER Vehicule (DESCRIPTOR (#code = 'Car'))");
		Assert.assertEquals(1, res);
		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from m_class where code = 'Vehicule'");
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery("select * from m_class where code = 'Car'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals(mClass.getInternalId().longValue(), resultset.getLong("superclass"));

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLCreateClassFromEntityUnderClassEntity() {
		log.debug("MariusQLMDLTest.testMDLCreateClassFromEntityUnderClassEntity()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		this.getSession().setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule'))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE #CLASS Car UNDER Vehicule (DESCRIPTOR (#code = 'Car'))");
		Assert.assertEquals(1, res);

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLCreateClassWithDefaultNamespace() {
		log.debug("MariusQLMDLTest.testMDLCreateClassWithDefaultNamespace()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule'))");
		Assert.assertEquals(1, res);

		this.getSession().setDefaultNameSpace("http://www.lias-lab.fr");
		res = statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule'))");
		Assert.assertEquals(1, res);

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLCreateClassWithRealProperties() {
		log.debug("MariusQLMDLTest.testMDLCreateClassWithSimpleTypeAttributes()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement
				.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (prop1 real))");
		Assert.assertEquals(1, res);

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from m_class where code = 'Vehicule'");
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery("select * from m_property where code = 'prop1'");
			Assert.assertTrue(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLCreateClassWithURITypeProperties() {
		log.debug("MariusQLMDLTest.testMDLCreateClassWithURITypeProperties()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement
				.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (prop1 uritype))");
		Assert.assertEquals(1, res);

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from m_class where code = 'Vehicule'");
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery("select * from m_property where code = 'prop1'");
			Assert.assertTrue(resultset.next());

			resultset.close();
			stmt.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLCreateClassWithCountTypeProperties() {
		log.debug("MariusQLMDLTest.testMDLCreateClassWithCountTypeProperties()");

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

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLCreateClassWithStringProperties() {
		log.debug("MariusQLMDLTest.testMDLCreateClassWithSimpleTypeAttributes()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement
				.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (prop1 string))");
		Assert.assertEquals(1, res);

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from m_class where code = 'Vehicule'");
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery("select * from m_property where code = 'prop1'");
			Assert.assertTrue(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLCreateClassWithCollectionOfSimpleType() {
		log.debug("MariusQLMDLTest.testMDLCreateClassWithCollectionOfSimpleType()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement
				.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (prop int array))");
		Assert.assertEquals(1, res);

		try {
			MariusQLResultSet resultset = statement
					.executeQuery("select #rid from #datatype where #dtype = 'array' and #collectiontype = 2");
			Assert.assertTrue(resultset.next());
			Long dtypeRid = resultset.getLong("rid");

			resultset = statement
					.executeQuery("select #rid from #property where #code = 'prop' and #range = " + dtypeRid);
			Assert.assertTrue(resultset.next());
			long propRid = resultset.getLong("rid");

			resultset = statement.executeQuery("select #rid, #directproperties from #class where #code = 'Vehicule'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals(MariusQLConstants.OPEN_BRACKET + propRid + MariusQLConstants.CLOSE_BRACKET,
					this.listToString(resultset.getList("directproperties", Long.class)));

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
	public void testMDLCreateClassWithCollectionOfReference() {
		log.debug("MariusQLMDLTest.testMDLCreateClassWithCollectionOfReference()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule'))");
		Assert.assertEquals(1, res);

		MClass klassVehicule = FactoryCore.createExistingMClass(getSession(), "Vehicule");
		Assert.assertNotNull(klassVehicule);

		res = statement
				.executeUpdate("CREATE #CLASS Car (DESCRIPTOR (#code = 'Car') PROPERTIES (p1 ref(Vehicule) array))");
		Assert.assertEquals(1, res);

		MClass klassCar = FactoryCore.createExistingMClass(getSession(), "Car");
		Assert.assertNotNull(klassCar);

		MProperty prop = (MProperty) klassCar.getDefinedDescription("p1");
		Assert.assertNotNull(prop);

		Datatype collection = prop.getRange();
		Assert.assertNotNull(collection);

		Datatype ref = ((MDatatypeCollection) collection).getDatatype();
		Assert.assertNotNull(ref);

		try {
			MariusQLResultSet resultset = statement.executeQuery(
					"select #rid from #datatype where #dtype = 'ref' and #onclass = " + klassVehicule.getInternalId());
			Assert.assertTrue(resultset.next());

			resultset = statement.executeQuery(
					"select #rid from #datatype where #dtype = 'array' and #collectiontype = " + ref.getInternalId());
			Assert.assertTrue(resultset.next());

			resultset = statement.executeQuery(
					"select #rid from #property where #code = 'p1' and #range = " + collection.getInternalId());
			Assert.assertTrue(resultset.next());

			resultset = statement.executeQuery("select #rid, #directproperties from #class where #code = 'Car'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals(MariusQLConstants.OPEN_BRACKET + prop.getInternalId() + MariusQLConstants.CLOSE_BRACKET,
					this.listToString(resultset.getList("directproperties", Long.class)));

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
	public void testMDLCreateClassWithBooleanProperties() {
		log.debug("MariusQLMDLTest.testMDLCreateClassWithSimpleTypeAttributes()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement
				.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (prop1 boolean))");
		Assert.assertEquals(1, res);

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from m_class where code = 'Vehicule'");
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery("select * from m_property where code = 'prop1'");
			Assert.assertTrue(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLCreateClassWithIntProperties() {
		log.debug("MariusQLMDLTest.testMDLCreateClassWithSimpleProperties()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement
				.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') PROPERTIES (prop1 int))");
		Assert.assertEquals(1, res);

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from m_class where code = 'Vehicule'");
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery("select * from m_property where code = 'prop1'");
			Assert.assertTrue(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLCreateClassWithReferenceOnExistingClass() {
		log.debug("MariusQLMDLTest.testMDLCreateClassWithSimpleProperties()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') )");
		MClass mClass = FactoryCore.createExistingMClass(getSession(), "Vehicule");

		res = statement
				.executeUpdate("CREATE #CLASS Car (DESCRIPTOR (#code = 'Car') PROPERTIES (prop1 REF (Vehicule)))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("ALTER #CLASS Car ADD prop2 REF (Vehicule)");
		Assert.assertEquals(1, res);

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from m_class where code = 'Vehicule'");
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery("select * from m_class where code = 'Car'");
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery("select * from m_property where code = 'prop1'");
			Assert.assertTrue(resultset.next());
			Long rang = resultset.getLong("range");

			resultset = stmt.executeQuery("select * from m_property where code = 'prop2'");
			Assert.assertTrue(resultset.next());
			Long rang2 = resultset.getLong("range");

			resultset = stmt.executeQuery("select * from m_datatype where dtype = 'ref' AND onclass = "
					+ mClass.getInternalId() + " AND rid = " + rang.longValue());
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery("select * from m_datatype where dtype = 'ref' AND onclass = "
					+ mClass.getInternalId() + " and rid = " + rang2.longValue());
			Assert.assertTrue(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLCreateClassWithReferenceOnNOTExistingClass() {
		log.debug("MariusQLMMDLTest.testCreateEntityWithReference()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		try {
			statement.executeUpdate("CREATE #CLASS Car (DESCRIPTOR (#code = 'Car') PROPERTIES (prop1 REF (Vehicule)))");
			Assert.fail("Must throw an MMEntityNotFoundException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Class not found: Vehicule", e.getMessage());
		}

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from m_class where code = 'car'");
			Assert.assertFalse(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLDropClassWhenEntityNotExists() {
		log.debug("MariusQLMDLTest.testMDLDropClassWhenEntityNotExists()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		this.getSession().setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		try {
			statement.executeUpdate("DROP #NotExists AClass");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Entity not found: NotExists", e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLDropClassWhenClassNotExists() {
		log.debug("MariusQLMDLTest.testMDLDropClassWhenClassNotExists()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		this.getSession().setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		try {
			statement.executeUpdate("DROP #CLASS NotExistsClass");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Class not found: NotExistsClass", e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLDropStaticClass() {
		log.debug("MariusQLMDLTest.testMDLDropStaticClass()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		this.getSession().setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate("CREATE #STATICCLASS MyStaticClass (DESCRIPTOR (#code = 'MyStaticClass'))");
		Assert.assertEquals(1, res);

		MGenericClass klass = FactoryCore.createExistingMGenericClass(getSession(), "MyStaticClass");
		Assert.assertNotNull(klass);

		res = statement.executeUpdate("DROP #STATICCLASS MyStaticClass");
		Assert.assertEquals(1, res);
		try {
			FactoryCore.createExistingMGenericClass(getSession(), "MyStaticClass");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Class or static class not found: MyStaticClass", e.getMessage());
		}

		try {
			Statement stmt = this.getSession().createSQLStatement();

			ResultSet resultset = stmt.executeQuery("select rid from m_class where code = 'MyStaticClass'");
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery("select rid from m_datatype where onclass = " + klass.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLDropClass() {
		log.debug("MariusQLMDLTest.testMDLDropClass()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		this.getSession().setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement
				.executeUpdate("CREATE #CLASS MyClass (DESCRIPTOR (#code = 'MyClass') PROPERTIES (prop int))");
		Assert.assertEquals(1, res);

		MGenericClass klass = FactoryCore.createExistingMGenericClass(getSession(), "MyClass");
		Assert.assertNotNull(klass);

		res = statement.executeUpdate("DROP #CLASS MyClass");
		Assert.assertEquals(1, res);
		try {
			FactoryCore.createExistingMGenericClass(getSession(), "MyClass");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Class or static class not found: MyClass", e.getMessage());
		}

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select rid from m_class where code = 'MyClass'");
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery("select rid from m_datatype where onclass = " + klass.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery("select rid from m_property where scope = " + klass.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLDropClassWithSimpleDatatype() {
		log.debug("MariusQLMDLTest.testMDLDropClassWithSimpleDatatype()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		this.getSession().setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate(
				"CREATE #CLASS MyClass (DESCRIPTOR (#code = 'MyClass') PROPERTIES (p1 int, p2 real, p3 boolean, p4 string))");
		Assert.assertEquals(1, res);
		MGenericClass klass = FactoryCore.createExistingMGenericClass(getSession(), "MyClass");
		Assert.assertNotNull(klass);

		res = statement.executeUpdate("DROP #CLASS MyClass");
		Assert.assertEquals(1, res);
		try {
			FactoryCore.createExistingMGenericClass(getSession(), "MyClass");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Class or static class not found: MyClass", e.getMessage());
		}

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select rid from m_class where code = 'MyClass'");
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery("select rid from m_property where scope = " + klass.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLDropClassFromEntityUnderStaticClassEntity() {
		log.debug("MariusQLMDLTest.testMDLDropClassFromEntityUnderStaticClassEntity()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		this.getSession().setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate("CREATE ENTITY #MyEntity (#attr int)");
		Assert.assertEquals(1, res);
		res = statement.executeUpdate("CREATE #MyEntity MyEntityClass (DESCRIPTOR (#code = 'MyEntityClass'))");
		Assert.assertEquals(1, res);

		MGenericClass klass = FactoryCore.createExistingMGenericClass(getSession(), "MyEntityClass");
		Assert.assertNotNull(klass);

		res = statement.executeUpdate("DROP #MyEntity MyEntityClass");
		Assert.assertEquals(1, res);
		try {
			FactoryCore.createExistingMGenericClass(getSession(), "MyEntityClass");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Class or static class not found: MyEntityClass", e.getMessage());
		}

		try {
			Statement stmt = this.getSession().createSQLStatement();

			ResultSet resultset = stmt.executeQuery("select rid from m_class where code = 'MyEntityClass'");
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery("select rid from m_datatype where onclass = " + klass.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLDropClassFromEntityUnderClassEntity() {
		log.debug("MariusQLMDLTest.testMDLDropClassFromEntityUnderClassEntity()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		this.getSession().setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate("CREATE ENTITY #MyEntity UNDER #CLASS (#attr int)");
		Assert.assertEquals(1, res);
		res = statement.executeUpdate(
				"CREATE #MyEntity MyEntityClass (DESCRIPTOR (#code = 'MyEntityClass') PROPERTIES (prop int))");
		Assert.assertEquals(1, res);

		MGenericClass klass = FactoryCore.createExistingMGenericClass(getSession(), "MyEntityClass");
		Assert.assertNotNull(klass);

		res = statement.executeUpdate("DROP #MyEntity MyEntityClass");
		Assert.assertEquals(1, res);

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select rid from m_class where code = 'MyEntityClass'");
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery("select rid from m_property where scope = " + klass.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLDropClassWhenChildOfSuperClass() {
		log.debug("MariusQLMDLTest.testMDLDropClassWhenChildOfSuperClass()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		this.getSession().setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement
				.executeUpdate("CREATE #CLASS MyClass (DESCRIPTOR (#code = 'MyClass') PROPERTIES (prop1 int))");
		Assert.assertEquals(1, res);
		MGenericClass superKlass = FactoryCore.createExistingMGenericClass(getSession(), "MyClass");
		Assert.assertNotNull(superKlass);

		res = statement.executeUpdate(
				"CREATE #CLASS MySubClass UNDER MyClass (DESCRIPTOR (#code = 'MySubClass') PROPERTIES (prop2 int))");
		Assert.assertEquals(1, res);
		MGenericClass klass = FactoryCore.createExistingMGenericClass(getSession(), "MySubClass");
		Assert.assertNotNull(klass);

		res = statement.executeUpdate("DROP #CLASS MySubClass");
		Assert.assertEquals(1, res);

		Assert.assertNotNull(FactoryCore.createExistingMGenericClass(getSession(), "MyClass"));
		try {
			FactoryCore.createExistingMGenericClass(getSession(), "MySubClass");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Class or static class not found: MySubClass", e.getMessage());
		}

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select rid from m_class where code = 'MyClass'");
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery("select rid from m_class where code = 'MySubClass'");
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery("select rid from m_property where scope = " + superKlass.getInternalId());
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery("select rid from m_property where scope = " + klass.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLDropClassWhenSuperClass() {
		log.debug("MariusQLMDLTest.testMDLDropClassWhenSuperClass()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		this.getSession().setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement
				.executeUpdate("CREATE #CLASS MyClass (DESCRIPTOR (#code = 'MyClass') PROPERTIES (prop1 int))");
		Assert.assertEquals(1, res);
		MGenericClass superKlass = FactoryCore.createExistingMGenericClass(getSession(), "MyClass");
		Assert.assertNotNull(superKlass);

		res = statement.executeUpdate(
				"CREATE #CLASS MySubClass UNDER MyClass (DESCRIPTOR (#code = 'MySubClass') PROPERTIES (prop2 int))");
		Assert.assertEquals(1, res);
		MGenericClass klass = FactoryCore.createExistingMGenericClass(getSession(), "MySubClass");
		Assert.assertNotNull(klass);

		try {
			statement.executeUpdate("DROP #CLASS MyClass");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Can not remove class which is parent class: MyClass", e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLDropClassUsingReferenceOnOtherClass() {
		log.debug("MariusQLMDLTest.testMDLDropClassUsingReferenceOnOtherClass()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') )");
		MGenericClass refKlass = FactoryCore.createExistingMGenericClass(getSession(), "Vehicule");
		Assert.assertNotNull(refKlass);

		res = statement
				.executeUpdate("CREATE #CLASS Car (DESCRIPTOR (#code = 'Car') PROPERTIES (prop REF (Vehicule)))");
		Assert.assertEquals(1, res);
		MGenericClass klass = FactoryCore.createExistingMGenericClass(getSession(), "Car");
		Assert.assertNotNull(klass);

		Description refProperty = klass.getDefinedDescription("prop");
		Assert.assertNotNull(refProperty);

		res = statement.executeUpdate("DROP #CLASS Car");
		Assert.assertEquals(1, res);
		try {
			FactoryCore.createExistingMGenericClass(getSession(), "Car");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Class or static class not found: Car", e.getMessage());
		}

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from m_class where code = 'Car'");
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery("select * from m_property where code = 'prop'");
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery(
					"select * from m_datatype where dtype = 'ref' and onclass = " + refKlass.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLDropClassReferencedByPropertiesInOtherClass() {
		log.debug("MariusQLMDLTest.testMDLDropClassReferencedByPropertiesInOtherClass()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') )");
		MGenericClass refKlass = FactoryCore.createExistingMGenericClass(getSession(), "Vehicule");
		Assert.assertNotNull(refKlass);

		res = statement
				.executeUpdate("CREATE #CLASS Car (DESCRIPTOR (#code = 'Car') PROPERTIES (prop REF (Vehicule)))");
		Assert.assertEquals(1, res);
		MGenericClass klass = FactoryCore.createExistingMGenericClass(getSession(), "Car");
		Assert.assertNotNull(klass);

		Description refProperty = klass.getDefinedDescription("prop");
		Assert.assertNotNull(refProperty);

		try {
			statement.executeUpdate("DROP #CLASS Vehicule");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Class is referenced by an existing property", e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLDropClassReferencedByCollectionOfReference() {
		log.debug("MariusQLMDLTest.testMDLDropClassReferencedByCollectionOfReference()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') )");
		MGenericClass refKlass = FactoryCore.createExistingMGenericClass(getSession(), "Vehicule");
		Assert.assertNotNull(refKlass);

		res = statement
				.executeUpdate("CREATE #CLASS Car (DESCRIPTOR (#code = 'Car') PROPERTIES (prop REF (Vehicule) array))");
		Assert.assertEquals(1, res);
		MGenericClass klass = FactoryCore.createExistingMGenericClass(getSession(), "Car");
		Assert.assertNotNull(klass);

		Description refProperty = klass.getDefinedDescription("prop");
		Assert.assertNotNull(refProperty);

		try {
			statement.executeUpdate("DROP #CLASS Vehicule");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Class is referenced by an existing property", e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLDropClassWithCollectionOfReference() {
		log.debug("MariusQLMDLTest.testMDLDropClassWithCollectionOfReference()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') )");
		Assert.assertEquals(1, res);

		MClass klassVehicule = FactoryCore.createExistingMClass(getSession(), "Vehicule");
		Assert.assertNotNull(klassVehicule);

		res = statement
				.executeUpdate("CREATE #CLASS Car (DESCRIPTOR (#code = 'Car') PROPERTIES (p1 ref(Vehicule) array))");
		Assert.assertEquals(1, res);

		MClass klassCar = FactoryCore.createExistingMClass(getSession(), "Car");
		Assert.assertNotNull(klassCar);

		MProperty prop = (MProperty) klassCar.getDefinedDescription("p1");
		Assert.assertNotNull(prop);

		Datatype collection = prop.getRange();
		Assert.assertNotNull(collection);

		Datatype ref = ((MDatatypeCollection) collection).getDatatype();
		Assert.assertNotNull(ref);

		res = statement.executeUpdate("DROP #CLASS Car");
		Assert.assertEquals(1, res);

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery(
					"select * from m_datatype where dtype = 'ref' and onclass = " + klassVehicule.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery(
					"select * from m_datatype where dtype = 'array' and collectiontype = " + ref.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery(
					"select * from m_property where code = 'p1' and range = " + collection.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery("select * from m_class where code = 'Car'");
			Assert.assertFalse(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLDropClassWithCollectionOfSimpleType() {
		log.debug("MariusQLMDLTest.testMDLDropClassWithCollectionOfSimpleType()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE #CLASS Car (DESCRIPTOR (#code = 'Car') PROPERTIES (p1 int array))");
		Assert.assertEquals(1, res);

		MClass klassCar = FactoryCore.createExistingMClass(getSession(), "Car");
		Assert.assertNotNull(klassCar);

		MProperty prop = (MProperty) klassCar.getDefinedDescription("p1");
		Assert.assertNotNull(prop);

		Datatype collection = prop.getRange();
		Assert.assertNotNull(collection);

		Datatype intDatatype = FactoryCore.createMSimpleDatatype(getSession(), DatatypeEnum.DATATYPEINT);
		Assert.assertNotNull(intDatatype);

		res = statement.executeUpdate("DROP #CLASS Car");
		Assert.assertEquals(1, res);

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from m_datatype where dtype = 'int'");
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery("select * from m_datatype where dtype = 'array' and collectiontype = "
					+ intDatatype.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery(
					"select * from m_property where code = 'p1' and range = " + collection.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery("select * from m_class where code = 'Car'");
			Assert.assertFalse(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLAlterClassAddProperties() {
		log.debug("MariusQLMDLTest.testMDLAlterClassAddProperties()");

		this.getSession().setReferenceLanguage(MariusQLConstants.FRENCH);
		this.getSession().setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate("CREATE #CLASS \"Vehicule Class\" (DESCRIPTOR (#code='Vehicule-Class'))");
		Assert.assertEquals(1, res);
		MClass mClass = FactoryCore.createExistingMClass(getSession(), "\"Vehicule Class\"");

		res = statement
				.executeUpdate("ALTER #CLASS \"Vehicule Class\" ADD \"my property\" int DESCRIPTOR(#code='prop-1')");
		Assert.assertEquals(1, res);

		try {
			res = statement.executeUpdate(
					"ALTER #CLASS \"Vehicule Class\" ADD \"my property\" int DESCRIPTOR(#code='prop-1')");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("property already exists", e.getMessage());
		}

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from m_property where code = 'prop-1'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals(mClass.getInternalId().longValue(), resultset.getLong("scope"));

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLAlterClassAddPropertiesWithReference() {
		log.debug("MariusQLMDLTest.testMDLAlterClassAddPropertiesWithReference()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		this.getSession().setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate("CREATE #CLASS VehiculeClass (DESCRIPTOR (#code = 'VehiculeClass'))");
		Assert.assertEquals(1, res);
		MClass mClass = FactoryCore.createExistingMClass(getSession(), "VehiculeClass");

		statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule'))");
		MClass mClass2 = FactoryCore.createExistingMClass(getSession(), "Vehicule");

		statement.executeUpdate("ALTER #CLASS VehiculeClass ADD prop1 REF (Vehicule)");
		Assert.assertEquals(1, res);

		statement.executeUpdate("ALTER #CLASS VehiculeClass ADD prop2 REF (Vehicule)");
		Assert.assertEquals(1, res);

		try {
			List<Long> propertyIds = new ArrayList<Long>();
			MariusQLResultSet resultset = statement.executeQuery(
					"select #rid from #datatype where #dtype = 'ref' and #onclass = " + mClass2.getInternalId());

			Assert.assertTrue(resultset.next());
			Long rid = resultset.getLong(1);

			resultset = statement.executeQuery("select #scope, #range, #rid from #property where #code = 'prop1'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals(mClass.getInternalId().longValue(), resultset.getLong("scope"));
			Assert.assertEquals(rid.longValue(), resultset.getLong("range"));
			propertyIds.add(resultset.getLong("rid"));

			resultset = statement.executeQuery("select #scope, #rid from #property where #code = 'prop2'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals(mClass.getInternalId().longValue(), resultset.getLong("scope"));
			propertyIds.add(resultset.getLong("rid"));

			resultset = statement
					.executeQuery("select #directproperties from #class where #rid = " + mClass.getInternalId());
			Assert.assertTrue(resultset.next());

			Assert.assertEquals(this.listToString(propertyIds), this.listToString(resultset.getList(1, Long.class)));
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
	public void testMDLAlterClassAddPropertiesWithSimpleCollection() {
		log.debug("MariusQLMDLTest.testMDLAlterClassAddPropertiesWithSimpleCollection()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		this.getSession().setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule'))");
		Assert.assertEquals(1, res);

		statement.executeUpdate("ALTER #CLASS Vehicule ADD prop int array");
		Assert.assertEquals(1, res);

		try {
			MariusQLResultSet resultset = statement
					.executeQuery("select #rid from #datatype where #dtype = 'array' and #collectiontype = 2");
			Assert.assertTrue(resultset.next());
			Long dtypeRid = resultset.getLong("rid");

			resultset = statement
					.executeQuery("select #rid from #Property where #code = 'prop' and #range = " + dtypeRid);
			Assert.assertTrue(resultset.next());
			long propRid = resultset.getLong("rid");

			resultset = statement.executeQuery("select #directproperties from #class where #code = 'Vehicule'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals(MariusQLConstants.OPEN_BRACKET + propRid + MariusQLConstants.CLOSE_BRACKET,
					this.listToString(resultset.getList(1, Long.class)));

			resultset.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		} finally {
			this.getSession().rollback();
			statement.close();
		}
	}

	@Test
	public void testMDLAlterClassAddPropertyWithCollectionOfReference() {
		log.debug("MariusQLMMDLTest.testMDLAlterClassAddPropertyWithCollectionOfReference()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		this.getSession().setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule'))");
		Assert.assertEquals(1, res);
		MClass mClass = FactoryCore.createExistingMClass(getSession(), "Vehicule");

		statement.executeUpdate("CREATE #CLASS Car (DESCRIPTOR (#code = 'Car') )");

		statement.executeUpdate("ALTER #CLASS Car ADD prop1 REF (Vehicule) array");
		Assert.assertEquals(1, res);

		MClass klassCar = FactoryCore.createExistingMClass(getSession(), "Car");
		Assert.assertNotNull(klassCar);

		MProperty prop = (MProperty) klassCar.getDefinedDescription("prop1");
		Assert.assertNotNull(prop);

		Datatype collection = prop.getRange();
		Assert.assertNotNull(collection);

		Datatype ref = ((MDatatypeCollection) collection).getDatatype();
		Assert.assertNotNull(ref);

		try {
			MariusQLResultSet resultset = statement.executeQuery(
					"select #rid from #DataType where #dtype = 'ref' and #onclass = " + mClass.getInternalId());
			Assert.assertTrue(resultset.next());

			resultset = statement.executeQuery(
					"select #rid from #DataType where #dtype = 'array' and #collectiontype = " + ref.getInternalId());
			Assert.assertTrue(resultset.next());

			resultset = statement.executeQuery(
					"select #rid from #Property where #code = 'prop1' and #range = " + collection.getInternalId());
			Assert.assertTrue(resultset.next());

			resultset = statement.executeQuery("select #rid, #directproperties from #class where #code = 'Car'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals(MariusQLConstants.OPEN_BRACKET + prop.getInternalId() + MariusQLConstants.CLOSE_BRACKET,
					this.listToString(resultset.getList("directproperties", Long.class)));

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
	public void testMDLAlterClassDropProperties() {
		log.debug("MariusQLMDLTest.testMDLAlterClassDropProperties()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		this.getSession().setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate("CREATE #CLASS VehiculeClass (DESCRIPTOR (#code = 'VehiculeClass'))");
		Assert.assertEquals(1, res);
		MClass mClass = FactoryCore.createExistingMClass(getSession(), "VehiculeClass");
		Assert.assertNotNull(mClass);

		statement.executeUpdate("ALTER #CLASS VehiculeClass ADD prop1 int");
		Assert.assertEquals(1, res);

		try {
			MariusQLResultSet resultset = statement.executeQuery("select #rid from #property where #code = 'prop1'");
			Assert.assertTrue(resultset.next());
			resultset.close();

			statement.executeUpdate("ALTER #CLASS VehiculeClass DROP prop1 int");

			resultset = statement.executeQuery("select #rid from #property where #code = 'prop1'");
			Assert.assertFalse(resultset.next());
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
	public void testMDLAlterClassDropInexistantProperties() {
		log.debug("MariusQLMDLTest.testMDLAlterClassDropInexistantProperties()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		this.getSession().setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate("CREATE #CLASS VehiculeClass (DESCRIPTOR (#code = 'VehiculeClass'))");
		Assert.assertEquals(1, res);
		MClass mClass = FactoryCore.createExistingMClass(getSession(), "VehiculeClass");
		Assert.assertNotNull(mClass);

		statement.executeUpdate("ALTER #CLASS VehiculeClass ADD prop1 int");
		Assert.assertEquals(1, res);

		try {
			statement.executeUpdate("ALTER #CLASS VehiculeClass DROP prop2 int");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("prop2 is not a defined property.", e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLAlterClassDropPropertiesWithReference() {
		log.debug("MariusQLMDLTest.testMDLAlterClassDropPropertiesWithReference()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		this.getSession().setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule') )");
		MClass mClass2 = FactoryCore.createExistingMClass(getSession(), "Vehicule");

		int res = statement.executeUpdate("CREATE #CLASS VehiculeClass (DESCRIPTOR (#code = 'VehiculeClass'))");
		Assert.assertEquals(1, res);

		statement.executeUpdate("ALTER #CLASS VehiculeClass ADD prop1 REF (Vehicule)");
		Assert.assertEquals(1, res);

		MClass mClass = FactoryCore.createExistingMClass(getSession(), "VehiculeClass");
		Assert.assertNotNull(mClass);

		Description refProperty = mClass.getDefinedDescription("prop1");
		Assert.assertNotNull(refProperty);
		Long range = refProperty.getRange().getInternalId();

		ResultSet resultset;

		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from m_property where code = 'prop1'");
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery("select rid from m_datatype where dtype = 'ref' and onclass = "
					+ mClass2.getInternalId() + " and rid = " + range);
			Assert.assertTrue(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		statement.executeUpdate("ALTER #CLASS VehiculeClass DROP prop1 REF (Vehicule)");
		Assert.assertEquals(1, res);

		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from m_property where code = 'prop1'");
			Assert.assertFalse(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLAlterClassDropPropertiesWithSimpleCollection() {
		log.debug("MariusQLMDLTest.testMDLAlterClassDropPropertiesWithSimpleCollection()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		this.getSession().setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule'))");
		Assert.assertEquals(1, res);

		statement.executeUpdate("ALTER #CLASS Vehicule ADD prop int array");
		Assert.assertEquals(1, res);

		try {
			MariusQLResultSet resultset = statement
					.executeQuery("select #rid from #datatype where #dtype = 'array' and #collectiontype = 2");
			Assert.assertTrue(resultset.next());
			Long dtypeRid = resultset.getLong("rid");

			resultset = statement
					.executeQuery("select #rid from #property where #code = 'prop' and #range = " + dtypeRid);
			Assert.assertTrue(resultset.next());
			long propRid = resultset.getLong("rid");

			resultset = statement.executeQuery("select #rid, #directproperties from #class where #code = 'Vehicule'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals(MariusQLConstants.OPEN_BRACKET + propRid + MariusQLConstants.CLOSE_BRACKET,
					this.listToString(resultset.getList("directproperties", Long.class)));

			statement.executeUpdate("ALTER #CLASS Vehicule DROP prop int array");

			resultset = statement
					.executeQuery("select #rid from #datatype where #dtype = 'array' and #collectiontype = 2");
			Assert.assertFalse(resultset.next());

			resultset = statement.executeQuery("select #rid from #property where #code = 'prop'");
			Assert.assertFalse(resultset.next());

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
	public void testMDLAlterClassDropPropertyWithCollectionOfReference() {
		log.debug("MariusQLMDLTest.testMDLAlterClassDropPropertyWithCollectionOfReference()");

		// ADD
		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		this.getSession().setDefaultNameSpace(MariusQLConstants.NO_NAMESPACE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate("CREATE #CLASS Vehicule (DESCRIPTOR (#code = 'Vehicule'))");
		Assert.assertEquals(1, res);
		MClass mClass = FactoryCore.createExistingMClass(getSession(), "Vehicule");

		statement.executeUpdate("CREATE #CLASS Car (DESCRIPTOR (#code = 'Car') )");

		statement.executeUpdate("ALTER #CLASS Car ADD prop1 REF (Vehicule) array");
		Assert.assertEquals(1, res);

		MClass klassCar = FactoryCore.createExistingMClass(getSession(), "Car");
		Assert.assertNotNull(klassCar);

		MProperty prop = (MProperty) klassCar.getDefinedDescription("prop1");
		Assert.assertNotNull(prop);

		Datatype collection = prop.getRange();
		Assert.assertNotNull(collection);

		Datatype ref = ((MDatatypeCollection) collection).getDatatype();
		Assert.assertNotNull(ref);

		try {
			MariusQLResultSet resultset = statement.executeQuery(
					"select #rid from #datatype where #dtype = 'ref' and #onclass = " + mClass.getInternalId());
			Assert.assertTrue(resultset.next());

			resultset = statement.executeQuery(
					"select #rid from #datatype where #dtype = 'array' and #collectiontype = " + ref.getInternalId());
			Assert.assertTrue(resultset.next());

			resultset = statement.executeQuery(
					"select #rid from #property where #code = 'prop1' and #range = " + collection.getInternalId());
			Assert.assertTrue(resultset.next());

			resultset = statement.executeQuery("select #rid, #directproperties from #class where #code = 'Car'");
			Assert.assertTrue(resultset.next());

			Assert.assertEquals(MariusQLConstants.OPEN_BRACKET + prop.getInternalId() + MariusQLConstants.CLOSE_BRACKET,
					this.listToString(resultset.getList("directproperties", Long.class)));

			resultset.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		// DROP
		statement.executeUpdate("ALTER #CLASS Car DROP prop1 REF (Vehicule) array");
		Assert.assertEquals(1, res);

		try {
			MariusQLResultSet resultset = statement.executeQuery(
					"select #rid from #datatype where #dtype = 'ref' and #onclass = " + mClass.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset = statement.executeQuery(
					"select #rid from #datatype where #dtype = 'array' and #collectiontype = " + ref.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset = statement.executeQuery(
					"select #rid from #property where #code = 'p1' and #range = " + collection.getInternalId());
			Assert.assertFalse(resultset.next());

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
	public void testMDLCreateWithMultilingualAttribute() {
		log.debug("MariusQLMQLTest.testMDLCreateWithMultilingualAttribute()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate(
				"CREATE #CLASS VehiculeClass (DESCRIPTOR (#code = 'VehiculeClass', #name[en] = 'Car', #name[fr] = 'Vehicule'))");

		Assert.assertEquals(1, res);
		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLCreateWithReferenceLanguage() {
		log.debug("MariusQLMQLTest.testMDLCreateWithReferenceLanguage()");

		this.getSession().setReferenceLanguage(MariusQLConstants.FRENCH);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement
				.executeUpdate("CREATE #CLASS Voiture (DESCRIPTOR (#code = '123456', #name[en] = 'Vehicule'))");
		Assert.assertEquals(1, res);

		final MariusQLResultSet executeQuery = statement.executeQuery("SELECT #CODE, #NAME[EN], #NAME[FR] FROM #CLASS");
		try {
			Assert.assertTrue(executeQuery.next());
			Assert.assertEquals("123456", executeQuery.getString(1));
			Assert.assertEquals("Vehicule", executeQuery.getString(2));
			Assert.assertEquals("Voiture", executeQuery.getString(3));
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testMDLCreateClassWithEnumProperties() {
		log.debug("MariusQLMQLTest.testMDLCreateClassWithEnumProperties()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int result = statement.executeUpdate("CREATE #CLASS CFCA (PROPERTIES (color ENUM ('NOIR','ROUGE','BLANC')))");
		Assert.assertEquals(1, result);

		try {
			MariusQLResultSet resultSet = statement
					.executeQuery("select p.#rid, p.#range from #Property p where p.#code = 'color'");
			Assert.assertTrue(resultSet.next());
			Assert.assertTrue(resultSet.getLong(1) != 0);
			Assert.assertTrue(resultSet.getLong(2) != 0);

			resultSet = statement
					.executeQuery("select d.#enumvalues from #Datatype d where d.#rid = " + resultSet.getLong(2));
			Assert.assertTrue(resultSet.next());
			Assert.assertEquals("{NOIR,ROUGE,BLANC}", this.listToString(resultSet.getList(1, String.class)));
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		statement.executeUpdate("ALTER #CLASS CFCA ADD Genre ENUM ('mâle', 'femelle', 'hermaphrodite')");

		try {
			MariusQLResultSet resultSet = statement
					.executeQuery("select p.#rid, p.#range from #Property p where p.#code = 'Genre'");
			Assert.assertTrue(resultSet.next());
			Assert.assertTrue(resultSet.getLong(1) != 0);
			Assert.assertTrue(resultSet.getLong(2) != 0);

			resultSet = statement
					.executeQuery("select d.#enumvalues from #Datatype d where d.#rid = " + resultSet.getLong(2));
			Assert.assertTrue(resultSet.next());
			Assert.assertEquals("{mâle,femelle,hermaphrodite}", this.listToString(resultSet.getList(1, String.class)));
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		result = statement.executeUpdate("CREATE EXTENT OF CFCA (Genre, color)");
		Assert.assertEquals(1, result);

		result = statement.executeUpdate("INSERT INTO CFCA (Genre, color) VALUES ('mâle', 'NOIR')");
		Assert.assertEquals(1, result);

		try {
			ResultSet resultSet = statement.executeQuery("select Genre, color from CFCA");
			Assert.assertTrue(resultSet.next());

			Assert.assertEquals("mâle", resultSet.getString(1));
			Assert.assertEquals("NOIR", resultSet.getString(2));
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}
}
