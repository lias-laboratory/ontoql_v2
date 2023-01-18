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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.AbstractDatatypeCollection;
import fr.ensma.lias.mariusql.core.AbstractDescription;
import fr.ensma.lias.mariusql.core.Category;
import fr.ensma.lias.mariusql.core.Datatype;
import fr.ensma.lias.mariusql.core.DatatypeReference;
import fr.ensma.lias.mariusql.core.metamodel.MMAttribute;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.core.model.MClassAndMPropertyDelegate.InternalIdentifierFunction;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotSupportedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * (OLD: EntityPropertyOntoDB)
 * 
 * @author Mickael BARON
 * @author Ghada TRIKI
 * @author Florian MHUN
 * @author Adel GHAMNIA
 */
public class MProperty extends AbstractDescription implements ModelElement {
    
    protected MClassAndMPropertyDelegate currentDelegate;
            
    protected MClass context;
    
    protected MClass scope;
    
    private final class PropertyInternalIdentifierFunction implements InternalIdentifierFunction {
	@Override
	public Long getInternalId() {
	    MClass currentScope = MProperty.this.scope; //MProperty.this.getScope();
	    
	    if (currentScope != null) {
		Long currentInternalId = currentScope.getInternalId(false);
		
		if (currentInternalId != null) {
		    return MProperty.this.getSession().getMariusQLDQL().getMPropertyId(currentInternalId, currentDelegate.getExternalIdentifierAttribute(), currentDelegate.getExternalIdentifierSQLValue());		    
		}
	    }
 	    
	    return null;
	}
    }
    
    public MProperty(MariusQLSession p, MMEntity pEntity) {
	super(p);
	
	this.currentDelegate = new MClassAndMPropertyDelegate(p, pEntity, new PropertyInternalIdentifierFunction());
    }
    
    public MProperty(MariusQLSession p, MMEntity pEntity, MClass pScopeClass) {
	super(p);
	
	this.currentDelegate = new MClassAndMPropertyDelegate(p, pEntity, new PropertyInternalIdentifierFunction());
	this.currentDelegate.setObjectValue(MariusQLConstants.SCOPE_CORE_ATTRIBUTE_NAME, pScopeClass);
	this.scope = pScopeClass;
    }

    @Override
    public String getName() {
	return this.currentDelegate.getIdentifier();
    }

    @Override
    public boolean isProperty() {
	return true;
    }

    @Override
    public boolean isAttribute() {
	return false;
    }

    @Override
    public boolean isMultilingualDescription() {
	return this.getRange().isMultilingualType();
    }

    @Override
    public void setLgCode(String lgCode) {
	throw new NotSupportedException();
    }

    @Override
    public String getTypeLabel() {
	return "property";
    }

    public String toSQLNullValue(boolean withAliasName) {
	return "null" + this.getSession().getMariusQLTypes().getCastProjection(this.getRange().toSQL()) + " " + (withAliasName ? ("as " + this.toSQL(false)) : "");
    }
    
    @Override
    public String toSQL(boolean withTableAlias) {
	StringBuilder sql = new StringBuilder();
	if(this.context != null && this.context.getTableAlias() != null && withTableAlias) {	    
	    sql.append(this.context.getTableAlias() + ".");
	}
	
	sql.append(MariusQLConstants.PROPERTY_PREFIX_COLUMN_TABLE_NAME + this.getInternalId());
	
	if (this.getRange().isAssociationType()) {    
	    sql.append(MariusQLConstants.UNDERSCORE + MariusQLConstants.PROPERTY_SUFFIX_COLUMN_REF_NAME);
	} else if (this.getRange().isCollectionType()) {
	    AbstractDatatypeCollection range = (AbstractDatatypeCollection) this.getRange();
	    if (range.getDatatype().isAssociationType()) {
		sql.append(MariusQLConstants.UNDERSCORE + MariusQLConstants.PROPERTY_SUFFIX_COLUMN_REF_COLLECTION_NAME);
	    }
	}
	
	return sql.toString();
    }
    
    @Override
    public String toSQL(Category currentContext) {
	StringBuilder sql = new StringBuilder();
	
	if(currentContext.getTableAlias() != null) {
	    sql.append(currentContext.getTableAlias() + ".");  
	}
	
	sql.append(this.toSQL(false));
	return sql.toString();
    }

    @Override
    public Category getCurrentContext() {
	return this.context;
    }

    @Override
    public void setCurrentContext(Category currentContext) {
	this.context = (MClass)currentContext;
    }

    @Override
    public Datatype getRange() {
	return (Datatype)this.currentDelegate.getReferenceValue(MariusQLConstants.RANGE_CORE_ATTRIBUTE_NAME);
    }

    @Override
    public void setRange(Datatype pRange) {
	this.currentDelegate.setObjectValue(MariusQLConstants.RANGE_CORE_ATTRIBUTE_NAME, pRange);	
    }

    @Override
    public boolean isDefined() {
	return isDefined(getCurrentContext());
    }

    @Override
    public boolean isDefined(Category context) {
	final List<MProperty> definedProperties = ((MClass)context).getAllDefinedProperties();
	for(MProperty current : definedProperties)
	    if(current.getInternalId().equals(this.getInternalId()))
		return true;

	return false;
    }

    @Override
    public int insert() {
	if (!this.isExists()) {
	    return this.currentDelegate.insert(false);
	} else {
	    throw new MariusQLException("property already exists.");
	}
    }
    
    @Override   
    public Long getInternalId() {
	return this.currentDelegate.getInternalId();
    }
    
    protected boolean isExists() {
	return this.getSession().getMariusQLDQL().isMPropertyExists(this.getScope().getInternalId(),MariusQLConstants.CODE_CORE_ATTRIBUTE_NAME , StringHelper.addSimpleQuotedString(this.getName()));
    }

    public MClass getScope() {
	return (MClass)(this.currentDelegate.getReferenceValue(MariusQLConstants.SCOPE_CORE_ATTRIBUTE_NAME));	
    }

    public void setScope(MClass pClass) {
	this.currentDelegate.setObjectValue(MariusQLConstants.SCOPE_CORE_ATTRIBUTE_NAME, pClass);	
    }

    @Override
    public int update() {
	if (this.getInternalId() == null) {
	    return this.insert();
	} else {
	    return this.currentDelegate.update(false);
	}
    }
    
    protected List<String> getExtentDefinition() {
	List<String> sqlDescription = new ArrayList<String>();
	
  	if (this.getRange().isAssociationType()) {
  	    sqlDescription.add(this.getSQLDefinition());
  	    sqlDescription.add(this.getInstanceReferenceTableColumnNameSQLDefinition());
  	} else if (this.getRange().isCollectionAssociationType()) {
  	    sqlDescription.add(this.getSQLDefinition());
  	    sqlDescription.add(this.getInstanceReferenceCollectionTableColumnNameSQLDefinition());
  	} else {
  	    sqlDescription.add(this.getSQLDefinition());
  	}
  	return sqlDescription;	
    }
    
    @Override
    public int create() {
	throw new NotSupportedException();
    }
    
    public void setName(String identifier) {
	this.currentDelegate.setName(identifier);
	this.loadContext();
    }
    
    public void setCode(String identifier) {
	this.currentDelegate.setCode(identifier);
	this.loadContext();
    }
    
    public void setInternalId(Long p) {
	this.currentDelegate.setInternalId(p);
	this.loadContext();
    }
    
    /**
     * Load the property context (identifier)
     */
    private void loadContext() {
	this.getName(); // force loading identifier from database
    }
    
    public String getTableName() {
	return MariusQLConstants.M_PROPERTY_TABLE_NAME;
    }
    

    /**
     * @return Returns the external identifier of this property
     */
    public String getExternalId() {
	throw new NotSupportedException();
    }

    @Override
    public void drop() {
	throw new NotSupportedException();
    }

    @Override
    public int delete() {
	Datatype range = this.getRange();
	int res = this.currentDelegate.delete();
	
	if (!range.isSimpleType()){
	    return range.delete();
	}
	
	return res;
    }
    
    /**
     * The column name in instance level in which to store the table name where to fetch the referenced instance
     * 
     * @return
     */
    public String getInstanceReferenceTableColumnName() {
	if (!this.getRange().isAssociationType()) {
	    throw new MariusQLException("The property is not of reference datatype");
	}
	
	return MariusQLConstants.PROPERTY_PREFIX_COLUMN_TABLE_NAME 
		+ this.getInternalId() 
		+ MariusQLConstants.UNDERSCORE 
		+ MariusQLConstants.PROPERTY_SUFFIX_COLUMN_REF_TABLE_NAME;
    }
    
    /**
     * The column name in instance level in which to store the table name where to fetch the referenced instance
     * 
     * @return
     */
    public String getInstanceReferenceCollectionTableColumnName() {
	if (!this.getRange().isCollectionAssociationType()) {
	    throw new MariusQLException("The property is not of collection of references datatype");
	}
	
	return MariusQLConstants.PROPERTY_PREFIX_COLUMN_TABLE_NAME 
		+ this.getInternalId() 
		+ MariusQLConstants.UNDERSCORE 
		+ MariusQLConstants.PROPERTY_SUFFIX_COLUMN_REF_COLLECTION_TABLE_NAME;
    }
    
    /**
     * @return
     */
    public String getInstanceReferenceTableColumnNameSQLDefinition() {
	return this.getInstanceReferenceTableColumnName() + " " + this.getRange().toSQL();
    }
    
    /**
     * @return
     */
    public String getInstanceReferenceCollectionTableColumnNameSQLDefinition() {
	return this.getInstanceReferenceCollectionTableColumnName() + " " + this.getRange().toSQL();
    }
    
    @Override
    public String getSQLDefinition() {
	String res = null;

	Datatype range = this.getRange();
	res = this.toSQL(false) + " " + range.toSQL();

	return res;
    }
    
    @Override
    public String toSQL(Category context, boolean polymorph) {
	throw new NotSupportedException();
    }
    
    public String toPropertyText() {
	return toPropertyText(this.getCurrentContext());
    }
    
    /**
     * @param currentContext
     * @return
     */
    public String toPropertyText(Category currentContext) {
	boolean polymorph = this.getCurrentContext() != null ? currentContext.isPolymorph() : false;
	return this.toPropertyText(currentContext, polymorph);
    }
    
    /**
     * @param context
     * @param polymorph
     * @return
     */
    protected String toPropertyText(Category context, boolean polymorph) {
	String res = MariusQLConstants.NULL_VALUE;
	if (context != null) {
	    String tableAlias = context.getTableAlias();
	     String alias = tableAlias==null ? context.toSQL() :
	     tableAlias;
	    res = alias + "." + MariusQLConstants.RID_COLUMN_NAME;
	} else {
	    res = MariusQLConstants.RANGE_CORE_ATTRIBUTE_NAME;
	}
	return res;
    }
    
    public List<Long> getInstanceReferenceCollectionTableColumnValues(List<Long> refRids) {
	if (!this.getRange().isCollectionAssociationType()) {
	    throw new MariusQLException("The property is not of collection of references datatype");
	}
	
	List<Long> refClassRids = new ArrayList<Long>();
	refClassRids.add(new Long(3));
	
	DatatypeReference reference = this.getRange().toCollection().getDatatype().toReference();
	MariusQLStatement statement = this.getSession().createMariusQLStatement();
	
	for (int i = 0; i < refRids.size(); i++) {
	    String mariusql = "SELECT c." + MariusQLConstants.TYPE_OF_ID_TOKEN_NAME +
		    " FROM " + reference.getCategory().getName() + " c" + 
		    " WHERE " + MariusQLConstants.RID_COLUMN_NAME + " = " + refRids.get(i);

	    try {
		ResultSet res = statement.executeQuery(mariusql);
		res.next();
		refClassRids.add(res.getLong(1));
	    } catch (SQLException e) {
		throw new MariusQLException("fatal error");
	    }
	}
	
	statement.close();
	
	return refClassRids;
    }

    @Override
    public boolean isCore() {
	throw new NotSupportedException();
    }

    @Override
    public void setIsCore(boolean isCore) {
	throw new NotSupportedException();
    }
    
    @Override
    public boolean equals(Object obj) {
	if (!(obj instanceof MProperty)) {
	    return false;
	}
	
	MProperty property = (MProperty) obj;
	
	return property.getName().equals(this.getName());
    }
    
    @Override
    public void setValue(MMAttribute pAttribute, String value) {
	String attributeName = pAttribute.getName();
	
	if (attributeName.equals(MariusQLConstants.RID_COLUMN_NAME)) {
	    this.setInternalId(Long.valueOf(value));
	} else if (attributeName.equals(MariusQLConstants.CODE_CORE_ATTRIBUTE_NAME)) {
	    this.setCode(value);
	} else if (attributeName.equals(MariusQLConstants.NAME_CORE_ATTRIBUTE_NAME)) {
	    this.setName(value);
	} else {
	    this.currentDelegate.setStringValue(pAttribute, value);
	}
	
	if (this.isIdentifier()) {
	    this.loadContext();
	}
    }
    
    @Override
    public void setValue(MMAttribute pAttribute, String lg, String value) {
	this.currentDelegate.setStringValue(pAttribute, lg, value);
	
	if (this.isIdentifier()) {
	    this.loadContext();
	}
    }
    
    @Override
    public String getValue(String attributeName) {
	return this.currentDelegate.getStringValue(attributeName);
    }
    
    @Override
    public MMEntity getMMEntity() {
	return this.currentDelegate.getMMEntity();
    }
    
    @Override
    public String toString() {
	return this.getName();
    }
    
    public Long getInternalId(boolean forceLoading) {
	return this.currentDelegate.getInternalId(forceLoading);
    }
}
