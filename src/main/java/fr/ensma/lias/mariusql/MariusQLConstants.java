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

/**
 * @author Mickael BARON
 * @author Ghada TRIKI
 * @author Adel GHAMNIA
 * @author Valentin CASSAIR
 * @author Florian MHUN
 */
public interface MariusQLConstants {

	public static final int MAX_VARCHAR_LENGTH = 4096;

	public static final String MARIUSQL_VERSION = "0.5";

	public static final String RID_COLUMN_NAME = "rid";

	public static final String META_MODEL_SEQUENCE_NAME = "mm_rid_seq";

	public static final String MODEL_SEQUENCE_NAME = "m_rid_seq";

	public static final String INSTANCE_SEQUENCE_NAME = "i_rid_seq";

	public static final String MM_ENTITY_NAME = "mmentity";

	public static final String MM_ATTRIBUTE_NAME = "mmattribute";

	public static final String MM_DATATYPE_NAME = "mmdatatype";

	public static final String MM_ENTITY_TABLE_NAME = "mm_entity";

	public static final String MM_ENTITY_NAME_ATTRIBUTE_NAME = "name";

	public static final String MM_ENTITY_IS_META_META_MODEL_ATTRIBUTE_NAME = "ismetametamodel";

	public static final String MM_ENTITY_IS_CORE_ATTRIBUTE_NAME = "iscore";

	public static final String MM_ENTITY_MAPPEDTABLENAME_ATTRIBUTE_NAME = "mappedtablename";

	public static final String MM_ENTITY_ATTRIBUTES_ATTRIBUTE_NAME = "attributes";

	public static final String MM_ENTITY_SUPERENTITY_ATTRIBUTE_NAME = "superentity";

	public static final String MM_ATTRIBUTE_TABLE_NAME = "mm_attribute";

	public static final String MM_ATTRIBUTE_NAME_ATTRIBUTE_NAME = "name";

	public static final String MM_ATTRIBUTE_SCOPE_ATTRIBUTE_NAME = "scope";

	public static final String MM_ATTRIBUTE_RANGE_ATTRIBUTE_NAME = "range";

	public static final String MM_ATTRIBUTE_IS_OPTIONAL_ATTRIBUTE_NAME = "isoptional";

	public static final String MM_ATTRIBUTE_IS_CORE_ATTRIBUTE_NAME = "iscore";

	public static final String MM_DATATYPE_TABLE_NAME = "mm_datatype";

	public static final String MM_DATATYPE_DTYPE_ATTRIBUTE_NAME = "dtype";

	public static final String MM_DATATYPE_IS_SIMPLE_TYPE_ATTRIBUTE_NAME = "issimpletype";

	public static final String MM_DATATYPE_ON_CLASS_ATTRIBUTE_NAME = "onclass";

	public static final String MM_DATATYPE_COLLECTION_TYPE_ATTRIBUTE_NAME = "collectiontype";

	public static final String MM_DATATYPE_IS_MANY_TO_MANY_ATTRIBUTE_NAME = "ismanytomany";

	public static final String MM_DATATYPE_REVERSE_ATTRIBUTE_ATTRIBUTE_NAME = "reverseattribute";

	public static final String OID_TOKEN_NAME = "oid";

	public static final String M_CLASS_TABLE_NAME = "m_class";

	public static final String M_CLASS_DTYPE_ATTRIBUTE_NAME = "dtype";

	public static final String M_CLASS_CODE_ATTRIBUTE_NAME = "code";

	public static final String M_CLASS_IS_EXTENSION_ATTRIBUTE_NAME = "isextension";

	public static final String M_CLASS_PROPERTIES_ATTRIBUTE_NAME = "properties";

	public static final String M_CLASS_USED_PROPERTIES_ATTRIBUTE_NAME = "usedproperties";

	public static final String M_CLASS_SUPERCLASS_ATTRIBUTE_NAME = "superclass";

	public static final String M_CLASS_PACKAGE_ATTRIBUTE_NAME = "package";

	public static final String M_PROPERTY_TABLE_NAME = "m_property";

	public static final String M_PROPERTY_DTYPE_ATTRIBUTE_NAME = "dtype";

	public static final String M_PROPERTY_CODE_ATTRIBUTE_NAME = "code";

	public static final String M_PROPERTY_SCOPE_ATTRIBUTE_NAME = "scope";

	public static final String M_PROPERTY_RANGE_ATTRIBUTE_NAME = "range";

	public static final String M_DATATYPE_TABLE_NAME = "m_datatype";

	public static final String M_DATATYPE_DTYPE_ATTRIBUTE_NAME = "dtype";

	public static final String M_DATATYPE_IS_SIMPLE_TYPE_ATTRIBUTE_NAME = "issimpletype";

	public static final String M_DATATYPE_ON_CLASS_ATTRIBUTE_NAME = "onclass";

	public static final String M_DATATYPE_COLLECTION_TYPE_ATTRIBUTE_NAME = "collectiontype";

	public static final String M_DATATYPE_ENUMVALUES_ATTRIBUTE_NAME = "enumvalues";

	public static final String DATATYPE_CORE_ENTITY_TABLE_NAME = "m_datatype";

	public static final String CLASS_CORE_ENTITY_NAME = "class";

	public static final String STATIC_CLASS_CORE_ENTITY_NAME = "staticclass";

	public static final String MODEL_PREFIX_TABLE_NAME = "m_";

	public static final String INSTANCE_PREFIX_TABLE_NAME = "i_";

	public static final String PROPERTY_PREFIX_COLUMN_TABLE_NAME = "p";

	public static final String ATTRIBUTE_PREFIX_COLUMN_TABLE_NAME = "a_";

	public static final String PROPERTY_SUFFIX_COLUMN_REF_NAME = "ref";

	public static final String PROPERTY_SUFFIX_COLUMN_REF_TABLE_NAME = "tablename";

	public static final String PROPERTY_SUFFIX_COLUMN_REF_COLLECTION_NAME = "refs";

	public static final String REF_TABLE_RIDS_COLUMN_NAME = "ref_tablenames";

	public static final String PROPERTY_SUFFIX_COLUMN_REF_COLLECTION_TABLE_NAME = "tablenames";

	public static final String DATATYPE_CORE_ENTITY_NAME = "datatype";

	public static final String PROPERTY_CORE_ENTITY_NAME = "property";

	public static final String DESCRIMINATOR_CORE_ATTRIBUTE_NAME = "dtype";

	public static final String PACKAGE_NAME_CORE_ATTRIBUTE_NAME = "package";

	public static final String CODE_CORE_ATTRIBUTE_NAME = "code";

	public static final String NAME_CORE_ATTRIBUTE_NAME = "name";

	public static final String PROPERTIES_CORE_ATTRIBUTE_NAME = "directproperties";

	public static final String USED_PROPERTIES_CORE_ATTRIBUTE_NAME = "usedproperties";

	public static final String IS_EXTENSION_CORE_ATTRIBUTE_NAME = "isextension";

	public static final String SCOPE_CORE_ATTRIBUTE_NAME = "scope";

	public static final String RANGE_CORE_ATTRIBUTE_NAME = "range";

	public static final String SUPER_CLASS_CORE_ATTRIBUTE_NAME = "superclass";

	public static final String OPEN_BRACKET = "{";

	public static final String CLOSE_BRACKET = "}";

	public static final String OPEN_SQUARE = "[";

	public static final String CLOSE_SQUARE = "]";

	public static final String OPEN_PARENTHESIS = "(";

	public static final String CLOSE_PARENTHESIS = ")";

	public static final String NULL_VALUE = "NULL";

	public static final String LANGUAGE_NAME = "name";

	public static final String LANGUAGE_CODE = "code";

	public static final String LANGUAGE_DESCRIPTION = "description";

	public static final String LANGUAGE_TABLE_NAME = "languages";

	public static final String METADATA_TABLE_NAME = "metadata";

	public static final String METANAME_VERSION = "version";

	public static final String METANAME_COLUMN_NAME = "metaname";

	public static final String METAVALUE_COLUMN_NAME = "metavalue";

	// BooleanOperators
	public static final String OP_EG = "=";

	public static final String OP_SUP = ">";

	public static final String OP_INF = "<";

	public static final String OP_SUPEG = ">=";

	public static final String OP_INFEG = "<=";

	public static final String OP_LIKE = "LIKE";

	public static final String OP_NOT_LIKE = "NOT LIKE";

	// ArithmeticOperators
	public static final String OP_PLUS = "+";

	public static final String OP_MINUS = "-";

	public static final String OP_DIV = "/";

	public static final String OP_STAR = "*";

	public static final String OP_CONCAT = "||";

	public static final String FUN_UPPER = "UPPER";

	public static final String FUN_LOWER = "LOWER";

	public static final String FUN_LENGTH = "LENGTH";

	public static final String FUN_BIT_LENGTH = "BIT_LENGTH";

	public static final String FUN_ABS = "ABS";

	public static final String FUN_SQRT = "SQRT";

	public static final String FUN_NULLIF = "NULLIF";

	public static final String FUN_COALESCE = "COALESCE";

	public static final String FUN_UNNEST = "UNNEST";

	public static final String ARRAY_TO_STRING = "ARRAY_TO_STRING";

	public static final String FUN_CONCAT = "CONCAT";

	public static final String OP_IN = "IN";

	public static final String UNDERSCORE = "_";

	public final static String PREFIX_EXTERNAL_ID = "@";

	public final static String PREFIX_INTERNAL_ID = "!";

	public static final String VARCHAR_NAME = "VARCHAR";

	public static final String ORACLE_VARCHAR_NAME = "VARCHAR2";

	public static final String REF_NAME = "REF";

	public static final String STRING_NAME = "STRING";

	public static final String MULTISTRING_NAME = "MULTISTRING";

	public static final String INT_NAME = "INT";

	public static final String REAL_NAME = "REAL";

	public static final String BOOLEAN_NAME = "BOOLEAN";

	public static final String DATE_NAME = "DATE";

	public static final String URI_NAME = "URITYPE";

	public static final String BOOLEANTYPE_NAME = "BOOLEAN";

	public static final String COUNTTYPE_NAME = "COUNTTYPE";

	public static final String ENUMERATE_NAME = "ENUM";

	public static final String COLLECTION_NAME = "ARRAY";

	public static final String ORACLE_COLLECTION_NAME = "TARRAYINT";

	public static final String ORACLE_COLLECTION_NAME_STRING = "TARRAYSTRING";

	public static final String ORACLE_COLLECTION_NAME_DOUBLE = "TARRAYDOUBLE";

	public static final String ORACLE_COLLECTION_NAME_BOOLEAN = "TARRAYBOOLEAN";

	public static final String ASSOCIATION_NAME = "REF";

	public static final String PREFIX_METAMODEL_ELEMENT = "#";

	public final static String NO_NAMESPACE = null;

	public final static String QUOTED_STRING = "\"";

	public final static String SIMPLE_QUOTED_STRING = "'";

	public final static String DOT_NAME = ".";

	public final static String ALIAS_NAME = "AS";

	public final static String BLANK_NAME = " ";

	public final static String NO_LANGUAGE = "NO_LANGUAGE";

	public final static String DEFAULT_LANGUAGE = "en";

	public final static String ENGLISH = "en";

	public final static String FRENCH = "fr";

	public final static String VERSION_DEFAULT_VALUE = "001";

	public static final String INSTANCE_NAME_DELIMITER = "#";

	public static final String SELECT_TOKEN_NAME = "SELECT";

	public static final String FROM_TOKEN_NAME = "FROM";

	public static final String UNION_ALL_TOKEN_NAME = "UNION ALL";

	public static final String INNER_JOIN_TOKEN_NAME = "INNER JOIN";

	public static final String LEFT_OUTER_JOIN_TOKEN_NAME = "LEFT OUTER JOIN";

	public static final String IN_LIST_TOKEN_NAME = "IN_LIST";

	public static final String ITS_TYPE_ATTRIBUTE = "isType";

	public static final String TYPE_OF_ID_TOKEN_NAME = "TYPEOF_ID";

	public static final String PREFIX_NAMESPACE_RDF = "rdf";

	public static final String USE_CONCEPT_EXISTS_CHECK = "OntoQL2.USE_CONCEPT_EXISTS_CHECK";

	public static final String USE_CLASS_EXISTS_CHECK = "OntoQL2.USE_CLASS_EXISTS_CHECK";

	public static final String USE_APPLICABLE_PROPERTY_EXISTS_CHECK = "OntoQL2.USE_APPLICABLE_PROPERTY_EXISTS_CHECK";

	public static final String MIMPLEMENTATION_TYPE = "type";

	public static final String WEBSERVICE_TYPE = "WebService";

	public static final String EXTERNAL_PROGRAM_TYPE = "ExternalProgram";

	public static final String IMPLEMENTATION_IMPLEMENT_OPERATION = "implementOperation";

	public static final String MIMPLEMENTATION_LOCATION = "location";

	public static final String MWEBSERVICE_NAMESPACE = "wsnamespace";

	public static final String MWEBSERVICE_OPERATION = "wsoperation";

	public static final String IMPLEMENTATION_TYPE = INSTANCE_NAME_DELIMITER + MIMPLEMENTATION_TYPE;

	public static final String IMPLEMENTATION_LOCATION = INSTANCE_NAME_DELIMITER + "location";

	public static final String WEBSERVICE_NAMESPACE = INSTANCE_NAME_DELIMITER + MWEBSERVICE_NAMESPACE;

	public static final String WEBSERVICE_OPERATION = INSTANCE_NAME_DELIMITER + "operation";

	public static final String EXTERNAL_PROGRAM_CLASS = INSTANCE_NAME_DELIMITER + "operationClass";

	public static final String EXTERNAL_PROGRAM_METHOD = INSTANCE_NAME_DELIMITER + "methodName";
}
