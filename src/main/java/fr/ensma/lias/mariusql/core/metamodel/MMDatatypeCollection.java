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
import fr.ensma.lias.mariusql.core.AbstractDatatypeCollection;
import fr.ensma.lias.mariusql.core.Datatype;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * @author Mickael BARON
 * @author Florian MHUN
 */
public class MMDatatypeCollection extends AbstractDatatypeCollection {

    public MMDatatypeCollection(MariusQLSession pSession, Long pRid, Datatype collectionType, boolean pIsManyToMany) {
 	super(pSession, pRid);
 	
 	this.setDatatype(collectionType);
 	this.setManyToMany(pIsManyToMany);
    }
    
    public MMDatatypeCollection(MariusQLSession pSession, Datatype collectionType, boolean pIsManyToMany) {
	super(pSession, null);
	
 	this.setDatatype(collectionType);
 	this.setManyToMany(pIsManyToMany);
    }

    @Override
    public Datatype getDatatype() {
	if (this.datatype == null) {
	    throw new MariusQLException(" the datatype is null");
	} else {
	    return this.datatype;
	}
    }

    @Override
    public String getTableName() {
	return MariusQLConstants.MM_DATATYPE_TABLE_NAME;
    }

    @Override
    public String toSQL() {
	return getSession().getMariusQLTypes().getCollectionType(this.getDatatype().getDatatypeEnum());
    }
    
    @Override
    public int insert() {
	this.setInternalId(this.getSession().getMariusQLTypes().getSequenceNextVal(MariusQLConstants.META_MODEL_SEQUENCE_NAME));
	
	List<String> columns = new ArrayList<String>();
	columns.add(MariusQLConstants.RID_COLUMN_NAME);
	columns.add(MariusQLConstants.MM_DATATYPE_DTYPE_ATTRIBUTE_NAME);
	columns.add(MariusQLConstants.MM_DATATYPE_COLLECTION_TYPE_ATTRIBUTE_NAME);
	columns.add(MariusQLConstants.MM_DATATYPE_IS_MANY_TO_MANY_ATTRIBUTE_NAME);
	columns.add(MariusQLConstants.MM_DATATYPE_IS_SIMPLE_TYPE_ATTRIBUTE_NAME);
	
	List<String> values = new ArrayList<String>();
	values.add(this.getInternalId().toString());
	values.add(StringHelper.addSimpleQuotedString(this.getDatatypeEnum().getName()).toLowerCase());
	values.add(this.getDatatype().getInternalId().toString());
	values.add(new Boolean(this.isManyToMany()).toString());
	values.add(new Boolean(false).toString());
	
	return this.getSession().getMariusQLDML().insertRow(this.getTableName(), columns, values);
    }
}
