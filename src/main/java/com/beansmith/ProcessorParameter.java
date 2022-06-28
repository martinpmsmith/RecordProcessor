package com.beansmith;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProcessorParameter {
    public enum ParameterType {
        INPUT,
        OUTPUT,
        CONSTANT;
    }

    private String sourceName;
    private String targetName;
    private String value;
    private ParameterType type;
}
