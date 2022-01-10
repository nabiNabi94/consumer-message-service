package ru.digitalleague.backend.consumermessageservice.service;

import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.digitalleague.backend.consumermessageservice.entity.OrdersItem;
import ru.digitalleague.backend.consumermessageservice.entity.User;
import ru.digitalleague.backend.consumermessageservice.model.Answer;
import ru.digitalleague.backend.consumermessageservice.reposirtories.OrderRepository;
import ru.digitalleague.backend.consumermessageservice.reposirtories.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@EnableKafka
public class KafkaConsumer {
    private final Gson gson;
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

    @KafkaListener(topics = "vn-project-in", groupId = "my_group")
    public void getMessage(ConsumerRecord<String, String> record) {
        User user1 = gson.fromJson(record.value(), User.class);
        User user = gson.fromJson(record.value(), User.class);
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
            kafkaProducer.sendMessages(new Answer("OK", timestamp, finalRequestId, s.getUuid()));
        });
        booleanListMap.get(false)
                .forEach(s -> kafkaProducer
                        .sendMessages(new Answer("OK", timestamp, finalRequestId, s.getUuid())));
    }
}
