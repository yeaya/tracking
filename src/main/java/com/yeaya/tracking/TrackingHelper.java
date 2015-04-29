package com.yeaya.tracking;

public class TrackingHelper {
	private static final TrackingHelper instance = new TrackingHelper();

	public static TrackingHelper getInstance() {
		return instance;
	}
	
	public Tracking parse(String trackingCode) {
		return TrackingImpl.create(trackingCode);	
	}

	public Tracking create() {
		Tracking tracking = TrackingImpl.create();
		return tracking;
	}

	public Tracking open(String trackingCode) {
		Tracking tracking = TrackingImpl.create(trackingCode);
		ThreadLocalTrackingStore.getInstance().set(tracking);
		return tracking;
	}

	public Tracking openOrCreate(String trackingCode) {
		Tracking tracking = TrackingImpl.create(trackingCode);
		if (tracking == null) {
			tracking = TrackingImpl.create();
		}
		ThreadLocalTrackingStore.getInstance().set(tracking);
		return tracking;
	}

	public Tracking get() {
		return ThreadLocalTrackingStore.getInstance().get();
	}

	public Tracking getOrCreate() {
		Tracking tracking = ThreadLocalTrackingStore.getInstance().get();
		if (tracking == null) {
			tracking = TrackingImpl.create();
			ThreadLocalTrackingStore.getInstance().set(tracking);
		}
		return tracking;
	}

	public void close() {
		ThreadLocalTrackingStore.getInstance().remove();
	}
}
