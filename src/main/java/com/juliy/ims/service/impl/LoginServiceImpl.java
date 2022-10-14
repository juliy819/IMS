package com.juliy.ims.service.impl;

import com.juliy.ims.common.CustomException;
import com.juliy.ims.dao.UserDao;
import com.juliy.ims.dao.impl.UserDaoImpl;
import com.juliy.ims.entity.User;
import com.juliy.ims.service.LoginService;
import com.juliy.ims.utils.Md5Util;

/**
 * 登录页面业务实现类
 * @author JuLiy
 * @date 2022/9/27 15:43
 */
public class LoginServiceImpl implements LoginService {
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public void loginCheck(String username, String password) {
        if ("".equals(username)) {
            throw new CustomException("用户名不能为空");
        }
        if ("".equals(password)) {
            throw new CustomException("密码不能为空");
        }

        String md5Password = Md5Util.md5Encode(password, "UTF-8");
        User user = userDao.queryUser(username, md5Password);
        //用户存在且未被删除则返回true
        if (user == null || user.getDeleted().equals(Boolean.TRUE)) {
            throw new CustomException("用户名或密码错误！请重新输入");
        }
    }
}
