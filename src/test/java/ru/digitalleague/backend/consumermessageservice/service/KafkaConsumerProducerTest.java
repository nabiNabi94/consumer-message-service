package ru.digitalleague.backend.consumermessageservice.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.digitalleague.backend.consumermessageservice.entity.OrdersItem;
import ru.digitalleague.backend.consumermessageservice.entity.User;
import ru.digitalleague.backend.consumermessageservice.model.Answer;
import ru.digitalleague.backend.consumermessageservice.model.OrdersModel;
import ru.digitalleague.backend.consumermessageservice.model.UserModel;
import ru.digitalleague.backend.consumermessageservice.reposirtories.OrderRepository;
import ru.digitalleague.backend.consumermessageservice.reposirtories.UserRepository;
import ru.digitalleague.backend.consumermessageservice.util.LocalDateAdapter;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest
public class KafkaConsumerProducerTest extends IntegrationTest {

    private final Logger LOG = LogManager.getLogger(KafkaConsumerProducerTest.class);
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private Gson gson;

    @Value(value ="${spring.kafka.template.default-topic}")
    private String TOPIC_IN;

    @Value(value = "${topicsOut}")
    private String TOPIC_OUT;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
   private ru.digitalleague.backend.consumermessageservice.service.KafkaConsumer kafkaConsumer;

    @Test
    void testGettingMassageByConsumer(){
        OrdersModel ordersModel = new OrdersModel();
        UserModel userModel1 = new UserModel();
        List<OrdersModel> ordersModelList = List.of(ordersModel);
        CountDownLatch latch = new CountDownLatch(1);
        KafkaConsumer<String,String> consumer = consumer();
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        ordersModel
                .setCost(1000L)
                .setDateCreate(LocalDate.now())
                .setStatus("paid")
                .setUuid(UUID.randomUUID());
        userModel1
                .setUserFirstName("Vlad")
                .setUserLastName("Nabiev")
                .setOrdersItems(ordersModelList);
        String testUserJson2 = gson.toJson(userModel1);
        ListenableFuture send = kafkaTemplate.send(new ProducerRecord<>(TOPIC_IN, testUserJson2));

        send.addCallback(new ListenableFutureCallback<SendResult<String,String>>() {
            @Override
            public void onFailure(Throwable ex) {

            }

            @Override
            public void onSuccess(SendResult<String,String> result) {
                assertEquals(1L,result.getRecordMetadata().offset());
                assertEquals(TOPIC_IN,result.getRecordMetadata().topic());
                LOG.info("Kafka sent topic='{}' with offset={}",result.getRecordMetadata().topic(),
                        result.getRecordMetadata().offset());
            }
        });
        consumer.subscribe(Collections.singleton(TOPIC_IN));
        consumer.poll(Duration.ofSeconds(5))
                .forEach(record -> {assertEquals(testUserJson2,kafkaConsumer.getMessages(record).value());
                LOG.info("Consumer getting massage='{}' equals sending massage={}",kafkaConsumer.getMessages(record).value(),testUserJson2
                );});

    }
    @Test
    void integrationTestBusinessLogic() throws ExecutionException, InterruptedException {
        LOG.info("Start Test integrationTestBusinessLogic ");
        OrdersModel ordersModel = new OrdersModel();
        OrdersModel ordersModel1 = new OrdersModel();
        OrdersModel ordersModel2 = new OrdersModel();
        OrdersModel ordersModel3 = new OrdersModel();
        OrdersModel ordersModel4 = new OrdersModel();
        CountDownLatch latch = new CountDownLatch(1);
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        KafkaConsumer<String,String> consumerIn = consumer();
        KafkaConsumer<String,String> consumerOut = consumer();
        List<OrdersModel> ordersModelList = List.of(ordersModel1,ordersModel2,ordersModel,ordersModel4,ordersModel3);
        UserModel userModel2 = new UserModel();
        ordersModel
                .setCost(1000L)
                .setDateCreate(LocalDate.now())
                .setStatus("send")
                .setUuid(UUID.randomUUID());
        ordersModel1
                .setCost(2000L)
                .setDateCreate(LocalDate.now())
                .setStatus("paid")
                .setUuid(UUID.randomUUID());
        ordersModel2
                .setCost(1500L)
                .setDateCreate(LocalDate.now())
                .setStatus("canceled")
                .setUuid(UUID.randomUUID());
        ordersModel3
                .setCost(1500L)
                .setDateCreate(LocalDate.now())
                .setStatus("created")
                .setUuid(UUID.randomUUID());
        ordersModel4
                .setCost(1500L)
                .setDateCreate(LocalDate.now())
                .setStatus("confirmed")
                .setUuid(UUID.randomUUID());
        userModel2
                .setUserFirstName("Vlad")
                .setUserLastName("Nabiev")
                .setOrdersItems(ordersModelList);
        String userJson = gson.toJson(userModel2);
        kafkaTemplate.send(new ProducerRecord(TOPIC_IN,userJson)).get();
        consumerIn.subscribe(Collections.singletonList(TOPIC_IN));
        consumerIn.poll(Duration.ofSeconds(5))
                .forEach(r -> kafkaConsumer.getMessages(r));
        List<OrdersItem> all = orderRepository.findAll();
//        LOG.info("Comparing user from database={} and kafka user={}",user,userModel);
        all.stream().filter(item -> item.getUsers()!= null).forEach(item -> {assertTrue(item.getStatus() !=
                ordersModel3.getStatus() && item.getStatus() != ordersModel2.getStatus());
        LOG.info("Status user from DB={}",item);
        });
        consumerIn.close();
        consumerOut.subscribe(Collections.singletonList(TOPIC_OUT));
        ConsumerRecords<String, String> records = consumerOut.poll(Duration.ofSeconds(5));

        Map<Boolean, List<Answer>> mapAnswer = StreamSupport.stream(records.spliterator(), false)
                .map(item ->
                    gson.fromJson(item.value(), Answer.class))
                .collect(Collectors.partitioningBy(answer -> answer.getStatus().equals("OK")
                ));
        consumerOut.close();
        long countAnswerOK = mapAnswer.get(true).size();
        long countAnswerFalse = mapAnswer.get(false).size();
        assertEquals(3L,countAnswerOK);
        assertEquals(2L,countAnswerFalse);
        assertEquals(3,all.size());
        LOG.info("Count answered with status ok={} and status false={}",countAnswerOK,countAnswerFalse);
    }
    @Test
    void testConsumerMassage() throws ExecutionException, InterruptedException {
        OrdersModel ordersModel = new OrdersModel();
        OrdersModel ordersModel1 = new OrdersModel();
        OrdersModel ordersModel2 = new OrdersModel();
        CountDownLatch latch = new CountDownLatch(1);
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        KafkaConsumer<String,String> consumer = consumer();
        List<OrdersModel> ordersModelList = List.of(ordersModel1,ordersModel2,ordersModel);
        UserModel userModel = new UserModel();
        ordersModel
                .setCost(1000L)
                .setDateCreate(LocalDate.now())
                .setStatus("paid")
                .setUuid(UUID.randomUUID());
        ordersModel1
                .setCost(2000L)
                .setDateCreate(LocalDate.now())
                .setStatus("pay")
                .setUuid(UUID.randomUUID());
        ordersModel2
                .setCost(1500L)
                .setDateCreate(LocalDate.now())
                .setStatus("canceled")
                .setUuid(UUID.randomUUID());
        userModel
                .setUserFirstName("Vlad")
                .setUserLastName("Nabiev")
                .setOrdersItems(ordersModelList);

        String userJson1 = gson.toJson(userModel);
        kafkaTemplate.send(new ProducerRecord("vn-project-in",userJson1)).get();
        consumer.subscribe(Collections.singletonList("vn-project-in"));
        consumer.poll(Duration.ofSeconds(5))
                .forEach(r -> kafkaConsumer.getMessages(r));

    }

    private KafkaConsumer<String,String> consumer(){
        Properties props = new Properties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG,"test-group");
        props.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG,"true");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,kafka.getBootstrapServers());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new KafkaConsumer<>(props);
    }

    private Producer<String,String> producer(){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,kafka.getBootstrapServers());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG,"all");
        return new KafkaProducer<>(props);
    }

}
