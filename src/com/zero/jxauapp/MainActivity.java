package com.zero.jxauapp;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zero.goOut.GoOutInfo;

 /**   
 * Title: MainActivity
 * Description:主页面
 * @author DaiS
 * @version 1.0
 * @date 2013-12-20
 */   
public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener {
	// 侧边栏按钮
	private ImageButton sideMenuExtendBtn;
	// 地图ImageButton
	private ImageButton mapImageBtn;
	// 出行ImageButton
	private ImageButton gooutImageBtn;
	private Fragment mContent;
	// 出行服务，弹出菜单
	private PopupWindow mPopupWindow;
	// popupWindows对话框中的控件
	//农大，单选按钮
	private RadioButton nongDaRadioBtn;
	//财大，单选按钮
	private RadioButton caiDaRadioBtn;
	//下罗，单选按钮
	private RadioButton xiaLuoRadioBtn;
	//240复选框
	private CheckBox line240CheckBox;
	//704复选框
	private CheckBox line704CheckBox;
	//到达按钮
	private Button arrivelBtn;
	//出发按钮
	private Button setOffBtn;
	//出行服务的视图，用来找到组件
	private View popupView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置标题栏的标题
		setTitle("农大App");
		// 设置是否能够使用ActionBar来滑动
		setSlidingActionBarEnabled(true);
		// 设置是否显示Home图标按钮
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		// 设置主界面视图
		setContentView(R.layout.activity_main);
		// 初始化滑动菜单
		initSlidingMenu(savedInstanceState);
		// 为按钮设置监听器，点击触发侧边菜单。
		initBindListenerWidge();

		popupView = getLayoutInflater().inflate(R.layout.pop_menu, null);
		// popupView.setBackgroundColor(Color.BLUE);
		mPopupWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		// 这三行的作用是点击空白处的时候PopupWindow会消失
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),
				(Bitmap) null));
	}
	/**
	 * 初始化滑动菜单
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		// 设置滑动菜单的视图
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new SideManuFragment()).commit();
		// 实例化滑动菜单对象
		SlidingMenu sm = getSlidingMenu();
		// 设置滑动阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动阴影的图像资源
		sm.setShadowDrawable(R.drawable.slid_menu_shadow);
		// 设置滑动菜单视图的宽度
		sm.setBehindOffsetRes(R.dimen.slid_menu_offset);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}
	
	/**
	 * 为主界面中的组件添加监听
	 */
	public void initBindListenerWidge() {
		sideMenuExtendBtn = (ImageButton) findViewById(R.id.side_menu_extend_btn);
		mapImageBtn = (ImageButton) findViewById(R.id.map_imageBtn);
		gooutImageBtn = (ImageButton) findViewById(R.id.goout_imageBtn);

		mapImageBtn.setOnClickListener(this);
		sideMenuExtendBtn.setOnClickListener(this);
		gooutImageBtn.setOnClickListener(this);
	}
	
	/**
	 * 出行服务弹出菜单
	 */
	public void runPopWindow() {
		nongDaRadioBtn = (RadioButton) popupView
				.findViewById(R.id.current_location_radioBtn1);
		caiDaRadioBtn = (RadioButton) popupView
				.findViewById(R.id.current_location_radioBtn2);
		xiaLuoRadioBtn = (RadioButton) popupView
				.findViewById(R.id.current_location_radioBtn3);
		line240CheckBox = (CheckBox) popupView
				.findViewById(R.id.line240_checkBox);
		line704CheckBox = (CheckBox) popupView
				.findViewById(R.id.line704_checkBox);
		arrivelBtn = (Button) popupView.findViewById(R.id.arrive_btn);
		setOffBtn = (Button) popupView.findViewById(R.id.set_off_btn);
		
		arrivelBtn.setOnClickListener(new PopMenuBtnListener());
		setOffBtn.setOnClickListener(new PopMenuBtnListener());
	}
	
	 /**   
	 * Description:出行服务，弹出菜单中的按钮监听器
	 */   
	class PopMenuBtnListener implements OnClickListener{
		//线路
		String line;
		//当前位置
		String currentLocation;
		//方向
		boolean dir;
		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int ItemId = v.getId();
			
			if (line240CheckBox.isChecked() && line704CheckBox.isChecked()) {
				line="240 704";
			}else if (line704CheckBox.isChecked()) {
				line="704";
			}else{
				line="240";
			}
			
			if (caiDaRadioBtn.isChecked()) {
				currentLocation="财大";
			} else if (xiaLuoRadioBtn.isChecked()) {
				currentLocation="下罗";
			}else{
				currentLocation="农大";
			}
			
			if(ItemId==R.id.set_off_btn){
				dir=true;
			}else if(ItemId==R.id.arrive_btn){
				dir=false;
			}
			
			GoOutInfo info=new GoOutInfo(currentLocation,line,dir);
			Intent intent = new Intent();  
			Bundle bundle = new Bundle();  
			bundle.putSerializable("GoOutInfo", info);  
			intent.putExtras(bundle);  
			intent.setClass(MainActivity.this, GoOutAct.class);
			startActivity(intent);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int ItemId = v.getId();// 获取组件的id值
		switch (ItemId) {
		case R.id.side_menu_extend_btn:
			toggle();
			break;
		case R.id.map_imageBtn:
			startActivity(new Intent().setClass(MainActivity.this,
					jxauMapAct.class));
			break;
		case R.id.goout_imageBtn:
			// 弹出PopWindows
			 mPopupWindow.showAsDropDown(v);
			 runPopWindow();
			break;
		default:
			break;
		}
	}

	/**
	 * 切换Fragment，也是切换视图的内容。
	 * 可以将Activity 优化为Fragment...
	 */
	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
	}

	/**
	 * 菜单按钮点击事件，通过点击ActionBar的Home图标按钮来打开滑动菜单
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// 点击返回键关闭滑动菜单
		super.onBackPressed();
	}
}
