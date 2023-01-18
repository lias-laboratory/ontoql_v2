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
import fr.ensma.lias.mariusql.core.AbstractDatatypeEnumerate;
import fr.ensma.lias.mariusql.exception.NotYetImplementedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * @author Mickael BARON
 */
public class MDatatypeEnumerate extends AbstractDatatypeEnumerate {

    protected List<String> values = null;
    
    public MDatatypeEnumerate(MariusQLSession pSession) {
	super(pSession);
    }

    public MDatatypeEnumerate(MariusQLSession pSession, Long pRid) {
	super(pSession, pRid);
    }
    
    @Override
    public List<String> getValues() {
	throw new NotYetImplementedException();
    }

    @Override
    public void addValues(List<String> p) {
	throw new NotYetImplementedException();    
    }

    @Override
    public void addValue(String p) {
	throw new NotYetImplementedException();	
    }

    @Override
    public void setValue(String oldValue, String newValue) {
	throw new NotYetImplementedException();
    }

    @Override
    public void setValues(List<String> p) {
	this.values = p;
    }

    @Override
    public int insert() {
	this.setInternalId(this.getSession().getMariusQLTypes().getSequenceNextVal(MariusQLConstants.MODEL_SEQUENCE_NAME));

	List<String> columnsName = new ArrayList<String>();
	columnsName.add(MariusQLConstants.RID_COLUMN_NAME);
	columnsName.add(MariusQLConstants.M_DATATYPE_DTYPE_ATTRIBUTE_NAME);	
	columnsName.add(MariusQLConstants.M_DATATYPE_ENUMVALUES_ATTRIBUTE_NAME);
	
	List<String> columnsValue = new ArrayList<String>();
	columnsValue.add(this.getInternalId().toString());
	columnsValue.add(StringHelper.addSimpleQuotedString(this.getName()).toLowerCase());
	columnsValue.add(this.getSession().getMariusQLTypes().getCollectionStringQuotedSQLValue(values));	
	
	return session.getMariusQLDML().insertRow(this.getTableName(), columnsName, columnsValue);
    }    

    @Override
    public String toSQL() {
	return getSession().getMariusQLTypes().getStringType();
    }
}
