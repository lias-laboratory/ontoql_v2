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
package fr.ensma.lias.mariusql.driver;

import java.util.List;

/**
 * @author Mickael BARON
 * @author Ghada TRIKI
 */
public interface MariusQLDDLDriver extends MariusQLDriver {

	/**
	 * Create table
	 * 
	 * @param name
	 * @param primaryKeyName
	 * @param sequenceName
	 * @param attributes
	 * @param constraints
	 */
	void createTable(String name, String primaryKeyName, String primaryKeyType, String sequenceName,
			List<String> attributes);

	/**
	 * Add table columns
	 * 
	 * @param name
	 * @param newColumns
	 */
	int addTableColumns(String name, List<String> newColumns);

	/**
	 * Remove table columns
	 * 
	 * @param name
	 * @param columns
	 */
	int removeTableColumns(String name, List<String> columns);

	/**
	 * Drop table
	 * 
	 * @param name
	 * @param newColumns
	 */
	void dropTable(String name);

	/**
	 * Create sequence from a name.
	 * 
	 * @param name
	 */
	void createSequence(String name);

	/**
	 * Drop sequence if exists from a name.
	 * 
	 * @param name
	 */
	void dropSequence(String name);
}
