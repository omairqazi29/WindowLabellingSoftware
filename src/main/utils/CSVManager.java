package utils;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import jdk.nashorn.api.scripting.URLReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipInputStream;

// Represents a CSV manager for processing data in .csv files
public class CSVManager {
    private static CSVManager singleton = null;
    private static final char DELIMITER = ';';
    private static String THERMAL_URL =
            "https://raw.githubusercontent.com/omairqazi29/WindowLabellingSoftware/main/data/thermalData.csv";
    private static final String PERFORMANCE_URL =
            "https://raw.githubusercontent.com/omairqazi29/WindowLabellingSoftware/main/data/performanceData.csv";
    private static final String NRCAN_URL =
            "https://oee.nrcan.gc.ca/pml-lmp/index.cfm?action=app.download-telecharger&appliance=WINDOWS";

    private static List<String[]> records = new ArrayList<>();
    private static List<String[]> performanceRecords = new ArrayList<>();
    private static List<String[]> nrCanRecords = new ArrayList<>();

    // EFFECTS: constructs a CSVManager and saves the thermal, performance and NRCan records
    private CSVManager() {
        CSVParser parser = new CSVParserBuilder().withSeparator(DELIMITER).build();
        CSVReader reader;
        try {
            reader = new CSVReaderBuilder(new URLReader(new URL(THERMAL_URL))).withCSVParser(parser).build();
            records = reader.readAll();
            reader.close();
            reader = new CSVReaderBuilder(new URLReader(new URL(PERFORMANCE_URL))).withCSVParser(parser).build();
            performanceRecords = reader.readAll();
            reader.close();

            ZipInputStream nrZip = new ZipInputStream(new URL(NRCAN_URL).openConnection().getInputStream());
            nrZip.getNextEntry();
            parser = new CSVParserBuilder().withSeparator(',').build();
            reader = new CSVReaderBuilder(new InputStreamReader(nrZip)).withCSVParser(parser).build();
            nrCanRecords = reader.readAll();
            reader.close();
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    // EFFECTS: tries to construct a CSVManager with false csv url
    public static void testCSVManager() {
        THERMAL_URL = "falseURL";
        new CSVManager();
    }

    // MODIFIES: this
    // EFFECTS: returns the instance of CSVManager, or constructs a CSVManager if null and returns it
    public static CSVManager getInstance() {
        if (singleton == null) {
            singleton = new CSVManager();
        }
        return singleton;
    }

    public List<String[]> getRecords() {
        return records;
    }

    public List<String[]> getPerformanceRecords() {
        return performanceRecords;
    }

    public List<String[]> getNrCanRecords() {
        return nrCanRecords;
    }

    // EFFECTS: returns a list of window types for the given series
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

    // EFFECTS: returns a list of performance window types for the given series
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

    // EFFECTS: returns a list of glass options for the given series and window type
    public List<String> getGlassOption(String series, String window) {
        List<String> glassOptions = new ArrayList<>();
        for (String[] record : records.subList(1, records.size())) {
            if (record[0].equals(series) && record[1].equals(window)) {
                glassOptions.add(record[2]);
            }
        }
        return Collections.unmodifiableList(glassOptions);
    }

    // EFFECTS: returns a list of thermal ratings for the given series, window type and glass option
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

    // EFFECTS: returns the performance information for the given series and window type
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

    // EFFECTS: returns the model code for the given series, window type and glass option
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

    // EFFECTS: returns the test report information for the given series, window type and glass option
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

    // EFFECTS: returns true if given model is listed with NRCan, false otherwise
    public boolean isNRCan(String model) {
        boolean nrCan = false;
        String code = model.substring(model.indexOf('-')+1).toLowerCase();
        for (String[] record : nrCanRecords.subList(1, nrCanRecords.size())) {
            if (record[6].toLowerCase().contains(code)) {
                nrCan = true;
                break;
            }
        }
        return nrCan;
    }
}