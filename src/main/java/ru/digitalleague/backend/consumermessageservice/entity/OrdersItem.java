package ru.digitalleague.backend.consumermessageservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
@Data
@NoArgsConstructor
@Entity
@Table
@Accessors(chain = true)
public class OrdersItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, unique=true)
    private Long id;
    private UUID uuid;
    private Long cost;
    @Column(name ="date_create")
    private LocalDate dateCreate;
    private String status;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User users;



}
