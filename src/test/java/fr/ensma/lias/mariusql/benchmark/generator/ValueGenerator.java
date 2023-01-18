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
package fr.ensma.lias.mariusql.benchmark.generator;

import fr.ensma.lias.mariusql.benchmark.generator.datatypes.TypedValueGenerator;
import fr.ensma.lias.mariusql.core.Datatype;
import fr.ensma.lias.mariusql.exception.MariusQLException;

/**
 * @author Florian MHUN
 */
public class ValueGenerator implements Generator {

	private Datatype currentDatatype;

	public ValueGenerator() {
	}

	@Override
	public String generate() {
		if (currentDatatype == null) {
			throw new MariusQLException("Must set a datatype before generate value");
		}

		TypedValueGenerator valueGenerator = FactoryGenerator.createTypedValueGenerator(currentDatatype);

		return valueGenerator.generate();
	}

	public Datatype getCurrentDatatype() {
		return currentDatatype;
	}

	public void setCurrentDatatype(Datatype currentDatatype) {
		this.currentDatatype = currentDatatype;
	}

}
