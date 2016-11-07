package com.syntun.crawler;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;


public class Producer {
	  private final KafkaProducer<Integer, String> producer;
	  private final String topic;
	  public Producer(String topic)
	  {
	    Properties props = new Properties();
	    props.put("bootstrap.servers", KafkaProperties.bootstrap_servers);
	    props.put("client.id", KafkaProperties.clientId);
	    //props.put("key.serializer", "com.syntun.util.kafka.IntegerSerializer");
	    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	    
	    producer = new KafkaProducer<Integer, String>(props);
	    this.topic = topic;
	  }
	  
	  /**
	   * 发送数据到kafka
	   * @param message
	   * @throws InterruptedException
	   * @throws ExecutionException
	   */
	  public void sendData(String message) throws InterruptedException, ExecutionException{
		  producer.send(new ProducerRecord(topic,"key",message)).get();
	  }
	  
	  public static void main(String[] args) throws InterruptedException, ExecutionException {
		  new Producer("test").sendData("--lh");
	}
}
