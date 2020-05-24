//SJSU CS 218 Fall 2019 TEAM7

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/index.jsp")
/**
 * Servlet implementation class EmailServlet
 */
public class EmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmailServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		List<BucketList> approvalList = new ArrayList<BucketList>();
		approvalList.add(new BucketList("UnapprovedDocs/Doc1.docs", (long)105044));
		approvalList.add(new BucketList("UnapprovedDocs/Doc2.docs", (long)105044));
		approvalList.add(new BucketList("UnapprovedDocs/Doc3.docs", (long)105044));
		approvalList.add(new BucketList("UnapprovedDocs/Doc4.docs", (long)105044));
		request.setAttribute("approvalList", approvalList);

		request.getRequestDispatcher("/index.jsp").forward(request, response);
		
		//------------------ Remove this code after testing email --------------------------------
//		EmailService email = new EmailService();
//		try {
//			email.sendEmail();
//		} catch (Exception e) {
//			// TODO Auto-generated catch blocks
//			e.printStackTrace();
//		}
		
		//-----------------------Remove this code after testing email ------------------------
		
		
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String srcKey =request.getParameter("key");
		String destKey = srcKey.substring(srcKey.lastIndexOf("/")+1);
		//doGet(request, response);
	}

}
