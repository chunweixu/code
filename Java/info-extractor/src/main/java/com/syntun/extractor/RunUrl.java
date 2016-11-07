package com.syntun.extractor;

import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by liwWenBo on 2015/12/29.
 */
public class RunUrl implements Extractor{


   /* public static void main(String[] args) {
        LinkedList<BaseData> baseDataList = new BaseData().initbaseData();
        LinkedList<Result> resultURLs = new LinkedList<Result>();
        String url = "http://www.amazon.cn/gp/aag/details/ref=aag_m_ss?ie=UTF8&asin=B00BQ8RM1A&isAmazonFulfilled=0&isCBA=&marketplaceID=AAHKV2X7AFYLW&seller=51654612ass#aag_legalInfo";
        Result result = new Result();

        for (int i = 0; i < baseDataList.size(); i++) {
            String regex = baseDataList.get(i).getRegx();
            // System.out.println("regex" + regex);
            boolean b = url.matches(regex);
            if (b == true ) {
                result.setToken_id(baseDataList.get(i).getToken_id());
                result.setPageType( baseDataList.get(i).getPageType());
                result.setPlatform( baseDataList.get(i).getPlatform());
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

    private LinkedList<BaseData> baseDataList = null;
    public boolean init(InfoExtractorConfig config) {
        //配置文件位置？？
        baseDataList = new BaseData().initbaseData();
        if (baseDataList.isEmpty()){
            return false;
        }
        return  true;
    }

    public boolean run(JSONObject ctxt) {
        //url在rawpage.url下
        String url = ctxt.getString("url");

        Result result = new Result();

        for (int i = 0; i < baseDataList.size(); i++) {
            //预先编译??
            String regex = baseDataList.get(i).getRegx();
            // System.out.println("regex" + regex);
            // Pattern Matcher
            boolean b = url.matches(regex);
            if (b == true ) {
                //命名规则
                ctxt.put("token_id",baseDataList.get(i).getToken_id());
                ctxt.put("pageType",baseDataList.get(i).getPageType());
                ctxt.put("platform",baseDataList.get(i).getPlatform());
                return true;
            }
        }
        return false;
    }

    public void close() {
        baseDataList.clear();
    }

    public String getName() {
        return null;
    }
}
