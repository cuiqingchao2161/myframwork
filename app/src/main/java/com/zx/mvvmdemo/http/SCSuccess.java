/**   
 *  
 * @ProjectName:  SybercareSDK 
 * @Package:      com.sybercare.sdk.api  
 * @ClassName:    SCInformation   
 * @Description:  TODO
 * @Author:       zhaijinaye
 * @Company:      Sybercare Technologies Inc     
 * @CreateDate:   2015年2月12日 下午4:16:56   
 * @UpdateUser:   zhaijianye   
 * @UpdateDate:   2015年2月12日 下午4:16:56   
 * @UpdateRemark: TODO
 * @Version:      v1.0 
 * @CopyRight:    Copyright © 2014 康之元 Sybercare Technologies Inc. 
 *    
 */
package com.zx.mvvmdemo.http;

import com.zx.mvvmdemo.constant.SCConstants;

/**
 * 
 * @ClassName: SCInformation
 * @Description: TODO
 * @author zhaijianye
 * @CreateDate: 2015年2月12日 下午4:16:56
 * @UpdateUser: zhaijianye
 * @UpdateDate: 2015年2月12日 下午4:16:56
 * @UpdateRemark: TODO
 * 
 */
public class SCSuccess {

	/** 成功消息 */
	private String successMessage;
	/** 成功消息 编号 */
	private int successCode;
	/** 失败消息 */
	private String failureMessage;

	public SCSuccess() {
	}

	public SCSuccess(int successCode) {
		this.successCode = successCode;
	}

	public SCSuccess(String successMsg, int successCode, String failureMsg) {
		this.successMessage = successMsg;
		this.successCode = successCode;
		this.failureMessage = failureMsg;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getFailureMessage() {
		return failureMessage;
	}

	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}

	public int getSuccessCode() {
		return successCode;
	}

	public void setSuccessCode(int successCode) {
		this.successCode = successCode;
	}

    public static SCSuccess defaultSuccess(){
        return new SCSuccess(SCConstants.SCSuccessCode.get(3000)
                .toString(), 3000, "");
    }

}
