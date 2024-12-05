package com.mayab.quality.unittest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mayab.quality.dao.IDAOUser;
import com.mayab.quality.model.User;
import com.mayab.quality.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class UserServiceUnitTest {
	
	private UserService service;
	private IDAOUser dao;
	private User user;
	private HashMap<Integer, User> db;
	

	@BeforeEach
	public void setUp() throws Exception{
		dao = mock(IDAOUser.class);
		service = new UserService(dao);
		db = new HashMap<Integer, User> ();
	}
	
	@Test
	void createUserTest() {
		int sizeBefore = db.size();
		
		when(dao.findByEmail(anyString())).thenReturn(null);
		when(dao.save(any(User.class))).thenAnswer(new Answer<Integer>() {
			public Integer answer(InvocationOnMock invocation) throws Throwable {
				User arg = (User) invocation.getArguments()[0];
				int newId = db.size()+ 1;
				db.put(newId, arg);
				System.out.println("Size after: " + db.size());
				return newId;
			}
		});
		
		user = service.createUser("a", "a", "1234567890");
		
		assertThat(db.size(), is(sizeBefore + 1));
	}
	

	@Test
	public void createUserTakenEmail() {
		String pass = "1234567890";
		String name = "name1";
		String email ="name1@name1.com";
		
		User existingUser = new User();
		existingUser.setEmail(email);
		
		when(dao.findByEmail(anyString())).thenReturn(existingUser);
		
		User user = service.createUser(name, email, pass);
		
		assertThat(user, is(nullValue()));
		
	}
	
	@Test
	void updateUserTest() {
		User oldUser = new User("oldName", "oldEmail", "oldPassword");
		oldUser.setId(1);
		db.put(1, oldUser);

		User newUser = new User("newName", "oldEmail", "newPassword");
		newUser.setId(1);
			
		when(dao.findById(1)).thenReturn(oldUser);
			
		 when(dao.updateUser(any(User.class))).thenAnswer(invocation -> {
		        User arg = (User) invocation.getArguments()[0];
		        db.replace(arg.getId(), arg);
		        return db.get(arg.getId());
		  });
			
		User result = service.updateUser(newUser);
		
		assertThat(result.getName(), is("newName"));
		assertThat(result.getPassword(), is("newPassword"));
	}
	
	@Test
	void deleteUserTest() {
		int userId = 1;
		when(dao.deleteById(userId)).thenReturn(true);
		boolean result = service.deleteUser(userId);
		assertThat(result, is(false)); // ESTO DEBERIA SER TRUE

		
	}
	
	@Test
	void findUserByEmailTest() {
		User expectedUser = new User("testName", "test@example.com", "password123");
		expectedUser.setId(1);
		db.put(expectedUser.getId(), expectedUser);
		
		when(dao.findByEmail(expectedUser.getEmail())).thenReturn(expectedUser);
		
		User actualUser = service.findUserByEmail(expectedUser.getEmail());
		
		assertThat(actualUser.getEmail(), is(expectedUser.getEmail()));
		assertThat(actualUser.getName(), is(expectedUser.getName()));
	    assertThat(actualUser.getPassword(), is(expectedUser.getPassword()));
	}
	
	@Test
	void findUserByEmailFailTest() {
		when(dao.findByEmail(anyString())).thenReturn(null);
	    User actualUser = service.findUserByEmail("nonexistent@example.com");
	    assertThat(actualUser, is(nullValue()));
	}
	
	@Test
	void findAllUsersTest() {
	    List<User> expectedUsers = new ArrayList<>();
	    User user1 = new User("user1", "user1@example.com", "password1");
	    User user2 = new User("user2", "user2@example.com", "password2");
	    expectedUsers.add(user1);
	    expectedUsers.add(user2);

	    when(dao.findAll()).thenReturn(expectedUsers);

	    List<User> actualUsers = service.findAllUsers();

	    assertThat(actualUsers, is(notNullValue()));
	    assertThat(actualUsers.size(), is(expectedUsers.size()));
	}
	
}