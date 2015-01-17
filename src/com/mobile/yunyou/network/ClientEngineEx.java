package com.mobile.yunyou.network;


import org.json.JSONException;
import org.json.JSONObject;

import com.huizhilian.android.Caller;
import com.huizhilian.android.MessagePushHandler;
import com.huizhilian.android.Receiver;
import com.mobile.yunyou.YunyouApplication;
import com.mobile.yunyou.model.IToJsonObject;
import com.mobile.yunyou.model.PublicType;
import com.mobile.yunyou.model.ResponseDataPacket;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;
import com.mobile.yunyou.util.Utils;

public class ClientEngineEx {
	
	
	private static final CommonLog log = LogFactory.createLog();
	
	Caller cft = Caller.getInstance();
	private JSONObject headers = new JSONObject();
	private String did = "";
	private String sid = "";	
	
	private RequestCMDFactory mCmdFactory;
	
	private Receiver mReceiver;
	
	
	public ClientEngineEx()
	{	
		headers = getDeviceInfo();
		
		mReceiver = new Receiver(Caller.getInstance());
		mReceiver.start();	
		
		mCmdFactory = RequestCMDFactory.getInstance();
		
	}

	public void setSid(String sid)
	{
		this.sid = sid;
	}
	
	public void setDid(String did)
	{
		this.did = did;
	}
	
	public void setMessagePushHandler(MessagePushHandler handler)
	{
		mReceiver.setHandler(handler);
	}

	
	
	public ResponseDataPacket Request(int action, IToJsonObject object) throws Exception
	{
		JSONObject jsonObject = null;
		String cmd = mCmdFactory.getCMDString(action);
		if (cmd == null)
		{
			log.e("can't find the cmd!!!");
			return null;
		}else{
			log.e("Request cmd = " + cmd + ", action = " + Utils.toHexString(action));
		}
		
		JSONObject requestObject = getInitJsonObject();
		requestObject.put("cmd", cmd);
		if (object!= null)
		{
			requestObject.put("data", object.toJsonObject());
		}else{
			requestObject.put("data",new JSONObject());
		}
		


		
		if (action == PublicType.USER_HEART_MASID)
		{
			jsonObject = cft.udpRequestForRep(requestObject);
		}else{
			
			try {
				jsonObject = cft.sendRequest(requestObject);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				log.e("cft.sendRequest catch Exception!!!");
			}
		
		}
		
		
		
		if (jsonObject == null)
		{
			log.e("mRequestEngine.doRequest fail!!!");
			return null;
		}

		ResponseDataPacket responseDataPacket = new ResponseDataPacket();
		responseDataPacket.parseJson(jsonObject);	

		return responseDataPacket;
	}
	
	public ResponseDataPacket Request(int action, String did, IToJsonObject object) throws Exception
	{
		JSONObject jsonObject = null;
		String cmd = mCmdFactory.getCMDString(action);
		if (cmd == null)
		{
			log.e("can't find the cmd!!!");
			return null;
		}else{
			log.e("Request cmd = " + cmd + ", action = " + Utils.toHexString(action));
		}
		
		JSONObject requestObject = getInitJsonObject(did);
		requestObject.put("cmd", cmd);
		if (object!= null)
		{
			requestObject.put("data", object.toJsonObject());
		}else{
			requestObject.put("data",new JSONObject());
		}
		
		if (action == PublicType.USER_HEART_MASID)
		{
			jsonObject = cft.udpRequestForRep(requestObject);
		}else{
			
			try {
				jsonObject = cft.sendRequest(requestObject);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				log.e("cft.sendRequest catch Exception!!!");
			}
		
		}
		
		
		
		if (jsonObject == null)
		{
			log.e("mRequestEngine.doRequest fail!!!");
			return null;
		}

		ResponseDataPacket responseDataPacket = new ResponseDataPacket();
		responseDataPacket.parseJson(jsonObject);	

		return responseDataPacket;
	}
	
	
	private JSONObject getInitJsonObject() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("headers", headers);
		json.put("sid", sid);
		json.put("did", did);
		return json;
	}
	
	private JSONObject getInitJsonObject(String DID) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("headers", headers);
		json.put("sid", sid);
		json.put("did", DID);
		return json;
	}
	
	private  String LANG = "zh_ch";
	private  String CLIENT_PLATFORM = "ANDROID";
	private  String CLIENT_VERSION = "";
	private  String OS_VERSION = "";
	private  String MANUFACTURER = "";
	private  String MODEL = "";
	private JSONObject getDeviceInfo()
	{
		try {
			CLIENT_VERSION = Utils.getPackageVersionName(YunyouApplication.getInstance());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OS_VERSION = Utils.getOSVersion();
		MANUFACTURER = Utils.getDeviceManufacturer();
		MODEL = Utils.getDeviceModel();
		
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("lang", LANG);
			JSONObject ua = new JSONObject();
			ua.put("client_platform", CLIENT_PLATFORM);
			ua.put("client_version", CLIENT_VERSION);
			ua.put("os_version", OS_VERSION);
			ua.put("manufacturer", MANUFACTURER);
			ua.put("model", MODEL);
			jsonObject.put("ua", ua);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObject;
		
	}

}
