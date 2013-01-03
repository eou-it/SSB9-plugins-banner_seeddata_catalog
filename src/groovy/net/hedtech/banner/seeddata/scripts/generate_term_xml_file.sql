-- *****************************************************************************************
-- * Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.                    *
-- * This copyrighted software contains confidential and proprietary information of        *
-- * SunGard Higher Education and its subsidiaries. Any use of this software is limited    *
-- * solely to SunGard Higher Education licensees, and is further subject to the terms     *
-- * and conditions of one or more written license agreements between SunGard Higher       *
-- * Education and the licensee in question. SunGard is either a registered trademark or   *
-- * trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.*                       5
-- * Banner and Luminis are either registered trademarks or trademarks of SunGard Higher   *
-- * Education in the U.S.A. and/or other regions and/or countries.                        *
-- *****************************************************************************************

spool  termData.xml

var term varchar2(9)
define termvar char
prompt enter TERM
accept  termvar

begin
  :term :=  '&termvar';
end ;
/


--set term off
--set echo off
--set verify off

drop table testClob; 
drop table testTables;
drop sequence clob_sequence;
CREATE TABLE testClob
(
    Id NUMBER,
    Text CLOB,
    CONSTRAINT testClob_Pk PRIMARY KEY (Id)
);
create table testTables 
(  id number,   
   table_name varchar2(100),
   table_select varchar2(600)
 );
create sequence clob_sequence increment by 1;
-- create insert statements here that are part of the module


insert into testTables values ( clob_sequence.nextval, 'STVTERM', 'select * from stvterm where stvterm_code = '''   ||  :term || '''   order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SOBTERM', 'select * from SOBTERM  where SOBTERM_term_code = '''   ||  :term || '''   order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SOBPTRM', 'select * from SOBPTRM  where SOBPTRM_term_code = '''   ||  :term || '''   order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SORRTRM', 'select * from SORRTRM  where SORRTRM_term_code = '''   ||  :term || '''   order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SFBESTS', 'select * from SFBESTS  where SFBESTS_term_code = '''   ||  :term || '''   order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SFBRFST', 'select * from SFBRFST  where SFBRFST_term_code = '''   ||  :term || '''   order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SFRRSTS', 'select * from SFRRSTS  where SFRRSTS_term_code = '''   ||  :term || '''   order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SFRRFCR', 'select * from SFRRFCR  where SFRRFCR_term_code = '''   ||  :term || '''   order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SOBODTE', 'select * from SOBODTE  where SOBODTE_term_code = '''   ||  :term || '''   order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SOREXTN', 'select * from SOREXTN  where SOREXTN_term_code = '''   ||  :term || '''   order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SORRFND', 'select * from SORRFND  where SORRFND_term_code = '''   ||  :term || '''   order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SOBWLTC', 'select * from SOBWLTC  where SOBWLTC_term_code = '''   ||  :term || '''   order by 1,2');

set term on
set echo on
set verify on

Select Count(*) TableCnt from testTables; 

set term off
set echo off
set verify off
set serveroutput on

create Or replace procedure p_xmltable(p_table varchar2, p_select varchar2, p_id number) is 
 
my_context NUMBER := 0; 
xmlresult clob; 
cnt   number := 1 ; 

BEGIN 

  -- Create a New Context    
   my_context:= DBMS_XMLGEN.NEWCONTEXT (p_select); 
   -- Do Not omit empty tags 
   dbms_xmlgen.setNullHandling(my_context,2); 
  
   -- Set the Row Element 
   DBMS_XMLGEN.SETROWSETTAG(my_context,'SCHEDULED'); 
   DBMS_XMLGEN.SETROWTAG(my_context,p_table); 
   xmlresult := DBMS_XMLGEN.GETXML(my_context);

-- Generate XML Output
   insert into testClob ( Id, Text) values (p_id, xmlresult); 
   

  commit; 
  
  DBMS_XMLGEN.CLOSECONTEXT(my_context); 
End p_xmltable; 
/ 

Set serveroutput On Size 1000000
Declare 
  p_table varchar2(30);
  p_select varchar2(600); 
  p_id number; 
  cursor tablelist is 
     Select table_name, table_select , id 
     from testTables
     order by id; 
begin 
   Open tableList; 
  loop 
     Fetch tableList into p_table, p_select, p_id ; 
     Exit when tableList%notfound;
     dbms_output.put_line('table: ' || p_table || ' ' || p_select);
     p_xmltable(p_table, p_select, p_id); 
  End loop ; 
  Close tableList; 
 
End;
/

set term on
set echo on
set verify on
set feedback on

Select Count(*) TestClob from testClob ; 

set term off
set echo off
set verify off

Select '<?xml version="1.0" encoding="Windows-1252" ?>' from dual;  
set pages 0
set linesize 500
set long 10000000
set head off
column text format a9999

select text from testClob order by id;

drop table testClob;
drop table testTables;
drop sequence clob_sequence;
prompt dont forget to modify the spooled file And Remove sql output,  And extra <?xml lines 
Prompt also Remove extra root tags so there is only one <SCHEDULE> And <?SCHEDULE>
PROMPT each xml gen will produce a root tag  
spool off; 
