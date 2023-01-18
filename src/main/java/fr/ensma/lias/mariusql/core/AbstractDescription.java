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
package fr.ensma.lias.mariusql.core;

import java.util.Arrays;
import java.util.List;

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * @author Mickael BARON
 * @author Florian MHUN
 */
public abstract class AbstractDescription implements Description {

	/**
	 * The list of available description name to identify a MariusQL entity
	 */
	public final static List<String> IDENTIFIER_LIST = Arrays.asList(MariusQLConstants.RID_COLUMN_NAME,
			MariusQLConstants.CODE_CORE_ATTRIBUTE_NAME, MariusQLConstants.NAME_CORE_ATTRIBUTE_NAME);

	protected MariusQLSession refSession;

	public AbstractDescription(MariusQLSession pSession) {
		this.refSession = pSession;
	}

	@Override
	public String toSQL() {
		return toSQL(true);
	}

	public MariusQLSession getSession() {
		return this.refSession;
	}

	@Override
	public boolean isIdentifier() {
		return IDENTIFIER_LIST.contains(this.getName());
	}

	@Override
	public String toString() {
		return "Description[" + this.getInternalId() + "]";
	}
}
