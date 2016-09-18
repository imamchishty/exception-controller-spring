package com.shedhack.exception.controller.spring;


import com.shedhack.exception.core.ExceptionModel;
import org.springframework.stereotype.Component;

@Component
public class TestExceptionInterceptor implements ExceptionInterceptor {

    @Override
    public void handle(ExceptionModel model, Exception exception) {
        model.getParams().put("hello", "world");
    }
}
