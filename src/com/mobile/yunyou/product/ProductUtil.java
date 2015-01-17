package com.mobile.yunyou.product;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


import android.util.Log;

import com.mobile.yunyou.model.ProductType;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;



public class ProductUtil {
	private static final CommonLog log = LogFactory.createLog();
	private final static String NOTIFY_URL = "http://notify.java.jpxx.org/index.jsp";
	
	public static String getOrderInfo(ProductType.GetPackage object, String orderNO) {
		String strOrderInfo = "partner=" + "\"" + PartnerConfig.PARTNER + "\"";
		strOrderInfo += "&";
		strOrderInfo += "seller=" + "\"" + PartnerConfig.SELLER + "\"";
		strOrderInfo += "&";
		strOrderInfo += "out_trade_no=" + "\"" + orderNO + "\"";
		strOrderInfo += "&";
		strOrderInfo += "subject=" + "\"" + object.mName
				+ "\"";
		strOrderInfo += "&";
		strOrderInfo += "body=" + "\"" + object.mDetail + "\"";
		strOrderInfo += "&";
		strOrderInfo += "total_fee=" + "\"" + object.mPrice + "\"";
		strOrderInfo += "&";
		strOrderInfo += "notify_url=" + "\"" + NOTIFY_URL + "\"";
		
		return strOrderInfo;
	}
	
	
	public static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
		Date date = new Date();
		String strKey = format.format(date);

		java.util.Random r = new java.util.Random();
		strKey = strKey + r.nextInt();
		strKey = strKey.substring(0, 15);
		return strKey;
	}
	
	
	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 * @return
	 */
	public static String getSignType() {
		String getSignType = "sign_type=" + "\"" + "RSA" + "\"";
		return getSignType;
	}
	
	
	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param signType
	 *            签名方式
	 * @param content
	 *            待签名订单信息
	 * @return
	 */
	public static String sign(String signType, String content) {
		return Rsa.sign(content, PartnerConfig.RSA_PRIVATE);
	}
	
	
	public static String getSingResult(ProductType.GetPackage object, String orderNO){
		String orderInfo = getOrderInfo(object, orderNO);
		// 这里根据签名方式对订单信息进行签名
		String signType = getSignType();
		String strsign = sign(signType, orderInfo);
		log.e("strsign = " + strsign);
		

		// 对签名进行编码
		strsign = URLEncoder.encode(strsign);
		String info = orderInfo + "&sign=" + "\"" + strsign + "\"" + "&" + signType;
		log.e("info = " + info);
		
		return info;
	}
	
	

	

	// 组装好参数
	
	
}
