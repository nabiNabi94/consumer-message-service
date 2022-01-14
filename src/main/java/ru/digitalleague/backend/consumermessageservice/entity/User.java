package ru.digitalleague.backend.consumermessageservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.RowId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Users")
@Accessors(chain = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "user_first_name")
    private String userFirstName;
    @Column(name = "user_last_name")
    private String userLastName;
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "users",cascade = CascadeType.ALL)
    private List<OrdersItem> ordersItems;

    public void addOrder(OrdersItem order) {
        if (ordersItems == null) {
            ordersItems = new ArrayList<>();
        }
        order.setUsers(this);
        ordersItems.add(order);

    }


}
