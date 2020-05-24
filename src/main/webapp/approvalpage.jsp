<!-- //SJSU CS 218 Fall 2019 TEAM7 -->

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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Approvals</title>
</head>
<body>



	<h1>Displaying Approval List</h1>


	<form action="<%=request.getContextPath()%>/DocumentApproval"
		method="get" enctype="multipart/form-data">
		<span>Get all file names:</span> <span><input type="submit"
			value="Documents for Approval" /></span>
			
	</form>
	<table border="1" width="500" align="center">
		<tr bgcolor="00FF7F">
			<th><b>Item name</b></th>
			<th><b>Size</b></th>
			<th><b>Approve</b></th>

		</tr>
		<c:forEach items="${approvalList}" var="approval">


			<tr>
				<td>${approval.key}</td>
				<td>${approval.size}</td>
				<td>
					<form action="./DocumentApproval" method="post">
						<input type="submit" name="approve" value="Approve" />
						<input type="submit" name="reject" value="Reject" />
						<input type="submit" name="download" value = "Download"/>
						<input type="hidden" name="key" value="<c:out value="${approval.key}"/>" > 
						
						
					</form>
				</td>


			</tr>
			<!-- <td><c:out value="${approval.key}" /></td>
      <td><c:out value="${approval.size}" /></td>  -->

		</c:forEach>
	</table>
<a href = "index.jsp">Home Page</a>
	



	<hr />

</body>
</html>