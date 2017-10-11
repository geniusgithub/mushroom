package com.huizhilian.android;

import org.json.JSONObject;


public interface MessagePushHandler {
	/**
	 * 各类告警消息，包括SOS，区域告警，低电告警，签到等。
	 * @param obj
	 */
	public void messageHandler(JSONObject obj);
	
	/**
	 * 强制客户端下线
	 * @param obj
	 */
	public void forceLogoutHandler(JSONObject obj);
	
	/**
	 * 终端在线状态变化
	 * @param obj
	 */
	public void terminalStatusChangeHandler(JSONObject obj);
	
	/**
	 * 其他消息，以后扩展
	 * @param obj
	 */
	public void otherHandler(JSONObject obj);
	
	public void exceptionHandler(Exception ex);
}
