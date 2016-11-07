package com.syntun.crawler;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;



import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.consumer.Whitelist;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.Message;
import kafka.message.MessageAndOffset;

public class TestConsumer {

	private final ConsumerConnector consumer;
	private final SimpleConsumer simpleConsumer;
	private final String topic;

	public TestConsumer(String topic) {
		consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig());
		this.simpleConsumer = new SimpleConsumer(
				KafkaProperties.kafkaServerURL,
				KafkaProperties.kafkaServerPort,
				KafkaProperties.connectionTimeOut,
				KafkaProperties.kafkaProducerBufferSize,
				KafkaProperties.clientId);
		this.topic = topic;
	}

	private static ConsumerConfig createConsumerConfig() {
		Properties props = new Properties();
		props.put("zookeeper.connect",KafkaProperties.zkConnect);
		props.put("group.id", KafkaProperties.groupId);
		props.put("zookeeper.session.timeout.ms", "400");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");
		return new ConsumerConfig(props);
	}
	/**
	 * 获得所有懈怠数据---没有处理过的数据
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("rawtypes")
	public void getData() throws UnsupportedEncodingException{
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, new Integer(1));
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        KafkaStream stream =  consumerMap.get(topic).get(0);
        ConsumerIterator it = stream.iterator();
        while(it.hasNext()){
            System.out.println(getMessage(it.next().rawMessage$1()));  //如果不为空直接break;   就是获得一条数据
        }
	}
	/**
	 * 按offset来获得数据，
	 * @param offset
	 * @throws UnsupportedEncodingException
	 */
	public List<String> getData(long offset,int fetchSize) throws UnsupportedEncodingException {
		FetchRequest req = new FetchRequestBuilder().clientId(KafkaProperties.clientId).addFetch(topic, 0, offset, fetchSize).build();
		FetchResponse fetchResponse = simpleConsumer.fetch(req);
		return getMessages(fetchResponse.messageSet(topic, 0));
	}
	/**
	 * 直接默认UTF-8处理bytes为一个字符串
	 * @param message
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getMessage(Message message) throws UnsupportedEncodingException{
	      ByteBuffer buffer = message.payload();
	      byte [] bytes = new byte[buffer.remaining()];
	      buffer.get(bytes);
	      return new String(bytes, "UTF-8");
	}
	/**
	 * 直接默认UTF-8处理bytes为一个字符串
	 * @param messageSet
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	 private static List<String> getMessages(ByteBufferMessageSet messageSet)throws UnsupportedEncodingException {
		  List<String> list=new ArrayList<String>();
			for (MessageAndOffset messageAndOffset : messageSet) {
				ByteBuffer payload = messageAndOffset.message().payload();
				byte[] bytes = new byte[payload.limit()];
				payload.get(bytes);
				list.add(new String(bytes, "UTF-8"));
			}
			return list;
	 }

	
}
