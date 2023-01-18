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

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.mariusql.AbstractMariusQLTest;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;

/**
 * @author Mickael BARON
 */
public class MariusQLSessionTest extends AbstractMariusQLTest {

	private Logger log = LoggerFactory.getLogger(MariusQLSessionTest.class);

	@Test
	public void testExecuteUpdatesFromFile() {
		log.debug("MariusQLSessionTest.testExecuteUpdatesFromFile()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		try {
			final InputStream resourceAsStream = this.getClass().getClassLoader()
					.getResourceAsStream("humanequipement.ontoql");
			Assert.assertEquals(823, statement.executeUpdates(resourceAsStream));
		} catch (MariusQLException e) {
			e.printStackTrace();
		}
		this.getSession().commit();
		statement.close();
	}

	@Test
	public void testExecuteUpdates() {
		log.debug("MariusQLSessionTest.testExecuteUpdates()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		String statementQueries = "CREATE #CLASS \"0002-41982799300025#01-1#1\" (DESCRIPTOR (#code = '0002-41982799300025#01-1#1', #name[en] = 'Vehicle', #name[fr] = 'Vehicule', #definition[fr] = 'Un Vehicule', #definition[en] = 'A vehicle', #package='testNS'))\n"
				+ "CREATE #CLASS \"0002-41982799300025#01-2#1\" UNDER \"0002-41982799300025#01-1#1\" (DESCRIPTOR (#code = '0002-41982799300025#01-2#1', #name[en] = 'Motorcycle', #name[fr] = 'Vehicule à moteur', #definition[fr] = 'Un Véhicule à moteur', #definition[en] = 'A motorcycle', #package='testNS'))\n"
				+ "CREATE #CLASS \"0002-41982799300025#01-3#1\" UNDER \"0002-41982799300025#01-2#1\" (DESCRIPTOR (#code = '0002-41982799300025#01-3#1', #name[en] = 'Car', #name[fr] = 'Voiture', #definition[fr] = 'Une voiture', #definition[en] = 'A car', #package='testNS'))\n"
				+ "CREATE #CLASS \"0002-41982799300025#01-4#1\" (DESCRIPTOR (#code = '0002-41982799300025#01-4#1', #name[en] = 'Person', #name[fr] = 'Une personne', #definition[fr] = 'Une personne', #definition[en] = 'A person', #package='testNS'))\n"
				+

				"ALTER #CLASS \"0002-41982799300025#01-1#1\" ADD \"0002-41982799300025#02-1#1\" String DESCRIPTOR (#code = '0002-41982799300025#02-1#1', #name[fr] = 'modèle', #name[en] = 'model', #definition[en] = 'A model', #definition[fr] = 'Un modèle')\n"
				+ "ALTER #CLASS \"0002-41982799300025#01-2#1\" ADD \"0002-41982799300025#02-2#1\" Int DESCRIPTOR (#code = '0002-41982799300025#02-2#1', #name[fr] = 'capacité', #name[en] = 'capacity', #definition[en] = 'A capacity', #definition[fr] = 'Une capacité')\n"
				+ "ALTER #CLASS \"0002-41982799300025#01-3#1\" ADD \"0002-41982799300025#02-3#1\" Int DESCRIPTOR (#code = '0002-41982799300025#02-3#1', #name[fr] = 'roues', #name[en] = 'wheels', #definition[en] = 'A wheels', #definition[fr] = 'Une roue')\n"
				+ "ALTER #CLASS \"0002-41982799300025#01-4#1\" ADD \"0002-41982799300025#02-4#1\" String DESCRIPTOR (#code = '0002-41982799300025#02-4#1', #name[fr] = 'nom', #name[en] = 'name', #definition[en] = 'A name', #definition[fr] = 'Un nom')\n"
				+ "ALTER #CLASS \"0002-41982799300025#01-4#1\" ADD \"0002-41982799300025#02-5#1\" Ref(\"0002-41982799300025#01-1#1\") DESCRIPTOR (#code = '0002-41982799300025#02-5#1', #name[fr] = 'principal vehicule', #name[en] = 'mainVehicle', #definition[en] = 'The main vehicle', #definition[fr] = 'Le principal vehicule')\n"
				+

				"CREATE EXTENT OF \"0002-41982799300025#01-2#1\" (\"0002-41982799300025#02-2#1\",\"0002-41982799300025#02-1#1\")\n"
				+ "CREATE EXTENT OF \"0002-41982799300025#01-3#1\" (\"0002-41982799300025#02-3#1\")\n"
				+ "CREATE EXTENT OF \"0002-41982799300025#01-4#1\" (\"0002-41982799300025#02-4#1\",\"0002-41982799300025#02-5#1\")\n"
				+

				"INSERT INTO \"0002-41982799300025#01-2#1\" (rid,\"0002-41982799300025#02-1#1\",\"0002-41982799300025#02-2#1\") values (1,'Peugeot',1800)\n"
				+ "INSERT INTO \"0002-41982799300025#01-2#1\" (rid,\"0002-41982799300025#02-1#1\",\"0002-41982799300025#02-2#1\") values (2,'Clio',1200)\n"
				+ "INSERT INTO \"0002-41982799300025#01-3#1\" (rid,\"0002-41982799300025#02-3#1\") values (3,4)\n"
				+ "INSERT INTO \"0002-41982799300025#01-3#1\" (rid,\"0002-41982799300025#02-3#1\") values (4,5)\n"
				+ "INSERT INTO \"0002-41982799300025#01-4#1\" (rid,\"0002-41982799300025#02-4#1\") values (5,'selma')\n"
				+ "INSERT INTO \"0002-41982799300025#01-4#1\" (rid,\"0002-41982799300025#02-4#1\") values (6,'walid')\n"
				+

				"UPDATE \"0002-41982799300025#01-4#1\" SET \"0002-41982799300025#02-5#1\" = 1 WHERE rid = 5\n"
				+ "UPDATE \"0002-41982799300025#01-4#1\" SET \"0002-41982799300025#02-5#1\" = 2 WHERE rid = 6";

		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(20, statement.executeUpdates(statementQueries));
		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testGellAllNameSpaces() {
		log.debug("MariusQLSessionTest.testGellAllNameSpaces()");

		List<String> allNameSpaces = this.getSession().getAllNameSpaces();
		Assert.assertTrue(allNameSpaces.isEmpty());

		this.getSession().setDefaultNameSpace("http://www.lias.fr/");
		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int res = statement.executeUpdate("CREATE #CLASS Class1 (DESCRIPTOR (#code ='Class1')))");
		Assert.assertEquals(1, res);
		allNameSpaces = this.getSession().getAllNameSpaces();
		Assert.assertTrue(allNameSpaces.size() == 1);
		Assert.assertEquals("http://www.lias.fr/", allNameSpaces.get(0));
		res = statement.executeUpdate("CREATE #CLASS Class2 (DESCRIPTOR (#code ='Class2')))");
		Assert.assertEquals(1, res);
		allNameSpaces = this.getSession().getAllNameSpaces();
		Assert.assertTrue(allNameSpaces.size() == 1);
		Assert.assertEquals("http://www.lias.fr/", allNameSpaces.get(0));

		this.getSession().setDefaultNameSpace("http://www.lias-lab.fr/");
		res = statement.executeUpdate("CREATE #CLASS Class3 (DESCRIPTOR (#code ='Class3')))");
		Assert.assertEquals(1, res);
		allNameSpaces = this.getSession().getAllNameSpaces();
		Assert.assertTrue(allNameSpaces.size() == 2);
	}

	@Test
	public void testAddLanguage() {
		log.debug("MariusQLSessionTest.testAddLanguage()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		try {
			this.getSession().addLanguage("Espagnol", "es", "Spain");
			statement.executeUpdate(
					"CREATE #CLASS VehiculeClass (DESCRIPTOR (#code = 'VehiculeClass', #name[en] = 'Car', #name[fr] = 'Vehicule', #name[es] = 'Coche'))");
			ResultSet res = statement.executeQuery("SELECT #name[fr], #name[en],#name[es] from #CLASS ");
			Assert.assertTrue(res.next());
			Assert.assertEquals("Vehicule", res.getString(1));
			Assert.assertEquals("Car", res.getString(2));
			Assert.assertEquals("Coche", res.getString(3));
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			Assert.assertEquals(1, statement
					.executeUpdate("UPDATE #class SET #name[en] = 'Ca', #name[fr] = 'Vehicul', #name[es] = 'Coch'"));
			ResultSet res = statement.executeQuery("SELECT #name[fr], #name[en],#name[es] from #CLASS ");
			Assert.assertTrue(res.next());
			Assert.assertEquals("Vehicul", res.getString(1));
			Assert.assertEquals("Ca", res.getString(2));
			Assert.assertEquals("Coch", res.getString(3));
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			Assert.assertEquals(1, statement.executeUpdate(
					"INSERT INTO #class (#dtype, #name[en], #name[fr],#name[es],#code) values ('class','CAR', 'VEHICULE', 'COCHE', 'code2')"));
			ResultSet res = statement
					.executeQuery("SELECT #name[fr], #name[en],#name[es] from #CLASS WHERE #code = 'code2'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("VEHICULE", res.getString(1));
			Assert.assertEquals("CAR", res.getString(2));
			Assert.assertEquals("COCHE", res.getString(3));
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			this.getSession().setReferenceLanguage("Espagnol");
			ResultSet res = statement.executeQuery("SELECT #name from #CLASS WHERE #code = 'code2'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("COCHE", res.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			Assert.assertEquals(1, statement
					.executeUpdate("INSERT INTO #class (#dtype, #name, #code) values ('class', 'COCHE', 'code5')"));
			ResultSet res = statement.executeQuery("SELECT #name from #CLASS WHERE #code = 'code5'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("COCHE", res.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.fail("Do not throw a SQLException.");
		}

		try {
			this.getSession().setReferenceLanguage("Allemand");
			ResultSet res = statement.executeQuery("SELECT #name from #CLASS WHERE #code = 'code2'");
			Assert.assertTrue(res.next());
			Assert.assertEquals("Auto", res.getString(1));
		} catch (MariusQLException e) {
			Assert.assertEquals("The language does not exist: Allemand", e.getMessage());
		} catch (SQLException e) {
			Assert.fail("Do not throw a SQLException.");
		} finally {
			this.getSession().rollback();
		}

		statement.close();
	}
}
