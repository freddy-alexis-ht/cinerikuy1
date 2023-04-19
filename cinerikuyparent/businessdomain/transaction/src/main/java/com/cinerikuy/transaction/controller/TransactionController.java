package com.cinerikuy.transaction.controller;

import com.cinerikuy.transaction.entity.Transaction;
import com.cinerikuy.transaction.exception.BusinessRuleException;
import com.cinerikuy.transaction.repository.TransactionRepository;
import com.cinerikuy.transaction.service.Communication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    private Communication communication;

    @GetMapping()
    public ResponseEntity<List<Transaction>> get() {
        List<Transaction> lista = transactionRepository.findAll();
        if(lista == null || lista.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> get(@PathVariable long id) {
        return transactionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable long id, @RequestBody Transaction input) {
        Transaction exist = transactionRepository.findById(id).get();
        if(exist != null){
            exist.setCode(input.getCode());
            exist.setDni(input.getDni());
            exist.setMovieName(input.getMovieName());
        }
        Transaction obj = transactionRepository.save(exist);
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Transaction input) throws BusinessRuleException, UnknownHostException {
        communication.validateProductExistence(input);
        communication.validateMovieExistence(input);
        Transaction save = transactionRepository.save(input);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        Optional<Transaction> findById = transactionRepository.findById(id);
        if(findById.get() != null)
            transactionRepository.delete(findById.get());
        return ResponseEntity.ok().build();
    }
}
