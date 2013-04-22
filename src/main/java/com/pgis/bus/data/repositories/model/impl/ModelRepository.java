package com.pgis.bus.data.repositories.model.impl;

import java.util.Locale;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.helpers.LocaleHelper;
import com.pgis.bus.data.repositories.Repository;

public class ModelRepository extends Repository {

	protected String langID = "c_en";

	// LocaleHelper
	public ModelRepository(Locale locale, IConnectionManager connManager) {
		super(connManager);
		this.setLocale(locale);
	}

	public ModelRepository(String langID, IConnectionManager connManager) {
		super(connManager);
		this.langID = langID;
	}

	public ModelRepository(IConnectionManager connManager) {
		super(connManager);
	}

	public String getLocale() {
		return langID;
	}

	public void setLocale(String langID) {
		this.langID = langID;
	}

	public void setLocale(Locale locale) {
		this.langID = LocaleHelper.getDataBaseLanguage(locale);
	}

}
