package com.example.myecommerce.util;

public final class FileUtil {
    private FileUtil(){

    }

    public static String imageType(String contetType){
        switch (contetType) {
            case "image/jpg","image/jpeg" -> {
                return ".jpg";
            }
            case "image/png" -> {
                return ".png";
            }
            default -> {
                throw new IllegalArgumentException("Error, fileType not allowed");
            }
        }
    }
}
