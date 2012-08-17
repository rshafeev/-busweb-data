package com.pgis.bus.data.repositories;

public class RepositoryException extends Exception {
	public enum err_enum {
		c_connection_invalid, c_sql_err,c_transaction_err,c_rollback_err;

		public String getMessage() {
			switch (this) {
			case c_connection_invalid:
				return "Can not get connection from Data source connection pool";
			case c_sql_err:
				return "SQL error";
			}
			return "Unknown error";
		}
	}

	public RepositoryException(RepositoryException.err_enum err) {
		super(err.getMessage());
	}

}
