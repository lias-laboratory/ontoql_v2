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

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotSupportedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * (OLD: AbstractEntityDatatype)
 * 
 * Template implementation of the datatype.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 * @author Ghada TRIKI
 * @author Florian MHUN
 */
public abstract class AbstractDatatype implements Datatype {

	/**
	 * Id of this datatype.
	 */
	protected Long id;

	/**
	 * A session needed to access to the underlying OBDB
	 */
	protected MariusQLSession session;

	public AbstractDatatype(MariusQLSession pSession) {
		this.session = pSession;
	}

	public AbstractDatatype(MariusQLSession pSession, Long pInternalId) {
		this.session = pSession;
		this.id = pInternalId;
	}

	@Override
	public boolean isSimpleType() {
		return !this.isAssociationType() && !this.isCollectionType();
	}

	@Override
	public Long getInternalId() {
		return id;
	}

	@Override
	public void setInternalId(Long id) {
		this.id = id;
	}

	@Override
	public boolean isMultilingualType() {
		return false;
	}

	protected MariusQLSession getSession() {
		return session;
	}

	@Override
	public int create() {
		throw new NotSupportedException();
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
	public void drop() {
		throw new NotSupportedException();
	}

	@Override
	public int delete() {
		throw new NotSupportedException();
	}

	@Override
	public DatatypeCollection toCollection() {
		if (!this.isCollectionType()) {
			throw new MariusQLException("Datatype is not a collection");
		}

		return (DatatypeCollection) this;
	}

	@Override
	public DatatypeReference toReference() {
		if (!this.isAssociationType()) {
			throw new MariusQLException("Datatype is not a reference");
		}

		return (DatatypeReference) this;
	}

	@Override
	public String toString() {
		return "Datatype[" + this.getInternalId() + "]";
	}

	@Override
	public List<String> getArithmeticOperators() {
		return getDatatypeEnum().getBooleanOperators();
	}

	@Override
	public List<String> getBooleanOperators() {
		return getDatatypeEnum().getArithmeticOperators();
	}

	@Override
	public String getName() {
		return getDatatypeEnum().getName();
	}

	@Override
	public String getTableName() {
		return MariusQLConstants.M_DATATYPE_TABLE_NAME;
	}
}
