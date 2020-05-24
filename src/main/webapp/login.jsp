<!-- //SJSU CS 218 Fall 2019 TEAM7 -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>User Login Page</title>
</head>
<body>
<form action="./SignIn" method="get">
		<table style="with: 50%">
 
			<tr>
				<td>UserName</td>	
				<td><input type="text" name="username" /></td>
			</tr>
				<tr>
				<td>Password</td>
				<td><input type="password" name="password" /></td>
			</tr>
			<select name="Type" size="1">
		<option>Regular</option>
		<option>Evaluator</option>
	</select>
		</table>
		<input type="submit" name="login" value="Login" />
		
		</form>
</body>
</html>