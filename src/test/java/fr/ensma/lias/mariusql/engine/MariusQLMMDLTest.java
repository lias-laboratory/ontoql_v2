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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.mariusql.AbstractMariusQLTest;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.Category;
import fr.ensma.lias.mariusql.core.Datatype;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.core.Description;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.metamodel.MMAttribute;
import fr.ensma.lias.mariusql.core.metamodel.MMDatatypeBoolean;
import fr.ensma.lias.mariusql.core.metamodel.MMDatatypeCollection;
import fr.ensma.lias.mariusql.core.metamodel.MMDatatypeInt;
import fr.ensma.lias.mariusql.core.metamodel.MMDatatypeReal;
import fr.ensma.lias.mariusql.core.metamodel.MMDatatypeReference;
import fr.ensma.lias.mariusql.core.metamodel.MMDatatypeString;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSet;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;

/**
 * This test class is a particular Data Definition Language (DDL) test dedicated
 * for Meta Model Definition Language (MMDL).
 * 
 * @author Mickael BARON
 * @author Ghada TRIKI
 * @author Florian MHUN
 */
public class MariusQLMMDLTest extends AbstractMariusQLTest {

	private Logger log = LoggerFactory.getLogger(MariusQLMMDLTest.class);

	@Test
	public void jdbcTest() {
		try {
			Connection conn = this.getSession().getConnection();
			conn.setAutoCommit(false);

			Statement stmt = conn.createStatement();
			stmt.executeUpdate(
					"insert into mm_entity (rid,name,mappedtablename,iscore,superentity) values (50,'entityint','m_class',0,27)");
			stmt.executeUpdate("insert into mm_attribute (name,scope,range) values ('security',50,2)");

			stmt.close();
			conn.rollback();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateEntityCheckErrors() throws SQLException {
		log.debug("MariusQLMMDLTest.testMMDLCreateEntityCheckErrors()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE ENTITY #entityint (#security int)");
		Assert.assertEquals(1, res);

		MMEntity entity = FactoryCore.createExistingMMEntity(this.getSession(), "entityint");
		Assert.assertNotNull(entity);
		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testCreateEntityWithReferenceOnCoreEntity() throws SQLException {
		log.debug("MariusQLMMDLTest.testCreateEntityWithReferenceOnCoreEntity()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE entity #ContextURI (#nameCtx STRING, #classificationCtx STRING)");
		Assert.assertEquals(1, res);
		res = statement.executeUpdate("CREATE Entity #Context (#idContext INT, #URI REF(#ContextURI))");
		Assert.assertEquals(1, res);
		res = statement.executeUpdate(
				"CREATE Entity #UnitOfMeasurementContext UNDER #Context (#defaultUnitValue STRING,#targetUnitValue STRING,#conversionRate INT)");
		Assert.assertEquals(1, res);
		res = statement.executeUpdate("CREATE ENTITY #ContextLink (#prop REF (#property), #ctx REF(#ContextURI))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE #CLASS MedicalProcedure (PROPERTIES(nameProcedure STRING, price INT))");
		Assert.assertEquals(1, res);
		res = statement.executeUpdate("CREATE EXTENT OF MedicalProcedure (nameProcedure, price)");
		Assert.assertEquals(1, res);
		res = statement.executeUpdate("INSERT INTO MedicalProcedure (nameProcedure, price) VALUES ('proc1', 88)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate(
				"INSERT INTO #ContextURI (#code, #nameCtx, #classificationCtx) values ('URI_1', 'Price_default_unit', 'Unit_Context')");
		Assert.assertEquals(1, res);

		MariusQLResultSet executeQuery = statement.executeQuery("select #rid from #property where #code='price'");
		Assert.assertTrue(executeQuery.next());
		String priceRid = executeQuery.getString(1);

		executeQuery = statement.executeQuery("select #rid from #ContextURI where #code='URI_1'");
		Assert.assertTrue(executeQuery.next());
		String uri1 = executeQuery.getString(1);

		res = statement.executeUpdate(
				"INSERT INTO #ContextLink (#code, #prop, #ctx) values ('link_1'," + priceRid + "," + uri1 + ")");
		Assert.assertEquals(1, res);

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testCreateEntityWithIntegerAttribute() {
		log.debug("MariusQLMMDLTest.testMMDLCreateEntityWithIntegerAttribute()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE ENTITY #entityint (#attribute int)");
		Assert.assertEquals(1, res);

		MMEntity entity = FactoryCore.createExistingMMEntity(this.getSession(), "entityint");
		MMAttribute attribute = entity.getDefinedMMAttribute("attribute");
		Assert.assertTrue(attribute.getName().equals("attribute"));
		Assert.assertTrue(attribute.getRange() instanceof MMDatatypeInt);

		// Test in database
		try {
			Statement stmt = this.getSession().createSQLStatement();

			ResultSet resultset = stmt.executeQuery("SELECT * FROM mm_entity WHERE name = 'entityint'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals(27, resultset.getLong("superentity"));

			resultset = stmt.executeQuery("SELECT * FROM mm_attribute WHERE name = 'attribute'");
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
	public void testCreateEntityWithStringAttribute() {
		log.debug("MariusQLMMDLTest.testMMDLCreateEntityWithStringAttribute()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE ENTITY #entitystring (#attribute string)");
		Assert.assertEquals(1, res);

		MMEntity entity = FactoryCore.createExistingMMEntity(this.getSession(), "entitystring");
		Assert.assertNotNull(entity);

		MMAttribute attribute = entity.getDefinedMMAttribute("attribute");
		Assert.assertTrue(attribute.getName().equals("attribute"));
		Assert.assertTrue(attribute.getRange() instanceof MMDatatypeString);

		// Test in database
		try {
			Statement stmt = this.getSession().createSQLStatement();

			ResultSet resultset = stmt.executeQuery("SELECT * FROM mm_entity WHERE name = 'entitystring'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals(27, resultset.getLong("superentity"));

			resultset = stmt.executeQuery("SELECT * FROM mm_attribute WHERE name = 'attribute'");
			Assert.assertTrue(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().commit();// .rollback();
		statement.close();
	}

	@Test
	public void testCreateEntityWithBooleanAttribute() {
		log.debug("MariusQLMMDLTest.testMMDLCreateEntityWithBooleanAttribute()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE ENTITY #entityboolean (#attribute boolean)");
		Assert.assertEquals(1, res);

		MMEntity entity = FactoryCore.createExistingMMEntity(this.getSession(), "entityboolean");
		MMAttribute attribute = entity.getDefinedMMAttribute("attribute");
		Assert.assertTrue(attribute.getName().equals("attribute"));
		Assert.assertTrue(attribute.getRange() instanceof MMDatatypeBoolean);

		// Test in database
		try {
			Statement stmt = this.getSession().createSQLStatement();

			ResultSet resultset = stmt.executeQuery("SELECT * FROM mm_entity WHERE name = 'entityboolean'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals(27, resultset.getLong("superentity"));

			resultset = stmt.executeQuery("SELECT * FROM mm_attribute WHERE name = 'attribute'");
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
	public void testCreateEntityWithRealAttribute() {
		log.debug("MariusQLMMDLTest.testMMDLCreateEntityWithRealAttribute()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE ENTITY #entityreal (#attribute real)");
		Assert.assertEquals(1, res);
		res = statement.executeUpdate("CREATE ENTITY #entityreal2 (#attribute real)");
		Assert.assertEquals(1, res);

		MMEntity entity = FactoryCore.createExistingMMEntity(this.getSession(), "entityreal");
		MMAttribute attribute = entity.getDefinedMMAttribute("attribute");
		Assert.assertTrue(attribute.getName().equals("attribute"));
		Assert.assertTrue(attribute.getRange() instanceof MMDatatypeReal);

		// Test in database
		try {
			Statement stmt = this.getSession().createSQLStatement();

			ResultSet resultset = stmt.executeQuery("SELECT superentity FROM mm_entity WHERE name = 'entityreal'");
			Assert.assertTrue(resultset.next());
			Assert.assertEquals(27, resultset.getLong("superentity"));

			resultset = stmt.executeQuery("SELECT rid FROM mm_attribute WHERE name = 'attribute'");
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
	public void testCreateWithCollectionOfSimpleTypes() {
		log.debug("OntoQLODLTest.testCreateWithCollectionOfSimpleTypes()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		List<String> attributeNames = Arrays.asList("a1", "a2", "a3", "a4");
		int res = statement.executeUpdate(
				"CREATE ENTITY #entity (#a1 int array, #a2 real array, #a3 boolean array, #a4 string array)");
		Assert.assertEquals(1, res);

		MMEntity entity = FactoryCore.createExistingMMEntity(this.getSession(), "entity");
		Assert.assertNotNull(entity);

		List<Datatype> simpleDatatypes = new ArrayList<Datatype>();
		simpleDatatypes.add(FactoryCore.createMMSimpleDatatype(getSession(), DatatypeEnum.DATATYPEINT));
		simpleDatatypes.add(FactoryCore.createMMSimpleDatatype(getSession(), DatatypeEnum.DATATYPEREAL));
		simpleDatatypes.add(FactoryCore.createMMSimpleDatatype(getSession(), DatatypeEnum.DATATYPEBOOLEAN));
		simpleDatatypes.add(FactoryCore.createMMSimpleDatatype(getSession(), DatatypeEnum.DATATYPESTRING));

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			for (int i = 0; i < simpleDatatypes.size(); i++) {
				Datatype datatype = simpleDatatypes.get(i);
				Description attribute = entity.getDefinedDescription(attributeNames.get(i));

				resultset = stmt.executeQuery("SELECT * FROM mm_datatype WHERE dtype = 'array' AND collectiontype = "
						+ datatype.getInternalId());
				Assert.assertTrue(resultset.next());

				resultset = stmt.executeQuery(
						"SELECT * FROM mm_attribute WHERE rid = " + attribute.getInternalId() + " AND range = "
								+ attribute.getRange().getInternalId() + " AND scope = " + entity.getInternalId());
				Assert.assertTrue(resultset.next());

				resultset.close();
			}

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testCreateWithCollectionOfReference() {
		log.debug("OntoQLODLTest.testCreateWithCollectionOfReference()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE ENTITY #refentity (#refattribute int)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE ENTITY #entity (#attribute ref(#refentity) array)");
		Assert.assertEquals(1, res);

		MMEntity entity = FactoryCore.createExistingMMEntity(this.getSession(), "entity");
		Assert.assertNotNull(entity);

		Description attribute = entity.getDefinedDescription("attribute");
		MMDatatypeCollection collection = (MMDatatypeCollection) attribute.getRange();
		Assert.assertNotNull(collection);

		MMDatatypeReference reference = (MMDatatypeReference) collection.getDatatype();
		Assert.assertNotNull(reference);

		Category refEntity = reference.getCategory();
		Assert.assertNotNull(refEntity);
		Assert.assertEquals("refentity", refEntity.getName());

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery(
					"SELECT * FROM mm_datatype WHERE dtype = 'ref' AND onclass = " + refEntity.getInternalId());
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery("SELECT * FROM mm_datatype WHERE dtype = 'array' AND collectiontype = "
					+ reference.getInternalId());
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery("SELECT * FROM mm_attribute WHERE rid = " + attribute.getInternalId()
					+ " AND range = " + collection.getInternalId() + " AND scope = " + entity.getInternalId());
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
	public void testCreateEntityWithInheritance() {
		log.debug("MariusQLMMDLTest.testMMDLCreateEntityWithInheritance()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		ResultSet resultset;
		int firstentitystaticclassRid = -1, firstentityclassRid = -1;

		int res = statement.executeUpdate(
				"CREATE ENTITY #firstentitystaticclass UNDER #staticclass (#firstattributestaticclass int)");
		Assert.assertEquals(1, res);

		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery(
					"SELECT rid FROM mm_entity WHERE name = 'firstentitystaticclass' and superentity = 27");
			Assert.assertTrue(resultset.next());
			firstentitystaticclassRid = resultset.getInt(1);

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		res = statement.executeUpdate(
				"CREATE ENTITY #secondentitystaticclass UNDER #firstentitystaticclass (#secondattributestaticclass int)");
		Assert.assertEquals(1, res);
		MMEntity entity = FactoryCore.createExistingMMEntity(this.getSession(), "secondentitystaticclass");
		Assert.assertNotNull(entity);

		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt
					.executeQuery("SELECT rid FROM mm_entity WHERE name = 'secondentitystaticclass' and superentity = "
							+ firstentitystaticclassRid);
			Assert.assertTrue(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		res = statement.executeUpdate("CREATE ENTITY #firstentityclass UNDER #class (#firstattributeclass int)");
		Assert.assertEquals(1, res);
		entity = FactoryCore.createExistingMMEntity(this.getSession(), "firstentityclass");
		Assert.assertNotNull(entity);

		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt
					.executeQuery("SELECT rid FROM mm_entity WHERE name = 'firstentityclass' and superentity = 9");
			Assert.assertTrue(resultset.next());
			firstentityclassRid = resultset.getInt(1);

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		res = statement
				.executeUpdate("CREATE ENTITY #secondentityclass UNDER #firstentityclass (#secondattributeclass int)");
		Assert.assertEquals(1, res);
		entity = FactoryCore.createExistingMMEntity(this.getSession(), "secondentityclass");
		Assert.assertNotNull(entity);

		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt
					.executeQuery("SELECT rid FROM mm_entity WHERE name = 'secondentityclass' and superentity = "
							+ firstentityclassRid);
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
	public void testCreateEntityWithInheritanceAndDuplicateAttributes() {
		log.debug("OntoQLODLTest.testCreateEntityWithInheritanceAndDuplicateAttributes()");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE ENTITY #supentity (#duplicateattribute int)");
		Assert.assertEquals(1, res);

		try {
			statement.executeUpdate("CREATE ENTITY #subentity UNDER #supentity (#duplicateattribute string)");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Attribute 'duplicateattribute' already defined for entity 'subentity'",
					e.getMessage());
		}

		try {
			statement.executeUpdate("CREATE ENTITY #entity (#duplicateattribute string, #duplicateattribute string)");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Attribute 'duplicateattribute' already defined for entity 'entity'", e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testCreateEntityWithInheritanceAndDuplicateAttributesCaseInsensitive() {
		log.debug("OntoQLODLTest.testCreateEntityWithInheritanceAndDuplicateAttributesCaseInsensitive()");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE ENTITY #supentity (#duplicateattribute int)");
		Assert.assertEquals(1, res);

		try {
			statement.executeUpdate("CREATE ENTITY #subentity UNDER #supentity (#duplicateAttribute string)");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Attribute 'duplicateAttribute' already defined for entity 'subentity'",
					e.getMessage());
		}

		try {
			statement.executeUpdate("CREATE ENTITY #entity (#duplicateAttribute string, #duplicateattribute string)");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Attribute 'duplicateattribute' already defined for entity 'entity'", e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testCreateEntityWithReferenceOnExistingEntity() {
		log.debug("MariusQLMMDLTest.testCreateEntityWithReference()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE ENTITY #entityref (#attribute int)");
		Assert.assertEquals(1, res);
		MMEntity entity = FactoryCore.createExistingMMEntity(this.getSession(), "entityref");

		res = statement.executeUpdate("CREATE ENTITY #supentity (#refAttrinute REF (#entityref) )");
		Assert.assertEquals(1, res);

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from mm_entity where name = 'entityref'");
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery("select * from mm_entity where name = 'supentity'");
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery(
					"select * from mm_datatype where dtype = 'ref' AND onclass = " + entity.getInternalId());
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
	public void testCreateEntityWithReferenceOnNOTExistingEntity() {
		log.debug("MariusQLMMDLTest.testCreateEntityWithReference()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		try {
			statement.executeUpdate("CREATE ENTITY #supentity (#refAttrinute REF (#notExistingEntity) )");
			Assert.fail("Must throw an MMEntityNotFoundException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Entity not found: notExistingEntity", e.getMessage());
		}

		try {
			statement.executeUpdate("CREATE ENTITY #supentity (#refAttrinute REF (#supentity) )");
			Assert.fail("Must throw an MMEntityNotFoundException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Entity not found: supentity", e.getMessage());
		}

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from mm_entity where name = 'supentity'");
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
	public void testDropEntityNotExists() {
		log.debug("MariusQLMMDLTest.testDropEntityNotExists()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		try {
			statement.executeUpdate("DROP ENTITY #NotExistEntity");
			Assert.fail("Must throw an MMEntityNotFoundException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Entity not found: NotExistEntity", e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testDropEntityExists() throws SQLException {
		log.debug("MariusQLMMDLTest.testDropEntityExists()");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate("CREATE ENTITY #entity (#attr int)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE ENTITY #entitytest (#attr int)");
		Assert.assertEquals(1, res);
		MMEntity entity = FactoryCore.createExistingMMEntity(getSession(), "entity");
		Assert.assertNotNull(entity);
		MMAttribute attr = entity.getDefinedMMAttribute("attr");
		Assert.assertNotNull(attr);
		String attrName = attr.getDefinitionName(false);

		res = statement.executeUpdate("DROP ENTITY #entity");
		Assert.assertEquals(1, res);

		try {
			FactoryCore.createExistingMMEntity(this.getSession(), "entity");
			Assert.fail("Must throw a MariusQLException");
		} catch (MariusQLException e) {
			Assert.assertEquals("Entity not found: entity", e.getMessage());
		}

		Statement stmt = this.getSession().createSQLStatement();
		ResultSet resultset = stmt.executeQuery("select * from mm_entity where name = 'entity'");
		Assert.assertFalse(resultset.next());

		// check if the column is removed
		try {
			resultset = stmt.executeQuery("select " + attrName + " from m_class");
			Assert.fail("Must throw an SQLException");
		} catch (SQLException e) {
		}

		resultset.close();
		stmt.close();

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testDropEntityWithReferenceOnOtherEntity() {
		log.debug("MariusQLMMDLTest.testDropEntityWithReferenceOnOtherEntity()");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate("CREATE ENTITY #refEntity (#attr int)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE ENTITY #entity (#refAttrinute REF (#refEntity) )");
		Assert.assertEquals(1, res);
		MMEntity entity = FactoryCore.createExistingMMEntity(this.getSession(), "entity");

		res = statement.executeUpdate("DROP ENTITY #entity");
		Assert.assertEquals(1, res);

		try {
			FactoryCore.createExistingMMEntity(this.getSession(), "entity");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Entity not found: entity", e.getMessage());
		}

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from mm_entity where name = 'entity'");
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery(
					"select * from mm_datatype where dtype = 'ref' AND onclass = " + entity.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail();
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testDropEntityReferencedByOtherAttribute() {
		log.debug("MariusQLMMDLTest.testDropEntityReferencedByOtherAttribute()");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate("CREATE ENTITY #refEntity (#attr int)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE ENTITY #entity (#refAttr REF (#refEntity) )");
		Assert.assertEquals(1, res);

		try {
			res = statement.executeUpdate("DROP ENTITY #refEntity");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Entity is referenced: refEntity", e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testDropEntityWithRecursiveReference() {
		log.debug("MariusQLMMDLTest.testDropEntityWithRecursiveReference()");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate("CREATE ENTITY #refEntity (#attr int)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("ALTER ENTITY #refEntity ADD #refAttr REF (#refEntity)");
		Assert.assertEquals(1, res);
		MMEntity entity = FactoryCore.createExistingMMEntity(this.getSession(), "refEntity");

		res = statement.executeUpdate("DROP ENTITY #refEntity");
		Assert.assertEquals(1, res);

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from mm_entity where name = 'refEntity'");
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery("select * from mm_attribute where scope = " + entity.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery(
					"select * from mm_datatype where dtype = 'ref' AND onclass = " + entity.getInternalId());
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
	public void testDropEntityCore() {
		log.debug("MariusQLMMDLTest.testDropEntityCore()");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		// Test Meta Meta Model
		try {
			statement.executeUpdate("DROP ENTITY #mmentity");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Can not remove core entity: mmentity", e.getMessage());
		}

		try {
			Statement stmt = this.getSession().createSQLStatement();

			ResultSet resultset = stmt.executeQuery("select * from mm_entity where name = 'mmentity'");
			Assert.assertTrue(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		// Test Meta-Model
		try {
			statement.executeUpdate("DROP ENTITY #class");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Can not remove core entity: class", e.getMessage());
		}

		try {
			Statement stmt = this.getSession().createSQLStatement();

			ResultSet resultset = stmt.executeQuery("select * from mm_entity where name = 'class'");
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
	public void testDropEntityWhenSuperEntity() {
		log.debug("MariusQLMMDLTest.testDropEntityWhenSuperEntity()");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE ENTITY #supentity (#supattr int)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE ENTITY #subentity UNDER #supentity (#subattr int)");
		Assert.assertEquals(1, res);

		try {
			statement.executeUpdate("DROP ENTITY #supentity");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Entity is used as parent entity: supentity", e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	public void testDropEntityWhenChildOfSuperEntity() {
		log.debug("MariusQLMMDLTest.testDropEntityWhenChildOfSuperEntity()");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE ENTITY #supentity (#supattr int)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE ENTITY #subentity UNDER #supentity (#subattr int)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("DROP ENTITY #subentity");
		Assert.assertEquals(1, res);

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testDropWithCollectionOfSimpleTypes() {
		log.debug("OntoQLODLTest.testDropWithCollectionOfSimpleTypes()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate(
				"CREATE ENTITY #entity (#a1 int array, #a2 real array, #a3 boolean array, #a4 string array)");
		Assert.assertEquals(1, res);

		MMEntity entity = FactoryCore.createExistingMMEntity(this.getSession(), "entity");
		Assert.assertNotNull(entity);

		res = statement.executeUpdate("DROP ENTITY #entity");
		Assert.assertEquals(1, res);

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("SELECT * FROM mm_attribute WHERE scope = " + entity.getInternalId());
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
	public void testDropWithCollectionOfReference() {
		log.debug("OntoQLODLTest.testDropWithCollectionOfReference()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE ENTITY #refentity (#refattribute int)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE ENTITY #entity (#attribute ref(#refentity) array)");
		Assert.assertEquals(1, res);

		MMEntity entity = FactoryCore.createExistingMMEntity(this.getSession(), "entity");
		Assert.assertNotNull(entity);

		Description attribute = entity.getDefinedDescription("attribute");
		MMDatatypeCollection collection = (MMDatatypeCollection) attribute.getRange();
		Assert.assertNotNull(collection);

		MMDatatypeReference reference = (MMDatatypeReference) collection.getDatatype();
		Assert.assertNotNull(reference);

		Category refEntity = reference.getCategory();
		Assert.assertNotNull(refEntity);
		Assert.assertEquals("refentity", refEntity.getName());

		res = statement.executeUpdate("DROP ENTITY #entity");
		Assert.assertEquals(1, res);

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery(
					"SELECT * FROM mm_datatype WHERE dtype = 'ref' AND onclass = " + refEntity.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery("SELECT * FROM mm_datatype WHERE dtype = 'array' AND collectiontype = "
					+ reference.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery("SELECT * FROM mm_attribute WHERE range = " + collection.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		res = statement.executeUpdate("DROP ENTITY #refentity");
		Assert.assertEquals(1, res);

		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery(
					"SELECT * FROM mm_datatype WHERE dtype = 'ref' AND onclass = " + refEntity.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery("SELECT * FROM mm_datatype WHERE dtype = 'array' AND collectiontype = "
					+ reference.getInternalId());
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
	public void testAlterEntityDropAttribute() {
		log.debug("MariusQLMMDLTest.testAlterEntityDropAttribute()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res;

		try {
			res = statement.executeUpdate("ALTER ENTITY #RemoveEntity DROP #RemoveAttribute int");
			Assert.fail("Must throw an OntoQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals(e.getMessage(), "Entity not found: RemoveEntity");
		}

		res = statement.executeUpdate("CREATE ENTITY #EntityToTest (#SingleAttribute int)");
		Assert.assertEquals(1, res);

		try {
			res = statement.executeUpdate("ALTER ENTITY #EntityToTest DROP #InexistantAttribute int");
			Assert.fail("Must throw an OntoQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("The attribute #InexistantAttribute is not defined on the ENTITY EntityToTest",
					e.getMessage());
		}

		res = statement.executeUpdate("CREATE ENTITY #EntityToAlter (#RemoveAttribute int)");
		Assert.assertEquals(1, res);

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("SELECT rid FROM mm_attribute WHERE name = 'RemoveAttribute'");
			Assert.assertTrue(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		res = statement.executeUpdate("ALTER ENTITY #EntityToAlter DROP #RemoveAttribute int");
		Assert.assertEquals(1, res);

		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("SELECT rid FROM mm_attribute WHERE name = 'RemoveAttribute'");
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
	public void testAlterEntityDropCoreAttribute() {
		log.debug("MariusQLMMDLTest.testAlterEntityDropCoreAttribute()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		int res;

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		res = statement.executeUpdate("CREATE ENTITY #EntityToAlter (#SingleAttribute int)");
		Assert.assertEquals(1, res);

		try {
			res = statement.executeUpdate("ALTER ENTITY #EntityToAlter DROP #Code int");
			Assert.fail("Must throw a MariusQl exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Can not remove a core attribute : code", e.getMessage());
		}

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testAlterEntityDropAttributeUsedByOtherEntity() throws SQLException {
		log.debug("MariusQLMMDLTest.testAlterEntityDropAttribute()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		int res;

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		res = statement.executeUpdate("CREATE ENTITY #EntityToTest (#SingleAttribute int)");
		Assert.assertEquals(1, res);

		MMEntity entity = FactoryCore.createExistingMMEntity(getSession(), "EntityToTest");
		Assert.assertNotNull(entity);
		MMAttribute attr = entity.getDefinedMMAttribute("SingleAttribute");
		Assert.assertNotNull(attr);
		String singleattributeName = attr.getDefinitionName(false);

		res = statement.executeUpdate("CREATE ENTITY #EntityToTestwithSameAttribute (#SingleAttribute int)");
		Assert.assertEquals(1, res);

		ResultSet resultset;

		res = statement.executeUpdate("ALTER ENTITY #EntityToTest DROP #SingleAttribute int");
		Assert.assertEquals(1, res);

		Statement stmt = this.getSession().createSQLStatement();
		resultset = stmt.executeQuery("SELECT rid FROM mm_attribute WHERE name = 'RemoveAttribute'");
		Assert.assertFalse(resultset.next());

		try {
			resultset = stmt.executeQuery("select " + singleattributeName + " from m_class");
			Assert.fail("Must throw an SQLException");
		} catch (SQLException e) {
		}

		resultset.close();
		stmt.close();

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testAlterEntityAddAttribute() {
		log.debug("MariusQLMMDLTest.testAlterEntityAddAttribute()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res;
		try {
			res = statement.executeUpdate("ALTER ENTITY #RemoveEntity ADD #RemoveAttribute int");
			Assert.fail("Must throw a MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals(e.getMessage(), "Entity not found: RemoveEntity");
		}

		res = statement.executeUpdate("CREATE ENTITY #EntityToAlter (#FirstAttribute int)");
		Assert.assertEquals(1, res);

		try {
			res = statement.executeUpdate("ALTER ENTITY #EntityToAlter ADD #FirstAttribute int");
			Assert.fail("Must throw a MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Attribute 'FirstAttribute' already defined for entity 'EntityToAlter'",
					e.getMessage());
		}

		res = statement.executeUpdate("ALTER ENTITY #EntityToAlter ADD #SecondAttribute int");
		Assert.assertEquals(1, res);

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("SELECT rid FROM mm_attribute WHERE name = 'SecondAttribute'");
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
	public void testAlterEntityCoreDropAttribute() {
		log.debug("MariusQLMMDLTest.testAlterEntityCoreDropAttribute()");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		// Test remove attribute in Meta-Model
		try {
			statement.executeUpdate("ALTER ENTITY #class DROP #code string");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Can not modify a core entity: class", e.getMessage());
		}

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("SELECT rid FROM mm_attribute WHERE name = 'code'");
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
	public void testAlterEntityCoreAddAttribute() {
		log.debug("MariusQLMMDLTest.testAlterEntityCoreAddAttribute()");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		// Test add attribute in Méta Méta Model
		try {
			statement.executeUpdate("ALTER ENTITY #mmentity ADD #AddCoreAttribute int ");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Can not modify a core entity: mmentity", e.getMessage());
		}

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("SELECT rid FROM mm_attribute WHERE name = 'AddCoreAttribute'");
			Assert.assertFalse(resultset.next());

			resultset.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Don't throw an exception: " + e.getMessage());
		}

		// Test add attribute in Meta-Model
		try {
			statement.executeUpdate("ALTER ENTITY #class add #AddCoreAttribute string");
			Assert.fail("Must throw an MariusQLException exception");
		} catch (MariusQLException e) {
			Assert.assertEquals("Can not modify a core entity: class", e.getMessage());
		}

		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("SELECT rid FROM mm_attribute WHERE name = 'AddCoreAttribute'");
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
	public void testAlterEntityAddAttributeWithReference() {
		log.debug("MariusQLMMDLTest.testAlterEntityAddAttributeWithReference()");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate("CREATE ENTITY #refEntity (#attr int)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("ALTER ENTITY #refEntity ADD #refAttr REF (#refEntity)");
		Assert.assertEquals(1, res);
		MMEntity entity = FactoryCore.createExistingMMEntity(this.getSession(), "refEntity");

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from mm_entity where name = 'refEntity'");
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery("select * from mm_attribute where name = 'refAttr'");
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery(
					"select * from mm_datatype where dtype = 'ref' AND onclass = " + entity.getInternalId());
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
	public void testAlterEntityDropAttributeWithReference() {
		log.debug("MariusQLMMDLTest.testAlterEntityDropAttributeWithReference()");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE ENTITY #refEntity (#attr int)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE ENTITY #entity (#refAttr REF (#refEntity))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("ALTER ENTITY #entity DROP #refAttr REF (#refEntity)");
		Assert.assertEquals(1, res);
		MMEntity entity = FactoryCore.createExistingMMEntity(this.getSession(), "refEntity");

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from mm_entity where name = 'refEntity'");
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery("select * from mm_attribute where name = 'refAttr'");
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery(
					"select * from mm_datatype where dtype = 'ref' AND onclass = " + entity.getInternalId());
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
	public void testAlterEntityDropAttributeWithReferenceWhenUsedByOtherAttribute() {
		log.debug("MariusQLMMDLTest.testAlterEntityDropAttributeWithReferenceWhenUsedByOtherAttribute()");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE ENTITY #refEntity (#attr int)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE ENTITY #entity (#refAttr REF (#refEntity))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("CREATE ENTITY #entity2 (#refAttr2 REF (#refEntity))");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("ALTER ENTITY #entity DROP #refAttr REF (#refEntity)");
		Assert.assertEquals(1, res);
		MMEntity entity = FactoryCore.createExistingMMEntity(this.getSession(), "refEntity");

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from mm_entity where name = 'refEntity'");
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery("select * from mm_attribute where name = 'refAttr'");
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery(
					"select * from mm_datatype where dtype = 'ref' AND onclass = " + entity.getInternalId());
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
	public void testAlterEntityAddAttributeWithCollectionOfSimpleTypes() {
		log.debug("MariusQLMMDLTest.testAlterEntityAddAttributeWithCollectionOfSimpleTypes()");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate("CREATE ENTITY #entity (#attr int)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("ALTER ENTITY #entity ADD #newattr int array");
		Assert.assertEquals(1, res);
		MMEntity entity = FactoryCore.createExistingMMEntity(this.getSession(), "entity");

		Description attribute = entity.getDefinedDescription("newattr");
		MMDatatypeCollection collection = (MMDatatypeCollection) attribute.getRange();
		Assert.assertNotNull(collection);

		Datatype intDatatype = FactoryCore.createMMSimpleDatatype(this.getSession(), DatatypeEnum.DATATYPEINT);

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from mm_entity where name = 'entity'");
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery(
					"select * from mm_attribute where name = 'newattr' and range = " + collection.getInternalId());
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery("select * from mm_datatype where dtype = 'array' AND collectiontype = "
					+ intDatatype.getInternalId());
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
	public void testAlterEntityDropAttributeWithCollectionOfSimpleTypes() {
		log.debug("MariusQLMMDLTest.testAlterEntityDropAttributeWithCollectionOfSimpleTypes()");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate("CREATE ENTITY #entity (#newattr int array)");
		Assert.assertEquals(1, res);

		MMEntity entity = FactoryCore.createExistingMMEntity(this.getSession(), "entity");

		Description attribute = entity.getDefinedDescription("newattr");
		MMDatatypeCollection collection = (MMDatatypeCollection) attribute.getRange();
		Assert.assertNotNull(collection);

		Datatype intDatatype = FactoryCore.createMMSimpleDatatype(this.getSession(), DatatypeEnum.DATATYPEINT);

		res = statement.executeUpdate("ALTER ENTITY #entity DROP #newattr int array");
		Assert.assertEquals(1, res);

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from mm_entity where name = 'entity'");
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery(
					"select * from mm_attribute where name = 'newattr' and range = " + collection.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery("select * from mm_datatype where dtype = 'array' AND collectiontype = "
					+ intDatatype.getInternalId());
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
	public void testAlterEntityAddAttributeWithCollectionOfReference() {
		log.debug("MariusQLMMDLTest.testAlterEntityAddAttributeWithCollectionOfReference()");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate("CREATE ENTITY #refEntity (#attr int)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("ALTER ENTITY #refEntity ADD #refAttr REF (#refEntity) array");
		Assert.assertEquals(1, res);

		MMEntity entity = FactoryCore.createExistingMMEntity(this.getSession(), "refEntity");

		Description attribute = entity.getDefinedDescription("refAttr");
		MMDatatypeCollection collection = (MMDatatypeCollection) attribute.getRange();
		Assert.assertNotNull(collection);

		MMDatatypeReference reference = (MMDatatypeReference) collection.getDatatype();
		Assert.assertNotNull(reference);

		Category refEntity = reference.getCategory();
		Assert.assertNotNull(refEntity);
		Assert.assertEquals("refEntity", refEntity.getName());

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from mm_entity where name = 'refEntity'");
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery(
					"select * from mm_attribute where name = 'refAttr' and range = " + collection.getInternalId());
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery(
					"select * from mm_datatype where dtype = 'ref' AND onclass = " + entity.getInternalId());
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery("select * from mm_datatype where dtype = 'array' AND collectiontype = "
					+ reference.getInternalId());
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
	public void testAlterEntityDropAttributeWithCollectionOfReference() {
		log.debug("MariusQLMMDLTest.testAlterEntityDropAttributeWithCollectionOfReference()");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		int res = statement.executeUpdate("CREATE ENTITY #refEntity (#intAttr int)");
		Assert.assertEquals(1, res);

		res = statement.executeUpdate("ALTER ENTITY #refEntity ADD #refAttr REF (#refEntity) array");
		Assert.assertEquals(1, res);

		MMEntity entity = FactoryCore.createExistingMMEntity(this.getSession(), "refEntity");

		Description attribute = entity.getDefinedDescription("refAttr");
		MMDatatypeCollection collection = (MMDatatypeCollection) attribute.getRange();
		Assert.assertNotNull(collection);

		MMDatatypeReference reference = (MMDatatypeReference) collection.getDatatype();
		Assert.assertNotNull(reference);

		Category refEntity = reference.getCategory();
		Assert.assertNotNull(refEntity);
		Assert.assertEquals("refEntity", refEntity.getName());

		res = statement.executeUpdate("ALTER ENTITY #refEntity DROP #refAttr REF (#refEntity) array");
		Assert.assertEquals(1, res);

		ResultSet resultset;
		try {
			Statement stmt = this.getSession().createSQLStatement();

			resultset = stmt.executeQuery("select * from mm_entity where name = 'refEntity'");
			Assert.assertTrue(resultset.next());

			resultset = stmt.executeQuery(
					"select * from mm_attribute where name = 'refAttr' and range = " + collection.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery(
					"select * from mm_datatype where dtype = 'ref' AND onclass = " + entity.getInternalId());
			Assert.assertFalse(resultset.next());

			resultset = stmt.executeQuery("select * from mm_datatype where dtype = 'array' AND collectiontype = "
					+ reference.getInternalId());
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
}
