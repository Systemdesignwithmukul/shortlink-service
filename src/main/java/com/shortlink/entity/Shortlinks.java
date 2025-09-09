package com.shortlink.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Entity class representing a short link mapping
 */
@Entity
@Table(name = "shortlinks",
        uniqueConstraints = {
                @UniqueConstraint(name = "UC_SHORTLINKSORIGINAL_URL_HASH_COL",
                        columnNames = "original_url_hash")
        },
        indexes = {
                @Index(name = "idx_short_code", columnList = "short_code")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Shortlinks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "expiration_date")
    private Instant expirationDate;

    @Column(name = "original_url", length = 2048)
    private String originalUrl;

    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "original_url_hash", length = 64, nullable = false)
    private String originalUrlHash;

    @Size(max = 255)
    @Column(name = "short_code")
    private String shortCode;

    public boolean isExpired() {
        return expirationDate != null && expirationDate.isBefore(Instant.now());
    }


}