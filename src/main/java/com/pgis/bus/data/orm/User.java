package com.pgis.bus.data.orm;

import com.pgis.bus.data.IDBConnectionManager;

public class User extends ORMObject {
	private Integer id;
	private String name;
	private int role_id;

	public User() {
		super();
	}

	public User(IDBConnectionManager connManager) {
		super(connManager);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRoleID() {
		return role_id;
	}

	public void setRoleID(int role_id) {
		this.role_id = role_id;
	}

}
