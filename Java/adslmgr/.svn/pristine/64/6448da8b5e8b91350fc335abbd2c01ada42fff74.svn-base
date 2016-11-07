package com.syntun.crawler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NameCache {
	private static Logger logger = LogManager.getLogger(NameCache.class.getName()); 
	private ConcurrentHashMap<String, NameCacheNode> mapName = new ConcurrentHashMap<String, NameCacheNode>();
	
	InetAddress []getAddress(String host){
		InetAddress[] addrs = null;
		long curTime = System.currentTimeMillis(); //当前时间，毫秒
		
		NameCacheNode node = this.mapName.get(host);
		if(node == null){
			
			try {
				addrs = InetAddress.getAllByName(host);
			} catch (UnknownHostException e) {
				logger.warn("Unknow domain:" + host);
				return null;
			}
			node = new NameCacheNode();
			node.setAddressList(addrs);
			node.setHost(host);
			node.setModTime(curTime);
			mapName.putIfAbsent(host, node);
			return addrs;
		}
		else{
			//to do 过期更新
			return node.getAddressList();
		}
	}
}
