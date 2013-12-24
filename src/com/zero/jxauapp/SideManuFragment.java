package com.zero.jxauapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

 /**   
 * Title: SideManuFragment
 * Description:侧滑菜单
 * @author DaiS
 * @version 1.0
 * @date 2013-12-20
 */   
public class SideManuFragment extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		String[] sideManuArray = getResources().getStringArray(R.array.side_menu_item_array);
		
		ArrayAdapter<String> SideManuAdapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_list_item_1, android.R.id.text1, sideManuArray);
		
		setListAdapter(SideManuAdapter);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		switch (position) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			getActivity().startActivity(new Intent().setClass(getActivity(), jxauMapAct.class));
			break;
		case 4:
			newContent=new Fragment();
			Intent intent = new Intent();  
			intent.setClass(getActivity(), CommonPhoneAct.class); 
			getActivity().startActivity(intent);
			break;
		}
		if (newContent != null)
			switchFragment(newContent);
	}
	
//	public void switchContent(Fragment from, Fragment to) {
//        if (mContent != to) {
//            mContent = to;
//            FragmentTransaction transaction = mFragmentMan.beginTransaction().setCustomAnimations(
//                    android.R.anim.fade_in, R.anim.slide_out);
//            if (!to.isAdded()) {    //先判断是否被add过
//                transaction.hide(from).add(R.id.content_frame, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
//            } else {
//                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
//            }
//        }
//    }

	// 切换Fragment视图内ring
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			//fca.switchContent(fragment);
		} 
	}
}