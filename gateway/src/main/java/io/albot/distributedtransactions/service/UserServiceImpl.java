package io.albot.distributedtransactions.service;

import io.albot.distributedtransactions.dao.SubJobRepository;
import io.albot.distributedtransactions.dao.UserRepository;
import io.albot.distributedtransactions.dto.Passport;
import io.albot.distributedtransactions.dto.SocialNetworkData;
import io.albot.distributedtransactions.dto.TaxData;
import io.albot.distributedtransactions.dto.User;
import io.albot.distributedtransactions.entity.Job;
import io.albot.distributedtransactions.entity.JobStatus;
import io.albot.distributedtransactions.entity.SubJob;
import io.albot.distributedtransactions.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value("${passportUrl}")
    private String passportUrl;
    @Value("${tax-office-url}")
    private String taxServiceUrl;
    @Value("${social-network-url}")
    private String socialNetworkUrl;
    private final WebClient webClient;
    private final SubJobRepository subJobRepository;
    private final UserRepository userRepository;

    @Override
    public Mono<User> save(User user) {
        SubJob passportSubJob = new SubJob(JobStatus.CREATED);
        SubJob taxOfficeSubJob = new SubJob(JobStatus.CREATED);
        SubJob socialNetworkSubJob = new SubJob(JobStatus.CREATED);
        final User response = createUser(passportSubJob, taxOfficeSubJob, socialNetworkSubJob);

        return webClient.post().uri(passportUrl + "/save").bodyValue(user.getPassport())
                .retrieve()
                .bodyToMono(Passport.class)
                .doOnSuccess(p -> changeSubJobStatus(passportSubJob, JobStatus.FINISHED))
                .doOnError(e -> changeSubJobStatus(passportSubJob, JobStatus.ERROR))
                .zipWith(webClient.post().uri(taxServiceUrl + "/save").bodyValue(user.getTaxData())
                        .retrieve()
                        .bodyToMono(TaxData.class)
                        .doOnSuccess(t -> changeSubJobStatus(taxOfficeSubJob, JobStatus.FINISHED))
                        .doOnError(e -> changeSubJobStatus(taxOfficeSubJob, JobStatus.ERROR)), (p, t) -> response.setPassport(p).setTaxData(t))
                .zipWith(webClient.post().uri(socialNetworkUrl + "/save").bodyValue(user.getSocialNetworkData())
                        .retrieve()
                        .bodyToMono(SocialNetworkData.class)
                        .doOnSuccess(s -> changeSubJobStatus(socialNetworkSubJob, JobStatus.FINISHED))
                        .doOnError(e -> changeSubJobStatus(socialNetworkSubJob, JobStatus.ERROR)), User::setSocialNetworkData);
    }

    private User createUser(SubJob... subJobs) {
        UserEntity userEntity = new UserEntity();
        Job saveJob = new Job();
        for (SubJob subJob : subJobs) {
            subJob.setJob(saveJob);
        }
        saveJob.setSubJobs(Arrays.asList(subJobs));
        userEntity.setJob(saveJob);
        UserEntity entity = userRepository.save(userEntity);
        return new User().setId(entity.getId());
    }

    private void changeSubJobStatus(SubJob subJob, JobStatus status) {
        subJob.setStatus(status);
        subJobRepository.save(subJob);
    }

    @Override
    public Mono<User> find(long id) {
        return null;
    }
}
