package io.albot.distributedtransactions.sceduler;

import io.albot.distributedtransactions.dao.JobRepository;
import io.albot.distributedtransactions.entity.Job;
import io.albot.distributedtransactions.entity.JobStatus;
import io.albot.distributedtransactions.service.JobService;
import io.albot.distributedtransactions.service.RemoteServerException;
import io.albot.distributedtransactions.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CleanUpScheduler {
    @Value("${passport-url}")
    private String passportUrl;
    @Value("${tax-office-url}")
    private String taxServiceUrl;
    @Value("${social-network-url}")
    private String socialNetworkUrl;
    @Value("${retry-times}")
    private int retryTimes;
    private final JobRepository jobRepository;
    private final UserService userService;
    private final JobService jobService;
    private final WebClient webClient;
    private int counter;

    @Scheduled(cron = "0/30 * * * * ?")
    public void cleanUpUsers() {
        List<Job> jobs = jobRepository.findAllByStatus(JobStatus.ERROR);
        List<UUID> uuids = jobs.stream().map(Job::getId).collect(Collectors.toList());
        if (!uuids.isEmpty()) {
            Mono<Void> passportMono = webClient.post().uri(passportUrl + "/clean").bodyValue(uuids)
                    .retrieve()
                    .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new RemoteServerException()))
                    .bodyToMono(Void.class)
                    .retryWhen(Retry.max(retryTimes).filter(throwable -> throwable instanceof RemoteServerException));
            Mono<Void> taxOfficeMono = webClient.post().uri(taxServiceUrl + "/clean").bodyValue(uuids)
                    .retrieve()
                    .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new RemoteServerException()))
                    .bodyToMono(Void.class)
                    .retryWhen(Retry.max(retryTimes).filter(throwable -> throwable instanceof RemoteServerException));
            Mono<Void> socialNetworkMono = webClient.post().uri(socialNetworkUrl + "/clean").bodyValue(uuids)
                    .retrieve()
                    .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new RemoteServerException()))
                    .bodyToMono(Void.class)
                    .retryWhen(Retry.max(retryTimes).filter(throwable -> throwable instanceof RemoteServerException));
            Flux.merge(passportMono, taxOfficeMono, socialNetworkMono)
                    .doOnComplete(() -> {
                        System.out.println(counter++);
                        userService.deleteByJobs(jobs);
                        jobs.forEach(j -> jobService.changeJobStatus(j, JobStatus.CLEANED_UP));
                    }).subscribe();
        }
    }
}