package com.beansmith;

@FunctionalInterface
public interface RecordProcessor {
    void process(ValidatableRecord vr, ProcessorData params);
}