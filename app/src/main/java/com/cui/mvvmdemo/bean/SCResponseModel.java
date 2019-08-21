package com.cui.mvvmdemo.bean;


import com.cui.mvvmdemo.constant.SCConstants;
import com.cui.mvvmdemo.utils.SCUtil;

/**
 * Created by zhangxingxing-PC on 2015/12/8.
 */
public class SCResponseModel<T> {

    private String syb_consumerSeqNo;

    private String syb_info;

    private String syb_providerSeqNo;

    private String syb_status;

    public SCResponseModel() {

    }


    public String getSyb_consumerSeqNo() {
        return syb_consumerSeqNo;
    }

    public void setSyb_consumerSeqNo(String syb_consumerSeqNo) {
        this.syb_consumerSeqNo = syb_consumerSeqNo;
    }

    public String getSyb_info() {
        return syb_info;
    }

    public void setSyb_info(String syb_info) {
        this.syb_info = syb_info;
    }

    public String getSyb_providerSeqNo() {
        return syb_providerSeqNo;
    }

    public void setSyb_providerSeqNo(String syb_providerSeqNo) {
        this.syb_providerSeqNo = syb_providerSeqNo;
    }

    public String getSyb_status() {
        return syb_status;
    }

    public void setSyb_status(String syb_status) {
        this.syb_status = syb_status;
    }

    public T getSyb_data() {
        return syb_data;
    }

    public void setSyb_data(T syb_data) {
        this.syb_data = syb_data;
    }

    private T syb_data;

    public SCResponseModel(String syb_consumerSeqNo, String syb_info, String syb_providerSeqNo, String syb_status, T syb_data) {
        this.syb_consumerSeqNo = syb_consumerSeqNo;
        this.syb_info = syb_info;
        this.syb_providerSeqNo = syb_providerSeqNo;
        this.syb_status = syb_status;
        this.syb_data = syb_data;
    }

    public boolean isResponseSuccess(){
        return !SCUtil.isNullOrEmpty(syb_status) && syb_status.equals(SCConstants.SC_STATUS_SUCCESS);
    }
}
