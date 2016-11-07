package com.syntun.crawler;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.apache.http.conn.DnsResolver;

public class EmptyDnsResolver implements DnsResolver {
	public InetAddress[] resolve(String host) throws UnknownHostException {
		InetAddress[] addrList = new InetAddress[1];
		InetAddress addr = InetAddress.getByName("0.0.0.0");
		addrList[0] = addr;
		return addrList;
	}
}
