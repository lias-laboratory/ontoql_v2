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
package fr.ensma.lias.mariusql.core.model;

import fr.ensma.lias.mariusql.core.metamodel.MMAttribute;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;

/**
 * Describe a model element of an MMEntity.
 * MGenericClass and MProperty are both model element in MariusQL core.
 * 
 * @author Florian MHUN
 */
public interface ModelElement {

    /**
     * Set attribute value
     * 
     * @param pAttribute
     * @param value
     */
    void setValue(MMAttribute pAttribute, String value);
    
    /**
     * Set attribute value with language code
     * 
     * @param pAttribute
     * @param lg
     * @param value
     */
    void setValue(MMAttribute pAttribute, String lg, String value);
    
    /**
     * Get attribute value
     * 
     * @param attributeName
     * @return
     */
    String getValue(String attributeName);
    
    /**
     * Get the entity which describe this model element
     * 
     * @return
     */
    MMEntity getMMEntity();    
}
