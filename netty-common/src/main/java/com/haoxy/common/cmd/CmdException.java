package com.haoxy.common.cmd;


public class CmdException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8862128571550928662L;

	public CmdException(String string, Exception e) {
		super(string,e);
	}
	
	public CmdException(String string) {
		super(string);
	}
}