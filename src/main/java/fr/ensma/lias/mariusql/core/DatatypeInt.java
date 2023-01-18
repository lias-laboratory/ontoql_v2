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

import java.util.List;

import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * (OLD: EntityDatatypeInt)
 * 
 * The datatype int.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 * @author Florian MHUN
 */
public abstract class DatatypeInt extends AbstractDatatype {

	public DatatypeInt(MariusQLSession pSession, Long pRid) {
		super(pSession, pRid);
	}

	@Override
	public DatatypeEnum getDatatypeEnum() {
		return DatatypeEnum.DATATYPEINT;
	}

	@Override
	public List<String> getBooleanOperators() {
		return getDatatypeEnum().getBooleanOperators();

	}

	@Override
	public List<String> getArithmeticOperators() {
		return getDatatypeEnum().getArithmeticOperators();
	}

	@Override
	public boolean isAssociationType() {
		return false;
	}

	@Override
	public boolean isCollectionType() {
		return false;
	}

	@Override
	public boolean isCollectionAssociationType() {
		return false;
	}

	@Override
	public boolean isMultilingualType() {
		return false;
	}

	@Override
	public int delete() {
		return 0;
	}
}
