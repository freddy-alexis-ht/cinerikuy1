package com.cinerikuy.movie.service;

import com.cinerikuy.movie.entity.Movie;
import com.cinerikuy.movie.exception.BusinessRuleException;
import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MovieComm {

    private final WebClient.Builder webClientBuilder;

    public MovieComm(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    //define timeout
    TcpClient tcpClient = TcpClient
            .create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });

    // Validates cinemaCodes before save, cinemas must exist in product-DB
    public void validateCinemasExistence(Movie movie) throws BusinessRuleException, UnknownHostException {

        List<String> cinemaCodes = movie.getCinemaCodes();
        if (cinemaCodes != null) {
            for (Iterator<String> it = cinemaCodes.iterator(); it.hasNext();) {
                String code = it.next();
                String CinemaName = this.getCinemaName(code);
                if (CinemaName.trim().length() == 0) {
                    BusinessRuleException exception = new BusinessRuleException("1025", "Error de validación, cine no existe", HttpStatus.PRECONDITION_FAILED);
                    throw exception;
                }
            }
        }
    }

    private String getCinemaName(String cinemaCode) throws UnknownHostException {
        String name = null;
        try {
            WebClient webClient = webClientBuilder.clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                    .baseUrl("http://localhost:9088/cinemas")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", "http://localhost:9088/cinemas"))
                    .build();
            JsonNode json = webClient.method(HttpMethod.GET).uri("/code/"+cinemaCode)
                    .retrieve().bodyToMono(JsonNode.class).block();
            name = json.get("name").asText();
        } catch (WebClientResponseException e) {
            HttpStatus statusCode = e.getStatusCode();
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return "";
            } else {
                throw new UnknownHostException(e.getMessage());
            }
        }
        return name;
    }
}
