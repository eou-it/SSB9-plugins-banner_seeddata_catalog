/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
/*
	This scripts identifies and removes 
	Duplicate effective dated record for below Finance tables
		FTVCOAS, FTVACCI,FTVFTYP,FTVFUND
		FTVORGN, FTVACCT,FTVATYP,FTVPROG
		FTVLOCN, FTVACTV,FTVPROJ
*/
package net.hedtech.banner.seeddata

import groovy.sql.Sql

import java.sql.CallableStatement
import java.sql.Connection

/**
 *  DML for Cleaning up CIFOAPAL records
 */
 
public class FinanceCifoapalpCleanUpDML {

    def InputData connectInfo
    Sql conn
    Connection connectCall
    def xmlData
    def dummy

    public FinanceCifoapalpCleanUpDML( InputData connectInfo, Sql conn, Connection connectCall ) {
        this.conn = conn
        this.connectInfo = connectInfo
        this.connectCall = connectCall
		cifoapalpCleanup();
    }

	public FinanceCifoapalpCleanUpDML( InputData connectInfo, Sql conn, Connection connectCall, xmlData ) {
		this.connectInfo = connectInfo
		this.connectCall = conn.getConnection()
		this.xmlData = xmlData
		parseXmlData()
		cifoapalpCleanup()
	}

	def parseXmlData() {
		def userCreationData = new XmlParser().parseText( xmlData )
		this.dummy = userCreationData.FINANCEUSER_ORACLE_ID.text()
	}

    /**
     * Clean Finance CIFOAPALP Duplicate Records
     */
    def cifoapalpCleanup() {
		ftvcoasCleanup()
		ftvacciCleanup()
		ftvftypCleanup()
		ftvfundCleanup()
		ftvorgnCleanup()
		ftvatypCleanup()
		ftvacctCleanup()
		ftvprogCleanup()
		ftvactvCleanup()
		ftvlocnCleanup()
		ftvprojCleanup()
	}

	//Chart - FTVCOAS
    def ftvcoasCleanup() {
	try {
            final String cleanCoaQuery = 	" BEGIN \n" +
											" DELETE \n" +
											" FROM FTVCOAS A \n" +
											" WHERE A.ROWID > \n" +
											" ANY (SELECT  B.rowid \n" +
											" FROM FTVCOAS B \n" +
											" WHERE A.FTVCOAS_COAS_CODE = B.FTVCOAS_COAS_CODE \n" +
											" AND TRUNC(A.FTVCOAS_NCHG_DATE) = TRUNC(B.FTVCOAS_NCHG_DATE));" +
											" \n " +
											" DECLARE \n " +
											" CURSOR C1 (c ftvcoas.ftvcoas_coas_code%TYPE) IS \n " +
											" SELECT a.ROWID c_rowid, a.ftvcoas_coas_code coas, \n " +
											"          a.ftvcoas_eff_date eff_date, a.ftvcoas_nchg_date nchg_date \n " +
											"     FROM ftvcoas a \n " +
											"    WHERE ftvcoas_coas_code= c order by ftvcoas_nchg_date;\n " +
											"      \n " +
											"   coas_nchg_date DATE; \n " +
											"   coas_rowid ROWID; \n " +
											"   cnt INTEGER; \n " +
											" BEGIN \n " +
											"   \n " +
											" FOR i IN (SELECT DISTINCT ftvcoas_coas_code coas  \n " +
											"               FROM ftvcoas WHERE ftvcoas_COAS_CODE='B')  \n " +
											"   LOOP  \n " +
											"     cnt := 0;  \n " +
											"     coas_nchg_date := null;  \n " +
											"     coas_rowid := null;  \n " +
											"     FOR j IN C1(i.coas)  \n " +
											"     LOOP  \n " +
											"        coas_rowid := j.C_rowid;  \n " +
											"         IF cnt != 0  \n " +
											"         THEN   \n " +
											"             IF j.eff_date < coas_nchg_date \n " +
											"             THEN  \n " +
											"                dbms_output.put_line ('coas - ' || j.coas || ' - nchg date - ' || coas_nchg_date || ' - eff date - ' || j.eff_date );  \n " +
											"                 \n " +
											"                UPDATE ftvcoas  \n " +
											"                   SET ftvcoas_eff_date = coas_nchg_date \n " +
											"                 WHERE ROWID = coas_rowid; \n " +
											"             END IF; \n " +
											"         END IF; \n " +
											"         coas_nchg_date := j.nchg_date; \n " +
											"         cnt := cnt +1; \n " +
											"     END LOOP; \n " +
											"   END LOOP; \n " +
											" END; \n " +
											" COMMIT; \n " +
											" \n " +
											" END; "
			CallableStatement cleanCall = this.connectCall.prepareCall( cleanCoaQuery )
            cleanCall.execute()
			connectInfo.tableUpdate( "FTVCOAS_CLEAN_UP", 0, 0, 0, 0, 1 )			
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FTVCOAS_CLEAN_UP", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Error while Cleaning up Chart - FTVCOAS table - " + e
            }
        }
        finally {
            cleanCall.close()
        }
    }
			  
	//Index - FTVACCI
    def ftvacciCleanup() {
	try {
		final String cleanAcciQuery = 	" BEGIN	 \n" +
										" DELETE \n" +
										" FROM FTVACCI A \n" +
										" WHERE A.ROWID >  \n" +
										" ANY (SELECT  B.rowid \n" +
										" FROM FTVACCI B \n" +
										" WHERE A.FTVACCI_COAS_CODE = B.FTVACCI_COAS_CODE \n" +
										" AND A.FTVACCI_ACCI_CODE = B.FTVACCI_ACCI_CODE \n" +
										" AND TRUNC(A.FTVACCI_NCHG_DATE) = TRUNC(B.FTVACCI_NCHG_DATE)); \n" +
										" \n" +
										" \n" +
										" DECLARE \n" +
										" CURSOR I1 (c ftvacci.ftvacci_coas_code%TYPE,i ftvacci.ftvacci_acci_code%TYPE) IS \n" +
										"   SELECT a.ROWID i_rowid, a.ftvacci_coas_code coas,  \n" +
										"          a.ftvacci_acci_code acci, a.ftvacci_eff_date eff_date, a.ftvacci_nchg_date nchg_date  \n" +
										"     FROM ftvacci a  \n" +
										"    WHERE ftvacci_coas_code= c \n" +
										"      AND ftvacci_acci_code= i order by ftvacci_nchg_date; \n" +
										"           \n" +
										"   acci_nchg_date DATE; \n" +
										"   acci_rowid ROWID; \n" +
										"   cnt INTEGER; \n" +
										" BEGIN \n" +
										"  \n" +
										"   FOR i IN (SELECT DISTINCT ftvacci_coas_code coas, ftvacci_acci_code acci \n" +
										"                           FROM ftvacci) \n" +
										"   LOOP \n" +
										"         cnt := 0; \n" +
										"         acci_nchg_date := null; \n" +
										"         acci_rowid := null; \n" +
										"         FOR j IN i1(i.coas, i.acci) \n" +
										"         LOOP \n" +
										"            acci_rowid := j.i_rowid; \n" +
										"             IF cnt != 0 \n" +
										"             THEN \n" +
										"                     IF j.eff_date < acci_nchg_date \n" +
										"                     THEN \n" +
										"                        dbms_output.put_line ('acci - ' || j.acci || ' - nchg date - ' || acci_nchg_date || ' - eff date - ' || j.eff_date );  \n" +
										"                        UPDATE ftvacci  \n" +
										"                           SET ftvacci_eff_date = acci_nchg_date \n" +
										"                         WHERE ROWID = acci_rowid; \n" +
										"                     END IF; \n" +
										"             END IF; \n" +
										"             acci_nchg_date := j.nchg_date; \n" +
										"             cnt := cnt +1; \n" +
										"         END LOOP; \n" +
										"   END LOOP; \n" +
										" END; \n" +
										" COMMIT; \n "+
										" \n " +
										" END; \n"

			CallableStatement cleanCall = this.connectCall.prepareCall( cleanAcciQuery )
            cleanCall.execute()
			connectInfo.tableUpdate( "FTVACCI_CLEAN_UP", 0, 0, 0, 0, 1 )			
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FTVACCI_CLEAN_UP", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Error while Cleaning up Accounting Index - FTVACCI table - " + e
            }
        }
        finally {
            cleanCall.close()
        }
    }


	//Fund Type - FTVFTYP
    def ftvftypCleanup() {
	try {
		final String cleanFtypQuery = 	" BEGIN	 \n" +
										" DELETE \n" +
										" FROM FTVFTYP A \n" +
										" WHERE A.ROWID > \n" +
										" ANY (SELECT  B.rowid \n" +
										" FROM FTVFTYP B \n" +
										" WHERE A.FTVFTYP_COAS_CODE = B.FTVFTYP_COAS_CODE \n" +
										" AND A.FTVFTYP_FTYP_CODE = B.FTVFTYP_FTYP_CODE \n" +
										" AND TRUNC(A.FTVFTYP_NCHG_DATE) = TRUNC(B.FTVFTYP_NCHG_DATE)); \n" +
										"  \n" +
										" DECLARE \n" +
										" CURSOR I1 (c ftvftyp.ftvftyp_coas_code%TYPE,f ftvftyp.ftvftyp_ftyp_code%TYPE) IS  \n" +
										"   SELECT a.ROWID i_rowid, a.ftvftyp_coas_code coas,  \n" +
										"                  a.ftvftyp_ftyp_code ftyp, a.ftvftyp_eff_date eff_date, a.ftvftyp_nchg_date nchg_date  \n" +
										"         FROM ftvftyp a  \n" +
										"    WHERE ftvftyp_coas_code= c \n" +
										"          AND ftvftyp_ftyp_code= f order by ftvftyp_nchg_date; \n" +
										"           \n" +
										"   ftyp_nchg_date DATE; \n" +
										"   ftyp_rowid ROWID; \n" +
										"   cnt INTEGER; \n" +
										" BEGIN \n" +
										"  \n" +
										" FOR i IN (SELECT DISTINCT ftvftyp_coas_code coas, ftvftyp_ftyp_code ftyp \n" +
										"                           FROM ftvftyp) \n" +
										"   LOOP \n" +
										"         cnt := 0; \n" +
										"         ftyp_nchg_date := null; \n" +
										"         ftyp_rowid := null; \n" +
										"         FOR j IN i1(i.coas, i.ftyp) \n" +
										"         LOOP \n" +
										"            ftyp_rowid := j.i_rowid; \n" +
										"             IF cnt != 0 \n" +
										"             THEN \n" +
										"                     IF j.eff_date < ftyp_nchg_date \n" +
										"                     THEN \n" +
										"                        dbms_output.put_line ('ftyp - ' || j.ftyp || ' - nchg date - ' || ftyp_nchg_date || ' - eff date - ' || j.eff_date );  \n" +
										"                        UPDATE ftvftyp  \n" +
										"                           SET ftvftyp_eff_date = ftyp_nchg_date \n" +
										"                         WHERE ROWID = ftyp_rowid; \n" +
										"                     END IF; \n" +
										"             END IF; \n" +
										"             ftyp_nchg_date := j.nchg_date; \n" +
										"             cnt := cnt +1; \n" +
										"         END LOOP; \n" +
										"   END LOOP; \n" +
										" END; \n" +
										" COMMIT; \n "+
										" \n " +
										" END; "
			CallableStatement cleanCall = this.connectCall.prepareCall( cleanFtypQuery )
            cleanCall.execute()
			connectInfo.tableUpdate( "FTVFTYP_CLEAN_UP", 0, 0, 0, 0, 1 )			
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FTVFTYP_CLEAN_UP", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Error while Cleaning up Fund Type - FTVFTYP table - " + e
            }
        }
        finally {
            cleanCall.close()
        }
    }

	//Fund - FTVFUND
    def ftvfundCleanup() {
	try {
		final String cleanFundQuery = 	" BEGIN \n" +
										" DELETE \n" +
										" FROM FTVFUND A \n" +
										" WHERE A.ROWID >  \n" +
										" ANY (SELECT  B.rowid \n" +
										" FROM FTVFUND B \n" +
										" WHERE A.FTVFUND_COAS_CODE = B.FTVFUND_COAS_CODE \n" +
										" AND A.FTVFUND_FUND_CODE = B.FTVFUND_FUND_CODE \n" +
										" AND TRUNC(A.FTVFUND_NCHG_DATE) = TRUNC(B.FTVFUND_NCHG_DATE)); \n" +
										"  \n" +
										" DECLARE \n" +
										"   CURSOR F1 (c ftvfund.ftvfund_coas_code%TYPE,f ftvfund.ftvfund_fund_code%TYPE) IS  \n" +
										"   SELECT a.ROWID f_rowid, a.ftvfund_coas_code coas,  \n" +
										" 		 a.ftvfund_fund_code fund, a.ftvfund_eff_date eff_date, a.ftvfund_nchg_date nchg_date  \n" +
										" 	FROM ftvfund a  \n" +
										"    WHERE ftvfund_coas_code= c \n" +
										" 	 AND ftvfund_fund_code= f order by ftvfund_nchg_date; \n" +
										" 	  \n" +
										" fund_nchg_date DATE; \n" +
										"   fund_rowid ROWID; \n" +
										"   cnt INTEGER; \n" +
										" BEGIN \n" +
										"  \n" +
										"   FOR i IN (SELECT DISTINCT ftvfund_coas_code coas, ftvfund_fund_code fund \n" +
										" 			  FROM FTVFUND) \n" +
										"   LOOP \n" +
										" 	cnt := 0; \n" +
										" 	fund_nchg_date := null; \n" +
										" 	fund_rowid := null; \n" +
										" 	FOR j IN f1(i.coas, i.fund) \n" +
										" 	LOOP \n" +
										" 	   fund_rowid := j.f_rowid; \n" +
										" 		IF cnt != 0 \n" +
										" 		THEN \n" +
										" 			IF j.eff_date < fund_nchg_date \n" +
										" 			THEN \n" +
										" 			   dbms_output.put_line ('fund - ' || j.fund || ' - nchg date - ' || fund_nchg_date || ' - eff date - ' || j.eff_date );  \n" +
										" 			   UPDATE ftvfund  \n" +
										" 				  SET ftvfund_eff_date = fund_nchg_date \n" +
										" 				WHERE ROWID = fund_rowid; \n" +
										" 			END IF; \n" +
										" 		END IF; \n" +
										" 		fund_nchg_date := j.nchg_date; \n" +
										" 		cnt := cnt +1; \n" +
										" 	END LOOP; \n" +
										"   END LOOP; \n" +
										" END; \n" +
										" COMMIT; \n "+
										" \n " +
										" END;"

			CallableStatement cleanCall = this.connectCall.prepareCall( cleanFundQuery )
            cleanCall.execute()
			connectInfo.tableUpdate( "FTVFUND_CLEAN_UP", 0, 0, 0, 0, 1 )			
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FTVFUND_CLEAN_UP", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Error while Cleaning up Fund - FTVFUND table - " + e
            }
        }
        finally {
            cleanCall.close()
        }
    }
	
// Organization - FTVORGN
    def ftvorgnCleanup() {
	try {
		final String cleanOrgnQuery = 	" BEGIN	\n" +
										" DELETE \n" +
										" FROM FTVORGN A \n" +
										" WHERE A.ROWID >  \n" +
										" ANY (SELECT  B.rowid \n" +
										" FROM FTVORGN B \n" +
										" WHERE A.FTVORGN_COAS_CODE = B.FTVORGN_COAS_CODE \n" +
										" AND A.FTVORGN_ORGN_CODE = B.FTVORGN_ORGN_CODE \n" +
										" AND TRUNC(A.FTVORGN_NCHG_DATE) = TRUNC(B.FTVORGN_NCHG_DATE)); \n" +
										"  \n" +
										" DECLARE \n" +
										"   CURSOR O1 (c ftvorgn.ftvorgn_coas_code%TYPE,o ftvorgn.ftvorgn_orgn_code%TYPE) IS  \n" +
										"   SELECT a.ROWID i_rowid, a.ftvorgn_coas_code coas,  \n" +
										"                  a.ftvorgn_orgn_code orgn, a.ftvorgn_eff_date eff_date, a.ftvorgn_nchg_date nchg_date  \n" +
										"         FROM ftvorgn a  \n" +
										"    WHERE ftvorgn_coas_code= c \n" +
										"          AND ftvorgn_orgn_code= o order by ftvorgn_nchg_date; \n" +
										"           \n" +
										"   orgn_nchg_date DATE; \n" +
										"   orgn_rowid ROWID; \n" +
										"   cnt INTEGER; \n" +
										" BEGIN \n" +
										"  \n" +
										" FOR i IN (SELECT DISTINCT ftvorgn_coas_code coas, ftvorgn_orgn_code orgn \n" +
										"                           FROM ftvorgn) \n" +
										"   LOOP \n" +
										"         cnt := 0; \n" +
										"         orgn_nchg_date := null; \n" +
										"         orgn_rowid := null; \n" +
										"         FOR j IN O1(i.coas, i.orgn) \n" +
										"         LOOP \n" +
										"            orgn_rowid := j.i_rowid; \n" +
										"             IF cnt != 0 \n" +
										"             THEN \n" +
										"                     IF j.eff_date < orgn_nchg_date \n" +
										"                     THEN \n" +
										"                        dbms_output.put_line ('orgn  - ' || j.orgn || ' - nchg date - ' || orgn_nchg_date || ' - eff date - ' || j.eff_date );  \n" +
										"                        UPDATE ftvorgn  \n" +
										"                           SET ftvorgn_eff_date = orgn_nchg_date \n" +
										"                         WHERE ROWID = orgn_rowid; \n" +
										"                     END IF; \n" +
										"             END IF; \n" +
										"             orgn_nchg_date := j.nchg_date; \n" +
										"             cnt := cnt +1; \n" +
										"         END LOOP; \n" +
										"   END LOOP; \n" +
										" END; \n" +
										" \n " +
										" COMMIT; \n "+
										" \n " +
										" END; \n"
				  
				  
			CallableStatement cleanCall = this.connectCall.prepareCall( cleanOrgnQuery )
            cleanCall.execute()
			connectInfo.tableUpdate( "FTVORGN_CLEAN_UP", 0, 0, 0, 0, 1 )			
        }
        catch (Exception e) {
            connectInfo.tableUpdate( "FTVORGN_CLEAN_UP", 0, 0, 0, 1, 0 )
            if (connectInfo.showErrors) {
                println "Error while Cleaning up Organization - FTVORGN table - " + e
            }
        }
        finally {
            cleanCall.close()
        }
    }

    // Account Type - FTVATYP
    def ftvatypCleanup() {
		try {
			final String cleanAtypQuery = 	" BEGIN	\n" +
											" DELETE \n" +
											" FROM FTVATYP A \n" +
											" WHERE A.ROWID > \n" +
											" ANY (SELECT  B.rowid \n" +
											" FROM FTVATYP B \n" +
											" WHERE A.FTVATYP_COAS_CODE = B.FTVATYP_COAS_CODE \n" +
											" AND A.FTVATYP_ATYP_CODE = B.FTVATYP_ATYP_CODE	\n" +
											" AND TRUNC(A.FTVATYP_NCHG_DATE) = TRUNC(B.FTVATYP_NCHG_DATE));	\n" +
											" 	\n" +
											" DECLARE	\n" +
											"   CURSOR a1 (c ftvatyp.ftvatyp_coas_code%TYPE,a ftvatyp.ftvatyp_atyp_code%TYPE) IS 	\n" +
											"   SELECT a.ROWID i_rowid, a.ftvatyp_coas_code coas, 	\n" +
											"                  a.ftvatyp_atyp_code atyp, a.ftvatyp_eff_date eff_date, a.ftvatyp_nchg_date nchg_date 	\n" +
											"         FROM ftvatyp a 	\n" +
											"    WHERE ftvatyp_coas_code= c	\n" +
											"          AND ftvatyp_atyp_code= a order by ftvatyp_nchg_date;	\n" +
											"          	\n" +
											"   atyp_nchg_date DATE;	\n" +
											"   atyp_rowid ROWID;	\n" +
											"   cnt INTEGER;	\n" +
											" BEGIN	\n" +
											" 	\n" +
											"   FOR i IN (SELECT DISTINCT ftvatyp_coas_code coas, ftvatyp_atyp_code atyp	\n" +
											"                           FROM ftvatyp)	\n" +
											"   LOOP	\n" +
											"         cnt := 0;	\n" +
											"         atyp_nchg_date := null;	\n" +
											"         atyp_rowid := null;	\n" +
											"         FOR j IN a1(i.coas, i.atyp)	\n" +
											"         LOOP	\n" +
											"            atyp_rowid := j.i_rowid;	\n" +
											"             IF cnt != 0	\n" +
											"             THEN	\n" +
											"                     IF j.eff_date < atyp_nchg_date	\n" +
											"                     THEN	\n" +
											"                        dbms_output.put_line ('atyp - ' || j.atyp || ' - nchg date - ' || atyp_nchg_date || ' - eff date - ' || j.eff_date ); 	\n" +
											"                        UPDATE ftvatyp 	\n" +
											"                           SET ftvatyp_eff_date = atyp_nchg_date	\n" +
											"                         WHERE ROWID = atyp_rowid;	\n" +
											"                     END IF;	\n" +
											"             END IF;	\n" +
											"             atyp_nchg_date := j.nchg_date;	\n" +
											"             cnt := cnt +1;	\n" +
											"         END LOOP;	\n" +
											"   END LOOP;	\n" +
											" END;	\n" +
											" COMMIT; \n "+
											" \n " +
											" END; "
				CallableStatement cleanCall = this.connectCall.prepareCall( cleanAtypQuery )
				cleanCall.execute()
				connectInfo.tableUpdate( "FTVATYP_CLEAN_UP", 0, 0, 0, 0, 1 )
			}
			catch (Exception e) {
				connectInfo.tableUpdate( "FTVATYP_CLEAN_UP", 0, 0, 0, 1, 0 )
				if (connectInfo.showErrors) {
					println "Error while Cleaning up Account Type - FTVATYP table - " + e
				}
			}
			finally {
				cleanCall.close()
			}
    }


// Account - FTVACCT 
    def ftvacctCleanup() {
		try {
			final String cleanAcctQuery =	" BEGIN	\n" +
											" DELETE \n" +
											" FROM FTVACCT A \n" +
											" WHERE A.ROWID > \n" +
											" ANY (SELECT  B.rowid	\n" +
											" FROM FTVACCT B \n" +
											" WHERE A.FTVACCT_COAS_CODE = B.FTVACCT_COAS_CODE \n" +
											" AND A.FTVACCT_ACCT_CODE = B.FTVACCT_ACCT_CODE \n" +
											" AND TRUNC(A.FTVACCT_NCHG_DATE) = TRUNC(B.FTVACCT_NCHG_DATE)); \n" +
											"  \n" +
											" DECLARE	\n" +
											"   CURSOR a1 (c ftvacct.ftvacct_coas_code%TYPE,a ftvacct.ftvacct_acct_code%TYPE) IS 	\n" +
											"   SELECT a.ROWID i_rowid, a.ftvacct_coas_code coas, 	\n" +
											"                  a.ftvacct_acct_code acct, a.ftvacct_eff_date eff_date, a.ftvacct_nchg_date nchg_date 	\n" +
											"         FROM ftvacct a 	\n" +
											"    WHERE ftvacct_coas_code= c	\n" +
											"          AND ftvacct_acct_code= a order by ftvacct_nchg_date;	\n" +
											"          	\n" +
											"   acct_nchg_date DATE;	\n" +
											"   acct_rowid ROWID;	\n" +
											"   cnt INTEGER;	\n" +
											" BEGIN	\n" +
											" 	\n" +
											"   FOR i IN (SELECT DISTINCT ftvacct_coas_code coas, ftvacct_acct_code acct	\n" +
											"                           FROM ftvacct)	\n" +
											"   LOOP	\n" +
											"         cnt := 0;	\n" +
											"         acct_nchg_date := null;	\n" +
											"         acct_rowid := null;	\n" +
											"         FOR j IN a1(i.coas, i.acct)	\n" +
											"         LOOP	\n" +
											"            acct_rowid := j.i_rowid;	\n" +
											"             IF cnt != 0	\n" +
											"             THEN	\n" +
											"                     IF j.eff_date < acct_nchg_date	\n" +
											"                     THEN	\n" +
											"                        dbms_output.put_line ('acct - ' || j.acct || ' - nchg date - ' || acct_nchg_date || ' - eff date - ' || j.eff_date ); 	\n" +
											"                        UPDATE ftvacct 	\n" +
											"                           SET ftvacct_eff_date = acct_nchg_date	\n" +
											"                         WHERE ROWID = acct_rowid;	\n" +
											"                     END IF;	\n" +
											"             END IF;	\n" +
											"             acct_nchg_date := j.nchg_date;	\n" +
											"             cnt := cnt +1;	\n" +
											"         END LOOP;	\n" +
											"   END LOOP;	\n" +
											" END;	\n" +
											" \n " +
											" COMMIT; \n "+
											" \n " +
											" END;"

			CallableStatement cleanCall = this.connectCall.prepareCall( cleanAcctQuery )
				cleanCall.execute()
				connectInfo.tableUpdate( "FTVACCT_CLEAN_UP", 0, 0, 0, 0, 1 )				
			}
			catch (Exception e) {
				connectInfo.tableUpdate( "FTVACCT_CLEAN_UP", 0, 0, 0, 1, 0 )
				if (connectInfo.showErrors) {
					println "Error while Cleaning up Account - FTVACCT table - " + e
				}
			}
			finally {
				cleanCall.close()
			}
    }

// Program - FTVPROG
    def ftvprogCleanup() {
		try {
			final String cleanProgQuery = 	" BEGIN	\n" +
											" DELETE	\n" +
											" FROM FTVPROG A \n" +
											" WHERE A.ROWID > \n" +
											" ANY (SELECT  B.rowid 	\n" +
											" FROM FTVPROG B \n" +
											" WHERE A.FTVPROG_COAS_CODE = B.FTVPROG_COAS_CODE \n" +
											" AND A.FTVPROG_PROG_CODE = B.FTVPROG_PROG_CODE \n" +
											" AND TRUNC(A.FTVPROG_NCHG_DATE) = TRUNC(B.FTVPROG_NCHG_DATE)); \n" +
											"  \n" +
											" DECLARE	\n" +
											"   CURSOR p1 (c ftvprog.ftvprog_coas_code%TYPE,p ftvprog.ftvprog_prog_code%TYPE) IS 	\n" +
											"   SELECT a.ROWID i_rowid, a.ftvprog_coas_code coas, 	\n" +
											"                  a.ftvprog_prog_code prog, a.ftvprog_eff_date eff_date, a.ftvprog_nchg_date nchg_date 	\n" +
											"         FROM ftvprog a 	\n" +
											"    WHERE ftvprog_coas_code= c	\n" +
											"          AND ftvprog_prog_code= p order by ftvprog_nchg_date;	\n" +
											"          	\n" +
											"   prog_nchg_date DATE;	\n" +
											"   prog_rowid ROWID;	\n" +
											"   cnt INTEGER;	\n" +
											" BEGIN	\n" +
											" 	\n" +
											"   FOR i IN (SELECT DISTINCT ftvprog_coas_code coas, ftvprog_prog_code prog	\n" +
											"                           FROM ftvprog)	\n" +
											" LOOP	\n" +
											"         cnt := 0;	\n" +
											"         prog_nchg_date := null;	\n" +
											"         prog_rowid := null;	\n" +
											"         FOR j IN p1(i.coas, i.prog)	\n" +
											"         LOOP	\n" +
											"            prog_rowid := j.i_rowid;	\n" +
											"             IF cnt != 0	\n" +
											"             THEN	\n" +
											"                     IF j.eff_date < prog_nchg_date	\n" +
											"                     THEN	\n" +
											"                        dbms_output.put_line ('prog - ' || j.prog || ' - nchg date - ' || prog_nchg_date || ' - eff date - ' || j.eff_date ); 	\n" +
											"                        UPDATE ftvprog 	\n" +
											"                           SET ftvprog_eff_date = prog_nchg_date	\n" +
											"                         WHERE ROWID = prog_rowid;	\n" +
											"                     END IF;	\n" +
											"             END IF;	\n" +
											"             prog_nchg_date := j.nchg_date;	\n" +
											"             cnt := cnt +1;	\n" +
											"         END LOOP;	\n" +
											"   END LOOP;	\n" +
											" END;	\n" +
											" \n " +
											" COMMIT; \n "+
											" \n " +
											" END;"
											
				CallableStatement cleanCall = this.connectCall.prepareCall( cleanProgQuery )
				cleanCall.execute()
				connectInfo.tableUpdate( "FTVPROG_CLEAN_UP", 0, 0, 0, 0, 1 )				
			}
			catch (Exception e) {
				connectInfo.tableUpdate( "FTVPROG_CLEAN_UP", 0, 0, 0, 1, 0 )				
				if (connectInfo.showErrors) {
					println "Error while Cleaning up Account - FTVPROG table - " + e
				}
			}
			finally {
				cleanCall.close()
			}
    }

// Location - FTVLOCN
    def ftvlocnCleanup() {
		try {
			final String cleanLocnQuery = 	" BEGIN	\n" +
											" DELETE \n" +
											" FROM FTVLOCN A \n" +
											" WHERE A.ROWID > \n" +
											" ANY (SELECT  B.rowid \n" +
											" FROM FTVLOCN B \n" +
											" WHERE A.FTVLOCN_COAS_CODE = B.FTVLOCN_COAS_CODE \n" +
											" AND A.FTVLOCN_LOCN_CODE = B.FTVLOCN_LOCN_CODE \n" +
											" AND TRUNC(A.FTVLOCN_NCHG_DATE) = TRUNC(B.FTVLOCN_NCHG_DATE)); \n" +
											"  \n" +
											" DECLARE	\n" +
											"   CURSOR p1 (c ftvlocn.ftvlocn_coas_code%TYPE,p ftvlocn.ftvlocn_locn_code%TYPE) IS 	\n" +
											"   SELECT a.ROWID i_rowid, a.ftvlocn_coas_code coas, 	\n" +
											"                  a.ftvlocn_locn_code locn, a.ftvlocn_eff_date eff_date, a.ftvlocn_nchg_date nchg_date 	\n" +
											"         FROM ftvlocn a 	\n" +
											"    WHERE ftvlocn_coas_code= c	\n" +
											"          AND ftvlocn_locn_code= p order by ftvlocn_nchg_date;	\n" +
											"          	\n" +
											"   locn_nchg_date DATE;	\n" +
											"   locn_rowid ROWID;	\n" +
											"   cnt INTEGER;	\n" +
											" BEGIN	\n" +
											" 	\n" +
											"   FOR i IN (SELECT DISTINCT ftvlocn_coas_code coas, ftvlocn_locn_code locn	\n" +
											"                           FROM ftvlocn)	\n" +
											"   LOOP	\n" +
											"         cnt := 0;	\n" +
											"         locn_nchg_date := null;	\n" +
											"         locn_rowid := null;	\n" +
											"         FOR j IN p1(i.coas, i.locn)	\n" +
											"         LOOP	\n" +
											"            locn_rowid := j.i_rowid;	\n" +
											"             IF cnt != 0	\n" +
											"             THEN	\n" +
											"                     IF j.eff_date < locn_nchg_date	\n" +
											"                     THEN	\n" +
											"                        dbms_output.put_line ('locn - ' || j.locn || ' - nchg date - ' || locn_nchg_date || ' - eff date - ' || j.eff_date ); 	\n" +
											"                        UPDATE ftvlocn 	\n" +
											"                           SET ftvlocn_eff_date = locn_nchg_date	\n" +
											"                         WHERE ROWID = locn_rowid;	\n" +
											"                     END IF;	\n" +
											"             END IF;	\n" +
											"             locn_nchg_date := j.nchg_date;	\n" +
											"             cnt := cnt +1;	\n" +
											"         END LOOP;	\n" +
											"   END LOOP;	\n" +
											" END;	\n" +
											" COMMIT; \n "+
											" \n " +
											" END; \n"
			
				CallableStatement cleanCall = this.connectCall.prepareCall( cleanLocnQuery )
				cleanCall.execute()
				connectInfo.tableUpdate( "FTVLOCN_CLEAN_UP", 0, 0, 0, 0, 1 )				
			}
			catch (Exception e) {
				connectInfo.tableUpdate( "FTVLOCN_CLEAN_UP", 0, 0, 0, 1, 0 )
				if (connectInfo.showErrors) {
					println "Error while Cleaning up Location - FTVLOCN table - " + e
				}
			}
			finally {
				cleanCall.close()
			}
    }
	

	//Activity - FTVACTV
    def ftvactvCleanup() {
		try {
			final String cleanActvQuery = 	" BEGIN	\n" +
											" DELETE \n" +
											" FROM FTVACTV A \n" +
											" WHERE A.ROWID > \n" +
											" ANY (SELECT  B.rowid \n" +
											" FROM FTVACTV B \n" +
											" WHERE A.FTVACTV_COAS_CODE = B.FTVACTV_COAS_CODE \n" +
											" AND A.FTVACTV_ACTV_CODE = B.FTVACTV_ACTV_CODE \n" +
											" AND TRUNC(A.FTVACTV_NCHG_DATE) = TRUNC(B.FTVACTV_NCHG_DATE)); \n" +
											"  \n" +
											" DECLARE	\n" +
											"   CURSOR p1 (c ftvactv.ftvactv_coas_code%TYPE,p ftvactv.ftvactv_actv_code%TYPE) IS 	\n" +
											"   SELECT a.ROWID i_rowid, a.ftvactv_coas_code coas, 	\n" +
											"                  a.ftvactv_actv_code actv, a.ftvactv_eff_date eff_date, a.ftvactv_nchg_date nchg_date 	\n" +
											"         FROM ftvactv a 	\n" +
											"    WHERE ftvactv_coas_code= c	\n" +
											"          AND ftvactv_actv_code= p order by ftvactv_nchg_date;	\n" +
											"          	\n" +
											"   actv_nchg_date DATE;	\n" +
											"   actv_rowid ROWID;	\n" +
											"   cnt INTEGER;	\n" +
											" BEGIN	\n" +
											" 	\n" +
											"   FOR i IN (SELECT DISTINCT ftvactv_coas_code coas, ftvactv_actv_code actv	\n" +
											"                           FROM ftvactv)	\n" +
											"   LOOP	\n" +
											"         cnt := 0;	\n" +
											"         actv_nchg_date := null;	\n" +
											"         actv_rowid := null;	\n" +
											"         FOR j IN p1(i.coas, i.actv)	\n" +
											"         LOOP	\n" +
											"            actv_rowid := j.i_rowid;	\n" +
											"             IF cnt != 0	\n" +
											"             THEN	\n" +
											"                     IF j.eff_date < actv_nchg_date	\n" +
											"                     THEN	\n" +
											"                        dbms_output.put_line ('actv - ' || j.actv || ' - nchg date - ' || actv_nchg_date || ' - eff date - ' || j.eff_date ); 	\n" +
											"                        UPDATE ftvactv 	\n" +
											"                           SET ftvactv_eff_date = actv_nchg_date	\n" +
											"                         WHERE ROWID = actv_rowid;	\n" +
											"                     END IF;	\n" +
											"             END IF;	\n" +
											"             actv_nchg_date := j.nchg_date;	\n" +
											"             cnt := cnt +1;	\n" +
											"         END LOOP;	\n" +
											"   END LOOP;	\n" +
											" END;	\n" +
											" \n " +
											" COMMIT; \n "+
											" \n " +
											" END; \n"
			CallableStatement cleanCall = this.connectCall.prepareCall( cleanActvQuery )
				cleanCall.execute()
				connectInfo.tableUpdate( "FTVACTV_CLEAN_UP", 0, 0, 0, 0, 1 )				
			}
			catch (Exception e) {
				connectInfo.tableUpdate( "FTVACTV_CLEAN_UP", 0, 0, 0, 1, 0 )
				if (connectInfo.showErrors) {
					println "Error while Cleaning up Activity - FTVACTV table - " + e
				}
			}
			finally {
				cleanCall.close()
			}
    }


//Project - FTVPROJ
    def ftvprojCleanup() {
		try {
			final String cleanProjQuery = 	" BEGIN	\n" +
											" DELETE \n" +
											" FROM FTVPROJ A \n" +
											" WHERE A.ROWID > \n" + 
											" ANY (SELECT  B.rowid	\n" +
											" FROM FTVPROJ B	\n" +
											" WHERE A.FTVPROJ_COAS_CODE = B.FTVPROJ_COAS_CODE	\n" +
											" AND A.FTVPROJ_CODE = B.FTVPROJ_CODE	\n" +
											" AND TRUNC(A.FTVPROJ_EFF_DATE) = TRUNC(B.FTVPROJ_EFF_DATE));	\n" +
											" 	\n" +
											" DELETE	\n" +
											" FROM FTVPROJ A 	\n" +
											" WHERE A.FTVPROJ_USER_ID='GRAILS' 	\n" +
											" AND EXISTS (SELECT 1	\n" + 
											" FROM FTVPROJ B 	\n" +
											" WHERE A.FTVPROJ_COAS_CODE=B.FTVPROJ_COAS_CODE 	\n" +
											" AND A.FTVPROJ_CODE=B.FTVPROJ_CODE	\n" +
											" AND B.FTVPROJ_USER_ID!='GRAILS');	\n" +
											" \n " +
											" COMMIT; \n "+
											" \n " +											
											" END;	\n" 
				CallableStatement cleanCall = this.connectCall.prepareCall( cleanProjQuery )
				cleanCall.execute()
				connectInfo.tableUpdate( "FTVPROJ_CLEAN_UP", 0, 0, 0, 0, 1 )				
			}
			catch (Exception e) {
				connectInfo.tableUpdate( "FTVPROJ_CLEAN_UP", 0, 0, 0, 1, 0 )
				if (connectInfo.showErrors) {
					println "Error while Cleaning up Project - FTVPROJ table - " + e
				}
			}
			finally {
				cleanCall.close()
			}
    }

}





