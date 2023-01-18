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
package fr.ensma.lias.mariusql.cache.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * @author Mickael BARON
 */
public abstract class AbstractCache<C> implements DataCache<C> {

	protected Logger log = LoggerFactory.getLogger(DataCache.class);

	private MariusQLSession session;

	/**
	 * To know if the cache is initialized.
	 */
	private boolean initialized;

	/**
	 * Cache enabled or disabled
	 */
	private boolean enabled;

	public AbstractCache(MariusQLSession pSession) {
		this.session = pSession;
		this.enabled = false;
		this.initialized = false;
	}

	protected MariusQLSession getSession() {
		return this.session;
	}

	@Override
	public void initialize() {
		if (this.initialized) {
			throw new MariusQLException("Meta model cache is already initialized.");
		}

		this.initialized = true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;

		if (!this.isInitialized() && enabled) {
			this.initialize();
		}
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}

	@Override
	public void clean() {
		this.initialized = false;
	}
}
