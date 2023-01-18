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
import fr.ensma.lias.mariusql.sparql.CategorySPARQL;

}

/**
 * OntoQL Query tree to SQL Query tree  Transform.<br>
 * 
 * @author Stephane JEAN 
 */
class SPARQLMariusQLBaseWalker extends TreeParser;

options
{
	importVocab=SPARQL;        // import definitions from "sparql"
	buildAST=true;
}

{

	/** current operator */
	protected int tmpOperator = INNER;
	
	/** operator between classes */
	protected int operator;
	
	/** previous class identified in the query * */
	protected CategorySPARQL previousCategory = null;

	/** current class identified in the query * */
	protected CategorySPARQL currentCategory = null;
	
	/** true if the SPARQL query uses the UNION operator. **/
	protected boolean inUnionQuery = false;
	
	/** the where clause of the translated OntoQL query */
	protected AST whereClause;

	/** Add a class or an entity in the query **/
	protected void addClassOrEntity(AST nodeClass, AST nodeVar) {}

	/** Add a property to the current class defined in the query **/
	protected void addDescriptionOrConstraint(AST nodeScope,AST nodeDescription, AST nodeVar, AST nodeURI) {}

 	protected void addPropertyorAttribute(AST nodeScope, AST nodeDescription,
			AST nodeVar) { }

	/** Get the from clause of the OntoQL query **/
	protected AST getFromClause () {return null;}
	
	/** Add a predicate to the query (i.e, an OntoSelect operator). **/
	protected void addOntoSelect (AST node) {};
	
	/** Set the current operator **/
	protected void setTmpOperator (int operator) {
		tmpOperator = operator;
	}
	
	/** Set the operator between classes **/
	protected void setOperator () {
		operator = tmpOperator;
	}
	
	/** Translate a variable of the SELECT clause into a property **/
	protected AST resolveSelectElement(AST node, boolean aliasNeeded){ return null; };
	
	/** True if this query need a distinct */
	protected boolean needDistinct = false;
	
	protected void reinitTranslator() {}
	
	protected void postProcessSPARQLQuery() {}
	
	protected void addJoin(CategorySPARQL categoryToJoin, CategorySPARQL categoryJoined) {}
}

unionSparqlQuery
	: #(UNION {inUnionQuery=true;} unionSparqlQuery {reinitTranslator();} sparqlQuery) 
	| sparqlQuery
	;

// The main query rule.
sparqlQuery!
	: #( QUERY 
			 (n:namespaceClause)?
			 #(SELECT_WHERE
				w:whereClause
			    s:selectClause
			    (o:orderByClause)?
			)
		) {
		// todo I am obliged to encode the number corresponding to the token QUERY
		// because ANTLR recognize QUERY as a node.
		#sparqlQuery = #(astFactory.create(130, "QUERY"), #n, #([SELECT_FROM, "SELECT_FROM"], #w, #s), whereClause ,#o);
		postProcessSPARQLQuery(); 
	}
	;


namespaceClause!
	: #(PREFIX aliasns:QNAME ns:Q_IRI_REF ){
		String alias = #aliasns.getText();
		alias = alias.substring(0, alias.length()-1);
		String namespace = "'"+ #ns.getText()+"'";
		#namespaceClause = #([NAMESPACE,"NAMESPACE"], #([QUOTED_STRING, namespace], [NAME_ID, alias]));
	}
	;

namespaceAlias
	: #(c:QUOTED_STRING (a:NAME_ID)?)
	;


selectClause
	: #(select:SELECT (d:DISTINCT)? s:selectExprList ) {
		if (needDistinct && d == null) {
			#selectClause = #([#select.getType(),"SELECT"],[DISTINCT,"DISTINCT"], s);
		}
	}
	;

selectExprList 
	: ( s:selectExpr)+
	;

selectExpr!
	: v:VAR {
		#selectExpr = resolveSelectElement(v,true);
	}
	;

whereClause!
	: #(WHERE blocClass (blocClass {addJoin(currentCategory, previousCategory);})*) {
		#whereClause=getFromClause();
	}
	;

blocClass
	:  tripleType (filterOrTriplePropertyOrBlockClass)* 
	;

filterOrTriplePropertyOrBlockClass
	: filter | triplePropertyOrBlockClass
	;

filter
	: #(FILTER l:logicalExpr {addOntoSelect(#l);})
	;

logicalExpr
	: #(AND logicalExpr logicalExpr)
	| #(OR logicalExpr logicalExpr)
	| #(NOT logicalExpr)
	| comparisonExpr
	| #(BOUND e:expr){#logicalExpr=#(astFactory.create(SPARQLTokenTypes.IS_NOT_NULL, "is_not_null"), #e);} 
	;

comparisonExpr
	: #(EQ expr expr)
	| #(NE expr expr)
	| #(LT expr expr)
	| #(GT expr expr)
	| #(LE expr expr)
	| #(GE expr expr)
	;

expr!
	: v:VAR {#expr = resolveSelectElement(v, false);}
	| s:STRING_LITERAL1  {#expr = astFactory.create(SPARQLTokenTypes.QUOTED_STRING, s.getText());}
	| i:INTEGER  {#expr = astFactory.create(SPARQLTokenTypes.NUM_INT, i.getText());}
	| a:arithmeticExpr {#expr = #a;}
	| u:Q_IRI_REF {#expr = astFactory.create(SPARQLTokenTypes.QUOTED_STRING, "'"+u.getText()+"'");}
	;

arithmeticExpr
	: #(PLUS expr expr)
	| #(MINUS expr expr)
	| #(DIV expr expr)
	| #(STAR expr expr)
	| #(UNARY_MINUS expr)
	;

orderByClause!
	: #(o:ORDER (a:ASCENDING | d:DESCENDING) v:VAR) {
		#v = resolveSelectElement(v, false);
		AST s = #a==null? #d : #a;
		#orderByClause = #([#o.getType(), "order"],v,s);
	}
	;

tripleType
	: #(TRIPLE_TYPE v:VAR c:QNAME) {
		setOperator();
		addClassOrEntity(#c, #v);
		String prefix = c.getText().startsWith("rdf") ? "#" : "";
		String name = prefix + "oid";
		addPropertyorAttribute(#v, #([#v.getType(), name]), #v);
		if (prefix.equals("")) {
			String nameURI = #v.getText()+"URI";
			addPropertyorAttribute(#v, #([#v.getType(), "URI"]), #([#v.getType(), nameURI]));
		}
	}
	;

triplePropertyOrBlockClass
	: #(OPTIONAL {setTmpOperator(OPTIONAL);} (tripleProperty|blocClass {addJoin(currentCategory, previousCategory);})) 
	| tripleProperty  
	;

tripleProperty
	: #(TRIPLE_PROP scope:VAR p:QNAME (v:VAR | u:constant)) {
		addDescriptionOrConstraint(#scope, #p, #v, #u);
		setTmpOperator(INNER);
	}
	;

constant
	: Q_IRI_REF
	| s:STRING_LITERAL1
	;



