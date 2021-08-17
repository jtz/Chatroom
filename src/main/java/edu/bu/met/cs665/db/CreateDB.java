package edu.bu.met.cs665.db;

import edu.bu.met.cs665.model.ChatRoom;
import edu.bu.met.cs665.model.ChatRoomFactory;
import edu.bu.met.cs665.model.User;
import edu.bu.met.cs665.model.UserBuilder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import org.apache.log4j.Logger;

/**
 * Create database for JTChatroom.
 *
 * @author Jingtian
 */
public class CreateDB {

  public static final String url = "jdbc:sqlite:C:/lib/chatroom.db";

  private static Logger logger = Logger.getLogger(CreateDB.class);

  public static void main(String[] args) throws Exception {
    createTable();
  }

  /**
   * Create chatroom table and user table in database.
   *
   * @throws Exception Exception
   */
  public static void createTable() throws Exception {
    try (Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement()) {

      // create chatroom table, name is key
      String sql = "CREATE TABLE IF NOT EXISTS chatroom ( name text PRIMARY KEY );";

      stmt.execute(sql);
      logger.info("Successfully create table ChatRoom.");

      // create user table, name is key
      sql =
          "CREATE TABLE IF NOT EXISTS user ( name text PRIMARY KEY, "
              + " sex text, country text, chatroom text );";

      stmt.execute(sql);
      logger.info("Successfully create table User.");

      ChatRoomDB chatRoomDB = ChatRoomDB.getInstance();

      ChatRoomFactory chatRoomFactory = ChatRoomFactory.getInstance();

      // Initially create 3 chatrooms
      ChatRoom chatRoomA = chatRoomFactory.createChatRoom("ChatRoomA");
      ChatRoom chatRoomB = chatRoomFactory.createChatRoom("ChatRoomB");
      ChatRoom chatRoomC = chatRoomFactory.createChatRoom("ChatRoomC");

      chatRoomDB.addChatRoom(chatRoomA);
      chatRoomDB.addChatRoom(chatRoomB);
      chatRoomDB.addChatRoom(chatRoomC);

      // Initially create 5 users
      User userA = new UserBuilder("UserA").setCountry("CountryA").setSex("Male").build();
      User userB = new UserBuilder("UserB").setCountry("CountryB").setSex("Female").build();
      User userC = new UserBuilder("UserC").setCountry("CountryC").setSex("Female").build();
      User userD = new UserBuilder("UserD").setCountry("CountryD").setSex("Male").build();
      User userE = new UserBuilder("UserE").setCountry("CountryE").setSex("Female").build();

      chatRoomDB.addUser(userA);
      chatRoomDB.addUser(userB);
      chatRoomDB.addUser(userC);
      chatRoomDB.addUser(userD);
      chatRoomDB.addUser(userE);

      // attach users to chatrooms
      chatRoomA.attach(userA);
      chatRoomA.attach(userB);
      chatRoomC.attach(userC);
      chatRoomC.attach(userD);

    } catch (Exception e) {
      logger.error(e);
      throw e;
    }
  }
}
