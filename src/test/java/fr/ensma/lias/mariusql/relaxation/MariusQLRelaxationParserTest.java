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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;
import fr.ensma.lias.mariusql.AbstractMariusQLTest;
import fr.ensma.lias.mariusql.engine.MariusQLParser;
import fr.ensma.lias.mariusql.engine.util.ASTPrinter;

/**
 * @author Geraud FOKOU
 * @author Mickael BARON
 */
public class MariusQLRelaxationParserTest extends AbstractMariusQLTest {

	private Logger log = LoggerFactory.getLogger(MariusQLRelaxationParserTest.class);

	private void doParse(String input) throws RecognitionException, TokenStreamException {

		System.out.println("input -> " + ASTPrinter.escapeMultibyteChars(input) + "<-");
		MariusQLParser parser = MariusQLParser.getInstance(input);
		parser.statement();
		AST ast = parser.getAST();
		System.out.println("AST  ->  " + ast.toStringTree() + "");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		parser.showAst(ast, new PrintStream(baos));
		System.out.println(baos.toString());
		Assert.assertEquals("At least one error occurred during parsing!", 0,
				parser.getParseErrorHandler().getErrorCount());
	}

	private void parse(String input) throws RecognitionException, TokenStreamException {
		doParse(input);
	}

	@Test
	public void predicatRelaxationQuerySyntaxTest() throws Exception {
		log.debug("MariusQLRelaxationParserTest.predicatRelaxationQuerySyntaxTest()");

		parse("SELECT p.address.street FROM Person p ORDER BY p.address.street ASC APPROX PRED(street) ");
		parse("SELECT p.address.street FROM Person p WHERE p.address.street=25 APPROX PRED(street) TOP 15");
		parse("SELECT p.address.street FROM Person p WHERE p.address.street=25 APPROX PRED(p.address.street) TOP 15");
		parse("SELECT c.#name[en], c.#name[fr], c.#code, c.#definition[en], c.#definition[fr] FROM #class c WHERE c.#rid = (SELECT MAX(c1.#rid) FROM #class c1) APPROX PRED(c.#rid)");
		parse("SELECT computeAvg(marks) FROM Student WHERE marks.math>15 APPROX PRED(marks.math) USING IMPLEMENTATION externalcalculMoyenne->computeAvg");
	}

	@Test
	public void verticalRelaxationQuerySyntaxTest() throws Exception {
		log.debug("MariusQLRelaxationParserTest.verticalRelaxationQuerySyntaxTest()");

		parse("SELECT rid, nom, prenom, age, adresse, matricule FROM Etudiant APPROX GEN(Etudiant,Personne,2) TOP 15");
		parse("SELECT rid, nom, prenom, age, adresse, matricule FROM Ouvrier  APPROX GEN(Ouvrier, Salarie, 2) TOP 15");
		parse("SELECT rid, nom, prenom, age, adresse, matricule FROM Ouvrier  APPROX GEN(Ouvrier, Personne, 1) TOP 15");
		parse("SELECT rid, nom, prenom, age, adresse, matricule FROM Ouvrier  APPROX GEN(Ouvrier,2)");
	}

	@Test
	public void horizontalRelaxationQuerySyntaxTest() throws Exception {
		log.debug("MariusQLRelaxationParserTest.horizontalRelaxationQuerySyntaxTest()");

		parse("SELECT rid, nom, prenom, age, adresse, matricule FROM Etudiant APPROX SIB(Etudiant, [Salarie]) TOP 15");
		parse("SELECT rid, nom, prenom, age, adresse, matricule FROM Etudiant APPROX SIB(Etudiant, [Salarie, Ouvrier])");
		parse("SELECT rid, nom, prenom, age, adresse, matricule FROM Etudiant APPROX SIB(Etudiant)");
	}

	@Test
	public void gmpVerticalQueryRelaxationTest() throws Exception, TokenStreamException {
		log.debug("MariusQLRelaxationParserTest.gmpVerticalQueryRelaxationTest()");

		parse("SELECT name, nbstart, price, cityname FROM BedAndBreakfast B WHERE B.price<= 29 AND B.nbstart=5 AND B.cityname='Belgrade' APPROX GEN(BedAndBreakfast)");
		parse("SELECT name, nbstart, price, cityname FROM Inn I WHERE I.price<= 15 AND I.nbstart=5 AND I.cityname='Shanghai' APPROX GEN(Inn)");
		parse("SELECT name, nbstart, price, cityname FROM Motel M WHERE M.price<= 80 AND M.nbstart=5 AND M.cityname='Istanbul' APPROX GEN(Motel)");
		parse("SELECT name, nbstart, price, statename FROM Resort R WHERE R.price<= 85 AND R.nbstart=5 AND R.statename='California' APPROX GEN(Resort)");
	}

	@Test
	public void gmpHorizontalQueryRelaxationTest() throws Exception, TokenStreamException {
		log.debug("MariusQLRelaxationParserTest.gmpHorizontalQueryRelaxationTest()");

		parse("SELECT name, nbstart, price, cityname FROM Motel M WHERE M.nbstart<=4 AND M.cityname='Istanbul' APPROX SIB(Motel,[BedAndBreakfast])");
		parse("SELECT name, nbstart, price, cityname FROM Resort R WHERE R.nbstart>=8 AND R.cityname='Istanbul' APPROX SIB(Resort,[BedAndBreakfast])");
		parse("SELECT name, nbstart, price, cityname FROM Rental R WHERE R.nbstart=4  AND R.cityname='Istanbul' AND price<90 APPROX SIB(Resort,[Appartement])");
	}

	@Test
	public void fuzzyPredicatTest() throws RecognitionException, TokenStreamException {
		log.debug("MariusQLRelaxationParserTest.fuzzyPredicatTest()");

		parse("SELECT name, nbstart, price, cityname FROM Motel M WHERE M.nbstart<=4 AND fuzzy(M.price,[500,600,2,3])>0");
		parse("SELECT name, nbstart, price, cityname FROM Motel M WHERE M.nbstart<=4 AND fuzzy(M.price,[500,600,2,3])>=0");
		parse("SELECT name, nbstart, price, cityname FROM Motel M WHERE M.nbstart<=4 AND 0<=fuzzy(M.price,[500,600,2,3])");
		parse("SELECT name, nbstart, price, cityname FROM Motel M WHERE M.nbstart<=4 AND 0<fuzzy(M.price,[500,600,2,3])");
	}
}
