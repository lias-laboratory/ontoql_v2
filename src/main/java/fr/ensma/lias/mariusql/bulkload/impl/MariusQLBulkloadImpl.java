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
package fr.ensma.lias.mariusql.bulkload.impl;

import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.ensma.lias.mariusql.bulkload.BulkloadReport;
import fr.ensma.lias.mariusql.bulkload.MariusQLBulkload;
import fr.ensma.lias.mariusql.core.DatatypeEnumerate;
import fr.ensma.lias.mariusql.core.DatatypeMultiString;
import fr.ensma.lias.mariusql.core.DatatypeString;
import fr.ensma.lias.mariusql.core.Description;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.model.MClass;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotYetImplementedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * @author Mickael Baron
 */
public class MariusQLBulkloadImpl implements MariusQLBulkload {

	protected MariusQLSession refSession;

	public MariusQLBulkloadImpl(MariusQLSession pSession) {
		this.refSession = pSession;
	}

	@Override
	public BulkloadReport loadClassInstancesByCollection(String classIdentifier, List<String> propertiesOrder,
			List<List<String>> values) {
		MClass klass = FactoryCore.createExistingMClass(this.getSession(), classIdentifier);

		StringBuilder query = new StringBuilder();
		query.append("insert into ");
		query.append(klass.toSQL());
		query.append(" (");

		List<MProperty> properties = new ArrayList<MProperty>();
		for (String currentProperty : propertiesOrder) {
			if (klass.isUsedPropertyExists(currentProperty)) {
				final Description definedDescription = klass.getDefinedDescription(currentProperty);

				if (definedDescription == null) {
					throw new MariusQLException(currentProperty + " is not an defined property.");
				} else {
					properties.add((MProperty) definedDescription);
				}
			} else {
				throw new MariusQLException(currentProperty + " is not an used property.");
			}
		}

		List<Boolean> withQuote = new ArrayList<Boolean>();
		for (MProperty current : properties) {
			query.append(current.toSQL() + ",");

			if (current.getRange().isSimpleType()) {
				withQuote.add(current.getRange() instanceof DatatypeString
						|| current.getRange() instanceof DatatypeMultiString
						|| current.getRange() instanceof DatatypeEnumerate);
			} else if (current.getRange().isAssociationType()) {
				throw new NotYetImplementedException();
			} else if (current.getRange().isCollectionAssociationType()) {
				throw new NotYetImplementedException();
			} else if (current.getRange().isCollectionType()) {
				withQuote.add(false);
			} else {
				throw new NotYetImplementedException();
			}
		}
		query.delete(query.length() - 1, query.length());

		StringBuilder valuesClause = new StringBuilder();
		try {
			Statement statement = this.getSession().createSQLStatement();
			long startExecution = System.currentTimeMillis();
			int insertedValue = 0;
			for (int row = 0; row < values.size(); row++) {
				if (withQuote.size() != values.get(row).size()) {
					throw new NotYetImplementedException();
				}

				for (int col = 0; col < values.get(row).size(); col++) {
					String currentValue = values.get(row).get(col);

					if (withQuote.get(col)) {
						valuesClause.append("'" + currentValue + "',");
					} else {
						valuesClause.append(currentValue + ",");
					}
				}
				valuesClause.delete(valuesClause.length() - 1, valuesClause.length());

				final String queryValue = query.toString() + ") values (" + valuesClause.toString() + ")";
				insertedValue += statement.executeUpdate(queryValue);
				valuesClause = new StringBuilder();
			}
			BulkloadReport newBulkReport = new BulkloadReport(insertedValue,
					System.currentTimeMillis() - startExecution);

			return newBulkReport;
		} catch (SQLException e) {
			throw new NotYetImplementedException();
		}
	}

	@Override
	public BulkloadReport loadClassInstancesByInputStream(String classIdentifier, List<String> propertiesOrder,
			InputStream values) {
		throw new NotYetImplementedException();
	}

	/**
	 * Retrieve the current session.
	 * 
	 * @return
	 */
	public MariusQLSession getSession() {
		return this.refSession;
	}
}
