package com.egoists.coco_nut.android.kanban.card;

import java.util.Calendar;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.util.AndLog;

import android.content.Context;
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

public class CardView extends View {

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
	
	final int bet_subtitle_and_stars_x = bet_title_and_subtitle_x;
	int stars_x1;
	final int stars_y1 = 49;
	final int stars_height = 19;
	final int stars_width = 23;
	final int stars_dx = stars_width + 10;
	Bitmap star;
	
	final int clock_x1 = 54;
	final int clock_y1 = 84;
	final int clock_x2 = 94;
	final int clock_y2 = 120;
	Drawable clock;
	
	String clocktext;
	final int clocktext_x = 108;
	final int clock_comment_check_text_y = 110;
	final int clock_comment_check_text_size = 25;
	Paint clock_comment_check_text_paint;
	
	final int bet_clocktext_and_comment_x = 24;
	int comment_x;
	final int comment_y1 = 79;
	final int comment_y2 = 118;
	final int comment_width = 43;
	Drawable comment;
	
	final int bet_comment_and_commenttext_x = 6;
	int commenttext_x;
	
	final int bet_commenttext_and_check_x = 17;
	int check_x;
	final int check_y1 = 77;
	final int check_y2 = 117;
	final int check_width = 46;
	Drawable check;
	
	final int bet_check_and_checktext_x = 4;
	int checktext_x;
	
	final int photos_x = 49;
	final int photos_y = 136;
	final int photos_dx = 108;
	final int photos_diameter = 90;
	
	
	Card card;
	Point resolution;
	public CardView(Context context, Card card) {
		super(context);
		setBackgroundColor(Color.WHITE);
		resolution = new Point();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(resolution);
		this.card = card;
		initialize();
	}
	public void onDraw(Canvas canvas){
		
		//첫줄
		canvas.drawRect(0, 0, x(label_x), y(label_y), label_paint);
		canvas.drawText(card.title, titletext_x, titletext_y, titletext_paint);
		canvas.drawText(card.sub_title, subtitletext_x, subtitletext_y, subtitletext_paint);
		for (int i = 0; i < card.importance; i++){
			canvas.drawBitmap(star, stars_x1 + stars_dx * i, stars_y1, label_paint);
		}
		
		//두번째 줄
		clock.draw(canvas);
		canvas.drawText(clocktext, clocktext_x, clock_comment_check_text_y, clock_comment_check_text_paint);
		comment.draw(canvas);
		canvas.drawText(Integer.toString(card.comments.size())
				, commenttext_x, clock_comment_check_text_y, clock_comment_check_text_paint);
		check.draw(canvas);
		canvas.drawText(Integer.toString(card.checkboxes.size())
				, checktext_x, clock_comment_check_text_y, clock_comment_check_text_paint);
		
		//사람들 사진
		for (int i = 0; i < card.participants.size(); i++)
		{
			Drawable photo = card.participants.get(i).photo;
			if (photo == null){
				photo = getResources().getDrawable(R.drawable.card_personphoto_default);
			}
			photo.setBounds(photos_x + i * photos_dx, photos_y, 
					photos_x + i * photos_dx + photos_diameter, photos_y + photos_diameter);
			photo.draw(canvas);
		}
	}
	
	
	
	private int x(int x){
		return x * resolution.x / 720;
	}
	private int y(int y){
		return y * resolution.y / 1280;
	}
	private void initialize(){
		
		label_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		label_paint.setStyle(Paint.Style.FILL);
		label_paint.setColor(Color.WHITE);
		ColorFilter filter = new LightingColorFilter(KanbanData.getLabelColor(card.label), 1);
		label_paint.setColorFilter(filter);
		
		titletext_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		titletext_paint.setStyle(Paint.Style.FILL);
		titletext_paint.setColor(titletext_c);
		titletext_paint.setTextSize(titletext_size);
		
		subtitletext_x = titletext_x + (int) titletext_paint.measureText(card.title) 
				+ bet_title_and_subtitle_x;
		subtitletext_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		subtitletext_paint.setStyle(Paint.Style.FILL);
		subtitletext_paint.setColor(titletext_c);
		subtitletext_paint.setTextSize(subtitletext_size);

		stars_x1 = subtitletext_x + (int) subtitletext_paint.measureText(card.sub_title) 
				+ bet_subtitle_and_stars_x;
		star = Bitmap.createScaledBitmap(
				BitmapFactory.decodeResource(getResources(), R.drawable.card_star), 
				x(stars_width), y(stars_height), false);
		
		clock_comment_check_text_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		clock_comment_check_text_paint.setStyle(Paint.Style.FILL);
		clock_comment_check_text_paint.setTextSize(clock_comment_check_text_size);
		clock_comment_check_text_paint.setColor(titletext_c);
		clock = getResources().getDrawable(R.drawable.card_clock);
		clock.setBounds(clock_x1, clock_y1, clock_x2, clock_y2);
		clocktext = setClocktext();
		
		comment_x = clocktext_x + (int) clock_comment_check_text_paint.measureText(clocktext) 
				+ bet_clocktext_and_comment_x;
		comment = getResources().getDrawable(R.drawable.card_comment);
		comment.setBounds(comment_x, comment_y1, comment_x + comment_width, comment_y2);
		commenttext_x = comment_x + comment_width + bet_comment_and_commenttext_x;
		
		check_x = commenttext_x 
				+ (int) clock_comment_check_text_paint.measureText(Integer.toString(card.comments.size())) 
				+ bet_commenttext_and_check_x;
		check = getResources().getDrawable(R.drawable.card_check);
		check.setBounds(check_x, check_y1, check_x + check_width, check_y2);
		checktext_x = check_x + check_width + bet_check_and_checktext_x;
		
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
			return String.format("%02d", ed.get(Calendar.HOUR)) + ":" 
					+ String.format("%02d", ed.get(Calendar.MINUTE)) 
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









