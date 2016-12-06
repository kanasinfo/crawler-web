package com.ch.common;

public interface BusinessHandler<T> {
    public T handle() throws Exception;
}
