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
package fr.ensma.lias.mariusql.cache.querycache.impl;

import java.util.Map;

import fr.ensma.lias.mariusql.cache.LRUHashMap;
import fr.ensma.lias.mariusql.cache.querycache.QueryCache;
import fr.ensma.lias.mariusql.cache.querycache.QueryModel;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * @author Mickael BARON
 */
public class QueryCacheImpl implements QueryCache {

	protected Map<String, QueryModel> mapCache;

	protected boolean isEnabled = false;

	public QueryCacheImpl(MariusQLSession p, int maxSize) {
		mapCache = new LRUHashMap<String, QueryModel>(maxSize);
	}

	@Override
	public void addQuery(String ast, QueryModel sql) {
		mapCache.put(ast, sql);
	}

	@Override
	public boolean isExists(String ast) {
		return mapCache.containsKey(ast);
	}

	@Override
	public QueryModel getSQL(String ast) {
		return mapCache.get(ast);
	}

	@Override
	public boolean isEnabled() {
		return this.isEnabled;
	}

	@Override
	public void setEnabled(boolean p) {
		this.isEnabled = p;
	}
}
