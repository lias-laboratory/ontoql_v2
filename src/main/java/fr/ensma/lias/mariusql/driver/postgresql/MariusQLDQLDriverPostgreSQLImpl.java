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
package fr.ensma.lias.mariusql.driver.postgresql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.Datatype;
import fr.ensma.lias.mariusql.core.DatatypeBoolean;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.core.Description;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.metamodel.MMAttribute;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.core.model.MClass;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.driver.AbstractMariusQLDQLDriver;
import fr.ensma.lias.mariusql.driver.postgresql.util.SQLHelper;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * @author Mickael BARON
 * @author Ghada TRIKI
 * @author Adel GHAMNIA
 * @author Valentin CASSAIR
 * @author Florian MHUN
 */
public class MariusQLDQLDriverPostgreSQLImpl extends AbstractMariusQLDQLDriver {

	public MariusQLDQLDriverPostgreSQLImpl(MariusQLSession pSession) {
		super(pSession);
	}

	@Override
	public List<MProperty> getPropertyWithCollectionOfReferenceByOnClass(Long internalId) {
		String c = StringHelper.generateAlias("mc", 1);
		String d1 = StringHelper.generateAlias("md", 1);
		String d2 = StringHelper.generateAlias("md2", 1);
		String p = StringHelper.generateAlias("mp", 1);

		String query = "select " + SQLHelper.aliasColumn(MariusQLConstants.RID_COLUMN_NAME, p) + " , "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_CLASS_CODE_ATTRIBUTE_NAME, c) + " , "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_PROPERTY_RANGE_ATTRIBUTE_NAME, p) + " from "
				+ SQLHelper.aliasTable(MariusQLConstants.M_DATATYPE_TABLE_NAME, d1) + " , "
				+ SQLHelper.aliasTable(MariusQLConstants.M_PROPERTY_TABLE_NAME, p) + " , "
				+ SQLHelper.aliasTable(MariusQLConstants.M_CLASS_TABLE_NAME, c) + " where "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_DATATYPE_DTYPE_ATTRIBUTE_NAME, d1) + " = "
				+ DatatypeEnum.DATATYPECOLLECTION.toSQL() + " and "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_DATATYPE_COLLECTION_TYPE_ATTRIBUTE_NAME, d1)
				+ " in ( select " + SQLHelper.aliasColumn(MariusQLConstants.RID_COLUMN_NAME, d2) + " from "
				+ SQLHelper.aliasTable(MariusQLConstants.M_DATATYPE_TABLE_NAME, d2) + " where "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_DATATYPE_DTYPE_ATTRIBUTE_NAME, d2) + " = "
				+ DatatypeEnum.DATATYPEREFERENCE.toSQL() + " and "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_DATATYPE_ON_CLASS_ATTRIBUTE_NAME, d2) + " = " + internalId
				+ " and " + SQLHelper.aliasColumn(MariusQLConstants.RID_COLUMN_NAME, d1) + " = "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_PROPERTY_RANGE_ATTRIBUTE_NAME, p) + " and "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_CLASS_IS_EXTENSION_ATTRIBUTE_NAME, c) + " = true " + " and "
				+ " ARRAY[" + SQLHelper.aliasColumn(MariusQLConstants.RID_COLUMN_NAME, p) + "]::bigint[] && "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_CLASS_USED_PROPERTIES_ATTRIBUTE_NAME, c) + " ) ";

		try {
			List<MProperty> properties = new ArrayList<MProperty>();
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				MClass mClass = FactoryCore.createExistingMClass(this.getSession(), resultSet.getString(2));
				Datatype dataType = FactoryCore.createMDatatype(this.getSession(), resultSet.getLong(3));

				MProperty property = FactoryCore.createExistingMProperty(this.getSession(), resultSet.getLong(1));
				property.setScope(mClass);
				property.setRange(dataType);

				properties.add(property);
			}

			statement.close();

			return properties;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public boolean isMGenericClassReferenced(Long mclassRid) {
		String d1 = StringHelper.generateAlias("md", 1);
		String a1 = StringHelper.generateAlias("mp", 1);

		String query = "select " + SQLHelper.aliasColumn(MariusQLConstants.RID_COLUMN_NAME, d1) + " from "
				+ SQLHelper.aliasTable(MariusQLConstants.M_DATATYPE_TABLE_NAME, d1) + " where " + "("
				+ SQLHelper.aliasColumn(MariusQLConstants.M_DATATYPE_DTYPE_ATTRIBUTE_NAME, d1) + " = "
				+ DatatypeEnum.DATATYPEREFERENCE.toSQL() + " and "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_DATATYPE_ON_CLASS_ATTRIBUTE_NAME, d1) + " = " + mclassRid
				+ " and " + SQLHelper.aliasColumn(MariusQLConstants.RID_COLUMN_NAME, d1) + " not in " + "(" + "select "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_PROPERTY_RANGE_ATTRIBUTE_NAME, a1) + " from "
				+ SQLHelper.aliasTable(MariusQLConstants.M_PROPERTY_TABLE_NAME, a1) + " where "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_PROPERTY_SCOPE_ATTRIBUTE_NAME, a1) + " = " + mclassRid + ")"
				+ ")" + " and " + SQLHelper.aliasColumn(MariusQLConstants.M_DATATYPE_IS_SIMPLE_TYPE_ATTRIBUTE_NAME, d1)
				+ " = false";

		try {
			boolean exists = false;
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			exists = resultSet.next();

			statement.close();

			return exists;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public List<MProperty> getPropertyWithReferenceByOnClass(Long classId) {
		String c = StringHelper.generateAlias("mc", 1);
		String d = StringHelper.generateAlias("md", 1);
		String p = StringHelper.generateAlias("mp", 1);

		String query = "select " + SQLHelper.aliasColumn(MariusQLConstants.RID_COLUMN_NAME, p) + " , "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_CLASS_CODE_ATTRIBUTE_NAME, c) + " from "
				+ SQLHelper.aliasTable(MariusQLConstants.M_DATATYPE_TABLE_NAME, d) + " , "
				+ SQLHelper.aliasTable(MariusQLConstants.M_PROPERTY_TABLE_NAME, p) + " , "
				+ SQLHelper.aliasTable(MariusQLConstants.M_CLASS_TABLE_NAME, c) + " where "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_DATATYPE_DTYPE_ATTRIBUTE_NAME, d) + " = "
				+ DatatypeEnum.DATATYPEREFERENCE.toSQL() + " and "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_DATATYPE_ON_CLASS_ATTRIBUTE_NAME, d) + " = " + classId
				+ " and " + SQLHelper.aliasColumn(MariusQLConstants.RID_COLUMN_NAME, d) + " = "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_PROPERTY_RANGE_ATTRIBUTE_NAME, p) + " and "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_CLASS_IS_EXTENSION_ATTRIBUTE_NAME, c) + " = true " + " and "
				+ " ARRAY[" + SQLHelper.aliasColumn(MariusQLConstants.RID_COLUMN_NAME, p) + "]::bigint[] && "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_CLASS_USED_PROPERTIES_ATTRIBUTE_NAME, c);

		try {
			List<MProperty> properties = new ArrayList<MProperty>();
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				MClass mClass = FactoryCore.createExistingMClass(this.getSession(), resultSet.getString(2));

				MProperty property = FactoryCore.createExistingMProperty(this.getSession(), resultSet.getLong(1));
				property.setScope(mClass);

				properties.add(property);
			}

			statement.close();

			return properties;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public boolean isMMEntityReferenced(Long entityRid) {
		String d1 = StringHelper.generateAlias("mmd", 1);
		String a1 = StringHelper.generateAlias("mma", 1);

		String query = "select " + SQLHelper.aliasColumn(MariusQLConstants.RID_COLUMN_NAME, d1) + " from "
				+ SQLHelper.aliasTable(MariusQLConstants.MM_DATATYPE_TABLE_NAME, d1) + " where " + "("
				+ SQLHelper.aliasColumn(MariusQLConstants.MM_DATATYPE_DTYPE_ATTRIBUTE_NAME, d1) + " = "
				+ DatatypeEnum.DATATYPEREFERENCE.toSQL() + " and "
				+ SQLHelper.aliasColumn(MariusQLConstants.MM_DATATYPE_ON_CLASS_ATTRIBUTE_NAME, d1) + " = " + entityRid
				+ " and " + SQLHelper.aliasColumn(MariusQLConstants.RID_COLUMN_NAME, d1) + " not in " + "(" + "select "
				+ SQLHelper.aliasColumn(MariusQLConstants.MM_ATTRIBUTE_RANGE_ATTRIBUTE_NAME, a1) + " from "
				+ SQLHelper.aliasTable(MariusQLConstants.MM_ATTRIBUTE_TABLE_NAME, a1) + " where "
				+ SQLHelper.aliasColumn(MariusQLConstants.MM_ATTRIBUTE_SCOPE_ATTRIBUTE_NAME, a1) + " = " + entityRid
				+ ")" + ")" + " and "
				+ SQLHelper.aliasColumn(MariusQLConstants.MM_DATATYPE_IS_SIMPLE_TYPE_ATTRIBUTE_NAME, d1) + " = false";

		try {
			boolean exists = false;
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			exists = resultSet.next();

			statement.close();

			return exists;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public String getColumnValue(String tableName, String rid, Description column) {
		if (tableName == null || rid == null || column == null) {
			throw new MariusQLException("parameters error");
		}

		String query = "select " + column.toSQL() + " from " + tableName + " where " + MariusQLConstants.RID_COLUMN_NAME
				+ " = " + rid;

		try {
			String value = null;
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet.next()) {
				// jdbc getString() returns t or f sometimes (in PostgreSQL)
				// all datatype can be handled by checking column range
				if (column.getRange() instanceof DatatypeBoolean) {
					value = new Boolean(resultSet.getBoolean(1)).toString();
				} else {
					value = resultSet.getString(1);
				}
			}

			statement.close();

			return value;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public Long getMMSimpleDataTypeRid(String pType) {
		if (pType == null) {
			throw new MariusQLException("parameters error");
		}

		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + " from "
				+ MariusQLConstants.MM_DATATYPE_TABLE_NAME + " where "
				+ MariusQLConstants.MM_DATATYPE_DTYPE_ATTRIBUTE_NAME.toLowerCase() + " = "
				+ StringHelper.addSimpleQuotedString(pType.toLowerCase()) + " and "
				+ MariusQLConstants.MM_DATATYPE_IS_SIMPLE_TYPE_ATTRIBUTE_NAME + " = true";

		try {
			Long rid = null;
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet.next()) {
				rid = resultSet.getLong(1);
			}

			statement.close();

			return rid;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public MMEntity getMMEntityByName(String identifier) {
		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + ", "
				+ MariusQLConstants.MM_ENTITY_NAME_ATTRIBUTE_NAME + ","
				+ MariusQLConstants.MM_ENTITY_MAPPEDTABLENAME_ATTRIBUTE_NAME + ","
				+ MariusQLConstants.MM_ENTITY_IS_CORE_ATTRIBUTE_NAME + ","
				+ MariusQLConstants.MM_ENTITY_IS_META_META_MODEL_ATTRIBUTE_NAME + " from "
				+ MariusQLConstants.MM_ENTITY_TABLE_NAME + " where LOWER("
				+ MariusQLConstants.MM_ENTITY_NAME_ATTRIBUTE_NAME + ") = "
				+ StringHelper.addSimpleQuotedString(identifier.toLowerCase());

		try {
			MMEntity entity = null;
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet.next()) {
				entity = new MMEntity(this.getSession(), resultSet.getLong(1), resultSet.getString(2),
						resultSet.getString(3), resultSet.getBoolean(4), resultSet.getBoolean(5));
			}

			statement.close();

			return entity;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public Long getMMAttributeRidByName(String name, Long scopeId) {
		if (name == null || scopeId == null || scopeId == 0) {
			throw new MariusQLException("parameters error");
		}

		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + " from "
				+ MariusQLConstants.MM_ATTRIBUTE_TABLE_NAME + " where LOWER("
				+ MariusQLConstants.MM_ATTRIBUTE_NAME_ATTRIBUTE_NAME + ") = "
				+ StringHelper.addSimpleQuotedString(name.toLowerCase()) + " AND "
				+ MariusQLConstants.MM_ATTRIBUTE_SCOPE_ATTRIBUTE_NAME + " = " + scopeId;

		try {
			Long rid = null;
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet.next()) {
				rid = resultSet.getLong(1);
			}

			statement.close();

			return rid;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public Long getMMEntityRidByName(String name) {
		if (name == null) {
			throw new MariusQLException("parameters error");
		}

		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + " from " + MariusQLConstants.MM_ENTITY_TABLE_NAME
				+ " where LOWER(" + MariusQLConstants.MM_ENTITY_NAME_ATTRIBUTE_NAME + ") = "
				+ StringHelper.addSimpleQuotedString(name.toLowerCase());

		try {
			Long rid = null;
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet.next()) {
				rid = resultSet.getLong(1);
			}

			statement.close();

			return rid;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public Long getMSimpleDataTypeRid(String pType) {
		if (pType == null) {
			throw new MariusQLException("parameters error");
		}

		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + " from "
				+ MariusQLConstants.DATATYPE_CORE_ENTITY_TABLE_NAME + " where LOWER("
				+ MariusQLConstants.MM_DATATYPE_DTYPE_ATTRIBUTE_NAME + ") = "
				+ StringHelper.addSimpleQuotedString(pType.toLowerCase()) + " and "
				+ MariusQLConstants.MM_DATATYPE_IS_SIMPLE_TYPE_ATTRIBUTE_NAME + " = true";

		try {
			Long rid = null;
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet.next()) {
				rid = resultSet.getLong(1);
			}

			statement.close();

			return rid;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public boolean isMMAttributeExists(MMAttribute mmAttribute, Long entityiInternalId) {

		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + " from "
				+ MariusQLConstants.MM_ATTRIBUTE_TABLE_NAME + " where LOWER("
				+ MariusQLConstants.MM_ATTRIBUTE_NAME_ATTRIBUTE_NAME + ") = "
				+ StringHelper.addSimpleQuotedString(mmAttribute.getName().toLowerCase()) + " AND "
				+ MariusQLConstants.SCOPE_CORE_ATTRIBUTE_NAME + " != " + entityiInternalId;

		try {
			boolean exists = false;
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			exists = resultSet.next();

			statement.close();

			return exists;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	public boolean isMPropertyRangeOfCollectionDataType(String propertyName) {
		String cond = "";
		String refLanguage = this.getSession().getReferenceLanguage();

		if (refLanguage.equalsIgnoreCase(MariusQLConstants.ENGLISH))
			cond = MariusQLConstants.NAME_CORE_ATTRIBUTE_NAME + MariusQLConstants.UNDERSCORE
					+ MariusQLConstants.ENGLISH;
		else if (refLanguage.equalsIgnoreCase(MariusQLConstants.FRENCH))
			cond = MariusQLConstants.NAME_CORE_ATTRIBUTE_NAME + MariusQLConstants.UNDERSCORE + MariusQLConstants.FRENCH;
		else
			cond = MariusQLConstants.CODE_CORE_ATTRIBUTE_NAME;

		String query = "select p." + MariusQLConstants.RID_COLUMN_NAME + " from "
				+ MariusQLConstants.M_PROPERTY_TABLE_NAME + " p, " + MariusQLConstants.DATATYPE_CORE_ENTITY_TABLE_NAME
				+ " d " + " where p." + MariusQLConstants.RANGE_CORE_ATTRIBUTE_NAME + " = d."
				+ MariusQLConstants.RID_COLUMN_NAME + " and p." + cond + "=" + "'" + propertyName + "'" + " and d."
				+ MariusQLConstants.MM_DATATYPE_IS_SIMPLE_TYPE_ATTRIBUTE_NAME + " = " + "'FALSE'";
		try {
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);
			final boolean next = resultSet.next();

			statement.close();

			return next;
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public int getCountRow(String tableName, boolean isAccurate) {
		String query = "";

		if (isAccurate) {
			query = "select count(*) from " + tableName;
		} else {
			query = "SELECT reltuples FROM pg_class C LEFT JOIN pg_namespace N ON (N.oid = C.relnamespace) WHERE nspname NOT IN ('pg_catalog', 'information_schema') AND relkind='r' AND relname = '"
					+ tableName + "'";
		}

		try {
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet.next()) {
				final int int1 = resultSet.getInt(1);

				statement.close();
				return int1;
			} else {
				return 0;
			}
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public String getEmptyQuery() {
		// TODO: MB
		// return "(select null as rid where false)";
		return "where false";
	}
}
