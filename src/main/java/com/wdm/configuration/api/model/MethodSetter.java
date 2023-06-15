package com.wdm.configuration.api.model;

import java.util.Objects;
@FunctionalInterface
public interface MethodSetter<T> {
    void setValue(T value);
    static<T> void setValue (T value, MethodSetter<T> method ){
        if (Objects.nonNull(value)){
            method.setValue(value);
        }
    }
}

