package gr36.clubActiv.services;

import gr36.clubActiv.domain.entity.News;
import gr36.clubActiv.repository.NewsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NewsServiceImplTest {

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private NewsServiceImpl newsService;

    private News news;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        news = new News();
        news.setId(1L);
        news.setTitle("Test News");
        news.setDescription("Test Description");
    }

    @Test
    void testCreateNews() {
        when(newsRepository.save(news)).thenReturn(news);
        News createdNews = newsService.create(news);
        assertNotNull(createdNews);
        assertEquals(news.getId(), createdNews.getId());
        verify(newsRepository, times(1)).save(news);
    }

    @Test
    void testFindNewsById() {
        when(newsRepository.findById(1L)).thenReturn(Optional.of(news));
        News foundNews = newsService.findById(1L).orElse(null);
        assertNotNull(foundNews);
        assertEquals(news.getTitle(), foundNews.getTitle());
        verify(newsRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateNews() {
        when(newsRepository.findById(1L)).thenReturn(Optional.of(news));
        news.setTitle("Updated Title");
        when(newsRepository.save(news)).thenReturn(news);

        News updatedNews = newsService.update(1L, news);
        assertNotNull(updatedNews);
        assertEquals("Updated Title", updatedNews.getTitle());
        verify(newsRepository, times(1)).save(news);
    }

    @Test
    void testDeleteNews() {
        when(newsRepository.findById(1L)).thenReturn(Optional.of(news));
        doNothing().when(newsRepository).delete(news);

        newsService.delete(1L);
        verify(newsRepository, times(1)).delete(news);
    }

    @Test
    void testFindById_NewsNotFound() {
        when(newsRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            newsService.findById(1L).orElseThrow(() -> new RuntimeException("News not found"));
        });
        assertEquals("News not found", exception.getMessage());
    }
}
