package com.example.eCommerce.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.stereotype.Service;

import com.example.eCommerce.repository.UserRepository;
import com.example.eCommerce.Exception.UsernameNotFoundException;


@Service
public class UserService implements UserDetailsService {

     private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       
        return userRepository.findByEmail(email).orElseThrow(
            () -> new UsernameNotFoundException("There is no user with the given email"));
    }
    
}
