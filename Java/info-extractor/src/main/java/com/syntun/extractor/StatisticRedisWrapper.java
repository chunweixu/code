package com.syntun.extractor;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.Jedis;

public class StatisticRedisWrapper {
	private static Logger logger = LogManager.getLogger(StatisticExtractor.class.getName());
	
	private Map<String, Long> mapDomainStat = new HashMap<String, Long>();
	private long error = 0;
	private long success = 0;
	private Jedis jedis = null;
	private static long FLUSH_THRESHOLD = 100;
	
	private static String KEY_CRAWLER_ERROR = "CRAWLER_ERROR";
	private static String KEY_CRAWLER_SUCCESS = "CRAWLER_SUCCESS";
	private static String KEY_CRAWLER_RAWPAGE_TOTAL = "CRAWLER_RAWPAGE_TOTAL";
	private static String KEY_PREFIX_CRAWLER_RAWPAGE = "CRAWLER_RAWPAGE_";
	
	
	//incCrawlerError
	//incCrawlerSuccess
	//incCrawlerRawpageByDomain
	//flush
	
	public boolean init(String server,int port,String passwd){
		//System.out.println("Server:" + server +":" + port);
		try{
			jedis = new Jedis(server,port);
			if(passwd != null && (!passwd.isEmpty())){
				jedis.auth(passwd);
			}
		}
		catch(Exception e){
			logger.error("Init redis server error. Server:" + server +":" + port +",Exception:" + e.getMessage());
			return false;
		}
		return true;
	}
	
	public void close(){
		flush();
		jedis.close();
	}
	
	public void incCrawlerError(){
		error++;
		tryFlush();
	}
	
	public void incCrawlerSuccess(){
		success++;
		tryFlush();
	}
	
	public void incCrawlerRawpageByDomain(String domain){
		String lowDomain = KEY_PREFIX_CRAWLER_RAWPAGE + domain.toLowerCase();
		if(mapDomainStat.containsKey(lowDomain)){
			Long count = mapDomainStat.get(lowDomain);
			count++;
			mapDomainStat.put(lowDomain, count);
		}
		else{
			mapDomainStat.put(lowDomain, 1L);
		}
		tryFlush();
	}
	
	public void tryFlush(){
		if(error + success >= FLUSH_THRESHOLD){
			flush();
		}
	}
	
	public void flush(){
		try{
			long total = success + error;
			jedis.incrBy(KEY_CRAWLER_SUCCESS, success);
			success = 0;
			jedis.incrBy(KEY_CRAWLER_ERROR, error);
			error = 0;
			jedis.incrBy(KEY_CRAWLER_RAWPAGE_TOTAL, total);
			for(String key : mapDomainStat.keySet()){
				Long value = mapDomainStat.get(key);
				jedis.incrBy(key,value);
				mapDomainStat.put(key, 0L);
			}

		}
		catch(Exception e){
			logger.error("Can not flush to redis.");
			return;
		}
	}
	
}
