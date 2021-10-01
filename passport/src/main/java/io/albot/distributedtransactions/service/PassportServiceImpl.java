package io.albot.distributedtransactions.service;

import io.albot.distributedtransactions.dao.PassportRepository;
import io.albot.distributedtransactions.dto.Passport;
import io.albot.distributedtransactions.entity.PassportEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
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
    public Passport find(long id) {
        return null;
    }
}
