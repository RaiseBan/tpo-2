package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.example.functions.system.SystemFunction;
import org.example.util.CsvExporter;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class CsvExportTest {
    private static final String FILENAME = "test_results.csv";

    @AfterEach
    public void cleanup() {
        new File(FILENAME).delete();
    }

    @Test
    public void testCsvFileCreation() throws IOException {
        SystemFunction function = new SystemFunction(1e-6, 100);
        CsvExporter exporter = new CsvExporter();
        exporter.exportToCsv(function, -1, 1, 0.5, 1e-6, FILENAME);
//        System.out.println(Paths.get(FILENAME));
        assertTrue(Files.exists(Paths.get(FILENAME)));
    }

    @Test
    public void testCsvContent() throws IOException {
        SystemFunction function = new SystemFunction(1e-6, 100);
        CsvExporter exporter = new CsvExporter();
        exporter.exportToCsv(function, 0.5, 0.5, 0.1, 1e-6, FILENAME);
        String content = Files.readString(Paths.get(FILENAME));
        assertTrue(content.contains("0.5,"));
    }
} 