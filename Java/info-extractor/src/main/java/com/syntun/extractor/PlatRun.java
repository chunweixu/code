package com.syntun.extractor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;


//PageTypeExtracto

/**
 * Created by liwWenBo on 2015/12/29.
 */
public class PlatRun implements Extractor{
    private static Logger logger = LogManager.getLogger(PlatRun.class.getName());

   /* public static void main(String[] args) {
        LinkedList<PlatBaseData> platBaseDataList = new PlatBaseData().initbaseData();
        LinkedList<Result> resultURLs = new LinkedList<Result>();
        String url = "http://www.amazon.cn/gp/aag/details/ref=aag_m_ss?ie=UTF8&asin=B00BQ8RM1A&isAmazonFulfilled=0&isCBA=&marketplaceID=AAHKV2X7AFYLW&seller=51654612ass#aag_legalInfo";
        Result result = new Result();

        for (int i = 0; i < platBaseDataList.size(); i++) {
            String regex = platBaseDataList.get(i).getRegx();
            // System.out.println("regex" + regex);
            boolean b = url.matches(regex);
            if (b == true ) {
                result.setToken_id(platBaseDataList.get(i).getToken_id());
                result.setPageType( platBaseDataList.get(i).getPageType());
                result.setPlatform( platBaseDataList.get(i).getPlatform());
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
    private LinkedList<PlatBaseData> platBaseDataList = null;
    public boolean init(InfoExtractorConfig config) {
        HashMap<String,String> configMap= config.getExtractorConfig("PageType");
        if(configMap == null){
            logger.error("Can not find PageType config.");
            return false;
        }
        //System.out.println(configMap.toString());
        platBaseDataList = new PlatBaseData().initbaseData(configMap.get("rulefile"));
        if (platBaseDataList.isEmpty()){
            return false;
        }
        return  true;
    }
    public boolean run(JSONObject ctxt) {
        boolean flag=false;
        //url在rawpage.url下
        JSONObject rawpage = ctxt.getJSONObject("rawpage");
        if (rawpage == null) {
            logger.warn("not find rawpage");
            return false;
        }
            String url = rawpage.getString("url");
            for (int i = 0; i < platBaseDataList.size(); i++) {
                Matcher m = platBaseDataList.get(i).getPattern().matcher(url);
                if (m.find()) {
                    //位置
                    JSONObject pageTypeObj = new JSONObject();
                    pageTypeObj.put("tokenid", platBaseDataList.get(i).getTokenId());
                    ctxt.put("pageType",pageTypeObj);
                    rawpage.put("tokenid", platBaseDataList.get(i).getTokenId());
                    rawpage.put("pageType", platBaseDataList.get(i).getPageType());
                    rawpage.put("platform", platBaseDataList.get(i).getPlatform());
                    flag=true;
                }
                // String regex = platBaseDataList.get(i).getRegx();
                // System.out.println("regex" + regex);
                // Pattern Matcher
                //boolean b = url.matches(regex);
                //Matcher isNum = pattern.matcher(str);
            }
        if(!flag){
            logger.warn(url+"没有找到页面匹配信息");
        }
        return false;

    }

    public void close() {
        platBaseDataList.clear();
    }

    public String getName() {
        return null;
    }
}
