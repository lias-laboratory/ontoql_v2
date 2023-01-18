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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import fr.ensma.lias.mariusql.benchmark.AllTestsBenchmark;
import fr.ensma.lias.mariusql.bulkload.AllTestsBulkload;
import fr.ensma.lias.mariusql.cfg.MariusQLConfigTest;
import fr.ensma.lias.mariusql.concurrency.AllTestsConcurrency;
import fr.ensma.lias.mariusql.driver.AllTestsDriver;
import fr.ensma.lias.mariusql.engine.AllTestsEngine;
import fr.ensma.lias.mariusql.metric.AllTestsMetric;
import fr.ensma.lias.mariusql.querycache.AllTestsQueryCache;
import fr.ensma.lias.mariusql.relaxation.AllTestsRelaxation;
import fr.ensma.lias.mariusql.sparql.AllTestsSPARQL;
import fr.ensma.lias.mariusql.util.AllTestsUtil;

/**
 * @author Mickael BARON
 */
@RunWith(Suite.class)
@SuiteClasses(value = { MariusQLConfigTest.class, AllTestsEngine.class, AllTestsUtil.class, AllTestsSPARQL.class,
		AllTestsBenchmark.class, AllTestsDriver.class, AllTestsMetric.class, AllTestsRelaxation.class,
		AllTestsBulkload.class, AllTestsQueryCache.class, AllTestsConcurrency.class })
public class AllTests {

}
