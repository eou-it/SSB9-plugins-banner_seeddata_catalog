-- *****************************************************************************************
-- * Copyright 2014 Ellucian Company L.P. and its affiliates.                         *
-- *****************************************************************************************
--  extract program data, some compliance data and the a student's compliance data for a request
--  run the general studentdata xml file to get the student's data

spool &DEFAULT_SPOOL_DIR
drop table testClob;
drop table testTables;
drop sequence clob_sequence;


var program varchar2(14)
var bannerid varchar2(9);
var pidm number;
var request_no number;
prompt enter PROGRAM
accept  PROGRAM
prompt enter bannerid, press enter if you dont want the student data
accept bannerid
prompt enter request no, press enter if you dont want request data
accept request_no

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
  :program :=  upper('&PROGRAM' || '%');
  if ( '&bannerid' is not null ) then
    :bannerid := upper('&bannerid');
  end if;
  if '&request_no' is not null then
     :request_no :=  to_number('&request_no' ) ;
  end if;
end ;
/
begin
if :bannerid is not null then
  :pidm := gb_common.f_get_pidm(:bannerid);
end if;
end;
/
select :pidm, :request_no, :bannerid, :program from dual;

set verify off
set echo off
set term off
set feedback off
insert into testTables values ( clob_sequence.nextval,'SMRPRLE','select *	FROM 	smrprle	where 	smrprle_program like  ''' || :program || ''''   );

insert into testTables values ( clob_sequence.nextval,'SOBCURR','select sobcurr_program  program,	sobcurr.*	FROM 	sobcurr	where 	sobcurr_program like  ''' || :program || ''''  ||
   ' and exists ( select 1 from smrprle where smrprle_program = sobcurr_program   ) ' );
insert into testTables values ( clob_sequence.nextval,'SORMCRL','select sobcurr_program  program,	sormcrl.*	FROM 	sormcrl, sobcurr	where 	sobcurr_curr_rule = sormcrl_curr_rule and sobcurr_program like ''' || :program || ''''
 ||  ' and exists ( select 1 from smrprle where smrprle_program = sobcurr_program   )' );
insert into testTables values ( clob_sequence.nextval,'SORCMJR', 'select sobcurr_program  program,	sorcmjr.*	FROM 	sorcmjr, sobcurr	where 	sobcurr_curr_rule = sorcmjr_curr_rule and sobcurr_program like ''' || :program || ''''
||  ' and exists ( select 1 from stvmajr where stvmajr_code = sorcmjr_majr_code and stvmajr_user_id = ''GRAILS'' ) ' ||
  ' and exists ( select 1 from smrprle where smrprle_program = sobcurr_program   )' ) ;
insert into testTables values ( clob_sequence.nextval,'SORCMNR','select sobcurr_program  program,	sorcMNR.*	FROM 	sorcMNR, sobcurr	where 	sobcurr_curr_rule = sorcMNR_curr_rule and sobcurr_program like ''' || :program || ''''
||  ' and exists ( select 1 from stvmajr where stvmajr_code = sorcmnr_majr_code_minr and stvmajr_user_id = ''GRAILS'') ' ||
  ' and exists ( select 1 from smrprle where smrprle_program = sobcurr_program and smrprle_user_id = ''GRAILS'' )' )  ;
insert into testTables values ( clob_sequence.nextval,'SORCCON','select sobcurr_program  program,	sorcCON.*	FROM 	sorcCON, sobcurr	where 	sobcurr_curr_rule = sorcCON_curr_rule and sobcurr_program like ''' || :program || ''''
||  ' and exists ( select 1 from stvmajr where stvmajr_code = sorccon_majr_code_conc and stvmajr_user_id = ''GRAILS'') ' ||
  ' and exists ( select 1 from smrprle where smrprle_program = sobcurr_program   )' )  ;
-- program rules

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMBPGEN','select  	SMBPGEN.* FROM SMBPGEN where SMBPGEN_program like  ''' || :program || ''''   );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMBPVPR','select  	SMBPVPR.* FROM SMBPVPR where SMBPVPR_program like  ''' || :program || ''''   );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPAAP','select  	SMRPAAP.* FROM SMRPAAP where SMRPAAP_program like  ''' || :program || ''''   );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPATR','select  	SMRPATR.* FROM SMRPATR where SMRPATR_program like  ''' || :program || ''''   );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPCMT','select  	SMRPCMT.* FROM SMRPCMT where SMRPCMT_program like  ''' || :program || ''''   );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPLVL','select  	SMRPLVL.* FROM SMRPLVL where SMRPLVL_program like  ''' || :program || ''''   );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPNCR','select  	SMRPNCR.* FROM SMRPNCR where SMRPNCR_program like  ''' || :program || ''''   );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPOAN','select  	SMRPOAN.* FROM SMRPOAN where SMRPOAN_program like  ''' || :program || ''''   );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPRGC','select  	SMRPRGC.* FROM SMRPRGC where SMRPRGC_program like  ''' || :program || ''''   );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPRGD','select  	SMRPRGD.* FROM SMRPRGD where SMRPRGD_program like  ''' || :program || ''''   );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPRSA','select  	SMRPRSA.* FROM SMRPRSA where SMRPRSA_program like  ''' || :program || ''''   );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPRSC','select  	SMRPRSC.* FROM SMRPRSC where SMRPRSC_program like  ''' || :program || ''''   );
-- areas - only courses attached and a rule on the courses
insert into testTables values ( clob_sequence.nextval,'SMRALIB','select SMRALIB.*	FROM SMRALIB, smrpaap where SMRALIB_area = smrpaap_area and  smrpaap_program like  ''' || :program || ''''   );
insert into testTables values ( clob_sequence.nextval,'SMBAGEN','select SMBAGEN.*	FROM SMBAGEN, smrpaap where SMBAGEN_area = smrpaap_area and  smrpaap_program like ''' || :program || ''''   );
insert into testTables values ( clob_sequence.nextval,'SMRACAA','select SMRACAA.*	FROM SMRACAA, smrpaap where SMRACAA_area = smrpaap_area and  smrpaap_program like ''' || :program || ''''   );
insert into testTables values ( clob_sequence.nextval,'SMBAGRL','select SMBAGRL.*	FROM SMBAGRL, smrpaap where SMBAGRL_area = smrpaap_area and  smrpaap_program like  ''' || :program || ''''   );
insert into testTables values ( clob_sequence.nextval,'SMBARUL','select SMBARUL.*	FROM SMBARUL, smrpaap where SMBARUL_area = smrpaap_area and  smrpaap_program like ''' || :program || ''''   );
insert into testTables values ( clob_sequence.nextval,'SMRARUL','select SMRARUL.*	FROM SMRARUL, smrpaap where SMRARUL_area = smrpaap_area and  smrpaap_program like ''' || :program || ''''   );

-- request for compliance
begin
if :bannerid is not null and :request_no is not null then
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRRQCM' ,'select   '''  ||:bannerid|| '''  bannerid, SMRRQCM.* from SMRRQCM where SMRRQCM_request_no = ' || :request_no ||' and SMRRQCM_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMBAOGN' ,'select   '''  ||:bannerid|| '''  bannerid, SMBAOGN.* from SMBAOGN where SMBAOGN_request_no = ' || :request_no ||' and SMBAOGN_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMBDRRQ' ,'select   '''  ||:bannerid|| '''  bannerid, SMBDRRQ.* from SMBDRRQ where SMBDRRQ_request_no = ' || :request_no ||' and SMBDRRQ_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMBGOGN' ,'select   '''  ||:bannerid|| '''  bannerid, SMBGOGN.* from SMBGOGN where SMBGOGN_request_no = ' || :request_no ||' and SMBGOGN_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMBGRRQ' ,'select   '''  ||:bannerid|| '''  bannerid, SMBGRRQ.* from SMBGRRQ where SMBGRRQ_request_no = ' || :request_no ||' and SMBGRRQ_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMBPOGN' ,'select   '''  ||:bannerid|| '''  bannerid, SMBPOGN.* from SMBPOGN where SMBPOGN_request_no = ' || :request_no ||' and SMBPOGN_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRAOGD' ,'select   '''  ||:bannerid|| '''  bannerid, SMRAOGD.* from SMRAOGD where SMRAOGD_request_no = ' || :request_no ||' and SMRAOGD_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRAOLV' ,'select   '''  ||:bannerid|| '''  bannerid, SMRAOLV.* from SMRAOLV where SMRAOLV_request_no = ' || :request_no ||' and SMRAOLV_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRAOSA' ,'select   '''  ||:bannerid|| '''  bannerid, SMRAOSA.* from SMRAOSA where SMRAOSA_request_no = ' || :request_no ||' and SMRAOSA_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRAOST' ,'select   '''  ||:bannerid|| '''  bannerid, SMRAOST.* from SMRAOST where SMRAOST_request_no = ' || :request_no ||' and SMRAOST_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRCORQ' ,'select   '''  ||:bannerid|| '''  bannerid, SMRCORQ.* from SMRCORQ where SMRCORQ_request_no = ' || :request_no ||' and SMRCORQ_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRCXML' ,'select   '''  ||:bannerid|| '''  bannerid, SMRCXML.* from SMRCXML where SMRCXML_request_no = ' || :request_no ||' and SMRCXML_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRDOAN' ,'select   '''  ||:bannerid|| '''  bannerid, SMRDOAN.* from SMRDOAN where SMRDOAN_request_no = ' || :request_no ||' and SMRDOAN_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRDOCN' ,'select   '''  ||:bannerid|| '''  bannerid, SMRDOCN.* from SMRDOCN where SMRDOCN_request_no = ' || :request_no ||' and SMRDOCN_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRDOEX' ,'select   '''  ||:bannerid|| '''  bannerid, SMRDOEX.* from SMRDOEX where SMRDOEX_request_no = ' || :request_no ||' and SMRDOEX_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRDOLV' ,'select   '''  ||:bannerid|| '''  bannerid, SMRDOLV.* from SMRDOLV where SMRDOLV_request_no = ' || :request_no ||' and SMRDOLV_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRDORJ' ,'select   '''  ||:bannerid|| '''  bannerid, SMRDORJ.* from SMRDORJ where SMRDORJ_request_no = ' || :request_no ||' and SMRDORJ_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRDORQ' ,'select   '''  ||:bannerid|| '''  bannerid, SMRDORQ.* from SMRDORQ where SMRDORQ_request_no = ' || :request_no ||' and SMRDORQ_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRDOST' ,'select   '''  ||:bannerid|| '''  bannerid, SMRDOST.* from SMRDOST where SMRDOST_request_no = ' || :request_no ||' and SMRDOST_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRDOUS' ,'select   '''  ||:bannerid|| '''  bannerid, SMRDOUS.* from SMRDOUS where SMRDOUS_request_no = ' || :request_no ||' and SMRDOUS_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRDREX' ,'select   '''  ||:bannerid|| '''  bannerid, SMRDREX.* from SMRDREX where SMRDREX_request_no = ' || :request_no ||' and SMRDREX_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRDRLV' ,'select   '''  ||:bannerid|| '''  bannerid, SMRDRLV.* from SMRDRLV where SMRDRLV_request_no = ' || :request_no ||' and SMRDRLV_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRDRRQ' ,'select   '''  ||:bannerid|| '''  bannerid, SMRDRRQ.* from SMRDRRQ where SMRDRRQ_request_no = ' || :request_no ||' and SMRDRRQ_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRGOAT' ,'select   '''  ||:bannerid|| '''  bannerid, SMRGOAT.* from SMRGOAT where SMRGOAT_request_no = ' || :request_no ||' and SMRGOAT_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRGOGD' ,'select   '''  ||:bannerid|| '''  bannerid, SMRGOGD.* from SMRGOGD where SMRGOGD_request_no = ' || :request_no ||' and SMRGOGD_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRGOLV' ,'select   '''  ||:bannerid|| '''  bannerid, SMRGOLV.* from SMRGOLV where SMRGOLV_request_no = ' || :request_no ||' and SMRGOLV_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRGOSA' ,'select   '''  ||:bannerid|| '''  bannerid, SMRGOSA.* from SMRGOSA where SMRGOSA_request_no = ' || :request_no ||' and SMRGOSA_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRGRRQ' ,'select   '''  ||:bannerid|| '''  bannerid, SMRGRRQ.* from SMRGRRQ where SMRGRRQ_request_no = ' || :request_no ||' and SMRGRRQ_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPCRS' ,'select   '''  ||:bannerid|| '''  bannerid, SMRPCRS.* from SMRPCRS where SMRPCRS_request_no = ' || :request_no ||' and SMRPCRS_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPOAN' ,'select   '''  ||:bannerid|| '''  bannerid, SMRPOAN.* from SMRPOAN where SMRPOAN_request_no = ' || :request_no ||' and SMRPOAN_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPOAT' ,'select   '''  ||:bannerid|| '''  bannerid, SMRPOAT.* from SMRPOAT where SMRPOAT_request_no = ' || :request_no ||' and SMRPOAT_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPOGD' ,'select   '''  ||:bannerid|| '''  bannerid, SMRPOGD.* from SMRPOGD where SMRPOGD_request_no = ' || :request_no ||' and SMRPOGD_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPOLV' ,'select   '''  ||:bannerid|| '''  bannerid, SMRPOLV.* from SMRPOLV where SMRPOLV_request_no = ' || :request_no ||' and SMRPOLV_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPONC' ,'select   '''  ||:bannerid|| '''  bannerid, SMRPONC.* from SMRPONC where SMRPONC_request_no = ' || :request_no ||' and SMRPONC_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPOSA' ,'select   '''  ||:bannerid|| '''  bannerid, SMRPOSA.* from SMRPOSA where SMRPOSA_request_no = ' || :request_no ||' and SMRPOSA_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPRRQ' ,'select   '''  ||:bannerid|| '''  bannerid, SMRPRRQ.* from SMRPRRQ where SMRPRRQ_request_no = ' || :request_no ||' and SMRPRRQ_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMTASEL' ,'select   '''  ||:bannerid|| '''  bannerid, SMTASEL.* from SMTASEL where SMTASEL_request_no = ' || :request_no ||' and SMTASEL_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMTCRSE' ,'select   '''  ||:bannerid|| '''  bannerid, SMTCRSE.* from SMTCRSE where SMTCRSE_request_no = ' || :request_no ||' and SMTCRSE_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMTCUSE' ,'select   '''  ||:bannerid|| '''  bannerid, SMTCUSE.* from SMTCUSE where SMTCUSE_request_no = ' || :request_no ||' and SMTCUSE_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMTRUSE' ,'select   '''  ||:bannerid|| '''  bannerid, SMTRUSE.* from SMTRUSE where SMTRUSE_request_no = ' || :request_no ||' and SMTRUSE_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMTSPAT' ,'select   '''  ||:bannerid|| '''  bannerid, SMTSPAT.* from SMTSPAT where SMTSPAT_request_no = ' || :request_no ||' and SMTSPAT_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMTSPLT' ,'select   '''  ||:bannerid|| '''  bannerid, SMTSPLT.* from SMTSPLT where SMTSPLT_request_no = ' || :request_no ||' and SMTSPLT_pidm = ' || :pidm  );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMTSTRG' ,'select   '''  ||:bannerid|| '''  bannerid, SMTSTRG.* from SMTSTRG where SMTSTRG_request_no = ' || :request_no ||' and SMTSTRG_pidm = ' || :pidm  );
end if;
end;
/

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
  -- dbms_output.put_line('p_table: ' || p_table || ' select: ' || p_select);
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
set verify on
set echo on
set term on
set feedback on
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
    -- dbms_output.put_line('table: ' || p_table || ' pid: ' || p_id  || ' sel: ' || p_select)  ;
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
