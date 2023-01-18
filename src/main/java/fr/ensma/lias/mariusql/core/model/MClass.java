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
package fr.ensma.lias.mariusql.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.Category;
import fr.ensma.lias.mariusql.core.Description;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.metamodel.MMAttribute;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotSupportedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.util.MariusQLHelper;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * @author Mickael BARON
 * @author Ghada TRIKI
 * @author Adel GHAMNIA
 * @author Florian MHUN
 */
public class MClass extends MGenericClass {
    
    /**
     * The predefined rid property.
     */
    private MPredefinedProperty ridProperty;
    
    /**
     * The predefined typeof property.
     */
    private MPredefinedProperty typeofProperty;
    
    /**
     * Store the used properties.
     */
    private List<MProperty> usedProperties;
    
    public MClass(MariusQLSession pSession, MMEntity pEntity) {
	super(pSession, pEntity);
	
	this.ridProperty = FactoryCore.createNewMRidProperty(
		pSession
		, this);
	
	this.typeofProperty = FactoryCore.createNewMTypeOfProperty(
		pSession
		, this);
    }
    
    public MPredefinedProperty getRidProperty() {
	return this.ridProperty;
    }
    
    public MPredefinedProperty getTypeOfProperty() {
	return this.typeofProperty;
    }
    
    public void setDefinedProperties(List<MProperty> pProperties) {	
	this.currentDelegate.setObjectValue(MariusQLConstants.PROPERTIES_CORE_ATTRIBUTE_NAME, pProperties);
    }

    @SuppressWarnings("unchecked")
    public List<MProperty> getDefinedProperties() {
	final List<?> collectionValue = this.currentDelegate.getCollectionValue(MariusQLConstants.PROPERTIES_CORE_ATTRIBUTE_NAME);
	List<MProperty> properties = (List<MProperty>)collectionValue;

	return properties;
    }
    
    public List<MProperty> getAllDefinedProperties() {
	MGenericClass mClass = this.getSuperClass();
	
	List<MProperty> properties = new ArrayList<MProperty>(); 
	properties.addAll(this.getDefinedProperties());
	
	if (mClass != null && !mClass.isAbstract() && mClass.isClass()) {
	    properties.addAll(((MClass)mClass).getAllDefinedProperties());
	}
	
	return properties;
    }
    
    public void setExtension(Boolean p) {
	this.currentDelegate.setBooleanValue(MariusQLConstants.IS_EXTENSION_CORE_ATTRIBUTE_NAME, p);
    }
    
    public boolean hasExtension() {
	return Boolean.toString(true)
		.equals(this.currentDelegate.getStringValue(MariusQLConstants.IS_EXTENSION_CORE_ATTRIBUTE_NAME));
    }
    
    public void setUsedProperties(List<MProperty> pUsedProperties) {
	this.currentDelegate.setObjectValue(MariusQLConstants.USED_PROPERTIES_CORE_ATTRIBUTE_NAME, pUsedProperties);
    }
    
    @SuppressWarnings("unchecked")
    public List<MProperty> getUsedProperties() {
	if (this.usedProperties == null) {
	    final List<?> collectionValue = this.currentDelegate.getCollectionValue(MariusQLConstants.USED_PROPERTIES_CORE_ATTRIBUTE_NAME);	    
	    this.usedProperties = ((List<MProperty>)collectionValue);
	}
	
	return this.usedProperties;
    }

    @Override
    public int create() {
	List<MProperty> usedProperties = this.getUsedProperties();
	List<String> propertiesSQL = new ArrayList<String>();

	for (MProperty mProperty : usedProperties) {
	    propertiesSQL.addAll(mProperty.getExtentDefinition());    
	}
	
	this.getSession().getMariusQLDDL().createTable(this.toSQL(), MariusQLConstants.RID_COLUMN_NAME,
		this.getSession().getMariusQLTypes().getReferenceType(),
		MariusQLConstants.INSTANCE_SEQUENCE_NAME, propertiesSQL);
	
	this.setExtension(true);
	
	List<String> changedColumns = Arrays.asList(MariusQLConstants.IS_EXTENSION_CORE_ATTRIBUTE_NAME, MariusQLConstants.USED_PROPERTIES_CORE_ATTRIBUTE_NAME);
	
	return this.currentDelegate.update(false, changedColumns);
    }

    public void addNewUsedProperties(List<MProperty> addedProperties) {
	List<MProperty> usedProperties = this.getUsedProperties();
	List<String> propertiesSQL = new ArrayList<String>();

	List<MProperty> addedUsedproperties = new ArrayList<MProperty>();
	for (MProperty propertyToAdd : addedProperties){
	    if (this.isUsedPropertyExists(propertyToAdd.getName())) {
		throw new MariusQLException("property " + propertyToAdd.getName() + " is already used in class " + this.getName());
	    }
	    
	    propertiesSQL.addAll(propertyToAdd.getExtentDefinition());
	    addedUsedproperties.add(propertyToAdd);
	}

	usedProperties.addAll(addedUsedproperties);
	this.setUsedProperties(usedProperties);

	this.currentDelegate.update(false, Arrays.asList(MariusQLConstants.USED_PROPERTIES_CORE_ATTRIBUTE_NAME));

	this.getSession().getMariusQLDDL().addTableColumns(this.toSQL(), propertiesSQL);
    }
    
    public MProperty getUsedProperty(String identifier) {
	List<MProperty> usedProperties = this.getUsedProperties();
	
	for (MProperty usedProperty : usedProperties){
	    if (usedProperty.getName().equals(identifier)) {
		return usedProperty;
	    }
	}
	
	return null;
    }
    
    public void removeUsedProperty(String removedProperty) {
	if (!this.isUsedPropertyExists(removedProperty)) {
	    throw new MariusQLException("property " + removedProperty + " is not used in class " + this.getName());
	}
	
	List<MProperty> usedProperties = this.getUsedProperties();
	MProperty toremove = this.getUsedProperty(removedProperty);

	List<String> propertiesSQL = new ArrayList<String>();
	List<String> sqlDescription = new ArrayList<String>();
	
  	if (toremove.getRange().isAssociationType()) {
  	    sqlDescription.add(toremove.toSQL());
  	    sqlDescription.add(toremove.getInstanceReferenceTableColumnName());
  	} else if (toremove.getRange().isCollectionAssociationType()) {
  	    sqlDescription.add(toremove.toSQL());
  	    sqlDescription.add(toremove.getInstanceReferenceCollectionTableColumnName());
  	} else {
  	    sqlDescription.add(toremove.toSQL());
  	}
  	
	propertiesSQL.addAll(sqlDescription);
	
	usedProperties.remove(toremove);
	this.setUsedProperties(usedProperties);
	this.currentDelegate.update(false, Arrays.asList(MariusQLConstants.USED_PROPERTIES_CORE_ATTRIBUTE_NAME));
	
	this.getSession().getMariusQLDDL().removeTableColumns(this.toSQL(), propertiesSQL);	
    }

    public String getTableName() {
	return MariusQLConstants.M_CLASS_TABLE_NAME;
    }

    public boolean isUsedPropertyExists(String identifier) {
	if (identifier.equals(MariusQLConstants.RID_COLUMN_NAME)) {
	    return true;
	}
	
	List<MProperty> usedProperties = this.getUsedProperties();
	
	for (MProperty currentProperty : usedProperties) {
	    if (identifier.equals(currentProperty.getName())) {
		return true;
	    }
	}
	
	return false;
    }
    
    public boolean isDefinedPropertyExists(String identifier) {
	return this.getDefinedProperty(identifier) != null;
    }
    
    private MProperty getDefinedProperty(String identifier) {
	List<MProperty> definedProperties = this.getAllDefinedProperties();
	
	boolean identifierIsAnAlias =  (this.getCategoryAlias() != null && this.getCategoryAlias().equals(identifier));
	
	if (identifier.equalsIgnoreCase(MariusQLConstants.RID_COLUMN_NAME) || identifierIsAnAlias) {
	    return this.getRidProperty();
	} else if (identifier.equalsIgnoreCase(MariusQLConstants.TYPE_OF_ID_TOKEN_NAME)) {
	    return this.getTypeOfProperty();
	} 
	
	String propertyAttributeName = MariusQLHelper.getIdentifierAttributeName(identifier, session);
	String propertyIdentifier = MariusQLHelper.getCleanIdentifier(identifier, session);
	MMAttribute identifierAttribute = this.getMMEntity().getDefinedMMAttribute(propertyAttributeName);

	for (MProperty currentProperty : definedProperties) {
	    if (propertyIdentifier.toLowerCase().equals(currentProperty.getValue(identifierAttribute.toSQL()).toLowerCase())) {
		return currentProperty;
	    }
    	}

	return null;
    }

    @Override
    public Description getDefinedDescription(String identifier) {
	return this.getDefinedProperty(identifier);
    }

    @Override
    public String toSQL() {
	return MariusQLConstants.INSTANCE_PREFIX_TABLE_NAME + this.getInternalId();
    }
    
    @Override
    public String toSQLWithAlias() {
	return this.getTableAlias() == null ? MariusQLConstants.INSTANCE_PREFIX_TABLE_NAME + this.getInternalId() : MariusQLConstants.INSTANCE_PREFIX_TABLE_NAME + this.getInternalId() + " " + this.getTableAlias();
    }
    
    /**
     * Get sql projection of this class with all sub classes projection by union to manage polymophic selection
     * 
     * @return The sql union of projection
     */
    public String getSQLPolymorphProjection() {
	List<String> projections = new ArrayList<String>();
	List<MProperty> properties = this.getAllDefinedProperties();

	List<Category> categoryWithChildren = this.getHierarchyFromThisCategory();
	
	// Traverse all categories (including children of sub categories) for polymorphism.
	for (Category category : categoryWithChildren) {
	    if (category.isClass() && !category.isAbstract()) {
		MClass klass = ((MClass)category);
		
		if (!klass.hasExtension()) {
		    continue;
		}
		
		projections.add(klass.getSQLProjection(properties));
	    }
	}
	
	if (projections.isEmpty()) {
	    StringBuilder res = new StringBuilder();
	    res.append("select " + this.ridProperty.toSQLNullValue(true) + ", " + this.typeofProperty.toSQLNullValue(true));
	    
	    for (MProperty property : properties) {
		res.append(", " + property.toSQLNullValue(true));
	    }
	    
	    res.append(" " + this.getSession().getMariusQLDQL().getEmptyQuery());
	    
	    projections.add(res.toString());
	} 
	
	final String string = "(" + StringHelper.join(" union all ", projections.iterator()) + ")";	    
	return string;
    }
    
    /**
     * Get sql projection of this class only. See getSQLPolymorphProjection() for polymorphic projection.
     * 
     * @return
     */
    public String getSQLClassProjectionOnly() {
	if (!this.hasExtension()) {
	    return "(select " + this.ridProperty.toSQLNullValue(true) + " " + this.getSession().getMariusQLDQL().getEmptyQuery() + ")";
	}
	
	return this.getSQLProjection(this.getAllDefinedProperties());
    }
    
    /**
     * Get projection of this class by using the projected properties given in parameter. 
     * The rid and typeof columns are always selected.
     * 
     * @param projectedProperties properties to project if defined or inherited.
     * @return
     */
    private String getSQLProjection(List<MProperty> projectedProperties) {
	if (!this.hasExtension()) {
	    throw new NotSupportedException();
	}
	
	StringBuilder res = new StringBuilder();
	res.append("(select ");

	// Use table name as alias (should be unique).
	final String toSQLValue = this.toSQL();
	String alias = toSQLValue;

	// Select rid column.
	res.append(alias + "." + MariusQLConstants.RID_COLUMN_NAME + " as " + MariusQLConstants.RID_COLUMN_NAME + ", ");

	// Select typeof column using cast to text SQL function.
	res.append(this.getSession().getMariusQLTypes().getFactorySQLFunction()
		.createCastToTextFunction()
		.render(Arrays.asList(this.getInternalId().toString())));
	res.append(" as " + MariusQLConstants.TYPE_OF_ID_TOKEN_NAME);

	// Select property column or null if not used in this context.
	for (MProperty property : projectedProperties) {
	    res.append(", ");
	    if (this.isUsedPropertyExists(property.getName())) {
		final String propertyToSQL = property.toSQL(false);
		res.append(alias + "." + propertyToSQL + " as " + propertyToSQL);
	    } else {
		res.append(property.toSQLNullValue(true));
	    }
	}

	// Add from clause.
	res.append(" from " + toSQLValue + ")");
	return res.toString();
    }

    @Override
    public boolean isAbstract() {
	return false;
    }

    public boolean isReferencedByOtherUsedProperties() {
	List<MProperty> referencedProperties = this.getSession().getMariusQLDQL().getPropertyWithReferenceByOnClass(this.getInternalId());
	
	referencedProperties.addAll(
		this.getSession().getMariusQLDQL().getPropertyWithCollectionOfReferenceByOnClass(this.getInternalId()));
	
	for (MProperty refProperty : referencedProperties){
	    if (this.getSession().getMariusQLDQL().isDataReferencedByProperty(refProperty.getScope().toSQL(), refProperty.toSQL())){
		return true;
	    }
	}
	
	return false;
    }

    @Override
    public int delete() {
	int childrenCount = this.getSession().getMariusQLDQL().getNumberOfMGenericClassChildren(this);
	
	if (childrenCount > 0) {
	    throw new MariusQLException("Can not remove class which is parent class: " + this.getName());
	} else if (this.isReferenced()) {
	    throw new MariusQLException("Class is referenced by an existing property");
	}
	
	List<MProperty> properties = this.getDefinedProperties();
	for (MProperty property : properties) {
	    property.delete();
	}

	int deletedRows = this.currentDelegate.delete();
	
	if (deletedRows > 0) {
	    this.getSession().getModelCache().removeElement(this);
	}
	
	return deletedRows;
    }

    public void addDefinedProperty(MProperty propertyDefined) {
	List<MProperty> properties = this.getDefinedProperties();

	for (MProperty prop : properties) {
	    if (prop.getName().equalsIgnoreCase(propertyDefined.getName())) {
		throw new MariusQLException("property already exists");
	    }
	}

	properties.add(propertyDefined);
	this.setDefinedProperties(properties);
    }

    public void removeDefinedProperty(String propertyToDrop) {
	if (this.isUsedPropertyExists(propertyToDrop)) {
	    throw new MariusQLException("Can not remove a used property: " + propertyToDrop);
	}
	
	List<MProperty> definedProperties = new ArrayList<MProperty>(this.getDefinedProperties());
	
	Description property = this.getDefinedDescription(propertyToDrop);
	if (property == null) {
	    throw new MariusQLException(propertyToDrop + " is not a defined property.");
	}
	int deletedRows = property.delete();
	
	if (deletedRows > 0) {
	    definedProperties.remove(property);	
	    this.setDefinedProperties(definedProperties);
	}
    }
    
    @Override
    public void drop() {
	List<MProperty> usedProperties = this.getUsedProperties();
	
	if (!isReferencedByOtherUsedProperties()){
	    
	    this.getSession().getMariusQLDDL().dropTable(this.toSQL());
	    usedProperties.clear();
	    this.setUsedProperties(usedProperties);
	
	    this.currentDelegate.setBooleanValue(MariusQLConstants.IS_EXTENSION_CORE_ATTRIBUTE_NAME, false);
	    this.currentDelegate.update(false, Arrays.asList(MariusQLConstants.IS_EXTENSION_CORE_ATTRIBUTE_NAME, MariusQLConstants.USED_PROPERTIES_CORE_ATTRIBUTE_NAME));
	} else {
	    throw new MariusQLException("Class extension is referenced");
	}
    }
    
    @Override
    public boolean isModelInstanceExists(Long rid) {
	List<Category> categoryWithChildren = this.getHierarchyFromThisCategory();
	
	for (Category category : categoryWithChildren) {
	    if (!category.isClass() || category.isAbstract()) {
		throw new MariusQLException("logic error: children should be not abstract class");
	    }
	    
	    MClass klass = ((MClass) category);
	    
	    if (!klass.hasExtension()) {
		continue;
	    }
	    
	    if (null != this.getSession()
		    .getMariusQLDQL()
		    .getColumnValue(
			    klass.toSQL(), 
			    rid.toString(), 
			    klass.getDefinedDescription(MariusQLConstants.RID_COLUMN_NAME))) {
		return true; // instance found
	    }
	}
	
	return false; // instance not found
    }
}
