package io.albot.distributedtransactions.service;

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

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
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
    private final UserRepository userRepository;
    private final JobService jobService;

    @Override
    public Mono<User> save(User user) {
        SubJob passportSubJob = new SubJob(JobStatus.CREATED);
        SubJob taxOfficeSubJob = new SubJob(JobStatus.CREATED);
        SubJob socialNetworkSubJob = new SubJob(JobStatus.CREATED);
        Job saveJob = new Job(JobStatus.CREATED);
        final User target = createUser(saveJob, passportSubJob, taxOfficeSubJob, socialNetworkSubJob);

        return webClient.post().uri(passportUrl + "/save").bodyValue(user.getPassport().setJobId(saveJob.getId()))
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new RemoteServerException()))
                .bodyToMono(Passport.class)
                .retryWhen(Retry.max(retryTimes).filter(throwable -> throwable instanceof RemoteServerException))
                .doOnSuccess(p -> jobService.changeSubJobStatus(passportSubJob, JobStatus.FINISHED))
                .doOnError(e -> jobService.changeSubJobStatus(passportSubJob, JobStatus.ERROR))
                .zipWith(webClient.post().uri(taxServiceUrl + "/save").bodyValue(user.getTaxData().setJobId(saveJob.getId()))
                        .retrieve()
                        .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new RemoteServerException()))
                        .bodyToMono(TaxData.class)
                        .retryWhen(Retry.max(retryTimes).filter(throwable -> throwable instanceof RemoteServerException))
                        .doOnSuccess(t -> jobService.changeSubJobStatus(taxOfficeSubJob, JobStatus.FINISHED))
                        .doOnError(e -> jobService.changeSubJobStatus(taxOfficeSubJob, JobStatus.ERROR)), (p, t) -> target.setPassport(p).setTaxData(t))
                .zipWith(webClient.post().uri(socialNetworkUrl + "/save").bodyValue(user.getSocialNetworkData().setJobId(saveJob.getId()))
                        .retrieve()
                        .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new RemoteServerException()))
                        .bodyToMono(SocialNetworkData.class)
                        .retryWhen(Retry.max(retryTimes).filter(throwable -> throwable instanceof RemoteServerException))
                        .doOnSuccess(s -> jobService.changeSubJobStatus(socialNetworkSubJob, JobStatus.FINISHED))
                        .doOnError(e -> jobService.changeSubJobStatus(socialNetworkSubJob, JobStatus.ERROR)), User::setSocialNetworkData)
                .doOnSuccess(u -> jobService.changeJobStatus(saveJob, JobStatus.FINISHED))
                .doOnError(e -> jobService.changeJobStatus(saveJob, JobStatus.ERROR));
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

    @Override
    public Mono<User> find(long id) {
        return null;
    }

    @Override
    public void deleteByJobs(List<Job> jobs) {
        userRepository.deleteAllByJobIn(jobs);
    }
}
