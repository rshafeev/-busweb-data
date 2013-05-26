package com.pgis.bus.data.repositories.model.impl;

import java.util.Locale;

import com.pgis.bus.data.IConnectionManager;
import com.pgis.bus.data.helpers.LocaleHelper;
import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.data.repositories.orm.impl.Repository;

public class ModelRepository extends Repository {

	protected String langID = LangEnum.c_en.name();

	// LocaleHelper
	public ModelRepository(Locale locale, IConnectionManager connManager) {
		super(connManager);
		this.setLocale(locale);
	}

	public ModelRepository(LangEnum langID, IConnectionManager connManager) {
		super(connManager);
		this.langID = langID.name();
	}

	public ModelRepository(IConnectionManager connManager) {
		super(connManager);
	}

	public String getLocale() {
		return langID;
	}

	public void setLocale(LangEnum langID) {
		this.langID = langID.name();
	}

	public void setLocale(Locale locale) {
		this.langID = LocaleHelper.getDataBaseLanguage(locale);
	}

}
