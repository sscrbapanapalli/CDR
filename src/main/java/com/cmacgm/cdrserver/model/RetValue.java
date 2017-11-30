package com.cmacgm.cdrserver.model;

public class RetValue<T> {

	private Boolean status;
	private String message;
	private T data;
	private Integer totalRecord;

	public RetValue(Boolean status, String message, T data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public RetValue(Boolean status, String message, T data, Integer totalRecord) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
		this.totalRecord = totalRecord;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Integer getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(Integer totalRecord) {
		this.totalRecord = totalRecord;
	}
	
}