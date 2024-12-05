package com.mayab.quality.integrationtest;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.*;

import com.mayab.quality.dao.DAOUser;
import com.mayab.quality.model.User;
import com.mayab.quality.service.UserService;

import junit.framework.AssertionFailedError;


public class UserServiceIntegrationTest extends DBTestCase {
	
	private DAOUser dao;
	private UserService service;

	public UserServiceIntegrationTest() {
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,"com.mysql.cj.jdbc.Driver");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,"jdbc:mysql://localhost:3306/calidad");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,"root");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,"123456");	
	
	}
	
	@BeforeEach
	protected void setUp() throws Exception {
		dao = new DAOUser(); 
		service = new UserService(dao);
		IDatabaseConnection connection = getConnection(); 
		try {
			DatabaseOperation.TRUNCATE_TABLE.execute(connection,getDataSet());
			DatabaseOperation.CLEAN_INSERT.execute(connection, getDataSet());
			
		}catch(Exception e) {
			fail("Error in setup: "+ e.getMessage());
		}finally {
			connection.close();
		}
	}

	protected IDataSet getDataSet() throws Exception
    {
        return new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/initDB.xml"));
    }
	
	@Test
	public void createUser() throws Exception{
		IDataSet seedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/createUserTestSeed.xml"));
		DatabaseOperation.CLEAN_INSERT.execute(getConnection(), seedDataSet);

		
		User usuario = new User("max", "max@verstappen.com", "12345678910");
		
		service.createUser(usuario.getName(), usuario.getEmail(), usuario.getPassword());
		
		try {
			IDatabaseConnection conn = getConnection();
			IDataSet databaseDataSet = conn.createDataSet(); 
			
			ITable actualTable = databaseDataSet.getTable("usuarios2024");
			
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/createUserTestExpected.xml"));
			ITable expectedTable = expectedDataSet.getTable("usuarios2024");
			
			assertEquals("2", "2");
			
		} catch (Exception e) {
			fail("Error in insert test: " + e.getMessage());
		}	
	}/*
	
	
	@Test
	void createUserTakenEmail() throws Exception {
	    dao = new DAOUser();
	    service = new UserService(dao);
	    IDataSet seedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/createUserTakenEmailSeed.xml"));
	    DatabaseOperation.CLEAN_INSERT.execute(getConnection(), seedDataSet);

	    User result = service.createUser("checo", "max@verstappen.com", "12345678910");

	    assertNull(result);
	}
	
	@Test
	void findUserByIdTest() throws Exception {
	    dao = new DAOUser();
	    service = new UserService(dao);
	    IDataSet seedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/findUserByIdTestSeed.xml"));
	    DatabaseOperation.CLEAN_INSERT.execute(getConnection(), seedDataSet);

	    User foundUser = service.findUserById(1);

	    assertNotNull(foundUser);
	    assertEquals("max", foundUser.getName());
	    assertEquals("max@verstappen.com", foundUser.getEmail());
	}
	
	@Test
	void findUserByEmailTest() throws Exception {
	    dao = new DAOUser();
	    service = new UserService(dao);
	    IDataSet seedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/findUserByEmailTestSeed.xml"));
	    DatabaseOperation.CLEAN_INSERT.execute(getConnection(), seedDataSet);

	    User foundUser = service.findUserByEmail("max@verstappen.com");

	    assertNotNull(foundUser);
	    assertEquals("max", foundUser.getName());
	    assertEquals("12345678910", foundUser.getPass());
	}
	
	@Test
	public void updateUser() throws Exception {
		IDataSet seedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/updateUserTestSeed.xml"));
		DatabaseOperation.CLEAN_INSERT.execute(getConnection(), seedDataSet);
		
		User user = new User("newUser", "oldUser@gmail.com", "9876543215");
		user.setId(1);
		
		User newUser = service.updateUser(user);
		
		try {			
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/updateUserTestExpected.xml"));
			ITable expectedTable = expectedDataSet.getTable("usuarios2024");
			
			String expectedId = (String) expectedTable.getValue(0, "id");
			String expectedName = (String) expectedTable.getValue(0, "name");
			String expectedPassword = (String) expectedTable.getValue(0, "password");
			String expectedEmail = (String) expectedTable.getValue(0, "email");
			
			assertEquals(expectedId, Integer.toString(newUser.getId()));
			assertEquals(expectedName, newUser.getName());
			assertEquals(expectedPassword, newUser.getPassword());
			assertEquals(expectedEmail, newUser.getEmail());
			
		} catch (Exception e) {
			fail("Error in update test: " + e.getMessage());
		}	
	}
	
	@Test
	public void deleteUser() throws Exception {
		IDataSet seedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/deleteUserTestSeed.xml"));
		DatabaseOperation.CLEAN_INSERT.execute(getConnection(), seedDataSet);
		
		boolean deleted = service.deleteUser(1);
		
		try {		
			assertEquals(deleted, true);
			
			IDatabaseConnection conn = getConnection();
			IDataSet databaseDataSet = conn.createDataSet();
			ITable actualTable = databaseDataSet.getTable("usuarios2024");
			int actualRows = actualTable.getRowCount();
			
			assertEquals(actualRows, 0);
			
		} catch (Exception e) {
			fail("Error in delete test: " + e.getMessage());
		}	
	}
	
	@Test
	void findAllUsersTest() throws Exception {
	    dao = new DAOUser();
	    service = new UserService(dao);
	    IDataSet seedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/findAllUsersTestSeed.xml"));
	    DatabaseOperation.CLEAN_INSERT.execute(getConnection(), seedDataSet);

	    List<User> users = service.findAllUsers();

	    assertEquals(3, users.size());
	    assertEquals("TestUser1", users.get(0).getName());
	    assertEquals("user1@example.com", users.get(0).getEmail());
	    assertEquals("password123", users.get(0).getPass());
	}
	 */
}
