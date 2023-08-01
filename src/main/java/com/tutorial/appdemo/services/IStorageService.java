package com.tutorial.appdemo.services;

import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.util.stream.Stream;

 public interface IStorageService {
     String storeFile(MultipartFile file);
     Stream<Path> loadAll(); // Load add file from inside folder
     byte[] readFileContent(String fileName);
     void deleteAllFiles();
}
