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
package fr.ensma.lias.mariusql;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;

import fr.ensma.lias.mariusql.DriverDelegateTest.DriverEnum;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * @author Mickael BARON
 */
public class AbstractMariusQLTest {

	private DriverDelegateTest currentDriverDelegate;

	@Before
	public void setUp() throws ClassNotFoundException, SQLException, IOException {
		currentDriverDelegate = new DriverDelegateTest();
		currentDriverDelegate.setUp();
	}

	@After
	public void after() throws SQLException {
		currentDriverDelegate.after();
	}

	public MariusQLSession getSession() {
		return currentDriverDelegate.getSession();
	}

	public DriverEnum getCurrentDriver() {
		return currentDriverDelegate.getCurrentDriver();
	}

	/**
	 * Convert a list of object to String.
	 * 
	 * @param values
	 * @return
	 */
	protected String listToString(List values) {
		return MariusQLConstants.OPEN_BRACKET + StringHelper.join(",", values.iterator())
				+ MariusQLConstants.CLOSE_BRACKET;
	}
}
