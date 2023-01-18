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
package fr.ensma.lias.mariusql.engine.tree.dml;

import java.util.ArrayList;
import java.util.List;

import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.core.Description;
import fr.ensma.lias.mariusql.engine.MariusQLSQLWalkerNode;
import fr.ensma.lias.mariusql.engine.tree.DisplayableNode;

/**
 * Represents a set-clause of an OntoQL UPDATE statement.
 *
 * @author Valentin CASSAIR
 */
public class SetClause extends MariusQLSQLWalkerNode implements DisplayableNode {

	private static final long serialVersionUID = -8401458358903600093L;

	/**
	 * 
	 */
	private List<Description> descriptions;

	/**
	 * List of the values types.
	 */
	private List<DatatypeEnum> valuesTypes;

	/**
	 * The list of values.
	 */
	private List<String> values = new ArrayList<String>();

	public SetClause() {
		this.descriptions = new ArrayList<Description>();
		this.valuesTypes = new ArrayList<DatatypeEnum>();
		this.values = new ArrayList<String>();
	}

	/**
	 * Add a value for an ontologic query. No quoted string is required.
	 * 
	 * @param value a value
	 */
	public void addValue(String value) {
		values.add(value);
	}

	/**
	 * Add a type of value
	 * 
	 * @param type a type of value
	 */
	public void addValueType(DatatypeEnum type) {
		valuesTypes.add(type);
	}

	@Override
	public String getDisplayText() {
		StringBuilder buf = new StringBuilder();
		buf.append("SetClause={").append(descriptions).append("/").append(values).append("}");
		return buf.toString();
	}

	/**
	 * Get the list of values.
	 * 
	 * @return the list of values
	 */
	public List<String> getValues() {
		return values;
	}

	/**
	 * Get the list of datatype of this values.
	 * 
	 * @return the list of datatype of this values
	 */
	public List<DatatypeEnum> getValuesTypes() {
		return valuesTypes;
	}

	/**
	 * @return
	 */
	public List<Description> getDescriptions() {
		return this.descriptions;
	}

	/**
	 * 
	 * @param valuesTypes
	 */
	public void setValuesTypes(List<DatatypeEnum> valuesTypes) {
		this.valuesTypes = valuesTypes;
	}

	/**
	 * 
	 * @param values
	 */
	public void setValues(List<String> values) {
		this.values = values;
	}

	/**
	 * @param identifier
	 */
	public void addDescription(Description description) {
		descriptions.add(description);
	}
}
