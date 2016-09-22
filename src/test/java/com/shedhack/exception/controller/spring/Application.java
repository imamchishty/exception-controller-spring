package com.shedhack.exception.controller.spring;

import com.google.gson.Gson;
import com.shedhack.exception.controller.spring.config.EnableExceptionController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

/**
 * Test Application
 */
@SpringBootApplication
@EnableExceptionController
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }

    @Bean
    public List<ExceptionInterceptor> exceptionInterceptors () {
        return Arrays.asList(new TestExceptionInterceptor());
    }
}
