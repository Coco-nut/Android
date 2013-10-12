//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package com.egoists.coco_nut.android.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import com.egoists.coco_nut.android.R.id;
import com.egoists.coco_nut.android.R.layout;
import com.googlecode.androidannotations.api.SdkVersionHelper;

public final class ProjectSelectionActivity_
    extends ProjectSelectionActivity
{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_project_selection);
    }

    private void init_(Bundle savedInstanceState) {
    }

    private void afterSetContentView_() {
        mViewPager = ((ViewPager) findViewById(id.pager_project));
        {
            View view = findViewById(id.btnCreateProject);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ProjectSelectionActivity_.this.createNewProject();
                    }

                }
                );
            }
        }
        {
            View view = findViewById(id.btn_notices);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ProjectSelectionActivity_.this.goToKanban();
                    }

                }
                );
            }
        }
        {
            View view = findViewById(id.btn_setting);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ProjectSelectionActivity_.this.delLoginPreference();
                    }

                }
                );
            }
        }
        initViewPager();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        afterSetContentView_();
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        afterSetContentView_();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        afterSetContentView_();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (((SdkVersionHelper.getSdkInt()< 5)&&(keyCode == KeyEvent.KEYCODE_BACK))&&(event.getRepeatCount() == 0)) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    public static ProjectSelectionActivity_.IntentBuilder_ intent(Context context) {
        return new ProjectSelectionActivity_.IntentBuilder_(context);
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, ProjectSelectionActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public ProjectSelectionActivity_.IntentBuilder_ flags(int flags) {
            intent_.setFlags(flags);
            return this;
        }

        public void start() {
            context_.startActivity(intent_);
        }

        public void startForResult(int requestCode) {
            if (context_ instanceof Activity) {
                ((Activity) context_).startActivityForResult(intent_, requestCode);
            } else {
                context_.startActivity(intent_);
            }
        }

    }

}
