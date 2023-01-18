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
package fr.ensma.lias.mariusql.engine.tree;

import fr.ensma.lias.mariusql.engine.MariusQLSQLWalker;

/**
 * Common interface modeling the different OntoQL statements (i.e., INSERT,
 * UPDATE, DELETE, SELECT).
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 */
public interface Statement {

	/**
	 * Retreive the "phase 2" walker which generated this statement tree.
	 * 
	 * @return The OntoQLSQLWalker instance which generated this statement tree.
	 */
	MariusQLSQLWalker getWalker();

	/**
	 * Return the main token type representing the type of this statement.
	 * 
	 * @return The corresponding token type.
	 */
	int getStatementType();
}
