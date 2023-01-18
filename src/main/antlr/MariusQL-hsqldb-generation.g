header
{

/*********************************************************************************
 * This file is part of MariusQL Project.
 * Copyright (C) 2013  LIAS - ENSMA
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
package fr.ensma.lias.mariusql.driver.hsqldb.antlr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.mariusql.exception.NotYetImplementedException;
import fr.ensma.lias.mariusql.engine.antlr.MariusQLSQLTokenTypes;

}
/**
 * SQL Generator Tree Parser, providing SQL rendering of SQL ASTs produced by the previous phase, OntoqlSqlWalker.  All
 * syntax decoration such as extra spaces, lack of spaces, extra parens, etc. should be added by this class.
 * <br>
 * This grammar processes the SQL AST and produces an SQL string.  The intent is to move dialect-specific
 * code into a sub-class that will override some of the methods, just like the other two grammars in this system.
 *
 * @author Stephane JEAN 
 * @author Mickael BARON
 */
class HsqldbGeneratorBase extends TreeParser;

options {
	// Note: importVocab and exportVocab cause ANTLR to share the token type numbers between the
	// two grammars.  This means that the token type constants from the source tree are the same
	// as those in the target tree.  If this is not the case, tree translation can result in
	// token types from the *source* tree being present in the target tree.
	importVocab=MariusQLSQL;    // import definitions from "MariusQLSQL"
	exportVocab=HsqlDB;    // Call the resulting definitions "HsqlDB"
	buildAST=false;             // Don't build an AST.
}

tokens {
	COLUMN;
	TABLE;
}

{
	private static Logger log = LoggerFactory.getLogger(HsqldbGeneratorBase.class);

    /** 
     * The buffer resulting SQL statement is written to 
     */
	private StringBuffer buf = new StringBuffer();

	protected void out(String s) {
		buf.append(s);
	}	
	
	protected void out(AST n) {
		out(n.getText());
	}

	protected void separator(AST n, String sep) {
		if (n.getNextSibling() != null)
			out(sep);
	}
	
	protected void beginFunctionTemplate(AST m,AST i) {
		throw new NotYetImplementedException();
	}

	protected void endFunctionTemplate(AST m) {
	    throw new NotYetImplementedException();
	}
	
	protected void commaBetweenParameters(String comma) {
		out(comma);
	}	

	/**
	 * Returns the last character written to the output, or -1 if there isn't one.
	 */
	protected int getLastChar() {
		int len = buf.length();
		if ( len == 0 )
			return -1;
		else
			return buf.charAt( len - 1 );
	}
	
	/**
	 * Add a aspace if the previous token was not a space or a parenthesis.
	 */
	protected void optionalSpace() {
		// Implemented in the sub-class.
	}

	protected StringBuffer getStringBuffer() {
		return buf;
	}
	
	
	protected void fromFragmentSeparator(AST a) {
		AST next = a.getNextSibling();
		if (next != null && next.getType() != MariusQLSQLTokenTypes.JOIN_CONDITION) {
			out(", ");
		}
	}
	
	protected void reInit(){
		buf = new StringBuffer();
	}
}

statement {
	reInit();
	}
	: queryExpression	
	| insertStatement
	| updateStatement
	| deleteStatement
	| whereClause
	;
	


insertStatement
	: #(INSERT { out( "insert " ); }
		#(INTO { out( "into " );}
		r:TABLE { out(r); }
		(insertColumnList)?)
		(selectStatement | valueClause)
	)
	;

insertColumnList
	: #(RANGE {out("(");} (a:COLUMN {out(a); separator(a,", ");})+ {out(")");})
	;

valueClause
	: #(VALUES {out("values (");} (valueElement)+ {out(")");})
	;

valueElement
	: c:simpleExpr { separator(c,", "); } | ({out("(");}s:selectStatement {out(")");separator(s,", "); })
	;
	
updateStatement
	: #(UPDATE { out("update "); }
		#( FROM a:TABLE {out(a.getText());} )
		setClause
		(whereClause)?
	)
	;

setClause
	: #( SET { out(" set "); } eqExpr ( { out(", "); } eqExpr )* )
	;

setElement
	: p:COLUMN {out(p.getText().substring(p.getText().indexOf('.')+1,p.getText().length()));}
	;

eqExpr
	: #(EQ setElement { out("="); } valueElement)
	;

whereClause
	: #(WHERE { out(" where "); } booleanExpr[ false ] )
	;

deleteStatement
	// Note: not space needed at end of "delete" because the from rule included one before the "from" it outputs
	: #(DELETE { out("delete"); }
		from
		(whereClause)?
	)
	;

queryExpression
	: #(UNION { out("("); } queryExpression { out(") union ("); } queryExpression { out(")"); })
	| #(EXCEPT { out("("); } queryExpression { out(") except ("); } queryExpression { out(")"); })
	| #(INTERSECT { out("("); } queryExpression { out(") intersect ("); } queryExpression { out(")"); })
	| selectStatement
	;


selectStatement
	: query 
	;
	
query {} 
	:	
	#(SELECT {out("select ");}
		selectClause
		from
		( #(WHERE { out(" where "); } whereExpr ) )?
		( #(GROUP { out(" group by "); } groupExprs ( #(HAVING { out(" having "); } booleanExpr[false]) )? ) )?
		( #(ORDER { out(" order by "); } orderExprs ) )?
        ( #(LIMIT { out(" limit "); } selectExpr (OFFSET { out(" offset "); } selectExpr)? ) )?
        ( #(IMPLEMENTATION {}))?
	)
	;
	
orderExprs
	: ( expr ) (dir:orderDirection { out(" "); out(dir); })? ( {out(", "); } orderExprs)?
	;

whereExpr
	: booleanExpr[false]
	;
		
orderDirection
	: ASCENDING
	| DESCENDING
	;

groupExprs
	: expr ( {out(", "); } groupExprs)?
	;


selectClause
	: #(SELECT_CLAUSE (distinct)? ( selectColumn )+ )
	;

distinct
	: DISTINCT { out("distinct "); }
	;

selectColumn
	: p:selectExpr  { separator(p,", "); }
	;

selectExpr
	: c:constant { out(c); }
	| count
	| methodCall
	| aggregate
	| arithmeticExpr
	| arrayExpr
	;
	
count
	: #(COUNT { out("count("); } countExpr { out(")"); } ) 
	;

aggregate
	: #(a:AGGREGATE { out(a); out("("); }  expr { out(")"); } )
	;
	
methodCall
{log.debug("METHOD CALL");}
	: #(m:METHOD_CALL i:METHOD_NAME { beginFunctionTemplate(m,i); }
	 ( #(EXPR_LIST (arguments)? ) )?
	 { endFunctionTemplate(m); } )
	;
	
arguments
	: expr ( { commaBetweenParameters(", "); } expr )*
	;

countExpr
	// Syntacitic predicate resolves star all by itself, avoiding a conflict with STAR in expr.
	: ROW_STAR { out("*"); }
	| simpleExpr
	;


from
	: #(f:FROM { out(" from "); }
		(fromTable[true])* )
	;

fromTable[boolean addSeparator]
	// Write the table node (from fragment) and all the join fragments associated with it.
	// Mickael BARON : #( a:TABLE {out(a);} ((LEFT_OUTER { out(" left outer join "); } | INNER_JOIN { out(" inner join "); } | RIGHT_OUTER { out(" right outer join "); }) fromTable[false] (j:JOIN_CONDITION { out(" " +#j.getText()); } | #(ON {out (" on ");} booleanExpr[ false ]) )  )* {if (addSeparator) fromFragmentSeparator(a);} )	
	:                  #( a:TABLE {out(a);} ((LEFT_OUTER { out(" left outer join "); } | INNER_JOIN { out(" inner join "); } | RIGHT_OUTER { out(" right outer join "); }) fromTable[false] (j:JOIN_CONDITION { out(" " +#j.getText()); } | #(ON {out (" on ");} booleanExpr[ false ]) )  )* {if (addSeparator) fromFragmentSeparator(a);} )
	;


booleanOp[ boolean parens ]
	: #(AND booleanExpr[true] { out(" and "); } booleanExpr[true])
	| #(OR { if (parens) out("("); } booleanExpr[false] { out(" or "); } booleanExpr[false] { if (parens) out(")"); })
	| #(NOT { out(" not ("); } booleanExpr[false] { out(")"); } )
	;

booleanExpr[ boolean parens ]
	: booleanOp [ parens ]
	| comparisonExpr [ parens ]
	| j:JOIN_CONDITION { out(j); }
	;
	
comparisonExpr[ boolean parens ]
	: binaryComparisonExpression
	| { if (parens) out("("); } exoticComparisonExpression { if (parens) out(")"); }
	;
	
binaryComparisonExpression
	: #(EQ expr { out("="); } expr)
	| #(NE expr { out("<>"); } expr)
	| #(GT expr { out(">"); } expr)
	| #(GE expr { out(">="); } expr)
	| #(LT expr { out("<"); } expr)
	| #(LE expr { out("<="); } expr)
	;
	
exoticComparisonExpression
	: #(LIKE expr { optionalSpace(); out("like "); } expr )
	| #(NOT_LIKE expr { optionalSpace(); out("not like "); } expr)
	| #(BETWEEN expr { out(" between "); } expr { out(" and "); } expr)
	| #(NOT_BETWEEN expr { out(" not between "); } expr { out(" and "); } expr)
	| #(IN inLhs { optionalSpace(); out("in"); } inList )
	| #(NOT_IN inLhs { optionalSpace(); out("not in "); } inList )
	| #(EXISTS { optionalSpace(); out("exists "); } quantified )
	| #(IS_NULL expr) { out(" is null"); }
	| #(IS_NOT_NULL expr) { out(" is not null"); }
	;

inLhs
	: expr
	;

inList
	: #(IN_LIST { out(" "); } ( parenSelect | simpleExprList ) )
	;

simpleExprList
	: { out("("); } (e:simpleExpr { separator(e," , "); } )* { out(")"); }
	;


// A simple expression, or a sub-select with parens around it.
expr
	: simpleExpr
	| parenSelect
	| #(ANY { out("any "); } (quantified | c:COLUMN { out("(" + c + ")");}) )
	| #(ALL { out("all "); } quantified )
	| #(SOME { out("some "); } quantified )
	;

quantified
	: { out("("); } ( selectStatement ) { out(")"); } 
	;
	
parenSelect
	: { out("("); } selectStatement { out(")"); }
	;

simpleExpr
	: c:constant { out(c); }
	| methodCall
	| count
	| aggregate
	| arithmeticExpr
	| arrayExpr
	| datatypeExpr
	;

datatypeExpr
	: p:PREDEFINED_TYPE { out(p); }
	| #(REF c:constant) { out("ref "); out(c); }
	| #(ARRAY_DEF datatypeExpr) { out("array "); }
	;

arrayExpr
	: #(a:ARRAY { out(a); }  (simpleExprArray | queryExprArray))
	;

simpleExprOracleArray
	: { out("("); } (e:simpleExpr { separator(e," , "); } )* { out(")"); }
	;
	
queryExprOracleArray
	: { out("("); } query { out(")"); }
	;
	
simpleExprArray
	: { out("["); } (e:simpleExpr { separator(e," , "); } )* { out("]"); }
	;

queryExprArray
	: { out("("); } query { out(")"); }
	;


// add case expression
arithmeticExpr
	: additiveExpr
	| multiplicativeExpr
	| #(UNARY_MINUS { out("-"); } expr)
	| caseExpr
	;	
additiveExpr
	: #(PLUS expr { out("+"); } expr)
	| #(MINUS expr { out("-"); } expr)
	;

multiplicativeExpr
	: #(DIV nestedExpr { out("/"); } nestedExpr)
	| #(STAR nestedExpr { out("*"); } nestedExpr)
	;

nestedExpr
	// Generate parens around nested additive expressions, use a syntactic predicate to avoid conflicts with 'expr'.
	: (arithmeticExpr) => { out("("); } additiveExpr { out(")"); }
	| expr
	;


caseExpr
	: #(CASE { out("case"); } 
		( #(WHEN { out( " when "); } booleanExpr[false] { out(" then "); } expr) )+ 
		( #(ELSE { out(" else "); } expr) )?
		{ out(" end"); } )
	| #(CASE2 { out("case "); } expr
		( #(WHEN { out( " when "); } expr { out(" then "); } expr) )+ 
		( #(ELSE { out(" else "); } expr) )?
		{ out(" end"); } )
	;

constant
	: NUM_DOUBLE
	| NUM_INT
	| NUM_LONG
	| QUOTED_STRING
	| CONSTANT
	| TRUE
	| FALSE
	| NULL
	| COLUMN
	| ALIAS
	;
