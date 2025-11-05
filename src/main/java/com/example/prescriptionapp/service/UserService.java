package com.example.prescriptionapp.service;

import com.example.prescriptionapp.model.User;
import com.example.prescriptionapp.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.stream.Collectors;
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepo, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepo;
        this.passwordEncoder = encoder;

        if(userRepository.count() == 0) {
            User object = new User();
            object.setUsername("admin");
            object.setPassword(passwordEncoder.encode("adminpass"));
            object.setRoles("ROLE_USER,ROLE_ADMIN");
            userRepository.save(object);
        }
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var authorities = Arrays.stream(user.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
    public User register(String username, String rawPassword) {
        User object = new User();
        object.setUsername(username);
        object.setPassword(passwordEncoder.encode(rawPassword));
        object.setRoles("ROLE_USER");
        return userRepository.save(object);
    }
}
