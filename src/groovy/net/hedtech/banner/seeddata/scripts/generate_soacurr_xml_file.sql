-- *****************************************************************************************
-- * Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.                    *
-- * This copyrighted software contains confidential and proprietary information of        *
-- * SunGard Higher Education and its subsidiaries. Any use of this software is limited    *
-- * solely to SunGard Higher Education licensees, and is further subject to the terms     *
-- * and conditions of one or more written license agreements between SunGard Higher       *
-- * Education and the licensee in question. SunGard is either a registered trademark or   *
-- * trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.*
-- * Banner and Luminis are either registered trademarks or trademarks of SunGard Higher   *
-- * Education in the U.S.A. and/or other regions and/or countries.                        *
-- *****************************************************************************************




spool &DEFAULT_SPOOL_DIR 
drop table testClob; 
drop table testTables;
drop sequence clob_sequence;
 

var program varchar2(14)
prompt enter PROGRAM
accept  PROGRAM

CREATE TABLE testClob
(
    Id NUMBER,
    Text CLOB,
    CONSTRAINT testClob_Pk PRIMARY KEY (Id)
);
create table testTables 
(  id number,   
   table_name varchar2(30),
   table_select varchar2(2000)
 ); 

create sequence clob_sequence increment by 1;
-- create insert statements here that are part of the module 

begin
  :program :=  '&PROGRAM' || '%';
end ;
/
set verify off
set echo off
set term off
set feedback off
 
insert into testTables values ( clob_sequence.nextval,'SOBCURR','select sobcurr_program  program,	sobcurr.*	FROM 	sobcurr	where 	sobcurr_program like  ''' || :program || ''''  ||
   ' and exists ( select 1 from smrprle where smrprle_program = sobcurr_program and smrprle_user_id = ''GRAILS'' ) ' );
insert into testTables values ( clob_sequence.nextval,'SORMCRL','select sobcurr_program  program,	sormcrl.*	FROM 	sormcrl, sobcurr	where 	sobcurr_curr_rule = sormcrl_curr_rule and sobcurr_program like ''' || :program || ''''
 ||  ' and exists ( select 1 from smrprle where smrprle_program = sobcurr_program and smrprle_user_id = ''GRAILS'' )' );
insert into testTables values ( clob_sequence.nextval,'SORCMJR', 'select sobcurr_program  program,	sorcmjr.*	FROM 	sorcmjr, sobcurr	where 	sobcurr_curr_rule = sorcmjr_curr_rule and sobcurr_program like ''' || :program || ''''
||  ' and exists ( select 1 from stvmajr where stvmajr_code = sorcmjr_majr_code and stvmajr_user_id = ''GRAILS'' ) ' ||
  ' and exists ( select 1 from smrprle where smrprle_program = sobcurr_program and smrprle_user_id = ''GRAILS'' )' ) ;
insert into testTables values ( clob_sequence.nextval,'SORCMNR','select sobcurr_program  program,	sorcMNR.*	FROM 	sorcMNR, sobcurr	where 	sobcurr_curr_rule = sorcMNR_curr_rule and sobcurr_program like ''' || :program || ''''
||  ' and exists ( select 1 from stvmajr where stvmajr_code = sorcmnr_majr_code_minr and stvmajr_user_id = ''GRAILS'') ' ||
  ' and exists ( select 1 from smrprle where smrprle_program = sobcurr_program and smrprle_user_id = ''GRAILS'' )' )  ;
insert into testTables values ( clob_sequence.nextval,'SORCCON','select sobcurr_program  program,	sorcCON.*	FROM 	sorcCON, sobcurr	where 	sobcurr_curr_rule = sorcCON_curr_rule and sobcurr_program like ''' || :program || ''''
||  ' and exists ( select 1 from stvmajr where stvmajr_code = sorccon_majr_code_conc and stvmajr_user_id = ''GRAILS'') ' ||
  ' and exists ( select 1 from smrprle where smrprle_program = sobcurr_program and smrprle_user_id = ''GRAILS'' )' )  ;


set term on
set echo on
set verify on
set feedback on
   
Select Count(*) from testTables; 

set verify off
set echo off
set term off
set feedback off
create Or replace procedure p_xmltable(p_table varchar2, p_select varchar2, p_id number) is 
 
my_context NUMBER := 0; 
xmlresult clob; 
cnt   number := 1 ; 

BEGIN 
  --  dbms_output.put_line('p_table: ' || p_table || ' select: ' || p_select);
  --  delete testClob; 
  
  -- Create a New Context    
   my_context:= DBMS_XMLGEN.NEWCONTEXT (p_select); 
   -- Do Not omit empty tags 
   dbms_xmlgen.setNullHandling(my_context,2); 
  
   -- Set the Row Element 
   DBMS_XMLGEN.SETROWSETTAG(my_context,'CURRICRULE');
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
  p_select varchar2(2000);
  p_id number; 
  cursor tablelist is 
     Select table_name, table_select , id 
     from testTables
     order by id; 
begin 
   Open tableList; 
  loop 
     Fetch tableList into p_table, p_select, p_id ;
  --  dbms_output.put_line('table: ' || p_table || ' pid: ' || p_id  || ' sel: ' || p_select)  ;
     Exit when tableList%notfound;
     p_xmltable(p_table, p_select, p_id);
  End loop ; 
  Close tableList; 
 
End;
/

set term on
set echo on
set verify on
set feedback on

Select Count(*) from testClob ; 

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
Prompt also Remove extra root tags so there is only one <CURRICRULE> And </CURRICRULE>
PROMPT each xml gen will produce a root tag  
spool off; 
