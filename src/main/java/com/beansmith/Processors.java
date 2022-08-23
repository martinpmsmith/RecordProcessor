package com.beansmith;


import java.util.HashMap;

public class Processors {
    public static final String VALIDATE_FLOAT = "validateFloat";
    private static final HashMap<String, RecordProcessor> processors = new HashMap<>();

    public static void build() {

        processors.put(VALIDATE_FLOAT, Processors::validateFloat);
        processors.put("rangeCheck", Processors::rangeCheck);
        processors.put("validateNotNull", Processors::validateNotNull);
        processors.put("setDefaultIfNull", Processors::defaultIfNull);
    }

    public static RecordProcessor getProcessor(String key) {
        if (processors.isEmpty()) build();
        return processors.get(key);
    }

    public static void rangeCheck(ValidatableRecord record, ProcessorData parameters) {
        String sourceColumn = parameters.getSourceColumnName();
        String targetColumn  = parameters.getTargetColumnName() == null ? sourceColumn : parameters.getTargetColumnName();
        try {
            ConstantParameter low = parameters.getConstantParameter("lower") ;
            float lower = low == null ? -Float.MAX_VALUE : Float.parseFloat(low.getValue().toString());
            ConstantParameter up = parameters.getConstantParameter("upper");
            float upper = up == null ? Float.MAX_VALUE : Float.parseFloat(up.getValue().toString());
            String val = record.get(sourceColumn).toString();
            float valToTest = Float.parseFloat(val);
            boolean result = lower < valToTest && upper > valToTest;
            record.put(targetColumn, result);
        } catch (NumberFormatException nfe)
        {
            record.put(targetColumn, false);
        }
    }

    public static void validateNotNull(ValidatableRecord record, ProcessorData parameters) {
        String source = parameters.getSourceColumnName();
        Object valToTest = record.get(source);
        boolean valid = valToTest == null || valToTest.toString().isEmpty();
        record.put(parameters.getTargetColumnName(), true);

    }

    public static void defaultIfNull(ValidatableRecord record, ProcessorData parameters) {
        String source = parameters.getSourceColumnName();
        Object defaultValue = parameters.getConstantParameter("defaultValue").getValue();
        Object valToTest = record.get(source);
        if(valToTest == null || valToTest.toString().isEmpty()){
            record.put(parameters.getTargetColumnName(), defaultValue);
        }
    }


    public static void validateFloat(ValidatableRecord record, ProcessorData parameters) {
        String source = parameters.getSourceColumnName();
        Object valToTest = record.get(source);
        try {
            float val = Float.parseFloat(valToTest.toString());
            record.put(parameters.getTargetColumnName(), true);
        } catch (NumberFormatException e) {
            record.put(parameters.getTargetColumnName(), false);
        }
    }
}
