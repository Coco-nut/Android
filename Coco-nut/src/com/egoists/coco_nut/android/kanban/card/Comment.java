package com.egoists.coco_nut.android.kanban.card;

import java.io.Serializable;

public class Comment  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2380488578605951496L;
	public Person writer;
	public String content;
	public Comment(Person writer, String content){
		this.writer = writer;
		this.content = content;
	}
}
