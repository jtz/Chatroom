package edu.bu.met.cs665.model;

public class UserBuilder {
  private String name;
  private String sex;
  private String country;

  // only name is require field
  public UserBuilder(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSex() {
    return sex;
  }

  public UserBuilder setSex(String sex) {
    this.sex = sex;
    return this;
  }

  public String getCountry() {
    return country;
  }

  public UserBuilder setCountry(String country) {
    this.country = country;
    return this;
  }

  /**
   * Builder pattern to create User.
   *
   * @return User instance
   */
  public User build() {
    return new User(this);
  }
}
