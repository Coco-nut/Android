package com.egoists.coco_nut.android.kanban.card.detail;

import java.util.ArrayList;
import java.util.Calendar;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.kanban.card.Comment;
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

public class CommentButtonView extends View {

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
	
	
	ArrayList<Comment> comments;
	Point resolution;
	public CommentButtonView(final Context context) {
		super(context);
		setBackgroundColor(Color.WHITE);
		resolution = new Point();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(resolution);
		setMinimumHeight(y(258));
		this.comments = comments;
		initialize();
		
		setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				context.startActivity(new Intent(context, 
						com.egoists.coco_nut.android.kanban.card.CardCreationActivity_.class));
			}
		});
	}
	public void onDraw(Canvas canvas){
		
		
		
		
		
		
	}
	
	private int x(int x){
		return x * resolution.x / 720;
	}
	private int y(int y){
		return y * resolution.y / 1280;
	}
	private void initialize(){
		

		
		titletext_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		titletext_paint.setStyle(Paint.Style.FILL);
		titletext_paint.setColor(titletext_c);
		titletext_paint.setTextSize(x(titletext_size));
		



		
		clock_comment_check_text_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		clock_comment_check_text_paint.setStyle(Paint.Style.FILL);
		clock_comment_check_text_paint.setTextSize(x(clock_comment_check_text_size));
		clock_comment_check_text_paint.setColor(titletext_c);
		clock = getResources().getDrawable(R.drawable.card_clock);
		clock.setBounds(x(clock_x1), y(clock_y1), x(clock_x2), y(clock_y2));
		
		comment_x = clocktext_x + (int) clock_comment_check_text_paint.measureText(clocktext) 
				+ bet_clocktext_and_comment_x;
		comment = getResources().getDrawable(R.drawable.card_comment);
		comment.setBounds(x(comment_x), y(comment_y1), x(comment_x + comment_width), y(comment_y2));
		commenttext_x = comment_x + comment_width + bet_comment_and_commenttext_x;
		

		
	}

}









