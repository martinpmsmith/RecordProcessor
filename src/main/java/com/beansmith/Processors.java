package com.beansmith;


import java.util.HashMap;
import java.util.Map;

public class Processors {

    private static final HashMap<String, RecordProcessor> processors = new HashMap<>();

    public static void build() {

        processors.put("validateInt", Processors::validateInt);
        processors.put("rangeCheck", Processors::rangeCheck);
    }

    public static RecordProcessor getProcessor(String key) {
        if (processors.isEmpty()) build();
        return processors.get(key);
    }

    public static void rangeCheck(ValidatableRecord record, Map<String, ProcessorParameter> parameters) {
        ProcessorParameter param1 = parameters.entrySet().iterator().next().getValue();
        String sourceColumn = param1.getSourceName();
        String targetColumn = param1.getTargetName();
        float lower = parameters.get("lower") == null ? -Float.MAX_VALUE : Float.parseFloat(parameters.get("lower").getValue());
        float upper = parameters.get("upper") == null ? Float.MAX_VALUE : Float.parseFloat(parameters.get("upper").getValue());
        String val = record.get(sourceColumn).toString();
        if (val.isEmpty()) {
            record.put(targetColumn, false);
        } else {
            float valToTest = Float.parseFloat(val);
            boolean result = lower < valToTest && upper > valToTest;
            record.put(targetColumn, result);
        }
//        if the result if false add to audit

    }

    public static void rangeCheckEquals(ValidatableRecord record, Map<String, ProcessorParameter> parameters) {
    }

    public static void validateInt(ValidatableRecord record, Map<String, ProcessorParameter> parameters) {
        ProcessorParameter param = parameters.entrySet().iterator().next().getValue();
        String source = param.getSourceName();
        Object valToTest = record.get(source);
        try {
            int val = Integer.parseInt(valToTest.toString());
            record.put(param.getTargetName(), true);
        } catch (NumberFormatException e) {
            record.put(param.getTargetName(), false);
        }
    }
}
