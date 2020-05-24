<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>New User Addition</h1>
<form action="<%=request.getContextPath()%>/SignUp" method="post">
			<table style="with: 50%">
				<tr>
					<td>First Name</td>
					<td><input type="text" name="first_name" /></td>
				</tr>
				<tr>
					<td>Last Name</td>
					<td><input type="text" name="last_name" /></td>
				</tr>
				<tr>
					<td>UserName</td>
					<td><input type="text" name="username" /></td>
				</tr>
				<tr>
				    <td>Password</td>
				    <td><input type="password" name="password"/></td>	    
			    </tr>
				<tr>
					<td>Email Address:</td>
					<td><input type="email" name="email" /></td>
				</tr>
				<tr>
					<td>Phone No</td>
					<td><input type="text" name="contact" /></td>
				</tr>
				<tr>
					<td>Sign up as</td>
					<td> <select name="roles">
                        <option value="Regular">regular user</option>
                        <option value="Evaluator">Evaluator</option>
                       </select></td>
				</tr></table>
			<input type="submit" value="Submit" /></form>
			<div id="message">
              <h3>Password must contain the following:</h3>
                <ul>
                <li>Numbers</li>
                <li>special character (e.g. ~, !, @, #)</li>
                <li>Upper case Letters</li>
                <li>Lower case Letters</li>
                </ul>  
              </div>
</body>
</html>