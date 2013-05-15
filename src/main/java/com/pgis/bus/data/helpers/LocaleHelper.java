package com.pgis.bus.data.helpers;

import java.util.Locale;

import com.pgis.bus.data.orm.type.LangEnum;
import com.pgis.bus.net.models.LangEnumModel;

public class LocaleHelper {

	/**
	 * Коды языков, хранящ. в БД отличаются в написании от кода языка, хран. в классе Locale, поэтому данный метод
	 * задает соответствие между этими различиями
	 * 
	 * @param локаль
	 * @return код языка в соответствии с БД
	 */
	public static String getDataBaseLanguage(Locale locale) {
		if (locale.getLanguage() == null || locale.getLanguage().equalsIgnoreCase("rus"))
			return "c_ru";

		return "c_" + locale.getLanguage();
	}

	/**
	 * Преобразует идентификатор языка ru, en, uk в c_ru, c_en, c_uk
	 * 
	 * @param langID Идентификатор языка. Возможные значения ru, en, uk
	 * @return Идентификатор языка, используемый на уровне БД
	 */
	public static LangEnum getDataBaseLanguage(LangEnumModel langID) {
		return LangEnum.valueOf(langID);
	}

	public static String getLangID(Locale locale) {
		if (locale.getLanguage() == null || locale.getLanguage().equalsIgnoreCase("rus"))
			return "ru";
		return locale.getLanguage();
	}

}
