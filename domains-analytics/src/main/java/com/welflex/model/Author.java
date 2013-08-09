package com.welflex.model;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <!--
    ColumnFamily: Authors
    We'll store all the author data here.

    Row Key => Author's username (implies usernames must be unique)
    Column Name: an attribute for the entry (title, body, etc)
    Column Value: value of the associated attribute

    Access: get author by name (aka grab all columns from a specific Row)

    Authors : { // CF
        sacharya : { // row key
            // and the columns as "profile" attributes
            numPosts: 11,
            name: Sanjay Acharya,
            userName:sacharya
            password:^&*&^*^&*^
            twitterId: rambo,
            email: rambo@example.com,
            bio: "bla bla bla"
        },
        // and the other authors
        Author 2 {
            ...
        }
    }
-->
 *
 */
public class Author {

  private String userName;

  private String password;

  private String name;

  private int numberOfPosts;

  private String twitterId;

  private String email;

  private String biography;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getNumberOfPosts() {
    return numberOfPosts;
  }

  public void setNumberOfPosts(int numberOfPosts) {
    this.numberOfPosts = numberOfPosts;
  }

  public String getTwitterId() {
    return twitterId;
  }

  public void setTwitterId(String twitterId) {
    this.twitterId = twitterId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getBiography() {
    return biography;
  }

  public void setBiography(String biography) {
    this.biography = biography;
  }

  public String getUserName() {

    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
