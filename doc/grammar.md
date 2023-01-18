# OntoQL grammar

## Tokens

### Keywords

```
<ABS> ::= "abs"
<ADD> ::= "add"
<ALL> ::= "all"
<ALTER> ::= "alter"
<AND> ::= "and"
<ANY> ::= "any"
<ARRAY> ::= "array"
<AS> ::= "as"
<ASC> ::= "asc"
<ATTRIBUTE> ::= "attribute"
<AVG> ::= "avg"
<BETWEEN> ::= "between"
<BOOLEAN> ::= "boolean"
<CARDINALITY> ::= "cardinality"
<CASE> ::= "case"
<CAST> ::= "cast"
<CHECK> :: "check"
<COALESCE> ::= "coalesce"
<COLUMN> ::= "column"
<CONSTRAINT> ::= "constraint"
<COUNT> ::= "count"
<CREATE> ::= "create"
<CROSS> ::= "cross"
<DATE> ::= "date"
<DELETE> ::= "delete"
<DEREF> ::= "deref"
<DERIVED> ::= "derived"
<DESC> ::= "desc"
<DESCRIPTOR> ::= "descriptor"
<DISTINCT> ::= "distinct"
<DROP> ::= "drop"
<ELSE> ::= "else"
<END> ::= "end"
<ENTITY> ::= "entity"
<ESCAPE> ::= "escape"
<EXCEPT> ::= "except"
<EXISTS> ::= "exists"
<EXP> ::= "exp"
<EXTENT> ::= "extent"
<FALSE> ::= "false"
<FLOAT> ::= "float"
<FLOOR> ::= "floor"
<FOR> ::= "for"
<FOREIGN> ::= "foreign"
<FROM> ::= "from"
<FULL> ::= "full"
<GROUP BY> ::= "group by"
<HAVING> ::= "having"
<IN> ::= "in"
<INNER> ::= "inner"
<INSERT> ::= "insert"
<INT> ::= "int"
<INTEGER> ::= "integer"
<INTERSECT> ::= "intersect"
<INTO> ::= "into"
<IS> ::= "is"
<JOIN> ::= "join"
<KEY> ::= "key"
<LANGUAGE> ::= "language"
<LEFT> ::= "left"
<LIKE> ::= "like"
<LN> ::= "ln"
<LOWER> ::= "lower"
<MAX> ::= "max"
<MIN> ::= "min"
<MOD> ::= "mod"
<MULTILINGUAL> ::= "multilingual"
<NAMESPACE> ::= "namespace"
<NATURAL> ::= "natural"
<NONE> ::= "none"
<NOT> ::= "not"
<NULL> ::= "null"
<NULLIF> ::= "nullif"
<OF> ::= "of"
<ON> ::= "on"
<ONLY> ::= "only"
<OR> ::= "or"
<ORDER BY> ::= "order by"
<OUTER> ::= "outer"
<POWER> ::= "power"
<PRIMARY> ::= "primary"
<PROPERTY> ::= "property"
<REAL> ::= "real"
<REF> ::= "ref"
<REFERENCES> ::= "references"
<RIGHT> ::= "right"
<SELECT> ::= "select"
<SET> ::= "set"
<SIMILAR> ::= "similar"
<SOME> ::= "some"
<SQRT> ::= "sqrt"
<STRING> ::= "string"
<SUBSTRING> ::= "substring"
<SUM> ::= "sum"
<TABLE> ::= "table"
<THEN> ::= "then"
<TREAT> ::= "treat"
<TRUE> ::= "true"
<TYPEOF> ::= "typeof"
<UNDER> ::= "under"
<UNION> ::= "union"
<UNIQUE> ::= "unique"
<UNNEST> ::= "unnest"
<UPDATE> ::= "update"
<UPPER> ::= "upper"
<USING> ::= "using"
<VALUES> ::= "values"
<VARCHAR> ::= "varchar"
<VIEW> ::= "view"
<WHEN> ::= "when"
<WHERE> ::= "where"
```

### Lexical elements

Numbers

```
<unsigned numeric literal> ::= <exact numeric literal> | <approximate numeric literal>

<exact numeric literal> ::= <unsigned integer> [ "." [ <unsigned integer> ] ] | "." <unsigned integer>

<unsigned integer> ::= <digit> { <digit> }

<digit> ::= "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"

<approximate numeric literal> ::= <exact numeric literal> "E" <signed integer>

<signed integer> ::= [ <sign> ] <unsigned integer>

<sign> ::= "+" | "-"
```

Character, date and boolean

```
<general literal> ::= <character string literal> | <date literal> | <boolean literal>

<character string literal> ::= "'" [ <character representation list> ] "'"

<character representation> ::= <nonquote character> | <quote symbol>

<nonquote character> ::= "a" | "b" | "c" | "d" | "e" | "f" | "g" |
"h" | "i" | "j" | "k" | "l" | "m" | "n" | "o" | "p" | "q" | "r" |
"s" | "t" | "u" | "v" | "w" | "x" | "y" | "z"

<quote symbol> ::= "'""'"

<date literal> ::= "DATE" "'" <date value> "'"

<date value> ::= <unsigned integer> "-" <unsigned integer> "-"
                 <unsigned integer>

<boolean literal> ::= "TRUE" | "FALSE"
```

## Identifier

```
<identifier> ::= <identifier start> { <identifier part> }

<identifier part> ::= <identifier start> | <identifier extend>

<identifier start> ::= "_" | <nonquote character>

<identifier extend> ::= "$" | <digit>

<table name> ::= <identifier>

<column name> ::= <identifier>

<constraint name> ::= <identifier>

<alias name> ::= <identifier>

<function name> ::= <identifier>

<class id> ::= <identifier>

<property id> ::= <identifier>

<entity id> ::=   "#" <identifier>

<attribute id> ::= "#" <identifier>

<category id> ::= <table name> | <class id> | <entity id>

<category id polymorph> ::= <table name> | <class id> | "ONLY" "("<class id>")" 
                          | <entity id> | "ONLY" "("<entity id>")"

<description id> ::= <column name> | <property id> | <attribute id>

<namespace id> ::= <identifier>

<namespace alias> ::= <identifier>

<language id> ::= "AA" | "AB" | "AF" | "AM" | "AR" | "AS" | "AY" | "AZ" | "BA" | "BE" | "BG" | "BH" |
 "BI" | "BN" | "BO" | "BR" | "CA" | "CO" | "CS" | "CY" | "DA" | "DE" | "DZ" | "EL" | "EN" | "EO" | "ES" |
 "ET" | "EU" | "FA" | "FI" | "FJ" | "FO" | "FR" | "FY" | "GA" | "GD" | "GL" | "GN" | "GU" | "HA" | "HI" |
 "HR" | "HU" | "HY" | "IA" | "IE" | "IK" | "IN" | "IS" | "IT" | "IW" | "JA" | "JI" | "JW" | "KA" | "KK" | 
 "KL" | "KM" | "KN" | "KO" | "KS" | "KU" | "KY" | "LA" | "LN" | "LO" | "LT" | "LV" | "MG" | "MI" | "MK" |
 "ML" | "MN" | "MO" | "MR" | "MS" | "MT" | "MY" | "NA" | "NE" | "NL" | "NO" | "OC" | "OM" | "OR" | "PA" | 
 "PL" | "PS" | "PT" | "QU" | "RM" | "RN" | "RO" | "RU" | "RW" | "SA" | "SD" | "SG" | "SH" | "SI" | "SK" |
 "SL" | "SM" | "SN" | "SO" | "SQ" | "SR" | "SS" | "ST" | "SU" | "SV" | "SW" | "TA" | "TE" | "TG" | "TH" |
 "TI" | "TK" | "TL" | "TN" | "TO" | "TR" | "TS" | "TT" | "TW" | "UK" | "UR" | "UZ" | "VI" | "VO" | "WO" |
 "XH" | "YO" | "ZH" | "ZU"
```

## Resources

### Datatypes

```
<data type> ::= <predefined type> | <reference type> | <collection type>

<predefined type> ::= <character string type> | <numeric type>  | <boolean type> | <date type>

<character string type> ::= [ "MULTILINGUAL" ] "STRING" [ "(" <integer> ")" ] 
                            | [ "MULTILINGUAL" ] "VARCHAR" "(" <integer> ")"

<numeric type> ::= <exact numeric type> | <approximate numeric type>

<exact numeric type> ::= "INT" | "INTEGER"

<approximate numeric type> ::= "FLOAT" [ "(" <integer> ")" ] |"REAL"

<boolean type> ::= "BOOLEAN"

<date type> ::= "DATE"

<reference type> ::= "REF" "(" <referenced type> ")"

<referenced type> ::= <class id> | <entity id>

<collection type> ::= <data type> "ARRAY" [ "[" <integer> "]" ]
```

### Values

```
<value expression> ::= <numeric value expression> | <string value expression> 
                     | <collection value expression> | <boolean value expression>
```

Numeric values

```
<numeric value expression> ::= <term> | <numeric value expression> "+" <term> 
                             | <numeric value expression> "-" <term>

<term> ::= <factor> | <term> "*" <factor> | <term> "/" <factor>

<factor> ::= [ "-" ] <numeric primary>

<numeric primary> ::= <unsigned numeric literal> | <value expression primary> | <numeric value function>

<numeric value function> ::= <cardinality expression> | <absolute value expression> 
                           | <modulus expression> | <natural logarithm> 
                           | <exponential function> | <power function> 
                           | <square root> | <floor function>

<cardinality expression> ::= "CARDINALITY" "(" <collection value expression> ")"

<absolute value expression> ::= "ABS" "(" <numeric value expression> ")"

<modulus expression> ::= "MOD" "(" <numeric value expression>","
                         <numeric value expression> ")"

<natural logarithm> ::= "LN" "(" <numeric value expression> ")"

<exponential function> ::= "EXP" "(" <numeric value expression> ")"

<power function> ::= "POWER" "(" <numeric value expression>"," 
                     <numeric value expression> ")"

<square root> ::= "SQRT" "(" <numeric value expression> ")"

<floor function> ::= "FLOOR" "(" <numeric value expression> ")"
```

String values

```
<numeric value expression> ::= <term> | <numeric value expression> "+" <term> 
                             | <numeric value expression> "-" <term>

<term> ::= <factor> | <term> "*" <factor> | <term> "/" <factor>

<factor> ::= [ "-" ] <numeric primary>

<numeric primary> ::= <unsigned numeric literal> | <value expression primary> | <numeric value function>

<numeric value function> ::= <cardinality expression> | <absolute value expression> | <modulus expression> 
                           | <natural logarithm> | <exponential function> | <power function> 
                           | <square root> | <floor function>

<cardinality expression> ::= "CARDINALITY" "(" <collection value expression> ")"

<absolute value expression> ::= "ABS" "(" <numeric value expression> ")"

<modulus expression> ::= "MOD" "(" <numeric value expression>","
                         <numeric value expression> ")"

<natural logarithm> ::= "LN" "(" <numeric value expression> ")"

<exponential function> ::= "EXP" "(" <numeric value expression>")"

<power function> ::= "POWER" "(" <numeric value expression>"," 
                     <numeric value expression> ")"

<square root> ::= "SQRT" "(" <numeric value expression> ")"

<floor function> ::= "FLOOR" "(" <numeric value expression> ")"
```

Boolean values

```
<boolean value expression> ::= <boolean term> | <boolean value expression> "OR" <boolean term>

<boolean term> ::= <boolean factor> | <boolean term> "AND" <boolean factor>

<boolean factor> ::= [ "NOT" ] <boolean test>

<boolean test> ::= <predicate> | <boolean predicand>

<predicate> ::= <comparison predicate> | <between predicate>
              | <in predicate> | <like predicate> 
              | <null predicate> | <quantified predicate> 
              | <exists predicate>  | <type predicate>

<comparison predicate> ::= <value expression> <equality op> <value expression>

<equality op> ::= "=" | "<>" | ">" | ">=" | "<" | "<="

<between predicate> ::= <value expression> [ "NOT" ] "BETWEEN" 
                        <value expression> "AND" <value expression>

<in predicate> ::= [ "NOT" ] "IN" <in predicate value>

<in predicate value> ::= <subquery> | "(" <value expression list> ")"

<like predicate> ::= <value expression> [ "NOT" ] "LIKE" <value expression>

<null predicate> ::= <value expression> "IS" [ "NOT" ] "NULL"

<quantified predicate> ::= <value expression> <quantifier op> "(" <sub query> ")"

<quantifier op> ::= "ALL" | "SOME" | "ANY"

<exists predicate> ::= "EXISTS" <subquery>

<type predicate> ::= <value expression> "IS" [ "NOT" ] "OF" "("
<type list> ")"

<type list> ::= [ "ONLY" ] <is of type> { "," [ "ONLY" ] <is of type> }

<is of type> ::= <reference type> | <class id> | <entity id>

<boolean predicand> ::= "(" <boolean value expression> ")" 
                      | <boolean literal> | <value expression primary>
```

Collection values

```
<collection value expression> ::= <array concatenation> | <array primary>

<array concatenation> ::= <collection value expression> "||" <array primary>

<array primary> ::= <value expression primary> | <array value constructor>

<array value constructor> ::= <array value constructor by enumeration> 
                            | <array value constructor by query>

<array value constructor by enumeration> ::= "ARRAY" "[" <value expression list> "]"

<array value constructor by query> ::= "ARRAY" "(" <query expression> ")"
```

Expression producing values

```
<value expression primary> ::= <par value expression> | <nonpar value expression primary>

<par value expression> ::= "(" <value expression> ")"

<nonpar value expression primary> ::= <description reference> | <scalar subquery> 
                                    | <function call> | <aggregate function> 
                                    | <case expression> | <cast specification> 
                                    | <subtype treatment> | <typeof treatment> 
                                    | <reference resolution> | <null specification>

<description reference> ::= [ <identifier> "." ] <qualified description>

<qualified description> ::= <column name> | <property path expression> 
                          | <attribute path expression> | <identifier>

<property path expression> ::= <property id> { "." <property id>}

<attribute path expression> ::= <attribute id> { "." <attribute id> }

<scalar subquery> ::= <subquery>

<function call> ::= <function name> "(" [ <value expression list> ] ")"

<aggregate function> ::= "COUNT" "(" "*" ")" | <general set function>

<general set function> ::= <computational operation> 
                           "(" [ <set quantifier> ] <value expression> ")"

<computational operation> ::= "AVG" | "MAX" | "MIN" | "SUM" | "COUNT"

<set quantifier> ::= "DISTINCT" | "ALL"

<case expression> ::= <case abbreviation> | <case specification>

<case abbreviation> ::= "NULLIF" "(" <value expression> "," <value expression> ")" 
                      | "COALESCE" "(" <value expression list> ")"

<case specification> ::= <simple case> | <searched case>

<simple case> ::= "CASE" <value expression> <simple when clause list> [ <else clause> ] "END"

<simple when clause> ::= "WHEN" <value expression> "THEN" <value expression>

<else clause> ::= "ELSE" <value expression>

<searched case> ::= "CASE" <searched when clause list> [ <else clause> ] "END"

<searched when clause> ::= "WHEN" <search condition> "THEN" <value expression>

<search condition> ::= <boolean value expression>

<cast specification> ::= "CAST" "(" <value expression> "AS" <data type> ")"

<subtype treatment> ::= "TREAT" "(" <value expression> "AS" <target subtype> ")"

<target subtype> ::= <reference type> | <class id> | <entity id>

<typeof treatment> ::= "TYPEOF" "(" <value expression> ")"

<reference resolution> ::= "DEREF" "(" <value expression> ")"

<null specification> ::= "NULL"
```

## Data Definition Languages: DDL et ODL

```
<table definition> ::= "CREATE" "TABLE" <table name>  <table element list>

<table element> ::= <column definition> | <table constraint definition>

<column definition> ::= <column name> <data type> { <column constraint definition> }

<column constraint definition> ::= "NOT" "NULL" | <unique specification>
                                 | <references specification> | <check constraint definition>

<unique specification> ::=  "UNIQUE" | "PRIMARY" "KEY"

<references specification> ::= "REFERENCES" <table name> [ "(" <column name list> ")" ]

<check constraint definition> ::= "CHECK" "(" <search condition> ")"

<table constraint definition> ::= [ <constraint name definition> ] <table constraint>

<constraint name definition> ::= "CONSTRAINT" <constraint name>

<table constraint> ::= <unique constraint definition> | <referential constraint definition> 
                     | <check constraint definition>

<unique constraint definition> ::= <unique specification> "(" <column name list> ")"

<referential constraint definition> ::= "FOREIGN" "KEY" "(" <column name list> ")"  
                                         <references specification>

<alter table statement> ::= "ALTER" "TABLE" <table name> <alter table action>

<alter table action> ::= <add column definition> | <drop column definition> 
                       | <add table constraint definition> | <drop table constraint definition>

<add column definition> ::= "ADD" <column definition>

<drop column definition> ::= "DROP" <column name>

<add table constraint definition>  ::= "ADD" <table constraint definition>

<drop table constraint definition> ::= "DROP" "CONSTRAINT" <constraint name>

<drop table statement> ::= "DROP" "TABLE" <table name>
```

The DDL can be used to define classes and properties of ontologies

```
<class definition> ::= "CREATE" <entity id>  <class id> [ <view clause> ]  [ <under clause> ] 
                       [ <descriptor clause> ] [ <properties clause list> ]

<view clause> ::= "AS VIEW"

<under clause> ::= "UNDER" <class id list>

<descriptor clause> ::= "DESCRIPTOR" "(" <attribute value list> ")"

<attribute value> ::= <attribute id> "=" <value expression>

<properties clause> ::= <entity id> "(" <property definition list> ")"

<property definition> ::= <prop id> <datatype> [<descriptor clause>]

<alter class statement> ::= "ALTER" <class id> [<descriptor clause> ] 
                            [ <alter class action> ]

<alter class action> ::= <add property definition> | <drop property definition>

<add property definition> ::= "ADD" [<entity id>] <property definition> [<descriptor clause>]

<drop property definition> ::= "DROP" <property id>

<drop class definition> ::= "DROP" <class id>
```

It can also be used to define extension of classes

```
<extension definition> ::= "CREATE" "EXTENT" "OF" <class id> 
                           "(" <property id list>  ")" [<logical clause>]

<logical clause> ::= "TABLE" [<table and column name>]

<table and column name> ::= <table name> ["(" <column name list> ")"]

<alter extension statement> ::= "ALTER EXTENT" "OF" <class id> <alter extent action>

<alter extension action> ::= <add property definition> | <drop property definition>

<add property definition> ::= "ADD" [ "PROPERTY" ] <property id> ["COLUMN" <column name>]

<drop property definition> ::= "DROP" [ "PROPERTY" ] <property id>

<drop extension statement> ::= "DROP" "EXTENT" "OF" <class id>
```

The ODL can be used to define entities and attributes of the ontology model

```
<entity definition> ::= "CREATE" "ENTITY" <entity id>  [ <under clause> ] <attribute clause>

<under clause> ::= "UNDER" <entity id list>

<attribute clause> ::=  <attribute definition list>

<attribute definition> ::= <attribute id> <datatype> [ <derived clause> ]

<derived clause> ::= "DERIVED BY" <function name>

<alter entity statement> ::= "ALTER" "ENTITY" <entity id> <alter entity action>

<alter entity action> ::= <add attribute definition> | <drop attribute definition>

<add attribute definition> ::= "ADD" [ "ATTRIBUTE" ] <attribute definition>

<drop attribute definition> ::= "DROP" [ "ATTRIBUTE" ] <attribute id>

<drop entity statement> ::= "DROP" "ENTITY" <entity id>
```

## Data Manipulation Languages: DML and OML

```
<insert statement>  ::= "INSERT" "INTO" <category id>  <insert description and source>

<insert description and source> ::= <from subquery> | <from constructor>

<from subquery> ::= [ "(" <insert description list> ")" ] <query expression>

<insert description list> ::= <column name list> | <property id list> | <attribute id list>

<from constructor> ::= [ "(" <insert description list> ")" ] <values clause>

<values clause> ::= "VALUES" "(" <values expression list> ")"

<update statement> ::= "UPDATE" <category id polymorph> "SET" <set clause list> 
                       [ "WHERE" <search condition> ]

<set clause> ::= <description id> "="  <value expression>

<delete statement> ::= "DELETE" "FROM" <category id polymorph>
                       [ "WHERE" <search condition> ]
```

## Data Query Languages: DQL and OQL

```
<query expression> ::= <query term> 
                     | <query expression> "UNION" <set quantifier>  <query term> 
                     | <query expression> "EXCEPT" <set quantifier>  <query term>

<query term> ::= <query primary> |
                 <query term> "INTERSECT" <set quantifier>  <query primary>

<query primary> ::= <query specification> | "(" <query expression> ")"

<query specification> ::= <select clause> <from clause> [ <where clause> ] 
                          [ <group by clause> ] [ <having clause> ] [ <order by clause> ] 
                          [ <namespace clause> ] [ <language clause> ]
```

SELECT clause

```
<select clause> ::= "SELECT" [ <set quantifier> ] <select list>

<select list> ::= "*" | <select sublist> { "," <select sublist> }

<select sublist> ::= <value expression> [ <as clause> ]

<as clause> ::=  [ "AS" ] <alias name>
```

FROM clause

```
<from clause> ::= "FROM" <category reference list>

<category reference> ::= <category primary> | <joined category>

<category primary> ::= <category or subquery>  [ [ "AS" ] <alias name>]
                     | <collection derived category>  [ "AS" ] <alias name>

<category or subquery> ::= <category id polymorph> | <dynamic iterator> | <subquery>

<dynamic iterator> ::= <identifier> | "ONLY" "(" <identifier> ")"

<subquery> ::= "(" <query expression> ")"

<collection derived category> ::= "UNNEST" "(" <collection value expression> ")"

<joined category> ::= <cross join> | <qualified join> | <natural join>

<cross join> ::= <category reference> "CROSS" "JOIN"  <category primary>

<qualified join> ::= <category reference>[ <join type> ] "JOIN"
                     <categroy reference> <join specification>

<join type> ::= "INNER" | <outer join type> [ "OUTER" ]

<outer join type> ::= "LEFT" | "RIGHT" | "FULL"

<join specification> ::= <join condition> | <named columns join>

<join condition> ::= "ON" <search condition>

<named columns join> ::=  "USING" "(" <description id list> ")"

<natural join> ::= <category reference> "NATURAL" [ <join type>]  
                   "JOIN" <category primary>
```

WHERE, GROUP BY, HAVING and ORBER BY clauses

```
<where clause> ::= "WHERE" <search condition>

<group by clause> ::= "GROUP" "BY" [ <set quantifier> ] <description id list>

<having clause> ::= "HAVING" <search condition>

<order by> ::= "ORDER" "BY" <sort specification list>

<sort specification> ::= <sort key> [ <ordering specification> ]

<sort key> ::= <value expression>

<ordering specification> ::= "ASC" | "DESC"
```

NAMESPACE and LANGUAGE clauses

```
<namespace clause> ::= "USING" "NAMESPACE" <namespace definition list>

<namespace definition> ::= [ <namespace alias> "=" ] <namespace id>

<language clause> ::= "USING" "LANGUAGE" <language id>
```

## View Definition Language: VDL

```
<view definition> ::= "CREATE" "VIEW" <table name>  <view specification>
                      "AS" <query expression>

<view specification> ::= <regular view specification> 
                       | <referenceable view specification>

<regular view specification> ::= [ "(" <column name list> ")" ]

<referenceable view specification> ::= "OF" <class id>  [<property id list> ]
```

## Parameters of the language

```
<global namespace definition> ::= "SET" "NAMESPACE" [
                                  <namespace alias> "=" ] <namespace specification>

<namespace specification> ::= <namespace id> | "NONE"

<global language definition> ::= "SET" "LANGUAGE" <language specification>

<language specification> ::= <language id> | "NONE"
```