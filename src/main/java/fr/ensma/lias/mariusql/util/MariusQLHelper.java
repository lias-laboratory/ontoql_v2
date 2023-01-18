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

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.exception.NotSupportedException;
import fr.ensma.lias.mariusql.exception.NotYetImplementedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * @author Mickael BARON
 * @author Stéphane JEAN
 * @author Valentin CASSAIR
 * @author Ghada TRIKI
 */
public class MariusQLHelper {

	public final static String ENGLISH = "en";

	public final static String FRENCH = "fr";

	public final static String PREFIX_NAME_ID = "\"";

	public final static String SEPARATOR_EXTERNAL_ID = "-";

	public static boolean isNull(String value) {
		return value.equalsIgnoreCase(MariusQLConstants.NULL_VALUE);
	}

	public static List<Long> getSQLCollectionAssociationValues(String value) {
		List<Long> values = new ArrayList<Long>();

		if (value != null && !value.isEmpty()) {
			if (value.startsWith(MariusQLConstants.OPEN_BRACKET) && value.endsWith(MariusQLConstants.CLOSE_BRACKET)) {
				value = value.substring(1, value.length() - 1);
				if (!value.isEmpty()) {
					final String[] split = value.split(",");
					for (String string : split) {
						values.add(Long.valueOf(string));
					}
				}
			}
		}

		return values;
	}

	public static List<Long> getOntoQLCollectionAssociationValues(String value) {
		List<Long> values = new ArrayList<Long>();
		String val = value.replaceAll("\\s", "");

		if (val != null && !val.isEmpty()) {
			if (val.startsWith(MariusQLConstants.OPEN_SQUARE) && val.endsWith(MariusQLConstants.CLOSE_SQUARE)) {
				val = val.substring(1, val.length() - 1);
			}

			final String[] split = val.split(",");
			for (String string : split) {
				values.add(Long.valueOf(getQuotedString(string)));
			}
		}

		return values;
	}

	public static String getCollectionAssociationSQLValue(List<Long> pValues) {
		String returnValue = "";

		for (Long current : pValues) {
			returnValue = returnValue + current + ",";
		}

		if (!returnValue.isEmpty()) {
			returnValue = returnValue.substring(0, returnValue.length() - 1);
		}

		return MariusQLConstants.OPEN_BRACKET + returnValue + MariusQLConstants.CLOSE_BRACKET;
	}

	public static String getCollectionAssociationOntoQLValue(List<Long> pValues) {
		String returnValue = "";

		for (Long current : pValues) {
			returnValue = returnValue + current + ",";
		}

		if (!returnValue.isEmpty()) {
			returnValue = returnValue.substring(0, returnValue.length() - 1);
		}

		return MariusQLConstants.COLLECTION_NAME + MariusQLConstants.OPEN_SQUARE + returnValue
				+ MariusQLConstants.CLOSE_SQUARE;
	}

	/**
	 * Check if id value starts with a PREFIX_ONTOLOGYMODEL_ELEMENT identifier.
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isMetaModelElement(String id) {
		return id.startsWith(MariusQLConstants.PREFIX_METAMODEL_ELEMENT);
	}

	/**
	 * @param id
	 * @return
	 */
	public static String getEntityIdentifier(String id) {
		if (isMetaModelElement(id)) {
			return id.substring(1);
		} else {
			return id;
		}
	}

	/**
	 * check if a text is an internal identifier
	 * 
	 * @param id a text
	 * @return true if the text is an internal identifier
	 */
	public static boolean isInternalIdentifier(String id) {
		return StringHelper.isNumeric(id) || id.startsWith(MariusQLConstants.PREFIX_INTERNAL_ID);
	}

	/**
	 * Remove the syntax of an internal identifier (!)
	 * 
	 * @param id an internal identifier
	 * @return the internal identifier without the syntax
	 */
	public static String removeSyntaxInternalIdentifier(String id) {
		if (isInternalIdentifier(id) && !StringHelper.isNumeric(id)) {
			return id.substring(1);
		} else {
			return id;
		}
	}

	/**
	 * check if a text is an external identifier
	 * 
	 * @param id a text
	 * @return true if the text is an external identifier
	 */
	public static boolean isExternalIdentifier(String id) {
		return id.startsWith(MariusQLConstants.PREFIX_EXTERNAL_ID);
	}

	/**
	 * Remove the syntax of an internal identifier (!)
	 * 
	 * @param id an external identifier
	 * @return the external identifier without the syntax
	 */
	public static String removeSyntaxExternalId(String id) {
		if (isExternalIdentifier(id)) {
			return id.substring(1);
		} else {
			return id;
		}
	}

	/**
	 * check if a text is a name identifier
	 * 
	 * @param id a text
	 * @return true if the text is a name identifier
	 */
	public static boolean isNameIdentifier(String id) {
		return (id.startsWith(MariusQLConstants.QUOTED_STRING) && id.endsWith(MariusQLConstants.QUOTED_STRING))
				|| (id.startsWith(MariusQLConstants.SIMPLE_QUOTED_STRING)
						&& id.endsWith(MariusQLConstants.SIMPLE_QUOTED_STRING));
	}

	/**
	 * remove the "" from a name identifier
	 * 
	 * @param id the name which identify a class in the OntoQL syntax
	 * @return the name identifier without " ".
	 */
	public static String removeSyntaxNameIdentifier(String id) {
		if (isNameIdentifier(id)) {
			return id.substring(1, id.length() - 1);
		} else {
			return id;
		}
	}

	/**
	 * Return string value without quoted string.
	 * 
	 * @param id
	 * @return
	 */
	public static String getQuotedString(String id) {
		if (MariusQLHelper.isQuotedString(id)) {
			return StringHelper.removeFirstAndLastletter(id);
		} else if (MariusQLHelper.isSimpleQuotedString(id)) {
			return StringHelper.removeFirstAndLastletter(id);
		} else {
			return id;
		}
	}

	/**
	 * Check if id value starts with a quote.
	 * 
	 * @param id value
	 * @return true starts with a quote else false
	 */
	public static boolean isQuotedString(String id) {
		return id.startsWith(MariusQLConstants.QUOTED_STRING);
	}

	/**
	 * Check if id value starts with a simple quote.
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isSimpleQuotedString(String id) {
		return id.startsWith(MariusQLConstants.SIMPLE_QUOTED_STRING);
	}

	/**
	 * Return another language than those pass in parameter
	 */
	public static String getOtherLanguage(String lg) {
		String otherLg = null;
		if (lg != null) {
			if (lg.equals(ENGLISH))
				otherLg = FRENCH;
			else {
				otherLg = ENGLISH;
			}
		}
		return otherLg;
	}

	public static List<Long> getCollectionAssociationValues(String value) {
		List<Long> values = new ArrayList<Long>();
		if (value != null && !value.isEmpty()) {
			if (value.startsWith(MariusQLConstants.OPEN_BRACKET) && value.endsWith(MariusQLConstants.CLOSE_BRACKET)) {
				value = value.substring(1, value.length() - 1);

				final String[] split = value.split(",");
				for (String string : split) {
					values.add(Long.valueOf(string));
				}
			} else if (value.startsWith(MariusQLConstants.COLLECTION_NAME)) {
				value = value.replace(MariusQLConstants.COLLECTION_NAME, "");
				if (value.startsWith(MariusQLConstants.OPEN_SQUARE) && value.endsWith(MariusQLConstants.CLOSE_SQUARE)) {
					value = value.substring(1, value.length() - 1);
				}

				final String[] split = value.split(",");
				for (String string : split) {
					values.add(Long.valueOf(getQuotedString(string)));
				}
			}
		}

		return values;
	}

	public static String getIdentifierAttributeName(String identifier, MariusQLSession session) {
		String name = null;

		if (MariusQLHelper.isInternalIdentifier(identifier)) {
			name = MariusQLConstants.RID_COLUMN_NAME;
		} else if (MariusQLHelper.isExternalIdentifier(identifier) || session.isNoReferenceLanguage()) {
			name = MariusQLConstants.CODE_CORE_ATTRIBUTE_NAME;
		} else {
			name = MariusQLConstants.NAME_CORE_ATTRIBUTE_NAME;
		}

		return name;
	}

	public static String getCleanIdentifier(String identifier, MariusQLSession session) {
		String cleanIdentifer = identifier;

		if (MariusQLHelper.isInternalIdentifier(cleanIdentifer)) {
			cleanIdentifer = MariusQLHelper.removeSyntaxInternalIdentifier(cleanIdentifer);
		} else if (MariusQLHelper.isExternalIdentifier(cleanIdentifer) || session.isNoReferenceLanguage()) {
			cleanIdentifer = MariusQLHelper.removeSyntaxExternalId(cleanIdentifer);
			cleanIdentifer = MariusQLHelper.removeSyntaxNameIdentifier(cleanIdentifer);
		} else if (MariusQLHelper.isNameIdentifier(cleanIdentifer)) {
			cleanIdentifer = MariusQLHelper.removeSyntaxNameIdentifier(cleanIdentifer);
		}

		return cleanIdentifer;
	}

	public static DatatypeEnum getDatatypeEnum(String nameDatatype) {
		if (nameDatatype.equalsIgnoreCase(MariusQLConstants.INT_NAME)) {
			return DatatypeEnum.DATATYPEINT;
		} else if (nameDatatype.equalsIgnoreCase(MariusQLConstants.BOOLEAN_NAME)) {
			return DatatypeEnum.DATATYPEBOOLEAN;
		} else if (nameDatatype.equalsIgnoreCase(MariusQLConstants.STRING_NAME)) {
			return DatatypeEnum.DATATYPESTRING;
		} else if (nameDatatype.equalsIgnoreCase(MariusQLConstants.REAL_NAME)) {
			return DatatypeEnum.DATATYPEREAL;
		} else if (nameDatatype.equalsIgnoreCase(MariusQLConstants.COLLECTION_NAME)) {
			return DatatypeEnum.DATATYPECOLLECTION;
		} else if (nameDatatype.equalsIgnoreCase(MariusQLConstants.ASSOCIATION_NAME)) {
			return DatatypeEnum.DATATYPEREFERENCE;
		} else if (nameDatatype.equalsIgnoreCase(MariusQLConstants.MULTISTRING_NAME)) {
			return DatatypeEnum.DATATYPEMULTISTRING;
		} else if (nameDatatype.equalsIgnoreCase(MariusQLConstants.URI_NAME)) {
			return DatatypeEnum.DATATYPEURI;
		} else if (nameDatatype.equalsIgnoreCase(MariusQLConstants.ENUMERATE_NAME)) {
			return DatatypeEnum.DATATYPEENUMERATE;
		} else if (nameDatatype.equalsIgnoreCase(MariusQLConstants.COUNTTYPE_NAME)) {
			return DatatypeEnum.DATATYPECOUNT;
		} else {
			throw new NotYetImplementedException("Dataype " + nameDatatype + " does not exist");
		}
	}

	public static void isFeatureSupported(boolean value, String featureName) {
		if (!value) {
			throw new NotSupportedException("This feature is not supported: " + featureName);
		}
	}
}
