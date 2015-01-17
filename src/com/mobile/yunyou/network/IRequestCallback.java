package com.mobile.yunyou.network;

import org.json.JSONObject;

import com.mobile.yunyou.model.ResponseDataPacket;


public interface IRequestCallback {

		boolean onComplete(int requestAction, ResponseDataPacket dataPacket);

}
