package com.beansmith;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        ProcessorData rangeData = new ProcessorData(ProcessorData.ProcessorType.VALIDATION)
            .addInputParameter(new InputParameter("year", "LatestPopulationCensus"))
            .addConstantParameter(new ConstantParameter("lower", "1970"))
            .addConstantParameter(new ConstantParameter("upper", "2010"))
            .setTargetColumnName("YearRange_valid");

        ProcessorData intCheck = new ProcessorData(ProcessorData.ProcessorType.VALIDATION)
            .addInputParameter(new InputParameter("Year", "LatestPopulationCensus"))
            .setTargetColumnName("validateFloat_valid");

        ProcessorData defaultNull = new ProcessorData(ProcessorData.ProcessorType.ENRICHMENT)
            .addInputParameter(new InputParameter("p1", "LatestPopulationCensus"))
            .addConstantParameter(new ConstantParameter("defaultValue", "1066"));

        ProcessorData latestHouseholdDefault = new ProcessorData(ProcessorData.ProcessorType.ENRICHMENT)
            .addInputParameter(new InputParameter("p1", "LatestHouseholdSurvey"))
            .addConstantParameter(new ConstantParameter("defaultValue", "1234"));

        System.out.println("Hello world!");
        String filename = "data/Country_small.csv";

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
                    Processors.getProcessor("rangeCheck").process(data, rangeData );
                    Processors.getProcessor(Processors.VALIDATE_FLOAT).process(data, intCheck );
                    Processors.getProcessor("setDefaultIfNull").process(data, defaultNull );
                    Processors.getProcessor("setDefaultIfNull").process(data, latestHouseholdDefault );

                    validatableRecords.add(data);
                }
            }
            validatableRecords.subList(0, 45).forEach(item -> System.out.println("\n\n" + item.toString()));
        } catch (IOException e) {
            System.out.println("File not found");
        }

    }


}
