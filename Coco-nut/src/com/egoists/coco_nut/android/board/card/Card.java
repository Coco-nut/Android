package com.egoists.coco_nut.android.board.card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;


public class Card implements Serializable {
    private static final long serialVersionUID = -3366308941296985726L;
    
    public static final String ENTITY                 = "card";
    public static final String ENTITY_NAME_TITLE      = "title";      // 카드 제목
    public static final String ENTITY_NAME_SUBTITLE   = "subtitle";   // 카드 부제목
    public static final String ENTITY_NAME_DESCRIPTION = "description";  // 상세 설명 
    public static final String ENTITY_NAME_RATING     = "importance"; // 중요도
    public static final String ENTITY_NAME_STATE      = "state";      // 카드 상태
    public static final String ENTITY_NAME_LABEL      = "label";      // 카드 레이블
    public static final String ENTITY_NAME_PARTY      = "participants";      // 참여자
    public static final String ENTITY_NAME_ISMINE      = "ismine";      // 나도 참여자
    public static final String ENTITY_NAME_START_DATE      = "startdate";
    public static final String ENTITY_NAME_DUETO_DATE      = "enddate";
    public static final String ENTITY_NAME_DOING_DATE   = "timeofdoing";    // 하는 중으로 이동한 시간
    public static final String ENTITY_NAME_DONE_DATE    = "timeofdone";     // 한 일로 이동한 시간
    public static final String ENTITY_NAME_VOTERS    = "voters";     // 평가자 정보
    
    public static final int ENTITY_VALUE_STATE        = 0;            // 0:todo, 1:doing, 2:done
    
    
    public String uuid;
    public String title;
    public String sub_title;
    public String discription;
    
    public int label = -1;
    public float importance;
    
    public Calendar startdate;
    private Calendar expectedstartdate;
    public Calendar enddate;
    private Calendar expectedenddate;
    
    public Calendar timeofdoing;
    public Calendar timeofdone;
    
    public ArrayList<Person> participants;
    public ArrayList<Voter> voters = null;
    
    public int status; //0:do, 1:doing, 2:done
    public boolean ismine;
    
    
    Card(String title, String sub_title, int importance, ArrayList<Person> participants)
    {
        //User creates new card
        this.title = title;
        this.sub_title = sub_title;
        this.importance = importance;
        this.participants = participants;
        this.status = 0;
        for (int i = 0; i< this.participants.size(); i++)
            if (this.participants.get(i).isme){
                this.ismine = true;
                break;
            }
        //TODO: report change to server
    }
    public Card(String title, String sub_title, String discription, int label, int importance,
            Calendar startdate, Calendar enddate, Calendar expectedstartdate, 
            Calendar expectedenddate,
            ArrayList<Person> participants, int status, boolean ismine)
            {
        //Cards loaded from server
        this.title = title;
        this.sub_title = sub_title;
        this.discription = discription;
        this.label = label;
        this.importance = importance;
        this.startdate = startdate;
        this.enddate = enddate;
        this.expectedstartdate = expectedstartdate;
        this.expectedenddate = expectedenddate;
        this.participants = participants;
        this.status = status;
        this.ismine = ismine;
            }
    public void editCard(String title, String sub_title, String discription, int label, int importance,
            Calendar startdate, Calendar enddate, ArrayList<Person> participants)
    {
        this.title = title;
        this.sub_title = sub_title;
        this.discription = discription;
        this.label = label;
        this.importance = importance;
        if (status == 0){
            this.expectedstartdate = startdate;
            this.expectedenddate = enddate;
        }
        else if (status == 1){
            this.startdate = startdate;
            this.expectedenddate = enddate;
        }
        else{
            this.startdate = startdate;
            this.enddate = enddate;
        }
        for (int i = 0; i< this.participants.size(); i++)
            if (this.participants.get(i).isme){
                this.ismine = true;
                break;
            }
        //TODO: report change to server
    }
    public void fromDotoDoing(Calendar startdate)
    {
        this.startdate = startdate;
        enddate = Calendar.getInstance();
        enddate.setTimeInMillis(startdate.getTimeInMillis() + 24 * 3600 * 1000);
        timeofdoing = Calendar.getInstance();
        this.status = 1;
        //TODO: report change to server
    }
    public void fromDoingtoDo()
    {
        this.startdate = null;
        this.enddate = null;
        timeofdoing = null;
        this.status = 0;
        //TODO: report change to server
    }
    public void fromDoingtoDone(Calendar enddate)
    {
        this.enddate = enddate;
        this.status = 2;
        timeofdone = Calendar.getInstance();
        //TODO: peer rating, report change to server
    }
    public Calendar getstartdate()
    {
        if (status == 0) return expectedstartdate;
        else return startdate;
    }
    public Calendar getenddate()
    {
        if (enddate != null) return enddate;
        else return expectedenddate;
    }
//    public void toggleCheckbox(int index)
//    {
//        checkboxes.get(index).ischecked = !checkboxes.get(index).ischecked;
//        //TODO: report change to server
//    }
//    public void writeComment(Person writer, String content)
//    {
//        comments.add(new Comment(writer, content));
//        //TODO: report change to server
//    }
//    public void editComment(int index, String content)
//    {
//        comments.get(index).content = content;
//        //TODO: report change to server
//    }
//    public void deleteComment(int index)
//    {
//        comments.remove(index);
//        //TODO: report change to server
//    }

}
