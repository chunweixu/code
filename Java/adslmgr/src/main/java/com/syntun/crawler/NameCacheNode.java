package com.syntun.crawler;

import java.net.InetAddress;

public class NameCacheNode {
	private String host = null;
	private InetAddress[] AddressList = null;
	private long modTime;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public InetAddress[] getAddressList() {
		return AddressList;
	}
	public void setAddressList(InetAddress[] addressList) {
		AddressList = addressList;
	}
	public long getModTime() {
		return modTime;
	}
	public void setModTime(long modTime) {
		this.modTime = modTime;
	}
	
	
}
