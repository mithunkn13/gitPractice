//package com.blog.security;
//
//import com.blog.entity.Role;
//import com.blog.entity.User;
//import com.blog.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import java.util.Collection;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//public class CustomUserDetails implements UserDetailsService
//{
//
//    @Autowired
//    private UserRepository userRepo;
//    @Override
//    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException
//    {
//        User user = userRepo.findByEmailOrUsername(usernameOrEmail, usernameOrEmail).orElseThrow(
//                () -> new UsernameNotFoundException("User not found with username or email:" + usernameOrEmail)
//        );
//        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),mapToAuthority(user.getRoles()));
//
//    }
//
//    private Collection<? extends GrantedAuthority> mapToAuthority(Set<Role> roles)
//    {
//        return roles.stream().map((role)->new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
//    }
//}

package com.blog.security;

import com.blog.entity.Role;
import com.blog.entity.User;
import com.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService
{
    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepo.findByEmailOrUsername(usernameOrEmail, usernameOrEmail).orElseThrow(
                () -> new UsernameNotFoundException("User not found with Username or email : " + usernameOrEmail)
        );
        System.out.println(2);
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),
                mapToAuthority(user.getRoles()));
    }

    public Collection<? extends GrantedAuthority> mapToAuthority(Set<Role> roles)
    {
        return roles.stream().map((role)->new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
