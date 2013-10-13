package com.egoists.coco_nut.android.kanban.card;

import java.io.Serializable;

public class Checkbox implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1136675182229570303L;
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
