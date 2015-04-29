package com.yeaya.tracking;

import java.util.Map;

/**
 * Tracking
 * 
 * @author Yao Yuan (yeaya@163.com)
 */
public interface Tracking {
	public String getTrackingCode();
	public String getTrackingCode4Downstream();
	public String putOption(String key, String value);
	public String getOption(String key);
	public String removeOption(String key);
	public Map<String, String> getOptions();
}

