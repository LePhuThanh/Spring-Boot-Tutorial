package com.tutorial.appdemo.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;

@Service
public class ImageStorageService implements IStorageService{
    private final Path storageFolder = Paths.get("uploads");
    //Constructor
    public ImageStorageService() {
        try {
            Files.createDirectories(storageFolder); // Create a new folder & a necessary super-folder
        }catch (IOException  exception){
            throw new RuntimeException("Cannot initialize storage", exception);

        }
    }
    //To check file is image
    private boolean isImageFile(MultipartFile file) {
        //Let install FileNameUtils
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename()); // Get fileExtension
        return Arrays.asList(new String[] {"png", "jpg", "jpeg", "bmp"}) // Return file image
                .contains(fileExtension.trim().toLowerCase());
    }
    @Override
    public String storeFile(MultipartFile file) {
        try {
            System.out.println("Haha");
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");

            }
            //check file is image ?
            if(!isImageFile(file)) {
                throw new RuntimeException("You can only upload image file");
            }
            //file must be <= 5Mb
            float fileSizeInMegabytes = file.getSize() / 1_000_000_0f;
            if(fileSizeInMegabytes > 5.0f) {
                throw new RuntimeException("File must be < 5Mb");
            }
            //File must rename, why? // Clients load file the same => replace file in server => lost image
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-", ""); // Create a random string not duplicated & remove "-"
            generatedFileName = generatedFileName+"."+fileExtension;
            Path destinationFilePath = this.storageFolder.resolve(
                    Paths.get(generatedFileName))
                    .normalize().toAbsolutePath();
            if(!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())){
                throw new RuntimeException("Cannot store file outside directory.");
            }
            try(InputStream inputStream = file.getInputStream()){
                //Copy file => destinationFilePath // Its type => there are exiting => replace
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            return generatedFileName;

        }catch (IOException  exception){
            throw new RuntimeException("Cannot initialize storage", exception);

        }

    }

    @Override
    public Stream<Path> loadAll() {
        try{
            //list all files in storageFolder
            return Files.walk(this.storageFolder,1) //Load folder & file in storage don't load deep - folder & file
                    .filter(path -> { //red point to debug only check block code
                        return !path.equals(this.storageFolder) && !path.toString().contains("._");
                    })
                    .map(this.storageFolder::relativize);
        }catch (IOException exception) {
            throw new RuntimeException("Failed to load stored files", exception);
        }
    }

    @Override
    public byte[] readFileContent(String fileName) {
       try {
           Path file = storageFolder.resolve(fileName);
           Resource resource = new UrlResource(file.toUri());
           if(resource.exists() || resource.isReadable()) {
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream()); // Copy content of file => byte array
               return bytes;
           }else {
               throw new RuntimeException("Could not read file: " + fileName);
           }
       }catch (IOException exception){
            throw new RuntimeException("Could not read file: " + fileName, exception);
        }
    }

    @Override
    public void deleteAllFiles() {

    }
}
