package com.example.myecommerce.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class StorageConfig {

    @Value("${app.storage.products-path}")
    private String productImagesPath;

    @Value("${app.storage.profiles-path}")
    private String profileImagesPath;

    @Value("${app.storage.reports-path}")
    private String reportsPath;

    @Getter
    private Path productImagesLocation;
    @Getter
    private Path profileImagesLocation;
    @Getter
    private Path reportsLocation;

    @PostConstruct
    public void init(){
        this.productImagesLocation= Paths.get(productImagesPath).toAbsolutePath();
        this.profileImagesLocation=Paths.get(profileImagesPath).toAbsolutePath();
        this.reportsLocation=Paths.get(reportsPath).toAbsolutePath();
        createDirectoryIfNotExist();
    }

    public void createDirectoryIfNotExist(){
        try{
            if(!Files.exists(productImagesLocation)){
                Files.createDirectories(productImagesLocation);
            }
            if (!Files.exists(profileImagesLocation)) {
                Files.createDirectories(profileImagesLocation);
            }
            if(!Files.exists(reportsLocation)) {
                Files.createDirectories(reportsLocation);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error, directories not generated because" + e.getMessage());
        }
    }

}
