package com.zero.map;

import android.graphics.drawable.Drawable;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

 /**   
 * Title: CustomOverlay
 * Description:自定义图标覆盖层
 * @author DaiS
 * @version 1.0
 * @date 2013-12-15
 */   
public class CustomOverlay extends ItemizedOverlay<OverlayItem> {  
    //用MapView构造ItemizedOverlay  
    public CustomOverlay(Drawable mark,MapView mapView ){  
            super(mark,mapView);
          //  addItem(items);
    }  
    protected boolean onTap(int index) {  
        //在此处理item点击事件  
        System.out.println("item onTap: "+index);  
        return true;  
    }  
  
    public boolean onTap(GeoPoint pt, MapView mapView){  
            //在此处理MapView的点击事件，当返回 true时  
            super.onTap(pt,mapView);  
            return false;  
    }  
}          
