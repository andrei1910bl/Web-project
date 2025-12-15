<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello World!" %>
</h1>
<br/>
<form action="controller">

    <input type="hidden" name="command" value="login"/>
    Loggin: <input type ="text" name="login" value=""/>
    <br/>
    Password: <input type ="text" name="pass" value=""/>
    <br/>
    <input type="submit" name="sub" value="Push"/>
    <br/>
    ${login_msg}
</form>

</body>
</html>