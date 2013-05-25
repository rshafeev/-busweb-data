package com.pgis.bus.data.models.route.serialized;

import com.pgis.bus.data.orm.Route;

public interface SerializedRouteObjectModel {

	Route toORMObject();

	long getSerialVersionUID();
}
