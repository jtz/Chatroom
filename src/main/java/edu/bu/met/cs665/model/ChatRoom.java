package edu.bu.met.cs665.model;

import edu.bu.met.cs665.db.ChatRoomDB;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;

public class ChatRoom {

  private static Logger logger = Logger.getLogger(ChatRoom.class);

  private String name;
  private Set<User> users = new HashSet<User>();

  protected ChatRoom(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  /** Initialize chatroom by getting user lists. */
  public void initChatRoom() {

    ChatRoomDB chatRoomDB = ChatRoomDB.getInstance();
    try {
      users = chatRoomDB.listUserbyChatRoom(this);
    } catch (Exception e) {
      logger.error("Get exception when list users in ChatRoom", e);
    }
  }

  /**
   * Attach user to chatroom.
   *
   * @param user User object
   */
  public void attach(User user) {

    if (!users.contains(user)) {
      users.add(user);
    }
    ChatRoomDB chatRoomDB = ChatRoomDB.getInstance();
    try {
      chatRoomDB.updateUserChatRoom(user, this);
    } catch (SQLException e) {
      logger.error("Get exception when update user's chatRoom", e);
    }
  }

  /**
   * Detach user from chatroom.
   *
   * @param user User object
   */
  public void detach(User user) {

    if (users.contains(user)) {
      users.remove(user);
    }
    ChatRoomDB chatRoomDB = ChatRoomDB.getInstance();
    try {
      chatRoomDB.updateUserChatRoom(user, null);
    } catch (SQLException e) {
      logger.error("Get exception when update user's chatRoom", e);
    }
  }

  /**
   * Send message to all users in current chatroom.
   *
   * @param user User object
   * @param message message that will be sent
   */
  public void sendMessage(User user, String message) {

    if (!users.contains(user)) {
      logger.error("User: " + user.getName() + " get incorrect ChatRoom: " + this.getName());
      return;
    }

    StringBuilder sb = new StringBuilder();
    sb.append(user.getName()).append("|");
    if (user.getSex() != null) {
      sb.append(user.getSex());
    } else {
      sb.append("unknown");
    }
    sb.append("|");
    if (user.getCountry() != null) {
      sb.append(user.getCountry());
    } else {
      sb.append("unknown");
    }
    System.out.println();
    logger.info("[" + sb.toString() + "] send message: " + message);
    notifyAllUsers(user, message);
    System.out.println();
  }

  /**
   * Notify all users.
   *
   * @param user User object
   * @param message message that will be sent
   */
  public void notifyAllUsers(User user, String message) {
    for (User u : users) {
      if (!u.equals(user)) {
        u.receiveMessage(message);
      }
    }
  }

  /** The number of users in current chatroom. */
  public int userSize() {
    return users.size();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ChatRoom other = (ChatRoom) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Chatroom [name=" + name + "]";
  }
}
