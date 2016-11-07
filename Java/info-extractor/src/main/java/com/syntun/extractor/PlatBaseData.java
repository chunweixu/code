package com.syntun.extractor;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.LinkedList;
import java.util.regex.Pattern;

/**
 * Created by liWenBo on 2015/12/29.
 */

public class PlatBaseData {
    private  String  pageType;
    private  String  platform;
//    private  String  regx;
    private Pattern pattern;
    private String tokenid;
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

    public void setPattern(String regx) {
        this.pattern = Pattern.compile(regx);
    }

    public String getTokenId() {
        return tokenid;
    }

    public void setTokenId(String tokenid) {
        this.tokenid = tokenid;
    }

    //初始化正则
    public  LinkedList<PlatBaseData> initbaseData(String configPath){
        LinkedList baseDataList = new LinkedList<PlatBaseData>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder db = dbf.newDocumentBuilder();
            //配置文件位置
            Document doc = db.parse(configPath);

            NodeList urlList = doc.getElementsByTagName("data");

            for (int i = 0; i < urlList.getLength(); i++)
            {
                PlatBaseData platBaseData = new PlatBaseData();
                Node bbsData = urlList.item(i);
                NodeList nodeDetail = bbsData.getChildNodes();
                for (int j = 0; j < nodeDetail.getLength(); j++) {
                    Node detail = nodeDetail.item(j);
                    if ("token_id".equals(detail.getNodeName())){
                        //System.out.println("token_id: " + detail.getTextContent());
                        platBaseData.setTokenId(detail.getTextContent());
                    }
                    if ("platform".equals(detail.getNodeName())){
                        platBaseData.setPlatform(detail.getTextContent());
                       // System.out.println("platform:" + detail.getTextContent());
                    }
                    if ("pageType".equals(detail.getNodeName())){
                        platBaseData.setPageType(detail.getTextContent());
                        //System.out.println("pageType: " + detail.getTextContent());
                    }
                    if ("regx".equals(detail.getNodeName())){
                        platBaseData.setPattern(detail.getTextContent());
                       // System.out.println(detail.getTextContent());
                    }

                }
                baseDataList.add(platBaseData);
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
            Document doc = db.parse("conf/page.xml");

            NodeList urlList = doc.getElementsByTagName("data");
            System.out.println("共有" + urlList.getLength() + "个data节点");
            for (int i = 0; i < urlList.getLength(); i++)
            {
                Node bbsData = urlList.item(i);
                NodeList nodeDetail = bbsData.getChildNodes();
                for (int j = 0; j < nodeDetail.getLength(); j++) {
                    Node detail = nodeDetail.item(j);
                    if ("tokenid".equals(detail.getNodeName())){
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
