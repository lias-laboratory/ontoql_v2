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
package fr.ensma.lias.mariusql.jdbc.impl;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ensma.lias.mariusql.engine.tree.IdentNode;
import fr.ensma.lias.mariusql.engine.tree.dql.SelectExpression;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSetMetaData;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSetRelaxationMetaData;

/**
 * @author Mickael BARON
 */
public class MariusQLResultSetMetaDataImpl implements MariusQLResultSetMetaData {

	/**
	 * ResulSetMetaData delegate.
	 */
	protected ResultSetMetaData resultMetaData;

	/**
	 * 
	 */
	private Map<String, String> aliasColumns;

	/**
	 * List of used properties in select
	 */
	private List<SelectExpression> propertiesInSelect;

	/**
	 * 
	 */
	private MariusQLResultSetRelaxationMetaData relaxationMetaData;

	/**
	 * 
	 */
	private boolean isQueryFromCache;

	public MariusQLResultSetMetaDataImpl(ResultSetMetaData pResultMetaData, List<SelectExpression> pPropertiesInSelect,
			boolean pIsQueryFromCache) {
		this.resultMetaData = pResultMetaData;
		this.propertiesInSelect = pPropertiesInSelect;
		this.isQueryFromCache = pIsQueryFromCache;

		aliasColumns = new HashMap<String, String>();
		for (SelectExpression selectExpression : propertiesInSelect) {
			if (selectExpression instanceof IdentNode) {
				IdentNode currentNode = (IdentNode) selectExpression;
				aliasColumns.put(currentNode.getLabel(), currentNode.getText());
			}
		}
	}

	public String getNativeColumnFromAlias(String alias) {
		String string = this.aliasColumns.get(alias);

		return (string == null ? alias : string);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return resultMetaData.unwrap(iface);
	}

	@Override
	public int getColumnCount() throws SQLException {
		return resultMetaData.getColumnCount();
	}

	@Override
	public boolean isAutoIncrement(int column) throws SQLException {
		return resultMetaData.isAutoIncrement(column);
	}

	@Override
	public boolean isCaseSensitive(int column) throws SQLException {
		return resultMetaData.isCaseSensitive(column);
	}

	@Override
	public boolean isSearchable(int column) throws SQLException {
		return resultMetaData.isSearchable(column);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return resultMetaData.isWrapperFor(iface);
	}

	@Override
	public boolean isCurrency(int column) throws SQLException {
		return resultMetaData.isCurrency(column);
	}

	@Override
	public int isNullable(int column) throws SQLException {
		return resultMetaData.isNullable(column);
	}

	@Override
	public boolean isSigned(int column) throws SQLException {
		return resultMetaData.isSigned(column);
	}

	@Override
	public int getColumnDisplaySize(int column) throws SQLException {
		return resultMetaData.getColumnDisplaySize(column);
	}

	@Override
	public String getColumnLabel(int column) throws SQLException {
		return resultMetaData.getColumnLabel(column);
	}

	@Override
	public String getColumnName(int column) throws SQLException {
		SelectExpression exprInColumn = ((SelectExpression) propertiesInSelect.get(column - 1));
		String alias = exprInColumn.getAlias();
		if (alias == null) {
			return exprInColumn.getLabel();
		} else {
			return alias;
		}
	}

	@Override
	public String getSchemaName(int column) throws SQLException {
		return resultMetaData.getSchemaName(column);
	}

	@Override
	public int getPrecision(int column) throws SQLException {
		return resultMetaData.getPrecision(column);
	}

	@Override
	public int getScale(int column) throws SQLException {
		return resultMetaData.getScale(column);
	}

	@Override
	public String getTableName(int column) throws SQLException {
		return resultMetaData.getTableName(column);
	}

	@Override
	public String getCatalogName(int column) throws SQLException {
		return resultMetaData.getCatalogName(column);
	}

	@Override
	public int getColumnType(int column) throws SQLException {
		return resultMetaData.getColumnType(column);
	}

	@Override
	public String getColumnTypeName(int column) throws SQLException {
		return resultMetaData.getColumnTypeName(column);
	}

	@Override
	public boolean isReadOnly(int column) throws SQLException {
		return resultMetaData.isReadOnly(column);
	}

	@Override
	public boolean isWritable(int column) throws SQLException {
		return resultMetaData.isWritable(column);
	}

	@Override
	public boolean isDefinitelyWritable(int column) throws SQLException {
		return resultMetaData.isDefinitelyWritable(column);
	}

	@Override
	public String getColumnClassName(int column) throws SQLException {
		return resultMetaData.getColumnClassName(column);
	}

	@Override
	public MariusQLResultSetRelaxationMetaData getRelaxationMetaData() {
		return this.relaxationMetaData;
	}

	public void setRelaxationMetaData(MariusQLResultSetRelaxationMetaData p) {
		this.relaxationMetaData = p;
	}

	@Override
	public boolean isQueryFromCache() {
		return this.isQueryFromCache;
	}
}
