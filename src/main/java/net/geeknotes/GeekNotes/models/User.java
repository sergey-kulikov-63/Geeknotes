package net.geeknotes.GeekNotes.models;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Base64;

@Data
@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String userName;
    private String userLogin;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] userAvatar;
    private String userEmail;
    private String userAbout;
    private String userPassword;
    private String userRole;

    public User() {
    }

    public User(String userName, String userLogin,
                String userEmail, String userPassword, String userRole) {
        this.userName = userName;
        this.userLogin = userLogin;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userRole = userRole;
    }
    public boolean hasImage() {
        return userAvatar != null && userAvatar.length > 0;
    }
    public String getUserAvatar() {
        if (hasImage()) {
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(userAvatar);
        }
        return null;
    }
}