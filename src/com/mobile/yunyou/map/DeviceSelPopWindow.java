//package com.mobile.yunyou.map;
//
//import java.util.List;
//
//import com.amap.mapapi.core.v;
//import com.mobile.yunyou.R;
//import com.mobile.yunyou.model.GloalType;
//import com.mobile.yunyou.util.CommonLog;
//import com.mobile.yunyou.util.LogFactory;
//import com.mobile.yunyou.util.Utils;
//
//import android.R.integer;
//import android.content.Context;
//import android.graphics.drawable.ColorDrawable;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//
//public class DeviceSelPopWindow extends PopupWindow{
//
//	private static final CommonLog log = LogFactory.createLog();
//	
//	private Context mContext;
//	private ListView mListView;
//	private DeviceSelAdapter mAdapter;
//	
//	public DeviceSelPopWindow(Context context)
//	{
//		super(context);
//		
//		mContext = context;
//		init();
//	}
//	
//	public void setSelChangeListener(DeviceSelAdapter.SelChangeListener listener)
//	{
//		mAdapter.setListener(listener);
//	}
//	
//	private void init()
//	{
//		View view = LayoutInflater.from(mContext).inflate(R.layout.menu_down_layout, null);
//		setContentView(view);		
//		setWidth((int) (Utils.getScreenDensity(mContext) * 240));
//		setHeight(LayoutParams.WRAP_CONTENT);
//		
//		setFocusable(true);
//    	ColorDrawable dw = new ColorDrawable(0x00);
//		setBackgroundDrawable(dw);
//	
//		
//		mListView = (ListView) view.findViewById(R.id.listview);
//		
//
//		mAdapter = new DeviceSelAdapter(mContext);	
//		mListView.setAdapter(mAdapter);
//		mListView.setOnItemClickListener(mAdapter);
//		
//	}
//	
//	
//	public void refreshData(List<GloalType.DeviceInfoEx> list, int selIndex)
//	{
//		if (list != null && selIndex  != -1)
//		{
//			mAdapter.refreshData(list);
//			mAdapter.setSelectItem(selIndex);
//		}
//	}
//	
//	
//	
//}
