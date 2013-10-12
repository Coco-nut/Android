package com.egoists.coco_nut.android.kanban.card;

public class Checkbox{
	public String name;
	public boolean ischecked;
	Checkbox(String name){
		this.name = name;
		this.ischecked = false;
	}
	public Checkbox(String name, boolean ischecked){
		this.name = name;
		this.ischecked = ischecked;
	}
}
