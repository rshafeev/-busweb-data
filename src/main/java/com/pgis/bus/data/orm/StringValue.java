package com.pgis.bus.data.orm;

import java.util.ArrayList;
import java.util.Collection;

import com.pgis.bus.net.models.StringValueModel;

public class StringValue {
	private Integer id;
	private Integer key_id;
	private String lang_id;
	private String value;

	public StringValue() {

	}

	public StringValue(String lang_id, String value) {
		this.lang_id = lang_id;
		this.value = value;

	}

	public StringValue(int keyID, StringValueModel v) {
		this.key_id = keyID;
		this.id = v.getId();
		this.lang_id = v.getLang();
		this.value = v.getValue();
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

	public String getLangID() {
		return lang_id;
	}

	public void setLangID(String lang_id) {
		this.lang_id = lang_id;
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

	public static Collection<StringValue> createCollection(int keyID, Collection<StringValueModel> arr) {
		Collection<StringValue> coll = new ArrayList<StringValue>();
		for (StringValueModel v : arr) {
			coll.add(new StringValue(keyID, v));
		}

		return coll;
	}

}
