package com.zero.map;

import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.TextItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * Title: CustomItem
 * Description:自定义地图覆盖点，包括一个文字（CustomTextItem）和图片描述（OverlayItem）
 * 
 * @author DaiS
 * @date 2013-12-15
 */
public class CustomItem {
	//覆盖点的文字描述
	private CustomTextItem tItem;
	//覆盖点的图标表示
	private OverlayItem oItem;
	//图片ID号
	private int id;

	/**
	 * 由于OverlayItem可以自己设定图标，也可以使用它的上层的默认图标，所以有两个构造器。
	 * 带id的构造器，图标信息用id号表示
	 * @param mLat
	 * @param mLon
	 * @param title
	 * @param id
	 */
	public CustomItem(int mLat, int mLon, String title, int id) {
		oItem = new OverlayItem(new GeoPoint(mLat, mLon), title, title);
		this.id = id;
		tItem = new CustomTextItem(mLat, mLon, title);
	}

	/**
	 * 不带图标的构造器
	 * @param mLat
	 * @param mLon
	 * @param title
	 */
	public CustomItem(int mLat, int mLon, String title) {
		oItem = new OverlayItem(new GeoPoint(mLat, mLon), title, title);
		tItem = new CustomTextItem(mLat, mLon, title);
	}

	public TextItem getTextItem() {
		return tItem;
	}
	public void setTextItem(CustomTextItem tItem) {
		this.tItem=tItem;
	}
	public OverlayItem getOverlayItem() {
		return oItem;
	}
	public void setTextItem(OverlayItem oItem) {
		this.oItem=oItem;
	}
	
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id=id;
	}
}
