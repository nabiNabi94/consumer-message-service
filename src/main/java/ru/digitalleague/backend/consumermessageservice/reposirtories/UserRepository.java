package ru.digitalleague.backend.consumermessageservice.reposirtories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.digitalleague.backend.consumermessageservice.entity.User;

public interface UserRepository extends JpaRepository<User,Long>{
}
