<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h1>Response Compare Tester</h1>
<hr/>
<form action="RCServlet" method="post">
	<input type="radio" name="method" value="post" checked>POST
	<input type="radio" name="method" value="get">GET
	<p>
	<b>Server</b><p>
	<input name="server-port" type="text" size="80"/>
	<input type="submit" value="Submit" />
	<p>
	<b>Headers</b><p>
	<textarea name="headers" rows="10" cols="80"></textarea><p>
	<b>Body</b><p>
	<textarea name="body" rows="40" cols="160"></textarea><p>
</form>
</body>
</html>