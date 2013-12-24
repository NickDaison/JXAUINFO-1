package com.zero.jxauapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.Ground;
import com.baidu.mapapi.map.GroundOverlay;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.map.TextOverlay;
import com.baidu.mapapi.map.TransitOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.zero.map.CustomGround;
import com.zero.map.CustomItem;
import com.zero.map.CustomOverlay;
import com.zero.map.readXml.XmlReader;

/**
 * Title: jxauMapAct Description:地图页面
 * 
 * @author DaiS
 * @version 1.0
 * @date 2013-12-14
 */
public class jxauMapAct extends FragmentActivity implements OnClickListener{

	Button routeBtn; //线路选择
	BMapManager mBMapManager = null; //百度地图管理器
	MapView mMapView = null;//地图视图
	MKSearch mSearch = null;//搜索模块

	//线路规划相关
	LinearLayout linear; //对话框
	AutoCompleteTextView editStart;//起点输入自动补全框
	AutoCompleteTextView editEnd;//终点输入自动补全框
	Button mBtnDrive = null; //驾车搜索
	Button mBtnTransit = null; //公交搜索
	Button mBtnWalk = null; //步行搜索
	Button mbtnLoc = null;  //自动定位
	LocationClient locationClient = null; //定位
	Button mBtnHit = null;  //屏幕取点
	
	GeoPoint routeEndPoint; //线路规划的终点坐标
	GeoPoint routeStartPoint;//线路规划的起点坐标
	
	RouteOverlay routeOverlay = null;  //线路图层
	TransitOverlay transitOverlay = null;//公交图层
	
	ArrayAdapter<String> adapter;//自动补全监听器
	Map<String,GeoPoint> map= new HashMap<String,GeoPoint>();//存放从xml文件中添加的补全数据
	//表示输入框的状态
	private final String STATU_END="地图上的点";
	private final String STATU_START="当前位置";
	
	//出行方式，0表示步行，1表示公交，2表示开车
	private final int VEHICLE_WALK=0;   
	private final int VEHICLE_TRANSIT=1;
	private final int VEHICLE_DRIVE=2;
	
	private static final int UPDATE_TIME = 100000;//定时定位的时间间隔。单位毫秒
	//是否响应屏幕点击
	private boolean enClickAble=false;
	//百度地图的key
	private final String baiDuMapKey="lVHeadDQWp9Y18FdCmbORMZs";
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mBMapManager = new BMapManager(getApplication());
		mBMapManager.init(baiDuMapKey, null);
		
		setContentView(R.layout.jxau_map_layout);
		
		mMapView = (MapView) findViewById(R.id.bmapsView);
		// 设置启用内置的缩放控件
		mMapView.setBuiltInZoomControls(true);
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		MapController mMapController = mMapView.getController();
		// 用给定的经纬度构造一个中心点
		GeoPoint point = new GeoPoint((int) (28.768217 * 1E6),
				(int) (115.839311 * 1E6));
		mMapController.setCenter(point);// 设置地图中心点
		mMapController.setZoom(17);// 设置地图zoom级别
		mMapController.enableClick(false);
		//渲染覆盖点和覆盖面
		getCustomInfoFromAssetsXML();
		getGroundInfoFromAssetsXML();
		//初始化mSearch，添加监听器
	    mSearch = new MKSearch();
		mSearch.init(mBMapManager, myMKSearchListener);
		//为MapView添加点击事件监听
		mMapView.regMapTouchListner(MymapTouchListener);
		linear = (LinearLayout) findViewById(R.id.map_linear_layout);
		mBtnDrive = (Button) findViewById(R.id.drive_btn);
		mBtnTransit = (Button) findViewById(R.id.transit_btn);
		mBtnWalk = (Button) findViewById(R.id.walk_btn);
		editStart = (AutoCompleteTextView) findViewById(R.id.start_ACTextView);
		editEnd =  (AutoCompleteTextView) findViewById(R.id.end_ACTextView);
		mbtnLoc = (Button) findViewById(R.id.auto_loc_button);
		mBtnHit = (Button) findViewById(R.id.screen_hit_btn);
		routeBtn = (Button) findViewById(R.id.map_route_btn);
		
		//线路按钮，添加监听，单击显示线路规划对话框
		routeBtn.setOnClickListener(this);
		//为定位按钮添加监听
		mbtnLoc.setOnClickListener(this);
		mBtnHit.setOnClickListener(this);
		//为搜索按钮添加监听器
		mBtnDrive.setOnClickListener(this);
		mBtnTransit.setOnClickListener(this);
		mBtnWalk.setOnClickListener(this);
		//初始话定位服务
		initLocationService();
		InfoFromAssetsXML();
		initAutoCompleteTextView();
	}
	
	MKMapTouchListener MymapTouchListener=new MKMapTouchListener(){
		@Override
		public void onMapClick(GeoPoint point){
			// 在此处理地图点击事件
			if(enClickAble){
				editEnd.setText("地图上的点");
				routeEndPoint = point;
				Toast.makeText(getApplicationContext(), point.toString(),
						Toast.LENGTH_LONG).show();
				mMapView.getController().enableClick(false);
				enClickAble=false;
				mBtnHit.setText("屏幕取点");
			}
		}
		@Override
		public void onMapDoubleClick(GeoPoint point) {

		}

		@Override
		public void onMapLongClick(GeoPoint point) {

		}
	};
	/**
	 * AutoCompleteTextView监听器
	 */
	 TextWatcher watcher = new TextWatcher() {  
        @Override  
        public void onTextChanged(CharSequence s, int start, int before,  
                int count) {  
        	adapter.clear();  //清空adapter适配器  
            if (s.length() > 0 ) {
            	 for (String key : map.keySet()) {  
                     if(key.indexOf(s.toString().trim())!=-1){
                    	 adapter.add(key);
                     }  
                 }  
            }  
        }  
        @Override  
        public void beforeTextChanged(CharSequence s, int start, int count,  
                int after) {  
        }  
  
        @Override  
        public void afterTextChanged(Editable s) {
        }  
    };
	/**
	 * 初始化，自动补全框的功能
	 */
	public void initAutoCompleteTextView(){
		
		String[] autoStrings=new String[]{"北区食堂","南区食堂"};
        ArrayList<String> lst = new ArrayList<String>();
        //设置ArrayAdapter，并且设定以单行下拉列表风格展示（第二个参数设定）。
        lst.addAll(Arrays.asList(autoStrings));
        adapter = new ArrayAdapter<String>(this, 
                android.R.layout.simple_list_item_1, lst);
        //设置AutoCompleteTextView的Adapter
        editStart.setAdapter(adapter);
        editEnd.setAdapter(adapter);
        getCustomInfoFromAssetsXML();
        editStart.addTextChangedListener(watcher);
        editEnd.addTextChangedListener(watcher);
	}
	
	
	
	/**
	 * 初始化定位服务
	 */
	public void initLocationService() {
		
		locationClient = new LocationClient(this);
		// 设置定位条件
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 是否打开GPS
		option.setCoorType("bd09ll"); // 设置返回值的坐标类型。
		option.setPriority(LocationClientOption.NetWorkFirst); // 设置定位优先级
		option.setProdName("com.zero.jaxuapp"); // 设置产品线名称
		option.setScanSpan(UPDATE_TIME); // 设置定时定位的时间间隔。单位毫秒
		locationClient.setLocOption(option);

		// 注册位置监听器
		locationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				// TODO Auto-generated method stub
				if (location == null) {
					return;
				}
				routeStartPoint=new GeoPoint((int)(location.getLatitude()* 1E6),(int)(location.getLongitude()* 1E6));
				editStart.setText("当前位置");
				//
				Toast.makeText(jxauMapAct.this, "定位成功",
						Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onReceivePoi(BDLocation location) {
			}
		});
	}
	
	/**
	 * 显示起点选择对话框
	 * @param way
	 * @param startArray
	 * @param endArray
	 */
	public void showStartDialog(final int way,final String[] startArray,
			final String[] endArray) {
		new AlertDialog.Builder(jxauMapAct.this)
		.setTitle("请选择起点")
		.setSingleChoiceItems(startArray, -1, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//flag为true表示选择起点，false表示选择终点
					editStart.setText(startArray[which]);
			}
		})
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
					if(!editEnd.getText().toString().equals(STATU_END)){
						showEndDialog(way,endArray);
				}
			}
			})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				}
			})
		.create().show();
	}
	
	/**
	 * 显示终点选择对话框
	 * @param way
	 * @param endArray
	 */
	public void showEndDialog(final int way,final String[] endArray) {
		new AlertDialog.Builder(jxauMapAct.this)
		.setTitle("请选择终点")
		.setSingleChoiceItems(endArray, -1, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				editEnd.setText(endArray[which]);
			}
		})
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				searchRoute(way);
			}
			})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				}
			})
		.create().show();
	}
	/**
	 * 发起路线规划搜索
	 * 
	 * @param way表示查询的方式
	 */
	public void searchRoute(int way){
		// 发起搜索
		String start=editStart.getText().toString().trim();
		MKPlanNode stNode = new MKPlanNode();
		if(	start != null && !start.equals("当前位置") ){
			if(map.containsKey(start))
				stNode.pt=map.get(start);
			else
				stNode.name=start;
		}else if(start.equals("当前位置")){
			stNode.pt=routeStartPoint;
		}else {
			Toast.makeText(jxauMapAct.this, "请输入起点",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		String end = editEnd.getText().toString().trim();
		MKPlanNode enNode = new MKPlanNode();
		
		if( end != null && !end.equals("地图上的点") ){
			if(map.containsKey(end))
				enNode.pt=map.get(end);
			else
				enNode.name=end;
			
		} else if(end.equals("地图上的点")){
			enNode.pt=routeEndPoint;
		}else{
			Toast.makeText(jxauMapAct.this, "请输入终点或在地图上选点",
					Toast.LENGTH_LONG).show();
			return;
		}
		
		if (way==VEHICLE_DRIVE) {
			mSearch.drivingSearch("南昌", stNode, "南昌", enNode);
		} else if (way==VEHICLE_TRANSIT) {
			mSearch.transitSearch("南昌", stNode, enNode);
		} else{
			mSearch.walkingSearch("南昌", stNode, "南昌", enNode);
		}
	}
	/**
	 * 处理搜索时起点和终点不唯一的情况下，数据的筛选
	 * @param stPois
	 * @param enPois
	 * @param way
	 */
	public void dealWithMKSearchData(ArrayList<MKPoiInfo> stPois,ArrayList<MKPoiInfo> enPois,int way){
			 String[] start=new String[stPois.size()];
			 for(int i=0;i<stPois.size();i++){
				 start[i]=stPois.get(i).name;
			 }
			 String[] end=new String[enPois.size()];
			 for(int i=0;i<enPois.size();i++){
				 end[i]=enPois.get(i).name;
			 }
			 
			 if(!editStart.getText().toString().equals(STATU_START)){
				 showStartDialog(way,start,end);
			 }else if(!editEnd.getText().toString().equals(STATU_END)){
				 showEndDialog(way,end);
			 }else{
				 Toast.makeText(jxauMapAct.this, "无法找到该地址!",
							Toast.LENGTH_LONG).show();
			 }
	}
	/**
	 * 线路规划、检索POI监听器
	 */
	MKSearchListener myMKSearchListener = new MKSearchListener() {

		public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error) {
			if (error == MKEvent.ERROR_ROUTE_ADDR) {
				ArrayList<MKPoiInfo> stPois =res.getAddrResult().mStartPoiList;
				ArrayList<MKPoiInfo> enPois =res.getAddrResult().mEndPoiList;
				dealWithMKSearchData(stPois,enPois,VEHICLE_DRIVE);
				return;
			}
			// 错误号可参考MKEvent中的定义
			if (error != 0 || res == null) {
				Toast.makeText(jxauMapAct.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			routeOverlay = new RouteOverlay(jxauMapAct.this, mMapView);
			routeOverlay.setData(res.getPlan(0).getRoute(0));
			// 清除其他图层
			mMapView.getOverlays().clear();
			getCustomInfoFromAssetsXML();
			getGroundInfoFromAssetsXML();
			// 添加路线图层
			mMapView.getOverlays().add(routeOverlay);
			// 执行刷新使生效
			mMapView.refresh();
			// 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
			mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(),
					routeOverlay.getLonSpanE6());
			// 移动地图到起点
			mMapView.getController().animateTo(res.getStart().pt);
		}

		public void onGetTransitRouteResult(MKTransitRouteResult res, int error) {
			// 起点或终点有歧义，需要选择具体的城市列表或地址列表
			if (error == MKEvent.ERROR_ROUTE_ADDR) {
				ArrayList<MKPoiInfo> stPois =res.getAddrResult().mStartPoiList;
				ArrayList<MKPoiInfo> enPois =res.getAddrResult().mEndPoiList;
				dealWithMKSearchData(stPois,enPois,VEHICLE_TRANSIT);
			}
			if (error != 0 || res == null) {
				Toast.makeText(jxauMapAct.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
						.show();
				return;
			}

			transitOverlay = new TransitOverlay(jxauMapAct.this, mMapView);
			// 此处仅展示一个方案作为示例
			transitOverlay.setData(res.getPlan(0));
			// 清除其他图层
			mMapView.getOverlays().clear();
			getCustomInfoFromAssetsXML();
			getGroundInfoFromAssetsXML();
			// 添加路线图层
			mMapView.getOverlays().add(transitOverlay);
			// 执行刷新使生效
			mMapView.refresh();
			// 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
			mMapView.getController().zoomToSpan(transitOverlay.getLatSpanE6(),
					transitOverlay.getLonSpanE6());
			// 移动地图到起点
			mMapView.getController().animateTo(res.getStart().pt);
			// 重置路线节点索引，节点浏览时使用
		}
		
		public void onGetWalkingRouteResult(MKWalkingRouteResult res, int error) {
			// 起点或终点有歧义，需要选择具体的城市列表或地址列表
			if (error == MKEvent.ERROR_ROUTE_ADDR) {
				// 遍历所有地址
				 ArrayList<MKPoiInfo> stPois =res.getAddrResult().mStartPoiList;
				 ArrayList<MKPoiInfo> enPois =res.getAddrResult().mEndPoiList;
				 dealWithMKSearchData(stPois,enPois,VEHICLE_WALK);
				return;
			}
			if (error != 0 || res == null) {
				Toast.makeText(jxauMapAct.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			routeOverlay = new RouteOverlay(jxauMapAct.this, mMapView);
			// 此处仅展示一个方案作为示例
			routeOverlay.setData(res.getPlan(0).getRoute(0));
			// 清除其他图层
			mMapView.getOverlays().clear();
			getCustomInfoFromAssetsXML();
			getGroundInfoFromAssetsXML();
			// 添加路线图层
			mMapView.getOverlays().add(routeOverlay);
			// 执行刷新使生效
			mMapView.refresh();
			// 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
			mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(),
					routeOverlay.getLonSpanE6());
			// 移动地图到起点
			mMapView.getController().animateTo(res.getStart().pt);
			// 将路线数据保存给全局变量
		}

		public void onGetAddrResult(MKAddrInfo res, int error) {

		}

		public void onGetPoiResult(MKPoiResult res, int arg1, int arg2) {
		}

		public void onGetBusDetailResult(MKBusLineResult result, int iError) {
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
		}

		@Override
		public void onGetPoiDetailSearchResult(int type, int iError) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult result, int type,
				int error) {
			// TODO Auto-generated method stub
		}
	};
	
	/**
	 * 读取xml中的文件，用于模糊匹配用户输入
	 */
	public void InfoFromAssetsXML() {
			try {
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(true);
				XmlPullParser xpp = factory.newPullParser();
				xpp.setInput(getResources().getAssets().open("point_db.xml"), null);
				map=XmlReader.getPointDB(xpp);
			}catch (Exception e) {
				// TODO: handle exception
			}
			
		}
	/**
	 * 读取XML文件中的覆盖点信息，渲染地图
	 */
	public void getCustomInfoFromAssetsXML() {
		ArrayList<CustomItem> items = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(getResources().getAssets().open("pointInfo.xml"), null);
			items = XmlReader.getCustomItemInfo(xpp);
		} catch (Exception e) {
			// TODO: handle exception
		}
		Drawable mark = getResources().getDrawable(R.drawable.baidumap_ico);
		// 创建IteminizedOverlay
		CustomOverlay itemOverlay = new CustomOverlay(mark, mMapView);
		TextOverlay textOverlay = new TextOverlay(mMapView);
		// mMapView.getOverlays().clear();
		mMapView.getOverlays().add(itemOverlay);
		mMapView.getOverlays().add(textOverlay);
		// 用OverlayItem准备Overlay数据
		for (CustomItem c : items) {
			textOverlay.addText(c.getTextItem());
			itemOverlay.addItem(c.getOverlayItem());
		}
	}

	/**
	 * 读取XML中的矩形坐标信息，用图片覆盖
	 */
	public void getGroundInfoFromAssetsXML() {
		ArrayList<CustomGround> grounds = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(getResources().getAssets().open("groundInfo.xml"),
					null);
			grounds = XmlReader.getGroundInfo(xpp);
		} catch (Exception e) {
			// TODO: handle exception
		}
		GroundOverlay mGroundOverlay = new GroundOverlay(mMapView);
		mMapView.getOverlays().add(mGroundOverlay);
		for (CustomGround cg : grounds) {
			Drawable d = getResources().getDrawable(cg.getbitMapId());
			Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
			Ground mGround = new Ground(bitmap, cg.getLBPoint(),
					cg.getRTPoint());
			mGroundOverlay.addGround(mGround);
		}
		mMapView.refresh();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int ItemId = v.getId();// 获取组件的id值
		switch (ItemId) {
		case R.id.map_route_btn:
			if(routeBtn.getText().equals("线路")){
				linear.setVisibility(View.VISIBLE);
				routeBtn.setText("返回");
			}else if(routeBtn.getText().equals("返回")){
				linear.setVisibility(View.GONE);
				routeBtn.setText("线路");
				//清楚线路层
				mMapView.getOverlays().clear();
				getCustomInfoFromAssetsXML();
				getGroundInfoFromAssetsXML();
			}
			break;
		case R.id.auto_loc_button:
			if (locationClient == null) {
				return;
			}
			if (locationClient.isStarted()) {
				mbtnLoc.setText("自动定位");
				locationClient.stop();
			} else {
				mbtnLoc.setText("停止定位");
				locationClient.start();
				locationClient.requestLocation();
			}
			break;
		case R.id.screen_hit_btn:
			if(mBtnHit.getText().equals("屏幕取点")){
				enClickAble=true;
				mMapView.getController().enableClick(true);
				mBtnHit.setText("停止取点");
			}else if(mBtnHit.getText().equals("停止取点")){
				enClickAble=false;
				mMapView.getController().enableClick(false);
				mBtnHit.setText("屏幕取点");
			}
			break;
		case R.id.drive_btn:
			searchRoute(VEHICLE_DRIVE);
			break;
		case R.id.transit_btn:
			searchRoute(VEHICLE_TRANSIT);
			break;
		case R.id.walk_btn:
			searchRoute(VEHICLE_WALK);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		mMapView.destroy();
		if (mBMapManager != null) {
			mBMapManager.destroy();
			mBMapManager = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		if (mBMapManager != null) {
			mBMapManager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		if (mBMapManager != null) {
			mBMapManager.start();
		}
		super.onResume();
	}
}