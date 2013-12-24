package com.zero.goOut;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class getDataAsyncTask extends AsyncTask<Void, Void, List<BusInfo>> {
	private String line = "";
	private int dire = 0;
	private String location="";
	Handler handler=null;

	public getDataAsyncTask(String location, String line,int dire, Handler handler) {
		
		this.location=location;
		this.line = line;
		this.dire=dire;
		this.handler = handler;
	}

	@Override
	protected List<BusInfo> doInBackground(Void... params) {
		// deal data in here
		List<BusInfo> list = new ArrayList<BusInfo>();
		
		String url3 = "http://mybus.jx139.com/LineDetailQuery?lineId="+
				line+"&direction="+dire+"&";
		String info3 = HtmlDeal.GetContentFromUrl(url3);
		System.out.println("info3:"+info3);
		if(info3!=""){ 
			String mainInfo = HtmlDeal.getDivContentByJsoup(info3);
			if(mainInfo!="")
			{
				List<BusInfo> list3 = getBusInfo(mainInfo);
				list.addAll(list3);
			}
		}
		
		String url = "http://mybus.jx139.com/LineDetailQuery?lineId=1"+
				line+"&direction="+dire+"&";
		String info = HtmlDeal.GetContentFromUrl(url);
		if(info!=""){ 
			String mainInfo = HtmlDeal.getDivContentByJsoup(info);
			if(mainInfo!="")
			{
				List<BusInfo> list3 = getBusInfo(mainInfo);
				list.addAll(list3);
			}
		}
		
		String url2 = "http://mybus.jx139.com/LineDetailQuery?lineId=2"+
				line+"&direction="+dire+"&";
		String info2 = HtmlDeal.GetContentFromUrl(url2);
		if(info2!=""){ 
			String mainInfo = HtmlDeal.getDivContentByJsoup(info2);
			if(mainInfo!="")
			{
				List<BusInfo> list3 = getBusInfo(mainInfo);
				list.addAll(list3);
			}
		}
		BusInfo first = new BusInfo(null, 0, false);
		if(dire==1)
		{
			first.dire = true;
		}else first.dire = false;
		
		list.add(0, first);
		return list;
	}
	
	protected List<BusInfo> getBusInfo(String mainInfo){		
				
		List<BusInfo> list = HtmlDeal.SolveCase(mainInfo, location, dire);
		return list;
	}
	@Override
	protected void onPostExecute(List<BusInfo> result) {
		super.onPostExecute(result);
		// deal message
		Message msg = new Message();
		msg.obj = result;
		handler.sendMessage(msg);
	}
}
