package com.rde.apps.chronometer;

public class ChronoLap {
	public String elapsedTime;
	public String startTime;
	public ChronoLap(String elapsedTime, String startTime) {
		this.elapsedTime = elapsedTime;
		this.startTime = startTime;
	}
	
	public ChronoLap(String bothTimes){
		String[] arr = bothTimes.split(",");
		this.elapsedTime = "";
		this.startTime = "";
		if (arr.length > 0 ){
			elapsedTime = arr[0];
		}
		if (arr.length > 1 ){
			startTime = arr[1];
		}
		
	}
	
	public String toString() {
		return elapsedTime + ',' + startTime;
		
	}

}
