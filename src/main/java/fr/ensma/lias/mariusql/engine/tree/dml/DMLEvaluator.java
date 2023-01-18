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
package fr.ensma.lias.mariusql.engine.tree.dml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import antlr.RecognitionException;
import antlr.collections.AST;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.AbstractDatatypeCollection;
import fr.ensma.lias.mariusql.core.AbstractDatatypeReference;
import fr.ensma.lias.mariusql.core.Category;
import fr.ensma.lias.mariusql.core.Datatype;
import fr.ensma.lias.mariusql.core.DatatypeCollection;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.core.DatatypeMultiString;
import fr.ensma.lias.mariusql.core.DatatypeReference;
import fr.ensma.lias.mariusql.core.DatatypeString;
import fr.ensma.lias.mariusql.core.Description;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.model.MClass;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.driver.postgresql.antlr.PostgresSQLTokenTypes;
import fr.ensma.lias.mariusql.engine.MariusQLGenerator;
import fr.ensma.lias.mariusql.engine.MariusQLSQLWalker;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLSQLTokenTypes;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLTokenTypes;
import fr.ensma.lias.mariusql.engine.tree.ArrayNode;
import fr.ensma.lias.mariusql.engine.tree.IdentNode;
import fr.ensma.lias.mariusql.engine.tree.LiteralNode;
import fr.ensma.lias.mariusql.engine.tree.dql.FromClause;
import fr.ensma.lias.mariusql.engine.tree.dql.FromElement;
import fr.ensma.lias.mariusql.engine.tree.dql.SelectStatement;
import fr.ensma.lias.mariusql.engine.util.ASTUtil;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSet;
import fr.ensma.lias.mariusql.jdbc.impl.MariusQLResultSetImpl;
import fr.ensma.lias.mariusql.util.MariusQLHelper;

/**
 * @author Mickael BARON
 * @author Stéphane JEAN
 */
public class DMLEvaluator {

	private MariusQLSQLWalker walker;

	/**
	 * Constructor.
	 * 
	 * @param ontoQLWalker the walker that use this evaluator
	 */
	public DMLEvaluator(MariusQLSQLWalker walker) {
		this.walker = walker;
	}

	protected void auxEvaluateExt(Category category, AST node) {
		node.setType(PostgresSQLTokenTypes.TABLE);
		node.setText(category.toSQL());
	}

	public void evaluateExt(Category category, IdentNode node) {
		auxEvaluateExt(category, node);
	}

	public MariusQLSQLWalker getWalker() {
		return walker;
	}

	public void evaluateDescription(Description description, IdentNode node) {
		node.setType(PostgresSQLTokenTypes.COLUMN);
		node.setText(getSQLWithoutPrefix(description));
	}

	protected String getSQLWithoutPrefix(Description description) {
		String sql = description.toSQL();
		int indexPrefix = sql.indexOf('.');
		return sql.substring(indexPrefix + 1, sql.length());
	}

	/**
	 * Replace all subqueries by their results
	 * 
	 * @param intoClause   into clause of the insert statement
	 * @param valuesClause values clause of the insert statement
	 */
	private void replaceSubQueries(IntoClause intoClause, ValuesClause valuesClause) {
		// previous node of the current node
		AST previousNode = null;
		AST exprRefNode = valuesClause.getFirstChild();
		// multiple subqueries may be processed
		// so expressionInSelect contain SELECT clause
		// of different subqueries
		// This index stores the index in the expressionInSelect list
		// of the current subquery
		int indexInSelectList = 0;
		// index of the current value in the list of values
		int indexOfValue = 0;
		// hasChanged is true if a query has been executed
		Boolean hasChanged = new Boolean(false);
		while (exprRefNode != null) {
			AST newNode = null;
			if (exprRefNode.getType() == MariusQLSQLTokenTypes.ARRAY) {
				newNode = replaceSubQuery(intoClause, valuesClause, exprRefNode.getFirstChild(), indexInSelectList,
						hasChanged, indexOfValue);
			} else {
				newNode = replaceSubQuery(intoClause, valuesClause, exprRefNode, indexInSelectList, hasChanged,
						indexOfValue);
			}

			if (newNode != null) {
				if (previousNode != null) {
					previousNode.setNextSibling(newNode);
				} else {
					valuesClause.setFirstChild(newNode);
				}
				newNode.setNextSibling(exprRefNode.getNextSibling());
				exprRefNode = newNode;
			}
			previousNode = exprRefNode;
			exprRefNode = exprRefNode.getNextSibling();
			if (hasChanged.booleanValue()) {
				indexInSelectList++;
			}
			indexOfValue++;
		}
	}

	protected AST replaceSubQuery(IntoClause intoClause, ValuesClause valuesClause, AST exprRefNode,
			int indexInSelectList, Boolean isSubQuery, int indexOfValue) {
		AST res = null;
		if (exprRefNode.getType() == MariusQLSQLTokenTypes.SELECT) {

			isSubQuery = new Boolean(true);
			try {
				res = replaceSubOntologicQuery(intoClause, valuesClause, exprRefNode, indexInSelectList, indexOfValue);
			} catch (RecognitionException e) {
				throw new MariusQLException(e.getMessage());
			}
		}

		return res;
	}

	protected AST replaceSubOntologicQuery(IntoClause intoClause, ValuesClause valuesClause, AST exprRefNode,
			int indexInSelectList, int indexOfValue) throws RecognitionException {
		AST res = null;

		final Datatype currentDataType = intoClause.getDescriptions().get(indexOfValue).getRange();
		String sql = ";";
		try {
			MariusQLGenerator gen = this.getWalker().getSession().getGenerator();
			gen.statement((SelectStatement) exprRefNode);

			sql = gen.getSQL();

			Statement statement = this.getWalker().getSession().createSQLStatement();
			ResultSet sqlResultSet = statement.executeQuery(sql);

			MariusQLResultSet rs = new MariusQLResultSetImpl(sqlResultSet, getWalker().getExpressionInSelect());

			if (!rs.next()) {
				throw new MariusQLException("A subquery must retrieve at least one result.");
			} else {
				if (currentDataType.isSimpleType()) {
					Datatype currentSimpleType = (Datatype) currentDataType;
					if (currentSimpleType instanceof DatatypeString
							|| currentSimpleType instanceof DatatypeMultiString) {
						String newValue = MariusQLConstants.SIMPLE_QUOTED_STRING + rs.getString(1)
								+ MariusQLConstants.SIMPLE_QUOTED_STRING;
						res = ASTUtil.create(getWalker().getASTFactory(), MariusQLTokenTypes.QUOTED_STRING, newValue);
						valuesClause.setValue(indexOfValue, newValue);
						if (rs.next()) {
							throw new MariusQLException("A subquery return more than one result for an association.");
						}
					} else {
						throw new MariusQLException("The data type is not supported");
					}
				} else if (currentDataType instanceof DatatypeReference) {
					String newValue = rs.getString(1);
					res = ASTUtil.create(getWalker().getASTFactory(), MariusQLTokenTypes.NUM_LONG, newValue);
					valuesClause.setValue(indexOfValue, newValue);

					if (rs.next()) {
						throw new MariusQLException("A subquery return more than one result for an association.");
					} else {
						return res;
					}
				} else if (currentDataType instanceof DatatypeCollection) {
					String newValue = rs.getString(1);
					valuesClause.setValue(indexOfValue, newValue);

				} else {
					throw new MariusQLException("The data type is not supported");
				}
			}

			rs.close();
			statement.close();
		} catch (RecognitionException exc) {
			throw new MariusQLException(exc);
		} catch (SQLException e) {
			throw new MariusQLException("Could not execute query " + e.getMessage());
		}

		return res;
	}

	/**
	 * Do a last transformation of an insert statement.
	 * 
	 * @param insert an insert statement on an OBDB
	 */
	public void postProcessInsert(InsertStatement insert) {
		IntoClause intoClause = insert.getIntoClause();
		ValuesClause valuesClause = insert.getValuesClause();
		this.replaceSubQueries(intoClause, valuesClause);

		this.validateContext(intoClause.getCategoryInstantiated(), intoClause.getDescriptions());
		this.validateTypes(valuesClause.getValuesTypes(), valuesClause.getValues(), intoClause.getDescriptions());
		this.validateReferences(intoClause.getDescriptions(), valuesClause.getValues());

		this.validateIntoClauseInModel(intoClause, valuesClause);

		// TODO : only for instance
		this.addAdditionalSQLInstanceColumns(insert);
	}

	private void addAdditionalSQLInstanceColumns(InsertStatement insert) {
		List<Description> descriptions = insert.getIntoClause().getDescriptions();
		List<String> values = insert.getValuesClause().getValues();

		for (int i = 0; i < descriptions.size(); i++) {
			Description description = descriptions.get(i);
			String value = values.get(i);

			if (!description.isProperty()) {
				return;
			}

			Datatype descriptionRange = description.getRange();

			if (descriptionRange.isAssociationType() || descriptionRange.isCollectionAssociationType()) {
				if (descriptionRange.isAssociationType()) {
					this.addReferenceTableColumnInInsertNode(insert, (MProperty) description);
				} else if (descriptionRange.isCollectionAssociationType()) {
					this.addReferenceCollectionTableColumnInInsertNode(insert, (MProperty) description,
							MariusQLHelper.getOntoQLCollectionAssociationValues(value));
				}
			}
		}
	}

	private void addReferenceTableColumnInInsertNode(InsertStatement insert, MProperty refProperty) {
		AST columnsNode = insert.getFirstChild().getFirstChild().getNextSibling();
		AST valuesNode = insert.getFirstChild().getNextSibling();

		IdentNode refTableColumnNode = new IdentNode();
		LiteralNode refTableValueNode = new LiteralNode();

		refTableColumnNode.setType(PostgresSQLTokenTypes.COLUMN);
		refTableColumnNode.setText(refProperty.getInstanceReferenceTableColumnName());
		refTableValueNode.setType(PostgresSQLTokenTypes.NUM_INT);
		refTableValueNode.setText(refProperty.getRange().toReference().getCategory().getInternalId().toString());

		columnsNode.addChild(refTableColumnNode);
		valuesNode.addChild(refTableValueNode);
	}

	private void addReferenceCollectionTableColumnInInsertNode(InsertStatement insert, MProperty refCollectionProperty,
			List<Long> refValues) {
		AST columnsNode = insert.getFirstChild().getFirstChild().getNextSibling();
		AST valuesNode = insert.getFirstChild().getNextSibling();

		IdentNode refTableColumnNode = new IdentNode();
		ArrayNode refTableArrayValueNode = new ArrayNode();

		refTableColumnNode.setType(PostgresSQLTokenTypes.COLUMN);
		refTableColumnNode.setText(refCollectionProperty.getInstanceReferenceCollectionTableColumnName());
		refTableArrayValueNode.setType(PostgresSQLTokenTypes.ARRAY);
		refTableArrayValueNode.setText("ARRAY");

		List<Long> refClassRids = refCollectionProperty.getInstanceReferenceCollectionTableColumnValues(refValues);
		for (Long refClassRid : refClassRids) {
			LiteralNode refTableValueNode = new LiteralNode();

			refTableValueNode.setType(PostgresSQLTokenTypes.NUM_INT);
			refTableValueNode.setText(refClassRid.toString());

			refTableArrayValueNode.addChild(refTableValueNode);
		}

		columnsNode.addChild(refTableColumnNode);
		valuesNode.addChild(refTableArrayValueNode);
	}

	public void validateIntoClauseInModel(IntoClause intoClause, ValuesClause valuesClause) {
		AST node = intoClause.getFirstChild().getNextSibling().getFirstChild();
		AST previousnode = null;
		boolean containsDtype = false;
		boolean containCodeAttribute = false;
		while (node != null) {
			if (MariusQLConstants.M_PROPERTY_DTYPE_ATTRIBUTE_NAME.equalsIgnoreCase(node.getText())) {
				containsDtype = true;
			}
			if (MariusQLConstants.M_CLASS_CODE_ATTRIBUTE_NAME.equalsIgnoreCase(node.getText())) {
				containCodeAttribute = true;
			}

			previousnode = node;
			node = node.getNextSibling();
		}

		boolean categoryExists = FactoryCore.isMMEntityExists(intoClause.getCategoryInstanciatedName(),
				getWalker().getSession());

		if (!containsDtype && categoryExists) {
			IdentNode n = new IdentNode();
			n.setText(MariusQLConstants.M_PROPERTY_DTYPE_ATTRIBUTE_NAME);
			n.setType(PostgresSQLTokenTypes.COLUMN);
			previousnode.setNextSibling(n);

			LiteralNode l = new LiteralNode();
			l.setText(MariusQLConstants.SIMPLE_QUOTED_STRING + intoClause.getCategoryInstanciatedName()
					+ MariusQLConstants.SIMPLE_QUOTED_STRING);
			l.setType(PostgresSQLTokenTypes.QUOTED_STRING);
			node = valuesClause.getFirstChild();
			while (node != null) {
				if (node instanceof ArrayNode) {
					AST children = node.getFirstChild();
					while (children != null) {
						LiteralNode lit = (LiteralNode) children;
						lit.setText(MariusQLHelper.getQuotedString(lit.getText()));
						children = children.getNextSibling();
					}
				}
				previousnode = node;
				node = node.getNextSibling();
			}
			previousnode.setNextSibling(l);
		}

		if (!containCodeAttribute && categoryExists) {
			throw new MariusQLException("Does not contain the attribute code");
		}
	}

	/**
	 * Do a last transformation of an update statement.
	 * 
	 * @param update an update statement on an OBDB
	 */
	public void postProcessUpdate(UpdateStatement update) {
		FromClause fromClause = update.getFromClause();
		SetClause setClause = update.getSetClause();
		// update accept only one from element
		FromElement fromElement = (FromElement) fromClause.getFromElements().get(0);

		// set array type

		// TRICK: by pass select projection
		fromElement.setText(fromElement.getCategory().toSQLWithAlias());

		this.unquoteSQL(setClause);
		this.validateContext(fromElement.getCategory(), setClause.getDescriptions());
		this.validateTypes(setClause.getValuesTypes(), setClause.getValues(), setClause.getDescriptions());
		this.validateReferences(setClause.getDescriptions(), setClause.getValues());
	}

	private void unquoteSQL(SetClause setClause) {
		AST node = setClause;
		while (node != null) {
			AST array = node.getFirstChild().getFirstChild().getNextSibling();
			if (array instanceof ArrayNode) {
				AST child = array.getFirstChild();
				while (child != null) {
					if (child.getType() != MariusQLSQLTokenTypes.QUOTED_STRING) {
						child.setText(MariusQLHelper.getQuotedString(child.getText()));
					}
					child = child.getNextSibling();
				}
			}

			node = node.getNextSibling();
		}
	}

	private void validateContext(Category categoryInstantiated, List<Description> descriptions) {
		for (Description currentDescription : descriptions) {
			currentDescription.setCurrentContext(categoryInstantiated);
			if (categoryInstantiated.isClass()) {
				if (!((MClass) categoryInstantiated).isUsedPropertyExists(currentDescription.getName())) {
					throw new MariusQLException(currentDescription.getName() + " is not an used property.");
				}
			} else if (categoryInstantiated.isEntity()) {
				if (!currentDescription.isDefined(categoryInstantiated)) {
					throw new MariusQLException("The " + currentDescription.getTypeLabel() + " "
							+ currentDescription.getName() + " is not defined on the "
							+ categoryInstantiated.getTypeLabel() + " " + categoryInstantiated.getName());
				}
			} else {
				throw new MariusQLException("Is not a entity.");
			}
		}
	}

	private void validateTypes(List<DatatypeEnum> valuesTypes, List<String> values, List<Description> descriptions) {
		int nbrValues = valuesTypes.size();
		int nbrDescriptions = descriptions.size();
		if (nbrValues != nbrDescriptions) {
			throw new MariusQLException("Number of properties" + " valued (" + nbrDescriptions
					+ ") doesn't match the number of values (" + nbrValues + ") in this insert statement");
		}

		// check if the descriptions are defined on the category
		String currentValue;
		DatatypeEnum currentDatatypeValue;
		Datatype currentDatatypeDescription;
		Description currentDescription;
		for (int i = 0; i < nbrDescriptions; i++) {
			currentDescription = descriptions.get(i);
			currentDatatypeValue = (DatatypeEnum) valuesTypes.get(i);
			currentValue = (String) values.get(i);
			currentDatatypeDescription = currentDescription.getRange();

			// if the corresponding description-value are consistent
			// a null value is consistent with all types
			if (!MariusQLHelper.isNull(currentValue)
					&& !areCompatible(currentDatatypeValue, currentDatatypeDescription.getDatatypeEnum())) {
				String descriptionLabel = currentDescription.getTypeLabel();
				throw new MariusQLException("Type of the " + descriptionLabel + " " + currentDescription.getName()
						+ " (" + currentDatatypeDescription.getName() + ")" + " and value " + currentValue + " ("
						+ currentDatatypeValue.getName() + ")" + " at position " + (i + 1) + " are not compatible");
			}
		}
	}

	private void validateReferences(List<Description> descriptions, List<String> values) {
		Description currentDescription;
		String currentValue;

		for (int i = 0; i < descriptions.size(); i++) {
			currentDescription = descriptions.get(i);
			Datatype currentDatatype = currentDescription.getRange();
			currentValue = values.get(i);
			if (currentDatatype.isAssociationType()) {
				Category category = ((AbstractDatatypeReference) currentDatatype).getCategory();
				if (!category.isModelInstanceExists(Long.valueOf(currentValue))) {
					throw new MariusQLException(
							"Reference on class " + category.getName() + " not exists: " + currentValue);
				}
			} else if (currentDatatype.isCollectionAssociationType()) {
				Datatype reference = ((AbstractDatatypeCollection) currentDatatype).getDatatype();
				Category category = ((AbstractDatatypeReference) reference).getCategory();
				List<Long> collectionRefIds = MariusQLHelper.getOntoQLCollectionAssociationValues(currentValue);

				for (Long refId : collectionRefIds) {
					if (!category.isModelInstanceExists(refId)) {
						throw new MariusQLException(
								"Reference on class " + category.getName() + " not exists: " + refId);
					}
				}
			}
		}
	}

	/**
	 * Determine whether the two types are "assignment compatible".
	 * 
	 * @param target The type defined in the into-clause.
	 * @param source The type defined in the select clause.
	 * @return True if they are assignment compatible.
	 */
	private boolean areCompatible(DatatypeEnum source, DatatypeEnum target) {
		return (source.equals(target))
				|| (source == DatatypeEnum.DATATYPEINT && target == DatatypeEnum.DATATYPEREFERENCE)
				|| (source == DatatypeEnum.DATATYPESTRING && target == DatatypeEnum.DATATYPEMULTISTRING)
				|| (source == DatatypeEnum.DATATYPESTRING && target == DatatypeEnum.DATATYPEENUMERATE)
				|| (source == DatatypeEnum.DATATYPESTRING && target == DatatypeEnum.DATATYPEURI)
				|| (source == DatatypeEnum.DATATYPEINT && target == DatatypeEnum.DATATYPECOUNT);
	}
}
