package com.egoists.coco_nut.android.kanban.briefing;

import java.util.Calendar;

import com.egoists.coco_nut.android.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;

public class PeerView extends View {
	
	//Dummy Dataset : will be achieved from server later
	Calendar[] dateofchanges;
	int number_of_dates = 17;
	Calendar currentdate;
	int dday;
	long dday_Millis;
	
	
	//x y cordinated of things: will be scaled by screen definitions
	final int top_margin = 267;
	final int chart_height = 21 + 29*14 + 8;
	
	final int leftline_w = 4;
	final int leftline_c = Color.argb(100, 167, 167, 167);
	final int leftline_x = 50;
	final int leftline_y1 = top_margin + 13;
	final int leftline_y2 = top_margin + chart_height + 13;
	Paint leftline_paint;
	
	final int bottomline_w = 1;
	final int bottomline_c = Color.argb(100, 154, 154, 154);
	final int bottomline_x1 = leftline_x;
	final int bottomline_x2 = 689;
	final int bottomline_y = leftline_y2;
	Paint bottomline_paint;
	
	final int centerline_w = 1;
	final int centerline_c = Color.argb(100, 186, 186, 186);
	final int centerline_x1 = leftline_x + 2;
	final int centerline_x2 = 740;
	final int centerline_y1 = leftline_y1;
	final int centerline_y2 = leftline_y2;
	Paint centerline_paint;
	
	final int toptext_x1 = 85;
	final int toptext_x2 = 655;
	final int toptext_y = top_margin;
	final int toptext_c = Color.argb(100, 186, 186, 186);
	final int toptext_size = 25;
	Paint toptext_paint;
	
	final int icon_y1 = top_margin / 3;
	final int icon_y2 = icon_y1 + 40;
	final int icon_x1 = leftline_x;
	final int icon_x2 = icon_x1 + 28;
	final int icon_text_c = Color.argb(255, 94, 119, 142);
	final int icon_text_x = icon_x2 + 20;
	final int icon_text_y = icon_y2;
	final int icon_text_size = 33;
	Paint icontext_paint;
	Drawable icon;
	
	Point resolution;
	int nLines;
	
	public PeerView(Context context){
		super(context);
		//instantiate dummy data
		instantiatedummydates();
		
		//get screen resolution
		resolution = new Point();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(resolution);
		
		//Initializations
		initializePaints();
		setBackgroundColor(Color.WHITE);
		setMinimumHeight(y(Math.max(1070, top_margin + chart_height + 250)));
		
		//Calculate date difference
		long tmp = (currentdate.getTimeInMillis()-dateofchanges[0].getTimeInMillis())/24/3600/1000;
		nLines = numberofLinestoDraw((int)tmp);
		dday = (int)(tmp/nLines + 1);
		dday_Millis = (long)dday*24*3600*1000;

	}
	public void onDraw(Canvas canvas){
		
		//Draw leftLine
		canvas.drawLine( x(leftline_x), y(leftline_y1), x(leftline_x), y(leftline_y2), leftline_paint);
		
		//Draw bottomLine
		canvas.drawLine( x(bottomline_x1), y(bottomline_y), x(bottomline_x2), y(bottomline_y), bottomline_paint);
		
		//Draw centerLine
		double centerline_dx = (centerline_x2 - centerline_x1)/(nLines+1);
		if(nLines != 1)
			for(int i = 1; i <= nLines; i++)
				canvas.drawLine(x((int)(centerline_x1 + centerline_dx*i)), y(centerline_y1), 
						x((int)(centerline_x1 + centerline_dx*i)), y(centerline_y2), centerline_paint);
		

		icon = getResources().getDrawable(R.drawable.briefing_chart_icon);
		icon.setBounds(x(icon_x1), y(icon_y1), x(icon_x2), y(icon_y2));
		
		//Draw topTexts
		Calendar tmp = Calendar.getInstance();
		for(int i = 0; i< nLines; i++){
			tmp.setTimeInMillis(currentdate.getTimeInMillis()-dday_Millis*i);
			canvas.drawText((tmp.get(Calendar.MONTH)+1)+"."+tmp.get(Calendar.DATE),
					x((int)(centerline_x1 + (nLines-i)*centerline_dx)), y(toptext_y), toptext_paint);
		}
		
		icon.draw(canvas);
		canvas.drawText("상호평가 점수", x(icon_text_x), y(icon_text_y), icontext_paint);
		
	}
	
	private int x(int x){
		return x * resolution.x / 720;
	}
	private int y(int y){
		return y * resolution.y / 1280;
	}
	private int xy(int xy){
		return x(xy) > y(xy) ? y(xy) : x(xy);
	}
	private int ixy(int xy){
		return x(xy) > y(xy) ? xy * 1280 / resolution.y : xy * 720 / resolution.x;
	}
	
	private int numberofLinestoDraw(int days){
		return days / (days/10 + 1) + 1;
	}
	
	private void instantiatedummydates(){
		int[] changedate = {1, 3, 7, 8, 11, 12, 14, 15, 18, 22, 24, 25, 26, 28, 29, 30, 1};
		int[] changemonth = {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 10};
		dateofchanges = new Calendar[number_of_dates];
		
		currentdate = Calendar.getInstance();
		for(int i = 0; i< number_of_dates; i++)
		{
			dateofchanges[i] = Calendar.getInstance();
			dateofchanges[i].set(2013, changemonth[i]-1, changedate[i]);
		}
	}
	
	private void initializePaints(){
		
		leftline_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		leftline_paint.setStyle(Paint.Style.STROKE);
		leftline_paint.setStrokeWidth(leftline_w);
		leftline_paint.setColor(leftline_c);
		
		bottomline_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bottomline_paint.setStyle(Paint.Style.STROKE);
		bottomline_paint.setStrokeWidth(bottomline_w);
		bottomline_paint.setColor(bottomline_c);

		centerline_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		centerline_paint.setStyle(Paint.Style.STROKE);
		centerline_paint.setStrokeWidth(centerline_w);
		centerline_paint.setColor(centerline_c);
		
		toptext_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		toptext_paint.setStyle(Paint.Style.FILL);
		toptext_paint.setTypeface(Typeface.create((String)null, Typeface.BOLD));
		toptext_paint.setColor(toptext_c);
		toptext_paint.setTextSize(xy(toptext_size));	
		toptext_paint.setTextAlign(Align.CENTER);

		icontext_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		icontext_paint.setStyle(Paint.Style.FILL);
		icontext_paint.setTypeface(Typeface.create((String)null, Typeface.BOLD));
		icontext_paint.setColor(icon_text_c);
		icontext_paint.setTextSize(xy(icon_text_size));	
		icontext_paint.setTextAlign(Align.LEFT);
	}
}
