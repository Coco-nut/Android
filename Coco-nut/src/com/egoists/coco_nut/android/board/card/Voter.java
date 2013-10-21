package com.egoists.coco_nut.android.board.card;

import java.io.Serializable;

public class Voter implements Serializable {

    private static final long serialVersionUID = -8939677196894220534L;

    public static final String ENTITY_NAME_UUID       = "uuid";       // 사용자 UUID
    
    public String uuid;
    public Voter(String uuid) {
        this.uuid = uuid;
    }

}
