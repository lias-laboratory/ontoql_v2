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

import fr.ensma.lias.mariusql.exception.NotSupportedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * Datatype for relationship.
 * 
 * @author Mickael BARON
 * @author Florian MHUN
 */
public abstract class AbstractDatatypeCollection extends AbstractDatatype implements DatatypeCollection {

	protected Datatype datatype = null;

	protected Long internalDatatypeRid;

	protected boolean isManyToMany;

	/**
	 * @param pSession
	 * @param pRid
	 */
	public AbstractDatatypeCollection(MariusQLSession pSession, Long pRid) {
		super(pSession, pRid);
	}

	@Override
	public boolean isAssociationType() {
		return false;
	}

	@Override
	public boolean isCollectionType() {
		return true;
	}

	@Override
	public boolean isCollectionAssociationType() {
		return this.getDatatype().isAssociationType();
	}

	@Override
	public DatatypeEnum getDatatypeEnum() {
		return DatatypeEnum.DATATYPECOLLECTION;
	}

	@Override
	public List<String> getBooleanOperators() {
		throw new NotSupportedException();
	}

	@Override
	public List<String> getArithmeticOperators() {
		throw new NotSupportedException();
	}

	@Override
	public void setDatatype(Datatype d) {
		this.datatype = d;
	}

	public void setInternalDatatypeId(Long d) {
		this.internalDatatypeRid = d;
	}

	public Long getInternalDatatypeId() {
		return internalDatatypeRid;
	}

	@Override
	public boolean isManyToMany() {
		return this.isManyToMany;
	}

	@Override
	public void setManyToMany(boolean p) {
		this.isManyToMany = p;
	}

	@Override
	public Description getReverseDescription() {
		throw new NotSupportedException();
	}

	@Override
	public void setReverseDescription(Description p) {
		throw new NotSupportedException();
	}

	@Override
	public int delete() {
		this.getSession().getMariusQLDML().deleteRow(this.getTableName(), this.getInternalId());

		if (!this.getDatatype().isSimpleType()) {
			return this.getDatatype().delete();
		} else {
			return 0;
		}
	}
}
