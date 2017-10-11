package com.mobile.yunyou;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

public class testweibo {
	 private static final CommonLog log = LogFactory.createLog();
//	public static void test1() throws ClientProtocolException, IOException {
//		HttpPost httpPost = new HttpPost("http://www.360lbs.net:8080"); 
//		httpPost.setHeader("Connection", "keep-alive"); 
//		MultipartEntity mutiEntity = new MultipartEntity();
//		File file = new File("C:/Users/libo/Desktop/jpg/x.jpg");
//		mutiEntity
//				.addPart(
//						"json",
//						new StringBody(
//								"{\"data\":{},\"headers\":{\"ua\":{\"model\":\"Lenovo K910e\",\"manufacturer\":\"LENOVO\",\"client_platform\":\"ANDROID\",\"os_version\":\"4.4.2\",\"client_version\":\"1.2.0\"},\"lang\":\"zh_ch\"},\"did\":\"0\",\"sid\":\"A128966000007112\",\"cmd\":\"deviceset_avatar\"}",
//								Charset.forName("utf-8")));
//		mutiEntity.addPart("file", new FileBody(file));
//
//		httpPost.setEntity(mutiEntity);
//		HttpClient httpClient = new DefaultHttpClient();
//		HttpResponse httpResponse = httpClient.execute(httpPost);
//		HttpEntity httpEntity = httpResponse.getEntity();
//		String content = EntityUtils.toString(httpEntity);
//		System.out.println(content);
//	}

	public static void test(String filePath) throws IOException {
		URL url = new URL("http://www.360lbs.net:8080");
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.setConnectTimeout(8000);
		request.setDoOutput(true);
		request.setRequestMethod("POST");
		String json = "{\"data\":{},\"headers\":{\"ua\":{\"model\":\"Lenovo K910e\",\"manufacturer\":\"LENOVO\",\"client_platform\":\"ANDROID\",\"os_version\":\"4.4.2\",\"client_version\":\"1.2.0\"},\"lang\":\"zh_ch\"},\"did\":\"0\",\"sid\":\"A128966000007112\",\"cmd\":\"deviceset_avatar\"}";
		String boundary = "---------------------------37531613912423";
		String content = "--" + boundary
				+ "\r\nContent-Disposition: form-data; name=\"json\"\r\n\r\n";
		String pic = "\r\n--"
				+ boundary
				+ "\r\nContent-Disposition: form-data; name=\"file\"; filename=\"postpic.jpg\"\r\nContent-Type: image/jpeg\r\n\r\n";
		byte[] end_data = ("\r\n--" + boundary + "--\r\n").getBytes();

		File f = new File(filePath);

		FileInputStream stream = new FileInputStream(f);
		byte[] file = new byte[(int) f.length()];
		stream.read(file);
		request.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + boundary);
		request.setRequestProperty(
				"Content-Length",
				String.valueOf(content.getBytes().length
						+ json.getBytes().length + pic.getBytes().length
						+ f.length() + end_data.length));
		try {
			request.connect();
			OutputStream ot = request.getOutputStream();
			ot.write(content.getBytes());
			ot.write(json.getBytes());
			ot.write(pic.getBytes());
			ot.write(file);
			ot.write(end_data);
			ot.flush();
			ot.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(request.getResponseCode());
		System.out.println(request.getResponseMessage());

	}
	
	public static void uploadFile(String filePath) throws IOException {
		URL url = new URL("http://www.360lbs.net:8080");

		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.setConnectTimeout(8000);
		request.setUseCaches(false); // 不允许使用缓存
		request.setReadTimeout(10 * 1000);
		request.setDoOutput(true);
		request.setRequestMethod("POST");
		String json = "{\"data\":{},\"headers\":{\"ua\":{\"model\":\"Lenovo K910e\",\"manufacturer\":\"LENOVO\",\"client_platform\":\"ANDROID\",\"os_version\":\"4.4.2\",\"client_version\":\"1.2.0\"},\"lang\":\"zh_ch\"},\"did\":\"0\",\"sid\":\"A128966000007112\",\"cmd\":\"deviceset_avatar\"}";
		//String boundary = "---------------------------37531613912423";
		String boundary = UUID.randomUUID().toString();
		 
		String content = "--" + boundary
				+ "\r\nContent-Disposition: form-data; name=\"json\"\r\n\r\n";
		String pic = "\r\n--"
			+ boundary
				+ "\r\nContent-Disposition: form-data; name=\"file\"; filename=\"postpic.jpg\"\r\nContent-Type: image/jpeg\r\n\r\n";
		byte[] end_data = ("\r\n--" + boundary + "--\r\n").getBytes();

		File f = new File(filePath);

		FileInputStream stream = new FileInputStream(f);
		byte[] file = new byte[(int) f.length()];
		stream.read(file);
		request.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + boundary);
//		request.setRequestProperty(
//				"Content-Length",
//				String.valueOf(content.getBytes().length
//						+ json.getBytes().length + pic.getBytes().length
//						+ f.length() + end_data.length));
		try {
			request.connect();
			OutputStream ot = request.getOutputStream();
			ot.write(content.getBytes());
			log.e("content = \n" + content);
			ot.write(json.getBytes());
			log.e("json = \n" + json);
			ot.write(pic.getBytes());
			log.e("pic = \n" + pic);
			ot.write(file);
			ot.write(end_data);
			log.e("end_data = \n" + new String(end_data));
			ot.flush();
			ot.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.e(request.getResponseCode());
		log.e(request.getResponseMessage());

	}

//	public static void main(String[] args) throws IOException {
//		test1();
//
//	}
}
