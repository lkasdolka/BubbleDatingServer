package edu.bupt.util;

import java.awt.List;
import java.io.Closeable;
import java.io.File;
import java.net.ConnectException;
import java.security.interfaces.DSAKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.json.JSONObject;

import com.mysql.jdbc.authentication.MysqlClearPasswordPlugin;

import edu.bupt.bean.User;

public class SqlTool {
	private final static String USER = "andy";
	private final static String URL = "jdbc:mysql://localhost:3306";
	private final static String URL2 = "jdbc:mysql://localhost:3306/bubble_datiing";
	private final static String PW = "awesome222";
	private final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final static String DB_NAME = "bubble_datiing";
	private final static double EPSILON = 0.00001;

	private static Connection connection = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	private static BasicDataSource ds = null;

	public final static String STATUS_KEY = "status";

	public static void main(String[] args) {
		 try {
		 SqlTool.initConnectionPool();
		 } catch (SQLException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
		
		 insertInvitation("aaa","m","hi",new Timestamp(new Date().getTime()),0,0);
		 recycleRes();
//		String s = "https://a1.easemob.com/halfdog/bubbledating/users/loly";
//		System.out.println(s.length());
//		System.out.println(s.charAt(54));
	}

	public static BasicDataSource getInstance() {
		return ds;
	}

	public static void initConnectionPool() throws SQLException {
		ds = new BasicDataSource();
		ds.setDriverClassName(JDBC_DRIVER);
		ds.setUrl(URL2);
		ds.setUsername(USER);
		ds.setPassword(PW);
		ds.setDefaultAutoCommit(false);
	}

	public static void shutdownDataSource(DataSource ds) throws SQLException {
		BasicDataSource bds = (BasicDataSource) ds;
		bds.close();
	}

	public static void recycleRes() {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
				System.out.println("end the connection");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Deprecated
	public static void connectMysql() {

		try {
			// load driver
			Class.forName(JDBC_DRIVER);
			System.out.println("load drivers");

			connection = DriverManager.getConnection(URL, USER, PW);
			System.out.println("make connection");
			System.out.println("connection name:"
					+ connection.getClass().getName());

			connection.setCatalog(DB_NAME);
			System.out.println("set database");

			connection.setAutoCommit(false);
			System.out.println("ready for transaction");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Deprecated
	public static void endConnection() {
		try {
			connection.setAutoCommit(true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
				System.out.println("end the connection");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void selectAll(String tableName) {
		try {
			connection = SqlTool.getInstance().getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String sql = "select * from " + tableName;
		System.out.println(sql);
		try {
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
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
		} finally {
			SqlTool.recycleRes();
		}
	}

	public static boolean isExist(String colName, String value) {
		// u_name is string , here needs a quote

		String sql = "select " + colName + " from user_info where " + colName
				+ "='" + value + "'";
		System.out.println(sql);
		try {
			connection = SqlTool.ds.getConnection();
			System.out.println("acquire the connection");
			if (connection == null)
				System.out.println("connection is null.");
			else
				System.out.println("connection is not null");
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			SqlTool.recycleRes();
		}
		System.out.println("exception during query");
		return false;
	}

	public static User isExistMultiParam(ArrayList<String> colNames,
			ArrayList<String> values, String username) {
		if (colNames == null || values == null
				|| colNames.size() != values.size()) {
			return null;
		}
		String query = "select * from user_info where ";
		for (int i = 0; i < colNames.size(); i++) {
			if (i != colNames.size() - 1) {
				query += colNames.get(i) + "='" + values.get(i) + "' and ";
			} else {
				query += colNames.get(i) + "='" + values.get(i) + "'";
			}

		}
		System.out.println(query);
		try {
			connection = SqlTool.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(query);
			if (preparedStatement.execute()) {
				resultSet = preparedStatement.getResultSet();
				System.out.println("get full user info");
				if (resultSet.next()) {
					System.out.println("get target user info");
					preparedStatement = connection
							.prepareStatement("select * from user_info where u_name='"
									+ username + "'");
					if (preparedStatement.execute()) {
						resultSet = preparedStatement.getResultSet();
						if (resultSet.next()) {
							User userInfo = new User(resultSet.getInt(1),
									resultSet.getString(2),
									resultSet.getString(3),
									resultSet.getString(4),
									resultSet.getString(5), null, false);
							System.out.println("user entity in getUser:"
									+ userInfo.toString());
							return userInfo;
						} else {
							System.out.println("cannot find target user");
						}
					}

				}
			} else {
				System.out.println("no result");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			SqlTool.recycleRes();
		}

		return null;
	}

	public static ResponseStatus addUser(String uname, String pw, String email,
			String gender, String image) {
		if (isExist("u_name", uname)) {
			System.out.println(uname + " already exists!");
			return ResponseStatus.USER_NAME_DUPLICATE;
		}
		if (isExist("u_email", email)) {
			System.out.println(email + " already exists!");
			return ResponseStatus.EMAIL_DUPLICATE;
		}

		String sql = "insert into user_info (u_id,u_name,u_pw,u_email,u_gender,u_image) values ("
				+ null
				+ ",'"
				+ uname
				+ "','"
				+ pw
				+ "','"
				+ email
				+ "','"
				+ gender + "'," + null + ")";
		System.out.println(sql);
		try {
			connection = SqlTool.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(sql);
			if (!preparedStatement.execute()) {
				int count = preparedStatement.getUpdateCount();
				if (count > 0) {
					System.out.println("insert successfully," + count
							+ " line(s) updated");
					connection.commit();
					return ResponseStatus.OK;
				}
			} else {
				System.out.println("insert fails.");
				return ResponseStatus.UNKNOWN_ERROR;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			SqlTool.recycleRes();
		}
		System.out.println("exception happened during add user.");
		return ResponseStatus.UNKNOWN_ERROR;

	}

	public static ArrayList<JSONObject> queryPeopleAround() {
		ArrayList<JSONObject> res = new ArrayList<JSONObject>();
		try {
			// issue : group by cannot sort duplicate items desc with time ,and
			// present the oldest item
			connection = SqlTool.getInstance().getConnection();
			preparedStatement = connection
					.prepareStatement("select u_invi,u_posttime,u_name,u_gender,u_lat,u_lon from invitation;");
			if (preparedStatement.execute()) {
				resultSet = preparedStatement.getResultSet();
				while (resultSet.next()) {
					if (Math.abs(resultSet.getDouble(5)) < EPSILON
							|| Math.abs(resultSet.getDouble(6)) < EPSILON) {
						continue;
					}
					JSONObject item = new JSONObject();
//					item.put("u_id", resultSet.getInt(1));
					item.put("u_invi", resultSet.getString(1));
					long u_posttime = resultSet.getTimestamp(2).getTime();
					item.put("u_posttime", u_posttime);
					item.put("u_name", resultSet.getString(3));
					item.put("u_gender", resultSet.getString(4));
					item.put("u_loc_lat", resultSet.getDouble(5));
					item.put("u_loc_long", resultSet.getDouble(6));
					res.add(item);

					// System.out.println(item.toString());
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			SqlTool.recycleRes();
		}

		return res;

	}

	public static void updateUserLoc(String userName, double lat, double lon) {
		try {
			connection = SqlTool.getInstance().getConnection();
			System.out.println("get a connection from connection pool");
			preparedStatement = connection
					.prepareStatement("update user_info set u_loc_lat=?,u_loc_long=? where u_name=?");
			preparedStatement.setDouble(1, lat);
			preparedStatement.setDouble(2, lon);
			preparedStatement.setString(3, userName);
			System.out.println(preparedStatement.toString());
			System.out.println(preparedStatement.executeUpdate());
			connection.commit();
			System.out.println("update user loc success");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			SqlTool.recycleRes();
		}

	}

	public static int handFeedback(String username, String content) {

		try {
			connection = SqlTool.getInstance().getConnection();
			String insertSql = "insert into feedback (u_name,u_content) values (?,?)";
			preparedStatement = connection.prepareStatement(insertSql);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, content);

			boolean flag = preparedStatement.execute();
			if (!flag) {
				int count = preparedStatement.getUpdateCount();
				if (count != 0) {
					System.out
							.println("insert feed back success, insert count:"
									+ count);
					connection.commit();
					return 1;

				} else {
					System.out.println("insert failed.");
					return 0;
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			recycleRes();
		}
		return 0;

	}

	public static int insertInvitation(String name, String gender,
			String invitation, Timestamp posttime, double lat, double lon) {
		try {
			connection = SqlTool.getInstance().getConnection();
			String sql = "insert into invitation(u_name,u_gender,u_invi,u_posttime,u_lat,u_lon) "
					+ "values(?,?,?,?,?,?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, gender);
			preparedStatement.setString(3, invitation);
			preparedStatement.setTimestamp(4, posttime);
			preparedStatement.setDouble(5, lat);
			preparedStatement.setDouble(6, lon);

			boolean flag = preparedStatement.execute();

			if (!flag) {
				int count = preparedStatement.getUpdateCount();
				if (count != 0) {
					System.out
							.println("insert feed back success, insert count:"
									+ count);
					connection.commit();
					return 1;

				} else {
					System.out.println("insert failed.");
					return 0;
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			recycleRes();
		}
		return 0;
	}

	public static enum ResponseStatus {
		OK(0), USER_NAME_DUPLICATE(1), EMAIL_DUPLICATE(2), UNKNOWN_ERROR(3), UNKOWN_USERNAME(
				4), USERNAME_PASSWORD_UNCOMPATIBLE(5), USER_NOT_ON_HX(6);

		private final int value;

		ResponseStatus(int x) {
			value = x;
		}

		public int getValue() {
			return value;
		}

	}

	public static void printDataSourceStats(DataSource ds) {
		BasicDataSource bds = (BasicDataSource) ds;
		System.out.println("NumActive: " + bds.getNumActive());
		System.out.println("NumIdle: " + bds.getNumIdle());
	}

}
