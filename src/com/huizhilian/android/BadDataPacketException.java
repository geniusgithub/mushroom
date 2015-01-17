package com.huizhilian.android;

public class BadDataPacketException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -82943624757808531L;

	public BadDataPacketException(String reason) {
		super(reason);
	}
}
