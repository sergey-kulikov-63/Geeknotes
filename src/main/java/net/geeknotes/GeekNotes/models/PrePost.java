package net.geeknotes.GeekNotes.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Data
@Entity
@Table(name = "pre_posts")
public class PrePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prePostId;
    private String prePostTitle;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] prePostImg;
    private String prePostDescription;
    @Column(columnDefinition = "TEXT")
    private String prePostText;
    private String prePostTags;
    private String prePostUrl;

    public PrePost() {
    }

    public PrePost(String prePostTitle, byte[] prePostImg, String prePostDescription,
                   String prePostText, String prePostTags, String prePostUrl) {
        this.prePostTitle = prePostTitle;
        this.prePostImg = prePostImg;
        this.prePostDescription = prePostDescription;
        this.prePostText = prePostText;
        this.prePostTags = prePostTags;
        this.prePostUrl = prePostUrl;
    }
    public boolean hasImage() {
        return prePostImg != null && prePostImg.length > 0;
    }
    public String getPrePostImgBase64() {
        if (hasImage()) {
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(prePostImg);
        }
        return null;
    }
}