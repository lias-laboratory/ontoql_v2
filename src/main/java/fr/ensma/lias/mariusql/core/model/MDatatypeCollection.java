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
import fr.ensma.lias.mariusql.core.AbstractDatatypeCollection;
import fr.ensma.lias.mariusql.core.Datatype;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * @author Florian MHUN
 */
public class MDatatypeCollection extends AbstractDatatypeCollection {

    public MDatatypeCollection(MariusQLSession pSession, Long pRid, Datatype collectionType) {
 	super(pSession, pRid);
 	
 	this.setDatatype(collectionType);
    }
    
    public MDatatypeCollection(MariusQLSession pSession, Datatype collectionType, boolean pIsManyToMany) {
	super(pSession, null);
	
 	this.setDatatype(collectionType);
 	this.setManyToMany(pIsManyToMany);
    }

    @Override
    public Datatype getDatatype() {
	if (this.datatype == null) {
	    throw new MariusQLException("The data type is null");
	} else {
	    return this.datatype;
	}
    }

    @Override
    public String toSQL() {
	return getSession().getMariusQLTypes().getCollectionType(this.getDatatype().getDatatypeEnum());
    }
    
    @Override
    public int insert() {
	List<String> columns = new ArrayList<String>();
	
	this.setInternalId(this.getSession().getMariusQLTypes().getSequenceNextVal(MariusQLConstants.MODEL_SEQUENCE_NAME));
	
	columns.add(MariusQLConstants.RID_COLUMN_NAME);
	columns.add(MariusQLConstants.M_DATATYPE_DTYPE_ATTRIBUTE_NAME);
	columns.add(MariusQLConstants.M_DATATYPE_COLLECTION_TYPE_ATTRIBUTE_NAME);
	columns.add(MariusQLConstants.M_DATATYPE_IS_SIMPLE_TYPE_ATTRIBUTE_NAME);
	
	List<String> values = new ArrayList<String>();
	values.add(this.getInternalId().toString());
	values.add(this.getDatatypeEnum().toSQL());
	values.add(this.getDatatype().getInternalId().toString());
	values.add(new Boolean(false).toString());
	
	int insertedRows = this.getSession().getMariusQLDML().insertRow(this.getTableName(), columns, values);

	return insertedRows;
    }
}
