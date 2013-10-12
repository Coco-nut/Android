package com.egoists.coco_nut.android.project;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.widget.ListView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.BaasioDialogFactory;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.kth.baasio.callback.BaasioQueryCallback;
import com.kth.baasio.entity.BaasioBaseEntity;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.query.BaasioQuery;
import com.kth.baasio.query.BaasioQuery.ORDER_BY;

@EActivity(R.layout.activity_project_invitation)
public class ProjectInvitationActivity extends Activity {
    
    private UsersListAdapter mListAdapter;
    private Context mContext;
    
    @AfterViews
    void showUserList() {
        mContext = this;
        mListAdapter = new UsersListAdapter(mContext, new ArrayList<BaasioUser>());
        ListView list = (ListView)findViewById(R.id.list);  
        list.setAdapter(mListAdapter);
    }

    @Click({R.id.btnProjInvGetAllUsers})
    void getMyFriendsByBaasio() {
        BaasioQuery query = new BaasioQuery();
        query.setType(BaasioUser.ENTITY_TYPE);
        query.setOrderBy(BaasioBaseEntity.PROPERTY_MODIFIED, ORDER_BY.DESCENDING);
        query.queryInBackground(new BaasioQueryCallback() { // 질의 요청

            @Override
            public void onResponse(List<BaasioBaseEntity> entities, List<Object> list, BaasioQuery query, long timestamp) {
                AndLog.d("Succeed : get userlist");
                
                List<BaasioUser> users = BaasioBaseEntity.toType(entities, BaasioUser.class);
                refreshUserList(users);
            }

            @Override
            public void onException(BaasioException e) {
                AndLog.e(e.getErrorCode() + " : " + e.getErrorDescription());
                BaasioDialogFactory.createErrorDialog(mContext, e).show();
            }
        });
    }
    
    @UiThread
    void refreshUserList(List<BaasioUser> users) {
        mListAdapter.update(users);
    }
}
