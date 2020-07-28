package com.cui.lib.net;

import java.util.HashMap;
import java.util.Map;

public class SCConstants {
	/** 本地数据库名称 */
	public static final String SC_LOCALDB_NAME = "sybercare";
	/** 用户id */
	public static String SC_ACCOUNT_ID;
	/** 状态码 */
	public static final String SC_STATUS = "syb_status";
	/** token */
	public static final String SC_TOKEN = "syb_sessionToken";
	/** 信息 */
	public static final String SC_INFO = "syb_info";
	/** 提供方序列号 */
	public static final String SC_PROVIDERSEQNO = "syb_providerSeqNo";
	/** 消费方序列号 */
	public static final String SC_CONSUMERSEQNO = "syb_consumerSeqNo";
	/** 数据 */
	public static final String SC_DATA = "syb_data";

	public static final String SC_CONTENT_TYPE_JSON = "application/json";

	/** 判断日期格式是否为yyy-MM-dd hh:mm:ss */
	public static final String SC_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/** 服务器正确返回 */
	public static final String SC_STATUS_SUCCESS = "000";
	/** 服务器sessiontoken失效 */
	public static final String SC_STATUS_SESSION_TOKEN_FAILED = "1";
	/** 新注册用户申请验证码 */
	public static final String SC_TYPE_REQUESTVERIFY_REGISTER = "1";
	/** 注册 */
	public static final String SC_TYPE_REGISTER = "2";
	/** 已注册用户申请验证码 */
	public static final String SC_TYPE_REQUESTVERIFY_REGISTERED = "3";
	/** 新增用户 */
	public static final String SC_TYPE_NEWUSER = "4";
	/** 测试用 */
	public static final String SC_COMCODE_BAITING = "0001";

	/** 登出广播 */
	public static final String BROADCAST_RECEIVER_LOGOUT = "BROADCAST_RECEIVER_LOGOUT";

	public static final Map<Integer, String> SCErrorCode = new HashMap<Integer, String>();// 存放错误代码
	static {
		SCErrorCode.put(1, "Session token failed");

		SCErrorCode.put(2001, "Context is not available");
		SCErrorCode.put(2002,
				"SCResultInterface is not available,Please set your Activity where is using "
						+ "SCSDKOpenAPI implements SCResultInterface");
		SCErrorCode.put(2003, "Relogin please");

		SCErrorCode.put(1000, "Operation Failed");
		/* 登录注册 */
		SCErrorCode.put(1001, "Account is not available");
		SCErrorCode.put(1002, "Password is not available");
		SCErrorCode.put(1003, "DeviveToken is not available");
		SCErrorCode.put(1004, "VerifyCode is not available");
		SCErrorCode.put(1005, "Network is not available");
		SCErrorCode.put(1006, "Account Info is not available");
		SCErrorCode.put(1007, "Account Info is not available");

		/* 测量数据 */
		SCErrorCode.put(1008, "User Info is not available");
		SCErrorCode.put(1009, "Glucose Info is not available");
		SCErrorCode.put(1010, "Modify just on local is not available");
		SCErrorCode.put(1011, "Add just on local is not available");
		SCErrorCode.put(1012, "UserID is not available");
		SCErrorCode.put(1013, "Value is not available");
		SCErrorCode.put(1014, "Status is not available");
		SCErrorCode.put(1015, "MeasureTime is not available");
		SCErrorCode.put(1016, "Delete just on local is not available");
		/* 登录注册 */
		SCErrorCode.put(1017, "AreaCode is not available");
		SCErrorCode.put(1018, "ComCode is not available");
		/* 文件上传 */
		SCErrorCode.put(1019, "File path is not available");
		SCErrorCode.put(1020, "File is not exist");
		SCErrorCode.put(1021, "File size is limit with in 1M ");
		/* 测量数据点评 */
		SCErrorCode.put(1022, "DataID is not available");
		SCErrorCode.put(1023, "PersonID is not available");
		SCErrorCode.put(1024, "RecordDate is not available");

		SCErrorCode.put(1025, "Type is not available");

		/* 药品效果 */
		SCErrorCode.put(1026, "EffectClass is not available");

		/* 用药方案 */
		SCErrorCode.put(1027, "medicalSchemeType is not available");
		SCErrorCode.put(1028, "status is not available");
		SCErrorCode.put(1029, "personId is not available");
		SCErrorCode.put(1030, "medicalSchemeDetail is not available");
		SCErrorCode.put(1031, "drugId is not available");
		SCErrorCode.put(1032, "drugDose is not available");
		SCErrorCode.put(1033, "drugDoseTimes is not available");
		SCErrorCode.put(1034, "drugTimeSchedule is not available");
		SCErrorCode.put(1035, "medicalSchemeName is not available");
		SCErrorCode.put(1036, "userId is not available");
		SCErrorCode.put(1037, "medicalSchemeId is not available");
		SCErrorCode.put(1038, "medicalSchemeDetailId is not available");

		/* 血压 */
		SCErrorCode.put(1039, "systolic is not available");
		SCErrorCode.put(1040, "diastolic is not available");
		SCErrorCode.put(1041, "pluse is not available");
		SCErrorCode.put(1042, "measureTime is not available");
		SCErrorCode.put(1043, "pressureId is not available");

		/* 服药记录 */
		SCErrorCode.put(1044, "userId is not available");
		SCErrorCode.put(1045, "doseIsmeal is not available");
		SCErrorCode.put(1046, "doseTime is not available");
		SCErrorCode.put(1047, "doseClassifyId is not available");
		SCErrorCode.put(1048, "drugList is not available");
		SCErrorCode.put(1049, "drugId is not available");
		SCErrorCode.put(1050, "doseVolume is not available");
		SCErrorCode.put(1051, "drugDoseUnit is not available");
		SCErrorCode.put(1052, "doseDetailId is not available");
		SCErrorCode.put(1053, "doseId is not available");
		SCErrorCode.put(1054, "doseInfo is not available");

		/* BMI */
		SCErrorCode.put(1055, "bmiInfo is not available");
		SCErrorCode.put(1056, "userId is not available");
		SCErrorCode.put(1057, "height is not available");
		SCErrorCode.put(1058, "weight is not available");
		SCErrorCode.put(1059, "bmi is not available");
		SCErrorCode.put(1060, "measureTime is not available");
		SCErrorCode.put(1061, "device_sn is not available");

		/* 用户目标值 */
		SCErrorCode.put(1062, "targetInfo is not available");
		SCErrorCode.put(1063, "DataType is not available");
		SCErrorCode.put(1064, "CreateTime is not available");
		SCErrorCode.put(1065, "UserId is not available");
		SCErrorCode.put(1066, "targetValue is not available");
		SCErrorCode.put(1067, "max of target value is not available");
		SCErrorCode.put(1068, "min of target value is not available");
		SCErrorCode.put(1069, "type of target value is not available");
		SCErrorCode.put(1070, "dataId of target value is not available");

		/* 用户单位 */
		SCErrorCode.put(1071, "unit info is not available");
		SCErrorCode.put(1072, "data type is not available");
		SCErrorCode.put(1073, "measure unit id is not available");
		SCErrorCode.put(1074, "data id is not available");
		SCErrorCode.put(1075, "Measure Unit is not available");
		SCErrorCode.put(1076, "targetValue is not available");

		/* 病例 */
		SCErrorCode.put(1077, "symptom info is not available");
		SCErrorCode.put(1078, "user id is not available");
		SCErrorCode.put(1079, "symptomsDescription  is not available");
		SCErrorCode.put(1080, "symptom attach file url is not available");
		SCErrorCode.put(1081, "the attach count is limit in 6");
		SCErrorCode.put(1082, "BasicSymptomsId is not available");
		SCErrorCode.put(1090, "RecordDate is not allowed empty");

		/* Ease 环信 */
		SCErrorCode.put(1083, "easeId is not available");
		/* Drug */
		SCErrorCode.put(1084, "SCBaseModel info is not available");
		SCErrorCode.put(1085, "staff comcode is not available");
		SCErrorCode.put(1086, "user comcode is not available");

		/* Header 信息 */
		SCErrorCode.put(1087, "appkey and appId is not available");
		SCErrorCode.put(1088, "appkey is not available");
		SCErrorCode.put(1089, "appId is not available");
		SCErrorCode.put(1090, "startTime's format is not available");
		SCErrorCode.put(1091, "endTime's format is not available");

		/* 服药提醒 */
		SCErrorCode.put(1100, "recordDate is not available");
		SCErrorCode.put(1101, "dataId of target value is not available");
		SCErrorCode.put(1102, "dataId of target value is not available");
		SCErrorCode.put(1103, "dataId of target value is not available");
		SCErrorCode.put(1104, "dataId of target value is not available");
		SCErrorCode.put(1105, "dataId of target value is not available");
		SCErrorCode.put(1106, "dataId of target value is not available");
		SCErrorCode.put(1107, "dataId of target value is not available");
		/** 查找专员 **/
		SCErrorCode.put(1108, "Serchtype is not in [0,1]");
		SCErrorCode.put(1109, "Serchtype can not be null");
		SCErrorCode.put(1110, "Name or address can not be null or  empty");
		SCErrorCode.put(1111, "SearchModel can not be null");
		/** Ease 环信 **/
		SCErrorCode.put(1112, "searchtype is null or empty");
		SCErrorCode.put(1113, "searchtype is wrong!");
		SCErrorCode.put(1114, "keyWord is null");
		SCErrorCode.put(1115, "easeId is null or empty");

		/** 会员文章机构获取 **/
		SCErrorCode.put(1116, "cateLevel is null or empty");
		SCErrorCode.put(1117, "catetype is null or empty");
		SCErrorCode.put(1118, "catetype is invalid");

		/** 会员文章获取 **/
		SCErrorCode.put(1119, "type can not be null or empty");
		SCErrorCode.put(1120, "cateId  can not be null or empty");

		/**关注**/
		SCErrorCode.put(1121, "userId can not be null or empty");
		SCErrorCode.put(1122, "phone can not be null or empty");
		SCErrorCode.put(1123, "mainUserId  can not be null or empty");
		SCErrorCode.put(1124, "bondUserId  can not be null or empty");



		SCErrorCode.put(105, "Get user failed.");
		SCErrorCode.put(106, "Password is error.");
		SCErrorCode.put(204, "ID is not available.");

	}
	public static final Map<Integer, String> SCSuccessCode = new HashMap<Integer, String>();// 存放错误代码
	static {
		SCSuccessCode.put(3000, "Operation Success");
		SCSuccessCode.put(3001, "Delete Success");
	}

}
