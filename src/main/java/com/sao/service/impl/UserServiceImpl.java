package com.sao.service.impl;


import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import com.sao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sao.domain.User;
import com.sao.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public User saveOrUpateUser(User user) {
        return userRepository.save(user);
    }

    /**
     * 注册用户
     * @param user
     * @return
     */
    @Transactional
    @Override
    public User registerUser(User user) {
        user.setAvatar("/image/avatar-defualt.jpg");
        user.setNick(user.getUsername());
        user.setEncodePassword(user.getPassword());
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void removeUser(Long id) {
        userRepository.findOne(id);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Page<User> listUsersByUsernameLike(String username, Pageable pageable) {
        //模糊查询
        username="%"+username+"%";
        Page<User> users=userRepository.findByUsernameLike(username,pageable);
        return users;
    }

}
