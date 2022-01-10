package ru.digitalleague.backend.consumermessageservice.model;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.digitalleague.backend.consumermessageservice.entity.OrdersItem;
import java.util.List;

@Data
@NoArgsConstructor
public class UserModel {
    private Long id;
    private String userFirstName;
    private String userLastName;
    private List<OrdersItem> ordersItems;
}
