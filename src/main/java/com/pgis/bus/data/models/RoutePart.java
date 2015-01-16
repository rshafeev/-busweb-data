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
	private int ID;

	/**
	 * Индекс первой дуги
	 */
	private int startInd;

	/**
	 * Индекс последней дуги
	 */
	private int finishInd;

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public int getStartInd() {
		return startInd;
	}

	public void setStartInd(int startInd) {
		this.startInd = startInd;
	}

	public int getFinishInd() {
		return finishInd;
	}

	public void setFinishInd(int finishInd) {
		this.finishInd = finishInd;
	}
}
