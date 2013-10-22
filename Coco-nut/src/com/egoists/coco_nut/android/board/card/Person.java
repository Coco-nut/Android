package com.egoists.coco_nut.android.board.card;

import java.io.Serializable;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.egoists.coco_nut.android.R;
import com.kth.baasio.entity.user.BaasioUser;


public class Person implements Serializable {
    private static final long serialVersionUID = 1965186320993697142L;
    
    public static final String ENTITY_NAME_UUID       = "uuid";       // 사용자 UUID
    public static final String ENTITY_NAME_NAME       = "name";       // 사용자 이름
    public static final String ENTITY_NAME_PICTURE    = "picture";    // 사진
    public static final String ENTITY_NAME_PHONE      = "phone";      // 휴대폰 번호
    public static final String ENTITY_NAME_SUM_RATE   = "sumRate";    // 평가 점수
    
    public String uuid;
	public String name;
	public boolean isme;
	public String picture;
	public int sumRate;
	
	public Person(BaasioUser user, int sumRate) {
	    this(user.getUuid().toString(), user.getName(), user.getPicture(), false, sumRate);
	}
	
	public Person(String uuid, String name, String pictureUrl, boolean isme, int sumRate) {
	    this.uuid = uuid;
        this.name = name;
        this.picture = pictureUrl; 
        this.isme = isme;
        this.sumRate = sumRate;
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