package com.egoists.coco_nut.android.kanban.card.detail;

import java.util.UUID;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.kanban.card.Card;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.BaasioDialogFactory;
import com.egoists.coco_nut.android.util.LoginPreference;
import com.egoists.coco_nut.android.util.UniqueString;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.entity.group.BaasioGroup;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioException;

@EActivity(R.layout.activity_card_detail)
public class CardDetailActivity extends Activity {
    
	Card card;
	boolean showComments = false;
	LinearLayout linear_layout;
	CommentView comment;
	CheckListView checklist;
    
    @AfterViews
    void initForm() {
    	
        card = (Card)getIntent().getSerializableExtra("Card");
        
        HeaderView header = new HeaderView(this, card);
        TextView content = new TextView(this);
        content.setText(card.discription);
        int padding = (int)(getResources().getDisplayMetrics().density * 30 + 0.5);
        content.setPadding(padding, 0, padding, 0);
        content.setBackgroundColor(Color.WHITE);
        FooterView footer = new FooterView(this, card, showComments);
        
        linear_layout = (LinearLayout) findViewById(R.id.card_detail);
        
        linear_layout.addView(header);
        linear_layout.addView(content);
        linear_layout.addView(footer);
        add_commentcheck();
        
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    public void switch_commentcheck()
    {
    	showComments = !showComments;
    	if (showComments)
    		linear_layout.removeView(checklist);
    	else
    		linear_layout.removeView(comment);
    	add_commentcheck();
    }
    private void add_commentcheck()
    {
        if (showComments){
            comment = new CommentView(this, card.comments);
        	linear_layout.addView(comment);
        }
        else{
            checklist = new CheckListView(this, card.checkboxes);
        	linear_layout.addView(checklist);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_coconut, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.edit:
                editCard();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void editCard() {
		// TODO Auto-generated method stub
		
	}
}
