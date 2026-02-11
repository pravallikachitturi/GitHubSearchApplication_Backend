package com.example.githubsearch.controller;

import com.example.githubsearch.dto.GitHubRepoRequest;
import com.example.githubsearch.dto.GitHubSearchRequest;
import com.example.githubsearch.dto.GitHubSearchResponse;
import com.example.githubsearch.entity.GitHubRepositoryEntity;
import com.example.githubsearch.service.GitHubRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/github")
public class GitHubRepositoryController {

    @Autowired
    private GitHubRepositoryService service;

    // ==============================
    // SEARCH GITHUB + SAVE
    // ==============================
    @PostMapping("/search")
    public ResponseEntity<GitHubSearchResponse> searchRepositories(
            @RequestBody GitHubSearchRequest request) {

        return ResponseEntity.ok(service.searchAndSave(request));
    }


    // ==============================
    // FETCH STORED REPOSITORIES
    // ==============================
    @GetMapping("/repositories")
    public ResponseEntity<Page<GitHubRepositoryEntity>> getRepositories(
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer minStars,
            @RequestParam(defaultValue = "stars") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                service.getRepositories(language, minStars, sort, page, size)
        );
    }


    // ==============================
    // MANUAL INSERT (TESTING)
    // ==============================
    @PostMapping("/add")
    public ResponseEntity<GitHubRepositoryEntity> addRepository(
            @RequestBody GitHubRepoRequest request) {

        return ResponseEntity.ok(service.saveRepository(request));
    }
}
