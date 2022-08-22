package com.beansmith;

import lombok.Getter;

@Getter
public class ProcessorParameter {
    public ProcessorParameter(String name) {
        this.name = name;
    }

    protected String name;
}
