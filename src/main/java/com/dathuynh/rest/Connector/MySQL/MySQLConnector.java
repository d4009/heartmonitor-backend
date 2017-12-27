package com.dathuynh.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import com.dathuynh.rest.User;

public class MySQLConnector {
  private Connection connection = null;
  private Statement statement = null;
  private PreparedStatement preparedStatement = null;
  private ResultSet resultSet = null;

  public MySQLConnector() throws Exception {
    prepareConnection();
  }
  public void prepareConnection() throws Exception {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      String connectionString = "jdbc:mysql://" + MySQLConfig.HOST + "/" + MySQLConfig.DB_NAME + "?" + "user=" + MySQLConfig.USERNAME + "&password=" + MySQLConfig.PASSWORD;
      connection = DriverManager.getConnection(connectionString);
    } catch (Exception e) {
      throw e;
    }
  }

  public void newUser(User newUser) throws Exception{
    try {
      SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
      // String query = "INSERT INTO users (username, password, name, gender, dob, height, weight, emergency_phone) values ('"
      //              + newUser.getUsername() + "','"
      //              + newUser.getPassword() + "','"
      //              + newUser.getName() + "',"
      //              + newUser.getGender() + ","
      //              + newUser.getDob() + ","
      //              + newUser.getHeight() + ","
      //              + newUser.getWeight() + ",'"
      //              + newUser.getEmergencyPhone() + "');";
      String query = "INSERT INTO users (username, password, name, gender, dob, height, weight, emergency_phone) values (?, ?, ?, ?, ?, ?, ?, ?)";
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, newUser.getUsername());
      preparedStatement.setString(2, newUser.getPassword());
      preparedStatement.setString(3, newUser.getName());
      preparedStatement.setBoolean(4, newUser.getGender());
      preparedStatement.setDate(5, new java.sql.Date(newUser.getDob().getTime()));
      preparedStatement.setInt(6, newUser.getHeight());
      preparedStatement.setInt(7, newUser.getWeight());
      preparedStatement.setString(8, newUser.getEmergencyPhone());
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (Exception e) {
        throw e;
      }
    }
  }

  public User getUserByUsername(String username) throws Exception{
    try {
      String query = "SELECT * FROM users WHERE username = " + username + ";";
      statement = connection.createStatement();
      resultSet = statement.executeQuery(query);
      while (resultSet.next()){
        String password = resultSet.getString("password");
        String name = resultSet.getString("name");
        boolean gender = resultSet.getBoolean("gender");
        java.sql.Date sqlDob = resultSet.getDate("dob");
        Date dob = new Date(sqlDob.getTime());
        int height = resultSet.getInt("height");
        int weight = resultSet.getInt("weight");
        String emergency_phone = resultSet.getString("emergency_phone");
        return new
          User.UserBuilder(username, password)
          .name(name)
          .gender(gender)
          .dob(dob)
          .height(height)
          .weight(weight)
          .emergency_phone(emergency_phone)
          .build();
      }
    } catch (Exception e) {
      throw e;
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (Exception e) {
        throw e;
      }
      return null;
    }
  }

  public User login(String username, String password) throws Exception{
    try {
      String hashedPassword = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);
      String query = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + hashedPassword + "';";
      statement = connection.createStatement();
      resultSet = statement.executeQuery(query);
      resultSet.last();
      int count = resultSet.getRow();
      resultSet.beforeFirst();
      if (count > 0) {
        resultSet.next();
        String name = resultSet.getString("name");
        boolean gender = resultSet.getBoolean("gender");
        java.sql.Date sqlDob = resultSet.getDate("dob");
        Date dob = new Date(sqlDob.getTime());
        int height = resultSet.getInt("height");
        int weight = resultSet.getInt("weight");
        String emergency_phone = resultSet.getString("emergency_phone");
        return new
          User.UserBuilder(username, hashedPassword)
          .name(name)
          .gender(gender)
          .dob(dob)
          .height(height)
          .weight(weight)
          .emergency_phone(emergency_phone)
          .build();
      } else {
        return null;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (Exception e) {
        throw e;
      }
    }
  }

  public ArrayList<User> getPatients() throws Exception {
    String query = "SELECT * FROM users;";
    ArrayList<User> patients = new ArrayList<>();
    statement = connection.createStatement();
    resultSet = statement.executeQuery(query);
    while (resultSet.next()) {
      patients.add(new
      User.UserBuilder(resultSet.getString("username"), resultSet.getString("password"))
      .name(resultSet.getString("name"))
      .gender(resultSet.getBoolean("gender"))
      .dob(new Date(resultSet.getDate("dob").getTime()))
      .height(resultSet.getInt("height"))
      .weight(resultSet.getInt("weight"))
      .emergency_phone(resultSet.getString("emergency_phone"))
      .build());
    }
    return patients;
  }

  public void close() throws Exception{
    try {
      if (statement != null)
        statement.close();
      if (connection != null)
        connection.close();
      if (resultSet != null)
        resultSet.close();
    } catch (Exception e) {
      throw e;
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (Exception e) {
        throw e;
      }
    }
  }
}
