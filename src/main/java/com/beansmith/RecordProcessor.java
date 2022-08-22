package com.beansmith;

import java.util.Map;

@FunctionalInterface
public interface RecordProcessor {
    void process(ValidatableRecord vr, Map<String, ProcessorParameter> params);
}