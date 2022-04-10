package com.yjh.blob.Service;

import com.yjh.blob.po.User;

public interface UserService {
    User checkUser(String username,String password);
}
