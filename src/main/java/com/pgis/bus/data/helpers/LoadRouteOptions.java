package com.pgis.bus.data.helpers;

public class LoadRouteOptions {
	
	private boolean isLoadRouteNamesData;
	private LoadDirectRouteOptions directRouteOptions;
	
	public boolean isLoadRouteNamesData() {
		return isLoadRouteNamesData;
	}
	public void setLoadRouteNamesData(boolean isLoadRouteNamesData) {
		this.isLoadRouteNamesData = isLoadRouteNamesData;
	}
	public boolean isLoadDirectRouteData() {
		return directRouteOptions==null ? false : true;
	}
	
	public LoadDirectRouteOptions getDirectRouteOptions() {
		return directRouteOptions;
	}
	public void setDirectRouteOptions(LoadDirectRouteOptions directRouteOptions) {
		this.directRouteOptions = directRouteOptions;
	}
	
	
}
