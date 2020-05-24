//SJSU CS 218 Fall 2019 TEAM7

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminAddUserToGroupRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;

public class CognitoUtil {
	final String COGN_REGION ="region";
	final String COGN_POOLID="userPoolId";
	final String COGN_CLIENTID="clientId";
	
	public CognitoUtil() {}
	
	public String getProperty(String prop) {
		String output= "";
		try {
		   InputStream is = SignUp.class.getResourceAsStream("/cognito.properties");
		   Properties cognitoConfig=new Properties();
		   cognitoConfig.load(is);
		   output= cognitoConfig.getProperty(prop).trim();
		}
		catch (IOException io){
			io.printStackTrace();
		}
		return output;
	}
	
	public String getCogUserPoolId() {
		return getProperty(COGN_POOLID);
	}
	
	public String getCogRegion() {
		return getProperty(COGN_REGION);
	}
	
	public String getClientId() {
		return getProperty(COGN_CLIENTID);
	}
	
	public void addUserToGroup(AWSCognitoIdentityProvider cognitoClient,String username, String groupname){	 
		   AdminAddUserToGroupRequest addUserToGroupRequest = new AdminAddUserToGroupRequest()
		              .withGroupName(groupname)
		              .withUserPoolId(getProperty(COGN_POOLID))
		              .withUsername(username); 
		   cognitoClient.adminAddUserToGroup(addUserToGroupRequest);		 
		   cognitoClient.shutdown();	 	                             
	}
	
	public AWSCognitoIdentityProvider getAmazonCognitoIdentityClient() {
		ClasspathPropertiesFileCredentialsProvider propertiesFileCredentialsProvider = 
	       new ClasspathPropertiesFileCredentialsProvider();
	 
	       return AWSCognitoIdentityProviderClientBuilder.standard()
	                      .withCredentials(propertiesFileCredentialsProvider)
	                             .withRegion(getProperty(COGN_REGION))
	                             .build();
	   }
	
}
