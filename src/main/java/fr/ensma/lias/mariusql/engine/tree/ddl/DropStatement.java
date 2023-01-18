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
package fr.ensma.lias.mariusql.engine.tree.ddl;

import antlr.SemanticException;
import antlr.collections.AST;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.core.model.MClass;
import fr.ensma.lias.mariusql.core.model.MGenericClass;
import fr.ensma.lias.mariusql.engine.MariusQLSQLWalkerNode;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLSQLTokenTypes;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLTokenTypes;
import fr.ensma.lias.mariusql.engine.tree.Statement;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.util.MariusQLHelper;

/**
 * @author Ghada TRIKI
 * @author Florian MHUN
 */
public class DropStatement extends MariusQLSQLWalkerNode implements Statement {

	private static final long serialVersionUID = -6177506970435007154L;

	@Override
	public int getStatementType() {
		return MariusQLSQLTokenTypes.DROP;
	}

	/**
	 * Drop the given entity.
	 * 
	 * @throws JOBDBCException if a semantic exception is detected.
	 */
	public void process() throws SemanticException {

		AST entityTypeNode = getFirstChild();

		if (entityTypeNode.getType() == MariusQLTokenTypes.ENTITY) {
			processDropEntity(entityTypeNode.getFirstChild());
		} else if (entityTypeNode.getType() == MariusQLTokenTypes.EXTENT) {
			processDropExtent(entityTypeNode.getFirstChild());
		} else {
			processDropClass(entityTypeNode);
		}
	}

	private void processDropExtent(AST firstChild) {
		MClass aClass = FactoryCore.createExistingMClass(this.getSession(), firstChild.getText());
		aClass.drop();
		this.getWalker().setRowCount(1);
	}

	private void processDropEntity(AST nodeNameEntity) {
		String entityName = MariusQLHelper.getEntityIdentifier(nodeNameEntity.getText());
		MMEntity entity = FactoryCore.createExistingMMEntity(getWalker().getSession(), entityName);
		this.getWalker().setRowCount(entity.delete());
	}

	private void processDropClass(AST entityTypeNode) {
		AST classTypeNode = entityTypeNode.getFirstChild();

		String entityName = MariusQLHelper.getEntityIdentifier(entityTypeNode.getText());
		String genericClassName = classTypeNode.getText();

		MMEntity mmentity = FactoryCore.createExistingMMEntity(getWalker().getSession(), entityName);
		MGenericClass genericClass = null;

		if (mmentity.isSubMMEntityOf(MariusQLConstants.STATIC_CLASS_CORE_ENTITY_NAME)) {
			genericClass = FactoryCore.createExistingMStaticClass(getSession(), genericClassName);
		} else if (mmentity.isSubMMEntityOf(MariusQLConstants.CLASS_CORE_ENTITY_NAME)) {
			genericClass = FactoryCore.createExistingMClass(getSession(), genericClassName);
		} else {
			throw new MariusQLException("entity is abstract: " + mmentity.getName());
		}

		this.getWalker().setRowCount(genericClass.delete());
	}
}
