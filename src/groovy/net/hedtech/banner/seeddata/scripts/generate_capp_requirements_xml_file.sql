-- *****************************************************************************************
-- * Copyright 2014 - 2016 Ellucian Company L.P. and its affiliates.                         *
-- *****************************************************************************************
--  
-- Script to create facilitate moving Compliance Program requirements between instances.
-- The output will need to be editted to create a valid XML document, for which it can then
-- be used as an input for seed-data target.
--
spool &DEFAULT_SPOOL_DIR
drop table testClob;
drop table testTables;
drop sequence clob_sequence;


var program varchar2(14);
var effectiveTerm varchar2(7);
prompt Enter PROGRAM
accept PROGRAM

CREATE TABLE testClob
(
    Id NUMBER,
    Text CLOB,
    CONSTRAINT testClob_Pk PRIMARY KEY (Id)
);

CREATE TABLE testTables
(  id number,
   table_name varchar2(30),
   table_select varchar2(2000)
 );

CREATE SEQUENCE clob_sequence increment by 1;

BEGIN
  :program :=  upper('&PROGRAM');
END;
/
SELECT :program FROM DUAL;

set verify off
set echo off
set term off
set feedback off
--
-- Program requirements
--
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPRLE','SELECT * FROM SMRPRLE WHERE SMRPRLE_PROGRAM LIKE  ''' || :program || ''''   );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SOBCURR','SELECT * FROM SOBCURR WHERE SOBCURR_PROGRAM LIKE  ''' || :program || ''''   );

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SORMCRL','SELECT SOBCURR_PROGRAM program, SORMCRL.* FROM SORMCRL, SOBCURR WHERE SOBCURR_CURR_RULE = SORMCRL_CURR_RULE AND SOBCURR_PROGRAM LIKE  ''' || :program || ''''
 ||  ' AND EXISTS  ( SELECT  1 FROM SMRPRLE WHERE SMRPRLE_PROGRAM = SOBCURR_PROGRAM  )' );

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SORCMJR', 'SELECT SOBCURR_PROGRAM program,  SORCMJR.* FROM  SORCMJR, SOBCURR WHERE SOBCURR_CURR_RULE = SORCMJR_CURR_RULE AND SOBCURR_PROGRAM LIKE ''' || :program || ''''
||  ' AND EXISTS ( SELECT 1 FROM  STVMAJR WHERE STVMAJR_CODE = SORCMJR_MAJR_CODE AND STVMAJR_USER_ID = ''GRAILS'' ) ' ||
  ' AND EXISTS ( SELECT  1 FROM SMRPRLE WHERE SMRPRLE_PROGRAM = SOBCURR_PROGRAM   )' ) ;

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SORCMNR','SELECT SOBCURR_PROGRAM program, sorcMNR.* FROM  SORCMNR, SOBCURR WHERE   SOBCURR_CURR_RULE = SORCMNR_CURR_RULE AND SOBCURR_PROGRAM LIKE ''' || :program || ''''
||  ' AND EXISTS ( SELECT 1 FROM  STVMAJR WHERE STVMAJR_CODE = SORCMNR_MAJR_CODE_MINR AND STVMAJR_USER_ID = ''GRAILS'') ' ||
  ' AND EXISTS ( SELECT 1 FROM SMRPRLE WHERE SMRPRLE_PROGRAM = SOBCURR_PROGRAM AND SMRPRLE_USER_ID = ''GRAILS'' )' )  ;

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SORCCON','SELECT SOBCURR_PROGRAM program, sorcCON.* FROM  SORCCON, SOBCURR WHERE   SOBCURR_CURR_RULE = SORCCON_CURR_RULE AND SOBCURR_PROGRAM LIKE ''' || :program || ''''
||  ' AND EXISTS ( SELECT 1 FROM  STVMAJR WHERE STVMAJR_CODE = SORCCON_MAJR_CODE_CONC AND STVMAJR_USER_ID = ''GRAILS'') ' ||
  ' AND EXISTS ( SELECT 1 FROM SMRPRLE WHERE SMRPRLE_PROGRAM = SOBCURR_PROGRAM   )' )  ;

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMBPGEN','SELECT * FROM SMBPGEN WHERE SMBPGEN_PROGRAM LIKE  ''' || :program || ''''   );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRALIB','SELECT * FROM SMRALIB WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRALIB sel,  SMRPAAP
                                                                                                       WHERE sel.SMRALIB_AREA = SMRALIB.SMRALIB_AREA
                                                                                                         AND sel.SMRALIB_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 


INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPCMT','SELECT * FROM SMRPCMT WHERE SMRPCMT_PROGRAM LIKE  ''' || :program || ''''   );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPNCR','SELECT * FROM SMRPNCR WHERE SMRPNCR_PROGRAM LIKE  ''' || :program || ''''   );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPLVL','SELECT * FROM SMRPLVL WHERE SMRPLVL_PROGRAM LIKE  ''' || :program || ''''   );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPATR','SELECT * FROM SMRPATR WHERE SMRPATR_PROGRAM LIKE  ''' || :program || ''''   );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPRSA','SELECT * FROM SMRPRSA WHERE SMRPRSA_PROGRAM LIKE  ''' || :program || ''''   );
   INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPRSC','SELECT * FROM SMRPRSC WHERE SMRPRSC_PROGRAM LIKE  ''' || :program || ''''   );
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPRGD','SELECT * FROM SMRPRGD WHERE SMRPRGD_PROGRAM LIKE  ''' || :program || ''''   );   
   INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPRGC','SELECT * FROM SMRPRGC WHERE SMRPRGC_PROGRAM LIKE  ''' || :program || ''''   );   
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRPAAP','SELECT * FROM SMRPAAP WHERE SMRPAAP_PROGRAM LIKE  ''' || :program || ''''   );      
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMBPVPR','SELECT * FROM SMBPVPR WHERE SMBPVPR_PROGRAM LIKE  ''' || :program || ''''   );      
-- 
-- Area requirements from SMAALIB and SMAAQUA
--
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRAQUA','SELECT * FROM SMRAQUA WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRAQUA sel,  SMRPAAP
                                                                                                       WHERE sel.SMRAQUA_AREA = SMRAQUA.SMRAQUA_AREA
                                                                                                         AND sel.SMRAQUA_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRIECP','SELECT * FROM SMRIECP WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRIECP sel,  SMRPAAP
                                                                                                       WHERE sel.SMRIECP_AREA = SMRIECP.SMRIECP_AREA
                                                                                                         AND sel.SMRIECP_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 


INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRIECO','SELECT * FROM SMRIECO WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRIECO sel,  SMRPAAP
                                                                                                       WHERE sel.SMRIECO_AREA = SMRIECO.SMRIECO_AREA
                                                                                                         AND sel.SMRIECO_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 


INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRIEDG','SELECT * FROM SMRIEDG WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRIEDG sel,  SMRPAAP
                                                                                                       WHERE sel.SMRIEDG_AREA = SMRIEDG.SMRIEDG_AREA
                                                                                                         AND sel.SMRIEDG_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRIEDP','SELECT * FROM SMRIEDP WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRIEDP sel,  SMRPAAP
                                                                                                       WHERE sel.SMRIEDP_AREA = SMRIEDP.SMRIEDP_AREA
                                                                                                         AND sel.SMRIEDP_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRIEMJ','SELECT * FROM SMRIEMJ WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRIEMJ sel,  SMRPAAP
                                                                                                       WHERE sel.SMRIEMJ_AREA = SMRIEMJ.SMRIEMJ_AREA
                                                                                                         AND sel.SMRIEMJ_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRIECC','SELECT * FROM SMRIECC WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRIECC sel,  SMRPAAP
                                                                                                       WHERE sel.SMRIECC_AREA = SMRIECC.SMRIECC_AREA
                                                                                                         AND sel.SMRIECC_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRIEMN','SELECT * FROM SMRIEMN WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRIEMN sel,  SMRPAAP
                                                                                                       WHERE sel.SMRIEMN_AREA = SMRIEMN.SMRIEMN_AREA
                                                                                                         AND sel.SMRIEMN_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRIEAT','SELECT * FROM SMRIEAT WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRIEAT sel,  SMRPAAP
                                                                                                       WHERE sel.SMRIEAT_AREA = SMRIEAT.SMRIEAT_AREA
                                                                                                         AND sel.SMRIEAT_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 
--
-- Area requirements from SMAAREA
-- 
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMBAGEN','SELECT * FROM SMBAGEN WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMBAGEN sel,  SMRPAAP
                                                                                                       WHERE sel.SMBAGEN_AREA = SMBAGEN.SMBAGEN_AREA
                                                                                                         AND sel.SMBAGEN_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRACMT','SELECT * FROM SMRACMT WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRACMT sel,  SMRPAAP
                                                                                                       WHERE sel.SMRACMT_AREA = SMRACMT.SMRACMT_AREA
                                                                                                         AND sel.SMRACMT_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRALVL','SELECT * FROM SMRALVL WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRALVL sel,  SMRPAAP
                                                                                                       WHERE sel.SMRALVL_AREA = SMRALVL.SMRALVL_AREA
                                                                                                         AND sel.SMRALVL_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRARSA','SELECT * FROM SMRARSA WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRARSA sel,  SMRPAAP
                                                                                                       WHERE sel.SMRARSA_AREA = SMRARSA.SMRARSA_AREA
                                                                                                         AND sel.SMRARSA_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRARSC','SELECT * FROM SMRARSC WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRARSC sel,  SMRPAAP
                                                                                                       WHERE sel.SMRARSC_AREA = SMRARSC.SMRARSC_AREA
                                                                                                         AND sel.SMRARSC_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRARGD','SELECT * FROM SMRARGD WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRARGD sel,  SMRPAAP
                                                                                                       WHERE sel.SMRARGD_AREA = SMRARGD.SMRARGD_AREA
                                                                                                         AND sel.SMRARGD_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRARGC','SELECT * FROM SMRARGC WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRARGC sel,  SMRPAAP
                                                                                                       WHERE sel.SMRARGC_AREA = SMRARGC.SMRARGC_AREA
                                                                                                         AND sel.SMRARGC_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRAGAM','SELECT * FROM SMRAGAM WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRAGAM sel,  SMRPAAP
                                                                                                       WHERE sel.SMRAGAM_AREA = SMRAGAM.SMRAGAM_AREA
                                                                                                         AND sel.SMRAGAM_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

   INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMBAGRL','SELECT * FROM SMBAGRL WHERE EXISTS ( SELECT 1 
                                                                                                           FROM SMBAGRL sel,  SMRPAAP
                                                                                                          WHERE sel.SMBAGRL_AREA = SMBAGRL.SMBAGRL_AREA
                                                                                                            AND sel.SMBAGRL_AREA = SMRPAAP_AREA
                                                                                                            AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

   INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRAGRT','SELECT * FROM SMRAGRT WHERE EXISTS ( SELECT 1 
                                                                                                           FROM SMRAGRT sel,  SMRPAAP
                                                                                                          WHERE sel.SMRAGRT_AREA = SMRAGRT.SMRAGRT_AREA
                                                                                                            AND sel.SMRAGRT_AREA = SMRPAAP_AREA
                                                                                                            AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

   INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRAGRL','SELECT * FROM SMRAGRL WHERE EXISTS ( SELECT 1 
                                                                                                           FROM SMRAGRL sel,  SMRPAAP
                                                                                                          WHERE sel.SMRAGRL_AREA = SMRAGRL.SMRAGRL_AREA
                                                                                                            AND sel.SMRAGRL_AREA = SMRPAAP_AREA
                                                                                                            AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 


INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRACAA','SELECT * FROM SMRACAA WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRACAA sel,  SMRPAAP
                                                                                                       WHERE sel.SMRACAA_AREA = SMRACAA.SMRACAA_AREA
                                                                                                         AND sel.SMRACAA_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 


INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRACCM','SELECT * FROM SMRACCM WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRACCM sel,  SMRPAAP
                                                                                                       WHERE sel.SMRACCM_AREA = SMRACCM.SMRACCM_AREA
                                                                                                         AND sel.SMRACCM_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRACLV','SELECT * FROM SMRACLV WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRACLV sel,  SMRPAAP
                                                                                                       WHERE sel.SMRACLV_AREA = SMRACLV.SMRACLV_AREA
                                                                                                         AND sel.SMRACLV_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRAEXL','SELECT * FROM SMRAEXL WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRAEXL sel,  SMRPAAP
                                                                                                       WHERE sel.SMRAEXL_AREA = SMRAEXL.SMRAEXL_AREA
                                                                                                         AND sel.SMRAEXL_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMBARUL','SELECT * FROM SMBARUL WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMBARUL sel,  SMRPAAP
                                                                                                       WHERE sel.SMBARUL_AREA = SMBARUL.SMBARUL_AREA
                                                                                                         AND sel.SMBARUL_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRARUL','SELECT * FROM SMRARUL WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRARUL sel,  SMRPAAP
                                                                                                       WHERE sel.SMRARUL_AREA = SMRARUL.SMRARUL_AREA
                                                                                                         AND sel.SMRARUL_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRARLT','SELECT * FROM SMRARLT WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRARLT sel,  SMRPAAP
                                                                                                       WHERE sel.SMRARLT_AREA = SMRARLT.SMRARLT_AREA
                                                                                                         AND sel.SMRARLT_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 
--
-- Group requirements
--
INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRGLIB','SELECT * FROM SMRGLIB WHERE EXISTS ( SELECT 1
                                                                                                        FROM SMRGLIB sel,  SMRAGAM, SMRPAAP
                                                                                                       WHERE sel.SMRGLIB_GROUP = SMRGLIB.SMRGLIB_GROUP
                                                                                                         AND sel.SMRGLIB_GROUP = SMRAGAM_GROUP
                                                                                                         AND SMRAGAM_AREA = SMRPAAP_AREA
                                                                                                         AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' );

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMBGGEN','SELECT * FROM SMBGGEN WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMBGGEN sel, SMRAGAM, SMRPAAP
                                                                                                       WHERE sel.SMBGGEN_GROUP = SMBGGEN.SMBGGEN_GROUP
                                                                                                         AND sel.SMBGGEN_GROUP = SMRAGAM_GROUP
                                                                                                         AND SMRAGAM_AREA = SMRPAAP_AREA
                                                                                                        AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRGCMT','SELECT * FROM SMRGCMT WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRGCMT sel, SMRAGAM, SMRPAAP
                                                                                                       WHERE sel.SMRGCMT_GROUP = SMRGCMT.SMRGCMT_GROUP
                                                                                                         AND sel.SMRGCMT_GROUP = SMRAGAM_GROUP
                                                                                                         AND SMRAGAM_AREA = SMRPAAP_AREA
                                                                                                        AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRGLVL','SELECT * FROM SMRGLVL WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRGLVL sel, SMRAGAM, SMRPAAP
                                                                                                       WHERE sel.SMRGLVL_GROUP = SMRGLVL.SMRGLVL_GROUP
                                                                                                         AND sel.SMRGLVL_GROUP = SMRAGAM_GROUP
                                                                                                         AND SMRAGAM_AREA = SMRPAAP_AREA
                                                                                                        AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 


INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRGRSA','SELECT * FROM SMRGRSA WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRGRSA sel, SMRAGAM, SMRPAAP
                                                                                                       WHERE sel.SMRGRSA_GROUP = SMRGRSA.SMRGRSA_GROUP
                                                                                                         AND sel.SMRGRSA_GROUP = SMRAGAM_GROUP
                                                                                                         AND SMRAGAM_AREA = SMRPAAP_AREA
                                                                                                        AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRGRSC','SELECT * FROM SMRGRSC WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRGRSC sel, SMRAGAM, SMRPAAP
                                                                                                       WHERE sel.SMRGRSC_GROUP = SMRGRSC.SMRGRSC_GROUP
                                                                                                         AND sel.SMRGRSC_GROUP = SMRAGAM_GROUP
                                                                                                         AND SMRAGAM_AREA = SMRPAAP_AREA
                                                                                                        AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRGRGD','SELECT * FROM SMRGRGD WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRGRGD sel, SMRAGAM, SMRPAAP
                                                                                                       WHERE sel.SMRGRGD_GROUP = SMRGRGD.SMRGRGD_GROUP
                                                                                                         AND sel.SMRGRGD_GROUP = SMRAGAM_GROUP
                                                                                                         AND SMRAGAM_AREA = SMRPAAP_AREA
                                                                                                        AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRGRGC','SELECT * FROM SMRGRGC WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRGRGC sel, SMRAGAM, SMRPAAP
                                                                                                       WHERE sel.SMRGRGC_GROUP = SMRGRGC.SMRGRGC_GROUP
                                                                                                         AND sel.SMRGRGC_GROUP = SMRAGAM_GROUP
                                                                                                         AND SMRAGAM_AREA = SMRPAAP_AREA
                                                                                                        AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRGCAA','SELECT * FROM SMRGCAA WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRGCAA sel, SMRAGAM, SMRPAAP
                                                                                                       WHERE sel.SMRGCAA_GROUP = SMRGCAA.SMRGCAA_GROUP
                                                                                                         AND sel.SMRGCAA_GROUP = SMRAGAM_GROUP
                                                                                                         AND SMRAGAM_AREA = SMRPAAP_AREA
                                                                                                        AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRGCCM','SELECT * FROM SMRGCCM WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRGCCM sel, SMRAGAM, SMRPAAP
                                                                                                       WHERE sel.SMRGCCM_GROUP = SMRGCCM.SMRGCCM_GROUP
                                                                                                         AND sel.SMRGCCM_GROUP = SMRAGAM_GROUP
                                                                                                         AND SMRAGAM_AREA = SMRPAAP_AREA
                                                                                                        AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 


INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRGEXL','SELECT * FROM SMRGEXL WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRGEXL sel, SMRAGAM, SMRPAAP
                                                                                                       WHERE sel.SMRGEXL_GROUP = SMRGEXL.SMRGEXL_GROUP
                                                                                                         AND sel.SMRGEXL_GROUP = SMRAGAM_GROUP
                                                                                                         AND SMRAGAM_AREA = SMRPAAP_AREA
                                                                                                        AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' ); 

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRGCLV','SELECT * FROM SMRGCLV WHERE EXISTS ( SELECT 1 
                                                                                                        FROM SMRGCLV sel, SMRAGAM, SMRPAAP
                                                                                                       WHERE sel.SMRGCLV_GROUP = SMRGCLV.SMRGCLV_GROUP
                                                                                                         AND sel.SMRGCLV_GROUP = SMRAGAM_GROUP
                                                                                                         AND SMRAGAM_AREA = SMRPAAP_AREA
                                                                                                        AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' );

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMBGRUL','SELECT * FROM SMBGRUL WHERE EXISTS (  SELECT 1 
                                                                                                        FROM SMBGRUL sel, SMRAGAM, SMRPAAP
                                                                                                       WHERE sel.SMBGRUL_GROUP = SMBGRUL.SMBGRUL_GROUP
                                                                                                         AND sel.SMBGRUL_GROUP = SMRAGAM_GROUP
                                                                                                         AND SMRAGAM_AREA = SMRPAAP_AREA
                                                                                                        AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' );

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRGRUL','SELECT * FROM SMRGRUL WHERE EXISTS (  SELECT 1 
                                                                                                        FROM SMRGRUL sel, SMRAGAM, SMRPAAP
                                                                                                       WHERE sel.SMRGRUL_GROUP = SMRGRUL.SMRGRUL_GROUP
                                                                                                         AND sel.SMRGRUL_GROUP = SMRAGAM_GROUP
                                                                                                         AND SMRAGAM_AREA = SMRPAAP_AREA
                                                                                                        AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' );

INSERT INTO testTables VALUES ( clob_sequence.nextval,'SMRGRLT','SELECT * FROM SMRGRLT WHERE EXISTS (  SELECT 1 
                                                                                                        FROM SMRGRLT sel, SMRAGAM, SMRPAAP
                                                                                                       WHERE sel.SMRGRLT_GROUP = SMRGRLT.SMRGRLT_GROUP
                                                                                                         AND sel.SMRGRLT_GROUP = SMRAGAM_GROUP
                                                                                                         AND SMRAGAM_AREA = SMRPAAP_AREA
                                                                                                        AND SMRPAAP_PROGRAM LIKE ''' || :program || ''' ) ' );

set term on
set echo on
set verify on
set feedback on

SELECT COUNT(*) FROM testTables;

set verify off
set echo off
set term off
set feedback off

/*
CREATE OR REPLACE PROCEDURE p_xmltable(p_table varchar2, p_select varchar2, p_id number) IS
   my_context NUMBER := 0;
   xmlresult clob;
   result_clob clob;
   cnt   number := 1 ;
BEGIN
   my_context:= DBMS_XMLGEN.NEWCONTEXT (p_select);
   dbms_xmlgen.setNullHandling(my_context,2);

   DBMS_XMLGEN.SETROWSETTAG(my_context,NULL);
   DBMS_XMLGEN.SETROWTAG(my_context,p_table);
   xmlresult := DBMS_XMLGEN.GETXML(my_context);
   --result_clob = smkmxml.p_appendclob(result_clob,xmlresult);
   --INSERT INTO testClob ( Id, Text) values (p_id, result_clob);
   INSERT INTO testClob ( Id, Text) values (p_id, xmlresult);

   COMMIT;
  DBMS_XMLGEN.CLOSECONTEXT(my_context);
END p_xmltable;   
/
*/

CREATE OR REPLACE PROCEDURE p_xmltable(p_table IN varchar2,
                                       p_select IN varchar2,
                                       p_id IN number,
                                       p_xmlclob IN OUT CLOB) IS
   my_context NUMBER := 0;
   xmlresult clob;
   result_clob clob;
   cnt   number := 1 ;
BEGIN
   my_context:= DBMS_XMLGEN.NEWCONTEXT (p_select);
   dbms_xmlgen.setNullHandling(my_context,2);

   DBMS_XMLGEN.SETROWSETTAG(my_context,'CAPP');
   DBMS_XMLGEN.SETROWTAG(my_context,p_table);
   xmlresult := DBMS_XMLGEN.GETXML(my_context);
   smkmxml.p_appendclob(p_xmlclob, xmlresult);
  DBMS_XMLGEN.CLOSECONTEXT(my_context);
END p_xmltable;
/


set serveroutput On Size 1000000
set verify on
set echo on
set term on
set feedback on
DECLARE
  p_table varchar2(30);
  p_select varchar2(2000);
  p_id number;
  p_xmlclob CLOB;
  CURSOR tablelist IS
     SELECT table_name, table_select , id
     FROM testTables
     ORDER BY id;
BEGIN
   SMKMXML.P_INIT_CLOB(p_xmlclob);
   OPEN tableList;
   LOOP
      FETCH tableList INTO p_table, p_select, p_id ;
      EXIT WHEN tableList%NOTFOUND; 
      p_xmltable(p_table, p_select, p_id, p_xmlclob);
   END LOOP;
   CLOSE tableList;
   INSERT INTO testClob ( Id, Text) values (1, p_xmlclob);
   COMMIT;
END;
/

set term on
set echo on
set verify on
set feedback on

SELECT COUNT(*) FROM testClob ;
set pages 0
set linesize 500
set long 10000000
set head off
column text format a9999

Select '<?xml version="1.0" encoding="Windows-1252" ?>' from dual;
SELECT text FROM testClob ORDER BY id;

DROP TABLE testClob;
DROP TABLE testTables;
DROP SEQUENCE clob_sequence;
PROMPT dont forget to modify the spooled file And Remove sql output,  And extra <?xml lines
PROMPT also Remove extra root tags so there is only one <CAPP> And </CAPP>
PROMPT each xml gen will produce a root tag
spool off;