<%--
  Created by IntelliJ IDEA.
  User: Josh
  Date: 2017-01-08
  Time: 10:40 PM
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>CSV Upload</title>
    <script src="./js/csv.js"></script>
</head>
<body>
    <form action="csv_upload" method="post">
        <input type="file" name="csv" size="45">
        <input id="submit_button" type="submit" value="Confirm" accept=".csv">
        <br>
        <input list="companies_list" name="companies">
        <datalist id="companies_list">
            for(i: company_list.length){
                <option></option>
            }
        </datalist>
    </form>
    <p id="error"></p>
</body>
</html>
