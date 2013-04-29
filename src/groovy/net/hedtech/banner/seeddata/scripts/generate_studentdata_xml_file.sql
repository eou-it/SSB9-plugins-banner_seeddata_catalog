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
var bannerid varchar2(9)
var pidm number

define bid char
prompt enter BID
accept  BID

CREATE TABLE testClob
(
    Id NUMBER,
    Text CLOB,
    CONSTRAINT testClob_Pk PRIMARY KEY (Id)
);
create table testTables 
(  id number,   
   table_name varchar2(30),
   table_select varchar2(600)
 ); 

create sequence clob_sequence increment by 1;
-- create insert statements here that are part of the module 

begin
  :bannerid :=  '&BID';
  :pidm  :=   gb_common.f_get_pidm('&BID')  ;
end ;
/
set verify off
set echo off
set term off
set feedback off
 
insert into testTables values ( clob_sequence.nextval,'STUDENT_SPRIDEN','select '''   ||  :bannerid || '''  bannerid,	SPRIDEN.*	FROM SPRIDEN  where spriden_change_ind is null and SPRIDEN_pidm = ' || :pidm    );
insert into testTables values ( clob_sequence.nextval,'SPBPERS','select '''   ||  :bannerid || '''  bannerid,	SPBPERS.*	FROM 	SPBPERS	where 	SPBPERS_pidm = ' || :pidm    );
insert into testTables values ( clob_sequence.nextval,'SPRADDR','select '''   ||  :bannerid || '''  bannerid,	SPRADDR.*	FROM 	SPRADDR	where 	SPRADDR_pidm = ' || :pidm    );
insert into testTables values ( clob_sequence.nextval,'SPRTELE','select '''   ||  :bannerid || '''  bannerid,	SPRTELE.*	FROM 	SPRTELE	where 	SPRTELE_pidm = ' || :pidm    );
insert into testTables values ( clob_sequence.nextval,'GOREMAL','select '''   ||  :bannerid || '''  bannerid,	GOREMAL.*	FROM 	GOREMAL	where 	GOREMAL_pidm = ' || :pidm    );
insert into testTables values ( clob_sequence.nextval,'SPREMRG','select '''   ||  :bannerid || '''  bannerid,	SPREMRG.*	FROM 	SPREMRG	where 	SPREMRG_pidm = ' || :pidm    );
insert into testTables values ( clob_sequence.nextval,'GORADID','select '''   ||  :bannerid || '''  bannerid,	GORADID.*	FROM 	GORADID	where 	GORADID_pidm = ' || :pidm    );

insert into testTables values ( clob_sequence.nextval,'SRBRECR','select '''  || :bannerid || '''  bannerid,	SRBRECR.*	FROM 	SRBRECR	where 	SRBRECR_pidm = ' || :pidm    );
insert into testTables values ( clob_sequence.nextval,'SORLCUR','select '''  || :bannerid || '''  bannerid,	SORLCUR.*	FROM 	SORLCUR	where 	SORLCUR_pidm = ' || :pidm || ' and sorlcur_lmod_code = sb_curriculum_str.f_recruit');
insert into testTables values ( clob_sequence.nextval,'SORLFOS','select '''  || :bannerid || '''  bannerid,	SORLFOS.*	FROM 	SORLFOS, sorlcur	where 	SORLFOS_pidm = ' || :pidm || '  and sorlcur_pidm = sorlfos_pidm and sorlcur_seqno = sorlfos_lcur_seqno and sorlcur_lmod_code = sb_curriculum_str.f_recruit');
insert into testTables values ( clob_sequence.nextval,'SRRRATT','select '''  || :bannerid || '''  bannerid,	SRRRATT.*	FROM 	SRRRATT	where 	SRRRATT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SRRRCMT','select '''  || :bannerid || '''  bannerid,	SRRRCMT.*	FROM 	SRRRCMT	where 	SRRRCMT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SRRRSRC','select '''  || :bannerid || '''  bannerid,	SRRRSRC.*	FROM 	SRRRSRC	where 	SRRRSRC_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SRRCHRT','select '''  || :bannerid || '''  bannerid,	SRRCHRT.*	FROM 	SRRCHRT	where 	SRRCHRT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SRRLEND','select '''  || :bannerid || '''  bannerid,	SRRLEND.*	FROM 	SRRLEND	where 	SRRLEND_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SORCONT','select '''  || :bannerid || '''  bannerid,	SORCONT.*	FROM 	SORCONT	where 	SORCONT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'CURRICULUMBACK', 'select '''  || :bannerid || '''  bannerid,	SRBRECR_TERM_CODE term_code, SRBRECR_ADMIN_SEQNO key_seqno, sb_curriculum_str.f_recruit lmod	FROM 	SRBRECR	where 	SRBRECR_pidm = ' || :pidm    );

insert into testTables values ( clob_sequence.nextval,'SARADAP','select '''  || :bannerid || '''  bannerid,	SARADAP.*	FROM 	SARADAP	where 	SARADAP_pidm = ' || :pidm    );
insert into testTables values ( clob_sequence.nextval,'SORLCUR','select '''  || :bannerid || '''  bannerid,	SORLCUR.*	FROM 	SORLCUR	where 	SORLCUR_pidm = ' || :pidm || ' and sorlcur_lmod_code = sb_curriculum_str.f_admissions');
insert into testTables values ( clob_sequence.nextval,'SORLFOS','select '''  || :bannerid || '''  bannerid,	SORLFOS.*	FROM 	SORLFOS, SORLCUR	where 	SORLFOS_pidm = ' || :pidm || '  and sorlcur_pidm = sorlfos_pidm and sorlcur_seqno = sorlfos_lcur_seqno and sorlcur_lmod_code = sb_curriculum_str.f_admissions');
insert into testTables values ( clob_sequence.nextval,'SABSUPL','select '''  || :bannerid || '''  bannerid,	SABSUPL.*	FROM 	SABSUPL	where 	SABSUPL_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SARAATT','select '''  || :bannerid || '''  bannerid,	SARAATT.*	FROM 	SARAATT	where 	SARAATT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SARACMT','select '''  || :bannerid || '''  bannerid,	SARACMT.*	FROM 	SARACMT	where 	SARACMT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SARAPPD','select '''  || :bannerid || '''  bannerid,	SARAPPD.*	FROM 	SARAPPD	where 	SARAPPD_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SARCHKL','select '''  || :bannerid || '''  bannerid,	SARCHKL.*	FROM 	SARCHKL	where 	SARCHKL_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SARCHRT','select '''  || :bannerid || '''  bannerid,	SARCHRT.*	FROM 	SARCHRT	where 	SARCHRT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SARQUAN','select '''  || :bannerid || '''  bannerid,	SARQUAN.*	FROM 	SARQUAN	where 	SARQUAN_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SARRRAT','select '''  || :bannerid || '''  bannerid,	SARRRAT.*	FROM 	SARRRAT	where 	SARRRAT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SARRSRC','select '''  || :bannerid || '''  bannerid,	SARRSRC.*	FROM 	SARRSRC	where 	SARRSRC_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'CURRICULUMBACK', 'select '''  || :bannerid || '''  bannerid,	SARADAP_TERM_CODE_ENTRY term_code, SARADAP_APPL_NO key_seqno, sb_curriculum_str.f_ADMISSIONS lmod	FROM 	SARADAP	where 	SARADAP_pidm = ' || :pidm    );

insert into testTables values ( clob_sequence.nextval,'SGBSTDN','select '''  || :bannerid || '''  bannerid,	SGBSTDN.*	FROM 	SGBSTDN	where 	SGBSTDN_pidm = ' || :pidm    );
insert into testTables values ( clob_sequence.nextval,'SGRSTSP','select '''  || :bannerid || '''  bannerid,	SGRSTSP.*	FROM 	SGRSTSP	where 	SGRSTSP_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SORLCUR','select '''  || :bannerid || '''  bannerid,	SORLCUR.*	FROM 	SORLCUR	where 	SORLCUR_pidm = ' || :pidm || ' and sorlcur_lmod_code = sb_curriculum_str.f_learner');
insert into testTables values ( clob_sequence.nextval,'SORLFOS','select '''  || :bannerid || '''  bannerid,	SORLFOS.*	FROM 	SORLFOS, sorlcur	where 	SORLFOS_pidm = ' || :pidm || '  and sorlcur_pidm = sorlfos_pidm and sorlcur_seqno = sorlfos_lcur_seqno and sorlcur_lmod_code = sb_curriculum_str.f_learner');
insert into testTables values ( clob_sequence.nextval,'SGBAPRG','select '''  || :bannerid || '''  bannerid,	SGBAPRG.*	FROM 	SGBAPRG	where 	SGBAPRG_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGBDCPR','select '''  || :bannerid || '''  bannerid,	SGBDCPR.*	FROM 	SGBDCPR	where 	SGBDCPR_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGBEOPS','select '''  || :bannerid || '''  bannerid,	SGBEOPS.*	FROM 	SGBEOPS	where 	SGBEOPS_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGBOEDU','select '''  || :bannerid || '''  bannerid,	SGBOEDU.*	FROM 	SGBOEDU	where 	SGBOEDU_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGBUSER','select '''  || :bannerid || '''  bannerid,	SGBUSER.*	FROM 	SGBUSER	where 	SGBUSER_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRACMT','select '''  || :bannerid || '''  bannerid,	SGRACMT.*	FROM 	SGRACMT	where 	SGRACMT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRADVR','select '''  || :bannerid || '''  bannerid,	SGRADVR.*	FROM 	SGRADVR	where 	SGRADVR_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRASSI','select '''  || :bannerid || '''  bannerid,	SGRASSI.*	FROM 	SGRASSI	where 	SGRASSI_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRASST','select '''  || :bannerid || '''  bannerid,	SGRASST.*	FROM 	SGRASST	where 	SGRASST_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRATAD','select '''  || :bannerid || '''  bannerid,	SGRATAD.*	FROM 	SGRATAD	where 	SGRATAD_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRATCT','select '''  || :bannerid || '''  bannerid,	SGRATCT.*	FROM 	SGRATCT	where 	SGRATCT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRATHA','select '''  || :bannerid || '''  bannerid,	SGRATHA.*	FROM 	SGRATHA	where 	SGRATHA_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRATHC','select '''  || :bannerid || '''  bannerid,	SGRATHC.*	FROM 	SGRATHC	where 	SGRATHC_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRATHE','select '''  || :bannerid || '''  bannerid,	SGRATHE.*	FROM 	SGRATHE	where 	SGRATHE_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRATHT','select '''  || :bannerid || '''  bannerid,	SGRATHT.*	FROM 	SGRATHT	where 	SGRATHT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRCHRT','select '''  || :bannerid || '''  bannerid,	SGRCHRT.*	FROM 	SGRCHRT	where 	SGRCHRT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRCMNT','select '''  || :bannerid || '''  bannerid,	SGRCMNT.*	FROM 	SGRCMNT	where 	SGRCMNT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRCOOP','select '''  || :bannerid || '''  bannerid,	SGRCOOP.*	FROM 	SGRCOOP	where 	SGRCOOP_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRDISA','select '''  || :bannerid || '''  bannerid,	SGRDISA.*	FROM 	SGRDISA	where 	SGRDISA_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRDSER','select '''  || :bannerid || '''  bannerid,	SGRDSER.*	FROM 	SGRDSER	where 	SGRDSER_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRDUTY','select '''  || :bannerid || '''  bannerid,	SGRDUTY.*	FROM 	SGRDUTY	where 	SGRDUTY_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRESEL','select '''  || :bannerid || '''  bannerid,	SGRESEL.*	FROM 	SGRESEL	where 	SGRESEL_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRSACT','select '''  || :bannerid || '''  bannerid,	SGRSACT.*	FROM 	SGRSACT	where 	SGRSACT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRSATT','select '''  || :bannerid || '''  bannerid,	SGRSATT.*	FROM 	SGRSATT	where 	SGRSATT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRSCMT','select '''  || :bannerid || '''  bannerid,	SGRSCMT.*	FROM 	SGRSCMT	where 	SGRSCMT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRSPRT','select '''  || :bannerid || '''  bannerid,	SGRSPRT.*	FROM 	SGRSPRT	where 	SGRSPRT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SGRVETN','select '''  || :bannerid || '''  bannerid,	SGRVETN.*	FROM 	SGRVETN	where 	SGRVETN_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'CURRICULUMBACK', 'select '''  || :bannerid || '''  bannerid,	sgbstdn_TERM_CODE_eff term_code, 99 key_seqno, sb_curriculum_str.f_learner lmod	FROM 	sgbstdn	where 	sgbstdn_pidm = ' || :pidm    );

insert into testTables values ( clob_sequence.nextval,'SFBETRM','select '''  || :bannerid || '''  bannerid,	SFBETRM.*	FROM 	SFBETRM	where 	SFBETRM_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SFRENSP','select '''  || :bannerid || '''  bannerid,	SFRENSP.*	FROM 	SFRENSP	where 	SFRENSP_pidm = ' || :pidm);

insert into testTables values ( clob_sequence.nextval,'SFRSTCR','select '''  || :bannerid || '''  bannerid,	SFRSTCR.*	FROM 	SFRSTCR	where 	SFRSTCR_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SFRAREG','select '''  || :bannerid || '''  bannerid,	SFRAREG.*	FROM 	SFRAREG	where 	SFRAREG_pidm = ' || :pidm);

insert into testTables values ( clob_sequence.nextval,'SFRENRL','select '''  || :bannerid || '''  bannerid,	SFRENRL.*	FROM 	SFRENRL	where 	SFRENRL_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SFRENSP','select '''  || :bannerid || '''  bannerid,	SFRENSP.*	FROM 	SFRENSP	where 	SFRENSP_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SFRSRPO','select '''  || :bannerid || '''  bannerid,	SFRSRPO.*	FROM 	SFRSRPO	where 	SFRSRPO_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SFRTHST','select '''  || :bannerid || '''  bannerid,	SFRTHST.*	FROM 	SFRTHST	where 	SFRTHST_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SFRTIME','select '''  || :bannerid || '''  bannerid,	SFRTIME.*	FROM 	SFRTIME	where 	SFRTIME_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SFRWDRL','select '''  || :bannerid || '''  bannerid,	SFRWDRL.*	FROM 	SFRWDRL	where 	SFRWDRL_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SFRWLNT','select '''  || :bannerid || '''  bannerid,	SFRWLNT.*	FROM 	SFRWLNT	where 	SFRWLNT_pidm = ' || :pidm);

insert into testTables values ( clob_sequence.nextval,'SHRTTRM','select '''  || :bannerid || '''  bannerid,	SHRTTRM.* FROM 	SHRTTRM	where 	SHRTTRM_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRDGMR','select '''  || :bannerid || '''  bannerid,	SHRDGMR.*	FROM 	SHRDGMR	where 	SHRDGMR_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SORLCUR','select '''  || :bannerid || '''  bannerid,	SORLCUR.*	FROM 	SORLCUR	where 	SORLCUR_pidm = gb_common.f_get_pidm(''&&BID'') and sorlcur_lmod_code = sb_curriculum_str.f_outcome');
insert into testTables values ( clob_sequence.nextval,'SORLCUR','select '''  || :bannerid || '''  bannerid,	SORLCUR.*	FROM 	SORLFOS, sorlcur	where 	SORLFOS_pidm = gb_common.f_get_pidm(''&&BID'') and sorlcur_pidm = sorlfos_pidm and sorlcur_seqno = sorlfos_lcur_seqno and sorlcur_lmod_code = sb_curriculum_str.f_outcome');
insert into testTables values ( clob_sequence.nextval,'CURRICULUMBACK', 'select '''  || :bannerid || '''  bannerid,	shrdgmr_TERM_CODE_sturec term_code, shrdgmr_seq_no key_seqno, sb_curriculum_str.f_outcome lmod	FROM 	shrdgmr	where 	shrdgmr_pidm = ' || :pidm    );

insert into testTables values ( clob_sequence.nextval,'SHBDIPL','select '''  || :bannerid || '''  bannerid,	SHBDIPL.*	FROM 	SHBDIPL	where 	SHBDIPL_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHBCATT','select '''  || :bannerid || '''  bannerid,	SHBCATT.*	FROM 	SHBCATT	where 	SHBCATT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHBCOMI','select '''  || :bannerid || '''  bannerid,	SHBCOMI.*	FROM 	SHBCOMI	where 	SHBCOMI_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHBGAPP','select '''  || :bannerid || '''  bannerid,	SHBGAPP.*	FROM 	SHBGAPP	where 	SHBGAPP_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHBHEAD','select '''  || :bannerid || '''  bannerid,	SHBHEAD.*	FROM 	SHBHEAD	where 	SHBHEAD_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRATTR','select '''  || :bannerid || '''  bannerid,	SHRATTR.*	FROM 	SHRATTR	where 	SHRATTR_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRCATT','select '''  || :bannerid || '''  bannerid,	SHRCATT.*	FROM 	SHRCATT	where 	SHRCATT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRCGPA','select '''  || :bannerid || '''  bannerid,	SHRCGPA.*	FROM 	SHRCGPA	where 	SHRCGPA_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRCHRT','select '''  || :bannerid || '''  bannerid,	SHRCHRT.*	FROM 	SHRCHRT	where 	SHRCHRT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRCOMC','select '''  || :bannerid || '''  bannerid,	SHRCOMC.*	FROM 	SHRCOMC	where 	SHRCOMC_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRCOMM','select '''  || :bannerid || '''  bannerid,	SHRCOMM.*	FROM 	SHRCOMM	where 	SHRCOMM_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRDCMT','select '''  || :bannerid || '''  bannerid,	SHRDCMT.*	FROM 	SHRDCMT	where 	SHRDCMT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRDGCM','select '''  || :bannerid || '''  bannerid,	SHRDGCM.* FROM 	SHRDGCM	where 	SHRDGCM_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRDGDH','select '''  || :bannerid || '''  bannerid,	SHRDGDH.* FROM 	SHRDGDH	where 	SHRDGDH_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRDGIH','select '''  || :bannerid || '''  bannerid,	SHRDGIH.* FROM 	SHRDGIH	where 	SHRDGIH_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHREGPA','select '''  || :bannerid || '''  bannerid,	SHREGPA.* FROM 	SHREGPA	where 	SHREGPA_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHREPTD','select '''  || :bannerid || '''  bannerid,	SHREPTD.* FROM 	SHREPTD	where 	SHREPTD_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHREVNT','select '''  || :bannerid || '''  bannerid,	SHREVNT.* FROM 	SHREVNT	where 	SHREVNT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRGCOL','select '''  || :bannerid || '''  bannerid,	SHRGCOL.* FROM 	SHRGCOL	where 	SHRGCOL_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRGPAC','select '''  || :bannerid || '''  bannerid,	SHRGPAC.* FROM 	SHRGPAC	where 	SHRGPAC_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRGPAL','select '''  || :bannerid || '''  bannerid,	SHRGPAL.* FROM 	SHRGPAL	where 	SHRGPAL_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRINST','select '''  || :bannerid || '''  bannerid,	SHRINST.* FROM 	SHRINST	where 	SHRINST_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRLGPA','select '''  || :bannerid || '''  bannerid,	SHRLGPA.* FROM 	SHRLGPA	where 	SHRLGPA_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRNCRD','select '''  || :bannerid || '''  bannerid,	SHRNCRD.* FROM 	SHRNCRD	where 	SHRNCRD_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRNCRS','select '''  || :bannerid || '''  bannerid,	SHRNCRS.* FROM 	SHRNCRS	where 	SHRNCRS_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRQPND','select '''  || :bannerid || '''  bannerid,	SHRQPND.* FROM 	SHRQPND	where 	SHRQPND_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRQPNM','select '''  || :bannerid || '''  bannerid,	SHRQPNM.* FROM 	SHRQPNM	where 	SHRQPNM_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRRPEQ','select '''  || :bannerid || '''  bannerid,	SHRRPEQ.* FROM 	SHRRPEQ	where 	SHRRPEQ_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRSGPA','select '''  || :bannerid || '''  bannerid,	SHRSGPA.* FROM 	SHRSGPA	where 	SHRSGPA_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRSMRK','select '''  || :bannerid || '''  bannerid,	SHRSMRK.* FROM 	SHRSMRK	where 	SHRSMRK_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRSRKA','select '''  || :bannerid || '''  bannerid,	SHRSRKA.* FROM 	SHRSRKA	where 	SHRSRKA_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRTCKN','select '''  || :bannerid || '''  bannerid,	SHRTCKN.* FROM 	SHRTCKN	where 	SHRTCKN_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRTATT','select '''  || :bannerid || '''  bannerid,	SHRTATT.* FROM 	SHRTATT	where 	SHRTATT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRTCKD','select '''  || :bannerid || '''  bannerid,	SHRTCKD.* FROM 	SHRTCKD	where 	SHRTCKD_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRTCKG','select '''  || :bannerid || '''  bannerid,	SHRTCKG.* FROM 	SHRTCKG	where 	SHRTCKG_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRTCKL','select '''  || :bannerid || '''  bannerid,	SHRTCKL.* FROM 	SHRTCKL	where 	SHRTCKL_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRTGPA','select '''  || :bannerid || '''  bannerid,	SHRTGPA.* FROM 	SHRTGPA	where 	SHRTGPA_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRTMCM','select '''  || :bannerid || '''  bannerid,	SHRTMCM.* FROM 	SHRTMCM	where 	SHRTMCM_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRTRIT','select '''  || :bannerid || '''  bannerid,	SHRTRIT.* FROM 	SHRTRIT	where 	SHRTRIT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRTRAM','select '''  || :bannerid || '''  bannerid,	SHRTRAM.* FROM 	SHRTRAM	where 	SHRTRAM_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRTRCD','select '''  || :bannerid || '''  bannerid,	SHRTRCD.* FROM 	SHRTRCD	where 	SHRTRCD_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRTRCE','select '''  || :bannerid || '''  bannerid,	SHRTRCE.* FROM 	SHRTRCE	where 	SHRTRCE_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRTRCR','select '''  || :bannerid || '''  bannerid,	SHRTRCR.* FROM 	SHRTRCR	where 	SHRTRCR_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRTREP','select '''  || :bannerid || '''  bannerid,	SHRTREP.* FROM 	SHRTREP	where 	SHRTREP_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRTRTA','select '''  || :bannerid || '''  bannerid,	SHRTRTA.* FROM 	SHRTRTA	where 	SHRTRTA_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRTRTK','select '''  || :bannerid || '''  bannerid,	SHRTRTK.* FROM 	SHRTRTK	where 	SHRTRTK_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHRTTCM','select '''  || :bannerid || '''  bannerid,	SHRTTCM.* FROM 	SHRTTCM	where 	SHRTTCM_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHTIACT','select '''  || :bannerid || '''  bannerid,	SHTIACT.* FROM 	SHTIACT	where 	SHTIACT_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHTTRAN','select '''  || :bannerid || '''  bannerid,	SHTTRAN.* FROM 	SHTTRAN	where 	SHTTRAN_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'SHTTRNM','select '''  || :bannerid || '''  bannerid,	SHTTRNM.* FROM 	SHTTRNM	where 	SHTTRNM_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'GOBTPAC','select '''  || :bannerid || '''  bannerid,	GOBTPAC.* FROM GOBTPAC	where 	GOBTPAC_pidm = ' || :pidm);
insert into testTables values ( clob_sequence.nextval,'TWGRROLE','select '''  || :bannerid || '''  bannerid,	TWGRROLE.* FROM TWGRROLE	where 	TWGRROLE_pidm = ' || :pidm);


set term on
set echo on
set verify on
set feedback on
   
Select Count(*) from testTables; 

set verify off
set echo off
set term off
--set feedback off
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
   DBMS_XMLGEN.SETROWSETTAG(my_context,'STUDENTDATA');
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
  p_select varchar2(250); 
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
Prompt also Remove extra root tags so there is only one <STUDENTDATA> And </STUDENTDATA>
PROMPT each xml gen will produce a root tag  
spool off; 
