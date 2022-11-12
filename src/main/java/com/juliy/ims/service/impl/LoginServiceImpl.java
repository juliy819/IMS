package com.juliy.ims.service.impl;

import com.juliy.ims.common.CustomException;
import com.juliy.ims.dao.UserDao;
import com.juliy.ims.dao.impl.UserDaoImpl;
import com.juliy.ims.entity.User;
import com.juliy.ims.service.LoginService;
import com.juliy.ims.utils.Md5Util;

import java.io.*;
import java.util.Properties;

/**
 * 登录页面业务实现类
 * @author JuLiy
 * @date 2022/9/27 15:43
 */
public class LoginServiceImpl implements LoginService {
    static final String PROPERTIES_PATH = "src/main/resources/info.properties";
    final Properties infoProps = new Properties();
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public boolean loginCheck(String username, String password) {
        String md5Password = Md5Util.md5Encode(password, "UTF-8");
        User user = userDao.queryUser(username, md5Password);
        //用户存在且未被删除则返回true,否则返回false
        return user != null && !user.getDeleted().equals(Boolean.TRUE);
    }

    @Override
    public void loadSettings() throws IOException {
        try (InputStream in = new FileInputStream(PROPERTIES_PATH)) {
            infoProps.load(in);
        } catch (Exception e) {
            throw new CustomException("配置文件加载失败");
        }
    }

    @Override
    public boolean isRemember() {
        return Boolean.parseBoolean(infoProps.getProperty("remember"));
    }

    @Override
    public String[] getLoginInfo() {
        String[] userInfo = new String[2];
        userInfo[0] = infoProps.getProperty("loginUsername");
        userInfo[1] = infoProps.getProperty("loginPassword");
        return userInfo;
    }

    @Override
    public void saveLoginInfo(String username, String password, String remember) throws IOException {
        infoProps.setProperty("loginUsername", username);
        infoProps.setProperty("loginPassword", password);
        infoProps.setProperty("remember", remember);
        try (Writer writer = new FileWriter(PROPERTIES_PATH)) {
            infoProps.store(writer, null);
        }
    }
}
