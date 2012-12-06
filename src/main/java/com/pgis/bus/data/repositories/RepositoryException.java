package com.pgis.bus.data.repositories;

public class RepositoryException extends Exception {

	private static final long serialVersionUID = -7853309755220159118L;
	private err_enum errCode;

	public enum err_enum {
		c_connection_invalid, c_sql_err, c_transaction_err, c_rollback_err, c_route_data, c_route_already_exist, c_error_unknown, c_id_notFind, c_insertDirectRoute;

		public String getMessage() {
			switch (this) {
			case c_connection_invalid:
				return "Can not get connection from Data source connection pool";
			case c_sql_err:
				return "SQL error";
			default:
				break;
			}
			return this.name();
		}
	}

	public RepositoryException() {
		super(err_enum.c_error_unknown.name());
		this.errCode = err_enum.c_error_unknown;
	}

	public RepositoryException(String errorMessage) {
		super(errorMessage);
		this.errCode = err_enum.c_error_unknown;
	}

	public RepositoryException(RepositoryException.err_enum err) {
		super(err.getMessage());
		this.errCode = err;
	}
	public RepositoryException(RepositoryException.err_enum err,String text) {
		super(err.getMessage() + "\ntext: " + text);
		this.errCode = err;
	}

	public err_enum getErrorCode() {
		return errCode;
	}

}
