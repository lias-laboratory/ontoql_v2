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
package fr.ensma.lias.mariusql.driver.hsqldb;

import java.util.List;

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.driver.AbstractFactorySQLFunction;
import fr.ensma.lias.mariusql.driver.SQLFunction;
import fr.ensma.lias.mariusql.exception.MariusQLException;
import fr.ensma.lias.mariusql.exception.NotSupportedException;
import fr.ensma.lias.mariusql.util.StringHelper;

/**
 * 
 * @author Ghada TRIKI
 * @author Florian MHUN
 */
public class FactorySQLFunctionHsqlDBImpl extends AbstractFactorySQLFunction {

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
		return new SQLFunction() {

			@Override
			public boolean hasParenthesesIfNoArguments() {
				throw new NotSupportedException();
			}

			@Override
			public boolean hasArguments() {
				throw new NotSupportedException();
			}

			@Override
			public DatatypeEnum getReturnType() {
				return DatatypeEnum.DATATYPESTRING;
			}

			/**
			 * As mentioned in the page bellow, the BIT_LENGTH function returns double-byte
			 * so we need to divide the result.
			 * 
			 * @see Section DB2.
			 *      http://my.safaribooksonline.com/book/databases/sql/0596004818/4dot4dot3dot-numeric-scalar-functions/id3759762
			 */
			@Override
			public String render(List<String> args) {
				if (args.size() != 1) {
					throw new MariusQLException("sql function require 1 parameters");
				}

				return "(BIT_LENGTH(" + args.get(0) + ")/2)";
			}
		};
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
		return new SQLFunction() {

			@Override
			public boolean hasParenthesesIfNoArguments() {
				throw new NotSupportedException();
			}

			@Override
			public boolean hasArguments() {
				throw new NotSupportedException();
			}

			@Override
			public DatatypeEnum getReturnType() {
				return DatatypeEnum.DATATYPESTRING;
			}

			@Override
			public String render(List<String> args) {
				StringBuilder buf = new StringBuilder();
				buf.append(MariusQLConstants.FUN_COALESCE).append("(");
				for (int i = 0; i < args.size(); i++) {
					buf.append(args.get(i));
					if (i < args.size() - 1)
						buf.append(" , ");
				}
				return buf.append(" )").toString();
			}
		};
	}

	@Override
	public SQLFunction createArrayToStringFunction() {
		throw new NotSupportedException();
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

				return StringHelper.addSimpleQuotedString(args.get(0));
			}
		};
	}

	public SQLFunction createConcatFunction() {
		return this.createDefaultFunction(MariusQLConstants.FUN_CONCAT);
	}
}
