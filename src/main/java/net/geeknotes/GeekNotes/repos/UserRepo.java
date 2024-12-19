package net.geeknotes.GeekNotes.repos;

import net.geeknotes.GeekNotes.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByUserLogin(String userLogin);
}
