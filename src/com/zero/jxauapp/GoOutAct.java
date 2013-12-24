/**
 * 
 */
package com.zero.jxauapp;

import java.util.List;

import com.zero.goOut.BusInfo;
import com.zero.goOut.GoOutInfo;
import com.zero.goOut.getDataAsyncTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
/**
 * Title: GoOutAct Description:出行页面
 * 
 * @date 2013-12-19
 */
public class GoOutAct extends Activity {
	
	private TextView goOutTextView;
	// Update data in main thread
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			List<BusInfo> list = (List<BusInfo>) msg.obj;
			BusInfo in = list.get(0);
			if (in.getDirection())
				goOutTextView.append("正方向" + '\n');
			else
				goOutTextView.append("反方向" + '\n');
			for (int i = 1; i < list.size(); i++) {
				BusInfo info = list.get(i);
				goOutTextView.append(info.number + info.station + info.flag
						+ '\n');
			}
		};
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.go_out_layout);
		goOutTextView = (TextView) findViewById(R.id.go_out_textView);
		dealWithDataFromPopWindow();
	}

	public void dealWithDataFromPopWindow() {
		GoOutInfo info = (GoOutInfo) getIntent().getSerializableExtra(
				"GoOutInfo");
		String location;

		if (info.getCurrentLocation().endsWith("农大")) {
			location = "农大生活区站";
		} else if (info.getCurrentLocation().endsWith("下罗")) {
			location = "下罗站";
		} else {
			location = "财大站";
		}

		// new getDataAsyncTask(location,line,dire, handler).execute();
		// 1表示正向 2表示反方向

		if (info.getDirection()) {
			if (info.getLine().equals("240 704")) {
				new getDataAsyncTask(location, "240", 1, handler).execute();
				new getDataAsyncTask(location, "704", 1, handler).execute();
			} else {
				new getDataAsyncTask(location, info.getLine(), 1, handler)
						.execute();
			}
		} else {
			if (info.getLine().equals("240 704")) {
				new getDataAsyncTask(location, "240", 0, handler).execute();
				new getDataAsyncTask(location, "704", 0, handler).execute();
			} else {
				new getDataAsyncTask(location, info.getLine(), 0, handler)
						.execute();
			}
		}
	}
}
