package net.geeknotes.GeekNotes.repos;

import net.geeknotes.GeekNotes.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {
    Post findByPostUrl(String postUrl);
    List<Post> findByPostTitleContainingIgnoreCase(String postTitle); // Метод для поиска по названию
}
