package com.github.fckng0d.audioservice.domain;

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
@Table(name = "tracks", indexes = {
        @Index(name = "idx_name", columnList = "name"),
        @Index(name = "idx_album_ids", columnList = "album_id"),

})
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "track_seq_gen")
    @SequenceGenerator(
            name = "track_seq_gen",
            sequenceName = "track_seq",
            allocationSize = 5
    )
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "duration_seconds")
    private Short durationSeconds;

    @CreatedDate
    @Column(name = "release_date", nullable = false, updatable = false)
    private Instant releaseDate;

    @ElementCollection
    @CollectionTable(name = "track_languages", joinColumns = @JoinColumn(name = "track_id"))
    @Column(name = "tracks")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private List<Language> languages = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "track_genres", joinColumns = @JoinColumn(name = "track_id"))
    @Column(name = "genres")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private List<MusicGenre> genres = new ArrayList<>();

    @Column(name = "lyrics")
    private String lyrics;

    @Column(name = "track_url", nullable = false)
    private String trackUrl;

    @Column(name = "cover_image_id")
    private String coverImageUrl;

    @Column(name = "album_id")
    private UUID albumId;


    @ElementCollection
    @CollectionTable(name = "track_musician_nicknames",
            joinColumns = @JoinColumn(name = "track_id"),
            indexes = @Index(name = "idx_track_musician_nickname", columnList = "musician_nicknames")
    )
    @Column(name = "musician_nicknames")
    @Builder.Default
    private List<String> musicianNicknames = new ArrayList<>();

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @Column(name = "is_explicit", nullable = false)
    private Boolean isExplicit;

    @Column(name = "audition_count")
    private Long auditionCount;

    @Column(name = "track_in_favorites_count")
    private Long trackInFavoritesCount;
}
