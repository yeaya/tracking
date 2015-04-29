package com.yeaya.tracking;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import com.yeaya.basex.Base57;

/**
 * TrackingServiceImpl
 *
 * @author Yao Yuan (yeaya@163.com)
 */
public class TrackingImpl implements Tracking {
	protected volatile String _trackingCode = "";
	protected String _uniqueCode = "";
	protected String _callPath = "";
	protected AtomicInteger _callIndex = new AtomicInteger(0);
	protected HashMap<String, String> _options = new HashMap<String, String>();

	protected TrackingImpl() {
		
	}

	public static TrackingImpl create(String trackingCode) {
		if (trackingCode == null) {
			return null;
		}
		TrackingImpl tracking = new TrackingImpl();
		String[] fields = trackingCode.split("_");
		String uniqueId = fields[0];
		if (!parseUniqueIdAndOption(uniqueId, tracking)) {
			return null;
		}

		if (tracking._uniqueCode.isEmpty()) {
			return null;
		}
		for (int i = 1; i < fields.length; i++) {
			String field = fields[i];
			if (!parseCallpathAndOption(field, tracking)) {
				return null;
			}
		}
		tracking._trackingCode = trackingCode;
		return tracking;
	}
	
	public static TrackingImpl create() {
		TrackingImpl tracking = new TrackingImpl();
		tracking._uniqueCode = Base57.encodeUuid(UUID.randomUUID());
		tracking._trackingCode = encodeTrackingCode(tracking._uniqueCode, "", "");

		return tracking;
	}
	
	static private boolean parseUniqueIdAndOption(String field, TrackingImpl tracking) {
		String[] optionFields = field.split("[.]");
		if (optionFields.length >= 2) {
			tracking._uniqueCode = optionFields[0];
			if (optionFields.length % 2 == 0) {
				return false;
			}
			for (int i = 1; i < optionFields.length; i += 2) {
				String key = optionFields[i];
				String value = optionFields[i + 1];
				tracking._options.put(key, value);
			}
		} else {
			tracking._uniqueCode = field;
		}
		return true;
	}

	static private boolean parseCallpathAndOption(String field, TrackingImpl tracking) {
		String[] optionFields = field.split("[.]");
		tracking._callPath += "_" + optionFields[0];
		if (optionFields.length >= 2) {
			if (optionFields.length % 2 == 0) {
				return false;
			}
			for (int i = 1; i < optionFields.length; i += 2) {
				String key = optionFields[i];
				String value = optionFields[i + 1];
				tracking._options.put(key, value);
			}
		}
		return true;
	}
	
	
	
	private static String encodeOptions(Map<String, String> options) {
		StringBuilder sb = new StringBuilder(1024);
		if (options != null) {
			Iterator<Entry<String, String>> it = options.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> e = (Entry<String, String>)it.next();
				String key = (String)e.getKey();
				String value = (String)e.getValue();
				if (key != null) {
					String encodedKey = TrackingEncoder.encode(key);

					String encodedValue = "";
					if (value != null) {
						encodedValue = TrackingEncoder.encode(value);
					}
					sb.append(".").append(encodedKey).append(".").append(encodedValue);
				}
			}
		}
		return sb.toString();
	}

	private static String encodeTrackingCode(String fullUniqueId, String callPath, String options) {
		return fullUniqueId + callPath + options;
	}

	protected String getUniqueCode() {
		return _uniqueCode;
	}

	protected void createOptions() {
		if (_options == null) {
			_options = new HashMap<String, String>();
		}
	}

	public String getTrackingCode() {
		return _trackingCode;
	}

	public String getTrackingCode4Downstream() {
		String downstreamCallPath = _callPath + "_" + _callIndex.getAndIncrement();
		String optionsString = encodeOptions(_options);
	
		return encodeTrackingCode(_uniqueCode, downstreamCallPath, optionsString);
	}
	
	public synchronized String putOption(String key, String value) {
		if (key == null) {
			key = "";
		}
		if (value == null) {
			value = "";
		}
		createOptions();
		String oldValue = _options.put(key, value);

		String optionsString = encodeOptions(_options);
		
		String trackingCode = encodeTrackingCode(_uniqueCode, _callPath, optionsString);
		_trackingCode = trackingCode;
		return oldValue;
	}
	
	public synchronized String getOption(String key) {
		if (key == null) {
			key = "";
		}
		if (_options != null) {
			return _options.get(key);
		}
		return null;
	}
	
	public synchronized String removeOption(String key) {
		if (key == null) {
			key = "";
		}
		if (_options != null) {
			return _options.remove(key);
		}
		return null;
	}
	
	public synchronized Map<String, String> getOptions() {
		if (_options != null) {
			return new HashMap<String, String>(_options);
		}
		return new HashMap<String, String>();
	}
}
