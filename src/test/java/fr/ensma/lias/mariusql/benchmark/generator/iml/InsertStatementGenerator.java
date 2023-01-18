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
package fr.ensma.lias.mariusql.benchmark.generator.iml;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import fr.ensma.lias.mariusql.benchmark.generator.ColumnGenerator;
import fr.ensma.lias.mariusql.benchmark.generator.Generator;
import fr.ensma.lias.mariusql.benchmark.generator.ValueGenerator;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.model.MClass;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * @author Florian MHUN
 */
public class InsertStatementGenerator implements Generator {

	private MariusQLSession session;

	private String modelClassIdentifier;

	private MClass modelClass;

	private ColumnGenerator columnGenerator;

	private ValueGenerator valueGenerator;

	private PrintWriter writer;

	public InsertStatementGenerator(MariusQLSession session) {
		this.session = session;
		this.columnGenerator = new ColumnGenerator(session);
		this.valueGenerator = new ValueGenerator();
	}

	public void setCurrentModelClass(String modelClassIdentifier) {
		this.modelClassIdentifier = modelClassIdentifier;
		this.modelClass = FactoryCore.createExistingMClass(session, modelClassIdentifier);
	}

	@Override
	public String generate() {
		if (modelClassIdentifier == null) {
			throw new MariusQLException("Must set a model class before generate statement");
		}

		List<String> columns = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		List<MProperty> usedProperties = modelClass.getUsedProperties();

		if (usedProperties.isEmpty()) {
			throw new MariusQLException("model class " + modelClassIdentifier + " does not contains any used property");
		}

		for (MProperty property : usedProperties) {
			if (property.getRange().isCollectionAssociationType() || property.getRange().isAssociationType()) {
				continue;
			}

			columnGenerator.setCurrentProperty(property);
			valueGenerator.setCurrentDatatype(property.getRange());

			columns.add(columnGenerator.generate());
			values.add(valueGenerator.generate());
		}

		String mariusQL = "INSERT INTO " + modelClassIdentifier + " (" + StringHelper.join(", ", columns.iterator())
				+ ")" + " VALUES (" + StringHelper.join(", ", values.iterator()) + ")";

		if (writer != null) {
			writer.println(mariusQL + ";");
		}

		return mariusQL;
	}

	public void setOuputWriter(PrintWriter writer) {
		this.writer = writer;
	}

}
