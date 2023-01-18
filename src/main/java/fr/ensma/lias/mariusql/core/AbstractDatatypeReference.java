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
 * @author Ghada TRIKI
 * @author Florian MHUN
 */
public abstract class AbstractDatatypeReference extends AbstractDatatype implements DatatypeReference {

	protected Category category = null;

	protected Long internalCategoryId;

	/**
	 * @param pSession
	 * @param pRid
	 */
	public AbstractDatatypeReference(MariusQLSession pSession) {
		super(pSession);
	}

	public AbstractDatatypeReference(MariusQLSession pSession, Long pRid) {
		super(pSession, pRid);
	}

	@Override
	public boolean isAssociationType() {
		return true;
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
	public DatatypeEnum getDatatypeEnum() {
		return DatatypeEnum.DATATYPEREFERENCE;
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
	public void setCategory(Category c) {
		this.category = c;
	}

	@Override
	public Category getCategory() {
		return category;
	}

	public void setInternalCategoryId(Long d) {
		this.internalCategoryId = d;
	}

	public Long getInternalCategoryId() {
		return internalCategoryId;
	}

	@Override
	public int delete() {
		return this.getSession().getMariusQLDML().deleteRow(this.getTableName(), this.getInternalId());
	}
}
