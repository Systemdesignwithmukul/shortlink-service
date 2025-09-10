package com.shortlink.repository;

import com.shortlink.entity.Shortlinks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortlinkRepository extends JpaRepository<Shortlinks,Long> {
    Optional<Shortlinks> findById(Long id);

    Optional<Shortlinks> findByOriginalUrlHash(String originalUrlHash);

    Optional<Shortlinks> findByShortCode(String shortCode);
}
