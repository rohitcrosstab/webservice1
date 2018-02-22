<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<a href="rest/hello">Click Here</a> <hr>
<a href="rest/test">Click Here</a> <hr>
<a href="rest/hello/crosstab">Click Here</a>  <hr>
<a href="rest/hello/2014/12/05">Click Here</a>  
<form action="rest/hello/add" method="post">  
Enter Id:<input type="text" name="id"/><br/><br/>  
Enter Name:<input type="text" name="name"/><br/><br/>  
Enter Price:<input type="text" name="price"/><br/><br/>  
<input type="submit" value="Add Product"/>  <hr>
<a href="rest/files/txt">Download Text File</a>  
</form>  
</body>
</html>