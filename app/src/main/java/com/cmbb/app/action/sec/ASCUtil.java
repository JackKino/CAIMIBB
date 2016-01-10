package com.cmbb.app.action.sec;

public class ASCUtil {
	private static int ascNum;
	private static char strChar;

	// 460002571333031
	// 152154148 148148150 153155149 151151151 148151149
	// 989a94949496999b95979797949795
	// uwyTuu1MSwuZMBeuZMBeuZMBe
	// aPRUSoMYCH

	public static void main(String[] args) {
		String encode = encode("50:CC:F8:85:89:73");
		System.out.println(encode);
		System.out.println(decode(encode));
	}

	public static String encode(String str) {
		if ("".equals(str) || str == null) {
			return "";
		}
		String[] strArr = str.split("");
		String[] newArr = getStrArr(strArr);
		StringBuffer sb = new StringBuffer();
		for (String s : newArr) {
			sb.append(Integer.toString((getAsc(s) + 100), 16));
		}

		return sb.toString();
	}

	public static String decode(String str) {
		StringBuffer sb = new StringBuffer();
		if (str.length() % 2 == 0) {
			while (str.length() > 0) {
				String temp = str.substring(0, 2);
				int a = Integer.parseInt(temp, 16);
				int temp2 = a - 100;
				sb.append(backchar(temp2));
				str = str.substring(2);
			}
		} else {
			System.out.println("XXXXXXXXXX");
		}

		return sb.toString();
	}

	public static String[] getStrArr(String[] a) {
		int index = 0;// 要删除的index
		String[] ary = new String[a.length - 1];
		System.arraycopy(a, 0, ary, 0, index);
		System.arraycopy(a, index + 1, ary, index, ary.length - index);
		return ary;
	}

	/**
	 * 
	 * @param st
	 * @return
	 */
	public static int getAsc(String st) {
		byte[] gc = st.getBytes();
		ascNum = (int) gc[0];
		return ascNum;
	}

	/**
	 * 
	 * @param backnum
	 * @return
	 */
	public static char backchar(int backnum) {
		strChar = (char) backnum;
		return strChar;
	}
}
