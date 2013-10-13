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

public class FooterView extends View {
	
	final int photos_x = 49;
	final int photos_y = 136;
	final int photos_dx = 108;
	final int photos_diameter = 90;
	
	Card card;
	Point resolution;
	public FooterView(final Context context, Card card) {
		super(context);
		setBackgroundColor(Color.WHITE);
		resolution = new Point();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(resolution);
		setMinimumHeight(y(258));
		this.card = card;
	}
	public void onDraw(Canvas canvas){
		
		//사람들 사진
		for (int i = 0; i < card.participants.size(); i++)
		{
			Drawable photo = card.participants.get(i).photo;
			if (photo == null){
				photo = getResources().getDrawable(R.drawable.card_personphoto_default);
			}
			photo.setBounds(x(photos_x + i * photos_dx), y(photos_y), 
					x(photos_x + i * photos_dx + photos_diameter), y(photos_y + photos_diameter));
			photo.draw(canvas);
		}
	}
	
	private int x(int x){
		return x * resolution.x / 720;
	}
	private int y(int y){
		return y * resolution.y / 1280;
	}
}









