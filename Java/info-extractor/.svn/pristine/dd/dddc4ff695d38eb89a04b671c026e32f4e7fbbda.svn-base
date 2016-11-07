package com.syntun.extractor;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

/**
 * Created by liWenBo on 2015/12/29.
 */

public class PageBaseData {

    private  String  pageType;
    private  String  platform;
//    private  String  regx;
    private Pattern pattern;
    //private String tokenid;

    private static Logger logger = LogManager.getLogger(PageTypeExtractor.class.getName());
    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }



    public String getPageType() {
        return pageType;
    }

    public String getPlatform() {
        return platform;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public boolean setPattern(String regx) {
        if (regx == null || regx.isEmpty()){
            return false;
        }
        this.pattern = Pattern.compile(regx);
        return true;
    }

    /*public String getTokenId() {

        return tokenid;
    }

    public void setTokenId(String tokenid) {
        this.tokenid = tokenid;
    }*/

    /**
     * 验证
     */
    public boolean verification(){
        if ((platform==null || platform.isEmpty()) || (pageType==null || pageType.isEmpty()) || pattern==null){
            logger.error("Page Config Field Lose Error:"+(getPlatform() == null ? null : getPlatform())+","+(getPageType() == null ? null : getPageType())+","+(getPattern() == null ? null : getPattern()));
            return false;
        }
        return true;
    }
}
