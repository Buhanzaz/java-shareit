package ru.practicum.shareit.user.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

@Repository
public interface UserRepositoryInDB extends JpaRepository<User, Long> {
}
