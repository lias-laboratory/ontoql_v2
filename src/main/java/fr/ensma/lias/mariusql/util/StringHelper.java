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

import java.util.Iterator;
import java.util.StringTokenizer;

import fr.ensma.lias.mariusql.MariusQLConstants;

/**
 * @author Mickael BARON
 * @author Stéphane JEAN
 */
public final class StringHelper {

	private static final int ALIAS_TRUNCATE_LENGTH = 10;

	private StringHelper() {
	}

	/**
	 * @param inputSQL
	 * @return
	 */
	public static String formatSQL(String inputSQL) {

		String res = inputSQL;
		res = res.replaceAll("from", "\nfrom");
		res = res.replaceAll("union all ", "\nunion all \n");
		return res;
	}

	/**
	 * @param value
	 * @return
	 */
	public static String removeFirstAndLastletter(String value) {
		return value.substring(1, value.length() - 1);
	}

	/**
	 * Put the first letter of a string in uppercase.
	 * 
	 * @param name
	 * @return
	 */
	public static String firstLetterInUpperCase(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
	}

	/**
	 * @param string
	 * @return
	 */
	public static int lastIndexOfLetter(String string) {
		for (int i = 0; i < string.length(); i++) {
			char character = string.charAt(i);
			if (!Character.isLetter(character) /* && !('_'==character) */)
				return i - 1;
		}
		return string.length() - 1;
	}

	/**
	 * @param seperator
	 * @param strings
	 * @return
	 */
	public static String join(String seperator, String[] strings) {
		int length = strings.length;
		if (length == 0)
			return "";
		StringBuilder buf = new StringBuilder(length * strings[0].length()).append(strings[0]);
		for (int i = 1; i < length; i++) {
			buf.append(seperator).append(strings[i]);
		}
		return buf.toString();
	}

	/**
	 * @param seperator
	 * @param objects
	 * @return
	 */
	public static String join(String seperator, Iterator<?> objects) {
		StringBuilder buf = new StringBuilder();

		if (objects.hasNext())
			buf.append(objects.next());

		while (objects.hasNext()) {
			buf.append(seperator).append(objects.next());
		}
		return buf.toString();
	}

	/**
	 * @param x
	 * @param sep
	 * @param y
	 * @return
	 */
	public static String[] add(String[] x, String sep, String[] y) {
		String[] result = new String[x.length];
		for (int i = 0; i < x.length; i++) {
			result[i] = x[i] + sep + y[i];
		}
		return result;
	}

	/**
	 * @param string
	 * @param times
	 * @return
	 */
	public static String repeat(String string, int times) {
		StringBuilder buf = new StringBuilder(string.length() * times);
		for (int i = 0; i < times; i++)
			buf.append(string);
		return buf.toString();
	}

	/**
	 * @param template
	 * @param placeholder
	 * @param replacement
	 * @return
	 */
	public static String replace(String template, String placeholder, String replacement) {
		return replace(template, placeholder, replacement, false);
	}

	/**
	 * @param templates
	 * @param placeholder
	 * @param replacement
	 * @return
	 */
	public static String[] replace(String templates[], String placeholder, String replacement) {
		String[] result = new String[templates.length];
		for (int i = 0; i < templates.length; i++) {
			result[i] = replace(templates[i], placeholder, replacement);
			;
		}
		return result;
	}

	/**
	 * @param template
	 * @param placeholder
	 * @param replacement
	 * @param wholeWords
	 * @return
	 */
	public static String replace(String template, String placeholder, String replacement, boolean wholeWords) {
		int loc = template.indexOf(placeholder);
		if (loc < 0) {
			return template;
		} else {
			final boolean actuallyReplace = !wholeWords || loc + placeholder.length() == template.length()
					|| !Character.isJavaIdentifierPart(template.charAt(loc + placeholder.length()));
			String actualReplacement = actuallyReplace ? replacement : placeholder;
			return new StringBuilder(template.substring(0, loc)).append(actualReplacement).append(
					replace(template.substring(loc + placeholder.length()), placeholder, replacement, wholeWords))
					.toString();
		}
	}

	/**
	 * @param template
	 * @param placeholder
	 * @param replacement
	 * @return
	 */
	public static String replaceOnce(String template, String placeholder, String replacement) {
		int loc = template.indexOf(placeholder);
		if (loc < 0) {
			return template;
		} else {
			return new StringBuilder(template.substring(0, loc)).append(replacement)
					.append(template.substring(loc + placeholder.length())).toString();
		}
	}

	/**
	 * @param seperators
	 * @param list
	 * @return
	 */
	public static String[] split(String seperators, String list) {
		return split(seperators, list, false);
	}

	/**
	 * @param seperators
	 * @param list
	 * @param include
	 * @return
	 */
	public static String[] split(String seperators, String list, boolean include) {
		StringTokenizer tokens = new StringTokenizer(list, seperators, include);
		String[] result = new String[tokens.countTokens()];
		int i = 0;
		while (tokens.hasMoreTokens()) {
			result[i++] = tokens.nextToken();
		}
		return result;
	}

	/**
	 * @param qualifiedName
	 * @return
	 */
	public static String unqualify(String qualifiedName) {
		return qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1);
	}

	/**
	 * @param qualifiedName
	 * @return
	 */
	public static String qualifier(String qualifiedName) {
		int loc = qualifiedName.lastIndexOf(".");
		return (loc < 0) ? "" : qualifiedName.substring(0, loc);
	}

	/**
	 * @param columns
	 * @param suffix
	 * @return
	 */
	public static String[] suffix(String[] columns, String suffix) {
		if (suffix == null)
			return columns;
		String[] qualified = new String[columns.length];
		for (int i = 0; i < columns.length; i++) {
			qualified[i] = suffix(columns[i], suffix);
		}
		return qualified;
	}

	/**
	 * @param name
	 * @param suffix
	 * @return
	 */
	private static String suffix(String name, String suffix) {
		return (suffix == null) ? name : name + suffix;
	}

	/**
	 * @param qualifiedName
	 * @return
	 */
	public static String root(String qualifiedName) {
		int loc = qualifiedName.indexOf(".");
		return (loc < 0) ? qualifiedName : qualifiedName.substring(0, loc);
	}

	/**
	 * @param qualifiedName
	 * @return
	 */
	public static String unroot(String qualifiedName) {
		int loc = qualifiedName.indexOf(".");
		return (loc < 0) ? qualifiedName : qualifiedName.substring(loc + 1, qualifiedName.length());
	}

	/**
	 * @param tfString
	 * @return
	 */
	public static boolean booleanValue(String tfString) {
		String trimmed = tfString.trim().toLowerCase();
		return trimmed.equals("true") || trimmed.equals("t");
	}

	/**
	 * @param array
	 * @return
	 */
	public static String toString(Object[] array) {
		int len = array.length;
		if (len == 0)
			return "";
		StringBuilder buf = new StringBuilder(len * 12);
		for (int i = 0; i < len - 1; i++) {
			buf.append(array[i]).append(", ");
		}
		return buf.append(array[len - 1]).toString();
	}

	/**
	 * @param string
	 * @param placeholders
	 * @param replacements
	 * @return
	 */
	public static String[] multiply(String string, Iterator<?> placeholders, Iterator<?> replacements) {
		String[] result = new String[] { string };
		while (placeholders.hasNext()) {
			result = multiply(result, (String) placeholders.next(), (String[]) replacements.next());
		}
		return result;
	}

	/**
	 * @param strings
	 * @param placeholder
	 * @param replacements
	 * @return
	 */
	private static String[] multiply(String[] strings, String placeholder, String[] replacements) {
		String[] results = new String[replacements.length * strings.length];
		int n = 0;
		for (int i = 0; i < replacements.length; i++) {
			for (int j = 0; j < strings.length; j++) {
				results[n++] = replaceOnce(strings[j], placeholder, replacements[i]);
			}
		}
		return results;
	}

	/**
	 * @param string
	 * @param character
	 * @return
	 */
	public static int countUnquoted(String string, char character) {
		if ('\'' == character) {
			throw new IllegalArgumentException("Unquoted count of quotes is invalid");
		}
		if (string == null)
			return 0;
		// Impl note: takes advantage of the fact that an escpaed single quote
		// embedded within a quote-block can really be handled as two seperate
		// quote-blocks for the purposes of this method...
		int count = 0;
		int stringLength = string.length();
		boolean inQuote = false;
		for (int indx = 0; indx < stringLength; indx++) {
			char c = string.charAt(indx);
			if (inQuote) {
				if ('\'' == c) {
					inQuote = false;
				}
			} else if ('\'' == c) {
				inQuote = true;
			} else if (c == character) {
				count++;
			}
		}
		return count;
	}

	/**
	 * @param string
	 * @return
	 */
	public static boolean isNotEmpty(String string) {
		return string != null && string.length() > 0;
	}

	/**
	 * @param string
	 * @return
	 */
	public static boolean isEmpty(String string) {
		return string == null || string.length() == 0;
	}

	/**
	 * @param prefix
	 * @param name
	 * @return
	 */
	public static String qualify(String prefix, String name) {
		if (name == null || prefix == null) {
			throw new NullPointerException();
		}
		return new StringBuilder(prefix.length() + name.length() + 1).append(prefix).append('.').append(name)
				.toString();
	}

	/**
	 * @param prefix
	 * @param names
	 * @return
	 */
	public static String[] qualify(String prefix, String[] names) {
		if (prefix == null)
			return names;
		int len = names.length;
		String[] qualified = new String[len];
		for (int i = 0; i < len; i++) {
			qualified[i] = qualify(prefix, names[i]);
		}
		return qualified;
	}

	/**
	 * @param sqlString
	 * @param string
	 * @param startindex
	 * @return
	 */
	public static int firstIndexOfChar(String sqlString, String string, int startindex) {
		int matchAt = -1;
		for (int i = 0; i < string.length(); i++) {
			int curMatch = sqlString.indexOf(string.charAt(i), startindex);
			if (curMatch >= 0) {
				if (matchAt == -1) { // first time we find match!
					matchAt = curMatch;
				} else {
					matchAt = Math.min(matchAt, curMatch);
				}
			}
		}
		return matchAt;
	}

	/**
	 * @param string
	 * @param length
	 * @return
	 */
	public static String truncate(String string, int length) {
		if (string.length() <= length) {
			return string;
		} else {
			return string.substring(0, length);
		}
	}

	/**
	 * Generate a nice alias for the given class name or collection role name and
	 * unique integer. Subclasses of Loader do <em>not</em> have to use aliases of
	 * this form.
	 * 
	 * @return an alias of the form <tt>foo1_</tt>
	 */
	public static String generateAlias(String description, int unique) {
		return generateAliasRoot(description) + Integer.toString(unique) + '_';
	}

	/**
	 * @param description
	 * @return
	 */
	private static String generateAliasRoot(String description) {
		final String result = 'g'
				+ truncate(unqualifyEntityName(description), ALIAS_TRUNCATE_LENGTH).toLowerCase().replace('/', '_') // entityNames
						// may
						// now
						// include
						// slashes
						// for
						// the
						// representations
						.replace(' ', '_') // class name may contain space
						.replace('$', '_'); // classname may be an inner class
		if (Character.isDigit(result.charAt(result.length() - 1))) {
			return result + "x"; // ick!
		} else {
			return result;
		}
	}

	/**
	 * @param entityName
	 * @return
	 */
	public static String unqualifyEntityName(String entityName) {
		String result = unqualify(entityName);
		int slashPos = result.indexOf('/');
		if (slashPos > 0) {
			result = result.substring(0, slashPos - 1);
		}
		return result;
	}

	/**
	 * @param description
	 * @return
	 */
	public static String generateAlias(String description) {
		return generateAliasRoot(description) + '_';
	}

	/**
	 * @param str
	 * @return
	 */
	public static String toUpperCase(String str) {
		return str == null ? null : str.toUpperCase();
	}

	/**
	 * @param str
	 * @return
	 */
	public static String toLowerCase(String str) {
		return str == null ? null : str.toLowerCase();
	}

	/**
	 * @param filter
	 * @return
	 */
	public static String moveAndToBeginning(String filter) {
		if (filter.trim().length() > 0) {
			filter += " and ";
			if (filter.startsWith(" and "))
				filter = filter.substring(4);
		}
		return filter;
	}

	/**
	 * @param p
	 * @return
	 */
	public static String addQuotedString(String p) {
		StringBuilder currentBuffer = new StringBuilder();

		currentBuffer.append(MariusQLConstants.QUOTED_STRING);
		currentBuffer.append(p);
		currentBuffer.append(MariusQLConstants.QUOTED_STRING);

		return currentBuffer.toString();
	}

	/**
	 * @param p
	 * @return
	 */
	public static String addSimpleQuotedString(String p) {
		StringBuilder currentBuffer = new StringBuilder();

		currentBuffer.append(MariusQLConstants.SIMPLE_QUOTED_STRING);
		currentBuffer.append(p);
		currentBuffer.append(MariusQLConstants.SIMPLE_QUOTED_STRING);

		return currentBuffer.toString();
	}

	/**
	 * @param p
	 * @return
	 */
	public static String addQuotedStringIfIsRequired(String p) {
		if (p.length() != p.replaceAll("\\W", "").length()) {
			return "\"" + p + "\"";
		} else {
			return p;
		}
	}

	/**
	 * 
	 * @param identifier
	 * @return
	 */
	public static boolean isNumeric(String str) {
		try {
			Long.parseLong(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}