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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import fr.ensma.lias.mariusql.exception.NotYetImplementedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSet;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSetMetaData;

/**
 * @author Mickael BARON
 */
public class MariusQLResultSetRelaxationImpl implements MariusQLResultSet {

	protected List<MariusQLResultSet> resultSetDelegates;

	protected Integer currentResultSet;

	public MariusQLResultSetRelaxationImpl() {
		this.resultSetDelegates = new ArrayList<MariusQLResultSet>();
		this.currentResultSet = 0;
	}

	public void addMariusQLResultSet(MariusQLResultSet pResultSetDelegate) {
		resultSetDelegates.add(pResultSetDelegate);

		Collections.sort(resultSetDelegates, new Comparator<MariusQLResultSet>() {
			@Override
			public int compare(MariusQLResultSet o1, MariusQLResultSet o2) {
				return Double.compare(o2.getMariusQLMetaData().getRelaxationMetaData().getSimilarityBetweenQuery(),
						o1.getMariusQLMetaData().getRelaxationMetaData().getSimilarityBetweenQuery());
			}
		});
	}

	protected MariusQLResultSet getResultSet() {
		return this.resultSetDelegates.get(currentResultSet);
	}

	protected MariusQLResultSet getResultSet(int p) {
		return this.resultSetDelegates.get(p);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return this.getResultSet().unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return this.getResultSet().isWrapperFor(iface);
	}

	@Override
	public boolean next() throws SQLException {
		boolean res = resultSetDelegates.get(currentResultSet).next();

		if (res) {
			return true;
		} else {
			if (this.currentResultSet + 1 < this.resultSetDelegates.size()) {
				this.currentResultSet++;

				return this.next();
			} else {
				return res;
			}
		}
	}

	@Override
	public void close() throws SQLException {
		this.getResultSet().close();
	}

	@Override
	public boolean wasNull() throws SQLException {
		return this.getResultSet().wasNull();
	}

	@Override
	public String getString(int columnIndex) throws SQLException {
		return this.getResultSet().getString(columnIndex);
	}

	@Override
	public boolean getBoolean(int columnIndex) throws SQLException {
		return this.getResultSet().getBoolean(columnIndex);
	}

	@Override
	public byte getByte(int columnIndex) throws SQLException {
		return this.getResultSet().getByte(columnIndex);
	}

	@Override
	public short getShort(int columnIndex) throws SQLException {
		return this.getResultSet().getShort(columnIndex);
	}

	@Override
	public int getInt(int columnIndex) throws SQLException {
		return this.getResultSet().getInt(columnIndex);
	}

	@Override
	public long getLong(int columnIndex) throws SQLException {
		return this.getResultSet().getLong(columnIndex);
	}

	@Override
	public float getFloat(int columnIndex) throws SQLException {
		return this.getResultSet().getFloat(columnIndex);
	}

	@Override
	public double getDouble(int columnIndex) throws SQLException {
		return this.getResultSet().getDouble(columnIndex);
	}

	@Deprecated
	@Override
	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		return this.getResultSet().getBigDecimal(columnIndex, scale);
	}

	@Override
	public byte[] getBytes(int columnIndex) throws SQLException {
		return this.getResultSet().getBytes(columnIndex);
	}

	@Override
	public Date getDate(int columnIndex) throws SQLException {
		return this.getResultSet().getDate(columnIndex);
	}

	@Override
	public Time getTime(int columnIndex) throws SQLException {
		return this.getResultSet().getTime(columnIndex);
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return this.getResultSet().getTimestamp(columnIndex);
	}

	@Override
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return this.getResultSet().getAsciiStream(columnIndex);
	}

	@Override
	@Deprecated
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return this.getResultSet().getUnicodeStream(columnIndex);
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return this.getResultSet().getBinaryStream(columnIndex);
	}

	@Override
	public String getString(String columnLabel) throws SQLException {
		return this.getResultSet().getString(columnLabel);
	}

	@Override
	public boolean getBoolean(String columnLabel) throws SQLException {
		return this.getResultSet().getBoolean(columnLabel);
	}

	@Override
	public byte getByte(String columnLabel) throws SQLException {
		return this.getResultSet().getByte(columnLabel);
	}

	@Override
	public short getShort(String columnLabel) throws SQLException {
		return this.getResultSet().getShort(columnLabel);
	}

	@Override
	public int getInt(String columnLabel) throws SQLException {
		return this.getResultSet().getInt(
				((MariusQLResultSetMetaDataImpl) this.getMariusQLMetaData()).getNativeColumnFromAlias(columnLabel));
	}

	@Override
	public long getLong(String columnLabel) throws SQLException {
		return this.getResultSet().getLong(columnLabel);
	}

	@Override
	public float getFloat(String columnLabel) throws SQLException {
		return this.getResultSet().getFloat(columnLabel);
	}

	@Override
	public double getDouble(String columnLabel) throws SQLException {
		return this.getResultSet().getDouble(columnLabel);
	}

	@Override
	@Deprecated
	public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
		return this.getResultSet().getBigDecimal(columnLabel, scale);
	}

	@Override
	public byte[] getBytes(String columnLabel) throws SQLException {
		return this.getResultSet().getBytes(columnLabel);
	}

	@Override
	public Date getDate(String columnLabel) throws SQLException {
		return this.getResultSet().getDate(columnLabel);
	}

	@Override
	public Time getTime(String columnLabel) throws SQLException {
		return this.getResultSet().getTime(columnLabel);
	}

	@Override
	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		return this.getResultSet().getTimestamp(columnLabel);
	}

	@Override
	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		return this.getResultSet().getAsciiStream(columnLabel);
	}

	@Override
	@Deprecated
	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		return this.getResultSet().getUnicodeStream(columnLabel);
	}

	@Override
	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		return this.getResultSet().getBinaryStream(columnLabel);
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return this.getResultSet().getWarnings();
	}

	@Override
	public void clearWarnings() throws SQLException {
		this.getResultSet().clearWarnings();
	}

	@Override
	public String getCursorName() throws SQLException {
		return this.getResultSet().getCursorName();
	}

	@Override
	public Object getObject(int columnIndex) throws SQLException {
		return this.getResultSet().getObject(columnIndex);
	}

	@Override
	public Object getObject(String columnLabel) throws SQLException {
		return this.getResultSet().getObject(columnLabel);
	}

	@Override
	public int findColumn(String columnLabel) throws SQLException {
		return this.getResultSet().findColumn(columnLabel);
	}

	@Override
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return this.getResultSet().getCharacterStream(columnIndex);
	}

	@Override
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		return this.getResultSet().getCharacterStream(columnLabel);
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return this.getResultSet().getBigDecimal(columnIndex);
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		return this.getResultSet().getBigDecimal(columnLabel);
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		return this.getResultSet().isBeforeFirst();
	}

	@Override
	public boolean isAfterLast() throws SQLException {
		return this.getResultSet().isAfterLast();
	}

	@Override
	public boolean isFirst() throws SQLException {
		return this.getResultSet().isFirst();
	}

	@Override
	public boolean isLast() throws SQLException {
		return this.getResultSet().isLast();
	}

	@Override
	public void beforeFirst() throws SQLException {
		this.getResultSet().beforeFirst();
	}

	@Override
	public void afterLast() throws SQLException {
		this.getResultSet().afterLast();
	}

	@Override
	public boolean first() throws SQLException {
		return this.getResultSet().first();
	}

	@Override
	public boolean last() throws SQLException {
		return this.getResultSet().last();
	}

	@Override
	public int getRow() throws SQLException {
		return this.getResultSet().getRow();
	}

	@Override
	public boolean absolute(int row) throws SQLException {
		return this.getResultSet().absolute(row);
	}

	@Override
	public boolean relative(int rows) throws SQLException {
		return this.getResultSet().relative(rows);
	}

	@Override
	public boolean previous() throws SQLException {
		return this.getResultSet().previous();
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		this.getResultSet().setFetchDirection(direction);
	}

	@Override
	public int getFetchDirection() throws SQLException {
		return this.getResultSet().getFetchDirection();
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		this.getResultSet().setFetchSize(rows);
	}

	@Override
	public int getFetchSize() throws SQLException {
		return this.getResultSet().getFetchSize();
	}

	@Override
	public int getType() throws SQLException {
		return this.getResultSet().getType();
	}

	@Override
	public int getConcurrency() throws SQLException {
		return this.getResultSet().getConcurrency();
	}

	@Override
	public boolean rowUpdated() throws SQLException {
		return this.getResultSet().rowUpdated();
	}

	@Override
	public boolean rowInserted() throws SQLException {
		return this.getResultSet().rowInserted();
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		return this.getResultSet().rowDeleted();
	}

	@Override
	public void updateNull(int columnIndex) throws SQLException {
		this.getResultSet().updateNull(columnIndex);
	}

	@Override
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		this.getResultSet().updateBoolean(columnIndex, x);
	}

	@Override
	public void updateByte(int columnIndex, byte x) throws SQLException {
		this.getResultSet().updateByte(columnIndex, x);
	}

	@Override
	public void updateShort(int columnIndex, short x) throws SQLException {
		this.getResultSet().updateShort(columnIndex, x);
	}

	@Override
	public void updateInt(int columnIndex, int x) throws SQLException {
		this.getResultSet().updateInt(columnIndex, x);
	}

	@Override
	public void updateLong(int columnIndex, long x) throws SQLException {
		this.getResultSet().updateLong(columnIndex, x);
	}

	@Override
	public void updateFloat(int columnIndex, float x) throws SQLException {
		this.getResultSet().updateFloat(columnIndex, x);
	}

	@Override
	public void updateDouble(int columnIndex, double x) throws SQLException {
		this.getResultSet().updateDouble(columnIndex, x);
	}

	@Override
	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
		this.getResultSet().updateBigDecimal(columnIndex, x);
	}

	@Override
	public void updateString(int columnIndex, String x) throws SQLException {
		this.getResultSet().updateString(columnIndex, x);
	}

	@Override
	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		this.getResultSet().updateBytes(columnIndex, x);
	}

	@Override
	public void updateDate(int columnIndex, Date x) throws SQLException {
		this.getResultSet().updateDate(columnIndex, x);
	}

	@Override
	public void updateTime(int columnIndex, Time x) throws SQLException {
		this.getResultSet().updateTime(columnIndex, x);
	}

	@Override
	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
		this.getResultSet().updateTimestamp(columnIndex, x);
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
		this.getResultSet().updateAsciiStream(columnIndex, x, length);
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
		this.getResultSet().updateBinaryStream(columnIndex, x, length);
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
		this.getResultSet().updateCharacterStream(columnIndex, x, length);
	}

	@Override
	public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
		this.getResultSet().updateObject(columnIndex, x, scaleOrLength);
	}

	@Override
	public void updateObject(int columnIndex, Object x) throws SQLException {
		this.getResultSet().updateObject(columnIndex, x);
	}

	@Override
	public void updateNull(String columnLabel) throws SQLException {
		this.getResultSet().updateNull(columnLabel);
	}

	@Override
	public void updateBoolean(String columnLabel, boolean x) throws SQLException {
		this.getResultSet().updateBoolean(columnLabel, x);
	}

	@Override
	public void updateByte(String columnLabel, byte x) throws SQLException {
		this.getResultSet().updateByte(columnLabel, x);
	}

	@Override
	public void updateShort(String columnLabel, short x) throws SQLException {
		this.getResultSet().updateShort(columnLabel, x);
	}

	@Override
	public void updateInt(String columnLabel, int x) throws SQLException {
		this.getResultSet().updateInt(columnLabel, x);
	}

	@Override
	public void updateLong(String columnLabel, long x) throws SQLException {
		this.getResultSet().updateLong(columnLabel, x);
	}

	@Override
	public void updateFloat(String columnLabel, float x) throws SQLException {
		this.getResultSet().updateFloat(columnLabel, x);
	}

	@Override
	public void updateDouble(String columnLabel, double x) throws SQLException {
		this.getResultSet().updateDouble(columnLabel, x);
	}

	@Override
	public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
		this.getResultSet().updateBigDecimal(columnLabel, x);
	}

	@Override
	public void updateString(String columnLabel, String x) throws SQLException {
		this.getResultSet().updateString(columnLabel, x);
	}

	@Override
	public void updateBytes(String columnLabel, byte[] x) throws SQLException {
		this.getResultSet().updateBytes(columnLabel, x);
	}

	@Override
	public void updateDate(String columnLabel, Date x) throws SQLException {
		this.getResultSet().updateDate(columnLabel, x);
	}

	@Override
	public void updateTime(String columnLabel, Time x) throws SQLException {
		this.getResultSet().updateTime(columnLabel, x);
	}

	@Override
	public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
		this.getResultSet().updateTimestamp(columnLabel, x);
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
		this.getResultSet().updateAsciiStream(columnLabel, x, length);
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
		this.getResultSet().updateBinaryStream(columnLabel, x, length);
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
		this.getResultSet().updateCharacterStream(columnLabel, reader, length);
	}

	@Override
	public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
		this.getResultSet().updateObject(columnLabel, x, scaleOrLength);
	}

	@Override
	public void updateObject(String columnLabel, Object x) throws SQLException {
		this.getResultSet().updateObject(columnLabel, x);
	}

	@Override
	public void insertRow() throws SQLException {
		this.getResultSet().insertRow();
	}

	@Override
	public void updateRow() throws SQLException {
		this.getResultSet().updateRow();
	}

	@Override
	public void deleteRow() throws SQLException {
		this.getResultSet().deleteRow();
	}

	@Override
	public void refreshRow() throws SQLException {
		this.getResultSet().refreshRow();
	}

	@Override
	public void cancelRowUpdates() throws SQLException {
		this.getResultSet().cancelRowUpdates();
	}

	@Override
	public void moveToInsertRow() throws SQLException {
		this.getResultSet().moveToInsertRow();
	}

	@Override
	public void moveToCurrentRow() throws SQLException {
		this.getResultSet().moveToCurrentRow();
	}

	@Override
	public Statement getStatement() throws SQLException {
		return this.getResultSet().getStatement();
	}

	@Override
	public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
		return this.getResultSet().getObject(columnIndex, map);
	}

	@Override
	public Ref getRef(int columnIndex) throws SQLException {
		return this.getResultSet().getRef(columnIndex);
	}

	@Override
	public Blob getBlob(int columnIndex) throws SQLException {
		return this.getResultSet().getBlob(columnIndex);
	}

	@Override
	public Clob getClob(int columnIndex) throws SQLException {
		return this.getResultSet().getClob(columnIndex);
	}

	@Override
	public Array getArray(int columnIndex) throws SQLException {
		return this.getResultSet().getArray(columnIndex);
	}

	@Override
	public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
		return this.getResultSet().getObject(columnLabel, map);
	}

	@Override
	public Ref getRef(String columnLabel) throws SQLException {
		return this.getResultSet().getRef(columnLabel);
	}

	@Override
	public Blob getBlob(String columnLabel) throws SQLException {
		return this.getResultSet().getBlob(columnLabel);
	}

	@Override
	public Clob getClob(String columnLabel) throws SQLException {
		return this.getResultSet().getClob(columnLabel);
	}

	@Override
	public Array getArray(String columnLabel) throws SQLException {
		return this.getResultSet().getArray(columnLabel);
	}

	@Override
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return this.getResultSet().getDate(columnIndex, cal);
	}

	@Override
	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		return this.getResultSet().getDate(columnLabel, cal);
	}

	@Override
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return this.getResultSet().getTime(columnIndex, cal);
	}

	@Override
	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		return this.getResultSet().getTime(columnLabel, cal);
	}

	@Override
	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		return this.getResultSet().getTimestamp(columnIndex, cal);
	}

	@Override
	public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
		return this.getResultSet().getTimestamp(columnLabel, cal);
	}

	@Override
	public URL getURL(int columnIndex) throws SQLException {
		return this.getResultSet().getURL(columnIndex);
	}

	@Override
	public URL getURL(String columnLabel) throws SQLException {
		return this.getResultSet().getURL(columnLabel);
	}

	@Override
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		this.getResultSet().updateRef(columnIndex, x);
	}

	@Override
	public void updateRef(String columnLabel, Ref x) throws SQLException {
		this.getResultSet().updateRef(columnLabel, x);
	}

	@Override
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		this.getResultSet().updateBlob(columnIndex, x);
	}

	@Override
	public void updateBlob(String columnLabel, Blob x) throws SQLException {
		this.getResultSet().updateBlob(columnLabel, x);
	}

	@Override
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		this.getResultSet().updateClob(columnIndex, x);
	}

	@Override
	public void updateClob(String columnLabel, Clob x) throws SQLException {
		this.getResultSet().updateClob(columnLabel, x);
	}

	@Override
	public void updateArray(int columnIndex, Array x) throws SQLException {
		this.getResultSet().updateArray(columnIndex, x);
	}

	@Override
	public void updateArray(String columnLabel, Array x) throws SQLException {
		this.getResultSet().updateArray(columnLabel, x);
	}

	@Override
	public RowId getRowId(int columnIndex) throws SQLException {
		return this.getResultSet().getRowId(columnIndex);
	}

	@Override
	public RowId getRowId(String columnLabel) throws SQLException {
		return this.getResultSet().getRowId(columnLabel);
	}

	@Override
	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		this.getResultSet().updateRowId(columnIndex, x);
	}

	@Override
	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		this.getResultSet().updateRowId(columnLabel, x);
	}

	@Override
	public int getHoldability() throws SQLException {
		return this.getResultSet().getHoldability();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return this.getResultSet().isClosed();
	}

	@Override
	public void updateNString(int columnIndex, String nString) throws SQLException {
		this.getResultSet().updateNString(columnIndex, nString);
	}

	@Override
	public void updateNString(String columnLabel, String nString) throws SQLException {
		this.getResultSet().updateNString(columnLabel, nString);
	}

	@Override
	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		this.getResultSet().updateNClob(columnIndex, nClob);
	}

	@Override
	public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
		this.getResultSet().updateNClob(columnLabel, nClob);
	}

	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		return this.getResultSet().getNClob(columnIndex);
	}

	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		return this.getResultSet().getNClob(columnLabel);
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		return this.getResultSet().getSQLXML(columnIndex);
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		return this.getResultSet().getSQLXML(columnLabel);
	}

	@Override
	public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
		this.getResultSet().updateSQLXML(columnIndex, xmlObject);
	}

	@Override
	public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
		this.getResultSet().updateSQLXML(columnLabel, xmlObject);
	}

	@Override
	public String getNString(int columnIndex) throws SQLException {
		return this.getResultSet().getNString(columnIndex);
	}

	@Override
	public String getNString(String columnLabel) throws SQLException {
		return this.getResultSet().getNString(columnLabel);
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		return this.getResultSet().getNCharacterStream(columnIndex);
	}

	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		return this.getResultSet().getNCharacterStream(columnLabel);
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		this.getResultSet().updateNCharacterStream(columnIndex, x, length);
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		this.getResultSet().updateNCharacterStream(columnLabel, reader, length);
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
		this.getResultSet().updateAsciiStream(columnIndex, x, length);
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
		this.getResultSet().updateBinaryStream(columnIndex, x, length);
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		this.getResultSet().updateCharacterStream(columnIndex, x, length);
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
		this.getResultSet().updateAsciiStream(columnLabel, x, length);
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
		this.getResultSet().updateBinaryStream(columnLabel, x, length);
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		this.getResultSet().updateCharacterStream(columnLabel, reader, length);
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
		this.getResultSet().updateBlob(columnIndex, inputStream, length);
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
		this.getResultSet().updateBlob(columnLabel, inputStream, length);
	}

	@Override
	public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
		this.getResultSet().updateClob(columnIndex, reader, length);
	}

	@Override
	public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
		this.getResultSet().updateClob(columnLabel, reader, length);
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
		this.getResultSet().updateNClob(columnIndex, reader, length);
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
		this.getResultSet().updateNClob(columnLabel, reader, length);
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
		this.getResultSet().updateNCharacterStream(columnIndex, x);
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
		this.getResultSet().updateNCharacterStream(columnLabel, reader);
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
		this.getResultSet().updateAsciiStream(columnIndex, x);
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
		this.getResultSet().updateBinaryStream(columnIndex, x);
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
		this.getResultSet().updateCharacterStream(columnIndex, x);
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
		this.getResultSet().updateAsciiStream(columnLabel, x);
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
		this.getResultSet().updateBinaryStream(columnLabel, x);
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
		this.getResultSet().updateCharacterStream(columnLabel, reader);
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
		this.getResultSet().updateBlob(columnIndex, inputStream);
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
		this.getResultSet().updateBlob(columnLabel, inputStream);
	}

	@Override
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		this.getResultSet().updateClob(columnIndex, reader);
	}

	@Override
	public void updateClob(String columnLabel, Reader reader) throws SQLException {
		this.getResultSet().updateClob(columnLabel, reader);
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		this.getResultSet().updateNClob(columnIndex, reader);
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader) throws SQLException {
		this.getResultSet().updateNClob(columnLabel, reader);
	}

	@Override
	public MariusQLResultSetMetaData getMariusQLMetaData() {
		return this.getResultSet().getMariusQLMetaData();
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return this.getMariusQLMetaData();
	}

	@Override
	public MariusQLResultSetMetaData getMariusQLMetaData(int step) {
		return this.getResultSet(step).getMariusQLMetaData();
	}

	@Override
	public int getStepNumber() {
		return this.resultSetDelegates.size();
	}

	@Override
	public <T> List<T> getList(int columnIndex, Class<T> classType) throws SQLException {
		throw new NotYetImplementedException();
	}

	@Override
	public <T> List<T> getList(String columnName, Class<T> classType) throws SQLException {
		throw new NotYetImplementedException();
	}

	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		throw new NotYetImplementedException();
	}

	public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
		throw new NotYetImplementedException();
	}
}
