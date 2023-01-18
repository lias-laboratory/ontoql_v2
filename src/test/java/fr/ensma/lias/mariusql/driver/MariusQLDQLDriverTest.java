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
package fr.ensma.lias.mariusql.driver;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.mariusql.AbstractMariusQLTest;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSet;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;

/**
 * @author Mickael BARON
 */
public class MariusQLDQLDriverTest extends AbstractMariusQLTest {

	private Logger log = LoggerFactory.getLogger(MariusQLDQLDriverTest.class);

	@Test
	public void testGetCountRow() throws SQLException {
		log.debug("MariusQLDQLDriverTest.testGetCountRow()");

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

		final MariusQLResultSet executeQuery = statement
				.executeQuery("select #rid from #class where #code = 'Vehicule'");
		Assert.assertTrue(executeQuery.next());

		Assert.assertEquals(4, this.getSession().getMariusQLDQL().getCountRow("i_" + executeQuery.getInt(1), true));

		this.getSession().rollback();

		statement.close();
	}
}
