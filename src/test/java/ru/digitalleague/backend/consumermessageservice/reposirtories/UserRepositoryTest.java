//package ru.digitalleague.backend.consumermessageservice.reposirtories;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import ru.digitalleague.backend.consumermessageservice.entity.OrdersItem;
//import ru.digitalleague.backend.consumermessageservice.entity.User;
//
//import javax.transaction.Transactional;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//class UserRepositoryTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    @Transactional
//    public void addUser(){
//        OrdersItem order = new OrdersItem();
//        order.setUuid(UUID.randomUUID());
//        order.setCost(3232L);
//        order.setStatus("pay");
//        order.setDateCreate(LocalDate.now());
//        List<OrdersItem> orderList = List.of(order);
//        User user = new User();
//        user.setUserFirstName("Vlad");
//        user.setUserLastName("Nabiev");
//        userRepository.save(user);
//        User byId = userRepository.getById(1L);
//        assertEquals(user,byId);
//    }
//}