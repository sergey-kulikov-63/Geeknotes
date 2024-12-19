package net.geeknotes.GeekNotes.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    private String postTitle;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] postImg;
    private String postDescription;
    @Column(columnDefinition = "TEXT")
    private String postText;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> postTags = new HashSet<>();
    private String postUrl;
    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Comment> postComments = new ArrayList<>();

    public Post() {
    }

    public Post(String postTitle, byte[] postImg, String postDescription,
                String postText, String postUrl) {
        this.postTitle = postTitle;
        this.postImg = postImg;
        this.postDescription = postDescription;
        this.postText = postText;
        this.postUrl = postUrl;
    }
    public boolean hasImage() {
        return postImg != null && postImg.length > 0;
    }
    public String getPostImg() {
        if (hasImage()) {
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(postImg);
        }
        return null;
    }
}