package com.aibizs.einvoice.einvoice_api.controller.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
@Component
public class FileSystemStoreUtil {

    @Value("${einvoice.fs.transactions.dir}")
    private String path;
    @Value("${einvoice.oxalis-home}")
    private String oxalisHome;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void saveInFileSystem(String fileName, Record record) throws IOException {

        Path TRANSACTIONS_DIR = Paths.get(path);

        if (Files.notExists(TRANSACTIONS_DIR)) {
            Files.createDirectories(TRANSACTIONS_DIR);
        }

        Path file = TRANSACTIONS_DIR.resolve(fileName + ".json");

        objectMapper./*writerWithDefaultPrettyPrinter().*/writeValue(file.toFile(), record);
    }


    public String writeAsFile(String fileName, String content) {
        try {
            Path dir = Path.of(oxalisHome);
            Files.createDirectories(dir);
            Path file = dir.resolve(fileName);
//        Path file = dir.resolve("api-" + documentDTO.documentType() + "-" + documentDTO.instanceId() + ".xml");
            Files.writeString(file, content);
            return file.toAbsolutePath().toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
