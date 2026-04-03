package com.example.myecommerce.services;

import com.example.myecommerce.config.StorageConfig;
import com.example.myecommerce.enums.StorageType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final StorageConfig storageConfig;

    public void saveImage(MultipartFile image, String name, StorageType storageType) throws IOException {
        try{
            Path basePath=getBasePath(storageType);
            //Generamos path completo con fileName
            Path filePath = basePath.resolve(name);
            //Posteamos en repositorio copy(contenido,direccion,Metodo)
            Files.copy(image.getInputStream(),filePath, StandardCopyOption.REPLACE_EXISTING);//Replace Existing permite reemplazar una imagen con uuid ya existente.
        } catch (IOException e) {
            throw new IOException("Error al guardar imagen" + e.getCause());
        }
    }

    //Public ya que puede ser necesario para mostrar imagenes a users
    public Path getBasePath(StorageType storageType){
        Path path;
        switch (storageType){
            case PRODUCT -> path=storageConfig.getProductImagesLocation();
            case PROFILE -> path=storageConfig.getProfileImagesLocation();
            case REPORTS -> path=storageConfig.getReportsLocation();
            default -> throw new RuntimeException("Error STORAGETYPE unavalible");
        }
        return path;
    }
}
