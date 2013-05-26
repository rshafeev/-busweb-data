package com.pgis.bus.data.exp;

import java.sql.SQLException;

public class RepositoryException extends SQLException {

	private static final long serialVersionUID = -7853309755220159118L;
	private err_enum errCode;

	public enum err_enum
	{
		unknown,
		connection_invalid,
		transaction_err,
		rollback_err,
		already_exist,
		id_not_find,
		insert_rway,
		orm_obj_invalid,
		commit,
		rollback,
		return_data_not_found;

		public String getMessage() {
			switch (this) {
			case connection_invalid:
				return "Can not get connection from Data source connection pool";
			case transaction_err:
				return "SQL error";
			default:
				break;
			}
			return this.name();
		}
	}

	public RepositoryException(Exception e) {
		if (e instanceof SQLException) {
			errCode = err_enum.transaction_err;
		}
	}

	public RepositoryException() {
		super(err_enum.unknown.name());
		this.errCode = err_enum.unknown;

	}

	public RepositoryException(String errorMessage) {
		super(errorMessage);
		this.errCode = err_enum.unknown;
	}

	public RepositoryException(RepositoryException.err_enum err) {
		super(err.getMessage());
		this.errCode = err;
	}

	public RepositoryException(RepositoryException.err_enum err, String text) {
		super(err.getMessage() + "\ntext: " + text);
		this.errCode = err;
	}

	public err_enum getCode() {
		return errCode;
	}

}
