package org.studyeasy.sptingBlog.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.studyeasy.sptingBlog.models.Post;
import org.studyeasy.sptingBlog.services.PostService;
import org.springframework.ui.Model;

@Controller
public class HomeController{
    
    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String home(Model model){
        List<Post> posts = postService.getAll();
        model.addAttribute("posts", posts);
        return "home_views/home";
    }
    // @GetMapping("/editor")
    // public String editor(Model model){
    //     return "editor";
    // }

}