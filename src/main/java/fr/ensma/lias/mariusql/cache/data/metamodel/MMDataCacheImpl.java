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
package fr.ensma.lias.mariusql.cache.data.metamodel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import fr.ensma.lias.mariusql.cache.LRUHashMap;
import fr.ensma.lias.mariusql.cache.data.AbstractCache;
import fr.ensma.lias.mariusql.core.Datatype;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotSupportedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * @author Mickael BARON
 * @author Florian MHUN
 * @author Ghada TRIKI
 * @author Valentin CASSAIR
 */
public class MMDataCacheImpl extends AbstractCache<MMEntity> {

	/**
	 * Store simple datatype according to its name.
	 */
	protected Map<String, Datatype> mapSimpleDatatype;

	/**
	 * Store simple datatype according to its rid.
	 */
	protected Map<Long, String> mapSimpleDatatypeByRid;

	/**
	 * Store other datatype accoding to its rid.
	 */
	protected Map<Long, Datatype> mapDatatype;

	/**
	 * Store MMEntity according to name identifier.
	 */
	protected Map<String, MMEntity> mapEntity;

	/**
	 * Store MMEntity according to rid
	 */
	protected Map<Long, String> mapEntityByRid;

	public MMDataCacheImpl(MariusQLSession p, int maxSize) {
		super(p);

		this.mapSimpleDatatype = Collections.synchronizedMap(new LRUHashMap<String, Datatype>(maxSize));
		this.mapSimpleDatatypeByRid = Collections.synchronizedMap(new LRUHashMap<Long, String>(maxSize));

		this.mapDatatype = Collections.synchronizedMap(new LRUHashMap<Long, Datatype>(maxSize));
		this.mapEntity = Collections.synchronizedMap(new LRUHashMap<String, MMEntity>(maxSize));
		this.mapEntityByRid = Collections.synchronizedMap(new LRUHashMap<Long, String>(maxSize));
	}

	private boolean preChecking() throws MariusQLException {
		if (!this.isEnabled()) {
			return false;
		}

		if (!this.isInitialized()) {
			throw new MariusQLException("meta model cache is not initialized yet");
		}

		return true;
	}

	@Override
	public void initialize() {
		if (!this.isEnabled()) {
			return;
		}

		super.initialize();

		// Load the simpledatatype.
		this.initializeSimpleDatatype();
		// Load the metametamodel
		this.initializeMetaMetaModel();
		// Load the metamodel core
		this.initializeMetaModelCore();
	}

	@Override
	public boolean isSimpleTypeExists(DatatypeEnum type) {
		if (!this.preChecking()) {
			return false;
		}

		return mapSimpleDatatype.containsKey(type.getName());
	}

	@Override
	public boolean isElementExists(String identifier) {
		if (!this.preChecking()) {
			return false;
		}

		if (StringHelper.isNumeric(identifier)) {
			return this.isElementExists(Long.parseLong(identifier));
		}

		String key = this.getMappedKey(identifier);
		return mapEntity.containsKey(key);
	}

	@Override
	public boolean isElementExists(Long rid) {
		if (!this.preChecking()) {
			return false;
		}

		return mapEntityByRid.containsKey(rid);
	}

	protected void initializeMetaMetaModel() {
		log.info("initialize cache with meta meta model elements");

		final List<MMEntity> mmEntityCacheElements = this.getSession().getMariusQLDQL().getMMEntityFromMetaMetaModel();

		for (MMEntity mmEntityCacheElement : mmEntityCacheElements) {
			this.addElement(mmEntityCacheElement);
		}
	}

	protected void initializeMetaModelCore() {
		log.info("initialize cache with meta model elements");

		final List<MMEntity> mmEntityCacheElements = this.getSession().getMariusQLDQL().getMMEntityFromMetaModelCore();

		for (MMEntity mmEntityCacheElement : mmEntityCacheElements) {
			this.addElement(mmEntityCacheElement);
		}
	}

	/**
	 * Initialize all datatypes.
	 */
	protected void initializeSimpleDatatype() {
		log.info("initialize cache with meta model datatypes");

		List<DatatypeEnum> simpleDatatypes = Arrays.asList(DatatypeEnum.DATATYPEINT, DatatypeEnum.DATATYPESTRING,
				DatatypeEnum.DATATYPEBOOLEAN, DatatypeEnum.DATATYPEREAL, DatatypeEnum.DATATYPEMULTISTRING);

		for (DatatypeEnum datatypeType : simpleDatatypes) {
			Datatype datatype = FactoryCore.createMMSimpleDatatype(this.getSession(), datatypeType);

			mapSimpleDatatype.put(datatype.getName(), datatype);
			mapSimpleDatatypeByRid.put(datatype.getInternalId(), datatype.getName());
		}
	}

	@Override
	public Datatype getSimpleDatatype(DatatypeEnum type) {
		if (!this.preChecking()) {
			return null;
		}

		return this.mapSimpleDatatype.get(type.getName());
	}

	@Override
	public Datatype getSimpleDatatype(Long pRid) {
		if (!this.preChecking()) {
			return null;
		}

		final String currentType = mapSimpleDatatypeByRid.get(pRid);
		if (currentType == null) {
			return null;
		}

		return this.mapSimpleDatatype.get(currentType);
	}

	@Override
	public void addElement(MMEntity entity) {
		if (!this.preChecking()) {
			return;
		}

		if (entity == null || entity.getName() == null) {
			throw new MariusQLException("Error: cache null value");
		}

		String key = this.getMappedKey(entity.getName());

		if (!this.isElementExists(key)) {
			mapEntity.put(key, entity);
			mapEntityByRid.put(entity.getInternalId(), key);
		} else {
			throw new MariusQLException("Element already exists");
		}

		log.debug("Added new entity into cache: " + entity.getName());
	}

	@Override
	public void removeElement(MMEntity entity) {
		if (!this.preChecking()) {
			return;
		}

		if (entity == null || entity.getName() == null) {
			throw new MariusQLException("error: cache null value");
		}

		String key = this.getMappedKey(entity.getName());

		mapEntity.remove(key);
		mapEntityByRid.remove(entity.getInternalId());

		log.debug("removed entity from cache: " + entity.getName());
	}

	@Override
	public MMEntity getElement(String name) {
		if (!this.preChecking()) {
			return null;
		}

		if (StringHelper.isNumeric(name)) {
			return this.getElement(Long.parseLong(name));
		}

		String key = this.getMappedKey(name);
		MMEntity element = this.mapEntity.get(key);

		return element;
	}

	@Override
	public MMEntity getElement(Long rid) {
		if (!this.preChecking()) {
			return null;
		}

		String key = this.mapEntityByRid.get(rid);

		if (key == null) {
			return null;
		}

		return this.mapEntity.get(key);
	}

	@Override
	public void refresh() {
		throw new NotSupportedException();
	}

	@Override
	public void clean() {
		log.info("clean cache: " + mapEntity.size() + " entities removed");

		super.clean();

		this.mapEntity.clear();
		this.mapEntityByRid.clear();

		log.info("clean cache: " + mapSimpleDatatype.size() + " datatypes removed");

		this.mapSimpleDatatype.clear();
		this.mapSimpleDatatypeByRid.clear();

		this.initialize();
	}

	@Override
	public String getMappedKey(String name) {
		String key = name.toLowerCase();

		return key;
	}

	@Override
	public Datatype getDatatype(Long pRid) {
		if (!this.preChecking()) {
			return null;
		}

		return this.mapDatatype.get(pRid);
	}

	@Override
	public void addDatatype(Long pRid, Datatype p) {
		if (!this.preChecking()) {
			return;
		}

		this.mapDatatype.put(pRid, p);
	}
}
