package org.studyeasy.sptingBlog.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.studyeasy.sptingBlog.models.Account;
import org.studyeasy.sptingBlog.models.Authority;
import org.studyeasy.sptingBlog.repositories.AccountRepository;

@Service
public class AccountService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public Account save(Account account){
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findOneByEmailIgnoreCase(email);
        if (!account.isPresent()){
            throw new UsernameNotFoundException("Account doesn't exist");
        }    
        Account ret = account.get();

        List<GrantedAuthority> grantedAuthority = new ArrayList<>();
        grantedAuthority.add(new SimpleGrantedAuthority(ret.getRole()));

        for (Authority auth : ret.getAuthorities()){
            grantedAuthority.add(new SimpleGrantedAuthority(auth.getName()));
        }

        return new User(ret.getEmail(), ret.getPassword(), grantedAuthority);
    }


    public Optional<Account> findOneByEmail(String authUser) {
       return accountRepository.findOneByEmailIgnoreCase(authUser);
    }
}
