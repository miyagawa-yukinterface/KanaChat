package net.ironingot.kanachat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class KanaChatDictionary {
    private static final String FILE_NAME = "dictionary.xlsx";
    private static final String SHEET_NAME = "dictionary";
    private static final String WORD_HEADER = "word";
    private static final String READING_HEADER = "reading";

    private final JavaPlugin plugin;
    private final Path file;
    private final Map<String, List<String>> entries = new LinkedHashMap<String, List<String>>();

    public KanaChatDictionary(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = plugin.getDataFolder().toPath().resolve(FILE_NAME);
        load();
        migrateLegacyDictionary();
    }

    public synchronized Map<String, String> getEntries() {
        Map<String, String> result = new LinkedHashMap<String, String>();
        for (Map.Entry<String, List<String>> entry : entries.entrySet()) {
            result.put(entry.getKey(), String.join(", ", entry.getValue()));
        }
        return result;
    }

    public synchronized Map<String, String> getValues() {
        Map<String, String> result = new LinkedHashMap<String, String>();
        for (Map.Entry<String, List<String>> entry : entries.entrySet()) {
            for (String reading : entry.getValue()) {
                result.put(reading.toLowerCase(), entry.getKey());
            }
        }
        return result;
    }

    public synchronized List<String> getWords() {
        return new ArrayList<String>(entries.keySet());
    }

    public synchronized boolean remove(String word) {
        if (entries.remove(word) == null) {
            return false;
        }
        save();
        return true;
    }

    public synchronized void set(String word, String[] readings) {
        entries.put(word, new ArrayList<String>(Arrays.asList(readings)));
        save();
    }

    private void load() {
        if (!Files.exists(file)) {
            save();
            return;
        }

        try (InputStream input = Files.newInputStream(file);
             Workbook workbook = new XSSFWorkbook(input)) {
            Sheet sheet = workbook.getSheet(SHEET_NAME);
            if (sheet == null) {
                return;
            }
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }
                String word = getCellValue(row.getCell(0));
                String reading = getCellValue(row.getCell(1));
                if (!word.isEmpty() && !reading.isEmpty()) {
                    List<String> readings = entries.get(word);
                    if (readings == null) {
                        readings = new ArrayList<String>();
                        entries.put(word, readings);
                    }
                    readings.add(reading);
                }
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load " + file, exception);
        }
    }

    private void save() {
        try {
            Files.createDirectories(file.getParent());
            try (Workbook workbook = new XSSFWorkbook();
                 OutputStream output = Files.newOutputStream(file)) {
                Sheet sheet = workbook.createSheet(SHEET_NAME);
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue(WORD_HEADER);
                header.createCell(1).setCellValue(READING_HEADER);

                int rowIndex = 1;
                for (Map.Entry<String, List<String>> entry : entries.entrySet()) {
                    for (String reading : entry.getValue()) {
                        Row row = sheet.createRow(rowIndex++);
                        row.createCell(0).setCellValue(entry.getKey());
                        row.createCell(1).setCellValue(reading);
                    }
                }
                workbook.write(output);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to save " + file, exception);
        }
    }

    private void migrateLegacyDictionary() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("dictionary");
        if (section == null || section.getKeys(false).isEmpty()) {
            return;
        }

        for (String word : section.getKeys(false)) {
            List<String> readings = section.getStringList(word);
            if (!readings.isEmpty() && !entries.containsKey(word)) {
                entries.put(word, new ArrayList<String>(readings));
            }
        }
        save();
        plugin.getConfig().set("dictionary", null);
        plugin.saveConfig();
    }

    private String getCellValue(Cell cell) {
        return cell == null ? "" : cell.toString().trim();
    }
}
