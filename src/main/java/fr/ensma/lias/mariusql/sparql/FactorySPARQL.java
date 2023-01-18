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
package fr.ensma.lias.mariusql.sparql;

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.engine.SPARQLMariusQLWalker;

/**
 * Factory of SPARQL element
 * 
 * @author Stephane JEAN
 * @author Mickael BARON
 * @author Raoul TIAM
 * @author Adel GHAMNIA
 */
public class FactorySPARQL {

	public CategorySPARQL createCategory(String name, String variable, SPARQLMariusQLWalker walker) {
		CategorySPARQL res = null;
		if (name.startsWith(MariusQLConstants.PREFIX_NAMESPACE_RDF)) {
			res = new EntitySPARQL(name, variable, walker);
		} else {
			res = new ClassSPARQL(name, variable, walker);
		}
		return res;
	}

	public DescriptionSPARQL createDescription(String name, String variable, SPARQLMariusQLWalker walker) {
		DescriptionSPARQL res = null;
		if (name.startsWith(MariusQLConstants.PREFIX_NAMESPACE_RDF)
				|| name.startsWith(MariusQLConstants.PREFIX_METAMODEL_ELEMENT)) {
			res = new AttributeSPARQL(name, variable, walker);
		} else {
			res = new PropertySPARQL(name, variable, walker);
		}
		return res;
	}
}
