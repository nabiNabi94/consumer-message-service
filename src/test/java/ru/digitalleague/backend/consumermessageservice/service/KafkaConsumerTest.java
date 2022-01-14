//package ru.digitalleague.backend.consumermessageservice.service;
//
//import com.google.gson.Gson;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.testcontainers.containers.KafkaContainer;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.utility.DockerImageName;
//import ru.digitalleague.backend.consumermessageservice.ConsumerMessageServiceApplication;
//import ru.digitalleague.backend.consumermessageservice.model.OrdersModel;
//import ru.digitalleague.backend.consumermessageservice.model.UserModel;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.TimeUnit;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = ConsumerMessageServiceApplication.class,
//        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //, properties = {
//      //  "spring.datasource.url=jdbc:tc:postgresql:12.9:///postgres?TC_DEMON=true&TC_REUSABLE=true",
//        //"spring.datasource.Driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver"})
//@Testcontainers
//class KafkaConsumerTest{
//    @Autowired
//    private KafkaConsumer kafkaConsumer;
//    @Autowired
//    private KafkaProducer kafkaProducer;
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//    @Autowired
//    private Gson gson;
////    @Autowired
////    private ConsumerFactories consumerFactories;
////    @Autowired
////    private ProducerFactories producerFactories;
//    @Container
//    static KafkaContainer kafkaContainer =
//            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.0.0"))
//                    .withReuse(true);
//
//    @Container
//    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:12.9")
//            .withDatabaseName("postgres")
//            .withPassword("postgres")
//            .withUsername("postgres");
//
//    @DynamicPropertySource
//    static void properties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
//        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
//        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
//        registry.add("spring.kafka.bootstrap-servers",kafkaContainer::getBootstrapServers);
//    }
//
//
//
////    @Test
////    void getMessage() throws ExecutionException, InterruptedException {
////        OrdersModel ordersModel = new OrdersModel(1L, UUID.randomUUID(), 1000L, LocalDate.now(), "pay");
////        OrdersModel ordersModel1 = new OrdersModel(1L, UUID.randomUUID(), 1000L, LocalDate.now(), "send");
////        OrdersModel ordersModel2 = new OrdersModel(1L, UUID.randomUUID(), 1000L, LocalDate.now(), "canceled");
////        List<OrdersModel> ordersModels = List.of(ordersModel, ordersModel2, ordersModel1);
////        UserModel userModel = new UserModel(1L, "Vlad", "Nabiev", ordersModels);
////        String uuid = UUID.randomUUID().toString();
////        String json = gson.toJson(userModel);
////        ProducerRecord<String, String> record = new ProducerRecord<String, String>("vn-project-in", 0,"kay1", json);
////        record.headers().add("requestId", uuid.getBytes(StandardCharsets.UTF_8));
////
////        CompletableFuture<SendResult<String, String>> stringStringSendResult = this.kafkaTemplate.send(record).completable();
////
////
////        boolean await = kafkaConsumer.getLatch().await(1000, TimeUnit.MILLISECONDS);
////        System.out.println(kafkaConsumer.getMassage());
////        SendResult<String, String> sendResult = stringStringSendResult.get();
////        String value = sendResult.getProducerRecord().value();
////        assertEquals(json, value);
////    }
//
////    @Configuration
////    @EnableAutoConfiguration
////     class KafkaTestContainersConfiguration {
////
////        @Bean
////        public Map<String, Object> consumerConfigses() {
////            Map<String, Object> props = new HashMap<>();
////            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
////            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
////            props.put(ConsumerConfig.GROUP_ID_CONFIG, "baeldung");
////            // more standard configuration
////            return props;
////        }
////
////        @Bean
//        public ProducerFactory<String, String> producerFactoryes() {
//            Map<String, Object> configProps = new HashMap<>();
//            configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
//            // more standard configuration
//            return new DefaultKafkaProducerFactory<>(configProps);
//        }
////    }
//}