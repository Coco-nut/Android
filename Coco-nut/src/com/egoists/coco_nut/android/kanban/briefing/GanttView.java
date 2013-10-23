package com.egoists.coco_nut.android.kanban.briefing;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.board.BoardTabActivity;
import com.egoists.coco_nut.android.board.card.Card;
import com.egoists.coco_nut.android.util.AndLog;

public class GanttView extends View {
	
	BoardTabActivity mActivity;
	
	//Dummy Dataset : will be achieved from server later
	Calendar[] startdates;
	Calendar[] enddates;
	Calendar day_of_start;
	Calendar day_of_end;
	int dday;
	long dday_Millis;
	int number_of_cards;
	int[] category;
	int[] statuses;
	
	String[] category_name;
	int number_of_categories;
	//맨 처음 가능한 레이블 순서대로 used_categories가 들어오고, category와 category_name은 used_categories의 순서로 배치됨
	int[] used_categories;
	
	
	//x y cordinated of things: will be scaled by screen definitions
	final int top_margin = 267;
	int chart_height;
	
	final int leftline_w = 4;
	final int leftline_c = Color.argb(255, 217, 217, 217);
	final int leftline_x = 50;
	final int leftline_y1 = top_margin + 13;
	int leftline_y2;
	Paint leftline_paint;
	
	final int bottomline_w = 1;
	final int bottomline_c = Color.argb(255, 203, 203, 203);
	final int bottomline_x1 = leftline_x;
	final int bottomline_x2 = 689;
	Paint bottomline_paint;
	
	final int flag_x1 = leftline_x + 28;
	final int flag_x2 = leftline_x + 28 + 22;
	final int flag_dx = 200;
	int flag_y1;
	int flag_y2;
	final int flag_dy = 47;
	final int flag_text_c = Color.argb(255, 130, 130, 130);
	final int flag_text_x = leftline_x + 28 + 29;
	int flag_text_y;
	final int flag_text_size = 25;
	Paint flagtext_paint;
	Drawable[] flag;

	final int centerline_w = 1;
	final int centerline_c = Color.argb(255, 236, 236, 236);
	final int centerline_x1 = leftline_x + 2;
	final int centerline_x2 = 740;
	double centerline_dx;
	Paint centerline_paint;
	
	final int toptext_c = Color.argb(255, 217, 217, 217);
	final int toptext_size = 20;
	Paint toptext_paint;
	
	final int label_y1 = leftline_y1 + 21;
	final int label_dy = 29;
	final int label_h = 14;
	final int label_w = 918;
	Bitmap[] label;
	Bitmap[] label_cropped;
	Paint label_TODO_paint;
	Paint label_DONE_paint;
	
	final int icon_y1 = top_margin / 3;
	final int icon_y2 = icon_y1 + 40;
	final int icon_x2 = leftline_x + 28;
	final int icon_text_c = Color.argb(255, 94, 119, 142);
	final int icon_text_x = icon_x2 + 20;
	final int icon_text_size = 33;
	Paint icontext_paint;
	Drawable icon;
	Drawable down;
	
	int[] label_x1;
	int[] label_x2;
	
	Point resolution;
	int nLines;
	
	boolean loaded = false;
	public GanttView(Context context){
		super(context);
		mActivity = (BoardTabActivity) context;
		
		//get screen resolution
		resolution = new Point();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(resolution);

		setBackgroundColor(Color.WHITE);
		//Initializations
		initialize();
	}
	public void refresh(){
		if (mActivity.mCards != null)
		{
			loadData();
			locate();
			loaded = true;
		}
	}
	public void onDraw(Canvas canvas){

		icon.draw(canvas);
		down.draw(canvas);
		canvas.drawText("간트 차트", x(icon_text_x), y(icon_y2), icontext_paint);
		
		if(loaded)
		{
			//Draw leftLine
			canvas.drawLine( x(leftline_x), y(leftline_y1), x(leftline_x), y(leftline_y2), leftline_paint);
			
			//Draw bottomLine
			canvas.drawLine( x(bottomline_x1), y(leftline_y2), x(bottomline_x2), y(leftline_y2), bottomline_paint);
			
			//Draw centerLines
			if(nLines != 1)
				for(int i = 1; i <= nLines; i++)
					canvas.drawLine(x((int)(centerline_x1 + centerline_dx*i)), y(leftline_y1), 
							x((int)(centerline_x1 + centerline_dx*i)), y(leftline_y2), centerline_paint);
			
			//Draw topTexts
			Calendar tmp = Calendar.getInstance();
			for(int i = 0; i< nLines; i++){
				tmp.setTimeInMillis(day_of_end.getTimeInMillis()-dday_Millis*i);
				canvas.drawText((tmp.get(Calendar.MONTH)+1)+"."+tmp.get(Calendar.DATE),
						x((int)(centerline_x1 + (nLines-i)*centerline_dx)), y(top_margin),toptext_paint);
			}
			
			//Draw Labels_Done
			int label_count = 0;
			for(int i = 0; i< number_of_cards; i++)
				if (statuses[i] == 2){
					canvas.drawBitmap(label_cropped[i], x(label_x1[i]), y(label_y1 + label_count * label_dy), label_DONE_paint);
					label_count ++;
				}
	
			//Draw Labels_Doing
			for(int i = 0; i< number_of_cards; i++)
				if (statuses[i] == 1){
					canvas.drawBitmap(label_cropped[i], x(label_x1[i]), y(label_y1 + label_count * label_dy), null);
					label_count ++;
				}
	
			//Draw Labels_ToDo
			for(int i = 0; i< number_of_cards; i++)
				if (statuses[i] == 0){
					canvas.drawBitmap(label_cropped[i], x(label_x1[i]), y(label_y1 + label_count * label_dy), label_TODO_paint);
					label_count ++;
				}
			
			//Draw Flags and FlagTexts
			for(int i = 0; i< number_of_categories; i++){
				flag[i].draw(canvas);
				canvas.drawText(category_name[i], x(flag_text_x + i%3*flag_dx), y(flag_text_y + i/3*flag_dy), flagtext_paint);
			}
		}
		else
		{
			canvas.drawText("카드 로딩 안됨!", x(bottomline_x1), y(leftline_y2), flagtext_paint);
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

	private void loadData(){
		number_of_cards = mActivity.mCards.size();
		AndLog.e("number_of_cards : " + number_of_cards);
		startdates = new Calendar[number_of_cards];
		enddates = new Calendar[number_of_cards];
		category = new int[number_of_cards];
		statuses = new int[number_of_cards];
		
		day_of_start = Calendar.getInstance();
		day_of_end = Calendar.getInstance();
		day_of_start.set(Calendar.YEAR, 3000);
		day_of_end.set(Calendar.YEAR, 1000);
		
		ArrayList<Integer> categories = new ArrayList<Integer>();
		
		for(int i = 0; i< number_of_cards; i++)
		{
			startdates[i] = mActivity.mCards.get(i).startdate;
			if (startdates[i] == null)
				startdates[i] = day_of_start;	
			if (mActivity.mCards.get(i).enddate != null)
			{
				enddates[i] = (Calendar) mActivity.mCards.get(i).enddate.clone();
				enddates[i].setTimeInMillis(enddates[i].getTimeInMillis() + 3600*1000*24);
			}
			else
				enddates[i] = day_of_end;	
			if(day_of_start.compareTo(startdates[i]) > 0)
				day_of_start.setTimeInMillis(startdates[i].getTimeInMillis());
			if(day_of_end.compareTo(enddates[i]) < 0)
				day_of_end.setTimeInMillis(enddates[i].getTimeInMillis());
			
			category[i] = mActivity.mCards.get(i).label;
			if (!categories.contains(category[i]))
				categories.add(category[i]);
			statuses[i] = mActivity.mCards.get(i).status;
			AndLog.e("status of card" + statuses[i]);
		}
		
		int[] category_map = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		String[] labels = getResources().getStringArray(R.array.selectedCardLabel);
  		number_of_categories = categories.size();
		used_categories = new int[number_of_categories];
		category_name = new String[number_of_categories];
		
		for(int i = 0; i< number_of_categories; i++)
		{
			used_categories[i] = category_map[categories.get(i)];
			category_name[i] = labels[categories.get(i)];
		}
		for(int i = 0; i< number_of_cards; i++)
			category[i] = categories.indexOf(category[i]);
		

		flag = new Drawable[number_of_categories];
		label = new Bitmap[number_of_categories];

		//load flags and labels
		for (int i=0; i < number_of_categories; i++){
			int tid = getResources().getIdentifier("briefing_flag_"+used_categories[i],"drawable",mActivity.getPackageName());
			flag[i] = getResources().getDrawable(tid); 
			
			tid = getResources().getIdentifier("briefing_label_"+used_categories[i],"drawable",mActivity.getPackageName());
			label[i] = Bitmap.createScaledBitmap(
					BitmapFactory.decodeResource(getResources(), tid), x(label_w), y(label_h), false);
		}		
	}
	
	private void initialize(){
		
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
		toptext_paint.setTextSize(y(toptext_size));	
		toptext_paint.setTextAlign(Align.CENTER);
		
		flagtext_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		flagtext_paint.setStyle(Paint.Style.FILL);
		flagtext_paint.setTypeface(Typeface.create((String)null, Typeface.BOLD));
		flagtext_paint.setColor(flag_text_c);
		flagtext_paint.setTextSize(y(flag_text_size));	
		flagtext_paint.setTextAlign(Align.LEFT);

		icontext_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		icontext_paint.setStyle(Paint.Style.FILL);
		icontext_paint.setTypeface(Typeface.create((String)null, Typeface.BOLD));
		icontext_paint.setColor(icon_text_c);
		icontext_paint.setTextSize(y(icon_text_size));	
		icontext_paint.setTextAlign(Align.LEFT);
		
		label_TODO_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		label_TODO_paint.setStyle(Paint.Style.FILL);
		label_TODO_paint.setColor(Color.argb(100, 0, 0, 0));
		
		label_DONE_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		label_DONE_paint.setStyle(Paint.Style.FILL);
		label_DONE_paint.setColor(Color.argb(100, 100, 100, 100));

		icon = getResources().getDrawable(R.drawable.briefing_chart_icon);
		down = getResources().getDrawable(R.drawable.briefing_arrow_down);
	}
	
	private void locate(){
		chart_height = 21 + 29*number_of_cards + 8;
		leftline_y2 = top_margin + chart_height + 13;
		flag_y1 = leftline_y1 + chart_height + 12;
		flag_y2 = leftline_y1 + chart_height + 12 + 29;
		flag_text_y = top_margin + chart_height + 48;
		
		setMinimumHeight(y(Math.max(1050, top_margin + chart_height + 250)));
		for (int i=0; i < number_of_categories; i++)
			flag[i].setBounds( x(flag_x1 + i%3*flag_dx), y(flag_y1 + i/3*flag_dy), x(flag_x2 + i%3*flag_dx), y(flag_y2 + i/3*flag_dy));
		
		icon.setBounds(x(leftline_x), y(icon_y1), x(icon_x2), y(icon_y2));
		down.setBounds(x(330), y(996), x(390), y(Math.max(1050, top_margin + chart_height + 250)-20));
		label_cropped = new Bitmap[number_of_cards];
		label_x1 = new int[number_of_cards];
		label_x2 = new int[number_of_cards];

		//Calculate date difference
		long tmp = (day_of_end.getTimeInMillis()-day_of_start.getTimeInMillis())/24/3600/1000;
		nLines = numberofLinestoDraw((int)tmp);
		dday = (int)(tmp/nLines + 1);
		dday_Millis = (long)dday*24*3600*1000;
		centerline_dx = (centerline_x2 - centerline_x1)/(nLines+1);

		//Crop labels
		for(int i = 0; i< number_of_cards; i++)
		{
			double day_dx = centerline_dx / (double)dday;
			int rightend = centerline_x1 + (centerline_x2 - centerline_x1)/(nLines+1)*nLines;
			label_x1[i] = rightend - (int)(((day_of_end.getTimeInMillis() - startdates[i].getTimeInMillis())/24/3600/1000) * day_dx);
			label_x2[i] = rightend - (int)(((day_of_end.getTimeInMillis() - enddates[i].getTimeInMillis())/24/3600/1000) * day_dx);
			int crop_x = label_x2[i]-label_x1[i]+1;
			crop_x = crop_x > 1 ? crop_x : 1;
			label_cropped[i] = Bitmap.createBitmap(label[category[i]],0,0, x(crop_x), y(label_h));
		}	
	}
}
