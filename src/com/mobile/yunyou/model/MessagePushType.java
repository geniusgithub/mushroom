package com.mobile.yunyou.model;

import org.json.JSONObject;

import android.os.Parcelable;

import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;



//0x0200 - 0x0299
public class MessagePushType {

	private static final CommonLog log = LogFactory.createLog();
	
	
	//{"cmd":" messagepush_msg " ,"did":"123321123",
	//”data”:{“type”:” permit/forbid/signed/unsigned/lowpower/sos/remind”,”content”,”您的小孩。。。”,”time”:”yyyy-MM-dd HH:mm:ss” ,”badge”:int}}
	
	
	// 告警消息
	public final static int MSGPUSH_WARN_MASID = 0x0200;
	public static class DeviceWarnMsg implements  IParseString
	{
		private final static String KEY_DID = "did";
		private final static String KEY_DATA = "data";
		
		private final static String KEY_TYPE = "type";
		private final static String KEY_CONTENT = "content";
		private final static String KEY_TIME = "time";
		private final static String KEY_BADGE = "badge";
		
		public String mDid = "";
		public String mType = "";
		public String mContent = "";		
		public String mTime = "";	
		public int mBadge = 0;

		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub

			JSONObject jsonObject = new JSONObject(jsonString);		
			mDid = jsonObject.getString(KEY_DID);	
			
			JSONObject dataJsonObject = jsonObject.getJSONObject(KEY_DATA);	
			
			mType = dataJsonObject.getString(KEY_TYPE);				
			mContent = dataJsonObject.getString(KEY_CONTENT);					
			mTime = dataJsonObject.getString(KEY_TIME);	
			mBadge = dataJsonObject.optInt(KEY_BADGE);
			
			return true;
		}
		
	}
	
	
	//{"data":{"value":1},"did":"A000000012000079","cmd":"messagepush_terminalstatus"}
	
	
	// 终端在线消息
	public static class TerminalStatus implements  IParseString
	{
		private final static String KEY_DID = "did";
		private final static String KEY_DATA = "data";
		private final static String KEY_VALUE = "value";
		
		public String mDid = "";	
		public int    mValue = 0;
		
		@Override
		public boolean parseString(String jsonString) throws Exception {
			// TODO Auto-generated method stub

			JSONObject jsonObject = new JSONObject(jsonString);		
			mDid = jsonObject.getString(KEY_DID);	
			
			JSONObject dataJsonObject = jsonObject.getJSONObject(KEY_DATA);	
			
			mValue = dataJsonObject.getInt(KEY_VALUE);				

			
			return true;
		}
		
		
	}
}
