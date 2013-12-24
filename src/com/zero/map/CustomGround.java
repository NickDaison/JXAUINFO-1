/**
 * 
 */
package com.zero.map;

import com.baidu.platform.comapi.basestruct.GeoPoint;

 /**   
 * Title: CustomGround
 * Description:自定义的Ground类，用来作读取存放覆盖面的xml中数据的容器，用于初始化覆盖面。
 * @author DaiS
 * @version 1.0
 * @date 2013-12-18
 */

public class CustomGround {
	
	private GeoPoint LBPoint;
	private GeoPoint RTPoint;
	private int bitMapId;
	
	public CustomGround(int lbLa,int lbLo,int rtLa,int rtLo,int id ){
		LBPoint=new GeoPoint(lbLa,lbLo);
		RTPoint=new GeoPoint(rtLa,rtLo);
		this.bitMapId=id;
	}
	public GeoPoint getLBPoint(){
		return LBPoint;
	}
	
	public void setLBPoint(GeoPoint LBPoint){
		this.LBPoint=LBPoint;
	}
	public GeoPoint getRTPoint(){
		return RTPoint;
	}
	public void setRTPoint(GeoPoint RTPoint){
		this.RTPoint=RTPoint;
	}
	
	public int getbitMapId(){
		return bitMapId;
	}
	public void setbitMapId(int bitMapId){
		this.bitMapId=bitMapId;
	}
}
