package com.github.fckng0d.albumservice.domain;

import com.github.fckng0d.dto.Language;
import com.github.fckng0d.dto.MusicGenre;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "albums", indexes = {
        @Index(name = "idx_name", columnList = "name")
})
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(name = "album_languages", joinColumns = @JoinColumn(name = "album_id"))
    @Column(name = "language")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private List<Language> languages = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "album_genres", joinColumns = @JoinColumn(name = "album_id"))
    @Column(name = "genres")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private List<MusicGenre> genres = new ArrayList<>();

    @CreatedDate
    @Column(name = "release_date", nullable = false, updatable = false)
    private Instant releaseDate;

    @Column(name = "is_explicit", nullable = false)
    private Boolean isExplicit;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked;

    @Column(name = "total_duration_seconds", nullable = false)
    private Integer totalDurationSeconds;

    @Column(name = "audition_count")
    private Long auditionCount;

    @Column(name = "album_in_favorites_count")
    private Long albumInFavoritesCount;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @ElementCollection
    @CollectionTable(name = "album_track_ids", joinColumns = @JoinColumn(name = "album_id"))
    @Column(name = "track_ids")
    @Builder.Default
    private List<Long> trackIds = new ArrayList<>();

    /**
     * Исполнители (если принимают участие во всех треках, т.е. во всем альбоме)
     */
    @ElementCollection
    @CollectionTable(
            name = "album_musician_nicknames",
            joinColumns = @JoinColumn(name = "album_id"),
            indexes = @Index(name = "idx_album_musician_nickname", columnList = "musician_nicknames")
    )
    @Column(name = "musician_nicknames")
    @Builder.Default
    private List<String> musicianNicknames = new ArrayList<>();

    /**
     * Исполнители (если принимают участие в каком-либо треке, но не во всем альбоме)
     * нужно в будущем для одобрения заявки на участие
     */
    @ElementCollection
    @CollectionTable(name = "album_track_guest_nicknames",
            joinColumns = @JoinColumn(name = "album_id"),
            indexes = @Index(name = "idx_album_track_guest_nickname", columnList = "track_guest_nicknames"))
    @Column(name = "track_guest_nicknames")
    @Builder.Default
    private List<String> trackGuestNicknames = new ArrayList<>();

}
