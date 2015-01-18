
package com.mobile.yunyou.msg;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.List;

import com.mobile.yunyou.R;
import com.mobile.yunyou.map.data.LocationEx;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.ImageLoader;
import com.mobile.yunyou.util.LogFactory;

public class ChatMsgViewAdapter extends BaseAdapter implements OnItemClickListener{
	
	private static final CommonLog log = LogFactory.createLog();
	
	public static interface IMsgViewType
	{
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}
	
	public static interface ItemSelectChangeListener
	{
		public void onSelectChange(boolean flag);
	}
	
    private static final String TAG = ChatMsgViewAdapter.class.getSimpleName();

    private List<ChatMsgEntity> coll;

    private Context ctx;
    
    private LayoutInflater mInflater;
    
	private final ImageLoader imageDownloader;
	
    private boolean isShowCheckbox = false;
    
    private ItemSelectChangeListener mItemSelectChangeListener;

    public ChatMsgViewAdapter(Context context, List<ChatMsgEntity> coll) {
        ctx = context;
        this.coll = coll;
        mInflater = LayoutInflater.from(context);
    	imageDownloader = new ImageLoader(context);
    }

    public void setItemChangeListener(ItemSelectChangeListener listener)
    {
    	mItemSelectChangeListener = listener;
    }
    
    public void setData( List<ChatMsgEntity> coll)
    {
    	this.coll = coll;
    	notifyDataSetChanged();
    }
    
    public int getCount() {
        return coll.size();
    }

    public Object getItem(int position) {
        return coll.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    


	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
	 	ChatMsgEntity entity = coll.get(position);
	 	
	 	if (entity.getMsgType())
	 	{
	 		return IMsgViewType.IMVT_COM_MSG;
	 	}else{
	 		return IMsgViewType.IMVT_TO_MSG;
	 	}
	 	
	}


	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	
	public void setCheckbox(boolean flag)
	{
		isShowCheckbox = flag;
		notifyDataSetChanged();
	}
	
	
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	ChatMsgEntity entity = coll.get(position);
    	boolean isComMsg = entity.getMsgType();
    		
    	ViewHolder viewHolder = null;	
	    if (convertView == null)
	    {
	    	  if (isComMsg)
			  {
				  convertView = mInflater.inflate(R.layout.chatting_item_msg_text_left, null);
			  }else{
				  convertView = mInflater.inflate(R.layout.chatting_item_msg_text_right, null);
			  }

	    	  viewHolder = new ViewHolder();
	    	  viewHolder.ivHeadImageView = (ImageView) convertView.findViewById(R.id.iv_userhead);
			  viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_sendtime);
			  viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tv_username);
			  viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_chatcontent);
			  viewHolder.cbCheckBox = (CheckBox) convertView.findViewById(R.id.cb_checkbox);
			  viewHolder.isComMsg = isComMsg;
			  LocationEx fEx;
	
			  convertView.setTag(viewHolder);
	    }else{
	        viewHolder = (ViewHolder) convertView.getTag();
	    }
	
	    
	    
	    viewHolder.tvSendTime.setText(entity.getDate());
	    //viewHolder.tvUserName.setText(entity.getName());
	    viewHolder.tvUserName.setText("蘑菇伴侣");
	    viewHolder.tvContent.setText(entity.getText());
	    viewHolder.cbCheckBox.setChecked(entity.getSelectState());
	    viewHolder.ivHeadImageView.setImageResource(R.drawable.mogu_icon);
	    if (isShowCheckbox == true)
	    {
	    	 viewHolder.cbCheckBox.setVisibility(View.VISIBLE);
	    }else{
	    	viewHolder.cbCheckBox.setVisibility(View.GONE);
	    }
	    
//	    if (entity.getHeadUril().length() > 0){
//	    	imageDownloader.DisplayImage(entity.getHeadUril(), viewHolder.ivHeadImageView, false);
//	    }
	    
	    
	    return convertView;
    }
    
    
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		log.e("position = " + position);
		if (mItemSelectChangeListener != null)
		{
			coll.get(position).toggleSelectState();
			notifyDataSetChanged();	
			
			mItemSelectChangeListener.onSelectChange(isSelectAll());
		}
	
		
		
		
//		displayBoolean();
	} 
    
	
	public void selAll()
	{
		int size = coll.size();
		for(int i = 0; i < size; i++)
		{
			coll.get(i).setSelectState(true);
		}
		
		notifyDataSetChanged();
		
		if (mItemSelectChangeListener != null)
		{
			mItemSelectChangeListener.onSelectChange(true);
		}
	}
	
	public void cancelSelAll()
	{
		int size = coll.size();
		for(int i = 0; i < size; i++)
		{
			coll.get(i).setSelectState(false);
		}
		
		notifyDataSetChanged();
		
		if (mItemSelectChangeListener != null)
		{
			mItemSelectChangeListener.onSelectChange(false);
		}
	}

	private boolean isSelectAll()
	{
		int size = coll.size();
		if (size == 0)
		{
			return false;
		}
		for(int i = 0; i < size; i++)
		{
			boolean flag = coll.get(i).getSelectState();
			if (flag == false)
			{
				return false;
			}
		}
		
		return true;
	}
	
    static class ViewHolder { 
    	public ImageView ivHeadImageView;
        public TextView tvSendTime;
        public TextView tvUserName;
        public TextView tvContent;
        public CheckBox cbCheckBox;
        public boolean isComMsg = true;
    }


}
