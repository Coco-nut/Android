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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;

public class CommentButtonView extends View {

	Drawable icon;
	Drawable arrow_down;
	Drawable arrow_up;
	Point resolution;
	
	boolean isup;
	public CommentButtonView(final Context context, boolean isup) {
		super(context);
		setBackgroundColor(Color.rgb(164, 192, 204));
		resolution = new Point();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(resolution);
		
		this.isup = isup;
		
		initialize();
	}
	public void toggle()
	{
		isup = !isup;
		invalidate();
	}
	public void onDraw(Canvas canvas){
		if (isup){
			arrow_down.setAlpha(0);
			arrow_up.setAlpha(255);
		}
		else{
			arrow_up.setAlpha(0);
			arrow_down.setAlpha(255);
		} 
		
		icon.draw(canvas);
		arrow_down.draw(canvas);
		arrow_up.draw(canvas);
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
	private void initialize(){
		icon = getResources().getDrawable(R.drawable.card_detail_comment_icon);
		icon.setBounds(x(25), y(10), x(76), y(51));

		arrow_down = getResources().getDrawable(R.drawable.card_detail_arrow_down);
		arrow_down.setBounds(x(308), y(20), x(348), y(41));
		
		arrow_up = getResources().getDrawable(R.drawable.card_detail_arrow_up);
		arrow_up.setBounds(x(308), y(20), x(348), y(41));
	}

}









