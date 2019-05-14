-- *****************************************************************************************
-- * Copyright 2014 Ellucian Company L.P. and its affiliates.                         *
-- *****************************************************************************************

spool  bcmData.xml
var folder  varchar2(30);
prompt enter folder name
accept  folder

begin
  :folder :=  '&folder';
end ;
/


set term off
set echo off
set verify off
Alter Session Set  NLS_TIMESTAMP_FORMAT = 'YYYY-MM-DD HH24:MI:SS.FF';
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


insert into testTables values ( clob_sequence.nextval, 'GCRFLDR', 'select gcrfldr_name folder, gcrfldr.* from GCRFLDR where GCRFLDR_name =  '''   ||  :folder || ''' ');
insert into testTables values ( clob_sequence.nextval, 'GCRCFLD', 'select gcrfldr_name folder, GCRCFLD.* from GCRCFLD,GCRFLDR  where gcrfldr_surrogate_id = gcrcfld_folder_id and GCRFLDR_name = '''   ||  :folder || ''' ');
insert into testTables values ( clob_sequence.nextval, 'GCBQURY', 'select gcrfldr_name folder, GCBQURY.* from GCBQURY,GCRFLDR  where gcrfldr_surrogate_id = GCBQURY_folder_id and GCRFLDR_name =  '''   ||  :folder || ''' ');
insert into testTables values ( clob_sequence.nextval, 'GCBTMPL', 'select gcrfldr_name folder, GCBTMPL.* from GCBTMPL,GCRFLDR  where gcrfldr_surrogate_id = GCBTMPL_folder_id and GCRFLDR_name =  '''   ||  :folder || '''  ');
insert into testTables values ( clob_sequence.nextval, 'GCBEMTL', 'select gcrfldr_name folder, GCBTMPL_NAME template,  GCBEMTL.* from GCBEMTL, GCBTMPL,GCRFLDR where GCBEMTL_surrogate_id = GCBTMPL_surrogate_id and gcrfldr_surrogate_id = GCBTMPL_folder_id and GCRFLDR_name =  '''   ||  :folder || '''  ');

set term on
set echo on
set verify on

Select Count(*) TableCnt from testTables; 

-set erm off
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
   DBMS_XMLGEN.SETROWSETTAG(my_context,'COMMUNICATIONMANGEMENT');
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
