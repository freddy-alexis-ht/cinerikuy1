package com.cinerikuy.transaction.service;

import com.cinerikuy.transaction.entity.Transaction;
import com.cinerikuy.transaction.entity.ProductPojo;
import com.cinerikuy.transaction.exception.BusinessRuleException;
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
import java.util.concurrent.TimeUnit;

@Service
public class Communication {

    private final WebClient.Builder webClientBuilder;

    public Communication(WebClient.Builder webClientBuilder) {
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

    // Validates products before save, products must exist in product-DB
    public void validateProductExistence(Transaction transaction) throws BusinessRuleException, UnknownHostException {
        if (transaction.getProducts() != null) {
            for (Iterator<ProductPojo> it = transaction.getProducts().iterator(); it.hasNext();) {
                ProductPojo product = it.next();
                String productName = this.getProductName(product.getId());
                if (productName.trim().length() == 0) {
                    BusinessRuleException exception = new BusinessRuleException("1025", "Error de validación, producto no existe", HttpStatus.PRECONDITION_FAILED);
                    throw exception;
                }
            }
        }
    }

    // Validates movie before save, the movie must exist in movie-DB
    public void validateMovieExistence(Transaction transaction) throws BusinessRuleException, UnknownHostException {
        String movieName = this.getMovieName(transaction.getMovie().getId());
        if (movieName.trim().length() == 0) {
            BusinessRuleException exception = new BusinessRuleException("1026", "Error de validación, película no existe", HttpStatus.PRECONDITION_FAILED);
            throw exception;
        }
    }

    private String getProductName(long id) throws UnknownHostException {
        String name = null;
        try {
            WebClient webClient = webClientBuilder.clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                    .baseUrl("http://localhost:9092/products")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", "http://localhost:9092/products"))
                    .build();
            JsonNode json = webClient.method(HttpMethod.GET).uri("/"+id)
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

    private String getMovieName(long id) throws UnknownHostException {
        String name = null;
        try {
            WebClient webClient = webClientBuilder.clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                    .baseUrl("http://localhost:9089/movies")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", "http://localhost:9089/movies"))
                    .build();
            JsonNode json = webClient.method(HttpMethod.GET).uri("/"+id)
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
