package com.mobile.yunyou.util;

import java.io.File;

import android.content.Context;

public class FileCache extends AbstractFileCache{

	public FileCache(Context context) {
		super(context);
	
	}


	@Override
	public String getSavePath(String url) {
		return FileManager.getSavePath(url);
	}

	@Override
	public String getCacheDir() {
		
		return FileManager.getSaveFilePath();
	}

}
