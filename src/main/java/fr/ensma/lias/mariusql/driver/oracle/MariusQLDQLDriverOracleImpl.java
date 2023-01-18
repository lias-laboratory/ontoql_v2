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
package fr.ensma.lias.mariusql.driver.oracle;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import oracle.sql.ARRAY;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.Category;
import fr.ensma.lias.mariusql.core.Datatype;
import fr.ensma.lias.mariusql.core.DatatypeBoolean;
import fr.ensma.lias.mariusql.core.DatatypeCollection;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.core.Description;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.metamodel.MMAttribute;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.core.model.MClass;
import fr.ensma.lias.mariusql.core.model.MDatatypeCollection;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.driver.AbstractMariusQLDQLDriver;
import fr.ensma.lias.mariusql.driver.hsqldb.util.SQLHelper;
import fr.ensma.lias.mariusql.engine.tree.IdentNode;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotYetImplementedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * @author Mickael BARON
 * @author Ghada TRIKI
 * @author Larbi LATRECHE
 */
@SuppressWarnings("deprecation")
public class MariusQLDQLDriverOracleImpl extends AbstractMariusQLDQLDriver {

	public MariusQLDQLDriverOracleImpl(MariusQLSession pSession) {
		super(pSession);
	}

	@Override
	public Long getMMSimpleDataTypeRid(String pType) {
		if (pType == null) {
			throw new MariusQLException("parameters error");
		}

		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + " from "
				+ MariusQLConstants.MM_DATATYPE_TABLE_NAME + " where LOWER("
				+ MariusQLConstants.MM_DATATYPE_DTYPE_ATTRIBUTE_NAME + ") = "
				+ StringHelper.addSimpleQuotedString(pType.toLowerCase()) + " and "
				+ MariusQLConstants.MM_DATATYPE_IS_SIMPLE_TYPE_ATTRIBUTE_NAME + " = 1";

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
				+ MariusQLConstants.MM_DATATYPE_IS_SIMPLE_TYPE_ATTRIBUTE_NAME + " = 1";

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
	public List<MMEntity> getMMEntityFromMetaMetaModel() {
		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + ", "
				+ MariusQLConstants.MM_ENTITY_NAME_ATTRIBUTE_NAME + ","
				+ MariusQLConstants.MM_ENTITY_MAPPEDTABLENAME_ATTRIBUTE_NAME + ","
				+ MariusQLConstants.MM_ENTITY_IS_META_META_MODEL_ATTRIBUTE_NAME + " from "
				+ MariusQLConstants.MM_ENTITY_TABLE_NAME + " where "
				+ MariusQLConstants.MM_ENTITY_IS_META_META_MODEL_ATTRIBUTE_NAME + " = 1";

		List<MMEntity> entityElements = new ArrayList<MMEntity>();

		try {
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				entityElements.add(new MMEntity(this.getSession(), resultSet.getLong(1), resultSet.getString(2),
						resultSet.getString(3), false, resultSet.getBoolean(4)));
			}

			statement.close();

			return entityElements;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public List<MMEntity> getMMEntityFromMetaModelCore() {
		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + ", "
				+ MariusQLConstants.MM_ENTITY_NAME_ATTRIBUTE_NAME + ","
				+ MariusQLConstants.MM_ENTITY_MAPPEDTABLENAME_ATTRIBUTE_NAME + ","
				+ MariusQLConstants.MM_ENTITY_IS_META_META_MODEL_ATTRIBUTE_NAME + " from "
				+ MariusQLConstants.MM_ENTITY_TABLE_NAME + " where "
				+ MariusQLConstants.MM_ENTITY_IS_CORE_ATTRIBUTE_NAME + " = 1";

		List<MMEntity> entityElements = new ArrayList<MMEntity>();
		try {
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				entityElements.add(new MMEntity(this.getSession(), resultSet.getLong(1), resultSet.getString(2),
						resultSet.getString(3), true, resultSet.getBoolean(4)));
			}

			statement.close();

			return entityElements;
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
				+ SQLHelper.aliasColumn(MariusQLConstants.MM_DATATYPE_IS_SIMPLE_TYPE_ATTRIBUTE_NAME, d1) + " = 0";

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
				+ " = 0";

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
	public Long getLastMMDatatypeReferenceByOnClass(Category category) {
		String query = "select max(" + MariusQLConstants.RID_COLUMN_NAME + ") from "
				+ MariusQLConstants.MM_DATATYPE_TABLE_NAME + " where "
				+ MariusQLConstants.MM_DATATYPE_DTYPE_ATTRIBUTE_NAME.toLowerCase() + " = " + "'ref'" + " and "
				+ MariusQLConstants.MM_DATATYPE_IS_SIMPLE_TYPE_ATTRIBUTE_NAME + " = 0" + " and "
				+ MariusQLConstants.MM_DATATYPE_ON_CLASS_ATTRIBUTE_NAME + " = " + category.getInternalId().toString();

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
	public MDatatypeCollection getMDatatypeCollectionByDatatype(Datatype collectionDatatype) {
		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + ", "
				+ MariusQLConstants.M_DATATYPE_COLLECTION_TYPE_ATTRIBUTE_NAME + " from "
				+ MariusQLConstants.M_DATATYPE_TABLE_NAME + " where "
				+ MariusQLConstants.M_DATATYPE_DTYPE_ATTRIBUTE_NAME + " = " + DatatypeEnum.DATATYPECOLLECTION.toSQL()
				+ " and " + MariusQLConstants.M_DATATYPE_COLLECTION_TYPE_ATTRIBUTE_NAME + " = "
				+ collectionDatatype.getInternalId() + " and "
				+ MariusQLConstants.M_DATATYPE_IS_SIMPLE_TYPE_ATTRIBUTE_NAME + " = 0";

		try {
			MDatatypeCollection collection = null;
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet.next()) {
				Datatype collectionType = this.getMDataTypeByRid(resultSet.getLong(2));
				collection = new MDatatypeCollection(this.getSession(), new Long(resultSet.getLong(1)), collectionType);
			}

			statement.close();

			return collection;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public Long getMDatatypeByCollectionDatatype(Long collectionDatatypeRid) {
		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + " from "
				+ MariusQLConstants.M_DATATYPE_TABLE_NAME + " where "
				+ MariusQLConstants.M_DATATYPE_DTYPE_ATTRIBUTE_NAME + " = " + DatatypeEnum.DATATYPECOLLECTION.toSQL()
				+ " and " + MariusQLConstants.M_DATATYPE_COLLECTION_TYPE_ATTRIBUTE_NAME + " = " + collectionDatatypeRid
				+ " and " + MariusQLConstants.M_DATATYPE_IS_SIMPLE_TYPE_ATTRIBUTE_NAME + " = 0";

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

	public String getUsedProperties(Long classId) {
		String c = StringHelper.generateAlias("mc", 1);
		String d = StringHelper.generateAlias("md", 1);
		String p = StringHelper.generateAlias("mp", 1);

		String query = "select " + SQLHelper.aliasColumn(MariusQLConstants.M_CLASS_USED_PROPERTIES_ATTRIBUTE_NAME, c)
				+ " from " + SQLHelper.aliasTable(MariusQLConstants.M_DATATYPE_TABLE_NAME, d) + " , "
				+ SQLHelper.aliasTable(MariusQLConstants.M_PROPERTY_TABLE_NAME, p) + " , "
				+ SQLHelper.aliasTable(MariusQLConstants.M_CLASS_TABLE_NAME, c) + " where "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_DATATYPE_DTYPE_ATTRIBUTE_NAME, d) + " = "
				+ DatatypeEnum.DATATYPEREFERENCE.toSQL() + " and "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_DATATYPE_ON_CLASS_ATTRIBUTE_NAME, d) + " = " + classId
				+ " and " + SQLHelper.aliasColumn(MariusQLConstants.RID_COLUMN_NAME, d) + " = "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_PROPERTY_RANGE_ATTRIBUTE_NAME, p) + " and "
				+ SQLHelper.aliasColumn(MariusQLConstants.M_CLASS_IS_EXTENSION_ATTRIBUTE_NAME, c) + " = 1 ";

		try {
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);
			List<Long> rids = new ArrayList<Long>();
			Object data = new Object();

			while (resultSet.next()) {
				data = resultSet.getObject(1);
				rids.addAll(this.getArrayValues(data));
			}

			statement.close();

			if (!rids.isEmpty()) {
				return this.getCollectionValues(rids);
			}
			return null;

		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	public List<Long> getArrayValues(Object data) {
		BigDecimal[] values;
		List<Long> arrayValues = new ArrayList<Long>();
		try {
			values = (BigDecimal[]) ((ARRAY) data).getArray();
			for (int i = 0; i < values.length; i++) {
				BigDecimal out_value = (BigDecimal) values[i];
				arrayValues.add(out_value.longValue());
			}

			return arrayValues;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getCollectionValues(List<Long> pValues) {
		if (pValues != null) {
			String returnValue = "";

			for (Long current : pValues) {
				returnValue = returnValue + current + ",";
			}

			if (!returnValue.isEmpty()) {
				returnValue = returnValue.substring(0, returnValue.length() - 1);
			}
			return MariusQLConstants.OPEN_PARENTHESIS + returnValue + MariusQLConstants.CLOSE_PARENTHESIS;
		}
		return null;
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
				+ SQLHelper.aliasColumn(MariusQLConstants.M_CLASS_IS_EXTENSION_ATTRIBUTE_NAME, c) + " = 1 " + " and ( "
				+ SQLHelper.aliasColumn(MariusQLConstants.RID_COLUMN_NAME, p) + " in " + this.getUsedProperties(classId)
				+ " ) " + " and " + SQLHelper.aliasColumn(MariusQLConstants.RID_COLUMN_NAME, c) + " = "
				+ SQLHelper.aliasColumn(MariusQLConstants.SCOPE_CORE_ATTRIBUTE_NAME, p);

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

	public String getUsedPropertyWithCollectionOfReferenceByOnClass(Long internalId) {
		String c = StringHelper.generateAlias("mc", 1);
		String d1 = StringHelper.generateAlias("md", 1);
		String d2 = StringHelper.generateAlias("md2", 1);
		String p = StringHelper.generateAlias("mp", 1);

		String query = "select " + SQLHelper.aliasColumn(MariusQLConstants.M_CLASS_USED_PROPERTIES_ATTRIBUTE_NAME, c)
				+ " from " + SQLHelper.aliasTable(MariusQLConstants.M_DATATYPE_TABLE_NAME, d1) + " , "
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
				+ SQLHelper.aliasColumn(MariusQLConstants.M_CLASS_IS_EXTENSION_ATTRIBUTE_NAME, c) + " = 1 " + " )";

		try {
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);
			List<Long> rids = new ArrayList<Long>();
			Object data = new Object();

			while (resultSet.next()) {
				data = resultSet.getObject(1);
				rids.addAll(this.getArrayValues(data));
			}

			statement.close();

			if (!rids.isEmpty()) {
				return this.getCollectionValues(rids);
			}
			return null;

		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
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
				+ SQLHelper.aliasColumn(MariusQLConstants.M_CLASS_IS_EXTENSION_ATTRIBUTE_NAME, c) + " = 1 " + " and ( "
				+ SQLHelper.aliasColumn(MariusQLConstants.RID_COLUMN_NAME, p) + " IN "
				+ this.getUsedPropertyWithCollectionOfReferenceByOnClass(internalId) + " ) ) " + " and "
				+ SQLHelper.aliasColumn(MariusQLConstants.RID_COLUMN_NAME, c) + " = "
				+ SQLHelper.aliasColumn(MariusQLConstants.SCOPE_CORE_ATTRIBUTE_NAME, p);

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

	@Override
	public List<String> getReferencedDataCollectionByPropertyWithCondition(String tableName,
			String referencedProperty) {
		String query = "select " + referencedProperty + " from " + tableName;

		try {
			List<String> values = new ArrayList<String>();
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				values.add(this.getSession().getMariusQLTypes().getJDBCArray(resultSet.getObject(1)));
			}

			statement.close();

			return values;
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
				} else if (column.getRange() instanceof DatatypeCollection) {
					Object array = resultSet.getObject(1);
					if (array != null) {
						value = this.getSession().getMariusQLTypes().getJDBCArray(array);
					} else {
						value = "{}";
					}
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
	public String getRefsValues(String tableName, IdentNode pathPropNode) {
		String propName = pathPropNode.toString();

		if (pathPropNode.toString().contains(".")) {
			propName = pathPropNode.toString().split(Pattern.quote("."))[1];
		}

		String query = "select " + propName + " from " + tableName;

		try {
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);
			List<Long> rids = new ArrayList<Long>();
			Object data = new Object();

			while (resultSet.next()) {
				data = resultSet.getObject(1);
				rids.addAll(this.getArrayValues(data));
			}

			statement.close();

			if (!rids.isEmpty()) {
				return this.getCollectionValues(rids);
			}
			return null;

		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
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
			return resultSet.next();
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public int getCountRow(String tableName, boolean isAccurate) {
		throw new NotYetImplementedException();
	}

	@Override
	public String getEmptyQuery() {
		throw new NotYetImplementedException();
	}
}
