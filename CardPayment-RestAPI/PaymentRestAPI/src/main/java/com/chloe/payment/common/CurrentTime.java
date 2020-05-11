package com.chloe.payment.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentTime {
	Date time;
	public CurrentTime() {
		this.time = new Date();
	}
	
	public String getCurrentTime(String format) {
		//SimpleDateFormat timeFormat = new SimpleDateFormat ( "yyyyMMddHHmmss");
		SimpleDateFormat timeFormat = new SimpleDateFormat (format);
		String curTime = timeFormat.format(time);
		
		return curTime;
	}
}
