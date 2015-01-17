package com.mobile.yunyou.set;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;

import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.model.ProductType;
import com.mobile.yunyou.network.NetworkCenterEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

public class ProductMealManager {
	
	private static final CommonLog log = LogFactory.createLog();
	
    private NetworkCenterEx mNetworkCenterEx;
    
    private static ProductMealManager mInstance;
	
	private Context mContext;
	
	private List<ProductType.GetPackage> mPackageList = new ArrayList<ProductType.GetPackage>();


	public synchronized static ProductMealManager getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new ProductMealManager(YunyouApplication.getInstance());
		}
		
		return mInstance;
	}
	
	private ProductMealManager(Context context)
	{
		mContext = context;

		mNetworkCenterEx = NetworkCenterEx.getInstance();		
	}
	
	public void setPackageList( List<ProductType.GetPackage> list){
		mPackageList = list;
	}
	
	public List<ProductType.GetPackage>  getPackageList(){
		return mPackageList;
	}

	

}
