package SaikrishnaYadav;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import io.confluent.kafka.serializers.KafkaAvroSerializer;

import java.util.Properties;

public class SchemaRegistryProducer {
    public static void main(String[] args) {

        //Configuring Normal Producer

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //Serializer Should be used as Kafka Avro Serializer

        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());

        //The Actual Schema Registry Server URL

        properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");

        KafkaProducer<String, Employee> producer = new KafkaProducer<String, Employee>(properties);

        String topic = "EmpData";

        //Constructing Employee Object with the Generation Avro Model Class

        Employee employee = Employee.newBuilder()
                .setAge(26)
                .setFirstName("MS")
                .setLastName("Dhoni")
                .setHeight(171f)
                .build();

        ProducerRecord<String, Employee> producerRecord = new ProducerRecord<String, Employee>(
                topic, employee
        );

        System.out.println(employee);

        //Sending the Serialized Data to the Topic

        producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception == null) {
                    System.out.println(metadata);
                } else {
                    exception.printStackTrace();
                }
            }
        });
        producer.flush();
        producer.close();
    }
}