package com.ajmst.common.response;

import com.ajmst.common.exception.ExceptionUtil;

public class Response {
	private boolean isOk = true;
	private Object result = null;
	private Exception exception = null;
	
	public boolean isOk(){
		return isOk;
	}
	
	public void setIsOk(boolean isOk){
		this.isOk = isOk;
	}
	
	public void setResult(Object result){
		this.result = result;
	}
	
	public void setException(Exception e){
		this.setIsOk(false);
		this.exception = e;
	}

	public Object getResult() {
		return result;
	}

	public Exception getException() {
		return exception;
	}
	
	public String getExceptionStr(){
		return ExceptionUtil.getStackTrace(this.getException());
	}
	
	
}
