/*********************************************************************************
* This file is part of OntoQL Project.
* Copyright (C) 2006  LISI - ENSMA
*   Teleport 2 - 1 avenue Clement Ader
*   BP 40109 - 86961 Futuroscope Chasseneuil Cedex - FRANCE
* 
* OntoQL is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* OntoQL is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public License
* along with OntoQL.  If not, see <http://www.gnu.org/licenses/>.
**********************************************************************************/
package fr.ensma.lias.mariusql.engine.tree;

import antlr.SemanticException;
import antlr.collections.AST;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.driver.SQLFunction;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLSQLTokenTypes;
import fr.ensma.lias.mariusql.engine.tree.dql.AbstractSelectExpression;
import fr.ensma.lias.mariusql.engine.tree.dql.FromElement;
import fr.ensma.lias.mariusql.engine.tree.dql.SelectExpression;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotSupportedException;
import fr.ensma.lias.mariusql.util.MariusQLHelper;

/**
 * Represents a method call.
 * 
 * @author Stéphane JEAN
 */
public class MethodNode extends AbstractSelectExpression implements SelectExpression {

	private static final long serialVersionUID = -5040732737631184627L;

	/**
	 * Name of this method.
	 */
	private String methodName;

	/**
	 * From element referencing by this method.
	 */
	private FromElement fromElement;

	/**
	 * SQL function executed.
	 */
	private SQLFunction function;

	/**
	 * Is this call done in the SELECT clause?
	 */
	private boolean inSelect;

	/**
	 * Return datatype of this method.
	 */
	private DatatypeEnum datatype;

	public void setDatatype(DatatypeEnum datatype) {
		this.datatype = datatype;
	}

	/**
	 * Resolve this methode call.
	 * 
	 * @param inSelect true if this call in the SELECT clause
	 * @throws SemanticException if a semantic exception is detected
	 */
	public final void resolve(final boolean inSelect) throws SemanticException {
		// Get the function name node.
		AST name = getFirstChild();
		initializeMethodNode(name, inSelect);
		AST exprList = name.getNextSibling();

		if (name.toString().equalsIgnoreCase(MariusQLConstants.FUN_NULLIF)) {
			validateNullIfFunction(exprList);
		}

		dialectFunction(exprList);
	}

	private void validateNullIfFunction(AST exprList) {
		if (exprList.getFirstChild() instanceof IdentNode) {
			IdentNode prop = (IdentNode) exprList.getFirstChild();
			MProperty propdesc = (MProperty) prop.getDescription();
			AST propValue = exprList.getFirstChild().getNextSibling();

			if (propdesc.getRange().getName().equalsIgnoreCase(MariusQLConstants.INT_NAME.toLowerCase())) {
				propValue.setType(MariusQLSQLTokenTypes.NUM_INT);
				propValue.setText(MariusQLHelper.getQuotedString(propValue.getText()));
			} else if (propdesc.getRange().getName().equalsIgnoreCase(MariusQLConstants.REAL_NAME.toLowerCase())) {
				propValue.setType(MariusQLSQLTokenTypes.NUM_LONG);
				propValue.setText(MariusQLHelper.getQuotedString(propValue.getText()));
			}
		}
	}

	/**
	 * @return the SQL function called
	 */
	public final SQLFunction getSQLFunction() {
		return function;
	}

	/**
	 * @param exprList ?
	 */
	private void dialectFunction(final AST exprList) {
		function = this.getSession().getMariusQLTypes().getSQLFunction(methodName);

		if (function != null) {
			setDatatype(function.getReturnType());
		} else {
			throw new MariusQLException("The function is null");
		}
	}

	/**
	 * initialize this node.
	 * 
	 * @param name     name of this node
	 * @param inSelect true if in SELECT clause
	 */
	public final void initializeMethodNode(final AST name, final boolean inSelect) {
		name.setType(MariusQLSQLTokenTypes.METHOD_NAME);
		String text = name.getText();
		methodName = text.toLowerCase(); // Use the lower case function name.
		this.inSelect = inSelect; // Remember whether we're in a SELECT clause
		// or not.
	}

	/**
	 * @return the name of this method
	 */
	public String getMethodName() {
		return methodName;
	}

	@Override
	public final FromElement getFromElement() {
		return fromElement;
	}

	@Override
	public final DatatypeEnum getDataType() {
		return datatype;
	}

	@Override
	public final String getLabel() {
		throw new NotSupportedException();
	}

	public boolean isCallFromSelect() {
		return this.inSelect;
	}
}
