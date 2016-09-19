# exception-controller-spring

[![Build Status](https://travis-ci.org/imamchishty/exception-controller-spring.svg?branch=master "Travis CI")](https://travis-ci.org/imamchishty/exception-controller-spring) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.shedhack.exception/exception-controller-spring/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.shedhack.exception/exception-controller-spring) 

## Introduction
This component is a flexible, generic exception handler for spring applications. This produces a consistent response type for exceptional circumstances.

## Configuration
1. Simply add the following annotation to your configuration class:

`@EnableExceptionController`

2. Add the following properties to you yml/properties file:

`spring.application.name=demo`
`api.help.path=http://link`

### Manually configure the controller:

    @Bean
    public ExceptionController exceptionController() {
        return new ExceptionController(appName, helpLink);
    }

## Exception Interceptor
It is possible to intercept exceptions and to take some action, e.g. send an email, modify a property etc. The interceptor is called after the ExceptionModel is created and before the model + exception are logged.

To create a custom interceptor you simply need to implement:

    public interface ExceptionInterceptor {
        void handle(ExceptionModel model, Exception exception);
    }
   
Once you've created your implementation simply add this to you app config, e.g.

    @Bean
    public TestExceptionInterceptor testExceptionInterceptor() {
        return new TestExceptionInterceptor();
    }
  
This bean will automatically be autowired to the exception controller. An example interceptor is [Request Body Cache Interceptor](https://github.com/imamchishty/requestbody-cache-interceptor). This interceptor is used to attach the request body to the exception model.

## Typical response from the exception controller
    
    HTTP/1.1 400 Bad Request
    Server: Apache-Coyote/1.1
    Set-Cookie: JSESSIONID=0116CD632B554C894FA9898E7E1AE4F7; Path=/; HttpOnly
    Content-Type: application/json;charset=UTF-8
    Exception-Type: ExceptionModel
    Connection: close
    Date: Sat, 16 Apr 2016 09:05:04 GMT
    Exception-Id: 757379ce-940e-43ca-86e4-7b76bb4fa233
    Transfer-Encoding: Identity
    
    {"requestId":"683829211e-940e-43ca-86e4-7b76bb4fa233","exceptionId":"757379ce-940e-43ca-86e4-7b76bb4fa233","httpStatusDescription":"Bad Request","path":"/problem","sessionId":"0116CD632B554C894FA9898E7E1AE4F7","helpLink":"http://link","message":"Something horrible happened","exceptionClass":"com.shedhack.exception.core.BusinessException","applicationName":"demo","metadata":"exception-core-model","httpStatusCode":400,"params":{"user":"imam"},"businessCodes":{"E100":"account locked."},"context":{"thread-name":"http-nio-8080-exec-1"},"exceptionChain":[{"correlationId":"757379ce-940e-43ca-86e4-7b76bb4fa233","message":"Something horrible happened"}],"dateTime":1460797504911}
    
### Exceptions Count
A total count of exceptions can be found via:
    
`int getExceptionCount()`
    
