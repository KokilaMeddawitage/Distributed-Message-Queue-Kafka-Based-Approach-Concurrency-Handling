package com.example.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.CouchbaseClientFactory;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.convert.CouchbaseConverter;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

@Configuration
@EnableCouchbaseRepositories(basePackages = "com.example.demo.repos")  // Ensure repositories are scanned
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    @Override
    public String getConnectionString() {
        return "couchbase://localhost";
    }

    @Override
    public String getUserName() {
        return "Administrator";
    }

    @Override
    public String getPassword() {
        return "password";
    }

    @Override
    public String getBucketName() {
        return "users";  // Specify the bucket name manually
    }

    @Bean
    public CouchbaseTemplate couchbaseTemplate(CouchbaseClientFactory clientFactory, CouchbaseConverter converter) {
        return new CouchbaseTemplate(clientFactory, converter);
    }

}
