package com.egoists.coco_nut.android.kanban.briefing;

import java.util.Calendar;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.util.AndLog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;

public class GanttView extends View {
	
	//Dummy Dataset : will be achieved from server later
	Calendar[] startdates;
	Calendar[] enddates;
	Calendar day_of_start;
	Calendar day_of_end;
	int dday;
	long dday_Millis;
	int number_of_cards = 14;
	int[] category = {1, 2, 2, 2, 2, 3, 5, 4, 5, 1, 4, 5, 6, 5};
	
	String[] category_name = {"고궁 리서치", "고궁 답사", "중간 보고서 작성", "추가 자료 조사", "팀 회의", "기말 보고서 작성"};
	int number_of_categories = 6;
	//맨 처음 가능한 레이블 순서대로 used_categories가 들어오고, category와 category_name은 used_categories의 순서로 배치됨
	int[] used_categories = {1, 2, 3, 4, 5, 6};
	
	
	//x y cordinated of things: will be scaled by screen definitions
	final int top_margin = 237;
	final int chart_height = 21 + 29*number_of_cards + 8;
	
	final int leftline_w = 4;
	final int leftline_c = Color.argb(100, 167, 167, 167);
	final int leftline_x = 31;
	final int leftline_y1 = top_margin + 13;
	final int leftline_y2 = top_margin + chart_height + 13;
	
	final int bottomline_w = 1;
	final int bottomline_c = Color.argb(100, 154, 154, 154);
	final int bottomline_x1 = leftline_x;
	final int bottomline_x2 = 689;
	final int bottomline_y = leftline_y2;
	
	final int flag_x1 = 59;
	final int flag_x2 = 59 + 22;
	final int flag_dx = 200;
	final int flag_y1 = leftline_y1 + chart_height + 12;
	final int flag_y2 = leftline_y1 + chart_height + 12 + 29;
	final int flag_dy = 47;
	final int flag_text_c = Color.argb(100, 50, 50, 50);
	final int flag_text_x = 88;
	final int flag_text_y = top_margin + chart_height + 48;

	final int centerline_w = 1;
	final int centerline_c = Color.argb(100, 186, 186, 186);
	final int centerline_x1 = 33;
	final int centerline_x2 = 740;
	final int centerline_y1 = leftline_y1;
	final int centerline_y2 = leftline_y2;
	
	final int toptext_x1 = 85;
	final int toptext_x2 = 655;
	final int toptext_y = top_margin;
	
	final int label_y1 = leftline_y1 + 21;
	final int label_dy = 29;
	final int label_h = 14;
	final int label_w = 918;
	int[] label_x1;
	int[] label_x2;
	
	Point resolution;
	Paint paint;

	Drawable[] flag;
	Bitmap[] label;
	Bitmap[] label_cropped;
	int nLines;
	
	public GanttView(Context context){
		super(context);
		//instantiate dummy data
		instantiatedummydates();
		
		//get screen resolution
		resolution = new Point();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(resolution);
		
		//Initializations
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		flag = new Drawable[number_of_categories];
		label = new Bitmap[number_of_categories];
		label_cropped = new Bitmap[number_of_cards];
		label_x1 = new int[number_of_cards];
		label_x2 = new int[number_of_cards];
		setBackgroundColor(Color.WHITE);
		setMinimumHeight(Math.max(1000, top_margin + chart_height + 250));
		
		//load flags and labels
		for (int i=0; i < number_of_categories; i++){
			int tid = getResources().getIdentifier("briefing_flag_"+used_categories[i],"drawable",context.getPackageName());
			flag[i] = getResources().getDrawable(tid); 
			flag[i].setBounds( x(flag_x1 + i%3*flag_dx), y(flag_y1 + i/3*flag_dy), x(flag_x2 + i%3*flag_dx), y(flag_y2 + i/3*flag_dy));
			
			tid = getResources().getIdentifier("briefing_label_"+used_categories[i],"drawable",context.getPackageName());
			BitmapDrawable temp = (BitmapDrawable)getResources().getDrawable(tid);
			label[i] = Bitmap.createScaledBitmap(temp.getBitmap(), x(label_w), y(label_h), false);
		}
		
		//Calculate date difference
		long tmp = (day_of_end.getTimeInMillis()-day_of_start.getTimeInMillis())/24/3600/1000;
		nLines = numberofLinestoDraw((int)tmp);
		dday = (int)(tmp/nLines + 1);
		dday_Millis = (long)dday*24*3600*1000;

		//Crop labels
		for(int i = 0; i< number_of_cards; i++)
		{
			double day_dx = (double)(centerline_x2 - centerline_x1)/(nLines+1) / (double)dday;
			int rightend = centerline_x1 + (centerline_x2 - centerline_x1)/(nLines+1)*nLines;
			label_x1[i] = rightend - (int)(((day_of_end.getTimeInMillis() - startdates[i].getTimeInMillis())/24/3600/1000) * day_dx);
			label_x2[i] = rightend - (int)(((day_of_end.getTimeInMillis() - enddates[i].getTimeInMillis())/24/3600/1000) * day_dx);
			label_cropped[i] = Bitmap.createBitmap(label[category[i]-1],0,0, x(label_x2[i]-label_x1[i]), y(label_h));
		}
		
	}
	public void onDraw(Canvas canvas){
		
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(leftline_w);
		paint.setColor(Color.RED);
		paint.setColor(leftline_c);
		canvas.drawLine( x(leftline_x), y(leftline_y1), x(leftline_x), y(leftline_y2), paint);
		
		paint.setStrokeWidth(bottomline_w);
		paint.setColor(bottomline_c);
		canvas.drawLine( x(bottomline_x1), y(bottomline_y), x(bottomline_x2), y(bottomline_y), paint);
		
		paint.setStrokeWidth(centerline_w);
		paint.setColor(centerline_c);
		int centerline_dx = (centerline_x2 - centerline_x1)/(nLines+1);
		if(nLines != 1)
			for(int i = 1; i <= nLines; i++)
				canvas.drawLine(x(centerline_x1 + centerline_dx*i), y(centerline_y1), x(centerline_x1 + centerline_dx*i), y(centerline_y2), paint);
		
		paint.setStyle(Paint.Style.FILL);
		paint.setTypeface(Typeface.create((String)null, Typeface.BOLD));
		paint.setTextSize(20);	
		int toptext_dx = (toptext_x2 - toptext_x1)/(nLines-1);
		Calendar tmp = Calendar.getInstance();
		for(int i = 0; i< nLines; i++){
			tmp.setTimeInMillis(day_of_end.getTimeInMillis()-dday_Millis*i);
			canvas.drawText(tmp.get(Calendar.MONTH)+"."+tmp.get(Calendar.DATE),x(toptext_x1 + (nLines-i-1)*toptext_dx),y(toptext_y),paint);
		}
		
		for(int i = 0; i< number_of_cards; i++)
			canvas.drawBitmap(label_cropped[i], x(label_x1[i]), y(label_y1 + i * label_dy), null);
		
		paint.setColor(flag_text_c);
		paint.setTextSize(25);	
		for(int i = 0; i< number_of_categories; i++){
			flag[i].draw(canvas);
			canvas.drawText(category_name[i], x(flag_text_x + i%3*flag_dx), y(flag_text_y + i/3*flag_dy), paint);
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
		int[] startdate = {4, 15, 13, 11, 16, 21, 29, 5, 2, 5, 16, 13, 10, 21};
		int[] enddate = {17, 29, 26, 25, 29, 16, 6, 22, 9, 19, 3, 19, 30, 28};
		int[] startmonth = {9, 9, 9, 9, 9, 9, 9, 10, 9, 10, 9, 10, 10, 10};
		int[] endmonth = {9, 9, 9, 9, 9, 10, 10, 10, 9, 10, 10, 10, 10, 10};
		startdates = new Calendar[number_of_cards];
		enddates = new Calendar[number_of_cards];
		
		day_of_start = Calendar.getInstance();
		day_of_end = Calendar.getInstance();
		day_of_start.set(Calendar.YEAR, 3000);
		day_of_end.set(Calendar.YEAR, 1000);
		
		for(int i = 0; i< number_of_cards; i++)
		{
			startdates[i] = Calendar.getInstance();
			startdates[i].set(2013, startmonth[i], startdate[i]);
			enddates[i] = Calendar.getInstance();
			enddates[i].set(2013, endmonth[i], enddate[i]);
			if(day_of_start.compareTo(startdates[i]) > 0)
				day_of_start.setTimeInMillis(startdates[i].getTimeInMillis());
			if(day_of_end.compareTo(enddates[i]) < 0)
				day_of_end.setTimeInMillis(enddates[i].getTimeInMillis());
		}
	}
}
