package io.albot.distributedtransactions.dao;

import io.albot.distributedtransactions.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
