package com.nt.urlshortener.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nt.urlshortener.entity.Click;
import com.nt.urlshortener.entity.Link;
import com.nt.urlshortener.exception.NotFoundException;
import com.nt.urlshortener.repository.ClickRepository;
import com.nt.urlshortener.repository.LinkRepository;
import com.nt.urlshortener.util.ShortKeyGenerator;


@Service
public class LinkService {

	private final LinkRepository linkRepository;
	private final ClickRepository clickRepository;
	private final ShortKeyGenerator keyGenerator;


	@Value("${app.short-key-length:7}")
	private int shortKeyLength;


	public LinkService(LinkRepository linkRepository, ClickRepository clickRepository, ShortKeyGenerator keyGenerator) {
	this.linkRepository = linkRepository;
	this.clickRepository = clickRepository;
	this.keyGenerator = keyGenerator;
	}

	public Link createLink(String originalUrl, String title) {
	// Basic normalize
	if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
	originalUrl = "https://" + originalUrl;
	}


	// Attempt to generate unique key
	String key;
	int maxAttempts = 5;
	int attempts = 0;
	do {
	key = keyGenerator.generate(shortKeyLength);
	attempts++;
	if (attempts > maxAttempts) {
	throw new RuntimeException("Failed to generate unique short key");
	}
	} while (linkRepository.existsByShortKey(key));


	Link link = Link.builder()
	.shortKey(key)
	.originalUrl(originalUrl)
	.title(title)
	.isPublic(true)
	.build();


	return linkRepository.save(link);
	}


	public List<Link> listAll() {
		System.err.println("nawed");
	return linkRepository.findAll();
	}


	@Transactional
	public Link handleRedirect(String shortKey, String ip, String referer, String ua) {
	Link link = linkRepository.findByShortKey(shortKey)
	.orElseThrow(() -> new NotFoundException(ua));


	// increment click count
	link.setClickCount(link.getClickCount() + 1);
	linkRepository.save(link);


	// save click details asynchronously could be improved - for simplicity save now
	Click click = Click.builder()
	.link(link)
	.ipAddress(ip)
	.referer(referer)
	.userAgent(ua)
	.build();
	clickRepository.save(click);


	return link;
	}


	public Optional<Link> findByShortKey(String shortKey) {
	return linkRepository.findByShortKey(shortKey);
	}

}