package com.beansmith;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Map<String, ProcessorParameter> rangeParam = new LinkedHashMap<>();
        ProcessorParameter lower = new ProcessorParameter("Year",
                "YearRange_valid", "1970", ProcessorParameter.ParameterType.CONSTANT);
        ProcessorParameter upper = new ProcessorParameter("Year",
                "YearRange_valid", "2010", ProcessorParameter.ParameterType.CONSTANT);
        rangeParam.put("lower", lower);
        rangeParam.put("upper", upper);

        Map<String, ProcessorParameter> intParam = new LinkedHashMap<>();
        ProcessorParameter pp = new ProcessorParameter("Year",
                "Year_valid", "", ProcessorParameter.ParameterType.INPUT);
        intParam.put("p1", pp);

        System.out.println("Hello world!");
        String filename = "data/Country_small.csv";
        HashMap<String, RecordProcessor> map = new HashMap<>();

        try {
            Reader reader = Files.newBufferedReader(Paths.get(filename));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            List<ValidatableRecord> validatableRecords = new ArrayList<>();
            CSVRecord header = null;
            for (CSVRecord record : csvParser) {
                long recno = record.getRecordNumber();
                if (recno % 100000 == 0) System.out.println("Record no" + recno);
                if (record.getRecordNumber() == 1) {
                    header = record;
                } else {
                    ValidatableRecord data = new ValidatableRecord();
                    for (int i = 0; i < (header == null ? 0 : header.size()); i++) {
                        data.put(header.get(i), record.get(i));
                    }
                    Processors.getProcessor("rangeCheck").process(data, rangeParam);
                    Processors.getProcessor("validateInt").process(data, intParam);
                    validatableRecords.add(data);
                }
            }
            validatableRecords.subList(0, 45).forEach(item -> System.out.println(item.toString()));
//            validatableRecords.subList(400000, 400495).forEach(item -> System.out.println(item.toString()));


        } catch (IOException e) {
            System.out.println("File not found");
        }

    }


}
