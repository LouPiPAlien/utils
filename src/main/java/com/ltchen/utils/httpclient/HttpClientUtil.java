package com.ltchen.utils.httpclient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * 
 * @file : HttpClientUtil.java
 * @date : 2017年3月24日
 * @author : ltchen
 * @email : loupipalien@gmail.com
 * @desc : HttpClient工具类
 * 
 * 1、since 4.3 不再使用 DefaultHttpClient
 * 2、UrlEncodedFormEntity()的形式比较单一，只能是普通的键值对，局限性相对较大;而StringEntity()的形式比较自由，只要是字符串放进去，不论格式都可以。
 * 3、当请求体为Json字符串时发送请求时，需指定 Content type：httpost.setHeader("Content-type", "application/json");  否则默认使用 Content type 'text/plain;charset=UTF-8'。
 */
@SuppressWarnings("deprecation")
public class HttpClientUtil {

	private static Logger logger = Logger.getLogger(HttpClientUtil.class);
	
    /**
     * 私有化默认构造器
     */
	private HttpClientUtil() {
	}
	
	public static void main(String[] args){
		
//		 // 记录debug级别的信息  
//        logger.debug("This is debug message.");  
//        // 记录info级别的信息  
//        logger.info("This is info message.");  
//        // 记录error级别的信息  
//        logger.error("This is error message.");  
	
		String result = HttpClientUtil.doGet("http://127.0.0.1:8080/", null, null, null);
		System.out.println(result);
	}
	
    private static final String DEFAULT_CHARSET = "UTF-8";
	
    /**
     * http get method
     * @param url 
     * @param headerMap
     * @param paramMap
     * @param charset
     * @return
     */
	public static String doGet(String url, Map<String, String> paramMap, Map<String, String> headerMap, String charset) {
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		CloseableHttpResponse closeableHttpResponse = null;
		String result = null;
		if( charset == null ){
			charset = DEFAULT_CHARSET;
		}
		// 拼接url参数
		StringBuffer sb = new StringBuffer(url);
		if (paramMap != null && !paramMap.isEmpty()) {
			if (sb.indexOf("?") > 0) {
				sb.append("&");
			} else {
				sb.append("?");
			}
			for (String key : paramMap.keySet()) {
				sb.append(key).append("=").append(paramMap.get(key)).append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		//获取一个HttpGet对象
		HttpGet httpGet = new HttpGet(sb.toString());
		// 设置请求头
		if(headerMap != null){
			for (String key : headerMap.keySet()) {
				httpGet.setHeader(key, headerMap.get(key));
			}
		}
		try {
			closeableHttpResponse = closeableHttpClient.execute(httpGet);
			if ( closeableHttpResponse != null){
				if(closeableHttpResponse.getStatusLine().getStatusCode() == 200){
					HttpEntity httpEntity = new BufferedHttpEntity(closeableHttpResponse.getEntity());
					result = EntityUtils.toString(httpEntity, charset);
				}
				else{
					logger.info(closeableHttpResponse.getStatusLine().toString());
					logger.info(closeableHttpResponse.getAllHeaders().toString());
					logger.info(closeableHttpResponse.getEntity().toString());
				}
			}
		}catch (IOException e) {
			logger.error("IOException was catched:",e);
		}finally {
			if (closeableHttpResponse != null){
				try {
					closeableHttpResponse.close();
				} catch (IOException e) {
					logger.error("CloseableHttpResponse close failed:",e);
				}
			}
			if (closeableHttpClient != null){
				try {
					closeableHttpClient.close();
				} catch (IOException e) {
					logger.error("CloseableHttpClient close failed:",e);
				}
			}
		}
		return result;
	}

	public static HttpResponse doPost(String url, Map<String, String> headerMap, Map<String, String> entityMap,
			String charset) {
		//4.3版本以后不再使用DefaultHttpClient
		CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
		CloseableHttpResponse closeableHttpResponse = null;
		HttpResponse httpResponse = null;
		HttpPost httpPost = null;
		if( charset == null ){
			charset = DEFAULT_CHARSET;
		}
		httpPost = new HttpPost(url);
		// 设置请求头
		for (String key : headerMap.keySet()) {
			httpPost.setHeader(key, headerMap.get(key));
		}
		try {
			// 设置请求体
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			Iterator<Entry<String, String>> iterator = entityMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> elem = (Entry<String, String>) iterator.next();
				list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
			}
			if (list.size() > 0) {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
				httpPost.setEntity(entity);
			}
			closeableHttpResponse = closeableHttpClient.execute(httpPost);
			if ( closeableHttpResponse != null){
				//设置响应行
				httpResponse = new BasicHttpResponse(closeableHttpResponse.getStatusLine());
				//设置响应头
				httpResponse.setHeaders(closeableHttpResponse.getAllHeaders());
				//设置响应体
				httpResponse.setEntity(closeableHttpResponse.getEntity());;
			}
		} catch (Exception e) {
			e.printStackTrace();;
		}finally {
			if (closeableHttpResponse != null){
				try {
					closeableHttpResponse.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (closeableHttpClient != null){
				try {
					closeableHttpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return httpResponse;
	}

	public static HttpResponse doPost(String url, Map<String, String> headerMap, String entityStr, String charset) {
		//4.3版本以后不再使用DefaultHttpClient
		CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
		CloseableHttpResponse closeableHttpResponse = null;
		HttpResponse httpResponse = null;
		HttpPost httpPost = null;
		if( charset == null ){
			charset = DEFAULT_CHARSET;
		}
		httpPost = new HttpPost(url);
		// 设置请求头
		for (String key : headerMap.keySet()) {
			httpPost.setHeader(key, headerMap.get(key));
		}
		try {
			// 设置请求体
			StringEntity se = new StringEntity(entityStr, charset);
			httpPost.setEntity(se);
			closeableHttpResponse = closeableHttpClient.execute(httpPost);
			if ( closeableHttpResponse != null){
				//设置响应行
				httpResponse = new BasicHttpResponse(closeableHttpResponse.getStatusLine());
				//设置响应头
				httpResponse.setHeaders(closeableHttpResponse.getAllHeaders());
				//设置响应体
				httpResponse.setEntity(closeableHttpResponse.getEntity());;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (closeableHttpResponse != null){
				try {
					closeableHttpResponse.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (closeableHttpClient != null){
				try {
					closeableHttpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return httpResponse;
	}

	public static HttpResponse doPut(String url, Map<String, String> headerMap, String entityStr, String charset) {
		//4.3版本以后不再使用DefaultHttpClient
		CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
		CloseableHttpResponse closeableHttpResponse = null;
		HttpResponse httpResponse = null;
		HttpPut httpPut = null;
		if( charset == null ){
			charset = DEFAULT_CHARSET;
		}
		httpPut = new HttpPut(url);
		// 设置请求头
		for (String key : headerMap.keySet()) {
			httpPut.setHeader(key, headerMap.get(key));
		}
		try {
			// 设置请求体
			StringEntity se = new StringEntity(entityStr, charset);
			httpPut.setEntity(se);
			closeableHttpResponse = closeableHttpClient.execute(httpPut);
			if ( closeableHttpResponse != null){
				//设置响应行
				httpResponse = new BasicHttpResponse(closeableHttpResponse.getStatusLine());
				//设置响应头
				httpResponse.setHeaders(closeableHttpResponse.getAllHeaders());
				//设置响应体
				httpResponse.setEntity(closeableHttpResponse.getEntity());;
			}
		} catch (Exception e) {
			e.printStackTrace();;
		}finally {
			if (closeableHttpResponse != null){
				try {
					closeableHttpResponse.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (closeableHttpClient != null){
				try {
					closeableHttpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return httpResponse;
	}

	public static HttpResponse doDelete(String url, Map<String, String> headerMap, Map<String, String> paramMap,
			String charset) {
		//4.3版本以后不再使用DefaultHttpClient
		CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
		CloseableHttpResponse closeableHttpResponse = null;
		HttpResponse httpResponse = null;
		HttpDelete httpDelete = null;
		if( charset == null ){
			charset = DEFAULT_CHARSET;
		}
		StringBuffer sb = new StringBuffer(url);
		// 拼接url参数
		if (!paramMap.isEmpty()) {
			if (sb.indexOf("?") > 0) {
				sb.append("&");
			} else {
				sb.append("?");
			}
			for (String key : paramMap.keySet()) {
				sb.append(key).append("=").append(paramMap.get(key)).append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		httpDelete = new HttpDelete(url);
		// 设置请求头
		for (String key : headerMap.keySet()) {
			httpDelete.setHeader(key, headerMap.get(key));
		}
		try {
			closeableHttpResponse = closeableHttpClient.execute(httpDelete);
			if ( closeableHttpResponse != null){
				//设置响应行
				httpResponse = new BasicHttpResponse(closeableHttpResponse.getStatusLine());
				//设置响应头
				httpResponse.setHeaders(closeableHttpResponse.getAllHeaders());
				//设置响应体
				httpResponse.setEntity(closeableHttpResponse.getEntity());;
			}
		} catch (Exception e) {
			e.printStackTrace();;
		}finally {
			if (closeableHttpResponse != null){
				try {
					closeableHttpResponse.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (closeableHttpClient != null){
				try {
					closeableHttpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return httpResponse;
	}

	public String filePost(String url, Map<String, String> map, Map<String, File> files, String charset) {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		try {
			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost(url);
			// 设置参数
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> elem = (Entry<String, String>) iterator.next();
				params.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
			}
			HttpEntity entity = this.makeMultipartEntity(params, files);
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();;
		}
		return result;
	}

	private HttpEntity makeMultipartEntity(List<NameValuePair> params, final Map<String, File> files) {
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE); // 如果有SocketTimeoutException等情况，可修改这个枚举
		// builder.setCharset(Charset.forName("UTF-8"));
		// 不要用这个，会导致服务端接收不到参数
		if (params != null && params.size() > 0) {
			for (NameValuePair p : params) {
				builder.addTextBody(p.getName(), p.getValue(), ContentType.TEXT_PLAIN.withCharset("UTF-8"));
			}
		}
		if (files != null && files.size() > 0) {
			Set<Entry<String, File>> entries = files.entrySet();
			for (Entry<String, File> entry : entries) {
				// builder.addPart(entry.getKey(), new
				// FileBody(entry.getValue()));
				builder.addBinaryBody(entry.getKey(), entry.getValue(),
						ContentType.create("application/zip", Consts.ISO_8859_1), entry.getValue().getName());
			}
		}
		return builder.build();
	}

}
