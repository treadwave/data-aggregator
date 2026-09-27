package com.data_aggregator.Files;

import java.util.List;

public class FilesWriteService<T> {
    private String filename;
    private int id;

    public FilesWriteService(String filename, int id) {
        this.filename = filename;
        this.id = id;
    }

    public void writeJson(String source, Object data) throws Exception {
        JsonToFile jsonWrite = new JsonToFile(filename, source, id);
        jsonWrite.jsonToFile(data);
    }

    public void writeCsv(List<T> data, Class<T> type) throws Exception {
        CsvToFile<T> writeCsv = new CsvToFile<T>(filename);
        writeCsv.csvToFile(data, type);
    }

}
