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

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.DatatypeBoolean;
import fr.ensma.lias.mariusql.exception.NotSupportedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * (OLD: EntityDatatypeIntOntoDB)
 * 
 * The datatype int.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 */
public class MMDatatypeBoolean extends DatatypeBoolean {

    public MMDatatypeBoolean(MariusQLSession pSession, Long pRid) {
	super(pSession, pRid);
    }

    @Override
    public String getTableName() {
	return MariusQLConstants.MM_DATATYPE_TABLE_NAME;
    }

    @Override
    public int create() {
	throw new NotSupportedException();
    }

    @Override
    public int update() {
	throw new NotSupportedException();
    }

    @Override
    public String toSQL() {
	return getSession().getMariusQLTypes().getBooleanType();
    }
}
