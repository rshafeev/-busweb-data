package com.pgis.bus.data.orm.type;

import com.pgis.bus.net.models.AlgStrategyEnumModel;

public enum AlgStrategyEnum {
	c_time, c_cost, c_opt;

	public static AlgStrategyEnum valueOf(AlgStrategyEnumModel model) {
		AlgStrategyEnum obj = AlgStrategyEnum.valueOf("c_" + model.name());
		return obj;
	}

	public AlgStrategyEnumModel toModel() {
		AlgStrategyEnumModel model = AlgStrategyEnumModel.valueOf(this.name().substring(3));
		return model;
	}

}
