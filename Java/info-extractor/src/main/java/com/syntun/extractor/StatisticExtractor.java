package com.syntun.extractor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class StatisticExtractor implements Extractor {
	private static Logger logger = LogManager.getLogger(StatisticExtractor.class.getName());
	
	private final String extractorName = "StatisticExtractor";
	private StatisticRedisWrapper redisWrapper = new StatisticRedisWrapper();

	public boolean init(InfoExtractorConfig config) {
		HashMap<String,String> mapConfig = config.getExtractorConfig("Statistic");
		String redisServer = mapConfig.get("redis_server");
		String redisPort = mapConfig.get("redis_port");
		String redisPasswd = mapConfig.get("redis_passwd");
		if(redisServer == null || redisPort == null || redisServer.isEmpty() || redisPort.isEmpty()){
			logger.error("Redis config for StatisticExtractor error.");
			return false;
		}
		if(!redisWrapper.init(redisServer, Integer.parseInt(redisPort), redisPasswd)){
			logger.error("StatisticRedisWrapper init error.");
			return false;
		}
		return true;
	}

	public boolean run(JSONObject ctxt) {
		//logger.info("JSON:" + ctxt.toString());
		String strUrl = JSONHelper.getJsonString(ctxt, "rawpage.url");
		if(strUrl == null){
			redisWrapper.incCrawlerError();
			logger.error("No url in rawpage.");
			return false;
		}
		Boolean success = JSONHelper.getJsonBoolean(ctxt, "rawpage.success");
		if(success == null || (!success) ){
			redisWrapper.incCrawlerError();
			logger.error("Fetching is failed. url:" + strUrl);
			return false;
		}
		redisWrapper.incCrawlerSuccess();
		
		try {
			URL url = new URL(strUrl);
			redisWrapper.incCrawlerRawpageByDomain(url.getHost());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error("Invalid url:" + strUrl);
			return false;
		}
		return true;
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

	public String getName() {
		// TODO Auto-generated method stub
		return extractorName;
	}

}
