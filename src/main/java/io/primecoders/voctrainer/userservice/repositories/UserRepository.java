package io.primecoders.voctrainer.userservice.repositories;

import io.primecoders.voctrainer.userservice.models.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
    UserEntity findById(String id);
}
