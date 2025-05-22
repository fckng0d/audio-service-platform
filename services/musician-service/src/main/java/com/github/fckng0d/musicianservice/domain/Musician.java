package com.github.fckng0d.musicianservice.domain;

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
@Table(name = "musicians", indexes = {
        @Index(name = "idx_nickname", columnList = "nickname")
})
public class Musician {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name = "bio")
    private String bio;

    @CreatedDate
    @Column(name = "creation_date", nullable = false, updatable = false)
    private Instant creationDate;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified;

    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked;

    @Column(name = "avatar_image_url")
    private String avatarImageUrl;

    @Column(name = "header_image_url")
    private String headerImageUrl;

    @ElementCollection
    @CollectionTable(name = "musician_album_ids", joinColumns = @JoinColumn(name = "musician_id"))
    @Column(name = "album_ids")
    @Builder.Default
    private List<UUID> albumIds = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "musician_subscriber_ids", joinColumns = @JoinColumn(name = "musician_id"))
    @Column(name = "subscriber_ids")
    @Builder.Default
    private List<UUID> subscriberIds = new ArrayList<>();

    // TODO: many-to-many с полем nickname
//    private List<SocialNetwork> socialNetworks = new ArrayList<>();
}
