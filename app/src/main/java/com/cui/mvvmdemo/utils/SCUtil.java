package com.cui.mvvmdemo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;


import com.cui.mvvmdemo.constant.SCConstants;

import java.io.File;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SCUtil {
	public static String encodeMD5(String string) {
		String strConvert = null;
		if (null != string && !TextUtils.isEmpty(string)) {
			strConvert = encodeMD5(string, false);
		}

		return strConvert;
	}

	/**
	 * md5加密
	 * 
	 * @param source
	 *            要加密的byte数组
	 * @param isUpper
	 *            是否大写
	 * @return 加密后的字符串
	 */

	private static String encodeMD5(String source, boolean isUpper) {
		String strConvert = null;
		char lLower[] = { 'a', 'b', 'c', 'd', 'e', 'f' };
		char lUpper[] = { 'A', 'B', 'C', 'D', 'E', 'F' };
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'0', '0', '0', '0', '0', '0' };
		if (isUpper) {
			for (int i = 10; i < 16; i++) {
				hexDigits[i] = lUpper[i - 10];
			}
		} else {
			for (int i = 10; i < 16; i++) {
				hexDigits[i] = lLower[i - 10];
			}
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source.getBytes());
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
				// >>>
				// 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			strConvert = new String(str); // 换后的结果转换为字符串

		} catch (Exception e) {
			e.printStackTrace();

		}
		return strConvert;
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 获取当前网络类型
	 * 
	 * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 */

	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;

	public static int getNetworkType(Context context) {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!TextUtils.isEmpty(extraInfo)) {
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}

	/***
	 * 判断文件是否存在
	 * 
	 * @param strFilePath
	 *            文件地址
	 * @return
	 * @throws Exception
	 */
	public static boolean isFileExist(String strFilePath) throws Exception {
		boolean isFileExist = false;
		if (null != strFilePath && !TextUtils.isEmpty(strFilePath)) {
			File file = new File(strFilePath);
			isFileExist = file.exists();
			isFileExist = true;
		} else {
			isFileExist = false;
		}

		return isFileExist;
	}

	/***
	 * 替换url中的id
	 * 
	 * @param strUrl
	 * @param strAccountId
	 * @return
	 * @throws Exception
	 */
	public static String getUrl(String strUrl, String strAccountId)
			throws Exception {
		return strUrl.replaceFirst("pid", strAccountId);
	}

	/***
	 * 获取文件大小
	 * 
	 * @param strFilePath
	 *            文件路径
	 * @return
	 */
	public static long getFileSize(String strFilePath) {
		long fileSize = 0;
		if (null != strFilePath && !TextUtils.isEmpty(strFilePath)) {
			File file = new File(strFilePath);
			if (file.exists()) {
				fileSize = file.length();
			}
		}

		return fileSize;
	}

	/***
	 * 判断日期格式是否为YYYY-MM-DD HH:MM:SS
	 * 
	 * @param date
	 * @return 判断结果
	 */
	public static boolean isDateFormatCurrect(String time) {
		boolean isDateFormatCurrect = false;
		if (null == time || "".equalsIgnoreCase(time)) {
			return isDateFormatCurrect;
		}
		try {
			DateFormat formatter = new SimpleDateFormat(
					SCConstants.SC_DATE_FORMAT);
			Date date = formatter.parse(time);
			isDateFormatCurrect = time.equals(formatter.format(date));
		} catch (Exception e) {
			isDateFormatCurrect = false;
		}

		return isDateFormatCurrect;
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param msg
	 * @return
	 */
	public static boolean isNullOrEmpty(String msg) {
		if (msg == null || msg.isEmpty() || msg.trim().isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
}
