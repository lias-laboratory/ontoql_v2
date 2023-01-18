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
package fr.ensma.lias.mariusql.sparql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.mariusql.AbstractMariusQLTest;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.engine.MariusQLMQLTest;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;

/**
 * Test the extension of MariusQL to OWL
 * 
 * @author Stéphane JEAN
 * @author Adel GHAMNIA
 */
public class SPARQLToMariusQLTest extends AbstractMariusQLTest {

	private Logger log = LoggerFactory.getLogger(MariusQLMQLTest.class);

	protected StringBuilder queryOntoQL;

	protected MariusQLStatement statement;

	protected ResultSet resultSetObtenu;

	@Before
	public void initializeData() throws SQLException {
		log.debug("SPARQLToMariusQLTest.initianizeData");
		this.getSession().setReferenceLanguage(MariusQLConstants.ENGLISH);
		this.getSession().setDefaultNameSpace("http://www.lias.ensma.fr");
		statement = this.getSession().createMariusQLStatement();

		// Create class CHARENTE
		queryOntoQL = new StringBuilder();
		queryOntoQL.append("create #class CHARENTE ("
				+ "DESCRIPTOR (#name[en] = 'CHARENTE', #code='CLASS_CHARENTE', #definition[en] ='Rowing buoyancy aid. Chasuble buoyancy aid. 120g fabric. Alvéo foam. Adjustable side straps (for size M only). Double stitching. Adjustable leg strap.'))"
				+ "properties (uri String ) ;");
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL.toString()));

		// Create class EGALIS
		queryOntoQL = new StringBuilder();
		queryOntoQL.append("create #class EGALIS (" + "DESCRIPTOR (#name[en] = 'EGALIS', #code='EGALIS' ))"
				+ "properties (uri String  DESCRIPTOR (#name[en] = 'Size', #code='1' )) )");

		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL.toString()));

		// Create class EGALIS1
		queryOntoQL = new StringBuilder();
		queryOntoQL
				.append("create #class EGALIS1 UNDER EGALIS (" + "DESCRIPTOR (#name[en] = 'EGALIS1', #code='EGALIS1' ))"
						+ "properties (uri String  DESCRIPTOR (#name[en] = 'uri', #code='2' ) ))");

		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL.toString()));

		// create class PERSONAL_EQUIPEMENT/SAFETY
		queryOntoQL = new StringBuilder();
		queryOntoQL.append("create #class PERSONAL_EQUIPEMENTSAFETY ("
				+ "DESCRIPTOR (#name[en] = 'PERSONAL_EQUIPEMENTSAFETY',#name[fr]='PERSONAL_EQUIPEMENTSAFETY', #code='PERSONAL_EQUIPEMENTSAFETY' ))"
				+ "properties (uri String DESCRIPTOR (#name[en] = 'Size', #code='3' ) ))");
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL.toString()));

		// create class CAGS
		queryOntoQL = new StringBuilder();
		queryOntoQL.append("create #class CAGS UNDER PERSONAL_EQUIPEMENTSAFETY ("
				+ "DESCRIPTOR (#name[en] = 'CAGS',#name[fr]='CAGS', #code='CAGS'))"
				+ "properties (uri String  DESCRIPTOR (#name[en] = 'Size', #code='4' )))");

		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL.toString()));

		// create class EGALIS2
		queryOntoQL = new StringBuilder();
		queryOntoQL.append("create #class EGALIS2 UNDER CAGS ("
				+ "DESCRIPTOR (#name[en] = 'EGALIS2', #code='EGALIS2' ))" + "properties (uri String) );");
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL.toString()));

		// create class ZICRAL_TUBE_DIAMETER_28
		queryOntoQL = new StringBuilder();
		queryOntoQL.append("create #class ZICRAL_TUBE_DIAMETER_28 UNDER CAGS ("
				+ "DESCRIPTOR (#name[en] = 'ZICRAL_TUBE_DIAMETER_28',#name[fr]='ZICRAL_TUBE_DIAMETER_28', #code='ZICRAL_TUBE_DIAMETER_28' ))"
				+ "properties (uri String) );");

		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL.toString()));

		// create class ZICRAL_TUBE_DIAMETER_30
		queryOntoQL = new StringBuilder();
		queryOntoQL.append("create #class ZICRAL_TUBE_DIAMETER_30 UNDER CAGS ("
				+ "DESCRIPTOR (#name[en] = 'ZICRAL_TUBE_DIAMETER_30', #code='ZICRAL_TUBE_DIAMETER_30'))"
				+ "properties (uri String  DESCRIPTOR (#name[en] = 'uri',  #code='5' )))");

		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL.toString()));

		// Create class HUDSON
		queryOntoQL = new StringBuilder();
		queryOntoQL.append("CREATE #CLASS HUDSON (");
		queryOntoQL.append("DESCRIPTOR (");
		queryOntoQL.append("#code= 'HUDSON', ");
		queryOntoQL.append("#name[en] = 'HUDSON',#name[fr] ='HUDSON',  ");
		queryOntoQL.append(
				"#definition[en] ='High performance dry cag. Nylon heavy resistance fabric. Taped seams. Inner twin waist. Neoprene belt.')");
		queryOntoQL.append("PROPERTIES (");
		queryOntoQL.append(
				"uri String DESCRIPTOR (#code='Property_uri', #name[en] = 'uri',#definition[en] ='High performance dry cag. Nylon heavy resistance fabric. Taped seams. Inner twin waist. Neoprene belt.'),");
		queryOntoQL.append(
				"Size String DESCRIPTOR (#code='Property_Size', #name[en] = 'Size',#definition[en] ='High performance dry cag. Nylon heavy resistance fabric. Taped seams. Inner twin waist. Neoprene belt.'),");
		queryOntoQL.append(
				"Virage int DESCRIPTOR (#code='Property_Virage', #name[en] = 'Virage',#definition[en] ='High performance dry cag. Nylon heavy resistance fabric. Taped seams. Inner twin waist. Neoprene belt.'),");
		queryOntoQL.append("Reference String DESCRIPTOR (#code='Property_Reference', #name[en] = 'Reference')))");

		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL.toString()));
		queryOntoQL = new StringBuilder();
		queryOntoQL.append("select c.#code,#name[en], c.#name[fr] from #class c where c.#name[en] = 'HUDSON'");
		resultSetObtenu = statement.executeQuery(queryOntoQL.toString());
		Assert.assertTrue(resultSetObtenu.next());
		Assert.assertEquals("HUDSON", resultSetObtenu.getString(1));
		Assert.assertEquals("HUDSON", resultSetObtenu.getString(2));
		Assert.assertFalse(resultSetObtenu.next());
		String queryOntoQL1 = "create extent of HUDSON (uri, Size, Virage, Reference)";
		statement.executeUpdate(queryOntoQL1);
		queryOntoQL1 = "insert into HUDSON (uri, Size, Virage)" + " values ('http://300062', 'XL', 1)";
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL1));

		queryOntoQL1 = "insert into HUDSON (uri, Size, Virage) " + "values ('http://300061', 'L', 3)";
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL1));

		// Create class FOC_BABY
		queryOntoQL = new StringBuilder();
		queryOntoQL.append("CREATE #CLASS FOC_BABY (");
		queryOntoQL.append("DESCRIPTOR (");
		queryOntoQL.append("#code= 'FOC_BABY', ");
		queryOntoQL.append("#name[en] = 'FOC_BABY')");

		queryOntoQL.append("PROPERTIES (");

		queryOntoQL.append("uri String  DESCRIPTOR (#name[en] = 'uri' ),");
		queryOntoQL.append("Size String  DESCRIPTOR (#name[en] = 'Size' ),");
		queryOntoQL.append("its_hudsons REF (HUDSON) ARRAY ) )");

		statement.executeUpdate(queryOntoQL.toString());
		queryOntoQL = new StringBuilder();
		queryOntoQL.append("select c.#code, #name[en], c.#name[fr] from #class c where c.#name[en] = 'FOC_BABY'");
		resultSetObtenu = statement.executeQuery(queryOntoQL.toString());
		Assert.assertTrue(resultSetObtenu.next());
		Assert.assertEquals("FOC_BABY", resultSetObtenu.getString(1));
		Assert.assertEquals("FOC_BABY", resultSetObtenu.getString(2));
		Assert.assertFalse(resultSetObtenu.next());
		queryOntoQL1 = "create extent of FOC_BABY (uri, Size, its_hudsons )";
		statement.executeUpdate(queryOntoQL1);

		queryOntoQL1 = "insert into FOC_BABY (uri, Size, its_hudsons)"
				+ " values ('http://300075', 'XS',  ARRAY(select h.rid from HUDSON h))";
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL1));

		// Create class MUFFS
		queryOntoQL = new StringBuilder();
		queryOntoQL.append("CREATE #CLASS MUFFS (");
		queryOntoQL.append("DESCRIPTOR (");
		queryOntoQL.append("#code= 'MUFFS', ");
		queryOntoQL.append("#name[en] = 'MUFFS',#name[fr] ='MUFFS')");
		queryOntoQL.append("PROPERTIES (");
		queryOntoQL.append("Virage String,");
		queryOntoQL.append("Size String  DESCRIPTOR (#name[en] = 'Size',#name[fr] ='Size'),");
		queryOntoQL.append("uri String DESCRIPTOR (#name[en] = 'uri')))");

		statement.executeUpdate(queryOntoQL.toString());
		queryOntoQL = new StringBuilder();
		queryOntoQL.append("select c.#code, #name[en],c.#name[fr] from #class c where c.#name[en] = 'MUFFS'");
		resultSetObtenu = statement.executeQuery(queryOntoQL.toString());
		Assert.assertTrue(resultSetObtenu.next());
		Assert.assertEquals("MUFFS", resultSetObtenu.getString(1));
		Assert.assertEquals("MUFFS", resultSetObtenu.getString(2));
		Assert.assertFalse(resultSetObtenu.next());

		queryOntoQL1 = "create extent of MUFFS (Size, uri)";
		statement.executeUpdate(queryOntoQL1);

		queryOntoQL1 = "insert into MUFFS (Size, uri)" + " values (NULL, 'http://')";
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL1));
		queryOntoQL1 = "insert into MUFFS (Size, uri)" + " values ('Taille unique', 'http://389000')";
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL1));
		queryOntoQL1 = "insert into MUFFS (Size, uri)" + " values ('Taille unique', 'http://387000')";
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL1));
		queryOntoQL1 = "insert into MUFFS (Size, uri)" + " values ('Taille unique', 'http://388800')";
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL1));

		// Create the class ONTARIO
		queryOntoQL = new StringBuilder();
		queryOntoQL.append("CREATE #CLASS ONTARIO UNDER  EGALIS1 (");
		queryOntoQL.append("DESCRIPTOR (");
		queryOntoQL.append("#code= 'ONTARIO', ");
		queryOntoQL.append("#name[en] = 'ONTARIO',#name[fr] ='ONTARIO')");
		queryOntoQL.append("PROPERTIES (");
		queryOntoQL.append("Size String  DESCRIPTOR (#name[en] = 'Size' ),");
		queryOntoQL.append("Reference String DESCRIPTOR (#name[en] = 'Reference'),");
		queryOntoQL.append("uri String DESCRIPTOR (#name[en] = 'uri' )))");

		statement.executeUpdate(queryOntoQL.toString());
		queryOntoQL = new StringBuilder();
		queryOntoQL.append("select c.#code, c.#name[en] from #class c where c.#name[en] = 'ONTARIO'");
		resultSetObtenu = statement.executeQuery(queryOntoQL.toString());
		Assert.assertTrue(resultSetObtenu.next());
		Assert.assertEquals("ONTARIO", resultSetObtenu.getString(1));
		Assert.assertEquals("ONTARIO", resultSetObtenu.getString(2));
		Assert.assertFalse(resultSetObtenu.next());

		queryOntoQL1 = "create extent of ONTARIO (Size, Reference,  uri)";
		statement.executeUpdate(queryOntoQL1);

		queryOntoQL1 = "insert into ONTARIO (Size, Reference,  uri)" + " values ( 'L', '300030', null )";
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL1));
		queryOntoQL1 = "insert into ONTARIO (Size, Reference,  uri)" + " values ( 'XL', '300031', null )";
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL1));
		queryOntoQL1 = "insert into ONTARIO (Size, Reference,  uri)" + " values ( 'S', '300028', null )";
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL1));
		queryOntoQL1 = "insert into ONTARIO (Size, Reference,  uri)" + " values ( 'M', '300029',  null )";
		Assert.assertEquals(1, statement.executeUpdate(queryOntoQL1));
	}

	/**
	 * Test the translation of SPARQL content-query into OntoQL content-query
	 */
	@Test
	public void testQueryOnContent() throws SQLException {
		String querySPARQL = "PREFIX ns: <EGALIS_F> " + "SELECT ?f ?size WHERE {?f rdf:type ns:FOC_BABY . "
				+ " ?f ns:its_hudsons ?h . ?h ns:Size ?size " + "} ";
		ResultSet rs = statement.executeSPARQLQuery(querySPARQL);
		rs.next();
		Assert.assertEquals("http://300075", rs.getString(1));
		Assert.assertEquals("XL", rs.getString(2));
		rs.next();
		Assert.assertEquals("http://300075", rs.getString(1));

		Assert.assertEquals("L", rs.getString(2));

		querySPARQL = "PREFIX ns: <EGALIS_FR> " + "SELECT ?c ?s WHERE {?c rdf:type ns:HUDSON "
				+ "OPTIONAL {?c ns:Size ?s }" + "} ORDER BY DESC(?s)";
		rs = statement.executeSPARQLQuery(querySPARQL);
		rs.next();
		Assert.assertEquals("http://300062", rs.getString(1));
		Assert.assertEquals("XL", rs.getString(2));
		rs.next();
		Assert.assertEquals("http://300061", rs.getString(1));
		Assert.assertEquals("L", rs.getString(2));
		Assert.assertFalse(rs.next());

		querySPARQL = "PREFIX ns: <EGALIS_F> " + "SELECT ?f ?size  WHERE {?f rdf:type ns:FOC_BABY . "
				+ "?f ns:Size ?size " + "} ";
		rs = statement.executeSPARQLQuery(querySPARQL);
		rs.next();
		Assert.assertEquals("http://300075", rs.getString(1));
		Assert.assertEquals("XS", rs.getString(2));
		Assert.assertFalse(rs.next());

		querySPARQL = "PREFIX ns: <EGALIS_F> " + "SELECT ?f ?h  WHERE {?f rdf:type ns:FOC_BABY . "
				+ " ?f ns:its_hudsons ?h " + "} ";
		rs = statement.executeSPARQLQuery(querySPARQL);
		Assert.assertTrue(rs.next());

		Assert.assertEquals("http://300075", rs.getString(1));
		Assert.assertEquals("http://300062", rs.getString(2));
		rs.next();
		Assert.assertEquals("http://300075", rs.getString(1));
		Assert.assertEquals("http://300061", rs.getString(2));

		querySPARQL = "PREFIX ns: <EGALIS_FR> " + "SELECT ?c ?s WHERE {?c rdf:type ns:HUDSON "
				+ "OPTIONAL {?c ns:Size ?s }" + "} ORDER BY DESC(?s)";
		rs = statement.executeSPARQLQuery(querySPARQL);
		rs.next();
		Assert.assertEquals("http://300062", rs.getString(1));
		Assert.assertEquals("XL", rs.getString(2));
		rs.next();
		Assert.assertEquals("http://300061", rs.getString(1));
		Assert.assertEquals("L", rs.getString(2));
		Assert.assertFalse(rs.next());

		querySPARQL = "select ns:Size from ns:HUDSON USING NAMESPACE ns='http://lias.ensma.fr/' " + "union"
				+ " select ns:Size from ns:MUFFS USING NAMESPACE ns='http://lias.ensma.fr/'";
		rs = statement.executeQuery(querySPARQL);
		rs.next();
		Assert.assertNull("", rs.getString(1));
		rs.next();
		Assert.assertEquals("L", rs.getString(1));
		rs.next();
		Assert.assertEquals("Taille unique", rs.getString(1));
		rs.next();
		Assert.assertEquals("XL", rs.getString(1));
		Assert.assertFalse(rs.next());

		querySPARQL = "select ns:Size,NULL,ns:Reference from ns:ONTARIO USING NAMESPACE ns='http://lias.ensma.fr/'"
				+ " union " + "select ns:Size,ns:virage,NULL from ns:HUDSON USING NAMESPACE ns='http://lias.ensma.fr/'";
		rs = statement.executeQuery(querySPARQL);
		List<String> result1 = new ArrayList<String>();
		List<String> result2 = new ArrayList<String>();
		List<String> result3 = new ArrayList<String>();
		rs.next();
		result1.add(rs.getString(1));
		result2.add(rs.getString(2));
		result3.add(rs.getString(3));

		rs.next();
		result1.add(rs.getString(1));
		result2.add(rs.getString(2));
		result3.add(rs.getString(3));

		rs.next();
		result1.add(rs.getString(1));
		result2.add(rs.getString(2));
		result3.add(rs.getString(3));

		rs.next();
		result1.add(rs.getString(1));
		result2.add(rs.getString(2));
		result3.add(rs.getString(3));

		rs.next();
		result1.add(rs.getString(1));
		result2.add(rs.getString(2));
		result3.add(rs.getString(3));

		rs.next();
		result1.add(rs.getString(1));
		result2.add(rs.getString(2));
		result3.add(rs.getString(3));

		Assert.assertFalse(rs.next());

		Assert.assertTrue(result1.contains("L"));
		Assert.assertTrue(result2.contains(null));
		Assert.assertTrue(result3.contains("300030"));

		Assert.assertTrue(result1.contains("L"));
		Assert.assertTrue(result2.contains("3"));
		Assert.assertTrue(result3.contains(null));

		Assert.assertTrue(result1.contains("M"));
		Assert.assertTrue(result2.contains(null));
		Assert.assertTrue(result3.contains("300029"));

		Assert.assertTrue(result1.contains("S"));
		Assert.assertTrue(result2.contains(null));
		Assert.assertTrue(result3.contains("300028"));

		Assert.assertTrue(result1.contains("XL"));
		Assert.assertTrue(result2.contains(null));
		Assert.assertTrue(result3.contains("300031"));

		Assert.assertTrue(result1.contains("XL"));
		Assert.assertTrue(result2.contains("1"));
		Assert.assertTrue(result3.contains(null));

		// two classes with the union operator
		// and different properties
		querySPARQL = "PREFIX ns: <EGALIS_FR> " + "SELECT ?s ?v ?r WHERE { { ?c rdf:type ns:HUDSON . "
				+ " OPTIONAL {?c ns:Size ?s} OPTIONAL {?c ns:virage ?v} } UNION  "
				+ " {?c rdf:type ns:ONTARIO . OPTIONAL {?c ns:Size ?s} OPTIONAL {?c ns:Reference ?r} } }";
		rs = statement.executeSPARQLQuery(querySPARQL);
		result1 = new ArrayList<String>();
		result2 = new ArrayList<String>();
		result3 = new ArrayList<String>();
		rs.next();
		result1.add(rs.getString(1));
		result2.add(rs.getString(2));
		result3.add(rs.getString(3));

		rs.next();
		result1.add(rs.getString(1));
		result2.add(rs.getString(2));
		result3.add(rs.getString(3));

		rs.next();
		result1.add(rs.getString(1));
		result2.add(rs.getString(2));
		result3.add(rs.getString(3));

		rs.next();
		result1.add(rs.getString(1));
		result2.add(rs.getString(2));
		result3.add(rs.getString(3));

		rs.next();
		result1.add(rs.getString(1));
		result2.add(rs.getString(2));
		result3.add(rs.getString(3));

		rs.next();
		result1.add(rs.getString(1));
		result2.add(rs.getString(2));
		result3.add(rs.getString(3));

		Assert.assertFalse(rs.next());

		Assert.assertTrue(result1.contains("L"));
		Assert.assertTrue(result2.contains(null));
		Assert.assertTrue(result3.contains("300030"));

		Assert.assertTrue(result1.contains("L"));
		Assert.assertTrue(result2.contains("3"));
		Assert.assertTrue(result3.contains(null));

		Assert.assertTrue(result1.contains("M"));
		Assert.assertTrue(result2.contains(null));
		Assert.assertTrue(result3.contains("300029"));

		Assert.assertTrue(result1.contains("S"));
		Assert.assertTrue(result2.contains(null));
		Assert.assertTrue(result3.contains("300028"));

		Assert.assertTrue(result1.contains("XL"));
		Assert.assertTrue(result2.contains(null));
		Assert.assertTrue(result3.contains("300031"));

		Assert.assertTrue(result1.contains("XL"));
		Assert.assertTrue(result2.contains("1"));
		Assert.assertTrue(result3.contains(null));

		// One class without Optional behind properties
		querySPARQL = "PREFIX ns: <EGALIS_FR> " + "SELECT ?c ?s WHERE {?c rdf:type ns:MUFFS . " + "?c ns:Size ?s "
				+ "} ORDER BY ASC(?s)";
		rs = statement.executeSPARQLQuery(querySPARQL);
		rs.next();
		Assert.assertEquals("http://389000", rs.getString(1));
		Assert.assertEquals("Taille unique", rs.getString(2));
		rs.next();
		Assert.assertEquals("http://387000", rs.getString(1));
		Assert.assertEquals("Taille unique", rs.getString(2));
		rs.next();
		Assert.assertEquals("http://388800", rs.getString(1));
		Assert.assertEquals("Taille unique", rs.getString(2));
		rs.next();
		Assert.assertFalse(rs.next());

		querySPARQL = "PREFIX ns: <EGALIS_FR> " + "SELECT ?c ?s WHERE {?c rdf:type ns:MUFFS . "
				+ "?c ns:Size ?s FILTER(?c=<http://389000>) " + "} ORDER BY DESC(?s)";
		rs = statement.executeSPARQLQuery(querySPARQL);
		rs.next();
		Assert.assertEquals("http://389000", rs.getString(1));
		Assert.assertEquals("Taille unique", rs.getString(2));
		Assert.assertFalse(rs.next());

		querySPARQL = "PREFIX ns: <EGALIS_FR> " + "SELECT ?c ?s WHERE {?c rdf:type ns:HUDSON "
				+ "OPTIONAL {?c ns:Size ?s } FILTER (?s = 'L')" + "} ORDER BY DESC(?s)";
		rs = statement.executeSPARQLQuery(querySPARQL);
		rs.next();
		Assert.assertEquals("http://300061", rs.getString(1));
		Assert.assertEquals("L", rs.getString(2));
		Assert.assertFalse(rs.next());

		querySPARQL = "PREFIX ns: <EGALIS_FR> " + "SELECT ?c ?s WHERE {?c rdf:type ns:HUDSON "
				+ "OPTIONAL {?c ns:Size ?s } FILTER (?s = 'L' && ?c=<http://300061>)" + "} ORDER BY DESC(?s)";
		rs = statement.executeSPARQLQuery(querySPARQL);
		rs.next();
		Assert.assertEquals("http://300061", rs.getString(1));
		Assert.assertEquals("L", rs.getString(2));
		Assert.assertFalse(rs.next());

		querySPARQL = "PREFIX ns: <EGALIS_FR> " + "SELECT ?c ?s WHERE {?c rdf:type ns:HUDSON "
				+ "OPTIONAL {?c ns:Size ?s } FILTER (?s = 'L' || ?s='XL')" + "} ORDER BY DESC(?s)";
		rs = statement.executeSPARQLQuery(querySPARQL);
		rs.next();
		Assert.assertEquals("http://300062", rs.getString(1));
		Assert.assertEquals("XL", rs.getString(2));
		rs.next();
		Assert.assertEquals("http://300061", rs.getString(1));
		Assert.assertEquals("L", rs.getString(2));
		Assert.assertFalse(rs.next());

		querySPARQL = "PREFIX ns: <EGALIS_FR> " + "SELECT ?c ?s WHERE {?c rdf:type ns:MUFFS "
				+ "OPTIONAL {?c ns:Size ?s } FILTER (bound(?s))" + "} ORDER BY DESC(?s)";
		rs = statement.executeSPARQLQuery(querySPARQL);
		rs.next();
		Assert.assertEquals("http://389000", rs.getString(1));
		Assert.assertEquals("Taille unique", rs.getString(2));
		rs.next();
		rs.next();
		Assert.assertFalse(rs.next());

		querySPARQL = "PREFIX ns: <EGALIS_FR> " + "SELECT ?c ?s WHERE {?c rdf:type ns:MUFFS "
				+ "OPTIONAL {?c ns:Size ?s } FILTER (!bound(?s))" + "} ORDER BY DESC(?s)";
		rs = statement.executeSPARQLQuery(querySPARQL);
		rs.next();
		Assert.assertEquals("http://", rs.getString(1));
		Assert.assertNull(rs.getString(2));
		Assert.assertFalse(rs.next());

		querySPARQL = "PREFIX ns: <EGALIS_FR> " + "SELECT ?c ?v WHERE {?c rdf:type ns:HUDSON "
				+ "OPTIONAL {?c ns:virage ?v} FILTER (?v > 2)" + "} ORDER BY DESC(?v)";
		rs = statement.executeSPARQLQuery(querySPARQL);
		rs.next();
		Assert.assertEquals("http://300061", rs.getString(1));
		Assert.assertEquals("3", rs.getString(2));
		Assert.assertFalse(rs.next());

		querySPARQL = "PREFIX ns: <EGALIS_FR> " + "SELECT ?c ?v WHERE {?c rdf:type ns:HUDSON "
				+ "OPTIONAL {?c ns:virage ?v} FILTER (?v < 2)" + "} ORDER BY DESC(?v)";
		rs = statement.executeSPARQLQuery(querySPARQL);
		rs.next();
		Assert.assertEquals("http://300062", rs.getString(1));
		Assert.assertEquals("1", rs.getString(2));
		Assert.assertFalse(rs.next());

		querySPARQL = "PREFIX ns: <EGALIS_FR> " + "SELECT ?c ?v WHERE {?c rdf:type ns:HUDSON "
				+ "OPTIONAL {?c ns:virage ?v } FILTER (?v != 3)" + "} ORDER BY DESC(?v)";
		rs = statement.executeSPARQLQuery(querySPARQL);
		rs.next();
		Assert.assertEquals("http://300062", rs.getString(1));
		Assert.assertEquals("1", rs.getString(2));
		Assert.assertFalse(rs.next());

		querySPARQL = "PREFIX ns: <EGALIS_FR> " + "SELECT ?c ?v WHERE {?c rdf:type ns:HUDSON "
				+ "OPTIONAL {?c ns:virage ?v} FILTER (?v > 2)" + "} ORDER BY DESC(?v)";
		rs = statement.executeSPARQLQuery(querySPARQL);
		Assert.assertTrue(rs.next());
		Assert.assertEquals("http://300061", rs.getString(1));
		Assert.assertEquals("3", rs.getString(2));
		Assert.assertFalse(rs.next());

		// we put a NULL value
		String ontoQLInsert = "insert into HUDSON (Reference) values ('33')";
		statement.executeUpdate(ontoQLInsert);
		String ontoQL = "SELECT DISTINCT COALESCE(o.Size, h.Size), o.Size  from ns:HUDSON h JOIN ns:ONTARIO o ON (o.Size=h.Size OR o.Size IS NULL OR h.Size IS NULL) order by o.Size desc USING namespace ns='http://lias.ensma.fr/' ";
		rs = statement.executeQuery(ontoQL);
		//
		rs.next();
		Assert.assertEquals("XL", rs.getString(1));
		rs.next();
		Assert.assertEquals("S", rs.getString(1));
		rs.next();
		Assert.assertEquals("M", rs.getString(1));
		rs.next();
		Assert.assertEquals("L", rs.getString(1));
		Assert.assertFalse(rs.next());

		querySPARQL = "PREFIX ns: <EGALIS_FR> " + "SELECT ?s WHERE {?c rdf:type ns:HUDSON "
				+ "OPTIONAL {?c ns:Size ?s } ?o rdf:type ns:ONTARIO " + "OPTIONAL {?o ns:Size ?s } "
				+ "} ORDER BY DESC(?s)";
		rs = statement.executeSPARQLQuery(querySPARQL);
		rs.next();
		Assert.assertEquals("XL", rs.getString(1));
		rs.next();
		Assert.assertEquals("S", rs.getString(1));
		rs.next();
		Assert.assertEquals("M", rs.getString(1));
		rs.next();
		Assert.assertEquals("L", rs.getString(1));
		Assert.assertFalse(rs.next());

		ontoQLInsert = "insert into HUDSON (Size) values ('XXL')";
		statement.executeUpdate(ontoQLInsert);
		querySPARQL = "PREFIX ns: <EGALIS_FR> " + "SELECT ?s WHERE {?c rdf:type ns:HUDSON "
				+ "OPTIONAL {?c ns:Size ?s } OPTIONAL { ?o rdf:type ns:ONTARIO " + "OPTIONAL {?o ns:Size ?s } } "
				+ "} ORDER BY DESC(?s)";

		rs = statement.executeSPARQLQuery(querySPARQL);
		rs.next();
		Assert.assertEquals("XXL", rs.getString(1));
		rs.next();
		Assert.assertEquals("XL", rs.getString(1));
		rs.next();
		Assert.assertEquals("S", rs.getString(1));
		rs.next();
		Assert.assertEquals("M", rs.getString(1));
		rs.next();
		Assert.assertEquals("L", rs.getString(1));
		Assert.assertFalse(rs.next());

		ontoQLInsert = "insert into HUDSON (Size) values ('XL')";
		statement.executeUpdate(ontoQLInsert);
		querySPARQL = "PREFIX ns: <EGALIS_FR> " + "SELECT ?s ?r WHERE {?c rdf:type ns:HUDSON "
				+ "OPTIONAL {?c ns:Size ?s } OPTIONAL {?c ns:Reference ?r } " + " ?o rdf:type ns:ONTARIO "
				+ "OPTIONAL {?o ns:Size ?s } OPTIONAL {?o ns:Reference ?r }  " + "} ORDER BY DESC(?s)";
		resultSetObtenu = statement.executeSPARQLQuery(querySPARQL);
		resultSetObtenu.next();
		Assert.assertEquals("XL", resultSetObtenu.getString(1));
		Assert.assertEquals("300031", resultSetObtenu.getString(2));
		Assert.assertTrue(resultSetObtenu.next());
		Assert.assertEquals("L", resultSetObtenu.getString(1));
		Assert.assertEquals("300030", resultSetObtenu.getString(2));
		Assert.assertFalse(resultSetObtenu.next());
	}

	/**
	 * Test the translation of SPARQL content-query into MariusQL content-query
	 */
	// TODO: fix
	public void testQueryOnOntology() throws SQLException {
		this.getSession().setReferenceLanguage(MariusQLConstants.ENGLISH);
		this.getSession().setDefaultNameSpace("http://www.lias.ensma.fr");
		statement = this.getSession().createMariusQLStatement();

		String querySPARQL = "SELECT ?p1name ?p1def ?p2name WHERE {?p1 rdf:type rdfs:Class . "
				+ "  ?p1 rdfs:label ?p1name . OPTIONAL {?p1 rdfs:comment ?p1def}  FILTER (?p1name = 'HUDSON') OPTIONAL { "
				+ "  ?p2 rdf:type rdfs:Property . ?p2 rdfs:label ?p2name . ?p2 rdf:domain ?p1 . OPTIONAL {?p2 rdfs:comment ?p1def}  } }";
		resultSetObtenu = statement.executeSPARQLQuery(querySPARQL);
		List<String> propertyNames = new ArrayList<String>();
		Assert.assertTrue(resultSetObtenu.next());
		Assert.assertEquals("HUDSON", resultSetObtenu.getString(1));
		Assert.assertEquals(
				"High performance dry cag. Nylon heavy resistance fabric. Taped seams. Inner twin waist. Neoprene belt.",
				resultSetObtenu.getString(2));
		propertyNames.add(resultSetObtenu.getString(3));
		Assert.assertTrue(resultSetObtenu.next());
		propertyNames.add(resultSetObtenu.getString(3));
		Assert.assertTrue(resultSetObtenu.next());
		propertyNames.add(resultSetObtenu.getString(3));
		Assert.assertTrue(resultSetObtenu.next());
		propertyNames.add(resultSetObtenu.getString(3));

		Assert.assertTrue(propertyNames.contains("Reference"));
		Assert.assertTrue(propertyNames.contains("Virage"));
		Assert.assertTrue(propertyNames.contains("uri"));
		Assert.assertTrue(propertyNames.contains("Size"));

		querySPARQL = "SELECT ?namecsup WHERE {?c rdf:type rdfs:Class "
				+ "  OPTIONAL {?c rdfs:subClassOf ?csup} FILTER (?c = <EGALIS2>) "
				+ " ?csup rdf:type rdfs:Class OPTIONAL {?csup rdfs:label ?namecsup} } ORDER BY asc(?namecsup)";
		resultSetObtenu = statement.executeSPARQLQuery(querySPARQL);
		Assert.assertTrue(resultSetObtenu.next());
		Assert.assertEquals("CAGS", resultSetObtenu.getString(1));
		Assert.assertFalse(resultSetObtenu.next());

		querySPARQL = "SELECT ?csup WHERE {?c rdf:type rdfs:Class "
				+ " OPTIONAL {?c rdfs:label ?l} OPTIONAL {?c rdfs:subClassOf ?csup} FILTER (?c = <EGALIS2>) }";

		resultSetObtenu = statement.executeQuery("select #superclass from #class where #code = 'EGALIS2'");
		Assert.assertTrue(resultSetObtenu.next());
		String ridSuperClass = resultSetObtenu.getString(1);

		resultSetObtenu = statement.executeSPARQLQuery(querySPARQL);

		Assert.assertTrue(resultSetObtenu.next());
		Assert.assertEquals(ridSuperClass, resultSetObtenu.getString(1));
		Assert.assertFalse(resultSetObtenu.next());

		// two entities with the union operator
		// and the same attributes
		querySPARQL = "SELECT ?n WHERE { { ?c rdf:type rdfs:Class . " + " ?c rdfs:label ?n } UNION  "
				+ " {?c rdf:type rdfs:Property . ?c rdfs:label ?n } }";
		resultSetObtenu = statement.executeSPARQLQuery(querySPARQL);
		List<String> values = new ArrayList<String>();

		Assert.assertTrue(resultSetObtenu.next());
		values.add(resultSetObtenu.getString(1));
		Assert.assertTrue(resultSetObtenu.next());
		values.add(resultSetObtenu.getString(1));
		Assert.assertTrue(resultSetObtenu.next());
		values.add(resultSetObtenu.getString(1));

		values.contains("");
		values.contains("3 LITERS PLASTIC PUMP");
		values.contains("3mm NEOPRENE SPRAY DECK");

		querySPARQL = "SELECT ?l WHERE {?c rdf:type rdfs:Class " + " OPTIONAL {?c rdfs:label ?l} }"
				+ " ORDER BY DESC(?l)";
		resultSetObtenu = statement.executeSPARQLQuery(querySPARQL);
		Assert.assertTrue(resultSetObtenu.next());
		Assert.assertEquals("ZICRAL_TUBE_DIAMETER_30", resultSetObtenu.getString(1));
		Assert.assertTrue(resultSetObtenu.next());
		Assert.assertEquals("ZICRAL_TUBE_DIAMETER_28", resultSetObtenu.getString(1));
		// Definition is not defined
		querySPARQL = "SELECT ?comment WHERE {?c rdf:type rdfs:Class "
				+ " OPTIONAL {?c rdfs:comment ?comment}  FILTER (bound(?comment)) }" + " ORDER BY DESC(?comment)";
		resultSetObtenu = statement.executeSPARQLQuery(querySPARQL);
		Assert.assertTrue(resultSetObtenu.next());
		Assert.assertEquals(
				"Rowing buoyancy aid. Chasuble buoyancy aid. 120g fabric. Alvéo foam. Adjustable side straps (for size M only). Double stitching. Adjustable leg strap.",
				resultSetObtenu.getString(1));
		Assert.assertTrue(resultSetObtenu.next());
		Assert.assertEquals(
				"High performance dry cag. Nylon heavy resistance fabric. Taped seams. Inner twin waist. Neoprene belt.",
				resultSetObtenu.getString(1));

		querySPARQL = "SELECT ?l WHERE {?c rdf:type rdfs:Class "
				+ " OPTIONAL {?c rdfs:label ?l} FILTER (?l > 'ZICRAL_TUBE_DIAMETER_28') }" + " ORDER BY DESC(?l)";
		resultSetObtenu = statement.executeSPARQLQuery(querySPARQL);
		Assert.assertTrue(resultSetObtenu.next());
		Assert.assertEquals("ZICRAL_TUBE_DIAMETER_30", resultSetObtenu.getString(1));
		Assert.assertFalse(resultSetObtenu.next());

		querySPARQL = "SELECT ?l WHERE {?c rdf:type rdfs:Class "
				+ " OPTIONAL {?c rdfs:label ?l} FILTER (?l = 'ZICRAL_TUBE_DIAMETER_28' || ?l = 'ZICRAL_TUBE_DIAMETER_30') }"
				+ " ORDER BY DESC(?l)";
		resultSetObtenu = statement.executeSPARQLQuery(querySPARQL);
		Assert.assertTrue(resultSetObtenu.next());
		Assert.assertEquals("ZICRAL_TUBE_DIAMETER_30", resultSetObtenu.getString(1));
		Assert.assertTrue(resultSetObtenu.next());
		Assert.assertEquals("ZICRAL_TUBE_DIAMETER_28", resultSetObtenu.getString(1));
		Assert.assertFalse(resultSetObtenu.next());

		querySPARQL = "SELECT ?l WHERE {?c rdf:type rdfs:Class " + " OPTIONAL {?c rdfs:label ?l} FILTER (?l = 'CAGS') }"
				+ " ORDER BY DESC(?l)";
		resultSetObtenu = statement.executeSPARQLQuery(querySPARQL);
		Assert.assertTrue(resultSetObtenu.next());
		Assert.assertEquals("CAGS", resultSetObtenu.getString(1));

		querySPARQL = "SELECT ?l WHERE {?c rdf:type rdfs:Class "
				+ " OPTIONAL {?c rdfs:comment ?l} OPTIONAL {?c rdfs:label ?lbel} FILTER (?lbel='CHARENTE' && bound(?l)) }"
				+ " ORDER BY DESC(?l)";
		resultSetObtenu = statement.executeSPARQLQuery(querySPARQL);
		Assert.assertTrue(resultSetObtenu.next());
		Assert.assertEquals(
				"Rowing buoyancy aid. Chasuble buoyancy aid. 120g fabric. Alvéo foam. Adjustable side straps (for size M only). Double stitching. Adjustable leg strap.",
				resultSetObtenu.getString(1));

		querySPARQL = "SELECT ?csup WHERE {?c rdf:type rdfs:Class "
				+ " OPTIONAL {?c rdfs:label ?l} OPTIONAL {?c rdfs:subClassOf ?csup} FILTER (?l = 'ONTARIO') }";

		resultSetObtenu = statement.executeQuery("select #superclass from #class where #code = 'ONTARIO'");
		Assert.assertTrue(resultSetObtenu.next());
		ridSuperClass = resultSetObtenu.getString(1);

		resultSetObtenu = statement.executeSPARQLQuery(querySPARQL);
		Assert.assertTrue(resultSetObtenu.next());
		Assert.assertEquals(ridSuperClass, resultSetObtenu.getString(1));
		Assert.assertFalse(resultSetObtenu.next());

		querySPARQL = "SELECT ?p1name ?p1def ?p2name WHERE {?p1 rdf:type rdfs:Property . "
				+ "  ?p1 rdfs:label ?p1name . OPTIONAL {?p1 rdfs:comment ?p1def}  FILTER (?p1name = 'Size') . "
				+ "  ?p2 rdf:type rdfs:Property . ?p2 rdfs:label ?p2name . OPTIONAL {?p2 rdfs:comment ?p1def}  FILTER (?p2name = 'Reference') }";
		resultSetObtenu = statement.executeSPARQLQuery(querySPARQL);
		List<String> result1 = new ArrayList<String>();
		List<String> result2 = new ArrayList<String>();
		List<String> result3 = new ArrayList<String>();

		Assert.assertTrue(resultSetObtenu.next());
		result1.add(resultSetObtenu.getString(1));
		result2.add(resultSetObtenu.getString(2));
		result3.add(resultSetObtenu.getString(3));

		Assert.assertTrue(resultSetObtenu.next());
		result1.add(resultSetObtenu.getString(1));
		result2.add(resultSetObtenu.getString(2));
		result3.add(resultSetObtenu.getString(3));

		Assert.assertFalse(resultSetObtenu.next());

		Assert.assertTrue(result1.contains("Size"));
		Assert.assertTrue(result2.contains(null));
		Assert.assertTrue(result3.contains("Reference"));

		querySPARQL = "SELECT ?l WHERE {?c rdf:type rdfs:Class " + " OPTIONAL {?c rdfs:label ?l} FILTER (?l = 'CAGS') }"
				+ " ORDER BY DESC(?l)";
		resultSetObtenu = statement.executeSPARQLQuery(querySPARQL);
		Assert.assertTrue(resultSetObtenu.next());
		Assert.assertEquals("CAGS", resultSetObtenu.getString(1));
		querySPARQL = "SELECT ?domain ?range WHERE {?p rdf:type rdfs:Property "
				+ " OPTIONAL {?p rdfs:label ?name} OPTIONAL {?p rdfs:domain ?domain} OPTIONAL {?p rdfs:range ?range} FILTER (?name = 'Size') }";
		resultSetObtenu = statement.executeSPARQLQuery(querySPARQL);
		Assert.assertTrue(resultSetObtenu.next());
		// Must be corrected (the URI must be retrieved)
		Assert.assertNotNull(resultSetObtenu.getString(1));
		Assert.assertNotNull(resultSetObtenu.getString(2));

		querySPARQL = "SELECT ?domain ?range WHERE {?p rdf:type rdfs:Property "
				+ " OPTIONAL {?p rdfs:label ?name} OPTIONAL {?p rdfs:domain ?domain} OPTIONAL {?p rdfs:range ?range} FILTER (?name = 'Size') }";
		resultSetObtenu = statement.executeSPARQLQuery(querySPARQL);
		Assert.assertTrue(resultSetObtenu.next());
		Assert.assertNotNull(resultSetObtenu.getString(1));
		Assert.assertNotNull(resultSetObtenu.getString(2));

		querySPARQL = "SELECT ?comment WHERE {?p rdf:type rdfs:Property "
				+ " OPTIONAL {?p rdfs:label ?name} OPTIONAL {?p rdfs:comment ?comment} FILTER (?name = 'Size') }";
		resultSetObtenu = statement.executeSPARQLQuery(querySPARQL);
		result1 = new ArrayList<String>();
		Assert.assertTrue(resultSetObtenu.next());
		result1.add(resultSetObtenu.getString(1));
		Assert.assertTrue(resultSetObtenu.next());
		result1.add(resultSetObtenu.getString(1));
		Assert.assertTrue(result1.contains(
				"High performance dry cag. Nylon heavy resistance fabric. Taped seams. Inner twin waist. Neoprene belt."));

		querySPARQL = "SELECT ?name ?comment WHERE {?p rdf:type rdfs:Property . "
				+ " ?p rdfs:label ?name . ?p rdfs:comment ?comment  FILTER (?name = 'Size') }";
		resultSetObtenu = statement.executeSPARQLQuery(querySPARQL);
		Assert.assertTrue(resultSetObtenu.next());
		Assert.assertEquals("Size", resultSetObtenu.getString(1));
		Assert.assertEquals(
				"High performance dry cag. Nylon heavy resistance fabric. Taped seams. Inner twin waist. Neoprene belt.",
				resultSetObtenu.getString(2));
		Assert.assertFalse(resultSetObtenu.next());
	}

	@After
	public void closeTransaction() {
		this.getSession().rollback();
		statement.close();
	}
}
