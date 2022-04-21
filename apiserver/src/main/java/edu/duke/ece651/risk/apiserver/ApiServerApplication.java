package edu.duke.ece651.risk.apiserver;

import edu.duke.ece651.risk.apiserver.repository.APIGameHandlerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories
@SpringBootApplication
public class ApiServerApplication {

    /**
     * program entry point
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(ApiServerApplication.class, args);
    }

}
