package com.egoists.coco_nut.android.kanban.card.detail;

import java.util.ArrayList;
import java.util.Calendar;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.kanban.card.Checkbox;
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

public class CheckListView extends View {
	
	ArrayList<Checkbox> checkboxes;
	Point resolution;
	public CheckListView(final Context context, ArrayList<Checkbox> checkboxes) {
		super(context);
		setBackgroundColor(Color.GREEN);
		resolution = new Point();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(resolution);
		setMinimumHeight(y(258));
		this.checkboxes = checkboxes;
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
	private void initialize(){
	}
	
}









