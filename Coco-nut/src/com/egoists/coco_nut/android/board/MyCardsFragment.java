package com.egoists.coco_nut.android.board;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.egoists.coco_nut.android.R;
import com.egoists.coco_nut.android.board.adapter.CardListAdapter;
import com.egoists.coco_nut.android.login.LoginActivity;
import com.egoists.coco_nut.android.util.AndLog;
import com.egoists.coco_nut.android.util.BaasioDialogFactory;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.kth.baasio.callback.BaasioQueryCallback;
import com.kth.baasio.entity.BaasioBaseEntity;
import com.kth.baasio.entity.entity.BaasioEntity;
import com.kth.baasio.entity.group.BaasioGroup;
import com.kth.baasio.entity.user.BaasioUser;
import com.kth.baasio.exception.BaasioException;
import com.kth.baasio.query.BaasioQuery;

@EFragment
public class MyCardsFragment extends Fragment {
    private ProgressDialog mDialog;
    private final String RELATION_NAME          = "group_card";
    
    private CardListAdapter mListAdapter;
    Activity mContext;
    
    String mGroupUuid = null;
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // GROUP UUID 추출
        Bundle bundle = getArguments();
        if (bundle == null) {
            return null;
        }
        mGroupUuid = bundle.getString(BoardTabActivity.ARG_GROUP_UUID);
        AndLog.d("Group UUID : " + mGroupUuid);
        
        View view = inflater.inflate(R.layout.fragment_my_cards, container, false);
        
        // 카드 리스트
        mListAdapter = new CardListAdapter(mContext, new ArrayList<BaasioEntity>());
        ListView list = (ListView)view.findViewById(R.id.list);  
        list.setAdapter(mListAdapter);
        
        return view;
    }
    
    // 호출한 Activity와의 통신을 위함
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @AfterViews
    void calledAfterViewInjection() {
        getMyCardsByBaasio(mGroupUuid);
    }
    
    @UiThread
    void refreshCardList(List<BaasioEntity> cards) {
        mListAdapter.update(cards);
    }
    
    /**
     * 해당 그룹의 카드 리스트 추출
     * @param groupUuid
     */
    void getMyCardsByBaasio(String groupUuid) {
        mDialog = ProgressDialog.show(mContext, "", "카드 가져오는 중", true);
        BaasioGroup group = new BaasioGroup();
        group.setUuid(UUID.fromString(groupUuid));
        BaasioQuery query = new BaasioQuery();
        query.setRelation(
                group               // 그룹
                , RELATION_NAME);   // 관계 카드

        query.queryInBackground(new BaasioQueryCallback() {

            @Override
            public void onResponse(List<BaasioBaseEntity> entities, List<Object> list, BaasioQuery query, long timestamp) {
                mDialog.dismiss();
                // 성공
                List<BaasioEntity> cards = BaasioBaseEntity.toType(entities, BaasioEntity.class);
                
                refreshCardList(cards);
            }

            @Override
            public void onException(BaasioException e) {
                mDialog.dismiss();
                BaasioDialogFactory.createErrorDialog(mContext, e).show();
            }
        });
    }
}
