package com.tutorial.cdc.product.service;


import com.tutorial.cdc.product.document.Product;

import java.util.Map;

public interface ProductService {
    void handleEvent(String operation, Long documentId, String collection, Map<String, Object> payload);
}
