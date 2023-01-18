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
public interface MariusQLDMLDriver extends MariusQLDriver {

	/**
	 * @param name
	 * @param columns
	 * @param values
	 * @return
	 */
	int insertRow(String name, List<String> columns, List<String> values);

	/**
	 * @param name
	 * @param columns
	 * @return
	 */
	int updateRow(String name, List<String> columns, List<String> values, List<String> constraintNames,
			List<String> constraintValues);

	/**
	 * @param name
	 * @param column
	 * @param value
	 * @param constraint
	 * @return
	 */
	int updateRow(String name, String column, String value, String constraintName, String constraintValue);

	/**
	 * @param name
	 * @param rid
	 * @return
	 */
	int deleteRow(String name, Long rid);
}
