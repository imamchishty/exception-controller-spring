package com.shedhack.exception.controller.spring.config;

import com.shedhack.exception.controller.spring.ExceptionController;
import com.shedhack.exception.controller.spring.ExceptionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Provides default configuration of the exception controller.
 * Requires the following props to be available:
 *
 * spring.application.name : no default provided, mandatory
 * api.help.path: if not provided it defaults to a blank string
 *
 * @author imamchishty
 */
@Configuration
public class ExceptionControllerConfig {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${api.help.path:}")
    private String helpLink;

    @Autowired(required = false)
    private ExceptionInterceptor helper;

    @Bean
    public ExceptionController exceptionController() {
        return new ExceptionController(appName, helpLink, helper);
    }

}
