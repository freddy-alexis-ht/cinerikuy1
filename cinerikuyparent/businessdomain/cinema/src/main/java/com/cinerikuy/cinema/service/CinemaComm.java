package com.cinerikuy.cinema.service;

import com.cinerikuy.cinema.repository.CinemaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CinemaComm {

    @Autowired
    private CinemaRepository cinemaRepository;

    private final WebClient.Builder webClientBuilder;

    public CinemaComm(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    TcpClient tcpClient = TcpClient
            .create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });

    /** MÉTODOS */

    public <T> List<T> getMovies(String cinemaCode) {
        List<T> movies = new ArrayList<>();
        try {
            WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                    .baseUrl("http://localhost:9089/movies")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", "http://localhost:9089/movies"))
                    .build();

            List<Object> t = build.method(HttpMethod.GET).uri("/code/"+cinemaCode)
                    .retrieve().bodyToFlux(Object.class).collectList().block();

            movies = (List<T>) t;
        } catch (Exception e) {
            return movies;
        }
        return movies;
    }

}
