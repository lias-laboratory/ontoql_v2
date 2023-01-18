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
package fr.ensma.lias.mariusql.driver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.Category;
import fr.ensma.lias.mariusql.core.Datatype;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.metamodel.Language;
import fr.ensma.lias.mariusql.core.metamodel.MMAttribute;
import fr.ensma.lias.mariusql.core.metamodel.MMDatatypeCollection;
import fr.ensma.lias.mariusql.core.metamodel.MMDatatypeReference;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.core.metamodel.MMMultiAttribute;
import fr.ensma.lias.mariusql.core.model.MDatatypeCollection;
import fr.ensma.lias.mariusql.core.model.MDatatypeCount;
import fr.ensma.lias.mariusql.core.model.MDatatypeEnumerate;
import fr.ensma.lias.mariusql.core.model.MDatatypeReference;
import fr.ensma.lias.mariusql.core.model.MGenericClass;
import fr.ensma.lias.mariusql.engine.tree.IdentNode;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.util.MariusQLHelper;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * @author Ghada TRIKI
 * @author Valentin CASSAIR
 */
public abstract class AbstractMariusQLDQLDriver extends AbstractMariusQLDriver implements MariusQLDQLDriver {

	public AbstractMariusQLDQLDriver(MariusQLSession pSession) {
		super(pSession);
	}

	@Override
	public MMEntity getMMEntityByRid(Long rid) {
		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + ", "
				+ MariusQLConstants.MM_ENTITY_NAME_ATTRIBUTE_NAME + ","
				+ MariusQLConstants.MM_ENTITY_MAPPEDTABLENAME_ATTRIBUTE_NAME + ","
				+ MariusQLConstants.MM_ENTITY_IS_CORE_ATTRIBUTE_NAME + ","
				+ MariusQLConstants.MM_ENTITY_IS_META_META_MODEL_ATTRIBUTE_NAME + " from "
				+ MariusQLConstants.MM_ENTITY_TABLE_NAME + " where " + MariusQLConstants.RID_COLUMN_NAME + " = " + rid;

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
	public List<MMEntity> getMMEntityFromMetaMetaModel() {
		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + ", "
				+ MariusQLConstants.MM_ENTITY_NAME_ATTRIBUTE_NAME + ","
				+ MariusQLConstants.MM_ENTITY_MAPPEDTABLENAME_ATTRIBUTE_NAME + ","
				+ MariusQLConstants.MM_ENTITY_IS_META_META_MODEL_ATTRIBUTE_NAME + " from "
				+ MariusQLConstants.MM_ENTITY_TABLE_NAME + " where "
				+ MariusQLConstants.MM_ENTITY_IS_META_META_MODEL_ATTRIBUTE_NAME + " = true";

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
				+ MariusQLConstants.MM_ENTITY_IS_CORE_ATTRIBUTE_NAME + " = true";

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
	public Datatype getMMDataTypeByRid(Long rid) {
		String query = "select " + MariusQLConstants.MM_DATATYPE_DTYPE_ATTRIBUTE_NAME + ", "
				+ MariusQLConstants.MM_DATATYPE_ON_CLASS_ATTRIBUTE_NAME + ", "
				+ MariusQLConstants.MM_DATATYPE_COLLECTION_TYPE_ATTRIBUTE_NAME + ", "
				+ MariusQLConstants.MM_DATATYPE_IS_MANY_TO_MANY_ATTRIBUTE_NAME + ", "
				+ MariusQLConstants.MM_DATATYPE_REVERSE_ATTRIBUTE_ATTRIBUTE_NAME + " from "
				+ MariusQLConstants.MM_DATATYPE_TABLE_NAME + " where " + MariusQLConstants.RID_COLUMN_NAME + " = "
				+ rid;

		try {
			Datatype datatype = null;
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet.next()) {
				String dtype = resultSet.getString(1);
				Long classId = resultSet.getLong(2);
				Long collectionType = resultSet.getLong(3);
				boolean isManyToMany = resultSet.getBoolean(4);

				if (dtype.equalsIgnoreCase(Datatype.COLLECTION_NAME)) {
					datatype = new MMDatatypeCollection(this.getSession(), rid, this.getMMDataTypeByRid(collectionType),
							isManyToMany);
				} else if (dtype.equalsIgnoreCase(Datatype.ASSOCIATION_NAME)) {
					datatype = new MMDatatypeReference(this.getSession(), rid, classId);
				} else {
					datatype = FactoryCore.createMMSimpleDatatype(getSession(), MariusQLHelper.getDatatypeEnum(dtype));
				}
			}

			statement.close();

			return datatype;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public Long getMGenericClassRidByIdentifier(String tableName, String constraintName, String value) {
		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + " from " + tableName + " where " + constraintName
				+ " = " + value;

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
	public boolean isMGenericClassExists(String identifierName, String identifierValue) {
		String defaultNamespaceValue = this.getSession().getDefaultNameSpace();

		final String packageConstraint = MariusQLConstants.PACKAGE_NAME_CORE_ATTRIBUTE_NAME
				+ (defaultNamespaceValue == null ? " is null"
						: " = " + StringHelper.addSimpleQuotedString(defaultNamespaceValue));
		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + " from " + MariusQLConstants.M_CLASS_TABLE_NAME
				+ " where " + identifierName + " = " + identifierValue + " AND " + packageConstraint;
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
	public boolean isMPropertyExists(Long scope, String constraintName, String value) {
		final Long mPropertyId = this.getMPropertyId(scope, constraintName, value);

		return mPropertyId != null;
	}

	@Override
	public Long getMPropertyId(Long scope, String constraintName, String constraintValue) {
		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + " from "
				+ MariusQLConstants.M_PROPERTY_TABLE_NAME + " where " + constraintName + " = " + constraintValue
				+ " AND " + MariusQLConstants.SCOPE_CORE_ATTRIBUTE_NAME + " = " + scope;

		try {
			Long result = null;
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet.next()) {
				result = resultSet.getLong(1);
			}

			statement.close();

			return result;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public List<MMAttribute> getMMAttributeFromScopeRid(MMEntity pScope) {
		if (pScope == null) {
			throw new MariusQLException("parameters error");
		}

		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + ", "
				+ MariusQLConstants.MM_ATTRIBUTE_NAME_ATTRIBUTE_NAME + ", "
				+ MariusQLConstants.MM_ATTRIBUTE_RANGE_ATTRIBUTE_NAME + ", "
				+ MariusQLConstants.MM_ATTRIBUTE_IS_OPTIONAL_ATTRIBUTE_NAME + " , "
				+ MariusQLConstants.MM_ATTRIBUTE_IS_CORE_ATTRIBUTE_NAME + " from "
				+ MariusQLConstants.MM_ATTRIBUTE_TABLE_NAME + " where "
				+ MariusQLConstants.MM_ATTRIBUTE_SCOPE_ATTRIBUTE_NAME + " = " + pScope.getInternalId();

		List<MMAttribute> attributes = new ArrayList<MMAttribute>();

		try {
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				Datatype datatype = FactoryCore.createMMDatatype(getSession(), resultSet.getLong(3));
				MMAttribute attribute = null;

				if (datatype != null && MariusQLConstants.MULTISTRING_NAME.equalsIgnoreCase(datatype.getName())) {
					attribute = new MMMultiAttribute(this.getSession(), resultSet.getLong(1), resultSet.getString(2),
							pScope, resultSet.getLong(3), resultSet.getBoolean(4));
				} else {
					attribute = new MMAttribute(this.getSession(), resultSet.getLong(1), resultSet.getString(2), pScope,
							resultSet.getLong(3), resultSet.getBoolean(4));
				}
				attribute.setIsCore(resultSet.getBoolean(5));
				attribute.setCurrentContext(pScope);
				attributes.add(attribute);
			}

			statement.close();

			return attributes;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public List<MMAttribute> getMMAttributeByRange(Datatype ref) {
		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + ", "
				+ MariusQLConstants.MM_ATTRIBUTE_NAME_ATTRIBUTE_NAME + ", "
				+ MariusQLConstants.MM_ATTRIBUTE_SCOPE_ATTRIBUTE_NAME + ", "
				+ MariusQLConstants.MM_ATTRIBUTE_RANGE_ATTRIBUTE_NAME + ", "
				+ MariusQLConstants.MM_ATTRIBUTE_IS_OPTIONAL_ATTRIBUTE_NAME + " from "
				+ MariusQLConstants.MM_ATTRIBUTE_TABLE_NAME + " where "
				+ MariusQLConstants.MM_ATTRIBUTE_RANGE_ATTRIBUTE_NAME + " = " + ref.getInternalId();

		List<MMAttribute> attributes = new ArrayList<MMAttribute>();

		try {
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				MMEntity scope = FactoryCore.createExistingMMEntity(this.getSession(), resultSet.getLong(3));
				attributes.add(scope.getDefinedMMAttribute(resultSet.getString(2)));
			}

			statement.close();

			return attributes;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public String getMEntityDescriminatorIdentifier(String tableName, String constraintName, String value) {
		if (tableName == null || constraintName == null || value == null) {
			throw new MariusQLException("parameters error");
		}

		String query = "select " + MariusQLConstants.DESCRIMINATOR_CORE_ATTRIBUTE_NAME + " from " + tableName
				+ " where " + constraintName + " = " + value;
		try {
			String identifier = null;
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet.next()) {
				identifier = resultSet.getString(1);
			}

			statement.close();

			return identifier;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public String getSuperMMEntityNameByRid(Long pRid) {
		if (pRid == null) {
			throw new MariusQLException("parameters error");
		}

		String query = "select " + MariusQLConstants.MM_ENTITY_NAME_ATTRIBUTE_NAME + " from "
				+ MariusQLConstants.MM_ENTITY_TABLE_NAME + " where rid = (select "
				+ MariusQLConstants.MM_ENTITY_SUPERENTITY_ATTRIBUTE_NAME + " from "
				+ MariusQLConstants.MM_ENTITY_TABLE_NAME + " where rid = " + pRid + ")";

		try {
			String entityName = null;
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet.next()) {
				entityName = resultSet.getString(1);
			}

			statement.close();

			return entityName;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public List<String> getSubMMEntityNamesByRid(Long pRid) {
		if (pRid == null) {
			throw new MariusQLException("parameters error");
		}

		String query = "select " + MariusQLConstants.MM_ENTITY_NAME_ATTRIBUTE_NAME + " from "
				+ MariusQLConstants.MM_ENTITY_TABLE_NAME + " where "
				+ MariusQLConstants.MM_ENTITY_SUPERENTITY_ATTRIBUTE_NAME + " = " + pRid;

		try {
			List<String> entityNames = new ArrayList<String>();
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				entityNames.add(resultSet.getString(1));
			}

			statement.close();

			return entityNames;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public List<Category> getMMEntityBySuperEntity(Long parentRid) {
		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + ", "
				+ MariusQLConstants.MM_ENTITY_NAME_ATTRIBUTE_NAME + ","
				+ MariusQLConstants.MM_ENTITY_MAPPEDTABLENAME_ATTRIBUTE_NAME + ","
				+ MariusQLConstants.MM_ENTITY_IS_CORE_ATTRIBUTE_NAME + ","
				+ MariusQLConstants.MM_ENTITY_IS_META_META_MODEL_ATTRIBUTE_NAME + " from "
				+ MariusQLConstants.MM_ENTITY_TABLE_NAME + " where "
				+ MariusQLConstants.MM_ENTITY_SUPERENTITY_ATTRIBUTE_NAME + " = " + parentRid;

		try {
			List<Category> children = new ArrayList<Category>();
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				children.add(new MMEntity(this.getSession(), resultSet.getLong(1), resultSet.getString(2),
						resultSet.getString(3), resultSet.getBoolean(4), resultSet.getBoolean(5)));
			}

			statement.close();

			return children;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public Long getLastMMDatatypeReferenceByOnClass(Category category) {
		String query = "select max(" + MariusQLConstants.RID_COLUMN_NAME + ") from "
				+ MariusQLConstants.MM_DATATYPE_TABLE_NAME + " where "
				+ MariusQLConstants.MM_DATATYPE_DTYPE_ATTRIBUTE_NAME.toLowerCase() + " = " + "'ref'" + " and "
				+ MariusQLConstants.MM_DATATYPE_IS_SIMPLE_TYPE_ATTRIBUTE_NAME + " = false" + " and "
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
	public int getNumberOfMGenericClassChildren(MGenericClass mGenericClass) {
		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + " from " + MariusQLConstants.M_CLASS_TABLE_NAME
				+ " where " + MariusQLConstants.M_CLASS_SUPERCLASS_ATTRIBUTE_NAME + " = "
				+ mGenericClass.getInternalId();

		try {
			int count = 0;
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				count++;
			}

			statement.close();

			return count;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public List<String> getSubMClassIdentifierByRid(Long pRid) {
		if (pRid == null) {
			throw new MariusQLException("parameters error");
		}

		String identifier = null;

		if (this.getSession().isNoReferenceLanguage()) {
			identifier = MariusQLConstants.M_CLASS_CODE_ATTRIBUTE_NAME;
		} else {
			identifier = MariusQLConstants.NAME_CORE_ATTRIBUTE_NAME + MariusQLConstants.UNDERSCORE
					+ this.getSession().getReferenceLanguage();
		}

		String query = "select " + identifier.toLowerCase() + " from " + MariusQLConstants.M_CLASS_TABLE_NAME
				+ " where " + MariusQLConstants.M_CLASS_SUPERCLASS_ATTRIBUTE_NAME + " = " + pRid;

		try {
			List<String> classNames = new ArrayList<String>();
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				classNames.add(resultSet.getString(1));
			}

			statement.close();

			return classNames;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public Datatype getMDataTypeByRid(Long rid) {
		String query = "select " + MariusQLConstants.M_DATATYPE_DTYPE_ATTRIBUTE_NAME + ", "
				+ MariusQLConstants.M_DATATYPE_ON_CLASS_ATTRIBUTE_NAME + ", "
				+ MariusQLConstants.M_DATATYPE_COLLECTION_TYPE_ATTRIBUTE_NAME + " from "
				+ MariusQLConstants.M_DATATYPE_TABLE_NAME + " where " + MariusQLConstants.RID_COLUMN_NAME + " = " + rid;

		try {
			Datatype datatype = null;
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet.next()) {
				String dtype = resultSet.getString(1);
				Long classId = resultSet.getLong(2);

				if (dtype.equalsIgnoreCase(Datatype.ASSOCIATION_NAME)) {
					datatype = new MDatatypeReference(this.getSession(), rid, classId);
				} else if (dtype.equalsIgnoreCase(Datatype.COLLECTION_NAME)) {
					Datatype collectionDataType = FactoryCore.createMDatatype(this.getSession(), resultSet.getLong(3));
					datatype = new MDatatypeCollection(this.getSession(), rid, collectionDataType);
				} else if (dtype.equalsIgnoreCase(Datatype.COUNT_TYPE_NAME)) {
					datatype = new MDatatypeCount(this.getSession(), rid);
				} else if (dtype.equalsIgnoreCase(Datatype.ENNUMERATE_NAME)) {
					datatype = new MDatatypeEnumerate(this.getSession(), rid);
				} else {
					datatype = FactoryCore.createMSimpleDatatype(getSession(), MariusQLHelper.getDatatypeEnum(dtype));
				}
			}
			statement.close();

			return datatype;
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
				+ MariusQLConstants.M_DATATYPE_IS_SIMPLE_TYPE_ATTRIBUTE_NAME + " = false";

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
				+ " and " + MariusQLConstants.M_DATATYPE_IS_SIMPLE_TYPE_ATTRIBUTE_NAME + " = false";

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
	public boolean isDataReferencedByProperty(String tableName, String refPropertyName) {
		String query = "select " + refPropertyName + " from " + tableName;

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
	public boolean isReferencedDataByPropertyWithCondition(String tableName, String refPropertyName, Long value) {

		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + " from " + tableName + " where "
				+ refPropertyName + " = " + value;

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
	public List<Long> getDataRid(String tableName, List<String> properties, List<String> values) {

		StringBuilder condition = new StringBuilder();

		for (int i = 0; i < properties.size(); i++) {
			condition.append(properties.get(i) + " = " + values.get(i));
			if (i != properties.size() - 1) {
				condition.append(" and ");
			}
		}

		String query = "select " + MariusQLConstants.RID_COLUMN_NAME + " from " + tableName + " where " + condition;
		try {
			List<Long> rids = new ArrayList<Long>();
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				rids.add(resultSet.getLong(1));
			}

			statement.close();

			return rids;
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
				values.add(resultSet.getString(1));
			}

			statement.close();

			return values;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public boolean isLanguageExists(String languageName, String languageCode) {
		String query = "select " + MariusQLConstants.LANGUAGE_CODE + " from " + MariusQLConstants.LANGUAGE_TABLE_NAME
				+ " where " + MariusQLConstants.LANGUAGE_CODE + " = " + StringHelper.addSimpleQuotedString(languageCode)
				+ " AND " + MariusQLConstants.LANGUAGE_NAME + " = " + StringHelper.addSimpleQuotedString(languageName);

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
	public List<Language> getLanguages() {
		String query = "select " + MariusQLConstants.LANGUAGE_NAME + ", " + MariusQLConstants.LANGUAGE_CODE + ", "
				+ MariusQLConstants.LANGUAGE_DESCRIPTION + " from " + MariusQLConstants.LANGUAGE_TABLE_NAME;

		List<Language> languageElements = new ArrayList<Language>();

		try {
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				languageElements.add(new Language(this.getSession(), resultSet.getString(1), resultSet.getString(2),
						resultSet.getString(3)));
			}

			statement.close();

			return languageElements;
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public String getMariusQLVersion() {
		String query = "select " + MariusQLConstants.METAVALUE_COLUMN_NAME + " from "
				+ MariusQLConstants.METADATA_TABLE_NAME + " where " + MariusQLConstants.METANAME_COLUMN_NAME + " = '"
				+ MariusQLConstants.METANAME_VERSION + "'";

		try {
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet.next()) {
				String version = resultSet.getString(1);
				statement.close();

				return version;
			} else {
				throw new MariusQLException("Unable to retrieve the current version");
			}
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}
	}

	@Override
	public String getRefsValues(String tableName, IdentNode pathPropNode) {
		return pathPropNode.getSQL();
	}

	@Override
	public List<String> getAllNameSpaces() {
		String query = "select distinct(package) from m_class";

		List<String> result = new ArrayList<String>();
		try {
			Statement statement = this.getSession().createSQLStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				result.add(resultSet.getString(1));
			}

			statement.close();
		} catch (SQLException e) {
			throw new MariusQLException(e);
		}

		return result;
	}
}
