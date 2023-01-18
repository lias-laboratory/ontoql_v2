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
import java.util.Arrays;
import java.util.List;

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.AbstractDescription;
import fr.ensma.lias.mariusql.core.Category;
import fr.ensma.lias.mariusql.core.Datatype;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotSupportedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * (OLD: Attribute)
 * 
 * Represents an ontology attribut as part of an entity.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 * @author Ghada TRIKI
 * @author Florian MHUN
 */
public class MMAttribute extends AbstractDescription {

    protected boolean isOptional;
    
    protected Datatype range;
    
    /**
     * The domain of this attribute Its contextual (what alias ...).
     */
    protected MMEntity scope;
     
    protected String name;
    
    protected Long internalId;
    
    protected String mappedElement;
    
    protected boolean isCore = false;
    
    /**
     * The domain of this attribut It is contextual (alias ...).
     */
    private MMEntity entityContext;
    
    public MMAttribute(MariusQLSession pSession, String pName) {
	super(pSession);
	
	this.name = pName;
    }

    public MMAttribute(MariusQLSession pSession, Long pRid, String pName, MMEntity pScope, Long pRangeRid, boolean pIsOptional) {
	this(pSession, pName);
	
	this.internalId = pRid;	
	this.setRange(FactoryCore.createMMDatatype(this.getSession(), pRangeRid));
	this.setScope(pScope);
	this.scope.addDefinedAttribute(this);
	this.isOptional = pIsOptional;
    }
    
    /**
     * @param name
     */
    public MMAttribute(MariusQLSession pSession, String name, MMEntity ofEntity) {
	this(pSession, name);
	this.setScope(ofEntity);
	this.scope.addDefinedAttribute(this);
    }
    
    @Override
    public Long getInternalId() {
	if (this.internalId == null) {
	    this.internalId = this
		    .getSession()
		    .getMariusQLDQL()
		    .getMMAttributeRidByName(name, this.getScope().getInternalId());
	}
	
	return internalId;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public boolean isProperty() {
	return false;
    }

    @Override
    public boolean isAttribute() {
	return true;
    }

    @Override
    public boolean isMultilingualDescription() {
	return false;
    }

    @Override
    public String getTypeLabel() {
	throw new NotSupportedException();
    }
    
    @Override
    public String toSQL(boolean withAlias) {
	if (this.isCore()) {
	    return this.getName();
	}
	return this.getDefinitionName(withAlias);
    }

    @Override
    public String toSQL(Category currentContext) {
	StringBuffer res = new StringBuffer();
	if(currentContext.getTableAlias() != null) {
	    res.append(currentContext.getTableAlias() + ".");
	}
	
	res.append(this.toSQL());
	return res.toString();
    }

    @Override
    public Category getCurrentContext() {
	return this.entityContext;
    }

    @Override
    public void setCurrentContext(Category currentContext) {
	this.entityContext = (MMEntity)currentContext;
    }

    @Override
    public Datatype getRange() {
	return range;
    }

    @Override
    public void setRange(Datatype pRange) {
	this.range = pRange;
    }

    @Override
    public boolean isDefined() {
	if (this.entityContext == null) {
	    return false;
	} else {
	    return this.entityContext.getDefinedMMAttribute(this.getName()) != null;
	}
    }

    @Override
    public boolean isDefined(Category context) {
	return ((MMEntity) context).getAllAttributes().contains(this);
    }

    @Override
    public int insert() {
	this.setInternalId(this.getSession().getMariusQLTypes().getSequenceNextVal(MariusQLConstants.META_MODEL_SEQUENCE_NAME));
	
	List<String> columnsName = new ArrayList<String>();
	columnsName.add(MariusQLConstants.RID_COLUMN_NAME);
	columnsName.add(MariusQLConstants.MM_ATTRIBUTE_NAME_ATTRIBUTE_NAME);
	columnsName.add(MariusQLConstants.MM_ATTRIBUTE_SCOPE_ATTRIBUTE_NAME);
	columnsName.add(MariusQLConstants.MM_ATTRIBUTE_RANGE_ATTRIBUTE_NAME);
	
	List<String> columnsValue = new ArrayList<String>();
	columnsValue.add(this.getInternalId().toString());
	columnsValue.add(StringHelper.addSimpleQuotedString(this.getName()));
	columnsValue.add(Long.toString(this.getScope().getInternalId()));
	columnsValue.add(Long.toString(this.getRange().getInternalId()));
	
	int insertedRows = this.getSession().getMariusQLDML().insertRow(this.getTableName(), columnsName, columnsValue);
	
	// Attach attribute.
	if (insertedRows > 0) {
	    this.setCurrentContext(this.getScope());
	}
	
	return insertedRows;
    }
    
    public void setInternalId(Long rid) {
	this.internalId = rid;
    }

    public MMEntity getScope() {
	return this.scope;
    }
    
    public String getTableName() {
	return MariusQLConstants.MM_ATTRIBUTE_TABLE_NAME;
    }
    
    public void setScope(MMEntity ofEntity) {
	this.scope = ofEntity;
    }
    
    public boolean isOptional() {
	return isOptional;
    }
    
    public void setOptional(boolean isOptional) {
	this.isOptional = isOptional;
    }

    @Override
    public int update() {
	throw new NotSupportedException();
    }

    @Override
    public int create() {
	throw new NotSupportedException();
    }

    @Override
    public void drop() {
	throw new NotSupportedException();
	
    }

    @Override
    public int delete() {
	Datatype range = this.getRange();
	
	// remove attribute
	int returnDeletedRows = this.getSession().getMariusQLDML().deleteRow(this.getTableName(), this.getInternalId());
	
	if (returnDeletedRows > 0) {
	    // remove datatype
	    if (!range.isSimpleType()) {
		range.delete();
	    }
	    
	    // remove column in class core table
	    this.getSession().getMariusQLDDL().removeTableColumns(
		    MariusQLConstants.M_CLASS_TABLE_NAME, 
		    Arrays.asList(this.getDefinitionName(false)));
		
	    // detach attribute
	    this.setInternalId(null);
	    this.setCurrentContext(null);
	}
	
	return returnDeletedRows;
    }
  
    public String getDefinitionName(boolean withAlias) {
	if (this.isCore()) {
	    return this.getName();
	}
	
	return MariusQLConstants.ATTRIBUTE_PREFIX_COLUMN_TABLE_NAME
		+ this.getInternalId().toString();
    }
    
    @Override
    public String getSQLDefinition() {
	String res = null;
	Datatype range = this.getRange();
	
	if (this.isCore()){
	    res = this.toSQL() + " " + range.toSQL();
	}
	
	res = MariusQLConstants.ATTRIBUTE_PREFIX_COLUMN_TABLE_NAME
		+ this.getInternalId().toString()+ " " +range.toSQL();
	return res;
    }
    
    @Override
    public String toSQL(Category context, boolean polymorph) {
	throw new NotSupportedException();
    }

    public boolean isAlreadyExists(Long entityiInternalId) {
	return this.getSession().getMariusQLDQL().isMMAttributeExists(this, entityiInternalId);
    }

    @Override
    public void setLgCode(String lgCode) {
	throw new MariusQLException("not a multilang context");
    }

    @Override
    public String toString() {
	return name;
    }
    
    @Override
    public boolean isCore() {
	return this.isCore;
    }

    @Override
    public void setIsCore(boolean isCore) {
	this.isCore = isCore;
    }
}
