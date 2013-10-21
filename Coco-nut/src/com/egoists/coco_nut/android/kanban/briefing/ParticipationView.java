package com.egoists.coco_nut.android.kanban.briefing;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.board.BoardTabActivity;
import com.egoists.coco_nut.android.board.card.Card;
import com.egoists.coco_nut.android.board.card.Person;
import com.egoists.coco_nut.android.cache.ImageFetcher;
import com.kth.baasio.entity.user.BaasioUser;

public class ParticipationView extends RelativeLayout  {
	
	
	int number_of_people;
	double[] participation_ratio;
	ImageFetcher mImageFetcher;
	
	BoardTabActivity mActivity;
	
	//x y cordinated of things: will be scaled by screen definitions
	final int top_margin = 267;
	final int chart_height = 21 + 29*14 + 8;
	
	final double start_angle = -60;
	final int center_x = 360;
	final int center_y = top_margin + 273;
	final int circle_radius_outter = 185;
	final int circle_radius_inner = 117;
	final int circle_radius_center = 105;
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
	final int ratio_text_radius = 147;
	final int ratio_text_size = 25;
	Paint ratio_text_paint;
	int[] ratio_text_x;
	int[] ratio_text_y;
	ImageView[] faces;
	
	final int icon_y1 = top_margin / 3;
	final int icon_y2 = icon_y1 + 40;
	final int icon_x1 = 50;
	final int icon_x2 = icon_x1 + 28;
	final int icon_text_c = Color.argb(255, 94, 119, 142);
	final int icon_text_x = icon_x2 + 20;
	final int icon_text_y = icon_y2;
	final int icon_text_size = 33;
	Paint icontext_paint;
	Drawable icon;
	boolean loaded = false;
	Point resolution;
	int nLines;
	Drawable up;
	Drawable down;
	
	public ParticipationView(Context context){
		super(context);
		mActivity = (BoardTabActivity) context;
		mImageFetcher = new ImageFetcher(context);
		//get screen resolution
		resolution = new Point();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(resolution);
		
		//Initializations
		initialize();
		

	}
	public void refresh(){
		if (mActivity.mUsers != null && mActivity.mCards != null)
		{
			loadData();
			locate();
			loaded = true;
		}
	}
	public void onDraw(Canvas canvas){
		

		icon.draw(canvas);
		up.draw(canvas);
		down.draw(canvas);
		canvas.drawText("기여도 차트", x(icon_text_x), y(icon_text_y), icontext_paint);
		if (loaded)
		{
			
			double cumul_angle = start_angle;
			for(int i=0; i<number_of_people; i++)
			{
				canvas.drawLine(x(center_x), y(center_y), x(people_center_x[i]), y(people_center_y[i]), people_line_paint[i]);
				canvas.drawCircle(x(people_center_x[i]), y(people_center_y[i]), x(circle_radius_people), circle_outter_paint[i]);
				canvas.drawArc(circle_rect_outter, (float) cumul_angle, (float) (participation_ratio[i] * 360), true, circle_outter_paint[i]);
				canvas.drawArc(circle_rect_inner, (float) cumul_angle, (float) (participation_ratio[i] * 360), true, circle_inner_paint[i]);
				canvas.drawText((int)(participation_ratio[i]*100)+"%", x(ratio_text_x[i]), y(ratio_text_y[i]), ratio_text_paint);
				cumul_angle = cumul_angle + participation_ratio[i] * 360;
			}
			canvas.drawCircle(x(center_x), y(center_y), x(circle_radius_center), circle_center_paint);
		}
		else
		{
			Paint messagepaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			messagepaint.setStyle(Paint.Style.FILL);
			messagepaint.setColor(Color.BLACK);
			messagepaint.setTextSize(25);
			canvas.drawText("카드 또는 유저 로딩 안됨!", x(50), y(center_y), messagepaint);
		}
	}
	
	private int x(int x){
		return x * resolution.x / 720;
	}
	private int y(int y){
		return y * resolution.y / 1280;
	}
	private void loadData(){
		double W = 0.5;
		number_of_people = mActivity.mUsers.size();
		participation_ratio = new double[number_of_people];
		double normalization = 0;
		int i = 0;
		for (BaasioUser u : mActivity.mUsers)
		{
			participation_ratio[i] = 0;
			for (Card c : mActivity.mCards)
				if (c.status == 2)
					for (int j = 0; j < c.participants.size(); j++)
						if (c.participants.get(j).uuid.equals(u.getUuid().toString()))
							participation_ratio[i] += 
									Math.pow((1+W),c.importance)*c.participants.get(j).sumRate;
			normalization += participation_ratio[i];
			i++;
		}
		for (i=0; i< number_of_people; i++)
			if(normalization == 0)
				participation_ratio[i] = 1.0 / number_of_people;
			else
				participation_ratio[i] /= normalization;
		
	}
	private void locate(){
		people_center_x = new int[number_of_people];
		people_center_y = new int[number_of_people];
		ratio_text_x = new int[number_of_people];
		ratio_text_y = new int[number_of_people];
		people_line_paint = new Paint[number_of_people];
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
		if (faces != null)
		{
			for(int i=0; i< number_of_people; i++)
			{
				removeView(faces[i]);
			}
		}
		faces = new ImageView[number_of_people];
		for (int i=0; i< number_of_people; i++)
		{
			faces[i] = getImageView();
			if (mActivity.mUsers.get(i).getPicture() != null) 
				mImageFetcher.loadImage(mActivity.mUsers.get(i).getPicture(), faces[i], R.drawable.briefing_face);
			
			faces[i].setX(x(people_center_x[i] - 45));
			faces[i].setY(y(people_center_y[i]) - x(45));
			addView(faces[i]);
		}
	}
    private ImageView getImageView() {
        ImageView imgIcon = new ImageView(mActivity);
        
        // width = height = 40dp
        final float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (45 * scale + 0.5f);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pixels, pixels);
        // margin = 5dp
        pixels = pixels/8;
        layoutParams.setMargins(pixels, pixels, pixels, pixels);
        imgIcon.setLayoutParams(layoutParams);
        imgIcon.setImageResource(R.drawable.briefing_face);
        return imgIcon;
    }
	private void initialize(){
		setBackgroundColor(Color.WHITE);
		setMinimumHeight(y(Math.max(1050, top_margin + chart_height + 250)));
		
		icon = getResources().getDrawable(R.drawable.briefing_chart_icon);
		icon.setBounds(x(icon_x1), y(icon_y1), x(icon_x2), y(icon_y2));
		circle_rect_outter = new RectF(x(center_x-circle_radius_outter), y(center_y)-x(circle_radius_outter), 
				x(center_x+circle_radius_outter), y(center_y)+x(circle_radius_outter));
		circle_rect_inner = new RectF(x(center_x-circle_radius_inner), y(center_y)-x(circle_radius_inner), 
				x(center_x+circle_radius_inner), y(center_y)+x(circle_radius_inner));

		circle_center_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circle_center_paint.setStyle(Paint.Style.FILL);
		circle_center_paint.setColor(Color.WHITE);
		
		ratio_text_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		ratio_text_paint.setStyle(Paint.Style.FILL);
		ratio_text_paint.setTypeface(Typeface.create((String)null, Typeface.BOLD));
		ratio_text_paint.setColor(ratio_text_color);
		ratio_text_paint.setTextSize(ratio_text_size);	
		ratio_text_paint.setTextAlign(Align.CENTER);

		icontext_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		icontext_paint.setStyle(Paint.Style.FILL);
		icontext_paint.setTypeface(Typeface.create((String)null, Typeface.BOLD));
		icontext_paint.setColor(icon_text_c);
		icontext_paint.setTextSize(y(icon_text_size));	
		icontext_paint.setTextAlign(Align.LEFT);

		down = getResources().getDrawable(R.drawable.briefing_arrow_down);
		down.setBounds(x(330), y(996), x(390), y(1030));
		up = getResources().getDrawable(R.drawable.briefing_arrow_up);
		up.setBounds(x(330), y(16), x(390), y(50));
		
	}
}
