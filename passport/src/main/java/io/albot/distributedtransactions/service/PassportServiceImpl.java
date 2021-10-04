package io.albot.distributedtransactions.service;

import io.albot.distributedtransactions.dao.PassportRepository;
import io.albot.distributedtransactions.dto.Passport;
import io.albot.distributedtransactions.entity.PassportEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PassportServiceImpl implements PassportService {
    private final PassportRepository passportRepository;

    @Override
    public Passport save(Passport passport) {
        try {
            return passportRepository.save(new PassportEntity(passport)).toDto();
        } catch (DataIntegrityViolationException ex) {
            System.out.println("repeatable insert");
            return passportRepository.findByJobId(passport.getJobId()).toDto();
        }
    }

    @Override
    public void cleanUp(List<UUID> uuids) {
        passportRepository.deleteAllByJobIdIn(uuids);
    }

    @Override
    public Passport find(long id) {
        return null;
    }
}
