package edu.bupt.util;

import java.awt.List;
import java.io.Closeable;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;

import com.mysql.jdbc.authentication.MysqlClearPasswordPlugin;

import edu.bupt.bean.User;

public class MySql {
	
	
	

	private final static String USER = "andy";
	private final static String URL = "jdbc:mysql://localhost:3306";
	private final static String PW = "awesome222";
	private final static String DRIVER = "com.mysql.jdbc.Driver";
	private final static String DB_NAME = "bubble_datiing";
	
	private static Connection connection = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	
	public final static String STATUS_KEY = "status";
	
	
	public static void main(String[] args) {
		MySql.connectMysql();
		MySql.selectAll("user_info");
		
		System.out.println("add user test2:"+MySql.addUser("test4", "test2", "test4", "f", null).toString());
		System.out.println("add user Mary:"+MySql.addUser("Mary", "test2", "test2", "f", null).toString());
	}
	
	
	public static void connectMysql() {

		try {
			// load driver
			Class.forName(DRIVER);
			System.out.println("load drivers");

			connection = DriverManager.getConnection(URL, USER, PW);
			System.out.println("make connection");

			connection.setCatalog(DB_NAME);
			System.out.println("set database");
			
			connection.setAutoCommit(false);
			System.out.println("ready for transaction");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void endConnection(){
		try {
			connection.setAutoCommit(true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(resultSet != null){
			try {
				resultSet.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(preparedStatement != null){
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(connection != null){
			try {
				connection.close();
				System.out.println("end the connection");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void selectAll(String tableName){
		String sql = "select * from "+tableName;
		System.out.println(sql);
		try {
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				System.out.println(resultSet.getInt("u_id"));
				System.out.println(resultSet.getString("u_name"));
				System.out.println(resultSet.getString("u_pw"));
				System.out.println(resultSet.getString("u_email"));
				System.out.println(resultSet.getString("u_gender"));
				System.out.println(resultSet.getString("u_online"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean isExist(String colName,String value){
		//u_name is string , here needs a quote
		String sql = "select "+colName+" from user_info where "+colName+"='"+value+"'";
		System.out.println(sql);
		try {
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				return true;
			}else{
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("exception during query");
		return false;
	}
	
	public static User isExistMultiParam(ArrayList<String> colNames,
			ArrayList<String> values,String username) {
		if (colNames == null || values == null
				|| colNames.size() != values.size()) {
			return null;
		}
		String query = "select * from user_info where ";
		for (int i = 0; i < colNames.size(); i++) {
			if(i!=colNames.size()-1){
				query += colNames.get(i)+"='"+values.get(i)+"' and ";
			}else{
				query += colNames.get(i)+"='"+values.get(i)+"'";
			}
			
		}
		System.out.println(query);
		try {
			preparedStatement = connection.prepareStatement(query);
			if(preparedStatement.execute()){
				resultSet = preparedStatement.getResultSet();
				System.out.println("get full user info");
				if(resultSet.next()){
					System.out.println("get target user info");
					preparedStatement = connection.prepareStatement("select * from user_info where u_name='"+username+"'");
					if(preparedStatement.execute()){
						resultSet = preparedStatement.getResultSet();
						if(resultSet.next()){
							User userInfo = new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), null, false);
							System.out.println("user entity in getUser:"+userInfo.toString());
							return userInfo;
						}else{
							System.out.println("cannot find target user");
						}
					}
					
				}
			}else{
				System.out.println("no result");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public static ResponseStatus addUser(String uname,String pw,String email,String gender,String image){
		if(isExist("u_name",uname)){
			System.out.println(uname+ " already exists!");
			return ResponseStatus.USER_NAME_DUPLICATE;
		}
		if(isExist("u_email", email)){
			System.out.println(email+ " already exists!");
			return ResponseStatus.EMAIL_DUPLICATE;
		}
		
		String sql = "insert into user_info (u_id,u_name,u_pw,u_email,u_gender,u_image) values ("+null+",'"+uname+"','"+pw+"','"+email+"','"+gender+"',"+null+")";
		System.out.println(sql);
		try {
			preparedStatement = connection.prepareStatement(sql);
			if(!preparedStatement.execute()){
				int count = preparedStatement.getUpdateCount();
				if(count > 0){
					System.out.println("insert successfully,"+count+" line(s) updated");
					connection.commit();
					return ResponseStatus.OK;
				}
			}else{
				System.out.println("insert fails.");
				return ResponseStatus.UNKNOWN_ERROR;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("exception happened during add user.");
		return ResponseStatus.UNKNOWN_ERROR;
		
	}
	
	public static ArrayList<JSONObject> queryPeopleAround(){
		ArrayList<JSONObject> res = new ArrayList<JSONObject>();
		try {
			// issue : group by cannot sort duplicate items desc with time ,and present the oldest item
			preparedStatement = connection.prepareStatement("select distinct  invitation.u_id,u_invi,u_posttime,u_name,u_gender,u_loc_lat,u_loc_long from invitation  inner join user_info where  user_info.u_id = invitation.u_id group by invitation.u_id order by u_posttime desc ;");
			if(preparedStatement.execute()){
				resultSet = preparedStatement.getResultSet();
				while(resultSet.next()){
					JSONObject item = new JSONObject();
					item.put("u_id", resultSet.getInt(1));
					item.put("u_invi", resultSet.getString(2));
					item.put("u_posttime", resultSet.getString(3));
					item.put("u_name", resultSet.getString(4));
					item.put("u_gender", resultSet.getString(5));
					item.put("u_loc_lat", resultSet.getDouble(6));
					item.put("u_loc_long", resultSet.getDouble(7));
					res.add(item);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
		
	}
	
	
	public static enum ResponseStatus {
		OK(0),
		USER_NAME_DUPLICATE(1),
		EMAIL_DUPLICATE(2),
		UNKNOWN_ERROR(3),
		UNKOWN_USERNAME(4),
		USERNAME_PASSWORD_UNCOMPATIBLE(5);
		
		private final int value;
		
		ResponseStatus(int x){
			value = x;
		} 
		
		public int getValue(){
			return value;
		}
		
	}	
	

}
