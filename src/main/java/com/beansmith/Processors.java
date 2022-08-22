package com.beansmith;


import java.util.HashMap;

public class Processors {

    private static final HashMap<String, RecordProcessor2> processors = new HashMap<>();

    public static void build() {

        processors.put("validateInt", Processors::validateInt);
        processors.put("rangeCheck", Processors::rangeCheck);
        processors.put("validateNotNull", Processors::validateNotNull);
        processors.put("setDefaultIfNull", Processors::defaultIfNull);
    }

    public static RecordProcessor2 getProcessor(String key) {
        if (processors.isEmpty()) build();
        return processors.get(key);
    }

    public static void rangeCheck(ValidatableRecord record, ProcessorData parameters) {
        String sourceColumn = parameters.getSourceColumn().getSourceColumnName();
        String targetColumn  = parameters.getTargetColumn() == null ? sourceColumn : parameters.getTargetColumn();
        ConstantParameter low = parameters.getConstantParameter("lower") ;
        try {
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
        String source = parameters.getSourceColumn().getSourceColumnName();
        Object valToTest = record.get(source);
        boolean valid = valToTest == null || valToTest.toString().isEmpty();
        record.put(parameters.getTargetColumn(), true);

    }

    public static void defaultIfNull(ValidatableRecord record, ProcessorData parameters) {
        String source = parameters.getSourceColumn().getSourceColumnName();
        Object defaultValue = parameters.getConstantParameter("defaultValue").getValue();
        Object valToTest = record.get(source);
        if(valToTest == null || valToTest.toString().isEmpty()){
            record.put(source, defaultValue);
        }
    }


    public static void validateInt(ValidatableRecord record, ProcessorData parameters) {
        InputParameter param = parameters.getSourceColumn();
        String source = param.getSourceColumnName();
        Object valToTest = record.get(source);
        try {
            int val = Integer.parseInt(valToTest.toString());
            record.put(parameters.getTargetColumn(), true);
        } catch (NumberFormatException e) {
            record.put(parameters.getTargetColumn(), false);
        }
    }
}
