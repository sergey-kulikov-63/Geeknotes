package net.geeknotes.GeekNotes.repos;

import net.geeknotes.GeekNotes.models.PrePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrePostRepo extends JpaRepository<PrePost, Long> {
    PrePost findByPrePostUrl(String prePostUrl);
}
