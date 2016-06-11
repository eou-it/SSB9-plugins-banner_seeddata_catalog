/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.seeddata

import groovy.sql.Sql
import org.apache.commons.lang.StringUtils

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.RowId

/**
 *  DML for FinanceBudget Availability Foapal Account
 */
public class FinanceBudgetAvailabilityFoapalAccountDML {


    def accountTypeCode, acctCode, coaCode, acctDesc, acctCodePred, startCount, totalCount, accountCodePattern, accountCodePool

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    RowId tableRow = null


    public FinanceBudgetAvailabilityFoapalAccountDML( InputData connectInfo, Sql conn, Connection connectCall ) {

        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
    }


    public FinanceBudgetAvailabilityFoapalAccountDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
        this.connectInfo = connectInfo
        this.connectCall = conn.getConnection()
        this.xmlData = xmlData
        parseXmlData()
        populateAccount()
    }


    def parseXmlData() {
        def accountCreation = new XmlParser().parseText( xmlData )
        this.accountTypeCode = accountCreation.ACCOUNT_TYPE_CODE?.text()
        this.acctCode = accountCreation.ACCT_CODE?.text()

        this.coaCode= accountCreation.COA_CODE?.text()
        boolean isEmpty = StringUtils.isEmpty accountCreation.START_COUNT?.text()
        this.startCount = isEmpty ? 0 : accountCreation.START_COUNT?.text() as int
        this.acctDesc = accountCreation.ACCT_DESC?.text()
        this.acctCodePred = accountCreation.ACCT_CODE_PRED?.text()
        isEmpty = StringUtils.isEmpty accountCreation.TOTAL_COUNT?.text()
        this.totalCount = isEmpty ? 0 : accountCreation.TOTAL_COUNT?.text() as int
        this.accountCodePattern = accountCreation.ACCOUNT_CODE_PATTERN?.text()
        this.accountCodePool = accountCreation.ACCOUNT_CODE_POOL?.text()
    }

    /**
     * Populate Account
     */
    def populateAccount() {
        try {
            final String apiQuery =
                    "DECLARE\n" +
                            "  predefined_acctdesc        VARCHAR(35); -- Description pattern\n" +
                            "  predefined_acctpred        number;\n" +
                            "  predefined_normalBalance varchar2(1);\n" +
                            "  \n" +
                            "\tbegin\n" +
                            "  select decode(?, 51, 'Revenue Act ',61, 'Labor Act ', 71, 'Expenditure Act - Pool ', 82, 'Transfer Out ', 81, 'Transfer In ', 11, 'Assets Act ', 21, 'Liabilities Act ', 31, 'Control Act ', 41, 'Fund Balance Act ', '') into predefined_acctdesc from dual;\n" +
                            "  select decode(?, 51, 5175,61, 6121, 71, 7130, 81, 8100, 82, 8200, 11, 1007, 21, 2101, 31, 3011, 41, 3081, 0) into predefined_acctpred from dual;\n" +
                            "  select decode(?, 51, 'C',61, 'D', 71, 'D', 81, 'C', 82, 'D', 11, 'D', 21, 'C', 31, 'C', 41, 'C', '') into predefined_normalBalance from dual;\n" +
                            "  IF ? IS NOT NULL THEN\n" +
                            "\tDELETE FROM ftvacct where FTVACCT_ACCT_CODE=? AND FTVACCT_COAS_CODE = ?;\n" +
                            "\tINSERT INTO FTVACCT\n" +
                            "      (\n" +
                            "        FTVACCT_COAS_CODE,\n" +
                            "        FTVACCT_ACCT_CODE,\n" +
                            "        FTVACCT_EFF_DATE,\n" +
                            "        FTVACCT_ACTIVITY_DATE,\n" +
                            "        FTVACCT_USER_ID,\n" +
                            "        FTVACCT_NCHG_DATE,        \n" +
                            "        FTVACCT_TITLE,\n" +
                            "        FTVACCT_ATYP_CODE,\n" +
                            "        FTVACCT_NORMAL_BAL,\n" +
                            "        FTVACCT_STATUS_IND,\n" +
                            "        FTVACCT_ACCT_CODE_PRED,\n" +
                            "        FTVACCT_DATA_ENTRY_IND,\n" +
                            "        FTVACCT_DATA_ORIGIN\n" +
                            "      )\n" +
                            "      VALUES\n" +
                            "      (\n" +
                            "        ?,\n" +
                            "        ?,\n" +
                            "        '01-MAY-2010',\n" +
                            "        '01-MAY-2010',\n" +
                            "        'GRAILS',\n" +
                            "        '31-DEC-2099',\n" +
                            "        ?,\n" +
                            "        ?,\n" +
                            "        predefined_normalBalance,\n" +
                            "        'A',\n" +
                            "        predefined_acctpred,\n" +
                            "        'Y',\n" +
                            "        'GRAILS'\n" +
                            "      );\n" +
                            "  END IF;\n" +
                            "  \n" +
                            "  FOR ind IN ? .. ?\n" +
                            "  loop\n" +
                            "    delete from ftvacct where FTVACCT_ACCT_CODE=replace(?\n" +
                            "        ||TO_CHAR(ind, '00099'),' ','') AND FTVACCT_COAS_CODE = ?;\n" +
                            "    INSERT\n" +
                            "    INTO FTVACCT\n" +
                            "      (\n" +
                            "        FTVACCT_COAS_CODE,\n" +
                            "        FTVACCT_ACCT_CODE,\n" +
                            "        FTVACCT_EFF_DATE,\n" +
                            "        FTVACCT_ACTIVITY_DATE,\n" +
                            "        FTVACCT_USER_ID,\n" +
                            "        FTVACCT_NCHG_DATE,        \n" +
                            "        FTVACCT_TITLE,\n" +
                            "        FTVACCT_ATYP_CODE,\n" +
                            "        FTVACCT_NORMAL_BAL,\n" +
                            "        FTVACCT_STATUS_IND,\n" +
                            "        FTVACCT_ACCT_CODE_PRED,\n" +
                            "        FTVACCT_DATA_ENTRY_IND,\n" +
                            "        FTVACCT_DATA_ORIGIN,\n" +
                            "        FTVACCT_ACCT_CODE_POOL\n" +
                            "      )\n" +
                            "      VALUES\n" +
                            "      (\n" +
                            "        ?,\n" +
                            "        REPLACE(?\n" +
                            "        ||TO_CHAR(ind, '00099'),' ',''),\n" +
                            "        '01-MAY-2010',\n" +
                            "        '01-MAY-2010',\n" +
                            "        'GRAILS',\n" +
                            "        '31-DEC-2099',       \n" +
                            "        REPLACE(predefined_acctdesc\n" +
                            "        ||TO_CHAR(ind, '00099'),' ',''),\n" +
                            "        ?,\n" +
                            "        predefined_normalBalance,\n" +
                            "        'A',\n" +
                            "        predefined_acctpred,\n" +
                            "        'Y',\n" +
                            "        'GRAILS',\n" +
                            "        ?\n" +
                            "      );\n" +
                            "  END LOOP;\n" +
                            "  COMMIT;\n" +
                            "END;"
            CallableStatement insertCall = this.connectCall.prepareCall( apiQuery )
            insertCall.setString( 1, this.accountTypeCode )
            insertCall.setString( 2, this.accountTypeCode )
            insertCall.setString( 3, this.accountTypeCode )
            insertCall.setString( 4, StringUtils.isEmpty( this.acctCode ) ? null : this.acctCode )
            insertCall.setString( 5, this.acctCode )
            insertCall.setString( 6, this.coaCode )
            insertCall.setString( 7, this.coaCode )
            insertCall.setString( 8, this.acctCode )
            insertCall.setString( 9, this.acctDesc )
            insertCall.setString( 10, this.accountTypeCode )
           // insertCall.setString( 11, this.acctCodePred )

            insertCall.setInt( 11, this.startCount )
            insertCall.setInt( 12, this.totalCount )

            insertCall.setString( 13, this.accountCodePattern )
            insertCall.setString( 14, this.coaCode )

            insertCall.setString( 15, this.coaCode )
            insertCall.setString( 16, this.accountCodePattern )

            insertCall.setString( 17, this.accountTypeCode )
            insertCall.setString( 18, this.accountCodePool )
            insertCall.execute()
            connectInfo.tableUpdate( "FTVACCT", 0, 1, 0, 0, 0 )

        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FTVACCT", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Executing BA_FOAPAL_ACCOUNT" + e
            }
        }
        finally {
            //connectCall.close()
        }
    }
}
