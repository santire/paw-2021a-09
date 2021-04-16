
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form method="post">
    <div>
        <label>
            <spring.message code="hello.register.userForm.email"></spring.message>
            <input name="email" placeholder="email"/>
        </label>

    </div>
    <div>
        <label>
            <spring.message code="hello.register.userForm.password"></spring.message>
            <input name="password" placeholder="password"/>
        </label>
    </div>
    <div>
            <label>
                <spring.message code="hello.register.userForm.password"></spring.message>
                <input name="rememberme" type="checkbox"/>
                <spring.message code="hello.login.rememberme"></spring.message>
            </label>
    </div>
    <div>
        <input type="submit" value="submit"/>
    </div>

</form>

</body>
</html>
