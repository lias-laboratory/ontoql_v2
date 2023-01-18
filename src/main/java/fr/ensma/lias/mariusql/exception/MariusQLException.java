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
package fr.ensma.lias.mariusql.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mickael BARON
 */
public class MariusQLException extends RuntimeException {

	private static final long serialVersionUID = 5336316143576614936L;

	private static Logger log = LoggerFactory.getLogger(MariusQLException.class);

	public MariusQLException() {
		super();
	}

	public MariusQLException(String pMessage) {
		super(pMessage);

		log.debug(pMessage);
	}

	public MariusQLException(Throwable root) {
		super(root);
	}
}
