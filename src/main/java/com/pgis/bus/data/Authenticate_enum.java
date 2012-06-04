package com.pgis.bus.data;

public enum Authenticate_enum {
	c_auth_ok(0),c_role_invalid(1), c_login_invalid(2),c_password_invalid(3), c_unknown(-1);
	private final int value;

	private Authenticate_enum(int bar) {
		this.value = bar;
	}

	public int getValue() {
		return value;
	}

	public static Authenticate_enum getType(int typeID) {
		for (Authenticate_enum type : Authenticate_enum.values())
			if (type.getValue() == typeID)
				return type;
		return Authenticate_enum.c_unknown;
	}
}
