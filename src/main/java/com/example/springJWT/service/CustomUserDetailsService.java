package com.example.springJWT.service;

import com.example.springJWT.dto.CustumUserDetails;
import com.example.springJWT.entity.UserEntity;
import com.example.springJWT.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository){

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userData = userRepository.findByUsername(username);

        if(userData != null){
            return new CustumUserDetails(userData);
        }

        return null;
    }
}
