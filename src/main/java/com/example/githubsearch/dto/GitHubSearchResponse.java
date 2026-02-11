package com.example.githubsearch.dto;

import com.example.githubsearch.entity.GitHubRepositoryEntity;
import java.util.List;

public class GitHubSearchResponse {

    private String message;
    private List<GitHubRepositoryEntity> repositories;
    private long totalCount;

    public GitHubSearchResponse(String message,
                                 List<GitHubRepositoryEntity> repositories,
                                 long totalCount) {
        this.message = message;
        this.repositories = repositories;
        this.totalCount = totalCount;
    }

    public String getMessage() { return message; }
    public List<GitHubRepositoryEntity> getRepositories() { return repositories; }
    public long getTotalCount() { return totalCount; }
}
