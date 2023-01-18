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

import java.util.List;

import fr.ensma.lias.mariusql.MariusQLConstants;
import fr.ensma.lias.mariusql.core.DatatypeEnum;
import fr.ensma.lias.mariusql.exception.NotSupportedException;

/**
 * @author Ghada TRIKI
 * @author Florian MHUN
 * @author Adel GHAMNIA
 */
public abstract class AbstractFactorySQLFunction implements FactorySQLFunction {

	protected SQLFunction createDefaultFunction(final String name) {
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
				if (name.equalsIgnoreCase(MariusQLConstants.FUN_CONCAT)) {
					for (int i = 0; i < args.size(); i++) {
						buf.append(args.get(i));
						if (i < args.size() - 1)
							buf.append("||");
					}
					return buf.toString();
				} else {
					buf.append(name).append('(');
					for (int i = 0; i < args.size(); i++) {
						buf.append(args.get(i));
						if (i < args.size() - 1)
							buf.append(", ");
					}
					return buf.append(')').toString();
				}
			}
		};
	}

}
