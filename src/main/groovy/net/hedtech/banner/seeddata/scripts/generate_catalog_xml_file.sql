-- *****************************************************************************************
-- * Copyright 2010-2013 Ellucian Company L.P. and its affiliates.                         *
-- *****************************************************************************************




spool  catalogData.xml

var subject varchar2(4)
define subjectvar char
prompt enter SUBJECT
accept  subjectvar

begin
  :subject :=  '&subjectvar';
end ;
/
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
   table_name varchar2(30),
   table_select varchar2(4000)
 );
 create sequence clob_sequence increment by 1;
-- create insert statements here that are part of the module 
 
insert into testTables values (clob_sequence.nextval,'SCBCDEP', 'select * from scbcdep where scbcdep_coll_code = ''00'' and scbcdep_dept_code = ''BIOL'' ');
insert into testTables values (clob_sequence.nextval,'SCRCDTX', 'select * from scrcdtx where scrcdtx_coll_code = ''00'' and scrcdtx_dept_code = ''BIOL''');
insert into testTables values (clob_sequence.nextval,'SCBCDTL', 'select * from SCBCDTL where SCBCDTL_coll_code = ''00'' and SCBCDTL_dept_code = ''BIOL''');

insert into testTables values ( clob_sequence.nextval, 'SCBCRSE',
'SELECT   SCBCRSE_SUBJ_CODE, SCBCRSE_CRSE_NUMB,  SCBCRSE_EFF_TERM,   SCBCRSE_CSTA_CODE,  SCBCRSE_REPEAT_LIMIT,  SCBCRSE_MAX_RPT_UNITS,
   SCBCRSE_REPS_CODE,   SCBCRSE_COLL_CODE,  SCBCRSE_DEPT_CODE,SCBCRSE_DIVS_CODE, SCBCRSE_CIPC_CODE, SCBCRSE_CEU_IND,
   SCBCRSE_CREDIT_HR_LOW, SCBCRSE_CREDIT_HR_HIGH, SCBCRSE_CREDIT_HR_IND, SCBCRSE_BILL_HR_LOW,  SCBCRSE_BILL_HR_HIGH,
   SCBCRSE_BILL_HR_IND,   SCBCRSE_CONT_HR_LOW,   SCBCRSE_CONT_HR_HIGH,   SCBCRSE_CONT_HR_IND,   SCBCRSE_LEC_HR_LOW,
   SCBCRSE_LEC_HR_HIGH,   SCBCRSE_LEC_HR_IND,   SCBCRSE_LAB_HR_LOW,   SCBCRSE_LAB_HR_HIGH,   SCBCRSE_LAB_HR_IND,
   SCBCRSE_OTH_HR_LOW, SCBCRSE_OTH_HR_HIGH,  SCBCRSE_OTH_HR_IND,  SCBCRSE_TITLE, SCBCRSE_APRV_CODE, SCBCRSE_PREREQ_CHK_METHOD_CDE,
    SCBCRSE_DUNT_CODE ,   SCBCRSE_NUMBER_OF_UNITS ,   substr(f_default_gmod (scbcrse_subj_code, scbcrse_crse_numb, scbcrse_eff_term),1,1) SCRGMOD_GMOD_CODE,
     substr(f_get_course_levels
         (scbcrse_subj_code,scbcrse_crse_numb,scbcrse_eff_term,1),1,2) SCRLEVL_LEVL_CODE1,
    ( select scrschd_schd_code from scrschd 
         where scrschd_subj_code = scbcrse_subj_code
          and scrschd_crse_numb =  scbcrse_crse_numb
           and scrschd_eff_term =  scbcrse_eff_term 
           and rownum = 1)    SCRSCHD_SCHD_CODE,
    ( select  SCRSChd_WORKLOAD from scrschd 
         where scrschd_subj_code = scbcrse_subj_code
          and scrschd_crse_numb =  scbcrse_crse_numb
           and scrschd_eff_term =  scbcrse_eff_term 
           and rownum = 1) SCRSChd_WORKLOAD,
   ( select SCRSChd_MAX_ENRL from scrschd 
         where scrschd_subj_code = scbcrse_subj_code
          and scrschd_crse_numb =  scbcrse_crse_numb
           and scrschd_eff_term =  scbcrse_eff_term 
           and rownum = 1) SCRSChd_MAX_ENRL,
    ( select scrschd_adj_workload from scrschd 
         where scrschd_subj_code = scbcrse_subj_code
          and scrschd_crse_numb =  scbcrse_crse_numb
           and scrschd_eff_term =  scbcrse_eff_term 
           and rownum = 1) scrschd_adj_workload,
    scbcrse_capp_prereq_test_ind,
    scbcrse_pwav_code,
    scbcrse_tuiw_ind   ,
     scbcrse_add_fees_ind     
  FROM    SCBCRSE 
   where scbcrse_subj_code = '''   ||  :subject || '''
   order by 3,1,2 '); 
   
insert into testTables values (clob_sequence.nextval,'SCBDESC', 'select * from SCBDESC where SCBDESC_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCBSUPP', 'select * from SCBSUPP where SCBSUPP_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRATTR', 'select * from SCRATTR where SCRATTR_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRCORQ', 'select * from SCRCORQ where SCRCORQ_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRCPRT', 'select * from SCRCPRT where SCRCPRT_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRCRDF', 'select * from SCRCRDF where SCRCRDF_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRCLBD', 'select * from SCRCLBD where SCRCLBD_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCREQIV', 'select * from SCREQIV where SCREQIV_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRFEES', 'select * from SCRFEES where SCRFEES_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRGMOD', 'select * from SCRGMOD where SCRGMOD_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRINTG', 'select * from SCRINTG where SCRINTG_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRLEVL', 'select * from SCRLEVL where SCRLEVL_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRMEXC', 'select * from SCRMEXC where SCRMEXC_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRRARE', 'select * from SCRRARE where SCRRARE_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRRATT', 'select * from SCRRATT where SCRRATT_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRRCAM', 'select * from SCRRCAM where SCRRCAM_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRRCHR', 'select * from SCRRCHR where SCRRCHR_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRRCLS', 'select * from SCRRCLS where SCRRCLS_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRRCMP', 'select * from SCRRCMP where SCRRCMP_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRRCOL', 'select * from SCRRCOL where SCRRCOL_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRRDEG', 'select * from SCRRDEG where SCRRDEG_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRRDEP', 'select * from SCRRDEP where SCRRDEP_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRRLVL', 'select * from SCRRLVL where SCRRLVL_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRRMAJ', 'select * from SCRRMAJ where SCRRMAJ_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRRPRG', 'select * from SCRRPRG where SCRRPRG_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRRTRM', 'select * from SCRRTRM where SCRRTRM_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRRTST', 'select * from SCRRTST where SCRRTST_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRSBGI', 'select * from SCRSBGI where SCRSBGI_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRSPRT', 'select * from SCRSPRT where SCRSPRT_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRSYLN', 'select * from SCRSYLN where SCRSYLN_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRSYLO', 'select * from SCRSYLO where SCRSYLO_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRSYRM', 'select * from SCRSYRM where SCRSYRM_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRSYTR', 'select * from SCRSYTR where SCRSYTR_subj_code = '''   ||  :subject || '''');
insert into testTables values (clob_sequence.nextval,'SCRTEXT', 'select * from SCRTEXT where SCRTEXT_subj_code = '''   ||  :subject || '''');
Select Count(*) from testTables; 

 
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
   DBMS_XMLGEN.SETROWSETTAG(my_context,'CATALOGSELENIUM'); 
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
  p_select varchar2(4000); 
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
     p_xmltable(p_table, p_select, p_id); 
  End loop ; 
  Close tableList; 
 
End;
/ 
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
Prompt also Remove extra root tags so there is only one <CATALOGSELENIUM> And <?CATALOGSELENIUM>
PROMPT each xml gen will produce a root tag  
spool off; 
