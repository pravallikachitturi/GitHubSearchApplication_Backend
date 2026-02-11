package com.example.githubsearch.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;

import com.example.githubsearch.dto.GitHubApiResponse;
import com.example.githubsearch.dto.GitHubRepoRequest;
import com.example.githubsearch.dto.GitHubSearchRequest;
import com.example.githubsearch.dto.GitHubSearchResponse;
import com.example.githubsearch.entity.GitHubRepositoryEntity;
import com.example.githubsearch.exception.GitHubApiException;
import com.example.githubsearch.exception.RepositoryNotFoundException;
import com.example.githubsearch.repository.GitHubRepositoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class GitHubRepositoryService {
	@Value("${github.token:}")
	private String githubToken;


    @Autowired
    private WebClient webClient;

    @Autowired
    private GitHubRepositoryRepository repository;

    // =========================================================
    // SEARCH GITHUB + SAVE TO DB (Structured Response)
    // =========================================================
    public GitHubSearchResponse searchAndSave(GitHubSearchRequest request) {

        StringBuilder queryBuilder = new StringBuilder(request.getQuery());

        if (request.getLanguage() != null && !request.getLanguage().isBlank()) {
            queryBuilder.append("+language:").append(request.getLanguage());
        }

        String finalQuery = queryBuilder.toString();

        GitHubApiResponse response;

        var requestSpec = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/repositories")
                        .queryParam("q", finalQuery)
                        .queryParam("sort",
                                request.getSort() != null ? request.getSort() : "stars")
                        .queryParam("per_page", 20)
                        .build());

        if (githubToken != null && !githubToken.isBlank()) {
            requestSpec = requestSpec.header(
                    HttpHeaders.AUTHORIZATION,
                    "Bearer " + githubToken
            );
        }

        try {
            response = requestSpec
                    .retrieve()
                    .onStatus(status -> status.value() == 403,
                            clientResponse -> Mono.error(
                                    new GitHubApiException(
                                            "GitHub rate limit exceeded. Try again later."
                                    )
                            ))
                    .bodyToMono(GitHubApiResponse.class)
                    .block();
        } catch (Exception e) {
            throw new GitHubApiException(
                    "Failed to fetch repositories from GitHub: " + e.getMessage()
            );
        }

        if (response == null ||
                response.getItems() == null ||
                response.getItems().isEmpty()) {
            throw new RepositoryNotFoundException(
                    "No repositories found for the given search criteria."
            );
        }

        List<GitHubRepositoryEntity> entities = response.getItems()
                .stream()
                .map(item -> {
                    GitHubRepositoryEntity entity = new GitHubRepositoryEntity();
                    entity.setId(item.getId());
                    entity.setName(item.getName());
                    entity.setDescription(item.getDescription());
                    entity.setLanguage(item.getLanguage());
                    entity.setStars(item.getStargazers_count());
                    entity.setForks(item.getForks_count());
                    entity.setOwner(item.getOwner() != null ?
                            item.getOwner().getLogin() : null);
                    entity.setLastUpdated(
                            OffsetDateTime.parse(item.getUpdated_at())
                    );
                    return entity;
                })
                .toList();

        repository.saveAll(entities); // Upsert behavior

        return new GitHubSearchResponse(
                "Repositories fetched and saved successfully",
                entities,
                entities.size()
        );
    }

    // =========================================================
    // FETCH STORED DATA WITH FILTER + SORT + PAGINATION
    // =========================================================
    public Page<GitHubRepositoryEntity> getRepositories(
            String language,
            Integer minStars,
            String sortBy,
            int page,
            int size) {

        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "stars";
        }

        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, sortBy)
        );

        Page<GitHubRepositoryEntity> pageResult =
                repository.findAll(pageRequest);

        List<GitHubRepositoryEntity> filtered = pageResult.getContent()
                .stream()
                .filter(repo ->
                        (language == null ||
                                (repo.getLanguage() != null &&
                                        repo.getLanguage().equalsIgnoreCase(language)))
                                &&
                                (minStars == null ||
                                        (repo.getStars() != null &&
                                                repo.getStars() >= minStars))
                )
                .toList();

        return new PageImpl<>(filtered, pageRequest, filtered.size());
    }

    // =========================================================
    // MANUAL INSERT (FOR TESTING)
    // =========================================================
    public GitHubRepositoryEntity saveRepository(GitHubRepoRequest request) {

        GitHubRepositoryEntity entity = new GitHubRepositoryEntity();

        entity.setId(request.getId());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setOwner(request.getOwner());
        entity.setLanguage(request.getLanguage());
        entity.setStars(request.getStars());
        entity.setForks(request.getForks());
        entity.setLastUpdated(
                OffsetDateTime.parse(request.getLastUpdated())
        );

        return repository.save(entity);
    }
}
