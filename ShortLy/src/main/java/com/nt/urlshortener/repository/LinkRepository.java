package com.nt.urlshortener.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.urlshortener.entity.Link;


public interface LinkRepository extends JpaRepository<Link, Long> {
Optional<Link> findByShortKey(String shortKey);
boolean existsByShortKey(String shortKey);
}