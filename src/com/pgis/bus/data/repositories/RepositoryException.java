package com.pgis.bus.data.repositories;

public class RepositoryException extends Exception {
	public enum err_enum {
		c_connection_invalid;

		public String getMessage() {
			switch (this) {
			case c_connection_invalid:
				return "Can not get connection from Data source connection pool";

			}
			return "Unknown error";
		}
	}

	public RepositoryException(RepositoryException.err_enum err) {
		super(err.getMessage());
	}

}
