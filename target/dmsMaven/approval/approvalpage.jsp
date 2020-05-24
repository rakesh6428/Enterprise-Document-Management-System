<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="dmsMaven.BucketList"%>

<%@page language="java" import="java.util.*"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>



	<h1>Displaying Approval List</h1>


	<form action="<%=request.getContextPath()%>/DocumentApproval"
		method="get" enctype="multipart/form-data">
		<h4>Get all file names:</h4>
		<input type="submit" value="Download File" />
	</form>
	<table border="1" width="500" align="center">
		<tr bgcolor="00FF7F">
				<th><b>Item name</b></th>
				<th><b>Size</b></th>

			</tr>
		<c:forEach items="${approvalList}" var="approval">
			

			 <tr>
				<td>${approval.key}</td>
				<td>${approval.size}</td>
				</tr>
				<!-- <td><c:out value="${approval.key}" /></td>
      <td><c:out value="${approval.size}" /></td>  -->

		</c:forEach>
	</table>
	


	

	<hr />

</body>
</html>