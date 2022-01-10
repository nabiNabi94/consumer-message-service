package ru.digitalleague.backend.consumermessageservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.digitalleague.backend.consumermessageservice.entity.User;

import java.time.LocalDate;
import java.util.UUID;
@Data
@NoArgsConstructor
public class OrdersModel {
    private Long id;
    private UUID uuid;
    private Long cost;
    private LocalDate dateCreate;
    private String status;
    private User users;
}
