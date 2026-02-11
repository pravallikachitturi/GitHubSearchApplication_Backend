package com.example.githubsearch.repository;
import java.util.List;

import com.example.githubsearch.entity.GitHubRepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GitHubRepositoryRepository
        extends JpaRepository<GitHubRepositoryEntity, Long> {
	List<GitHubRepositoryEntity> findByLanguageIgnoreCase(String language);

    List<GitHubRepositoryEntity> findByStarsGreaterThanEqual(Integer stars);
}
