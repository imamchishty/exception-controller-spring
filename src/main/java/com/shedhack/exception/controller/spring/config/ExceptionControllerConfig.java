package com.shedhack.exception.controller.spring.config;

import com.google.gson.Gson;
import com.shedhack.exception.controller.spring.ExceptionController;
import com.shedhack.exception.controller.spring.ExceptionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Provides default configuration of the exception controller.
 * Requires the following props to be available:
 *
 * spring.application.name : no default provided, mandatory
 * api.help.path: if not provided it defaults to a blank string
 *
 * 'exceptionInterceptors' must be the bean name for interceptors list.
 * @author imamchishty
 */
@Configuration
public class ExceptionControllerConfig {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${api.help.path:}")
    private String helpLink;

    @Autowired(required = false)
    @Qualifier(value = "exceptionInterceptors")
    private List<ExceptionInterceptor> helpers;

    @Autowired(required = false)
    private Gson gson;

    @Bean
    public ExceptionController exceptionController() {
        return new ExceptionController(appName, helpLink, helpers, gson);
    }

}
