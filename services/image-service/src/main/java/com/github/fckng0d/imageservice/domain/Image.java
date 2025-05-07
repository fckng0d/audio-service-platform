package com.github.fckng0d.imageservice.domain;

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
@Table(name = "users", indexes = {
        @Index(name = "idx_url", columnList = "url")
})
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

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
