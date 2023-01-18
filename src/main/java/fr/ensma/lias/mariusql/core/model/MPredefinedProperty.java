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

import fr.ensma.lias.mariusql.core.Category;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.exception.NotSupportedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * The MProperty that represent a predefined property. 
 * Currently, there are RID and TYPEOF_ID as predefined properties
 * 
 * @author Florian MHUN
 */
public class MPredefinedProperty extends MProperty {
    
    private String code;
    
    public MPredefinedProperty(MariusQLSession p, String code, MMEntity pEntity) {
	super(p, pEntity);
	
	this.code = code;
    }
    
    public MPredefinedProperty(MariusQLSession p, String code, MMEntity pEntity, MClass pScopeClass) {
	super(p, pEntity, pScopeClass);
	
	this.code = code;
    }
    
    public String toSQLNullValue(boolean withPostAlias) {
	return "null" + this.getSession().getMariusQLTypes().getCastProjection(this.getRange().toSQL()) + " " + (withPostAlias ? ("as " + this.toSQL()) : "");
    }
    
    @Override
    public String getName() {
	return this.code;
    }
    
    @Override
    public String toSQL() {
	return this.code;
    }
    
    @Override
    public String toSQL(Category currentContext) {
	StringBuilder sql = new StringBuilder();
	sql.append("");
	
	if(currentContext.getTableAlias() != null) {
	    sql.append(currentContext.getTableAlias() + ".");  
	}
	
	sql.append(this.toSQL());
	return sql.toString();
    }
    
    @Override
    public int insert() {
	throw new NotSupportedException();
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
	throw new NotSupportedException();
    }
}
