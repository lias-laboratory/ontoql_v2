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
package fr.ensma.lias.mariusql.sample;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.mariusql.AbstractMariusQLTest;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;

/**
 * @author Mickael BARON
 */
public class VehicleTest extends AbstractMariusQLTest {

	private Logger log = LoggerFactory.getLogger(VehicleTest.class);

	@Test
	public void testVehicleTest() {
		log.debug("VehicleTest.testVehicleTest()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		this.getSession().setDefaultNameSpace("testNS");

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		int result = statement.executeUpdate(
				"CREATE #CLASS \"0002-41982799300025#01-1#1\" (DESCRIPTOR (#code = '0002-41982799300025#01-1#1', #name[en] = 'Vehicle', #name[fr] = 'Vehicule', #definition[fr] = 'Un Vehicule', #definition[en] = 'A vehicle', #package='testNS'))");
		result = statement.executeUpdate(
				"CREATE #CLASS \"0002-41982799300025#01-2#1\" UNDER \"0002-41982799300025#01-1#1\" (DESCRIPTOR (#code = '0002-41982799300025#01-2#1', #name[en] = 'Motorcycle', #name[fr] = 'Vehicule à moteur', #definition[fr] = 'Un Véhicule à moteur', #definition[en] = 'A motorcycle', #package='testNS'))");
		result = statement.executeUpdate(
				"CREATE #CLASS \"0002-41982799300025#01-3#1\" UNDER \"0002-41982799300025#01-2#1\" (DESCRIPTOR (#code = '0002-41982799300025#01-3#1', #name[en] = 'Car', #name[fr] = 'Voiture', #definition[fr] = 'Une voiture', #definition[en] = 'A car', #package='testNS'))");
		result = statement.executeUpdate(
				"CREATE #CLASS \"0002-41982799300025#01-4#1\" (DESCRIPTOR (#code = '0002-41982799300025#01-4#1', #name[en] = 'Person', #name[fr] = 'Une personne', #definition[fr] = 'Une personne', #definition[en] = 'A person', #package='testNS'))");

		result = statement.executeUpdate(
				"ALTER #CLASS \"0002-41982799300025#01-1#1\" ADD \"0002-41982799300025#02-1#1\" String DESCRIPTOR (#code = '0002-41982799300025#02-1#1', #name[fr] = 'modèle', #name[en] = 'model', #definition[en] = 'A model', #definition[fr] = 'Un modèle')");
		result = statement.executeUpdate(
				"ALTER #CLASS \"0002-41982799300025#01-2#1\" ADD \"0002-41982799300025#02-2#1\" Int DESCRIPTOR (#code = '0002-41982799300025#02-2#1', #name[fr] = 'capacité', #name[en] = 'capacity', #definition[en] = 'A capacity', #definition[fr] = 'Une capacité')");
		result = statement.executeUpdate(
				"ALTER #CLASS \"0002-41982799300025#01-3#1\" ADD \"0002-41982799300025#02-3#1\" Int DESCRIPTOR (#code = '0002-41982799300025#02-3#1', #name[fr] = 'roues', #name[en] = 'wheels', #definition[en] = 'A wheels', #definition[fr] = 'Une roue')");
		result = statement.executeUpdate(
				"ALTER #CLASS \"0002-41982799300025#01-4#1\" ADD \"0002-41982799300025#02-4#1\" String DESCRIPTOR (#code = '0002-41982799300025#02-4#1', #name[fr] = 'nom', #name[en] = 'name', #definition[en] = 'A name', #definition[fr] = 'Un nom')");
		result = statement.executeUpdate(
				"ALTER #CLASS \"0002-41982799300025#01-4#1\" ADD \"0002-41982799300025#02-5#1\" Ref(\"0002-41982799300025#01-1#1\") DESCRIPTOR (#code = '0002-41982799300025#02-5#1', #name[fr] = 'principal vehicule', #name[en] = 'mainVehicle', #definition[en] = 'The main vehicle', #definition[fr] = 'Le principal vehicule')");

		result = statement.executeUpdate(
				"CREATE EXTENT OF \"0002-41982799300025#01-2#1\" (\"0002-41982799300025#02-2#1\",\"0002-41982799300025#02-1#1\")");
		result = statement
				.executeUpdate("CREATE EXTENT OF \"0002-41982799300025#01-3#1\" (\"0002-41982799300025#02-3#1\")");
		result = statement.executeUpdate(
				"CREATE EXTENT OF \"0002-41982799300025#01-4#1\" (\"0002-41982799300025#02-4#1\",\"0002-41982799300025#02-5#1\")");

		result = statement.executeUpdate(
				"INSERT INTO \"0002-41982799300025#01-2#1\" (rid,\"0002-41982799300025#02-2#1\",\"0002-41982799300025#02-1#1\") values (1,1800,'Peugeot')");
		result = statement.executeUpdate(
				"INSERT INTO \"0002-41982799300025#01-2#1\" (rid,\"0002-41982799300025#02-2#1\",\"0002-41982799300025#02-1#1\") values (2,1200,'Clio')");
		result = statement.executeUpdate(
				"INSERT INTO \"0002-41982799300025#01-3#1\" (rid,\"0002-41982799300025#02-3#1\") values (3,4)");
		result = statement.executeUpdate(
				"INSERT INTO \"0002-41982799300025#01-3#1\" (rid,\"0002-41982799300025#02-3#1\") values (4,5)");

		result = statement.executeUpdate(
				"UPDATE \"0002-41982799300025#01-4#1\" SET \"0002-41982799300025#02-5#1\" = 1 WHERE rid = 5");
		result = statement.executeUpdate(
				"UPDATE \"0002-41982799300025#01-4#1\" SET \"0002-41982799300025#02-5#1\" = 2 WHERE rid = 6");

		Assert.assertEquals(0, result);

		this.getSession().rollback();
		statement.close();
	}
}
