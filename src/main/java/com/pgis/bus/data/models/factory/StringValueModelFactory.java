package com.pgis.bus.data.models.factory;

import java.util.ArrayList;
import java.util.Collection;

import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.net.models.StringValueModel;

public class StringValueModelFactory {

	public static StringValueModel createModel(StringValue v){
		StringValueModel model = new StringValueModel();
		model.setId(v.id);
		model.setLang(v.lang_id);
		model.setValue(v.value);
		return model;
	}
	
	public static Collection<StringValueModel> createModels(Collection<StringValue>  arr){
		Collection<StringValueModel> models = new ArrayList<StringValueModel>();
		for(StringValue v : arr){
			models.add(createModel(v));
		}
		return models;
	}
}
