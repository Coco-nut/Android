package com.egoists.coco_nut.android.kanban.briefing;

import java.util.Calendar;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.util.AndLog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;

public class ParticipationView extends View  {
	
	//Dummy Dataset : will be achieved from server later
	final int number_of_people = 6;
	final double[] participation_ratio = {0.22, 0.23, 0.19, 0.12, 0.14, 0.10};
	
	//x y cordinated of things: will be scaled by screen definitions
	final int top_margin = 137;
	final int chart_height = 21 + 29*14 + 8;
	
	final double start_angle = -60;
	final int center_x = 360;
	final int center_y = 510;
	final int circle_radius_outter = 200;
	final int circle_radius_inner = 125;
	final int circle_radius_center = 112;
	final int[] outter_color={Color.argb(255, 120, 199, 153), Color.argb(255, 245, 141, 122),
			Color.argb(255, 249, 174, 103), Color.argb(255, 114, 143, 175), 
			Color.argb(255, 208, 185, 152), Color.argb(255, 158, 139, 176)};
	final int[] inner_color={Color.argb(255, 105, 168, 132), Color.argb(255, 200, 114, 103),
			Color.argb(255, 195, 133, 84), Color.argb(255, 94, 119, 142), 
			Color.argb(255, 169, 152, 130), Color.argb(255, 124, 113, 138)};
	RectF circle_rect_outter;
	Paint[] circle_outter_paint;
	RectF circle_rect_inner;
	Paint[] circle_inner_paint;
	Paint circle_center_paint;
	
	
	final int circle_radius_people = 50;
	final int people_line_length = circle_radius_outter + 105;
	final int people_line_width = 6;
	int[] people_center_x;
	int[] people_center_y;
	Paint[] people_line_paint;
	
	final int ratio_text_color = Color.WHITE;
	final int ratio_text_radius = 162;
	final int ratio_text_size = 25;
	Paint ratio_text_paint;
	int[] ratio_text_x;
	int[] ratio_text_y;
	

	
	
	Point resolution;
	int nLines;
	
	public ParticipationView(Context context){
		super(context);
		//get screen resolution
		resolution = new Point();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(resolution);
		
		//Initializations
		initialize();
		setBackgroundColor(Color.WHITE);
		setMinimumHeight(y(Math.max(1015, top_margin + chart_height + 250)));
		

	}
	public void onDraw(Canvas canvas){
		
		double cumul_angle = start_angle;
		for(int i=0; i<number_of_people; i++)
		{
			canvas.drawLine(center_x, center_y, people_center_x[i], people_center_y[i], people_line_paint[i]);
			canvas.drawCircle(people_center_x[i], people_center_y[i], circle_radius_people, circle_outter_paint[i]);
			canvas.drawArc(circle_rect_outter, (float) cumul_angle, (float) (participation_ratio[i] * 360), true, circle_outter_paint[i]);
			canvas.drawArc(circle_rect_inner, (float) cumul_angle, (float) (participation_ratio[i] * 360), true, circle_inner_paint[i]);
			canvas.drawText((int)(participation_ratio[i]*100)+"%", ratio_text_x[i], ratio_text_y[i], ratio_text_paint);
			cumul_angle = cumul_angle + participation_ratio[i] * 360;
		}
		canvas.drawCircle(center_x, center_y, circle_radius_center, circle_center_paint);
	}
	
	private int x(int x){
		return x * resolution.x / 720;
	}
	private int y(int y){
		return y * resolution.y / 1280;
	}

	
	private void initialize(){
		circle_rect_outter = new RectF(center_x-circle_radius_outter, center_y-circle_radius_outter, 
				center_x+circle_radius_outter, center_y+circle_radius_outter);
		circle_rect_inner = new RectF(center_x-circle_radius_inner, center_y-circle_radius_inner, 
				center_x+circle_radius_inner, center_y+circle_radius_inner);
		circle_outter_paint = new Paint[number_of_people];
		circle_inner_paint = new Paint[number_of_people];
		for(int i = 0; i < number_of_people; i++)
		{
			circle_outter_paint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
			circle_outter_paint[i].setStyle(Paint.Style.FILL);
			circle_outter_paint[i].setColor(outter_color[i]);
			circle_inner_paint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
			circle_inner_paint[i].setStyle(Paint.Style.FILL);
			circle_inner_paint[i].setColor(inner_color[i]);
		}
		circle_center_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circle_center_paint.setStyle(Paint.Style.FILL);
		circle_center_paint.setColor(Color.WHITE);
		
		people_center_x = new int[number_of_people];
		people_center_y = new int[number_of_people];
		ratio_text_x = new int[number_of_people];
		ratio_text_y = new int[number_of_people];
		people_line_paint = new Paint[number_of_people];
		double cumul_angle = start_angle;
		for(int i = 0; i< number_of_people; i++ )
		{
			people_line_paint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
			people_line_paint[i].setStyle(Paint.Style.STROKE);
			people_line_paint[i].setStrokeWidth(people_line_width);
			people_line_paint[i].setColor(outter_color[i]);
			people_center_x[i] = center_x + (int)
					( Math.cos(Math.toRadians( cumul_angle + participation_ratio[i] * 180 )) * people_line_length);
			people_center_y[i] = center_y + (int)
					( Math.sin(Math.toRadians( cumul_angle + participation_ratio[i] * 180 )) * people_line_length);
			
			ratio_text_x[i] = center_x + (int)
					( Math.cos(Math.toRadians( cumul_angle + participation_ratio[i] * 180 )) * ratio_text_radius);
			ratio_text_y[i] = center_y + ratio_text_size/2 + (int)
					( Math.sin(Math.toRadians( cumul_angle + participation_ratio[i] * 180 )) * ratio_text_radius);
			
			cumul_angle = cumul_angle + participation_ratio[i] * 360;
		}

		ratio_text_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		ratio_text_paint.setStyle(Paint.Style.FILL);
		ratio_text_paint.setTypeface(Typeface.create((String)null, Typeface.BOLD));
		ratio_text_paint.setColor(ratio_text_color);
		ratio_text_paint.setTextSize(ratio_text_size);	
		ratio_text_paint.setTextAlign(Align.CENTER);
	}
}
