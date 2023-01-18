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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import antlr.RecognitionException;
import fr.ensma.lias.mariusql.exception.MariusQLException;

/**
 * An error handler that counts parsing errors and warnings.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 */
public class ErrorCounter implements ParseErrorHandler {

	/**
	 * A logger for this class.
	 */
	private Logger log = LoggerFactory.getLogger(ErrorCounter.class);

	/**
	 * A logger for the parser class.
	 */
	private Logger errorCounterLog = LoggerFactory.getLogger("fr.ensma.lias.marius.engine.PARSER");

	/**
	 * List of errors.
	 */
	private List<String> errorList = new ArrayList<String>();

	/**
	 * List of warning.
	 */
	private List<String> warningList = new ArrayList<String>();

	/**
	 * List of error detected by the parser antlr.
	 */
	private List<RecognitionException> recognitionExceptions = new ArrayList<RecognitionException>();

	/**
	 * Report a RecognitionException (Tokenizer exception).
	 * 
	 * @param e the RecognitionException to report
	 * @see ParseErrorHandler#reportError()
	 */
	public final void reportError(final RecognitionException e) {
		reportError(e.toString());
		recognitionExceptions.add(e);
		if (log.isDebugEnabled()) {
			log.debug(e.getMessage(), e);
		}
	}

	/**
	 * Report a message as an error.
	 * 
	 * @param message the message to report as an error
	 * @see ParseErrorHandler#reportError()
	 */
	public final void reportError(final String message) {
		errorCounterLog.error(message);
		errorList.add(message);
	}

	/**
	 * Get the number of errors reported.
	 * 
	 * @return the number of errors reported
	 * @see ErrorCounter#getErrorCount()
	 */
	public final int getErrorCount() {
		return errorList.size();
	}

	/**
	 * Report a message a warning.
	 * 
	 * @param message the message to report as a warning
	 * @see ParseErrorHandler#reportWarning()
	 */
	public final void reportWarning(final String message) {
		errorCounterLog.debug(message);
		warningList.add(message);
	}

	/**
	 * Helper method to format a message of error.
	 * 
	 * @return a message of error as a String
	 */
	private String getErrorString() {
		StringBuilder buf = new StringBuilder();
		for (Iterator<String> iterator = errorList.iterator(); iterator.hasNext();) {
			buf.append((String) iterator.next());
			if (iterator.hasNext()) {
				buf.append("\n");
			}

		}
		return buf.toString();
	}

	/**
	 * Throw an exception if at least one error has been reported.
	 * 
	 * @see ParseErrorHandler#throwQueryException()
	 */
	public final void throwQueryException() {
		if (getErrorCount() > 0) {
			if (recognitionExceptions.size() > 0) {
				throw new MariusQLException("QuerySyntaxException" + recognitionExceptions.get(0));
			} else {
				throw new MariusQLException(getErrorString());
			}
		} else {
			// all clear
			if (log.isDebugEnabled()) {
				log.debug("throwQueryException() : no errors");
			}
		}
	}
}
