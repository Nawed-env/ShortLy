package com.nt.urlshortener.entity;


import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Table(name = "links", indexes = { @Index(columnList = "shortKey", name = "idx_short_key") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Link {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


@Column(nullable = false, unique = true)
private String shortKey;


@Column(nullable = false, columnDefinition = "text")
private String originalUrl;


private String title;

@Builder.Default
private boolean isPublic = true;

@Builder.Default
private Long clickCount = 0L;

@Builder.Default
private Instant createdAt = Instant.now();
}