package com.egoists.coco_nut.android.board.event;

public class SignupEvent {
    public String id;
    public String passwd;
    
    public SignupEvent(String id, String passwd) {
        this.id = id;
        this.passwd = passwd;
    }

}
