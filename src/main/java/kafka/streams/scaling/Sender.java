package kafka.streams.scaling;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

public class Sender {

  private static final Logger LOG = Logger.getLogger(Sender.class);

  public static void main(String[] args) {
    Properties properties = new Properties();
    properties.put("bootstrap.servers", "localhost:9092");
    properties.put("acks", "all");
    properties.put("retries", 0);
    properties.put("batch.size", 16384);
    properties.put("linger.ms", 1);
    properties.put("buffer.memory", 33554432);

    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

    Producer<String, String> producer = new KafkaProducer<>(properties);

    List<String> keys = new ArrayList<>();
    for(int i = 0; i < 30; i++) {
      keys.add(UUID.randomUUID().toString());
    }

    LOG.info("Start");
    for (String key : keys) {
      for (int i = 0; i < 10000; i++) {
        send(producer, key, "val" + i);
      }
      send(producer, key, App.DONE);
    }

    LOG.info("Closing Kafka Producer");
    producer.close();
  }

  private static void send(Producer<String, String> producer, String key, String value) {
    ProducerRecord<String, String> record = new ProducerRecord<>("inScalingTopic", key, value);
    producer.send(record);
  }

}
