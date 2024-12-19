package net.geeknotes.GeekNotes.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String content;

    public Comment() {
    }

    public Comment(Post post, User user, String content) {
        this.post = post;
        this.user = user;
        this.content = content;
    }
}
