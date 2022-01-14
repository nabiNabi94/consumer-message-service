package ru.digitalleague.backend.consumermessageservice.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.digitalleague.backend.consumermessageservice.entity.OrdersItem;
import ru.digitalleague.backend.consumermessageservice.entity.User;
import ru.digitalleague.backend.consumermessageservice.model.OrdersModel;
import ru.digitalleague.backend.consumermessageservice.model.UserModel;
import ru.digitalleague.backend.consumermessageservice.reposirtories.OrderRepository;
import ru.digitalleague.backend.consumermessageservice.reposirtories.UserRepository;
import ru.digitalleague.backend.consumermessageservice.util.Mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DBService {

    Logger LOG = LogManager.getLogger(DBService.class);
    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private Mapper mapper;

    public DBService(UserRepository userRepository, OrderRepository orderRepository, Mapper mapper) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    public  Map<Boolean, List<OrdersModel>> chekOrderStatus(UserModel user) {

        Map<Boolean, List<OrdersModel>> result = user.getOrdersItems()
                .stream()
                .collect(Collectors.partitioningBy(o -> o.getStatus().equals("paid") || o.getStatus().equals("send") || o.getStatus().equals("confirmed")));
        LOG.info("");
            return result;

    }

    public void addUserToOrdersItemAndSaveToPostgres(UserModel userModel,List<OrdersModel> ordersModels) {
//        userModel.getOrdersItems().clear();
            User user = new User();
            user.setUserLastName(userModel.getUserLastName())
                            .setUserFirstName(userModel.getUserFirstName());
        List<OrdersItem> ordersItems = mapper.mapList(ordersModels, OrdersItem.class);
        ordersItems.forEach(user::addOrder);
        try {
            User save = userRepository.save(user);
            LOG.info("Save to database user={} orderItems={}",save,save.getOrdersItems());
            }catch (Exception e){
                LOG.trace("Exception save to DB={}",e.getMessage());
            }


    }

}

