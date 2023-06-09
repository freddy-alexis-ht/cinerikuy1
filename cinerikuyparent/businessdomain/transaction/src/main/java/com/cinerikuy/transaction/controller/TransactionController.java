package com.cinerikuy.transaction.controller;

import com.cinerikuy.transaction.dto.TransactionRequest;
import com.cinerikuy.transaction.dto.TransactionResponse;
import com.cinerikuy.transaction.entity.*;
import com.cinerikuy.transaction.exception.BusinessRuleException;
import com.cinerikuy.transaction.repository.TransactionRepository;
import com.cinerikuy.transaction.service.TransactionComm;
import com.cinerikuy.transaction.util.TransactionRequestMapper;
import com.cinerikuy.transaction.util.TransactionResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionComm transactionComm;
    @Autowired
    private TransactionRequestMapper reqMapper;
    @Autowired
    private TransactionResponseMapper resMapper;

    @GetMapping()
    public ResponseEntity<List<Transaction>> get() {
        List<Transaction> list = transactionRepository.findAll();
        if(list == null || list.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Transaction> get(@PathVariable long id) {
        return transactionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable long id, @RequestBody Transaction input) {
        Optional<Transaction> findById = transactionRepository.findById(id);
        if(!findById.isPresent())
            return ResponseEntity.notFound().build();
        Transaction exist = findById.get();
        // setting new values
        Transaction obj = transactionRepository.save(exist);
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody TransactionRequest request) throws BusinessRuleException, UnknownHostException {
        /** VALIDACIONES */
        // transactionComm.validateCustomerExistence(input);
        CustomerData customer = transactionComm.validateCustomerExistence(request);
        CinemaData cinema = transactionComm.validateCinemaExistence(request);
        MovieData movie = transactionComm.validateMovieExistence(request);
        List<ProductData> products = transactionComm.validateProductExistence(request);
        /** SETTEO */
        Transaction trx = reqMapper.TransactionRequestToTransaction(request);
        trx.setStatus(StatusEnum.PENDIENTE_DE_PAGO.toString());
        trx.setCustomerData(customer);
        trx.setCinemaData(cinema);
        trx.setMovieData(movie);
        trx.setProductDataList(products);
        Transaction save = transactionRepository.save(trx);
        TransactionResponse response = resMapper.TransactionToTransactionResponse(save);
        response.setCinemaData(save.getCinemaData());
        response.setMovieData(save.getMovieData());
        response.setCustomerData(save.getCustomerData());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        Optional<Transaction> findById = transactionRepository.findById(id);
        if(!findById.isPresent())
            return ResponseEntity.notFound().build();
        transactionRepository.delete(findById.get());
        return ResponseEntity.ok().build();
    }
}
