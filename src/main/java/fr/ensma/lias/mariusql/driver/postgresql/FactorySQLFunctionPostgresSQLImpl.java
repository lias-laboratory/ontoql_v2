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
package fr.ensma.lias.mariusql.driver.postgresql;

import java.util.List;

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.driver.AbstractFactorySQLFunction;
import fr.ensma.lias.mariusql.driver.SQLFunction;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotSupportedException;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * @author Ghada TRIKI
 * @author Florian MHUN
 */
public class FactorySQLFunctionPostgresSQLImpl extends AbstractFactorySQLFunction {

	@Override
	public SQLFunction createUpperFunction() {
		return this.createDefaultFunction(MariusQLConstants.FUN_UPPER);
	}

	@Override
	public SQLFunction createLowerFunction() {
		return this.createDefaultFunction(MariusQLConstants.FUN_LOWER);
	}

	@Override
	public SQLFunction createLengthFunction() {
		return this.createDefaultFunction(MariusQLConstants.FUN_LENGTH);
	}

	@Override
	public SQLFunction createBitLengthFunction() {
		return this.createDefaultFunction(MariusQLConstants.FUN_BIT_LENGTH);
	}

	@Override
	public SQLFunction createAbsFunction() {
		return this.createDefaultFunction(MariusQLConstants.FUN_ABS);
	}

	@Override
	public SQLFunction createSqrtFunction() {
		return this.createDefaultFunction(MariusQLConstants.FUN_SQRT);
	}

	@Override
	public SQLFunction createNullIfFunction() {
		return this.createDefaultFunction(MariusQLConstants.FUN_NULLIF);
	}

	@Override
	public SQLFunction createCoalesceFunction() {
		return this.createDefaultFunction(MariusQLConstants.FUN_COALESCE);
	}

	@Override
	public SQLFunction createArrayToStringFunction() {
		return this.createDefaultFunction(MariusQLConstants.ARRAY_TO_STRING);
	}

	@Override
	public SQLFunction createCastToTextFunction() {
		return new SQLFunction() {

			@Override
			public boolean hasParenthesesIfNoArguments() {
				throw new NotSupportedException();
			}

			@Override
			public boolean hasArguments() {
				return true;
			}

			@Override
			public DatatypeEnum getReturnType() {
				return DatatypeEnum.DATATYPESTRING;
			}

			@Override
			public String render(List<String> args) {
				if (args.size() != 1) {
					throw new MariusQLException("sql function require 1 parameters");
				}

				return "text(" + StringHelper.addSimpleQuotedString(args.get(0)) + ")";
			}
		};
	}

	@Override
	public SQLFunction createConcatFunction() {
		return this.createDefaultFunction(MariusQLConstants.FUN_CONCAT);
	}
}
