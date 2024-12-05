package com.mayab.quality.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mayab.quality.model.User;

public class DAOUser implements IDAOUser {
	
	public Connection getConnectionMySQL() {

		Connection con = null;
		try {
			// Establish the driver connector
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Set the URI for connecting the MySql database
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/calidad", "root", "123456");
		} catch (Exception e) {
			System.out.println(e);
		}
		return con;
	}

	@Override
	public User findByEmail(String email) {
		Connection connection = getConnectionMySQL();
		PreparedStatement preparedStatement;
		ResultSet rs;
		
		User result = null;

		try {
			// Declare statement query to run
			preparedStatement = connection.prepareStatement("SELECT * from usuarios2024 WHERE email = ?");
			// Set the values to match in the ? on query
			preparedStatement.setString(1, email);
			rs = preparedStatement.executeQuery();

			// Obtain the pointer to the data in generated table
			rs.next();

			int id = rs.getInt(1);
			String username  = rs.getString(2);
			String emailUser = rs.getString(3);
			String password = rs.getString(4);

			result = new User(username, emailUser, password);
			result.setId(id);

			connection.close();
			rs.close();
			preparedStatement.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		// Return statement
		return result;
	}

	@Override
	public int save(User user) {
		Connection connection = getConnectionMySQL();
		int result = -1;
		try {
			// Declare statement query to run
			PreparedStatement preparedStatement;
			preparedStatement = connection
					.prepareStatement("insert INTO usuarios2024(name,email,password) values(?,?,?)",
							Statement.RETURN_GENERATED_KEYS);
			// Set the values to match in the ? on query
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getPass());
			// Return the result of connection and statement
			if (preparedStatement.executeUpdate() >= 1) {
				try(ResultSet rs = preparedStatement.getGeneratedKeys()){
					if (rs.next()) {
						result = rs.getInt(1);
					}
				}
			}
			
			// Close connection with the database
			connection.close();
			preparedStatement.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		// Return statement
		return result;
	}

	@Override
	public List<User> findAll() {
		Connection connection = getConnectionMySQL();
		  PreparedStatement preparedStatement;
		  ResultSet rs;
		  boolean result = false;

		  User retrieved = null;

		  List<User> listaAlumnos = new ArrayList<User>();
		  
		  try {
		   // Declare statement query to run
		   preparedStatement = connection.prepareStatement("SELECT * from usuarios2024");
		   // Set the values to match in the ? on query
		   rs = preparedStatement.executeQuery();

		   // Obtain the pointer to the data in generated table
		   while (rs.next()) {

			   int id = rs.getInt(1);
			   String name = rs.getString(2);
			   String email = rs.getString(3);
			   String password = rs.getString(4); 
			   retrieved = new User(name, email,password);
			   retrieved.setId(id);
			   listaAlumnos.add(retrieved);
		   }
		   
			   connection.close();
			   rs.close();
			   preparedStatement.close();
	
			  } catch (Exception e) {
			   System.out.println(e);
			  }
			  return listaAlumnos;
	}

	@Override
	public User findById(int id) {
		Connection connection = getConnectionMySQL();
		PreparedStatement preparedStatement;
		ResultSet rs;
		
		User result = null;

		try {
			// Declare statement query to run
			preparedStatement = connection.prepareStatement("SELECT * from usuarios2024 WHERE id = ?");
			// Set the values to match in the ? on query
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();

			// Obtain the pointer to the data in generated table
			rs.next();

			int idUser = rs.getInt(1);
			String username  = rs.getString(2);
			String email = rs.getString(3);
			String password = rs.getString(4);

			result = new User(username, email, password);
			result.setId(id);

			connection.close();
			rs.close();
			preparedStatement.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		// Return statement
		return result;
	}

	@Override
	public boolean deleteById(int id) {
		Connection connection = getConnectionMySQL();
		boolean result = false;

		try {
			// Declare statement query to run
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement("Delete from usuarios2024 WHERE id = ?");
			// Set the values to match in the ? on query
			preparedStatement.setInt(1, id);

			// Return the result of connection and statement
			if (preparedStatement.executeUpdate() >= 1) {
				result = true;
			}
			// Close connection with the database
			connection.close();
			preparedStatement.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		// Return statement
		return result;
	}

	@Override
	public User updateUser(User userNew) {
		Connection connection = getConnectionMySQL();
		User result = null;

		try {
			// Declare statement query to run
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement("UPDATE usuarios2024 SET name = ?,password= ? WHERE id = ?");
			// Set the values to match in the ? on query
			preparedStatement.setString(1, userNew.getName());
			preparedStatement.setString(2, userNew.getPass());
			preparedStatement.setInt(3, userNew.getId());
			// Return the result of connection and statement
			if (preparedStatement.executeUpdate() >= 1) {
				result = userNew;
			}
			// Close connection with the database
			connection.close();
			preparedStatement.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		// Return statement
		return result;
	}

}
