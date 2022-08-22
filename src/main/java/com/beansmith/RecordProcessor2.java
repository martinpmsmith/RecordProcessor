package com.beansmith;

@FunctionalInterface
public interface RecordProcessor2 {
    void process(ValidatableRecord vr, ProcessorData params);
}