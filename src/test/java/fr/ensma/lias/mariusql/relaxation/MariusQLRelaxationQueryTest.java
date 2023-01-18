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
package fr.ensma.lias.mariusql.relaxation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.mariusql.AbstractMariusQLTest;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSet;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;

/**
 * @author Geraud FOKOU
 * @author Mickael BARON
 */
public class MariusQLRelaxationQueryTest extends AbstractMariusQLTest {

	public enum EnumTypes {
		TINT, TBOOLEAN, TSTRING;

		public String toSQL(String value) {
			if (this.name().equals("TSTRING")) {
				if (value.startsWith("\"") && value.endsWith("\"")) {
					return value.replace("\"", "'");
				} else {
					return "'" + value + "'";
				}
			} else if (this.name().equals("TBOOLEAN")) {
				if (value.equals("t")) {
					return "true";
				} else {
					return "false";
				}
			} else {
				return value;
			}
		}
	}

	private Logger log = LoggerFactory.getLogger(MariusQLRelaxationQueryTest.class);

	private String[] nameClass = { "Hotel", "Motel", "Inn", "BedAndBreakfast", "VacationRental", "Hostel", "Retreat",
			"Resort", "Other", "Apartment", "Lodging" };

	private String[] nameProperties = { "name", "nbstart", "price", "cityname", "statename", "countryname",
			"countrycode", "address", "location", "url", "iswifi", "hasclim", "hastv", "facilities" };

	private EnumTypes[] types = { EnumTypes.TSTRING, EnumTypes.TINT, EnumTypes.TINT, EnumTypes.TSTRING,
			EnumTypes.TSTRING, EnumTypes.TSTRING, EnumTypes.TSTRING, EnumTypes.TSTRING, EnumTypes.TSTRING,
			EnumTypes.TSTRING, EnumTypes.TBOOLEAN, EnumTypes.TBOOLEAN, EnumTypes.TBOOLEAN, EnumTypes.TSTRING };

	/**
	 * Creation of ontology, loading of instance all in cache memory.
	 * 
	 * @return
	 */
	@Before
	public void ontologyAndInstanceGMPSetUpCacheTest() {
		log.debug("MariusQLRelaxationGMPQueryTest.ontologyAndInstanceGMPSetUpCacheTest()");

		this.getSession().setReferenceLanguage(MariusQLConstants.ENGLISH);
		this.getSession().setDefaultNameSpace("http://www.lias.ensma.fr");
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		try {
			long start = System.currentTimeMillis();
			this.createLodgingOntology(statement);
			for (String currentClass : nameClass) {
				this.createLodgingInstances(statement, currentClass);
			}

			System.out.println("Execution time: " + (System.currentTimeMillis() - start));
		} finally {
			statement.close();
		}

		statement.close();
	}

	@After
	public void ontologyAndInstanceGMPTearDownCacheTest() {
		this.getSession().rollback();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private void createLodgingOntology(MariusQLStatement ontoQLStatement) {
		String queryOntoQL = "CREATE #CLASS Lodging ( "
				+ "DESCRIPTOR (#name[fr] = 'Logement', #code = '722671DB4BCD3') "
				+ "PROPERTIES (name String, nbstart Int, price Int, cityname String, statename String, countryname String, countrycode String, address String, location String, url String, iswifi Boolean, hasclim Boolean, hastv Boolean, facilities String))";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));

		queryOntoQL = "CREATE EXTENT of Lodging (name , nbstart , price , cityname , statename , countryname , countrycode , address , location , url , iswifi , hasclim , hastv , facilities)";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));

		queryOntoQL = "CREATE #CLASS Hotel UNDER Lodging( "
				+ "DESCRIPTOR (#name[fr] = 'Hotel', #code = '722671DB5F1BB'))";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));

		queryOntoQL = "CREATE EXTENT of Hotel (name , nbstart , price , cityname , statename , countryname , countrycode , address , location , url , iswifi , hasclim , hastv , facilities)";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));

		queryOntoQL = "CREATE #CLASS VacationRental UNDER Lodging( "
				+ "DESCRIPTOR (#name[fr] = 'Vacation Rental', #code = '722671DB6F2BB'))";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));

		queryOntoQL = "CREATE EXTENT of VacationRental (name , nbstart , price , cityname , statename , countryname , countrycode , address , location , url , iswifi , hasclim , hastv , facilities)";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));

		queryOntoQL = "CREATE #CLASS Other under Lodging ( "
				+ "DESCRIPTOR (#name[fr] = 'Autre Logement', #code = '722671DB5F4BB'))";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));

		queryOntoQL = "CREATE EXTENT of Other (name , nbstart , price , cityname , statename , countryname , countrycode , address , location , url , iswifi , hasclim , hastv , facilities)";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));

		queryOntoQL = "CREATE #CLASS Apartment under Lodging ( "
				+ "DESCRIPTOR (#name[fr] = 'Appartement', #code = '722671DB7F2BB'))";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));

		queryOntoQL = "CREATE EXTENT of Apartment (name , nbstart , price , cityname , statename , countryname , countrycode , address , location , url , iswifi , hasclim , hastv , facilities)";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));

		queryOntoQL = "CREATE #CLASS Motel UNDER Hotel ( "
				+ "DESCRIPTOR (#name[fr] = 'Motel', #code = '722671DB8F4BB'))";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));

		queryOntoQL = "CREATE EXTENT of Motel (name , nbstart , price , cityname , statename , countryname , countrycode , address , location , url , iswifi , hasclim , hastv , facilities)";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));

		queryOntoQL = "CREATE #CLASS Inn UNDER Hotel ( "
				+ "DESCRIPTOR (#name[fr] = 'Auberge', #code = '722671DB8F1BB'))";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));

		queryOntoQL = "CREATE EXTENT of Inn (name , nbstart , price , cityname , statename , countryname , countrycode , address , location , url , iswifi , hasclim , hastv , facilities)";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));

		queryOntoQL = "CREATE #CLASS BedAndBreakfast UNDER Hotel ( "
				+ "DESCRIPTOR (#name[fr] = 'B&B', #code = '722671DB8F2BB'))";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));

		queryOntoQL = "CREATE EXTENT of BedAndBreakfast (name , nbstart , price , cityname , statename , countryname , countrycode , address , location , url , iswifi , hasclim , hastv , facilities)";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));

		queryOntoQL = "CREATE #CLASS Hostel UNDER Hotel ( "
				+ "DESCRIPTOR (#name[fr] = 'Hospice', #code = '722671DB6F4BB'))";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));

		queryOntoQL = "CREATE EXTENT of Hostel (name , nbstart , price , cityname , statename , countryname , countrycode , address , location , url , iswifi , hasclim , hastv , facilities)";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));

		queryOntoQL = "CREATE #CLASS Retreat UNDER Hotel ( "
				+ "DESCRIPTOR (#name[fr] = 'Maison de retraite', #code = '722671DB6F2BB'))";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));

		queryOntoQL = "CREATE EXTENT of Retreat (name , nbstart , price , cityname , statename , countryname , countrycode , address , location , url , iswifi , hasclim , hastv , facilities)";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));

		queryOntoQL = "CREATE #CLASS Resort UNDER Hotel ( "
				+ "DESCRIPTOR (#name[fr] = 'Haut Standing', #code = '722671DB9F3BB'))";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));

		queryOntoQL = "CREATE EXTENT of Resort (name , nbstart , price , cityname , statename , countryname , countrycode , address , location , url , iswifi , hasclim , hastv , facilities)";
		Assert.assertEquals(1, ontoQLStatement.executeUpdate(queryOntoQL));
	}

	private void createLodgingInstances(MariusQLStatement ontoQLStatement, String className) {
		InputStream resourceAsStream = getClass().getResourceAsStream("/relaxation/" + className + ".csv");

		BufferedReader br = null;

		String line = "";
		String cvsSplitBy = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

		try {
			br = new BufferedReader(new InputStreamReader(resourceAsStream));
			int count = 0;
			while ((line = br.readLine()) != null) {
				String[] country = line.split(cvsSplitBy);
				final String query = buildQuery(className, country);
				log.debug(count + " " + query);
				ontoQLStatement.executeUpdate(query);
				count++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String buildQuery(String nameClass, String[] values) {
		String insertClause = "INSERT INTO " + nameClass + " (";
		String valueClause = "VALUES (";

		String currentValue;
		EnumTypes currentType;

		for (int i = 1; i < values.length; i++) {
			currentValue = values[i];
			currentType = types[i - 1];

			if (!values[i].isEmpty()) {
				insertClause += nameProperties[i - 1] + ", ";
				valueClause += currentType.toSQL(currentValue) + ", ";
			}
		}

		insertClause = insertClause.substring(0, insertClause.length() - 2);
		valueClause = valueClause.substring(0, valueClause.length() - 2);

		return insertClause + ") " + valueClause + ")";
	}

	@Test
	public void testVerticalRelaxationWithNum() throws SQLException {
		log.debug("MariusQLRelaxationQueryTest.testVerticalRelaxationWithNum");

		this.getSession().setReferenceLanguage(MariusQLConstants.ENGLISH);
		this.getSession().setDefaultNameSpace("http://www.lias.ensma.fr");
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		String query = "SELECT name, nbstart, price, cityname FROM BedAndBreakfast B APPROX GEN(BedAndBreakfast, 2)";
		final MariusQLResultSet executeQuery = statement.executeQuery(query);

		Assert.assertEquals(3, executeQuery.getStepNumber());
		Assert.assertEquals(
				" ( query ( SELECT_FROM ( FROM ( RANGE BedAndBreakfast B ) ) ( SELECT name nbstart price cityname ) ) )",
				executeQuery.getMariusQLMetaData(0).getRelaxationMetaData().getRelaxedQuery());
		Assert.assertEquals(
				" ( query ( SELECT_FROM ( FROM ( RANGE Hotel B ) ) ( SELECT name nbstart price cityname ) ) )",
				executeQuery.getMariusQLMetaData(1).getRelaxationMetaData().getRelaxedQuery());
		Assert.assertEquals(
				" ( query ( SELECT_FROM ( FROM ( RANGE Lodging B ) ) ( SELECT name nbstart price cityname ) ) )",
				executeQuery.getMariusQLMetaData(2).getRelaxationMetaData().getRelaxedQuery());

		statement.close();
	}

	@Test
	public void testVerticalRelaxationWithNumProblems() throws SQLException {
		log.debug("MariusQLRelaxationQueryTest.testVerticalRelaxationWithNumProblems");

		this.getSession().setReferenceLanguage(MariusQLConstants.ENGLISH);
		this.getSession().setDefaultNameSpace("http://www.lias.ensma.fr");
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		String query = "SELECT name, nbstart, price, cityname FROM BedAndBreakfast B APPROX GEN(BedAndBreakfast, 10)";
		try {
			statement.executeQuery(query);
		} catch (MariusQLException e) {
			Assert.assertEquals("Inn is not a super class of BedAndBreakfast", e.getMessage());
		}
	}

	@Test
	public void testVerticalRelaxationWithIdentifier() throws SQLException {
		log.debug("MariusQLRelaxationQueryTest.testVerticalRelaxationWithIdentifier");

		this.getSession().setReferenceLanguage(MariusQLConstants.ENGLISH);
		this.getSession().setDefaultNameSpace("http://www.lias.ensma.fr");
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		String query = "SELECT name, nbstart, price, cityname FROM BedAndBreakfast B APPROX GEN(BedAndBreakfast, Lodging)";
		final MariusQLResultSet executeQuery = statement.executeQuery(query);

		Assert.assertEquals(2, executeQuery.getStepNumber());
		Assert.assertEquals(
				" ( query ( SELECT_FROM ( FROM ( RANGE BedAndBreakfast B ) ) ( SELECT name nbstart price cityname ) ) )",
				executeQuery.getMariusQLMetaData(0).getRelaxationMetaData().getRelaxedQuery());
		Assert.assertEquals(
				" ( query ( SELECT_FROM ( FROM ( RANGE Lodging B ) ) ( SELECT name nbstart price cityname ) ) )",
				executeQuery.getMariusQLMetaData(1).getRelaxationMetaData().getRelaxedQuery());

		statement.close();
	}

	@Test
	public void testVerticalRelaxationWithIdentifierProblems() throws SQLException {
		log.debug("MariusQLRelaxationQueryTest.testVerticalRelaxationWithIdentifierProblems");

		this.getSession().setReferenceLanguage(MariusQLConstants.ENGLISH);
		this.getSession().setDefaultNameSpace("http://www.lias.ensma.fr");
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		String query = "SELECT name, nbstart, price, cityname FROM BedAndBreakfast B APPROX GEN(BedAndBreakfast, Inn)";

		try {
			statement.executeQuery(query);
		} catch (MariusQLException e) {
			Assert.assertEquals("Inn is not a super class of BedAndBreakfast", e.getMessage());
		}

		statement.close();
	}

	@Test
	public void testHorizontalRelaxation() throws SQLException {
		log.debug("MariusQLRelaxationQueryTest.testVerticalRelaxationWithIdentifier");

		this.getSession().setReferenceLanguage(MariusQLConstants.ENGLISH);
		this.getSession().setDefaultNameSpace("http://www.lias.ensma.fr");
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		String query = "SELECT name, nbstart, price, cityname FROM Motel APPROX SIB(Motel,[BedAndBreakfast, Inn])";
		final MariusQLResultSet executeQuery = statement.executeQuery(query);

		Assert.assertEquals(3, executeQuery.getStepNumber());
		Assert.assertEquals(
				" ( query ( SELECT_FROM ( FROM ( RANGE Motel ) ) ( SELECT name nbstart price cityname ) ) )",
				executeQuery.getMariusQLMetaData(0).getRelaxationMetaData().getRelaxedQuery());
		Assert.assertEquals(
				" ( query ( SELECT_FROM ( FROM ( RANGE BedAndBreakfast ) ) ( SELECT name nbstart price cityname ) ) )",
				executeQuery.getMariusQLMetaData(1).getRelaxationMetaData().getRelaxedQuery());

		statement.close();
	}

	@Test
	public void testHorizontalRelaxationProblems() throws SQLException {
		log.debug("MariusQLRelaxationQueryTest.testVerticalRelaxationWithIdentifier");

		this.getSession().setReferenceLanguage(MariusQLConstants.ENGLISH);
		this.getSession().setDefaultNameSpace("http://www.lias.ensma.fr");
		MariusQLStatement statement = this.getSession().createMariusQLStatement();

		String query = "SELECT name, nbstart, price, cityname FROM Motel APPROX SIB(Motel,[Lodging, Inn])";
		try {
			statement.executeQuery(query);
		} catch (MariusQLException e) {
			Assert.assertEquals("Lodging is not a sibling class of Motel", e.getMessage());
		}
	}
}
