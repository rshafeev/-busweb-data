package com.pgis.bus.data.orm.type;

import com.pgis.bus.net.models.DayEnumModel;

public enum DayEnum {
	c_Sunday, c_Monday, c_Tuesday, c_Wednesday, c_Thursday, c_Friday, c_Saturday, c_all;

	public static DayEnum valueOf(DayEnumModel model) {
		DayEnum obj = DayEnum.valueOf("c_" + model.name());
		return obj;
	}

	public DayEnumModel toModel() {
		return DayEnumModel.valueOf(this.name().substring(2));
	}
}
