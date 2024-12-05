package com.mayab.quality.dao;

import java.util.List;

import com.mayab.quality.model.User;

public interface IDAOUser {
	User findByEmail(String name);
	int save(User user);
	List<User> findAll();
	User findById(int id);
	boolean deleteById(int id);
	User updateUser(User userOld);
}
