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
package fr.ensma.lias.mariusql.cache;

import fr.ensma.lias.mariusql.cache.data.DataCache;
import fr.ensma.lias.mariusql.cache.data.metamodel.MMDataCacheImpl;
import fr.ensma.lias.mariusql.cache.data.model.MDataCacheImpl;
import fr.ensma.lias.mariusql.cache.querycache.QueryCache;
import fr.ensma.lias.mariusql.cache.querycache.impl.QueryCacheImpl;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.core.model.MGenericClass;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * @author Florian MHUN
 */
public class FactoryCache {

	/**
	 * Maximum number of element into the cache
	 */
	private static final int MAX_CACHE_SIZE = 500;

	public static DataCache<MMEntity> createMetaModelCache(MariusQLSession pSession) {
		return new MMDataCacheImpl(pSession, FactoryCache.MAX_CACHE_SIZE);
	}

	public static DataCache<MGenericClass> createModelCache(MariusQLSession pSession) {
		return new MDataCacheImpl(pSession, FactoryCache.MAX_CACHE_SIZE);
	}

	public static QueryCache createQueryCache(MariusQLSession pSession) {
		return new QueryCacheImpl(pSession, FactoryCache.MAX_CACHE_SIZE);
	}
}
