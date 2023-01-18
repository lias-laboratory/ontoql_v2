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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.PersistentObject;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotSupportedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * @author Valentin CASSAIR
 */
public class Language implements PersistentObject {

    private String code = null;
    
    private String name = null;
    
    private String description = null;
    
    private MariusQLSession session = null;
    
    private Map<String, List<String>> tableColumns = new HashMap<String, List<String>>();

    public Language(MariusQLSession s, String name, String code,
	    String description) {
	this.session = s;
	this.code = code;
	this.name = name;
	this.description = description;
    }

    @Override
    public int insert() {
	if (!session.getMariusQLDQL().isLanguageExists(name, code)) {
	    List<String> columns = new ArrayList<String>();
	    List<String> values = new ArrayList<String>();
	    
	    columns.add(MariusQLConstants.NAME_CORE_ATTRIBUTE_NAME);
	    columns.add(MariusQLConstants.CODE_CORE_ATTRIBUTE_NAME);
	    values.add(StringHelper.addSimpleQuotedString(name));
	    values.add(StringHelper.addSimpleQuotedString(code));

	    if (description != null && !description.isEmpty()) {
		columns.add(MariusQLConstants.LANGUAGE_DESCRIPTION);
		values.add(StringHelper.addSimpleQuotedString(description));
	    }

	    int insertedRows = session.getMariusQLDML().insertRow(MariusQLConstants.LANGUAGE_TABLE_NAME,
		    columns, values);
	    List<MMAttribute> list = session.getMariusQLDQL()
		    .getMMAttributeByRange(
			    FactoryCore.createMMSimpleDatatype(session,
				    DatatypeEnum.DATATYPEMULTISTRING));
	    for (MMAttribute attribute : list) {
		MMMultiAttribute multiAttribute = (MMMultiAttribute) attribute;
		multiAttribute.setLgCode(code);
		List<String> tables = null;
		if (!tableColumns.containsKey(attribute.toSQL())){
		    tables = new ArrayList<String>();
		} else {
		    tables = tableColumns.get(attribute.toSQL());
		}
		List<String> newColumns = new ArrayList<String>();
		
		if (!tables.contains(attribute.getScope().getMappedElement())) {    
		    newColumns.add(multiAttribute.getSQLDefinition());
		    session.getMariusQLDDL().addTableColumns(
				attribute.getScope().getMappedElement(), newColumns);
		    tables.add(attribute.getScope().getMappedElement());
		    tableColumns.put(attribute.toSQL(), tables);
		}
		
	    }

	    return insertedRows;
	} else {
	    throw new MariusQLException("Language already exists.");
	}
    }

    @Override
    public Long getInternalId() {
	throw new NotSupportedException();
    }

    @Override
    public int create() {
	throw new NotSupportedException();
    }

    @Override
    public void drop() {
	throw new NotSupportedException();
    }

    @Override
    public int delete() {
	throw new NotSupportedException();
    }

    @Override
    public int update() {
	throw new NotSupportedException();
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public MariusQLSession getSession() {
	return session;
    }

    public void setSession(MariusQLSession session) {
	this.session = session;
    }
}
