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
package fr.ensma.lias.mariusql.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mickael BARON
 */
public class StringHelperTest {
	@Test
	public void testRemoveFirstAndLastletter() {
		String removeFirstAndLastletter = StringHelper.removeFirstAndLastletter("'class'");
		Assert.assertEquals("class", removeFirstAndLastletter);
	}

	@Test
	public void testFormatSQL() {
		String formatSQL = StringHelper.formatSQL("select #code from #class");
		Assert.assertEquals("select #code \nfrom #class", formatSQL);
	}

	@Test
	public void testFirstLetterInUpperCase() {
		String firstLetterInUpperCase = StringHelper.firstLetterInUpperCase("class");
		Assert.assertEquals("Class", firstLetterInUpperCase);
	}

	@Test
	public void testLastIndexOfLetter() {
		int lastIndexOfLetter = StringHelper.lastIndexOfLetter("ceci est un texte");
		Assert.assertEquals(3, lastIndexOfLetter);
	}

	@Test
	public void testJoinWithTab() {
		String join = StringHelper.join("/", new String[] { "ceci", "est", "un", "texte" });
		Assert.assertEquals("ceci/est/un/texte", join);
	}

	@Test
	public void testJoinWithIterator() {
		List<String> listString = new ArrayList<String>();
		listString.add("ceci");
		listString.add("est");
		listString.add("un");
		listString.add("texte");

		Assert.assertEquals("ceci/est/un/texte", StringHelper.join("/", listString.iterator()));
	}

	@Test
	public void testAdd() {
		String[] firstTab = { "ceci", "est", "un", "texte", "a", "gauche" };
		String[] secondTab = { "hello", "the", "world", "texte", "a", "droite" };
		String[] add = StringHelper.add(firstTab, "/", secondTab);

		for (int i = 0; i < firstTab.length; i++) {
			Assert.assertEquals(firstTab[i] + "/" + secondTab[i], add[i]);
		}
	}

	@Test
	public void testRepeat() {
		String repeat = StringHelper.repeat("hello", 3);
		Assert.assertEquals("hellohellohello", repeat);
	}

	@Test
	public void testReplace() {
		String replace = StringHelper.replace("bonjour je me remplace mais pas le reste du message", "je me remplace",
				"tu as été remplacé");
		Assert.assertEquals("bonjour tu as été remplacé mais pas le reste du message", replace);

		String[] replace2 = StringHelper.replace(new String[] { "bonjour je me remplace mais pas le reste du message",
				"bonjour je me remplace également et moi aussi je me remplace également mais pas le reste du message" },
				"je me remplace", "tu as été remplacé");
		Assert.assertEquals(2, replace2.length);
		Assert.assertEquals("bonjour tu as été remplacé mais pas le reste du message", replace2[0]);
		Assert.assertEquals(
				"bonjour tu as été remplacé également et moi aussi tu as été remplacé également mais pas le reste du message",
				replace2[1]);
	}

	@Test
	public void testReplaceOnce() {
		String replaceOnce = StringHelper.replaceOnce(
				"bonjour je me remplace et moi aussi je me remplace mais pas le reste du message", "je me remplace",
				"tu as été remplacé");

		Assert.assertEquals("bonjour tu as été remplacé et moi aussi je me remplace mais pas le reste du message",
				replaceOnce);
	}
}
