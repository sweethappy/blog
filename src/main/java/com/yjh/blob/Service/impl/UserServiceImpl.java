package com.yjh.blob.Service.impl;

import com.yjh.blob.Service.UserService;
import com.yjh.blob.dao.UserRepository;
import com.yjh.blob.po.User;
import com.yjh.blob.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }
}
