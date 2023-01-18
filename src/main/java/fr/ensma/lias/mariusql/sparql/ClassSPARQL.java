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

import fr.ensma.lias.mariusql.engine.SPARQLMariusQLWalker;

/**
 * A SPARQL class
 * 
 * @author Stephane JEAN
 * @author Adel GHAMNIA
 */
public class ClassSPARQL extends AbstractCategorySPARQL {

	public boolean isClass() {
		return true;
	}

	public ClassSPARQL(String text, String variable, SPARQLMariusQLWalker walker) {
		this.walker = walker;
		this.variable = variable;
		int indexOfNamespace = text.indexOf(':');
		if (indexOfNamespace != -1) {
			namespace = text.substring(0, indexOfNamespace);
			name = text.substring(indexOfNamespace + 1, text.length());
		} else {
			name = text;
		}
		// oid = new PropertySPARQL(namespace + ":oid", variable, walker);
		rid = new PropertySPARQL(namespace + ":rid", variable, walker);
		rid.setScope(this);
		walker.registerVariable(variable, rid);
		alias = walker.getAliasGenerator().createName(name);
	}
}
