package com.example.demo.services;

import com.example.demo.consumers.IdConsumer;
import com.example.demo.models.IdDocument;
import com.example.demo.producers.IdProducer;
import com.example.demo.repos.IdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IdDBService {
    private final IdRepository idRepository;

    @Autowired
    public IdDBService(IdRepository idRepository) {
        this.idRepository = idRepository;
    }


    public boolean saveId(String airlineDesignator, String requestId) {
        try {
            // Check if ID exists
            if (idRepository.existsById(requestId)) {
                System.out.println("ID already exists: " + requestId);
                return false; // Do not commit
            }

            // Store ID in Couchbase
            IdDocument newId = new IdDocument();
            newId.setId("PNR-" + requestId);
            newId.setUniqueId(requestId);
            newId.setStatus("AVAILABLE");
            newId.setAirlineDesignator(airlineDesignator);

            idRepository.save(newId);
            System.out.println("Inserted ID: " + requestId);
            return true;
        } catch (Exception e) {
            System.err.println("Error storing ID: " + requestId);
            return false;
        }
    }



    public Optional<IdDocument> updateDB(String uniqueId) {
        String documentId = "PNR-" + uniqueId;
        Optional<IdDocument> idDocumentOptional = idRepository.retrieveIdByDocumentId(documentId);
        if (idDocumentOptional.isPresent()) {
            idDocumentOptional.get().setStatus("USED");

            IdDocument updatedId = idRepository.save(idDocumentOptional.get());
            return Optional.of(updatedId);
        }
        return null;
    }
}
