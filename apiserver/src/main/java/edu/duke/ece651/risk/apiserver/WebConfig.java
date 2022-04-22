package edu.duke.ece651.risk.apiserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    //    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/chat/**")
//                .allowCredentials(true)
//                .allowedHeaders("*")
//                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE", "PATCH")
//                .allowedOrigins("*");
//    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }
}
