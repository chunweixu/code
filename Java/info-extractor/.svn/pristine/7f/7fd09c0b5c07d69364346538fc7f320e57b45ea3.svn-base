package com.syntun.extractor;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class ExtractorManager {
	private List<Extractor> listExtractor = new ArrayList<Extractor>();
	private static Logger logger = LogManager.getLogger(ExtractorManager.class.getName());
	
	public boolean registerExtractor(Extractor extractor){
		listExtractor.add(extractor);
		return true;
	}
	
	public boolean run(JSONObject ctxt){
		boolean retVal = true;
		String url = JSONHelper.getJsonString(ctxt, "rawpage.url");
		logger.info("============== Start Processing:" + ((url!=null)?url:"null") + " ==============");
		for(Extractor e: listExtractor){
			logger.info("Calling:" + e.getName());
			if(!e.run(ctxt)){
				logger.warn(String.format("Extractor run failed. name:%s",e.getName()));
				retVal = false;
				break;
			}
		}
		logger.info("============== End Processing:" + ((url!=null)?url:"null") + " ==============");
		return retVal;
	}
	
	public boolean destroy(){
		for(Extractor e: listExtractor){
			e.close();
		}
		listExtractor.clear();
		return true;
	}
}
