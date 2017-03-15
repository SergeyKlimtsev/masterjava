<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" omit-xml-declaration="yes" indent="no"/>
    <xsl:strip-space elements="*"/>
    <xsl:template match="/">
        <html>
            <body>
                <h1>Users</h1>
                <table border="1">
                    <tr>
                        <th>Name</th>
                    </tr>
                    <xsl:for-each select="*[name()='Payload']/*[name()='Users']/*[name()='User']">
                        <tr>
                            <td><xsl:value-of select="."/></td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
    <xsl:template match="text()"/>
</xsl:stylesheet>