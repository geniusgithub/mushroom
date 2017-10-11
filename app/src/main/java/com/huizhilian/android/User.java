//package com.huizhilian.android;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.mobile.yunyou.model.DeviceSetType;
//import com.mobile.yunyou.model.PublicType;
//
//import android.util.Log;
//
//public class User {
//	Caller cft = Caller.getInstance();
//
//	private JSONObject headers = new JSONObject();
//	private JSONObject json = new JSONObject();
//	private String sid = "";
//	private String did = "";
// 
//	/**
//	 * 此构造函数中需要获取操作系统信息，此处用默认值
//	 * @throws JSONException 
//	 */
//	public User(JSONObject headJsonObject) throws JSONException {
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
//	public void setHeadersJsonObject(JSONObject jsonObject)
//	{
//		headers = jsonObject;
//	}
//	
//	private void init() throws JSONException {		
//		json = new JSONObject();
//		json.put("headers", headers);
//		json.put("sid", sid);
//		json.put("did", did);
//		
//	}
//	
//	/**
//	 * 
//	 * @param username 
//	 * @param password
//	 * @param phoneNum
//	 * @param email
//	 * @param trueName
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject register(String username, String password,
//			String phoneNum, String email, String trueName) throws Exception {
//		init();
//		json.put("cmd", "user_register");
//		JSONObject data = new JSONObject();
//		data.put("type", "A");
//		data.put("company", "");
//		data.put("username", username);
//		data.put("password", password);
//		data.put("phoneNum", phoneNum);
//		data.put("email", email);
//		data.put("trueName", trueName);
//		json.put("data", data);
//		return cft.sendRequest(json);
//	}
//
//	/**
//	 * 用户登录
//	 * 
//	 * @param username
//	 * @param password
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject login(String username, String password) throws Exception {
//		init();
//		json.put("cmd", "user_login");
//		JSONObject data = new JSONObject();
//		data.put("username", username);
//		data.put("password", password);
//		json.put("data", data);
//		return cft.sendRequest(json);
//	}
//	
//	
//	/**
//	 * 修改密码
//	 * 
//	 * @param username
//	 * @param password
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject changePassword(String oldpassword, String newpassword) throws Exception {
//		init();
//		json.put("cmd", "user_changepassword");
//		JSONObject data = new JSONObject();
//		data.put("orignalPassword", oldpassword);
//		data.put("newPassword", newpassword);
//		json.put("data", data);
//		return cft.sendRequest(json);
//	}
//	
//	/**
//	 * 绑定账户
//	 * 
//	 * @param username
//	 * @param password
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject userBind(String username, String password) throws Exception {
//		init();
//		json.put("cmd", "user_bind");
//		JSONObject data = new JSONObject();
//		data.put("username", username);
//		data.put("password", password);
//		json.put("data", data);
//		return cft.sendRequest(json);
//	}
//	
//
//	/**
//	 * 解除绑定
//	 * 
//	 * @param 
//	 * @param 
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject userUnBind() throws Exception {
//		init();
//		json.put("cmd", "user_unbind");
//		return cft.sendRequest(json);
//	}
//	
//	/**
//	 * 获取用户信息
//	 * 
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject getUserInfo() throws Exception {
//		init();
//		json.put("cmd", "user_info");
//		return cft.sendRequest(json);
//	}
//	
//
//	/**
//	 * 更改用户信息
//	 * 
//	 * @param username
//	 * @param password
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject changeUserInfo(String name, String tel, String birthday, String email, String addr, String sex) throws Exception {
//		init();
//		
//		json.put("cmd", "user_changeinfo");
//		JSONObject data = new JSONObject();
//		data.put("trueName", name);
//		data.put("phoneNum", tel);
//		data.put("birtyday", birthday);
//		data.put("email", email);
//		data.put("addr", addr);
//		data.put("sex", sex);
//	
//		json.put("data", data);	
//		return cft.sendRequest(json);
//	}
//
//	
//	/**
//	 *   注销用户
//	 * 
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject logout() throws Exception {
//		init();
//		json.put("cmd", "user_logout");
//		return cft.sendRequest(json);
//	}
//	
//	/**
//	 *   发送心跳包
//	 * 
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject userHeart() throws Exception {
//		init();
//		Log.e("", "userHeart");
//		json.put("cmd", "user_heartbeat");
//		return cft.udpRequestForRep(json);
//	}
//	
////	public static void main(String[] args) throws Exception {
////		Receiver receiver = new Receiver(Caller.getInstance());
////		receiver.start();
////		User user = new User();
////		// 登录
////		user.login("peter", "adfe2!");
////		// 注册
//////		user.register("peter", "sdfasd", "13111111111", "email@gmail.com", "张三");
////	}
//	
//	
//	
//	/**
//	 *   上传GPS信息
//	 * 
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject userUploadGPS(PublicType.UserUploadGps info) throws Exception {
//		Log.e("", "user_uploadgps");
//		json = new JSONObject();
//		json.put("cmd", "user_uploadgps");
//		json.put("sid", sid);
//		json.put("data", info.toJsonObject());
//		return cft.sendRequest(json);
//	}
//	
//	
//	/**
//	 *   删除消息
//	 * 
//	 * @return
//	 * @throws Exception 
//	 */
//	public JSONObject userDelMsg(DeviceSetType.DeviceDelMsg info) throws Exception {
//		Log.e("", "user_delalertmessage");
//		init();
//		json.put("cmd", "user_delalertmessage");
//		json.put("data", info.toJsonObject());
//		return cft.sendRequest(json);
//	}
//}
