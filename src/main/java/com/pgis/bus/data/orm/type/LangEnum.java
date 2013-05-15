package com.pgis.bus.data.orm.type;

import java.util.Locale;

import com.pgis.bus.data.helpers.LocaleHelper;
import com.pgis.bus.net.models.LangEnumModel;

public enum LangEnum {
	c_ru, c_en, c_uk;

	public static LangEnum valueOf(Locale locale) {
		String langID = LocaleHelper.getDataBaseLanguage(locale);
		LangEnum obj = LangEnum.valueOf(langID);
		return obj;
	}

	public static LangEnum valueOf(LangEnumModel model) {
		LangEnum obj = LangEnum.valueOf("c_" + model.name());
		return obj;
	}

	public LangEnumModel toModel() {
		LangEnumModel model = LangEnumModel.valueOf(this.name().substring(2));
		return model;
	}
}
