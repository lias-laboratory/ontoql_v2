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
package fr.ensma.lias.mariusql.concurrency;

import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.mariusql.AbstractMariusQLTest;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;

/**
 * This test class is a particular Data Query Language (MQL) test dedicated for
 * Instance Quey Language (IQL).
 * 
 * @author Mickael BARON
 */
public class MariusQLQueryConcurrencyTest extends AbstractMariusQLTest {

	private static final int NB_THREADS = 20;

	private Logger log = LoggerFactory.getLogger(MariusQLQueryConcurrencyTest.class);

	private volatile Exception exec;

	@Before
	public void setup() {
		exec = null;
	}

	private void createExecutorService(Runnable incrementer) {
		// Submit several jobs that increment the counter
		ExecutorService pool = Executors.newFixedThreadPool(10);
		for (int i = 0; i < NB_THREADS; i++) {
			pool.submit(incrementer);
		}
		pool.shutdown();

		try {
			pool.awaitTermination(50, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			Assert.fail("Do not throw a SQLException.");
		}

		Assert.assertNull(exec);
	}

	@Test
	public void testIQLSimpleProperties() {
		log.debug("MariusQLIQLTest.testIQLSimpleProperties()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		Assert.assertEquals(1, statement
				.executeUpdate("CREATE #CLASS Vehicule (PROPERTIES (propertyIntFirst Int, propertyIntSecond Int))"));
		Assert.assertEquals(1,
				statement.executeUpdate("CREATE EXTENT OF Vehicule (propertyIntFirst, propertyIntSecond)"));
		Assert.assertEquals(1,
				statement.executeUpdate("INSERT INTO Vehicule (propertyIntFirst, propertyIntSecond) values (0, 1)"));

		statement.close();

		createExecutorService(new Runnable() {
			@Override
			public void run() {
				MariusQLStatement statement = MariusQLQueryConcurrencyTest.this.getSession().createMariusQLStatement();
				final ResultSet executeQuery = statement
						.executeQuery("SELECT propertyIntFirst, propertyIntSecond FROM Vehicule");

				try {
					Assert.assertTrue(executeQuery.next());
					Assert.assertEquals(0, executeQuery.getInt(1));
					Assert.assertEquals(1, executeQuery.getInt(2));
				} catch (Exception e) {
					exec = e;
				} finally {
					MariusQLQueryConcurrencyTest.this.getSession().rollback();
				}
			}
		});
	}

	@Test
	public void testMQLSelectClass() throws InterruptedException {
		log.debug("MariusQLMQLTest.testMQLSelectClass()");

		this.getSession().setReferenceLanguage(MariusQLConstants.NO_LANGUAGE);

		MariusQLStatement statement = this.getSession().createMariusQLStatement();
		statement.executeUpdate("CREATE #CLASS BB3DFD4 (DESCRIPTOR (#code = 'BB3DFD4'))");
		statement.close();

		createExecutorService(new Runnable() {
			@Override
			public void run() {
				MariusQLStatement statement = MariusQLQueryConcurrencyTest.this.getSession().createMariusQLStatement();
				final ResultSet executeQuery = statement.executeQuery("SELECT #CODE from #CLASS");

				try {
					Assert.assertTrue(executeQuery.next());
					Assert.assertEquals("BB3DFD4", executeQuery.getString(1));
				} catch (Exception e) {
					exec = e;
				} finally {
					MariusQLQueryConcurrencyTest.this.getSession().rollback();
				}
			}
		});
	}
}
