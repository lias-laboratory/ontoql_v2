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

import fr.ensma.lias.mariusql.engine.util.ASTUtil;

}

/**
 * @author Stephane JEAN 
 * @author Mickael BARON
 * @author Geraud FOKOU
 */ 
 class MariusQLBaseParser extends Parser;

options
{
    exportVocab=MariusQL;
    buildAST=true;
    k=3; // For 'not like', 'not in', etc.
}

tokens
{
    // -- Marius Keyword tokens --
    ADD="add";
    ALL="all";
    ALTER="alter";
    AND="and";
    ANY="any";
    ARRAY="array";
    AS="as";
    ASCENDING="asc";
    ATTRIBUTE="attribute";
    AVG="avg";
    BETWEEN="between";
    BOOLEAN="boolean";
    REAL="real";
    ENUM="enum";
    CASE="case";
    COUNT="count";
    CREATE="create";
    CROSS="cross";
    DELETE="delete";
    DESCENDING="desc";
    DESCRIPTOR="descriptor";
    DISTINCT="distinct";
    DROP="drop";
    ELSE="else";
    END="end";
    ENTITY="entity";
    EXCEPT="except";
    EXISTS="exists";
    EXTENT="extent";
    FALSE="false";
    FROM="from";
    FULL="full";
    GROUP="group";
    HAVING="having";
    IN="in";
    INNER="inner";
    INSERT="insert";
    INT="int";
    INTERSECT="intersect";
    INTO="into";    
    IS="is";
    JOIN="join";
    LANGUAGE="language";    
    LEFT="left";
    LIKE="like";
    MAX="max";
    MIN="min";
    MULTILINGUAL="multilingual";
    NAMESPACE="namespace";  
    NATURAL="natural";
    NONE="none";
    NOT="not";  
    NULL="null";
    OF="of";
    ON="on";
    ONLY="only";
    OR="or";
    ORDER="order";
    OUTER="outer";
    PREFERRING="preferring";
    PROPERTY="property";
    REF="ref";
    RIGHT="right";
    SELECT="select";
    SET="set";
    STRING="string";
    SUM="sum";
    TARRAYINT="tarrayint";
    TARRAYSTRING="tarraystring";
    TARRAYDOUBLE="tarraydouble";
    TARRAYBOOLEAN="tarrayboolean";
    THEN="then";
    TRUE="true";
    TYPEOF="typeof";
    UNDER="under";
    UNION="union";
    UNION_ALL="union all";
    UNNEST="unnest";
    UPDATE="update";
    USING="using";
    VALUES="values";
    VIEW="view";
    WHEN="when";
    WHERE="where";
    URI="uriType";
    LIMIT="limit";
    OFFSET="offset";
    COUNTTYPE="countType";
    
    // Keywords for procedural feature.
    OPERATION="operation";
    IMPLEMENTATION="implementation";
    INPUT="input";
    OUTPUT="output";
    IMPLEMENTS="implements";
    DEFAULT_IMPLEMENTATION="default_implementation";
    FOR="for";
    
    // Keywords for relaxation feature.
    APPROX="approx";
    PRED="pred";
    GEN="gen";
    SIB="sib";
    TOP="top";
    FUZZY="fuzzy";
    NOT_FUZZY;
    
    // Keywords for a posteriori case of
    CASEOF="caseof";
    MAP="map";

    // A corriger
    CONTEXT="context";
    PROPERTIES="properties";
    CLASS="class";
    
    // -- Synthetic token types --
    AGGREGATE;      // One of the aggregate functions (e.g. min, max, avg)
    ALIAS;
    DOT;
    NAMESPACE_ALIAS;
    ROW_STAR;
    EXPR_LIST;
    IN_LIST;
    LANGUE_OP;
    INDEX_OP;
    IS_NOT_NULL;
    IS_NULL;            // Unary 'is null' operator.
    IS_NOT_OF;
    IS_OF;          // Unary 'is null' operator.
    METHOD_CALL;
    NOT_BETWEEN;
    CASE2;
    NOT_IN;
    NOT_LIKE;
    ORDER_ELEMENT;
    QUERY;
    RANGE;
    PROPERTY_DEF;
    ATTRIBUTE_DEF;
    ATTRIBUTES;
    SELECT_FROM;
    UNARY_MINUS;
    UNARY_PLUS;
    VECTOR_EXPR;        // ( x, y, z )
    IDENT;
    DATATYPE;
    PREDEFINED_TYPE;
    ARRAY_DEF;
    MAPPED_PROPERTIES;
    
    // Literal tokens.
    CONSTANT;
    NUM_DOUBLE;
    NUM_FLOAT;
    NUM_LONG;
}

{    
    /**
     * Returns the negated equivalent of the expression.
     * @param x The expression to negate.
     */
    public AST negateNode(AST x) {
        // Just create a 'not' parent for the default behavior.
        return ASTUtil.createParent(astFactory, NOT, "not", x);
    }

    /**
     * Returns the 'cleaned up' version of a comparison operator sub-tree.
     * @param x The comparison operator to clean up.
     */
    public AST processEqualityExpression(AST x) throws RecognitionException {
        return x;
    }
    
}

statement
    : ( ddlStatement | queryExpression | insertStatement | deleteStatement | updateStatement)
    ;

ddlStatement
    : (createStatement | alterStatement | dropStatement | parameterStatement)
    ;

createStatement
    : CREATE^ (ddlExtent | viewDefinition | ontologyDefinition | entityDefinition | mBehaviorDefinition)
    ;

mBehaviorDefinition
    : mOperationDefinition
    | mImplementationDefinition
    ;
    
mOperationDefinition
    : OPERATION^ identifier (inputClause)? (outputClause)?
    ;
       
inputClause
    : INPUT^ OPEN! datatype (COMMA! datatype)* CLOSE!
    ;    
    
outputClause
    : OUTPUT^ OPEN! datatype CLOSE!
    ;
    
mImplementationDefinition
	: IMPLEMENTATION^ identifier implementOperationClause descriptorClause
	;   
    
viewDefinition
    : VIEW^ (identifier)? (typedClause)? AS! OPEN! selectStatement CLOSE!
    ;

typedClause
    : OF^ identifier
    ;

entityDefinition
    : ENTITY^ ONTOLOGY_MODEL_ID (subEntityClause)? (entityElementList)?
    ;

subEntityClause
    : UNDER^ ONTOLOGY_MODEL_ID
    ;

entityElementList
    : OPEN! a:attributeDefinition (COMMA! attributeDefinition)* CLOSE! {
        #entityElementList = #([ATTRIBUTES, "ATTRIBUTES"], #a);
    }
    ;

attributeDefinition!
    : a:ONTOLOGY_MODEL_ID d:datatype {
        #attributeDefinition = #([ATTRIBUTE_DEF, "ATTRIBUTE_DEF"], #a, #d);
    }
    ;
               
ddlExtent
    : EXTENT^ OF! identifier OPEN! propIdentifier(COMMA! propIdentifier)* CLOSE!
    ;

ontologyDefinition
    : ontologyDefinitionHead (ontologyDefinitionBody)?
    ;
    
ontologyDefinitionHead
    : ONTOLOGY_MODEL_ID^ identifier (implementOperationClause)? (optionalHeadClause)?
    ;

implementOperationClause
	: IMPLEMENTS^ identifier
	;

ontologyDefinitionBody
    : OPEN! (descriptorClause)? (classOrOperationClauses)? CLOSE! 
    | aPosterioriCaseOfDefinitionBody
    ;
    
classOrOperationClauses
    : propertiesClause
    | operationClauses
    ;    
    
operationClauses
	: inputClause (outputClause)?
    | outputClause
    ;	
    
propertiesClause
    : PROPERTIES^ OPEN! propertyDefinition (COMMA! propertyDefinition)* CLOSE!
    ;         
                
aPosterioriCaseOfDefinitionBody
    : caseOfClause (mappedPropertiesClause)?
    ;

caseOfClause
    : CASEOF^ identifier 
    ;
           
mappedPropertiesClause
    : OPEN! l:listOfMapProperty CLOSE! {
        #mappedPropertiesClause = #([MAPPED_PROPERTIES, "MAPPED_PROPERTIES"], #l);
    }
    ;

listOfMapProperty
    : mapProperty (COMMA! mapProperty)*
    ;

mapProperty
    : propIdentifier MAP^ propIdentifier
    ;

optionalHeadClause
    :   
    (UNDER^ | CONTEXT^) identifier
    ;

descriptorClause
    : DESCRIPTOR^ OPEN! assignment (COMMA! assignment)* CLOSE!
    ;

propertyDefinition!
    : p:propIdentifier d:datatype (desc:descriptorClause)? {
        #propertyDefinition = #([PROPERTY_DEF, "PROPERTY_DEF"], #p, #d, #desc);
    }
    ;


// Yet, I have not resolved how to nest collectionType without
// leading to an infinite recursion of antlr
datatype
    : (predefinedType | referenceType) (a:ARRAY^ {#a.setType(ARRAY_DEF);})?
    ; 
    
predefinedType
    : ( (MULTILINGUAL)? STRING^ 
    | INT
    | COUNTTYPE
    | URI
    | REAL
    | ENUM^ inList
    | BOOLEAN ) {#predefinedType.setType(PREDEFINED_TYPE);}
    ;

referenceType
    : (REF^ OPEN! identifier CLOSE! )
    ;

alterStatement
    : ALTER^ (alterEntity | alterClass | alterExtent)
    ;

alterEntity
    : ENTITY^ ONTOLOGY_MODEL_ID alterEntityAction
    ;

alterEntityAction
    : addAttributeDefinition
    | dropAttribute
    ;

addAttributeDefinition
    : ADD (ATTRIBUTE!)? attributeDefinition
    ;

dropAttribute
    : DROP (ATTRIBUTE!)? ONTOLOGY_MODEL_ID
    ;

alterExtent
    : EXTENT^ OF! classIdentifier alterExtentAction
    ;

alterExtentAction
    : addPropertyExtent
    | dropPropertyExtent
    ;
    
addPropertyExtent
	: ADD propIdentifier 
	;
	
dropPropertyExtent
	: DROP propIdentifier
	;
	
alterClass
    : ONTOLOGY_MODEL_ID^ classIdentifier alterClassAction
    ;

alterClassAction
    : addPropertyDefinition
    | dropPropertyDefinition
    ;

addPropertyDefinition
    : ADD (PROPERTY!)? propertyDefinition
    ;

dropPropertyDefinition
    : DROP (PROPERTY!)? propIdentifier
    ;

dropStatement
    : DROP^ (dropEntity | dropClass | dropExtent)
    ;
    
dropEntity
    : ENTITY^ ONTOLOGY_MODEL_ID
    ;
    
dropClass
    : ONTOLOGY_MODEL_ID^ identifier
    ;
    
dropExtent
	: EXTENT^ OF! identifier 
	;

parameterStatement
    :   SET^ (namepaceSpecification | languageSpecification | defaultImplementation)
    ;

namepaceSpecification
    : NAMESPACE (namespaceAlias | NONE)
    ;

languageSpecification
    : LANGUAGE (languageId | NONE)
    ;
    
defaultImplementation
	: DEFAULT_IMPLEMENTATION^ identifier forOperationClause
	;
	
forOperationClause
	: FOR^ identifier
	;

languageId
    : FR | EN
    ;

updateStatement
    : UPDATE^
        targetClass
        setClause
        (whereClause)?
    ;

setClause
    : (SET^ assignment (COMMA! assignment)*)
    ;

assignment
    : propIdentifier EQ^ (concatenation|datatype)
    ;


insertStatement
    : INSERT^ intoClause (selectStatement|valueClause)
    ;

valueClause 
    : VALUES^ OPEN! concatenation ( COMMA! concatenation )* CLOSE!
    ;

intoClause
    : INTO^ classIdentifier (insertablePropertySpec)?
    ;
    
insertablePropertySpec
    : OPEN! propIdentifier ( COMMA! propIdentifier )* CLOSE! {
        // Just need *something* to distinguish this on the hql-sql.g side
        #insertablePropertySpec = #([RANGE, "property-spec"], #insertablePropertySpec);
    }
    ;

deleteStatement
    : DELETE^ FROM! targetClass
        (whereClause)?
    ;

targetClass!
    : c:classIdentifier {
        AST #range = #([RANGE, "RANGE"], #c);
        #targetClass = #([FROM, "FROM"], #range);
    }
    | o:ONLY OPEN! conly:classIdentifier CLOSE! {
        AST #range = #([RANGE, "RANGE"], #conly, #o);
        #targetClass = #([FROM, "FROM"], #range);
    }
    ;

classIdentifier
    : identifier
    ;

queryExpression
    : queryTerm ((UNION^|EXCEPT^) (ALL|DISTINCT)? queryTerm)*
    ;

queryTerm
    : queryPrimary (INTERSECT^ (ALL|DISTINCT)? queryPrimary)*
    ;

queryPrimary
    : selectStatement
    | OPEN! queryExpression CLOSE!
    ;

selectStatement
    : queryRule 
    ;


queryRule!
    : sf:selectFrom
      (w:whereClause)?
      (g:groupByClause)?
      (o:orderByClause)?
      (l:limitClause)?
      (a:approxClause)?
      (p:preferenceClause)?
      (i:usingImplementationClause)?
      (n:namespaceClause)? {
        #queryRule = #([QUERY,"query"],n,sf,w,g,o,l,a,p,i);
      }
    ;

selectFrom!
    :  (s:selectClause) (f:fromClause)
    {
        // Create an artificial token so the 'FROM' can be placed
        // before the SELECT in the tree to make tree processing
        // simpler.
        #selectFrom = #([SELECT_FROM,"SELECT_FROM"],f,s);
    }
    ;

selectClause
    : SELECT^ (DISTINCT)? selectList
    ;

selectList
    : STAR { #STAR.setType(ROW_STAR); }
    | selectSublist ( COMMA! selectSublist )*
    ;

selectSublist
    : valueExpression ( AS^ identifier )?
    ;

fromClause
    : FROM^ tableReference ( COMMA! tableReference )*
    ;


tableReference
    : t:tablePrimary (j:joinedTable)? 
    ;

tablePrimary
    : fromClass
    | onlySpec
    | collectionDerivedTable 
    ;
    
fromClass!
    :  c:identifier (a:asAlias)?
      { #fromClass = #([RANGE, "RANGE"], #c, null, #a); }
    ;

onlySpec!
    : o:ONLY OPEN! c:identifier CLOSE! (a:asAlias)?
      { #onlySpec = #([RANGE, "RANGE"], #c, #o, #a); }
    ;

collectionDerivedTable
    : UNNEST^ OPEN! collectionValueExpression CLOSE! asAlias
    ;

// todo allow concatenation of collectionValue
collectionValueExpression
    : valueExpression
    ;

// Alias rule - Parses the optional 'as' token and forces an AST identifier node.
asAlias
    : (AS!)? alias
    ;

alias
    : a:identifier { #a.setType(ALIAS); }
    ;

joinedTable
    : crossJoin
    | qualifiedJoin
    | naturalJoin
    ;

crossJoin
    : CROSS JOIN tableReference
    ;

qualifiedJoin 
    : (joinType)? JOIN^ tableReference joinCondition {
    
    }
    ;

joinCondition
    : ON^ logicalExpression
    ;

naturalJoin 
    : NATURAL (joinType)? JOIN^ tableReference
    ;

joinType
    : INNER
    | outerJoinType (OUTER)?
    ;

outerJoinType
    : LEFT
    | RIGHT
    | FULL
    ;

    
//## groupByClause:
//##     GROUP_BY path ( COMMA path )*;

groupByClause
    : GROUP^ 
        "by"! valueExpression ( COMMA! valueExpression )*
        (havingClause)?
    ;

//## orderByClause:
//##     ORDER_BY selectedPropertiesList;

orderByClause
    : ORDER^ "by"! orderElement ( COMMA! orderElement )*
    ;

limitClause
    : LIMIT^ NUM_INT (OFFSET NUM_INT)?
    ;

approxClause
    : APPROX^ approxExpression (numberAnswer)?
    ;

approxExpression
    :PRED^ OPEN! path CLOSE!
    |GEN^  OPEN! classIdentifier (COMMA! classIdentifier)? (COMMA! NUM_INT)? CLOSE!
    |SIB^  OPEN! classIdentifier (COMMA! OPEN_BRACKET! (classIdentifier (COMMA! classIdentifier)*) CLOSE_BRACKET!)? CLOSE!
    ;

numberAnswer
    :TOP^ NUM_INT
    ;

preferenceClause
    : PREFERRING^ identifier
    ;
    
usingImplementationClause
	: USING! IMPLEMENTATION^ implementationForOperation (COMMA! implementationForOperation)*
	;
	
implementationForOperation
	: identifier ARROW! identifier
	;

namespaceClause
    : USING! NAMESPACE^ namespaceAlias (COMMA! namespaceAlias)*
    ;

namespaceAlias
    : (NAME_ID EQ!)? QUOTED_STRING^
    ;

orderElement
    : valueExpression ( ascendingOrDescending )?
    ;

ascendingOrDescending
    : ( "asc" | "ascending" )   { #ascendingOrDescending.setType(ASCENDING); }
    | ( "desc" | "descending")  { #ascendingOrDescending.setType(DESCENDING); }
    ;

//## havingClause:
//##     HAVING logicalExpression;

havingClause
    : HAVING^ logicalExpression
    ;

//## whereClause:
//##     WHERE logicalExpression;

whereClause
    : WHERE^ logicalExpression
    ;



// expressions
// Note that most of these expressions follow the pattern
//   thisLevelExpression :
//       nextHigherPrecedenceExpression
//           (OPERATOR nextHigherPrecedenceExpression)*
// which is a standard recursive definition for a parsing an expression.
//
// Operator precedence in Marius
// lowest  --> ( 7)  OR
//             ( 6)  AND, NOT
//             ( 5)  equality: ==, <>, !=, is
//             ( 4)  relational: <, <=, >, >=,
//                   LIKE, NOT LIKE, BETWEEN, NOT BETWEEN, IN, NOT IN
//             ( 3)  addition and subtraction: +(binary) -(binary)
//             ( 2)  multiplication: * / %, concatenate: ||
// highest --> ( 1)  +(unary) -(unary)
//                   aggregate function
//                   ()  (explicit parenthesis)
//

logicalExpression
    : valueExpression
    ;

// Main expression rule
valueExpression
    : logicalOrExpression
    ;

// level 7 - OR
logicalOrExpression
    : logicalAndExpression ( OR^ logicalAndExpression )*
    ;

// level 6 - AND, NOT
logicalAndExpression
    : negatedExpression ( AND^ negatedExpression )*
    ;

// NOT nodes aren't generated.  Instead, the operator in the sub-tree will be
// negated, if possible.   Expressions without a NOT parent are passed through.
negatedExpression!
    : NOT^ x:negatedExpression { #negatedExpression = negateNode(#x); }
    | y:equalityExpression { #negatedExpression = #y; }
    ;

//## OP: EQ | LT | GT | LE | GE | NE | SQL_NE | LIKE;

// level 5 - EQ, NE
equalityExpression
    : x:relationalExpression (
        ( EQ^
        | is:IS^    { #is.setType(EQ); } (NOT! { #is.setType(NE); } )? (OF)?
        | NE^
        | ne:SQL_NE^    { #ne.setType(NE); }
        ) y:relationalExpression)* {
            // Post process the equality expression to clean up 'is null', etc.
            #equalityExpression = processEqualityExpression(#equalityExpression);
        } 
    ;

// level 4 - LT, GT, LE, GE, LIKE, NOT LIKE, BETWEEN, NOT BETWEEN, FUZZY NOT FUZZY
// NOTE: The NOT prefix for LIKE and BETWEEN will be represented in the
// token type.  When traversing the AST, use the token type, and not the
// token text to interpret the semantics of these nodes.
relationalExpression
    : concatenation (
        ( ( ( LT^ | GT^ | LE^ | GE^ ) additiveExpression )* )
        // Disable node production for the optional 'not'.
        | (n:NOT!)? (
            // Represent the optional NOT prefix using the token type by
            // testing 'n' and setting the token type accordingly.
            (i:IN^ {
                    #i.setType( (n == null) ? IN : NOT_IN);
                    #i.setText( (n == null) ? "in" : "not in");
                }
                inList)
            | (b:BETWEEN^ {
                    #b.setType( (n == null) ? BETWEEN : NOT_BETWEEN);
                    #b.setText( (n == null) ? "between" : "not between");
                }
                betweenList )
            | (l:LIKE^ {
                    #l.setType( (n == null) ? LIKE : NOT_LIKE);
                    #l.setText( (n == null) ? "like" : "not like");
                }
                concatenation)
             )
        )
    ;

inList
    : x:compoundExpr
    { #inList = #([IN_LIST,"inList"], #inList); }
    ;

betweenList
    : concatenation AND! concatenation
    ;

//level 4 - string concatenation
concatenation
    : additiveExpression 
    ( c:CONCAT^ { #c.setType(EXPR_LIST); #c.setText("concatList"); } 
      additiveExpression
      ( CONCAT! additiveExpression )* 
      { #concatenation = #([METHOD_CALL, "||"], #([IDENT, "concat"]), #c ); } )?
    ;

// level 3 - binary plus and minus
additiveExpression
    : multiplyExpression ( ( PLUS^ | MINUS^ ) multiplyExpression )*
    ;

// level 2 - binary multiply and divide
multiplyExpression
    : unaryExpression ( ( STAR^ | DIV^ ) unaryExpression )*
    ;
    
// level 1 - unary minus, unary plus, not
unaryExpression
    : MINUS^ {#MINUS.setType(UNARY_MINUS);} unaryExpression
    | PLUS^ {#PLUS.setType(UNARY_PLUS);} unaryExpression
    | caseExpression
    | quantifiedExpression
    | primaryExpression
    | fuzzyExpression
    ;
    
fuzzyExpression
    : FUZZY^ OPEN! path COMMA! fuzzyPredicat CLOSE! 
    ;
    
fuzzyPredicat
    : OPEN_BRACKET! constant COMMA! constant COMMA! constant COMMA! constant CLOSE_BRACKET!
    ;   

caseExpression
    : CASE^ (whenClause)+ (elseClause)? END! 
    | CASE^ { #CASE.setType(CASE2); } unaryExpression (altWhenClause)+ (elseClause)? END!
    ;
    
whenClause
    : (WHEN^ logicalExpression THEN! unaryExpression)
    ;
    
altWhenClause
    : (WHEN^ unaryExpression THEN! unaryExpression)
    ;
    
elseClause
    : (ELSE^ unaryExpression)
    ;   
    
quantifiedExpression
    : ( EXISTS^ | ALL^ ) OPEN! ( subQuery ) CLOSE!
    | (ANY^) OPEN! ( subQuery | propIdentifier ) CLOSE!
    ;


// level 0 - the basic element of an expression
primaryExpression
    :   identPrimary 
    |   constant
    |   OPEN! ( ( (ONLY)? (valueExpression | datatype) (COMMA! (ONLY)? (valueExpression | datatype))* ) | subQuery) CLOSE!
    |   arrayValueExpression
    |	arrayOracleExpression
    ;

arrayOracleExpression
	: oracleArrayValueConstructorByEnumeration
	;
	
	
arrayValueExpression 
    : arrayValueConstructorByEnumeration
    | arrayValueConstructorByQuery
    ;

oracleArrayValueConstructorByEnumeration
	: TARRAYINT^ OPEN! (valueExpression (COMMA! valueExpression)*) CLOSE!
	| TARRAYSTRING^ OPEN! (valueExpression (COMMA! valueExpression)*) CLOSE!
	| TARRAYDOUBLE^ OPEN! (valueExpression (COMMA! valueExpression)*) CLOSE!
	;
arrayValueConstructorByEnumeration
    : ARRAY^ OPEN_BRACKET! (valueExpression (COMMA! valueExpression)*) CLOSE_BRACKET!
    ;

arrayValueConstructorByQuery
    : ARRAY^ OPEN! subQuery CLOSE!
    ;

// identifier, followed by (dot ident), or method calls.
identPrimary
    : propIdentifier 
            ( options { greedy=true; } : DOT^ path )?
            ( options { greedy=true; } :
                ( op:OPEN^ { #op.setType(METHOD_CALL);} exprList CLOSE! (DOT^ path)? )
            )?
    // Also allow special 'aggregate functions' such as count(), avg(), etc.
    | aggregate
    ;

exprList
    : valueExpression ((COMMA! valueExpression)* | AS! (datatype | identifier))
    { #exprList = #([EXPR_LIST,"exprList"], #exprList); }
    ;


//## aggregate:
//##     ( aggregateFunction OPEN path CLOSE ) | ( COUNT OPEN STAR CLOSE ) | ( COUNT OPEN (DISTINCT | ALL) path CLOSE );

//## aggregateFunction:
//##     COUNT | 'sum' | 'avg' | 'max' | 'min';

aggregate
    : ( SUM^ | AVG^ | MAX^ | MIN^ ) OPEN! additiveExpression CLOSE! { #aggregate.setType(AGGREGATE); }
    // Special case for count - It's 'parameters' can be keywords.
    |  COUNT^ OPEN! ( STAR { #STAR.setType(ROW_STAR); }  | ( DISTINCT )? path ) CLOSE!
    ;

                                           
compoundExpr
    :   (OPEN! ( (valueExpression (COMMA! valueExpression)*) | subQuery ) CLOSE!)
    ;

subQuery
    : queryRule
    ;

constant
    : NUM_INT
    | NUM_FLOAT
    | NUM_LONG
    | NUM_DOUBLE
    | QUOTED_STRING
    | NULL
    | TRUE
    | FALSE
    ;

//##propertyPath
//##    : (i:identifier COLON^)? path 
//##    ;

path
    :  propIdentifier  ( DOT^ path )?
    ;

propIdentifier
    : i:identifier (lb:OPEN_BRACKET^ (NAME_ID {#lb.setType(LANGUE_OP);} | NUM_INT {#lb.setType(INDEX_OP);} )  CLOSE_BRACKET!)?
    ;
    
identifier
    : (n:NAME_ID {#n.setType(NAMESPACE_ALIAS);} COLON!)?(NAME_ID^
    | INTERNAL_ID^
    | EXTERNAL_ID^
    | ONTOLOGY_MODEL_ID^)
     {
        #identifier.setType(IDENT);
    }
    ;

/**
 * Marius Lexer
 * <br>
 * This lexer provides the Marius parser with tokens when a reference language is defined
 * @author Stephane Jean
 */
class MariusQLBaseLexer extends Lexer;

options {
    exportVocab=MariusQL;      // call the vocabulary "Marius"
    testLiterals = false;
    k=3; // needed for fr, en and to distinguish with identifier.
    charVocabulary='\u0000'..'\uFFFE';  // Allow any char 
    caseSensitive = false;
    caseSensitiveLiterals = false;
}


// -- Keywords --

EQ: '=';
LT: '<';
GT: '>';
SQL_NE: "<>";
NE: "!=" | "^=";
LE: "<=";
GE: ">=";

COMMA: ',';

ARROW: "->";

OPEN: '(';
CLOSE: ')';
OPEN_BRACKET: '[';
CLOSE_BRACKET: ']';

PLUS: '+';
MINUS: '-';
STAR: '*';
DIV: '/';
COLON: ":";
CONCAT: "||";

protected
LETTER
    :	'a'..'z'
    ;
       
protected
ID_START_LETTER
    :    '_'
    |    'a'..'z'
    |    '\u0080'..'\ufffe'  //  Allow unicode chars in identifiers
    ;

protected
ID_LETTER
    :    ID_START_LETTER
    |    '$'
    |    '0'..'9'
    ;

INTERNAL_ID
    : '!' ('0'..'9')+
    ;

EXTERNAL_ID
    : '@' ( 'a' .. 'z' | '0' .. '9' | '_' | '$' )+ ( options {greedy=true;} : MINUS ('0' .. '9')*)?
    ;

NAME_ID options { testLiterals=true; }
    : ID_START_LETTER ( ID_LETTER )*
    | DOUBLE_QUOTED_STRING
    ;
    
ONTOLOGY_MODEL_ID
    : '#' NAME_ID
    ;
    
protected
DOUBLE_QUOTED_STRING
      : '\"' ( (ESCdqs)=> ESCdqs | ~'\"' )* '\"'
    ;

protected
ESCdqs
    :
        '\'' '\''
    ;

QUOTED_STRING
      : '\'' ( (ESCqs)=> ESCqs | ~'\'' )* '\''
    ;

protected
ESCqs
    :
        '\'' '\''
    ;

WS  :   (   ' '
        |   '\t'
        |   '\r' '\n' { newline(); }
        |   '\n'      { newline(); }
        |   '\r'      { newline(); }
        )
        {$setType(Token.SKIP);} //ignore this token
    ;

//--- From the Java example grammar ---
// a numeric literal
NUM_INT
    {boolean isDecimal=false; Token t=null;}
    :   '.' {_ttype = DOT;}
            (   ('0'..'9')+ (EXPONENT)? (f1:FLOAT_SUFFIX {t=f1;})?
                {
                    if (t != null && t.getText().toUpperCase().indexOf('F')>=0)
                    {
                        _ttype = NUM_FLOAT;
                    }
                    else
                    {
                        _ttype = NUM_DOUBLE; // assume double
                    }
                }
            )?
    |   (   '0' {isDecimal = true;} // special case for just '0'
            (   ('x')
                (                                           // hex
                    // the 'e'|'E' and float suffix stuff look
                    // like hex digits, hence the (...)+ doesn't
                    // know when to stop: ambig.  ANTLR resolves
                    // it correctly by matching immediately.  It
                    // is therefore ok to hush warning.
                    options { warnWhenFollowAmbig=false; }
                :   HEX_DIGIT
                )+
            |   ('0'..'7')+                                 // octal
            )?
        |   ('1'..'9') ('0'..'9')*  {isDecimal=true;}       // non-zero decimal
        )
        (   ('l') { _ttype = NUM_LONG; }

        // only check to see if it's a float if looks like decimal so far
        |   {isDecimal}?
            (   '.' ('0'..'9')* (EXPONENT)? (f2:FLOAT_SUFFIX {t=f2;})?
            |   EXPONENT (f3:FLOAT_SUFFIX {t=f3;})?
            |   f4:FLOAT_SUFFIX {t=f4;}
            )
            {
                if (t != null && t.getText().toUpperCase() .indexOf('F') >= 0)
                {
                    _ttype = NUM_FLOAT;
                }
                else
                {
                    _ttype = NUM_DOUBLE; // assume double
                }
            }
        )?
    ;

// hexadecimal digit (again, note it's protected!)
protected
HEX_DIGIT
    :   ('0'..'9'|'a'..'f')
    ;

// a couple protected methods to assist in matching floating point numbers
protected
EXPONENT
    :   ('e') ('+'|'-')? ('0'..'9')+
    ;

protected
FLOAT_SUFFIX
    :   'f'|'d'
    ;

