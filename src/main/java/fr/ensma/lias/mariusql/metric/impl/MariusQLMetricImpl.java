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
package fr.ensma.lias.mariusql.metric.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ensma.lias.mariusql.core.Category;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.model.MGenericClass;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSet;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;
import fr.ensma.lias.mariusql.metric.MariusQLMetric;

/**
 * @author Mickael BARON
 */
public class MariusQLMetricImpl implements MariusQLMetric {

	protected MariusQLSession refSession;

	protected Map<String, Integer> classRowCount;

	protected Map<String, Integer> classPolymorphRowCount;

	protected Integer classCountDefaultNamespace;

	protected Integer allClassRowCount;

	public MariusQLMetricImpl(MariusQLSession pSession) {
		this.refSession = pSession;
		this.classRowCount = new HashMap<String, Integer>();
		this.classPolymorphRowCount = new HashMap<String, Integer>();
		this.classCountDefaultNamespace = null;
		this.allClassRowCount = null;
	}

	@Override
	public int getClassRowCount(String classIdentifier, boolean force) {
		boolean needNewExecution = true;

		if (!force) {
			needNewExecution = !classRowCount.containsKey(classIdentifier);
		} else {
			needNewExecution = true;
		}

		if (needNewExecution) {
			final MGenericClass createExistingMGenericClass = FactoryCore.createExistingMGenericClass(getSession(),
					classIdentifier);
			final int result = this.getSession().getMariusQLDQL().getCountRow(createExistingMGenericClass.toSQL(),
					true);

			classRowCount.put(classIdentifier, result);

			return result;
		} else {
			return classRowCount.get(classIdentifier);
		}
	}

	@Override
	public int getClassPolymorphRowCount(String classIdentifier, boolean force) {
		boolean needNewExecution = true;

		if (!force) {
			needNewExecution = !classPolymorphRowCount.containsKey(classIdentifier);
		} else {
			needNewExecution = true;
		}

		if (needNewExecution) {
			final MGenericClass createExistingMGenericClass = FactoryCore.createExistingMGenericClass(getSession(),
					classIdentifier);
			final List<Category> allSubcategories = createExistingMGenericClass.getHierarchyFromThisCategory();

			int polymorphRowCount = 0;
			for (Category category : allSubcategories) {
				polymorphRowCount += this.getSession().getMariusQLDQL().getCountRow(category.toSQL(), true);
			}

			classPolymorphRowCount.put(classIdentifier, polymorphRowCount);

			return polymorphRowCount;
		} else {
			return classPolymorphRowCount.get(classIdentifier);
		}
	}

	@Override
	public int getClassCount(boolean force) {
		boolean needNewExecution = true;

		if (!force) {
			needNewExecution = classCountDefaultNamespace == null;
		} else {
			needNewExecution = true;
		}

		if (needNewExecution) {
			final MariusQLStatement createMariusQLStatement = this.getSession().createMariusQLStatement();
			final MariusQLResultSet executeQuery = createMariusQLStatement.executeQuery("SELECT count(*) from #Class");

			int count = 0;
			try {
				if (executeQuery.next()) {
					count = executeQuery.getInt(1);
				}
			} catch (SQLException e) {
				throw new MariusQLException(e.getMessage());
			}

			createMariusQLStatement.close();
			return count;
		} else {
			return classCountDefaultNamespace;
		}
	}

	protected MariusQLSession getSession() {
		return this.refSession;
	}

	@Override
	public int getAllClassRowCount(boolean force) {
		boolean needNewExecution = true;

		if (!force) {
			needNewExecution = this.allClassRowCount == null;
		} else {
			needNewExecution = true;
		}

		if (needNewExecution) {
			this.allClassRowCount = 0;
			String identifierParameter = null;

			if (this.getSession().isNoReferenceLanguage()) {
				identifierParameter = "code";
			} else {
				identifierParameter = "name[" + this.getSession().getReferenceLanguage() + "]";
			}

			final MariusQLStatement createMariusQLStatement = this.getSession().createMariusQLStatement();
			final MariusQLResultSet executeQuery = createMariusQLStatement
					.executeQuery("SELECT " + identifierParameter + ", isextension from #Class");

			try {
				while (executeQuery.next()) {
					if (executeQuery.getBoolean(2)) {
						allClassRowCount += this.getClassRowCount(executeQuery.getString(1), force);
					}
				}
			} catch (SQLException e) {
				throw new MariusQLException(e.getMessage());
			}

			createMariusQLStatement.close();

			return this.allClassRowCount;
		} else {
			return this.allClassRowCount;
		}
	}
}
