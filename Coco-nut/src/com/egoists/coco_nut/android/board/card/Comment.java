package com.egoists.coco_nut.android.board.card;

import java.io.Serializable;

public class Comment implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -3117912308614759403L;
    
    public static final String ENTITY_COMMENTER                 = "commenter";
    public static final String ENTITY_COMMENTER_UUID                 = "commenterUuid";
    public static final String ENTITY_COMMENTER_PICTURE                 = "commenterPicture";
    public static final String ENTITY_COMMENTE                 = "comment";
    public static final String ENTITY_TIME                 = "time";
    
    public String commenter;
    public String commenterUuid;
    public String commenterPicture;
    public String comment;
    public long time;
    
    public Comment(String commenter, String commenterUuid, String picture, String comment, long time) {
        this.comment = commenter;
        this.commenterUuid = commenterUuid;
        this.commenterPicture = picture;
        this.comment = comment;
        this.time = time;
    }

}
