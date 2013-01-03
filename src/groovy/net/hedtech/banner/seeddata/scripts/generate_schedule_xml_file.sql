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




--spool  &DEFAULT_SPOOL_DIR/scheduleData.xml
spool   scheduleData.xml
var term varchar2(9)
define termvar char
prompt enter TERM
accept  termvar

begin
  :term :=  '&termvar';
end ;
/

set term off
set echo off
set verify off

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


insert into testTables values ( clob_sequence.nextval, 'SSBSECT', 'select * from SSBSECT where ssbsect_term_code = '''   ||  :term || '''   order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSBDESC', 'select * from SSBDESC where ssbdesc_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRATTR', 'select * from SSRATTR where ssrattr_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRBLCK', 'select * from SSRBLCK where ssrblck_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRCLBD', 'select * from SSRCLBD where ssrclbd_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRCORQ', 'select * from SSRCORQ where ssrcorq_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSREVAL', 'select * from SSREVAL where ssreval_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRFEES', 'select * from SSRFEES where ssrfees_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRLINK', 'select * from SSRLINK  where ssrlink_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRMEET', 'select * from SSRMEET where ssrmeet_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRMPRT', 'select * from SSRMPRT where ssrmprt_term_code = '''   ||  :term || '''   order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRMRDF', 'select * from SSRMRDF  where ssrmrdf_term_code = '''   ||  :term || '''   order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRRARE', 'select * from SSRRARE where ssrrare_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRRATT', 'select * from SSRRATT where ssrratt_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRRCHR', 'select * from SSRRCHR where ssrrchr_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SRRRCLS', 'select * from SSRRCLS where ssrrcls_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRRCMP', 'select * from SSRRCMP where ssrrcmp_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRRCOL', 'select * from SSRRCOL where ssrrcol_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRRDEG', 'select * from SSRRDEG where ssrrdeg_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRRDEP', 'select * from SSRRDEP where ssrrdep_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRRESV', 'select * from SSRRESV where ssrresv_term_code = '''   ||  :term || '''   order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRRLVL', 'select * from SSRRLVL where ssrrlvl_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRRMAJ', 'select * from SSRRMAJ where ssrrmaj_term_code = '''   ||  :term || '''   order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRRPRG', 'select * from SSRRPRG where ssrrprg_term_code = '''   ||  :term || '''   order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRRTST', 'select * from SSRRTST where ssrrtst_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRSCCD', 'select * from SSRSCCD where ssrsccd_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRSPRT', 'select * from SSRSPRT where ssrsprt_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRSRDF', 'select * from SSRSRDF where ssrsrdf_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRSYLN', 'select * from SSRSYLN  where ssrsyln_term_code = '''   ||  :term || '''   order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRSYLO', 'select * from SSRSYLO  where ssrsylo_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRSYRM', 'select * from SSRSYRM where ssrsyrm_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRSYTR', 'select * from SSRSYTR where ssrsytr_term_code = '''   ||  :term || '''   order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRTEXT', 'select * from SSRTEXT where ssrtext_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRXLST', 'select * from SSRXLST where ssrxlst_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRRSTS', 'select * from SSRRSTS where ssrrsts_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SPRIDEN', 'select spriden_id bannerid,SPRIDEN.* FROM SPRIDEN where spriden_change_ind is null and SPRIDEN_pidm in ( select sirasgn_pidm from sirasgn where  sirasgn_term_code = '''   ||  :term || ''')'   );
insert into testTables values (clob_sequence.nextval,  'SIBINST', 'select spriden_id bannerid,SIBINST.* from spriden, SIBINST where spriden_pidm = sibinst_pidm and spriden_change_ind is null and sibinst_pidm in ( select sirasgn_pidm from sirasgn where  sirasgn_term_code = '''   ||  :term || ''')'   );
insert into testTables values (clob_sequence.nextval,  'SIRASGN', 'select spriden_id bannerid,SIRASGN.* from spriden,SIrasgn where spriden_pidm = sirasgn_pidm and spriden_change_ind is null and sirasgn_term_code = '''   ||  :term || '''  order by 1,2');


insert into testTables values ( clob_sequence.nextval, 'SSREXTN', 'select * from SSREXTN where ssrextn_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SSRRFND', 'select * from SSRRFND  where ssrrfnd_term_code = '''   ||  :term || '''   order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SORRSTS', 'select * from SORRSTS where sorrsts_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SOREXTN', 'select * from SOREXTN where sorextn_term_code = '''   ||  :term || '''    order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SORRFND', 'select * from SORRFND  where sorrfnd_term_code = '''   ||  :term || '''   order by 1,2');
insert into testTables values ( clob_sequence.nextval, 'SOBTERM', 'select * from SOBTERM  where SOBTERM_term_code = '''   ||  :term || '''   order by 1,2');

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
