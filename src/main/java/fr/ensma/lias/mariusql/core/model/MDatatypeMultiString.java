/**
 * 
 */
package fr.ensma.lias.mariusql.core.model;

import fr.ensma.lias.mariusql.core.DatatypeMultiString;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * @author Ghada TRIKI
 * @author Mickael BARON
 */
public class MDatatypeMultiString extends DatatypeMultiString {    
    
    public MDatatypeMultiString(MariusQLSession pSession, Long pRid) {
	super(pSession, pRid);
    }

    @Override
    public String toSQL() {
	return getSession().getMariusQLTypes().getStringType();
    }
}
