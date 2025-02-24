package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.util.UUID;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdDocument {
    @Id
    private String id;

    @Field
    private String uniqueId;

    @Field
    private String status; // Example: "processed", "pending"

    @Field
    private String airlineDesignator;

//    public IdDocument() {
//        this.id = UUID.randomUUID().toString(); // Auto-generate unique ID;
//    }
}
