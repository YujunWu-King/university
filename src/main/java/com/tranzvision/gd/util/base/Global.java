package com.tranzvision.gd.util.base;

import java.util.Vector;

public class Global {
	public static String[] split(String str, String sep) {
		int size = 0;
		Vector<String> v = new Vector<String>();
		int pos = -1;
		while (!str.equals("")) {
			pos = str.indexOf(sep);
			if (pos != -1) {
				v.add(str.substring(0, pos));
				str = str.substring(pos + sep.length());
				if (str.equalsIgnoreCase("")) {
					v.add("");
				}
			} else {
				v.add(str);
				str = "";
			}
		}

		size = v.size();
		String[] array = new String[size];
		for (int i = 0; i < size; i++) {
			array[i] = v.elementAt(i).toString();
		}
		return array;
	}
}
