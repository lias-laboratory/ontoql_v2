# OntoQL Query examples

Queries are case-insensitive, except for names of ontology classes and properties. So SELECT is the same as SeLeCt but Size is not equivalent to SIZE and Person is not equivalent to PERSON. In the following examples, we use uppercase OntoQL keywords.

## Data Definition Language (DDL)

DDL allows to create a new class in an ontology:

```sql
CREATE #Class Vehicle (
   DESCRIPTOR (
      #name[fr]='Véhicule',
      #code='AFBDF54D',
      #version='002')
   PROPERTIES ("number of wheels" INT, model STRING)
)
```

A class can be defined as a subclass of another class:

```sql
CREATE #Class Motorcycle UNDER Vehicle

CREATE #Class Car UNDER Vehicle (
        DESCRIPTOR (
                #name[fr]='Voiture'
        )
        PROPERTIES (
                color STRING
        )
)
CREATE #Class CarBlue UNDER Car
```

The properties of a class can be of datatype collection or a reference to another class.

```sql
CREATE #class Person (
  DESCRIPTOR (#name[fr]='Personne',
              #code='AXDFBDF54D',
              #version='001')
  PROPERTIES (name STRING,
              age INT,
              "main car" REF(Vehicle), 
              "other cars" REF(Vehicle) ARRAY)
)
```

## Data Manipulation Language (DML)

To manipulate the data, an extent must first be defined on a class. An extent can be a table:

```sql
CREATE EXTENT OF Car ("number of wheels", model, color)
CREATE EXTENT OF Motorcycle ("number of wheels", model)
CREATE EXTENT OF Person (name, "main vehicle", "other vehicles")
```

An extent can also be a view:

```sql
CREATE VIEW OF CarBlue AS 
   (SELECT oid, "number of wheels", model FROM Vehicle WHERE color='Blue')
```

Once an extent has been created for a class, the DML can be used to INSERT new data instances.

```sql
INSERT INTO Car ("number of wheels",model,color) values (4, 'Z3', 'Red')
INSERT INTO Car ("number of wheels",model,color) values (4, 'Mercedes-Benz CLS', 'Blue')
INSERT INTO Car ("number of wheels",model,color) values (4, 'Mercedes-Benz GL', 'Blue')
INSERT INTO Motorcycle ("number of wheels",model) values (2, 'Honda 599')
INSERT INTO Motorcycle ("number of wheels",model) values (2, 'Honda 600')
INSERT INTO Person (name, "main car", "other cars") VALUES ('Dupont', (SELECT c.oid FROM Car c WHERE c.model = 'Z3'), ARRAY(SELECT c.oid FROM CarBlue AS c))
INSERT INTO Person (name, "main car") VALUES ('Dupont', (SELECT m.oid FROM Motorcycle AS m WHERE m.model = 'Honda 600'))
```

The DML can be used to UPDATE existing data instances.

```sql
UPDATE Person SET name='Dupond' WHERE name = 'Dupont'
UPDATE Person SET "main vehicle" = (SELECT oid FROM Motorcycle WHERE model = 'Honda 599) WHERE name = 'Dupond'
```

The DML can be used to delete existing data instances.

```sql
DELETE FROM Person WHERE "main car" in (SELECT m.oid FROM Motorcycle AS m WHERE m.model = 'Honda 600')
DELETE FROM Motorcycle WHERE model = 'Honda 600'
```

## Data Query Language (DQL)

Data Query Language can be used to retrieve data that reference an ontology.

### SELECT and FROM clause

The SELECT clause performs properties projection of instances of classes in the FROM clause.

Classes and properties are identified by names in different natural languages and by internal and external identifiers. Here's four means to express the same query:

```sql
-- written in english
SELECT "number of wheels", model FROM Car
-- written in french
SELECT "nombre de roues", modèle FROM Voiture
-- using external identifier
SELECT 71DC24862420C-001, 71DC2486B78EF-001 FROM 71DC2FDBFD904-001
-- using internal identifier
SELECT !1204, !1202 FROM !1068
```

### WHERE clause

Traditional comparison operators, artithmetics operators and function may be used in the WHERE CLAUSE.

```sql
SELECT "number of wheels", model FROM Vehicle WHERE  "number of wheels" = 4
SELECT "number of wheels", model FROM Vehicle WHERE model LIKE 'Mercedes-Benz%'
SELECT "number of wheels", model FROM Car WHERE color in ('Red','Blue')
SELECT "number of wheels", model FROM Car WHERE color is null
SELECT "number of wheels", model FROM Car WHERE color is not null
SELECT "number of wheels", model FROM Vehicle WHERE  "number of wheels"/2+2 = 4
SELECT sqrt("number of wheels"), model FROM Vehicle WHERE  "number of wheels"/2+2 = 4 and model like 'Mercedes-Benz%'
SELECT "number of wheels", model FROM Vehicle WHERE  "number of wheels"/2+2 = 4 or model like UPPER('Mercedes-Benz')
```

### ALIAS

Alias are available following SQL syntax.

```sql
SELECT Car."number of wheels", Car.model FROM Car 
SELECT c."number of wheels", c.model FROM Car AS c
SELECT c."number of wheels", c.model FROM Car AS c
```

### Explicit join

```sql
SELECT m.oid, c.oid
FROM Motorcycle AS m, Car AS c
WHERE m.model = c.model
```

Join operation using subquery

```sql
SELECT m.oid
FROM Motorcycle AS m
WHERE m.model in (SELECT c.model FROM Car AS c)
ORDER BY m.oid

SELECT m.oid
FROM Motorcycle AS m
WHERE m.model > all (SELECT c.model FROM Car AS c)
ORDER BY m.oid

SELECT m.model
FROM Motorcycle AS m
WHERE m."number of wheels" in (SELECT v."number of wheels"-2 FROM Vehicle AS v)

SELECT name 
  FROM Person 
 WHERE "main car" in (SELECT v.oid 
                          FROM Car AS v 
                          WHERE v.color = 'Blue') 
ORDER BY name desc
```

### Path expressions

```sql
SELECT p."main car".model FROM Person AS p
SELECT p.name FROM Person p WHERE  p."main car".model LIKE 'Mercedes-Benz%'
SELECT p1.name,p2.name FROM Person p1, Person p2 WHERE  p1."main car".model = p2."main car".model
```

### Aggregate operators

Simple queries involving aggregate operators.

```sql
SELECT max("number of wheels") FROM Vehicle
SELECT min("number of wheels") FROM Vehicle
SELECT avg("number of wheels") FROM Vehicle
SELECT sum("number of wheels") FROM Vehicle
SELECT sum("number of wheels"+2/3.5) FROM Vehicle
SELECT count(model) FROM Vehicle GROUP BY model ORDER BY "number of wheels"
SELECT count(model), 'Model-' || model || '-' FROM Vehicle GROUP BY model HAVING "number of wheels" > 1 ORDER BY "number of wheels" asc
SELECT max("main car"."number of wheels") FROM Person
```

### Collection handling

```sql
SELECT "other cars" FROM Person
SELECT "other cars"[1] FROM Person where "other cars"[2] is not null
SELECT v."number of wheels", v.model FROM Person AS p, unnest(p."other cars") AS v 
```

### Checking

OntoQL perform different checking on a query.

```sql
-- existence of a property
SELECT mode FROM Vehicle
-- existence of a class
SELECT model FROM Vehcle
-- definition of a property on a class of the FROM clause
SELECT color FROM Vehicle
-- type checking
SELECT "number of wheels"+2-model FROM Vehicle
```

### SQL Compatibility

An SQL query can be executed by OntoQL.

```sql
SELECT column_nbWheels from table_Car
```

### SQL2003 functionalities

Test the most specific type of an instance:

```sql
SELECT CASE 
           WHEN DEREF(i.oid) IS OF (ONLY Car) THEN 'a car' 
           WHEN DEREF(i.oid) IS OF (ONLY Motorcycle) THEN 'a motorcycle' 
           ELSE 'an other vehicle' 
       END 
FROM Vehicle AS i
```

Downcast an instance

```sql
SELECT CASE 
           WHEN DEREF(v.oid) IS OF (Car) THEN TREAT(v AS Car).color 
           ELSE 'this vehicle has not a color' 
       END 
FROM Vehicle AS v
```

## Ontology Definition Language (ODL)

The ontology definition language can be used to extend the ontology model used. For example, one can add the OWL constructors of restrictions:

```sql
CREATE ENTITY #OWLRestriction UNDER #Class (
  #onProperty REF(#Property)
)

CREATE ENTITY #OWLRestrictionAllValuesFrom UNDER #OWLRestriction (
  #allValuesFrom REF(#Class)
)
```

## Ontology Manipulation Language (OML)

The ontology manipulation language can be used add new concepts in an ontology using the INSERT syntax

```sql
INSERT INTO #OWLRestrictionAllValuesFrom (#name[en], #onProperty, #allValuesFrom) 
        VALUES ('RedCar', 'color', 'Car') 
```

## Ontology Query Language (OQL)

Queries on ontologies retrieve ontology concepts. Queries on ontologies

```sql
SELECT #namespace FROM #ontology
```

Queries on concepts (class or property) of an ontology

```sql
SELECT #code, #name[en], #definition{en] FROM #concepts where #definedBy.#namespace = 'http://dfs.com/schema.rdf#'
```

Queries on ontology classes

```sql
SELECT #code, #version FROM #Class
SELECT #name[fr] FROM #Class
SELECT #name[fr] FROM #Class WHERE #name[en]='Vehicle'
SELECT #name[fr] FROM #Class WHERE #name[en] like 'C%'
SELECT count(*) FROM #Class WHERE #name[en] like '%C'

SELECT #directSuperclasses FROM #Class WHERE #name[en] = 'Car'
SELECT directSuperclass.#name[en] 
  FROM #Class AS c, 
       UNNEST(c.#directSuperclasses) AS directSuperclass 
 WHERE c.#name[en] = 'Car'
 SELECT #superclasses FROM #Class WHERE #name[en] = 'Car'
 SELECT #directSubclasses FROM #Class WHERE #name[en] = 'Car'
 SELECT #subclasses FROM #Class WHERE #name[en] = 'Car'
 SELECT #scopeProperties FROM #Class WHERE #name[en] = 'Car'
 SELECT #properties FROM #Class WHERE #name[en] = 'Car'
 SELECT #usedProperties FROM #Class WHERE #name[en] = 'Car'
```

Queries on ontology properties

```sql
SELECT p1.#code, p1.#version FROM #Property AS p1 WHERE p1.#oid=1204
SELECT c1.#name[en], c2.#name[fr] FROM #Class AS c1, #Class AS c2 WHERE c1.#version <> c2.#version
SELECT #name[fr] FROM #Property WHERE #version > all (SELECT c.#version FROM #Class AS c)
SELECT #scope.#name[fr] FROM #Property WHERE #oid = 1204
SELECT #scope.#name[fr],#scope.#name[en] FROM #Property WHERE #name[en] = 'Size'
SELECT #range.#oid FROM #Property WHERE #name[en] = 'Size'
```

Queries on ontology datatypes

```sql
SELECT #oid FROM #DATATYPE WHERE #oid < 1559
SELECT #onClass.#name[en] FROM #RefType
SELECT #ofDatatype FROM #CollectionType
```

## Queries both on ontology and on content

### Queries from ontologies to content

Retrieve the classes whose name contains Car with their instances:

```sql
SELECT i.oid, p.#name[en], i.p
  FROM #Class AS c, c AS i, UNNEST(c.#properties) AS p
 WHERE c.#name[en] LIKE '%Car%' 
ORDER BY i.oid DESC
```

h3. Queries from content to ontology

Retrieve all vehicles with the name of the class they belong to and its super-classes:

```sql
SELECT i."number of wheels",
       i.model, 
       TYPEOF(i).#name[en], 
       TYPEOF(i).#superclasses 
  FROM Vehicle AS i 
 WHERE TYPEOF(i).#name[en] like 'C%' 
ORDER BY i.oid ASC
```