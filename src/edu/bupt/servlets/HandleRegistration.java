package edu.bupt.servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import edu.bupt.util.ConstantArgs;
import edu.bupt.util.HXTool;
import edu.bupt.util.SqlTool;
import edu.bupt.util.SqlTool.ResponseStatus;

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
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);

	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// String rootDir =
		// request.getSession().getServletContext().getRealPath("/");
		// System.out.println("root dir:"+rootDir);

		// String storageDir = rootDir + File.separator + "img";
		
		int addResponse = ResponseStatus.INSERT_FAILED.getValue();

		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		String avatar = request.getParameter("avatar");
		double lat = Double.parseDouble(request.getParameter("lat"));
		double lon = Double.parseDouble(request.getParameter("lon"));

		System.out.println("new changes");
		System.out.println("username:" + userName + "\npassword:" + password
				+ "\nemail+:" + email + "\ngender:" + gender + "\nlat:" + lat
				+ "\nlon:" + lon);

		
		/* store avatar in img path*/
		byte[] avatarData = Base64.decodeBase64(avatar);
		File avatarImage = new File(ConstantArgs.IMAGE_CACHE_DIR, userName
				+ ".png");
		if (avatarImage.exists()) {
			avatarImage.delete();
		}
		avatarImage.createNewFile();
		FileOutputStream outputStream = new FileOutputStream(avatarImage);
		outputStream.write(avatarData);
		outputStream.flush();
		outputStream.close();

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JSONObject resJsonObject = new JSONObject();

		boolean f1 = SqlTool.isExist("u_name", userName);
		boolean f2 = SqlTool.isExist("u_email", email);
		System.out.println(f1 + "," + f2);
		if (f1) {
			resJsonObject.put(SqlTool.STATUS_KEY,
					SqlTool.ResponseStatus.USER_NAME_DUPLICATE.getValue());
			out.print(resJsonObject);
			out.flush();
			out.close();
			return ;
		}
		if (f2) {
			resJsonObject.put(SqlTool.STATUS_KEY,
					SqlTool.ResponseStatus.EMAIL_DUPLICATE.getValue());
			out.print(resJsonObject);
			out.flush();
			out.close();
			return ;
		}

		try {
			addResponse = SqlTool.addUser(userName, password,
					email, gender, null,lat,lon);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		if (addRes == SqlTool.ResponseStatus.OK) {
////			// 更新地理坐标
////			SqlTool.updateUserLoc(userName, lat, lon);
//			// 注册环信账号
//			int statusCode = HXTool.registerHXUser(userName, password);
//			if (statusCode == 200) {
//				resJsonObject.put(SqlTool.STATUS_KEY, ResponseStatus.OK.getValue());
//				prep
//			} else {
//				resJsonObject.put(SqlTool.STATUS_KEY,
//						SqlTool.ResponseStatus.HX_REGISTER_FAILED.getValue());
//			}
//		}else{
//			/*数据库插入失败*/
//			resJsonObject.put(SqlTool.STATUS_KEY,
//					addRes.getValue());
//		}

		resJsonObject.put(SqlTool.STATUS_KEY, addResponse);
		out.print(resJsonObject);
		out.flush();
		out.close();

	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
