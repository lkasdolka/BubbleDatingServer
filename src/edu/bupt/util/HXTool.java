package edu.bupt.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
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
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class HXTool {
	private static final String CLIENT_ID = "YXA6LUM2wPFDEeSFuOcmeziT4Q";
	private static final String CLIENT_SECRET = "YXA6zNTcEdfFaANjcQnP3IdnpeV-LIc";
	private static final String GET_ACCESS_URL = "https://a1.easemob.com/halfdog/bubbledating/token";
	private static final String REGISTER_URL = "https://a1.easemob.com/halfdog/bubbledating/users";
	private static final String CER_PATH = "F:\\Program Files\\Java\\jre1.8.0_31\\lib\\security\\cacerts";
	private static final String CER_PATH2 = "F:\\Program Files\\Java\\jre1.8.0_31\\lib\\security\\cacerts";
	
	public static void main(String[] args) {
//		System.setProperty("javax.net.ssl.trustStore", CER_PATH);
//		System.out.println(getAccessToken());
//		int resCode = registerHXUser("aaa", "aaa");
//		if(resCode == 200)System.out.println("register successfully");
//		else System.out.println("certain problem occurred");
		getAccessToken();
		System.out.println(getUser("aaa"));
	}

	public static String getAccessToken() {
//		System.setProperty("javax.net.ssl.trustStore", CER_PATH);
		String res = null;
		CloseableHttpClient httpclient = getHttpClient();
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
				JSONObject jsonObject = new JSONObject(content);
				ConstantArgs.HX_ACCESS_TOKEN = jsonObject.getString("access_token");
				System.out.println("HX_ACCESS_TOKEN:"+ConstantArgs.HX_ACCESS_TOKEN);
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
		String ans = null;
		int statusCode = -1;
		
//		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpClient httpclient = getHttpClient();
		HttpGet httpGet = new HttpGet(REGISTER_URL+"/"+username);
		System.out.println("get user:"+REGISTER_URL+"/"+username);
		httpGet.setHeader("Authorization","Bearer "+ConstantArgs.HX_ACCESS_TOKEN);
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
		
		String ans = null;
		int statusCode = -1;
		int registerStatus = -1;
		
		CloseableHttpClient httpclient = getHttpClient();
		HttpPost httpPost = new HttpPost(REGISTER_URL);
		JSONObject body = new JSONObject();
		body.put("username", user);
		body.put("password", pw);
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Authorization","Bearer "+ ConstantArgs.HX_ACCESS_TOKEN);
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
		CloseableHttpClient httpclient = getHttpClient();
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
		CloseableHttpClient httpclient = getHttpClient();
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
	private static CloseableHttpClient getHttpClient() {  
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();  
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();  
        registryBuilder.register("http", plainSF);  
//指定信任密钥存储对象和连接套接字工厂  
        try {  
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());  
            //信任任何链接  
            TrustStrategy anyTrustStrategy = new TrustStrategy() {  
                @Override  
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {  
                    return true;  
                }

            };  
            SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();  
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
            registryBuilder.register("https", sslSF);  
        } catch (KeyStoreException e) {  
            throw new RuntimeException(e);  
        } catch (KeyManagementException e) {  
            throw new RuntimeException(e);  
        } catch (NoSuchAlgorithmException e) {  
            throw new RuntimeException(e);  
        }  
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();  
        //设置连接管理器  
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);  
//      connManager.setDefaultConnectionConfig(connConfig);  
//      connManager.setDefaultSocketConfig(socketConfig);  
        //构建客户端  
        return HttpClientBuilder.create().setConnectionManager(connManager).build();  
    }  
	
}
