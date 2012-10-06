package com.pgis.bus.data.models;

/**
 * Хранит параметры для загрузки геоданных о маршруте, начиная с дуги, индекс
 * которой indexStart, и заканчивая дугой indexFinish. Коллекция геоданных о
 * маршруте, которая возвращается в ответ на данный объект имеет тип
 * RouteGeoData
 * 
 * @author romario
 * 
 */
public class RoutePart {
	/**
	 * id маршрута(точнее направления маршрута: у каждого маршрута есть два
	 * направления: прямой и обратный. Поэтому дуги маршрута зависят также от
	 * направления)
	 */
	private int directRouteID;

	/**
	 * Индекс первой дуги
	 */
	private int indexStart;

	/**
	 * Индекс последней дуги
	 */
	private int indexFinish;

	public int getDirectRouteID() {
		return directRouteID;
	}

	public void setDirectRouteID(int directRouteID) {
		this.directRouteID = directRouteID;
	}

	public int getIndexStart() {
		return indexStart;
	}

	public void setIndexStart(int indexStart) {
		this.indexStart = indexStart;
	}

	public int getIndexFinish() {
		return indexFinish;
	}

	public void setIndexFinish(int indexFinish) {
		this.indexFinish = indexFinish;
	}
}
