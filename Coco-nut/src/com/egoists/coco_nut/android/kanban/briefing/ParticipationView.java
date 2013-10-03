package com.egoists.coco_nut.android.kanban.briefing;

import java.util.Calendar;

import com.egoists.coco_nut.android.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;

public class ParticipationView extends View  {
	
	//Dummy Dataset : will be achieved from server later
	
	
	//x y cordinated of things: will be scaled by screen definitions
	final int top_margin = 137;
	final int chart_height = 21 + 29*14 + 8;
	
	Point resolution;
	int nLines;
	Drawable Test;
	
	public ParticipationView(Context context){
		super(context);
		Test = getResources().getDrawable(R.drawable.test);
		Test.setBounds(0,top_margin,720,720+top_margin);
		//get screen resolution
		resolution = new Point();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(resolution);
		
		//Initializations
		initializePaints();
		setBackgroundColor(Color.WHITE);
		setMinimumHeight(y(Math.max(1015, top_margin + chart_height + 250)));
		

	}
	public void onDraw(Canvas canvas){
		Test.draw(canvas);
	}
	
	private int x(int x){
		return x * resolution.x / 720;
	}
	private int y(int y){
		return y * resolution.y / 1280;
	}

	
	private void initializePaints(){
	}
}
