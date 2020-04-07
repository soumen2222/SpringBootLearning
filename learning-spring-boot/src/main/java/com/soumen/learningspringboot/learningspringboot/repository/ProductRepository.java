package com.soumen.learningspringboot.learningspringboot.repository;

import com.soumen.learningspringboot.learningspringboot.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductRepository
        extends ReactiveMongoRepository<Product, String> {
}
