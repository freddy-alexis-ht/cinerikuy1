package com.cinerikuy.transaction.service;

import com.cinerikuy.transaction.dto.TransactionRequest;
import com.cinerikuy.transaction.entity.CinemaData;
import com.cinerikuy.transaction.entity.CustomerData;
import com.cinerikuy.transaction.entity.MovieData;
import com.cinerikuy.transaction.entity.ProductData;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionComm {

    private final WebClient.Builder webClientBuilder;

    public TransactionComm(WebClient.Builder webClientBuilder) {
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

    // Validates customer before save, the customer must exist in customer-DB
    public CustomerData validateCustomerExistence(TransactionRequest request) throws BusinessRuleException, UnknownHostException {
        CustomerData customerData = this.getCustomerData(request.getCustomerData());
        if (customerData == null || customerData.getCustomerUsername().trim().length() == 0) {
            BusinessRuleException exception = new BusinessRuleException("1026", "Error de validación, customer no existe", HttpStatus.PRECONDITION_FAILED);
            throw exception;
        }
        return customerData;
    }
    private CustomerData getCustomerData(CustomerData customerData) throws UnknownHostException {
        try {
            WebClient webClient = webClientBuilder.clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                    .baseUrl("http://localhost:9091/customers")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", "http://localhost:9091/customers"))
                    .build();
            JsonNode json = webClient.method(HttpMethod.GET).uri("/dni/"+customerData.getCustomerDni())
                    .retrieve().bodyToMono(JsonNode.class).block();
            customerData.setCustomerFirstName(json.get("firstName").asText());
            customerData.setCustomerLastName(json.get("lastName").asText());
        } catch (WebClientResponseException e) {
            HttpStatus statusCode = e.getStatusCode();
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            } else {
                throw new UnknownHostException(e.getMessage());
            }
        }
        return customerData;
    }

    // Validates movie before save, the movie must exist in movie-DB
    public CinemaData validateCinemaExistence(TransactionRequest request) throws BusinessRuleException, UnknownHostException {
        CinemaData cinemaData = this.getCinemaData(request.getCinemaData());
        if (cinemaData == null || cinemaData.getCinemaName().trim().length() == 0)
            throw new BusinessRuleException("1026", "Error de validación, cine no existe", HttpStatus.PRECONDITION_FAILED);
        return cinemaData;
    }
    private CinemaData getCinemaData(CinemaData cinemaData) throws UnknownHostException {
        try {
            WebClient webClient = webClientBuilder.clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                    .baseUrl("http://localhost:9088/cinemas")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", "http://localhost:9088/cinemas"))
                    .build();
            JsonNode json = webClient.method(HttpMethod.GET).uri("/code/"+cinemaData.getCinemaCode())
                    .retrieve().bodyToMono(JsonNode.class).block();
            cinemaData.setCinemaName(json.get("name").asText());
        } catch (WebClientResponseException e) {
            HttpStatus statusCode = e.getStatusCode();
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            } else {
                throw new UnknownHostException(e.getMessage());
            }
        }
        return cinemaData;
    }


    // Validates movie before save, the movie must exist in movie-DB
    public MovieData validateMovieExistence(TransactionRequest request) throws BusinessRuleException, UnknownHostException {
        MovieData movieData = this.getMovieData(request.getMovieData());
        if (movieData == null || movieData.getMovieName().trim().length() == 0) {
            BusinessRuleException exception = new BusinessRuleException("1026", "Error de validación, película no existe", HttpStatus.PRECONDITION_FAILED);
            throw exception;
        }
        return movieData;
    }
    private MovieData getMovieData(MovieData movieData) throws UnknownHostException {
        try {
            WebClient webClient = webClientBuilder.clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                    .baseUrl("http://localhost:9089/movies")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", "http://localhost:9089/movies"))
                    .build();
            JsonNode json = webClient.method(HttpMethod.GET).uri("/code/"+movieData.getMovieCode())
                    .retrieve().bodyToMono(JsonNode.class).block();
            movieData.setMovieName(json.get("name").asText());
            movieData.setMovieTicketPrice(json.get("price").asInt());
        } catch (WebClientResponseException e) {
            HttpStatus statusCode = e.getStatusCode();
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            } else {
                throw new UnknownHostException(e.getMessage());
            }
        }
        return movieData;
    }


    // Validates products before save, products must exist in product-DB
    public List<ProductData> validateProductExistence(TransactionRequest request) throws BusinessRuleException, UnknownHostException {
        List<ProductData> productDataList = new ArrayList<>();
        if (request.getProductDataList() != null) {
            for (Iterator<ProductData> it = request.getProductDataList().iterator(); it.hasNext();) {
                ProductData productData = it.next();
                ProductData returned = this.getProductData(productData.getProductCode());
                if (returned == null || returned.getProductType().trim().length() == 0) {
                    throw new BusinessRuleException("1025", "Error de validación, producto no existe", HttpStatus.PRECONDITION_FAILED);
                }
                productData.setProductType(returned.getProductType());
                productDataList.add(productData);
            }
        }
        return productDataList;
    }
    private ProductData getProductData(String productCode) throws UnknownHostException {
        ProductData productData = new ProductData();
        try {
            WebClient webClient = webClientBuilder.clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                    .baseUrl("http://localhost:9092/products")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", "http://localhost:9092/products"))
                    .build();
            JsonNode json = webClient.method(HttpMethod.GET).uri("/code/"+productCode)
                    .retrieve().bodyToMono(JsonNode.class).block();
            productData.setProductType(json.get("type").asText());
            productData.setProductPrice(json.get("price").asInt());
        } catch (WebClientResponseException e) {
            HttpStatus statusCode = e.getStatusCode();
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            } else {
                throw new UnknownHostException(e.getMessage());
            }
        }
        return productData;
    }

}
