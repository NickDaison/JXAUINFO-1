package com.zero.map;

import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.map.TextItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

 /**   
 * Title: CustomTextItem
 * Description:自定义TextItem,统一文字的大小和颜色，以及背景颜色
 * @author DaiS
 * @version 1.0
 * @date 2013-12-17
 */   
public class CustomTextItem extends TextItem{
	
	//文字大小和颜色，以及背景颜色的配置属性
	private  int FONTSIZE=25;
	private final Symbol.Color FONTCOLOR = new Symbol().new Color(255,0,255,0);    
	private final Symbol.Color BGCOLOR = new Symbol().new Color(150,80,80,80);
	
	public CustomTextItem(int latitude,int longtitude,String name){
		super();
		this.fontSize=FONTSIZE;
		this.bgColor=BGCOLOR;
		this.fontColor=FONTCOLOR;
		this.pt=new GeoPoint(latitude,longtitude);
		this.text=name;
	}
	public int getFontSize(){
		return FONTSIZE;
	}
	public void setFontSize(int font){
		this.FONTSIZE=font;
	}
}
