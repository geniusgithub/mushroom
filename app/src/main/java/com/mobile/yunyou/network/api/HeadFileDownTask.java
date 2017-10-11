package com.mobile.yunyou.network.api;


public class HeadFileDownTask extends AbstractFileDownTask{

	private final static int REQUEST_COUNT = 3;
	
	public HeadFileDownTask(String requestUrl, String saveUri,
			AbstractTaskCallback callback) {
		super(requestUrl, saveUri, callback);

	}

	@Override
	protected int getRequestCount() {
	
		return REQUEST_COUNT;
	}

}
