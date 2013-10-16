package com.egoists.coco_nut.android.board.card;

import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.board.event.CardDetailEvent;
import com.egoists.coco_nut.android.util.AndLog;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_card_detail_all)
public class CardDetailActivity extends Activity {
    @ViewById
    TextView txtCardDetailTitle;
    @ViewById
    TextView txtCardDetailSubTitle;
    @ViewById
    TextView txtCardDetailDueTo;
    
    @AfterViews
    void initCard() {
        EventBus.getDefault().register(this);
        
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        actionBar.setTitle("카드 상세");
    }
    
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.only_back_coconut, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    ///////////////////////////////////////////////////////
    //  EventBus 관련 이벤트 처리부
    ///////////////////////////////////////////////////////
        
     // 서버로부터 다시 카드를 받는다
    public void onEvent(CardDetailEvent event) {
        AndLog.d("Recieved card detail event");
        txtCardDetailTitle.setText(event.card.title);
        txtCardDetailSubTitle.setText(event.card.sub_title);
    }
}
