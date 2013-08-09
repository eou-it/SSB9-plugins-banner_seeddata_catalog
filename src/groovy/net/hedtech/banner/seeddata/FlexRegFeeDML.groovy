/*********************************************************************************
  Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.Connection

/**
 * Flex Reg Discount to prevent dropping registration tables
 */

public class FlexRegFeeDML {


    def InputData connectInfo
    Sql conn
    Connection connectCall
    java.sql.RowId tableRow = null
    def xmlData
    List columns
    List indexColumns
    def pidm
    def bannerid


    public FlexRegFeeDML(InputData connectInfo, Sql conn, Connection connectCall, xmlData) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
        this.xmlData = xmlData
        processDiscount()

    }

    /**
     * Process the sfrstcr records.  Use the dynamic insert process but update the enrollment counts in ssbsect after the insert
     *
     */

    def processDiscount() {
        def apiData = new XmlParser().parseText(xmlData)

        // banner ID
        // crn
        // term code
        this.pidm = connectInfo.saveStudentPidm
        this.bannerid = apiData.BANNERID?.text()
        if (connectInfo.debugThis) {
            println "--------- New XML Flec Reg Discount record ----------"
            println "${bannerid}    ${apiData?.CRN?.text()}    ${apiData?.TERM_CODE?.text()}   }    "
        }
        if (apiData?.CRN?.text() && apiData?.TERM_CODE?.text() && bannerid)


            try {

                conn.call("""
                    DECLARE
                    cid  VARCHAR2(32);
                    pidm NUMBER;
                    term_code VARCHAR(6);
                    crn VARCHAR(6);
                    catalog VARCHAR(6);
                    cart_source VARCHAR(6);
                    cart_status_finalized VARCHAR(6);
                    cart_item_status_registered VARCHAR(6);
                    invoice_number varchar(25);
                    tuition_tbbdetc_code varchar(6);
                    coupon_tbbdetc_code varchar(6);
                    discount_tbbdetc_code varchar(6);

                BEGIN

                ----------------------------------------
                -- Test case setup data
                ----------------------------------------
                    pidm :=  ${pidm} ;
                    term_code :=  ${apiData?.TERM_CODE?.text()} ;
                    crn :=  ${apiData?.CRN?.text()}  ;

                ----------------------------------------
                -- Potential variables that may need changing, but probably not
                ----------------------------------------

                    catalog := 'A0';                  --- assumes this is in SFTVCTLG
                    tuition_tbbdetc_code := 'THOR';   --- assumes this exists in tbbdetc
                    coupon_tbbdetc_code := 'HCOU';    --- assumes this exists in tbbdetc
                    discount_tbbdetc_code := 'HDIS';    --- assumes this exists in tbbdetc
                    insert into flexreg.sftvctlg (
                    SFTVCTLG_CODE        ,
                    SFTVCTLG_DESC       ,
                    SFTVCTLG_START_DATE ,
                    SFTVCTLG_END_DATE     ,
                    SFTVCTLG_ACTIVITY_DATE ,
                    SFTVCTLG_USER_ID    ,
                    SFTVCTLG_DATA_ORIGIN  )
                    select 'A0', 'Initial seed data', sysdate - 100, sysdate + 200, sysdate, 'GRAILS','GRAILS'
                    from dual
                    where not exists ( select 1 from flexreg.sftvctlg a where a.sftvctlg_code = 'A0');

                     delete FLEXREG.sffrfees where sffrfees_term_code = term_code and sffrfees_crn = crn;
                    delete FLEXREG.SFKRITEM where sfkritem_crn = crn and sfkritem_term_code = term_code;
                    delete flexreg.sfkbcart where sfkbcart_pidm = pidm;


                --  cart status values
                    cart_source := 'A';
                    cart_status_finalized := 'F';
                    cart_item_status_registered := 'P';

                    --Create a bogus cart id
                    select LOCALTIMESTAMP into cid from dual;

                     SELECT   LTRIM (TO_CHAR (sfobiseq.NEXTVAL, '00000009'))
                           INTO   invoice_number
                           FROM   DUAL;

                    -- Create the initial cart
                    insert into FLEXREG.SFKBCART (SFKBCART_CID,SFKBCART_PIDM,SFKBCART_STATUS,SFKBCART_INVOICE_NUMBER,
                      SFKBCART_CTLG_CODE,SFKBCART_ACTIVITY_DATE,SFKBCART_USER_ID,SFKBCART_DATA_ORIGIN,SFKBCART_VERSION,  SFKBCART_SOURCE)
                        values (cid,pidm,cart_status_finalized,invoice_number,catalog,SYSDATE,'FLEXREG_USER','TEST',0,'A');

                    -- Create cart items
                    insert into FLEXREG.SFKRITEM (SFKRITEM_CID,SFKRITEM_CRN,SFKRITEM_TERM_CODE, SFKRITEM_STATUS,
                      SFKRITEM_MESSAGE,SFKRITEM_COST,SFKRITEM_START_DATE,SFKRITEM_END_DATE,SFKRITEM_ACTIVITY_DATE,SFKRITEM_USER_ID,
                      SFKRITEM_DATA_ORIGIN,SFKRITEM_RSTS_CODE,SFKRITEM_GMOD_CODE,SFKRITEM_CREDIT_HR,SFKRITEM_LEVL_CODE,SFKRITEM_VERSION)
                    values (cid,crn,term_code,cart_item_status_registered,null,null,null,null,sysdate,'FLEXREG_USER','TEST','RW',null,null,null,0);

                    -- Create a class fee
                    INSERT INTO FLEXREG.sffrfees (sffrfees_cid,sffrfees_term_code,sffrfees_crn,sffrfees_detail_code,
                         sffrfees_amount,sffrfees_note,sffrfees_type_ind,sffrfees_activity_date,sffrfees_user_id,sffrfees_data_origin)
                    values(cid, term_code, crn,tuition_tbbdetc_code,500,null,'F',sysdate,'FLEXREG_USER','TEST');

                    -- Create a coupon
                    INSERT INTO sffrfees (sffrfees_cid,sffrfees_term_code,sffrfees_crn,sffrfees_detail_code,
                         sffrfees_amount,sffrfees_note,sffrfees_type_ind,sffrfees_activity_date,sffrfees_user_id,sffrfees_data_origin)
                    values(cid, term_code, crn,coupon_tbbdetc_code,-50,'Coupon 10% off','C',sysdate,'FLEXREG_USER','TEST');

                    -- Create a discount
                    INSERT INTO sffrfees (sffrfees_cid,sffrfees_term_code,sffrfees_crn,sffrfees_detail_code,
                         sffrfees_amount,sffrfees_note,sffrfees_type_ind,sffrfees_activity_date,sffrfees_user_id,sffrfees_data_origin)
                            values(cid, term_code, crn,discount_tbbdetc_code,-100,'10% early bird','D',sysdate,'FLEXREG_USER','TEST');
                    COMMIT;
                END;
                """)
                connectInfo.tableUpdate("FLEXREG", 0, 0, 1, 0, 0)


            }
            catch (Exception e) {
                if (connectInfo.showErrors) {
                    println "Problem executing update of flex reg fee${bannerid} ${apiData.TERM_CODE.text()} ${apiData.CRN.text()} from FlexRegFeeDML.groovy for ${connectInfo.tableName}: $e.message"
                }
            }
    }
}


