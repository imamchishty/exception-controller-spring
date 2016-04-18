package com.shedhack.exception.controller.spring;

import com.shedhack.exception.core.ExceptionModel;

/**
 * <pre>
 *     This interface should be implemented if you wish to do something with the handled exception.
 *     The handle method will be called before the exception controller returns the response object
 *     to the client.
 * </pre>
 *
 * @author imamchishty
 */
public interface ExceptionInterceptor {

    void handle(ExceptionModel model, Exception exception);

}
