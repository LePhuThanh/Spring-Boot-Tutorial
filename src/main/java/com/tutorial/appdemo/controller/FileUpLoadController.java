package com.tutorial.appdemo.controller;


import com.tutorial.appdemo.models.ResponseObject;
import com.tutorial.appdemo.services.IStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;


import java.util.List;
import java.util.stream.Collectors;

@Controller //Return view for clients
@RequestMapping(path = "/api/v1/FileUpLoad")
public class FileUpLoadController {
    //This is controller receive picture from client
    //Inject service = create object (only 1 time) which implements IStorageService interface
    //Inject Storage Service here
    @Autowired
    private IStorageService storageService;

    @PostMapping("")
    public ResponseEntity<ResponseObject> uploadFile(@RequestParam("file")MultipartFile file) {
        try {
            //save file to folder => use a service
            String generatedFileName = storageService.storeFile(file);
            return  ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Upload file successfully", generatedFileName)
            );
            // 2ecaf5ceedc9488fb1a448bddc37388c.jpg => how to open this file in web browser ?
        }catch (Exception exception) {
            return  ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Ok", exception.getMessage(), ""));
        }
    }

    //get image's url
    @GetMapping("/files/{fileName:.+}")
    // files/2ecaf5ceedc9488fb1a448bddc37388c.jpg
    public ResponseEntity<byte[]> readDetailFile(@PathVariable String fileName) {
        try {
            byte[] bytes = storageService.readFileContent(fileName);
            return ResponseEntity
                    .ok() // = status(HttpStatus.OK)
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(bytes);

        }catch (Exception exception){
            return ResponseEntity.noContent().build();
        }
    }
    // How to load all uploaded file ?
    @GetMapping("")
    public ResponseEntity<ResponseObject> getUploadedFiles() {
        try {
            List<String> urls = storageService.loadAll()
                    .map(path -> {
                        //convert fileName to url(send request "readDetailFile")
                        String urlPath = MvcUriComponentsBuilder
                                .fromMethodName(FileUpLoadController.class, "readDetailFile", path.getFileName().toString())
                                .build().toUri().toString();
                        return urlPath;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ResponseObject("Ok", "List files successfully",urls));
        }catch (Exception exception) {
            return ResponseEntity.ok(new
                    ResponseObject("Failed", "List files failed", new String[] {}));
        }
    }
}
