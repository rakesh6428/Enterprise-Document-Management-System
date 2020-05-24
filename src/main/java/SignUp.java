//SJSU CS 218 Fall 2019 TEAM7

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//import net.sf.kdgcommons.lang.StringUtil;
//import org.apache.log4j.*;

@SuppressWarnings("serial")
@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	final String EMAIL = "email";
	final String LAST_NAME ="last_name";
	final String FIRST_NAME="first_name";
	final String USER_NAME="username";
	final String PASSW="password";
	final String PHONE_NUM="contact";
	final String ROLE="roles";
	CognitoUtil cognUtil;
	//protected String cognitoPoolID;
	//protected String region;
	
	public SignUp() {
		super();
		cognUtil= new CognitoUtil();
	        // TODO Auto-generated constructor stub
	}
	
	
	
   @Override
	public void doPost (HttpServletRequest request, HttpServletResponse response)
	 throws ServletException, IOException {
	  String email_address = request.getParameter(EMAIL);
	  String fn = request.getParameter(FIRST_NAME);
	  String ln = request.getParameter(LAST_NAME);
	  String pwd= request.getParameter(PASSW);
	  String phone=request.getParameter(PHONE_NUM);
	  String usrnm = request.getParameter(USER_NAME);
	  String role = request.getParameter(ROLE);
	  //String cognitoPoolID=getProperty(COGN_POOLID);
	  PrintWriter out = response.getWriter();	
	  try {
		  //System.out.println(cognitoPoolID);
		  AWSCognitoIdentityProvider cognitoClient = cognUtil.getAmazonCognitoIdentityClient();
		  AdminCreateUserRequest cognitoRequest = new AdminCreateUserRequest()
				  .withUserPoolId(cognUtil.getCogUserPoolId())
				  .withUsername(usrnm)
				  .withUserAttributes(
					new AttributeType()
					    .withName("email")
					    .withValue(email_address),
					new AttributeType()
					    .withName("given_name")
					    .withValue(fn),
				    new AttributeType()
				        .withName("family_name")
				        .withValue(ln),
				    new AttributeType()
			            .withName("phone_number")
			            .withValue(phone),
			        new AttributeType()
			            .withName("email_verified")
			            .withValue("true"))
				    .withTemporaryPassword(pwd)
				    .withMessageAction("SUPPRESS")
				    .withDesiredDeliveryMediums(DeliveryMediumType.EMAIL)
				    .withForceAliasCreation(Boolean.FALSE);
		  cognitoClient.adminCreateUser(cognitoRequest);
		  cognUtil.addUserToGroup(cognitoClient, usrnm,role);
		  
		  
		  Connection con = null;
			
			try {
				Class.forName("com.mysql.jdbc.Driver");  
				con=DriverManager.getConnection(  
				"jdbc:mysql://aa13qtthnrs6eww.camy8ilsaacv.us-east-1.rds.amazonaws.com:3306/ebdb","amsdmsaws","amsdmsaws");
				
			 
			String query = " insert into User (role,username)"
	                + " values (?, ?)";
	        PreparedStatement preparedStmt = con.prepareStatement(query);
	        preparedStmt.setString (1, role);
	        preparedStmt.setString (2, usrnm);
	        
	        preparedStmt.execute();
	    	
	    	System.out.println("Data written successfully!");
	        con.close();
			}catch(Exception e){ System.out.println(e);}
		  
		  response.sendRedirect(request.getContextPath()+"/login.jsp");
		  //out.print("User created");
	  } catch(InvalidPasswordException ipex) {
		  logger.debug("Invalid password");
		  response.setStatus(HttpServletResponse.SC_OK);
	      response.setContentType("text/plain");
	      out.print("Password must be with at least length 8 containing the following: " 
	    		     +"\n"+ "-numbers"+ 
	    		       "\n"+ "-special character (e.g. ~, !, @, #)" +
	    		       "\n"+ "Uppercase Letters"+
	    		       "\n"+ "Lowercase Letters");
	  } 
	  catch (UsernameExistsException ex){
		  logger.debug("username exists");
		  response.setStatus(HttpServletResponse.SC_OK);
	      response.setContentType("text/plain");
	      out.print("username exists");
	  }
	 
	}
}