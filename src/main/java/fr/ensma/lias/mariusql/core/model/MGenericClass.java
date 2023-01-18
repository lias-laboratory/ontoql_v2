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
import fr.ensma.lias.mariusql.core.AbstractCategory;
import fr.ensma.lias.mariusql.core.Category;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.metamodel.MMAttribute;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.core.model.MClassAndMPropertyDelegate.InternalIdentifierFunction;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotYetImplementedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * @author Mickael BARON
 * @author Valentin CASSAIR
 * @author Florian MHUN
 */
public abstract class MGenericClass extends AbstractCategory implements ModelElement {
    
    protected MClassAndMPropertyDelegate currentDelegate;
    
    /**
     * All the direct subclasses of the class
     */
    protected List<Category> directSubclasses = null;
    
    protected MGenericClass superClass = null;
    
    protected List<MGenericClass> allSuperClasses = null;
    
    public MGenericClass(MariusQLSession p, MMEntity pEntity) {
	super(p);
	
	this.currentDelegate = new MClassAndMPropertyDelegate(p, pEntity, new InternalIdentifierFunction() {
	    @Override
	    public Long getInternalId() {
		String attribute = currentDelegate.getExternalIdentifierAttribute();
		String sqlValue = currentDelegate.getExternalIdentifierSQLValue();
		
		return MGenericClass.this.getSession().getMariusQLDQL().getMGenericClassRidByIdentifier(currentDelegate.getMTableName(), attribute, sqlValue);
	    }
	});
    }    
    
    public Long getInternalId(boolean forceLoading) {
	return this.currentDelegate.getInternalId(forceLoading);
    }
    
    @Override
    public Long getInternalId() {
	return this.currentDelegate.getInternalId();
    }

    @Override
    public String getName() {
	return this.currentDelegate.getIdentifier();
    }

    @Override
    public boolean isClass() {
	return true;
    }

    @Override
    public boolean isEntity() {
	return false;
    }

    @Override
    public String getTypeLabel() {
	return "class";
    }
    
    public List<MGenericClass> getAllSubClasses() {
	final List<Category> allSubcategories = this.getAllSubcategories();
	
	List<MGenericClass> genericClass = new ArrayList<MGenericClass>();
	for (Category category : allSubcategories) {
	    if (category instanceof MGenericClass) {
		genericClass.add((MGenericClass)category);
	    } else {
		throw new NotYetImplementedException();
	    }
	}
	
	return genericClass;
    }
    
    @Override
    public List<Category> getDirectSubCategories() {
	if (!isLoadedDirectSubclasses()) {
	    final List<String> subMClassNamesByRid = this.getSession().getMariusQLDQL().getSubMClassIdentifierByRid(this.getInternalId());
	    List<Category> classes = new ArrayList<Category>();

	    for (String name : subMClassNamesByRid) {
		classes.add(FactoryCore.createExistingMGenericClass(session, name));
	    }
	    
	    this.directSubclasses = classes;
	}
	return directSubclasses;
    }

    @Override
    public int insert() {
	if (!this.currentDelegate.isValueDefined(MariusQLConstants.PACKAGE_NAME_CORE_ATTRIBUTE_NAME)) {
	    this.currentDelegate.setStringValue(MariusQLConstants.PACKAGE_NAME_CORE_ATTRIBUTE_NAME, this.getSession().getDefaultNameSpace());
	}
	
	int res = this.currentDelegate.insert(true);
	
	return res;
    }
   
    @Override
    public int update() {
	if (!this.currentDelegate.isValueDefined(MariusQLConstants.PACKAGE_NAME_CORE_ATTRIBUTE_NAME)) {
	    this.currentDelegate.setStringValue(MariusQLConstants.PACKAGE_NAME_CORE_ATTRIBUTE_NAME, this.getSession().getDefaultNameSpace());
	}
	return this.currentDelegate.update(true);
    }

    @Override
    public void setValue(MMAttribute pAttribute, String value) {
	String attributeName = pAttribute.getName();
	
	if (attributeName.equals(MariusQLConstants.RID_COLUMN_NAME)) {
	    this.setInternalId(value);
	} else if (attributeName.equals(MariusQLConstants.CODE_CORE_ATTRIBUTE_NAME)) {
	    this.setCode(value);
	} else if (attributeName.equals(MariusQLConstants.NAME_CORE_ATTRIBUTE_NAME)) {
	    this.setName(value);
	} else {
	    this.currentDelegate.setStringValue(pAttribute, value);
	}
    }
    
    @Override
    public void setValue(MMAttribute pAttribute, String lg, String value) {
	this.currentDelegate.setStringValue(pAttribute, lg, value);
    }
    
    @Override
    public String getValue(String attributeName) {
	return this.currentDelegate.getStringValue(attributeName);
    }
    
    @Override
    public MMEntity getMMEntity() {
	return this.currentDelegate.getMMEntity();
    }
    
    public void setName(String p) {
	this.currentDelegate.setName(p);
    }
    
    public void setCode(String p) {
	this.currentDelegate.setCode(p);
    }
    
    public void setInternalId(String p) {
	this.currentDelegate.setInternalId(Long.valueOf(p));
    }
    
    public void setSuperClass(MGenericClass superClass) {
	if (this.isAbstract() && !superClass.isAbstract()) {
	    throw new MariusQLException("Can not create static class under class");
	}
	
	this.currentDelegate.setObjectValue(MariusQLConstants.SUPER_CLASS_CORE_ATTRIBUTE_NAME, superClass);
    } 
    
    public MGenericClass getSuperClass() {
	if (!this.isLoadedSuperClass()) {
	    final MClass referenceValue = (MClass) this.currentDelegate.getReferenceValue(MariusQLConstants.M_CLASS_SUPERCLASS_ATTRIBUTE_NAME);	    
	    this.superClass = referenceValue;
	    
	    return referenceValue;	    
	} else {
	    return this.superClass;
	}	
    }
    
    public List<MGenericClass> getAllSuperClasses() {
	if (!this.isLoadedAllSuperClasses()) {
	    List<MGenericClass> allSuperClasses = new ArrayList<MGenericClass>();	
	    MGenericClass currentClass = this;		
	    
	    while(currentClass.getSuperClass() != null) {
		allSuperClasses.add(currentClass.getSuperClass());
		
		currentClass = currentClass.getSuperClass();
	    }
	    
	    return allSuperClasses;	    
	} else {
	    return this.allSuperClasses;
	}	
    }
    
    /**
     * True if all the direct subclasses of the class has been loaded.
     */
    protected boolean isLoadedDirectSubclasses() {
	return this.directSubclasses != null;
    }
    
    /**
     * True if superClass of the class has been loaded.
     * 
     * @return
     */
    protected boolean isLoadedSuperClass() {
	return this.superClass != null;
    }
    
    protected boolean isLoadedAllSuperClasses() {
	return this.allSuperClasses != null;
    }
    
    @Override
    public boolean isReferenced() {
	return this.getSession().getMariusQLDQL().isMGenericClassReferenced(this.getInternalId());
    }
}
