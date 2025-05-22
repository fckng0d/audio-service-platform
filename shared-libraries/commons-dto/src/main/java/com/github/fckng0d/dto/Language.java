package com.github.fckng0d.dto;

import lombok.Getter;

@Getter
public enum Language {
    ENGLISH("English"),
    RUSSIAN("Russian"),
    UKRAINIAN("Ukrainian"),
    SPANISH("Spanish"),
    FRENCH("French"),
    GERMAN("German"),
    CHINESE("Chinese"),
    JAPANESE("Japanese"),
    KOREAN("Korean"),
    ITALIAN("Italian"),
    PORTUGUESE("Portuguese"),
    ARABIC("Arabic"),
    TURKISH("Turkish"),
    POLISH("Polish"),
    SWEDISH("Swedish"),
    NORWEGIAN("Norwegian"),
    FINNISH("Finnish"),
    GREEK("Greek");

    private final String displayName;

    Language(String displayName) {
        this.displayName = displayName;
    }
}
