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

import fr.ensma.lias.mariusql.benchmark.generator.datatypes.BooleanValueGenerator;
import fr.ensma.lias.mariusql.benchmark.generator.datatypes.CollectionValueGenerator;
import fr.ensma.lias.mariusql.benchmark.generator.datatypes.IntegerValueGenerator;
import fr.ensma.lias.mariusql.benchmark.generator.datatypes.MultiStringValueGenerator;
import fr.ensma.lias.mariusql.benchmark.generator.datatypes.RealValueGenerator;
import fr.ensma.lias.mariusql.benchmark.generator.datatypes.ReferenceValueGenerator;
import fr.ensma.lias.mariusql.benchmark.generator.datatypes.StringValueGenerator;
import fr.ensma.lias.mariusql.benchmark.generator.datatypes.TypedValueGenerator;
import fr.ensma.lias.mariusql.benchmark.generator.datatypes.URIValueGenerator;
import fr.ensma.lias.mariusql.core.Datatype;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.exception.NotYetImplementedException;

/**
 * @author Florian MHUN
 */
public class FactoryGenerator {

	public static TypedValueGenerator createTypedValueGenerator(Datatype range) {
		DatatypeEnum datatype = range.getDatatypeEnum();

		if (datatype.equals(DatatypeEnum.DATATYPEINT)) {
			return new IntegerValueGenerator();
		} else if (datatype.equals(DatatypeEnum.DATATYPEREAL)) {
			return new RealValueGenerator();
		} else if (datatype.equals(DatatypeEnum.DATATYPEBOOLEAN)) {
			return new BooleanValueGenerator();
		} else if (datatype.equals(DatatypeEnum.DATATYPESTRING)) {
			return new StringValueGenerator();
		} else if (datatype.equals(DatatypeEnum.DATATYPEMULTISTRING)) {
			return new MultiStringValueGenerator();
		} else if (datatype.equals(DatatypeEnum.DATATYPEURI)) {
			return new URIValueGenerator();
		} else if (datatype.equals(DatatypeEnum.DATATYPECOLLECTION)) {
			return new CollectionValueGenerator(range.toCollection());
		} else if (datatype.equals(DatatypeEnum.DATATYPEREFERENCE)) {
			return new ReferenceValueGenerator(range.toReference());
		} else {
			throw new NotYetImplementedException(datatype.getName());
		}
	}

}
