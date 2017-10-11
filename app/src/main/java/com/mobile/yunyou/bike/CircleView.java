package com.mobile.yunyou.bike;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.View;

public class CircleView extends View{

	private Context mContext;
	private int mCenterX = 0;
	private int mCenterY = 0;
	private int mRadius = 0;
	private Paint circlePaint = new Paint();
	
	public CircleView(Context context) {
		super(context);
		mContext = context;
		initPaint();
	}
	

	private void initPaint()
	{
		circlePaint = new Paint();  
		circlePaint.setAntiAlias(true);  
		circlePaint.setColor(Color.RED);  
		circlePaint.setAlpha(50);  
		circlePaint.setStyle(Style.FILL);

	}
	
	
	public void updateCircle(int radius){
		mRadius = radius;
		postInvalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (mRadius > 0){
			canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, circlePaint);
		}

		
	}


	
}
