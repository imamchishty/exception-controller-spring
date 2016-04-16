package com.shedhack.exception.controller.spring;

import com.shedhack.exception.core.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/problem")
    public ResponseEntity<String> problem(){

        throw BusinessException.builder("Something horrible happened").withBusinessCode(BusinessCodes.E100)
                .withParam("user", "imam").build();
    }

}
