package com.syntun.crawler;

import java.io.IOException;
import java.net.CookieStore;


import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FetcherResponseHandler implements ResponseHandler<FetcherResponse> {
	private static Logger logger = LogManager.getLogger(FetcherThread.class.getName());
	private String knownCharset = null;
	
	public void setKnownCharset(String knownCharset) {
		this.knownCharset = knownCharset;
	}

	public FetcherResponse handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		FetcherResponse ret = new FetcherResponse();
		
		ret.setHeaders(response.getAllHeaders());
		ret.setCode(response.getStatusLine().getStatusCode());
		ContentType type = ContentType.getOrDefault(response.getEntity());
//		String strCharset = null;
//		if(type.getCharset() == null){
//			strCharset = knownCharset;
//		}
//		else{
//			strCharset = type.getCharset().name();
//		}
//		if(strCharset == null){
//			strCharset = "utf-8";
//		}
		byte[] byteContent = EntityUtils.toByteArray(response.getEntity());
		String strCharset = "ISO-8859-1";
		if(type.getCharset()!=null){//优先使用http头中的
			strCharset = type.getCharset().name();
		}
		else{
			String strHead = new String(byteContent,0,byteContent.length>1024?1024:byteContent.length,"ISO-8859-1");
			strHead = strHead.toLowerCase();
			if(strHead.indexOf("charset=\"gbk\"") != -1){
				strCharset = "GBK";
			}
			else if(strHead.indexOf("charset=\"utf") != -1){
				strCharset = "UTF-8";
			}
		}

		String body = new String(byteContent,strCharset);

		ret.setBody(body);
		//判断是否跳转，跳转保存location
		if(302 == response.getStatusLine().getStatusCode()
				|| 301 == response.getStatusLine().getStatusCode()){
			Header hdrLocation = response.getLastHeader("Location");
			if(hdrLocation == null){
				ret.setLocation(null);
			}
			ret.setLocation(hdrLocation.getValue());
		}
		return ret;
	}

}
