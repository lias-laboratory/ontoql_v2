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

import fr.ensma.lias.mariusql.core.Datatype;
import fr.ensma.lias.mariusql.core.DatatypeEnum;

/**
 * @author Mickael BARON
 * @author Ghada TRIKI
 * @author Florian MHUN
 */
public interface DataCache<C> {

	/**
	 * Must be fired on startup.
	 * 
	 * @param mariusQLSessionImpl
	 */
	void initialize();

	/**
	 * 
	 */
	void clean();

	/**
	 * Refresh the cache.
	 */
	void refresh();

	/**
	 * @param type
	 * @return
	 */
	Datatype getSimpleDatatype(DatatypeEnum type);

	/**
	 * @param pRid
	 * @return
	 */
	Datatype getSimpleDatatype(Long pRid);

	/**
	 * @param pRid
	 * @return
	 */
	Datatype getDatatype(Long pRid);

	void addDatatype(Long pRid, Datatype p);

	/**
	 * @param name
	 * @return
	 */
	void addElement(C name);

	/**
	 * @param name
	 */
	void removeElement(C name);

	/**
	 * @param name
	 * @return
	 */
	boolean isElementExists(String name);

	/**
	 * @param rid
	 * @return
	 */
	boolean isElementExists(Long rid);

	/**
	 * @param type
	 * @return
	 */
	boolean isSimpleTypeExists(DatatypeEnum type);

	/**
	 * @param name
	 * @return
	 */
	C getElement(String name);

	/**
	 * 
	 * @param rid
	 * @return
	 */
	C getElement(Long rid);

	/**
	 * Is the cache enabled
	 */
	boolean isEnabled();

	/**
	 * Enable or disable cache
	 * 
	 * @param enabled
	 */
	void setEnabled(boolean enabled);

	/**
	 * Is the cache initialized
	 * 
	 * @return
	 */
	boolean isInitialized();

	/**
	 * Get key which is stored into the map of object using the session namespace
	 * 
	 * @param name
	 * @return
	 */
	String getMappedKey(String name);
}
