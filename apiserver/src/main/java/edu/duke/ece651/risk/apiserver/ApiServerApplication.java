package edu.duke.ece651.risk.apiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

import static org.springframework.context.annotation.EnableLoadTimeWeaving.AspectJWeaving.ENABLED;

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
