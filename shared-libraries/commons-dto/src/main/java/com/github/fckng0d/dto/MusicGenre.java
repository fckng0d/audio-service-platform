package com.github.fckng0d.dto;

public enum MusicGenre {
    POP("Pop"),
    ROCK("Rock"),
    HIP_HOP("Hip-Hop"),
    RAP("Rap"),
    RNB("R&B"),
    ELECTRONIC("Electronic"),
    EDM("EDM"),
    HOUSE("House"),
    TECHNO("Techno"),
    TRANCE("Trance"),
    DUBSTEP("Dubstep"),
    DRUM_AND_BASS("Drum and Bass"),
    PHONK("Phonk"),
    HYPERPOP("Hyperpop"),
    INDIE("Indie"),
    ALTERNATIVE("Alternative"),
    METAL("Metal"),
    PUNK("Punk"),
    JAZZ("Jazz"),
    BLUES("Blues"),
    CLASSICAL("Classical"),
    FOLK("Folk"),
    COUNTRY("Country"),
    REGGAE("Reggae"),
    LATIN("Latin"),
    K_POP("K-Pop"),
    J_POP("J-Pop"),
    AMBIENT("Ambient"),
    LOFI("Lo-fi"),
    TRAP("Trap"),
    SOUL("Soul"),
    FUNK("Funk"),
    DISCO("Disco"),
    GOSPEL("Gospel"),
    WORLD("World"),
    EXPERIMENTAL("Experimental");

    private final String displayName;

    MusicGenre(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
