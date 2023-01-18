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
package fr.ensma.lias.mariusql.benchmark.util;

/**
 * Timer class to track time execution Usage: {@code
 * 	Timer timer = new Timer();
 * 	timer.start();
 * 	
 * 	// later in the code...
 * 	timer.snaphot("Snapshot 1"); // will print elapsed time since start() was called
 * * 	timer.snaphot("Snapshot 2"); // will print elapsed time since the last call of snapshot()
 * }
 * 
 * @author Florian MHUN
 */
public class Timer {

	private long startAt;

	/**
	 * Take a snapshot of current time elapsed and print to stdout
	 * 
	 * @param subject
	 */
	public void snapshot(String subject) {
		if (startAt == 0) {
			throw new RuntimeException("timer is not started");
		}

		long endAt = System.nanoTime();
		System.out.println(subject + " - Elapsed time: " + toSecs(endAt - this.startAt) + " seconds");
		this.startAt = endAt;
	}

	/**
	 * Start the timer. Must be called before using the timer
	 */
	public void start() {
		this.startAt = System.nanoTime();
	}

	/**
	 * Convert nano seconds to seconds
	 * 
	 * @param nanoSecs
	 * @return
	 */
	private double toSecs(double nanoSecs) {
		return (double) nanoSecs / 1000000000.0;
	}
}