package com.chao.share.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "spring.data.mongodb")
@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.host}")
    private String mongoHost;

    @Value("${spring.data.mongodb.port}")
    private String mongoPort;

    @Value("${spring.data.mongodb.database}")
    private String dataBaseName;

    @Value("${spring.data.mongodb.username}")
    private String mongoUser;

    @Value("${spring.data.mongodb.password}")
    private String mongoPassword;

    @Value("${spring.data.mongodb.gridfs.bucket}")
    private String gridFsBucket;

    @Bean
    public MongoClientSettings mongoClientSettings() {
        MongoCredential mongoCredential = MongoCredential.createCredential(mongoUser, dataBaseName, mongoPassword.toCharArray());
        return MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("mongodb://" + mongoHost + ":" + mongoPort))
                .credential(mongoCredential)
                .build();
    }

    @Bean
    public MongoClient mongoClient(MongoClientSettings mongoClientSettings) {
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public GridFSBucket getgridFSBucket(MongoClient mongoClient) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dataBaseName);
        return GridFSBuckets.create(mongoDatabase);
    }

}
