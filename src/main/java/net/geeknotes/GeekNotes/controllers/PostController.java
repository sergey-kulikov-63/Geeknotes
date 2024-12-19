package net.geeknotes.GeekNotes.controllers;

import net.geeknotes.GeekNotes.models.Comment;
import net.geeknotes.GeekNotes.models.Post;
import net.geeknotes.GeekNotes.models.PrePost;
import net.geeknotes.GeekNotes.models.Tag;
import net.geeknotes.GeekNotes.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class PostController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private PrePostRepo prePostRepo;
    @Autowired
    private TagRepo tagRepo;
    @Autowired
    private CommentRepo commentRepo;

    @GetMapping("/")
    public String redirect() {
        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String posts(@RequestParam(value = "query", required = false) String query, Model model) {
        if (query != null && !query.isEmpty()) {
            model.addAttribute("posts", postRepo.findByPostTitleContainingIgnoreCase(query));
        } else {
            model.addAttribute("posts", postRepo.findAll());
        }
        model.addAttribute("titles", postRepo.findAll().stream()
                .map(post -> post.getPostTitle())
                .collect(Collectors.toList()));
        return "posts";
    }

    @GetMapping("/posts/{postUrl}")
    public String post(@PathVariable String postUrl, Model model) {
        model.addAttribute("post", postRepo.findByPostUrl(postUrl));
        return "post";
    }

    @GetMapping("/posts/{postUrl}/update")
    public String updatePost(@PathVariable String postUrl, Model model) {
        model.addAttribute("post", postRepo.findByPostUrl(postUrl));
        return "update-post";
    }

    @Transactional
    @PostMapping("/posts/{postUrl}/update")
    public String updatePost(@PathVariable String postUrl,
                             @RequestParam String postTitle,
                             @RequestParam String postDescription,
                             @RequestParam String postText){
        Post post = postRepo.findByPostUrl(postUrl);
        post.setPostTitle(postTitle);
        post.setPostDescription(postDescription);
        post.setPostText(postText);
        postRepo.save(post);
        return "redirect:/posts/{postUrl}";
    }

    @Transactional
    @PostMapping("/posts/{postUrl}/delete")
    public String deletePost(@PathVariable String postUrl) {
        postRepo.delete(postRepo.findByPostUrl(postUrl));
        return "redirect:/posts";
    }

    @GetMapping("/posts/pre-posts")
    public String prePosts(Model model) {
        model.addAttribute("preposts", prePostRepo.findAll());
        return "pre-posts";
    }

    @GetMapping("/posts/pre-posts/{prePostUrl}")
    public String prePost(@PathVariable String prePostUrl, Model model) {
        model.addAttribute("prepost", prePostRepo.findByPrePostUrl(prePostUrl));
        return "pre-post";
    }

    @GetMapping("/posts/pre-posts/{prePostUrl}/update")
    public String updatePrePost(@PathVariable String prePostUrl, Model model) {
        model.addAttribute("prepost", prePostRepo.findByPrePostUrl(prePostUrl));
        return "update-pre-post";
    }

    @Transactional
    @PostMapping("/posts/pre-posts/{prePostUrl}/update")
    public String updatePrePost(@PathVariable String prePostUrl,
                                @RequestParam String prePostTitle,
                                @RequestParam String prePostDescription,
                                @RequestParam String prePostText){
        PrePost prePost = prePostRepo.findByPrePostUrl(prePostUrl);
        prePost.setPrePostTitle(prePostTitle);
        prePost.setPrePostDescription(prePostDescription);
        prePost.setPrePostText(prePostText);
        prePostRepo.save(prePost);
        return "redirect:/posts/pre-posts/{prePostUrl}";
    }

    @GetMapping("/posts/pre-posts/create-pre-post")
    public String createPrePost() {
        return "create-pre-post";
    }

    @Transactional
    @PostMapping("/posts/pre-posts/create-pre-post")
    public String createPrePost(@RequestParam String prePostTitle,
                                @RequestParam MultipartFile prePostImg,
                                @RequestParam String prePostDescription,
                                @RequestParam String prePostText,
                                @RequestParam String prePostUrl,
                                @RequestParam String prePostTags) throws IOException {
        Set<String> uniqueTags = new HashSet<>();
        for (String tag : prePostTags.split(",")) {
            uniqueTags.add(tag.trim());
        }
        prePostRepo.save(new PrePost(prePostTitle, prePostImg.getBytes(), prePostDescription,
                prePostText, uniqueTags.toString().replaceAll("[\\[\\]]", ""), prePostUrl));
        return "redirect:/posts/pre-posts";
    }

    @Transactional
    @PostMapping("/posts/pre-posts/{prePostUrl}/delete")
    public String deletePrePost(@PathVariable String prePostUrl) {
        prePostRepo.delete(prePostRepo.findByPrePostUrl(prePostUrl));
        return "redirect:/posts";
    }

    @Transactional
    @PostMapping("/posts/pre-posts/{prePostUrl}/publish")
    public String publishPrePost(@PathVariable String prePostUrl) {
        PrePost prePost = prePostRepo.findByPrePostUrl(prePostUrl);
        Post post = new Post (
                prePost.getPrePostTitle(),
                prePost.getPrePostImg(),
                prePost.getPrePostDescription(),
                prePost.getPrePostText(),
                prePost.getPrePostUrl()
        );
        String[] tagNames = prePost.getPrePostTags().split(",");
        for (String tagName : tagNames) {
            tagName = tagName.trim();
            Tag tag = tagRepo.findByTagName(tagName);
            if (tag == null) {
                tag = new Tag();
                tag.setTagName(tagName);
                tagRepo.save(tag);
            }
            post.getPostTags().add(tag);
        }
        postRepo.save(post);
        prePostRepo.delete(prePost);
        return "redirect:/posts";
    }

    @Transactional
    @PostMapping("/posts/{postUrl}/comment")
    public String addComment(@PathVariable String postUrl, @RequestParam String commentContent, Authentication a) {
        commentRepo.save(new Comment(postRepo.findByPostUrl(postUrl),
                userRepo.findByUserLogin(a.getName()),commentContent));
        return "redirect:/posts/{postUrl}";
    }
}