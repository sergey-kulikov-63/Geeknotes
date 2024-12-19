package net.geeknotes.GeekNotes.controllers;

import net.coobird.thumbnailator.Thumbnails;
import net.geeknotes.GeekNotes.models.User;
import net.geeknotes.GeekNotes.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class UserController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/sign-up")
    public String signUp() {
        return "sign-up";
    }
    @Transactional
    @PostMapping("/sign-up")
    public String signUp(@RequestParam String userName,
                         @RequestParam String userLogin,
                         @RequestParam String userEmail,
                         @RequestParam String userPassword,
                         @RequestParam String submitUserPassword, Model model) {
        if (userRepo.findByUserLogin(userLogin) != null) {
            model.addAttribute("error", "Этот логин занят");
            return "sign-up";
        }
        if (!userPassword.equals(submitUserPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            return "sign-up";
        }
        userRepo.save(new User(userName, userLogin, userEmail, passwordEncoder.encode(userPassword), "USER"));
        return "redirect:/";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/users/{userLogin}")
    public String user(@PathVariable String userLogin, Model model) {
        model.addAttribute("user", userRepo.findByUserLogin(userLogin));
        return "user";
    }
    @GetMapping("/users/{userLogin}/update")
    public String updateUser(@PathVariable String userLogin, Authentication a, Model model) {
        if (a.getName().equals(userLogin)) {
            model.addAttribute("user", userRepo.findByUserLogin(userLogin));
            return "update-user";
        } else
            return "redirect:/";
    }
    @PostMapping("/users/{userLogin}/update")
    public String updateUser(@PathVariable String userLogin,
                             @RequestParam String userName,
                             @RequestParam String userEmail,
                             @RequestParam String userAbout,
                             @RequestParam MultipartFile userAvatar) throws IOException {
        User user = userRepo.findByUserLogin(userLogin);
        if (userAvatar != null && !userAvatar.isEmpty()) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(userAvatar.getInputStream())
                    .size(360, 360)
                    .keepAspectRatio(false)
                    .outputFormat("JPEG")
                    .toOutputStream(outputStream);
            user.setUserAvatar(outputStream.toByteArray());
        }
        user.setUserName(userName);
        user.setUserEmail(userEmail);
        user.setUserAbout(userAbout);
        userRepo.save(user);
        return "redirect:/users/{userLogin}";  // Перенаправление на страницу профиля
    }
}
