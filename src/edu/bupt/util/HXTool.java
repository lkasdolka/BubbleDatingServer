package edu.bupt.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class HXTool {
	private static final String CLIENT_ID = "YXA6LUM2wPFDEeSFuOcmeziT4Q";
	private static final String CLIENT_SECRET = "YXA6zNTcEdfFaANjcQnP3IdnpeV-LIc";
	private static final String GET_ACCESS_URL = "https://a1.easemob.com/halfdog/bubbledating/token";
	private static final String REGISTER_URL = "https://a1.easemob.com/halfdog/bubbledating/users";
	private static String ACCESS_TOKEN = "";

	public static void main(String[] args) {
		System.setProperty("javax.net.ssl.trustStore", "F:\\Program Files\\Java\\jdk1.8.0_31\\lib\\cacerts");
//		System.out.println(getAccessToken());
//		int resCode = registerHXUser("aaa", "aaa");
//		if(resCode == 200)System.out.println("register successfully");
//		else System.out.println("certain problem occurred");
		System.out.println(getUser("aaas"));
	}

	public static String getAccessToken() {
		System.setProperty("javax.net.ssl.trustStore", "F:\\Program Files\\Java\\jdk1.8.0_31\\lib\\cacerts");
		String res = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(GET_ACCESS_URL);
		JSONObject body = new JSONObject();
		body.put("grant_type", "client_credentials");
		body.put("client_id", CLIENT_ID);
		body.put("client_secret", CLIENT_SECRET);
		System.out.println(body.toString());
		CloseableHttpResponse response2 = null;
		try {
			StringEntity entity = new StringEntity(body.toString(), ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			response2 = httpclient.execute(httpPost);
			System.out.println(response2.getStatusLine());
			HttpEntity entity2 = response2.getEntity();
			if (entity2 != null) {
				String content = EntityUtils.toString(entity2);
				// System.out.println("response:"+content);
				res = content;
				EntityUtils.consume(entity2);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			try {
				if (response2 != null) {
					response2.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}
	
	public static boolean getUser(String username){
		String res = getAccessToken();
		String ans = null;
		int statusCode = -1;
		JSONObject response = new JSONObject(res);
		ACCESS_TOKEN = response.getString("access_token");
		
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(REGISTER_URL+"/"+username);
		httpGet.setHeader("Authorization","Bearer "+ACCESS_TOKEN);
		CloseableHttpResponse response1 = null;
		try {
			response1 = httpclient.execute(httpGet);
			System.out.println(response1.getStatusLine());
			statusCode = response1.getStatusLine().getStatusCode();
			HttpEntity entity1 = response1.getEntity();
			if (entity1 != null) {
				ans = EntityUtils.toString(entity1);
				System.out.println(ans);
				EntityUtils.consume(entity1);
			}

		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		finally {
			try {
				response1.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(statusCode == 200) return true;
		else return false;
//		return statusCode;
		
		
	}
	
	public static int registerHXUser(String user, String pw){
		String res = getAccessToken();
		String ans = null;
		int statusCode = -1;
		JSONObject response = new JSONObject(res);
		ACCESS_TOKEN = response.getString("access_token");
		
		int registerStatus = -1;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(REGISTER_URL);
		JSONObject body = new JSONObject();
		body.put("username", user);
		body.put("password", pw);
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Authorization","Bearer "+ACCESS_TOKEN);
		CloseableHttpResponse response2 = null;
		try {
			StringEntity entity = new StringEntity(body.toString(), ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			response2 = httpclient.execute(httpPost);
			System.out.println(response2.getStatusLine());
			statusCode = response2.getStatusLine().getStatusCode();
			
			HttpEntity entity2 = response2.getEntity();
			if (entity2 != null) {
				String content = EntityUtils.toString(entity2);
				// System.out.println("response:"+content);
				ans = content;
				EntityUtils.consume(entity2);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			try {
				if (response2 != null) {
					response2.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return statusCode;
	}
	

	public static String get(String url) {
		String res = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response1 = null;
		try {
			response1 = httpclient.execute(httpGet);
			System.out.println(response1.getStatusLine());
			HttpEntity entity1 = response1.getEntity();
			if (entity1 != null) {
				res = EntityUtils.toString(entity1);
				System.out.println(res);
				EntityUtils.consume(entity1);
			}

		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		finally {
			try {
				response1.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}

	public static String post(String url, String username, String pw) {
		String res = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("username", username));
		nvps.add(new BasicNameValuePair("password", pw));
		CloseableHttpResponse response2 = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			response2 = httpclient.execute(httpPost);
			System.out.println(response2.getStatusLine());
			HttpEntity entity2 = response2.getEntity();
			if (entity2 != null) {
				res = EntityUtils.toString(entity2);
				System.out.println(res);
				EntityUtils.consume(entity2);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				response2.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}
}
