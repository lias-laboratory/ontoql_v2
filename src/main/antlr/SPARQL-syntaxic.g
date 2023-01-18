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
package fr.ensma.lias.mariusql.engine.antlr;


}
/**
 * SPARQL Grammar
 * <br>
 * This grammar parses the SPARQL query language according to
 * the working draft of October 4, 2006. The comments provided
 * are extracted from the grammar given in this working draft.
 *
 * @author Stephane JEAN 
 */
class SPARQLBaseParser extends Parser;

options
{
	importVocab=MariusQL;
	exportVocab=SPARQL;
	buildAST=true;
	k=2;  
}

tokens
{
	// -- SPARQL Keyword tokens --
	PREFIX;
	OPTIONAL;
	BOUND;
	FILTER;
	TYPE="rdf:type";
	TRIPLE_TYPE;
	TRIPLE_PROP;
	UNION_LEFT;
	UNION_RIGHT;
	SELECT_WHERE;
}
{
	/** selectClause of the SPARQL query. */
	protected AST selectClauseAST = null;
	
	/** namespaceClause of the SPARQL query. */
	protected AST namespaceClauseAST = null;
	
	/** result of an union query. */
	protected AST unionQueryAST = null;
	
	/** Create a query of an union query. */
	protected AST createUnionQuery(AST triples1, AST triples2) {
		AST res = astFactory.create(UNION);
		res.addChild(createQuery(triples1));
		res.addChild(createQuery(triples2));
		return res;
	}
	
	/** Create a query of an union and a query. */
	protected AST createUnion(AST union, AST triples2) {
		AST res = astFactory.create(UNION);
		res.addChild(union);
		res.addChild(createQuery(triples2));
		return res;
	}
	
	/** Create a query of an union query. */
	protected AST createQuery(AST triples) {
		AST query = astFactory.create(QUERY);
		query.addChild(astFactory.dupTree(namespaceClauseAST));
		AST selectFromAST = astFactory.create(SELECT_WHERE);
		AST whereAST = astFactory.create(WHERE);
		whereAST.addChild(triples);
		selectFromAST.addChild(whereAST);
		selectFromAST.addChild(selectClauseAST);
		query.addChild(selectFromAST);
		return query;
	}
	
}


// [1] Query ::= Prolog
// ( SelectQuery | ConstructQuery | DescribeQuery | AskQuery ) 
query!
	: p:prolog s:selectQuery {
		if (unionQueryAST == null) {
			#query = #([QUERY,"query"],p,s);
		}
		else {
			#query = unionQueryAST;
		}
		
	}
	;

// [2] Prolog ::= BaseDecl? PrefixDecl* 
prolog!
	: (prefixDecl)* {
		namespaceClauseAST = #([PREFIX,"PREFIX"],[QNAME,"geo:"],[Q_IRI_REF,"http://lias.ensma.fr/"]);
		#prolog = #([PREFIX,"PREFIX"],[QNAME,"geo:"],[Q_IRI_REF,"http://lias.ensma.fr/"]);
	}
	;

// [4] PrefixDecl ::= 'PREFIX' QNAME_NS Q_IRI_REF 
prefixDecl
	: PREFIX^ QNAME Q_IRI_REF 
	;
	
// [5] SelectQuery ::= 'SELECT' 'DISTINCT'? ( Var+ | '*' ) DatasetClause* WhereClause SolutionModifier 
selectQuery!
	: s:selectClause {selectClauseAST = #s;} w:whereClause m:solutionModifier {
		#selectQuery = #([SELECT_WHERE,"SELECT_WHERE"],w,s,m);
	}
	;

selectClause
	: SELECT^ (DISTINCT)? (VAR)+ 
	;

// [13] WhereClause ::= 'WHERE'? GroupGraphPattern
whereClause
	: (WHERE^)? groupGraphPattern
	;

// [14] SolutionModifier ::= OrderClause? LimitClause? OffsetClause? 
solutionModifier
	: (orderClause)?
	;

// [15] OrderClause ::= 'ORDER' 'BY' OrderCondition+
orderClause
	: ORDER^ "by"! (orderCondition)+
	;

//[16] OrderCondition ::= ( ( 'ASC' | 'DESC' ) BrackettedExpression )
//| ( FunctionCall | Var | BrackettedExpression ) 
orderCondition
	: ("asc" | "desc")  brackettedExpression 
	;

	
	
// [19] GroupGraphPattern ::= '{' GraphPattern '}' 
groupGraphPattern
	: OPEN_CURLY! g:graphPattern CLOSE_CURLY!
	;

// [20] GraphPattern ::= FilteredBasicGraphPattern ( GraphPatternNotTriples '.'? GraphPattern )? 
graphPattern
	: filteredBasicGraphPattern ( graphPatternNotTriples (DOT!)? graphPattern)?
	;


// [21] FilteredBasicGraphPattern ::= BlockOfTriples? ( Constraint '.'? FilteredBasicGraphPattern )? 
filteredBasicGraphPattern
	: (blockOfTriples)? ( constraint (DOT!)? filteredBasicGraphPattern )?
	;

// [22] BlockOfTriples ::= TriplesSameSubject ( '.' TriplesSameSubject? )* 
blockOfTriples
	: triplesSameSubject ( DOT! (triplesSameSubject)? )* 
	;

// [23] GraphPatternNotTriples ::= OptionalGraphPattern | GroupOrUnionGraphPattern | GraphGraphPattern 
graphPatternNotTriples
	: optionalGraphPattern | groupOrUnionGraphPattern 
	;

// [24] OptionalGraphPattern ::= 'OPTIONAL' GroupGraphPattern
optionalGraphPattern
	: OPTIONAL^ groupGraphPattern
	;

// [26] GroupOrUnionGraphPattern ::= GroupGraphPattern ( 'UNION' GroupGraphPattern )* 
groupOrUnionGraphPattern!
	: g1:groupGraphPattern (UNION g2:groupGraphPattern {
		if (unionQueryAST == null)
			unionQueryAST =createUnionQuery(#g1,#g2);
		else 
			unionQueryAST = createUnion(unionQueryAST, #g2);
	})* 
	;

// [27] Constraint ::= 'FILTER' ( BrackettedExpression | BuiltInCall | FunctionCall ) 
constraint
	: FILTER^ ( brackettedExpression | builtInCall )
	;

// [32] TriplesSameSubject ::= VarOrTerm PropertyListNotEmpty | TriplesNode PropertyList
// [34] PropertyListNotEmpty ::= Verb ObjectList ( ';' PropertyList )?  
triplesSameSubject!
	: s:varOrTerm p:verb o:objectList {
		if (#p.getType()==TYPE)
			#triplesSameSubject = #([TRIPLE_TYPE],s,o);
		else 
			#triplesSameSubject = #([TRIPLE_PROP],s,p,o);
	}
	;

// [35] ObjectList ::= GraphNode ( ',' ObjectList )? 
objectList
	: graphNode
	;

// [36] Verb ::= VarOrIRIref | 'a'
verb
	: varOrIRIref
	;

// [40] GraphNode ::= VarOrTerm | TriplesNode
graphNode
	: varOrTerm
	;

// [41] VarOrTerm ::= Var | GraphTerm 
varOrTerm
	: VAR | graphTerm
	;

// [42] VarOrIRIref ::= Var | IRIref 
varOrIRIref
	: var | iriRef | TYPE
	;

// [44] Var ::= VAR1 | VAR2 
var : VAR ;

// [45] GraphTerm ::= IRIref | RDFLiteral | ( '-' | '+' )? NumericLiteral | BooleanLiteral | BlankNode | NIL 
graphTerm
	: iriRef | rdfLiteral
	;

// [46] Expression ::= ConditionalOrExpression 
expression
	: conditionalOrExpression
	;
	
// [47] ConditionalOrExpression ::= ConditionalAndExpression ( '||' ConditionalAndExpression )* 
conditionalOrExpression
	:  conditionalAndExpression ( OR^ conditionalAndExpression )*
	;

// [48] ConditionalAndExpression ::= ValueLogical ( '&&' ValueLogical )* 
conditionalAndExpression
	: valueLogical ( AND^ valueLogical )* 
	;

// [49] ValueLogical ::= RelationalExpression 
valueLogical
	: relationalExpression
	;
	
// [50] RelationalExpression ::= NumericExpression ( '=' NumericExpression | '!=' NumericExpression | '<' NumericExpression | '>' NumericExpression | '<=' NumericExpression | '>=' NumericExpression )? 
relationalExpression
	: numericExpression ( EQ^ numericExpression | NE^ numericExpression | LT^ numericExpression | GT^ numericExpression | LE^ numericExpression | GE^ numericExpression )?
	;

// [51] NumericExpression ::= AdditiveExpression 
numericExpression
	: additiveExpression
	;

// [52] AdditiveExpression ::= MultiplicativeExpression ( '+' MultiplicativeExpression | '-' MultiplicativeExpression )* 
additiveExpression
	: multiplicativeExpression ( PLUS^ multiplicativeExpression | MINUS^ multiplicativeExpression)*
	;

// [53] MultiplicativeExpression ::= UnaryExpression ( '*' UnaryExpression | '/' UnaryExpression )* 
multiplicativeExpression
	: unaryExpression ( STAR^ unaryExpression | DIV^ unaryExpression )*
	;

// [54] UnaryExpression ::= '!' PrimaryExpression
// | '+' PrimaryExpression
// | '-' PrimaryExpression
// | PrimaryExpression 

unaryExpression
	: NOT^ primaryExpression
	| PLUS^ primaryExpression
	| MINUS^ primaryExpression
	| primaryExpression
	;

// [55] PrimaryExpression ::= BrackettedExpression | BuiltInCall | IRIrefOrFunction | RDFLiteral | NumericLiteral | BooleanLiteral | BlankNode | Var 
primaryExpression
	: brackettedExpression | builtInCall | var | rdfLiteral | numericLiteral | Q_IRI_REF
	;
	
// [56] BrackettedExpression ::= '(' Expression ')'
brackettedExpression
	: OPEN! expression CLOSE!
	;

//[57] BuiltInCall ::= 'STR' '(' Expression ')'
//| 'LANG' '(' Expression ')'
//| 'LANGMATCHES' '(' Expression ',' Expression ')'
//| 'DATATYPE' '(' Expression ')'
//| 'BOUND' '(' Var ')'
//| 'isIRI' '(' Expression ')'
//| 'isURI' '(' Expression ')'
//| 'isBLANK' '(' Expression ')'
//| 'isLITERAL' '(' Expression ')'
//| RegexExpression 

builtInCall
	: BOUND^ OPEN! var CLOSE!
	;

// [60] RDFLiteral ::= String ( LANGTAG | ( '^^' IRIref ) )? 
rdfLiteral
	: string
	;

// [61] NumericLiteral ::= INTEGER | DECIMAL | DOUBLE 
numericLiteral
	: INTEGER
	;

// [63] String ::= STRING_LITERAL1 | STRING_LITERAL2 | STRING_LITERAL_LONG1 | STRING_LITERAL_LONG2 
string
	: STRING_LITERAL1
	;

// [64] IRIref ::= Q_IRI_REF | QName 
iriRef
	: Q_IRI_REF | q:QNAME 
	;





/**
 * SPARQL Lexer
 * <br>
 * This lexer provides the SPARQL parser with tokens
 * @author Stephane Jean
 */
class SPARQLBaseLexer extends Lexer;

options {
	exportVocab=SPARQL;      // call the vocabulary "SPARQL"
	k=2;
	caseSensitive = false;
	caseSensitiveLiterals = false;
	charVocabulary='\u0000'..'\uFFFE' ;
}

// -- Keywords --
OPEN_CURLY: "{";
CLOSE_CURLY: "}";
OPEN: "(";
CLOSE: ")";
DOT: ".";
OR: "||";
AND: "&&";
PLUS: '+';
MINUS: '-';
STAR: '*';
DIV: '/';
EQ: '=';
LT: '<';
GT: '>';
NE: "!=";
NOT: "!";
LE: "<=";
GE: ">=";

protected
UNDERSCORE: '_';

protected
COLON: ":";

// Prefix of variable
protected
QUESTION_MARK: "?"; 
protected
DOLLAR: "$";

// [74] INTEGER ::= [0-9]+ 
INTEGER
	: ('0'..'9')+
	;

// [78] STRING_LITERAL1 ::= "'" ( ([^#x27#x5C#xA#xD]) | ECHAR | UCHAR )* "'" 
STRING_LITERAL1
	  : '\'' ( (ESCqs)=> ESCqs | ~'\'' )* '\''
	;
protected
ESCqs
	:
		'\'' '\''
	;

// [67] Q_IRI_REF ::= '<' ([^<>'{}|^`]-[#x00-#x20])* '>' 
Q_IRI_REF
    : LT! ( options {greedy=false;} : ~('<' | '>' | ' ' | '"' | '=' | '{' | '}' | '|' | '^' | '\\' | '`'))+ GT!
    ;
	
// [69] QNAME ::= NCNAME_PREFIX? ':' NCNAME? 
QNAME options {testLiterals=true; }
	: ( (NCNAME_PREFIX)? COLON ) =>   
        (NCNAME_PREFIX)? COLON ( options {greedy=true;} : NCNAME )? 
	// Keywork
    | ("prefix")=>"prefix" {$setType(PREFIX);}
    | ("distinct")=>"distinct" {$setType(DISTINCT);}
    | ("optional")=>"optional" {$setType(OPTIONAL);}
    | ("bound")=>"bound" {$setType(BOUND);}
    | ("filter")=>"filter" {$setType(FILTER);}
    | ("order")=>"order" {$setType(ORDER);}
    | ("by")=>"by" 
    | ("desc")=>"desc" 
    | ("asc")=>"asc" 
    | ("union")=>"union" {$setType(UNION);}
    | ("select")=>"select" {$setType(SELECT);}
    | ("where")=>"where" {$setType(WHERE);}
	;

// [92] NCNAME_PREFIX ::= NCCHAR1p ((NCCHAR|'.')* NCCHAR)?
protected
NCNAME_PREFIX
	: NCCHAR1p ((NCCHAR|DOT)* NCCHAR)? {
		String originalText= $getText;
		if (!originalText.startsWith("rdf"))
			setText("geo");
	}
	;

// [93] NCNAME ::= NCCHAR1 ((NCCHAR|'.')* NCCHAR)? 
protected
NCNAME
	: NCCHAR1 ((NCCHAR|DOT)* NCCHAR)? 
	;

// [91] NCCHAR ::= NCCHAR1 | '-' | [0-9] | #x00B7 | [#x0300-#x036F] | [#x203F-#x2040]
protected
NCCHAR
	: NCCHAR1
	| ( '0' .. '9' )
	;
	
// [44] Var ::= VAR1 | VAR2
// [71] VAR1 ::= '?' VARNAME 
// [72] VAR2 ::= '$' VARNAME 
VAR 
	: (QUESTION_MARK|DOLLAR) (VARNAME)
    ;

// [90] VARNAME ::= ( NCCHAR1 | [0-9] ) ( NCCHAR1 | [0-9] | #x00B7 | [#x0300-#x036F] | [#x203F-#x2040] )* 
protected
VARNAME
	: (NCCHAR1 | ( '0' .. '9' ))+
	;

// [89] NCCHAR1 ::= NCCHAR1p | '_' 
protected
NCCHAR1
	: NCCHAR1p | UNDERSCORE 
	;

// [88] NCCHAR1p ::= [A-Z]
//| [a-z]
//| [#x00C0-#x00D6]
//| [#x00D8-#x00F6]
//| [#x00F8-#x02FF]
//| [#x0370-#x037D]
//| [#x037F-#x1FFF]
//| [#x200C-#x200D]
//| [#x2070-#x218F]
//| [#x2C00-#x2FEF]
//| [#x3001-#xD7FF]
//| [#xF900-#xFDCF]
//| [#xFDF0-#xFFFD]
//| [#x10000-#xEFFFF]
//| UCHAR
protected
NCCHAR1p
	: ( 'a'..'z' ) 
	;
	
WS  :   (   ' '
		|   '\t'
		|   '\r' '\n' { newline(); }
		|   '\n'      { newline(); }
		|   '\r'      { newline(); }
		)
		{$setType(Token.SKIP);} //ignore this token
	;
	



