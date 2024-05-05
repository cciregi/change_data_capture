package com.tutorial.cdc.product.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "product")
@Getter
@Setter
public class Product {
    @Id
    private String id;
    private String name;
    private Float price;
    private String description;
    private String sourceCollection;
    private String mongoId;
}
