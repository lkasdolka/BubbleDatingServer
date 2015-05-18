package edu.bupt.util;
import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class MyServletContext implements ServletContextListener {
	
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
//		ConstantArgs.IMAGE_CACHE_DIR = sce.getServletContext().getRealPath("/");
		String rootDir = sce.getServletContext().getRealPath("/");
		System.out.println("root dir: "+ rootDir);
		ConstantArgs.IMAGE_CACHE_DIR = rootDir + File.separator +"WEB-INF"+File.separator+ ConstantArgs.IMG_DIR;
		File mImageCacheDir = new File(ConstantArgs.IMAGE_CACHE_DIR);
		if(!mImageCacheDir.exists()){
			mImageCacheDir.mkdir();
			System.out.println("mkdir of image cache folder");
		}
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
