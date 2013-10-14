package com.egoists.coco_nut.android.kanban.card.detail;

import java.util.Calendar;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.kanban.card.Card;
import com.egoists.coco_nut.android.kanban.card.KanbanData;
import com.egoists.coco_nut.android.util.AndLog;

import android.app.Activity;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class FooterView extends LinearLayout {
	
	final int photos_x = 705;
	final int photos_y = 75;
	final int photos_d = 105;
	final int photos_diameter = 90;
	final int people_on_line = 6;
	final int buttons_y = 210;
	int line_num;
	int remain_num;
	
	Card card;
	Point resolution;
	public FooterView(final Context context, Card card, boolean showComments) {
		super(context);
		setBackgroundColor(Color.WHITE);
		resolution = new Point();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(resolution);
		setMinimumHeight(y(270));
		this.card = card;
		
		line_num = card.participants.size() / 6;
		remain_num = card.participants.size() % 6;
		
		LinearLayout buttons = new LinearLayout(context);
        buttons.setOrientation(LinearLayout.HORIZONTAL);
        final CheckListButtonView checkbutton = new CheckListButtonView(context, !showComments);
        buttons.addView(checkbutton);
        
		ViewGroup.LayoutParams lp = checkbutton.getLayoutParams();
		lp.width = x(358);
		lp.height = y(60);
		checkbutton.setLayoutParams(lp);
        
        
        LinearLayout space = new LinearLayout(context);
        space.setBackgroundColor(Color.WHITE);
		space.setMinimumWidth((int)(getResources().getDisplayMetrics().density * 2 + 0.5));
        space.setOrientation(LinearLayout.HORIZONTAL);
		buttons.addView(space);
		final CommentButtonView commentbutton = new CommentButtonView(context, showComments);
        buttons.addView(commentbutton);
        
		lp = commentbutton.getLayoutParams();
		lp.width = x(358);
		lp.height = y(60);
		commentbutton.setLayoutParams(lp);
        
        addView(buttons);
        buttons.setY(y(line_num * photos_d + buttons_y));
        
		buttons.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				commentbutton.toggle();
				checkbutton.toggle();
				
				((CardDetailActivity)context).switch_commentcheck();
			}
		});
		
	}
	public void onDraw(Canvas canvas){
		
		//사람들 사진
		for (int i = 0; i < remain_num; i++)
		{
			Drawable photo = card.participants.get(i).getPhoto(getResources());
			if (photo == null){
				photo = getResources().getDrawable(R.drawable.card_personphoto_default);
			}
			photo.setBounds(x(photos_x - i * photos_d - photos_diameter), y(photos_y), 
					x(photos_x - i * photos_d), y(photos_y + photos_diameter));
			photo.draw(canvas);
		}
		for (int i = 1; i <= line_num ; i ++)
		{
			for (int j = 0; j < people_on_line; j++)
			{
				Drawable photo = card.participants.get(i).getPhoto(getResources());
				if (photo == null){
					photo = getResources().getDrawable(R.drawable.card_personphoto_default);
				}
				photo.setBounds(x(photos_x - i * photos_d - photos_diameter), y(photos_y + j * photos_d), 
						x(photos_x - i * photos_d), y(photos_y + photos_diameter + j * photos_d));
				photo.draw(canvas);
			}
		}
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
}









