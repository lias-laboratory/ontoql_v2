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
package fr.ensma.lias.mariusql.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.util.StringHelper;


/**
 * @author Mickael BARON
 * @author Florian MHUN
 * @author Ghada TRIKI
 */
public enum DatatypeEnum {
    
    DATATYPEINT(MariusQLConstants.INT_NAME, Arrays.asList(MariusQLConstants.OP_EG, MariusQLConstants.OP_SUP, MariusQLConstants.OP_INF, MariusQLConstants.OP_SUPEG, MariusQLConstants.OP_INFEG), Arrays.asList(MariusQLConstants.OP_PLUS, MariusQLConstants.OP_MINUS, MariusQLConstants.OP_DIV, MariusQLConstants.OP_STAR, MariusQLConstants.OP_EG, MariusQLConstants.OP_CONCAT)),
    DATATYPESTRING(MariusQLConstants.STRING_NAME, Arrays.asList(MariusQLConstants.OP_LIKE, MariusQLConstants.OP_NOT_LIKE, MariusQLConstants.OP_EG, MariusQLConstants.OP_SUP, MariusQLConstants.OP_INF, MariusQLConstants.OP_SUPEG, MariusQLConstants.OP_INFEG), Arrays.asList(MariusQLConstants.OP_CONCAT)),
    DATATYPEBOOLEAN(MariusQLConstants.BOOLEAN_NAME, new ArrayList<String>(), Arrays.asList(MariusQLConstants.OP_EG)),
    DATATYPEREAL(MariusQLConstants.REAL_NAME, Arrays.asList(MariusQLConstants.OP_EG, MariusQLConstants.OP_SUP, MariusQLConstants.OP_INF, MariusQLConstants.OP_SUPEG, MariusQLConstants.OP_INFEG), Arrays.asList(MariusQLConstants.OP_PLUS, MariusQLConstants.OP_MINUS, MariusQLConstants.OP_DIV, MariusQLConstants.OP_STAR, MariusQLConstants.OP_EG, MariusQLConstants.OP_CONCAT)),
    DATATYPECOLLECTION(MariusQLConstants.COLLECTION_NAME, Arrays.asList(MariusQLConstants.OP_EG), Arrays.asList(MariusQLConstants.OP_CONCAT)),
    DATATYPEORACLECOLLECTION(MariusQLConstants.ORACLE_COLLECTION_NAME, Arrays.asList(MariusQLConstants.OP_EG), Arrays.asList(MariusQLConstants.OP_CONCAT)),
    DATATYPEREFERENCE(MariusQLConstants.ASSOCIATION_NAME, Arrays.asList(MariusQLConstants.OP_EG), new ArrayList<String>()),
    DATATYPEMULTISTRING(MariusQLConstants.MULTISTRING_NAME, Arrays.asList(MariusQLConstants.OP_LIKE, MariusQLConstants.OP_NOT_LIKE, MariusQLConstants.OP_EG, MariusQLConstants.OP_SUP, MariusQLConstants.OP_INF, MariusQLConstants.OP_SUPEG, MariusQLConstants.OP_INFEG), Arrays.asList(MariusQLConstants.OP_CONCAT)),
    DATATYPEURI(MariusQLConstants.URI_NAME, Arrays.asList(MariusQLConstants.OP_LIKE, MariusQLConstants.OP_NOT_LIKE, MariusQLConstants.OP_EG, MariusQLConstants.OP_SUP, MariusQLConstants.OP_INF, MariusQLConstants.OP_SUPEG, MariusQLConstants.OP_INFEG), Arrays.asList(MariusQLConstants.OP_CONCAT)),
    DATATYPEENUMERATE(MariusQLConstants.ENUMERATE_NAME, Arrays.asList(MariusQLConstants.OP_IN, MariusQLConstants.OP_LIKE, MariusQLConstants.OP_EG, MariusQLConstants.OP_SUP, MariusQLConstants.OP_INF, MariusQLConstants.OP_SUPEG, MariusQLConstants.OP_INFEG), Arrays.asList(MariusQLConstants.OP_CONCAT)),
    DATATYPECOUNT(MariusQLConstants.COUNTTYPE_NAME, Arrays.asList(MariusQLConstants.OP_EG, MariusQLConstants.OP_SUP, MariusQLConstants.OP_INF, MariusQLConstants.OP_SUPEG, MariusQLConstants.OP_INFEG), Arrays.asList(MariusQLConstants.OP_PLUS, MariusQLConstants.OP_MINUS, MariusQLConstants.OP_DIV, MariusQLConstants.OP_STAR, MariusQLConstants.OP_EG, MariusQLConstants.OP_CONCAT));

	private String name;

	private List<String> booleanOperators;

	private List<String> arithmeticOperators;

	DatatypeEnum(String pName, List<String> pBooleanOperators, List<String> pArithmeticOperators) {
		this.name = pName;
		this.booleanOperators = pBooleanOperators;
		this.arithmeticOperators = pArithmeticOperators;
	}

	public String getName() {
		return this.name;
	}

	public List<String> getBooleanOperators() {
		return booleanOperators;
	}

	public List<String> getArithmeticOperators() {
		return arithmeticOperators;
	}

	public String toSQL() {
		return StringHelper.addSimpleQuotedString(getName()).toLowerCase();
	}
}
