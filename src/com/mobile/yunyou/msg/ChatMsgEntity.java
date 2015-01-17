
package com.mobile.yunyou.msg;

import com.mobile.yunyou.model.DeviceSetType.DeviceMsgData;
import com.mobile.yunyou.network.api.HeadFileConfigure;

public class ChatMsgEntity {

    private String name = "";

    private String date = "";

    private String text = "";
    
    private int type = 0;
    
    private int ids = 0;
    
    private String mHeadUrl = ""; 
    
    private boolean isSelect = false;

    private boolean isComMeg = true;
    
	
	
    public ChatMsgEntity(DeviceMsgData msgData) {
        super();

        this.name = msgData.mAlias;
        this.date = msgData.mTime;
        this.text = msgData.mMessage;
        this.type = msgData.mType;
        this.ids = msgData.mID;
        this.isComMeg = true;
        this.mHeadUrl = HeadFileConfigure.getRequestUri(msgData.mDid);
        
    }
    
    public int getIDS()
    {
    	return ids;
    }
    
    public void setIDS(int ids)
    {
    	this.ids = ids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    public String getHeadUril() {
        return mHeadUrl;
    }

    public void setHeadUrl(String url) {
        this.mHeadUrl = url;
    }
    
    
    public int getType() {
        return type;
    }

    public void setType(int type) {
    	this.type = type;
    }


    public boolean getMsgType() {
        return isComMeg;
    }

    public void setMsgType(boolean isComMsg) {
    	isComMeg = isComMsg;
    }
    
    public boolean getSelectState() {
        return isSelect;
    }

    public void setSelectState(boolean flag) {
    	isSelect = flag;
    }
    
    public void toggleSelectState()
    {
    	isSelect = !isSelect;
    }
    

    public ChatMsgEntity() {
    }

    public ChatMsgEntity(String name, String date, String text, boolean isComMsg) {
        super();
        this.name = name;
        this.date = date;
        this.text = text;
        this.isComMeg = isComMsg;
    }

}
