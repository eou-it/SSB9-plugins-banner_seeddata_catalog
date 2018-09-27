-- *****************************************************************************************
-- * Copyright 2010-2013 Ellucian Company L.P. and its affiliates.                         *
-- *****************************************************************************************






-- this script will generate the parts you need to build the prepared callable statement For an api 
prompt enter the table you want to generate api Call too
Accept tableName char   
prompt enter Name of class (ClassTimesDML For ssrmeet For example)
Accept classname char 

spool &DEFAULT_SPOOL_DIR./preparedCall.groovy 
Set head off pagesize 0

 
 
Set serveroutput On Size 1000000
Declare 
  
  p_table varchar2(30) := upper('&&tableName');
  p_select varchar2(250); 
  p_id number; 
  p_parmcnt  number; 
  ptext varchar2(250); 
  p_api  varchar2(250); 
  parm_column varchar2(250); 
  cnt number := 0; 
  api_type varchar2(20); 
  
  cursor find_api_c is 
    Select gurmesg_package_name 
     from gurmesg where gurmesg_base_table = p_table; 
  
  cursor parms_c is 
  select text from sys.dba_source where type =   'PACKAGE' and name = p_api  
        and line > ( select x.line from sys.dba_source x where x.type =   'PACKAGE' and x.name = p_api  
                     and x.text like '%' || api_type || '%' 
                     and  (  (  instr(x.text,'--',1) > 0 and  instr(x.text,'--',1) > instr(x.text,  api_type  ,1) )  or 
                          (  instr(x.text,'*',1)>  0 and   instr(x.text,'*',1) > instr(x.text,  api_type  ,1) )     Or 
                        (  instr(x.text,'--',1) = 0  and  instr(x.text,'*',1) = 0 )   )  )   
        and  (  (  instr(text,'--',1) > 0 and  instr(text,'--',1) > instr(text,  api_type ,1) )  or 
             (  instr(text,'*',1)>  0 and   instr(text,'*',1) > instr(text, api_type  ,1) )     Or 
             (  instr(text,'--',1) = 0  and  instr(text,'*',1) = 0 )   )  
        order by line;
        
   
  cursor columntype is 
     Select data_Type, data_scale from all_tab_columns  
     where table_name = p_table
     And column_name = upper(parm_column); 
  
    coltype  varchar2(30); 
    dscale number; 
    endparm number;  
    startparm number;
    tabparm  number;
    temp varchar2(250); 
    temp2 varchar2(250); 
    parmcnt number; 
    outline varchar2(4000);
    parm_name varchar2(50);
    parm_cnt number := 0; 
    parm1  varchar2(50);
    parm2  varchar2(50);
    parm3  varchar2(50);
    apiname varchar2(50); 
    use_parm_no varchar2(1);
    tt pls_integer := 0 ;
    classname varchar2(30) := '&&classname' ; 
    keycnt pls_integer := 0;
    keystring varchar2(4000) := null;
    defstring varchar2(4000);
    defkey varchar2(60);
    cursor def_c is
    Select '     def ' || lower(column_name), upper(column_name) from all_tab_columns
           where table_name = upper(p_table) 
           And column_name Not in ( upper(p_table)|| '_ACTIVITY_DATE', upper(p_table)
             || '_USER_ID', upper(p_table)|| '_DATA_ORIGIN')
           order by column_id; 
begin 
 outline := '/** ***************************************************************************** ' || chr(10) ||
 '© 2011 SunGard Higher Education.  All Rights Reserved.     ' || chr(10) ||
 'CONFIDENTIAL BUSINESS INFORMATION     ' || chr(10) ||
 'THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION ' || chr(10) ||
 'AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,  ' || chr(10) ||
 'NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED ' || chr(10) ||
 'WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY     ' || chr(10) ||
 '****************************************************************************** */'   ;
 dbms_output.put_line(outline);
  outline :=
'package net.hedtech.banner.seeddata    ' || chr(10) ||    chr(10) ||
'import groovy.sql.Sql         ' || chr(10) ||
'import java.sql.Connection     ' || chr(10) ||
'import java.sql.CallableStatement  ' || chr(10) ||
'import java.text.*'  ;
dbms_output.put_line(outline); 
 outline :=
'/**          ' || chr(10) ||
' *  DML for ' || p_table  || chr(10) ||
'*/  ' || chr(10) ||
'public class ' || classname || ' {  '  ;
 dbms_output.put_line(outline);
Open  def_c ;
loop 
   Fetch def_c into defstring, defkey;
   Exit when def_c%notfound;
   keycnt := keycnt + 1;
   if keycnt < 5 then
       keystring := keystring || '${apiData.' || defkey || '.text()}    ';
   end if;
   dbms_output.put_line(defstring);
 End loop;
 Close def_c;


 outline :=
    '  def InputData  connectInfo ' || chr(10) ||
    '  Sql conn' || chr(10) ||
    '  Connection connectCall' || chr(10) ||
    '  def xmlData    ' || chr(10) ||
    '  java.sql.RowId tableRow  = null ' ;
 dbms_output.put_line(outline);
 outline :=
    '  public ' || classname || ' (InputData connectInfo, Sql conn, Connection connectCall, xmlData  )' || chr(10) ||
    ' {' || chr(10) || chr(10) ||  
    '    this.conn = conn' || chr(10) || 
    '    this.connectInfo =  connectInfo' || chr(10) || 
    '    this.connectCall = connectCall' ;
  dbms_output.put_line(outline);

  outline :=
    '    this.xmlData = xmlData  ' || chr(10) ||
    '    parseXmlData()   ' || chr(10) ||
    '    process' || initcap( p_table)|| '()'  || chr(10) ||
    '} ' ;
  dbms_output.put_line(outline);

  outline := 'def parseXmlData() {  ' || chr(10) ||
   '  def apiData = new XmlParser().parseText(xmlData) ' || chr(10) || chr(10) ||
   ' if (connectInfo.debugThis) {   ' || chr(10) ||
   '   println "--------- New XML ' || p_table || ' record ----------"   ' || chr(10) ||
   '   println "' || keystring || '"' || chr(10) ||
      ' }  ' ;
  dbms_output.put_line(outline);
  open  def_c ;
 loop
   Fetch def_c into defstring, defkey;
   Exit when def_c%notfound;
    defstring :=  ' this.' || lower(defkey) || ' = apiData.' || defkey || '.text()' ;
    dbms_output.put_line(defstring  );
 End loop;
 Close def_c;
 dbms_output.put_line( '}') ;
  --  parse the sql based on if this is for an update or create
  use_parm_no := upper('&&parmno');
  outline := ' def process' || initcap( p_table) || '() { ' ;
  dbms_output.put_line(outline) ;

  outline :=
      '   tableRow  = null ' || chr(10) ||
      '   //TODO modify this select to use key data' || chr(10) || 
      '   String rowSQL = """select rowid table_row from '  || p_table ||   chr(10) ||
      '           where ..<add conditions>.. """ '    || chr(10) ||
      '   try {'        || chr(10) ||
      '    conn.eachRow(rowSQL){ row ->'    || chr(10) ||
      '      tableRow  = row.table_row } ' || chr(10) ||
      '     }  ' ;
  dbms_output.put_line(outline);
  outline :=
      '   catch (Exception e) { '    || chr(10) ||
      '     if ( connectInfo.showErrors  ){ ' || chr(10) ||
      '       println "${rowSQL}" ' || chr(10) ||
      '       println "Problem selecting ' || p_table || ' rowid ' || classname || '.groovy: $e.message" ' || chr(10) ||
      '      }    ' || chr(10) ||
      '     }' ;
  dbms_output.put_line(outline);
  outline :=
      '   if ( !tableRow) { '  ;
  dbms_output.put_line(outline)   ;

  -- Find the api package 
  Open find_api_c;
  Fetch find_api_c into p_api; 
  Close find_api_c; 

  api_type := 'p_create' ;
  apiname := 'Insert';
  -- Get the number of parms  
   Open parms_c; 
  loop 
     Fetch parms_c into ptext; 
     Exit when parms_c%notfound; 
     
     endparm := 0; 
     startparm := 0; 
 
     Select InStr(ptext,'p_') into startparm from dual; 
     Select InStr(ptext,')') into endparm from dual; 
     if startparm > 0 then      
        parm_cnt := parm_cnt + 1; 
     End if;
     If endparm > 0 then 
        Exit ;
     End If; 
  End loop ; 
  --- Format the String assignment 
  cnt := 0 ;
  dbms_output.put_line(' //  parm count is ' || parm_cnt ); 
  outline := '   try { ' || chr(10) || '     String API = "{call  ' || lower(p_api) || '.' || api_type || '(' ;
  loop 
    exit when  cnt = parm_cnt;
    cnt := cnt + 1; 
    if cnt = 1 then 
       outline := outline || '?' ;
    else 
       outline := outline || ',?' ; 
    end if;  
  end loop; 
  outline := outline || ')}" ' || chr(10) ;  
  outline := outline || '     CallableStatement insertCall = this.connectCall.prepareCall(API)'; 
  dbms_output.put_line(outline);  
  Close parms_c; 
  
  -- Format And Print the parm assignments 
  parm_cnt := 0 ; 
  outline := ''; 
  Open parms_c; 
  loop 
     Fetch parms_c into ptext; 
     Exit when parms_c%notfound; 
     
   
     endparm := 0; 
     startparm := 0; 
     tabparm := 0; 
     Select InStr(ptext,'p_') into startparm from dual; 
     if startparm > 0 then 
     
          parm_cnt := parm_cnt + 1; 
          Select InStr(ptext,' ',startparm) into endparm from dual;
          Select substr(ptext,startparm,endparm) into temp from dual;
          tt := 0 ;
          while tt<length(temp) loop
             tt := tt + 1;
           if substr(temp,tt,1) = ' '
              then
                 tabparm := tt ;
                 exit;
              end if;

          end loop;
          temp := substr(temp,1,tt);
          Select p_table || '_' || substr(temp,3,length(temp) - 2) into temp2 from dual;
          parm_column := temp2;
        
          Select replace(temp2,' ',''), lower(replace(temp,' ','')) into parm_column, parm_name from dual;
          Open columntype ;
          Fetch columntype into coltype, dscale ; 
          Close columntype; 
          dbms_output.put_line('      // parm ' || parm_cnt || ' ' || temp || ' ' || lower(parm_column) || ' ' || coltype);      
          parm_column := 'this.' || lower(parm_column) ; 
          If parm_cnt = 1 Then 
                parm1 := parm_column;
          elsif parm_cnt = 2 Then 
                parm2 := parm_column; 
          elsif parm_cnt = 3 Then 
                parm3 := parm_column;
          End If; 
       
         --- Format the parameter using the number Or the parm Name   
         

          If parm_name Like '%rowid%' then 
             If  upper('&&pcreate') = 'Y' then 
                outline := '       insertCall.registerOutParameter(' || parm_cnt || ',java.sql.Types.ROWID)' ; 
             Else 
                 outline := '      insertCall.setROWID(' || parm_cnt || ',tableRow)' ;
             End If; 
          elsIf parm_name  = 'p_data_origin' then 
              
                 outline := '      insertCall.setString(' || parm_cnt || ',"GRAILS")' ;
          elsIf parm_name  = 'p_user_id' then 
              
                 outline := '      insertCall.setString(' || parm_cnt || ',"GRAILS")' ;     
          elsIf coltype = 'DATE' then 
            outline := '      if (( ' || parm_column || ' == "") || (' || parm_column ||'== null) || ' || chr(10) ||
                       '            (!' || parm_column || ')) {' || chr(10) ||
                       '            insertCall.setNull(' || parm_cnt || ',java.sql.Types.DATE)  ' || chr(10) ||
                       '      } ' || chr(10) ||
                       '      else {' || chr(10) || 
                       '        def ddate = new ColumnDateValue(' || parm_column || ')' || chr(10) ||
                       '        String unfDate = ddate.formatJavaDate() ' || chr(10) || 
                       '        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");   ' || chr(10) ||
                       '        java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime()); ' || chr(10) || 
                       '        insertCall.setDate(' || parm_cnt || ', sqlDate) ' || chr(10) ||
                       '      } ' || chr(10) ;
          
          
          Elsif coltype in ('VARCHAR2','CLOB' ) then 
              outline := '      insertCall.setString(' || parm_cnt || ',' || parm_column || ') ' || chr(10) ; 
         
          elsif coltype = 'NUMBER' and dscale = 0 then 
            outline := '      if (( ' || parm_column || ' == "") || (' || parm_column ||'== null) || (!' || parm_column || '))' || chr(10) ||
                       '          { insertCall.setNull(' || parm_cnt || ',java.sql.Types.INTEGER) } ' || chr(10) || 
                       '      else {' || chr(10) ||  
                       '           insertCall.setInt(' || parm_cnt || ',' || parm_column || '.toInteger()) } ' || chr(10) ; 
           
          elsif coltype = 'NUMBER' and dscale > 0 then 
             outline := '      if (( ' || parm_column || ' == "") || (' || parm_column ||'== null) || (!' || parm_column || ')){' || chr(10) ||
                       '            insertCall.setNull(' || parm_cnt || ',java.sql.Types.DOUBLE)   ' || chr(10) ||
                       '       } ' || chr(10)   ||
                       '      else {' || chr(10) ||  
                       '           insertCall.setDouble(' || parm_cnt || ',' || parm_column || '.toDouble()) ' || chr(10) ||
                       '      } ' || chr(10) ;


          End If;  
         
    dbms_output.put_line(outline); 
    
    end if; -- parm exist on the line  
                  
    select instr(ptext,')') into endparm from dual;
    If endparm > 0 then 
        Exit ;
     End If; 
  End loop ;  
  Close parms_c; 
  
  outline :=      '     if (connectInfo.debugThis ) {' || chr(10) ||
                 '             println "'|| apiname || ' ' || p_table || ' ${' || parm1 || '} ${' || parm2 ||'} ${' || parm3 ||'}"'
                 || chr(10) || ' }' ;
  dbms_output.put_line(outline);  

  outline :=      '    try {  ' || chr(10) ||
                  '        insertCall.executeUpdate() ' || chr(10) ||
                  '        connectInfo.tableUpdate("' || p_table || '",0,1,0,0,0)  ' || chr(10) ||
                  '       }  ' || chr(10) ||
                  '    catch (Exception e) { ' || chr(10) ||
                  '         connectInfo.tableUpdate("' || p_table || '",0,0,0,1,0) ' || chr(10) ||
                  '         if ( connectInfo.showErrors  ){' || chr(10) ||
                  '          println "'|| apiname || ' ' || p_table || ' ${' || parm1 || '} ${' || parm2 ||'} ${' || parm3 ||'}"' || chr(10) ||
                  '           println "Problem executing insert for table ' || p_table || ' from ' || classname || '.groovy: $e.message" ' || chr(10) ||
                  '         } ' || chr(10) ||
                  '       } ' || chr(10) ||
                  '     finally { ' || chr(10) ||
                  '        insertCall.close() ' || chr(10) ||
                  '       }   ' || chr(10) ||
                  '     }   ' || chr(10) ||
                  '  catch (Exception e) { ' || chr(10) ||
                  '        connectInfo.tableUpdate("' || p_table || '",0,0,0,1,0) ' || chr(10) ||
                  '        if ( connectInfo.showErrors  ){' || chr(10) ||
                  '           println "'|| apiname || ' ' || p_table || ' ${' || parm1 || '} ${' || parm2 ||'} ${' || parm3 ||'}"' || chr(10) ||
                  '           println "Problem setting up insert for table ' || p_table || ' from ' || classname || '.groovy: $e.message" ' || chr(10) ||
                  '         } ' || chr(10) ||
                  '      } '  || chr(10) || 
                  '  } else {  // update the data ' ;
    dbms_output.put_line(outline);
    ---- now set up the update statement

  api_type := 'p_update' ;
  apiname := 'Update';
  -- Get the number of parms
  parm_cnt := 0 ; 
   Open parms_c;
  loop
     Fetch parms_c into ptext;
     Exit when parms_c%notfound;

     endparm := 0;
     startparm := 0;

     Select InStr(ptext,'p_') into startparm from dual;
     Select InStr(ptext,')') into endparm from dual;
     if startparm > 0 then
        parm_cnt := parm_cnt + 1;
     End if;
     If endparm > 0 then
        Exit ;
     End If;
  End loop ;
  --- Format the String assignment
  cnt := 0 ;
  dbms_output.put_line(' //  parm count is ' || parm_cnt );
  outline := '   try { ' || chr(10) || '     String API = "{call  ' || lower(p_api) || '.' || api_type || '(' ;
  loop
    exit when  cnt = parm_cnt;
    cnt := cnt + 1;
    if cnt = 1 then
       outline := outline || '?' ;
    else
       outline := outline || ',?' ;
    end if;
  end loop;
  outline := outline || ')}" ' || chr(10) ;
  outline := outline || '     CallableStatement insertCall = this.connectCall.prepareCall(API)';
  dbms_output.put_line(outline);
  Close parms_c;

  -- Format And Print the parm assignments
  parm_cnt := 0 ;
  outline := '';
  Open parms_c;
  loop
     Fetch parms_c into ptext;
     Exit when parms_c%notfound;


     endparm := 0;
     startparm := 0;
     tabparm := 0;
     Select InStr(ptext,'p_') into startparm from dual;
     if startparm > 0 then

          parm_cnt := parm_cnt + 1;
          Select InStr(ptext,' ',startparm) into endparm from dual;
          Select substr(ptext,startparm,endparm) into temp from dual;
          tt := 0 ;
          while tt<length(temp) loop
             tt := tt + 1;
           if substr(temp,tt,1) = ' '
              then
                 tabparm := tt ;
                 exit;
              end if;

          end loop;
          temp := substr(temp,1,tt);
          Select p_table || '_' || substr(temp,3,length(temp) - 2) into temp2 from dual;
          parm_column := temp2;

          Select replace(temp2,' ',''), lower(replace(temp,' ','')) into parm_column, parm_name from dual;
          Open columntype ;
          Fetch columntype into coltype, dscale ;
          Close columntype;
          dbms_output.put_line('      // parm ' || parm_cnt || ' ' || temp || ' ' || lower(parm_column) || ' ' || coltype);
          parm_column := 'this.' || lower(parm_column) ;
          If parm_cnt = 1 Then
                parm1 := parm_column;
          elsif parm_cnt = 2 Then
                parm2 := parm_column;
          elsif parm_cnt = 3 Then
                parm3 := parm_column;
          End If;

         --- Format the parameter using the number Or the parm Name

         If  nvl(upper(use_parm_no), 'N') != 'Y'    then
          If parm_name Like '%rowid%' then

                 outline := '      insertCall.setROWID(' || parm_cnt || ',tableRow)' ;

          elsIf parm_name  = 'p_data_origin' then

                 outline := '      insertCall.setString(' || parm_cnt || ',"GRAILS")' ;
          elsIf parm_name  = 'p_user_id' then

                 outline := '      insertCall.setString(' || parm_cnt || ',"GRAILS")' ;
          elsIf coltype = 'DATE' then
             outline := '      if (( ' || parm_column || ' == "") || (' || parm_column ||'== null) || (!' || parm_column || '))' || chr(10) ||
                       '          { insertCall.setNull(' || parm_cnt || ',java.sql.Types.DATE) } ' || chr(10) ||
                       '      else {' || chr(10) ||
                       '        def ddate = new ColumnDateValue(' || parm_column || ')' || chr(10) ||
                       '        String unfDate = ddate.formatJavaDate() ' || chr(10) ||
                       '        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");   ' || chr(10) ||
                       '        java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime()); ' || chr(10) ||
                       '        insertCall.setDate(' || parm_cnt || ', sqlDate) } ' || chr(10) ;


          Elsif coltype in ('VARCHAR2','CLOB' ) then
              outline := '      insertCall.setString(' || parm_cnt || ',' || parm_column || ') ' || chr(10) ;

          elsif coltype = 'NUMBER' and dscale = 0 then
            outline := '      if (( ' || parm_column || ' == "") || (' || parm_column ||'== null) || (!' || parm_column || '))' || chr(10) ||
                       '          { insertCall.setNull(' || parm_cnt || ',java.sql.Types.INTEGER) } ' || chr(10) ||
                       '      else {' || chr(10) ||
                       '           insertCall.setInt(' || parm_cnt || ',' || parm_column || '.toInteger()) } ' || chr(10) ;

          elsif coltype = 'NUMBER' and dscale > 0 then
             outline := '      if (( ' || parm_column || ' == "") || (' || parm_column ||'== null) || (!' || parm_column || '))' || chr(10) ||
                       '          { insertCall.setNull(' || parm_cnt || ',java.sql.Types.DOUBLE) } ' || chr(10) ||
                       '      else {' || chr(10) ||
                       '           insertCall.setDouble(' || parm_cnt || ',' || parm_column || '.toDouble()) } ' || chr(10) ;
          End If;
         -- use the parm Name
         Else
          If parm_name Like '%rowid%' then
             If  upper('&&pcreate') = 'Y' then
                outline := '       insertCall.registerOutParameter("' || parm_name|| '",java.sql.Types.ROWID)' ;
             Else
                 outline := '      insertCall.setROWID("' || parm_name|| '",recordRow)' ;
             End If;
          elsIf parm_name  = 'p_data_origin' then

                 outline := '      insertCall.setString("p_data_origin","GRAILS")' ;
          elsIf parm_name  = 'p_user_id' then

                 outline := '      insertCall.setString("p_user_id","GRAILS")' ;
          elsIf coltype = 'DATE' then
             outline := '      if (( ' || parm_column || ' == "") || (' || parm_column ||'== null) || (!' || parm_column || '))' || chr(10) ||
                       '          { insertCall.setNull("' || parm_name|| '",java.sql.Types.DATE) } ' || chr(10) ||
                       '      else {' || chr(10) ||
                       '        def ddate = new ColumnDateValue(' || parm_column || ')' || chr(10) ||
                       '        String unfDate = ddate.formatJavaDate() ' || chr(10) ||
                       '        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");   ' || chr(10) ||
                       '        java.sql.Date sqlDate = new java.sql.Date(formatter.parse(unfDate).getTime()); ' || chr(10) ||
                       '        insertCall.setDate("' || parm_name || '", sqlDate) } ' || chr(10) ;


           Elsif coltype in ('VARCHAR2','CLOB' ) then
              outline := '      insertCall.setString("' || parm_name || '",' || parm_column || ') ' || chr(10) ;

           elsif coltype = 'NUMBER' and dscale = 0 then
            outline := '      if (( ' || parm_column || ' == "") || (' || parm_column ||'== null) || (!' || parm_column || '))' || chr(10) ||
                       '          { insertCall.setNull("' || parm_name|| '",java.sql.Types.INTEGER) } ' || chr(10) ||
                       '      else {' || chr(10) ||
                       '           insertCall.setInt("' || parm_name|| '",' || parm_column || '.toInteger()) } ' || chr(10) ;

           elsif coltype = 'NUMBER' and dscale > 0 then
             outline := '      if (( ' || parm_column || ' == "") || (' || parm_column ||'== null) || (!' || parm_column || '))' || chr(10) ||
                       '          { insertCall.setNull("' || parm_name|| '",java.sql.Types.DOUBLE) } ' || chr(10) ||
                       '      else {' || chr(10) ||
                       '           insertCall.setDouble("' || parm_name|| '",' || parm_column || '.toDouble()) } ' || chr(10) ;
           End If;
          End If;

    dbms_output.put_line(outline);

    end if; -- parm exist on the line

    select instr(ptext,')') into endparm from dual;
    If endparm > 0 then
        Exit ;
     End If;
  End loop ;
  Close parms_c;

  outline :=      '     if (connectInfo.debugThis ) {' || chr(10) ||
                 '             println "'|| apiname || ' ' || p_table || ' ${' || parm1 || '} ${' || parm2 ||'} ${' || parm3 ||'}"'
                 || chr(10) || ' }' ;
  dbms_output.put_line(outline);

    outline :=    '    try {  ' || chr(10) ||
                  '        insertCall.executeUpdate() ' || chr(10) ||
                  '        connectInfo.tableUpdate("' || p_table || '",0,0,1,0,0)  ' || chr(10) ||
                  '       }  ' || chr(10) ||
                  '    catch (Exception e) { ' || chr(10) ||
                  '         connectInfo.tableUpdate("' || p_table || '",0,0,0,1,0) ' || chr(10) ||
                  '         if ( connectInfo.showErrors  ){' || chr(10) ||
                  '          println "'|| apiname || ' ' || p_table || ' ${' || parm1 || '} ${' || parm2 ||'} ${' || parm3 ||'}"' || chr(10) ||
                  '           println "Problem executing update for table ' || p_table || ' from ' || classname || '.groovy: $e.message" ' || chr(10) ||
                  '         } ' || chr(10) ||
                  '       } ' || chr(10) ||
                  '     finally { ' || chr(10) ||
                  '        insertCall.close() ' || chr(10) ||
                  '       }   ' || chr(10) ||
                  '     }   ' || chr(10) ||
                  '  catch (Exception e) { ' || chr(10) ||
                  '        connectInfo.tableUpdate("' || p_table || '",0,0,0,1,0) ' || chr(10) ||
                  '        if ( connectInfo.showErrors  ){' || chr(10) ||
                  '           println "'|| apiname || ' ' || p_table || ' ${' || parm1 || '} ${' || parm2 ||'} ${' || parm3 ||'}"' || chr(10) ||
                  '           println "Problem setting up update for table ' || p_table || ' from ' || classname || '.groovy: $e.message" ' || chr(10) ||
                  '         } ' || chr(10) ||
                  '      } '  || chr(10) ||
                  '  } ' ;

  dbms_output.put_line(outline);
   outline :=   '  } '    ;
  dbms_output.put_line(outline)   ;
   outline :=   '  } '    ;
  dbms_output.put_line(outline)   ;
 
End;
/ 
  
spool off; 
