package com.egoists.coco_nut.android.kanban.briefing;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.view.View;
import android.view.WindowManager;

public class PeerView extends View {
	
	//Dummy Dataset : will be achieved from server later
	Calendar[] dateofchanges;
	int number_of_dates = 17;
	Calendar currentdate;
	int dday;
	long dday_Millis;
	int[] WIP = {1, 2, 2, 2, 2, 3, 5, 4, 5, 1, 4, 5, 6, 5, 3, 2, 1};
	int[] Done = {0, 0, 1, 2, 3, 3, 3, 4, 4, 8, 8, 8, 8, 9, 11, 12, 13};
	int maxWD;
	
	//맨 처음 가능한 레이블 순서대로 used_categories가 들어오고, category와 category_name은 used_categories의 순서로 배치됨
	int[] used_categories = {1, 2, 3, 4, 5, 6};
	
	
	//x y cordinated of things: will be scaled by screen definitions
	final int top_margin = 237;
	final int chart_height = 21 + 29*14 + 8;
	
	final int leftline_w = 4;
	final int leftline_c = Color.argb(100, 167, 167, 167);
	final int leftline_x = 31;
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

	final int graph_y1 = leftline_y1 + 21;
	final int graph_y2 = leftline_y2;
	final int doneline_w = 2;
	final int doneline_c = Color.argb(100, 0, 0, 0);
	Paint doneline_paint;
	final int wipline_w = 2;
	final int wipline_c = Color.argb(100, 255, 0, 0);
	Paint wipline_paint;
	
	final int donewippath_c = Color.argb(70, 0, 255, 0);
	Path donewippath;
	Paint donewippath_paint;
	final int donepath_c = Color.argb(70, 0, 0, 255);
	Path donepath;
	Paint donepath_paint;
	
	final int toptext_x1 = 85;
	final int toptext_x2 = 655;
	final int toptext_y = top_margin;
	final int toptext_c = Color.argb(100, 186, 186, 186);
	Paint toptext_paint;
	
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
		setMinimumHeight(y(Math.max(1010, top_margin + chart_height + 250)));
		
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
		
		/*//Draw graphLine
		int[] pointstofill_x = new int[2*number_of_dates];
		int[] pointstofill_y = new int[2*number_of_dates];
		double day_dx = centerline_dx/ (double)dday;
		int graph_x1 = centerline_x1 + (centerline_x2 - centerline_x1)/(nLines+1)*nLines;
		double point_x1 = graph_x1 - 
				(int)(((currentdate.getTimeInMillis() - dateofchanges[0].getTimeInMillis())/24/3600/1000) * day_dx);
		double dy = (graph_y2 - graph_y1)/maxWD;
		for(int i = 0; i < number_of_dates - 1; i++)
		{
			double point_x2 = graph_x1 - 
					(int)(((currentdate.getTimeInMillis() - dateofchanges[i+1].getTimeInMillis())/24/3600/1000) * day_dx);
			canvas.drawLine((int)point_x1, (int)(graph_y2 - Done[i]*dy), 
					(int)point_x2, (int)(graph_y2 - Done[i+1]*dy), doneline_paint);
			canvas.drawLine((int)point_x1, (int)(graph_y2 - (Done[i] + WIP[i])*dy), 
					(int)point_x2, (int)(graph_y2 - (Done[i+1] + WIP[i+1])*dy), wipline_paint);
			
			pointstofill_x[i] = (int)point_x1;
			pointstofill_x[2*number_of_dates-i-1] = (int)point_x1;
			pointstofill_y[i] = (int)(graph_y2 - (Done[i] + WIP[i])*dy);
			pointstofill_y[2*number_of_dates-i-1] = (int)(graph_y2 - Done[i]*dy);
			
			point_x1 = point_x2;
		}
		pointstofill_x[number_of_dates-1] = pointstofill_x[number_of_dates] = (int)point_x1;		
		pointstofill_y[number_of_dates-1] = (int)(graph_y2 - (Done[number_of_dates-1] + WIP[number_of_dates-1])*dy);
		pointstofill_y[number_of_dates] = (int)(graph_y2 - Done[number_of_dates-1]*dy);
		
		//Fill graph between done line and wip line
		donewippath.moveTo(pointstofill_x[0], pointstofill_y[0]);
		for(int i = 1; i < number_of_dates*2; i++)
			donewippath.lineTo(pointstofill_x[i], pointstofill_y[i]);
		donewippath.lineTo(pointstofill_x[0], pointstofill_y[0]);
		canvas.drawPath(donewippath, donewippath_paint);
		
		//Fill graph under done line
		donepath.moveTo(pointstofill_x[number_of_dates-1], graph_y2);
		for(int i = 0; i < number_of_dates; i++)
			donepath.lineTo(pointstofill_x[number_of_dates + i], pointstofill_y[number_of_dates + i]);
		donepath.lineTo(pointstofill_x[number_of_dates*2-1], graph_y2);
		donepath.lineTo(pointstofill_x[number_of_dates-1], graph_y2);
		canvas.drawPath(donepath, donepath_paint);*/
		
		
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
		int[] changedate = {1, 3, 7, 8, 11, 12, 14, 15, 18, 22, 24, 25, 26, 28, 29, 30, 1};
		int[] changemonth = {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 10};
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
		
		doneline_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		doneline_paint.setStyle(Paint.Style.STROKE);
		doneline_paint.setStrokeWidth(doneline_w);
		doneline_paint.setColor(doneline_c);
		
		wipline_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		wipline_paint.setStyle(Paint.Style.STROKE);
		wipline_paint.setStrokeWidth(wipline_w);
		wipline_paint.setColor(wipline_c);
		
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
	}
}
