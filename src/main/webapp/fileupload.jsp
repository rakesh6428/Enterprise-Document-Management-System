<!-- //SJSU CS 218 Fall 2019 TEAM7 -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Document Upload</title>
</head>
<body>
<center>
	<h2>File Upload:</h2>
	<h4>Select the type of file to be uploaded</h4>
	<select name="Type" size="1">
		<option>Artwork</option>
		<option>Document</option>
	</select>
	<br /> Select a file to upload:
	
	<form action="<%=request.getContextPath()%>/UploadFile" method="post"
		enctype="multipart/form-data">
		<input type="file" name="file" size="50" /> <br /> <input
			type="submit" value="Upload File" />
			<input type = "submit" name="homepage" value = "Home Page" />
	</form>
	
	</center>
</body>
</html>