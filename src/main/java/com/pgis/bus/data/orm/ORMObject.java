package com.pgis.bus.data.orm;

import com.pgis.bus.data.IConnectionManager;

public class ORMObject {

	protected IConnectionManager connManager;

	public ORMObject() {

	}

	public ORMObject(IConnectionManager connManager) {
		this.connManager = connManager;
	}

	public void setConnManager(IConnectionManager connManager) {
		this.connManager = connManager;
	}

	public void copy(ORMObject from) {
		this.connManager = from.connManager;
	}

}
