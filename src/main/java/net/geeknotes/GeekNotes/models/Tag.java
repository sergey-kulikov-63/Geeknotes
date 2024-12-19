package net.geeknotes.GeekNotes.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String tagName;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "postTags")
    private List<Post> posts;
}