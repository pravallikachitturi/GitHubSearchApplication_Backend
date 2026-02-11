package com.example.githubsearch.repository;

import com.example.githubsearch.entity.GitHubRepositoryEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GitHubRepositoryRepositoryTest {

    @Autowired
    private GitHubRepositoryRepository repository;

    @Test
    void shouldSaveRepository() {

        GitHubRepositoryEntity entity = new GitHubRepositoryEntity();
        entity.setId(1L);
        entity.setName("TestRepo");
        entity.setOwner("test-owner");   // REQUIRED
        entity.setDescription("Test description");
        entity.setLanguage("Java");
        entity.setStars(100);
        entity.setForks(50);
        entity.setLastUpdated(OffsetDateTime.now());

        GitHubRepositoryEntity saved = repository.save(entity);

        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(repository.findAll()).hasSize(1);
    }
}
