package com.egoists.coco_nut.android.board.event;

public class UpdateDuetoDateEvent {
    public int y;
    public int m;
    public int d;
    
    public UpdateDuetoDateEvent(int y, int m, int d) {
        this.y = y;
        this.m = m;
        this.d = d;
    }

}
