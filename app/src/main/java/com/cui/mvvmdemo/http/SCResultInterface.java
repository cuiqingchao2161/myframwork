package com.cui.mvvmdemo.http;


public interface SCResultInterface {
	/***
	 * 
	 */
	public <T> void onSuccess(T t, SCSuccess scInformation, SCError scError);

	/***
	 * 
	 */
	public void onFailure(SCSuccess scInformation, SCError scError,
                          Throwable throwable);

	/***
	 * 
	 */
	public void onCancle(SCSuccess scInformation, SCError scError);

	public void onFinish(SCSuccess scInformation, SCError scError);

}
