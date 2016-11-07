package com.syntun.extractor;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.LinkedList;

/**
 * Created by liWenBo on 2015/12/29.
 */

public class BaseData {
    private  String  pageType;
    private  String  platform;
    private  String  regx;
    private String token_id;
    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setRegx(String regx) {
        this.regx = regx;
    }

    public String getPageType() {
        return pageType;
    }

    public String getPlatform() {
        return platform;
    }

    public String getRegx() {
        return regx;
    }

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    //初始化正则
    public  LinkedList<BaseData> initbaseData(){
        LinkedList baseDataList = new LinkedList<BaseData>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder db = dbf.newDocumentBuilder();
            //配置文件位置
            Document doc = db.parse("config/config.xml");

            NodeList urlList = doc.getElementsByTagName("data");

            for (int i = 0; i < urlList.getLength(); i++)
            {
                BaseData baseData = new BaseData();
                Node bbsData = urlList.item(i);
                NodeList nodeDetail = bbsData.getChildNodes();
                for (int j = 0; j < nodeDetail.getLength(); j++) {
                    Node detail = nodeDetail.item(j);
                    if ("token_id".equals(detail.getNodeName())){
                        //System.out.println("token_id: " + detail.getTextContent());
                        baseData.setToken_id(detail.getTextContent());
                    }
                    if ("platform".equals(detail.getNodeName())){
                        baseData.setPlatform(detail.getTextContent());
                       // System.out.println("platform:" + detail.getTextContent());
                    }
                    if ("pageType".equals(detail.getNodeName())){
                        baseData.setPageType(detail.getTextContent());
                        //System.out.println("pageType: " + detail.getTextContent());
                    }
                    if ("regx".equals(detail.getNodeName())){
                        baseData.setRegx(detail.getTextContent());
                       // System.out.println(detail.getTextContent());
                    }

                }
                baseDataList.add(baseData);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  baseDataList;
    }


    public static void main(String[] args) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse("conf/config.xml");

            NodeList urlList = doc.getElementsByTagName("data");
            System.out.println("共有" + urlList.getLength() + "个data节点");
            for (int i = 0; i < urlList.getLength(); i++)
            {
                Node bbsData = urlList.item(i);
                NodeList nodeDetail = bbsData.getChildNodes();
                for (int j = 0; j < nodeDetail.getLength(); j++) {
                    Node detail = nodeDetail.item(j);
                    if ("token_id".equals(detail.getNodeName())){
                        //System.out.println("token_id: " + detail.getTextContent());
                    }
                    if ("platform".equals(detail.getNodeName())){
                        System.out.println("platform:" + detail.getTextContent());
                    }
                    if ("pageType".equals(detail.getNodeName())){
                        System.out.println("pageType: " + detail.getTextContent());
                    }
                    if ("regx".equals(detail.getNodeName())){
                        System.out.println(detail.getTextContent());
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
