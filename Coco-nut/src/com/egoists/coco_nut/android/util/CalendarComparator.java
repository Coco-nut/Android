package com.egoists.coco_nut.android.util;

import java.util.Calendar;
import java.util.Comparator;
@SuppressWarnings("rawtypes")
public class CalendarComparator implements Comparator {
	@Override
	public int compare(Object x, Object y) {
	    Calendar xcal = (Calendar) x;
	    Calendar ycal = (Calendar) y;
	    if ( xcal.before(ycal) ) return -1;
	    if ( xcal.after(ycal) ) return 1;
	    return 0;
	  }
}
