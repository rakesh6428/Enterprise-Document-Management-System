//SJSU CS 218 Fall 2019 TEAM7
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * Servlet implementation class DocumentApproval
 */

@WebServlet("/approvalpage.jsp")
public class DocumentApproval extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String UPLOAD_DIR = "General/";
	private static String bucketName = "cs218fa19dmsbucket";
	AmazonS3 s3Client = new AmazonS3Client();
 SignIn userInfo = new SignIn();
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DocumentApproval() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String user = (String)session.getAttribute("username");
		System.out.println(user);
		userInfo.navigateToHome(request, response,user);
		ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withMaxKeys(1000);
		ListObjectsV2Result result;
		
		List<BucketList> approvalList = new ArrayList<BucketList>();
		BucketList buckets = new BucketList();

		result = s3Client.listObjectsV2(req);
		for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
			if (objectSummary.getKey().startsWith("Unapproved")) {

				approvalList.add(new BucketList(objectSummary.getKey(), objectSummary.getSize()));

				System.out.printf(" - %s (size: %d)\n", objectSummary.getKey(), objectSummary.getSize());
				System.out.println(approvalList);
			}

		}
		request.setAttribute("approvalList", approvalList);

		request.getRequestDispatcher("/approvalpage.jsp").forward(request, response);

	}

	
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		String user = (String)session.getAttribute("username");
		System.out.println(user);
		userInfo.navigateToHome(request, response,user);
		
		String srcBucketName = bucketName;
		String srcKey = request.getParameter("key");
		
		String dstBucketName = bucketName;
		String destKey = srcKey.substring(srcKey.lastIndexOf("/") + 1);
		String dstKey = "ApprovedDocs" + File.separator + destKey;

		String download = request.getParameter("download");
		String approve = request.getParameter("approve");
		String reject = request.getParameter("reject");

		if (download != null && download.equals("Download")) {
			String keyName = destKey;
			ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withMaxKeys(1000);
			ListObjectsV2Result result;
			List<String> objectList = new ArrayList<String>();

			do {
				result = s3Client.listObjectsV2(req);
				for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
					objectList.add(objectSummary.getKey());
					System.out.printf(" - %s (size: %d)\n", objectSummary.getKey(), objectSummary.getSize());
				}
				for (String object : objectList) {
					if (object.contains(keyName)) {
						keyName = object.toString();
					}
				}
				String token = result.getNextContinuationToken();
				System.out.println("Next Continuation Token: " + token);
				req.setContinuationToken(token);
			} while (result.isTruncated());
			try {
				boolean filenameExists = s3Client.doesObjectExist(bucketName, keyName);
				if (filenameExists)

					
					{
						S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, keyName));
						
						{
							byte[] buffer = new byte[5 * 1024 * 1024];
							response.setContentType("application/octet-stream");
							response.setHeader("Content-Disposition", "attachment;filename=" + keyName);
							ServletOutputStream output = response.getOutputStream();
							InputStream input = object.getObjectContent();
							try {

								int bytesRead = -1;
								while ((bytesRead = input.read(buffer)) != -1) {

									output.write(buffer, 0, bytesRead);
								}

							} finally {

								if (input != null) {
									input.close();
								}

								output.flush();
								if (output != null) {
									output.close();
								}
							}
							response.getOutputStream().println("<p>Thanks for uploading:" + keyName + "</p>");
							response.getOutputStream().println(
									"<p>Upload another document <a href=\"filedownload.jsp\">here</a>.</p>");
 
							  response.sendRedirect(request.getContextPath()+"/login.jsp");
						
						
						}
					} else {

						/***** Set Response Content Type *****/
						response.setContentType("text/html");

						/***** Print The Response *****/
						response.getWriter().println("<h3>File " + keyName + " Is Not Present .....!</h3>");
					}

			} catch (AmazonServiceException e) {
				System.err.println(e.getErrorMessage());
				System.exit(1);
			} catch (FileNotFoundException e) {
				System.err.println(e.getMessage());
				System.exit(1);
			} catch (IOException e) {
				System.err.println(e.getMessage());
				System.exit(1);
			}
			System.out.println("Done!");

		} else if (approve != null && approve.equals("Approve")) {

			try {
				CopyObjectRequest copyObjectRequest = new CopyObjectRequest(srcBucketName, srcKey, dstBucketName,
						dstKey);
				CopyObjectResult result = s3Client.copyObject(copyObjectRequest);
				insertIntoApprovalTable(user,srcKey,approve);
				
				
				
				
				String eTag = result.getETag();
				System.out.println(eTag);
				response.getOutputStream().println("<p>Your document " + destKey + "is now approved</p>");

//				response.getOutputStream().println(
//						"<p>Approve remaining documents from <a href=\"http://amsdmsaws-env.ewpqphptwq.us-east-1.elasticbeanstalk.com/approvalpage.jsp\">here</a>.</p>");

				response.getOutputStream().println(
						"<p>Approve remaining documents from <a href=\"approvalpage.jsp\">here</a>.</p>");
				
				s3Client.deleteObject(bucketName, srcKey);
			} catch (AmazonServiceException server) {
				System.out.println("Error communicating with S3 network --> Server");
				System.out.println("Error Message:    " + server.getMessage());
				System.out.println("HTTP Status Code: " + server.getStatusCode());
				System.out.println("AWS Error Code:   " + server.getErrorCode());
				System.out.println("Error Type:       " + server.getErrorType());
				System.out.println("Request ID:       " + server.getRequestId());
			} catch (AmazonClientException client) {
				System.out.println("Error communicating with S3 network --> Client");
				System.out.println("Error Message: " + client.getMessage());
			}
		} else if (reject != null && reject.equals("Reject")) {
			try {
				s3Client.deleteObject(bucketName, srcKey);
				response.getOutputStream().println("<p>Your document " + destKey + "is now Rejected and deleted</p>");
				insertIntoApprovalTable(user,srcKey,reject);
//				response.getOutputStream().println(
//						"<p>Approve remaining documents from <a href=\"http://amsdmsaws-env.ewpqphptwq.us-east-1.elasticbeanstalk.com/approvalpage.jsp\">here</a>.</p>");
			
				response.getOutputStream().println(
						"<p>Approve remaining documents from <a href=\"approvalpage.jsp\">here</a>.</p>");
				
			} catch (AmazonServiceException server) {
				System.out.println("Error communicating with S3 network --> Server");
				System.out.println("Error Message:    " + server.getMessage());
				System.out.println("HTTP Status Code: " + server.getStatusCode());
				System.out.println("AWS Error Code:   " + server.getErrorCode());
				System.out.println("Error Type:       " + server.getErrorType());
				System.out.println("Request ID:       " + server.getRequestId());
			} catch (AmazonClientException client) {
				System.out.println("Error communicating with S3 network --> Client");
				System.out.println("Error Message: " + client.getMessage());
			}
		}

	}
	public void insertIntoApprovalTable(String user,String srcKey,String operation)
	{
		Connection con = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");  
			con=DriverManager.getConnection(  
			"jdbc:mysql://aa13qtthnrs6eww.camy8ilsaacv.us-east-1.rds.amazonaws.com:3306/ebdb","amsdmsaws","amsdmsaws");
			
		 
		String query = " insert into ApprovalHistory (username,filename,operation)"
                + " values (?, ?,?)";
        PreparedStatement preparedStmt = con.prepareStatement(query);
        preparedStmt.setString (1, user);
        preparedStmt.setString (2, srcKey);
        preparedStmt.setString (3, operation);
        
        preparedStmt.execute();
    	
    	System.out.println("Data written successfully!");
        con.close();
		}catch(Exception e){ System.out.println(e);}
	}

}
