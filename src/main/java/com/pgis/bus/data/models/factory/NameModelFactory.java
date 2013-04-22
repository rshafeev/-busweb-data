package com.pgis.bus.data.models.factory;

import java.util.ArrayList;
import java.util.Collection;

import com.pgis.bus.data.orm.StringValue;
import com.pgis.bus.net.models.NameModel;

public class NameModelFactory {

	public static NameModel createModel(StringValue value) {
		NameModel model = new NameModel();
		model.setId(value.getId());
		model.setLangID(value.getLangID());
		model.setName(value.getValue());
		return model;
	}

	public static Collection<NameModel> createModels(Collection<StringValue> arr) {
		Collection<NameModel> model = new ArrayList<NameModel>();
		for (StringValue v : arr) {
			model.add(createModel(v));
		}
		return model;
	}
}
