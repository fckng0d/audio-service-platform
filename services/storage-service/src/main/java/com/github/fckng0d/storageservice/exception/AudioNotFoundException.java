package com.github.fckng0d.storageservice.exception;

public class AudioNotFoundException extends RuntimeException {
    public AudioNotFoundException(long audioId) {
        super("Audio not found by id: " + audioId);
    }

    public AudioNotFoundException(String url) {
        super("Audio not found by url: " + url);
    }
}
