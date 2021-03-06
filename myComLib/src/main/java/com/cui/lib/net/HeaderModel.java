package com.cui.lib.net;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 


import com.cui.lib.core.BaseModel;

/**
 * Entity mapped to table SCHEADER_MODEL.
 */
public class HeaderModel extends BaseModel {

	private Long id;
	private String syb_appId;
	private String syb_appKey;
	private String syb_operatorCode;
	private String syb_sessionToken;

	public HeaderModel() {
	}

	public HeaderModel(Long id) {
		this.id = id;
	}

	public HeaderModel(Long id, String syb_appId, String syb_appKey,
					   String syb_operatorCode, String syb_sessionToken) {
		this.id = id;
		this.syb_appId = syb_appId;
		this.syb_appKey = syb_appKey;
		this.syb_operatorCode = syb_operatorCode;
		this.syb_sessionToken = syb_sessionToken;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSyb_appId() {
		return syb_appId;
	}

	public void setSyb_appId(String syb_appId) {
		this.syb_appId = syb_appId;
	}

	public String getSyb_appKey() {
		return syb_appKey;
	}

	public void setSyb_appKey(String syb_appKey) {
		this.syb_appKey = syb_appKey;
	}

	public String getSyb_operatorCode() {
		return syb_operatorCode;
	}

	public void setSyb_operatorCode(String syb_operatorCode) {
		this.syb_operatorCode = syb_operatorCode;
	}

	public String getSyb_sessionToken() {
		return syb_sessionToken;
	}

	public void setSyb_sessionToken(String syb_sessionToken) {
		this.syb_sessionToken = syb_sessionToken;
	}

}
