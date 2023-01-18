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
package fr.ensma.lias.mariusql.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Mickael BARON
 */
public interface MariusQLResultSet extends ResultSet {

	/**
	 * @return
	 */
	MariusQLResultSetMetaData getMariusQLMetaData();

	/**
	 * @param step
	 * @return
	 */
	MariusQLResultSetMetaData getMariusQLMetaData(int step);

	/**
	 * @return
	 */
	int getStepNumber();

	/**
	 * @param columnIndex
	 * @param classType
	 * @return
	 * @throws SQLException
	 */
	<T> List<T> getList(int columnIndex, Class<T> classType) throws SQLException;

	/**
	 * @param columnIndex
	 * @param classType
	 * @return
	 * @throws SQLException
	 */
	<T> List<T> getList(String columnName, Class<T> classType) throws SQLException;
}
