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
 * @author Stephane JEAN
 * @author Mickael BARON
 * @author Raoul TIAM
 * @author Adel GHAMNIA
 */
public class AttributeSPARQL extends AbstractDescriptionSPARQL {

	public boolean isAttribute() {
		return true;
	}

	public boolean isProperty() {
		return false;
	}

	public AttributeSPARQL(String text, String var, SPARQLMariusQLWalker walker) {
		this.walker = walker;
		name = text;
		if (!text.equals("#rid")) {
			int indexOfNamespace = text.indexOf(':');
			name = MariusQLConstants.PREFIX_METAMODEL_ELEMENT
					+ walker.getMappingOfEntityOrAttribute(text.substring(indexOfNamespace + 1, text.length()));
		}
		variable = var;
	}

	@Override
	public boolean isMultivalued() {
		return name.equals("#superclasses");
	}

	@Override
	public boolean isOptional() {
		return (name.equals("#definition"));
	}
}
