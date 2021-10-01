package io.albot.distributedtransactions.service;

import io.albot.distributedtransactions.dao.JobRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetrySpec;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value("${passport-url}")
    private String passportUrl;
    @Value("${tax-office-url}")
    private String taxServiceUrl;
    @Value("${social-network-url}")
    private String socialNetworkUrl;
    @Value("${retry-times}")
    private int retryTimes;
    private final WebClient webClient;
    private final JobRepository jobRepository;
    private final SubJobRepository subJobRepository;
    private final UserRepository userRepository;

    @Override
    public Mono<User> save(User user) {
        SubJob passportSubJob = new SubJob(JobStatus.CREATED);
        SubJob taxOfficeSubJob = new SubJob(JobStatus.CREATED);
        SubJob socialNetworkSubJob = new SubJob(JobStatus.CREATED);
        Job saveJob = new Job(JobStatus.CREATED);
        final User target = createUser(saveJob, passportSubJob, taxOfficeSubJob, socialNetworkSubJob);
        RetrySpec retrySpec = Retry.max(retryTimes).filter(throwable -> throwable instanceof RemoteServerException);

        return webClient.post().uri(passportUrl + "/save").bodyValue(user.getPassport().setJobId(passportSubJob.getId()))
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new RemoteServerException()))
                .bodyToMono(Passport.class)
                .retryWhen(retrySpec)
                .doOnSuccess(p -> changeSubJobStatus(passportSubJob, JobStatus.FINISHED))
                .doOnError(e -> changeSubJobStatus(passportSubJob, JobStatus.ERROR))
                .zipWith(webClient.post().uri(taxServiceUrl + "/save").bodyValue(user.getTaxData().setJobId(taxOfficeSubJob.getId()))
                        .retrieve()
                        .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new RemoteServerException()))
                        .bodyToMono(TaxData.class)
                        .retryWhen(retrySpec)
                        .doOnSuccess(t -> changeSubJobStatus(taxOfficeSubJob, JobStatus.FINISHED))
                        .doOnError(e -> changeSubJobStatus(taxOfficeSubJob, JobStatus.ERROR)), (p, t) -> target.setPassport(p).setTaxData(t))
                .zipWith(webClient.post().uri(socialNetworkUrl + "/save").bodyValue(user.getSocialNetworkData().setJobId(socialNetworkSubJob.getId()))
                        .retrieve()
                        .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new RemoteServerException()))
                        .bodyToMono(SocialNetworkData.class)
                        .retryWhen(retrySpec)
                        .doOnSuccess(s -> changeSubJobStatus(socialNetworkSubJob, JobStatus.FINISHED))
                        .doOnError(e -> changeSubJobStatus(socialNetworkSubJob, JobStatus.ERROR)), User::setSocialNetworkData)
                .doOnSuccess(u -> changeJobStatus(saveJob, JobStatus.FINISHED))
                .doOnError(e -> changeJobStatus(saveJob, JobStatus.ERROR));
    }

    private User createUser(Job job, SubJob... subJobs) {
        UserEntity userEntity = new UserEntity();
        for (SubJob subJob : subJobs) {
            subJob.setJob(job);
        }
        job.setSubJobs(Arrays.asList(subJobs));
        userEntity.setJob(job);
        UserEntity entity = userRepository.save(userEntity);
        return new User().setId(entity.getId());
    }

    private void changeSubJobStatus(SubJob subJob, JobStatus status) {
        subJob.setStatus(status);
        subJobRepository.save(subJob);
    }

    private void changeJobStatus(Job job, JobStatus status) {
        job.setStatus(status);
        jobRepository.save(job);
    }

    @Override
    public Mono<User> find(long id) {
        return null;
    }
}
