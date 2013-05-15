package com.pgis.bus.data.impl;

import java.sql.SQLException;

public class TransactConnectionException extends SQLException {

	private static final long serialVersionUID = -7920183294246554527L;

	public enum err_enum {
		c_connect_to_db(1), c_received_connect(2);
		private final int value;

		private err_enum(int bar) {
			this.value = bar;
		}

		public int getValue() {
			return value;
		}

		public String getMessage() {
			switch (this) {
			case c_connect_to_db:
				return "DB connection problem";

			case c_received_connect:
				return "Can not received connection from connection manager";
			}
			return "unknown error";
		}
	}

	public TransactConnectionException(err_enum err) {
		super(err.getMessage());

	}
}