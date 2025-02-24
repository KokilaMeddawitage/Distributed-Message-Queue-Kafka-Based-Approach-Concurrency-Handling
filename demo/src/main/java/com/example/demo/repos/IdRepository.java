package com.example.demo.repos;

import com.example.demo.models.IdDocument;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdRepository extends CouchbaseRepository<IdDocument, String> {

    @Query("#{#n1ql.selectEntity}  USE KEYS ['#{[0]}'] ")
    Optional<IdDocument> retrieveIdByDocumentId(String documentId);
}