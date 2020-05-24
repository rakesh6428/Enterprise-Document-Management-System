//SJSU CS 218 Fall 2019 TEAM7

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {

    // Replace sender@example.com with your "From" address.
    // This address must be verified.
    static final String FROM = "rakesh.mvm@gmail.com";
    static final String FROMNAME = "AMS DMS Admin";
	
    // Replace recipient@example.com with a "To" address. If your account 
    // is still in the sandbox, this address must be verified.
    static final String TO = "sahasrabuddheyash@gmail.com";
    
    // Replace smtp_username with your Amazon SES SMTP user name.
    static final String SMTP_USERNAME = "AKIATRCQ5NXURYLNAQQU";
    
    // Replace smtp_password with your Amazon SES SMTP password.
    static final String SMTP_PASSWORD = "BDiZBvZF0qvtLWKbgVfwTwa9p8Dwy+i5QiMZiyHveHQh";
    
    // The name of the Configuration Set to use for this message.
    // If you comment out or remove this variable, you will also need to
    // comment out or remove the header below.
    //static final String CONFIGSET = "ConfigSet";
    
    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
    // See https://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html#region-endpoints
    // for more information.
    static final String HOST = "email-smtp.us-east-1.amazonaws.com";
    
    // The port you will connect to on the Amazon SES SMTP endpoint. 
    static final int PORT = 587;
    
    static final String SUBJECT = "Requesting for Approval";
    
    static final String BODY = String.join(
    	    System.getProperty("line.separator"),
    	    "<h1>Seeking Approval</h1>",
    	    "<p>A file has been uploaded and pending for your approval ", 
    	    
    	    "Please review it by logging into account",
    	    
    	    "http://amsdmsaws-env.ewpqphptwq.us-east-1.elasticbeanstalk.com/approvalpage.jsp"
    	    
    	    
    	);

    public  void sendEmail() throws Exception {

        // Create a Properties object to contain connection configuration information.
    	Properties props = System.getProperties();
    	props.put("mail.transport.protocol", "smtps");
    	props.put("mail.smtp.port", PORT); 
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtps.auth", "true");

        // Create a Session object to represent a mail session with the specified properties. 
    	Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information. 
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM,FROMNAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
        msg.setSubject(SUBJECT);
        msg.setContent(BODY,"text/html");
        
        // Add a configuration set header. Comment or delete the 
        // next line if you are not using a configuration set
       // msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);
            
        // Create a transport.
        Transport transport = session.getTransport();
                    
        // Send the message.
        try
        {
            System.out.println("Sending...");
            
            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
        	
            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
        }
        catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }
        finally
        {
            // Close and terminate the connection.
            transport.close();
        }
    }
}