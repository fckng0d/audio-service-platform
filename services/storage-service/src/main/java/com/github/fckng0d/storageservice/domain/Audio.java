package com.github.fckng0d.storageservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "audios", indexes = {
        @Index(name = "idx_url", columnList = "url")
})
public class Audio {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audio_seq_gen")
    @SequenceGenerator(
            name = "audio_gen",
            sequenceName = "audio_seq",
            allocationSize = 5
    )
    private Long id;

    @Column(name = "duration_serconds")
    private Short durationSeconds;

    @Column(name = "url", nullable = false, unique = true)
    private String url;

    @Column(name = "original_file_name", nullable = false)
    private String originalFileName;

    @Column(name = "file_extension", nullable = false)
    private String fileExtension;

    @Column(name = "file_size")
    private Long fileSize;


    @CreatedDate
    @Column(name = "upload_date")
    private Instant uploadDate;
}