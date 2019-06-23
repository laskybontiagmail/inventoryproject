package com.aiur.model.rest;

/**
 * Calculate the elapse execution time
 */
@SuppressWarnings("serial")
public class ElapseExecutionTime {
	
	private long startTime;
	private long endTime;
	/**
	 * Constructor.
	 */
	public ElapseExecutionTime() {
		
	}

	public void setStartTime() {
		this.startTime = System.currentTimeMillis();
	}

	public void setEndTime() {
		this.endTime = System.currentTimeMillis();
	}
	
	/**
	 * Show elapsed execution time
	 */
	public String getElapsed() {
		long difference = endTime - startTime;
		return (" @@@ {Execution time} sec:" + (difference / 1000.0) + " ms.:" + difference);
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
}
