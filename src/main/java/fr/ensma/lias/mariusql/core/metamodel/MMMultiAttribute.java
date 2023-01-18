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
package fr.ensma.lias.mariusql.core.metamodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.Datatype;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * Represents an ontology attribut as part of an entity.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 * @author Valentin CASSAIR
 */
public class MMMultiAttribute extends MMAttribute {

    public static final String DEFAULT_LANGUAGE_CODE = MariusQLConstants.ENGLISH;
    
    private Logger log = LoggerFactory.getLogger(MMMultiAttribute.class);
    
    protected String lgCode;
    
    public MMMultiAttribute(MariusQLSession pSession, Long pRid, String pName, MMEntity pScope, Long pRangeRid, boolean pIsOptional) {
	super(pSession, pRid, pName, pScope, pRangeRid, pIsOptional);
    }
    
    @Override
    public boolean isMultilingualDescription() {
	return true;
    }
    
    @Override
    public void setLgCode(String lgCode) {
	log.info("multilang-attribute '" + this.getName() 
		+ "' will use defined language [" + lgCode + "] instead of session language [" + this.getSession().getReferenceLanguage() + "]");
	
	this.lgCode = lgCode;	
    }
    
    public String getLgCode() {
	return lgCode;
    }
    
    @Override
    public String toSQL() {
	String languageCode = null;
	
	if (this.getLgCode() == null) {
	    String sessionLanguage = this.getSession().getReferenceLanguage();
	    languageCode = sessionLanguage;
	} else {
    	    languageCode = this.getLgCode();
    	}
	
	// When no language, use default language.
	if (MariusQLConstants.NO_LANGUAGE.equalsIgnoreCase(languageCode)) {
	    languageCode = MMMultiAttribute.DEFAULT_LANGUAGE_CODE;
	}
	
	return this.getDefinitionName(true) + MariusQLConstants.UNDERSCORE + languageCode.toLowerCase();
    }
    
    @Override
    public String getSQLDefinition() {
	String res = null;

	Datatype range = this.getRange();

	if (this.isCore()) {
	    res = this.toSQL() + " " + range.toSQL();
	} else {
	    res = MariusQLConstants.ATTRIBUTE_PREFIX_COLUMN_TABLE_NAME + this.getInternalId().toString() + " " + range.toSQL();
	}

	return res;
    }
}
