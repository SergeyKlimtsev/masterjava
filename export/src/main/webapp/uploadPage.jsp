<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 18.03.2017
  Time: 20:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Upload</title>
</head>
<body>
    <form method="post" action="upload" enctype="multipart/form-data">
        Select file to upload:
        <input type="file" name="upload" />
        <br/><br/>
        <input type="submit" value="Upload" />
    </form>
</body>
</html>
