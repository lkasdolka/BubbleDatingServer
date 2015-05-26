package edu.bupt.servlets;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.bupt.util.ConstantArgs;

public class HandleDownloadImage extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public HandleDownloadImage() {
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

		String fileName = request.getParameter("img");
		String wholeFilePath = ConstantArgs.IMAGE_CACHE_DIR + File.separator + fileName;
		System.out.println("request file name is :"+ wholeFilePath);
		response.setContentType("image/png");
		
		File targetFile = new File(wholeFilePath);
		BufferedImage bi = ImageIO.read(targetFile);
		
		OutputStream out = response.getOutputStream();
		response.setHeader("Cache-Control","max-age=99999999");
		
		
		if(!targetFile.exists()){
			System.out.println("target file not exists.");
			
		}else{
			System.out.println("found target file");
			ImageIO.write(bi, "png", out);
			
//			InputStream inputStream = new FileInputStream(targetFile);
//			OutputStream outputStream = new BufferedOutputStream(out);
//			byte[] buffer = new byte[8192];
//			for(int length = 0; (length = inputStream.read(buffer))>0;){
//				outputStream.write(buffer,0,length);
//			}
		}
		
		
		
		
		out.flush();
		out.close();
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

		doGet(request, response);
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
