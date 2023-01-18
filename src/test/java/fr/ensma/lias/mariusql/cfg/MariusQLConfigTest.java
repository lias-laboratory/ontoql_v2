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
package fr.ensma.lias.mariusql.cfg;

import java.util.Properties;

import org.aeonbits.owner.ConfigFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mickael BARON
 */
public class MariusQLConfigTest {

	@Test
	public void loadMariusQLConfigFileTest() {
		MariusQLConfig cfg = ConfigFactory.create(MariusQLConfig.class);

		Assert.assertEquals("localhost", cfg.host());
		Assert.assertEquals(5433, cfg.port());
		Assert.assertEquals("postgres", cfg.user());
		Assert.assertEquals("psql", cfg.password());
		Assert.assertEquals("mariusqltest", cfg.sid());
	}

	@Test
	public void loadMariusQLConfigProgrammaticallyTest() {
		Properties props = new Properties();
		props.setProperty("server.host", "localhost");
		props.setProperty("server.port", "5433");
		props.setProperty("server.user", "postgres");
		props.setProperty("server.password", "psql");
		props.setProperty("server.sid", "mariusqltest");

		props.setProperty("driver.class", "fr.ensma.lias.mariusql.driver.posgresql.MariusQLDriverImpl");

		MariusQLConfig cfg = ConfigFactory.create(MariusQLConfig.class, props);

		Assert.assertEquals("localhost", cfg.host());
		Assert.assertEquals(5433, cfg.port());
		Assert.assertEquals("postgres", cfg.user());
		Assert.assertEquals("psql", cfg.password());
		Assert.assertEquals("mariusqltest", cfg.sid());
	}
}
