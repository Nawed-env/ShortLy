package com.nt.urlshortener.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.urlshortener.entity.Click;


public interface ClickRepository extends JpaRepository<Click, Long> {
}