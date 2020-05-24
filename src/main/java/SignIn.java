//SJSU CS 218 Fall 2019 TEAM7

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeRequest;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.ChallengeNameType;
import com.amazonaws.services.cognitoidp.model.GetUserResult;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;

@SuppressWarnings("serial")
@WebServlet(name = "SignIn", urlPatterns = { "/SignIn" })
public class SignIn extends HttpServlet {

	final String USER_NAME = "username";
	final String PASSW = "password";
	final String COGN_POOLID = "userPoolId";
	final String COGN_CLIENTID = "clientId";
	 String loggedInUser;
	private Logger logger = LoggerFactory.getLogger(getClass());
	CognitoUtil cognUtil;
	HttpSession session;
	Cookie cookie;
	public SignIn() {
		super();
		cognUtil = new CognitoUtil();
		// TODO Auto-generated constructor stub
	}

//	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		PrintWriter out = response.getWriter();
//		response.setStatus(HttpServletResponse.SC_OK);
//		response.setContentType("text/plain");
//		response.sendRedirect("http://localhost:8080/dmsMaven/index.jsp");
//		//response.sendRedirect("http://amsdms-env.cvkp3hpbq4.us-east-1.elasticbeanstalk.com/index.jsp");
//	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String act = request.getParameter("newuser");
		if (act != null && act.equals("NewUser")) {
			//response.sendRedirect("http://localhost:8080/dmsMaven/newuser.jsp");
			//response.sendRedirect("http://amsdmsaws-env.ewpqphptwq.us-east-1.elasticbeanstalk.com/newuser.jsp");
			response.sendRedirect("newuser.jsp");
		} else {

			String usrnm = request.getParameter(USER_NAME);
			//loggedInUser = usrnm;
			String pwd = request.getParameter(PASSW);
			PrintWriter out = response.getWriter();
			try {
				if (StringUtils.isBlank(usrnm) || StringUtils.isBlank(pwd)) {
					logger.debug("Empty Fields");
					response.setStatus(HttpServletResponse.SC_OK);
					response.setContentType("text/plain");
					out.print("Email or Password is required to log in to your account !");
				}

				AWSCognitoIdentityProvider cognitoClient = cognUtil.getAmazonCognitoIdentityClient();
				Map<String, String> authParameters = new HashMap<String, String>();
				authParameters.put("USERNAME", usrnm);
				authParameters.put("PASSWORD", pwd);
				loggedInUser = authParameters.get("USERNAME");
				
				AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
						.withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH).withAuthParameters(authParameters)
						.withClientId(cognUtil.getProperty(COGN_CLIENTID))
						.withUserPoolId(cognUtil.getProperty(COGN_POOLID));
				AdminInitiateAuthResult authResponse = cognitoClient.adminInitiateAuth(authRequest);
			
				System.out.println(authResponse.getChallengeName());
				logger.debug("Logged In with username {}", usrnm);
				System.out.println(usrnm);
				session = request.getSession(true);
				session.setAttribute("username", usrnm);
				
				
				
				// response.accessToken.payload['cognito:groups']
				 Connection con = null;
				 Statement st = null;
				 String role = null;
				 ResultSet result = null;
					try {
						Class.forName("com.mysql.jdbc.Driver");  
						con=DriverManager.getConnection(  
						"jdbc:mysql://aa13qtthnrs6eww.camy8ilsaacv.us-east-1.rds.amazonaws.com:3306/ebdb","amsdmsaws","amsdmsaws");
					st=con.createStatement();	
				String select = "select role from ebdb.User where username = '" + usrnm + "'";
				result= st.executeQuery(select);
				 
					}catch(Exception e){ System.out.println(e);}
		    	try {
					if(result.next()!=false) {
					 role = result.getString("role");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
				//String userType = request.getParameter("Type");
				if(role!=null &&role.equals("Regular"))
				{
					response.setStatus(HttpServletResponse.SC_OK);
					
					
					response.setContentType("text/plain");
					//response.sendRedirect("http://localhost:8080/dmsMaven/indexregular.jsp");
					//response.sendRedirect("http://amsdmsaws-env.ewpqphptwq.us-east-1.elasticbeanstalk.com/indexregular.jsp");
					response.sendRedirect("indexregular.jsp");
					return;
				}
				else {
				response.setStatus(HttpServletResponse.SC_OK);
				
				
				response.setContentType("text/plain");
				//response.sendRedirect("http://localhost:8080/dmsMaven/index.jsp");
				//response.sendRedirect("http://amsdmsaws-env.ewpqphptwq.us-east-1.elasticbeanstalk.com/index.jsp");
				response.sendRedirect("index.jsp");
				return;
				}
			} catch (UserNotFoundException ex) {
				logger.debug("Failed to find user");
				response.setStatus(HttpServletResponse.SC_OK);
				response.setContentType("text/plain");
				out.print("User not found. Failed to log in. Please try again.");
			}

		}
	}
	public void navigateToHome(HttpServletRequest request, HttpServletResponse response,String user)throws ServletException,IOException{
		//SignIn userdetails = new SignIn();
		
		//GetUserResult userInfo = new GetUserResult();
		String username = user;
		//String username = (String)session.getAttribute("username");
		 Connection con = null;
		 Statement st = null;
		 String role = null;
		 ResultSet resultset = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");  
				con=DriverManager.getConnection(  
				"jdbc:mysql://aa13qtthnrs6eww.camy8ilsaacv.us-east-1.rds.amazonaws.com:3306/ebdb","amsdmsaws","amsdmsaws");
			st=con.createStatement();	
		String select = "select role from ebdb.User where username = '" + username + "'";
		resultset= st.executeQuery(select);
		 
			}catch(Exception e){ System.out.println(e);}
    	try {
			if(resultset.next()!=false) {
			 role = resultset.getString("role");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
		if (request.getParameter("homepage") != null) {
			if (role != null && role.equals("Regular")) {
				response.sendRedirect(request.getContextPath() + "/indexregular.jsp");
				 return;
			} else if(role!=null){
				response.sendRedirect(request.getContextPath() + "/index.jsp");
				return;
			}
			
		}
		}catch(Exception e)
		{
	response.getWriter().print("Session Invalid. Please login");
		}
		
	}
}
