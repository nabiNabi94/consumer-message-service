package ru.digitalleague.backend.consumermessageservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Answer {
    private String status;
    private Long timestamp;
    private String requestId;
    private UUID orderId;
}
