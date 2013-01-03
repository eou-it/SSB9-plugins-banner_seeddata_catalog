<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
  <body>
    <h2>General Person Data</h2>    
    <table border="1">
      <tr bgcolor="#9acd32">
           <th>ID</th>
           <th>PIDM</th>
           <th>Last_Name</th>
           <th>First_Name</th>
           <th>Middle</th>
           <th>SSN</th>
           <th>birthDate</th>
           <th>Legacy</th>
           <th>Ethn</th>
           <th>EthnCde</th> 
           <th>ConfirmCD</th>
           <th>ConfirmDT</th>
           <th>Mrtl</th>
           <th>Relg</th>
           <th>Sex</th>
           <th>PrefFirst</th>
           <th>Prefix</th>
           <th>Suffix</th>
           <th>Citz</th>
           <th>Atyp</th>
           <th>Street1</th>
           <th>Street2</th>
           <th>Street3</th>
           <th>city</th>
           <th>ST</th>
           <th>Zip</th>
           <th>Cnty</th>
           <th>Natn</th>
           <th>Status</th>
           <th>Asrc</th>
           <th>Tele</th>
           <th>Area</th>
           <th>Number</th>
           <th>Ext</th>
           <th>Tele_Status</th>
           <th>AddrSeq</th>
           <th>Primary</th>
           <th>Intl</th>
           <th>Email</th>
           <th>EmailAddr</th>
           <th>EmailSt</th>
           <th>comment</th>
           <th>Dead</th>       
       </tr>
      <xsl:for-each select="genPerson/data">
       <tr>
           <td><xsl:value-of select="ID"/></td>
           <td><xsl:value-of select="PIDM"/></td>
           <td><xsl:value-of select="Last_Name"/></td>
           <td><xsl:value-of select="First_Name"/></td>
           <td><xsl:value-of select="Middle"/></td>
           <td><xsl:value-of select="SSN"/></td>
           <td><xsl:value-of select="birthDate2"/></td>
           <td><xsl:value-of select="Legacy"/></td>
           <td><xsl:value-of select="Ethn"/></td>
           <td><xsl:value-of select="EthnCde"/></td>
           <td><xsl:value-of select="ConfirmCD"/></td>
           <td><xsl:value-of select="ConfirmDT"/></td>
           <td><xsl:value-of select="Mrtl"/></td>
           <td><xsl:value-of select="Relg"/></td>
           <td><xsl:value-of select="Sex"/></td>
           <td><xsl:value-of select="PrefFirst"/></td>
           <td><xsl:value-of select="Prefix"/></td>
           <td><xsl:value-of select="Suffix"/></td>
           <td><xsl:value-of select="Citz"/></td>
           <td><xsl:value-of select="Atyp"/></td>
           <td><xsl:value-of select="Street1"/></td>
           <td><xsl:value-of select="Street2"/></td>
           <td><xsl:value-of select="Street3"/></td>
           <td><xsl:value-of select="city"/></td>
           <td><xsl:value-of select="ST"/></td>
           <td><xsl:value-of select="Zip"/></td>
           <td><xsl:value-of select="Cnty"/></td>
           <td><xsl:value-of select="Natn"/></td>
           <td><xsl:value-of select="Status"/></td>
           <td><xsl:value-of select="Asrc"/></td>
           <td><xsl:value-of select="Tele"/></td>
           <td><xsl:value-of select="Area"/></td>
           <td><xsl:value-of select="Number"/></td>
           <td><xsl:value-of select="Ext"/></td>
           <td><xsl:value-of select="Tele_Status"/></td>
           <td><xsl:value-of select="AddrSeq"/></td>
           <td><xsl:value-of select="Primary"/></td>
           <td><xsl:value-of select="Intl"/></td>
           <td><xsl:value-of select="Email"/></td>
           <td><xsl:value-of select="EmailAddr"/></td>
           <td><xsl:value-of select="EmailSt"/></td>
           <td><xsl:value-of select="comment"/></td>
           <td><xsl:value-of select="Dead"/></td> 
       </tr>
      </xsl:for-each>
     </table>
  </body>
  </html>
</xsl:template>

</xsl:stylesheet>

