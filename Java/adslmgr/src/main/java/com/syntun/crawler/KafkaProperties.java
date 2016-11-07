package com.syntun.crawler;

public interface KafkaProperties
{
   //---  
  final static String bootstrap_servers = "192.168.0.73:9092";
  final static String metadata_broker_list = "192.168.0.73:9092";
  final static String zkConnect = "192.168.0.73:2181";
  //---  
  final static String kafkaServerURL = "192.168.0.73";
  final static int kafkaServerPort = 9092;
  final static int kafkaProducerBufferSize = 64*1024;
  final static int connectionTimeOut = 100000;
  final static int reconnectInterval = 10000;
  //---  
  final static  String groupId = "group1";
  final static String clientId = "SimpleConsumerDemoClient";
  final static String topic = "test";

}