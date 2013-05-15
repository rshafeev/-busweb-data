package com.pgis.bus.data.orm.type;

public enum AuthenticateResult {
	c_auth_ok(0), c_role_invalid(1), c_login_invalid(2), c_password_invalid(3), c_unknown(-1);
	private final int value;

	private AuthenticateResult(int bar) {
		this.value = bar;
	}

	public int getValue() {
		return value;
	}

	public static AuthenticateResult getType(int typeID) {
		for (AuthenticateResult type : AuthenticateResult.values())
			if (type.getValue() == typeID)
				return type;
		return AuthenticateResult.c_unknown;
	}
}
