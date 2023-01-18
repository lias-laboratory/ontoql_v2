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
package fr.ensma.lias.mariusql.engine.tree.dql;

import java.util.ArrayList;
import java.util.List;

import antlr.collections.AST;
import fr.ensma.lias.mariusql.core.Category;
import fr.ensma.lias.mariusql.core.FactoryCore;
import fr.ensma.lias.mariusql.core.model.MClass;
import fr.ensma.lias.mariusql.core.model.MGenericClass;
import fr.ensma.lias.mariusql.engine.MariusQLSQLWalker;
import fr.ensma.lias.mariusql.engine.MariusQLSQLWalkerNode;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLSQLTokenTypes;
import fr.ensma.lias.mariusql.engine.util.ASTUtil;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotYetImplementedException;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSet;
import fr.ensma.lias.mariusql.jdbc.MariusQLResultSetRelaxationMetaData.RelaxationEnum;
import fr.ensma.lias.mariusql.jdbc.MariusQLStatement;
import fr.ensma.lias.mariusql.jdbc.impl.MariusQLResultSetMetaDataImpl;
import fr.ensma.lias.mariusql.jdbc.impl.MariusQLResultSetRelaxationImpl;
import fr.ensma.lias.mariusql.jdbc.impl.MariusQLResultSetRelaxationMetaDataImpl;
import fr.ensma.lias.mariusql.jdbc.impl.MariusQLStatementImpl;

/**
 * Represents a relaxation node.
 * 
 * @author Mickael BARON
 */
public class ApproxNode extends MariusQLSQLWalkerNode {

	private static final long serialVersionUID = 6855039173286237495L;

	protected int limit;

	protected int currentLimit;

	protected MClass relaxClass;

	protected List<MClass> limitClasses;

	protected List<MariusQLStatement> newStatements;

	public int getLimit() {
		return this.limit;
	}

	public MClass getRelaxClass() {
		return this.relaxClass;
	}

	public List<MClass> getLimitClasses() {
		return this.limitClasses;
	}

	public int getCurrentLimit() {
		return this.currentLimit;
	}

	public void setCurrentLimit(int p) {
		this.currentLimit = p;
	}

	protected void resolveSib(AST sibAST) {
		AST classIdentifierAST = sibAST.getFirstChild();
		String classIdentifier = classIdentifierAST.getText();
		relaxClass = FactoryCore.createExistingMClass(this.getSession(), classIdentifier);

		final MGenericClass superClass = relaxClass.getSuperClass();
		List<Category> directSubCategories = null;
		if (superClass == null) {
			throw new NotYetImplementedException();
		} else {
			directSubCategories = superClass.getDirectSubCategories();
		}

		if (!this.isInFromClause()) {
			throw new MariusQLException("Relaxed Class must be located into the From clause:" + relaxClass.getName());
		}

		limitClasses = new ArrayList<MClass>();
		while (classIdentifierAST.getNextSibling() != null) {
			AST currentIdentifierLimit = classIdentifierAST.getNextSibling();
			String currentClassLimitIdentifier = currentIdentifierLimit.getText();

			final MClass createExistingMClass = FactoryCore.createExistingMClass(this.getSession(),
					currentClassLimitIdentifier);

			if (!directSubCategories.contains(createExistingMClass)) {
				throw new MariusQLException(
						currentClassLimitIdentifier + " is not a sibling class of " + classIdentifier);
			}

			limitClasses.add(createExistingMClass);
			classIdentifierAST = currentIdentifierLimit;
		}
	}

	protected void resolveGen(AST genAST) {
		AST classIdentifierAST = genAST.getFirstChild();
		String classIdentifier = classIdentifierAST.getText();
		relaxClass = FactoryCore.createExistingMClass(this.getSession(), classIdentifier);

		if (!this.isInFromClause()) {
			throw new MariusQLException("Relaxed Class must be located into the From clause:" + relaxClass.getName());
		}

		AST limitAST = classIdentifierAST.getNextSibling();
		if (limitAST != null) {
			if (limitAST.getType() == MariusQLSQLTokenTypes.NUM_INT) {
				int maxSuperClass = relaxClass.getAllSuperClasses().size();
				limit = Math.min(Integer.valueOf(limitAST.getText()), maxSuperClass);
			} else {
				String classLimitIdentifier = limitAST.getText();
				final MClass limitClass = FactoryCore.createExistingMClass(this.getSession(), classLimitIdentifier);
				this.getLimitClasses().add(limitClass);

				if (!this.isSuperClass(limitClass)) {
					throw new MariusQLException(
							limitClass.getName() + " is not a super class of " + relaxClass.getName());
				}
			}
		}
	}

	private boolean isSuperClass(MClass untilIdentifier) {
		MGenericClass superClass = relaxClass.getSuperClass();

		while (superClass != null) {
			if (untilIdentifier.getName().equals(superClass.getName())) {
				return true;
			} else {
				superClass = superClass.getSuperClass();
			}
		}

		return false;
	}

	/**
	 * Check if classIdentifier is the same as the from element.
	 * 
	 * @param pIdentifierToRelax
	 * @return
	 */
	private boolean isInFromClause() {
		// Check if classIdentifier is the same as the from element.
		final FromClause currentFromClause = this.getWalker().getCurrentFromClause();
		final List<FromElement> fromElements = currentFromClause.getFromElements();
		for (FromElement fromElement : fromElements) {
			final Category currentCategory = fromElement.getCategory();

			if (currentCategory instanceof MClass) {
				boolean value = (((MClass) currentCategory).getName().equals(relaxClass.getName()));

				if (value) {
					return true;
				}
			}
		}

		return false;
	}

	protected void resolvePred() {
		throw new NotYetImplementedException();
	}

	public void resolve() {
		AST approxExpression = this.getFirstChild();

		this.newStatements = new ArrayList<MariusQLStatement>();
		this.limitClasses = new ArrayList<MClass>();

		switch (approxExpression.getType()) {
		case MariusQLSQLTokenTypes.GEN:
			this.resolveGen(approxExpression);
			break;
		case MariusQLSQLTokenTypes.SIB:
			this.resolveSib(approxExpression);
			break;
		case MariusQLSQLTokenTypes.PRED:
			throw new NotYetImplementedException();
		default: {
			throw new NotYetImplementedException();
		}
		}
	}

	/**
	 * @param c
	 * @return
	 * @throws GMPCleverQueryException
	 */
	private double ic(String classIdentifier) {
		double pr = pr(classIdentifier);
		return -(double) (Math.log(pr));
	}

	private double pr(String classIdentifier) {
		final int allClassRowCount = this.getSession().getMariusQLMetric().getAllClassRowCount(false);
		final int classRowCount = this.getSession().getMariusQLMetric().getClassPolymorphRowCount(classIdentifier,
				false);

		return (double) classRowCount / allClassRowCount;
	}

	/**
	 * psac (C,C') est la superclasse commune à C et C'la plus basse dans la
	 * hiérarchie (psac = plus spécifique ancêtre commun).
	 * 
	 * @param c
	 * @param cprime
	 * @return
	 */
	private MClass psac(MClass classIdentifier, MClass relaxClassIdentifier) {
		final List<MGenericClass> superClassesOfClass = classIdentifier.getAllSuperClasses();

		final MGenericClass superClass = classIdentifier.getSuperClass();
		if (superClass == null) {
			throw new NotYetImplementedException();
		}

		MGenericClass samelowLevelParent = null;
		if (classIdentifier.getName().equals(relaxClassIdentifier.getName())) {
			samelowLevelParent = relaxClassIdentifier;
		} else if (superClassesOfClass.contains(relaxClassIdentifier)) {
			samelowLevelParent = relaxClassIdentifier;
		} else {
			samelowLevelParent = superClass;
		}

		return (MClass) samelowLevelParent;
	}

	private double similarity(MariusQLResultSet executeQuery, MClass classIdentifier, MClass relaxClassIdentifier) {
		MClass psac = psac(classIdentifier, relaxClassIdentifier);

		final double icpsac = ic(psac.getName());
		final double icclass = ic(classIdentifier.getName());
		final double icrelaxedClass = ic(relaxClassIdentifier.getName());

		return Math.abs(icpsac / (icclass + icrelaxedClass - icpsac));
	}

	protected MariusQLStatementImpl createMariusQLStatement() {
		MariusQLStatementImpl newStatement = (MariusQLStatementImpl) this.getSession().createMariusQLStatement();
		this.newStatements.add(newStatement);
		return newStatement;
	}

	private MariusQLResultSet processSib(AST queryAST, AST sibAST) {
		return this.abstractProcess(queryAST, sibAST, RelaxationEnum.SIB);
	}

	protected MariusQLResultSet processGen(AST queryAST, AST genAST) {
		return this.abstractProcess(queryAST, genAST, RelaxationEnum.GEN);
	}

	protected MariusQLResultSet abstractProcess(AST queryAST, AST genAST, RelaxationEnum relaxationType) {
		MariusQLResultSetRelaxationImpl resultSet = new MariusQLResultSetRelaxationImpl();
		ASTUtil.removeChild(queryAST, this);

		final String queryContentToRelax = queryAST.toStringList();
		MClass currentIdentifier = this.getRelaxClass();
		MClass nextIdentifier = null;

		MariusQLSQLWalker currentWalker = new MariusQLSQLWalker(this.getSession());
		currentWalker.setRelaxation(true);
		MariusQLResultSet executeQuery = this.createMariusQLStatement().executeQuery(queryAST, currentWalker);
		int step = 0;
		((MariusQLResultSetMetaDataImpl) executeQuery.getMariusQLMetaData())
				.setRelaxationMetaData(new MariusQLResultSetRelaxationMetaDataImpl(relaxationType, queryContentToRelax,
						queryContentToRelax, step, currentIdentifier.getName(), currentIdentifier.getName(), 1.0));

		resultSet.addMariusQLResultSet(executeQuery);

		if (this.getLimitClasses().isEmpty()) {
			for (step = 1; step <= this.getLimit(); step++) {
				nextIdentifier = (MClass) currentIdentifier.getSuperClass();
				final MariusQLResultSet executeWithRelaxation = executeWithRelaxation(relaxationType,
						currentIdentifier.getName(), nextIdentifier, queryAST, queryContentToRelax, step);
				resultSet.addMariusQLResultSet(executeWithRelaxation);
				currentIdentifier = nextIdentifier;
			}
		} else {
			for (MClass currentLimitClass : this.getLimitClasses()) {
				step++;
				resultSet.addMariusQLResultSet(executeWithRelaxation(relaxationType, currentIdentifier.getName(),
						currentLimitClass, queryAST, queryContentToRelax, step));
			}
		}

		return resultSet;
	}

	private MariusQLResultSet executeWithRelaxation(RelaxationEnum relaxationType, String currentIdentifier,
			MClass nextIdentifier, AST query, String queryToRelax, int step) {
		MariusQLSQLWalker currentWalker = new MariusQLSQLWalker(this.getSession());
		currentWalker.setRelaxation(true);
		currentWalker.setReplaceClassIdentifierForRelaxation(currentIdentifier, nextIdentifier.getName());
		MariusQLResultSet executeQuery = createMariusQLStatement().executeQuery(query, currentWalker);
		String relaxedQuery = query.toStringList();

		((MariusQLResultSetMetaDataImpl) executeQuery.getMariusQLMetaData())
				.setRelaxationMetaData(new MariusQLResultSetRelaxationMetaDataImpl(relaxationType, queryToRelax,
						relaxedQuery, step, currentIdentifier, currentIdentifier,
						similarity(executeQuery, this.getRelaxClass(), nextIdentifier)));

		return executeQuery;
	}

	public MariusQLResultSet process(AST query) {
		AST approxExpression = this.getFirstChild();

		MariusQLResultSet resultApprox;

		switch (approxExpression.getType()) {
		case MariusQLSQLTokenTypes.GEN:
			resultApprox = this.processGen(query, approxExpression);
			break;
		case MariusQLSQLTokenTypes.SIB:
			resultApprox = this.processSib(query, approxExpression);
			break;
		case MariusQLSQLTokenTypes.PRED:
			throw new NotYetImplementedException();
		default: {
			throw new NotYetImplementedException();
		}
		}

		return resultApprox;
	}

	public List<MariusQLStatement> getSubStatements() {
		return this.newStatements;
	}
}
