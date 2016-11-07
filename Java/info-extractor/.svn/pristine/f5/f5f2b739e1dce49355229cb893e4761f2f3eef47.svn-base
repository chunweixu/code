package com.syntun.extractor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;




/**
 * Created by liwWenBo on 2015/12/29.
 */
public class PageTypeExtractor implements Extractor{
    private static Logger logger = LogManager.getLogger(PageTypeExtractor.class.getName());
    private static String extractorName = "PageInfoExtractor";

   /* public static void main(String[] args) {
        LinkedList<PageBaseData> pageBaseDataList = new PageBaseData().initbaseData();
        LinkedList<Result> resultURLs = new LinkedList<Result>();
        String url = "http://www.amazon.cn/gp/aag/details/ref=aag_m_ss?ie=UTF8&asin=B00BQ8RM1A&isAmazonFulfilled=0&isCBA=&marketplaceID=AAHKV2X7AFYLW&seller=51654612ass#aag_legalInfo";
        Result result = new Result();

        for (int i = 0; i < pageBaseDataList.size(); i++) {
            String regex = pageBaseDataList.get(i).getRegx();
            // System.out.println("regex" + regex);
            boolean b = url.matches(regex);
            if (b == true ) {
                result.setToken_id(pageBaseDataList.get(i).getToken_id());
                result.setPageType( pageBaseDataList.get(i).getPageType());
                result.setPlatform( pageBaseDataList.get(i).getPlatform());
                resultURLs.add(result);
            }

        }
        //System.out.println(resultURLs.size());

       for(int i = 0;i<resultURLs.size();i++){
           System.out.println(resultURLs.get(i).getToken_id());
           System.out.println(resultURLs.get(i).getPageType());
           System.out.println(resultURLs.get(i).getPlatform());
       }*/


//    }
//初始化正则
	public ArrayList<PageBaseData> initbaseData(String configPath){
        ArrayList<PageBaseData> baseDataList = new ArrayList<PageBaseData>();
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    try
	    {
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        //配置文件位置
	        Document doc = db.parse(configPath);
	        NodeList urlList = doc.getElementsByTagName("data");
	        for (int i = 0; i < urlList.getLength(); i++)
	        {
	            PageBaseData pageBaseData = new PageBaseData();
	            Node pageData = urlList.item(i);
	            NodeList nodeDetail = pageData.getChildNodes();
	            for (int j = 0; j < nodeDetail.getLength(); j++) {
	                Node detail = nodeDetail.item(j);
	                /*if ("tokenid".equals(detail.getNodeName())){
	                    //System.out.println("token_id: " + detail.getTextContent());
	                    //pageBaseData.setTokenId(detail.getTextContent());
	                    continue;
	                }*/
	                if ("platform".equals(detail.getNodeName())){
	                    pageBaseData.setPlatform(detail.getTextContent());
	                    // System.out.println("platform:" + detail.getTextContent());
	                    continue;
	                }
	                if ("pageType".equals(detail.getNodeName())){
	                    pageBaseData.setPageType(detail.getTextContent());
	                    //System.out.println("pageType: " + detail.getTextContent());
	                    continue;
	                }
	                if ("regx".equals(detail.getNodeName())){
	                    pageBaseData.setPattern(detail.getTextContent());
	                    continue;
	                    // System.out.println(detail.getTextContent());
	                }
	            }
	            if(!pageBaseData.verification()){
	            	return null;
	            }
	            baseDataList.add(pageBaseData);
	        }
	    }catch (Exception e){
	        logger.error("Error on initbaseData() Msg:"+ e.getMessage());
	        return null;
	    }
	    return  baseDataList;
	}
    private ArrayList<PageBaseData> pageBaseDataList = null;
    public boolean init(InfoExtractorConfig config) {
        HashMap<String,String> configMap= config.getExtractorConfig("PageType");
        if(configMap == null){
            logger.error("Can not find PageType config.");
            return false;
        }
        //System.out.println(configMap.toString());
        String rulefilePath = configMap.get("rulefile");
        if (null == rulefilePath || rulefilePath.isEmpty()){
            logger.error("Can not find rulefile config.");
            return false;
        }
        pageBaseDataList = initbaseData(rulefilePath);
        if (null == pageBaseDataList || pageBaseDataList.isEmpty()){
            return false;
        }
        return  true;
    }
    public boolean run(JSONObject ctxt) {
        boolean flag=false;
        //url在rawpage.url下
        JSONObject rawpage = ctxt.getJSONObject("rawpage");
        if (rawpage == null) {
            logger.error("not find rawpage");
            return false;
        }
        String url = rawpage.getString("url");
        if (url == null){
            logger.error("not find url");
            return false;
        }
        for (int i = 0; i < pageBaseDataList.size(); i++) {
            Matcher m = pageBaseDataList.get(i).getPattern().matcher(url);
            if (m.find()) {
                JSONObject pageTypeObj = new JSONObject();
                //pageTypeObj.put("tokenid", pageBaseDataList.get(i).getTokenId());
                pageTypeObj.put("pageType", pageBaseDataList.get(i).getPageType());
                pageTypeObj.put("platform", pageBaseDataList.get(i).getPlatform());
                ctxt.put("pageType",pageTypeObj);
                logger.info(String.format("PageTypeResult:%s,%s,%s", url,pageBaseDataList.get(i).getPageType(),pageBaseDataList.get(i).getPlatform()));
                return true;
            }

        }
        logger.error(url+"没有找到页面匹配信息");
        return false;

    }

    public void close() {
        pageBaseDataList.clear();
    }

    public String getName() {
        return extractorName;
    }
}
