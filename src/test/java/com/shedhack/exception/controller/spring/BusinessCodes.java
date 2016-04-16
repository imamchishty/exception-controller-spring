package com.shedhack.exception.controller.spring;

import com.shedhack.exception.core.BusinessCode;

/**
 * Created by imamchishty on 16/04/2016.
 */
public enum BusinessCodes implements BusinessCode{

    E100("account locked.");

    private final String getDescription;

    BusinessCodes(String s) {
        this.getDescription = s;
    }

    public String getCode() {
        return this.name();
    }

    public String getDescription() {
        return this.getDescription;
    }
}
