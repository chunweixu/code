package com.syntun.crawler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RedialManager {
	private Map<String,Long> mapRedial = new HashMap<String,Long>();
	private long REDIAL_STEP_TIME = 120000; //120秒间隔
	private static Logger logger = LogManager.getLogger(RedialManager.class.getName());
	
	void redial(String ifName){
		boolean needRedial = false;
		long curTime = System.currentTimeMillis();
		synchronized(mapRedial){
			if(!mapRedial.containsKey(ifName)){
				mapRedial.put(ifName, curTime);
				needRedial = true;
			}
			else{
				if((curTime - mapRedial.get(ifName)) > REDIAL_STEP_TIME){
					needRedial = true;
					mapRedial.put(ifName, curTime);
				}
			}
		}
		
		if(needRedial && CrawlerConfig.CHECKER_REDIAL_PATH != null){
			logger.info("Interface redialed:" + ifName);
			File f = new File(CrawlerConfig.CHECKER_REDIAL_PATH + "/" + ifName);
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
