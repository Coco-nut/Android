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

public class CommentView extends View {

	ArrayList<Comment> comments;
	Point resolution;
	public CommentView(final Context context, ArrayList<Comment> comments) {
		super(context);
		setBackgroundColor(Color.BLUE);
		resolution = new Point();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(resolution);
		setMinimumHeight(y(258));
		this.comments = comments;
		initialize();
		
	}
	public void onDraw(Canvas canvas){
		
		
		
		
		
		
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
		

		
	}

}









