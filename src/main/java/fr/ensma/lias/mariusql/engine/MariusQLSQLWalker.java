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
package fr.ensma.lias.mariusql.engine;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import antlr.RecognitionException;
import antlr.SemanticException;
import antlr.collections.AST;
import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.Category;
import fr.ensma.lias.mariusql.core.Datatype;
import fr.ensma.lias.mariusql.core.DatatypeCollection;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.core.DatatypeReference;
import fr.ensma.lias.mariusql.core.Description;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.metamodel.MMAttribute;
import fr.ensma.lias.mariusql.core.metamodel.MMEntity;
import fr.ensma.lias.mariusql.core.model.MClass;
import fr.ensma.lias.mariusql.core.model.MGenericClass;
import fr.ensma.lias.mariusql.core.model.MProperty;
import fr.ensma.lias.mariusql.driver.postgresql.antlr.PostgresSQLTokenTypes;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLSQLBaseWalker;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLSQLTokenTypes;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLTokenTypes;
import fr.ensma.lias.mariusql.engine.tree.ArrayNode;
import fr.ensma.lias.mariusql.engine.tree.IdentNode;
import fr.ensma.lias.mariusql.engine.tree.LiteralNode;
import fr.ensma.lias.mariusql.engine.tree.MethodNode;
import fr.ensma.lias.mariusql.engine.tree.ResolvableNode;
import fr.ensma.lias.mariusql.engine.tree.ddl.AlterStatement;
import fr.ensma.lias.mariusql.engine.tree.ddl.CreateStatement;
import fr.ensma.lias.mariusql.engine.tree.ddl.DropStatement;
import fr.ensma.lias.mariusql.engine.tree.dml.DMLEvaluator;
import fr.ensma.lias.mariusql.engine.tree.dml.DeleteStatement;
import fr.ensma.lias.mariusql.engine.tree.dml.InsertStatement;
import fr.ensma.lias.mariusql.engine.tree.dml.IntoClause;
import fr.ensma.lias.mariusql.engine.tree.dml.SetClause;
import fr.ensma.lias.mariusql.engine.tree.dml.UpdateStatement;
import fr.ensma.lias.mariusql.engine.tree.dml.ValuesClause;
import fr.ensma.lias.mariusql.engine.tree.dql.AbstractSelectExpression;
import fr.ensma.lias.mariusql.engine.tree.dql.ApproxNode;
import fr.ensma.lias.mariusql.engine.tree.dql.FromClause;
import fr.ensma.lias.mariusql.engine.tree.dql.FromElement;
import fr.ensma.lias.mariusql.engine.tree.dql.SelectExpression;
import fr.ensma.lias.mariusql.engine.tree.dql.SelectStatement;
import fr.ensma.lias.mariusql.engine.util.ASTPrinter;
import fr.ensma.lias.mariusql.engine.util.ASTUtil;
import fr.ensma.lias.mariusql.engine.util.AliasGenerator;
import fr.ensma.lias.mariusql.engine.util.ErrorCounter;
import fr.ensma.lias.mariusql.engine.util.ErrorReporter;
import fr.ensma.lias.mariusql.engine.util.ParseErrorHandler;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotYetImplementedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSet;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;
import fr.ensma.lias.mariusql.jdbc.impl.MariusQLResultSetImpl;
import fr.ensma.lias.mariusql.util.MariusQLHelper;

/**
 * Implements methods used by the MariusQL->SQL tree transform grammar (a.k.a.
 * the second phase).
 * 
 * Isolates the MariusQL API-specific code from the ANTLR generated code. Uses
 * SqlASTFactory to create customized AST nodes.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 * @author Ghada TRIKI
 * @author Adel GHAMNIA
 * @author Valentin CASSAIR
 * @author Géraud FOKOU
 */
public class MariusQLSQLWalker extends MariusQLSQLBaseWalker implements ErrorReporter {

	/**
	 * Handles parser errors.
	 */
	private ParseErrorHandler parseErrorHandler;

	/**
	 * A printer of tree.
	 */
	private ASTPrinter printer;

	/**
	 * Current session.
	 */
	private MariusQLSession session;

	/**
	 * A factory to create entity element.
	 */
	private FactoryCore factoryEntity;

	/**
	 * Current defaultNamespace.
	 */
	private String defaultNamespace = null;

	/**
	 * Count result for updating ontologic queries (update, insert, delete).
	 */
	private int rowCount = 0;

	/**
	 * Alias of the namespaces used in this query
	 */
	private Map<String, String> namespacesAlias = new Hashtable<String, String>();

	/**
	 * List of pathProperties to explicit.
	 */
	private List<MProperty> pathPropertiesProceed = new ArrayList<MProperty>();

	/**
	 * Generator of alias names for tables.
	 */
	private AliasGenerator aliasGenerator;

	/**
	 * List of FromElement added to explicit join.
	 */
	private List<FromElement> fromElementAdded = new ArrayList<FromElement>();

	/**
	 * The current FROM clause of a SELECT statement.
	 */
	private FromClause currentFromClause = null;

	/**
	 * The current context for an UPDATE statement.
	 */
	private SetClause currentSetClause;

	/**
	 * List of Expression in first select This list is used to fill
	 * ResultSetMetaData.
	 */
	private List<SelectExpression> expressionInSelect;

	/**
	 * The current context for an INSERT statement.
	 */
	private IntoClause currentIntoClause;

	/**
	 * 
	 */
	private DMLEvaluator dmlEvaluator;

	/**
	 * 
	 */
	private ValuesClause currentValueClause;

	/**
	 * 
	 */
	private ApproxNode currentRelaxationNode;

	/**
	 * 
	 */
	private boolean isRelaxation = false;

	/**
	 * 
	 */
	private String previousClassIdentifier;

	/**
	 * 
	 */
	private String newClassIdentifier;

	public MariusQLSQLWalker(MariusQLSession pSession) {
		this.session = pSession;
		this.defaultNamespace = session.getDefaultNameSpace();

		this.setASTFactory(new MariusQLSQLASTFactory(this));

		this.expressionInSelect = new ArrayList<SelectExpression>();
		this.aliasGenerator = new AliasGenerator();
		this.parseErrorHandler = new ErrorCounter();
		this.printer = new ASTPrinter(PostgresSQLTokenTypes.class);
		this.dmlEvaluator = new DMLEvaluator(this);
	}

	public FromClause getCurrentFromClause() {
		return currentFromClause;
	}

	/**
	 * @return Returns an alias generator.
	 */
	public AliasGenerator getAliasGenerator() {
		return this.aliasGenerator;
	}

	public DMLEvaluator getDMLEvaluator() {
		return this.dmlEvaluator;
	}

	@Override
	public void reportError(RecognitionException e) {
	}

	@Override
	public void reportError(String s) {
	}

	@Override
	public void reportWarning(String s) {
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int updateCount) {
		this.rowCount = updateCount;
	}

	public final ParseErrorHandler getParseErrorHandler() {
		return parseErrorHandler;
	}

	public void showAst(AST ast, PrintStream out) {
		showAst(ast, new PrintWriter(out));
	}

	private void showAst(AST ast, PrintWriter pw) {
		printer.showAst(ast, pw);
	}

	/**
	 * @return Returns the expressionInSelect.
	 */
	public final List<SelectExpression> getExpressionInSelect() {
		return expressionInSelect;
	}

	/**
	 * @return the defaultNamespace
	 */
	public String getDefaultNamespace() {
		return defaultNamespace;
	}

	/**
	 * @return Returns the session.
	 */
	public MariusQLSession getSession() {
		return session;
	}

	@Override
	protected void processQuery(AST select, AST query) throws SemanticException {
		if (this.defaultNamespace != null && this.fromClauseContainsMClass()) {
			String alias = this.currentFromClause.getFromElements().get(0).getCategory().getTableAlias();
			AST condition = ASTUtil.create(getASTFactory(), EQ, "=");
			condition.addChild(ASTUtil.create(astFactory, PostgresSQLTokenTypes.COLUMN, alias + ".package"));
			condition.addChild(ASTUtil.create(getASTFactory(), PostgresSQLTokenTypes.QUOTED_STRING,
					"'" + this.defaultNamespace + "'"));
			query.addChild(addWhereClause(ASTUtil.create(getASTFactory(), WHERE, "WHERE"), condition));
		}
		this.defaultNamespace = this.getSession().getDefaultNameSpace();
		this.currentFromClause = this.currentFromClause.getParentFromClause();
	}

	@Override
	protected AST addWhereClause(AST whereClauseBuilt, AST logicalExpression) {

		AST res = whereClauseBuilt;
		res.setFirstChild(logicalExpression);
		AST currentWhereClause = currentFromClause.getNextSibling();
		if (currentWhereClause != null && currentWhereClause.getType() == MariusQLTokenTypes.WHERE) {
			// a where clause has already been added
			// the logical expression must be attached using an AND
			AST andClause = ASTUtil.create(getASTFactory(), MariusQLTokenTypes.AND, "and");
			andClause.addChild(logicalExpression);
			andClause.addChild(currentWhereClause.getFirstChild());
			currentWhereClause.setFirstChild(andClause);
			res = null;
		} else if (currentWhereClause != null && currentWhereClause.getType() == MariusQLTokenTypes.ORDER) {
			currentWhereClause = currentFromClause;
			AST whereClause = ASTUtil.createSibling(getASTFactory(), MariusQLTokenTypes.WHERE, "WHERE",
					currentWhereClause);
			whereClause.addChild(logicalExpression);
			whereClause.addChild(currentWhereClause.getFirstChild());
			res = null;
		}
		return res;
	}

	/**
	 * If the path has already been resolved return the corresponding from element
	 * generated.
	 * 
	 * @param property association property for which we search the range
	 * @return the from element if the path has already been resolved, else null
	 */
	public final FromElement getGeneratedFromElement(final MProperty property) {
		FromElement res = null;
		MProperty currentPathProperty;

		for (int i = 0; i < pathPropertiesProceed.size(); i++) {
			currentPathProperty = (MProperty) pathPropertiesProceed.get(i);
			if (isEquivalent(property, currentPathProperty)) {
				res = (FromElement) fromElementAdded.get(i);
			}
		}
		return res;
	}

	@Override
	protected void postProcessCreate(AST create) throws SemanticException {
		((CreateStatement) create).process();
	}

	@Override
	protected void postProcessDrop(AST drop) throws SemanticException {
		((DropStatement) drop).process();
	}

	@Override
	protected void postProcessAlter(AST alter) throws SemanticException {
		((AlterStatement) alter).process();
	}

	@Override
	protected void postProcessDelete(AST delete) throws SemanticException {
		((DeleteStatement) delete).process();
	}

	@Override
	protected final void pushFromClause(final AST fromNode, final AST inputFromNode) {
		FromClause newFromClause = (FromClause) fromNode;
		newFromClause.setParentFromClause(currentFromClause);
		currentFromClause = newFromClause;
	}

	@Override
	protected void addSelectExpr(AST node) {
		// A Select Expression is added only if this is the top level query
		// or if it is a sub query in a DML statement.
		if ((currentFromClause.getParentFromClause() == null) || (!isSelectStatement())) {
			// Moreover the projection must be manually managed (for example see
			// Star node)
			// So, we must check if we must add this automatically

			boolean isIdentNode = node instanceof IdentNode;
			if (!isIdentNode || ((IdentNode) node).isToAddInProjectionList()) {
				expressionInSelect.add((SelectExpression) node);
			}
		}
	}

	@Override
	protected final AST resolve(final AST node, final AST prefix) throws SemanticException {
		ResolvableNode r = (ResolvableNode) node;
		return r.resolve(prefix);
	}

	@Override
	protected final AST createFromElement(final AST node, final AST star, final AST alias, final boolean genAlias,
			boolean isDml) throws SemanticException {
		if (this.isRelaxation()) {
			if (node.getText().equals(this.previousClassIdentifier)) {
				node.setText(this.newClassIdentifier);
			}
		}

		FromElement fromElement = currentFromClause.addFromElement(node, star, alias, genAlias, isDml);
		return fromElement;
	}

	@Override
	protected void setIntoClause(AST intoClause) {
		currentIntoClause = (IntoClause) intoClause;
	}

	@Override
	protected void processInsertTarget(AST intoClause, AST element) throws SemanticException {
		IntoClause currentIntoClause = (IntoClause) intoClause;
		currentIntoClause.initialize(element.getText());
	}

	/**
	 * @return the currentIntoClause
	 */
	public IntoClause getCurrentIntoClause() {
		return currentIntoClause;
	}

	@Override
	protected void processInsertColumnElement(AST intoColumnElement) throws SemanticException {
		IdentNode currentIntoColumnElement = (IdentNode) intoColumnElement;
		IntoClause currentIntoClause = getCurrentIntoClause();
		final Category categoryInstantiated = currentIntoClause.getCategoryInstantiated();
		Description descriptionValuated = currentIntoColumnElement.loadDescription(categoryInstantiated);
		currentIntoClause.addDescription(descriptionValuated);

		if (categoryInstantiated.isClass()) {
			boolean exists = ((MClass) categoryInstantiated).isUsedPropertyExists(descriptionValuated.getName());

			if (!exists) {
				throw new MariusQLException(descriptionValuated.getName() + " is not a used property.");
			}
		}

		dmlEvaluator.evaluateDescription(descriptionValuated, currentIntoColumnElement);
	}

	@Override
	protected void setValuesClause(AST valuesClause) {
		currentValueClause = (ValuesClause) valuesClause;
	}

	@Override
	protected void addValueInInsert(AST valueElement) throws SemanticException {
		AbstractSelectExpression valueNode = (AbstractSelectExpression) valueElement;
		String valueToAdd = valueNode.getText();
		if (valueNode.getType() == PostgresSQLTokenTypes.ARRAY) {
			AST firstChild = valueNode.getFirstChild();
			if (firstChild.getType() == PostgresSQLTokenTypes.QUERY) {
				throw new NotYetImplementedException();
			} else {
				valueToAdd = getArrayValues(firstChild);
			}
		}
		currentValueClause.addValue(valueToAdd);
		currentValueClause.addValueType(valueNode.getDataType());
	}

	/**
	 * @param node
	 * @return
	 */
	private String getArrayValues(AST node) {
		String res = MariusQLHelper.getQuotedString(node.getText());
		AST currentNode = node.getNextSibling();
		while (currentNode != null) {
			res += ", " + MariusQLHelper.getQuotedString(currentNode.getText());
			currentNode = currentNode.getNextSibling();
		}
		return "[" + res + "]";
	}

	@Override
	protected void postProcessInsert(AST insert) {
		InsertStatement insertStatement = (InsertStatement) insert;
		// The statement is validated. It requires to check that
		// all properties of type REF
		// are valued by existing values and valued also their tablenames
		// insertStatement.validate();
		// Let a chance to the evaluator to do some other transformations
		this.getDMLEvaluator().postProcessInsert(insertStatement);
		this.setInsertArrayType(insertStatement);
		this.setInsertBooleanTypes(insertStatement);
	}

	private void setInsertBooleanTypes(InsertStatement insertStatement) {
		ValuesClause valueClause = insertStatement.getValuesClause();
		AST node = valueClause.getFirstChild();
		while (node != null) {
			AST nodeValue = node;
			if (nodeValue instanceof LiteralNode) {
				if (((LiteralNode) nodeValue).getDataType().equals(DatatypeEnum.DATATYPEBOOLEAN)) {
					if (nodeValue.getText().equalsIgnoreCase("true")) {
						nodeValue.setText(this.getSession().getMariusQLTypes().getTrue());
					} else if (nodeValue.getText().equalsIgnoreCase("false")) {
						nodeValue.setText(this.getSession().getMariusQLTypes().getFalse());
					}
				}
			}

			node = node.getNextSibling();
		}
	}

	private void setInsertArrayType(InsertStatement insertStatement) {
		ValuesClause valueClause = insertStatement.getValuesClause();
		AST node = valueClause.getFirstChild();
		while (node != null) {
			AST array = node;
			if (array instanceof ArrayNode) {
				this.getSession().getMariusQLTypes().setInsertArrayDescription(valueClause);
			}

			node = node.getNextSibling();
		}
	}

	@Override
	protected void setAlias(AST selectExpr, AST ident) {
		String alias = ident.getText();
		((SelectExpression) selectExpr).setAlias(alias);
	}

	protected void checkType(AST node, AST operatorNode) {
		DatatypeEnum dataType = ((SelectExpression) node).getDataType();

		ArrayList<String> operators = new ArrayList<String>();
		operators.addAll(dataType.getBooleanOperators());
		operators.addAll(dataType.getArithmeticOperators());

		String operator = operatorNode.getText();

		if (!operator.equals("(")) { // This is a function call
			if (!operators.contains(operator.toUpperCase())) {
				String msg = ((SelectExpression) node).getLabel();
				throw new MariusQLException("The operator '" + operator + "' can not be used on " + msg);
			}
		}
	}

	@Override
	protected void checkType(AST nodeLeft, AST operatorNode, AST nodeRight) {
		checkType(nodeLeft, operatorNode);
		if (nodeRight != null) {
			checkType(nodeRight, operatorNode);
		}

		// We need to cast an integer into a bigint if we do this concatenation
		// prop_collection_ref || int need to be revised (done for ewokhub)
		String operator = operatorNode.getText();

		if (operator.equals(Datatype.OP_CONCAT)) {
			AST nodeExprList = operatorNode.getFirstChild().getNextSibling();
			AST nodeValue = nodeExprList.getFirstChild();

			if (nodeValue.getType() == MariusQLTokenTypes.NUM_INT) {
				throw new NotYetImplementedException();
			}
		}
	}

	/**
	 * Create a node corresponding to a given description (property or attribute
	 * 
	 * @param fromElement the element in the from clause related to this description
	 * @param description an attribute or property
	 * @return the node corresponding to this description
	 */
	public IdentNode createDescriptionNode(FromElement fromElement, Description description) {
		// The result is a node corresponding to a description (property or
		// attribute)

		IdentNode res = null;

		String identifier = description.isAttribute() ? "#" + description.getName() : "!" + description.getInternalId();
		// The result node
		res = (IdentNode) ASTUtil.create(getASTFactory(), MariusQLSQLTokenTypes.IDENT, identifier);
		res.setDescription(description);
		res.setFromElement(fromElement);

		return res;
	}

	/**
	 * Create a list of node corresponding to the list of the defined properties on
	 * a from element
	 * 
	 * @param fromElement a from element
	 * @param lastRes     the last element of the result list
	 * @param addInSelect True if each element of the list must be added to the
	 *                    projection list
	 * @return the list of the defined properties on a from element
	 */
	public List<IdentNode> createDescriptionsNodes(FromElement fromElement, boolean addInSelect)
			throws SemanticException {

		return fromElement.isEntityFromElement() ? createMMAttributeNodes(fromElement, addInSelect)
				: createMPropertyNodes(fromElement, addInSelect);

	}

	private List<IdentNode> createMPropertyNodes(FromElement fromElement, boolean addInSelect)
			throws SemanticException {
		// The result is the first and last node of a linked list of node
		// corresponding to the different properties
		// The list may be empty
		List<IdentNode> res = new ArrayList<IdentNode>(2);
		res.add(null);
		res.add(null);
		List<MProperty> descriptions = ((MClass) fromElement.getCategory()).getAllDefinedProperties();
		descriptions.add(0, ((MClass) fromElement.getCategory()).getRidProperty());
		for (int i = 0; i < descriptions.size(); i++) {
			Description currentDescription = descriptions.get(i);
			currentDescription.setCurrentContext(fromElement.getCategory());
			IdentNode currentDescriptionNode = this.createDescriptionNode(fromElement, currentDescription);
			// translate this node in SQL (not a path)
			List<IdentNode> currentPropNodeTranslated = currentDescriptionNode.translateToSQL();
			if (currentPropNodeTranslated == null || currentPropNodeTranslated.size() != 2) {
				throw new NotYetImplementedException();
			}

			// The first node is the result.
			if (i == 0) {
				res.set(0, currentPropNodeTranslated.get(0));
			} else {
				// LastRes is not null.
				res.get(1).setNextSibling(currentPropNodeTranslated.get(0));
			}
			res.set(1, currentPropNodeTranslated.get(1));

			// Add it eventually to the projection list.
			if (addInSelect) {
				getExpressionInSelect().add(currentDescriptionNode);
			}
		}

		return res;
	}

	private List<IdentNode> createMMAttributeNodes(FromElement fromElement, boolean addInSelect)
			throws SemanticException {
		// The result is the first and last node of a linked list of node
		// corresponding to the different properties
		// The list may be empty
		List<IdentNode> res = new ArrayList<IdentNode>(2);
		res.add(null);
		res.add(null);
		List<MMAttribute> descriptions = ((MMEntity) fromElement.getCategory())
				// .getDefinedAttributes();
				.getAllAttributes();
		for (int i = 0; i < descriptions.size(); i++) {
			Description currentDescription = descriptions.get(i);
			currentDescription.setCurrentContext(fromElement.getCategory());
			IdentNode currentDescriptionNode = this.createDescriptionNode(fromElement, currentDescription);
			// translate this node in SQL (not a path)
			List<IdentNode> currentPropNodeTranslated = currentDescriptionNode.translateToSQL();
			if (currentPropNodeTranslated == null || currentPropNodeTranslated.size() != 2) {
				throw new NotYetImplementedException();
			}

			// The first node is the result.
			if (i == 0) {
				res.set(0, currentPropNodeTranslated.get(0));
			} else {
				// LastRes is not null.
				res.get(1).setNextSibling(currentPropNodeTranslated.get(0));
			}
			res.set(1, currentPropNodeTranslated.get(1));

			// Add it eventually to the projection list.
			if (addInSelect) {
				getExpressionInSelect().add(currentDescriptionNode);
			}
		}

		return res;
	}

	/**
	 * Compare two path property used in the path expression resolution
	 */
	protected boolean isEquivalent(MProperty propertyToMatch, MProperty property) {
		boolean isEquivalent = false;
		if (isEquivalentContext((MClass) propertyToMatch.getCurrentContext(), (MClass) property.getCurrentContext())) {
			if (propertyToMatch.getInternalId().equals(property.getInternalId())) {
				isEquivalent = true;
			}
		}
		return isEquivalent;
	}

	/**
	 * Compare two context of path property in the path expression resolution
	 */
	protected boolean isEquivalentContext(MClass contextToMatch, MClass context) {
		boolean isEquivalent = false;
		if (contextToMatch == null && context == null) {
			isEquivalent = true;
		} else if (contextToMatch != null && context != null) {
			String contextToMatchAlias = contextToMatch.getCategoryAlias();
			String contextAlias = context.getCategoryAlias();
			if (contextToMatchAlias != null && contextAlias != null) {
				isEquivalent = contextToMatchAlias.equals(contextAlias);
			} else if (contextToMatchAlias == null && contextAlias == null) {
				Long contextToMatchId = contextToMatch.getInternalId();
				Long contextId = context.getInternalId();
				if (contextToMatchId == null && contextId == null) {
					isEquivalent = true;
				} else if (contextToMatchId != null && contextId != null) {
					isEquivalent = contextToMatchId.equals(contextId);
				} else {
					throw new NotYetImplementedException();
				}
			}
		}
		return isEquivalent;

	}

	/**
	 * Create a type node corresponding to a given class
	 * 
	 * @param aClass a class
	 * @return the typeof node corresponding to this class
	 */
	public IdentNode createPropertyTypeOfNode(FromElement fromElement) {
		// The result is a node corresponding to an oid property
		IdentNode res = null;

		// The class corresponding to the from element
		MClass aClass = (MClass) fromElement.getCategory();
		getFactoryEntity();
		// The property corresponding to the result node
		MProperty propTypeOf = FactoryCore.createExistingMProperty(getSession(), aClass.getInternalId());

		// The result node
		res = createDescriptionNode(fromElement, propTypeOf);

		return res;
	}

	/**
	 * @return Returns the factoryEntity.
	 */
	public final FactoryCore getFactoryEntity() {
		return factoryEntity;
	}

	/**
	 * Resolve a function call (yet only concat is implemented)
	 */
	@Override
	protected void processFunction(AST functionCall, boolean inSelect) throws SemanticException {
		MethodNode methodNode = (MethodNode) functionCall;
		methodNode.resolve(inSelect);
	}

	/**
	 * Define a given namespace as the default local namespace for a statement.
	 */
	protected void setLocalNamespace(AST nodeNamespace, AST nodeAliasNamespace) {
		String namespaceValue = nodeNamespace.getText().substring(1, nodeNamespace.getText().length() - 1);
		if (nodeAliasNamespace == null) {
			this.defaultNamespace = namespaceValue;
		} else {
			this.namespacesAlias.put(nodeAliasNamespace.getText(), namespaceValue);
		}
	}

	@Override
	protected AST resolveIsOf(AST instanceNode, boolean neg) {
		IdentNode descriptionNode = (IdentNode) instanceNode;

		FromElement fromElementInstance = descriptionNode.getFromElement();

		if (fromElementInstance == null) {
			throw new NotYetImplementedException();
		}

		if (fromElementInstance.isEntityFromElement()) {
			if (!fromElementInstance.getCategory().isEntity()) {
				throw new NotYetImplementedException();
			}

			instanceNode.setType(PostgresSQLTokenTypes.COLUMN);
			instanceNode.setText(fromElementInstance.getText() + MariusQLConstants.DOT_NAME
					+ MariusQLConstants.DESCRIMINATOR_CORE_ATTRIBUTE_NAME);
			AST inNode = ASTUtil.create(astFactory, MariusQLSQLTokenTypes.IN, MariusQLConstants.OP_IN);
			inNode.setFirstChild(instanceNode);
			AST inListNode = ASTUtil.create(astFactory, MariusQLSQLTokenTypes.IN_LIST,
					MariusQLConstants.IN_LIST_TOKEN_NAME);

			// If the node contains a reference
			if (inNode.getFirstChild().getText().contains(" ")) {
				inNode.getFirstChild().setText(inNode.getFirstChild().getText().split(" ")[1]);
			}

			AST entityIteratorNode = instanceNode.getNextSibling();
			inListNode.setFirstChild(entityIteratorNode);
			AST currentEntityNode = null;
			// The previous sibling of the current category node
			// must be kept
			AST previousEntityNode = null;
			MMEntity currentEntity = null;

			// A boolean is needed to know if each category must
			// be considered with all its subclasses
			boolean isPolymorph = true;
			while (entityIteratorNode != null) {
				currentEntityNode = entityIteratorNode;
				entityIteratorNode = entityIteratorNode.getNextSibling();

				if (currentEntityNode.getType() == MariusQLSQLTokenTypes.ONLY) {
					isPolymorph = false;
					if (previousEntityNode == null) {
						inListNode.setFirstChild(entityIteratorNode);
					} else {
						// // This is not the first node.
						previousEntityNode.setNextSibling(entityIteratorNode);
					}
				} else {
					AST currentNodeEntity = currentEntityNode;
					if (currentEntityNode.getType() == MariusQLSQLTokenTypes.REF) {
						currentNodeEntity = currentEntityNode.getFirstChild();
					}

					currentEntity = FactoryCore.createExistingMMEntity(getSession(),
							MariusQLHelper.getEntityIdentifier(currentNodeEntity.getText()));

					String internalId = MariusQLConstants.SIMPLE_QUOTED_STRING + currentEntity.getName()
							+ MariusQLConstants.SIMPLE_QUOTED_STRING;
					int typeOfNode = MariusQLTokenTypes.NUM_INT;
					currentEntityNode.setText(internalId);
					currentEntityNode.setType(typeOfNode);
					if (isPolymorph) {
						List<Category> itsUnderEntities = currentEntity.getDirectSubCategories();
						for (int i = 0; i < itsUnderEntities.size(); i++) {
							internalId = MariusQLConstants.SIMPLE_QUOTED_STRING
									+ ((MMEntity) itsUnderEntities.get(i)).getName()
									+ MariusQLConstants.SIMPLE_QUOTED_STRING;
							ASTUtil.insertSibling(astFactory.create(typeOfNode, internalId), currentEntityNode);
						}
					}
					isPolymorph = true;
				}
				previousEntityNode = currentEntityNode;
			}
			instanceNode.setNextSibling(inListNode);

			return inNode;
		} else if (fromElementInstance.isClassFromElement()) {
			instanceNode.setType(PostgresSQLTokenTypes.COLUMN);

			MClass fromClass = (MClass) fromElementInstance.getCategory();
			fromElementInstance.setText(fromClass.getSQLPolymorphProjection() + fromClass.getTableAlias());
			instanceNode.setText(fromElementInstance.getGeneratedAlias() + MariusQLConstants.DOT_NAME
					+ MariusQLConstants.TYPE_OF_ID_TOKEN_NAME);

			AST inNode = ASTUtil.create(astFactory, MariusQLTokenTypes.IN, MariusQLConstants.OP_IN);
			inNode.setFirstChild(instanceNode);
			AST inListNode = ASTUtil.create(astFactory, MariusQLSQLTokenTypes.IN_LIST,
					MariusQLConstants.IN_LIST_TOKEN_NAME);

			// If the node contains a reference
			if (inNode.getFirstChild().getText().contains(" ")) {
				inNode.getFirstChild().setText(inNode.getFirstChild().getText().split(" ")[1]);
			}

			AST entityIteratorNode = instanceNode.getNextSibling();
			inListNode.setFirstChild(entityIteratorNode);
			AST currentClassNode = null;
			// The previous sibling of the current category node
			// must be kept
			AST previousClassNode = null;
			long currentClass = 0;
			boolean isPolymorph = true;
			while (entityIteratorNode != null) {
				currentClassNode = entityIteratorNode;
				entityIteratorNode = entityIteratorNode.getNextSibling();

				if (currentClassNode.getType() == MariusQLTokenTypes.ONLY) {
					isPolymorph = false;
					if (previousClassNode == null) {
						inListNode.setFirstChild(entityIteratorNode);
					} else {
						// This is not the first node.
						previousClassNode.setNextSibling(entityIteratorNode);
					}
				} else {
					AST currentNodeClass = currentClassNode;
					if (currentClassNode.getType() == MariusQLTokenTypes.REF) {
						currentNodeClass = currentClassNode.getFirstChild();
					}

					MClass aClass = FactoryCore.createExistingMClass(session, currentNodeClass.getText());
					currentClass = aClass.getInternalId();
					String internalId = MariusQLConstants.SIMPLE_QUOTED_STRING + Long.toString(currentClass)
							+ MariusQLConstants.SIMPLE_QUOTED_STRING;
					int typeOfNode = MariusQLTokenTypes.NUM_INT;
					currentClassNode.setText(internalId);
					currentClassNode.setType(typeOfNode);
					if (isPolymorph) {
						List<Category> currentSubclass = aClass.getDirectSubCategories();
						for (int i = 0; i < currentSubclass.size(); i++) {
							if (currentSubclass.get(i).isClass()) {
								internalId = MariusQLConstants.SIMPLE_QUOTED_STRING
										+ Long.toString(currentSubclass.get(i).getInternalId())
										+ MariusQLConstants.SIMPLE_QUOTED_STRING;
							} else {
								throw new NotYetImplementedException();
							}
							ASTUtil.insertSibling(astFactory.create(typeOfNode, internalId), currentClassNode);
						}
					}
					isPolymorph = true;
				}
				previousClassNode = currentClassNode;

			}
			instanceNode.setNextSibling(inListNode);

			return inNode;
		} else {
			throw new NotYetImplementedException();
		}
	}

	@Override
	protected void setSetClause(AST setClause) {
		currentSetClause = (SetClause) setClause;
	}

	@Override
	protected void postProcessUpdate(AST update) {
		UpdateStatement updateStatement = (UpdateStatement) update;
		this.getDMLEvaluator().postProcessUpdate(updateStatement);
		this.setArrayType(updateStatement);
		this.setUpdateBooleanTypes(updateStatement);
	}

	private void setUpdateBooleanTypes(UpdateStatement updateStatement) {
		SetClause setClause = updateStatement.getSetClause();
		AST node = setClause;
		AST nodeValue = node.getFirstChild();
		while (nodeValue != null) {
			if (nodeValue.getFirstChild().getNextSibling() instanceof LiteralNode) {
				if (((LiteralNode) nodeValue.getFirstChild().getNextSibling()).getDataType()
						.equals(DatatypeEnum.DATATYPEBOOLEAN)) {

					if (nodeValue.getFirstChild().getNextSibling().getText().equalsIgnoreCase("true")) {
						nodeValue.getFirstChild().getNextSibling()
								.setText(this.getSession().getMariusQLTypes().getTrue());
					} else if (nodeValue.getFirstChild().getNextSibling().getText().equalsIgnoreCase("false")) {
						nodeValue.getFirstChild().getNextSibling()
								.setText(this.getSession().getMariusQLTypes().getFalse());
					}
				}
			}

			nodeValue = nodeValue.getNextSibling();
		}
	}

	private void setArrayType(UpdateStatement updateStatement) {
		SetClause setClause = updateStatement.getSetClause();
		AST node = setClause;
		while (node != null) {
			AST array = node.getFirstChild().getFirstChild().getNextSibling();
			if (array instanceof ArrayNode) {
				this.getSession().getMariusQLTypes().setArrayDescription(setClause);
			}

			node = node.getNextSibling();
		}
	}

	@Override
	protected AST addTypeOfUpdate(AST assignmentNode) {
		AST res = assignmentNode;
		AST attributeNode = null;
		AST valueNode = null;
		int currentIndex = 0;
		Boolean hasChanged = new Boolean(false);
		while (assignmentNode != null) {
			attributeNode = assignmentNode.getFirstChild();
			valueNode = attributeNode.getNextSibling();

			String value = "";
			if (valueNode.getType() == ARRAY) {
				AST arrayValueNode = valueNode.getFirstChild();
				if (isQueryNode(arrayValueNode)) {
					value = replaceArraySubQueryValue(valueNode);
					arrayValueNode.setType(MariusQLSQLTokenTypes.NUM_LONG);
					arrayValueNode.setText(value);
				} else {
					while (arrayValueNode != null) {
						value += arrayValueNode.getText() + ",";
						arrayValueNode = arrayValueNode.getNextSibling();
					}
					value = value.substring(0, value.length() - 1);
				}
			} else {
				replaceSubQueryValue(valueNode, currentIndex, hasChanged);
				value = valueNode.getText();
			}

			this.currentSetClause.addDescription(((IdentNode) attributeNode).getDescription());
			this.currentSetClause.addValue(value);
			this.currentSetClause.addValueType(((IdentNode) attributeNode).getDataType());
			assignmentNode = assignmentNode.getNextSibling();
			if (hasChanged.booleanValue()) {
				currentIndex++;
			}
		}

		return res;
	}

	protected String replaceArraySubQueryValue(AST exprRefNode) {
		String res = "";
		try {
			MariusQLGenerator gen = this.getSession().getGenerator();
			gen.statement((SelectStatement) exprRefNode.getFirstChild());
			String sql = gen.getSQL();

			Statement statement = this.getSession().createSQLStatement();
			ResultSet sqlResultSet = statement.executeQuery(sql);
			MariusQLResultSet rs = new MariusQLResultSetImpl(sqlResultSet, expressionInSelect);

			while (rs.next()) {
				res += rs.getString(1) + ",";
			}

			res = res.substring(0, res.length() - 1);

			if (res.equals("")) {
				throw new MariusQLException("A subquery must retrieve at least one result.");
			}

			rs.close();
			statement.close();
		} catch (RecognitionException e) {
			throw new MariusQLException(e);
		} catch (SQLException e) {
			throw new MariusQLException("Could not execute query " + e.getMessage());
		}

		return res;
	}

	/**
	 * Replace a subQuery at the given index in expressionInSelect by a value in DML
	 * statement
	 * 
	 * @param the        subquery node
	 * @param the        index of this node in expressionInSelect
	 * @param isSubQuery Out parameter stating is this node was a subquery
	 * @return The typeof value node of this subquery if is a query that return an
	 *         instance
	 */
	protected AST replaceSubQueryValue(AST exprRefNode, int currentIndex, Boolean isSubQuery) {
		AST res = null;
		AST expr = exprRefNode;

		// If there are several FromClause
		if (exprRefNode.getFirstChild() != null) {
			if (exprRefNode.getFirstChild().getNextSibling() != null) {
				if (exprRefNode.getFirstChild().getNextSibling().getNextSibling() != null) {
					if (exprRefNode.getFirstChild().getNextSibling().getNextSibling().getNextSibling() != null) {
						expr.getFirstChild().getNextSibling().getNextSibling().setNextSibling(null);
					}
				}
			}
		}

		if (isQueryNode(expr)) {
			isSubQuery = new Boolean(true);
			try {
				MariusQLGenerator gen = this.getSession().getGenerator();
				gen.statement((SelectStatement) expr);
				String sql = gen.getSQL();

				Statement statement = this.getSession().createSQLStatement();
				ResultSet sqlResultSet = statement.executeQuery(sql);
				MariusQLResultSet rs = new MariusQLResultSetImpl(sqlResultSet, expressionInSelect);

				ResultSetMetaData rsmd = rs.getMetaData();
				String columnType = rsmd.getColumnTypeName(1);

				if (!rs.next()) {
					throw new MariusQLException("A subquery must retrieve at least one result.");
				} else {
					this.getSession().getMariusQLTypes().setExprRefNode(columnType, exprRefNode, rs);

					if (rs.next()) {
						throw new MariusQLException("A subquery return more than one result.");
					}
				}

				rs.close();
				statement.close();
			} catch (RecognitionException e) {
				throw new MariusQLException(e);
			} catch (SQLException e) {
				throw new MariusQLException("Could not execute query " + e.getMessage());
			}
		}

		return res;
	}

	/**
	 * @param node
	 * @return
	 */
	protected boolean isQueryNode(AST node) {
		return (node.getType() == MariusQLTokenTypes.SELECT);
	}

	/**
	 * Reinitialise ce composant dans une requête union
	 */
	protected void reinitWalker() {
		expressionInSelect = new ArrayList<SelectExpression>();
	}

	@Override
	protected AST unnest(AST propertyNode, AST aliasNode) {

		IdentNode identNode = (IdentNode) propertyNode;

		if (identNode.getDescription().isAttribute()) {
			return unnestAttribute(identNode, aliasNode);
		} else {
			return unnestProperty(identNode, aliasNode);
		}
	}

	/**
	 * @param identAttributeNode
	 * @param aliasNode
	 * @return
	 */
	protected AST unnestAttribute(IdentNode identAttributeNode, AST aliasNode) {
		MMAttribute attributeNode = (MMAttribute) identAttributeNode.getDescription();
		FromElement joinFromElement = identAttributeNode.getFromElement();
		String joinFromElementAlias = joinFromElement.getCategory().getTableAlias();
		Description description = identAttributeNode.getDescription();
		Datatype typeOfDescription = description.getRange();

		if (!typeOfDescription.isCollectionAssociationType()) {
			// for the moment only collection of ref can be unnested.
			throw new MariusQLException("Only a collection of reference can be unnested");
		}

		Category rangeCategoryOfDescription = attributeNode.getScope();
		rangeCategoryOfDescription.setCategoryAlias(aliasNode.getText());
		FromElement generatedFromElement = new FromElement(rangeCategoryOfDescription, true, this);

		currentFromClause.registerFromElement(generatedFromElement);

		// 2 cases:
		// - 1) unnest of a collection attribut of an iterator in the same from
		// clause.
		// - 2) unnest of a collection attribut of an iterator in an other from
		// clause (subquery).

		boolean isUnnestSubquery = joinFromElement.getFromClause().getLevel() != currentFromClause.getLevel();

		if (isUnnestSubquery) {
			// Case 2 - The join is implicit
			throw new NotYetImplementedException();
		} else {
			// Case 1 - The join must be explicited
			String mappedJoinFromElementAttribute = attributeNode.getName();
			generatedFromElement.setText(this.getSession().getMariusQLTypes().getUnnestFunction() + "("
					+ joinFromElementAlias + MariusQLConstants.DOT_NAME + mappedJoinFromElementAttribute + ")");
		}
		return generatedFromElement;
	}

	protected AST unnestProperty(IdentNode identPropertyNode, AST aliasNode) {
		MProperty propertyNode = (MProperty) identPropertyNode.getDescription();
		FromElement joinFromElement = identPropertyNode.getFromElement();
		Description description = identPropertyNode.getDescription();
		Datatype typeOfDescription = description.getRange();

		if (!typeOfDescription.isCollectionAssociationType()) {
			// for the moment only collection of ref can be unnested.
			throw new MariusQLException("Only a collection of reference can be unnested");
		}

		// Type of the collection.
		Category rangeProperty = propertyNode.getCurrentContext();
		// Category rangeProperty = propertyNode.getScope();
		rangeProperty.setCategoryAlias(aliasNode.getText());
		FromElement generatedFromElement = new FromElement(rangeProperty, false, this);
		// generatedFromElement.setAlias(aliasNode.getText());

		currentFromClause.registerFromElement(generatedFromElement);

		// 2 cases:
		// - 1) unnest of a collection property of an iterator in the same from
		// clause.
		// - 2) unnest of a collection property of an iterator in an other from
		// clause (subquery).

		boolean isUnnestSubquery = joinFromElement.getFromClause().getLevel() != currentFromClause.getLevel();

		if (isUnnestSubquery) {
			// Case 2 - The join is implicit
			throw new NotYetImplementedException();
		} else {
			// case 1) the join must be explicited

			try {
				generatedFromElement = addImplicitJoin(joinFromElement, identPropertyNode, false);
			} catch (SemanticException e) {
				throw new NotYetImplementedException();
			}
		}

		return generatedFromElement;
	}

	@Override
	protected boolean isOntologicUnnest(AST propertyNode) {

		if (propertyNode instanceof IdentNode) {
			IdentNode identPropertyNode = (IdentNode) propertyNode;

			return identPropertyNode.getDescription().isAttribute();
		}
		return false;
	}

	@Override
	protected void addQueryInInsert(AST queryElement) throws SemanticException {
		SelectStatement select = (SelectStatement) queryElement;
		SelectExpression projectElement = (SelectExpression) select.getSelectClause().getFirstSelectExpression();
		currentValueClause.addValue(""); // the value is unknown until the
		// query is executed
		currentValueClause.addValueType(projectElement.getDataType());
	}

	/**
	 * @param fromElement
	 * @param pathPropNode
	 * @param polymorph
	 * @return
	 * @throws SemanticException
	 */
	public FromElement addImplicitJoin(FromElement fromElement, IdentNode pathPropNode, boolean polymorph)
			throws SemanticException {

		MProperty pathProp = (MProperty) pathPropNode.getDescription();
		AST leftJoin = ASTUtil.create(astFactory, MariusQLSQLTokenTypes.LEFT_OUTER,
				this.getSession().getMariusQLTypes().getLeftOuterJoin());

		final MGenericClass rangeClass;

		if (pathProp.getRange().isCollectionAssociationType()) {
			rangeClass = (MGenericClass) ((DatatypeReference) ((DatatypeCollection) pathProp.getRange()).getDatatype())
					.getCategory();
		} else if (pathProp.getRange().isAssociationType()) {
			rangeClass = (MGenericClass) ((DatatypeReference) pathProp.getRange()).getCategory();
		} else {
			throw new NotYetImplementedException();
		}

		FromElement generatedFromElement = getCurrentFromClause().getFromElement(pathProp.getName());
		if (generatedFromElement == null)
			generatedFromElement = new FromElement(rangeClass, true, this);
		else
			rangeClass.setTableAlias(generatedFromElement.getCategory().getTableAlias());

		// Change true by polymorph if you want that a non polymorphic query
		// is also non polymorphic on path expression

		if (!rangeClass.isAbstract()) {
			final MProperty usedPropertyByName = ((MClass) rangeClass).getRidProperty();
			// Build LEFT OUTER JOIN condition. Example: g6251x0_.p6151_ref =
			// g6250x0_.rid
			boolean isArrayTable = pathPropNode.getSQL().endsWith(
					MariusQLConstants.UNDERSCORE + MariusQLConstants.PROPERTY_SUFFIX_COLUMN_REF_COLLECTION_NAME);
			String condition = this.getSession().getMariusQLTypes().addImplicitJoinCondition(isArrayTable, pathPropNode,
					usedPropertyByName, rangeClass);
			AST joinCondition = ASTUtil.create(astFactory, PostgresSQLTokenTypes.JOIN_CONDITION, condition);

			fromElement.addChild(leftJoin);
			fromElement.addChild(generatedFromElement);
			fromElement.addChild(joinCondition);

			pathPropertiesProceed.add(pathProp);
			fromElementAdded.add(generatedFromElement);
		} else {
			throw new NotYetImplementedException();
		}

		return generatedFromElement;
	}

	/**
	 * @return the namespace corresponding to a given alias
	 */
	public final String getNamespace(String alias) {
		return (String) namespacesAlias.get(alias);
	}

	@Override
	protected void validateType(AST node, AST node2) {
		IdentNode prop = (IdentNode) node;
		MProperty propdesc = (MProperty) prop.getDescription();
		String dataType = propdesc.getRange().getName();

		if (dataType.equalsIgnoreCase(MariusQLConstants.INT_NAME.toLowerCase())) {
			node2.setType(MariusQLSQLTokenTypes.NUM_INT);
			node2.setText(MariusQLHelper.getQuotedString(node2.getText()));
		} else if (dataType.equalsIgnoreCase(MariusQLConstants.REAL_NAME.toLowerCase())) {
			node2.setType(MariusQLSQLTokenTypes.NUM_LONG);
			node2.setText(MariusQLHelper.getQuotedString(node2.getText()));
		}
	}

	private boolean fromClauseContainsMClass() {
		return currentFromClause.toStringList().contains(MariusQLConstants.M_CLASS_TABLE_NAME);
	}

	@Override
	public void setApproxNode(AST node) {
		this.currentRelaxationNode = (ApproxNode) node;

		this.currentRelaxationNode.resolve();
	}

	public ApproxNode getRelaxationNode() {
		return this.currentRelaxationNode;
	}

	public boolean isRelaxationQuery() {
		return this.currentRelaxationNode != null;
	}

	public void setRelaxation(boolean pRelaxation) {
		this.isRelaxation = pRelaxation;
	}

	public boolean isRelaxation() {
		return this.isRelaxation;
	}

	public void setReplaceClassIdentifierForRelaxation(String previousClassIdentifier, String newClassIdentifier) {
		this.previousClassIdentifier = previousClassIdentifier;
		this.newClassIdentifier = newClassIdentifier;
	}
}
