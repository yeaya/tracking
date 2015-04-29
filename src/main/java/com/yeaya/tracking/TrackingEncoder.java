package com.yeaya.tracking;

import java.io.CharArrayWriter;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException ;

/**
 * TrackingEncoder
 * 
 * @author Yao Yuan (yeaya@163.com)
 */
public class TrackingEncoder {
    static final int caseDiff = ('a' - 'A');

    protected static boolean needEncoding(int c) {
    	if (c >='a' && c <= 'z') {
            return false;
        }
        if (c >= 'A' && c <= 'Z') {
        	return false;
        }
        if (c >= '0' && c <= '9') {
            return false;
        }
        return true;
    }

    protected static void encode(CharArrayWriter writer, Charset charset, StringBuffer out) {
    	writer.flush();
        byte[] bytes = new String(writer.toCharArray()).getBytes(charset);
        for (int i = 0; i < bytes.length; i++) {
            out.append('%');
            char c = Character.forDigit((bytes[i] >> 4) & 0xF, 16);
            if (Character.isLetter(c)) {
                c -= caseDiff;
            }
            out.append(c);
            c = Character.forDigit(bytes[i] & 0xF, 16);
            if (Character.isLetter(c)) {
                c -= caseDiff;
            }
            out.append(c);
        }
        writer.reset();
    }

    public static String encode(String s) {
        CharArrayWriter writer = new CharArrayWriter();
        Charset charset;

        try {
            charset = Charset.forName("UTF-8");
        } catch (IllegalCharsetNameException e) {
            e.printStackTrace();
            return "";
        } catch (UnsupportedCharsetException e) {
            e.printStackTrace();
            return "";
        }

        StringBuffer out = new StringBuffer(s.length());

        for (int i = 0; i < s.length(); i++) {
            int c = (int) s.charAt(i);
            if (!needEncoding(c)) {
            	if (writer.size() > 0) {
        			TrackingEncoder.encode(writer, charset, out);
        		}
                out.append((char)c);
            } else {
            	if (c == ' ') {
                	if (writer.size() > 0) {
            			TrackingEncoder.encode(writer, charset, out);
            		}
                    c = '+';
                    out.append((char)c);
                } else {
                    writer.write(c);
                }
            }
        }
        
    	if (writer.size() > 0) {
			TrackingEncoder.encode(writer, charset, out);
		}

        return out.toString();
    }
}
