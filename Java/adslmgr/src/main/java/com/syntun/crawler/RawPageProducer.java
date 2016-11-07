package com.syntun.crawler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RawPageProducer extends Thread {
	private static Logger logger = LogManager.getLogger(FetcherThread.class.getName());
	private String m_topic = null;
	private String m_client_name = CrawlerConfig.RAW_PAGE_CLIENT_NAME;
	
	private KafkaProducer<String, String> m_producer = null;
	
	private List<ProducerRecord<String,String>> m_reqList = Collections.synchronizedList(new ArrayList<ProducerRecord<String,String>>());
	
	private FutureManager futureManager = null;
//	private List<Future<RecordMetadata>> list=null;
	
	public boolean init() {
		Properties props = new Properties();
		String strBrokers = "";
		Iterator<String> it = null;
		for (it = CrawlerConfig.BROKER_LIST.iterator(); it.hasNext(); ) {
			String broker = it.next();
			strBrokers = strBrokers + broker + ":" + CrawlerConfig.BROKER_PORT;
			if (it.hasNext()) {
				strBrokers = strBrokers + ",";
			}
		}
		m_topic = CrawlerConfig.RAW_PAGE_TOPIC;
		props.put("bootstrap.servers", strBrokers);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	    props.put("client.id", m_client_name);
	    props.put("request.required.acks", "1");
	    props.put("max.request.size", "81920000");
		
		m_producer = new KafkaProducer<String, String>(props);
		futureManager = new FutureManager(m_topic);
		
		return true;
	}
	
	public boolean postRawPage(String rawPage, String url) {
		ProducerRecord<String,String> record = new ProducerRecord<String,String>(m_topic,url,rawPage);
		synchronized (m_reqList) {
			if(m_reqList.size()>5000){
				logger.warn("Kafka producer blocked. wait 1 second.");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			m_reqList.add(record);
		}
		return true;
	}
	
	
	@Override
	public void run() {
		if (m_topic == null) {
			super.run();
			return;
		}
		long msgSent = 0;
		while (true) {	
			if (m_reqList.size() <= 0) {
				try {
					logger.debug("Body sent:" + Long.toString(msgSent));
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			List<ProducerRecord<String,String>> reqList = new ArrayList<ProducerRecord<String,String>>();
			synchronized (m_reqList) {
				reqList.addAll(m_reqList);
				m_reqList.clear();
			}
			Iterator<ProducerRecord<String,String>> it = reqList.iterator();
			Future<RecordMetadata> meta;
			for(;it.hasNext();){
				msgSent++;
				meta = m_producer.send(it.next());
				futureManager.addData(meta);
			}
		}
	}

}
