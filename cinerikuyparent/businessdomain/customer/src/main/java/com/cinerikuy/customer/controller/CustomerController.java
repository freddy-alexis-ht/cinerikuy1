package com.cinerikuy.customer.controller;

import com.cinerikuy.customer.entity.Customer;
import com.cinerikuy.customer.exception.BusinessRuleException;
import com.cinerikuy.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping()
    public ResponseEntity<List<Customer>> get() {
        List<Customer> list = customerRepository.findAll();
        if(list == null || list.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Customer> get(@PathVariable long id) {
        return customerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable long id, @RequestBody Customer input) {
        Optional<Customer> findById = customerRepository.findById(id);
        if(!findById.isPresent())
            return ResponseEntity.notFound().build();
        Customer exist = findById.get();
        exist.setDni(input.getDni());
        exist.setUsername(input.getUsername());
        exist.setFirstName(input.getFirstName());
        exist.setLastName(input.getLastName());
        Customer obj = customerRepository.save(exist);
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Customer input) throws BusinessRuleException, UnknownHostException {
        Customer save = customerRepository.save(input);
        return ResponseEntity.ok(save);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        Optional<Customer> findById = customerRepository.findById(id);
        if(!findById.isPresent())
            return ResponseEntity.notFound().build();
        customerRepository.delete(findById.get());
        return ResponseEntity.ok().build();
    }

    /** OTHER METHODS */

    @GetMapping("/dni/{customerDni}")
    public ResponseEntity<Customer> get(@PathVariable String customerDni) {
        Optional<Customer> findByDni = customerRepository.findByDni(customerDni);
        if(!findByDni.isPresent())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(findByDni.get());
    }
    
}
