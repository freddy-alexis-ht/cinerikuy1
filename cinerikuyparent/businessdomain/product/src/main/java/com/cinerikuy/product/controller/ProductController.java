package com.cinerikuy.product.controller;

import com.cinerikuy.product.entity.Product;
import com.cinerikuy.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping()
    public ResponseEntity<List<Product>> get() {
        List<Product> list = productRepository.findAll();
        if(list == null || list.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Product> get(@PathVariable long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable long id, @RequestBody Product input) {
        Optional<Product> findById = productRepository.findById(id);
        if(!findById.isPresent())
            return ResponseEntity.notFound().build();
        Product exist = findById.get();
        exist.setCode(input.getCode());
        exist.setName(input.getName());
        exist.setPrice(input.getPrice());
        Product obj = productRepository.save(exist);
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Product input) {
        Product save = productRepository.save(input);
        return ResponseEntity.ok(save);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        Optional<Product> findById = productRepository.findById(id);
        if(!findById.isPresent())
            return ResponseEntity.notFound().build();
        productRepository.delete(findById.get());
        return ResponseEntity.ok().build();
    }

    /** OTHER METHODS */
    @GetMapping("/code/{productCode}")
    public ResponseEntity<Product> get(@PathVariable String productCode) {
        Optional<Product> findByCode = productRepository.findByCode(productCode);
        if(!findByCode.isPresent())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(findByCode.get());
    }

}
