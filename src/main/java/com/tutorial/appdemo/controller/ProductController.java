package com.tutorial.appdemo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.appdemo.models.Product;
import com.tutorial.appdemo.models.ResponseObject;
import com.tutorial.appdemo.repositories.ProductRepository;

@RestController // Inform Java Spring this class is controller
@RequestMapping(path = "/api/v1/Products") // link to send request for this controller

public class ProductController {
    // DI = Dependency Injection
    @Autowired // Object ProductController will be create when app is create // Then only use
    private ProductRepository repository;

    // ------------------------------------------------------------------------------------------------------------//
    @GetMapping("") // This request is: http://localhost:8080/api/v1/Products/getAllProducts //v1 is version 1
    List<Product> getAllProducts() {
        return repository.findAll(); //H2 DB = In-memory Database // You can also send request using Postman
    }

    // ------------------------------------------------------------------------------------------------------------//
    @GetMapping("/{id}")
    // Let's return an object with: data, message, status

    ResponseEntity<ResponseObject> findById(@PathVariable Long id) { // ResponseEntity including method HttpStatus....
        Optional<Product> foundProduct = repository.findById(id); // Optional means: value return maybe NULL

        return foundProduct.isPresent() ? // isPresent will check Optional have value or not value
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Ok", "Query Product Successfully", foundProduct))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("False", "Cannot Find Product With Id = " + id, ""));

    }

    // ------------------------------------------------------------------------------------------------------------//
    // insert new Product with POST method
    // Postman :row , JSON
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct) {
        // 2 products must not have the same name ! //Call function in JPA to validate
        List<Product> foundProducts = repository.findByProductName(newProduct.getProductName().trim());
        // trim() is used to remove leading & trailing spaces of string.
        return foundProducts.size() > 0 ? ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("Failed", "Product Name Already Taken", ""))
                : ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Ok", "Insert Product Sucessfully", repository.save(newProduct)));
    }

    // ------------------------------------------------------------------------------------------------------------//
    // update, upsert = update if found, otherwise insert
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        // pass line 56
        Product updatedProduct = repository.findById(id) // Look in repository have or don't have
                // Have => mapping all value update new information
                .map(product -> {
                    product.setProductName(newProduct.getProductName());
                    product.setYear(newProduct.getYear());
                    product.setPrice(newProduct.getPrice());
                    return repository.save(product);
                }).orElseGet(() -> {
                    // Don't have -> save new product
                    newProduct.setId(id);
                    return repository.save(newProduct);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Ok", "Update Product Successfully", updatedProduct));
    }

    // ------------------------------------------------------------------------------------------------------------//
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        boolean exist = repository.existsById(id);
        if (exist) {
            repository.deleteById(id); // Available in JPA
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Delete Product Successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Cannot Find Product To Delete", ""));

    }

}
