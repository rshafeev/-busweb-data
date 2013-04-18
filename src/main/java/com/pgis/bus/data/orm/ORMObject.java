package com.pgis.bus.data.orm;

import com.pgis.bus.data.IDBConnectionManager;

public class ORMObject {

	protected IDBConnectionManager connManager;

	public ORMObject() {

	}

	public ORMObject(IDBConnectionManager connManager) {
		this.connManager = connManager;
	}

	public void setConnManager(IDBConnectionManager connManager) {
		this.connManager = connManager;
	}

	public void copy(ORMObject from) {
		this.connManager = from.connManager;
	}

}
