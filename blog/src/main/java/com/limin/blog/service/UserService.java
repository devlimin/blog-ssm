package com.limin.blog.service;

import java.util.List;

import com.limin.blog.entity.User;
import com.limin.blog.entity.UserExample;

public interface UserService {
	
	void regist(User user);
	void update(User user);
	void deleteById(long id);
	
	User findById(long id);
	User findByName(String username);
	User login(String username, String password);
	List<User> findByExample(UserExample example);
}
