package edu.bu.met.cs665.model;

import org.apache.log4j.Logger;

public class User {

  private static Logger logger = Logger.getLogger(User.class);

  private final String name;
  private final String sex;
  private final String country;

  protected User(UserBuilder builder) {
    this.name = builder.getName();
    this.sex = builder.getSex();
    this.country = builder.getCountry();
  }

  public String getName() {
    return name;
  }

  public String getSex() {
    return sex;
  }

  public String getCountry() {
    return country;
  }

  public void receiveMessage(String message) {
    logger.info("   [" + name + "] get message: " + message);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((country == null) ? 0 : country.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((sex == null) ? 0 : sex.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    User other = (User) obj;
    if (country == null) {
      if (other.country != null) {
        return false;
      }
    } else if (!country.equals(other.country)) {
      return false;
    }
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    if (sex == null) {
      if (other.sex != null) {
        return false;
      }
    } else if (!sex.equals(other.sex)) {
      return false;
    }
    return true;
  }
}
