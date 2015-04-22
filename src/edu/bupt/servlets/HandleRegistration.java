package edu.bupt.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import edu.bupt.util.MySql;

public class HandleRegistration extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public HandleRegistration() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
		
//		System.out.println("This is a get request.");
//		System.out.println(request.toString());
//		String userName  = request.getParameter("username");
//		String password = request.getParameter("password");
//		String email = request.getParameter("email");
//		String gender = request.getParameter("gender");
//		
//		System.out.println("new changes");
//		System.out.println("username:"+userName+
//				"\npassword:"+password+
//				"\nemail+:"+email+
//				"\ngender:"+gender);
//		
//		response.setContentType("text/html");
//		PrintWriter out = response.getWriter();
//		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
//		out.println("<HTML>");
//		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
//		out.println("  <BODY>");
//		out.print("    This is ");
//		out.print(this.getClass());
//		out.println(", using the GET method");
//		out.println("  </BODY>");
//		out.println("</HTML>");
//		out.flush();
//		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		System.out.println("This is a post request.");
		System.out.println(request.toString());
		
		String userName  = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		
		System.out.println("new changes");
		System.out.println("username:"+userName+
				"\npassword:"+password+
				"\nemail+:"+email+
				"\ngender:"+gender);
		
		MySql.connectMysql();
		boolean f1 = MySql.isExist("u_name",userName);
		boolean f2 = MySql.isExist("u_email", email);
		System.out.println(f1+","+f2);
//		
//		if(!f1 && !f2){
//			MySql.addUser(userName, password, email, gender, null);
//		}
		
		MySql.ResponseStatus addRes = MySql.addUser(userName, password, email, gender, null);
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		JSONObject resJsonObject = new JSONObject();
		
//		if(f1){
//			resJsonObject.put(STATUS_KEY, MySql.MySqlEnum.USER_NAME_DUPLICATE.getValue());
//		}else if(f2){
//			resJsonObject.put(STATUS_KEY, MySql.MySqlEnum.EMAIL_DUPLICATE.getValue());
//		}
		
		resJsonObject.put(MySql.STATUS_KEY, addRes.getValue());
		out.print(resJsonObject);
		out.flush();
		out.close();
		
		MySql.endConnection();
		
		
		
//		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
//		out.println("<HTML>");
//		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
//		out.println("  <BODY>");
//		out.print("    This is ");
//		out.print(this.getClass());
//		out.println(", using the POST method");
//		out.println("  </BODY>");
//		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
