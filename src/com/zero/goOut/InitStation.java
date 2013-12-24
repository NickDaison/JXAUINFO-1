package com.zero.goOut;

import java.util.ArrayList;
import java.util.List;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

class InitStation extends AsyncTask<Void, Void, List<String>> {
	private String station;
	private Handler handler;

	InitStation(String station, Handler handler) {
		this.station = station;
		this.handler = handler;
	}

	@Override
	protected List<String> doInBackground(Void... params) {
		final String url = "http://mybus.jx139.com/StationLineQuery?station="
				+ station + "&";
		String info = HtmlDeal.GetContentFromUrl(url);
		String mainInfo = HtmlDeal.getDivContentByJsoup_station(info);
		List<String> list = SolveCase(mainInfo);
		if (list.isEmpty())
			return null;
		// 移除相同的字串
		removeDuplicate(list);
		return list;
	}

	protected  List<String> removeDuplicate(List<String> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).equals(list.get(i))) {
					list.remove(j);
				}
			}
		}
		return list;
	}

	protected List<String> SolveCase(String info) {
		if (info == null)
			return null;
		List<String> list = new ArrayList<String>();
		char data[] = info.toCharArray();
		String line = "";
		for (int i = 0; i < data.length; i++) {
			if (data[i] == '.') {
				int j = i + 1;

				// 得到线路
				while (!HtmlDeal.isChinese(data[j])) {
					if (data[j] >= '0' && data[j] <= '9')
						line += data[j];
					j++;
				}
				if (line != null)
					list.add(line);

			}
			line = "";
		}
		return list;
	}

	protected void onPostExecute(List<String> result) {
		super.onPostExecute(result);
		// deal message
		Message msg = new Message();
		msg.obj = result;
		handler.sendMessage(msg);
	}
}
