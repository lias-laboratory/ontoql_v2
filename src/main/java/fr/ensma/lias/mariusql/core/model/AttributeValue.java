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
import java.util.List;

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.Datatype;
import fr.ensma.lias.mariusql.core.DatatypeBoolean;
import fr.ensma.lias.mariusql.core.DatatypeCollection;
import fr.ensma.lias.mariusql.core.DatatypeMultiString;
import fr.ensma.lias.mariusql.core.DatatypeReference;
import fr.ensma.lias.mariusql.core.DatatypeString;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.PersistentObject;
import fr.ensma.lias.mariusql.core.metamodel.MMAttribute;
import fr.ensma.lias.mariusql.core.metamodel.MMDatatypeCollection;
import fr.ensma.lias.mariusql.core.metamodel.MMDatatypeReference;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotSupportedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.util.MariusQLHelper;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * @author Mickael BARON
 * @author Ghada TRIKI
 * @author Valentin CASSAIR
 */
public class AttributeValue {

    /**
     * Used for both Simple and Reference types.
     */
    protected Object value;
    
    /**
     * Attribute associated with the value.
     */
    protected MMAttribute attributObject;
    
    /**
     * Is used to know if there is a value or it's a null value.
     */
    protected boolean isInitialized = false;

    /**
     * Is used to know if there is a value to be modified.
     */
    protected boolean isModified = false;
    
    /**
     * 
     */
    protected MariusQLSession refSession;
    
    public AttributeValue(MariusQLSession p, MMAttribute pAttributeObject) {
	this.attributObject = pAttributeObject;
	this.refSession = p;
    }

    public boolean isMultilingualType() {
	return this.attributObject.getRange().isMultilingualType();
    }
    
    /**
     * @return True if the datatype is a simple type.
     */
    public boolean isSimpleType() {
	return !isReferenceType() && !isCollectionReferenceType();
    }
    
    /**
     * @return True if the datatype is an association type.
     */
    public boolean isReferenceType() {
	return this.attributObject.getRange().isAssociationType();
    }

    /**
     * @return True if the datatype is a collection of association type.
     */
    public boolean isCollectionReferenceType() {
	return this.attributObject.getRange().isCollectionType();
    }
    
    /**
     * @return True if the datatype is a collection of association type AND is many to many relationship.
     */
    public boolean isManyToManyCollectionReferenceType() {
	if (this.isCollectionReferenceType()) {
	    DatatypeCollection range = (DatatypeCollection)this.attributObject.getRange();
	    
	    return range.isManyToMany();
	} else {
	    return false;
	}
    }
    
    public String getManyToManyCollectionReferenceTypeSQLValue(Long internalId) {
	if (this.isInitialized()) {
	    List<?> currentList = this.getCollectionReferenceTypeValue(internalId);

	    List<Long> ids = new ArrayList<Long>();
	    for (Object currentValue : currentList) {
		ids.add(((PersistentObject)currentValue).getInternalId());		
	    }
	    return refSession.getMariusQLTypes().getCollectionAssociationQuotedSQLValue(ids);
	} else {
	    throw new MariusQLException("The attribute value is not initialized");
	}
    }

    public void setValue(Object value) {
        this.value = value;
        this.isInitialized = true;
        this.isModified = true;
    }
    
    public MMAttribute getAttribute() {
        return this.attributObject;
    }

    public void setAttribute(MMAttribute attributObject) {
        this.attributObject = attributObject;
    }

    public boolean isInitialized() {
        return this.isInitialized;
    }
    
    public boolean isModified() {
	return this.isModified;
    }

    private Object getValue() {
	return this.value;
    }
    
    public String getSimpleTypeValue(Long internalId) {
	if (this.isInitialized() || internalId == null) {
	    if (this.attributObject.getRange() instanceof DatatypeString
		    || this.attributObject.getRange() instanceof DatatypeBoolean 
		    || this.attributObject.getRange() instanceof DatatypeMultiString) {
		final Object currentValue = this.getValue();
		if (currentValue != null) {
		    return currentValue.toString();		
		} else {
		    return null;
		}
	    } else {
		throw new MariusQLException("The range is not supported");	
	    }
	} else {
	    String values = this.refSession.getMariusQLDQL().getColumnValue(
		    this.attributObject.getScope().getMappedElement(),
		    Long.toString(internalId),
		    this.attributObject);
	    
	    if (values == null) {
		throw new MariusQLException("The value is null");
	    }
	    
	    return values;
	}
    }
    
    public String getSimpleTypeSQLValue(Long internalId) {
	if (this.attributObject.getRange() instanceof DatatypeString || this.attributObject.getRange() instanceof DatatypeMultiString ) {
	    String simpleTypeValue = this.getSimpleTypeValue(internalId);
	    if (simpleTypeValue != null) {
		return StringHelper.addSimpleQuotedString(simpleTypeValue);		
	    } else {
		return null;
	    }
	} else if (this.attributObject.getRange() instanceof DatatypeBoolean) {
	    return this.getSimpleTypeValue(internalId);
	} else {
	    throw new MariusQLException("the range is not supported");
	}
    }    

    public Object getReferenceTypeValue(Long internalId) {
	if (this.isInitialized()) {
	    return this.getValue();
	} else {
	    String value = this.refSession.getMariusQLDQL().getColumnValue(
		    this.attributObject.getScope().getMappedElement(),
		    internalId.toString(),
		    this.attributObject);
	    
	    if (value == null) {
		return null;
	    }
	    
	    final MMEntity reference = (MMEntity)((DatatypeReference)this.attributObject.getRange()).getCategory();
	 
	    if ((MariusQLConstants.M_DATATYPE_TABLE_NAME).equalsIgnoreCase(reference.getMappedElement())) {
		return FactoryCore.createMDatatype(refSession, Long.valueOf(value));
	    } else if ((MariusQLConstants.M_CLASS_TABLE_NAME).equalsIgnoreCase(reference.getMappedElement())) {
		return FactoryCore.createExistingMGenericClass(refSession, value);
	    } else {
		return null;
	    }
	}
    }
    
    public String getReferenceTypeSQLValue(Long internalId) {
	if (this.isInitialized()) {
	    final Object currentValue = this.getValue();
	    Object internalValue = ((PersistentObject)currentValue).getInternalId();
	    
	    return internalValue != null ? internalValue.toString() : null;  
	} else {
	    return this.refSession.getMariusQLDQL().getColumnValue(
		    this.attributObject.getScope().getMappedElement(),
		    Long.toString(internalId),
		    this.attributObject);
	}
    }
    
    public List<?> getCollectionReferenceTypeValue(Long internalId) {
	if (this.isInitialized()) {
	    if (this.getValue() instanceof List) {
		return (List<?>) this.getValue();
	    } else {
		if (((String) getValue()).contains(",")) {
		    List<Object> collections = new ArrayList<Object>();
		    String[] references = ((String) getValue()).split(",");
		    for (int i = 1; i < references.length; i++) {
			collections.add(FactoryCore.createExistingMGenericClass(refSession, Long.valueOf(references[i])));
		    }
		    return collections;
		} else {
		    throw new MariusQLException("The attribute is not a collection");
		}
	    }
	} else {
	    String values = this.refSession.getMariusQLDQL().getColumnValue(
		    this.attributObject.getScope().getMappedElement(),
		    Long.toString(internalId), this.attributObject);
	    
	    //values = this.refSession.getMariusQLTypes().getArray(values);
	    
	    final List<Long> longValues = MariusQLHelper
		    .getSQLCollectionAssociationValues(values);
	    final Datatype datatype = ((MMDatatypeCollection) this.attributObject
		    .getRange()).getDatatype();
	    final MMEntity elementByName = (MMEntity) ((MMDatatypeReference) datatype)
		    .getCategory();

	    // Class, Property...

	    List<Object> collections = new ArrayList<Object>();

	    if ((MariusQLConstants.M_CLASS_TABLE_NAME)
		    .equalsIgnoreCase(elementByName.getMappedElement())) {
		for (Long currentLong : longValues) {
		    collections.add(FactoryCore.createExistingMClass(
			    refSession, MariusQLConstants.PREFIX_INTERNAL_ID
				    + currentLong));
		}
	    } else if ((MariusQLConstants.M_PROPERTY_TABLE_NAME)
		    .equalsIgnoreCase(elementByName.getMappedElement())) {
		for (Long currentLong : longValues) {
		    collections.add(FactoryCore.createExistingMProperty(
			    refSession, currentLong));
		}
	    } else {
		throw new MariusQLException("The attribute is not a property or a class");
	    }

	    return collections;
	}
    }
    
    public void setModified(boolean pIsModified) {
	this.isModified = pIsModified;
    }
    
    /**
     * @param key
     * @param columnNames
     * @param columnValues
     */
    public void insert(String key, List<String> columnNames, List<String> columnValues) {
	String value = null;
	
	if (this.isSimpleType()) {
	    value = this.getSimpleTypeSQLValue(null);
	} else if (this.isCollectionReferenceType()) {
	    List<?> currentList = this.getCollectionReferenceTypeValue(null);
		
	    if (this.isManyToManyCollectionReferenceType()) {
		throw new NotSupportedException("Many to many collection reference not supported");
	    } else {
		List<Long> ids = new ArrayList<Long>();
		for (Object currentObject : currentList) {
		    if (!(currentObject instanceof MClass)) {
		    	((PersistentObject)currentObject).insert();
		    }
		    ids.add(((PersistentObject)currentObject).getInternalId());
		}
		
		value = refSession.getMariusQLTypes().getCollectionAssociationQuotedSQLValue(ids);
	    }	    
	} else if (this.isReferenceType()) {
	    value = this.getReferenceTypeSQLValue(null);
	} else {
	    throw new MariusQLException("Is not a reference");
	}
	
	this.setModified(false);
	
	columnNames.add(key);
	columnValues.add(value);
    }
    
    /**
     * @param key
     * @param columnNames
     * @param columnValues
     */
    public void update(String key, List<String> columnNames, List<String> columnValues) {
	String value = null;
	if (this.isSimpleType()) {
	    value = this.getSimpleTypeSQLValue(null);
	} else if (this.isCollectionReferenceType()) {
	    List<?> currentList = this.getCollectionReferenceTypeValue(null);
	    if (this.isManyToManyCollectionReferenceType()) {
		value = this.getManyToManyCollectionReferenceTypeSQLValue(null);
	    } else {
		List<Long> ids = new ArrayList<Long>();
		for (Object currentObject : currentList) {
		    PersistentObject pObject = ((PersistentObject)currentObject);
		    pObject.update();
		    ids.add(pObject.getInternalId());
		}
		
		value = refSession.getMariusQLTypes().getCollectionAssociationQuotedSQLValue(ids);
	    }	    
	} else if (this.isReferenceType()) {
	    value = this.getReferenceTypeSQLValue(null);
	} else {
	    throw new MariusQLException("Is not a reference");
	}
	
	columnNames.add(key);
	columnValues.add(value);
    }
}
