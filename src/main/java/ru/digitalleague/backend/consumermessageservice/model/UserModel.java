package ru.digitalleague.backend.consumermessageservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private String userFirstName;
    private String userLastName;
    private List<OrdersModel> ordersItems;
}
