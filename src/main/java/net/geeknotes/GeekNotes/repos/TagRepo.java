package net.geeknotes.GeekNotes.repos;

import net.geeknotes.GeekNotes.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepo extends JpaRepository<Tag, Integer> {
    Tag findByTagName(String tagName);
}
