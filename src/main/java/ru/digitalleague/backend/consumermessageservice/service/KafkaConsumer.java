package ru.digitalleague.backend.consumermessageservice.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.digitalleague.backend.consumermessageservice.entity.OrdersItem;
import ru.digitalleague.backend.consumermessageservice.entity.User;
import ru.digitalleague.backend.consumermessageservice.model.Answer;
import ru.digitalleague.backend.consumermessageservice.reposirtories.OrderRepository;
import ru.digitalleague.backend.consumermessageservice.reposirtories.UserRepository;
import ru.digitalleague.backend.consumermessageservice.util.LocalDateAdapter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Service
@EnableKafka
@Data
public class KafkaConsumer{
    private Logger LOG = LogManager.getLogger(KafkaConsumer.class);
    private Gson gson;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final DBService dbService;
    private final KafkaProducer kafkaProducer;

    public KafkaConsumer(Gson gson, OrderRepository orderRepository, UserRepository userRepository, DBService dbService, KafkaProducer kafkaProducer) {
        this.gson = gson;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.dbService = dbService;
        this.kafkaProducer = kafkaProducer;
    }

    @KafkaListener(topics = "${spring.kafka.template.default-topic}")
    public ConsumerRecord<String, String> getMessages(ConsumerRecord<String, String> record) {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        User user1 = gson.fromJson(record.value(), User.class);
        User user = gson.fromJson(record.value(), User.class);
        LOG.info("Getting massages from kafka={}",record);
        String requestId = "";
        Long timestamp = record.timestamp();
        for (Header c : record.headers()) {
            requestId = new String(c.value());
        }
        Map<Boolean, List<OrdersItem>> booleanListMap = dbService.chekOrderStatus(user);
        String finalRequestId = requestId;
        booleanListMap.get(true)
                .forEach(s -> {
            dbService.addUserToOrdersItemAndSavetoPostgres(user1, s);
                    try {
                        kafkaProducer.sendMessages(new Answer("OK", timestamp, finalRequestId, s.getUuid()));
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
        booleanListMap.get(false)
                .forEach(s -> {
                    try {
                        kafkaProducer
                                .sendMessages(new Answer("False", timestamp, finalRequestId, s.getUuid()));
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
    return record;
    }
}
