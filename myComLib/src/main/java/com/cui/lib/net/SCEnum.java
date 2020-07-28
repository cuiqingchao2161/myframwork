package com.cui.lib.net;

public class SCEnum {
	/***
	 * 获取数据方式
	 * 
	 * @author Sybercare.Inc
	 * 
	 */
	public enum STYLE_GETDATA {
		/** 仅仅从本地获取 */
		LOCALONLY,
		/** 仅仅从服务端获取 */
		SERVERONLY,
		/** 先从本地获取再从服务端获取 */
		LOCALANDSERVER
	}

	public enum STYLE_REGISTER {
		REQUEST_VERIFYMSG, REGISTER_ACCOUNT,
	}

}
