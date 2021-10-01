package io.albot.distributedtransactions.service;

import io.albot.distributedtransactions.dto.Passport;
import io.albot.distributedtransactions.dto.SocialNetworkData;
import io.albot.distributedtransactions.dto.TaxData;
import io.albot.distributedtransactions.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final WebClient webClient;
    @Value("${passportUrl}")
    private String passportUrl;
    @Value("${tax-office-url}")
    private String taxServiceUrl;
    @Value("${social-network-url}")
    private String socialNetworkUrl;

    @Override
    public Mono<User> save(User user) {
        return webClient.post().uri(passportUrl + "/save").bodyValue(new Passport(1234, 567890))
                .retrieve()
                .bodyToMono(Passport.class)
                .doOnEach(System.out::println)
                .zipWith(webClient.post().uri(taxServiceUrl + "/save").bodyValue(new TaxData(1234567L))
                        .retrieve()
                        .bodyToMono(TaxData.class)
                        .doOnEach(System.out::println), (p, t) -> new User(p, t, null))
                .zipWith(webClient.post().uri(socialNetworkUrl + "/save").bodyValue(new SocialNetworkData(1234))
                        .retrieve()
                        .bodyToMono(SocialNetworkData.class)
                        .doOnEach(System.out::println), (u, s) -> {
                    u.setSocialNetworkData(s);
                    return u;
                });
    }

    @Override
    public Mono<User> find(long id) {
        return null;
    }
}
