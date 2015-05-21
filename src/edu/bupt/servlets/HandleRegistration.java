package edu.bupt.servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;



import edu.bupt.util.ConstantArgs;
import edu.bupt.util.HXTool;
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

		
//		String rootDir = request.getSession().getServletContext().getRealPath("/");
//		System.out.println("root dir:"+rootDir);
		
//		String storageDir = rootDir + File.separator + "img";
		
		String userName  = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		String avatar = request.getParameter("avatar");
		double lat = Double.parseDouble(request.getParameter("lat"));
		double lon = Double.parseDouble(request.getParameter("lon"));
		
		
		
		System.out.println("new changes");
		System.out.println("username:"+userName+
				"\npassword:"+password+
				"\nemail+:"+email+
				"\ngender:"+gender+
				"\nlat:"+lat+
				"\nlon:"+lon);
		
		byte[] avatarData = Base64.decodeBase64(avatar);
		File avatarImage = new File(ConstantArgs.IMAGE_CACHE_DIR,userName+".png");
		avatarImage.createNewFile();
		FileOutputStream outputStream = new FileOutputStream(avatarImage);
		outputStream.write(avatarData);
		outputStream.flush();
		outputStream.close();
		
		
		
		MySql.connectMysql();
		boolean f1 = MySql.isExist("u_name",userName);
		boolean f2 = MySql.isExist("u_email", email);
		System.out.println(f1+","+f2);
		
		MySql.ResponseStatus addRes = MySql.addUser(userName, password, email, gender, null);
		if(addRes == MySql.ResponseStatus.OK){
			//更新地理坐标
			MySql.updateUserLoc(userName, lat, lon);
			//注册环信账号
			int statusCode = HXTool.registerHXUser(userName, password);
			if(statusCode == 200){}
			else{
				if(addRes == MySql.ResponseStatus.OK){
					addRes = MySql.ResponseStatus.USER_NOT_ON_HX;
				}
		}
		}
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
