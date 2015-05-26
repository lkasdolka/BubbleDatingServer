package edu.bupt.util;
import java.io.File;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class MyServletContext implements ServletContextListener {
	
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
//		ConstantArgs.IMAGE_CACHE_DIR = sce.getServletContext().getRealPath("/");
		String rootDir = sce.getServletContext().getRealPath("/");
//		System.out.println("root dir: "+ rootDir);
		ConstantArgs.IMAGE_CACHE_DIR = rootDir +File.separator+ ConstantArgs.IMG_DIR;
		System.out.println("image cache dir:"+ ConstantArgs.IMAGE_CACHE_DIR);
		File mImageCacheDir = new File(ConstantArgs.IMAGE_CACHE_DIR);
		if(!mImageCacheDir.exists()){
			mImageCacheDir.mkdir();
			System.out.println("mkdir of image cache folder");
		}
		
		try {
			SqlTool.initConnectionPool();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		try {
			SqlTool.shutdownDataSource(SqlTool.getInstance());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
