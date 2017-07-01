package com.limin.blog.service.impl;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.limin.blog.entity.User;
import com.limin.blog.entity.UserExample;
import com.limin.blog.mapper.UserMapper;
import com.limin.blog.service.UserService;
import com.limin.blog.util.MD5Util;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    public void regist(User user) {

        user.setState((byte) 0);//未验证状态
        user.setCode(UUID.randomUUID().toString());//生成认证码
        user.setSalt(UUID.randomUUID().toString().substring(0, 10));
        user.setPassword(MD5Util.string2MD5(user.getPassword() + user.getSalt()));
        String headUrl = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        System.out.println(headUrl+"=================================");
        user.setHeadUrl(headUrl);
        userMapper.insert(user);

       /* new Thread(// 发送认证邮件
                new MailHelper(user.getCode(), user.getEmail())).start();*/
    }

    public void update(User user) {
        if (user.getId() == null) {
            return;
        }
        userMapper.updateByPrimaryKeySelective(user);
    }

    public void deleteById(long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    public User findById(long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public User findByName(String username) {
        if (username == null || username.trim().equals("")) {
            return null;
        }
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        return userMapper.selectByExample(example).get(0);
    }

    public User login(String username, String password) {
        if (username == null || username.trim().equals("") || password == null
                || password.trim().equals("")) {
            return null;
        }
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<User> userList = userMapper.selectByExample(example);
        User user = null;
        if (userList.size() < 0) {
            return null;
        }
        user = userList.get(0);
        String md5 = MD5Util.string2MD5(password+user.getSalt());
        if(md5.equals(user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public List<User> findByExample(UserExample example) {
        List<User> list = userMapper.selectByExample(example);
        return list;
    }

}
