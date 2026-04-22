package org.studyeasy.sptingBlog.Controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.studyeasy.sptingBlog.models.Account;
import org.studyeasy.sptingBlog.models.Post;
import org.studyeasy.sptingBlog.services.AccountService;
import org.studyeasy.sptingBlog.services.PostService;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable Long id, Model model, Principal principal) {
        Optional<Post> optionalPost = postService.getById(id);
        String authUser = "email";
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);

            if (principal != null) {
                authUser = principal.getName();
            }
            if (authUser.equals(post.getAccount().getEmail())) {
                model.addAttribute("isOwner", true);
            } else {
                model.addAttribute("isOwner", false);
            }
            return "post_views/post";
        } else {
            return "404";
        }
    }

    @GetMapping("/post/add")
    @PreAuthorize("isAuthenticated()")
    public String addPost(Model model, Principal principal) {
        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }
        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);

        if (optionalAccount.isPresent()) {
            Post post = new Post();
            post.setAccount(optionalAccount.get());
            model.addAttribute("post", post);
            return "post_views/addPost";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/post/add")
    public String addPostHandler(@ModelAttribute Post post, Principal principal) {
        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }

        if (post.getAccount().getEmail().compareToIgnoreCase(authUser) < 0) {
            return "redirect:/?error";
        }

        postService.save(post);

        return "redirect:/post/" + post.getId();
    }

    @GetMapping("/post/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String getEditPage(@PathVariable Long id, Model model) {
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "post_views/editPost";
        } else {
            return "404";
        }
    }

    @PostMapping("/post/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post post) {
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            existingPost.setTitle(post.getTitle());
            existingPost.setBody(post.getBody());
            postService.save(existingPost);
            return "redirect:/post/" + existingPost.getId();
        } else {
            return "404";
        }
    }

    @GetMapping("/post/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String deletePost(@PathVariable Long id) {
      Optional<Post> optionalPost = postService.getById(id);
      if (optionalPost.isPresent()){
        Post post = optionalPost.get();
        postService.delete(post);
        return "redirect:/";
      } else {
        return "redirect:/?error";
      }
    }

}
