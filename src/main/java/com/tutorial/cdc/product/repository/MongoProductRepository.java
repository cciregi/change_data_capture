package com.tutorial.cdc.product.repository;

import com.tutorial.cdc.product.document.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoProductRepository extends MongoRepository<Product, String> {
    void removeProductByMongoId(String mongoId);

    Product findByMongoId(String mongoId);
}
