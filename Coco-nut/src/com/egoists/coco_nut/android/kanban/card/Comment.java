package com.egoists.coco_nut.android.kanban.card;

public class Comment{
	public Person writer;
	public String content;
	public Comment(Person writer, String content){
		this.writer = writer;
		this.content = content;
	}
}
