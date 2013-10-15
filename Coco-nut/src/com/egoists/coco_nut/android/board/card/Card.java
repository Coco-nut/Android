package com.egoists.coco_nut.android.board.card;

import java.util.ArrayList;
import java.util.Calendar;

import com.egoists.coco_nut.android.kanban.card.Checkbox;
import com.egoists.coco_nut.android.kanban.card.Comment;
import com.egoists.coco_nut.android.kanban.card.Person;

public class Card {
    public static final String ENTITY                 = "card";
    public static final String ENTITY_NAME_TITLE      = "title";      // 카드 제목
    public static final String ENTITY_NAME_SUBTITLE   = "subtitle";   // 카드 부제목
    public static final String ENTITY_NAME_RATING     = "rating";     // 중요도
    public static final String ENTITY_NAME_STATE      = "state";      // 카드 상태
    
    public String title;
    public String sub_title;
    public String discription;
    
    public int label;
    public float importance;
    
    private Calendar startdate;
    private Calendar expectedstartdate;
    private Calendar enddate;
    private Calendar expectedenddate;
    
    
    public ArrayList<Comment> comments;
    public ArrayList<Checkbox> checkboxes;
    public ArrayList<Person> participants;
    
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
        comments = new ArrayList<Comment>();
        checkboxes = new ArrayList<Checkbox>();
        for (int i = 0; i< this.participants.size(); i++)
            if (this.participants.get(i).isme){
                this.ismine = true;
                break;
            }
        //TODO: report change to server
    }
    public Card(String title, String sub_title, String discription, int label, int importance,
            Calendar startdate, Calendar enddate, Calendar expectedstartdate, 
            Calendar expectedenddate, ArrayList<Comment> comments, ArrayList<Checkbox> checkboxes,
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
        this.comments = comments;
        this.checkboxes = checkboxes;
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
        this.status = 1;
        //TODO: report change to server
    }
    public void fromDoingtoDo()
    {
        this.startdate = null;
        this.enddate = null;
        this.status = 0;
        //TODO: report change to server
    }
    public void fromDoingtoDone(Calendar enddate)
    {
        this.enddate = enddate;
        this.status = 2;
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
    public void toggleCheckbox(int index)
    {
        checkboxes.get(index).ischecked = !checkboxes.get(index).ischecked;
        //TODO: report change to server
    }
    public void writeComment(Person writer, String content)
    {
        comments.add(new Comment(writer, content));
        //TODO: report change to server
    }
    public void editComment(int index, String content)
    {
        comments.get(index).content = content;
        //TODO: report change to server
    }
    public void deleteComment(int index)
    {
        comments.remove(index);
        //TODO: report change to server
    }

}
