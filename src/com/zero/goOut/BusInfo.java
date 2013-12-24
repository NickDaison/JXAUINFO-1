package com.zero.goOut;

/* 车辆信息  */
/* station 表示前往或者到达的站点名称*/
/* number 表示前往或者到达的公交数量 */
/* flag—— true 表示到达 ， false 表示前往*/
/* dire  表示方向， true表示正方向， false 表示反方向*/
public class BusInfo {
	public String station;
	public int number;
	public boolean flag;
	public boolean dire;
	
	public boolean getDirection(){
		return dire;
	}
	public BusInfo(String s, int n, boolean f){
		station  =s;
		number = n;
		flag = f;
	}
}

