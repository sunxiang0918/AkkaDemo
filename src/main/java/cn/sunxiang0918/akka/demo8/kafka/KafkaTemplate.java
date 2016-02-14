package cn.sunxiang0918.akka.demo8.kafka;

import java.io.Serializable;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class KafkaTemplate {

    private Producer<String, Serializable> producer;

    public KafkaTemplate(String urls) {
        Properties props = new Properties();
        props.put("metadata.broker.list", urls);
        props.put("serializer.class", "cn.sunxiang0918.akka.demo8.kafka.ObjectEncoder");
        props.put("request.required.acks", "1");

        ProducerConfig config = new ProducerConfig(props);

        producer = new Producer<>(config);
    }

    public void convertAndSend(String destinationName, Object message) {
        assert message instanceof Serializable;
        KeyedMessage<String, Serializable> data = new KeyedMessage<>(destinationName, destinationName, (Serializable) message);
        producer.send(data);
    }
}
