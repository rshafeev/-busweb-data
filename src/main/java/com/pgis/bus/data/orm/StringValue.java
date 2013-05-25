package com.pgis.bus.data.orm;

import java.util.ArrayList;
import java.util.Collection;

import com.pgis.bus.data.orm.type.LangEnum;

public class StringValue implements Cloneable {
	private Integer id;
	private Integer key_id;
	private LangEnum lang_id;
	private String value;

	public StringValue() {

	}

	public StringValue(LangEnum lang_id, String value) {
		this.lang_id = lang_id;
		this.value = value;

	}

	public StringValue(Integer id, Integer key_id, LangEnum lang_id, String value) {
		this.lang_id = lang_id;
		this.id = id;
		this.key_id = key_id;
		this.value = value;

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getKeyID() {
		return key_id;
	}

	public void setKeyID(Integer key_id) {
		this.key_id = key_id;
	}

	public LangEnum getLangID() {
		return lang_id;
	}

	public void setLangID(LangEnum lang_id) {
		this.lang_id = lang_id;
	}

	public void setLangID(String lang_id) {
		this.lang_id = LangEnum.valueOf(lang_id);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "StringValue [id=" + id + ", key_id=" + key_id + ", lang_id=" + lang_id + ", value=" + value + "]";
	}
	
	@Override
	public StringValue clone() throws CloneNotSupportedException {
		StringValue stringValue = (StringValue) super.clone();
		stringValue.id = this.id;
		stringValue.key_id = this.key_id;
		stringValue.lang_id = this.lang_id;
		stringValue.value = this.value;
		return stringValue;
	}
}
