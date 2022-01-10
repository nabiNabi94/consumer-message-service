package ru.digitalleague.backend.consumermessageservice.reposirtories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.digitalleague.backend.consumermessageservice.entity.OrdersItem;

public interface OrderRepository extends JpaRepository<OrdersItem,Long> {
}
