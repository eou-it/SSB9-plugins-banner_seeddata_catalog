-- *****************************************************************************************
-- * Copyright 2010-2013 Ellucian Company L.P. and its affiliates.                         *
-- *****************************************************************************************

-- this script will generate a Select statement For the table entered And report records that are 
-- horizon test data.  Those records are identified With GRAILS in the data origin, user id Or 01/01/2010
-- in the activity date. 
-- This script only runs On Oracle 11.1.0.5 And above.
 
prompt enter the table you want to generate query Call too
Accept tableName char 
prompt enter number of rows to Return
Accept tableCnt number  

spool &DEFAULT_SPOOL_DIR./TestDataQuery.txt 
Set head off pagesize 0
set linesize 300
 
Set serveroutput On Size 1000000
set linesize 400
Declare 
  
     tablec  number := nvl(&&tableCnt, 99999); 
     tablen  varchar2(30) := upper('&&tableName'); 
     
     
     v_rc SYS_REFCURSOR;
     any_query  varchar2(4000);
     banner_column varchar2(30);  
     tab_column   varchar2(30); 
     tab_where    varchar2(200); 
     cursor columns_c is 
       Select column_name 
       from all_tab_columns
       where column_name = banner_column;
  
     cursor tabs_c is 
        Select table_name from dba_tables where table_name = tablen; 
     tabs varchar2(30);

     cursor api_c is
        select GURMESG_BASE_TABLE from gurmesg where gurmesg_base_table = upper(tablen);
     apitab varchar2(30);
    -- generates dyn sql For sqlplus 
    PROCEDURE seed_sql(
                     p_refcursor IN OUT SYS_REFCURSOR
                     ) AS
  
       v_desc       DBMS_SQL.DESC_TAB;
       v_cols       BINARY_INTEGER;
       v_cursor     BINARY_INTEGER;
  
       v_varchar2   VARCHAR2(4000);
       v_number     NUMBER;
       v_date       DATE;
 
       v_data       VARCHAR2(32767);
       v_data2       VARCHAR2(32767);
       i  pls_integer := 0 ; 
       ii pls_integer := 0; 
       colname  varchar2(30); 
       collen   pls_integer := 0; 
    BEGIN
 
      /* Convert refcursor "parameter" to DBMS_SQL cursor... */
      v_cursor := DBMS_SQL.TO_CURSOR_NUMBER(p_refcursor);
 
      /* Describe the cursor... */
      DBMS_SQL.DESCRIBE_COLUMNS(v_cursor, v_cols, v_desc);
        /* Define columns to be fetched. We're only using V2, NUM, DATE for example... */
      ii := 0; 
      FOR i IN 1 .. v_cols LOOP
      
           IF v_desc(i).col_type = 2 THEN
             DBMS_SQL.DEFINE_COLUMN(v_cursor, i, v_number);
           ELSIf v_desc(i).col_type = 12 THEN
            DBMS_SQL.DEFINE_COLUMN(v_cursor, i, v_date);
           ELSE
            DBMS_SQL.DEFINE_COLUMN(v_cursor, i, v_varchar2, 4000);
          END IF;
       
      END LOOP;
       /* Format And output the header */
      DBMS_OUTPUT.NEW_LINE;
      FOR i IN 1 .. v_cols LOOP
            colname := substr(v_desc(i).col_name, 9, length(v_desc(i).col_name) - 8  ); 
            If colname in ( 'DATA_ORIGIN' , 'USER_ID', 'ACTIVITY_DATE')
               Then collen := 12; 
            Else  
            	  collen  :=  greatest (  v_desc(i).col_max_len / 4,  length(colname) ) + 1;
            End If; 
          
            If colname != 'VERSION' and colname != 'SURROGATE_ID'  then 
              v_data := v_data ||
                   CASE v_desc(i).col_type
                      WHEN 2
                         THEN LPAD(colname, collen , ' ')
                      WHEN 12
                         THEN RPAD(colname, 12, ' ')
                      Else RPAD(colname, collen, ' ')
                    End || ' ';
               v_data2 := v_data2 ||
                   CASE v_desc(i).col_type
                      WHEN 2
                        THEN LPAD('-', collen , '-')
                      WHEN 12
                        THEN RPAD('-', 12, '-')
                      ELSE RPAD('-', collen , '-')
                   END || ' ';
            End If; 
      END LOOP;
      DBMS_OUTPUT.PUT_LINE(v_data);
      DBMS_OUTPUT.PUT_LINE(v_data2);
 
      /* Fetch all data but output only number of records asked for... */
      ii := 0; 
      WHILE DBMS_SQL.FETCH_ROWS(v_cursor) > 0 LOOP
 
         v_data := NULL;
 
         FOR i IN 1 .. v_cols LOOP
            colname := substr(v_desc(i).col_name, 9, length(v_desc(i).col_name) - 8  ); 
            If colname in ( 'DATA_ORIGIN' , 'USER_ID')
               Then collen := 12; 
             Else  
            	  collen  := greatest (  v_desc(i).col_max_len / 4,  length(colname) ) + 1;
            End If; 
            If colname != 'VERSION' and colname != 'SURROGATE_ID'  then 
            IF v_desc(i).col_type = 2 THEN
               DBMS_SQL.COLUMN_VALUE(v_cursor, i, v_number);
               If v_number is null Then v_number := 0; end if; 
               v_data := v_data ||
                         LPAD(v_number, collen, ' ' ) || ' '  ;
            ELSIF v_desc(i).col_type = 12 THEN
               DBMS_SQL.COLUMN_VALUE(v_cursor, i, v_date);
               If v_date is null Then 
               	    v_varchar2 := ' '; 
               	    v_data := v_data || RPAD(v_varchar2, collen, ' ') || ' '; 
               Else  v_data := v_data || RPAD(v_date, 12, ' ') || ' ' ;
               end if; 
            ELSE
               DBMS_SQL.COLUMN_VALUE(v_cursor, i, v_varchar2);
               If v_varchar2 is null Then v_varchar2 := ' '; end if; 
               v_data := v_data ||
                         RPAD(v_varchar2, collen, ' ') || ' ';
            END IF;
           End If; 
         END LOOP;
 
         DBMS_OUTPUT.PUT_LINE(v_data);
         ii := ii + 1;
         If ii > tablec Then 
         	  Exit;
         End If; 
 
      END LOOP;
      DBMS_SQL.CLOSE_CURSOR(v_cursor);
 
  End seed_sql;

begin 
     
     Open tabs_c;
     Fetch tabs_c into tabs;
     If tabs_c%notfound Then 
     	   Close tabs_c;
     	   dbms_output.put_line('No such table, ' || tablen); 
     	   Return;
     End If;
  --   dbms_output.put_line('Horizon Test Data for ' || tablen); 
     Close tabs_c;

     open api_c;
     fetch api_c into apitab;
     if api_c%notfound then
        apitab := null;
     end if;
     close api_c;
  --   dbms_output.put_line('api table: ' || apitab) ;
     --  Format where clause, looking For data origin Or user id = grails, Or activity Date = 1/1/2010
     if  apitab is null then
         dbms_output.put_line('find table by activity date') ;
         banner_column := upper(tablen) || '_ACTIVITY_DATE';
         Open columns_c;
         Fetch columns_c into tab_column;
         If columns_c%notfound Then
               tab_where   := '';
         Else
               tab_where := ' where trunc( ' ||  banner_column || ') = to_date(' || chr(39) || '01012010' ||  chr(39) ||  ' ,' ||
                                       chr(39) || 'MMDDYYYY' || chr(39) || ')'  ;
         End If;
         Close columns_c;

     else
       banner_column := upper(tablen) || '_DATA_ORIGIN';
       Open columns_c;
       Fetch columns_c into tab_column;
       If columns_c%notfound Then
           Close columns_c; 
           banner_column := upper(tablen) || '_USER_ID';
           Open columns_c;
           Fetch columns_c into tab_column; 
           If columns_c%notfound Then 
                 close columns_c; 
                 banner_column := upper(tablen) || '_ACTIVITY_DATE';
                 Open columns_c;
                 Fetch columns_c into tab_column; 
                  If columns_c%notfound Then 
                     tab_where   := ''; 
                 Else 
                     tab_where := ' where trunc( ' ||  banner_column || ') = to_date(' || chr(39) || '01012010' ||  chr(39) ||  ' ,' || 
                                       chr(39) || 'MMDDYYYY' || chr(39) || ')'  ; 
                 End If; 
                  Close columns_c;
           Else 
               Close columns_c; 
               tab_where := ' where ' ||  banner_column || ' = ' ||  chr(39) ||'GRAILS' ||  chr(39)  ; 
           End If;
       Else
        Close columns_c; 
        tab_where := ' where ' ||  banner_column || ' = ' ||  chr(39) ||'GRAILS'||  chr(39)  ; 
       End If; 
     End if;

  
     any_query := 'select * from ' || tablen || tab_where || ' order by 1,2,3';
   --  dbms_output.put_line('query: ' || any_query)    ;
     OPEN v_rc FOR any_query;
     seed_sql(v_rc);
 
End;
/ 

spool off;
exit; 
