package com.github.fckng0d.imageservice.exception;

public class ImageNotFoundException extends RuntimeException {
    public ImageNotFoundException(Long imageId) {
        super("Image not found by id: " + imageId);
    }

    public ImageNotFoundException(String url) {
        super("Image not found by url: " + url);
    }
}
