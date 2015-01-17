package com.mobile.yunyou.network.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.FileHelper;
import com.mobile.yunyou.util.LogFactory;

public abstract class AbstractFileDownTask implements Runnable{
	
	protected static final CommonLog log = LogFactory.createLog();
	
	private final static int CONNECT_TIME_OUT = 5000;
	
	public String requesetMethod = "GET";	
	public String requestUrl; 							
	public String saveUri; 		
	public int responsCode = 0;
	public boolean isDownloadSuccess = false;
	public AbstractTaskCallback callback;
	
	public AbstractFileDownTask(String requestUrl, String saveUri, AbstractTaskCallback callback){
		this.requestUrl = requestUrl;
		this.saveUri = saveUri;
		this.callback = callback;
	}
	
	protected abstract int getRequestCount();
	
	@Override
	public void run() {
		log.e("AbstractFileDownTask run");
		boolean isParamValid = isParamValid();
		boolean ret = false;
		int reRequestCount = getRequestCount();
		if(isParamValid){		
			int count = 0;
			while(true){
				ret = request();
				if (ret || count > reRequestCount){
				
					break;
				}
				count++;
				log.d("request fail,now count = " + count);
			}
		}else{
			log.d("isParamValid = false!!!");
		}
	
		if (callback != null){
			callback.setTask(this);
			callback.downLoadComplete(ret);
		}
	}
	
	private boolean request(){
		
		try {
			URL url = new URL(requestUrl);
		//	URL url = new URL("http://www.360lbs.net/avatar/test.jpg");
			
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod(requesetMethod);
			conn.setRequestProperty("Connection", "Keep-Alive"); 	
			conn.setConnectTimeout(CONNECT_TIME_OUT); 
			responsCode = conn.getResponseCode();
			if (responsCode != 200){
				log.d("responsCode = " + responsCode + ", so Fail!!!");
				return false;
			}
		
			InputStream inputStream = conn.getInputStream();
			isDownloadSuccess  = FileHelper.writeFile(saveUri, inputStream);
			log.e("fileDownLoadTask writefile ret = " + isDownloadSuccess + 
					"\nrequestUri = " + requestUrl + 
					"\nsaveUri = " + saveUri);

			inputStream.close();
			return isDownloadSuccess;
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			log.e("catch MalformedURLException e = " + e.getMessage());
		}catch (IOException e) {
			e.printStackTrace();
			log.e("catch IOException e = " + e.getMessage());
		}
	
		
		return false;
	}

	protected boolean isParamValid(){
		if (requestUrl == null || saveUri == null){
			return false;
		}
		
		return true;
	}
}
