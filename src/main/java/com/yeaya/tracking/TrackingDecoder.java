package com.yeaya.tracking;

import java.net.URLDecoder;

/**
 * TrackingDecoder
 * 
 * @author Yao Yuan (yeaya@163.com)
 */
public class TrackingDecoder {
	public static String decode(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
    }
}
