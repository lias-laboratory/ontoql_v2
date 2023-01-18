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
import fr.ensma.lias.mariusql.core.AbstractDatatypeCount;
import fr.ensma.lias.mariusql.exception.NotYetImplementedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.util.MariusQLHelper;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * The datatype count.
 * 
 * @author Mickael BARON
 */
public class MDatatypeCount extends AbstractDatatypeCount {

    public MDatatypeCount(MariusQLSession pSession, Long pRid) {
	super(pSession, pRid);	
    }
    
    public MDatatypeCount(MariusQLSession pSession) {
	super(pSession);	
    }

    @Override
    public String toSQL() {
	return this.getSession().getMariusQLTypes().getReferenceType() + this.getSession().getMariusQLTypes().getDefaultSequenceNextValue(this.getSequenceName());
    }
    
    @Override
    public int insert() {
	MariusQLHelper.isFeatureSupported(this.getSession().getMariusQLFeatures().isCountTypeSupported(), "CountType");

	this.setInternalId(this.getSession().getMariusQLTypes().getSequenceNextVal(MariusQLConstants.MODEL_SEQUENCE_NAME));

	List<String> columnsName = new ArrayList<String>();
	columnsName.add(MariusQLConstants.RID_COLUMN_NAME);
	columnsName.add(MariusQLConstants.M_DATATYPE_DTYPE_ATTRIBUTE_NAME);	
	
	List<String> columnsValue = new ArrayList<String>();
	final String internalId = this.getInternalId().toString();
	columnsValue.add(internalId);
	columnsValue.add(StringHelper.addSimpleQuotedString(this.getName()).toLowerCase());
	
	this.getSession().getMariusQLDDL().createSequence(this.getSequenceName());
	return this.getSession().getMariusQLDML().insertRow(this.getTableName(), columnsName, columnsValue);
    }

    @Override
    public int delete() {
	throw new NotYetImplementedException();
    }
    
    protected String getSequenceName() {
	return "counttype_" + this.getInternalId() + "_seq";
    }
}
