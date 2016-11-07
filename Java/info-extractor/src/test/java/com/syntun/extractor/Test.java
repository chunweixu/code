package com.syntun.extractor;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/1/4.
 */
public class Test {
    public static void main(String[] args) {
        Extractor url = new RunUrl();
        a(url,new JSONObject());
//        url.init(new InfoExtractorConfig());
//        JSONObject ctxt=new JSONObject();
//        ctxt.put("url","http://www.amazon.cn/gp/aag/details/ref=aag_m_ss?ie=UTF8&asin=B00BQ8RM1A&isAmazonFulfilled=0&isCBA=&marketplaceID=AAHKV2X7AFYLW&seller=51654612ass#aag_legalInfo");
//        System.out.println(url.run(ctxt));
//        System.out.println(ctxt.toString());
    }
    public static void a(Extractor e,JSONObject ctxt){
        e.init(new InfoExtractorConfig());
        e.run(ctxt);
    }
}
