package edu.bupt.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import edu.bupt.util.SqlTool;

public class HandleNewInvitation extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public HandleNewInvitation() {
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

		String username = request.getParameter("name");
		String gender = request.getParameter("gender");
		String invitation = request.getParameter("invitation");
		long posttimeMillis = Long.parseLong(request.getParameter("posttime"));
		Timestamp posttime = new Timestamp(posttimeMillis);
		double lat = Double.parseDouble(request.getParameter("lat"));
		double lon = Double.parseDouble(request.getParameter("long"));
		
		System.out.println("username:"+username+",gender:"+gender+",invitation:"+invitation+
				",posttime:"+posttime+",lat:"+lat+",lon:"+lon);
		
		
		int flag = SqlTool.insertInvitation(username,gender,invitation,posttime,lat,lon);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Map<String, Integer> data = new HashMap<String, Integer>();
		data.put("result", flag);
		JSONObject res = new JSONObject(data);
		out.print(res);
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
