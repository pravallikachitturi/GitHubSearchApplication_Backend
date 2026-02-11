package com.example.githubsearch.dto;

import java.util.List;

public class GitHubApiResponse {

    private List<RepositoryItem> items;

    public List<RepositoryItem> getItems() { return items; }
    public void setItems(List<RepositoryItem> items) { this.items = items; }

    public static class RepositoryItem {

        private Long id;
        private String name;
        private String description;
        private String language;
        private Integer stargazers_count;
        private Integer forks_count;
        private String updated_at;
        private Owner owner;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }

        public Integer getStargazers_count() { return stargazers_count; }
        public void setStargazers_count(Integer stargazers_count) { this.stargazers_count = stargazers_count; }

        public Integer getForks_count() { return forks_count; }
        public void setForks_count(Integer forks_count) { this.forks_count = forks_count; }

        public String getUpdated_at() { return updated_at; }
        public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }

        public Owner getOwner() { return owner; }
        public void setOwner(Owner owner) { this.owner = owner; }
    }

    public static class Owner {
        private String login;

        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
    }
}
