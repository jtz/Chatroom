package edu.bu.met.cs665.db;

import static edu.bu.met.cs665.db.CreateDB.url;

import edu.bu.met.cs665.model.ChatRoom;
import edu.bu.met.cs665.model.ChatRoomFactory;
import edu.bu.met.cs665.model.User;
import edu.bu.met.cs665.model.UserBuilder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * Chatroom database. Use SQLite.
 *
 * @author Jingtian
 */
public class ChatRoomDB {

  private static Logger logger = Logger.getLogger(ChatRoomDB.class);
  private static ChatRoomDB instance = new ChatRoomDB();

  private ChatRoomDB() {}

  /**
   * Singleton pattern to get the only one CharRoomDB instance.
   *
   * @return ChatRoomDB Instance
   */
  public static ChatRoomDB getInstance() {
    return instance;
  }

  /**
   * Add chatroom to database.
   *
   * @param chatRoom ChatRoom object
   * @return ChatRoom object
   * @throws Exception SQLException
   */
  public ChatRoom addChatRoom(ChatRoom chatRoom) throws Exception {

    // check if chatroom already exist
    if (checkChatRoom(chatRoom.getName())) {
      logger.trace("ChatRoom: " + chatRoom.getName() + " exists, ignore create.");
      return chatRoom;
    }

    String sql = "INSERT INTO chatroom( name ) VALUES(?)";

    try (Connection conn = DriverManager.getConnection(url);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, chatRoom.getName());
      pstmt.executeUpdate();

      logger.trace("Successfully add ChatRoom: " + chatRoom.getName() + " into DB.");

      return chatRoom;
    } catch (SQLException e) {
      logger.error(e);
      throw e;
    }
  }

  /**
   * Check if chatroom already exist in database.
   *
   * @param name chatroom name
   * @return true if exist, false if not
   * @throws Exception SQLException
   */
  public boolean checkChatRoom(String name) throws Exception {

    String sql = "SELECT name FROM chatroom WHERE name = ?";
    ResultSet rs = null;
    boolean value = false;
    try (Connection conn = DriverManager.getConnection(url);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, name);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        if (rs.getString("name") != null) {
          value = true;
        }
      }
    } catch (SQLException e) {
      logger.error(e);
      throw e;
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
    return value;
  }

  /**
   * Get a list of chatrooms in database.
   *
   * @return a list of chatrooms
   * @throws Exception Exception
   */
  public List<ChatRoom> listChatRoom() throws Exception {

    String sql = "SELECT name FROM chatroom";

    List<ChatRoom> chatRoomList = new ArrayList<ChatRoom>();
    ResultSet rs = null;
    try (Connection conn = DriverManager.getConnection(url);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      rs = pstmt.executeQuery();
      while (rs.next()) {
        String chatRoomName = rs.getString("name");
        ChatRoomFactory chatRoomFactory = ChatRoomFactory.getInstance();
        chatRoomList.add(chatRoomFactory.createChatRoom(chatRoomName));
      }
    } catch (Exception e) {
      logger.error(e);
      throw e;
    } finally {
      if (rs != null) {
        rs.close();
      }
    }

    logger.trace("Success get " + chatRoomList.size() + " ChatRooms.");
    return chatRoomList;
  }

  /**
   * Add user to database.
   *
   * @param user User object
   * @return the added user
   * @throws Exception SQLException
   */
  public User addUser(User user) throws Exception {
    if (checkUser(user.getName())) {
      logger.trace("User: " + user.getName() + " exist, ignore create.");
      return user;
    }

    String sql = "INSERT INTO user( name,sex,country ) VALUES(?,?,?)";

    try (Connection conn = DriverManager.getConnection(url);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, user.getName());
      pstmt.setString(2, user.getSex());
      pstmt.setString(3, user.getCountry());
      pstmt.executeUpdate();

      logger.trace("Success add User: " + user.getName() + " into DB.");

      return user;
    } catch (SQLException e) {
      logger.error(e);
      throw e;
    }
  }

  /**
   * Check if user already exist in database.
   *
   * @param name user name
   * @return true if exist, false if not
   * @throws Exception Exception
   */
  public boolean checkUser(String name) throws Exception {
    String sql = "SELECT name FROM user WHERE name = ?";

    ResultSet rs = null;
    try (Connection conn = DriverManager.getConnection(url);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, name);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        if (rs.getString("name") != null) {
          return true;
        }
      }
    } catch (Exception e) {
      logger.error(e);
      throw e;
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
    return false;
  }

  /**
   * Get a list of users in database.
   *
   * @return a list of users
   * @throws Exception Exception
   */
  public List<User> listUser() throws Exception {
    String sql = "SELECT name,sex,country,chatroom FROM user";

    List<User> userList = new ArrayList<User>();
    ResultSet rs = null;
    try (Connection conn = DriverManager.getConnection(url);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      rs = pstmt.executeQuery();
      while (rs.next()) {
        User user =
            new UserBuilder(rs.getString("name"))
                .setSex(rs.getString("sex"))
                .setCountry(rs.getString("country"))
                .build();
        userList.add(user);
      }
    } catch (Exception e) {
      logger.error(e);
      throw e;
    } finally {
      if (rs != null) {
        rs.close();
      }
    }

    logger.trace("Success get " + userList.size() + " Users.");
    return userList;
  }

  /**
   * Get a list of users in specified chatroom.
   *
   * @param chatRoom chatroom name
   * @return a list of users
   * @throws Exception Exception
   */
  public Set<User> listUserbyChatRoom(ChatRoom chatRoom) throws Exception {

    String sql = "SELECT name,sex,country,chatroom FROM user WHERE chatroom = ?";

    Set<User> userList = new HashSet<User>();
    ResultSet rs = null;
    try (Connection conn = DriverManager.getConnection(url);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, chatRoom.getName());
      rs = pstmt.executeQuery();

      while (rs.next()) {
        User user =
            new UserBuilder(rs.getString("name"))
                .setSex(rs.getString("sex"))
                .setCountry(rs.getString("country"))
                .build();
        userList.add(user);
      }
    } catch (Exception e) {
      logger.error(e);
      throw e;
    } finally {
      if (rs != null) {
        rs.close();
      }
    }

    logger.trace(
        "Successfully get " + userList.size() + " Users of ChatRoom: " + chatRoom.getName());
    return userList;
  }

  /**
   * Update users in specified chatroom.
   *
   * @param user User object
   * @param chatRoom ChatRoom object
   * @throws SQLException SQLException
   */
  public void updateUserChatRoom(User user, ChatRoom chatRoom) throws SQLException {

    String sql = "UPDATE user SET chatroom = ? WHERE name = ?";
    String chatRoomName = null;
    if (chatRoom != null) {
      chatRoomName = chatRoom.getName();
    }
    try (Connection conn = DriverManager.getConnection(url);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, chatRoomName);
      pstmt.setString(2, user.getName());
      pstmt.executeUpdate();

      logger.trace("Success update User: " + user.getName() + " chatroom: " + chatRoomName);
    } catch (SQLException e) {
      logger.error(e);
      throw e;
    }
  }

  /**
   * Get chatroom of specified user.
   *
   * @param name user name
   * @return chatroom name string
   * @throws Exception Exception
   */
  public String getUserChatRoom(String name) throws Exception {
    String sql = "SELECT chatroom FROM user WHERE name = ?";

    ResultSet rs = null;
    try (Connection conn = DriverManager.getConnection(url);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, name);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        return rs.getString("chatroom");
      }
    } catch (Exception e) {
      logger.error(e);
      throw e;
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
    return null;
  }
}
