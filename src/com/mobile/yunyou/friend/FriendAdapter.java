package com.mobile.yunyou.friend;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.yunyou.R;
import com.mobile.yunyou.util.ImageLoader;


public class FriendAdapter extends BaseAdapter implements OnClickListener{

	public static interface IPosCallback{
		public void onPosCallback(int pos);
	};
	
	private List<FriendObject> data = new ArrayList<FriendObject>();
	
	private Context mContext;
	
	private final ImageLoader imageDownloader;
	
	private IPosCallback mCallback;
	
	private boolean mIsShowBtn = true;
	
	public FriendAdapter(Context context, List<FriendObject> data)
	{
		mContext = context;
		this.data = data;
		imageDownloader = new ImageLoader(context);
	}
	
	public void setPosCallback(IPosCallback callback){
		mCallback = callback;
	}
	
	public void setShowStatus(boolean flag){
		mIsShowBtn = flag;
	}
	
	public void refreshData(List<FriendObject> data)
	{
		this.data = data;
		notifyDataSetChanged();
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return data.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		 ViewHolder holder = null; 


		if (view == null)
		{
			view = LayoutInflater.from(mContext).inflate(R.layout.friend_list_item_layout, null);
			holder = new ViewHolder();
			holder.tvName = (TextView) view.findViewById(R.id.tvName);
			holder.tvPhone = (TextView) view.findViewById(R.id.tvPhone);
			//holder.cbBox = (CheckBox) view.findViewById(R.id.checkBox);
			holder.btUnbind = (Button) view.findViewById(R.id.btn_unbind);
			holder.btUnbind.setOnClickListener(this);
			holder.proHeadImageView = (ImageView) view.findViewById(R.id.ivUserProfile);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		
		FriendObject object = data.get(pos);
		holder.tvName.setText(object.mUserName);
		holder.tvPhone.setText(object.mPhone);
		holder.btUnbind.setTag(pos);
		holder.btUnbind.setVisibility(mIsShowBtn ? View.VISIBLE : view.GONE);
		//holder.cbBox.setChecked(true);
		holder.proHeadImageView.setImageResource(R.drawable.default_head);
		if (object.mHeadUrl.length() > 0){
			imageDownloader.DisplayImage(object.mHeadUrl, holder.proHeadImageView, false);
		}
		
		
		return view;
	}
	
	
    static class ViewHolder { 
        public TextView tvName;
        public TextView tvPhone;
        public Button btUnbind;
      //  public CheckBox cbBox;
        public ImageView proHeadImageView;
    }


	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.btn_unbind:
			int pos = (Integer) view.getTag();
			if (mCallback != null){
				mCallback.onPosCallback(pos);
			}
			break;
		}
		
	} 


	
	
	
}
