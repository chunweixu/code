package com.syntun.extractor;

/**
 * Created by LiWenBo on 2016/1/4.
 */
public class PlatNew {
    //public  String url;
    public  String pageType; //返回URL的类型
    public  String platform;//平台
    private String tokenid;//结点
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

    public String getTokenId() {
        return tokenid;
    }

    public void setTokenId(String tokenid) {
        this.tokenid = tokenid;
    }
}
