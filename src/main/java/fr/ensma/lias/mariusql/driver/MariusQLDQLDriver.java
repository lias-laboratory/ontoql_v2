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

import java.sql.SQLException;
import java.util.List;

import fr.ensma.lias.mariusql.core.Category;
import fr.ensma.lias.mariusql.core.Datatype;
import fr.ensma.lias.mariusql.core.Description;
import fr.ensma.lias.mariusql.core.metamodel.Language;
import fr.ensma.lias.mariusql.core.metamodel.MMAttribute;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.core.model.MDatatypeCollection;
import fr.ensma.lias.mariusql.core.model.MGenericClass;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.engine.tree.IdentNode;

/**
 * @author Mickael BARON
 * @author Ghada TRIKI
 * @author Florian MHUN
 * @author Adel GHAMNIA
 */
public interface MariusQLDQLDriver extends MariusQLDriver {

	/**
	 * Return rid for SimpleType of metamodel level.
	 * 
	 * @param pType
	 * @return
	 */
	Long getMMSimpleDataTypeRid(String pType);

	/**
	 * Return rid for SimpleType of model level.
	 * 
	 * @param intName
	 * @return
	 */
	Long getMSimpleDataTypeRid(String intName);

	/**
	 * @param rid
	 * @return
	 */
	Datatype getMMDataTypeByRid(Long rid);

	/**
	 * Return rid for a given metamodel Entity.
	 * 
	 * @param name
	 * @return
	 */
	Long getMMEntityRidByName(String name);

	/**
	 * @param pRid
	 * @return
	 */
	String getSuperMMEntityNameByRid(Long pRid);

	/**
	 * @param pRid
	 * @return
	 */
	List<String> getSubMMEntityNamesByRid(Long pRid);

	/**
	 * @param pRid
	 * @return
	 */
	List<String> getSubMClassIdentifierByRid(Long pRid);

	/**
	 * @param name
	 * @param scopeId
	 * @return
	 */
	Long getMMAttributeRidByName(String name, Long scopeId);

	/**
	 * @param name
	 * @return
	 */
	Long getMGenericClassRidByIdentifier(String tableName, String constraintName, String value);

	/**
	 * @param tableName
	 * @param constraintName
	 * @param value
	 * @return
	 */
	String getMEntityDescriminatorIdentifier(String tableName, String constraintName, String value);

	/**
	 * @param identifierName
	 * @param identifierValue
	 * @return
	 */
	boolean isMGenericClassExists(String identifierName, String identifierValue);

	/**
	 * @param tableName
	 * @param constraintName
	 * @param value
	 * @return
	 */
	boolean isMPropertyExists(Long scope, String constraintName, String value);

	/**
	 * @param scope
	 * @param constraintName
	 * @param constraintValue
	 * @return
	 */
	Long getMPropertyId(Long scope, String constraintName, String constraintValue);

	/**
	 * @return
	 */
	List<MMEntity> getMMEntityFromMetaMetaModel();

	/**
	 * @return
	 */
	List<MMEntity> getMMEntityFromMetaModelCore();

	/**
	 * @param rid
	 * @return
	 */
	List<MMAttribute> getMMAttributeFromScopeRid(MMEntity pScope);

	/**
	 * 
	 * @param ref
	 * @return
	 */
	List<MMAttribute> getMMAttributeByRange(Datatype ref);

	/**
	 * @param tableName
	 * @param columnAttribut
	 * @param rid
	 * @return
	 */
	String getColumnValue(String tableName, String rid, Description columnAttribut);

	/**
	 * @param parentRid
	 * @return
	 */
	List<Category> getMMEntityBySuperEntity(Long parentRid);

	/**
	 * 
	 * @param cagetory
	 * @return
	 */
	Long getLastMMDatatypeReferenceByOnClass(Category cagetory);

	/**
	 * @param mGenericClass
	 * @return
	 */
	int getNumberOfMGenericClassChildren(MGenericClass mGenericClass);

	/**
	 * 
	 * @param rid
	 * @return
	 */
	Datatype getMDataTypeByRid(Long rid);

	/**
	 * @param collectionDatatype
	 * @return
	 */
	MDatatypeCollection getMDatatypeCollectionByDatatype(Datatype collectionDatatype);

	/**
	 * @param collectionDatatypeRid
	 * @return
	 */
	Long getMDatatypeByCollectionDatatype(Long collectionDatatypeRid);

	/**
	 * @param entityRid
	 * @return
	 */
	boolean isMMEntityReferenced(Long entityRid);

	/**
	 * @param mclassRid
	 * @return
	 */
	boolean isMGenericClassReferenced(Long mclassRid);

	/**
	 * 
	 * @param internalId
	 * @return
	 */
	List<MProperty> getPropertyWithReferenceByOnClass(Long classId);

	/**
	 * 
	 * @param internalId
	 * @return
	 */
	List<MProperty> getPropertyWithCollectionOfReferenceByOnClass(Long internalId);

	/**
	 * 
	 * @param sql
	 * @param sql2
	 * @return
	 */
	boolean isDataReferencedByProperty(String sql, String sql2);

	/**
	 * 
	 * @param sql
	 * @param sql2
	 * @return
	 */
	boolean isReferencedDataByPropertyWithCondition(String sql, String sql2, Long value);

	/**
	 * 
	 * @param sql
	 * @param properties
	 * @param values
	 * @return
	 */
	List<Long> getDataRid(String sql, List<String> properties, List<String> values);

	/**
	 * 
	 * @param tableName
	 * @param referencedPropertie
	 * @return
	 */
	List<String> getReferencedDataCollectionByPropertyWithCondition(String tableName, String referencedPropertie);

	/**
	 * 
	 * @param mmAttribute
	 * @param entityiInternalId
	 * @return
	 */
	boolean isMMAttributeExists(MMAttribute mmAttribute, Long entityiInternalId);

	/**
	 * 
	 * @param identifier
	 * @return
	 */
	MMEntity getMMEntityByName(String identifier);

	/**
	 * 
	 * @param rid
	 * @return
	 */
	MMEntity getMMEntityByRid(Long rid);

	/**
	 * 
	 * @param propertyName
	 * @return
	 * @throws SQLException
	 */
	public boolean isMPropertyRangeOfCollectionDataType(String propertyName);

	/**
	 * 
	 * @param languageName
	 * @param languageCode
	 * @return
	 */
	boolean isLanguageExists(String languageName, String languageCode);

	/**
	 * 
	 * @return
	 */
	List<Language> getLanguages();

	/**
	 * 
	 * @return
	 */
	String getMariusQLVersion();

	/**
	 * 
	 * @param sql
	 * @param pathPropNode
	 * @return
	 */
	String getRefsValues(String tableName, IdentNode pathPropNode);

	/**
	 * @param tableName
	 * @param isAccurate
	 * @return
	 */
	int getCountRow(String tableName, boolean isAccurate);

	/**
	 * @return
	 */
	String getEmptyQuery();

	/**
	 * @return
	 */
	List<String> getAllNameSpaces();
}
