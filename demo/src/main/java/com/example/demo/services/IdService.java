package com.example.demo.services;

import com.example.demo.consumers.IdConsumer;
import com.example.demo.models.IdDocument;
import com.example.demo.producers.IdProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IdService {
    private final IdProducer idProducer;
    private final IdConsumer idConsumer;
    private final IdDBService idDBService;

    @Autowired
    public IdService(IdProducer idProducer, IdConsumer idConsumer, IdDBService idDBService) {
        this.idProducer = idProducer;
        this.idConsumer = idConsumer;
        this.idDBService = idDBService;
    }



    public String generateId(String airlineDesignator, String requestId) {
        // kafka topic update
        idProducer.sendIdRequest(airlineDesignator, requestId);

        // database update
        boolean stored = idDBService.saveId(airlineDesignator, requestId);

        if (stored) {
            return "Stored: " + requestId;
        } else {
            return "Duplicate: " + requestId;
        }
    }


    public String getId(String airlineDesignator) {
        String uniqueId = idConsumer.pollMessages(airlineDesignator);
        Optional<IdDocument> fetchedId = idDBService.updateDB(uniqueId);
        return uniqueId;
    }
}
