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
package fr.ensma.lias.mariusql.metric;

import java.sql.SQLException;

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
public class MariusQLMetricTest extends AbstractMariusQLTest {

	private Logger log = LoggerFactory.getLogger(MariusQLMetricTest.class);

	@Test
	public void testGetClassRowCount() throws SQLException {
		log.debug("MariusQLMetricTest.testGetClassRowCount()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (p1 int, p2 real, p3 boolean, p4 string))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (p1, p2, p3, p4)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (p1, p2, p3, p4) values (1, 1.5, true, 'string')"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (p1, p2, p3, p4) values (1, 1.5, true, 'string')"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (p1, p2, p3, p4) values (1, 1.5, true, 'string')"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (p1, p2, p3, p4) values (1, 1.5, true, 'string')"));

		Assert.assertEquals(4, this.getSession().getMariusQLMetric().getClassRowCount("Vehicule", false));
		Assert.assertEquals(4, this.getSession().getMariusQLMetric().getClassRowCount("Vehicule", true));

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testGetClassCount() throws SQLException {
		log.debug("MariusQLMetricTest.testGetClassCount()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS VehiculeA (PROPERTIES (p1 int, p2 real, p3 boolean, p4 string))"));
		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS VehiculeB (PROPERTIES (p1 int, p2 real, p3 boolean, p4 string))"));
		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS VehiculeC (PROPERTIES (p1 int, p2 real, p3 boolean, p4 string))"));

		Assert.assertEquals(3, this.getSession().getMariusQLMetric().getClassCount(false));
		Assert.assertEquals(3, this.getSession().getMariusQLMetric().getClassCount(true));

		this.getSession().rollback();
		statement.close();
	}

	@Test
	public void testGelAllClassRowCount() {
		log.debug("MariusQLMetricTest.testGetAllClassRowCount()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (p1 int, p2 real, p3 boolean, p4 string))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (p1, p2, p3, p4)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (p1, p2, p3, p4) values (1, 1.5, true, 'string')"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (p1, p2, p3, p4) values (1, 1.5, true, 'string')"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (p1, p2, p3, p4) values (1, 1.5, true, 'string')"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (p1, p2, p3, p4) values (1, 1.5, true, 'string')"));

		Assert.assertEquals(1,
				statement.executeUpdate("CREATE #CLASS Humain (PROPERTIES (p5 int, p6 real, p7 boolean, p8 string))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Humain (p5, p6, p7, p8)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Humain (p5, p6, p7, p8) values (1, 1.5, true, 'string')"));

		Assert.assertEquals(1, statement.executeUpdate("CREATE #CLASS Voiture (PROPERTIES (p9 int))"));

		Assert.assertEquals(5, this.getSession().getMariusQLMetric().getAllClassRowCount(false));

		statement.close();
	}

	@Test
	public void testGetClassPolymorphRowCount() {
		log.debug("MariusQLMetricTest.testGetClassPolymorphRowCount()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (p1 int, p2 real, p3 boolean, p4 string))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Vehicule (p1, p2, p3, p4)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (p1, p2, p3, p4) values (1, 1.5, true, 'string')"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (p1, p2, p3, p4) values (1, 1.5, true, 'string')"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (p1, p2, p3, p4) values (1, 1.5, true, 'string')"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (p1, p2, p3, p4) values (1, 1.5, true, 'string')"));

		Assert.assertEquals(1, statement.executeUpdate("CREATE #CLASS Voiture under Vehicule (PROPERTIES (p9 int))"));
		Assert.assertEquals(1, statement.executeUpdate("CREATE EXTENT OF Voiture (p1, p2, p3, p4, p9)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Voiture (p1, p2, p3, p4, p9) values (1, 1.5, true, 'string', 1)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Voiture (p1, p2, p3, p4, p9) values (1, 1.5, true, 'string', 1)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Voiture (p1, p2, p3, p4, p9) values (1, 1.5, true, 'string', 1)"));

		Assert.assertEquals(7, this.getSession().getMariusQLMetric().getClassPolymorphRowCount("Vehicule", false));

		statement.close();
	}
}
