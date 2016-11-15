package com.shedhack.exception.controller.spring.threadcontext;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Rather than creating a dependency to thread-context-hander.jar/com/shedhack/thread/context/model.ThreadContextModel
 * I'm duplicating as this will not change.
 */
public class ThreadContextModel {

    // ----------------------
    // Constructor and fields
    // ----------------------
    public ThreadContextModel()
    {

    }

    private String id;

    private Date timestamp;

    private String methodName;

    private Map<String, Object> params = new HashMap<>();

    private Map<String, Object> context = new HashMap<>();

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    public void addContext(String key, Object context) {
        this.context.put(key, context);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void addParam(String key, Object value) {
        this.params.put(key, value);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThreadContextModel that = (ThreadContextModel) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;
        if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) return false;
        if (params != null ? !params.equals(that.params) : that.params != null) return false;
        return context != null ? context.equals(that.context) : that.context == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
        result = 31 * result + (params != null ? params.hashCode() : 0);
        result = 31 * result + (context != null ? context.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"ThreadContextModel\":{"
                + "\"id\":\"" + id + "\""
                + ", \"timestamp\":" + timestamp
                + ", \"methodName\":\"" + methodName + "\""
                + ", \"params\":" + params
                + ", \"context\":" + context
                + "}}";
    }
}