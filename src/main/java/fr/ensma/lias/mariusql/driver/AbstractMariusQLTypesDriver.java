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
package fr.ensma.lias.mariusql.driver;

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.engine.tree.dml.SetClause;
import fr.ensma.lias.mariusql.engine.tree.dml.ValuesClause;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.jdbc.MariusQLSession;

/**
 * @author Ghada TRIKI
 * @author Mickael BARON
 */
public abstract class AbstractMariusQLTypesDriver extends AbstractMariusQLDriver implements MariusQLTypesDriver {

	private static final String BOOLEAN = "boolean";

	private static final String TRUE = "true";

	private static final String FALSE = "false";

	public AbstractMariusQLTypesDriver(MariusQLSession pSession) {
		super(pSession);
	}

	@Override
	public String getTrue() {
		return AbstractMariusQLTypesDriver.TRUE;
	}

	@Override
	public String getFalse() {
		return AbstractMariusQLTypesDriver.FALSE;
	}

	@Override
	public String getBooleanType() {
		return AbstractMariusQLTypesDriver.BOOLEAN;
	}

	@Override
	public SQLFunction getSQLFunction(final String name) {
		if (name.equalsIgnoreCase(MariusQLConstants.FUN_UPPER)) {
			return this.getFactorySQLFunction().createUpperFunction();
		} else if (name.equalsIgnoreCase(MariusQLConstants.FUN_LOWER)) {
			return this.getFactorySQLFunction().createLowerFunction();
		} else if (name.equalsIgnoreCase(MariusQLConstants.FUN_LENGTH)) {
			return this.getFactorySQLFunction().createLengthFunction();
		} else if (name.equalsIgnoreCase(MariusQLConstants.FUN_BIT_LENGTH)) {
			return this.getFactorySQLFunction().createBitLengthFunction();
		} else if (name.equalsIgnoreCase(MariusQLConstants.FUN_ABS)) {
			return this.getFactorySQLFunction().createAbsFunction();
		} else if (name.equalsIgnoreCase(MariusQLConstants.FUN_SQRT)) {
			return this.getFactorySQLFunction().createSqrtFunction();
		} else if (name.equalsIgnoreCase(MariusQLConstants.FUN_NULLIF)) {
			return this.getFactorySQLFunction().createNullIfFunction();
		} else if (name.equalsIgnoreCase(MariusQLConstants.FUN_COALESCE)) {
			return this.getFactorySQLFunction().createCoalesceFunction();
		} else if (name.equalsIgnoreCase(MariusQLConstants.ARRAY_TO_STRING)) {
			return this.getFactorySQLFunction().createArrayToStringFunction();
		} else if (name.equalsIgnoreCase(MariusQLConstants.FUN_CONCAT)) {
			return this.getFactorySQLFunction().createConcatFunction();
		} else {
			throw new MariusQLException("The function does not exist");
		}
	}

	@Override
	public String getUnnestFunction() {
		return MariusQLConstants.FUN_UNNEST;
	}

	@Override
	public void setArrayDescription(SetClause setClause) {
	}

	@Override
	public void setInsertArrayDescription(ValuesClause valueClause) {
	}
}
