package com.tutorial.cdc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DebeziumConnectorConfig {
    @Value("${spring.data.mongodb.uri}")
    private String mongoDbUri;

    @Value("${spring.data.mongodb.username:}")
    private String mongoDbUsername;

    @Value("${spring.data.mongodb.password:}")
    private String mongoDbPassword;

    @Value("${collection.include.list}")
    private String collectionIncludeList;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${database.hostname}")
    private String databaseHostname;

    @Value("${database.port}")
    private String databasePort;

    @Value("${database.user}")
    private String databaseUser;

    @Value("${database.password}")
    private String databasePassword;

    @Value("${database.dbname}")
    private String databaseDbname;

    @Value("${database.include.list}")
    private String databaseIncludeList;

    @Bean
    public io.debezium.config.Configuration mongodbConnector() {

        Map<String, String> configMap = new HashMap<>();

        //This sets the name of the Debezium connector instance. It’s used for logging and metrics.
        configMap.put("name", "cdc-postgresql");
        //This specifies the Java class for the connector. Debezium uses this to create the connector instance.
        configMap.put("connector.class", "io.debezium.connector.postgresql.PostgresConnector");

        //This sets the Java class that Debezium uses to store the progress of the connector.
        configMap.put("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
        configMap.put("offset.storage.file.filename", "/tmp/offsets.dat");
        configMap.put("offset.flush.interval.ms", "60000");

        //This is the PostgreSQL connection string that Debezium uses to connect to your PostgreSQL instance
        configMap.put("database.hostname", databaseHostname);
        configMap.put("database.port", databasePort);
        configMap.put("database.user", databaseUser);
        configMap.put("database.password", databasePassword);
        configMap.put("database.dbname", databaseDbname);

        //This prefix is added to all Kafka topics that this connector writes to.
        configMap.put("topic.prefix", "sbd-postgresql-connector");

        return io.debezium.config.Configuration.from(configMap);
    }
}
