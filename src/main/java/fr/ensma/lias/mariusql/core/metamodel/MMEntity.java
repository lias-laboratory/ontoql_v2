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
package fr.ensma.lias.mariusql.core.metamodel;

import java.util.ArrayList;
import java.util.List;

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.AbstractCategory;
import fr.ensma.lias.mariusql.core.Category;
import fr.ensma.lias.mariusql.core.Description;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotSupportedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * An entity of the meta model loaded.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 * @author Ghada TRIKI
 * @author Florian MHUN
 * @author Valentin CASSAIR
 */
public class MMEntity extends AbstractCategory {
    
    /**
     * Represents sub-entities.
     */
    protected List<Category> directSubEntities;
    
    /**
     * Super entity of this entity. e.g, concept for the entity class.
     */
    protected MMEntity superMapEntity;
    
    protected boolean isSuperMapEntityInitialized;
    
    /**
     * Attributes defined or redefined by this entity.
     */
    protected List<MMAttribute> definedAttributes;
    
    /**
     * Attributes defined and deleted from entity 
     */
    protected List<MMAttribute> deletedAttributes;
    
    /**
     * Name of entity (identifier).
     */
    protected String name;
    
    /**
     * Technical id of this identifier.
     */
    protected Long internalId;
    
    /**
     * Mapped element.
     */
    protected String mappedElement;
    
    /**
     * Is core entity
     */
    private boolean isCore;
        
    /**
     * Is Meta Meta Model entity (MMEntity or MMAttribut)
     */
    private boolean isMetaMetaModel;

    public MMEntity(MariusQLSession pSession, Long pRid, String pName, String pMappedTable, boolean pIsCore, boolean pIsMetaMetaModel) {
	super(pSession);
	
	this.isSuperMapEntityInitialized = false;
	
	this.name = pName;
	this.internalId = pRid;
	this.isCore = pIsCore;
	this.isMetaMetaModel = pIsMetaMetaModel;
	this.mappedElement = pMappedTable;
	
	this.deletedAttributes = new ArrayList<MMAttribute>();
	this.definedAttributes = new ArrayList<MMAttribute>();

	this.loadDefinedAttributes();
    }
    
    public void setDefinedAttributes(List<MMAttribute> definedAttributes) {
        this.definedAttributes = definedAttributes;
    }

    public MMEntity(MariusQLSession pSession, String pName, String pMappedTable) {
	super(pSession);		
	
	this.name = pName;
	this.mappedElement = pMappedTable;
	
	this.deletedAttributes = new ArrayList<MMAttribute>();
	this.definedAttributes = new ArrayList<MMAttribute>();
	
	this.loadDefinedAttributes();
    }
      
    public boolean isMetaMetaModel() {
        return isMetaMetaModel;
    }
    
    /**
     * Setter for the superEntity attribute.
     * 
     * @param superEntity
     */
    public void setSuperEntity(MMEntity pSuperEntity) {
	this.superMapEntity = pSuperEntity;
    }
    
    /**
     * @return
     */
    public MMEntity getSuperEntity() {
	if (!this.isSuperMapEntityInitialized && this.superMapEntity == null) {
	    final String superMMEntityNameByRid = this.getSession().getMariusQLDQL().getSuperMMEntityNameByRid(this.getInternalId());
	    this.isSuperMapEntityInitialized = true;
	    if (superMMEntityNameByRid != null) {
		this.superMapEntity = FactoryCore.createExistingMMEntity(getSession(), superMMEntityNameByRid);
	    }	    
	}
	
	return this.superMapEntity;	    
    }
    
    @Override
    public Long getInternalId() {
	if (this.internalId == null) {
	    this.internalId = this.getSession().getMariusQLDQL().getMMEntityRidByName(this.getName());
	} 
	return this.internalId;
    }

    @Override
    public String getName() {
	return this.name;
    }

    @Override
    public boolean isClass() {
	return false;
    }

    @Override
    public boolean isEntity() {
	return true;
    }

    @Override
    public boolean isAbstract() {
	return isSubMMEntityOf(MariusQLConstants.STATIC_CLASS_CORE_ENTITY_NAME);
    }

    @Override
    public String getTypeLabel() {
	throw new NotSupportedException();
    }

    @Override
    public String toSQL() {
	return this.getMappedElement();
    }    
    
    public String getTableName() {
	return MariusQLConstants.MM_ENTITY_TABLE_NAME;
    }
    
    public String getMappedElement() {
	if (this.mappedElement == null) {
	    return MariusQLConstants.MODEL_PREFIX_TABLE_NAME + this.getName();
	} else {
	    return this.mappedElement;	    
	}
    }
    
    public List<Category> getSubcategories() {
	return this.getSession().getMariusQLDQL().getMMEntityBySuperEntity(this.getInternalId());
    }
    
    public void loadDefinedAttributes() {
	this.definedAttributes = this.getSession().getMariusQLDQL().getMMAttributeFromScopeRid(this);
    }
    
    /**
     * @return Returns the attributes applicable on this entity.
     */
    public List<MMAttribute> getDefinedAttributes() {
	if (this.definedAttributes == null) {
	    loadDefinedAttributes();
	}
	
	return this.definedAttributes;	    
    }

    /**
     * @return Returns the attributes defined by this entity.
     */
    public List<MMAttribute> getAllAttributes() {
	List<MMAttribute> res = new ArrayList<MMAttribute>(this.getDefinedAttributes());
	res.addAll(this.deletedAttributes);
	if (this.getSuperEntity() != null) {
	    List<MMAttribute> allAttributesOfSuperEntity = this.getSuperEntity().getAllAttributes();
	    MMAttribute currentAttribut = null;
	    int index = 0; // index of insertion
	    for (int i = 0; i < allAttributesOfSuperEntity.size(); i++) {
		currentAttribut = allAttributesOfSuperEntity.get(i);
		if (!this.getDefinedAttributes().contains(currentAttribut)) {
		    res.add(index, currentAttribut);
		    index++;
		}
	    }
	}
	
	return res;
    }
    
    @Override
    public Description getDefinedDescription(String identifier) {
	final List<MMAttribute> allAttributes = this.getAllAttributes();
	
	if (identifier.startsWith(MariusQLConstants.PREFIX_METAMODEL_ELEMENT)) {
	    identifier = identifier.substring(1);
	}

	boolean identifierIsAnAlias =  (this.getCategoryAlias() != null && this.getCategoryAlias().equals(identifier));
	if (identifier.equalsIgnoreCase(MariusQLConstants.RID_COLUMN_NAME) || identifierIsAnAlias) {
	    identifier = MariusQLConstants.RID_COLUMN_NAME;
	}	
	
	for (MMAttribute currentAttribute : allAttributes) {
	    if (identifier.equalsIgnoreCase(currentAttribute.getName())) {
		currentAttribute.setCurrentContext(this);
		return currentAttribute;
	    }   
	}
	
	return null;
    }
    
    public MMAttribute getDefinedMMAttribute(String identifier) {
	return (MMAttribute)getDefinedDescription(identifier);
    }

    @Override
    public int create() {
	List<MMAttribute> attributes = this.getDefinedAttributes();
	MMAttribute currentMapAttribute = null;
	
	List<String> newColumns = new ArrayList<String>();
	for (int i = 0; i < attributes.size(); i++) {
	    // test if the column attribute already exists 
	    currentMapAttribute = (MMAttribute) attributes.get(i);
	    if (!currentMapAttribute.isAlreadyExists(this.getInternalId())){	
		String currentSqlDefinition = currentMapAttribute.getSQLDefinition();
	   
		if (currentSqlDefinition != null) {
		    newColumns.add(currentSqlDefinition);		
		} else {
		    throw new MariusQLException("the sql definition is null");
		}
	    }
	}
	
	// Modify the table structure.
	return this.getSession().getMariusQLDDL().addTableColumns(this.getMappedElement(), newColumns);
    }
    
    @Override
    public void drop() {
	throw new NotSupportedException();
    }

    @Override
    public int insert() {
	List<String> columnsName = new ArrayList<String>();
	
	this.internalId = this.getSession().getMariusQLTypes().getSequenceNextVal(MariusQLConstants.META_MODEL_SEQUENCE_NAME);

	columnsName.add(MariusQLConstants.RID_COLUMN_NAME);
	columnsName.add(MariusQLConstants.MM_ENTITY_NAME_ATTRIBUTE_NAME);	
	columnsName.add(MariusQLConstants.MM_ENTITY_MAPPEDTABLENAME_ATTRIBUTE_NAME);
	columnsName.add(MariusQLConstants.MM_ENTITY_IS_CORE_ATTRIBUTE_NAME);
	List<String> columnsValue = new ArrayList<String>();
	columnsValue.add(this.getInternalId().toString());
	columnsValue.add(StringHelper.addSimpleQuotedString(this.getName()));
	columnsValue.add(StringHelper.addSimpleQuotedString(this.toSQL()));
	columnsValue.add(StringHelper.addSimpleQuotedString((new Boolean(this.isCore)).toString()));
	
	final MMEntity superEntity = this.getSuperEntity();
	if (superEntity != null) {
	    columnsName.add(MariusQLConstants.MM_ENTITY_SUPERENTITY_ATTRIBUTE_NAME);
	    columnsValue.add(superEntity.getInternalId().toString());	    
	}	    
	    
	// insert entity
	int insertedRows = this.getSession().getMariusQLDML().insertRow(this.getTableName(), columnsName, columnsValue);	
	
	for(MMAttribute currentAttribute : this.getDefinedAttributes()) {
	    currentAttribute.insert(); 
	}
	
	// Update attribute rids
	List<Long> rids = new ArrayList<Long>();
	for(MMAttribute currentAttribute : this.getDefinedAttributes()) {
	    rids.add(currentAttribute.getInternalId()); 
	}

	this.getSession()
		.getMariusQLDML()
		.updateRow(
			this.getTableName(),
			MariusQLConstants.MM_ENTITY_ATTRIBUTES_ATTRIBUTE_NAME,
			this.getSession().getMariusQLTypes().getCollectionAssociationQuotedSQLValue(rids),
			MariusQLConstants.RID_COLUMN_NAME,
			this.getInternalId().toString()); 
	
	return insertedRows;
    }

    @Override
    public int delete() {
	if (this.isCore() || this.isMetaMetaModel()) {
	    throw new MariusQLException("Can not remove core entity: " + this.getName());
	} else if (this.hasSubCategories()) {
	    throw new MariusQLException("Entity is used as parent entity: " + this.getName());
	} else if (this.isReferenced()) {
	    throw new MariusQLException("Entity is referenced: " + this.getName());
	}
	
	List<MMAttribute> mmattributes = this.getDefinedAttributes();
	for (MMAttribute mmattribute : mmattributes) {
	    mmattribute.delete();
	}
	
	int deletedRows = this.getSession().getMariusQLDML().deleteRow(this.getTableName(), this.getInternalId());
	
	if (deletedRows > 0) {
    	    this.getSession().getMetaModelCache().removeElement(this);
    	}
	
	return deletedRows;
    }
    /**
     * Add an attribute defined by this entity.
     * 
     * @param an
     *            attribute defined by this entity
     */
    public void addDefinedAttribute(MMAttribute a) {
	if (this.containsMMAttribute(a)) {
	    throw new MariusQLException("Attribute '" + a.getName() + "' already defined for entity '"+ this.getName() + "'");
	}
	
	this.definedAttributes.add(a);
    }
    
    /**
     * Remove an attribute defined by this entity
     * 
     * @param a
     */
    public void removeDefinedAttribute(MMAttribute a) {
	this.deletedAttributes.add(a);
	this.definedAttributes.remove(a);
    }

    @Override
    public int update() {
	if (this.isCore() || this.isMetaMetaModel()) {
	    throw new MariusQLException("Can not modify a core entity: " + this.getName());
	}
	
	for (MMAttribute removedAttribute : this.deletedAttributes) {
	    if (removedAttribute.isCore()) {
		throw new MariusQLException("Can not remove a core attribute : " + removedAttribute.getName());
	    }
	    removedAttribute.delete();
	}
	
	deletedAttributes.clear();
	
	String currentSqlDefinition = null;
	List<String> newColumns = new ArrayList<String>();
	
	for (MMAttribute attribute : this.getDefinedAttributes()) {
	    if (!attribute.isDefined()) {
		
		attribute.insert();
		currentSqlDefinition = attribute.getSQLDefinition();
		
		if (!attribute.isAlreadyExists(this.getInternalId())){
		    if (currentSqlDefinition != null) {
			newColumns.add(attribute.getSQLDefinition());		
		    } else {
			throw new MariusQLException("The sql definition is null");
		    }    
		}
	    }
	}
	
	//add column
	this.getSession().getMariusQLDDL().addTableColumns(this.getMappedElement(), newColumns);
	
	//update list of attributes rid in entity table 
	List<Long> rids = new ArrayList<Long>();
	for (MMAttribute currentAttribute : this.getDefinedAttributes()) {
	    rids.add(currentAttribute.getInternalId()); 
	}
	
	return this.getSession()
		.getMariusQLDML()
		.updateRow(
			this.getTableName(),
			MariusQLConstants.MM_ENTITY_ATTRIBUTES_ATTRIBUTE_NAME,
			this.getSession().getMariusQLTypes().getCollectionAssociationQuotedSQLValue(rids),
			MariusQLConstants.RID_COLUMN_NAME,
			this.getInternalId().toString());
    }
    
    public boolean isSubMMEntityOf(String mother) {
	if (mother == null) {
	    throw new MariusQLException("The entity's mother is null");
	}

	if (mother.equalsIgnoreCase(this.getName())) {
	    return true;
	}
	
	if (this.getSuperEntity() != null) {
	    return this.getSuperEntity().isSubMMEntityOf(mother);
	} else {
	    return false;
	}
    }

    public boolean containsMMAttribute(MMAttribute attribute) {
	List<MMAttribute> attributes = this.getAllAttributes();
	
	for (MMAttribute currentAttribute : attributes) {
	    if (currentAttribute.getName().equals(attribute.getName())) {
		return true;
	    }
	}
	
	return false;
    }

    public boolean isCore() {
	return isCore;
    }

    /**
     * Get the Meta Meta Model entity of this Meta Model entity.
     * 
     * @return The Meta Meta Model entity or itself if this entity is already Meta Meta Model
     */
    public MMEntity getMetaMetaModelEntity() {
	if (this.isSubMMEntityOf(MariusQLConstants.CLASS_CORE_ENTITY_NAME)) {
	    return FactoryCore.createExistingMMEntity(getSession(), MariusQLConstants.CLASS_CORE_ENTITY_NAME);
	} else {
	    return FactoryCore.createExistingMMEntity(getSession(), MariusQLConstants.STATIC_CLASS_CORE_ENTITY_NAME);
	}
    }

    @Override
    public boolean isReferenced() {
	return this.getSession().getMariusQLDQL().isMMEntityReferenced(this.getInternalId());
    }

    @Override
    public List<Category> getDirectSubCategories() {
	if (this.directSubEntities == null) {
	    final List<String> subMMEntityNamesByRid = this.getSession().getMariusQLDQL().getSubMMEntityNamesByRid(this.getInternalId());
	    List<Category> entities = new ArrayList<Category>();

	    for (String name : subMMEntityNamesByRid) {
		entities.add(FactoryCore.createExistingMMEntity(this.getSession(), name));
	    }
	    
	    this.directSubEntities = entities;
	}	
	return this.directSubEntities;
    }

    @Override
    public boolean isModelInstanceExists(Long rid) {
	List<Category> categoryWithChildren = this.getHierarchyFromThisCategory();
	
	for (Category entity : categoryWithChildren) {
	    if (entity.isEntity()) {
		if (null != this.getSession()
			.getMariusQLDQL()
			.getColumnValue(				
				entity.toSQL(), 
				rid.toString(), 
				entity.getDefinedDescription(MariusQLConstants.RID_COLUMN_NAME))) {
		    return true;
		}
	    }
	}
	
	return false;
    }

    @Override
    public String toSQLWithAlias() {
    	return this.getTableAlias() == null ? toSQL() : toSQL() + " "+this.getTableAlias();
    }
    
    
    public MMEntity cloneMMEntity(MMEntity mmentity) {
	return new MMEntity(session, mmentity.getInternalId(), mmentity.getName(), mmentity.getMappedElement(), mmentity.isCore(), mmentity.isMetaMetaModel());
    }
}
