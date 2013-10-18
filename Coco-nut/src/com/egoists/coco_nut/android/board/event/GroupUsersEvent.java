package com.egoists.coco_nut.android.board.event;

import java.util.List;

import com.kth.baasio.entity.user.BaasioUser;

public class GroupUsersEvent {
    public List<BaasioUser> users;

    public GroupUsersEvent(List<BaasioUser> users) {
        this.users = users;
    }

}
