package com.pgis.bus.data.orm;

import com.pgis.bus.data.orm.type.LangEnum;

public class Language implements Cloneable {
	public LangEnum id;
	public String name;

	@Override
	public Language clone() throws CloneNotSupportedException {
		Language language = (Language) super.clone();
		language.id = this.id;
		if (this.name != null)
		{
		language.name = new String(this.name);
		}
		return language;
	}

	public LangEnum getId() {
		return id;
	}

	public void setId(LangEnum id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
