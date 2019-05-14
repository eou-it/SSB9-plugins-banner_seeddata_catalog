-- *****************************************************************************************
-- * Copyright 2013 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************
SET ECHO ON
SET TERMOUT ON
SET FEEDBACK ON
SET TRIMSPOOL ON

SPOOL generalCommunicationData_file.log
DROP TABLE testclob;
DROP TABLE testtables;
DROP SEQUENCE clob_sequence;

PROMPT script will create GeneralCommunicationData.xml

-- Create a table to hold the generated xml
CREATE TABLE testclob
(
   id                                 NUMBER
  ,text                               CLOB
  ,CONSTRAINT testclob_pk PRIMARY KEY (id)
);

-- create a table to hold the list of the tables to extract

CREATE TABLE testtables
(
   id                                 NUMBER
  ,table_name                         VARCHAR2 (30)
  ,table_select                       VARCHAR2 (4000) DEFAULT NULL
);

CREATE SEQUENCE clob_sequence INCREMENT BY 1;


-- Insert the tables to extract, in proper build dependency order
INSERT INTO testtables (id, table_name, table_select)
     VALUES (clob_sequence.NEXTVAL, 'GCRFLDR', NULL);
INSERT INTO testtables
     VALUES (clob_sequence.NEXTVAL, 'GCRORAN', NULL);
INSERT INTO testtables
     VALUES (clob_sequence.NEXTVAL, 'GCRMBAC', NULL);
INSERT INTO testtables
     VALUES (clob_sequence.NEXTVAL, 'GCROADR', NULL);
INSERT INTO testtables
     VALUES (clob_sequence.NEXTVAL, 'GCBQURY', NULL);
REM INSERT INTO testtables
REM      VALUES (clob_sequence.NEXTVAL, 'GCRSLIS', null );
REM INSERT INTO testtables
REM      VALUES (clob_sequence.NEXTVAL, 'GCRLENT', null );
REM INSERT INTO testtables
REM      VALUES (clob_sequence.NEXTVAL, 'GCBEERR', null );
INSERT INTO testtables
        VALUES (
                  clob_sequence.NEXTVAL
                 ,'GCRCFLD'
                 ,'SELECT gcrcfld_surrogate_id
      ,gcrcfld_name
      ,gcrcfld_folder_id
      ,gcrcfld_immutableid
      ,gcrcfld_status
      ,gcrcfld_user_id
      ,gcrcfld_activity_date
      ,gcrcfld_description
      ,HTF.escape_sc (gcrcfld_formatstring) gcrcfld_formatstring
      ,gcrcfld_rule_uri
      ,gcrcfld_groovy_formatter
      ,gcrcfld_preview_value
      ,gcrcfld_render_as_html
      ,gcrcfld_created_by
      ,gcrcfld_returns_array
      ,gcrcfld_statement_type
      ,gcrcfld_rule_statement
      ,gcrcfld_version
      ,gcrcfld_data_origin
      ,gcrcfld_vpdi_code
  FROM gcrcfld');
INSERT INTO testtables
     VALUES (clob_sequence.NEXTVAL, 'GCRFPRM', NULL);
REM INSERT INTO testtables
REM      VALUES (clob_sequence.NEXTVAL, 'GCBCJOB', null );
REM INSERT INTO testtables
REM      VALUES (clob_sequence.NEXTVAL, 'GCRCJIT', null );
REM INSERT INTO testtables
REM      VALUES (clob_sequence.NEXTVAL, 'GCBRDAT', null );
INSERT INTO testtables
     VALUES (clob_sequence.NEXTVAL, 'GCRFVAL', NULL);
INSERT INTO testtables
     VALUES (clob_sequence.NEXTVAL, 'GCBTMPL', NULL);
INSERT INTO testtables
     VALUES (clob_sequence.NEXTVAL, 'GCBEMTL', NULL);
REM INSERT INTO testtables
REM      VALUES (clob_sequence.NEXTVAL, 'GCRCITM', null );
REM INSERT INTO testtables
REM      VALUES (clob_sequence.NEXTVAL, 'GCREITM', null );



CREATE OR REPLACE PROCEDURE p_xmltable (p_table IN VARCHAR2, p_select IN VARCHAR2)
--
--  This procedure extracts the rows from the given table as xml and stores the
--  data in a table for later output
--
IS
   my_context                         NUMBER := 0;
   xmlresult                          CLOB;
   fmtclob                            CLOB;
   clob_length                        NUMBER;

   lv_xml                             VARCHAR2 (32727);

   lv_start                           NUMBER;
   lv_temp                            NUMBER;

BEGIN
   -- Create a New Context
   my_context := DBMS_XMLGEN.newcontext (nvl(p_select, 'select * from ' || p_table));
   -- Do Not omit empty tags
   DBMS_XMLGEN.setnullhandling (my_context, 2);


   -- Set the Row Element
   DBMS_XMLGEN.setrowsettag (my_context, 'GENERALDATA');
   DBMS_XMLGEN.setrowtag (my_context, UPPER (p_table));
   -- Generate the xml
   xmlresult := DBMS_XMLGEN.getxml (my_context);
   DBMS_LOB.createtemporary (fmtclob, TRUE, DBMS_LOB.session);
   clob_length := DBMS_LOB.getlength (xmlresult);
   lv_start := 36;

   -- Until we are run oracle 12 or higher, we have to break the clob up into chunks
   -- so we can spool the data. In 12 and higher you can use dbms_output directly, as they
   -- removed the limit.
   IF clob_length > 1
   THEN
      LOOP
         lv_temp := clob_length - lv_start;

         IF lv_temp > 32726
         THEN
            lv_temp := 32726;
         END IF;

         DBMS_LOB.read (xmlresult
                       ,lv_temp
                       ,lv_start
                       ,lv_xml);
         lv_start := lv_start + 32726;
         DBMS_LOB.writeappend (fmtclob, lv_temp, lv_xml);
         EXIT WHEN lv_start >= clob_length;
      END LOOP;

      clob_length := DBMS_LOB.getlength (fmtclob);
      -- 14 = length of </studentdata>
      DBMS_LOB.TRIM (fmtclob, (clob_length - 14));

      DBMS_LOB.ERASE (xmlresult, clob_length, 1);
      DBMS_LOB.freetemporary (xmlresult);
   END IF;

   INSERT INTO testclob (id, text)
        VALUES (clob_sequence.NEXTVAL, fmtclob);

   COMMIT;
END p_xmltable;
/

SELECT COUNT (*) FROM testtables;


SET SERVEROUTPUT ON SIZE 1000000

REM This calls the xml generation procedure on each table listed

BEGIN
   FOR c1 IN (  SELECT table_name, table_select
                  FROM testtables
              ORDER BY id)
   LOOP
      dbms_output.put_Line('selected '||c1.table_name);
      p_xmltable (c1.table_name, c1.table_select);
   END LOOP;
END;
/


SELECT COUNT (*) FROM testclob;

SET PAGES 0
SET LINESIZE 32767
SET LONG 10000000
SET HEAD OFF
COLUMN text FORMAT a9999
SET TERM OFF
SET ECHO OFF
SET VERIFY OFF
SET FEEDBACK OFF
SPOOL OFF;
SPOOL GeneralCommunicationData.xml

-- Write out the header
SELECT    '<?xml version="1.0" encoding="Windows-1252" ?>'|| CHR (10) || '<GENERALCOMMUNICATIONDATA>'
  FROM DUAL;

-- Write out the xml data
  SELECT text
    FROM testclob
   WHERE DBMS_LOB.getlength (text) > 5
ORDER BY id;
-- Write out the footer
SELECT '</GENERALCOMMUNICATIONDATA>' FROM DUAL;


SPOOL OFF

-- Clean up
DROP TABLE testclob;
DROP TABLE testtables;
DROP SEQUENCE clob_sequence;

EXIT;