package com.cmbb.app.action.sec;

import java.util.ArrayList;

import android.util.Log;

/**
 * 涓嶅悓鐨勫湴鏂圭敤涓嶅悓鐨勭増鏈殑鍘嬬缉鏂规硶
 * 
 * @author Rocky [2013-06-01]
 */
public class DigitalUtil {

	public static final int V1 = 0;// 鍔犲瘑鐗堟湰1
	public static final int V2 = 1;// 鍔犲瘑鐗堟湰2
	private static char[][] table = new char[][] {
			new char[] { '7', '1', '6', 'x', 'O', 's', 'S', 'b', 'J', 'r', 'u',
					'a', '3', 'v', 'P', 'T', 'A', 'I', 'w', '2', 'V', 'e', 'Z',
					'n', 'K', 'o', '-', '5', 'm', '+', 'L', 'd', 'Y', 'N', 'f',
					'k', 'g', '0', 'M', 'h', 'B', 'l', '4', 'G', 'F', 'c', 'q',
					'D', 'C', 'U', 'i', 't', 'Q', 'R', 'H', '8', 'X', 'j', 'E',
					'9', 'z', 'p', 'y', 'W' },
			new char[] { '7', '1', '6', 'x', 'O', 's', 'S', 'b', 'J', 'r', 'u',
					'a', '3', 'v', 'P', 'T', 'A', 'I', 'w', '2', 'V', 'e', 'Z',
					'n', 'K', 'o', 'y', '5', 'm', 'W', 'L', 'd', 'Y', 'N', 'f',
					'k', 'g', '0', 'M', 'h', 'B', 'l', '4', 'G', 'F', 'c', 'q',
					'D', 'C', 'U', 'i', 't', 'Q', 'R', 'H', '8', 'X', 'j', 'E',
					'9', 'z', 'p' } };

	private static ArrayList<Integer> baseXXtoArray(int id, int version) {
		ArrayList<Integer> value = new ArrayList<Integer>();
		while (id > 0) {
			int remainder = id % table[version].length;
			value.add(remainder);
			id = id / table[version].length;
		}
		return value;
	}

	private static int char2Int(char c, int version) {
		for (int i = 0; i < table[version].length; i++) {
			if (table[version][i] == c) {
				return i;
			}
		}
		return 0;
	}

	private static int decode(ArrayList<Integer> baseXX, int version) {
		for (int i = 1; i <= 6 - baseXX.size(); i++) {
			baseXX.add(0, 0);
		}
		int id = 0;
		int size = baseXX.size();
		for (int i = 0; i < size; i++) {
			int value = baseXX.get(i);
			id += (int) (value * Math.pow(table[version].length, size - i - 1));
		}

		return id;
	}

	/**
	 * 鍔犲瘑鏁村瀷瀛楃涓?
	 * 
	 * @param value
	 * @param version
	 *            鐗堟湰鍙?
	 * @return
	 */
	public static String encode(int value, int version) {
		ArrayList<Integer> values = baseXXtoArray(value, version);
		StringBuilder sb = new StringBuilder();
		for (Integer i : values) {
			sb.insert(0, table[version][i]);
		}
		return sb.toString();

	}

	/**
	 * 瑙ｅ瘑鏁村瀷瀛楃涓?
	 * 
	 * @param value
	 * @param version
	 *            鐗堟湰鍙?
	 * @return
	 */
	public static int decode(String value, int version) {
		if (value == null || "".equals(value)) {
			return 0;
		}
		ArrayList<Integer> values = new ArrayList<Integer>();
		for (int i = 0; i < value.length(); i++) {
			values.add(char2Int(value.charAt(i), version));
		}
		return decode(values, version);
	}

	/**
	 * 鍔犲瘑绾害
	 * 
	 * @param lat
	 * @param version
	 *            鍔犲瘑鐗堟湰
	 * @param decimal
	 *            绮剧‘搴? 灏忔暟浣嶆暟
	 * @return
	 */
	public static String encodeLat(double lat, int version, int decimal) {
		decimal = decimal < 1 || decimal > 8 ? 8 : decimal;
		if (lat < 0) {
			lat = 90 - lat;
		}
		return encode((int) (lat * Math.pow(10, decimal)), version);
	}

	/**
	 * 鍔犲瘑绾害
	 * 
	 * @param lat
	 * @param version
	 *            鍔犲瘑鐗堟湰
	 * @param decimal
	 *            绮剧‘搴? 灏忔暟浣嶆暟
	 * @return
	 */
	public static String encodeLat(double lat, int version) {
		return encodeLat(lat, version, 6);
	}

	/**
	 * 鍔犲瘑缁忓害
	 * 
	 * @param lon
	 * @param version
	 * @param decimal
	 *            绮剧‘搴? 灏忔暟浣嶆暟
	 * @return
	 */
	public static String encodeLon(double lon, int version, int decimal) {
		decimal = decimal < 1 || decimal > 8 ? 8 : decimal;
		if (lon < 0) {
			lon = 180 - lon;
		}
		return encode((int) Math.round(lon * Math.pow(10, decimal)), version);
	}

	/**
	 * 鍔犲瘑缁忓害
	 * 
	 * @param lon
	 * @param version
	 * @param decimal
	 *            绮剧‘搴? 灏忔暟浣嶆暟
	 * @return
	 */
	public static String encodeLon(double lon, int version) {
		return encodeLon(lon, version, 6);
	}

	/**
	 * 瑙ｅ瘑绾害, 榛樿6浣嶇簿纭害
	 * 
	 * @param value
	 * @param version
	 *            鍔犲瘑鐗堟湰
	 * @return
	 */
	public static double decodeLat(String value, int version) {
		return decodeLat(value, version, 6);
	}

	/**
	 * 瑙ｅ瘑缁忓害, 榛樿6浣嶇簿纭害
	 * 
	 * @param value
	 * @param version
	 *            鍔犲瘑鐗堟湰
	 * @return
	 */
	public static double decodeLon(String value, int version) {
		return decodeLon(value, version, 6);
	}

	/**
	 * 瑙ｅ瘑绾害
	 * 
	 * @param value
	 * @param version
	 *            鍔犲瘑鐗堟湰
	 * @param decimal
	 *            绮剧‘搴? 灏忔暟浣嶆暟
	 * @return
	 */
	public static double decodeLat(String value, int version, int decimal) {
		double rs = decode(value, version) / Math.pow(10, decimal);
		return rs > 90 ? 90 - rs : rs;
	}

	/**
	 * 瑙ｅ瘑缁忓害
	 * 
	 * @param value
	 * @param version
	 * @param decimal
	 *            绮剧‘搴? 灏忔暟浣嶆暟
	 * @return
	 */
	public static double decodeLon(String value, int version, int decimal) {
		double rs = decode(value, version) / Math.pow(10, decimal);
		return rs > 180 ? 180 - rs : rs;
	}

	public static void main(String[] args) {
		// double lon = 108.389601;
		// System.out.println("aaa = " + lon);
		//
		// String s = DigitalUtil.encodeLon(lon, DigitalUtil.V2, 6);
		// System.out.println("aaxx = " + s);
		//
		// double rs = DigitalUtil.decodeLon(s, DigitalUtil.V2, 6);
		// System.out.println("rrxx = " + rs);
		//
		//
		// double lat = 22.64233;
		// System.out.println("aaa = " + lon);
		//
		// String lat_s = DigitalUtil.encodeLon(lat, DigitalUtil.V2, 6);
		// System.out.println("lat_s = " + lat_s);
		//
		// double lat_rs = DigitalUtil.decodeLon(lat_s, DigitalUtil.V2, 6);
		// System.out.println("lat_rr = " + lat_rs);

		Log.e("XXXXXXXX", "bs=" + DigitalUtil.encode(3801, 12) + ","
				+ DigitalUtil.encode(10133, 12) + ",100");
	}
}
