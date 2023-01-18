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
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.mariusql.AbstractMariusQLTest;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSet;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;

/**
 * This test class is a particular Model Query Language (MQL) test dedicated for
 * Model Quey Language (MQL).
 * 
 * @author Ghada TRIKI
 */
public class MariusQLMMQLTest extends AbstractMariusQLTest {

	private Logger log = LoggerFactory.getLogger(MariusQLMMQLTest.class);

	@Test
	public void testMMQLSimpleQueries() {
		log.debug("MariusQLMQLTest.testMMQLSimpleQueries()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		List<String> entities = Arrays.asList("mmentity", "mmattribute", "mmdatatype", "datatype", "class", "property",
				"staticclass", "boolean", "int", "real", "string", "multistring", "uritype", "enum", "counttype");

		try {
			ResultSet res = statement.executeQuery("SELECT #NAME from #MMENTITY");
			Assert.assertTrue(res.next());
			Assert.assertTrue(entities.contains(res.getString(1)));
			Assert.assertTrue(res.next());
			Assert.assertTrue(entities.contains(res.getString(1)));
			Assert.assertTrue(res.next());
			Assert.assertTrue(entities.contains(res.getString(1)));
			Assert.assertTrue(res.next());
			Assert.assertTrue(entities.contains(res.getString(1)));
			Assert.assertTrue(res.next());
			Assert.assertTrue(entities.contains(res.getString(1)));
			Assert.assertTrue(res.next());
			Assert.assertTrue(entities.contains(res.getString(1)));
			Assert.assertTrue(res.next());
			Assert.assertTrue(entities.contains(res.getString(1)));
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}

	@Test
	public void testMMQLMetaTest() throws SQLException {
		log.debug("MariusQLMQLTest.testMMQLMetaTest()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		MariusQLResultSet res = statement.executeQuery(
				"select #rid, #name, #package, #mappedtablename, #ismetametamodel, #iscore, #attributes, #superentity from #MMENTITY where #name = 'mmentity'");
		Assert.assertTrue(res.next());
		Assert.assertNotNull(res.getString(1));
		Assert.assertEquals("mmentity", res.getString(2));
		Assert.assertNull(res.getString(3));
		Assert.assertEquals("mm_entity", res.getString(4));
		Assert.assertTrue(res.getBoolean(5));
		Assert.assertFalse(res.getBoolean(6));

		Assert.assertEquals("{41,42,43,44,45,46,47,48}", this.listToString(res.getList(7, Long.class)));
		Assert.assertNull(res.getString(8));

		res = statement.executeQuery(
				"select #rid, #name, #package, #mappedtablename, #ismetametamodel, #iscore, #attributes, #superentity from #MMENTITY where #name = 'class'");
		Assert.assertTrue(res.next());
		Assert.assertNotNull(res.getString(1));
		Assert.assertEquals("class", res.getString(2));
		Assert.assertNull(res.getString(3));
		Assert.assertEquals("m_class", res.getString(4));
		Assert.assertFalse(res.getBoolean(5));
		Assert.assertTrue(res.getBoolean(6));
		Assert.assertEquals("{10,12,21,24,26,30,35,38,40,52}", this.listToString(res.getList(7, Long.class)));
		Assert.assertNull(res.getString(8));

		this.getSession().rollback();
		statement.close();
	}
}
