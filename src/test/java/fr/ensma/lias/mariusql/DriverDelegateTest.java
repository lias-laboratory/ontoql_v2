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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.jdbc.impl.MariusQLSessionImpl;

/**
 * @author Mickael BARON
 */
public class DriverDelegateTest {

	public enum DriverEnum {
		POSTGRESQL, HSQLDB, ORACLE
	}

	public static final String POSTGRESQL = "POSTGRESQL";

	public static final String HSQLDB = "HSQLDB";

	public static final String ORACLE = "ORACLE";

	private static final DriverEnum DEFAULT_DRIVER = DriverEnum.HSQLDB;

	private DriverEnum currentDriver;

	private MariusQLSession currentMariusSession;

	public DriverEnum getCurrentDriver() {
		return currentDriver;
	}

	private static boolean toBoolean(String name) {
		return name == null || (name.equalsIgnoreCase("true"));
	}

	public void setUp() throws IOException, SQLException {
		String driverName = System.getProperty("driver");
		boolean useMetaModelCache = toBoolean(System.getProperty("useMetaModelCache"));
		boolean useModelCache = toBoolean(System.getProperty("useModelCache"));

		if (HSQLDB.equalsIgnoreCase(driverName)) {
			currentDriver = DriverEnum.HSQLDB;
		} else if (POSTGRESQL.equalsIgnoreCase(driverName)) {
			currentDriver = DriverEnum.POSTGRESQL;
		} else if (ORACLE.equalsIgnoreCase(driverName)) {
			currentDriver = DriverEnum.ORACLE;
		} else {
			currentDriver = DEFAULT_DRIVER;
		}

		Properties props = new Properties();
		switch (currentDriver) {
		case HSQLDB: {
			props.setProperty("server.host", "mem");
			props.setProperty("server.port", "5432");
			props.setProperty("server.user", "sa");
			props.setProperty("server.password", "");
			props.setProperty("server.sid", "mariusqltest");
			props.setProperty("driver.class", "fr.ensma.lias.mariusql.driver.hsqldb.MariusQLDriverImpl");

			Connection connexion = DriverManager.getConnection("jdbc:hsqldb:mem:mariusqltest", "sa", "");
			MariusQLScriptRunner newScriptRunner = new MariusQLScriptRunner(connexion, false, false);
			InputStream resourceAsStream = getClass().getResourceAsStream("/sql/template_mariusdb_hsqldb.sql");
			newScriptRunner.runScript(new InputStreamReader(resourceAsStream));
			break;
		}
		case POSTGRESQL: {
			props.setProperty("server.host", "localhost");
			props.setProperty("server.port", "5433");
			props.setProperty("server.user", "postgres");
			props.setProperty("server.password", "psql");
			props.setProperty("server.sid", "mariusqltest");
			props.setProperty("driver.class", "fr.ensma.lias.mariusql.driver.postgresql.MariusQLDriverImpl");

			break;
		}
		case ORACLE: {
			props.setProperty("server.host", "localhost");
			props.setProperty("server.port", "1521");
			props.setProperty("server.user", "system");
			props.setProperty("server.password", "psql");
			props.setProperty("server.sid", "mariusqlltest");
			props.setProperty("driver.class", "fr.ensma.lias.mariusql.driver.oracle.MariusQLDriverImpl");

			System.out.println("run script");
			Connection connexion = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:mariusqltest",
					"system", "psql");
			MariusQLScriptRunner newScriptRunner = new MariusQLScriptRunner(connexion, false, false);
			InputStream resourceAsStream = getClass().getResourceAsStream("/sql/template_mariusdb_oracle.sql");
			newScriptRunner.runScript(new InputStreamReader(resourceAsStream));

			break;
		}
		}

		// Create session.
		currentMariusSession = new MariusQLSessionImpl(props);

		// Enable or disable cache.
		currentMariusSession.getMetaModelCache().setEnabled(useMetaModelCache);
		currentMariusSession.getModelCache().setEnabled(useModelCache);
	}

	public void after() throws SQLException {
		switch (currentDriver) {
		case HSQLDB: {
			Statement statement = currentMariusSession.createSQLStatement();
			statement.executeUpdate("SHUTDOWN");
			statement.close();
			break;
		}
		case POSTGRESQL: {
			currentMariusSession.close();
			break;
		}
		case ORACLE: {
			Statement statement = currentMariusSession.createSQLStatement();
			Statement statementSupp = currentMariusSession.createSQLStatement();
			ResultSet resultSet = statement
					.executeQuery("select 'drop table ' || table_name from user_tables where table_name like 'I_%'");

			while (resultSet.next()) {
				statementSupp.executeUpdate(resultSet.getString(1));
			}
			currentMariusSession.close();
			break;
		}
		default: {
		}
		}
	}

	public MariusQLSession getSession() {
		return currentMariusSession;
	}
}
