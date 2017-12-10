package com.dathuynh.rest;

import java.util.Date;

public class User {
  private final String username;
  private final String password;
  private final String name;
  private final boolean gender;
  private final Date dob;
  private final int height;
  private final int weight;
  private final String emergency_phone;

  private User(UserBuilder builder){
    this.username = builder.username;
    this.password = builder.password;
    this.name = builder.name;
    this.gender = builder.gender;
    this.dob = builder.dob;
    this.height = builder.height;
    this.weight = builder.weight;
    this.emergency_phone = builder.emergency_phone;
  }

  public String getUsername() {
    return this.username;
  }
  //
  // public void setUsername(String username){
  //   this.username = username;
  // }
  //
  public String getPassword() {
    return this.password;
  }
  //
  // public void setPassword(String password){
  //   this.password = password;
  // }
  //
  public String getName(){
    return this.name;
  }
  //
  // public void setName(String name){
  //   this.name = name;
  // }
  //
  public boolean getGender(){
    return gender;
  }
  //
  // public void setGender(boolean gender){
  //   this.gender = gender;
  // }
  //
  public Date getDob(){
    return dob;
  }
  //
  // public void setDob(Date dob){
  //   this.dob = dob;
  // }
  //
  public int getHeight(){
    return height;
  }
  //
  // public void setHeight(int height){
  //   this.height = height;
  // }
  //
  public int getWeight(){
    return weight;
  }
  //
  // public void setWeight(int weight){
  //   this.weight = weight;
  // }
  //
  public String getEmergencyPhone(){
    return emergency_phone;
  }
  //
  // public void setEmergencyPhone(String emergency_phone){
  //   this.emergency_phone = emergency_phone;
  // }
  //
  public static class UserBuilder {
    private final String username;
    private final String password;
    private String name;
    private boolean gender;
    private Date dob;
    private int height;
    private int weight;
    private String emergency_phone;

    public UserBuilder(String username, String password){
      this.username = username;
      this.password = password;
    }

    public UserBuilder name(String name){
      this.name = name;
      return this;
    }

    public UserBuilder gender(boolean gender){
      this.gender = gender;
      return this;
    }

    public UserBuilder dob(Date dob){
      this.dob = dob;
      return this;
    }

    public UserBuilder height(int height){
      this.height = height;
      return this;
    }

    public UserBuilder weight(int weight){
      this.weight = weight;
      return this;
    }

    public UserBuilder emergency_phone(String emergency_phone){
      this.emergency_phone = emergency_phone;
      return this;
    }

    public User build() {
      return new User(this);
    }
  }
}
