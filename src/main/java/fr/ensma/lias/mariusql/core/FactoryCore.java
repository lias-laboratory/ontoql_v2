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
package fr.ensma.lias.mariusql.core;

import java.util.List;

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.metamodel.Language;
import fr.ensma.lias.mariusql.core.metamodel.MMAttribute;
import fr.ensma.lias.mariusql.core.metamodel.MMDatatypeBoolean;
import fr.ensma.lias.mariusql.core.metamodel.MMDatatypeCollection;
import fr.ensma.lias.mariusql.core.metamodel.MMDatatypeInt;
import fr.ensma.lias.mariusql.core.metamodel.MMDatatypeMultiString;
import fr.ensma.lias.mariusql.core.metamodel.MMDatatypeReal;
import fr.ensma.lias.mariusql.core.metamodel.MMDatatypeReference;
import fr.ensma.lias.mariusql.core.metamodel.MMDatatypeString;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.core.model.IdentifierEnum;
import fr.ensma.lias.mariusql.core.model.MClass;
import fr.ensma.lias.mariusql.core.model.MDatatypeBoolean;
import fr.ensma.lias.mariusql.core.model.MDatatypeCollection;
import fr.ensma.lias.mariusql.core.model.MDatatypeCount;
import fr.ensma.lias.mariusql.core.model.MDatatypeEnumerate;
import fr.ensma.lias.mariusql.core.model.MDatatypeInt;
import fr.ensma.lias.mariusql.core.model.MDatatypeMultiString;
import fr.ensma.lias.mariusql.core.model.MDatatypeReal;
import fr.ensma.lias.mariusql.core.model.MDatatypeReference;
import fr.ensma.lias.mariusql.core.model.MDatatypeString;
import fr.ensma.lias.mariusql.core.model.MDatatypeURI;
import fr.ensma.lias.mariusql.core.model.MGenericClass;
import fr.ensma.lias.mariusql.core.model.MPredefinedProperty;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.core.model.MStaticClass;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotYetImplementedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.util.MariusQLHelper;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * Implementation on OntoDB of AbstractFactory.
 * 
 * @author Mickael BARON
 * @author Ghada TRIKI
 * @author Valentin CASSAIR
 * @author Florian MHUN
 */
public class FactoryCore {

	public static MMEntity createNewMMEntity(MariusQLSession session, String identifier) {
		if (FactoryCore.isMMEntityExists(identifier, session)) {
			throw new MariusQLException("Entity already exists: " + identifier);
		}

		return new MMEntity(session, identifier, MariusQLConstants.M_CLASS_TABLE_NAME);
	}

	public static MMEntity createExistingMMEntity(MariusQLSession session, String identifier) {
		if (session.getMetaModelCache().isElementExists(identifier)) {
			return session.getMetaModelCache().getElement(identifier);
//	    MMEntity mmentity =  session.getMetaModelCache().getElement(identifier);
//	    mmentity = new MMEntity(session, mmentity.getInternalId(), mmentity.getName(), mmentity.getMappedElement(), mmentity.isCore(), mmentity.isMetaMetaModel());
//	    return mmentity;
		}

		MMEntity mmentity = session.getMariusQLDQL().getMMEntityByName(identifier);
		if (mmentity == null) {
			throw new MariusQLException("Entity not found: " + identifier);
		}

		session.getMetaModelCache().addElement(mmentity);

		return mmentity;
	}

	public static MMEntity createExistingMMEntity(MariusQLSession session, Long rid) {
		if (session.getMetaModelCache().isElementExists(rid)) {
			return session.getMetaModelCache().getElement(rid);
		}

		MMEntity mmentity = session.getMariusQLDQL().getMMEntityByRid(rid);
		if (mmentity == null) {
			throw new MariusQLException("Entity not found: " + rid);
		}

		session.getMetaModelCache().addElement(mmentity);

		return mmentity;
	}

	public static Datatype createMSimpleDatatype(MariusQLSession session, DatatypeEnum datatypeName) {
		if (session.getModelCache().isSimpleTypeExists(datatypeName)) {
			return session.getModelCache().getSimpleDatatype(datatypeName);
		}

		Long rid = session.getMariusQLDQL().getMSimpleDataTypeRid(datatypeName.getName());
		if (rid == null) {
			throw new MariusQLException("The rid of the simpletype is null.");
		}

		Datatype datatype;

		switch (datatypeName) {
		case DATATYPEINT: {
			datatype = new MDatatypeInt(session, rid);
			break;
		}
		case DATATYPEREAL: {
			datatype = new MDatatypeReal(session, rid);
			break;
		}
		case DATATYPESTRING: {
			datatype = new MDatatypeString(session, rid);
			break;
		}
		case DATATYPEBOOLEAN: {
			datatype = new MDatatypeBoolean(session, rid);
			break;
		}
		case DATATYPEMULTISTRING: {
			datatype = new MDatatypeMultiString(session, rid);
			break;
		}
		case DATATYPEURI: {
			datatype = new MDatatypeURI(session, rid);
			break;
		}
		default: {
			throw new MariusQLException("not a simple type: " + datatypeName.getName());
		}
		}

		return datatype;
	}

	public static Datatype createMMSimpleDatatype(MariusQLSession session, DatatypeEnum datatypeName) {
		if (session.getMetaModelCache().isSimpleTypeExists(datatypeName)) {
			return session.getMetaModelCache().getSimpleDatatype(datatypeName);
		}

		Long rid = session.getMariusQLDQL().getMMSimpleDataTypeRid(datatypeName.getName());
		if (rid == null) {
			throw new MariusQLException("The rid is null");
		}

		Datatype datatype;

		switch (datatypeName) {
		case DATATYPEINT: {
			datatype = new MMDatatypeInt(session, rid);
			break;
		}
		case DATATYPEREAL: {
			datatype = new MMDatatypeReal(session, rid);
			break;
		}
		case DATATYPESTRING: {
			datatype = new MMDatatypeString(session, rid);
			break;
		}
		case DATATYPEBOOLEAN: {
			datatype = new MMDatatypeBoolean(session, rid);
			break;
		}
		case DATATYPEMULTISTRING: {
			datatype = new MMDatatypeMultiString(session, rid);
			break;
		}
		default: {
			throw new MariusQLException("not a simple type: " + datatypeName.getName());
		}
		}

		return datatype;
	}

	public static Datatype createNewMMDatatypeCollection(MariusQLSession session, Datatype collectionDatatype) {
		MMDatatypeCollection collection = new MMDatatypeCollection(session, collectionDatatype, false);
		collection.insert();

		return collection;
	}

	public static Datatype createNewMDatatypeCollection(MariusQLSession session, Datatype collectionDatatype) {
		MDatatypeCollection collection = null;// session.getMariusQLDQL().getMDatatypeCollectionByDatatype(collectionDatatype);

		if (collection == null) {
			collection = new MDatatypeCollection(session, collectionDatatype, false);
			collection.insert();
		}

		return collection;
	}

	public static Datatype createMMDatatype(MariusQLSession session, Long rid) {
		Datatype datatype = session.getMetaModelCache().getSimpleDatatype(rid);

		if (datatype == null) {
			datatype = session.getMetaModelCache().getDatatype(rid);

			if (datatype == null) {
				datatype = session.getMariusQLDQL().getMMDataTypeByRid(rid);
				session.getMetaModelCache().addDatatype(rid, datatype);
			}
		}

		if (datatype == null) {
			throw new MariusQLException("meta model datatype not found: " + rid);
		}

		return datatype;
	}

	public static Datatype createMDatatype(MariusQLSession session, Long rid) {
		Datatype datatype = session.getModelCache().getSimpleDatatype(rid);

		if (datatype == null) {
			datatype = session.getMariusQLDQL().getMDataTypeByRid(rid);
		}

		if (datatype == null) {
			throw new MariusQLException("model datatype not found: " + rid);
		}

		return datatype;
	}

	private static MGenericClass createNewModelClass(MariusQLSession session, MMEntity pClassEntity,
			String identifier) {
		String classIdentifierType = MariusQLHelper.getIdentifierAttributeName(identifier, session);
		String classIdentifier = MariusQLHelper.getCleanIdentifier(identifier, session);

		MMAttribute identifierAttribute = pClassEntity.getDefinedMMAttribute(classIdentifierType);
		if (FactoryCore.isMGenericClassExists(identifierAttribute, classIdentifier, session)) {
			throw new MariusQLException("Class already exists.");
		}

		MGenericClass klass = null;

		if (pClassEntity.isAbstract()) {
			klass = new MStaticClass(session, pClassEntity);
		} else {
			klass = new MClass(session, pClassEntity);
		}

		klass.setValue(identifierAttribute, classIdentifier);

		return klass;
	}

	public static MStaticClass createNewMStaticClass(MariusQLSession session, MMEntity pClassEntity,
			String identifier) {
		if (!pClassEntity.isAbstract()) {
			throw new MariusQLException(
					"Can not create static class based on non-abstract entity: " + pClassEntity.getName());
		}

		final MStaticClass createNewModelClass = (MStaticClass) FactoryCore.createNewModelClass(session, pClassEntity,
				identifier);

		// Fetch from cache first.
		if (session.getModelCache().isElementExists(identifier)) {
			throw new NotYetImplementedException();
		} else {
			session.getModelCache().addElement(createNewModelClass);
		}

		return createNewModelClass;
	}

	public static MClass createNewMClass(MariusQLSession session, MMEntity pClassEntity, String identifier) {
		if (pClassEntity.isAbstract()) {
			throw new MariusQLException("Can not create class based on abstact entity: " + pClassEntity.getName());
		}

		final MClass createNewModelClass = (MClass) FactoryCore.createNewModelClass(session, pClassEntity, identifier);

		// Fetch from cache first.
		if (session.getModelCache().isElementExists(identifier)) {
			throw new NotYetImplementedException();
		} else {
			session.getModelCache().addElement(createNewModelClass);
		}

		return createNewModelClass;
	}

	public static boolean isStaticClass(MariusQLSession session, String identifier) {
		final MGenericClass klass = FactoryCore.createExistingModelClass(session, identifier);

		return klass != null && klass.isAbstract();
	}

	public static boolean isClass(MariusQLSession session, String identifier) {
		final MGenericClass klass = FactoryCore.createExistingModelClass(session, identifier);

		return klass != null && !klass.isAbstract();
	}

	public static MStaticClass createExistingMStaticClass(MariusQLSession session, Long rid) {
		return FactoryCore.createExistingMStaticClass(session, rid.toString());
	}

	public static MStaticClass createExistingMStaticClass(MariusQLSession session, String identifier) {
		final MGenericClass klass = FactoryCore.createExistingModelClass(session, identifier);

		if (klass != null && klass.isAbstract()) {
			return (MStaticClass) klass;
		} else {
			throw new MariusQLException("Static class not found: " + identifier);
		}
	}

	public static MClass createExistingMClass(MariusQLSession session, String identifier) {
		final MGenericClass klass = FactoryCore.createExistingModelClass(session, identifier);

		if (klass != null && !klass.isAbstract()) {
			return (MClass) klass;
		} else {
			throw new MariusQLException("Class not found: " + identifier);
		}
	}

	public static MClass createExistingMClass(MariusQLSession session, Long rid) {
		final MClass createExistingMClass = FactoryCore.createExistingMClass(session, rid.toString());
		return createExistingMClass;
	}

	public static MGenericClass createExistingMGenericClass(MariusQLSession session, Long rid) {
		return FactoryCore.createExistingMGenericClass(session, rid.toString());
	}

	public static MGenericClass createExistingMGenericClass(MariusQLSession session, String identifier) {
		MGenericClass klass = FactoryCore.createExistingModelClass(session, identifier);
		if (klass != null) {
			if (klass.isClass() && !isStaticClass(session, klass.getName())) {
				List<MProperty> definedProperties = ((MClass) klass).getDefinedProperties();
				for (int i = 0; i < definedProperties.size(); i++) {
					definedProperties.get(i).setCurrentContext(klass);
				}

				((MClass) klass).setDefinedProperties(definedProperties);
			}
			return klass;
		} else {
			throw new MariusQLException("Class or static class not found: " + identifier);
		}
	}

	private static MGenericClass createExistingModelClass(MariusQLSession session, String identifier) {
		String classIdentifierType = MariusQLHelper.getIdentifierAttributeName(identifier, session);
		String classIdentifier = MariusQLHelper.getCleanIdentifier(identifier, session);

		// Fetch from cache first
		if (session.getModelCache().isElementExists(classIdentifier)) {
			return session.getModelCache().getElement(classIdentifier);
		}

		// Load mclass entity for identifier attribute
		MMEntity modelClassEntity = FactoryCore.createExistingMMEntity(session,
				MariusQLConstants.CLASS_CORE_ENTITY_NAME);
		MMAttribute identifierAttribute = modelClassEntity.getDefinedMMAttribute(classIdentifierType);

		String identifierSqlValue = classIdentifier;
		if (!StringHelper.isNumeric(classIdentifierType)) {
			identifierSqlValue = StringHelper.addSimpleQuotedString(identifierSqlValue);
		}

		// Fetch type of existing class
		final String classTypeOf = session.getMariusQLDQL().getMEntityDescriminatorIdentifier(
				MariusQLConstants.M_CLASS_TABLE_NAME, identifierAttribute.toSQL(), identifierSqlValue);

		if (classTypeOf == null) {
			return null;
		}

		// load existing class entity
		MMEntity classEntity = FactoryCore.createExistingMMEntity(session, classTypeOf);
		if (classEntity == null) {
			throw new MariusQLException("The MMEntity is null");
		}

		// create class
		MGenericClass klass = null;
		if (classEntity.isAbstract()) {
			klass = new MStaticClass(session, classEntity);
		} else {
			klass = new MClass(session, classEntity);
		}

		klass.setValue(identifierAttribute, classIdentifier);

		session.getModelCache().addElement(klass);

		return klass;
	}

	public static MProperty createExistingMProperty(MariusQLSession session, Long rid) {
		final String typeOf = session.getMariusQLDQL().getMEntityDescriminatorIdentifier(
				MariusQLConstants.M_PROPERTY_TABLE_NAME, MariusQLConstants.RID_COLUMN_NAME, rid.toString());

		if (typeOf == null) {
			throw new MariusQLException("The identifier is null");
		}

		final MMEntity propertyEntity = FactoryCore.createExistingMMEntity(session, typeOf);

		final String propertyCode = session.getMariusQLDQL().getColumnValue(MariusQLConstants.M_PROPERTY_TABLE_NAME,
				rid.toString(), propertyEntity.getDefinedDescription(MariusQLConstants.M_PROPERTY_CODE_ATTRIBUTE_NAME));

		MProperty newProperty = null;

		if (propertyCode.equalsIgnoreCase(MariusQLConstants.RID_COLUMN_NAME)) {
			newProperty = new MPredefinedProperty(session, MariusQLConstants.RID_COLUMN_NAME, propertyEntity);
		} else {
			newProperty = new MProperty(session, propertyEntity);
		}

		newProperty.setInternalId(rid);

		return newProperty;
	}

	public static MPredefinedProperty createNewMRidProperty(MariusQLSession session, MClass scopeClass) {
		MPredefinedProperty property = new MPredefinedProperty(session, MariusQLConstants.RID_COLUMN_NAME,
				FactoryCore.createExistingMMEntity(session, MariusQLConstants.PROPERTY_CORE_ENTITY_NAME), scopeClass);

		property.setRange(FactoryCore.createMSimpleDatatype(session, DatatypeEnum.DATATYPEINT));

		return property;
	}

	public static MPredefinedProperty createNewMTypeOfProperty(MariusQLSession session, MClass scopeClass) {
		MPredefinedProperty property = new MPredefinedProperty(session, MariusQLConstants.TYPE_OF_ID_TOKEN_NAME,
				FactoryCore.createExistingMMEntity(session, MariusQLConstants.PROPERTY_CORE_ENTITY_NAME), scopeClass);

		property.setRange(FactoryCore.createMSimpleDatatype(session, DatatypeEnum.DATATYPEINT));

		return property;
	}

	public static MProperty createNewMProperty(MariusQLSession session, MMEntity pEntity, String identifier,
			MClass scopeClass) {
		String propertyIdentifierType = MariusQLHelper.getIdentifierAttributeName(identifier, session);
		String propertyIdentifier = MariusQLHelper.getCleanIdentifier(identifier, session);

		MProperty mProperty = new MProperty(session, pEntity, scopeClass);
		MMAttribute identifierAttribute = pEntity.getDefinedMMAttribute(propertyIdentifierType);

		mProperty.setValue(identifierAttribute, propertyIdentifier);

		// code is mandatory
		if (!propertyIdentifierType.equals(MariusQLConstants.CODE_CORE_ATTRIBUTE_NAME)) {
			mProperty.setValue(pEntity.getDefinedMMAttribute(MariusQLConstants.CODE_CORE_ATTRIBUTE_NAME),
					propertyIdentifier);
		}

		return mProperty;
	}

	public static Datatype createNewMMDatatypeReference(MariusQLSession session, String name) {
		MMEntity entity = FactoryCore.createExistingMMEntity(session, name);
		MMDatatypeReference ref = new MMDatatypeReference(session, entity);

		ref.insert();

		return ref;
	}

	public static Datatype createNewMDatatypeCount(MariusQLSession session) {
		final MDatatypeCount mDatatypeCount = new MDatatypeCount(session);

		mDatatypeCount.insert();

		return mDatatypeCount;
	}

	public static Datatype createNewMDatatypeEnumerate(MariusQLSession session, List<String> name) {
		final MDatatypeEnumerate mDatatypeEnumerate = new MDatatypeEnumerate(session);
		mDatatypeEnumerate.setValues(name);

		mDatatypeEnumerate.insert();

		return mDatatypeEnumerate;
	}

	public static Datatype createNewMDatatypeReference(MariusQLSession session, String name) {
		MDatatypeReference res;

		MClass mClass = FactoryCore.createExistingMClass(session, name);
		res = new MDatatypeReference(session, mClass);
		res.insert();

		return res;
	}

	public static boolean isMMEntityExists(String identifier, MariusQLSession session) {
		boolean exists = session.getMetaModelCache().isElementExists(identifier);

		if (exists) {
			return true;
		} else {
			return session.getMariusQLDQL().getMMEntityRidByName(identifier) != null;
		}
	}

	public static boolean isMGenericClassExists(MMAttribute identifierAttribute, String identifier,
			MariusQLSession session) {

		boolean exists = session.getModelCache().isElementExists(identifier);
		if (exists) {
			return true;
		} else {
			String identifierSqlValue = identifier;
			if (!identifierAttribute.getName().equals(IdentifierEnum.internal.getMappedSQL())) {
				identifierSqlValue = StringHelper.addSimpleQuotedString(identifierSqlValue);
			}
			return session.getMariusQLDQL().isMGenericClassExists(identifierAttribute.toSQL(), identifierSqlValue);
		}
	}

	public static Language createNewLanguage(MariusQLSession session, String name, String code, String description) {
		Language lg = new Language(session, name, code, description);
		lg.insert();
		return lg;
	}
}
