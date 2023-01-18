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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.Description;
import fr.ensma.lias.mariusql.core.metamodel.MMAttribute;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.util.MariusQLHelper;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * @author Mickael BARON
 * @author Ghada TRIKI
 * @author Adel GHAMNIA
 * @author Valentin CASSAIR
 * @author Florian MHUN
 */
public class MClassAndMPropertyDelegate {

    private Logger log = LoggerFactory.getLogger(MClassAndMPropertyDelegate.class);
    
    public interface InternalIdentifierFunction {
	
	Long getInternalId();
    }
    
    protected InternalIdentifierFunction internalIdFunction;
    
    /**
     * Current session.
     */
    protected MariusQLSession session;

    /**
     * Internal identifier of this class.
     */
    protected Long internalId;

    /**
     * Store MMAttribute object from attribute identifier.
     */
    protected Map<String, AttributeValue> attributeValues;

    /**
     * Entity defined this concept.
     */
    protected MMEntity refEntity;
    
    /**
     * Identifier of this concept, it mays contain name, code or RID
     */
    protected String identifier;
    
    public MClassAndMPropertyDelegate(MariusQLSession p, MMEntity pEntity, InternalIdentifierFunction pInternalIdFunction) {
	this.internalIdFunction = pInternalIdFunction;
	this.session = p;
	this.attributeValues = new HashMap<String, AttributeValue>();
	
	this.refEntity = pEntity;
	this.unInitializeValues();
    }
    
    public MMEntity getMMEntity() {
	return this.refEntity;
    }
        
    private void unInitializeValues() {
	final List<MMAttribute> allAttributes = this.getMMEntity().getAllAttributes();
	for (MMAttribute mmAttribute : allAttributes) {
	    AttributeValue newAttributeValue = new AttributeValue(this.getSession(), mmAttribute);
	    this.attributeValues.put(mmAttribute.toSQL(), newAttributeValue);
	}
    }

    protected void initializeIdentifier(String identifier) {
	if (identifier != null) {
	    if (MariusQLHelper.isInternalIdentifier(identifier)) {
		identifier = MariusQLHelper
			.removeSyntaxInternalIdentifier(identifier);
		this.setInternalId(Long.valueOf(identifier));
	    } else if (MariusQLHelper.isExternalIdentifier(identifier)) {
		identifier = MariusQLHelper
			.removeSyntaxInternalIdentifier(identifier);
		this.setCode(identifier);
	    } else if (this.getSession().isNoReferenceLanguage()) {
		this.setCode(identifier);
	    } else {
		if (MariusQLHelper.isNameIdentifier(identifier)) {
		    identifier = MariusQLHelper
			    .removeSyntaxNameIdentifier(identifier);
		}
		this.setName(identifier);
	    }
	} else {
	    throw new MariusQLException("Unable to initialize the identifier");
	}	
    }
    
    protected MariusQLSession getSession() {
	return this.session;
    }

    /**
     * Set the value of the external identifier of this class
     */
    public void setInternalId(Long internalId) {
	this.internalId = internalId;
    }

    public String getExternalIdentifierSQLValue() {
    	return this.attributeValues
		.get(getExternalIdentifierAttribute())
		.getSimpleTypeSQLValue(null); // give null to force no db access
    }

    private String getExternalIdentifierValue() {
	return this.attributeValues
		.get(getExternalIdentifierAttribute())
		.getSimpleTypeValue(this.getInternalId());
    }
    
    public String getIdentifier() {
	if (this.identifier == null) {
	    this.identifier = this.getExternalIdentifierValue();
	}
	
	return identifier;
    }
    
    public void setCode(String identifier) {
	this.setStringValue(
		this.getMMEntity().getDefinedMMAttribute(MariusQLConstants.CODE_CORE_ATTRIBUTE_NAME),
		identifier);
    }

    public String getExternalIdentifierAttribute() {
	if (this.getSession().isNoReferenceLanguage()) {
	    return this.getMMEntity().getDefinedDescription(MariusQLConstants.CODE_CORE_ATTRIBUTE_NAME).toSQL();
	} else {
	    return this.getMMEntity().getDefinedDescription(MariusQLConstants.NAME_CORE_ATTRIBUTE_NAME).toSQL();
	}
    }
    
    public void setObjectValue(String attributeName, Object value) {
	MMAttribute pAttribute = this.getMMEntity().getDefinedMMAttribute(attributeName);
	
	AttributeValue newAttributeValue = new AttributeValue(this.getSession(), pAttribute);
	newAttributeValue.setValue(value);
	
	final String definitionName = pAttribute.getDefinitionName(false);
	this.attributeValues.put(definitionName, newAttributeValue);
	
    }
    
    public List<?> getCollectionValue(String attributeName) {
	return this.attributeValues.get(attributeName).getCollectionReferenceTypeValue(this.getInternalId());
    }
    
    public boolean isAttributeDefined(String name) {
	final AttributeValue attributeValue = this.attributeValues.get(name);
	
	return (attributeValue != null);
    }
    
    public boolean isValueDefined(String name) {
	final AttributeValue attributeValue = this.attributeValues.get(name);
	
	if (attributeValue == null) {
	    return false;
	} else {
	    return attributeValue.isInitialized();
	}
    }
    
    public String getStringValue(String attributeName) {
	return this.attributeValues.get(attributeName).getSimpleTypeValue(this.getInternalId());
    }
    
    public void setStringValue(String attributeName, String value) {
	final Description definedDescription = this.getMMEntity().getDefinedDescription(attributeName);
	
	this.setStringValue((MMAttribute)definedDescription, value);
    }
    
    public void setStringValue(MMAttribute pAttribute, String value) {
	this.setStringValue(pAttribute, null, value);
    }

    public void setStringValue(MMAttribute pAttribute, String lg, String value) {
	String key = null;
	if (lg != null) {
	    if (!pAttribute.isMultilingualDescription()) {
		throw new MariusQLException("Multilingual not supported");
	    }
	    
	    key = pAttribute.getDefinitionName(false) + MariusQLConstants.UNDERSCORE + lg.toLowerCase();
	} else {
	    key = pAttribute.getDefinitionName(false);
	}

	final AttributeValue attributeValue = new AttributeValue(this.getSession(), pAttribute);
	attributeValue.setValue(value);
	
	this.attributeValues.put(key, attributeValue);
    }

    public boolean getBooleanValue(String attributeName) {
	return "t".equals(this.attributeValues.get(attributeName).getSimpleTypeValue(this.getInternalId()));
    }
    
    public void setBooleanValue(String attributeName, Boolean b) {
	final Description definedDescription = this.getMMEntity().getDefinedDescription(attributeName);
	
	this.setBooleanValue((MMAttribute)definedDescription, b);
    }
    
    public void setBooleanValue(MMAttribute attribute, Boolean b) {
	final AttributeValue attributeValue = new AttributeValue(this.getSession(), attribute);
	
	attributeValue.setValue(b);
	
	this.attributeValues.put(attribute.getDefinitionName(false), attributeValue);
	
    }
    
    protected String getCode() {
	final Object value = this.attributeValues.get(MariusQLConstants.CODE_CORE_ATTRIBUTE_NAME).getSimpleTypeValue(this.getInternalId());
	
	if (value != null) {
	    return value.toString();
	} else {
	    return null;
	}
    }

    public void setName(String identifier) {
	this.setName(identifier, this.getSession().getReferenceLanguage());
    }
    
    public void setName(String identifier, String lg) {
	this.setStringValue(
		this.getMMEntity()
			.getDefinedMMAttribute(MariusQLConstants.NAME_CORE_ATTRIBUTE_NAME),
		lg, identifier);
    }
    
    public String getMTableName() {
	return this.getMMEntity().toSQL();
    }
    
    public int delete() {
	return this.getSession().getMariusQLDML().deleteRow(this.getMTableName(), this.getInternalId());
    }
    
    public int insert(boolean isPackageAttributeExists) {
	final String entityName = this.getMMEntity().getName();

	List<String> columnsName = new ArrayList<String>();
	List<String> columnsValue = new ArrayList<String>();
	
	// Create the row. 
	this.setInternalId(this.getSession().getMariusQLTypes().getSequenceNextVal(MariusQLConstants.MODEL_SEQUENCE_NAME));	
	final String externalIdentifierAttribute = this.getExternalIdentifierAttribute();
	final String externalIdentifierValue = this.getExternalIdentifierSQLValue();

	columnsName.add(externalIdentifierAttribute);
	attributeValues.get(externalIdentifierAttribute).setModified(false);
	columnsValue.add(externalIdentifierValue);
	columnsName.add(MariusQLConstants.RID_COLUMN_NAME);
	columnsValue.add(this.getInternalId().toString());
	
	// Manage multilangue, must add code attribute.
	if (!MariusQLConstants.CODE_CORE_ATTRIBUTE_NAME.equals(externalIdentifierAttribute)) {
	    columnsName.add(MariusQLConstants.CODE_CORE_ATTRIBUTE_NAME);
	    columnsValue.add(StringHelper.addSimpleQuotedString(this.getCode()));
	    attributeValues.get(MariusQLConstants.CODE_CORE_ATTRIBUTE_NAME).setModified(false);
	}
	
	// Create identifier constraints.
	List<String> constraintNames = new ArrayList<String>();
	List<String> constraintValues = new ArrayList<String>();
	constraintNames.add(MariusQLConstants.RID_COLUMN_NAME);
	constraintValues.add(this.getInternalId().toString());

	if (isPackageAttributeExists) {
	    columnsName.add(MariusQLConstants.PACKAGE_NAME_CORE_ATTRIBUTE_NAME);
	    String packageValue = this.getStringValue(MariusQLConstants.PACKAGE_NAME_CORE_ATTRIBUTE_NAME);
	    if (packageValue != null) {
		packageValue = StringHelper.addSimpleQuotedString(packageValue);		
	    }	    
	    columnsValue.add(packageValue);
	    constraintNames.add(MariusQLConstants.PACKAGE_NAME_CORE_ATTRIBUTE_NAME);
	    constraintValues.add(packageValue);
	    attributeValues.get(MariusQLConstants.PACKAGE_NAME_CORE_ATTRIBUTE_NAME).setModified(false);
	}
		
	columnsName.add(this.getMMEntity().getDefinedMMAttribute(MariusQLConstants.DESCRIMINATOR_CORE_ATTRIBUTE_NAME).getName());
	columnsValue.add(StringHelper.addSimpleQuotedString(entityName));	
		
	int result = this.getSession().getMariusQLDML().insertRow(this.getMTableName(), columnsName, columnsValue);
	if (log.isDebugEnabled()) {
	    for (int currentColumnNameIndex = 0 ; currentColumnNameIndex < columnsName.size() ; currentColumnNameIndex++) {
		log.debug(entityName + " - inserted attribute [" + columnsName.get(currentColumnNameIndex) + ":" + columnsValue.get(currentColumnNameIndex) + "]");
	    }	    
	}
	
	columnsName = new ArrayList<String>();
	columnsValue = new ArrayList<String>();
	
	boolean updateIsNeeded = false;
	for (String current : attributeValues.keySet()) {
	    final AttributeValue attributeValue = attributeValues.get(current);

	    if (attributeValue.isModified()) {
		updateIsNeeded = true;
		attributeValue.insert(current, columnsName, columnsValue);
		
		if (log.isDebugEnabled()) {
		    log.debug(entityName + " - inserted attribute [" + columnsName.get(columnsName.size() - 1) + ":" + columnsValue.get(columnsName.size() - 1) + "]");		    
		}
	    }	    
	}
	
	if (updateIsNeeded) {
	    this.getSession().getMariusQLDML().updateRow(this.getMTableName(), columnsName, columnsValue, constraintNames, constraintValues);	    
	}
	
	return result;
    }

    public int update(boolean isPackageExists) {
	final String externalIdentifierAttribute = this.getExternalIdentifierAttribute();
	final String externalIdentifierValue = this.getExternalIdentifierSQLValue();
	
	// Create identifier constraints.
	List<String> constraintNames = new ArrayList<String>();
	List<String> constraintValues = new ArrayList<String>();
	constraintNames.add(externalIdentifierAttribute);
	constraintValues.add(externalIdentifierValue);
			
	if (isPackageExists) {
	    String packageValue = this.getStringValue(MariusQLConstants.PACKAGE_NAME_CORE_ATTRIBUTE_NAME);
	    if (packageValue != null) {
		packageValue = StringHelper.addSimpleQuotedString(packageValue);		
	    }	
	    constraintNames.add(MariusQLConstants.PACKAGE_NAME_CORE_ATTRIBUTE_NAME);
	    constraintValues.add(packageValue);
	}
	
	List<String> columnsName = new ArrayList<String>();
	List<String> columnsValue = new ArrayList<String>();
	
	for (String current : attributeValues.keySet()) {
	    final AttributeValue attributeValue = attributeValues.get(current);
	    
	    if (attributeValue.isModified()) {
		attributeValue.update(current, columnsName, columnsValue);

		if (log.isDebugEnabled()) {
		    log.debug(this.getMMEntity().getName() + " - updated attribute [" + columnsName.get(columnsName.size() - 1) + ":" + columnsValue.get(columnsName.size() - 1) + "]");		    
		}
	    }
	}
	
	return this.getSession().getMariusQLDML().updateRow(this.getMTableName(), columnsName, columnsValue, constraintNames, constraintValues);
    }
    
    public int update(boolean isPackageExists, List<String> attributes) {
	List<String> columnsName = new ArrayList<String>();
	List<String> columnsValue = new ArrayList<String>();
	
	for (String currentAttribute : attributes) {
	    final AttributeValue attributeValue = this.attributeValues.get(currentAttribute);
	    
	    attributeValue.update(currentAttribute, columnsName, columnsValue);
	}

	// Create identifier constraints.
	List<String> constraintNames = new ArrayList<String>();
	List<String> constraintValues = new ArrayList<String>();
	
	if (this.isInternalId()) {
	    constraintNames.add(MariusQLConstants.RID_COLUMN_NAME);
	    constraintValues.add(this.getInternalId().toString());
	} else {
	    constraintNames.add(this.getExternalIdentifierAttribute());
	    constraintValues.add(this.getExternalIdentifierSQLValue());	    
	}
	
	if (isPackageExists) {
	    String packageValue = this.getStringValue(MariusQLConstants.PACKAGE_NAME_CORE_ATTRIBUTE_NAME);
	    if (packageValue != null) {
		packageValue = StringHelper.addSimpleQuotedString(packageValue);		
	    }	
	    constraintNames.add(MariusQLConstants.PACKAGE_NAME_CORE_ATTRIBUTE_NAME);
	    constraintValues.add(packageValue);
	}
	
	return this.getSession().getMariusQLDML().updateRow(this.getMTableName(), columnsName, columnsValue, constraintNames, constraintValues);
    }
    
    public Long getInternalId(boolean forceLoading) {
	if (forceLoading) {
	    return this.getInternalId();
	} else {
	    return this.internalId;
	}
    }
    
    public boolean isInternalId() {
	return this.internalId != null && this.internalId != 0;
    }
    
    public Long getInternalId() {
	if (this.internalId == null) {
	    this.internalId = this.internalIdFunction.getInternalId();
	}
	
	return this.internalId;
    }

    public Object getReferenceValue(String scopeCoreAttributeName) {
	return this.attributeValues.get(scopeCoreAttributeName).getReferenceTypeValue(this.getInternalId());
    }    
}
