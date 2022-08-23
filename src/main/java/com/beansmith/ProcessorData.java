package com.beansmith;

import java.util.LinkedHashMap;
import java.util.Map;


public class ProcessorData {

    public enum ProcessorType {
        VALIDATION,
        TRANSFORMATION,
        ENRICHMENT
    }

    public ProcessorData(ProcessorType type) {
        this.type = type;
    }

    ProcessorType type;

    Map<String, InputParameter> inputs = new LinkedHashMap<>();
    private String targetColumnName;

    Map<String, ConstantParameter> constants = new LinkedHashMap<>();

    public String getTargetColumnName() {
        if (type.equals(ProcessorType.ENRICHMENT))
            return getSourceColumnName();
        else
            return targetColumnName;
    }

    public String getSourceColumnName() {
        if (inputs.size() == 0)
            throw new IllegalStateException("Parameter data must have at least one input column");
        return inputs.entrySet().stream().findFirst().get().getValue().getSourceColumnName();
    }

    public ProcessorData setTargetColumnName(String output) {
        this.targetColumnName = output;
        return this;
    }

    public ProcessorData addInputParameter(InputParameter input) {
        inputs.put(input.getName(), input);
        return this;
    }

    public ProcessorData addConstantParameter(ConstantParameter constant) {
        constants.put(constant.getName(), constant);
        return this;
    }

    public ConstantParameter getConstantParameter(String name) {
        return constants.get(name);
    }

    public InputParameter getInputParameter(String name) {
        return inputs.get(name);
    }

    public ProcessorType getType() {
        return type;
    }
}
