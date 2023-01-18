--************************************************************************************************************--
---------------------------------------- MariusQL Oracle SCHEMA ------------------------------------------------
--************************************************************************************************************--

DROP TABLE mm_datatype  CASCADE CONSTRAINT;
DROP TABLE mm_entity    CASCADE CONSTRAINT;
DROP TABLE mm_attribute CASCADE CONSTRAINT;
DROP TABLE m_datatype   CASCADE CONSTRAINT;
DROP TABLE m_class      CASCADE CONSTRAINT;
DROP TABLE m_property   CASCADE CONSTRAINT;
DROP TABLE languages    CASCADE CONSTRAINT;
DROP TABLE about        CASCADE CONSTRAINT;

DROP SEQUENCE mm_rid_seq;
DROP SEQUENCE m_rid_seq;
DROP SEQUENCE i_rid_seq;
DROP SEQUENCE l_rid_seq;
DROP SEQUENCE a_rid_seq;

CREATE SEQUENCE mm_rid_seq
  START WITH 100
  NOMAXVALUE
  MINVALUE 1
  CACHE 5;
  
CREATE SEQUENCE m_rid_seq
  START WITH 100
  NOMAXVALUE
  MINVALUE 1
  CACHE 5;

CREATE SEQUENCE i_rid_seq
  START WITH 100
  NOMAXVALUE
  MINVALUE 1
  CACHE 5;
  
CREATE SEQUENCE l_rid_seq
  START WITH 100
  NOMAXVALUE
  MINVALUE 1
  CACHE 5;
  
CREATE SEQUENCE a_rid_seq
  START WITH 100
  NOMAXVALUE
  MINVALUE 1
  CACHE 5;

------------------------------------------------------------------------------------------------- Metameta model
----------------------------------------------------------------------------------------------------------------
CREATE TABLE mm_entity (
  rid               int DEFAULT mm_rid_seq.NEXTVAL PRIMARY KEY,
  name              varchar2(255) NOT NULL,
  package           varchar2(255),
  mappedtablename   varchar2(255) NOT NULL,
  ismetametamodel   number(1) DEFAULT 0,
  iscore            number(1) DEFAULT 0,
  attributes        TARRAYINT,
  superentity       int,
  
  CONSTRAINT ct_mm_entity_superentity FOREIGN KEY (superentity) REFERENCES mm_entity (rid),
  CONSTRAINT ct_mm_entity_name UNIQUE (name)
);

CREATE TABLE mm_datatype (
  rid               int DEFAULT mm_rid_seq.NEXTVAL PRIMARY KEY,
  dtype             varchar2(40) NOT NULL,
  onclass           int,
  collectiontype    int,
  ismanytomany      number(1),
  reverseattribute  int,
  issimpletype      number(1) DEFAULT 0,
  
  CONSTRAINT ct_mm_datatype_onclass FOREIGN KEY (onclass) REFERENCES mm_entity (rid),
  CONSTRAINT ct_mm_datatype_collectiontype FOREIGN KEY (collectiontype) REFERENCES mm_datatype (rid)
);

CREATE TABLE mm_attribute (
  rid           int DEFAULT mm_rid_seq.NEXTVAL PRIMARY KEY,
  name          varchar2(255),
  scope         int NOT NULL,
  range         int NOT NULL,
  isoptional    number(1) DEFAULT 1,
  iscore        number(1) DEFAULT 0,
  
  CONSTRAINT ct_mm_attribute_scope FOREIGN KEY (scope) REFERENCES mm_entity (rid),
  CONSTRAINT ct_mm_attribute_range FOREIGN KEY (range) REFERENCES mm_datatype (rid)
);
ALTER TABLE mm_datatype ADD CONSTRAINT ct_mm_datatype_reverseattrib FOREIGN KEY (reverseattribute)REFERENCES mm_attribute (rid);

insert into mm_entity (rid, name, mappedtablename, iscore) values (28, 'boolean', 'm_datatype', 1); 
insert into mm_entity (rid, name, mappedtablename, iscore) values (29, 'int', 'm_datatype', 1);
insert into mm_entity (rid, name, mappedtablename, iscore) values (30, 'real', 'm_datatype', 1);
insert into mm_entity (rid, name, mappedtablename, iscore) values (31, 'string', 'm_datatype', 1);
insert into mm_entity (rid, name, mappedtablename, iscore) values (32, 'multistring', 'm_datatype', 1);
insert into mm_entity (rid, name, mappedtablename, iscore, superentity) values (33, 'uritype', 'm_datatype', 1, 31);
insert into mm_entity (rid, name, mappedtablename, iscore, superentity) values (63, 'enum', 'm_datatype', true, 31);  
insert into mm_entity (rid, name, mappedtablename, iscore, superentity) values (64, 'counttype', 'm_datatype', true, 29);  
insert into mm_entity (rid, name, mappedtablename, iscore) values (65, 'array', 'm_datatype', true);  
insert into mm_entity (rid, name, mappedtablename, iscore) values (66, 'ref', 'm_datatype', true);    

insert into mm_datatype (rid, dtype, isSimpleType) values (1, 'boolean', 1);
insert into mm_datatype (rid, dtype, isSimpleType) values (2, 'int', 1);
insert into mm_datatype (rid, dtype, isSimpleType) values (3, 'real', 1);
insert into mm_datatype (rid, dtype, isSimpleType) values (4, 'string', 1);
insert into mm_datatype (rid, dtype, isSimpleType) values (5, 'multistring', 1);

insert into mm_entity (rid, name, mappedtablename, ismetametamodel) values (6, 'mmentity', 'mm_entity', 1);	
insert into mm_entity (rid, name, mappedtablename, ismetametamodel) values (7, 'mmattribute', 'mm_attribute', 1);
insert into mm_entity (rid, name, mappedtablename, ismetametamodel) values (8, 'mmdatatype', 'mm_datatype', 1);	

----------------------------------------------------------------------------------------------------- Meta model
----------------------------------------------------------------------------------------------------------------
create table m_class (
  rid              int DEFAULT m_rid_seq.NEXTVAL PRIMARY KEY,
  dtype            varchar2(40) NOT NULL,
  code             varchar2(255) NOT NULL,
  name_fr          varchar2(255),
  name_en          varchar2(255),
  definition_fr	   varchar2(4096),
  definition_en    varchar2(4096),
  isextension      number(1) DEFAULT 0,
  directproperties TARRAYINT,
  usedproperties   TARRAYINT,
  superclass       int,
  package          varchar2(255),
  
  CONSTRAINT ct_m_class_superclass FOREIGN KEY (superclass) REFERENCES m_class (rid)
);

CREATE TABLE m_datatype (
  rid             int DEFAULT m_rid_seq.NEXTVAL PRIMARY KEY,
  dtype           varchar2(40) NOT NULL,
  onclass         int,
  collectiontype  int,
  issimpletype    number(1),
  enumvalues      TARRAYSTRING,
  
  CONSTRAINT ct_m_datatype_onclass FOREIGN KEY (onclass) REFERENCES m_class (rid),
  CONSTRAINT ct_m_datatype_collectiontype FOREIGN KEY (collectiontype) REFERENCES m_datatype (rid)
);

create table m_property (
  rid     int DEFAULT m_rid_seq.NEXTVAL PRIMARY KEY,
  dtype   varchar2(40) NOT NULL,
  code    varchar2(255) NOT NULL,
  name_fr varchar2(255),
  name_en varchar2(255),
  definition_fr varchar2(4096),
  definition_en varchar2(4096),
  scope   int,
  range   int,
  ismandatory	number(1),
  isvisible number(1),
  
  CONSTRAINT ct_m_property_scope FOREIGN KEY (scope) REFERENCES m_class (rid),
  CONSTRAINT ct_m_property_range FOREIGN KEY (range) REFERENCES m_datatype (rid)
);

insert into m_datatype (rid, dtype, isSimpleType) values (1, 'boolean', 1);
insert into m_datatype (rid, dtype, isSimpleType) values (2, 'int', 1);
insert into m_datatype (rid, dtype, isSimpleType) values (3, 'real', 1);
insert into m_datatype (rid, dtype, isSimpleType) values (4, 'string', 1);
insert into m_datatype (rid, dtype, isSimpleType) values (5, 'multistring', 1);
insert into m_datatype (rid, dtype, isSimpleType) values (6, 'uritype', 1);

insert into mm_entity (rid, name, mappedtablename, iscore) values (9, 'class', 'm_class', 1);
insert into mm_attribute (rid, name, scope, range, iscore) values (30, 'rid', 9, 2, 1);
insert into mm_attribute (rid, name, scope, range, iscore) values (10, 'code', 9, 4, 1);
insert into mm_attribute (rid, name, scope, range, iscore) values (24, 'dtype', 9, 4, 1);  
insert into mm_attribute (rid, name, scope, range, iscore) values (38, 'package', 9, 4, 1);    
insert into mm_attribute (rid, name, scope, range, iscore) values (40, 'name', 9, 5, 1); 
insert into mm_datatype (rid, dtype, onclass) values (34, 'ref', 9); 
insert into mm_attribute (rid, name, scope, range, iscore) values (35, 'superclass', 9, 34, 1); 
 
insert into mm_entity (rid, name, mappedtablename, iscore) values (11, 'property', 'm_property', 1);
insert into mm_attribute (rid, name, scope, range, iscore) values (31, 'rid', 11, 2, 1);  
insert into mm_attribute (rid, name, scope, range, iscore) values (16, 'code', 11, 4, 1);
insert into mm_attribute (rid, name, scope, range, iscore) values (25, 'dtype', 11, 4, 1);
insert into mm_attribute (rid, name, scope, range, iscore) values (50, 'name', 11, 5, 1); 
insert into mm_attribute (rid, name, scope, range, iscore) values (51, 'definition', 11, 5, 1);
insert into mm_attribute (rid, name, scope, range, iscore) values (53, 'ismandatory', 11, 1, 1);
insert into mm_datatype (rid, dtype, onclass) values (18, 'ref', 9); 
insert into mm_attribute (rid, name, scope, range, isoptional, iscore) values (17, 'scope', 11, 18, 0, 1);
insert into mm_attribute (rid, name, scope, range, iscore) values (67, 'isvisible', 11, 1, true);

insert into mm_entity (rid, name, mappedtablename, iscore) values (15, 'datatype', 'm_datatype', 1);
insert into mm_attribute (rid, name, scope, range, iscore) values (32, 'rid', 15, 2, 1);
insert into mm_datatype (rid, dtype, collectiontype, ismanytomany) values (55, 'array', 4, true);
insert into mm_attribute (rid, name, scope, range, iscore) values (56, 'enumvalues', 15, 55, true);
insert into mm_attribute (rid, name, scope, range, iscore) values (57, 'dtype', 15, 4, true);
insert into mm_datatype (rid, dtype, onclass) values (58, 'ref', 9);
insert into mm_attribute (rid, name, scope, range, iscore) values (59, 'onclass', 15, 58, true); 
insert into mm_datatype (rid, dtype, onclass) values (60, 'ref', 15);
insert into mm_datatype (rid, dtype, collectiontype, ismanytomany) values (61, 'array', 60, true);  
insert into mm_attribute (rid, name, scope, range, iscore) values (62, 'collectiontype', 15, 61, true);

insert into mm_datatype (rid, dtype, onclass) values (20, 'ref', 15); 
insert into mm_attribute (rid, name, scope, range, isoptional, iscore) values (19, 'range', 11, 20, 0, 1);
insert into mm_datatype (rid, dtype, onclass) values (14, 'ref', 11);	
insert into mm_datatype (rid, dtype, collectiontype, ismanytomany, reverseattribute) values (13, 'array', 14, 0, 17);
insert into mm_attribute (rid, name, scope, range, iscore) values (12, 'directproperties', 9, 13, 1);
insert into mm_datatype (rid, dtype, onclass) values (23, 'ref', 11); 
insert into mm_datatype (rid, dtype, collectiontype, ismanytomany) values (22, 'array', 23, 1);
insert into mm_attribute (rid, name, scope, range, iscore) values (21, 'usedproperties', 9, 22, 1);
insert into mm_attribute (rid, name, scope, range, iscore) values (26, 'isextension', 9, 1, 1);  

insert into mm_entity (rid, name, mappedtablename, iscore) values (27, 'staticclass', 'm_class', 1);
insert into mm_attribute (rid, name, scope, range, iscore) values (33, 'rid', 27, 2, 1);  
insert into mm_attribute (rid, name, scope, range, iscore) values (28, 'code', 27, 4, 1);
insert into mm_attribute (rid, name, scope, range, iscore) values (29, 'dtype', 27, 4, 1);
insert into mm_datatype (rid, dtype, onclass) values (36, 'ref', 27); 
insert into mm_attribute (rid, name, scope, range, iscore) values (37, 'superclass', 27, 36, 1);  
insert into mm_attribute (rid, name, scope, range, iscore) values (39, 'package', 27, 4, 1); 
insert into mm_attribute (rid, name, scope, range, iscore) values (49, 'name', 27, 5, 1);
insert into mm_attribute (rid, name, scope, range, iscore) values (54, 'definition', 27, 5, 1);

insert into mm_attribute (rid, name, scope, range, iscore) values (41, 'rid', 6, 4, 1); 
insert into mm_attribute (rid, name, scope, range, iscore) values (42, 'name', 6, 2, 1); 
insert into mm_attribute (rid, name, scope, range, iscore) values (43, 'package', 6, 4, 1); 
insert into mm_attribute (rid, name, scope, range, iscore) values (44, 'mappedtablename', 6, 4, 1); 
insert into mm_attribute (rid, name, scope, range, iscore) values (45, 'ismetametamodel', 6, 1, 1); 
insert into mm_attribute (rid, name, scope, range, iscore) values (46, 'iscore', 6, 1, 1); 
insert into mm_datatype (rid, dtype, onclass) values (37, 'ref', 7); 
insert into mm_datatype (rid, dtype, collectiontype, ismanytomany) values (38, 'array', 37, 1); 
insert into mm_attribute (rid, name, scope, range, iscore) values (47, 'attributes', 6, 4, 1); 
insert into mm_attribute (rid, name, scope, range, iscore) values (48, 'superentity', 6, 4, 1); 

create table languages (
  rid int DEFAULT l_rid_seq.NEXTVAL PRIMARY KEY,
  name varchar2(255) NOT NULL,
  code varchar2(255) NOT NULL,
  description varchar2(255)
);

insert into languages (name, code) values ('Français', 'fr');
insert into languages (name, code) values ('English', 'en');


create table metadata (
  rid int DEFAULT a_rid_seq.NEXTVAL PRIMARY KEY,
  metaname varchar2(40) NOT NULL,
  metavalue varchar2(255)
);

insert into metadata (metaname, metavalue) values ('version', '0.5');

update mm_entity set attributes = TARRAYINT(10,12,21,24,26,30,35,38,40,52) where rid = 9;
update mm_entity set attributes = TARRAYINT(16,17,19,25,31,50,51,53,67) where rid = 11;
update mm_entity set attributes = TARRAYINT(33,28,29,36,37,39,49,54) where rid = 27;
update mm_entity set attributes = TARRAYINT(41,42,43,44,45,46,47,48) where rid = 6;
update mm_entity set attributes = TAARAYSTRING(32,56,57,59,62) where rid = 15;

-- DO NOT REMOVE THIS LINE
commit;
