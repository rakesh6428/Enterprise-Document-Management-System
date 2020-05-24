<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="dmsMaven.BucketList"%>

<%@page language="java" import="java.util.*"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html >
<html>
<head>
<meta charset="UTF-8">
<title>Home Page</title>
</head>
<body>
	<h1>Audit and Document Management System</h1>
	 
<div id="tabs" width:300px;margin:20px auto; position:relative; >  
    <ul id="navigation" margin:0 0 10px; padding:0;list-style:none;>  
        <li class="selected"><a href="fileupload.jsp" id="tab-0"  >File Upload</a></li>  
        <li><a href="filedownload.jsp" id="tab-1" >File Download</a></li>  
        <li><a href="approvalpage.jsp" id="tab-2" >Approval Page</a></li>  
       
    </ul>  
    <form action ="./SignUp" method="post">
    <input type="submit" name="newuser" value="NewUser" /> 
    </form>
</div>  
</body>
</html>