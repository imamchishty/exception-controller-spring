package com.shedhack.exception.controller.spring;

import com.google.gson.Gson;
import com.shedhack.exception.core.BusinessException;
import com.shedhack.exception.core.ExceptionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 *     Controller is responsible for handling all exceptions.
 *     In all circumstances the client is responded to with a {@link ExceptionModel}.
 *
 *     {@link ExceptionInterceptor} is called allowing an easy way to access the {@link ExceptionModel}
 *     and the original exception. You simply implement this and make it available in the
 *     Spring Context, if not available then no interceptor will be called.
 * </pre>
 */
@ControllerAdvice
public class ExceptionController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    private final String applicationId, helpLink;

    private Gson gson;

    private static final String THREAD_CONTEXT = "threadName";

    private static final String HEADER_EXCEPTION_TYPE_KEY =  "exceptionType";

    private static final String HEADER_EXCEPTION_TYPE_VAL = "exceptionModel";

    private static final String HEADER_EXCEPTION_ID_KEY = "exceptionId";

    private static final String HEADER_REQUEST_ID_KEY = "requestId";

    private static final String HEADER_GROUP_ID_KEY = "groupId";

    private static AtomicInteger EXCEPTION_COUNT = new AtomicInteger(0);

    private final ExceptionInterceptor interceptor;

    public ExceptionController(String applicationId, String helpLink, ExceptionInterceptor interceptor, Gson gson) {
        this.applicationId = applicationId;
        this.helpLink = helpLink;
        this.interceptor = interceptor;

        // autowired required=false
        if(gson == null) {
            this.gson = new Gson();
        }
        else {
            this.gson = gson;
        }
    }

    /**
     * Handles {@link BusinessException}, the default HTTP code is HttpStatus.BAD_REQUEST
     * @param exception the exception thrown by a business service/controller.
     * @param request the initial HttpServletRequest
     * @return client model contains suitable meta-data for clients to react accordingly.
     */
    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<ExceptionModel> handleServiceException(BusinessException exception, HttpServletRequest request) {

        // Will set the exception ID as well as business codes - IF either are found. Will create new exception ID if missing.

        ExceptionModel exceptionModel =  ExceptionModel.builder(applicationId, exception)
                .withPath(request.getRequestURI())
                .withSessionId(request.getSession().getId())
                .withParams(exception.getParams().isEmpty() ? mapParamsFromRequest(request.getParameterMap()) : exception.getParams())
                .withHttpCode(determineHttpCode(exception), determineHttpDescription(exception))
                .withHelpLink(helpLink).withRequestId(determineRequestId(request)).withContext(THREAD_CONTEXT, determineThreadContext())
                .withGroupId(determinGroupId(request))
                .build();

        log(exceptionModel, exception);
        intercept(exceptionModel, exception);

        return sendResponse(exceptionModel, HttpStatus.BAD_REQUEST);
    }

    /**
     * Default Exception Handler - returns HTTP 500
     * @param exception caught
     * @param request original request
     * @return exception model
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ExceptionModel> handleInvalidRequest(Exception exception, HttpServletRequest request) {

        ExceptionModel exceptionModel =  ExceptionModel.builder(applicationId, exception)
                .withPath(request.getRequestURI())
                .withSessionId(request.getSession().getId())
                .withParams(mapParamsFromRequest(request.getParameterMap()))
                .withHttpCode(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .withHelpLink(helpLink).withRequestId(determineRequestId(request)).withContext(THREAD_CONTEXT, determineThreadContext())
                .withGroupId(determinGroupId(request))
                .build();

        log(exceptionModel, exception);
        intercept(exceptionModel, exception);

        return sendResponse(exceptionModel, HttpStatus.BAD_REQUEST);
    }

    private void intercept(ExceptionModel exceptionModel, Exception exception) {
        if(interceptor !=null) {
            interceptor.handle(exceptionModel, exception);
        }
    }

    /**
     * Creates the response entity and sets the HTTP headers, including HEADER_EXCEPTION_ID_KEY
     * @param model exception wrapper
     * @param status status code
     * @return ExceptionModel
     */
    public ResponseEntity<ExceptionModel> sendResponse(ExceptionModel model, HttpStatus status) {

        MultiValueMap<String, Object> headers = new LinkedMultiValueMap<>();

        headers.add(HEADER_EXCEPTION_TYPE_KEY, HEADER_EXCEPTION_TYPE_VAL);
        headers.add(HEADER_EXCEPTION_ID_KEY, model.getExceptionId());

        incrementExceptionCount();

        return new ResponseEntity(model, headers, status);
    }

    /**
     * Logs to a file (at ERROR level), uses sl4j.
     * @param exceptionModel model
     * @param exception exception
     */
    public void log(ExceptionModel exceptionModel, Exception exception) {
        logger.error(gson.toJson(exceptionModel), exception);
    }

    // --------------
    // Helper methods
    // --------------

    /**
     * Maps from the HTTP Servlet Request Params to a simple Map
     * @param requestParams Map<String, String[]>
     * @return map
     */
    private Map<String, Object> mapParamsFromRequest(Map<String, String[]> requestParams) {

        Map<String, Object> map = new HashMap<>(requestParams.size());

        for (String key : requestParams.keySet()) {
            map.put(key, requestParams.get(key));
        }

        return map;
    }

    /**
     * Determines the status code from the exception
     * @param exception caught exception
     * @return status code
     */
    private int determineHttpCode(BusinessException exception) {

        if(exception.getHttpCode() != null) {
            return HttpStatus.valueOf(exception.getHttpCode()).value();
        }

        // default HTTP Status code
        return HttpStatus.BAD_REQUEST.value();
    }

    /**
     * Based on the status code sets the http status description
     * @param exception caught exception
     * @return description of the status code.
     */
    private String determineHttpDescription(BusinessException exception) {

        if(exception.getHttpCode() != null) {
            return HttpStatus.valueOf(exception.getHttpCode()).getReasonPhrase();
        }

        // default HTTP Status description
        return HttpStatus.BAD_REQUEST.getReasonPhrase();
    }

    /**
     * Searches for request-id header in the original request.
     * @return request id
     */
    public String determineRequestId(HttpServletRequest request) {

        return request.getHeader(HEADER_REQUEST_ID_KEY);
    }

    /**
     * Searches for group-id header in the original request.
     * @return group id
     */
    public String determinGroupId(HttpServletRequest request) {
        return request.getHeader(HEADER_GROUP_ID_KEY);
    }


    /**
     * Returns the current thread name, this is usually a good place to set contextual details.
     * @return thread context
     */
    public String determineThreadContext() {
        return Thread.currentThread().getName();
    }

    /**
     * Count of exceptions caught.
     * @return the current exception count
     */
    public static int getExceptionCount() {
        return EXCEPTION_COUNT.get();
    }

    /**
     * Increments the number of exceptions.
     */
    protected static void incrementExceptionCount() {
        EXCEPTION_COUNT.incrementAndGet();
    }
}