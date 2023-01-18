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
import fr.ensma.lias.mariusql.core.AbstractDatatypeReference;
import fr.ensma.lias.mariusql.core.Category;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * @author Mickael BARON
 * @author Ghada TRIKI
 * @author Florian MHUN
 */
public class MMDatatypeReference extends AbstractDatatypeReference {
    
    public MMDatatypeReference(MariusQLSession pSession, MMEntity entity) {
	super(pSession);
	this.setCategory(entity);
    }
    public MMDatatypeReference(MariusQLSession pSession, Long pRid, Long pCatId) {
	super(pSession, pRid);
	
	this.setInternalCategoryId(pCatId);
    }
    
    @Override
    public Category getCategory() {
	if (this.category == null) {
	    if (this.getInternalCategoryId() == null) {
		throw new MariusQLException("the categoryId is null");			
	    }
	    this.category = FactoryCore.createExistingMMEntity(this.getSession(), this.getInternalCategoryId());
	}
	return this.category;
    }

    @Override
    public String getTableName() {
	return MariusQLConstants.MM_DATATYPE_TABLE_NAME;
    }

    @Override
    public String toSQL() {
	return this.getSession().getMariusQLTypes().getReferenceType();
    }
    
    @Override
    public int insert() {
	this.setInternalId(this.getSession().getMariusQLTypes().getSequenceNextVal(MariusQLConstants.META_MODEL_SEQUENCE_NAME));
	
	List<String> columnsName = new ArrayList<String>();
	columnsName.add(MariusQLConstants.RID_COLUMN_NAME);
	columnsName.add(MariusQLConstants.MM_DATATYPE_DTYPE_ATTRIBUTE_NAME);	
	columnsName.add(MariusQLConstants.MM_DATATYPE_ON_CLASS_ATTRIBUTE_NAME);
	columnsName.add(MariusQLConstants.MM_DATATYPE_IS_SIMPLE_TYPE_ATTRIBUTE_NAME);
	List<String> columnsValue = new ArrayList<String>();
	columnsValue.add(this.getInternalId().toString());
	columnsValue.add(StringHelper.addSimpleQuotedString(this.getName()).toLowerCase());
	columnsValue.add(this.getCategory().getInternalId().toString());
	columnsValue.add(new Boolean(false).toString());
	
	int returnInsertedRows = session.getMariusQLDML().insertRow(this.getTableName(), columnsName, columnsValue);
		
	return returnInsertedRows;
    }
}
