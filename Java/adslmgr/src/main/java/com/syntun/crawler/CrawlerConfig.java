package com.syntun.crawler;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

class InterfaceConfig{
	public InetAddress ip = null;
	public String name = null;
}

public class CrawlerConfig {
	
	
	private static Logger logger = LogManager.getLogger(FetcherThread.class.getName());
	
	private static Document m_configRoot = null;
	public static int DEFAULT_SOCKET_TIMEOUT = 10000;
	public static int DEFAULT_CONNECTION_TIMEOUT = 10000;
	public static int DEFAULT_FETCHER_THREAD_NUMBER = 1;
	
	public static int SOCKET_TIMEOUT = DEFAULT_SOCKET_TIMEOUT;
	public static int CONNECTION_TIMEOUT = DEFAULT_CONNECTION_TIMEOUT;
	public static int FETCHER_THREAD_NUMBER = DEFAULT_FETCHER_THREAD_NUMBER;
	public static List<InterfaceConfig> INTERFACE_LIST = new ArrayList<InterfaceConfig>();
	public static List<String> BROKER_LIST = new ArrayList<String>();
	public static int BROKER_PORT = 9092;
	public static String URL_SELECTOR_TOPIC = null;
	public static int URL_SELECTOR_PARTITION = -1;
	public static List<Integer> URL_SELECTOR_PARTITION_LIST=new ArrayList<Integer>();
	public static String RAW_PAGE_TOPIC = null;
	public static String RAW_PAGE_CLIENT_NAME = null;
	public static boolean RAW_PAGE_COMPRESS = false;
	public static String OFFSET_ZOOKEEPER_SERVER = null;
	public static String DOMAIN_CONFIG_PATH = null;
	public static String CHECKER_MYSQL_SERVER = null;
	public static String CHECKER_MYSQL_DB = null;
	public static String CHECKER_MYSQL_USER = null;
	public static String CHECKER_MYSQL_PASSWD = null;
	public static String CHECKER_REDIAL_PATH = null;
	
	public static boolean ReadXml(String fileName){
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		boolean retval = false;
		try{
			 DocumentBuilder domBuilder=domFactory.newDocumentBuilder();
			 InputStream is=new FileInputStream(fileName);
			 m_configRoot = domBuilder.parse(is);
			 retval = true;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			retval = false;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			retval = false;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			retval = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			retval = false;
		}
		return retval;
	}
	
	public static boolean parseSocketTimeout() {
		NodeList nodes = m_configRoot.getElementsByTagName("socketTimeout");
		if (nodes == null || nodes.getLength() != 1) {
			return false;
		}
		Node node = nodes.item(0);
		CrawlerConfig.SOCKET_TIMEOUT = Integer.valueOf(node.getTextContent());
		return true;
	}
	
	public static boolean parseConnectionTimeout() {
		NodeList nodes = m_configRoot.getElementsByTagName("connectionTimeout");
		if (nodes == null || nodes.getLength() != 1) {
			return false;
		}
		Node node = nodes.item(0);
		CrawlerConfig.CONNECTION_TIMEOUT = Integer.valueOf(node.getTextContent());
		return true;
	}
	
	public static boolean parseFetcherThreadNumber() {
		NodeList nodes = m_configRoot.getElementsByTagName("fetcherThreadNum");
		if (nodes == null || nodes.getLength() != 1) {
			return false;
		}
		Node node = nodes.item(0);
		CrawlerConfig.FETCHER_THREAD_NUMBER = Integer.valueOf(node.getTextContent());
		
		return true;
	}
	
	private static String getChildNodeContent(Node node, String name) {
		for (int i = 0; i < node.getChildNodes().getLength(); ++i) {
			Node curNode = node.getChildNodes().item(i);
			if (curNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			//System.out.println(curNode.getTextContent());
			if (!name.equals(curNode.getNodeName())) {
				continue;
			}
			return curNode.getTextContent();
		}
		return null;
	}
	
	private static List<String> getChildNodeContentList(Node node, String name) {
		List<String> list=new ArrayList<String>();
		for (int i = 0; i < node.getChildNodes().getLength(); ++i) {
			Node curNode = node.getChildNodes().item(i);
			if (curNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			//System.out.println(curNode.getTextContent());
			if (!name.equals(curNode.getNodeName())) {
				continue;
			}
			list.add(curNode.getTextContent());
		}
		
		if(list.size()==0){
			return null;
		}
		return list;
	}
	
	private static List<String> getAllChildContentsByName(Node node, String name) {
		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < node.getChildNodes().getLength(); ++i) {
			Node curNode = node.getChildNodes().item(i);
			if(curNode.getNodeType() != Node.ELEMENT_NODE){
				continue;
			}
			if(!name.equals(curNode.getNodeName())){
				continue;
			}
			ret.add(curNode.getTextContent());
		}
		return ret;
	}
	
	public static boolean parseInterfaces() {
		NodeList nodes = m_configRoot.getElementsByTagName("interfaces");
		if (nodes == null || nodes.getLength() != 1) {
			return false;
		}
		Node ifNode = nodes.item(0);
		String strIp;
		String ifName;
		for (Node curNode = ifNode.getFirstChild(); curNode != null; curNode = curNode.getNextSibling()) {
			if (curNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			if (!curNode.getNodeName().equals("interface")) {
				continue;
			}
			
			InterfaceConfig ifCfg = new InterfaceConfig();
			strIp = getChildNodeContent(curNode,"localIp");
			if (strIp == null) {
				logger.error("Invalid localIp.");
				return false;
			}
			ifName = getChildNodeContent(curNode,"name");
			if (ifName == null) {
				logger.error("Invalid interface name.");
				return false;
			}
			
			try {
				ifCfg.ip = InetAddress.getByName(strIp);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				logger.error("Can not parse ip:" + strIp);
				e.printStackTrace();
				return false;
			}
			ifCfg.name = ifName;
			
			INTERFACE_LIST.add(ifCfg);
		}
		return true;
	}
	public static boolean parseGlobal() {
		NodeList nodes = m_configRoot.getElementsByTagName("global");
		if (nodes == null || nodes.getLength() != 1) {
			return false;
		}
		OFFSET_ZOOKEEPER_SERVER = getChildNodeContent(nodes.item(0),"zookeeper");
		if (OFFSET_ZOOKEEPER_SERVER == null) {
			return false;
		}
		return true;
	}
	
	public static boolean parseKafka() {
		NodeList nodes = m_configRoot.getElementsByTagName("kafka");
		if (nodes == null || nodes.getLength() != 1) {
			return false;
		}
		BROKER_LIST = getAllChildContentsByName(nodes.item(0),"broker");
		if (BROKER_LIST == null || BROKER_LIST.size()<=0) {
			logger.error("No kafka broker found.");
			return false;
		}
		String strPort = getChildNodeContent(nodes.item(0),"port");
		if (strPort == null) {
			logger.error("Invalid kafka port.");
			return false;
		}
		BROKER_PORT = Integer.valueOf(strPort);		
		return true;
	}
	
	public static boolean parseUrlSelector() {
		NodeList nodes = m_configRoot.getElementsByTagName("urlSelector");
		if (nodes == null || nodes.getLength() != 1) {
			return false;
		}
		URL_SELECTOR_TOPIC = getChildNodeContent(nodes.item(0),"topic");
		List<String> partition_list=getChildNodeContentList(nodes.item(0),"partition");

		if (URL_SELECTOR_TOPIC == null || partition_list == null) {
			return false;
		}
		for (String partition : partition_list) {
			URL_SELECTOR_PARTITION_LIST.add(Integer.valueOf(partition));
		}
		return true;
	}
	
	public static boolean parseRawPage() {
		NodeList nodes = m_configRoot.getElementsByTagName("rawPage");
		if (nodes == null || nodes.getLength() != 1) {
			return false;
		}
		RAW_PAGE_TOPIC = getChildNodeContent(nodes.item(0),"topic");
		if (RAW_PAGE_TOPIC == null) {
			return false;
		}
		RAW_PAGE_CLIENT_NAME = getChildNodeContent(nodes.item(0),"clientName");
		if (RAW_PAGE_CLIENT_NAME == null) {
			return false;
		}
		String strCompress = getChildNodeContent(nodes.item(0),"compress");
		if(strCompress != null && strCompress.equalsIgnoreCase("true")){
			RAW_PAGE_COMPRESS = true;
		}
		else{
			RAW_PAGE_COMPRESS = false;
		}
		return true;
	}
	
	public static boolean parseDomainInfo(){
		NodeList nodes = m_configRoot.getElementsByTagName("domainInfo");
		if (nodes == null || nodes.getLength() != 1) {
			return false;
		}
		
		DOMAIN_CONFIG_PATH = getChildNodeContent(nodes.item(0), "path");
		if (DOMAIN_CONFIG_PATH == null) {
			return false;
		}
		return true;
	}
	
	public static boolean parseChecker(){
		NodeList nodes = m_configRoot.getElementsByTagName("checker");
		if (nodes == null || nodes.getLength() != 1) {
			return false;
		}
		
		CHECKER_MYSQL_SERVER = getChildNodeContent(nodes.item(0), "mysql_server");
		if (CHECKER_MYSQL_SERVER == null) {
			return false;
		}
		
		CHECKER_MYSQL_DB = getChildNodeContent(nodes.item(0), "mysql_db");
		if (CHECKER_MYSQL_DB == null) {
			return false;
		}
		
		CHECKER_MYSQL_USER = getChildNodeContent(nodes.item(0), "mysql_user");
		if (CHECKER_MYSQL_USER == null) {
			return false;
		}
		
		CHECKER_MYSQL_PASSWD = getChildNodeContent(nodes.item(0), "mysql_passwd");
		if (CHECKER_MYSQL_PASSWD == null) {
			return false;
		}
		
		CHECKER_REDIAL_PATH = getChildNodeContent(nodes.item(0), "redial_path");
		if (CHECKER_REDIAL_PATH == null) {
			return false;
		}
		
		
		return true;
	}

	public static boolean parseConfig(String fileName) {
		if (!ReadXml(fileName)) {
			return false;
		}
		
		if (!parseSocketTimeout()) {	
			SOCKET_TIMEOUT = DEFAULT_SOCKET_TIMEOUT;
		}
		
		if (!parseConnectionTimeout()) {
			CONNECTION_TIMEOUT = DEFAULT_CONNECTION_TIMEOUT;
		}
		
		if (!parseFetcherThreadNumber()) {
			FETCHER_THREAD_NUMBER = DEFAULT_FETCHER_THREAD_NUMBER;
		}
		
		if (!parseInterfaces()) {
			logger.error("Parse interface config error.");
			return false;
		}
		
		if (!parseKafka()) {
			logger.error("Parse kafka config error.");
			return false;
		}
		
		if (!parseUrlSelector()) {
			logger.error("Parse urlselector config error.");
			return false;
		}
		
		if (!parseRawPage()) {
			logger.error("Parse rawpage config error.");
			return false;
		}
		
		if(!parseDomainInfo()){
			logger.warn("No domain info settings.");
			//return false;
		} 
		
		if(!parseChecker()){
			logger.error("Parse checker config error.");
			return false;
		}
		
		if(!parseGlobal()){
			logger.error("Parse global config error.");
			return false;
		}
		return true;
	}
	
}
