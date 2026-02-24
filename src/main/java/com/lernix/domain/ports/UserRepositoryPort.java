package com.lernix.domain.ports;

import com.lernix.domain.model.User;
import com.lernix.domain.model.Email;
import com.lernix.domain.model.UserId;
import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByEmail(Email email);
    boolean existsByEmail(Email email);
    Optional<User> findById(UserId id);
    void deleteById(UserId id);
    long count();
}

