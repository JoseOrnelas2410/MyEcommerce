package com.example.myecommerce.config;

import com.example.myecommerce.enums.StorageType;
import com.example.myecommerce.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
@Configuration
@RequiredArgsConstructor
//Clase necesaria para servir images de mi local file system
public class WebConfig implements WebMvcConfigurer {

    private final StorageService storageService;

    /*necesarios para al llamar una imagen desde th con
    th:src="@{|/images/products/${product.productImageRoute}${product.extension}|}"
    thymelead cargue en automatico mi producto
    Errores que arroja consola web sin webConfig
    Not allowed to load local resource: file:///D:/GlobalQuark/images/products/17257674-5626-41de-ab6a-ccb55bb96750.jpg
    Failed to load resource: the server responded with a status of 404 ()
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String productsPath = storageService.getBasePath(StorageType.PRODUCT)
                .toUri() //Agrega el file:/// sin problemas de concatenacion
                .toString();

        if(!productsPath.endsWith("/")) productsPath += "/";

        registry.addResourceHandler("/images/products/**")
                .addResourceLocations(productsPath);

        String profilesPath = storageService.getBasePath(StorageType.PROFILE)
                .toUri()
                .toString();
        if(!profilesPath.endsWith("/")) profilesPath += "/";

        registry.addResourceHandler("/images/profile/**")
                        .addResourceLocations(profilesPath);

        System.out.println("DEBUG - Resource Handler: /images/products/**");
        System.out.println("DEBUG - Resource Location: " + productsPath);

        System.out.println("DEBUG - Resource Handler: /images/profiles/**");
        System.out.println("DEBUG - Resource Location: " + profilesPath);
    }
}
