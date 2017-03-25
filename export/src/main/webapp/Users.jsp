<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 19.03.2017
  Time: 13:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Users</title>
</head>
<body>
<table border="1" cellpadding="8" cellspacing="0">
    <tr>
        <th>User name</th>
        <th>Email</th>
        <th>Flag</th>
    </tr>
    <c:forEach items="${users}" var="user">
        <tr>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${user.flag}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
