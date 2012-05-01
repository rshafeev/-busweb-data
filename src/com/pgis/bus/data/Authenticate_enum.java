package com.pgis.bus.data;

public enum Authenticate_enum {
	c_password_invalid(1), 
	c_login_invalid(2),
	c_auth_ok(0);
	private final int value;

	private Authenticate_enum(int bar) {
		this.value = bar;
	}

	public int getValue() {
		return value;
	}
}
