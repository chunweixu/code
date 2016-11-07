package com.syntun.extractor;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PageInfoManager {
	private static Logger logger = LogManager.getLogger(PageInfoManager.class.getName());
	private Map<String, PageInfoTemplate> map = new HashMap<String, PageInfoTemplate>();
    
    private Map<String, PageInfoTemplate> getMap() {
		return map;
    }
    
    public List<Map<String, String>> getResult(String content, String baseUri) {
    	List<Map<String, String>> result;
		String domain;
		try {
			domain = new URI(baseUri).getHost().toLowerCase();
			if (getMap().containsKey(domain)) {
				result = getMap().get(domain).getPageInfo(content, baseUri);
				return result;
			} else {
				logger.warn("Match template failure");
				return null;
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
    
	public boolean load(String tempDir) {
		File dir;
		try {
			dir = new File(tempDir);
		    for (File file : dir.listFiles()) {
		    	if(file.isDirectory()){
		    		logger.info("Skipping template directory:" + file.getName());
		    		continue;
		    	}
		    	if(!file.getName().endsWith(".xml")){
		    		logger.info("Skipping non-xml file:" + file.getName());
		    		continue;
		    	}
			    PageInfoTemplate template = new PageInfoTemplate();
			    if (template.initTemplate(file.getAbsolutePath())) {
			    	String[] domains = template.getDomains();
			    	for (int i = 0; i < domains.length; i++) {
			    		if (!map.containsKey(domains[i].toLowerCase())) {
			    		    map.put(domains[i].toLowerCase(), template);
			    		    logger.info("PageInfoExtractor template loaded: " + file.getAbsolutePath());
			    		} else {
			    			logger.error("Duplicate domains");
			    			return false;
			    		}
			    	}
			    } else {
			    	logger.error("Initialize template failure");
			    	return false;
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
    }
	
}
