/**
 * 
 */
package com.zero.map.readXml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.zero.map.CustomGround;
import com.zero.map.CustomItem;

 /**   
 * Title: XmlReader
 * Description:用来读取存储地图覆盖点的xml文件
 * @author DaiS
 * @date 2013-12-18
 */

public class XmlReader {
	
	private static ArrayList<CustomItem> items = new ArrayList<CustomItem>();
	private static ArrayList<CustomGround> grounds=new ArrayList<CustomGround>();
	private static HashMap<String,GeoPoint> map= new HashMap<String,GeoPoint>();
	/**
	 * Description:用来读取存放覆盖点的xml文件
	 * @param XmlPullParser解析器
	 * @return 存放覆盖点信息的ArrayList<CustomItem>
	 */
	public static ArrayList<CustomItem> getCustomItemInfo(XmlPullParser xpp){
		try {
			int lat=0,lon=0,ID=0;
			String name="";
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
		        switch (eventType) {
		        case XmlPullParser.START_DOCUMENT:
		            break;
		        case XmlPullParser.END_DOCUMENT:
		            break;
		        case XmlPullParser.START_TAG: {
		            String tagName = xpp.getName();
		            if (tagName != null && tagName.equals("pointItem")) {
		            	
		            }
		            if (tagName != null && tagName.equals("latitude")) {
		                    lat=Integer.parseInt(xpp.nextText());
		            }
		            if (tagName != null && tagName.equals("longtitude")) {
		                	lon=Integer.parseInt(xpp.nextText());
		            }
		            if (tagName != null && tagName.equals("name")) {
		                    name=xpp.nextText();
		            }
		            if (tagName != null && tagName.equals("ImageResId")) {
		                	ID = Integer.parseInt(xpp.nextText().replace("0x",""), 16);
		            }
		        }
		        break;
		        case XmlPullParser.END_TAG: {
		        	 if (xpp.getName().equals("pointItem")) {
	                       items.add(new CustomItem(lat,lon,name,ID));
	                   }
		        }
		            break;
		        case XmlPullParser.TEXT:
		            break;
		        default:
		            break;
		        }
		        eventType = xpp.next();
		    }
		} catch (Exception e) {
		    // TODO: handle exception
		}
		return items;
	}
	/**
	 * Description:用来读取存放覆盖面的xml文件
	 * @param XmlPullParser解析器
	 * @return 存放覆盖点信息的ArrayList<CustomGround>
	 */
	public static ArrayList<CustomGround> getGroundInfo(XmlPullParser xpp){
		int lbLat=0,lbLon=0,id=0,rtLat=0,rtLon=0;
		try {
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
		        switch (eventType) {
		        case XmlPullParser.START_DOCUMENT:
		            break;
		        case XmlPullParser.END_DOCUMENT:
		            break;
		        case XmlPullParser.START_TAG: {
		            String tagName = xpp.getName();
		            if (tagName != null && tagName.equals("groundItem")) {
		            }
		            if (tagName != null && tagName.equals("LBlatitude")) {
		                    lbLat=Integer.parseInt(xpp.nextText());
		            }
		            if (tagName != null && tagName.equals("LBlongtitude")) {
		                	lbLon=Integer.parseInt(xpp.nextText());
		            }
		            if (tagName != null && tagName.equals("RTlatitude")) {
		                    rtLat=Integer.parseInt(xpp.nextText());
		            }
		            if (tagName != null && tagName.equals("RTlongtitude")) {
		            	    rtLon=Integer.parseInt(xpp.nextText());
	            }
		            if (tagName != null && tagName.equals("ImageResId")) {
		                	id = Integer.parseInt(xpp.nextText().replace("0x",""), 16);
		            }
		        }
		        break;
		        case XmlPullParser.END_TAG: {
		        	 if (xpp.getName().equals("groundItem")) {
	                       grounds.add(new CustomGround(lbLat,lbLon,rtLat,rtLon,id));
	                   }
		        }
		            break;
		        case XmlPullParser.TEXT:
		            break;
		        default:
		            break;
		        }
		        eventType = xpp.next();
		    }
		} catch (Exception e) {
		    // TODO: handle exception
		}
		return grounds;
	}
	
	public static HashMap<String,GeoPoint> getPointDB(XmlPullParser xpp){
		
		try {
			int lat=0,lon=0;
			String name="";
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
		        switch (eventType) {
		        case XmlPullParser.START_DOCUMENT:
		            break;
		        case XmlPullParser.END_DOCUMENT:
		            break;
		        case XmlPullParser.START_TAG: {
		            String tagName = xpp.getName();
		            if (tagName != null && tagName.equals("pointItem")) {
		            }
		            if (tagName != null && tagName.equals("latitude")) {
		                    lat=Integer.parseInt(xpp.nextText());
		            }
		            if (tagName != null && tagName.equals("longtitude")) {
		                	lon=Integer.parseInt(xpp.nextText());
		            }
		            if (tagName != null && tagName.equals("name")) {
		                    name=xpp.nextText();
		            }
		        }
		        break;
		        case XmlPullParser.END_TAG: {
		        	 if (xpp.getName().equals("pointItem")) {
	                      map.put(name, new GeoPoint(lat,lon));
	                   }
		        }
		            break;
		        case XmlPullParser.TEXT:
		            break;
		        default:
		            break;
		        }
		        eventType = xpp.next();
		} 
		}catch (Exception e) {
			// TODO: handle exception
		}
		return map;
	}
}
