package com.mayab.quality.service;

import java.util.ArrayList;
import java.util.List;

import com.mayab.quality.dao.IDAOUser;
import com.mayab.quality.model.User;

public class UserService {
private IDAOUser dao;
	
	public UserService(IDAOUser dao) {
		this.dao = dao;
	}
	
	public User createUser(String name, String email, String pass) {
		User user = null;
		
		//password length validation
		if (pass.length() <= 16 && pass.length() >= 8) {
			user=dao.findByEmail(email);
			if(user == null) {
				user = new User(name, email, pass);
				int id = dao.save(user);
				user.setId(id);
				return user;
			}
			else {
				return null;
			}
		}
		return null;
	}
	
	public List<User> findAllUsers(){
		List<User> users = new ArrayList<User>();
		users = dao.findAll();
	
		return users;
	}

	public User findUserByEmail(String email) {
		
		return dao.findByEmail(email);
	}

	public User findUserById(int id) {
		
		return dao.findById(id);
	}
    
    public User updateUser(User user) {
    	User userOld = dao.findById(user.getId());
    	userOld.setName(user.getName());
    	userOld.setPass(user.getPass());
    	return dao.updateUser(userOld);
    }

    public boolean deleteUser(int id) {
    	return dao.deleteById(id);
    }
}
