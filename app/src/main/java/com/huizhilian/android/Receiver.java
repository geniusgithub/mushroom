package com.huizhilian.android;

import java.net.SocketTimeoutException;

import org.json.JSONObject;

import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

public class Receiver extends Thread {
	
	private static final CommonLog log = LogFactory.createLog();
	
	
	Caller caller;
	private MessagePushHandler handler;

	public Receiver(Caller caller) {
		this.caller = caller;
	}

	public void run() {
		while (true) {
			try {
				byte[] bytes = caller.receive();
				AdrClientDataPacket packet = new AdrClientDataPacket();
				try {
					packet.read(bytes);
					if (!packet.verify()) {
						continue;
					}
					JSONObject obj = new JSONObject(packet.getBody());
					// message push
					if (obj.getString("cmd").startsWith("messagepush")) {
					
						if (handler != null) {
							String cmd = obj.getString("cmd");
							log.e("Receiver	messagepush	-->		cmd = " + cmd);
							if (cmd.equals("messagepush_msg")) {
								handler.messageHandler(obj);
							} else if (cmd.equals("messagepush_logout")) {
								handler.forceLogoutHandler(obj);
							} else if (cmd.equals("messagepush_terminalstatus")) {
								handler.terminalStatusChangeHandler(obj);
							} else {
								handler.otherHandler(obj);
							}
						}
					} else {
						caller.onReceivePacket(packet);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (SocketTimeoutException ex) {
				
			} catch (Exception e) {
				e.printStackTrace();
				if (handler != null) {
					handler.exceptionHandler(e);
				}

			}
		}
	}

	public void setHandler(MessagePushHandler handler) {
		this.handler = handler;
	}

	public MessagePushHandler getHandler() {
		return handler;
	}
}
