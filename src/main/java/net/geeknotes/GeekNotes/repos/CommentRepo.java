package net.geeknotes.GeekNotes.repos;

import net.geeknotes.GeekNotes.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
}