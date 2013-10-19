package com.egoists.coco_nut.android.board.event;


public class UpdateStartDateEvent {
    public int y;
    public int m;
    public int d;
    
    public UpdateStartDateEvent(int y, int m, int d) {
        this.y = y;
        this.m = m;
        this.d = d;
    }

}
