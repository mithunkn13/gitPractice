//package com.blog.controller;
//
//import com.blog.entity.User;
//import com.blog.payload.LoginDto;
//import com.blog.payload.SignUpDto;
//import com.blog.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController
//{
//    @Autowired
//    private UserRepository userRepo;
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto)
//    {
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword());
//        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        return new ResponseEntity<>("User Signed-In Sucessfully",HttpStatus.OK);
//    }
//
//    @PostMapping("/signUp")
//    public ResponseEntity<String> registerUser(@RequestBody SignUpDto signUpDto)
//    {
//        if(userRepo.existsByEmail(signUpDto.getEmail()))
//        {
//            return new ResponseEntity<>("Email Id Already Exist", HttpStatus.BAD_REQUEST);
//        }
//        if(userRepo.existsByUsername(signUpDto.getUsername()))
//        {
//            return new ResponseEntity<>("Username Already Exist",HttpStatus.BAD_REQUEST);
//        }
//        User user = new User();
//        user.setName(signUpDto.getName());
//        user.setEmail(signUpDto.getEmail());
//        user.setUsername(signUpDto.getUsername());
//        user.setPassword(signUpDto.getPassword());
//        userRepo.save(user);
//        return new ResponseEntity<>("User Created",HttpStatus.OK);
//    }
//}
package com.blog.controller;

import com.blog.entity.ERole;
import com.blog.entity.Role;
import com.blog.entity.User;
import com.blog.exception.ResourceNotFoundException;
import com.blog.payload.JWTAuthResponse;
import com.blog.payload.LoginDto;
import com.blog.payload.SignUpDto;
import com.blog.repository.RolesRepository;
import com.blog.repository.UserRepository;
import com.blog.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController
{
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RolesRepository roleRepo;

    @Autowired
    private JwtTokenProvider tokenProvider;


    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        System.out.println(1);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println(3);
        // get token form tokenProvider

        String token = tokenProvider.generateToken(authentication);
        System.out.println(5);
        return ResponseEntity.ok(new JWTAuthResponse(token));
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpDto signUpDto)
    {
        if(userRepo.existsByEmail(signUpDto.getEmail()))
        {
            return new ResponseEntity<>("Email Already Exist", HttpStatus.BAD_REQUEST);
        }
        if(userRepo.existsByUsername(signUpDto.getUsername()))
        {
            return new ResponseEntity<>("Username Already Exist", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setName(signUpDto.getName());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setUsername(signUpDto.getUsername());

        Set<String> strRoles = signUpDto.getRoles();
        Set<Role> rolesSet = new HashSet<>();

        if(strRoles == null || strRoles.isEmpty())
        {
            Role guestRole = roleRepo.findByName(ERole.ROLE_GUEST.toString()).orElseThrow(
                    ()-> new ResourceNotFoundException("Role Not Found")
            );
            rolesSet.add(guestRole);
            user.setRoles(rolesSet);
        }
        else
        {
            for( String s : strRoles)
            {
                switch (s)
                {
                    case("admin"):
                        Role roleAdmin = roleRepo.findByName(ERole.ROLE_ADMIN.toString()).orElseThrow(
                                () -> new ResourceNotFoundException("Role Not Found")
                        );
                        rolesSet.add(roleAdmin);
                        break;
                    case("user"):
                        Role roleUser = roleRepo.findByName(ERole.ROLE_USER.toString()).orElseThrow(
                                () -> new ResourceNotFoundException("Role Not Found")
                        );
                        rolesSet.add(roleUser);
                        break;
                    case("guest"):
                        Role roleGuest = roleRepo.findByName(ERole.ROLE_GUEST.toString()).orElseThrow(
                                () -> new ResourceNotFoundException("Role Not Found")
                        );
                        rolesSet.add(roleGuest);
                        break;
                    default:throw new ResourceNotFoundException("s "+"Role Not Found");
                }
            }
        }
        user.setRoles(rolesSet);
        userRepo.save(user);

//        Role roleAdmin = roleRepo.findByName("ROLE_ADMIN").get();
//        user.setRoles(Collections.singleton(roleAdmin));
//        userRepo.save(user);

        return new ResponseEntity<>("User Registered Successfully",HttpStatus.CREATED);
    }
}
