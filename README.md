# exception-controller-spring
exception-controller-spring


    @Bean
    public ExceptionController exceptionController() {
        return new ExceptionController(appName, helpLink);
    }
    
Produces
    
    HTTP/1.1 400 Bad Request
    Server: Apache-Coyote/1.1
    Set-Cookie: JSESSIONID=0116CD632B554C894FA9898E7E1AE4F7; Path=/; HttpOnly
    Content-Type: application/json;charset=UTF-8
    Exception-Type: ExceptionModel
    Connection: close
    Date: Sat, 16 Apr 2016 09:05:04 GMT
    Exception-Id: 757379ce-940e-43ca-86e4-7b76bb4fa233
    Transfer-Encoding: Identity
    
    {"requestId":null,"exceptionId":"757379ce-940e-43ca-86e4-7b76bb4fa233","httpStatusDescription":"Bad Request","path":"/problem","sessionId":"0116CD632B554C894FA9898E7E1AE4F7","helpLink":"http://link","message":"Something horrible happened","exceptionClass":"com.shedhack.exception.core.BusinessException","applicationName":"demo","metadata":"exception-core-model","httpStatusCode":400,"params":{"user":"imam"},"businessCodes":{"E100":"account locked."},"context":{"thread-name":"http-nio-8080-exec-1"},"exceptionChain":[{"correlationId":"757379ce-940e-43ca-86e4-7b76bb4fa233","message":"Something horrible happened"}],"dateTime":1460797504911}
    
Configuration
    
    @SpringBootApplication
    @EnableExceptionController
    public class Application {
    
        public static void main(String... args) {
            SpringApplication.run(Application.class, args);
        }
    
    }

Properties
    
    spring.application.name=demo
    api.help.path=http://link
    
    
Exceptions Count
    
    int getExceptionCount()
    
    
ExceptionHelper helper
    
    void handle(ExceptionModel model, Exception exception);