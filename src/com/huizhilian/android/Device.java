//package com.huizhilian.android;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.LogFactory;
//
//import android.util.Log;
//
//
//public class Device {
//	Caller cft = Caller.getInstance();
//	private JSONObject headers;
//	private JSONObject json;
//	private String did = "";
//	private String sid = "";
//
//	private static final CommonLog log = LogFactory.createLog();
//	
//	/**
//	 * 此构造函数中需要获取操作系统信息，此处用默认值
//	 */
//	public Device(JSONObject headJsonObject) {
//		headers = headJsonObject;
//	}
//	
//	public void setSid(String sid)
//	{
//		this.sid = sid;
//	}
//	
//	public void setDid(String did)
//	{
//		this.did = did;
//	}
//
//
//	private void init() throws JSONException {
//		json = new JSONObject();
//		json.put("headers", headers);
//		json.put("sid", sid);
//		json.put("did", did);
//	}
//	
//
//	/**
//	 * 按键设置，所有参数按照key1,phoneNumber1,name1，key2,phoneNumber2,name2...的顺序传入
//	 * 
//	 * @param params
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject keyset(String... params) throws Exception {
//		init();
//		if (params.length % 3 != 0)
//			throw new ParamsException(
//					"参数应该按照key1,phoneNumber1,name1，key2,phoneNumber2,name2...的顺序传入");
//		json.put("cmd", "deviceset_key");
//		JSONObject data = new JSONObject();
//		JSONArray array = new JSONArray();
//		for (int i = 0; i < params.length; i += 3) {
//			JSONObject obj = new JSONObject();
//			obj.put("key", params[i]);
//			obj.put("phoneNumber", params[i + 1]);
//			obj.put("name", params[i + 2]);
//			array.put(obj);
//		}
//		data.put("array", array);
//		json.put("data", data);
//
//
//		return cft.sendRequest(json);
//	}
//
//	public JSONObject getKeyset() throws Exception {
//		init();
//		json.put("cmd", "deviceget_key");
//		return cft.sendRequest(json);
//	}
//
//	public JSONObject calltime(int value) throws Exception {
//		init();
//		json.put("cmd", "deviceset_calltime");
//
//		JSONObject obj = new JSONObject();
//		obj.put("value", value);
//		json.put("data", obj);
//		return cft.sendRequest(json);
//	}
//
//	public JSONObject getCalltime() throws Exception {
//		init();
//		json.put("cmd", "deviceget_calltime");
//		return cft.sendRequest(json);
//	}
//
//	/**
//	 * 白名单，参数按照phoneNumber1，name1，phoneNumber2,name2...顺序传入
//	 * 
//	 * @param params
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject whitelistset(String... params) throws Exception {
//		init();
//		if ( params.length % 2 != 0)
//			throw new ParamsException(
//					"参数按照phoneNu mber1，name1，phoneNumber2,name2...顺序传入");
//		json.put("cmd", "deviceset_whitelist");
//		JSONObject data = new JSONObject();
//		JSONArray array = new JSONArray();
//		for (int i = 0; i < params.length; i += 2) {
//			JSONObject obj = new JSONObject();
//			obj.put("phoneNumber", params[i]);
//			obj.put("name", params[i + 1]);
//			array.put(obj);
//		}
//		data.put("array", array);
//		json.put("data", data);
//
//		return cft.sendRequest(json);
//	}
//
//	public JSONObject getClock() throws Exception {
//		init();
//		json.put("cmd", "deviceget_clock");
//		return cft.sendRequest(json);
//	}
//
//	/**
//	 * 
//	 * 
//	 * @param params
//	 *            闹钟设置，参数值顺序为time(HHmm) cycle(int 0 or 1) wk(字符串，都好分割1-7) on(int
//	 *            0 or 1)
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject clockset(Object... params) throws Exception {
//		init();
//		if (params.length % 5 != 0)
//			throw new ParamsException(
//					"参数按照time(HHmm) cycle(int 0 or 1) wk(字符串，逗号分割1-7) on(int 0 or 1)...顺序传入");
//		json.put("cmd", "deviceset_clock");
//		JSONObject data = new JSONObject();
//		JSONArray array = new JSONArray();
//		for (int i = 0; i < params.length; i += 5) {
//			JSONObject obj = new JSONObject();
//			obj.put("time", params[i]);
//			obj.put("cycle", params[i + 1]);
//			
//			String wkString = (String) params[i + 2];	
//			log.e("wkString = " + wkString);
//			//wkString = wkString.substring(1, wkString.length() - 1);
////			log.e("wkString = " + wkString);
////			JSONArray arr = new JSONArray();
////			if (wkString.length() != 0)
////			{
////				String[] wk = wkString.split(",");
////				for (String day : wk) {					
////					arr.put(day);
////				}
////			}
//			
//			obj.put("on", params[i + 3]);
//			obj.put("wk", wkString);
//			obj.put("name", params[i + 4]);
//			array.put(obj);
//		}
//		data.put("array", array);
//		json.put("data", data);
//
//		return cft.sendRequest(json);
//	}
//
//	public JSONObject getWhitelist() throws Exception {
//		init();
//		json.put("cmd", "deviceget_whitelist");
//		return cft.sendRequest(json);
//	}
//
//	public JSONObject scenemode(String value) throws Exception {
//		init();
//		json.put("cmd", "deviceset_scenemode");
//		JSONObject obj = new JSONObject();
//		obj.put("value", value);
//		json.put("data", obj);
//		return cft.sendRequest(json);
//	}
//
//	public JSONObject getSceneMode() throws Exception {
//		init();
//		json.put("cmd", "deviceget_scenemode");
//		return cft.sendRequest(json);
//	}
//	
//	public JSONObject phoneCallMode(String value) throws Exception {
//		init();
//		json.put("cmd", "deviceset_callmodel");
//		JSONObject obj = new JSONObject();
//		obj.put("value", value);
//		json.put("data", obj);
//		return cft.sendRequest(json);
//	}
//
//	public JSONObject getPhoneCallMode() throws Exception {
//		init();
//		json.put("cmd", "deviceget_callmodel");
//		return cft.sendRequest(json);
//	}
//
//	public JSONObject powerset(String value) throws Exception {
//		init();
//		json.put("cmd", "deviceset_power");
//		JSONObject obj = new JSONObject();
//		obj.put("value", value);
//		json.put("data", obj);
//		return cft.sendRequest(json);
//	}
//
//	public JSONObject getPowerstatus() throws Exception {
//		init();
//		json.put("cmd", "deviceget_power");
//		return cft.sendRequest(json);
//	}
//
//	public JSONObject lowpower(int value) throws Exception {
//		init();
//		json.put("cmd", "deviceset_lowpower");
//
//		JSONObject obj = new JSONObject();
//		obj.put("value", value);
//		json.put("data", obj);
//		return cft.sendRequest(json);
//	}
//
//	public JSONObject getLowpower() throws Exception {
//		init();
//		json.put("cmd", "deviceget_lowpower");
//		return cft.sendRequest(json);
//	}
//
//	public JSONObject gpsinterval(int value) throws Exception {
//		init();
//		json.put("cmd", "deviceset_gpsuploadinterval");
//
//		JSONObject obj = new JSONObject();
//		obj.put("value", value);
//		json.put("data", obj);
//		return cft.sendRequest(json);
//	}
//
//	public JSONObject getGpsinterval() throws Exception {
//		init();
//		json.put("cmd", "deviceget_gpsuploadinterval");
//		return cft.sendRequest(json);
//	}
//
//	public JSONObject getGpstimetable() throws Exception {
//		init();
//		json.put("cmd", "deviceget_gpsuploadtimetable");
//		return cft.sendRequest(json);
//	}
//
//	/**
//	 * 
//	 * 
//	 * @param params
//	 *            GPS上报时间设置，参数值顺序为 wk(字符串，都好分割1-7) starttime(HHmm) endtime(HHmm)
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject gpstimetable(Object... params) throws Exception {
//		init();
//		if (params.length % 3 != 0)
//			throw new ParamsException(
//					"参数按照wk(字符串，都好分割1-7) starttime(HHmm) endtime(HHmm)...顺序传入");
//		json.put("cmd", "deviceset_gpsuploadtimetable");
//		JSONObject data = new JSONObject();
//		JSONArray array = new JSONArray();
//		for (int i = 0; i < params.length; i += 3) {
//			JSONObject obj = new JSONObject();
//			
//			String wkString = (String) params[i];			
//			wkString = wkString.substring(1, wkString.length() - 1);
//			JSONArray arr = new JSONArray();
//			if (wkString.length() != 0)
//			{
//				String[] wk = wkString.split(",");
//				for (String day : wk) {
//					arr.put(Integer.parseInt(day));
//				}
//			}
//			obj.put("wk", arr);
//			obj.put("startTime", params[i + 1]);
//			obj.put("endTime", params[i + 2]);
//			array.put(obj);
//		}
//		data.put("array", array);
//		json.put("data", data);
//
//		return cft.sendRequest(json);
//	}
//
//	public JSONObject getArea(String type) throws Exception {
//		init();
//		json.put("cmd", "deviceget_area");
//		JSONObject obj = new JSONObject();
//		obj.put("type", type);
//		json.put("data", obj);
//		return cft.sendRequest(json);
//	}
//
//	/**
//	 * 
//	 * 
//	 * @param params
//	 *            GPS上报时间设置，参数值顺序为type,lat,lon,radius,name
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject addarea(Object... params) throws Exception {
//		init();
//		if (params.length == 0 || params.length % 5 != 0)
//			throw new ParamsException("参数按照type,lat,lon,radius,name...顺序传入");
//		json.put("cmd", "deviceset_addarea");
//		JSONObject data = new JSONObject();
//		JSONArray array = new JSONArray();
//		for (int i = 0; i < params.length; i += 5) {
//			JSONObject obj = new JSONObject();
//			obj.put("type", params[i]);
//			obj.put("lat", params[i + 1]);
//			obj.put("lon", params[i + 2]);
//			obj.put("radius", params[i + 3]);
//			obj.put("name", params[i + 4]);
//			array.put(obj);
//		}
//		data.put("array", array);
//		json.put("data", data);
//
//		return cft.sendRequest(json);
//	}
//
//	/**
//	 * 
//	 * 
//	 * @param params
//	 *            GPS上报时间设置，参数值顺序为id,type,lat,lon,radius,name
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject modifyArea(Object... params) throws Exception {
//		init();
//		if (params.length == 0 || params.length % 6 != 0)
//			throw new ParamsException("参数按照id,type,lat,lon,radius,name...顺序传入");
//		json.put("cmd", "deviceset_modifyarea");
//		JSONObject data = new JSONObject();
//		JSONArray array = new JSONArray();
//		for (int i = 0; i < params.length; i += 6) {
//			JSONObject obj = new JSONObject();
//			obj.put("id", params[i]);
//			obj.put("type", params[i + 1]);
//			obj.put("lat", params[i + 2]);
//			obj.put("lon", params[i + 3]);
//			obj.put("radius", params[i + 4]);
//			obj.put("name", params[i + 5]);
//			array.put(obj);
//		}
//		data.put("array", array);
//		json.put("data", data);
//
//		return cft.sendRequest(json);
//	}
//
//	public JSONObject delArea(int id) throws Exception {
//		init();
//		json.put("cmd", "deviceset_delarea");
//		JSONObject obj = new JSONObject();
//		obj.put("id", id);
//		json.put("data", obj);
//		return cft.sendRequest(json);
//	}
//	
//	
//	/**
//	 * 
//	 * 
//	 * @param params
//	 *             远程监听  value值为电话号码
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject remoteMonitor(String value) throws Exception {
//		init();
//		json.put("cmd", "deviceset_monitor");
//
//		JSONObject obj = new JSONObject();
//		obj.put("value", value);
//		json.put("data", obj);
//		return cft.sendRequest(json);
//	}
//	
//	
//	/**
//	 * 
//	 * 
//	 * @param params
//	 *             获取终端设备GPS
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject locationGet() throws Exception {
//		init();
//		json.put("cmd", "locationget_instant");
//
//		JSONObject obj = new JSONObject();
//		json.put("data", obj);
//		return cft.sendRequest(json);
//	}
//	
//	/**
//	 * 
//	 * 
//	 * @param params
//	 *             获取终端在线状态
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject getOnlineStatus() throws Exception {
//		init();
//		json.put("cmd", "deviceget_online");
//
//		JSONObject obj = new JSONObject();
//		json.put("data", obj);
//		return cft.sendRequest(json);
//	}
//
//	/**
//	 * 
//	 * 
//	 * @param params
//	 *             获取终端消息
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject getDeviceMsg(int offset, int num) throws Exception {
//		init();
//		json.put("cmd", "user_getalertmessage");
//
//		JSONObject obj = new JSONObject();
//		obj.put("offset", offset);
//		obj.put("num", num);
//		
//		json.put("data", obj);
//		return cft.sendRequest(json);
//	}
//	
//	/**
//	 * 
//	 * 
//	 * @param params
//	 *             获取未读消息数
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject getUnreadMsg() throws Exception {
//		init();
//		json.put("cmd", "user_getunreadalertmessage");
//
//		JSONObject obj = new JSONObject();
//		json.put("data", obj);
//		return cft.sendRequest(json);
//	}
//}
