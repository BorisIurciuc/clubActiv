package gr36.clubActiv.domain.dto;

import java.util.Objects;

public class NewsDto {
    private String title;
    private String description;
    private String createdBy;

    // Конструкторы
    public NewsDto() {}

    public NewsDto(String title, String description, String createdBy) {
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
    }

    // Геттеры и сеттеры
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewsDto newsDto)) return false;
        return Objects.equals(title, newsDto.title) && Objects.equals(description, newsDto.description) && Objects.equals(createdBy, newsDto.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, createdBy);
    }

    @Override
    public String toString() {
        return "NewsDto{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
