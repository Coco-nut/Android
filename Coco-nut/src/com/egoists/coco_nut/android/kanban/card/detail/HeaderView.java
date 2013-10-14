package com.egoists.coco_nut.android.kanban.card.detail;

import java.util.Calendar;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.kanban.card.Card;
import com.egoists.coco_nut.android.kanban.card.KanbanData;
import com.egoists.coco_nut.android.util.AndLog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;

public class HeaderView extends View {

	final int label_x = 17;
	final int label_y = 258;
	Paint label_paint;
	
	final int titletext_x = 50;
	final int titletext_y = 71;
	final int titletext_size = 45;
	Paint titletext_paint;
	final int titletext_c = Color.argb(255, 99, 99, 99);
	
	final int bet_title_and_subtitle_x = 14;
	int subtitletext_x;
	final int subtitletext_size = 22;
	Paint subtitletext_paint;
	final int subtitletext_y = titletext_y;
		
	final int clock_x1 = 54;
	final int clock_y1 = 84;
	final int clock_x2 = 94;
	final int clock_y2 = 120;
	Drawable clock;
	
	String clocktext;
	final int clocktext_x = 108;
	final int clock_text_y = 110;
	final int clock_text_size = 25;
	Paint clock_text_paint;
		
	Card card;
	Point resolution;
	public HeaderView(final Context context, Card card) {
		super(context);
		setBackgroundColor(Color.WHITE);
		resolution = new Point();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(resolution);
		setMinimumHeight(y(165));
		this.card = card;
		initialize();

	}
	public void onDraw(Canvas canvas){
		
		//첫줄
		canvas.drawText(card.title, x(titletext_x), y(titletext_y), titletext_paint);
		canvas.drawText(card.sub_title, x(subtitletext_x), y(subtitletext_y), subtitletext_paint);
		
		//두번째 줄
		clock.draw(canvas);
		canvas.drawText(clocktext, x(clocktext_x), y(clock_text_y), clock_text_paint);
		//TODO: label 그리기
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
	private void initialize(){
		titletext_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		titletext_paint.setStyle(Paint.Style.FILL);
		titletext_paint.setColor(titletext_c);
		titletext_paint.setTextSize(xy(titletext_size));
		
		subtitletext_x = titletext_x + ixy( (int) titletext_paint.measureText(card.title) ) 
				+ bet_title_and_subtitle_x;
		subtitletext_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		subtitletext_paint.setStyle(Paint.Style.FILL);
		subtitletext_paint.setColor(titletext_c);
		subtitletext_paint.setTextSize(xy(subtitletext_size));
		
		clock_text_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		clock_text_paint.setStyle(Paint.Style.FILL);
		clock_text_paint.setTextSize(xy(clock_text_size));
		clock_text_paint.setColor(titletext_c);
		clock = getResources().getDrawable(R.drawable.card_clock);
		clock.setBounds(x(clock_x1), y(clock_y1), x(clock_x2), y(clock_y2));
		clocktext = setClocktext();
		
		
	}
	
	private String setClocktext()
	{
		String[] AMPM = {"AM", "PM"}; 
		String[] DATE_OF_WEEK = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
		if(card.getstartdate() == null && card.getenddate() == null)
			return "";
		else if (card.getstartdate() == null){
			Calendar ed = card.getenddate();
			return "~ " + String.format("%02d", ed.get(Calendar.HOUR)) + ":" 
					+ String.format("%02d", ed.get(Calendar.MINUTE)) + " " 
					+ AMPM[ed.get(Calendar.AM_PM)] + "  " + ed.get(Calendar.YEAR) + "/" 
					+ String.format("%02d", (ed.get(Calendar.MONTH) + 1)) 
					+ "/" + String.format("%02d", ed.get(Calendar.DATE)) + "/" 
					+ DATE_OF_WEEK[ed.get(Calendar.DAY_OF_WEEK)]; 
		}
		else if (card.getenddate() == null){
			Calendar sd = card.getstartdate();
			return sd.get(Calendar.YEAR) + "/" + String.format("%02d", (sd.get(Calendar.MONTH) + 1)) + "/" 
					+ String.format("%02d", sd.get(Calendar.DATE)) + "/" 
					+ DATE_OF_WEEK[sd.get(Calendar.DAY_OF_WEEK)] + "  " 
					+ String.format("%02d", sd.get(Calendar.HOUR)) + ":" 
					+ String.format("%02d", sd.get(Calendar.MINUTE)) + " " 
					+ AMPM[sd.get(Calendar.AM_PM)] + " ~";
		}
		else if (card.getenddate().get(Calendar.YEAR) == card.getstartdate().get(Calendar.YEAR) 
				&& card.getenddate().get(Calendar.MONTH) == card.getstartdate().get(Calendar.MONTH)
				&& card.getenddate().get(Calendar.DATE) == card.getstartdate().get(Calendar.DATE)){
			Calendar sd = card.getstartdate();
			Calendar ed = card.getenddate();
			return String.format("%02d", sd.get(Calendar.HOUR)) + ":" 
					+ String.format("%02d", sd.get(Calendar.MINUTE)) 
					+ " ~ " + String.format("%02d", ed.get(Calendar.HOUR)) 
					+ ":" + String.format("%02d", ed.get(Calendar.MINUTE)) + " " 
					+ AMPM[ed.get(Calendar.AM_PM)] + "  " + ed.get(Calendar.YEAR) + "/" 
					+ String.format("%02d", (ed.get(Calendar.MONTH) + 1)) 
					+ "/" + String.format("%02d", ed.get(Calendar.DATE)) + "/" 
					+ DATE_OF_WEEK[ed.get(Calendar.DAY_OF_WEEK)]; 
		}
		else {
			Calendar sd = card.getstartdate();
			Calendar ed = card.getenddate();
			return  sd.get(Calendar.YEAR) + "/" + String.format("%02d", (sd.get(Calendar.MONTH) + 1)) 
					+ String.format("%02d", "/" + sd.get(Calendar.DATE)) 
					+ "/" + DATE_OF_WEEK[sd.get(Calendar.DAY_OF_WEEK)] 
					+ " ~ " + ed.get(Calendar.YEAR) + "/" 
					+ String.format("%02d", (ed.get(Calendar.MONTH) + 1)) 
					+ "/" + String.format("%02d", ed.get(Calendar.DATE)) + "/" 
					+ DATE_OF_WEEK[ed.get(Calendar.DAY_OF_WEEK)]; 
		}
	}
}









