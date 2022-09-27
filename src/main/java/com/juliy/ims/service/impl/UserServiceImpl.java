package com.juliy.ims.service.impl;

import com.juliy.ims.dao.UserDao;
import com.juliy.ims.dao.impl.UserDaoImpl;
import com.juliy.ims.service.UserService;

/**
 * User业务类
 * @author JuLiy
 * @date 2022/9/27 15:43
 */
public class UserServiceImpl implements UserService {
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public boolean loginCheck(String username, String password) {
        return userDao.queryUser(username, password);
    }
}
