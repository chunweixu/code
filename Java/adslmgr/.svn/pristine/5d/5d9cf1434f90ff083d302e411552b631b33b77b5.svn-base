package com.syntun.crawler;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
/**
 * 向zookeeper写入offset数据
 * @author 
 *
 */
public class ZookeeperUtil {
	
	private static Logger logger = LogManager.getLogger(ZookeeperUtil.class.getName());
	
	private ZooKeeper zookeeper=null;
	private  final String root=ZOOKEEPER_ROOTPATH;
	private  String path_offset=DEFAULT_PATH;
	private String zookeeper_conn= CrawlerConfig.OFFSET_ZOOKEEPER_SERVER;     //需要配置项
	private int sessionTimeout=30000;
	private int retries = 0;
	
	public static final String DEFAULT_PATH="/root/offset";
	public static final String ZOOKEEPER_ROOTPATH="/root";
	
	/**
	 * 
	 * @param topic
	 * @param m_partition
	 */
	public ZookeeperUtil(String topic,int m_partition) {
				try {
					initZookeeper();
					this.path_offset=DEFAULT_PATH+"_"+topic+"_"+m_partition;
					this.createOffsetNode();
				} catch (KeeperException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	public void rebuild(String topic,int m_partition){
		this.path_offset=DEFAULT_PATH+"_"+topic+"_"+m_partition; 
		try {
			this.createOffsetNode();
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public  boolean initZookeeper(){
		try {
			this.zookeeper = new ZooKeeper(zookeeper_conn, sessionTimeout, new Watcher() { 
			    public void process(WatchedEvent event) { 
			    	logger.info(event.toString());
			    } 
			});
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	

	public synchronized boolean  writeOffset(long offset){
		int old_version=0;
		int version=0;
		try {
			old_version = zookeeper.exists(path_offset, false).getVersion();
			version = zookeeper.setData(path_offset, (offset+"").getBytes(), -1).getVersion();
		} catch (KeeperException e) {
			logger.error("Reconnect to zookeeper server.");
			retries++;
			if(retries < 10 && initZookeeper()){
				retries = 0;
				return writeOffset(offset);
			}
			e.printStackTrace();
				
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return old_version!=version;
	}
	
	
	public synchronized boolean writeOffset(long offset,String path) throws KeeperException, InterruptedException{
		int old_version=zookeeper.exists(path, false).getVersion();
		int version=zookeeper.setData(path, (offset+"").getBytes(), -1).getVersion();
		return old_version!=version;
	}

	public long getDataOffset() {
		String offsetStr="";
		try {
			byte[] b=zookeeper.getData(path_offset,false,null);
			if(null==b){
				offsetStr="0";
			}else
			offsetStr=new String(b);
			return Long.parseLong(offsetStr);
		} catch (NumberFormatException e) {
			logger.error("path:"+path_offset+"  get offset:"+offsetStr+"   error:NumberFormatException");
			e.printStackTrace();
			System.exit(0);
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public long getDataOffset(String path) throws KeeperException, InterruptedException{
		String offsetStr=new String(zookeeper.getData(path,false,null));
		try {
			return Long.parseLong(offsetStr);
		} catch (NumberFormatException e) {
			logger.error("path:"+path+"  get offset:"+offsetStr+"   error:NumberFormatException");
			e.printStackTrace();
			System.exit(0);
		}
		return -1;
	}
	
	public void createOffsetNode() throws KeeperException, InterruptedException{
		if(null==zookeeper.exists(root, false)){
			zookeeper.create(root,null, Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
		}
		if(null==zookeeper.exists(path_offset, false)){
			zookeeper.create(path_offset,null, Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT); 
		}
	}
	
	public void doTest() throws KeeperException, InterruptedException, IOException{
		String topic ="test";
		int m_partition=1;
		long offset=1000;
		new ZookeeperUtil(topic,m_partition).writeOffset(offset);
		System.out.println(new ZookeeperUtil(topic,m_partition).getDataOffset());
	}
	
	public static void main(String[] args) throws KeeperException, InterruptedException {
		String topic="rawpage";
		ZookeeperUtil zu=new ZookeeperUtil(topic,0);
		System.out.println(zu.getDataOffset());//zookeeper.getData("/test1",false,null));
		zu.rebuild(topic, 1);
		System.out.println(zu.getDataOffset());
		zu.rebuild(topic, 2);
		System.out.println(zu.getDataOffset());
	}
}
