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

import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * @author Mickael BARON
 */
public class FactoryDriver {

	public static MariusQLDriverAPI create(MariusQLSession pSession, String driverClass) {
		Class<?> forName;
		try {
			forName = Class.forName(driverClass);
			final MariusQLDriverAPI newInstance = (MariusQLDriverAPI) forName.newInstance();
			newInstance.initialize(pSession);

			return newInstance;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new MariusQLException("Problem during the creation: " + e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new MariusQLException("Problem during the creation: " + e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new MariusQLException("Problem during the creation" + e.getMessage());
		}
	}
}
