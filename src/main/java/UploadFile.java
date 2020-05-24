//SJSU CS 218 Fall 2019 TEAM7
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.*;

import java.nio.file.InvalidPathException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManagerConfiguration;

/**
 * Servlet implementation class UploadFile
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, // 10 MB
		maxFileSize = 1024 * 1024 * 50, // 50 MB
		maxRequestSize = 1024 * 1024 * 100)
public class UploadFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String UPLOAD_DIR = "General/";
	private static String bucketName = "cs218fa19dmsbucket";
	AmazonS3 s3Client = new AmazonS3Client();
	SignIn userInfo = new SignIn();
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadFile() {
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
		String keyName = request.getParameter("filename");
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
			
			//----------Latest added code! -------------------------------------------\
			//	if(keyName!= null)
			//----------LAtest added code! -------------------------------------------\		
			{
				S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, keyName));
				if (!keyName.contains("Unapproved")) {
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
//					response.getOutputStream().println(
//							"<p>Upload another document <a href=\"http://amsdmsaws-env.ewpqphptwq.us-east-1.elasticbeanstalk.com/filedownload.jsp\">here</a>.</p>");
					response.getOutputStream().println(
							"<p>Upload another document <a href=\"filedownload.jsp\">here</a>.</p>");
				} else {
					/***** Set Response Content Type *****/
					response.setContentType("text/html");

					/***** Print The Response *****/
					response.getWriter()
							.println("<h3>You are not authorised to download this file  " + keyName + " !</h3>");
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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String user = (String)session.getAttribute("username");
		System.out.println(user);
		userInfo.navigateToHome(request, response,user);
		String uploadFilePath = UPLOAD_DIR;
		File fileSaveDir = new File(uploadFilePath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdirs();
		}
		String fileName = null;
		try {
			Part filePart = request.getPart("file");
			String contentType = filePart.getContentType();

			if (contentType.startsWith("image/")) {
				uploadFilePath = "Artwork/";
			} else if (contentType.startsWith("application/pdf") || contentType.startsWith("application/msword")
					|| contentType.startsWith("application/vnd")) {
				uploadFilePath = "UnapprovedDocs/";
			} else {
				System.out.println("Invalid file format");
				return;
			}

			fileName = filePart.getSubmittedFileName();
			long fileSize = filePart.getSize();
			try {

				File uploadFileName1 = new File(UPLOAD_DIR + File.separator + fileName);
				String uploadFileName = uploadFilePath + fileName;
				boolean filenameExists = s3Client.doesObjectExist(bucketName, uploadFileName);
				if (filenameExists) {
					String extension = uploadFileName.substring(uploadFileName.lastIndexOf("."));
					uploadFileName = uploadFileName.substring(0, uploadFileName.lastIndexOf("."));
					String currentTimeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
					uploadFileName = uploadFileName + currentTimeStamp + extension;

				}
				InputStream fileInputStream = filePart.getInputStream();

				System.out.println("The  uploadFilePath+fileName is " + uploadFileName);

				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentLength(filePart.getSize());
				System.out.println("The  metadata is " + metadata);

				System.out.println("Uploading file to s3");
				s3Client.putObject(bucketName, uploadFileName, fileInputStream, metadata);
			
				EmailService email = new EmailService();
				try {
					email.sendEmail();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				response.getOutputStream().println("<p>Thanks for uploading:" + fileName + "</p>");
//				response.getOutputStream().println(
//						"<p>Upload another document <a href=\"http://amsdmsaws-env.ewpqphptwq.us-east-1.elasticbeanstalk.com/fileupload.jsp\">here</a>.</p>");
				response.getOutputStream().println(
						"<p>Upload another document <a href=\"fileupload.jsp\">here</a>.</p>");

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

			// doGet(request, response);
		} catch (InvalidPathException ipe) {
			System.out.println("Error in path:" + ipe.getMessage());

		}
	}

}
