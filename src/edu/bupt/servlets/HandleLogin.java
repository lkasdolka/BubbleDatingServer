package edu.bupt.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import edu.bupt.bean.User;
import edu.bupt.util.MySql;

public class HandleLogin extends HttpServlet {
	

	/**
	 * Constructor of the object.
	 */
	public HandleLogin() {
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

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		JSONObject resData = new JSONObject();
		MySql.connectMysql();
		String userName  = request.getParameter("username");
		String password = request.getParameter("password");
		System.out.println("username:"+userName);
		System.out.println("password:"+password);
		ArrayList<String> queryKeys = new ArrayList<String>();
		ArrayList<String> queryValues = new ArrayList<String>();
		queryKeys.add("u_name");
		queryKeys.add("u_pw");
		queryValues.add(userName);
		queryValues.add(password);
		
		
		if(!MySql.isExist("u_name", userName)){
			resData.put(MySql.STATUS_KEY, MySql.ResponseStatus.UNKOWN_USERNAME.getValue());
		}else{
			User user = MySql.isExistMultiParam(queryKeys,queryValues,userName);
			if(user != null){
//				System.out.println("user bean:"+user.toString());
				resData.put(MySql.STATUS_KEY, MySql.ResponseStatus.OK.getValue());
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("u_id", user.getmId());
				map.put("u_name", user.getmName());
				map.put("u_password", user.getmPw());
				map.put("u_email", user.getmEmail());
				map.put("u_gender", user.getmGender());
				map.put("u_image", user.getmImage());
				map.put("u_online", user.getmOnline());
				JSONObject u_info = new JSONObject(map);
				System.out.println(u_info.toString());
				resData.put("user_info", u_info);
			}else{
				resData.put(MySql.STATUS_KEY, MySql.ResponseStatus.USERNAME_PASSWORD_UNCOMPATIBLE.getValue());
			}
		}
		
		out.print(resData);
		out.flush();
		out.close();
		MySql.endConnection();
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
