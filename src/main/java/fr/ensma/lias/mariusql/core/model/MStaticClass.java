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

import fr.ensma.lias.mariusql.core.Description;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotSupportedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * @author Mickael BARON
 * @author Ghada TRIKI
 * @author Florian MHUN
 */
public class MStaticClass extends MGenericClass {

    public MStaticClass(MariusQLSession pSession, MMEntity pEntity) {
	super(pSession, pEntity);	
    }

    @Override
    public int create() {
	throw new MariusQLException("Operation not available for static class");
    }

    @Override
    public Description getDefinedDescription(String identifier) {
	throw new MariusQLException("Operation not available for static class");
    }

    @Override
    public String toSQL() {
	throw new NotSupportedException();
    }
    
    @Override
    public boolean isAbstract() {
	return true;
    }
    
    @Override
    public void drop() {
	throw new MariusQLException("Operation not available for static class");
    }
    
    @Override
    public int delete() {
	int childrenCount = this.getSession().getMariusQLDQL().getNumberOfMGenericClassChildren(this);
	
	if (childrenCount > 0) {
	    throw new MariusQLException("Can not remove class which is parent class: " + this.getName());
	}
	
	int deletedRows = this.currentDelegate.delete();
	
	if (deletedRows > 0) {
	    this.getSession().getModelCache().removeElement(this);
	}
	
	return deletedRows;
    }

    @Override
    public boolean isModelInstanceExists(Long rid) {
	throw new NotSupportedException();
    }

    @Override
    public String toSQLWithAlias() {
	throw new NotSupportedException();
    }    
}
