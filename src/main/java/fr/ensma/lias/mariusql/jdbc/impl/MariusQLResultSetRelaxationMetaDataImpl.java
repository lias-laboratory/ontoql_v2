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
package fr.ensma.lias.mariusql.jdbc.impl;

import fr.ensma.lias.mariusql.jdbc.MariusQLResultSetRelaxationMetaData;

/**
 * @author Geraud FOKOU
 * @author Mickael BARON
 */
public class MariusQLResultSetRelaxationMetaDataImpl implements MariusQLResultSetRelaxationMetaData {

	private RelaxationEnum relaxationType;

	private String queryToRelax;

	private String relaxedQuery;

	private int relaxationStep;

	private String classToRelax;

	private String relaxedClass;

	private double similarityBetweenQuery;

	public MariusQLResultSetRelaxationMetaDataImpl(RelaxationEnum relaxationType, String queryToRelax,
			String relaxedQuery, int relaxationStep, String classToRelax, String relaxedClass,
			double similarityBetweenQuery) {
		this.relaxationType = relaxationType;
		this.queryToRelax = queryToRelax;
		this.relaxedQuery = relaxedQuery;
		this.relaxationStep = relaxationStep;
		this.classToRelax = classToRelax;
		this.relaxedClass = relaxedClass;
		this.similarityBetweenQuery = similarityBetweenQuery;
	}

	public RelaxationEnum getRelaxationType() {
		return relaxationType;
	}

	public String getQueryToRelax() {
		return queryToRelax;
	}

	public String getRelaxedQuery() {
		return relaxedQuery;
	}

	public int getRelaxationStep() {
		return relaxationStep;
	}

	public String getClassToRelax() {
		return classToRelax;
	}

	public String getRelaxedClass() {
		return relaxedClass;
	}

	public double getSimilarityBetweenQuery() {
		return similarityBetweenQuery;
	}
}
