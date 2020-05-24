<!-- //SJSU CS 218 Fall 2019 TEAM7 -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Document download</title>
</head>
<body>
<h1>Audit and Document Management System </h1>
<h3>Enter the filename to be downloaded</h3>
     
      <form action = "<%=request.getContextPath()%>/UploadFile" method = "get"
         enctype = "multipart/form-data">
         <input type="text" name="filename" value="">
        
         <br />
         <input type = "submit" value = "Download File" />
         <input type = "submit" name="homepage" value = "Home Page" />
      </form>
		
</body>
</html>