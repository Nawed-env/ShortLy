package com.nt.urlshortener.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

import com.nt.urlshortener.dto.CreateLinkRequest;
import com.nt.urlshortener.entity.Link;
import com.nt.urlshortener.service.LinkService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LinkController {

    private final LinkService linkService;

    @Value("${app.base-url:http://localhost:5432}")
    private String baseUrl;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    // ===================== REST API =====================

    @PostMapping("/api/links")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody CreateLinkRequest req) {

        Link saved = linkService.createLink(req.getOriginalUrl(), req.getTitle());

        String shortUrl = baseUrl + "/" + saved.getShortKey();

        return ResponseEntity.ok(
                Map.of(
                        "shortKey", saved.getShortKey(),
                        "shortUrl", shortUrl,
                        "originalUrl", saved.getOriginalUrl()
                )
        );
    }

    @GetMapping("/api/links")
    @ResponseBody
    public List<Link> getAllLinks() {
        return linkService.listAll();
    }

    // ===================== VIEW (HTML) =====================

    @GetMapping("/links")
    public String showLinksPage(Model model) {
        model.addAttribute("baseUrl", baseUrl);
        return "links";     // loads templates/links.html
    }

    // ===================== REDIRECT =====================

    @GetMapping("/{shortKey}")
    public RedirectView redirectToOriginal(
            @PathVariable String shortKey,
            HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        String referer = request.getHeader("Referer");
        String ua = request.getHeader("User-Agent");

        Link link = linkService.handleRedirect(shortKey, ip, referer, ua);

        RedirectView rv = new RedirectView();
        rv.setUrl(link.getOriginalUrl());
        rv.setStatusCode(org.springframework.http.HttpStatus.FOUND);

        return rv;
    }
}
