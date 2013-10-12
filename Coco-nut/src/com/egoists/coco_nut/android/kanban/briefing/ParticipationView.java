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
	final int number_of_people = 12;
	final double[] participation_ratio = {0.09, 0.09, 0.09, 0.09, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08, 0.08};
	
	//x y cordinated of things: will be scaled by screen definitions
	final int top_margin = 137;
	final int chart_height = 21 + 29*14 + 8;
	
	final double start_angle = -60;
	final int center_x = 360;
	final int center_y = 510;
	final int circle_radius_outter = 200;
	final int circle_radius_inner = 125;
	final int circle_radius_center = 112;
	final int[] outter_color={Color.parseColor("#F4C9C3"),Color.parseColor("#F58D7A"),
			Color.parseColor("#AFCCE1"),Color.parseColor("#728FAF"),
			Color.parseColor("#BCE0C7"),Color.parseColor("#79C799"),
			Color.parseColor("#C7B8D7"),Color.parseColor("#9E8BB0"),
			Color.parseColor("#F9AE68"),Color.parseColor("#D0B998"),
			Color.parseColor("#BCBEC0"),Color.parseColor("#999999")};
	final int[] inner_color={Color.parseColor("#F5958C"),Color.parseColor("#C67166"),
			Color.parseColor("#6FAACE"),Color.parseColor("#5D768C"),
			Color.parseColor("#84C992"),Color.parseColor("#69A883"),
			Color.parseColor("#AD97C8"),Color.parseColor("#786F89"),
			Color.parseColor("#E99754"),Color.parseColor("#A89780"),
			Color.parseColor("#A2A6AA"),Color.parseColor("#787878")};
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
	

	
	Drawable face;
	Point resolution;
	int nLines;
	
	public ParticipationView(Context context){
		super(context);
		//get screen resolution
		resolution = new Point();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(resolution);
		
		//Initializations
		initialize();
		face = getResources().getDrawable(R.drawable.briefing_face);
		setBackgroundColor(Color.WHITE);
		setMinimumHeight(y(Math.max(1010, top_margin + chart_height + 250)));
		

	}
	public void onDraw(Canvas canvas){
		
		double cumul_angle = start_angle;
		for(int i=0; i<number_of_people; i++)
		{
			canvas.drawLine(x(center_x), y(center_y), x(people_center_x[i]), y(people_center_y[i]), people_line_paint[i]);
			canvas.drawCircle(x(people_center_x[i]), y(people_center_y[i]), x(circle_radius_people), circle_outter_paint[i]);
			face.setBounds(x(people_center_x[i] - circle_radius_people/2), y(people_center_y[i]) - x(circle_radius_people/2),
					x(people_center_x[i] + circle_radius_people/2), y(people_center_y[i]) + x(circle_radius_people)/2 );
			face.draw(canvas);
			canvas.drawArc(circle_rect_outter, (float) cumul_angle, (float) (participation_ratio[i] * 360), true, circle_outter_paint[i]);
			canvas.drawArc(circle_rect_inner, (float) cumul_angle, (float) (participation_ratio[i] * 360), true, circle_inner_paint[i]);
			canvas.drawText((int)(participation_ratio[i]*100)+"%", x(ratio_text_x[i]), y(ratio_text_y[i]), ratio_text_paint);
			cumul_angle = cumul_angle + participation_ratio[i] * 360;
		}
		canvas.drawCircle(x(center_x), y(center_y), x(circle_radius_center), circle_center_paint);
	}
	
	private int x(int x){
		return x * resolution.x / 720;
	}
	private int y(int y){
		return y * resolution.y / 1280;
	}

	
	private void initialize(){
		circle_rect_outter = new RectF(x(center_x-circle_radius_outter), y(center_y)-x(circle_radius_outter), 
				x(center_x+circle_radius_outter), y(center_y)+x(circle_radius_outter));
		circle_rect_inner = new RectF(x(center_x-circle_radius_inner), y(center_y)-x(circle_radius_inner), 
				x(center_x+circle_radius_inner), y(center_y)+x(circle_radius_inner));
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
