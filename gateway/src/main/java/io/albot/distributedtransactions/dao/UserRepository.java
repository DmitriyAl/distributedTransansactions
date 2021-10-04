package io.albot.distributedtransactions.dao;

import io.albot.distributedtransactions.entity.Job;
import io.albot.distributedtransactions.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    void deleteAllByJobIn(List<Job> jobs);
}
