/**
 * 
 */
package com.zero.goOut;

import java.io.Serializable;

 /**   
 * Title: GoOutInfo
 * Description:传递出行信息
 * @author DaiS
 * @date 2013-12-20
 */

public class GoOutInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String currentLocation;
	private String line;
	
	//true表示出发，false表示到达
	private boolean direction;
	
	public GoOutInfo(String cL,String line,boolean dir){
		this.currentLocation=cL;
		this.line=line;
		this.direction=dir;
	} 
	
	public String  getCurrentLocation(){
		return currentLocation;
	}
	public void setCurrentLocation(String cL){
		this.currentLocation=cL;
	}
	
	public String getLine(){
		return line;
	}
	public void setLine(String line){
		this.line=line;
	}
	
	public boolean getDirection(){
		return direction;
	}
	public void setDirection(boolean dir){
		this.direction=dir;
	}
	
}
