package com.zx.mvvmdemo.http;

public class SCError {

	// 错误编号
	private int errorCode;
	// 错误描述
	private String strErrorBrife;
	// 错误原因
	private String strErrorReason;

	private String strExceptionMesage;

	public SCError() {
	}

	public SCError(int strErrorCode) {
		this.errorCode = strErrorCode;
	}

	public SCError(int strErrorCode, String strErrorBrife,
                   String strErrorReason, String strExceptionMesage) {
		this.strErrorBrife = strErrorBrife;
		this.errorCode = strErrorCode;
		this.strErrorReason = strErrorReason;
		this.strExceptionMesage = strExceptionMesage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int strErrorCode) {
		this.errorCode = strErrorCode;
	}

	public String getStrErrorBrife() {
		return strErrorBrife;
	}

	public void setStrErrorBrife(String strErrorBrife) {
		this.strErrorBrife = strErrorBrife;
	}

	public String getStrErrorReason() {
		return strErrorReason;
	}

	public void setStrErrorReason(String strErrorReason) {
		this.strErrorReason = strErrorReason;
	}

	public String getStrExceptionMesage() {
		return strExceptionMesage;
	}

	public void setStrExceptionMesage(String strExceptionMesage) {
		this.strExceptionMesage = strExceptionMesage;
	}

}
