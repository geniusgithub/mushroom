package com.mobile.yunyou.network.api;


public abstract class AbstractTaskCallback {

	
	private AbstractFileDownTask mFileDownTask;
	
	public AbstractTaskCallback callback;
	
	
	public AbstractTaskCallback(){
		
	}
	
	public void setTask(AbstractFileDownTask fileDownTask){
		mFileDownTask = fileDownTask;
	}
	
	public String getSaveUri(){
		if (mFileDownTask != null){
			return mFileDownTask.saveUri;
		}
		
		return null;
	}
	
	public String getRequestUri(){
		if (mFileDownTask != null){
			return mFileDownTask.requestUrl;
		}
		
		return null;
	}
	
	public boolean isSuccess(){
		if (mFileDownTask != null){
			return mFileDownTask.isDownloadSuccess;
		}
		
		return false;
	}
	
	public  abstract void downLoadComplete(final boolean isSuccess);
}
