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
            final String cleanCoaQuery = " BEGIN \n" +
									" DELETE \n" +
									" FROM FTVCOAS A \n" +
									" WHERE A.ROWID > \n" + 
									" ANY (SELECT  B.rowid \n" +
									" FROM FTVCOAS B \n" +
									" WHERE A.FTVCOAS_COAS_CODE = B.FTVCOAS_COAS_CODE \n" +
									" AND TRUNC(A.FTVCOAS_NCHG_DATE) = TRUNC(B.FTVCOAS_NCHG_DATE));" +
									" \n " +
									" DELETE \n" +
									" FROM FTVCOAS A  \n" +
									" WHERE A.FTVCOAS_USER_ID='GRAILS'  \n" +
									" AND EXISTS (SELECT 1  \n" +
									" FROM FTVCOAS B  \n" +
									" WHERE A.FTVCOAS_COAS_CODE=B.FTVCOAS_COAS_CODE  \n" +
									" AND B.FTVCOAS_USER_ID!='GRAILS');  \n" +
									" \n " +
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
										" DELETE \n" +
										" FROM FTVACCI A  \n" +
										" WHERE A.FTVACCI_USER_ID='GRAILS'  \n" +
										" AND EXISTS (SELECT 1  \n" +
										" FROM FTVACCI B  \n" +
										" WHERE A.FTVACCI_COAS_CODE=B.FTVACCI_COAS_CODE  \n" +
										" AND A.FTVACCI_ACCI_CODE=B.FTVACCI_ACCI_CODE \n" +
										" AND B.FTVACCI_USER_ID!='GRAILS');  \n" +
										" \n " +
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
										" DELETE \n" +
										" FROM FTVFTYP A  \n" +
										" WHERE A.FTVFTYP_USER_ID='GRAILS'  \n" +
										" AND EXISTS (SELECT 1 \n" + 
										" FROM FTVFTYP B  \n" +
										" WHERE A.FTVFTYP_COAS_CODE=B.FTVFTYP_COAS_CODE  \n" +
										" AND A.FTVFTYP_FTYP_CODE=B.FTVFTYP_FTYP_CODE \n" +
										" AND B.FTVFTYP_USER_ID!='GRAILS');  \n" +
										" \n " +
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
										" DELETE \n" +	
										" FROM FTVFUND A  \n" +	
										" WHERE A.FTVFUND_USER_ID='GRAILS'  \n" +	
										" AND EXISTS (SELECT 1  \n" +	
										" FROM FTVFUND B  \n" +	
										" WHERE A.FTVFUND_COAS_CODE=B.FTVFUND_COAS_CODE  \n" +	
										" AND A.FTVFUND_FUND_CODE=B.FTVFUND_FUND_CODE \n" +	
										" AND B.FTVFUND_USER_ID!='GRAILS');  \n" +
										" \n " +
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
										" DELETE \n" +
										" FROM FTVORGN A  \n" +
										" WHERE A.FTVORGN_USER_ID='GRAILS' \n" + 
										" AND EXISTS (SELECT 1 \n" + 
										" FROM FTVORGN B \n" + 
										" WHERE A.FTVORGN_COAS_CODE=B.FTVORGN_COAS_CODE \n" + 
										" AND A.FTVORGN_ORGN_CODE=B.FTVORGN_ORGN_CODE \n" +
										" AND B.FTVORGN_USER_ID!='GRAILS'); \n" +
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
											" DELETE \n" +
											" FROM FTVATYP A \n" + 
											" WHERE A.FTVATYP_USER_ID='GRAILS' \n" +  
											" AND EXISTS (SELECT 1 \n" +  
											" FROM FTVATYP B \n" +  
											" WHERE A.FTVATYP_COAS_CODE=B.FTVATYP_COAS_CODE \n" +  
											" AND A.FTVATYP_ATYP_CODE=B.FTVATYP_ATYP_CODE \n" + 
											" AND B.FTVATYP_USER_ID!='GRAILS'); \n" +
											" \n " +
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
			final String cleanAcctQuery = 	" BEGIN	\n" +
											" DELETE \n" +
											" FROM FTVACCT A \n" +
											" WHERE A.ROWID > \n" + 
											" ANY (SELECT  B.rowid	\n" +
											" FROM FTVACCT B \n" +
											" WHERE A.FTVACCT_COAS_CODE = B.FTVACCT_COAS_CODE \n" +
											" AND A.FTVACCT_ACCT_CODE = B.FTVACCT_ACCT_CODE \n" +
											" AND TRUNC(A.FTVACCT_NCHG_DATE) = TRUNC(B.FTVACCT_NCHG_DATE)); \n" +
											"  \n" +
											" DELETE \n" +
											" FROM FTVACCT A \n" + 
											" WHERE A.FTVACCT_USER_ID='GRAILS' \n" + 
											" AND EXISTS (SELECT 1 \n" + 
											" FROM FTVACCT B \n" + 
											" WHERE A.FTVACCT_COAS_CODE=B.FTVACCT_COAS_CODE \n" + 
											" AND A.FTVACCT_ACCT_CODE=B.FTVACCT_ACCT_CODE \n" +
											" AND B.FTVACCT_USER_ID!='GRAILS'); \n" +
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
											" DELETE \n" +
											" FROM FTVPROG A \n" + 
											" WHERE A.FTVPROG_USER_ID='GRAILS' \n" +
											" AND EXISTS (SELECT 1 \n" + 
											" FROM FTVPROG B \n" + 
											" WHERE A.FTVPROG_COAS_CODE=B.FTVPROG_COAS_CODE \n" + 
											" AND A.FTVPROG_PROG_CODE=B.FTVPROG_PROG_CODE \n" +
											" AND B.FTVPROG_USER_ID!='GRAILS'); \n" +
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
											" DELETE \n" +
											" FROM FTVLOCN A \n" + 
											" WHERE A.FTVLOCN_USER_ID='GRAILS'  \n" +
											" AND EXISTS (SELECT 1 \n" +
											" FROM FTVLOCN B \n" + 
											" WHERE A.FTVLOCN_COAS_CODE=B.FTVLOCN_COAS_CODE \n" + 
											" AND A.FTVLOCN_LOCN_CODE=B.FTVLOCN_LOCN_CODE \n" +
											" AND B.FTVLOCN_USER_ID!='GRAILS'); \n" +
											" \n " +
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
											" DELETE \n" +
											" FROM FTVACTV A  \n" +
											" WHERE A.FTVACTV_USER_ID='GRAILS' \n" +
											" AND EXISTS (SELECT 1 \n" + 
											" FROM FTVACTV B \n" + 
											" WHERE A.FTVACTV_COAS_CODE=B.FTVACTV_COAS_CODE \n" + 
											" AND A.FTVACTV_ACTV_CODE=B.FTVACTV_ACTV_CODE \n" +
											" AND B.FTVACTV_USER_ID!='GRAILS'); \n" +
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





