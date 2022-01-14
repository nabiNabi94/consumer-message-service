package ru.digitalleague.backend.consumermessageservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.UUID;
@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersModel {
    private UUID uuid;
    private Long cost;
    private LocalDate dateCreate;
    private String status;
}
