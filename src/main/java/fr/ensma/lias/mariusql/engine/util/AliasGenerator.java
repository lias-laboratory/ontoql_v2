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
package fr.ensma.lias.mariusql.engine.util;

import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * Generates class/table/column aliases during semantic analysis and SQL
 * rendering.
 *
 * @author Mickael BARON
 * @author Stéphane JEAN
 */
public class AliasGenerator {

	/**
	 * Counter use to generate a value.
	 */
	private int next = 0;

	/**
	 * Default constructor.
	 */
	public AliasGenerator() {
	}

	/**
	 * @return the next value of the internal counter
	 */
	private int nextCount() {
		return next++;
	}

	/**
	 * Generate a name.
	 * 
	 * @param name a name used to generate an another name
	 * @return the generated name
	 */
	public final String createName(final String name) {
		return StringHelper.generateAlias(name, nextCount());
	}

	/**
	 * Generate an alias for a namespace.
	 */
	public final String createAliasNamespace(final String name) {
		return name.substring(0, 1).toLowerCase() + "_" + name.substring(1, 2).toLowerCase();
	}
}
