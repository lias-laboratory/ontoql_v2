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
package fr.ensma.lias.mariusql.benchmark.generator.datatypes;

import java.util.ArrayList;
import java.util.List;

import fr.ensma.lias.mariusql.benchmark.generator.FactoryGenerator;
import fr.ensma.lias.mariusql.core.DatatypeCollection;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * @author Florian MHUN
 */
public class CollectionValueGenerator implements TypedValueGenerator {

	private TypedValueGenerator wrappedValueGenerator;

	public CollectionValueGenerator(DatatypeCollection collection) {
		this.wrappedValueGenerator = FactoryGenerator.createTypedValueGenerator(collection.getDatatype());
	}

	@Override
	public String generate() {
		List<String> values = new ArrayList<String>();

		for (int i = 0; i < 50; i++) {
			values.add(wrappedValueGenerator.generate());
		}

		return "ARRAY[" + StringHelper.join(",", values.iterator()) + "]";
	}

	@Override
	public DatatypeEnum getDatatypeEnum() {
		return DatatypeEnum.DATATYPECOLLECTION;
	}

}
