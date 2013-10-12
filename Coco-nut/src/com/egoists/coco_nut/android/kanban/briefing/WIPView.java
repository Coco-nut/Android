package com.egoists.coco_nut.android.kanban.briefing;

import java.util.Calendar;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.util.AndLog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;

public class WIPView extends View {
	
	//Dummy Dataset : will be achieved from server later
	Calendar[] dateofchanges;
	final int number_of_dates = 17;
	final int[] WIP = {1, 3, 6, 4, 2, 5, 6, 4, 7, 6, 5, 4, 3, 5, 3, 2, 1};
	final int[] Done = {0, 0, 1, 2, 3, 3, 3, 4, 4, 8, 8, 8, 8, 9, 11, 12, 13};
	final int[] changedate = {1, 3, 7, 8, 11, 12, 14, 15, 18, 22, 24, 25, 26, 28, 29, 30, 1};
	final int[] changemonth = {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 10};
	final int maxWIP = 4;
	final int alarm_WIP = 6;
	
	//x y cordinated of things: will be scaled by screen definitions
	final int background_c = Color.WHITE;
	final int top_margin = 237;
	final int chart_height = 21 + 29*14 + 8;
	
	final int leftline_w = 4;
	final int leftline_c = Color.argb(255, 217, 217, 217);
	final int leftline_x = 31;
	final int leftline_y1 = top_margin + 13;
	final int leftline_y2 = top_margin + chart_height + 13;
	Paint leftline_paint;
	
	final int bottomline_w = 1;
	final int bottomline_c = Color.argb(255, 203, 203, 203);
	final int bottomline_x1 = leftline_x;
	final int bottomline_x2 = 689;
	final int bottomline_y = leftline_y2;
	Paint bottomline_paint;
	
	final int centerline_w = 1;
	final int centerline_c = Color.argb(255, 236, 236, 236);
	final int centerline_x1 = leftline_x + 2;
	final int centerline_x2 = 740;
	final int centerline_y1 = leftline_y1;
	final int centerline_y2 = leftline_y2;
	double centerline_dx;
	Paint centerline_paint;
	
	final int flag_x1 = 59;
	final int flag_x2 = 59 + 22;
	final int flag_dx = 200;
	final int flag_y1 = leftline_y1 + chart_height + 12 + 2;
	final int flag_y2 = leftline_y1 + chart_height + 12 + 24;
	final int flag_dy = 47;
	RectF[] flags;
	final int flag_text_c = Color.argb(255, 130, 130, 130);
	final int flag_text_x = 88;
	final int flag_text_y = top_margin + chart_height + 48;
	Paint flagtext_paint;
	
	int graph_x2;
	final int graph_y1 = leftline_y1 + 21;
	final int graph_y2 = leftline_y2;

	final int overwippath_c = Color.argb(220, 255, 138, 138);
	Paint overwippath_paint;
	Path overwippath;
	Paint mask_paint;
	Path overwip_maskpath;
	
	final int donewippath_c = Color.argb(160, 159, 194, 217);
	Path donewippath;
	Paint donewippath_paint;
	final int donepath_c = Color.argb(255, 114, 144, 173);
	Path donepath;
	Paint donepath_paint;
	
	final int toptext_x1 = 85;
	final int toptext_x2 = 655;
	final int toptext_y = top_margin;
	final int toptext_c = Color.argb(255, 217, 217, 217);
	Paint toptext_paint;
	
	Drawable alarm;
	final int alarm_x = 18;
	final int alarm_y = 24;
	final int max_alarms = (centerline_x2 - centerline_x1) / alarm_x / 2;
	int number_of_alarms = 0;
	int[] alarm_center_x;
	int[] alarm_center_y;
	
	Point resolution;
	int nLines;
	int dday;
	int maxWD;
	long dday_Millis;
	Calendar currentdate;
	
	public WIPView(Context context){
		super(context);
		//instantiate dummy data
		instantiatedummydates();
		
		//get screen resolution
		resolution = new Point();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(resolution);
		
		//Initializations
		initialize();
		setBackgroundColor(background_c);
		setMinimumHeight(y(Math.max(1010, top_margin + chart_height + 250)));

	}
	public void onDraw(Canvas canvas){
		
		//Fill graph between done line and wip line
		canvas.drawPath(donewippath, donewippath_paint);
		canvas.drawPath(overwippath, overwippath_paint);
		canvas.drawPath(overwip_maskpath, mask_paint);
		
		//Draw leftLine
		canvas.drawLine( x(leftline_x), y(leftline_y1), x(leftline_x), y(leftline_y2), leftline_paint);
		
		//Draw bottomLine
		canvas.drawLine( x(bottomline_x1), y(bottomline_y), x(bottomline_x2), y(bottomline_y), bottomline_paint);
		
		//Draw centerLine
		if(nLines != 1)
			for(int i = 1; i <= nLines; i++)
				canvas.drawLine(x((int)(centerline_x1 + centerline_dx*i)), y(centerline_y1), 
						x((int)(centerline_x1 + centerline_dx*i)), y(centerline_y2), centerline_paint);
		
		//Draw alarms
		for(int i = 0; i <number_of_alarms; i ++)
		{
			alarm.setBounds(x(alarm_center_x[i] - alarm_x/2), y(alarm_center_y[i] - alarm_y/2), 
					x(alarm_center_x[i] + alarm_x/2), y(alarm_center_y[i] + alarm_y/2));
			alarm.draw(canvas);
		}
		
		//draw flags and flagtexts
		canvas.drawRoundRect(flags[0], 5, 5, donewippath_paint);
		canvas.drawRoundRect(flags[1], 5, 5, donepath_paint);
		canvas.drawRoundRect(flags[2], 5, 5, overwippath_paint);
		canvas.drawText("하는 중", x(flag_text_x), y(flag_text_y), flagtext_paint);
		canvas.drawText("완료", x(flag_text_x + flag_dx), y(flag_text_y), flagtext_paint);
		canvas.drawText("동시진행 허용량 초과", x(flag_text_x + 2*flag_dx), y(flag_text_y), flagtext_paint);
		
		//Fill graph under done line
		canvas.drawPath(donepath, donepath_paint);
		
		
		//Draw topTexts
		Calendar tmp = Calendar.getInstance();
		for(int i = 0; i< nLines; i++){
			tmp.setTimeInMillis(currentdate.getTimeInMillis()-dday_Millis*i);
			canvas.drawText((tmp.get(Calendar.MONTH)+1)+"."+tmp.get(Calendar.DATE),
					x((int)(centerline_x1 + (nLines-i)*centerline_dx)), y(toptext_y), toptext_paint);
		}
		
	}
	
	private int x(int x){
		return x * resolution.x / 720;
	}
	private int y(int y){
		return y * resolution.y / 1280;
	}
	
	private int numberofLinestoDraw(int days){
		return days / (days/10 + 1) + 1;
	}
	
	private void instantiatedummydates(){
		dateofchanges = new Calendar[number_of_dates];
		
		currentdate = Calendar.getInstance();
		maxWD = 0;
		for(int i = 0; i< number_of_dates; i++)
		{
			dateofchanges[i] = Calendar.getInstance();
			dateofchanges[i].set(2013, changemonth[i]-1, changedate[i]);
			if (maxWD < WIP[i] + Done[i])
				maxWD = WIP[i] + Done[i];
		}
	}
	
	private void initialize(){
		alarm = getResources().getDrawable(R.drawable.briefing_alarm); 
		alarm_center_x = new int[max_alarms];
		alarm_center_y = new int[max_alarms];
		
		//Calculate date difference
		long tmp = (currentdate.getTimeInMillis()-dateofchanges[0].getTimeInMillis())/24/3600/1000;
		nLines = numberofLinestoDraw((int)tmp);
		dday = (int)(tmp/nLines + 1);
		dday_Millis = (long)dday*24*3600*1000;
		
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
		
		flagtext_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		flagtext_paint.setStyle(Paint.Style.FILL);
		flagtext_paint.setTypeface(Typeface.create((String)null, Typeface.BOLD));
		flagtext_paint.setColor(flag_text_c);
		flagtext_paint.setTextSize(25);	
		flagtext_paint.setTextAlign(Align.LEFT);
		
		donewippath_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		donewippath_paint.setStyle(Paint.Style.FILL);
		donewippath_paint.setColor(donewippath_c);
		donewippath = new Path();
		
		donepath_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		donepath_paint.setStyle(Paint.Style.FILL);
		donepath_paint.setColor(donepath_c);
		donepath = new Path();
		
		toptext_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		toptext_paint.setStyle(Paint.Style.FILL);
		toptext_paint.setTypeface(Typeface.create((String)null, Typeface.BOLD));
		toptext_paint.setColor(toptext_c);
		toptext_paint.setTextSize(20);
		toptext_paint.setTextAlign(Align.CENTER);

		overwippath_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		overwippath_paint.setStyle(Paint.Style.FILL);
		overwippath_paint.setColor(overwippath_c);
		overwippath = new Path();
		
		mask_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mask_paint.setStyle(Paint.Style.FILL);
		mask_paint.setColor(background_c);
		overwip_maskpath = new Path();
		

		centerline_dx = (centerline_x2 - centerline_x1)/(nLines+1);
		int[] pointstofill_x = new int[2*number_of_dates];
		int[] pointstofill_y = new int[2*number_of_dates];
		double day_dx = centerline_dx/ (double)dday;
		int graph_x2 = centerline_x1 + (centerline_x2 - centerline_x1)/(nLines+1)*nLines;
		double dy = (graph_y2 - graph_y1)/maxWD;
		double close_alarm = Double.MIN_VALUE;
		for(int i = 0; i < number_of_dates; i++)
		{
			double point_x1 = graph_x2 - 
					(int)(((currentdate.getTimeInMillis() - dateofchanges[i].getTimeInMillis())/24/3600/1000) * day_dx);
			pointstofill_x[i] = (int)point_x1;
			pointstofill_x[2*number_of_dates-i-1] = (int)point_x1;
			pointstofill_y[i] = (int)(graph_y2 - (Done[i] + WIP[i])*dy);
			pointstofill_y[2*number_of_dates-i-1] = (int)(graph_y2 - Done[i]*dy);
			
			if (point_x1 - close_alarm >= 2*alarm_x && WIP[i] >= alarm_WIP){
				alarm_center_x[number_of_alarms] = (int)point_x1;
				alarm_center_y[number_of_alarms] = (int)(graph_y2 - (Done[i] + WIP[i])*dy) - alarm_y/2;
				close_alarm = point_x1; 
				number_of_alarms++;
			}
			
		}

		flags = new RectF[3];
		flags[0] = new RectF(x(flag_x1), y(flag_y1), x(flag_x2), y(flag_y2));
		flags[1] = new RectF(x(flag_x1 + flag_dx), y(flag_y1), x(flag_x2 + flag_dx), y(flag_y2));
		flags[2] = new RectF(x(flag_x1 + 2*flag_dx), y(flag_y1), x(flag_x2 + 2*flag_dx), y(flag_y2));
		
		
		int pathstart_x = (pointstofill_y[1] == pointstofill_y[0]) ? leftline_x :
			Math.max( leftline_x, pointstofill_x[0] - (pointstofill_y[0] - bottomline_y) * 
					(pointstofill_x[1] - pointstofill_x[0]) / (pointstofill_y[1] - pointstofill_y[0]));
	
		int pathstart_y = (pathstart_x != leftline_x) ? bottomline_y : 
			pointstofill_y[0] - (pointstofill_y[1] - pointstofill_y[0]) / (pointstofill_x[1] - pointstofill_x[0]) 
				* (pointstofill_x[0] - leftline_x) ;
		
		donewippath.moveTo(x(pathstart_x), y(pathstart_y));
		for(int i = 0; i < number_of_dates; i++)
			donewippath.lineTo(x(pointstofill_x[i]), y(pointstofill_y[i]));
		donewippath.lineTo(x(graph_x2), y(pointstofill_y[number_of_dates -1]));
		donewippath.lineTo(x(graph_x2), y(pointstofill_y[number_of_dates]));
		for(int i = number_of_dates; i < number_of_dates*2; i++)
			donewippath.lineTo(x(pointstofill_x[i]), y(pointstofill_y[i]));
		donewippath.lineTo(x(pathstart_x), y(pointstofill_y[number_of_dates*2-1]));
		donewippath.lineTo(x(pathstart_x), y(pathstart_y));
		
		donepath.moveTo(x(graph_x2), y(graph_y2));
		donepath.lineTo(x(graph_x2), y(pointstofill_y[number_of_dates]));
		for(int i = 0; i < number_of_dates; i++)
			donepath.lineTo(x(pointstofill_x[number_of_dates + i]), y(pointstofill_y[number_of_dates + i]));
		donepath.lineTo(x(pointstofill_x[number_of_dates*2-1]), y(graph_y2));
		donepath.lineTo(x(graph_x2), y(graph_y2));
		
		overwippath.moveTo(x(graph_x2), y(graph_y1));
		overwippath.lineTo(x(graph_x2), y(Math.max(graph_y1, (int)(pointstofill_y[number_of_dates] - dy * maxWIP))));
		for(int i = 0; i < number_of_dates; i++)
			overwippath.lineTo(x(pointstofill_x[number_of_dates + i]), 
					y(Math.max(graph_y1, (int)(pointstofill_y[number_of_dates + i] - dy * maxWIP))));
		overwippath.lineTo(x(pointstofill_x[number_of_dates*2-1]), y(graph_y1));
		overwippath.lineTo(x(graph_x2), y(graph_y1));

		overwip_maskpath.moveTo(x(pathstart_x), y(pathstart_y));
		for(int i = 0; i < number_of_dates; i++)
			overwip_maskpath.lineTo(x(pointstofill_x[i]), y(pointstofill_y[i]));
		overwip_maskpath.lineTo(x(graph_x2), y(pointstofill_y[number_of_dates -1]));
		overwip_maskpath.lineTo(x(graph_x2), y(graph_y1));
		overwip_maskpath.lineTo(x(pathstart_x), y(graph_y1));
		overwip_maskpath.lineTo(x(pathstart_x), y(pathstart_y));
		
		
	}
}
