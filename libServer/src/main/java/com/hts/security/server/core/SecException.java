package com.hts.security.server.core;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 *
 *
 *@author qj
 *
 */



public class SecException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int errorCode;

	public SecException(int errorCode) {
		this.errorCode = errorCode;
	}

	public SecException(int errorCode, String detailMessage) {
		super(detailMessage);
		this.errorCode = errorCode;
	}

	public SecException(int errorCode, Throwable throwable) {
		super(throwable);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void printStackTrace(PrintStream printStream){
		printStream.println("ErrorCode = "+ getErrorCode());
		super.printStackTrace(printStream);
	}

	public void printStackTrace(PrintWriter printWriter){
		printWriter.println("ErrorCode = "+ getErrorCode());
		super.printStackTrace(printWriter);
	}

}

