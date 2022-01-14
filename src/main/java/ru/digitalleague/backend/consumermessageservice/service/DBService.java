package ru.digitalleague.backend.consumermessageservice.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.digitalleague.backend.consumermessageservice.entity.OrdersItem;
import ru.digitalleague.backend.consumermessageservice.entity.User;
import ru.digitalleague.backend.consumermessageservice.reposirtories.OrderRepository;
import ru.digitalleague.backend.consumermessageservice.reposirtories.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DBService {
    Logger LOG = LogManager.getLogger(DBService.class);
    private UserRepository userRepository;
    private OrderRepository orderRepository;


    public DBService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public  Map<Boolean, List<OrdersItem>> chekOrderStatus(User user) {

        Map<Boolean, List<OrdersItem>> result = user.getOrdersItems()
                .stream()
                .collect(Collectors.partitioningBy(o -> o.getStatus().equals("paid") || o.getStatus().equals("send") || o.getStatus().equals("confirmed")));
        LOG.info("");
            return result;

    }

    public void addUserToOrdersItemAndSavetoPostgres(User user, OrdersItem ordersItem) {
        user.getOrdersItems().clear();
            user.addOrder(ordersItem);
            userRepository.save(user);
            LOG.info("Save to database user={}",user);
    }

}

