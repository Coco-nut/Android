package com.egoists.coco_nut.android.kanban.briefing;

import java.text.DecimalFormat;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.board.BoardTabActivity;
import com.egoists.coco_nut.android.board.card.Card;
import com.egoists.coco_nut.android.board.card.Person;
import com.egoists.coco_nut.android.cache.ImageFetcher;
import com.egoists.coco_nut.android.util.AndLog;
import com.kth.baasio.entity.user.BaasioUser;

public class PeerView extends RelativeLayout {
	

	final int[] outter_color={Color.parseColor("#F4C9C3"),Color.parseColor("#F58D7A"),
			Color.parseColor("#AFCCE1"),Color.parseColor("#728FAF"),
			Color.parseColor("#BCE0C7"),Color.parseColor("#79C799"),
			Color.parseColor("#C7B8D7"),Color.parseColor("#9E8BB0"),
			Color.parseColor("#F9AE68"),Color.parseColor("#D0B998"),
			Color.parseColor("#BCBEC0"),Color.parseColor("#999999")};
	final int top_margin = 267;
	final int chart_height = 21 + 29*14 + 8;
	
	final int leftline_x = 50;
	
	final int icon_y1 = top_margin / 3;
	final int icon_y2 = icon_y1 + 40;
	final int icon_x1 = leftline_x;
	final int icon_x2 = icon_x1 + 28;
	final int icon_text_c = Color.argb(255, 94, 119, 142);
	final int icon_text_x = icon_x2 + 20;
	final int icon_text_y = icon_y2;
	final int icon_text_size = 33;
	Paint icontext_paint;
	Drawable icon;
	Drawable up;

	final int picturecenter_x = icon_text_x + 25;
	final int picturecenter_y = icon_y2 + 100;
	final int picturecircleradius = 50;
	final int pictureradius = 45;
	final int people_name_x = 200;
	final int people_name_y = picturecenter_y + 10;
	final int people_rate_x = 300;
	final int star_x = people_name_x + 160;
	final int star_y = picturecenter_y - 37;
	
	final int people_dy = 120;
	Paint[] peoplepaint;
	ImageView[] pictures;
	String[] names;
	Bitmap[] star;
	
	int number_of_people;
	double[] ratings;
	
	ImageFetcher mImageFetcher;
	BoardTabActivity mActivity;
	//x y cordinated of things: will be scaled by screen definitions
	DecimalFormat df;
	
	Point resolution;
	boolean loaded = false;
	
	public PeerView(Context context){
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
		canvas.drawText("상호평가 점수", x(icon_text_x), y(icon_text_y), icontext_paint);
		if (loaded)
		{
			for(int i=0; i<number_of_people;i++)
			{
				canvas.drawCircle(x(picturecenter_x), y(picturecenter_y + people_dy*i), x(picturecircleradius), peoplepaint[i]);
				canvas.drawText(names[i], x(people_name_x), y(people_name_y+ people_dy*i), icontext_paint);
				
				canvas.drawText(df.format(ratings[i]), x(people_rate_x), y(people_name_y+ people_dy*i), icontext_paint);
				canvas.drawBitmap(star[i], x(star_x), y(star_y + people_dy*i),peoplepaint[i]);
			}
		}	
	}
	
	private int x(int x){
		return x * resolution.x / 720;
	}
	private int y(int y){
		return y * resolution.y / 1280;
	}
	
	
	private void loadData(){
		number_of_people = mActivity.mUsers.size();
		ratings = new double[number_of_people];
		double[] divider = new double[number_of_people];
		int i = 0;
		for (BaasioUser u : mActivity.mUsers)
		{
			ratings[i] = 0;
			divider[i] = 0;
			for (Card c : mActivity.mCards)
				if (c.status == 2)
					for (int j = 0; j < c.participants.size(); j++)
						if (c.participants.get(j).uuid.equals(u.getUuid())){
							ratings[i] += c.participants.get(j).sumRate;
							divider[i] ++;
						}
			i++;
		}
		for (i=0; i< number_of_people; i++)
			if(divider[i] == 0)
				ratings[i] = 5.0;
			else
				ratings[i] /= (divider[i] * (number_of_people - 1));
		
		names = new String[number_of_people];
		pictures = new ImageView[number_of_people];
		star = new Bitmap[number_of_people];
		for (i=0;i<number_of_people; i++)
		{
			BaasioUser person = mActivity.mUsers.get(i); 
			
			pictures[i] = getImageView();
			if (person.getPicture() != null)
				mImageFetcher.loadImage(person.getPicture(), pictures[i]);
			this.addView(pictures[i]);
			pictures[i].setX(x(picturecenter_x - pictureradius));
			pictures[i].setY(y(picturecenter_y - pictureradius +i*people_dy));
			
			names[i] = person.getName();
			star[i] = Bitmap.createScaledBitmap(
					BitmapFactory.decodeResource(getResources(), R.drawable.briefing_star), x(330), y(60), false);
			star[i] = Bitmap.createBitmap(star[i],0,0, x(getcuttingedge(ratings[i])), y(60));
		}
		setMinimumHeight(y(Math.max(1050, people_name_y + people_dy * number_of_people + 150)));
	}
	private void locate(){
	}
	
	private void initialize(){
		df = new DecimalFormat("#.0");
		setBackgroundColor(Color.WHITE);
		setMinimumHeight(y(1050));
		icontext_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		icontext_paint.setStyle(Paint.Style.FILL);
		icontext_paint.setTypeface(Typeface.create((String)null, Typeface.BOLD));
		icontext_paint.setColor(icon_text_c);
		icontext_paint.setTextSize(y(icon_text_size));	
		icontext_paint.setTextAlign(Align.LEFT);

		icon = getResources().getDrawable(R.drawable.briefing_chart_icon);
		icon.setBounds(x(icon_x1), y(icon_y1), x(icon_x2), y(icon_y2));
		
		up = getResources().getDrawable(R.drawable.briefing_arrow_up);
		up.setBounds(x(330), y(16), x(390), y(50));
		
		peoplepaint = new Paint[12];
		for (int i = 0; i< 12; i++)
		{
			peoplepaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
			peoplepaint[i].setStyle(Style.FILL);
			peoplepaint[i].setColor(outter_color[i]);
			peoplepaint[i].setColorFilter(new LightingColorFilter(outter_color[i], 1));
			
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
    private int getcuttingedge(double rating){
    	if (rating >= 5.0)
    		return 330;
    	else if(rating > 4.0)
    		return (int)(269 + (321.0-269.0)*(rating - 4.0));
    	else if(rating > 3.0)
    		return (int)(203 + (255.0-203.0)*(rating - 3.0));
    	else if(rating > 2.0)
    		return (int)(136 + (188.0-136.0)*(rating - 2.0));
    	else if(rating > 1.0)
    		return (int)(70 + (122.0-70.0)*(rating - 1.0));
    	else if(rating >= 0.0)
    		return (int)(4 + (56.0-4.0)*(rating));
    	else{
    		AndLog.e("Negative rating Error!!");
    		return 0;
    	}
    }
}















