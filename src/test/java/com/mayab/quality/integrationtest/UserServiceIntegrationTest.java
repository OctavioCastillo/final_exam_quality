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
import com.mayab.quality.dao.IDAOUser;
import com.mayab.quality.model.User;
import com.mayab.quality.service.UserService;

import junit.framework.AssertionFailedError;


class UserServiceIntegrationTest extends DBTestCase {
	
	private IDAOUser dao;
	private UserService service;

	public UserServiceIntegrationTest() {
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,"com.mysql.cj.jdbc.Driver");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,"jdbc:mysql://localhost:3307/calidad");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,"root");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,"123456");	
	
	}
	
	@Override
	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/initDB.xml"));
	}
	
	@Override
    protected DatabaseOperation getSetUpOperation() throws Exception {
        return DatabaseOperation.CLEAN_INSERT;
    }
	
	@Override
    protected DatabaseOperation getTearDownOperation() throws Exception {
        return DatabaseOperation.DELETE_ALL;
    }
	
	
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
	
	
}
