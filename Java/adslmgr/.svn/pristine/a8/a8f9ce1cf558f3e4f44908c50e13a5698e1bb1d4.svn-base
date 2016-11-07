package com.syntun.crawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UrlUniq {
	private HashMap<String, Long> uniqMap = new HashMap<String, Long>(33554432);
	private long step=(2*3600*1000); //2小时间隔
	private static Logger logger = LogManager.getLogger(FetcherThread.class.getName());
	
	public  boolean needCrawl(String url){
		Long curTime = System.currentTimeMillis();
		Long lastTime = uniqMap.get(url);
		if(lastTime == null){
			//logger.info("PUT_TIME:" + curTime + "##URL:" + url);
			uniqMap.put(url, curTime);
			return true;
		}
		else{
			if(lastTime + step > curTime){ //还没到时间，不需要抓
				//logger.info("UNIQ_TIME:" + lastTime + "##URL:" + url);
				return false;
			}
			else{
				uniqMap.put(url, curTime);
				return true;
			}
		}
	}
	
	public void removeExpired(){
		Iterator<String> it = uniqMap.keySet().iterator();
		long curTime = System.currentTimeMillis();
		List<String> removeList = new ArrayList<String>();
		while(it.hasNext()){
			String key = it.next();
			long nodeTime = uniqMap.get(key);
			if(nodeTime + step < curTime){
				//需要删除
				
				removeList.add(key);
			}
		}
		
		for(String key : removeList){
			uniqMap.remove(key);
		}
	}
}
