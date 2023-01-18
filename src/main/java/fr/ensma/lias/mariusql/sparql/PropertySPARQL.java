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
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * @author Stéphane JEAN
 * @author Adel GHAMNIA
 */
public class PropertySPARQL extends AbstractDescriptionSPARQL {

	boolean isMultivalued = false;

	boolean isMultivaluedInitialized = false;

	public boolean isAttribute() {
		return false;
	}

	public boolean isProperty() {
		return true;
	}

	public PropertySPARQL(String text, String var, SPARQLMariusQLWalker walker) {
		this.walker = walker;
		int indexOfNamespace = text.indexOf(':');
		if (indexOfNamespace != -1) {
			namespace = text.substring(0, indexOfNamespace);
			name = text.substring(indexOfNamespace + 1, text.length());
		} else {
			name = text;
		}
		variable = var;
	}

	@Override
	public boolean isMultivalued() {
		if (!isMultivaluedInitialized) {
			loadMultivalued();
		}
		return isMultivalued;
	}

	public void loadMultivalued() {
		MariusQLSession session = walker.getSession();
		isMultivalued = session.getMariusQLDQL().isMPropertyRangeOfCollectionDataType(name);
		isMultivaluedInitialized = true;
	}

	@Override
	public boolean isOptional() {
		return (!name.equals("rid") && (!name.equals("URI") && (!name.equals("subdivision")) && !isMultivalued()));
	}
}
