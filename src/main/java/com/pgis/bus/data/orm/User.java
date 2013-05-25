package com.pgis.bus.data.orm;

import com.pgis.bus.data.IConnectionManager;

public class User extends ORMObject implements Cloneable {
	private Integer id;
	private String name;
	private int role_id;

	public User() {
		super();
	}

	public User(IConnectionManager connManager) {
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
	
	@Override
	public User clone() throws CloneNotSupportedException {
		User user = (User) super.clone();
		user.id = this.id;
		if (this.name != null)
		{
		user.name = new String(this.name);
		}
		user.id = this.id;
		return user;
	}

}
