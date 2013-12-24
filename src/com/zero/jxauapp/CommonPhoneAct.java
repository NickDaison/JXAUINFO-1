package com.zero.jxauapp;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

 /**   
 * Title: CommonPhoneAct
 * Description:常用电话页面示例，待改进！
 * @author DaiS
 * @version 1.0
 * @date 2013-12-15
 */   
public class CommonPhoneAct extends FragmentActivity{
	
	private ListView listView;
    //private List<String> data = new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
         
        listView = new ListView(this);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData()));
        setContentView(listView);
    }
     
    private List<String> getData(){
        List<String> data = new ArrayList<String>();
        data.add("校园110 : 110");
        data.add("北区食堂   : 189");
        data.add("教务处  : 122");
        data.add("申通快递 : 121");
        return data;
    }
}
