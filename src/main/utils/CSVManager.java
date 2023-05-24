package main.utils;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CSVManager {
    private static CSVManager singleton = null;
    private static final char DELIMITER = ';';
    private static final String PATH = "./data/thermalData.csv";
    private static final String PERFORMANCE_PATH = "./data/performanceData.csv";

    private static List<String[]> records = new ArrayList<>();
    private static List<String[]> performanceRecords = new ArrayList<>();

    private CSVManager() {
        CSVParser parser = new CSVParserBuilder().withSeparator(DELIMITER).build();
        CSVReader reader;
        try {
            reader = new CSVReaderBuilder(new FileReader(PATH)).withCSVParser(parser).build();
            records = reader.readAll();
            reader.close();
            reader = new CSVReaderBuilder(new FileReader(PERFORMANCE_PATH)).withCSVParser(parser).build();
            performanceRecords = reader.readAll();
            reader.close();
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    // EFFECTS: creates a CSVManager
    public static CSVManager getInstance() {
        if (singleton == null) {
            singleton = new CSVManager();
        }
        return singleton;
    }

    public List<String> getWindowType(String forSeries) {
        List<String> windowTypes = new ArrayList<>();
        for (String[] record : records.subList(1, records.size())) {
            String series = record[0];
            String window = record[1];
            if (series.equals(forSeries) && !windowTypes.contains(window)) {
                windowTypes.add(window);
            }
        }
        return Collections.unmodifiableList(windowTypes);
    }

    public List<String> getPerformanceType(String forSeries) {
        List<String> performanceTypes = new ArrayList<>();
        for (String[] record : performanceRecords.subList(1, performanceRecords.size())) {
            String series = record[0];
            String window = record[1];
            if (series.equals(forSeries) && !performanceTypes.contains(window)) {
                performanceTypes.add(window);
            }
        }
        return Collections.unmodifiableList(performanceTypes);
    }

    public List<String> getGlassOption(String series, String window) {
        List<String> glassOptions = new ArrayList<>();
        for (String[] record : records.subList(1, records.size())) {
            if (record[0].equals(series) && record[1].equals(window)) {
                glassOptions.add(record[2]);
            }
        }
        return Collections.unmodifiableList(glassOptions);
    }

    public List<Double> getRatings(String series, String window, String glass) {
        List<Double> ratings = new ArrayList<>();
        for (String[] record : records.subList(1, records.size())) {
            if (record[0].equals(series) && record[1].equals(window) && record[2].equals(glass)) {
                ratings.add(Double.valueOf(record[4]));
                ratings.add(Double.valueOf(record[5]));
                ratings.add(Double.valueOf(record[6]));
                ratings.add(Double.valueOf(record[7]));
                break;
            }
        }
        return ratings;
    }

    public String getPerformanceRatings(String series, String window) {
        String performance = "";
        for (String[] record : performanceRecords.subList(1, performanceRecords.size())) {
            if (record[0].equals(series) && record[1].equals(window)) {
                performance += record[2] + "  –  " + record[3] + "\n";
                performance += "Size tested:   " + record[4] + "\n";
                performance += "Positive Design Pressure (DP):   " + record[5] + "\n";
                performance += "Negative Design Pressure (DP):   " + record[6] + "\n";
                performance += "Water Penetration Resistance Test Pressure:   " + record[7] + "\n";
                performance += "Canadian Air Filtration/Exfiltration:   " + record[8] + "\n";
                performance += "Report:   " + record[9];
                break;
            }
        }
        return performance;
    }

    public String getModelCode(String series, String window, String glass) {
        String model = "";
        for (String[] record : records.subList(1, records.size())) {
            if (record[0].equals(series) && record[1].equals(window) && record[2].equals(glass)) {
                model = record[3];
                break;
            }
        }
        return model;
    }

    public String getReport(String series, String window, String glass) {
        String report = "";
        for (String[] record : records.subList(1, records.size())) {
            if (record[0].equals(series) && record[1].equals(window) && record[2].equals(glass)) {
                report = record[8];
                break;
            }
        }
        return report;
    }
}