package com.example.githubsearch.service;

import com.example.githubsearch.dto.GitHubSearchRequest;
import com.example.githubsearch.dto.GitHubApiResponse;
import com.example.githubsearch.entity.GitHubRepositoryEntity;
import com.example.githubsearch.repository.GitHubRepositoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class GitHubRepositoryServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private GitHubRepositoryRepository repository;

    @InjectMocks
    private GitHubRepositoryService service;

    @Test
    void shouldThrowExceptionWhenResponseIsNull() {

        GitHubSearchRequest request = new GitHubSearchRequest();
        request.setQuery("spring");

        when(webClient.get()).thenReturn(null);

        assertThrows(Exception.class, () -> {
            service.searchAndSave(request);
        });
    }
}
