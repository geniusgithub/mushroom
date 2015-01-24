package com.huizhilian.android;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.mobile.yunyou.ServiceIPConfig;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;

//调用后台，可以通过http，udp等方式。
public class Caller {

	private static final CommonLog log = LogFactory.createLog();
	
	private Map<Integer, Object> lockMap = new ConcurrentHashMap<Integer, Object>();
	private Map<Integer, AdrClientDataPacket> waitPackets = new ConcurrentHashMap<Integer, AdrClientDataPacket>();

	//private String udpIp = "www.360lbs.net";

	
	private DatagramSocket socket;
//	private static final String addr = "http://www.360lbs.net:8080";

	
	private InetAddress serverAddress;
	
	private int REQUEST_COUNT = 3;

	public static Caller getInstance() {
		return InstanceHelper.instance;
	}

	private static class InstanceHelper {
		private static Caller instance = new Caller();
	}

	/**
	 * 向服务器发送数据
	 * 
	 * @param line
	 *            要发送的 byte[]
	 * @throws Exception
	 */

	public boolean send(byte[] line) {
		if (serverAddress == null) {
			try {
				serverAddress = InetAddress.getByName(ServiceIPConfig.udpIp);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.e("serverAddress = InetAddress.getByName(udpIp) fail!!!");
				return false;
			}
		}
		
		// 创建一个DatagramPacket对象，并指定要讲这个数据包发送到网络当中的哪个地址，以及端口号
		DatagramPacket packet = new DatagramPacket(line, line.length, serverAddress, ServiceIPConfig.udpPort);
		
		// 调用socket对象的send方法，发送数据
		try {
			socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public void init() throws Exception {
		Log.d("caller",ServiceIPConfig.udpIp);
		// 创建一个InetAddree
	
		// 首先创建一个DatagramSocket对象
		socket = new DatagramSocket();
		socket.setSoTimeout(5000);
		
	//	serverAddress = InetAddress.getByName(udpIp);

	}

	private void addWaitPacket(AdrClientDataPacket packet) {
		waitPackets.put(packet.getSeqNo(), packet);
	}

	public boolean onReceivePacket(AdrClientDataPacket packet) {
		if (packet == null) {
			return false;
		}
		if (lockMap.containsKey(packet.getSeqNo())) {
			addWaitPacket(packet);
			Object lock = lockMap.remove(packet.getSeqNo());
			synchronized (lock) {
				lock.notify();
			}
			return false;
		}
		return true;

	}

	/**
	 * 接收服务器发送来的数据
	 * 
	 * @return
	 * @throws IOException
	 */
	public byte[] receive() throws IOException {
		byte[] data = new byte[1024 * 10];
		// 创建一个空的DatagramPacket对象
		DatagramPacket packet = new DatagramPacket(data, data.length);
		while (socket == null) {
			try {
				Thread.sleep(2000);
				init();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		socket.receive(packet);
		int length = packet.getLength();
		byte[] bytes = new byte[length];
		System.arraycopy(packet.getData(), 0, bytes, 0, length);
		return bytes;
	}

	private AdrClientDataPacket removeWaitPacket(Integer seq) {
		return (AdrClientDataPacket) waitPackets.remove(seq);
	}

	/**
	 * 发送需要在一定时间内的请求消息，如果该时间段内无响应，则重发
	 * 
	 * @param msg
	 * @param timeOut
	 * @param resendTime
	 * @return
	 * @throws Exception
	 */
	private AdrClientDataPacket sendRequestForRep(AdrClientDataPacket msg)
			throws Exception {
//		AdrClientDataPacket resp = udpRequest(msg);
//		// UDP 获取失败，使用Http再试一次
//		if (resp == null) {
//			resp = httpRequest(msg);
//		}
		
		AdrClientDataPacket resp = null;
		for(int i = 0; i < REQUEST_COUNT; i++)
		{
			try {
				resp = httpRequest(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.e("httpRequest(msg) catch Exception  cmd = " + msg.getCMD());
			}
			
			if (resp != null)
			{
				break;
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			log.e("request again...cmd = " + msg.getCMD());
		}
	
		
		return resp;
	}

	private AdrClientDataPacket udpRequest(AdrClientDataPacket msg)
			throws Exception {
		if (msg == null) {
			return null;
		}
		int resendTime = 3;
		long timeOut = 5000;
		Object lock = new Object();
		lockMap.put(msg.getSeqNo(), lock);
		AdrClientDataPacket resp = null;
		for (int i = 0; i < resendTime; i++) {
			byte[] bytes = msg.write();
			
			boolean ret = send(bytes);
			if (ret)
			{
				resp = removeWaitPacket(msg.getSeqNo());
				if (resp == null) {
					synchronized (lock) {
						try {
							lock.wait(timeOut);
						} catch (InterruptedException ex) {
						}
					}
					resp = removeWaitPacket(msg.getSeqNo());
					if (resp != null) {
						break;
					}
				} else {
					break;
				}
			}else{
				Thread.sleep(timeOut);
			}
			
		}
		lockMap.remove(msg.getSeqNo());
		return (AdrClientDataPacket) resp;
	}

	private Caller() {
		try {
			init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public AdrClientDataPacket httpRequest(AdrClientDataPacket msg)
			throws IOException {
		if (msg == null) {
			return null;
		}
		HttpURLConnection urlConn = null;
		OutputStream out = null;
		InputStream in = null;
		try {
			URL url = new URL(ServiceIPConfig.addr);			
			urlConn = (HttpURLConnection) url.openConnection();		
			//urlConn.setReadTimeout(5000);
			urlConn.setConnectTimeout(5000);
			urlConn.setRequestMethod("POST");
			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setUseCaches(false);
			out = urlConn.getOutputStream();
			out.write(msg.getBody().getBytes("utf-8"));
			out.flush();
			in = urlConn.getInputStream();
			int getContentLen = urlConn.getContentLength();
		
			log.e("url = " + ServiceIPConfig.addr + "\nbody = " + msg.getBody());
			
			ByteArrayOutputStream bos = null;
			byte[] bytes=null;
			try {
				// 输出图像到客户端
				bos = new ByteArrayOutputStream();
				int i=-1;
				byte[] block = new byte[8192];
				while ((i = in.read(block)) != -1) {
					bos.write(block,0,i);
					block = new byte[8192];
				}
				bytes = bos.toByteArray();
			
			} finally {
				if (bos != null) {
					bos.close();
				}
			}		
			if(bytes==null)return null;
	
			//log.e("bytes.lenth = " + bytes.length);
			AdrClientDataPacket packet = new AdrClientDataPacket();
			String body = new String(bytes, "utf-8");
			log.d("body len = " + body.length() + "\n" + "body = " + body);
			packet.setBody(body);
			return packet;
		} finally {
			Util.close(in, out);
			if (urlConn != null)
			{
				urlConn.disconnect();
			}else{
				log.e("urlConn = NULL... addr = " + ServiceIPConfig.addr);
			}
			
		}
	}

	private static int seq = 0;
	private synchronized int getSeq()
	{
		return seq++;
	}

	public JSONObject sendRequest(JSONObject request)  throws Exception{
		Log.d("sendRequest", "request = " + request.toString());
		
		String cmd = "";
		try {
			 cmd = request.getString("cmd");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		AdrClientDataPacket packet = new AdrClientDataPacket(cmd);
		packet.setBody("json=" + request.toString());
		packet.setSeqNo(getSeq());
		AdrClientDataPacket resp = null;
		try {
			resp =  sendRequestForRep(packet);
			if (resp != null)
			{
				String body = resp.getBody();
				return new JSONObject(resp.getBody());
			}	
		} catch (IOException e) {
			e.printStackTrace();
		    log.e("catch IOException");
			JSONObject response = new JSONObject();
			response.put("rsp", 0);
			response.put("msg", "参数错误");
			return response; 
			
		}
		
		return null;
	}
	
	public JSONObject udpRequestForRep(JSONObject request) throws Exception {

		Log.d("udpRequest", "request = " + request.toString());
		AdrClientDataPacket packet = new AdrClientDataPacket();
		packet.setBody(request.toString());
		packet.setSeqNo(seq++);
		AdrClientDataPacket resp = null;
		try {
			
			resp = udpRequest(packet);
			String body = resp.getBody();
			return new JSONObject(body);
		} catch (IOException e) {
			e.printStackTrace();
		    log.e("catch IOException");
			JSONObject response = new JSONObject();
			response.put("rsp", 0);
			response.put("msg", "参数错误");
			return response;
		}
		
	}
	
	
}
