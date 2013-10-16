package com.egoists.coco_nut.android.board.card;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.egoists.coco_nut.android.R;


public class Person {
    public static final String ENTITY_NAME_PICTURE    = "picture";      // 사진
    public static final String ENTITY_NAME_PHONE      = "phone";      // 휴대폰 번호
    
    public String uuid;
	public String name;
	public boolean isme;
	public String pictureUrl;
	
	public Person(String uuid, String name, String pictureUrl, boolean isme){
	    this.uuid = uuid;
        this.name = name;
        this.pictureUrl = pictureUrl; 
        this.isme = isme;
    }

    public static ImageView getImageView(Context con) {
        ImageView imgIcon = new ImageView(con);
        
        // width = height = 40dp
        final float scale = con.getResources().getDisplayMetrics().density;
        int pixels = (int) (40 * scale + 0.5f);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pixels, pixels);
        // margin = 5dp
        pixels = pixels/8;
        layoutParams.setMargins(pixels, pixels, pixels, pixels);
        imgIcon.setLayoutParams(layoutParams);
        imgIcon.setImageResource(R.drawable.card_personphoto_default);
        return imgIcon;
    }
}