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
package fr.ensma.lias.mariusql.cache.data.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.cache.LRUHashMap;
import fr.ensma.lias.mariusql.cache.data.AbstractCache;
import fr.ensma.lias.mariusql.core.Datatype;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.model.MGenericClass;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotSupportedException;
import fr.ensma.lias.mariusql.exception.NotYetImplementedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * @author Mickael BARON
 * @author Florian MHUN
 * @author Ghada TRIKI
 */
public class MDataCacheImpl extends AbstractCache<MGenericClass> {

	/**
	 * Store simple datatype according to its name.
	 */
	protected Map<String, Datatype> mapSimpleDatatype;

	/**
	 * Store simple datatype according to its rid.
	 */
	protected Map<Long, String> mapSimpleDatatypeByRid;

	/**
	 * Store MGenericClass according to code identifier.
	 */
	protected Map<String, MGenericClass> mapClass;

	/**
	 * Store MGenericClass according to rid
	 */
	protected Map<Long, String> mapClassByRid;

	public MDataCacheImpl(MariusQLSession p, int maxSize) {
		super(p);

		this.mapSimpleDatatype = Collections.synchronizedMap(new LRUHashMap<String, Datatype>(maxSize));
		this.mapSimpleDatatypeByRid = Collections.synchronizedMap(new LRUHashMap<Long, String>(maxSize));

		this.mapClass = Collections.synchronizedMap(new LRUHashMap<String, MGenericClass>(maxSize));
		this.mapClassByRid = Collections.synchronizedMap(new LRUHashMap<Long, String>(maxSize));
	}

	@Override
	public void initialize() {
		if (!this.isEnabled()) {
			return;
		}

		super.initialize();

		// Load the simpledatatype.
		this.initializeSimpleDatatype();
	}

	/**
	 * Initialize all datatypes.
	 */
	protected void initializeSimpleDatatype() {
		log.info("initialize cache with model datatypes");

		List<DatatypeEnum> simpleDatatypes = Arrays.asList(DatatypeEnum.DATATYPEINT, DatatypeEnum.DATATYPESTRING,
				DatatypeEnum.DATATYPEBOOLEAN, DatatypeEnum.DATATYPEREAL, DatatypeEnum.DATATYPEMULTISTRING,
				DatatypeEnum.DATATYPEURI);

		for (DatatypeEnum datatypeType : simpleDatatypes) {
			Datatype datatype = FactoryCore.createMSimpleDatatype(this.getSession(), datatypeType);

			mapSimpleDatatype.put(datatype.getName(), datatype);
			mapSimpleDatatypeByRid.put(datatype.getInternalId(), datatype.getName());
		}
	}

	@Override
	public void refresh() {
		throw new NotSupportedException();
	}

	@Override
	public Datatype getSimpleDatatype(DatatypeEnum type) {
		if (!this.isEnabled()) {
			return null;
		}

		if (!this.isInitialized()) {
			throw new MariusQLException("model cache is not initialized yet");
		}

		return this.mapSimpleDatatype.get(type.getName());
	}

	@Override
	public Datatype getSimpleDatatype(Long pRid) {
		if (!this.isEnabled()) {
			return null;
		}

		if (!this.isInitialized()) {
			throw new MariusQLException("model cache is not initialized yet");
		}

		final String currentType = mapSimpleDatatypeByRid.get(pRid);
		if (currentType == null) {
			return null;
		}

		return this.mapSimpleDatatype.get(currentType);
	}

	@Override
	public void addElement(MGenericClass klass) {
		if (!this.isEnabled()) {
			return;
		}

		if (!this.isInitialized()) {
			throw new MariusQLException("model cache is not initialized yet");
		}

		if (klass == null || klass.getName() == null) {
			klass.getName();
			throw new MariusQLException("error: attempt to cache null value");
		}

		String key = this.getMappedKey(klass.getName());

		if (!this.isElementExists(key)) {
			mapClass.put(key, klass);
			mapClassByRid.put(klass.getInternalId(), key);
		} else {
			throw new MariusQLException("class " + klass.getName() + " aldreay exists in cache");
		}

		log.debug("added new class into model cache: " + klass.getName());
	}

	@Override
	public boolean isElementExists(String identifier) {
		if (!this.isEnabled()) {
			return false;
		}

		if (StringHelper.isNumeric(identifier)) {
			return this.isElementExists(Long.parseLong(identifier));
		}

		if (!this.isInitialized()) {
			throw new MariusQLException("model cache is not initialized yet");
		}

		return mapClass.containsKey(this.getMappedKey(identifier));
	}

	@Override
	public boolean isSimpleTypeExists(DatatypeEnum type) {
		if (!this.isEnabled()) {
			return false;
		}

		if (!this.isInitialized()) {
			throw new MariusQLException("model cache is not initialized yet");
		}

		return mapSimpleDatatype.containsKey(type.getName());
	}

	@Override
	public boolean isElementExists(Long rid) {
		if (!this.isEnabled()) {
			return false;
		}

		if (!this.isInitialized()) {
			throw new MariusQLException("meta model cache is not initialized yet");
		}

		return mapClassByRid.containsKey(rid);
	}

	@Override
	public MGenericClass getElement(String name) {
		if (!this.isEnabled()) {
			return null;
		}

		if (StringHelper.isNumeric(name)) {
			return this.getElement(Long.parseLong(name));
		}

		if (!this.isInitialized()) {
			throw new MariusQLException("model cache is not initialized yet");
		}

		return this.mapClass.get(this.getMappedKey(name));
	}

	@Override
	public MGenericClass getElement(Long rid) {
		if (!this.isEnabled()) {
			return null;
		}

		if (!this.isInitialized()) {
			throw new MariusQLException("meta model cache is not initialized yet");
		}

		String key = this.mapClassByRid.get(rid);

		if (key == null) {
			return null;
		}

		return this.mapClass.get(key);
	}

	@Override
	public void clean() {
		log.info("clean cache: " + mapClass.size() + " classes removed");

		super.clean();

		this.mapClass.clear();
		this.mapClassByRid.clear();

		log.info("clean cache: " + mapSimpleDatatype.size() + " datatypes removed");

		this.mapSimpleDatatype.clear();
		this.mapSimpleDatatypeByRid.clear();

		this.initialize();
	}

	@Override
	public void removeElement(MGenericClass klass) {
		if (!this.isEnabled()) {
			return;
		}

		if (!this.isInitialized()) {
			throw new MariusQLException("model cache is not initialized yet");
		}

		String key = this.getMappedKey(klass.getName());

		mapClass.remove(key);
		mapClassByRid.remove(klass.getInternalId());

		log.debug("removed class from cache: " + klass.getName());
	}

	/**
	 * Mapped key format in model cache :
	 * 
	 * [namespace][name|code]_[language]
	 */
	@Override
	public String getMappedKey(String name) {
		String key = name.toLowerCase();

		final String namespace = this.getSession().getDefaultNameSpace();
		final String language = this.getSession().getReferenceLanguage();

		if (namespace != null) {
			key = namespace + key;
		}

		if (language != null) {
			key = key + MariusQLConstants.UNDERSCORE + language;
		}

		return key;
	}

	@Override
	public Datatype getDatatype(Long pRid) {
		throw new NotYetImplementedException();
	}

	@Override
	public void addDatatype(Long pRid, Datatype p) {
		throw new NotYetImplementedException();
	}
}
