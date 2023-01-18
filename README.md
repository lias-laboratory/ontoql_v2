# This repository is for OntoQL 2

You are looking at the repository of OntoQL for OntoDB version 2.

Please refer to [OntoDB repository](https://github.com/lias-laboratory/ontodb) to have a big picture of OntoDB ontology based database tool.

## Overview

The OntoQL language provides access to 

* ontology instances through its Data Denition, Manipulation, and Query Languages, 
* ontologies through its Ontology Denition, Manipulation, and Query Languages,
* both ontologies and their instances.

### The OntoQL Data Definition, Manipulation and Query Languages

#### Data Definition Language (DDL)

_Definition of the ontology part_

OntoQL provides with the resources to create, update and delete concepts of an ontology (classes, properties, . . . ) and of attribute values (names, definitions, . . . ). The following instruction creates a class with an English name _Laboratory_. It extends the class _Research Institute_ and gives definitions and names in other languages (French and Spanish). Four properties are defined and created: the _name_ of the laboratory, its _director_, its _acronym_ and its _team_.

```sql
CREATE CLASS Laboratory EXTENDS "Research Institute"(
  DESCRIPTOR (
    #name[fr,es] = (‘Laboratoire’,‘Laboratorio’),
    #definition = ‘workplace for conducting research activities’,
    #definition[fr] = ‘lieu pour mener des recherches’)
  PROPERTIES (
    name String, 
    director Person, 
    acronym String, 
    team SET OF Person)
);
```

Two specific clauses are introduced: *DESCRIPTOR* for introducing class attributes and descriptions (prefixed by #) and *PROPERTIES* to describe the relevant characterization properties of the concept. The valuations of these properties define the instances of concepts. A property may be modified. The following clause changes the English name and adds an _illustration_ to the _director_ property.

_Definition of the content part_

The extent of a class is defined from the ontology by choosing which properties are valued for a given class.

```sql
CREATE EXTENT OF Laboratory (name, team, director);
```

This clause creates a container (table) of instances of the class _Laboratory_ with the properties _name_, _team_ and _director_. The link with the ontology and its concept definitions is also kept in the database.

#### The data manipulation language (DML)

When the extent of a class is defined, like in SQL3, class instances can be inserted, deleted and updated. The following clause creates a new instance of the Laboratory class.

```sql
INSERT INTO Laboratory (name, acronym) VALUES (‘Laboratoire d’Informatique Scientifique et Industrielle’, ‘LISI’);
```

The properties valued in an *INSERT* clause may be not described in the extent of a class (the acronym property in the previous clause). In this case, OntoQL offers three options: 1) either a _NULL_ value is inserted or 2) an error is returned and the clause is rejected or 3) the extent of the class is completed by a new property and all the values of this property are completed with _NULL_ values for the other instances.

To manipulate a property which type is another class, there is need to use nested clauses. For example,

```sql
UPDATE ontology_sic:Laboratory SET director = (SELECT p FROM p in ontology_sic:Person WHERE p.name=‘LIENHARDT’ and p.surname=‘Pascal’) WHERE acronym = ‘SIC’;
```

modifies the _director_ of the _laboratory_ which _acronym_ is SIC by retrieving the _director_ in a class _Person_ of the ontology _ontology sic_.

#### The data query language (DQL)

OntoQL allows the query of contents. Moreover, since the content is linked to the ontology, querying the content does not rely on any specific logical database model. Therefore, two applications sharing a common ontology will have the right to run a common query even if the underlying logical database models are different.

Before giving the intuition of the content querying, let us recall some basic characteristics of instances.

* Each instance has an unique identifier (oid).
* Each instance has a basic class in the ontology.
* Each instance is described by the values of the properties defined in the extent of the class.
* Ontology classes may be linked by an inheritance relationship.

Querying the content will be similar to querying the ontology, except that that properties will not be prefixed by the #symbol.

Next query returns the _laboratory names_ whose members do not have homonymous _name_ with the _director_ of this _laboratory_.

```sql
SELECT l.name
FROM l in Laboratory
WHERE l.director.name <> ALL (SELECT member.name
FROM member in l.team)
```

Notice that the same query written in SQL on the logical model presented on figure 3 is much more complicated. It requires to retrieve all the tables associated to the class _Laboratory_.

A polymorphic search operator * allows a query to retrieve the instances of a class and of all its subclasses. As illustrated below, the first query retrieves the names of instances which basic class is Research Institute while the second one returns the names for the Laboratory class as well.

```sql
SELECT name FROM "Research Institute"

SELECT nom FROM "Institut de Recherche"*
```

Notice that the second query of the previous example is written in French. This possibility is provided thanks to the capability of the ontology to support multilingual attribute names and concept language translations.

### The OntoQL Ontology Definition, Manipulation and Query Languages

#### Ontology Definition Language (ODL)

The ODL should support the extension of the core ontology model by dening new entities and new attributes. Since the core ontology model is not static, its entities and attributes must not be encoded as keywords of the OntoQL language. As we have seen, we have chosen is to prex each element of this model by the # character. Moreover, to keep an uniform syntax between the DDL and the ODL, this language creates, modies and deletes entities and  attributes of the core ontology model using a syntax similar to the manipulation of SQL user-dened types (CREATE, ALTER, DROP).

```sql
CREATE ENTITY #OWLAllValuesFrom UNDER #Class (
#onProperty REF(#Property),
#allValuesFrom REF(#Class) )
```

This statement adds the OWLAllValuesFrom entity to our core ontology model as a subentity of the Class entity . This entity is created with two attributes, onProperty and allValuesFrom, which take respectively as values identiers of properties and identiers of classes. 

#### Ontology Manipulation Language (OML)

The OML should support the denition, modication and deletion of elements of ontologies. Thus, syntactic equivalences are dened between DML and DDL statements. These two syntactic constructions are valid but in general the second one is more compact.

```sql
INSERT INTO #OWLRestrictionAllValuesFrom
(#name[en], #name[fr], #onProperty, #allValuesFrom)
VALUES ('InvalidPost', 'Post invalide', 'hasModifiers', 'Post')
```

This example shows that values of multilingual attributes can be dened in dierent natural languages ([en] for English and [fr] for French). It also shows that the names of classes and properties can be used to identify them. Indeed the value of the onProperty attribute is hasModifiers: the name of a property (and not its identier). Notice that thanks to the syntactic equivalences previously presented, we could have written this statement with
the CREATE syntax closer to the creation of user-dened types in SQL.

#### Ontology Query Language (OQL)

Finally the OQL can be used to search elements dened in a ontology. Queries are similar to the one of the DQL except that entities and properties are used instead of classes and properties.

```sql
SELECT #name[en], #allValuesFrom.#name[en]
FROM #OWLRestrictionAllValuesFrom
WHERE #onProperty.#name[en] = 'hasModifiers'
```

This query consists in a selection and a projection. The selection retrieves the restrictions on the property named in English hasModifiers. The used path expression in this selection is composed of the onProperty attribute which retrieves the identier of the property on which the restriction is dened and of the name attribute which retrieves the name in English of this property from its identier. The projection also applies the name attribute to retrieve the name of the restriction and the path expression composed of the allValuesFrom and name attributes to retrieve the name of the class in which the property implied in the restriction must take its values.

An usual query on an ontology consists in searching all subclasses (direct and indirect) of a given class. This kind of query is hard to express in OntoQL since it involves a recursive processing. As a consequence, we have chosen to define a set of derived attributes to express these kind of queries. Thus, subclasses and superclasses (direct or indirect) are computed by the #directSubclasses, #superclasses, #directSubclasses and #subclasses attributes. Properties defined on a class are given by the #scopeProperties. To get also the inherited property, the #properties attribute is dened. Finally, the properties used to describe instances of a given class are given by the #usedProperties attribute.

```sql
SELECT csup.#name
FROM #Class AS c,
UNNEST(c.#superclasses) AS csup
WHERE c.#name = 'Post'
```

The Post class is retrieved using the WHERE clause and identied by the c iterator. The UNNEST operator provides the csup iterator on the superclasses of the c class. Finally the names of superclasses are projected in the SELECT clause.

### Querying both ontology and content

Since the links between ontologies and their contents are kept in the Ontology Based Database model, OntoQL has exploited this capability to allow querying both ontology and content in the same query.

#### From Ontology to content

To query contents, OntoQL suggests an iterator i on instances of a class C by writing i in C or C as i. Next query returns the identifiers of instances belonging to the polymorphic extent of a class which French name begins
with the string "Per".

```sql
SELECT i.oid
FROM C in #class, i in C*
WHERE C.#name[fr] like ‘Per%’
```

Moreover, it permits to retrieve and/or use the values of a property discovered by the query itself on the ontology part. The following query allows the retrieval of the values of the properties of the instances obtained in the previous query.

```sql
SELECT i.oid, p.#name[fr], i.p
FROM C in #class, p in C.#properties, i in C*
WHERE C.#name[fr] like ‘Per%’
```

The previous query returns a tuple per property, but to obtain a single tuple per instance, one can write:

```sql
SELECT i.oid, (SELECT i.p, p.#name[fr]
FROM p in C.#properties)
FROM C in #class, i in C*
WHERE C.#name[fr] like ‘Per%’
```

#### From content to ontology

OntoQL proposes the use of the typeOf operator to make distinction between properties and retrieve information from the ontology part starting from the content part. This typeOf operator is implemented thanks to the link between ontology and content stored in the OBDB database. For example, the following query

```sql
SELECT i.name, i.surname, typeOf(i).#name[fr]
FROM i in Person*
```

returns the French name of the basic class of the polymorphic instances of the class Person.

This kind of queries is particularly useful in a system where instances are originated from different sources and specializing a shared ontology.

## Software requirements

* Postgresql
* Java >= 8
* Maven (for compilation step)

## Usage

We suppose that OntoDBSchema V2 is correctly installed. If not, please refer to this [page](https://github.com/lias-laboratory/ontodbschema_v2).

* Compile the project and deploy the artifcats to the local Maven repository.

```
$ mvn clean install
```

* Create a new maven project and add a dependency on OntoQL

```
<groupId>fr.ensma.lias</groupId>
<artifactId>mariusql</artifactId>
<version>2.1-SNAPSHOT</version>
```

* Create a new class called `OntoQLSampleTest` and copy the following content.

```java
public class OntoQLSampleTest {
    public static void main(String[] args) throws SQLException {
        Properties props = new Properties();

        props.setProperty("server.host", "localhost");
        props.setProperty("server.port", "5433");
        props.setProperty("server.user", "postgres");
        props.setProperty("server.password", "psql");
        props.setProperty("server.sid", "mariusqltest");
        props.setProperty("driver.class", "fr.ensma.lias.mariusql.driver.postgresql.MariusQLDriverImpl");

        // Create session
        MariusQLSession currentMariusSession = new MariusQLSessionImpl(props);
        currentMariusSession.getMetaModelCache().setEnabled(true);
        currentMariusSession.getModelCache().setEnabled(true);
        currentMariusSession.getQueryCache().setEnabled(true);

        currentMariusSession.setReferenceLanguage(MariusQLConstants.FRENCH);
        currentMariusSession.setDefaultNameSpace("http://www.lias.fr/");

        MariusQLStatement statement = currentMariusSession.createMariusQLStatement();
        statement.executeUpdate("CREATE #CLASS myFirstClass (DESCRIPTOR (#code = 'myFirstClassCode'))");

        final MariusQLResultSet executeQuery = statement.executeQuery("select #code from #class where #code='myFirstClassCode'");
        executeQuery.next();
        System.out.println(executeQuery.getString(1));

        currentMariusSession.rollback();
    }
}
```

## Software licence agreement

Details the license agreement of OntoQL V2: [LICENCE](LICENCE)

## Code analysis

* Lines of Code: 17 329
* Programming Languages: Java, XML