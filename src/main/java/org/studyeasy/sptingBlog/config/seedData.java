package org.studyeasy.sptingBlog.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.studyeasy.sptingBlog.models.Account;
import org.studyeasy.sptingBlog.models.Authority;
import org.studyeasy.sptingBlog.models.Post;
import org.studyeasy.sptingBlog.services.AccountService;
import org.studyeasy.sptingBlog.services.AuthorityService;
import org.studyeasy.sptingBlog.services.PostService;
import org.studyeasy.sptingBlog.util.constants.Privilages;
import org.studyeasy.sptingBlog.util.constants.Roles;

@Component
public class seedData implements CommandLineRunner{


    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityService authorityService;

    @Override
    public void run(String... args) throws Exception {

        for(Privilages auth : Privilages.values()){
            Authority authority = new Authority();
            authority.setId(auth.getPrivilageId());
            authority.setName(auth.getPrivilageString());
            authorityService.save(authority);
        }

        Account account1 = new Account();
            account1.setEmail("user@acc.acc");
            account1.setFirstName("user");
            account1.setLastName("acc1LN");
            account1.setPassword("acc1P");
            account1.setRole(Roles.USER.getRole());
            accountService.save(account1);
        
        Account account2 = new Account();
            account2.setEmail("admin@acc.acc");
            account2.setFirstName("admin");
            account2.setLastName("acc2LN");
            account2.setPassword("acc2P");
            account2.setRole(Roles.ADMIN.getRole());
            accountService.save(account2);
        
        Account account3 = new Account();
            account3.setEmail("editor@acc.acc");
            account3.setFirstName("editor");
            account3.setLastName("acc3LN");
            account3.setPassword("acc3P");
            account3.setRole(Roles.EDITOR.getRole());
            accountService.save(account3);
    
        Account account4 = new Account();
            account4.setEmail("seditor@acc.acc");
            account4.setFirstName("super_editor");
            account4.setLastName("acc4LN");
            account4.setPassword("acc4P");
            account4.setRole(Roles.EDITOR.getRole());
            Set<Authority> authorities4 = new HashSet<>();
            authorityService.findById(Privilages.RESET_ANY_USER_PASSWORD.getPrivilageId()).ifPresent(authorities4::add);
            authorityService.findById(Privilages.ACCESS_ADMIN_PANEL.getPrivilageId()).ifPresent(authorities4::add);
            account4.setAuthorities(authorities4);
            accountService.save(account4);

        List<Post> posts = postService.getAll();  

        if (posts.isEmpty()){
            Post post1 = new Post();
                post1.setBody("Post1");
                post1.setTitle("Title1");
                post1.setAccount(account1);
                postService.save(post1);
            
            Post post2 = new Post();
                post2.setBody("Post2");
                post2.setTitle("Title2");
                post2.setAccount(account4);
                postService.save(post2);
        }
    }

}