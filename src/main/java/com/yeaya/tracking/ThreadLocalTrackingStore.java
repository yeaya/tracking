package com.yeaya.tracking;

import java.lang.InheritableThreadLocal;

/**
 * ThreadLocalTrackingStore
 * 
 * @author Yao Yuan (yeaya@163.com)
 */
public class ThreadLocalTrackingStore {

	private static final ThreadLocalTrackingStore instance = new ThreadLocalTrackingStore();
	private InheritableThreadLocal<Tracking> itlTracking = new InheritableThreadLocal<Tracking>();

	public static ThreadLocalTrackingStore getInstance() {
		return instance;
	}

	public void set(Tracking tracking) {
		itlTracking.set(tracking);
	}

	public Tracking get() {
		return itlTracking.get();
	}

	public void remove() {
		itlTracking.remove();
	}
}
