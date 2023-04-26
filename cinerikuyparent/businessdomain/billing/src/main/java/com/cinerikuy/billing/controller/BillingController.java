package com.cinerikuy.billing.controller;

import com.cinerikuy.billing.entity.Billing;
import com.cinerikuy.billing.repository.BillingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/billings")
public class BillingController {

    @Autowired
    private BillingRepository billingRepo;
    @PostMapping
    public ResponseEntity<?> post(@RequestBody Billing billing) {
        Billing save = billingRepo.save(billing);
        return ResponseEntity.ok(save);
    }
}
