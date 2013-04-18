package com.pgis.bus.data.repositories.model.impl;

import java.util.Locale;

import com.pgis.bus.data.IDBConnectionManager;
import com.pgis.bus.data.helpers.LocaleHelper;
import com.pgis.bus.data.repositories.Repository;

public class ModelRepository extends Repository {

	protected String langID = "c_en";

	// LocaleHelper
	public ModelRepository(Locale locale, IDBConnectionManager connManager) {
		super(connManager);
		this.langID = LocaleHelper.getDataBaseLanguage(locale);
	}

	public ModelRepository(Locale locale, IDBConnectionManager connManager, boolean isCommited) {
		super(connManager, isCommited);
		this.langID = LocaleHelper.getDataBaseLanguage(locale);
	}

	public ModelRepository(String langID, IDBConnectionManager connManager) {
		super(connManager);
		this.langID = langID;
	}

	public ModelRepository(String langID, IDBConnectionManager connManager, boolean isCommited) {
		super(connManager, isCommited);
		this.langID = langID;
	}

	public ModelRepository(IDBConnectionManager connManager) {
		super(connManager);
	}

	public ModelRepository(IDBConnectionManager connManager, boolean isCommited) {
		super(connManager, isCommited);
	}

	public String getLangID() {
		return langID;
	}

	public void setLangID(String langID) {
		this.langID = langID;
	}

}
