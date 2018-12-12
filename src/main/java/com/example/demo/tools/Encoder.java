package com.example.demo.tools;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import java.util.StringTokenizer;

/**
 * TODO:请添加注释.(修改好了把TODO删除)lqh add.
 * 
 * 
 * 
 */
public class Encoder {

	private static BitSet dontNeedEncoding;

	// static final int CASEDIFF = 32;

	public Encoder() {
	}

	public static String encodeHTML(String s) {
		if (s == null) {
			return null;
		}
		char[] ac = new char[s.length()];
		s.getChars(0, s.length(), ac, 0);
		StringBuffer stringbuffer = new StringBuffer(ac.length + 50);
		for (int i = 0; i < ac.length; i++) {
			switch (ac[i]) {
			case 60: // '<'
				stringbuffer.append("&lt;");
				break;

			case 62: // '>'
				stringbuffer.append("&gt;");
				break;

			case 38: // '&'
				stringbuffer.append("&amp;");
				break;

			case 34: // '"'
				stringbuffer.append("&quot;");
				break;

			default:
				stringbuffer.append(ac[i]);
				break;
			}
		}

		return stringbuffer.toString();
	}

	public static String encodeJS(String s) {
		if (s == null) {
			return null;
		}
		char[] ac = new char[s.length()];
		s.getChars(0, s.length(), ac, 0);
		StringBuffer stringbuffer = new StringBuffer(ac.length + 50);
		for (int i = 0; i < ac.length; i++) {
			switch (ac[i]) {
			case 13: // '\r'
				stringbuffer.append("\\n");
				if (i < ac.length - 1 && ac[i + 1] == '\n') {
					i++;
				}
				break;

			case 10: // '\n'
				stringbuffer.append("\\n");
				break;

			case 34: // '"'
			case 39: // '\''
			case 92: // '\\'
				stringbuffer.append('\\');
				stringbuffer.append(ac[i]);
				break;

			default:
				stringbuffer.append(ac[i]);
				break;
			}
		}

		return stringbuffer.toString();
	}

	public static String quoteObjectProp(String s) {
		String s1 = s;
		StringTokenizer stringtokenizer = new StringTokenizer(s, "'");
		boolean flag = true;
		while (stringtokenizer.hasMoreTokens()) {
			if (!flag) {
				s1 = s1 + "''" + stringtokenizer.nextToken();
			} else {
				flag = false;
				s1 = stringtokenizer.nextToken();
			}
		}
		s1 = "'" + s1 + "'";
		return s1;
	}

	public static String encodeCookie(String s) {
		if (s == null) {
			return null;
		} else {
			return a(s.toCharArray());
		}
	}

	public static String encodeURL(String s) {
		if (s == null) {
			return null;
		}
		char[] ac;
		try {
			byte[] abyte0 = s.getBytes("UTF-8");
			ac = new char[abyte0.length];
			for (int i = abyte0.length - 1; i >= 0; i--) {
				ac[i] = (char) (abyte0[i] & 0xff);
			}

		} catch (UnsupportedEncodingException unsupportedencodingexception) {
			ac = s.toCharArray();
		}
		return a(ac);
	}

	private static String a(char[] ac) {
		StringBuffer stringbuffer = new StringBuffer(ac.length);
		for (int i = 0; i < ac.length; i++) {
			int j = ac[i];
			if (dontNeedEncoding.get(j)) {
				if (j == 32) {
					j = 43;
				}
				stringbuffer.append((char) j);
			} else {
				stringbuffer.append('%');
				byte byte0 = 1;
				if (j > 255) {
					byte0 = 3;
					stringbuffer.append('u');
				}
				for (int k = byte0; k >= 0; k--) {
					int l = ((j & 61440 >> (3 - k) * 4) >> k * 4) + 48;
					if (l > 57) {
						l += 7;
					}
					stringbuffer.append((char) l);
				}

			}
		}

		return stringbuffer.toString();
	}

	public static String decodeCookie(String s) {
		if (s == null) {
			return null;
		}
		StringBuffer stringbuffer = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case 43: // '+'
				stringbuffer.append(' ');
				break;

			case 37: // '%'
				try {
					if (s.charAt(i + 1) == 'u') {
						stringbuffer.append((char) Integer.parseInt(s.substring(i + 2, i + 6), 16));
						i += 5;
					} else {
						stringbuffer.append((char) Integer.parseInt(s.substring(i + 1, i + 3), 16));
						i += 2;
					}
				} catch (NumberFormatException numberformatexception) {
					throw new IllegalArgumentException();
				}
				break;

			default:
				stringbuffer.append(c);
				break;
			}
		}

		String s1 = stringbuffer.toString();
		return s1;
	}

	static {
		dontNeedEncoding = new BitSet(256);
		for (int i = 97; i <= 122; i++) {
			dontNeedEncoding.set(i);
		}

		for (int j = 65; j <= 90; j++) {
			dontNeedEncoding.set(j);
		}

		for (int k = 48; k <= 57; k++) {
			dontNeedEncoding.set(k);
		}

		dontNeedEncoding.set(32);
		dontNeedEncoding.set(45);
		dontNeedEncoding.set(95);
		dontNeedEncoding.set(46);
		dontNeedEncoding.set(42);
	}
}