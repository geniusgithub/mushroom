package com.mobile.yunyou.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mobile.yunyou.model.DeviceSetType.GpsStillTime;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

import android.R.integer;
import android.util.Log;


//0x0300 - 0x0399
public class ProductType {

	private static final CommonLog log = LogFactory.createLog();
	
	public final static String KEY_ARRAY = "array";
	
	// 获取套餐
	public final static int PRODUCT_GET_PACKET_MASID = 0x0300;
	public static class GetPackage  implements IParseString
	{
		public final static String KEY_ID = "id";
		public final static String KEY_NAME = "name";
		public final static String KEY_PRICE = "price";
		public final static String KEY_DESC = "desc";
		public final static String KEY_DETAIL = "detail";
		public final static String KEY_VALIDTIME = "valid_time";
		
		public int mID = 0;
		public String mName = "";
		public double mPrice = 0;
		public String mDesc = "";
		public String mDetail = "";
		public int mValidTime = 0;
		
		
		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub

			JSONObject jsonObject = new JSONObject(jsonString);
			mID = jsonObject.getInt(KEY_ID);
			mName = jsonObject.getString(KEY_NAME);
			mPrice = jsonObject.getDouble(KEY_PRICE);
			mDesc = jsonObject.getString(KEY_DESC);
			mDetail = jsonObject.getString(KEY_DETAIL);
			mValidTime = jsonObject.getInt(KEY_VALIDTIME);
			
			return true;
		}
		
	}
	
	public static class GetPackageGroup  implements IParseString
	{
		
		public List<GetPackage> mGetPacageList = new ArrayList<GetPackage>();

		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub
		
			JSONObject jsonObject = new JSONObject(jsonString);		
			log.e("GetPackageGroup = " + jsonString);
			JSONArray jsonArray = jsonObject.getJSONArray(KEY_ARRAY);
			log.e("gogogo = " + jsonString);
			int size = jsonArray.length();
			for(int i = 0; i < size; i++)
			{
				GetPackage object = new GetPackage();
				
				try {
					object.parseString(jsonArray.getJSONObject(i).toString());
					mGetPacageList.add(object);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();				
				}
			}
			
			return true;
			
		}
		
	}
	
	// 创建订单
	public final static int PRODUCT_CREATE_ORDER_MASID = 0x0301;
	public static class CreateOrder  implements IToJsonObject
	{

		public final static String KEY_PACKAGEID = "package_id";
		public final static String KEY_QUANTITY = "quantity";
		public final static String KEY_PAYVIA = "pay_via";
		public final static String KEY_PRICE = "price";
		public final static String KEY_TOTALFEE = "total_fee";
		public final static String KEY_DID = "did";
		
		public int mPackageId = 0;
		public int mQuantity = 1;
		public String mPayVia = "alipay";
		public double mPrice = 0;
		public double mTotalFee = 0;
		public String mDid = "";
		
		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_PACKAGEID, mPackageId);
			jsonObject.put(KEY_QUANTITY, mQuantity);
			jsonObject.put(KEY_PAYVIA, mPayVia);
			jsonObject.put(KEY_PRICE, mPrice);
			jsonObject.put(KEY_TOTALFEE, mTotalFee);
			jsonObject.put(KEY_DID, mDid);
			return jsonObject;
		}
		
	}
	
	public static class CreateOrderResult  implements IParseString
	{
		public final static String KEY_ORDERID = "order_id";
		
		public String mOrderID = "";
		
		@Override
		public boolean parseString(String jsString) throws Exception {
			// TODO Auto-generated method stub
			JSONObject jsonObject = new JSONObject(jsString);
			mOrderID = jsonObject.getString(KEY_ORDERID);
			return true;
		}

		
		
	}
	

	// 购买套餐成功回调通知
	public final static int PRODUCT_BUY_NOTIFY_MASID = 0x0302;
	public static class BuyNotifyReturn  implements IToJsonObject
	{

		public final static String KEY_ORDER_ID = "order_id";
		public final static String KEY_PAYMETHOD = "pay_method";
		public final static String KEY_TRADENO = "trade_no";
		public final static String KEY_EMAIL = "buyer_email";
		public final static String KEY_BUYERID = "buyer_id";
		public final static String KEY_TOTALFEE= "total_fee";
		
		public String mOrderID = "";
		public String mPayMethod = "";
		public String mTradeNo = "";
		public String mEmail = "";
		public String mBuyerID = "";
		public double mTotalFee = 0;
		
		
		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_ORDER_ID, mOrderID);
			jsonObject.put(KEY_PAYMETHOD, mPayMethod);
			jsonObject.put(KEY_TRADENO, mTradeNo);
			jsonObject.put(KEY_EMAIL,  mEmail);
			jsonObject.put(KEY_BUYERID, mBuyerID);
			jsonObject.put(KEY_TOTALFEE, mTotalFee);
			return jsonObject;
		}
		
	}
	public final static int PRODUCT_BUY_RETURN_MASID = 0x0303;
	


	// 购买套餐记录
	public final static int PRODUCT_GET_PACKAGE_HISTORY_MASID = 0x0304;
	public static class PackageHistory  implements IToJsonObject
	{

		public final static String KEY_TYPE = "type";
		public final static String KEY_OFFSET = "offset";
		public final static String KEY_NUM = "num";
	
		
		public int mType= 0;
		public int mOffset = 0;
		public int mNum = 0;
	
		
		
		@Override
		public JSONObject toJsonObject() throws JSONException {
			// TODO Auto-generated method stub
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(KEY_TYPE, mType);
			jsonObject.put(KEY_OFFSET, mOffset);
			jsonObject.put(KEY_NUM, mNum);
			return jsonObject;
		}
		
	}
	
	
	public static class PackageHistoryResult  implements IParseString
	{

		public final static String KEY_BUYTIME = "buy_time";
		public final static String KEY_ACTIVETIME = "active_time";
		public final static String KEY_PACKAGENAME = "package_name";
		public final static String KEY_PRICE = "price";
		public final static String KEY_TILLTIME = "till_time";
		
		
		public String mBuyTime = "";
		public String mActiveTime = "";
		public String mPackageName = "";
		public double mPrice = 0;
		public String mTillTime = "";
		@Override
		public boolean parseString(String jString) throws Exception {
			// TODO Auto-generated method stub

			JSONObject jsonObject = new JSONObject(jString);
			mBuyTime = jsonObject.getString(KEY_BUYTIME);
			mActiveTime = jsonObject.getString(KEY_ACTIVETIME);
			mPackageName = jsonObject.getString(KEY_PACKAGENAME);
			mPrice = jsonObject.getDouble(KEY_PRICE);
			mTillTime = jsonObject.getString(KEY_TILLTIME);
			
			return true;
		}

		
	
		
	}
	
	public static class PackageHistoryResultGrounp  implements IParseString
	{
		public final static String KEY_PACKAGES = "packages";
		
		public List<PackageHistoryResult> mGetPacageList = new ArrayList<PackageHistoryResult>();

		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub
		
			JSONObject jsonObject = new JSONObject(jsonString);			
			JSONArray jsonArray = jsonObject.getJSONArray(KEY_PACKAGES);
			int size = jsonArray.length();
			for(int i = 0; i < size; i++)
			{
				PackageHistoryResult object = new PackageHistoryResult();
				
				try {
					object.parseString(jsonArray.getJSONObject(i).toString());
					mGetPacageList.add(object);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			
			return true;
			
		}
	}


	
	
}
