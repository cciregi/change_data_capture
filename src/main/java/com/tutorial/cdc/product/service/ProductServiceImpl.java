package com.tutorial.cdc.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.tutorial.cdc.product.document.Product;
import com.tutorial.cdc.product.repository.MongoProductRepository;
import io.debezium.data.Envelope;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService{
    private final MongoProductRepository mongoProductRepository;

    @Override
    @Transactional
    public void handleEvent(String operation, Long documentId, String collection, Map<String, Object> payload) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        Product product = objectMapper.convertValue(payload, Product.class);

        // Check if the operation is either CREATE or READ
        if (Envelope.Operation.CREATE.code().equals(operation) || Envelope.Operation.READ.code().equals(operation)) {
            // Set the MongoDB document ID to the product
            product.setMongoId(String.valueOf(documentId));
            product.setSourceCollection(collection);
            // Save the updated product information to the database
            mongoProductRepository.save(product);
            // If the operation is DELETE
        } else if(Envelope.Operation.UPDATE.code().equals(operation)){
            var productToUpdate = mongoProductRepository.findByMongoId(String.valueOf(documentId));
            product.setId(productToUpdate.getId());
            product.setMongoId(String.valueOf(documentId));
            product.setSourceCollection(collection);
            mongoProductRepository.save(product);
        }
        // If the operation is DELETE
        else if (Envelope.Operation.DELETE.code().equals(operation)) {
            // Remove the product from the database using the MongoDB document ID
            mongoProductRepository.removeProductByMongoId(String.valueOf(documentId));
        }
    }
}
