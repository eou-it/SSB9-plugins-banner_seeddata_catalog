-- *****************************************************************************************
-- * Copyright 2016 Ellucian Company L.P. and its affiliates.                         *
-- *****************************************************************************************

spool  validationData.xml

var tablevar varchar2(9)
define tablevar char
prompt enter TABLENAME
accept  tablevar

begin
  :tablevar :=  '&tablevar';
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


insert into testTables values ( clob_sequence.nextval,    :tablevar   , 'select * from '   ||  :tablevar || ' order by 1');

select * from testTables;

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
   dbms_output.put_line(p_select || ' xml1: ' || my_context);
   -- Do Not omit empty tags 
   dbms_xmlgen.setNullHandling(my_context,2); 
   dbms_output.put_line('next line');
   -- Set the Row Element 
   DBMS_XMLGEN.SETROWSETTAG(my_context,'VALIDATION');
   DBMS_XMLGEN.SETROWTAG(my_context,p_table); 
   xmlresult := DBMS_XMLGEN.GETXML(my_context);

-- Generate XML Output
   dbms_output.put_line('xml results: '  );
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
   dbms_output.put_line('before loop to get text');
   loop
     Fetch tableList into p_table, p_select, p_id ; 
     Exit when tableList%notfound;
     dbms_output.put_line('table: ' || p_table || ' Select: ' || p_select);
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
set linesize 120
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
